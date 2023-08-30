package com.github.alexthe666.rats.server.block;

import com.github.alexthe666.rats.server.block.entity.RatHoleBlockEntity;
import com.github.alexthe666.rats.server.entity.rat.DiggingRat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("deprecation")
public class RatHoleBlock extends BaseEntityBlock {

	public static final BooleanProperty NORTH = BooleanProperty.create("north");
	public static final BooleanProperty EAST = BooleanProperty.create("east");
	public static final BooleanProperty SOUTH = BooleanProperty.create("south");
	public static final BooleanProperty WEST = BooleanProperty.create("west");

	private static final VoxelShape TOP_AABB = Block.box(0, 8, 0, 16, 16, 16);
	private static final VoxelShape NS_LEFT_AABB = Block.box(0, 0, 0, 4, 8, 16);
	private static final VoxelShape NS_RIGHT_AABB = Block.box(12, 0, 0, 16, 8, 16);
	private static final VoxelShape EW_LEFT_AABB = Block.box(0, 0, 0, 16, 8, 4);
	private static final VoxelShape EW_RIGHT_AABB = Block.box(0, 0, 12, 16, 8, 16);
	private static final VoxelShape NORTH_CORNER_AABB = Block.box(0, 0, 0, 4, 8, 4);
	private static final VoxelShape EAST_CORNER_AABB = Block.box(12, 0, 0, 16, 8, 4);
	private static final VoxelShape SOUTH_CORNER_AABB = Block.box(0, 0, 12, 4, 8, 16);
	private static final VoxelShape WEST_CORNER_AABB = Block.box(12, 0, 12, 16, 8, 16);
	private VoxelShape shape;

	public RatHoleBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any()
				.setValue(NORTH, false)
				.setValue(EAST, false)
				.setValue(SOUTH, false)
				.setValue(WEST, false)
		);
		this.shape = Shapes.join(TOP_AABB, NORTH_CORNER_AABB, BooleanOp.OR).optimize();
		this.shape = Shapes.join(this.shape, SOUTH_CORNER_AABB, BooleanOp.OR).optimize();
		this.shape = Shapes.join(this.shape, EAST_CORNER_AABB, BooleanOp.OR).optimize();
		this.shape = Shapes.join(this.shape, WEST_CORNER_AABB, BooleanOp.OR).optimize();
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return context instanceof EntityCollisionContext ctx && ctx.getEntity() instanceof DiggingRat ? TOP_AABB : super.getCollisionShape(state, getter, pos, context);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Level level = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		BlockState northState = level.getBlockState(blockpos.north());
		BlockState eastState = level.getBlockState(blockpos.east());
		BlockState southState = level.getBlockState(blockpos.south());
		BlockState westState = level.getBlockState(blockpos.west());
		return Objects.requireNonNull(super.getStateForPlacement(context))
				.setValue(NORTH, this.shouldFaceHaveHole(northState, northState.isFaceSturdy(level, blockpos.north(), Direction.SOUTH)))
				.setValue(EAST, this.shouldFaceHaveHole(eastState, eastState.isFaceSturdy(level, blockpos.east(), Direction.WEST)))
				.setValue(SOUTH, this.shouldFaceHaveHole(southState, southState.isFaceSturdy(level, blockpos.south(), Direction.NORTH)))
				.setValue(WEST, this.shouldFaceHaveHole(westState, westState.isFaceSturdy(level, blockpos.west(), Direction.EAST)));
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		BlockEntity entity = builder.getParameter(LootContextParams.BLOCK_ENTITY);
		if (entity instanceof RatHoleBlockEntity hole) {
			return hole.getImitatedBlockState().getDrops(builder);
		}

		return super.getDrops(state, builder);
	}

	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		VoxelShape shape1 = this.shape;
		if (state.getBlock() instanceof RatHoleBlock) {
			if (state.getValue(NORTH)) {
				shape1 = Shapes.join(shape1, EW_LEFT_AABB, BooleanOp.OR);
			}
			if (state.getValue(SOUTH)) {
				shape1 = Shapes.join(shape1, EW_RIGHT_AABB, BooleanOp.OR);
			}
			if (state.getValue(WEST)) {
				shape1 = Shapes.join(shape1, NS_LEFT_AABB, BooleanOp.OR);
			}
			if (state.getValue(EAST)) {
				shape1 = Shapes.join(shape1, NS_RIGHT_AABB, BooleanOp.OR);
			}
		}
		return shape1;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, WEST, SOUTH);
	}

	private boolean shouldFaceHaveHole(BlockState state, boolean occluding) {
		return occluding || state.getBlock() == this;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RatHoleBlockEntity(pos, state);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		BooleanProperty connect = switch (facing) {
			case NORTH -> NORTH;
			case SOUTH -> SOUTH;
			case EAST -> EAST;
			case WEST -> WEST;
			default -> null;
		};
		if (connect == null) {
			return state;
		}
		return state.setValue(connect, this.shouldFaceHaveHole(facingState, facingState.isFaceSturdy(level, facingPos, facing.getOpposite())));
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
		if (level.getBlockEntity(pos) instanceof RatHoleBlockEntity hole) {
			return new ItemStack(hole.getImitatedBlockState().getBlock());
		}
		return new ItemStack(Blocks.OAK_PLANKS);
	}
}