package com.github.alexthe666.rats.server.block;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@SuppressWarnings("deprecation")
public class RatQuarryPlatformBlock extends Block {

	private static final VoxelShape SHAPE = Block.box(0.0D, 13.0D, 0.0D, 16.0D, 16.0D, 16.0D);

	public RatQuarryPlatformBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return this.getShape(state, getter, pos, context);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		if (context instanceof EntityCollisionContext eContext) {
			if (eContext.getEntity() != null && !eContext.getEntity().isCrouching()) {
				return SHAPE;
			}
		}
		return Shapes.empty();
	}

	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDist) {
		if (!(entity instanceof TamedRat)) {
			super.fallOn(level, state, pos, entity, fallDist);
		}
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType type) {
		return false;
	}
}
