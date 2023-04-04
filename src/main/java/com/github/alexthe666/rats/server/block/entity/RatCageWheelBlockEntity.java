package com.github.alexthe666.rats.server.block.entity;

import com.github.alexthe666.rats.registry.RatsBlockEntityRegistry;
import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.block.RatCageBlock;
import com.github.alexthe666.rats.server.block.RatCageWheelBlock;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.RatsEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class RatCageWheelBlockEntity extends DecoratedRatCageBlockEntity {

	public int useTicks = 0;
	public float wheelRot;
	public float prevWheelRot;
	private final RandomSource random = RandomSource.create();
	private TamedRat wheeler;
	private float goalOfWheel = 0.0F;
	private int dismountCooldown = 0;
	public final LazyOptional<RatsEnergyStorage> energyStorage = RatsEnergyStorage.create(1000, 10, 10, 0);

	public RatCageWheelBlockEntity(BlockPos pos, BlockState state) {
		super(RatsBlockEntityRegistry.RAT_CAGE_WHEEL.get(), pos, state);
	}

	@Override
	public ItemStack getContainedItem() {
		return new ItemStack(RatsItemRegistry.RAT_WHEEL.get());
	}

	@Override
	public void setContainedItem(ItemStack stack) {
	}

	@Override
	public void saveAdditional(CompoundTag compound) {
		compound.putInt("UseTicks", this.useTicks);
		if (this.energyStorage.resolve().isPresent()) {
			compound.putInt("StoredEnergy", this.energyStorage.resolve().get().energy);
		}

		compound.putInt("DismountCooldown", this.dismountCooldown);
		super.saveAdditional(compound);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		this.useTicks = compound.getInt("UseTicks");
		this.dismountCooldown = compound.getInt("DismountCooldown");
		if (this.energyStorage.resolve().isPresent()) {
			this.energyStorage.resolve().get().energy = compound.getInt("StoredEnergy");
		}

	}

	public static void tick(Level level, BlockPos pos, BlockState state, RatCageWheelBlockEntity te) {
		te.prevWheelRot = te.wheelRot;
		float wheelAdd = 20.0F;
		if (te.dismountCooldown > 0) {
			--te.dismountCooldown;
		}

		if (te.wheeler == null && te.dismountCooldown <= 0) {

			for (TamedRat rat : level.getEntitiesOfClass(TamedRat.class, new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0D, pos.getY() + 1.0D, pos.getZ() + 1.0D))) {
				if (rat.isTame()) {
					te.wheeler = rat;
				}
			}
		}

		if (te.wheeler != null) {
			double dist = te.wheeler.distanceToSqr(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F);
			BlockState ratWheelState = level.getBlockState(te.wheeler.blockPosition());
			Direction facing = Direction.NORTH;
			if (state.is(RatsBlockRegistry.RAT_CAGE_WHEEL.get())) {
				facing = state.getValue(RatCageWheelBlock.FACING);
			}

			te.wheeler.setYRot(facing.toYRot());
			te.wheeler.yBodyRot = te.wheeler.getYRot();
			if (te.useTicks <= 20 || !(dist > 3.0D) && ratWheelState == state) {
				if (te.wheeler != null) {
					te.wheeler.setPos(pos.getX() + 0.5F, pos.getY() + 0.15F, pos.getZ() + 0.5F);
					te.wheeler.setInWheel(true);
					++te.useTicks;
					if (te.energyStorage.resolve().isPresent()) {
						RatsEnergyStorage storage = te.energyStorage.resolve().get();
						int nrg = 10;
						if (RatUpgradeUtils.hasUpgrade(te.wheeler, RatsItemRegistry.RAT_UPGRADE_SPEED.get())) {
							wheelAdd = 40.0F;
							nrg = 20;
						}

						if (storage.receiveEnergy(nrg, true) != 0) {
							storage.receiveEnergy(nrg, false);
						}

						te.wheelRot += wheelAdd;
						RatCageBlock cageBlock = (RatCageBlock) state.getBlock();
						if (te.useTicks > 200 && te.useTicks % 100 == 0 && te.random.nextFloat() > 0.25F) {

							for (Direction direction : Direction.values()) {
								if (cageBlock.canFenceConnectTo(level.getBlockState(pos.relative(direction))) == 1 && te.wheeler != null) {
									te.wheeler.setPos((float) pos.relative(direction).getX() + 0.5F, (float) pos.relative(direction).getY() + 0.5F, (float) pos.relative(direction).getZ() + 0.5F);
									te.wheeler.setInWheel(false);
									te.wheeler = null;
									te.dismountCooldown = 1200 + te.random.nextInt(1200);
								}
							}
						}
					}
				}
			} else {
				te.wheeler.setInWheel(false);
				te.wheeler = null;
			}
		} else {
			if (te.useTicks != 0) {
				te.wheelRot %= 360.0F;
				te.goalOfWheel = (float) Mth.floor((te.wheelRot + 90.0F) / 90.0F) * 90.0F % 360.0F;
				te.prevWheelRot = te.wheelRot % 360.0F;
			}

			te.useTicks = 0;
			if (Math.toRadians(te.wheelRot) % 90.0D != 0.0D && !(Math.abs(te.goalOfWheel - te.wheelRot) < 1.5F)) {
				if (te.wheelRot > te.goalOfWheel) {
					te.wheelRot -= Math.min(wheelAdd, te.wheelRot - te.goalOfWheel);
				} else if (te.wheelRot < te.goalOfWheel) {
					te.wheelRot += Math.min(wheelAdd, te.goalOfWheel - te.wheelRot);
				}
			} else {
				te.goalOfWheel = 0.0F;
				te.prevWheelRot = 0.0F;
				te.wheelRot = 0.0F;
			}
		}

	}

	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
		return capability == ForgeCapabilities.ENERGY ? this.energyStorage.cast() : super.getCapability(capability, facing);
	}
}
