package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.block.entity.AutoCurdlerBlockEntity;
import com.github.alexthe666.rats.server.message.UpdateCurdlerFluidPacket;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.message.RatsNetworkHandler;
import com.github.alexthe666.rats.server.message.UpdateRatFluidPacket;
import com.github.alexthe666.rats.server.misc.RatUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.PacketDistributor;

import java.util.EnumSet;
import java.util.List;

public class RatDepositGoal extends Goal implements RatWorkGoal {
	private final TamedRat rat;
	private final DepositType type;
	private BlockPos targetBlock = null;

	public RatDepositGoal(TamedRat entity, DepositType type) {
		super();
		this.rat = entity;
		this.type = type;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (!this.rat.canMove()) return false;
		if (!this.rat.getCommand().workCommand) return false;
		if (this.rat.getTarget() != null) return false;
		if (this.rat.getDepositPos().isEmpty() || !this.rat.getDepositPos().get().dimension().equals(this.rat.getLevel().dimension()) || RatUtils.isBlockProtected(this.rat.getLevel(), this.rat.getDepositPos().get().pos(), this.rat))
			return false;

		BlockEntity te = this.rat.getLevel().getBlockEntity(this.rat.getDepositPos().get().pos());
		if (te == null) return false;

		if (this.type == DepositType.INVENTORY) {
			if (!this.rat.shouldDepositItem(this.rat.getMainHandItem())) return false;
			if (this.rat.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
				return false;
			}
			if (RatUpgradeUtils.hasUpgrade(this.rat, RatsItemRegistry.RAT_UPGRADE_PLATTER.get())) {
				if (this.rat.getMainHandItem().getCount() < 64 && !this.getItemsOfTypeAround(this.rat.getMainHandItem()).isEmpty())
					return false;
			}
			if (te.getCapability(ForgeCapabilities.ITEM_HANDLER, this.rat.depositFacing).resolve().isEmpty()) {
				return false;
			}

		} else if (this.type == DepositType.ENERGY) {
			if (this.rat.getRFTransferRate() <= 0 || this.rat.getHeldRF() <= 0) {
				return false;
			}
			if (te.getCapability(ForgeCapabilities.ENERGY, this.rat.depositFacing).resolve().isEmpty()) {
				return false;
			}
		} else if (this.type == DepositType.FLUID) {
			if (this.rat.transportingFluid.isEmpty() || this.rat.transportingFluid.getAmount() == 0) {
				return false;
			}
			if (te.getCapability(ForgeCapabilities.FLUID_HANDLER, this.rat.depositFacing).resolve().isEmpty()) {
				return false;
			}
		}
		this.resetTarget();
		return targetBlock != null;
	}

	private List<ItemEntity> getItemsOfTypeAround(ItemStack stack) {
		return this.rat.getLevel().getEntitiesOfClass(ItemEntity.class, this.rat.getBoundingBox().inflate(this.rat.getRadius()), item -> {
			if (!ItemStack.isSameItemSameTags(stack, item.getItem())) return false;
			Path path = this.rat.getNavigation().createPath(item, 1);
			return path != null && path.canReach();
		});
	}

	private void resetTarget() {
		this.targetBlock = this.rat.getDepositPos().get().pos();
	}

	@Override
	public boolean canContinueToUse() {
		if (RatUpgradeUtils.hasUpgrade(this.rat, RatsItemRegistry.RAT_UPGRADE_PLATTER.get())) {
			if (this.rat.getMainHandItem().getCount() < 64 && this.getItemsOfTypeAround(this.rat.getMainHandItem()).isEmpty())
				return false;
		}
		return this.targetBlock != null && !this.rat.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && this.rat.shouldDepositItem(this.rat.getMainHandItem());
	}

	@Override
	public void start() {
		this.rat.isCurrentlyWorking = true;
	}

	@Override
	public void stop() {
		this.rat.isCurrentlyWorking = false;
	}

	private Vec3 getMovePos() {
		return Vec3.atCenterOf(this.targetBlock);
	}

	@Override
	public void tick() {
		BlockEntity te = this.rat.getLevel().getBlockEntity(this.targetBlock);
		if (this.targetBlock != null && te != null) {
			this.rat.getNavigation().moveTo(this.getMovePos().x(), this.getMovePos().y(), this.getMovePos().z(), 1.25D);
			double distance = Math.sqrt(this.rat.distanceToSqr(this.targetBlock.getX() + 0.5D, this.targetBlock.getY() + 0.5D, this.targetBlock.getZ() + 0.5D));
			if (distance < 4.5D * this.rat.getRatDistanceModifier() && distance > 2.5D * this.rat.getRatDistanceModifier() && te instanceof Container container) {
				this.toggleChest(container, true);
			}
			if (distance <= 2.0D * this.rat.getRatDistanceModifier()) {
				if (te instanceof Container container) {
					this.toggleChest(container, false);
				}

				this.executeTask(te);
				this.targetBlock = null;
				this.stop();
			}
		}
	}

	public void toggleChest(Container te, boolean open) {
		if (te instanceof ChestBlockEntity chest) {
			if (open) {
				this.rat.getLevel().blockEvent(this.targetBlock, chest.getBlockState().getBlock(), 1, 1);
			} else {
				this.rat.getLevel().blockEvent(this.targetBlock, chest.getBlockState().getBlock(), 1, 0);
			}
		}
	}

	private void executeTask(BlockEntity entity) {
		if (this.type == DepositType.INVENTORY) {
			LazyOptional<IItemHandler> handler = entity.getCapability(ForgeCapabilities.ITEM_HANDLER, this.rat.depositFacing);
			if (handler.resolve().isPresent()) {
				IItemHandler resolvedHandler = handler.resolve().get();
				ItemStack duplicate = this.rat.getItemInHand(InteractionHand.MAIN_HAND).copy();
				if (!ItemHandlerHelper.insertItem(resolvedHandler, duplicate, true).equals(duplicate)) {
					ItemStack shrunkenStack = ItemHandlerHelper.insertItem(resolvedHandler, duplicate, false);
					if (shrunkenStack.isEmpty()) {
						this.rat.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
					} else {
						this.rat.setItemInHand(InteractionHand.MAIN_HAND, shrunkenStack);
					}
				}
			}
		} else if (this.type == DepositType.ENERGY) {
			LazyOptional<IEnergyStorage> handler = entity.getCapability(ForgeCapabilities.ENERGY, this.rat.depositFacing);
			if (handler.resolve().isPresent()) {
				IEnergyStorage storage = handler.resolve().get();
				int howMuchWeHave = this.rat.getHeldRF();
				int inputtedEnergy = 0;
				try {
					if (storage.canReceive() && storage.receiveEnergy(howMuchWeHave, true) > 0) {
						inputtedEnergy = storage.receiveEnergy(howMuchWeHave, false);
					}
				} catch (Exception e) {
					//container is empty
				}
				if (inputtedEnergy > 0) {
					this.rat.setHeldRF(Math.max(0, this.rat.getHeldRF() - inputtedEnergy));
				}
			}
		} else if (this.type == DepositType.FLUID) {
			FluidStack copiedFluid = this.rat.transportingFluid.copy();
			LazyOptional<IFluidHandler> handler = entity.getCapability(ForgeCapabilities.FLUID_HANDLER, this.rat.depositFacing);
			if (handler.resolve().isPresent()) {
				IFluidHandler fluidHandler = handler.resolve().get();
				if (!this.rat.transportingFluid.isEmpty()) {
					int minusAmount = 0;
					try {
						if (fluidHandler.getTanks() > 0) {
							FluidStack firstTank = fluidHandler.getFluidInTank(0);
							if (fluidHandler.getTanks() > 1) {
								for (int i = 0; i < fluidHandler.getTanks(); i++) {
									FluidStack otherTank = fluidHandler.getFluidInTank(i);
									if (copiedFluid != null && copiedFluid.isFluidEqual(otherTank)) {
										firstTank = otherTank;
									}
								}
							}
							if (firstTank.isEmpty() || (copiedFluid == null || copiedFluid.isFluidEqual(firstTank))) {
								if (fluidHandler.fill(copiedFluid, IFluidHandler.FluidAction.SIMULATE) != 0) {
									minusAmount = fluidHandler.fill(copiedFluid, IFluidHandler.FluidAction.EXECUTE);
								}
							}
						}
					} catch (Exception e) {
						//container is empty
					}

					if (minusAmount > 0) {
						int total = copiedFluid.getAmount() - minusAmount;
						if (total <= 0) {
							this.rat.transportingFluid = FluidStack.EMPTY;
						} else {
							this.rat.transportingFluid.setAmount(total);
						}
						if (!this.rat.getLevel().isClientSide()) {
							RatsNetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new UpdateRatFluidPacket(this.rat.getId(), this.rat.transportingFluid));
							if (this.rat.getLevel().getBlockEntity(this.targetBlock) instanceof AutoCurdlerBlockEntity curdler) {
								RatsNetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new UpdateCurdlerFluidPacket(this.targetBlock.asLong(), curdler.getTank().getFluid()));
							}
						}
						SoundEvent sound = this.rat.transportingFluid.isEmpty() ? SoundEvents.BUCKET_EMPTY : SoundEvents.BUCKET_FILL;
						this.rat.playSound(sound, 1, 1);
					}
				}
			}
		}
	}

	@Override
	public TaskType getRatTaskType() {
		return TaskType.DEPOSIT;
	}

	public enum DepositType {
		INVENTORY,
		FLUID,
		ENERGY;
	}
}
