package com.github.alexthe666.rats.server.block.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.registry.RatsBlockEntityRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.registry.RatsParticleRegistry;
import com.github.alexthe666.rats.server.inventory.UpgradeCombinerMenu;
import com.github.alexthe666.rats.server.items.upgrades.BaseRatUpgradeItem;
import com.github.alexthe666.rats.server.items.upgrades.CombinedRatUpgradeItem;
import com.github.alexthe666.rats.server.misc.RatsLangConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.Nullable;

public class UpgradeCombinerBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, MenuProvider {

	private static final int[] SLOTS_TOP = new int[]{0, 2};
	private static final int[] SLOTS_SIDE = new int[]{1};
	private static final int[] SLOTS_BOTTOM = new int[]{3};
	public int tickCount;
	public float ratRotation;
	public float ratRotationPrev;
	public float tRot;
	public int burnTime;
	public int burnDuration;
	public int cookTime;
	public int totalCookTime;
	private final IItemHandler handlerUp = new SidedInvWrapper(this, Direction.UP);
	private final IItemHandler handlerDown = new SidedInvWrapper(this, Direction.DOWN);
	private final IItemHandler handlerNorth = new SidedInvWrapper(this, Direction.NORTH);
	private NonNullList<ItemStack> combinerStacks = NonNullList.withSize(4, ItemStack.EMPTY);
	public final ContainerData data = new ContainerData() {
		@Override
		public int get(int index) {
			return switch (index) {
				case 0 -> UpgradeCombinerBlockEntity.this.cookTime;
				case 1 -> UpgradeCombinerBlockEntity.this.totalCookTime;
				case 2 -> UpgradeCombinerBlockEntity.this.burnTime;
				case 3 -> UpgradeCombinerBlockEntity.this.burnDuration;
				default -> 0;
			};
		}

		@Override
		public void set(int index, int value) {
			switch (index) {
				case 0 -> UpgradeCombinerBlockEntity.this.cookTime = value;
				case 1 -> UpgradeCombinerBlockEntity.this.totalCookTime = value;
				case 2 -> UpgradeCombinerBlockEntity.this.burnTime = value;
				case 3 -> UpgradeCombinerBlockEntity.this.burnDuration = value;
			}

		}

		@Override
		public int getCount() {
			return 4;
		}
	};

	public UpgradeCombinerBlockEntity(BlockPos pos, BlockState state) {
		super(RatsBlockEntityRegistry.UPGRADE_COMBINER.get(), pos, state);
	}

	public static int getItemBurnTime(ItemStack stack) {
		return stack.getItem() == getFuel() ? 150 : 0;
	}

	public static Item getFuel() {
		if (RatsMod.RATLANTIS_DATAPACK_ENABLED) {
			return RatlantisItemRegistry.GEM_OF_RATLANTIS.get();
		} else {
			return Items.EMERALD;
		}
	}

	// Note: In 1.21, getRenderBoundingBox is no longer part of BlockEntity interface.
	// Rendering bounds are handled differently now. Keeping method for custom use.
	public AABB getRenderBoundingBox() {
		BlockPos pos = this.getBlockPos();
		return new AABB(Vec3.atLowerCornerOf(pos), Vec3.atLowerCornerOf(pos.offset(1, 2, 1)));
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.combinerStacks;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> items) {
		this.combinerStacks = items;
	}

	@Override
	public int getContainerSize() {
		return this.combinerStacks.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.combinerStacks) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getItem(int index) {
		return this.combinerStacks.get(index);
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		return ContainerHelper.removeItem(this.combinerStacks, index, count);
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		return ContainerHelper.takeItem(this.combinerStacks, index);
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		ItemStack itemstack = this.combinerStacks.get(index);
		boolean flag = !stack.isEmpty() && ItemStack.isSameItemSameComponents(stack, itemstack);
		this.combinerStacks.set(index, stack);

		if (stack.getCount() > this.getMaxStackSize()) {
			stack.setCount(this.getMaxStackSize());
		}

		if (index == 0 && !flag) {
			this.totalCookTime = 300;
			this.cookTime = 0;
			this.setChanged();
		}
	}

	@Override
	public void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
		super.loadAdditional(compound, provider);
		this.combinerStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(compound, this.combinerStacks, provider);
		this.burnTime = compound.getInt("BurnTime");
		this.cookTime = compound.getInt("CookTime");
		this.totalCookTime = compound.getInt("CookTimeTotal");
		this.burnDuration = getItemBurnTime(this.combinerStacks.get(1));
	}

	@Override
	public void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
		super.saveAdditional(compound, provider);
		compound.putInt("BurnTime", (short) this.burnTime);
		compound.putInt("CookTime", (short) this.cookTime);
		compound.putInt("CookTimeTotal", (short) this.totalCookTime);
		ContainerHelper.saveAllItems(compound, this.combinerStacks, provider);
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	public boolean isBurning() {
		return this.burnTime > 0;
	}

	public static void tick(Level level, BlockPos pos, BlockState state, UpgradeCombinerBlockEntity te) {
		te.totalCookTime = 300;
		te.ratRotationPrev = te.ratRotation;
		te.tickCount++;
		Player player = level.getNearestPlayer((float) pos.getX() + 0.5F, (float) pos.getY() + 0.5F, (float) pos.getZ() + 0.5F, 10.0D, false);
		if (player != null) {
			double d0 = player.getX() - (double) ((float) pos.getX() + 0.5F);
			double d1 = player.getZ() - (double) ((float) pos.getZ() + 0.5F);
			te.tRot = (float) Mth.atan2(d1, d0);
		} else {
			te.tRot += 0.04F;
		}

		while (te.ratRotation >= (float) Math.PI) {
			te.ratRotation -= ((float) Math.PI * 2F);
		}

		while (te.ratRotation < -(float) Math.PI) {
			te.ratRotation += ((float) Math.PI * 2F);
		}
		float f2;

		for (f2 = te.tRot - te.ratRotation; f2 >= (float) Math.PI; f2 -= ((float) Math.PI * 2F)) {
		}

		while (f2 < -(float) Math.PI) {
			f2 += ((float) Math.PI * 2F);
		}

		te.ratRotation += f2 * 0.4F;


		if (te.isBurning()) {
			--te.burnTime;
		}

		if (level.isClientSide()) {
			float radius = (float) Math.sin(te.tickCount * 0.1D);
			double extraY = pos.getY() + 1.05;
			for (int i = 0; i < 3; i++) {
				float angle = (0.01745329251F * (te.tickCount * 4 + i * 120));
				double extraX = (double) (radius * Mth.sin((float) (Math.PI + angle))) + pos.getX() + 0.5D;
				double extraZ = (double) (radius * Mth.cos(angle)) + pos.getZ() + 0.5D;
				level.addParticle(RatsParticleRegistry.UPGRADE_COMBINER.get(), extraX, extraY, extraZ, 0F, 0F, 0F);
			}
		}

		ItemStack fuel = te.combinerStacks.get(1);
		if (te.isBurning() || !fuel.isEmpty() && !te.combinerStacks.get(0).isEmpty() && !te.combinerStacks.get(2).isEmpty()) {
			if (!te.isBurning() && te.canSmelt()) {
				te.burnTime = getItemBurnTime(fuel);
				te.burnDuration = te.burnTime;

				if (te.isBurning()) {

					if (!fuel.isEmpty()) {
						Item item = fuel.getItem();
						fuel.shrink(1);

						if (fuel.isEmpty()) {
							ItemStack item1 = item.getCraftingRemainingItem(fuel);
							te.combinerStacks.set(1, item1);
						}
					}
				}
			}

			if (te.isBurning() && te.canSmelt()) {
				++te.cookTime;

				if (te.cookTime == te.totalCookTime) {
					te.cookTime = 0;
					te.totalCookTime = 300;
					te.smeltItem();
				}
			} else {
				te.cookTime = 0;
			}
		} else if (!te.isBurning() && te.cookTime > 0) {
			te.cookTime = Mth.clamp(te.cookTime - 2, 0, te.totalCookTime);
		}
	}

	public void smeltItem() {
		if (this.canSmelt()) {
			ItemStack itemstack = this.combinerStacks.get(0);
			ItemStack itemstack2 = this.combinerStacks.get(3);
			ItemStack itemstack1 = this.getCombinerResult(itemstack, this.combinerStacks.get(2));

			if (itemstack2.isEmpty()) {
				this.combinerStacks.set(3, itemstack1.copy());
			} else if (itemstack2.is(itemstack1.getItem())) {
				itemstack2.grow(itemstack1.getCount());
			}
			itemstack.shrink(1);
			this.combinerStacks.get(2).shrink(1);
		}
	}

	private ItemStack getCombinerResult(ItemStack combiner, ItemStack stack) {
		ItemStack result = combiner.copy();
		CompoundTag tag = result.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
		if (tag.contains("Items") && this.level != null) {
			ContainerHelper.loadAllItems(tag, nonnulllist, this.level.registryAccess());
		}
		int addIndex = -1;
		for (int i = 0; i < nonnulllist.size(); i++) {
			if (nonnulllist.get(i).isEmpty()) {
				addIndex = i;
				break;
			}
		}
		if (addIndex == -1) {
			return result;
		}
		nonnulllist.set(addIndex, stack.copy());
		if (this.level != null) {
			ContainerHelper.saveAllItems(tag, nonnulllist, this.level.registryAccess());
		}
		CustomData.set(DataComponents.CUSTOM_DATA, result, tag);
		return result;
	}

	public boolean canSmelt() {
		if (!this.combinerStacks.get(0).isEmpty() && this.combinerStacks.get(0).is(RatsItemRegistry.RAT_UPGRADE_COMBINED.get())) {
			return this.level != null && CombinedRatUpgradeItem.canCombineWithUpgrade(this.combinerStacks.get(0), this.combinerStacks.get(2), this.level.registryAccess());
		}
		return false;
	}

	public static boolean canCombine(ItemStack combinerSlot, ItemStack toBeCombinedSlot) {
		if (!combinerSlot.isEmpty() && combinerSlot.is(RatsItemRegistry.RAT_UPGRADE_COMBINED.get())) {
			// Note: Using simplified version without conflict check since we don't have registries here
			return CombinedRatUpgradeItem.canCombineWithUpgrade(combinerSlot, toBeCombinedSlot);
		}
		return false;
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack stack) {
		if (index == 3) {
			return false;
		} else if (index == 2) {
			return stack.getItem() instanceof BaseRatUpgradeItem && !(stack.getItem() instanceof CombinedRatUpgradeItem);
		} else if (index == 1) {
			return stack.getItem() == getFuel();
		} else {
			return stack.getItem() instanceof CombinedRatUpgradeItem;
		}
	}

	@Override
	public void clearContent() {
		this.combinerStacks.clear();
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		if (side == Direction.DOWN) {
			return SLOTS_BOTTOM;
		} else if (side == Direction.UP) {
			return SLOTS_TOP;
		} else {
			return SLOTS_SIDE;
		}
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
		return this.canPlaceItem(index, stack);
	}

	@Override
	public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
		return direction == Direction.DOWN && index == 1;
	}

	public IItemHandler getItemHandler(Direction direction) {
		return switch (direction) {
			case UP -> handlerUp;
			case DOWN -> handlerDown;
			default -> handlerNorth;
		};
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable(RatsLangConstants.UPGRADE_COMBINER);
	}

	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory player) {
		return new UpgradeCombinerMenu(id, this, player, this.data);
	}
}







