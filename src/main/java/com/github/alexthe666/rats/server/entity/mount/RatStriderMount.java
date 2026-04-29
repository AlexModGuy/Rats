package com.github.alexthe666.rats.server.entity.mount;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.common.NeoForgeMod;

import java.util.UUID;
import net.minecraft.network.syncher.SynchedEntityData;

public class RatStriderMount extends RatMountBase {

	// 1.21: AttributeModifier ctor takes (ResourceLocation id, double, Operation); UUID-keyed lookup removed.
	private static final net.minecraft.resources.ResourceLocation SUFFOCATING_MODIFIER_ID = net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.github.alexthe666.rats.RatsMod.MODID, "strider_suffocating");
	private static final AttributeModifier SUFFOCATING_MODIFIER = new AttributeModifier(SUFFOCATING_MODIFIER_ID, -0.34F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
	private static final EntityDataAccessor<Boolean> DATA_SUFFOCATING = SynchedEntityData.defineId(RatStriderMount.class, EntityDataSerializers.BOOLEAN);

	public RatStriderMount(EntityType<? extends PathfinderMob> type, Level level) {
		super(type, level);
		this.setPathfindingMalus(PathType.WATER, -1.0F);
		this.setPathfindingMalus(PathType.LAVA, 0.0F);
		this.setPathfindingMalus(PathType.DANGER_FIRE, 0.0F);
		this.setPathfindingMalus(PathType.DAMAGE_FIRE, 0.0F);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.175F).add(Attributes.ATTACK_DAMAGE, 3.0D).add(Attributes.FOLLOW_RANGE, 16.0D);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_SUFFOCATING, false);
	}

	public void setSuffocating(boolean suffocating) {
		this.entityData.set(DATA_SUFFOCATING, suffocating);
		AttributeInstance attribute = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (attribute != null) {
			attribute.removeModifier(SUFFOCATING_MODIFIER_ID);
			if (suffocating) {
				attribute.addTransientModifier(SUFFOCATING_MODIFIER);
			}
		}

	}

	public boolean isSuffocating() {
		return this.entityData.get(DATA_SUFFOCATING);
	}

	@Override
	public boolean canStandOnFluid(FluidState p_204067_) {
		return p_204067_.is(FluidTags.LAVA);
	}

	@Override
	public double getPassengersRidingOffset() {
		float f = Math.min(0.25F, this.walkAnimation.speed());
		float f1 = this.walkAnimation.position();
		return (double) this.getBbHeight() - 0.1D + (double) (0.12F * Mth.cos(f1 * 1.5F) * 2.0F * f);
	}

	@Override
	protected float getRiddenSpeed(Player player) {
		return (float) (this.getAttributeValue(Attributes.MOVEMENT_SPEED) * (this.isSuffocating() ? 0.35D : 0.55D));
	}

	@Override
	protected float nextStep() {
		return this.moveDist + 0.6F;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(this.isInLava() ? SoundEvents.STRIDER_STEP_LAVA : SoundEvents.STRIDER_STEP, 1.0F, 1.0F);
	}

	@Override
	protected void checkFallDamage(double yDistance, boolean onGround, BlockState state, BlockPos pos) {
		this.checkInsideBlocks();
		if (this.isInLava()) {
			this.resetFallDistance();
		} else {
			super.checkFallDamage(yDistance, onGround, state, pos);
		}
	}

	@Override
	public void tick() {
		if (!this.isNoAi()) {
			BlockState blockstate = this.level().getBlockState(this.blockPosition());
			BlockState blockstate1 = this.getBlockStateOn();
			boolean flag = blockstate.is(BlockTags.STRIDER_WARM_BLOCKS) || blockstate1.is(BlockTags.STRIDER_WARM_BLOCKS) || this.getFluidTypeHeight(NeoForgeMod.LAVA_TYPE.value()) > 0.0D;

			this.setSuffocating(!flag);
		}

		super.tick();
		this.floatStrider();
		this.checkInsideBlocks();
	}

	private void floatStrider() {
		if (this.isInLava()) {
			CollisionContext collisioncontext = CollisionContext.of(this);
			if (collisioncontext.isAbove(LiquidBlock.STABLE_SHAPE, this.blockPosition(), true) && !this.level().getFluidState(this.blockPosition().above()).is(FluidTags.LAVA)) {
				this.setOnGround(true);
			} else {
				this.setDeltaMovement(this.getDeltaMovement().scale(0.5D).add(0.0D, 0.05D, 0.0D));
			}
		}

	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.STRIDER_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.STRIDER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.STRIDER_DEATH;
	}

	@Override
	public boolean isSensitiveToWater() {
		return true;
	}

	@Override
	public boolean isOnFire() {
		return false;
	}

	@Override
	protected PathNavigation createNavigation(Level level) {
		return new StriderPathNavigation(this, level);
	}

	@Override
	public float getWalkTargetValue(BlockPos pos, LevelReader reader) {
		if (reader.getBlockState(pos).getFluidState().is(FluidTags.LAVA)) {
			return 10.0F;
		} else {
			return this.isInLava() ? Float.NEGATIVE_INFINITY : 0.0F;
		}
	}

	@Override
	public Item getUpgradeItem() {
		return RatsItemRegistry.RAT_UPGRADE_STRIDER_MOUNT.get();
	}

	@Override
	public void adjustRatTailRotation(AbstractRat rat, AdvancedModelBox upperTail, AdvancedModelBox lowerTail) {
		this.progressRotation(upperTail, rat.sitProgress, 1.0F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(lowerTail, rat.sitProgress, -0.1F, 0.0F, 0.0F, 20.0F);
	}

	static class StriderPathNavigation extends GroundPathNavigation {
		StriderPathNavigation(RatStriderMount strider, Level level) {
			super(strider, level);
		}

		protected boolean hasValidPathType(PathType types) {
			return types == PathType.LAVA || types == PathType.DAMAGE_FIRE || types == PathType.DANGER_FIRE || super.hasValidPathType(types);
		}

		public boolean isStableDestination(BlockPos pos) {
			return this.level.getBlockState(pos).is(Blocks.LAVA) || super.isStableDestination(pos);
		}
	}
}
