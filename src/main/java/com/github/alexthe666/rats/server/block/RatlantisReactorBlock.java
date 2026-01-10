package com.github.alexthe666.rats.server.block;

import com.github.alexthe666.rats.server.block.entity.RatlantisReactorBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class RatlantisReactorBlock extends BaseEntityBlock {

	public static final MapCodec<RatlantisReactorBlock> CODEC = simpleCodec(RatlantisReactorBlock::new);

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	public RatlantisReactorBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RatlantisReactorBlockEntity(pos, state);
	}
}







