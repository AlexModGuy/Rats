package com.github.alexthe666.rats.server.block.entity;

import com.github.alexthe666.rats.registry.RatlantisBlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RatlantisPortalBlockEntity extends TheEndPortalBlockEntity {

	public RatlantisPortalBlockEntity(BlockPos pos, BlockState state) {
		super(RatlantisBlockEntityRegistry.RATLANTIS_PORTAL.get(), pos, state);
	}

	@Override
	public boolean shouldRenderFace(Direction direction) {
		return this.getLevel() == null || Block.shouldRenderFace(this.getBlockState(), this.getLevel(), this.getBlockPos(), direction, this.getBlockPos().relative(direction));
	}
}