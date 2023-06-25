package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import com.github.alexthe666.rats.server.entity.monster.PlagueCloud;
import com.github.alexthe666.rats.server.entity.monster.boss.BlackDeath;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.event.ForgeEventFactory;

public class BlackDeathSummonCloudGoal extends BlackDeathAbstractSummonGoal {
	public BlackDeathSummonCloudGoal(BlackDeath death) {
		super(death);
	}

	@Override
	public boolean canUse() {
		if (this.death.getRatsSummoned() >= 15) {
			return super.canUse();
		}
		return false;
	}

	@Override
	public int getAttackCooldown() {
		return 120;
	}

	@Override
	public void summonEntity() {
		PlagueCloud cloud = new PlagueCloud(RatsEntityRegistry.PLAGUE_CLOUD.get(), this.death.level());
		ForgeEventFactory.onFinalizeSpawn(cloud, (ServerLevel) this.death.level(), this.death.level().getCurrentDifficultyAt(this.death.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
		cloud.copyPosition(this.death);
		this.death.level().addFreshEntity(cloud);
		cloud.setOwnerId(this.death.getUUID());
		if (this.death.getTarget() != null) {
			cloud.setTarget(this.death.getTarget());
		}
		this.death.setCloudsSummoned(this.death.getCloudsSummoned() + 1);
	}

	@Override
	public boolean hasSummonedEnough() {
		return this.death.getCloudsSummoned() >= RatConfig.bdMaxCloudSpawns;
	}
}
