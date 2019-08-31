package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.server.blocks.BlockRatCage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IRatCageDecoration {

    boolean canStay(World world, BlockPos pos, BlockRatCage cageBlock);
}
