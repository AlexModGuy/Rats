package com.github.alexthe666.rats.server.block;

import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.server.block.entity.DecoratedRatCageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class RatCageDecoratedBlock extends RatCageBlock implements EntityBlock {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	public RatCageDecoratedBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (level.getBlockEntity(pos) != null && level.getBlockEntity(pos) instanceof DecoratedRatCageBlockEntity decorated && state.getBlock() != newState.getBlock()) {
			if (!decorated.getContainedItem().isEmpty()) {
				ItemEntity ItemEntity = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, decorated.getContainedItem());
				if (!level.isClientSide()) {
					level.addFreshEntity(ItemEntity);
				}
			}
		}
		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(FACING));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new DecoratedRatCageBlockEntity(pos, state);
	}

	@SuppressWarnings("unchecked")
	@Nullable
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> levelType, BlockEntityType<E> registryType, BlockEntityTicker<? super E> ticker) {
		return registryType == levelType ? (BlockEntityTicker<A>) ticker : null;
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
		return new ItemStack(RatsBlockRegistry.RAT_CAGE.get());
	}
}
