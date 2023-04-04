package com.github.alexthe666.rats.server.block.entity;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.registry.RatsBlockEntityRegistry;
import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.registry.RatsParticleRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MilkCauldronBlockEntity extends BlockEntity {
	int tickCount;

	public MilkCauldronBlockEntity(BlockPos pos, BlockState state) {
		super(RatsBlockEntityRegistry.MILK_CAULDRON.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, MilkCauldronBlockEntity te) {
		te.tickCount++;
		if (RatConfig.cheesemaking && te.tickCount >= RatConfig.milkCauldronTime) {
			level.setBlockAndUpdate(pos, RatsBlockRegistry.CHEESE_CAULDRON.get().defaultBlockState());
			level.playSound(null, pos, RatsSoundRegistry.CHEESE_MADE.get(), SoundSource.BLOCKS, 1, 1);
			if (level.isClientSide()) {
				for (int i = 0; i < 10; i++) {
					level.addParticle(RatsParticleRegistry.MILK_BUBBLE.get(),
							pos.getX() + level.getRandom().nextFloat(),
							pos.getY() + 0.9375F,
							pos.getZ() + level.getRandom().nextFloat(),
							0.0D, 0.0D, 0.0D);
				}
			}
		}
	}

	@Override
	public void saveAdditional(CompoundTag compound) {
		compound.putInt("TicksExisted", tickCount);
		super.saveAdditional(compound);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		tickCount = compound.getInt("TicksExisted");
	}

}
