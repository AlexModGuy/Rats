package com.github.alexthe666.rats.server.entity.rat;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatsDataSerializerRegistry;
import com.github.alexthe666.rats.server.inventory.RatMenu;
import com.github.alexthe666.rats.server.inventory.container.RatContainer;
import com.github.alexthe666.rats.server.items.RatStaffItem;
import com.github.alexthe666.rats.server.message.OpenRatScreenPacket;
import com.github.alexthe666.rats.server.message.RatsNetworkHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class InventoryRat extends DiggingRat implements ContainerListener {

	private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(InventoryRat.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Optional<GlobalPos>> RADIUS_CENTER = SynchedEntityData.defineId(InventoryRat.class, EntityDataSerializers.OPTIONAL_GLOBAL_POS);
	private static final EntityDataAccessor<Optional<GlobalPos>> HOME_POS = SynchedEntityData.defineId(InventoryRat.class, EntityDataSerializers.OPTIONAL_GLOBAL_POS);
	private static final EntityDataAccessor<Integer> SEARCH_RADIUS = SynchedEntityData.defineId(InventoryRat.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<List<GlobalPos>> PATROL_NODES = SynchedEntityData.defineId(InventoryRat.class, RatsDataSerializerRegistry.GLOBAL_POS_LIST.get());

	private RatContainer inventory;
	private LazyOptional<?> itemHandler = null;
	private boolean inventoryOpen;

	protected InventoryRat(EntityType<? extends TamableAnimal> type, Level level) {
		super(type, level);
		this.createInventory();
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(COMMAND, 0);
		this.getEntityData().define(RADIUS_CENTER, Optional.empty());
		this.getEntityData().define(HOME_POS, Optional.empty());
		this.getEntityData().define(SEARCH_RADIUS, RatConfig.defaultRatRadius);
		this.getEntityData().define(PATROL_NODES, new ArrayList<>());
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		ListTag listtag = new ListTag();
		for (int i = 0; i < this.getInventory().getContainerSize(); ++i) {
			ItemStack itemstack = this.getInventory().getItem(i);
			if (!itemstack.isEmpty()) {
				CompoundTag compoundtag = new CompoundTag();
				compoundtag.putByte("Slot", (byte) i);
				itemstack.save(compoundtag);
				listtag.add(compoundtag);
			}
		}
		tag.put("Items", listtag);

		this.getHomePoint().flatMap(pos -> GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, pos).resultOrPartial(RatsMod.LOGGER::error)).ifPresent(tag1 -> tag.put("HomePos", tag1));

		this.getRadiusCenter().flatMap(pos -> GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, pos).resultOrPartial(RatsMod.LOGGER::error)).ifPresent(tag1 -> tag.put("RadiusPos", tag1));
		tag.putInt("SearchRadius", this.getRadius());

		if (!this.getPatrolNodes().isEmpty()) {
			ListTag listTag = new ListTag();
			this.getPatrolNodes().forEach(pos -> GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, pos).resultOrPartial(RatsMod.LOGGER::error).ifPresent(listTag::add));
			tag.put("PatrolNodesTag", listTag);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		ListTag listtag = tag.getList("Items", 10);

		for (int i = 0; i < listtag.size(); ++i) {
			CompoundTag compoundtag = listtag.getCompound(i);
			int j = compoundtag.getByte("Slot") & 255;
			if (j < this.getInventory().getContainerSize()) {
				this.getInventory().setItem(j, ItemStack.of(compoundtag));
			}
		}
		if (tag.contains("HomePos")) {
			this.setHomePoint(GlobalPos.CODEC.parse(NbtOps.INSTANCE, tag.get("HomePos")).resultOrPartial(RatsMod.LOGGER::error).orElse(null));
		}
		if (tag.contains("RadiusPos")) {
			this.setRadiusCenter(GlobalPos.CODEC.parse(NbtOps.INSTANCE, tag.get("RadiusPos")).resultOrPartial(RatsMod.LOGGER::error).orElse(null));
		}
		this.setRadius(tag.getInt("SearchRadius"));

		if (tag.contains("PatrolNodesTag")) {
			ListTag listTag = tag.getList("PatrolNodesTag", Tag.TAG_COMPOUND);

			for (Tag pos : listTag) {
				GlobalPos.CODEC.parse(NbtOps.INSTANCE, pos).resultOrPartial(RatsMod.LOGGER::error).ifPresent(globalPos -> this.getPatrolNodes().add(globalPos));
			}
		}
	}

	private void createInventory() {
		SimpleContainer simplecontainer = this.getInventory();
		this.inventory = new RatContainer(this, 6);
		if (simplecontainer != null) {
			simplecontainer.removeListener(this);
			int i = Math.min(simplecontainer.getContainerSize(), this.getInventory().getContainerSize());

			for (int j = 0; j < i; ++j) {
				ItemStack itemstack = simplecontainer.getItem(j);
				if (!itemstack.isEmpty()) {
					this.getInventory().setItem(j, itemstack.copy());
				}
			}
		}

		this.getInventory().addListener(this);
		this.itemHandler = LazyOptional.of(() -> new InvWrapper(this.getInventory()));
	}

	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
		if (this.isAlive() && capability == ForgeCapabilities.ITEM_HANDLER && this.itemHandler != null)
			return this.itemHandler.cast();
		return super.getCapability(capability, facing);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		if (this.itemHandler != null) {
			LazyOptional<?> oldHandler = this.itemHandler;
			this.itemHandler = null;
			oldHandler.invalidate();
		}
	}

	@Override
	public boolean canMove() {
		return super.canMove() && this.getCommand().freeMove && !this.inventoryOpen;
	}

	@Override
	public ItemStack getItemBySlot(EquipmentSlot slot) {
		if (this.getInventory() != null) {
			if (slot == EquipmentSlot.MAINHAND) {
				return this.getInventory().getItem(0);
			} else if (slot == EquipmentSlot.HEAD) {
				return this.getInventory().getItem(1);
			} else if (slot == EquipmentSlot.OFFHAND) {
				return this.getInventory().getItem(2);
			} else if (slot == EquipmentSlot.CHEST) {
				return this.getInventory().getItem(3);
			} else if (slot == EquipmentSlot.LEGS) {
				return this.getInventory().getItem(4);
			} else {
				return slot == EquipmentSlot.FEET ? this.getInventory().getItem(5) : super.getItemBySlot(slot);
			}
		}
		return super.getItemBySlot(slot);
	}

	@Override
	public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
		if (this.getInventory() != null) {
			if (slot == EquipmentSlot.MAINHAND) {
				this.getInventory().setItem(0, stack);
			} else if (slot == EquipmentSlot.HEAD) {
				this.getInventory().setItem(1, stack);
			} else if (slot == EquipmentSlot.OFFHAND) {
				this.getInventory().setItem(2, stack);
			} else if (slot == EquipmentSlot.CHEST) {
				this.getInventory().setItem(3, stack);
			} else if (slot == EquipmentSlot.LEGS) {
				this.getInventory().setItem(4, stack);
			} else if (slot == EquipmentSlot.FEET) {
				this.getInventory().setItem(5, stack);
			} else {
				super.setItemSlot(slot, stack);
			}
		} else {
			super.setItemSlot(slot, stack);
		}
	}

	public void openGUI(Player player) {
		if (!this.level().isClientSide()) {
			ServerPlayer sp = (ServerPlayer) player;
			if (sp.containerMenu != sp.inventoryMenu) {
				sp.closeContainer();
			}

			sp.nextContainerCounter();
			RatsNetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp), new OpenRatScreenPacket(sp.containerCounter, this.getId()));
			sp.containerMenu = new RatMenu(sp.containerCounter, this.getInventory(), sp.getInventory());
			sp.initMenu(sp.containerMenu);
			this.inventoryOpen = true;
			MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(sp, sp.containerMenu));
		}
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		InteractionHand otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
		if (!player.isShiftKeyDown() && this.isOwnedBy(player) && !(stack.getItem() instanceof RatStaffItem) && !(player.getItemInHand(otherHand).getItem() instanceof RatStaffItem)) {
			this.openGUI(player);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Nullable
	public SimpleContainer getInventory() {
		return this.inventory;
	}

	public void closeInventory() {
		this.inventoryOpen = false;
	}

	//command logic

	public int getCommandInteger() {
		return this.getEntityData().get(COMMAND);
	}

	public void setCommandInteger(int command) {
		if (!this.level().isClientSide() && command != this.getCommandInteger()) {
			this.getNavigation().stop();
			this.goalSelector.getRunningGoals().forEach(WrappedGoal::stop);
			if (this instanceof TamedRat rat) rat.crafting = false;
		}
		this.getEntityData().set(COMMAND, command);
		this.setOrderedToSit(command == RatCommand.SIT.ordinal());
	}

	public RatCommand getCommand() {
		return RatCommand.values()[Mth.clamp(this.getCommandInteger(), 0, RatCommand.values().length - 1)];
	}

	public void setCommand(RatCommand command) {
		this.setCommandInteger(command.ordinal());
	}

	public boolean isFollowing() {
		return this.getCommandInteger() == 2;
	}

	public boolean isTargetCommand() {
		return this.getCommandInteger() == 4 || this.getCommandInteger() == 5;
	}

	public boolean shouldHunt() {
		return this.getCommandInteger() == 3 || this.isPatrolCommand();
	}

	public boolean shouldWander() {
		return this.getCommand().allowsWandering;
	}

	public boolean isAttackCommand() {
		return this.getCommand().allowsAttacking;
	}

	public boolean isPatrolCommand() {
		return this.getCommandInteger() == 8;
	}

	public Optional<GlobalPos> getHomePoint() {
		return this.getEntityData().get(HOME_POS);
	}

	public void setHomePoint(@Nullable GlobalPos home) {
		this.getEntityData().set(HOME_POS, Optional.ofNullable(home));
	}

	public int getRadius() {
		return this.getRadiusCenter().isPresent() ? this.getEntityData().get(SEARCH_RADIUS) : RatConfig.defaultRatRadius;
	}

	public void setRadius(int radius) {
		this.getEntityData().set(SEARCH_RADIUS, radius);
	}

	public Optional<GlobalPos> getRadiusCenter() {
		return this.getEntityData().get(RADIUS_CENTER);
	}

	public void setRadiusCenter(@Nullable GlobalPos pos) {
		this.getEntityData().set(RADIUS_CENTER, Optional.ofNullable(pos));
	}

	public BlockPos getSearchCenter() {
		if (this.getRadiusCenter().isEmpty()) {
			return this.blockPosition();
		} else {
			return this.getRadiusCenter().get().pos();
		}
	}

	public List<GlobalPos> getPatrolNodes() {
		return this.getEntityData().get(PATROL_NODES);
	}

	@Override
	public void containerChanged(Container container) {

	}
}
