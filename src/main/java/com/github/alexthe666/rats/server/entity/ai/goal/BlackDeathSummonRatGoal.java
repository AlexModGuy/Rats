package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import com.github.alexthe666.rats.server.entity.monster.boss.BlackDeath;
import com.github.alexthe666.rats.server.entity.rat.Rat;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.event.ForgeEventFactory;

public class BlackDeathSummonRatGoal extends BlackDeathAbstractSummonGoal {
	public BlackDeathSummonRatGoal(BlackDeath death) {
		super(death);
	}

	@Override
	public int getAttackCooldown() {
		return 40;
	}

	@Override
	public void summonEntity() {
		this.death.level().broadcastEntityEvent(this.death, (byte) 82);

		Rat rat = new Rat(RatsEntityRegistry.RAT.get(), this.death.level());
		ForgeEventFactory.onFinalizeSpawn(rat, (ServerLevel) this.death.level(), this.death.level().getCurrentDifficultyAt(this.death.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
		rat.copyPosition(this.death);
		rat.setPlagued(true);
		this.death.level().addFreshEntity(rat);
		rat.setOwnerUUID(this.death.getUUID());
		if (this.death.getTarget() != null) {
			rat.setTarget(this.death.getTarget());
		}
		this.death.setRatsSummoned(this.death.getRatsSummoned() + 1);
	}

	@Override
	public boolean hasSummonedEnough() {
		return this.death.getRatsSummoned() >= RatConfig.bdMaxRatSpawns;
	}
}
