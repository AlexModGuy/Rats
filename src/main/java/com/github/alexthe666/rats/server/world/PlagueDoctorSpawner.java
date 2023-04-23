package com.github.alexthe666.rats.server.world;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import com.github.alexthe666.rats.server.entity.rat.Rat;
import com.github.alexthe666.rats.server.entity.misc.PlagueDoctor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PlagueDoctorSpawner implements CustomSpawner {
	private int tickDelay;
	private int spawnDelay;
	private int spawnChance;

	public PlagueDoctorSpawner(ServerLevel level) {
		this.tickDelay = 1200;
		PlagueDoctorWorldData worldinfo = PlagueDoctorWorldData.get(level);
		this.spawnDelay = worldinfo.getDoctorSpawnDelay();
		this.spawnChance = worldinfo.getDoctorSpawnChance();
		if (this.spawnDelay == 0 && this.spawnChance == 0) {
			this.spawnDelay = 24000;
			worldinfo.setDoctorSpawnDelay(this.spawnDelay);
			this.spawnChance = 25;
			worldinfo.setDoctorSpawnChance(this.spawnChance);
		}

	}

	@Override
	public int tick(ServerLevel level, boolean spawnEnemies, boolean spawnFriendlies) {
		if (level.getGameRules().getBoolean(RatsMod.SPAWN_PLAGUE_DOCTORS) && this.tickDelay-- <= 0) {
			this.tickDelay = 1200;
			PlagueDoctorWorldData data = PlagueDoctorWorldData.get(level);
			if (data != null) {
				this.spawnDelay -= 1200;
				data.setDoctorSpawnDelay(this.spawnDelay);
				if (this.spawnDelay <= 0) {
					this.spawnDelay = 24000;
					if (level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
						int i = this.spawnChance;
						this.spawnChance = Mth.clamp(this.spawnChance + 25, 25, 75);
						data.setDoctorSpawnChance(this.spawnChance);
						if (level.getRandom().nextInt(100) <= i && this.canSpawnDoctor(level)) {
							this.spawnChance = 25;
							data.setDirty();
							return 1;
						}
					}
				}
			}
		}
		return 0;
	}

	private boolean canSpawnDoctor(ServerLevel level) {
		Player player = level.getRandomPlayer();
		if (player == null) {
			return true;
		} else if (level.getRandom().nextInt(5) != 0) {
			return false;
		} else {
			BlockPos blockpos = player.blockPosition();
			PoiManager poimanager = level.getPoiManager();
			Optional<BlockPos> optional = poimanager.find(poiTypeHolder -> poiTypeHolder.is(PoiTypes.MEETING), pos -> true, blockpos, 48, PoiManager.Occupancy.ANY);
			BlockPos blockpos1 = optional.orElse(blockpos);
			BlockPos blockpos2 = this.findNearbySpawnPos(level, blockpos1, 48);
			if (blockpos2 != null && this.hasSpace(level, blockpos2)) {
				if (level.getBiome(blockpos2).is(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS)) {
					return false;
				}

				PlagueDoctor plagueDoctor = RatsEntityRegistry.PLAGUE_DOCTOR.get().spawn(level, blockpos2, MobSpawnType.EVENT);
				if (plagueDoctor != null) {
					for (int j = 0; j < 2; ++j) {
						this.spawnARat(level, plagueDoctor);
					}
					PlagueDoctorWorldData data = PlagueDoctorWorldData.get(level);
					if (data != null) {
						data.setPlagueDoctorID(plagueDoctor.getUUID());
						plagueDoctor.setWillDespawn(true);
						plagueDoctor.setDespawnDelay(24000);
						plagueDoctor.setWanderTarget(blockpos1);
						plagueDoctor.restrictTo(blockpos1, 32);

						return true;
					}
				}
			}

			return false;
		}
	}

	private void spawnARat(ServerLevel level, PlagueDoctor doctor) {
		BlockPos blockpos = this.findNearbySpawnPos(level, doctor.blockPosition(), 4);
		if (blockpos != null) {
			Rat rat = RatsEntityRegistry.RAT.get().spawn(level, blockpos, MobSpawnType.EVENT);
			if (rat != null) {
				rat.setLeashedTo(doctor, true);
				rat.setPlagued(false);
				rat.setOwnerUUID(doctor.getUUID());
			}
		}
	}

	@Nullable
	private BlockPos findNearbySpawnPos(ServerLevel level, BlockPos pos, int distance) {
		BlockPos blockpos = null;

		for (int i = 0; i < 10; ++i) {
			int j = pos.getX() + level.getRandom().nextInt(distance * 2) - distance;
			int k = pos.getZ() + level.getRandom().nextInt(distance * 2) - distance;
			int l = level.getHeight(Heightmap.Types.WORLD_SURFACE, j, k);
			BlockPos blockpos1 = new BlockPos(j, l, k);
			if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, level, blockpos1, EntityType.WANDERING_TRADER)) {
				blockpos = blockpos1;
				break;
			}
		}

		return blockpos;
	}

	private boolean hasSpace(BlockGetter getter, BlockPos pos) {
		for (BlockPos blockpos : BlockPos.betweenClosed(pos, pos.offset(1, 2, 1))) {
			if (!getter.getBlockState(blockpos).getCollisionShape(getter, blockpos).isEmpty()) {
				return false;
			}
		}

		return true;
	}
}
