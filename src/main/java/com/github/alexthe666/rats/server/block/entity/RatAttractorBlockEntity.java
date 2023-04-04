package com.github.alexthe666.rats.server.block.entity;

import com.github.alexthe666.rats.registry.RatsBlockEntityRegistry;
import com.github.alexthe666.rats.server.block.RatAttractorBlock;
import com.github.alexthe666.rats.server.entity.rat.Rat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class RatAttractorBlockEntity extends BlockEntity {
	public int tickCount = 0;

	public RatAttractorBlockEntity(BlockPos pos, BlockState state) {
		super(RatsBlockEntityRegistry.RAT_ATTRACTOR.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, RatAttractorBlockEntity te) {
		boolean active = false;
		te.tickCount++;
		if (state.getBlock() instanceof RatAttractorBlock attractor) {
			attractor.updateState(state, level, pos);
			active = state.getValue(RatAttractorBlock.POWERED);
		}

		if (active) {
			float i = pos.getX() + 0.5F;
			float j = pos.getY();
			float k = pos.getZ() + 0.5F;
			if (level.isClientSide()) {
				float f1 = 0.6F + 0.4F;
				float f2 = Math.max(0.0F, 0.7F - 0.5F);
				float f3 = Math.max(0.0F, 0.6F - 0.7F);
				level.addParticle(DustParticleOptions.REDSTONE, i + (level.getRandom().nextDouble() - 0.5D) * 0.65D, j + 0.15D + (level.getRandom().nextDouble() - 0.5D), k + (level.getRandom().nextDouble() - 0.5D) * 0.65D, f1, f2, f3);
			} else {
				double d0 = 15;
				if (te.tickCount % 20 == 0 && level.getRandom().nextInt(3) == 0) {
					List<Rat> rats = level.getEntitiesOfClass(Rat.class, new AABB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0));
					for (Rat rat : rats) {
						if (!rat.isTame() && rat.getTarget() == null) {
							rat.getNavigation().moveTo(i, j, k, 1.0D);
						}
					}
				}
			}
		}
	}
}
