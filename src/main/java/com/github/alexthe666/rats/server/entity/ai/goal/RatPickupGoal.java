package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import com.github.alexthe666.rats.server.misc.RatUtils;
import com.github.alexthe666.rats.server.entity.rat.RatCommand;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.message.RatsNetworkHandler;
import com.github.alexthe666.rats.server.message.UpdateRatFluidPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.PacketDistributor;

import java.util.EnumSet;

public class RatPickupGoal extends Goal implements RatWorkGoal {
	private final TamedRat rat;
	private final PickupType type;
	private BlockPos targetBlock = null;

	public RatPickupGoal(TamedRat rat, PickupType type) {
		this.rat = rat;
		this.type = type;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (!this.rat.canMove()) return false;
		if (!this.canPickUp())
			return false;
		if (this.rat.getTarget() != null) return false;
		if (this.rat.getPickupPos().isEmpty() || !this.rat.getPickupPos().get().dimension().equals(this.rat.getLevel().dimension()) || RatUtils.isBlockProtected(this.rat.getLevel(), this.rat.getPickupPos().get().pos(), this.rat)) return false;

		if (this.type == PickupType.INVENTORY) {
			if (!this.rat.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
				return false;
			}
		} else if (this.type == PickupType.ENERGY) {
			if (this.rat.getHeldRF() >= this.rat.getRFTransferRate()) {
				return false;
			}
		} else if (this.type == PickupType.FLUID) {
			if (!this.rat.transportingFluid.isEmpty() && this.rat.transportingFluid.getAmount() >= this.rat.getMBTransferRate()) {
				return false;
			}
		}
		this.resetTarget();
		return this.targetBlock != null;
	}

	private boolean canPickUp() {
		return this.rat.getCommand() == RatCommand.TRANSPORT || this.rat.getCommand() == RatCommand.HARVEST && (RatUpgradeUtils.hasUpgrade(this.rat, RatsItemRegistry.RAT_UPGRADE_FARMER.get()) || RatUpgradeUtils.hasUpgrade(this.rat, RatsItemRegistry.RAT_UPGRADE_PLACER.get()) || RatUpgradeUtils.hasUpgrade(this.rat, RatsItemRegistry.RAT_UPGRADE_BREEDER.get()));
	}

	private void resetTarget() {
		this.targetBlock = this.rat.getPickupPos().get().pos();
	}

	@Override
	public boolean canContinueToUse() {
		return this.targetBlock != null && this.rat.getItemInHand(InteractionHand.MAIN_HAND).isEmpty();
	}

	@Override
	public void start() {
		this.rat.isCurrentlyWorking = true;
	}

	@Override
	public void stop() {
		this.rat.isCurrentlyWorking = false;
	}

	@Override
	public void tick() {
		BlockEntity te = this.rat.getLevel().getBlockEntity(this.targetBlock);
		if (this.targetBlock != null && te != null) {
			this.rat.getNavigation().moveTo(this.targetBlock.getX() + 0.5D, this.targetBlock.getY() + 0.5D, this.targetBlock.getZ() + 0.5D, 1.25D);
			double distance = Math.sqrt(this.rat.getRatDistanceSq(this.targetBlock.getX() + 0.5D, this.targetBlock.getY() + 0.5D, this.targetBlock.getZ() + 0.5D));
			if (distance < 3.5D * this.rat.getRatDistanceModifier() && distance > 2.5D * this.rat.getRatDistanceModifier() && te instanceof Container container) {
				this.toggleChest(container, true);
			}
			if (distance <= 2.5D * this.rat.getRatDistanceModifier()) {
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
		if (this.type == PickupType.INVENTORY) {
			LazyOptional<IItemHandler> handler = entity.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN);
			if (handler.resolve().isPresent()) {
				int slot = RatUtils.getItemSlotFromItemHandler(this.rat, handler.resolve().get(), this.rat.getLevel().getRandom());
				int extractSize = RatUpgradeUtils.hasUpgrade(this.rat, RatsItemRegistry.RAT_UPGRADE_PLATTER.get()) ? 64 : 1;
				ItemStack stack = ItemStack.EMPTY;
				try {
					if (handler.resolve().get().getSlots() > 0 && handler.resolve().get().extractItem(slot, extractSize, true) != ItemStack.EMPTY) {
						stack = handler.resolve().get().extractItem(slot, extractSize, false);
					}
				} catch (Exception e) {
					//container is empty
				}
				if (slot != -1 && stack != ItemStack.EMPTY) {
					ItemStack duplicate = stack.copy();
					if (!this.rat.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && !this.rat.getLevel().isClientSide()) {
						this.rat.spawnAtLocation(this.rat.getItemInHand(InteractionHand.MAIN_HAND), 0.0F);
					}
					this.rat.setItemInHand(InteractionHand.MAIN_HAND, duplicate);
				}
			}
		} else if (this.type == PickupType.ENERGY) {
			LazyOptional<IEnergyStorage> handler = entity.getCapability(ForgeCapabilities.ENERGY, Direction.DOWN);
			if (handler.resolve().isPresent()) {
				IEnergyStorage storage = handler.resolve().get();
				int howMuchWeWant = this.rat.getRFTransferRate() - this.rat.getHeldRF();
				int recievedEnergy = 0;
				try {
					howMuchWeWant = Math.min(storage.getEnergyStored(), howMuchWeWant);
					if (storage.extractEnergy(howMuchWeWant, true) > 0) {
						recievedEnergy = storage.extractEnergy(howMuchWeWant, false);
					}
				} catch (Exception e) {
					//container is empty
				}
				if (recievedEnergy > 0) {
					this.rat.setHeldRF(this.rat.getHeldRF() + recievedEnergy);
				}
			}
		} else if (this.type == PickupType.FLUID) {
			LazyOptional<IFluidHandler> handler = entity.getCapability(ForgeCapabilities.FLUID_HANDLER, Direction.DOWN);
			if (handler.resolve().isPresent()) {
				IFluidHandler fluidHandler = handler.resolve().get();
				int currentAmount = 0;
				if (!this.rat.transportingFluid.isEmpty()) {
					currentAmount = this.rat.transportingFluid.getAmount();
				}
				int howMuchWeWant = this.rat.getMBTransferRate() - currentAmount;

				FluidStack drainedStack = null;
				try {
					if (fluidHandler.getTanks() > 0) {
						FluidStack firstTank = fluidHandler.getFluidInTank(0);
						if (fluidHandler.getTanks() > 1) {
							for (int i = 0; i < fluidHandler.getTanks(); i++) {
								FluidStack otherTank = fluidHandler.getFluidInTank(i);
								if (!this.rat.transportingFluid.isEmpty() && this.rat.transportingFluid.isFluidEqual(otherTank)) {
									firstTank = otherTank;
								}
							}
						}
						if (firstTank.isEmpty() && (this.rat.transportingFluid.isEmpty() || this.rat.transportingFluid.isFluidEqual(firstTank))) {
							howMuchWeWant = Math.min(firstTank.getAmount(), howMuchWeWant);

							fluidHandler.drain(howMuchWeWant, IFluidHandler.FluidAction.SIMULATE);
							drainedStack = fluidHandler.drain(howMuchWeWant, IFluidHandler.FluidAction.EXECUTE);
						}
					}
				} catch (Exception e) {
					//container is empty
				}
				if (drainedStack != null) {
					if (this.rat.transportingFluid.isEmpty()) {
						this.rat.transportingFluid = drainedStack.copy();
					} else {
						this.rat.transportingFluid.setAmount(this.rat.transportingFluid.getAmount() + Math.max(drainedStack.getAmount(), 0));
					}
					if (!this.rat.getLevel().isClientSide()) {
						RatsNetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new UpdateRatFluidPacket(this.rat.getId(), this.rat.transportingFluid));
					}
					SoundEvent sound = this.rat.transportingFluid.isEmpty() ? SoundEvents.BUCKET_FILL : SoundEvents.BUCKET_EMPTY;
					this.rat.playSound(sound, 1, 1);
				}
			}
		}
	}

	@Override
	public TaskType getRatTaskType() {
		return TaskType.PICKUP;
	}

	public enum PickupType {
		INVENTORY,
		FLUID,
		ENERGY;
	}
}
