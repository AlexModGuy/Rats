package com.github.alexthe666.rats.server.block;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.data.tags.RatsBlockTags;
import com.github.alexthe666.rats.registry.RatlantisBlockEntityRegistry;
import com.github.alexthe666.rats.registry.RatlantisBlockRegistry;
import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.worldgen.RatlantisDimensionRegistry;
import com.github.alexthe666.rats.server.block.entity.RatlanteanAutomatonHeadBlockEntity;
import com.github.alexthe666.rats.server.entity.monster.boss.RatlanteanAutomaton;
import com.github.alexthe666.rats.server.misc.RatsLangConstants;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Predicate;

@SuppressWarnings("deprecation")
public class RatlanteanAutomatonHeadBlock extends BaseEntityBlock implements WearableOnHead, CustomItemRarity {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, Block.box(4.0D, 5.0D, 0.0D, 12.0D, 14.0D, 10.0D),
			Direction.EAST, Block.box(6.0D, 5.0D, 4.0D, 16.0D, 14.0D, 12.0D),
			Direction.SOUTH, Block.box(4.0D, 5.0D, 6.0D, 12.0D, 14.0D, 16.0D),
			Direction.WEST, Block.box(0.0D, 5.0D, 4.0D, 10.0D, 14.0D, 12.0D)));

	private static final Predicate<BlockState> IS_MARBLE = state -> state != null && state.is(RatsBlockTags.MARBLED_CHEESE);
	private static BlockPattern golemBasePattern;
	private static BlockPattern golemPattern;

	public RatlanteanAutomatonHeadBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return SHAPES.get(state.getValue(FACING));
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public Rarity getRarity() {
		return RatsMod.RATLANTIS_SPECIAL;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moving) {
		if (oldState.getBlock() != state.getBlock()) {
			trySpawnGolem(level, pos);
		}
	}

	public static boolean canSpawnGolem(Level level, BlockPos pos) {
		if (pos.getY() >= level.getMinBuildHeight() + 2 && level.getDifficulty() != Difficulty.PEACEFUL && !level.isClientSide()) {
			return getGolemBasePattern().find(level, pos) != null;
		} else {
			return false;
		}
	}

	public static void trySpawnGolem(Level level, BlockPos pos) {
		if (level.getCurrentDifficultyAt(pos).getDifficulty() == Difficulty.PEACEFUL) return;
		if (RatConfig.summonAutomatonOnlyInRatlantis && !level.dimension().equals(RatlantisDimensionRegistry.DIMENSION_KEY)) {
			for (Player player : level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(16.0D))) {
				player.displayClientMessage(Component.translatable(RatsLangConstants.AUTOMATON_RATLANTIS_ONLY), true);
			}
			return;
		}
		BlockPattern.BlockPatternMatch matcher = getGolemPattern().find(level, pos);

		if (matcher != null) {
			for (int j = 0; j < getGolemPattern().getWidth(); ++j) {
				for (int k = 0; k < getGolemPattern().getHeight(); ++k) {
					if (!matcher.getBlock(j, k, 0).getState().canBeReplaced()) {
						level.setBlock(matcher.getBlock(j, k, 0).getPos(), Blocks.AIR.defaultBlockState(), 2);
					}
				}
			}

			BlockPos blockpos = matcher.getBlock(1, 2, 0).getPos();
			RatlanteanAutomaton automaton = new RatlanteanAutomaton(RatlantisEntityRegistry.RATLANTEAN_AUTOMATON.get(), level);
			automaton.moveTo((double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.05D, (double) blockpos.getZ() + 0.5D, 0.0F, 0.0F);
			level.addFreshEntity(automaton);
			for (ServerPlayer player : level.getEntitiesOfClass(ServerPlayer.class, automaton.getBoundingBox().inflate(5.0D))) {
				CriteriaTriggers.SUMMONED_ENTITY.trigger(player, automaton);
			}

			for (int i1 = 0; i1 < getGolemPattern().getWidth(); ++i1) {
				for (int j1 = 0; j1 < getGolemPattern().getHeight(); ++j1) {
					BlockInWorld block = matcher.getBlock(i1, j1, 0);
					level.sendBlockUpdated(block.getPos(), block.getState(), block.getState(), 4);
				}
			}
		}
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	protected static BlockPattern getGolemBasePattern() {
		if (golemBasePattern == null) {
			golemBasePattern = BlockPatternBuilder.start().aisle("~ ~", "#X#", "~#~")
					.where('#', BlockInWorld.hasState(IS_MARBLE))
					.where('~', BlockInWorld.hasState(BlockStateBase::canBeReplaced))
					.where('X', BlockInWorld.hasState(BlockStatePredicate.forBlock(RatlantisBlockRegistry.MARBLED_CHEESE_GOLEM_CORE.get()))).build();
		}

		return golemBasePattern;
	}

	protected static BlockPattern getGolemPattern() {
		if (golemPattern == null) {
			golemPattern = BlockPatternBuilder.start().aisle("~^~", "#X#", "~#~")
					.where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD.get())))
					.where('#', BlockInWorld.hasState(IS_MARBLE))
					.where('~', BlockInWorld.hasState(BlockStateBase::canBeReplaced))
					.where('X', BlockInWorld.hasState(BlockStatePredicate.forBlock(RatlantisBlockRegistry.MARBLED_CHEESE_GOLEM_CORE.get()))).build();
		}

		return golemPattern;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RatlanteanAutomatonHeadBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, RatlantisBlockEntityRegistry.AUTOMATON_HEAD.get(), RatlanteanAutomatonHeadBlockEntity::tick);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType type) {
		return false;
	}
}
