package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.server.block.RatCageBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public interface RatCageDecoration {

	Direction getSupportedFace(Direction inputDir);
}
