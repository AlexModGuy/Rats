package com.github.alexthe666.rats.server.block.entity;

import com.github.alexthe666.rats.registry.RatlantisBlockEntityRegistry;
import com.github.alexthe666.rats.registry.RatlantisBlockRegistry;
import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class RatlantisTokenBlockEntity extends BlockEntity {

	public int tickCount;
	public float ratRotation;
	public float ratRotationPrev;
	public float tRot;

	public RatlantisTokenBlockEntity(BlockPos pos, BlockState state) {
		super(RatlantisBlockEntityRegistry.TOKEN.get(), pos, state);
	}

	public void saveAdditional(CompoundTag compound) {
		compound.putInt("TicksExisted", tickCount);
		super.saveAdditional(compound);
	}

	public void load(CompoundTag compound) {
		super.load(compound);
		tickCount = compound.getInt("TicksExisted");
	}

	public AABB getRenderBoundingBox() {
		return new AABB(this.getBlockPos().offset(-3, -4, -3), this.getBlockPos().offset(3, 4, 3));
	}

	public static void tick(Level level, BlockPos pos, BlockState state, RatlantisTokenBlockEntity te) {
		te.tickCount++;
		te.ratRotationPrev = te.ratRotation;
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

		if (te.tickCount >= 592) {
			level.explode(null, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ(), 3, Level.ExplosionInteraction.NONE);
			level.setBlockAndUpdate(pos.above(3), RatsBlockRegistry.MARBLED_CHEESE_RAW.get().defaultBlockState());
			level.setBlockAndUpdate(pos.above(2), RatlantisBlockRegistry.RATLANTIS_PORTAL.get().defaultBlockState());
			level.setBlockAndUpdate(pos.above(1), RatlantisBlockRegistry.RATLANTIS_PORTAL.get().defaultBlockState());
			level.setBlockAndUpdate(pos, RatsBlockRegistry.MARBLED_CHEESE_RAW.get().defaultBlockState());
		}

	}
}
