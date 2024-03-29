package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import com.github.alexthe666.rats.server.entity.monster.PlagueBeast;
import com.github.alexthe666.rats.server.entity.monster.boss.BlackDeath;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.ForgeEventFactory;

public class BlackDeathSummonBeastGoal extends BlackDeathAbstractSummonGoal {
	public BlackDeathSummonBeastGoal(BlackDeath death) {
		super(death);
	}

	@Override
	public boolean canUse() {
		if (this.death.getRatsSummoned() >= 15 && this.death.getCloudsSummoned() > 1) {
			return super.canUse();
		}
		return false;
	}

	@Override
	public int getAttackCooldown() {
		return 200;
	}

	@Override
	public void summonEntity() {
		PlagueBeast beast = new PlagueBeast(RatsEntityRegistry.PLAGUE_BEAST.get(), this.death.level());
		ForgeEventFactory.onFinalizeSpawn(beast, (ServerLevelAccessor) this.death.level(), this.death.level().getCurrentDifficultyAt(this.death.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
		beast.copyPosition(this.death);
		this.death.level().addFreshEntity(beast);
		beast.setOwnerId(this.death.getUUID());
		if (this.death.getTarget() != null) {
			beast.setTarget(this.death.getTarget());
		}
		this.death.setBeastsSummoned(this.death.getBeastsSummoned() + 1);
	}

	@Override
	public boolean hasSummonedEnough() {
		return this.death.getBeastsSummoned() >= RatConfig.bdMaxBeastSpawns;
	}
}
