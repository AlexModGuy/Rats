package com.github.alexthe666.rats.server.entity.mount;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class RatGolemMount extends RatMountBase {

	private int attackTimer;

	public RatGolemMount(EntityType<? extends PathfinderMob> type, Level level) {
		super(type, level);
		this.riderY = 1.95F;
		this.riderXZ = -0.1F;
	}

	@Override
	public float getStepHeight() {
		return 1.0F;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 100.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.2D)
				.add(Attributes.ATTACK_DAMAGE, 1.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
	}

	public void aiStep() {
		super.aiStep();
		if (this.attackTimer > 0) {
			--this.attackTimer;
		}

		if (this.getDeltaMovement().horizontalDistance() > (double) 2.5000003E-7F && this.getRandom().nextInt(5) == 0) {
			int i = Mth.floor(this.getX());
			int j = Mth.floor(this.getY() - (double) 0.2F);
			int k = Mth.floor(this.getZ());
			BlockState blockstate = this.level().getBlockState(new BlockPos(i, j, k));
			if (!blockstate.isAir()) {
				this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate), this.getX() + ((double) this.getRandom().nextFloat() - 0.5D) * (double) this.getBbWidth(), this.getBoundingBox().minY + 0.1D, this.getZ() + ((double) this.getRandom().nextFloat() - 0.5D) * (double) this.getBbWidth(), 4.0D * ((double) this.getRandom().nextFloat() - 0.5D), 0.5D, ((double) this.getRandom().nextFloat() - 0.5D) * 4.0D);
			}
		}
	}

	public boolean hurt(DamageSource source, float amount) {
		Cracks irongolementity$cracks = this.getCracks();
		boolean flag = super.hurt(source, amount);
		if (flag && this.getCracks() != irongolementity$cracks) {
			this.playSound(SoundEvents.IRON_GOLEM_DAMAGE, 1.0F, 1.0F);
		}
		return flag;
	}

	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!stack.is(Items.IRON_INGOT)) {
			return super.mobInteract(player, hand);
		} else {
			float f = this.getHealth();
			this.heal(25.0F);
			if (this.getHealth() == f) {
				return super.mobInteract(player, hand);
			} else {
				float f1 = 1.0F + (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F;
				this.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, f1);
				if (!player.getAbilities().instabuild) {
					stack.shrink(1);
				}
				return InteractionResult.SUCCESS;
			}
		}
	}

	public RatGolemMount.Cracks getCracks() {
		return RatGolemMount.Cracks.byFraction(this.getHealth() / this.getMaxHealth());
	}

	public boolean doHurtTarget(Entity entity) {
		this.attackTimer = 10;
		this.level().broadcastEntityEvent(this, (byte) 4);
		boolean flag = entity.hurt(this.damageSources().mobAttack(this), (float) (7 + this.getRandom().nextInt(15)));
		if (flag) {
			entity.setDeltaMovement(entity.getDeltaMovement().add(0.0D, 0.4F, 0.0D));
			this.doEnchantDamageEffects(this, entity);
		}

		this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
		return flag;
	}

	public void handleEntityEvent(byte id) {
		if (id == 4) {
			this.attackTimer = 10;
			this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
		} else {
			super.handleEntityEvent(id);
		}

	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.IRON_GOLEM_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.IRON_GOLEM_DEATH;
	}

	protected void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F);
	}

	public int getAttackTimer() {
		return this.attackTimer;
	}

	@Override
	public Item getUpgradeItem() {
		return RatsItemRegistry.RAT_UPGRADE_GOLEM_MOUNT.get();
	}

	@Override
	public void adjustRatTailRotation(AbstractRat rat, AdvancedModelBox upperTail, AdvancedModelBox lowerTail) {
		this.progressRotation(upperTail, rat.sitProgress, -0.5F, 0.0F, 0.0F, 20.0F);
	}

	public enum Cracks {
		NONE(1.0F),
		LOW(0.75F),
		MEDIUM(0.5F),
		HIGH(0.25F);

		private static final List<RatGolemMount.Cracks> BY_DAMAGE = Stream.of(values()).sorted(Comparator.comparingDouble(cracks -> cracks.fraction)).collect(ImmutableList.toImmutableList());
		private final float fraction;

		Cracks(float fraction) {
			this.fraction = fraction;
		}

		public static RatGolemMount.Cracks byFraction(float fraction) {
			for (RatGolemMount.Cracks cracks : BY_DAMAGE) {
				if (fraction < cracks.fraction) {
					return cracks;
				}
			}

			return NONE;
		}
	}
}
