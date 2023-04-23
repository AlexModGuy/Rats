package com.github.alexthe666.rats.server.entity.projectile;

import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.Rat;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class RatArrow extends AbstractArrow {

	private final ItemStack stack;

	public RatArrow(EntityType<? extends AbstractArrow> type, Level level) {
		super(type, level);
		this.stack = new ItemStack(RatsItemRegistry.RAT_ARROW.get());
	}

	public RatArrow(EntityType<? extends AbstractArrow> type, Level level, LivingEntity shooter, ItemStack stack) {
		super(type, shooter, level);
		this.stack = stack;
	}

	@Override
	protected ItemStack getPickupItem() {
		return new ItemStack(Items.ARROW);
	}

	@Override
	protected void onHit(HitResult result) {
		super.onHit(result);
		if (result.getType() != HitResult.Type.MISS) {
			Entity entity = null;
			BlockPos pos = null;
			if (result instanceof EntityHitResult eResult) {
				entity = eResult.getEntity();
				pos = eResult.getEntity().blockPosition();
			}
			Rat rat = new Rat(RatsEntityRegistry.RAT.get(), this.getLevel());
			CompoundTag ratTag = new CompoundTag();
			if (stack.getTag() != null && !stack.getTag().getCompound("Rat").isEmpty()) {
				ratTag = stack.getTag().getCompound("Rat");
			}
			rat.readAdditionalSaveData(ratTag);
			if (result instanceof BlockHitResult bResult) {
				pos = bResult.getBlockPos();

			}
			if (pos == null) {
				pos = BlockPos.containing(result.getLocation());
			} else {
				if (result instanceof BlockHitResult bResult) {
					pos = pos.relative(bResult.getDirection());
				}
			}
			rat.setPos(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
			if (!this.getLevel().isClientSide()) {
				this.getLevel().addFreshEntity(rat);
			}
			if (entity instanceof LivingEntity living && !rat.isAlliedTo(entity)) {
				rat.setTarget(living);
			}
			if (this.inGround) {
				this.discard();
				if (!this.getLevel().isClientSide()) {
					this.spawnAtLocation(this.getPickupItem(), 0.0F);
				}
			}
		}
	}
}
