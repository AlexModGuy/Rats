package com.github.alexthe666.rats.server.block.entity;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.registry.RatlantisBlockEntityRegistry;
import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatsParticleRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.monster.boss.Dutchrat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.ForgeEventFactory;

public class DutchratBellBlockEntity extends BlockEntity {
	public int ticks;
	public boolean shaking;
	public Direction clickDirection;
	private int ticksToExplode = -1;

	public DutchratBellBlockEntity(BlockPos pos, BlockState state) {
		super(RatlantisBlockEntityRegistry.DUTCHRAT_BELL.get(), pos, state);
	}

	@Override
	public boolean triggerEvent(int id, int type) {
		if (id == 1) {
			this.clickDirection = Direction.from2DDataValue(type);
			this.ticks = 0;
			this.shaking = true;
			return true;
		} else if (id == 2) {
			this.ticksToExplode = 0;
			return true;
		} else {
			return super.triggerEvent(id, type);
		}
	}

	public static void tick(Level level, BlockPos pos, BlockState state, DutchratBellBlockEntity te) {
		if (level.getCurrentDifficultyAt(pos).getDifficulty() == Difficulty.PEACEFUL) {
			te.ticksToExplode = -1;
		}

		if (te.shaking) {
			++te.ticks;
		}

		if (te.ticks >= 120) {
			te.shaking = false;
			te.ticks = 0;
		}

		if (te.ticks >= 5 && te.ticksToExplode == -1) {
			if (!level.isClientSide()) {
				if (level.isDay()) {
					AABB bb = new AABB(pos.getX() - 10, pos.getY() - 10, pos.getZ() - 10, pos.getX() + 10, pos.getY() + 10, pos.getZ() + 10);
					for (Player players : level.getEntitiesOfClass(Player.class, bb)) {
						players.displayClientMessage(Component.translatable("entity.rats.dutchrat.daytime"), true);
					}
				} else if (level.getCurrentDifficultyAt(pos).getDifficulty() != Difficulty.PEACEFUL) {
					te.ticksToExplode = 0;
					Dutchrat dutchrat = new Dutchrat(RatlantisEntityRegistry.DUTCHRAT.get(), level);
					dutchrat.setPos(pos.getX() + 0.5D, pos.getY() + 10.0D, pos.getZ() + 0.5D);
					dutchrat.restrictTo(pos, RatConfig.dutchratRestrictionRadius);
					dutchrat.setBellSummoned();
					ForgeEventFactory.onFinalizeSpawn(dutchrat, (ServerLevelAccessor) level, level.getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED, null, null);
					level.addFreshEntity(dutchrat);
					level.blockEvent(pos, state.getBlock(), 2, Direction.NORTH.get2DDataValue());
				}
			}
		}
		if (te.ticksToExplode > -1) {
			te.ticksToExplode++;
		}

		if (te.ticksToExplode > 0 && te.ticksToExplode <= 50) {
			for (int i = 0; i < 10; i++) {
				float angle = (0.01745329251F * (te.ticksToExplode * (te.ticksToExplode * 0.1F) + i * 36));
				double extraX = (Mth.abs(te.ticksToExplode - 50) * 0.25D) * Mth.sin(Mth.PI + angle) + pos.getX() + 0.5D;
				double extraZ = (Mth.abs(te.ticksToExplode - 50) * 0.25D) * Mth.cos(angle) + pos.getZ() + 0.5D;
				level.addParticle(RatsParticleRegistry.DUTCHRAT_SMOKE.get(), extraX, pos.getY() + 0.75D, extraZ, 24.0F, 0F, 0F);
			}
		} else if (te.ticksToExplode > 60 && te.ticksToExplode < 168) {
			level.addParticle(RatsParticleRegistry.DUTCHRAT_SMOKE.get(), pos.getX() + 0.5D, pos.getY() + 1 + te.ticksToExplode % 12, pos.getZ() + 0.5D, 12.0F, 0F, 0F);
		}

		if (te.ticksToExplode > 80 && te.ticksToExplode < 170) {
			for (int i = 0; i < Math.min((te.ticksToExplode - 75) / 10, 5); i++) {
				float angle = (0.01745329251F * (te.ticksToExplode * 20 + i * (360.0F / (i + 1))));
				double extraX = 2.0D * Mth.sin(Mth.PI + angle) + pos.getX() + 0.5D;
				double extraZ = 2.0D * Mth.cos(angle) + pos.getZ() + 0.5D;
				level.addParticle(RatsParticleRegistry.DUTCHRAT_SMOKE.get(), extraX, pos.getY() + 10 + i, extraZ, 12.0F, 0F, 0F);
			}
		}

		if (te.ticksToExplode == 160) {
			level.playSound(null, pos, RatsSoundRegistry.DUTCHRAT_LAUGH.get(), SoundSource.BLOCKS, 10.0F, 1.0F);
		}

		if (te.ticksToExplode == 180) {
			level.destroyBlock(pos, false);
		}
	}

	public void onHit(Level level, Direction direction) {
		BlockPos blockpos = this.getBlockPos();
		this.clickDirection = direction;
		if (this.shaking) {
			this.ticks = 0;
		} else {
			this.shaking = true;
		}

		level.blockEvent(blockpos, this.getBlockState().getBlock(), 1, direction.get2DDataValue());
	}

	public boolean canDestroyBell() {
		return this.ticksToExplode < 0;
	}
}