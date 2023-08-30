package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.server.entity.misc.PlagueDoctor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class PlagueDoctorFollowGolemGoal extends Goal {
	private final PlagueDoctor doctor;
	@Nullable
	private IronGolem ironGolem;
	private int takeGolemRoseTick;
	private boolean tookGolemRose;

	public PlagueDoctorFollowGolemGoal(PlagueDoctor doctor) {
		this.doctor = doctor;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (this.doctor.level().isDay()) {
			List<IronGolem> list = this.doctor.level().getEntitiesOfClass(IronGolem.class, this.doctor.getBoundingBox().inflate(6.0D, 2.0D, 6.0D), golem -> golem.getOfferFlowerTick() > 0);
			if (!list.isEmpty()) {
				this.ironGolem = list.get(0);
				return this.ironGolem != null;
			}
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		return this.ironGolem == null || this.ironGolem.getOfferFlowerTick() > 0;
	}

	@Override
	public void start() {
		this.takeGolemRoseTick = this.doctor.getRandom().nextInt(320);
		this.tookGolemRose = false;
		if (this.ironGolem != null) {
			this.ironGolem.getNavigation().stop();
		}
	}

	@Override
	public void stop() {
		this.ironGolem = null;
		this.doctor.getNavigation().stop();
	}

	@Override
	public void tick() {
		if (this.ironGolem != null) {
			this.doctor.getLookControl().setLookAt(this.ironGolem, 30.0F, 30.0F);

			if (this.ironGolem.getOfferFlowerTick() == this.takeGolemRoseTick) {
				this.doctor.getNavigation().moveTo(this.ironGolem, 0.95D);
				this.tookGolemRose = true;
			}

			if (this.tookGolemRose && this.doctor.distanceToSqr(this.ironGolem) < 5.0D) {
				this.ironGolem.offerFlower(false);
				this.doctor.playSound(SoundEvents.BUNDLE_REMOVE_ONE);
				this.doctor.getNavigation().stop();
				this.doctor.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.POPPY));
			}
		} else {
			this.stop();
		}
	}
}