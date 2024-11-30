package com.github.alexthe666.rats.server.block.entity;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.data.tags.RatsItemTags;
import com.github.alexthe666.rats.registry.RatsBlockEntityRegistry;
import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.registry.RatsParticleRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.inventory.AutoCurdlerMenu;
import com.github.alexthe666.rats.server.message.RatsNetworkHandler;
import com.github.alexthe666.rats.server.message.UpdateCurdlerFluidPacket;
import com.github.alexthe666.rats.server.misc.RatsLangConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Optional;

public class AutoCurdlerBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, MenuProvider {
	private static final int[] SLOTS_TOP = new int[]{0};
	private static final int[] SLOTS_BOTTOM = new int[]{1};
	private final FluidTank tank = new FluidTank(FluidType.BUCKET_VOLUME * 5, fluidStack -> isMilkFluid(fluidStack));
	final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN);
	private final LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> this.tank);
	private NonNullList<ItemStack> curdlerStacks = NonNullList.withSize(2, ItemStack.EMPTY);
	public int cookTime;
	public int totalCookTime;
	public final ContainerData data = new ContainerData() {
		public int get(int index) {
			return switch (index) {
				case 0 -> AutoCurdlerBlockEntity.this.cookTime;
				case 1 -> AutoCurdlerBlockEntity.this.totalCookTime;
				case 2 -> AutoCurdlerBlockEntity.this.tank.getFluidAmount();
				case 3 -> AutoCurdlerBlockEntity.this.tank.getCapacity();
				default -> 0;
			};
		}

		public void set(int index, int value) {
			switch (index) {
				case 0 -> AutoCurdlerBlockEntity.this.cookTime = value;
				case 1 -> AutoCurdlerBlockEntity.this.totalCookTime = value;
			}

		}

		public int getCount() {
			return 4;
		}
	};

	public AutoCurdlerBlockEntity(BlockPos pos, BlockState state) {
		super(RatsBlockEntityRegistry.AUTO_CURDLER.get(), pos, state);
	}

	public static boolean isMilk(ItemStack stack) {
		if (stack.is(RatsItemTags.MILK_BUCKETS)) {
			return true;
		}
		Optional<FluidStack> fluidStack = FluidUtil.getFluidContained(stack);
		return fluidStack.isPresent() && isMilkFluid(fluidStack.get());
	}

	@Override
	public int getContainerSize() {
		return this.curdlerStacks.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.curdlerStacks) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getItem(int index) {
		return this.curdlerStacks.get(index);
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		return ContainerHelper.removeItem(this.curdlerStacks, index, count);
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		return ContainerHelper.takeItem(this.curdlerStacks, index);
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		this.curdlerStacks.set(index, stack);

		if (stack.getCount() > this.getMaxStackSize()) {
			stack.setCount(this.getMaxStackSize());
		}
		this.setChanged();
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		this.tank.readFromNBT(compound);
		this.curdlerStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(compound, this.curdlerStacks);
		this.cookTime = compound.getInt("CookTime");
		this.totalCookTime = compound.getInt("CookTimeTotal");
	}

	@Override
	public void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		this.tank.writeToNBT(compound);
		compound.putInt("CookTime", (short) this.cookTime);
		compound.putInt("CookTimeTotal", (short) this.totalCookTime);
		ContainerHelper.saveAllItems(compound, this.curdlerStacks);
	}

	@Override
	public boolean stillValid(Player player) {
		if (this.getLevel() != null) {
			if (this.getLevel().getBlockEntity(this.getBlockPos()) != this) {
				return false;
			} else {
				return !(player.distanceToSqr((double) this.getBlockPos().getX() + 0.5D, (double) this.getBlockPos().getY() + 0.5D, (double) this.getBlockPos().getZ() + 0.5D) > 64.0D);
			}
		}
		return true;
	}

	public boolean hasEnoughMilk() {
		return this.tank.getFluidAmount() >= FluidType.BUCKET_VOLUME && isMilkFluid(this.tank.getFluid());
	}

	public static void tick(Level level, BlockPos pos, BlockState state, AutoCurdlerBlockEntity te) {
		if (!level.isClientSide()) {
			te.totalCookTime = RatConfig.milkCauldronTime;
			if (te.hasEnoughMilk()) {
				if (te.canMakeCheese()) {
					++te.cookTime;

					if (te.cookTime == te.totalCookTime) {
						te.cookTime = 0;
						te.totalCookTime = RatConfig.milkCauldronTime;
						te.makeCheese(level);
					}
				} else {
					te.cookTime = 0;
				}
			} else if (!te.hasEnoughMilk() && te.cookTime > 0) {
				te.cookTime = Mth.clamp(te.cookTime - 2, 0, te.totalCookTime);
			}
			if (isMilk(te.getItem(0))) {
				Optional<FluidStack> fluidStackOptional = FluidUtil.getFluidContained(te.getItem(0));
				fluidStackOptional.ifPresent(fluidStack -> {
					LazyOptional<IFluidHandlerItem> fluidHandlerOptional = FluidUtil.getFluidHandler(te.getItem(0));
					fluidHandlerOptional.ifPresent(fluidHandler -> {
						if (fluidHandler.drain(te.tank.getCapacity() - te.tank.getFluidAmount(), IFluidHandler.FluidAction.SIMULATE).getAmount() > 0) {
							if (te.tank.fill(fluidStack.copy(), IFluidHandler.FluidAction.SIMULATE) != 0) {
								int amount = te.tank.fill(fluidStack.copy(), IFluidHandler.FluidAction.EXECUTE);
								fluidHandler.drain(amount, IFluidHandler.FluidAction.EXECUTE);
								//support container changing tanks
								ItemStack container = fluidHandler.getContainer();
								if (te.getItem(0) != container) {
									te.setItem(0, container);
								}
							}
							te.setChanged();
						}
					});
				});
			}
		}
	}

	public void makeCheese(Level level) {
		if (this.canMakeCheese()) {
			ItemStack toAdd = new ItemStack(RatsBlockRegistry.BLOCK_OF_CHEESE.get());
			if (this.getItem(1).is(toAdd.getItem()) && this.getItem(1).getCount() < 64) {
				this.getItem(1).grow(1);
			} else if (this.getItem(1).isEmpty()) {
				this.setItem(1, toAdd.copy());
			}
			this.tank.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE);
			level.playSound(null, this.getBlockPos(), RatsSoundRegistry.CHEESE_MADE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
			for (int i = 0; i < 10; i++) {
				((ServerLevel) level).sendParticles(RatsParticleRegistry.MILK_BUBBLE.get(),
						this.getBlockPos().getX() + 0.25F + (level.getRandom().nextFloat() * 0.5F),
						(this.getBlockPos().getY() + 0.55F) + (this.tank.getFluidAmount() * 0.0001F),
						this.getBlockPos().getZ() + 0.25F + (level.getRandom().nextFloat() * 0.5F),
						1, 0.0D, 0.0D, 0.0D, 0);
			}
			this.setChanged();
		}
	}

	public boolean canMakeCheese() {
		if (this.tank.getFluidAmount() < FluidType.BUCKET_VOLUME) {
			return false;
		} else {
			ItemStack itemstack = new ItemStack(RatsBlockRegistry.BLOCK_OF_CHEESE.get());

			if (itemstack.isEmpty()) {
				return false;
			} else {
				ItemStack itemstack1 = this.getItem(1);
				if (itemstack1.isEmpty()) {
					return true;
				} else if (!itemstack1.is(itemstack.getItem())) {
					return false;
				} else if (itemstack1.getCount() + itemstack.getCount() <= this.getMaxStackSize() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize())  // Forge fix: make furnace respect stack sizes in furnace recipes
				{
					return true;
				} else {
					return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
				}
			}
		}
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (this.getLevel() != null && !this.getLevel().isClientSide()) {
			RatsNetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new UpdateCurdlerFluidPacket(this.getBlockPos().asLong(), this.tank.getFluid()));
		}
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack stack) {
		if (index == 1) {
			return false;
		} else {
			return isMilk(stack);
		}
	}

	private static boolean isMilkFluid(FluidStack fluid) {
		return fluid.getTranslationKey().contains("milk") || fluid.getTranslationKey().contains("Milk");
	}

	@Override
	public void clearContent() {
		this.curdlerStacks.clear();
	}

	public FluidTank getTank() {
		return this.tank;
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		if (side == Direction.DOWN) {
			return SLOTS_BOTTOM;
		} else {
			return SLOTS_TOP;
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

	@Override
	protected Component getDefaultName() {
		return Component.translatable(RatsLangConstants.CURDLER);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
		if (!this.remove && facing != null && capability == ForgeCapabilities.ITEM_HANDLER) {
			if (facing == Direction.DOWN)
				return this.handlers[1].cast();
			else
				return this.handlers[0].cast();
		}
		if (capability == ForgeCapabilities.FLUID_HANDLER)
			return this.holder.cast();
		return LazyOptional.empty();
	}

	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
		return new AutoCurdlerMenu(id, this, inventory, this.data);
	}
}
