package com.github.alexthe666.rats.server.entity.ai.navigation.control;

import com.github.alexthe666.rats.server.block.RatCageBlock;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class RatCageMoveControl extends RatMoveControl {

	private final TamedRat rat;

	public RatCageMoveControl(TamedRat rat) {
		super(rat);
		this.rat = rat;
	}

	@Override
	public void tick() {
		//don't fire cage movement as often
		if (this.rat.tickCount % 60 == 0 && this.rat.getRandom().nextInt(10) == 0) {
			if (this.operation == MoveControl.Operation.STRAFE) {
				float f = (float) this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
				float f1 = (float) this.speedModifier * f;
				float f2 = this.strafeForwards;
				float f3 = this.strafeRight;
				float f4 = Mth.sqrt(f2 * f2 + f3 * f3);
				if (f4 < 1.0F) {
					f4 = 1.0F;
				}

				f4 = f1 / f4;
				f2 *= f4;
				f3 *= f4;
				float f5 = Mth.sin(this.mob.getYRot() * ((float) Math.PI / 180F));
				float f6 = Mth.cos(this.mob.getYRot() * ((float) Math.PI / 180F));
				float f7 = f2 * f6 - f3 * f5;
				float f8 = f3 * f6 + f2 * f5;
				//only path towards a block if it's a cage
				if (this.rat.getLevel().getBlockState(this.rat.blockPosition().offset((int) f7, 0, (int) f8)).getBlock() instanceof RatCageBlock) {
					this.strafeForwards = 1.0F;
					this.strafeRight = 0.0F;
				}

				this.mob.setSpeed(f1);
				this.mob.setZza(this.strafeForwards);
				this.mob.setXxa(this.strafeRight);
				this.operation = MoveControl.Operation.WAIT;
			} else if (this.operation == MoveControl.Operation.MOVE_TO) {
				this.operation = MoveControl.Operation.WAIT;
				double d0 = this.wantedX - this.mob.getX();
				double d1 = this.wantedZ - this.mob.getZ();
				double d2 = this.wantedY - this.mob.getY();
				double d3 = d0 * d0 + d2 * d2 + d1 * d1;
				if (d3 < (double) 2.5000003E-7F) {
					this.mob.setZza(0.0F);
					return;
				}

				float f9 = (float) (Mth.atan2(d1, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
				this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f9, 90.0F));
				this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
				//remove jump code
//				BlockPos blockpos = this.mob.blockPosition();
//				BlockState blockstate = this.mob.level.getBlockState(blockpos);
//				VoxelShape voxelshape = blockstate.getCollisionShape(this.mob.level, blockpos);
//				if (d2 > (double)this.mob.getStepHeight() && d0 * d0 + d1 * d1 < (double)Math.max(1.0F, this.mob.getBbWidth()) || !voxelshape.isEmpty() && this.mob.getY() < voxelshape.max(Direction.Axis.Y) + (double)blockpos.getY() && !blockstate.is(BlockTags.DOORS) && !blockstate.is(BlockTags.FENCES)) {
//					this.mob.getJumpControl().jump();
//					this.operation = MoveControl.Operation.JUMPING;
//				}
			} else if (this.operation == MoveControl.Operation.JUMPING) {
				this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
				if (this.mob.isOnGround()) {
					this.operation = MoveControl.Operation.WAIT;
				}
			} else {
				this.mob.setZza(0.0F);
			}
		}
	}
}
