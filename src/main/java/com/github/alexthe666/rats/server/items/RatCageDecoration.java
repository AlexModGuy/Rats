package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.server.block.RatCageBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface RatCageDecoration {

	boolean canStay(Level world, BlockPos pos, RatCageBlock cageBlock);
}
