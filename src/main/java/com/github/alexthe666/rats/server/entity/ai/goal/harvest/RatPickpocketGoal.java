package com.github.alexthe666.rats.server.entity.ai.goal.harvest;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ReputationEventHandler;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.List;

public class RatPickpocketGoal extends BaseRatHarvestGoal {

	public RatPickpocketGoal(TamedRat rat) {
		super(rat);
	}

	@Override
	public boolean canUse() {
		if (!super.canUse() || !this.checkTheBasics(false, false)) {
			return false;
		}
		if (!this.rat.getMainHandItem().isEmpty() || this.rat.pickpocketCooldown > 0) {
			return false;
		}
		this.resetTarget();
		return this.getTargetEntity() != null;
	}

	private void resetTarget() {
		int radius = this.rat.getRadius();
		AABB bb = new AABB(-radius, -radius, -radius, radius, radius, radius).move(this.rat.getSearchCenter());
		List<AbstractVillager> list = this.rat.level().getEntitiesOfClass(AbstractVillager.class, bb, villager -> !villager.getOffers().isEmpty());
		list.sort(Comparator.comparingDouble(this.rat::distanceToSqr));
		if (!list.isEmpty()) {
			this.setTargetEntity(list.get(0));
		}
	}

	@Override
	public void tick() {
		if (this.getTargetEntity() != null && this.getTargetEntity().isAlive() && this.rat.getOwner() != null && this.rat.getMainHandItem().isEmpty()) {
			this.rat.getNavigation().moveTo(this.getTargetEntity(), 1.05D);
			if (this.rat.distanceToSqr(this.getTargetEntity()) < this.rat.getRatHarvestDistance(1.0D)) {
				if (this.getTargetEntity() instanceof AbstractVillager villager && !villager.getOffers().isEmpty()) {
					MerchantOffer offer = villager.getOffers().get(this.rat.getRandom().nextInt(villager.getOffers().size() - 1));
					if (!offer.getResult().isEmpty()) {
						this.rat.setItemInHand(InteractionHand.MAIN_HAND, offer.getResult().copy());
						this.rat.playSound(SoundEvents.ARMOR_EQUIP_LEATHER, 2.0F, 1.0F);
						this.rat.pickpocketCooldown = 6000 + this.rat.getRandom().nextInt(6000);
						this.rat.getNavigation().moveTo(this.rat.getOwner(), 1.25D);
						villager.getBrain().setActiveActivityIfPossible(Activity.AVOID);
						if (villager instanceof ReputationEventHandler handler && this.rat.getRandom().nextInt(10) == 0) {
							((ServerLevel) this.rat.level()).onReputationEvent(ReputationEventType.VILLAGER_HURT, this.rat.getOwner(), handler);
						}
						if (this.rat.getRandom().nextInt(10) == 0) {
							List<IronGolem> golems = this.rat.level().getEntitiesOfClass(IronGolem.class, this.rat.getBoundingBox().inflate(8.0F));
							for (IronGolem golem : golems) {
								if (golem.getTarget() == null && golem.getOfferFlowerTick() <= 0) {
									golem.setTarget(this.rat);
								}
							}
						}
						this.stop();
					}
				}
				this.stop();
			}
		} else {
			this.stop();
		}
	}
}
