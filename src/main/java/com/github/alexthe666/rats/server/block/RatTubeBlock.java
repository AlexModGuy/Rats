package com.github.alexthe666.rats.server.block;

import com.github.alexthe666.rats.registry.RatsBlockEntityRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.block.entity.RatTubeBlockEntity;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.RatTubeItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SuppressWarnings("deprecation")
public class RatTubeBlock extends BaseEntityBlock {

	public static final BooleanProperty NORTH = BooleanProperty.create("north");
	public static final BooleanProperty EAST = BooleanProperty.create("east");
	public static final BooleanProperty SOUTH = BooleanProperty.create("south");
	public static final BooleanProperty WEST = BooleanProperty.create("west");
	public static final BooleanProperty UP = BooleanProperty.create("up");
	public static final BooleanProperty DOWN = BooleanProperty.create("down");

	public static final BooleanProperty OPEN_NORTH = BooleanProperty.create("open_north");
	public static final BooleanProperty OPEN_EAST = BooleanProperty.create("open_east");
	public static final BooleanProperty OPEN_SOUTH = BooleanProperty.create("open_south");
	public static final BooleanProperty OPEN_WEST = BooleanProperty.create("open_west");
	public static final BooleanProperty OPEN_UP = BooleanProperty.create("open_up");
	public static final BooleanProperty OPEN_DOWN = BooleanProperty.create("open_down");
	public static final BooleanProperty[] ALL_OPEN_PROPS = new BooleanProperty[]{OPEN_DOWN, OPEN_UP, OPEN_NORTH, OPEN_SOUTH, OPEN_WEST, OPEN_EAST};

	private static final VoxelShape UP_AABB = Block.box(3, 12, 3, 13, 13, 13);
	private static final VoxelShape UP_AABB_CONNECT_1 = Block.box(3, 13, 3, 13, 16, 4);
	private static final VoxelShape UP_AABB_CONNECT_2 = Block.box(3, 13, 3, 4, 16, 13);
	private static final VoxelShape UP_AABB_CONNECT_3 = Block.box(12, 13, 3, 13, 16, 13);
	private static final VoxelShape UP_AABB_CONNECT_4 = Block.box(3, 13, 12, 13, 16, 13);

	//AABB maps to stop mem leaks
	private static final Map<Direction, VoxelShape> AABB_MAP = new HashMap<>();
	private static final Map<Direction, VoxelShape> AABB_CONNECTOR_1_MAP = new HashMap<>();
	private static final Map<Direction, VoxelShape> AABB_CONNECTOR_2_MAP = new HashMap<>();
	private static final Map<Direction, VoxelShape> AABB_CONNECTOR_3_MAP = new HashMap<>();
	private static final Map<Direction, VoxelShape> AABB_CONNECTOR_4_MAP = new HashMap<>();
	private static final Map<String, VoxelShape> OVERALL_AABB_MAP = new HashMap<>();

	public RatTubeBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any()
				.setValue(NORTH, false)
				.setValue(EAST, false)
				.setValue(SOUTH, false)
				.setValue(WEST, false)
				.setValue(UP, false)
				.setValue(DOWN, false)
				.setValue(OPEN_NORTH, false)
				.setValue(OPEN_EAST, false)
				.setValue(OPEN_SOUTH, false)
				.setValue(OPEN_WEST, false)
				.setValue(OPEN_UP, false)
				.setValue(OPEN_DOWN, false)
		);
	}

	public static VoxelShape rotateAABB(VoxelShape aabb, Direction facing) {
		return switch (facing) {
			case UP -> aabb;
			case DOWN ->
					makeCuboidShapeNoResize(aabb.bounds().minX, 1 - aabb.bounds().maxY, aabb.bounds().minZ, aabb.bounds().maxX, 1 - aabb.bounds().minY, aabb.bounds().maxZ);
			case NORTH ->
					makeCuboidShapeNoResize(aabb.bounds().minX, aabb.bounds().minZ, 1 - aabb.bounds().maxY, aabb.bounds().maxX, aabb.bounds().maxZ, 1 - aabb.bounds().minY);
			case SOUTH ->
					makeCuboidShapeNoResize(aabb.bounds().minX, aabb.bounds().minZ, aabb.bounds().minY, aabb.bounds().maxX, aabb.bounds().maxZ, aabb.bounds().maxY);
			case EAST ->
					makeCuboidShapeNoResize(aabb.bounds().minY, aabb.bounds().minX, aabb.bounds().minZ, aabb.bounds().maxY, aabb.bounds().maxX, aabb.bounds().maxZ);
			case WEST ->
					makeCuboidShapeNoResize(1 - aabb.bounds().maxY, aabb.bounds().minX, aabb.bounds().minZ, 1 - aabb.bounds().minY, aabb.bounds().maxX, aabb.bounds().maxZ);
		};
	}

	public static VoxelShape makeCuboidShapeNoResize(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Shapes.create(x1, y1, z1, x2, y2, z2);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		if (state.getBlock() instanceof RatTubeBlock) {
			String key = "up:" + state.getValue(UP) + " down: " + state.getValue(DOWN) + " north: " + state.getValue(NORTH) +
					" east: " + state.getValue(EAST) + " south: " + state.getValue(SOUTH) + " west: " + state.getValue(WEST) +
					"up_open:" + state.getValue(OPEN_UP) + " down_open: " + state.getValue(OPEN_DOWN) + " north_open: " + state.getValue(OPEN_NORTH) +
					" east_open: " + state.getValue(OPEN_EAST) + " south_open: " + state.getValue(OPEN_SOUTH) + " west_open: " + state.getValue(OPEN_WEST);
			if (OVERALL_AABB_MAP.get(key) == null) {
				VoxelShape shape = generateRatTubeState(state);
				OVERALL_AABB_MAP.put(key, shape);
				return shape;
			} else {
				return OVERALL_AABB_MAP.get(key);
			}
		}
		return super.getShape(state, level, pos, context);
	}

	public VoxelShape generateRatTubeState(BlockState state) {
		VoxelShape shape = Block.box(0, 0, 0, 0, 0, 0);
		if (state.getBlock() instanceof RatTubeBlock) {
			if (!state.getValue(UP) && !state.getValue(OPEN_UP)) {
				shape = Shapes.join(shape, rotateWithMap(UP_AABB, Direction.UP, AABB_MAP), BooleanOp.OR);
			}
			if (!state.getValue(DOWN) && !state.getValue(OPEN_DOWN)) {
				shape = Shapes.join(shape, rotateWithMap(UP_AABB, Direction.DOWN, AABB_MAP), BooleanOp.OR);
			}
			if (!state.getValue(NORTH) && !state.getValue(OPEN_NORTH)) {
				shape = Shapes.join(shape, rotateWithMap(UP_AABB, Direction.NORTH, AABB_MAP), BooleanOp.OR);
			}
			if (!state.getValue(SOUTH) && !state.getValue(OPEN_SOUTH)) {
				shape = Shapes.join(shape, rotateWithMap(UP_AABB, Direction.SOUTH, AABB_MAP), BooleanOp.OR);
			}
			if (!state.getValue(EAST) && !state.getValue(OPEN_EAST)) {
				shape = Shapes.join(shape, rotateWithMap(UP_AABB, Direction.EAST, AABB_MAP), BooleanOp.OR);
			}
			if (!state.getValue(WEST) && !state.getValue(OPEN_WEST)) {
				shape = Shapes.join(shape, rotateWithMap(UP_AABB, Direction.WEST, AABB_MAP), BooleanOp.OR);
			}

			if (state.getValue(UP)) {
				shape = Shapes.join(shape, rotateWithMap(UP_AABB_CONNECT_1, Direction.UP, AABB_CONNECTOR_1_MAP), BooleanOp.OR);
				shape = Shapes.join(shape, rotateWithMap(UP_AABB_CONNECT_2, Direction.UP, AABB_CONNECTOR_2_MAP), BooleanOp.OR);
				shape = Shapes.join(shape, rotateWithMap(UP_AABB_CONNECT_3, Direction.UP, AABB_CONNECTOR_3_MAP), BooleanOp.OR);
				shape = Shapes.join(shape, rotateWithMap(UP_AABB_CONNECT_4, Direction.UP, AABB_CONNECTOR_4_MAP), BooleanOp.OR);
			}
			if (state.getValue(DOWN)) {
				shape = Shapes.join(shape, rotateWithMap(UP_AABB_CONNECT_1, Direction.DOWN, AABB_CONNECTOR_1_MAP), BooleanOp.OR);
				shape = Shapes.join(shape, rotateWithMap(UP_AABB_CONNECT_2, Direction.DOWN, AABB_CONNECTOR_2_MAP), BooleanOp.OR);
				shape = Shapes.join(shape, rotateWithMap(UP_AABB_CONNECT_3, Direction.DOWN, AABB_CONNECTOR_3_MAP), BooleanOp.OR);
				shape = Shapes.join(shape, rotateWithMap(UP_AABB_CONNECT_4, Direction.DOWN, AABB_CONNECTOR_4_MAP), BooleanOp.OR);
			}
			if (state.getValue(NORTH)) {
				shape = Shapes.join(shape, rotateWithMap(UP_AABB_CONNECT_1, Direction.NORTH, AABB_CONNECTOR_1_MAP), BooleanOp.OR);
				shape = Shapes.join(shape, rotateWithMap(UP_AABB_CONNECT_2, Direction.NORTH, AABB_CONNECTOR_2_MAP), BooleanOp.OR);
				shape = Shapes.join(shape, rotateWithMap(UP_AABB_CONNECT_3, Direction.NORTH, AABB_CONNECTOR_3_MAP), BooleanOp.OR);
				shape = Shapes.join(shape, rotateWithMap(UP_AABB_CONNECT_4, Direction.NORTH, AABB_CONNECTOR_4_MAP), BooleanOp.OR);
			}
			if (state.getValue(SOUTH)) {
				shape = Shapes.join(shape, rotateWithMap(UP_AABB_CONNECT_1, Direction.SOUTH, AABB_CONNECTOR_1_MAP), BooleanOp.OR);
				shape = Shapes.join(shape, rotateWithMap(UP_AABB_CONNECT_2, Direction.SOUTH, AABB_CONNECTOR_2_MAP), BooleanOp.OR);
				shape = Shapes.join(shape, rotateWithMap(UP_AABB_CONNECT_3, Direction.SOUTH, AABB_CONNECTOR_3_MAP), BooleanOp.OR);
				shape = Shapes.join(shape, rotateWithMap(UP_AABB_CONNECT_4, Direction.SOUTH, AABB_CONNECTOR_4_MAP), BooleanOp.OR);
			}
			if (state.getValue(EAST)) {
				shape = Shapes.join(shape, rotateWithMap(UP_AABB_CONNECT_1, Direction.EAST, AABB_CONNECTOR_1_MAP), BooleanOp.OR);
				shape = Shapes.join(shape, rotateWithMap(UP_AABB_CONNECT_2, Direction.EAST, AABB_CONNECTOR_2_MAP), BooleanOp.OR);
				shape = Shapes.join(shape, rotateWithMap(UP_AABB_CONNECT_3, Direction.EAST, AABB_CONNECTOR_3_MAP), BooleanOp.OR);
				shape = Shapes.join(shape, rotateWithMap(UP_AABB_CONNECT_4, Direction.EAST, AABB_CONNECTOR_4_MAP), BooleanOp.OR);
			}
			if (state.getValue(WEST)) {
				shape = Shapes.join(shape, rotateWithMap(UP_AABB_CONNECT_1, Direction.WEST, AABB_CONNECTOR_1_MAP), BooleanOp.OR);
				shape = Shapes.join(shape, rotateWithMap(UP_AABB_CONNECT_2, Direction.WEST, AABB_CONNECTOR_2_MAP), BooleanOp.OR);
				shape = Shapes.join(shape, rotateWithMap(UP_AABB_CONNECT_3, Direction.WEST, AABB_CONNECTOR_3_MAP), BooleanOp.OR);
				shape = Shapes.join(shape, rotateWithMap(UP_AABB_CONNECT_4, Direction.WEST, AABB_CONNECTOR_4_MAP), BooleanOp.OR);
			}
		}
		return shape.optimize();
	}

	public VoxelShape rotateWithMap(VoxelShape aabb, Direction facing, Map<Direction, VoxelShape> checkMap) {
		if (checkMap.get(facing) == null) {
			VoxelShape newAABB = rotateAABB(aabb, facing);
			checkMap.put(facing, newAABB);
			return newAABB;
		} else {
			return checkMap.get(facing);
		}
	}

	private boolean canBeOpenNextToBlock(BlockState sideState) {
		return !(sideState.getBlock() instanceof RatTubeBlock);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN, OPEN_NORTH, OPEN_SOUTH, OPEN_EAST, OPEN_WEST, OPEN_UP, OPEN_DOWN);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockGetter getter = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		BlockPos blockpos1 = blockpos.north();
		BlockPos blockpos2 = blockpos.east();
		BlockPos blockpos3 = blockpos.south();
		BlockPos blockpos4 = blockpos.west();
		BlockPos blockpos5 = blockpos.above();
		BlockPos blockpos6 = blockpos.below();
		BlockState blockstate = getter.getBlockState(blockpos1);
		BlockState blockstate1 = getter.getBlockState(blockpos2);
		BlockState blockstate2 = getter.getBlockState(blockpos3);
		BlockState blockstate3 = getter.getBlockState(blockpos4);
		BlockState blockstate4 = getter.getBlockState(blockpos5);
		BlockState blockstate5 = getter.getBlockState(blockpos6);
		return Objects.requireNonNull(super.getStateForPlacement(context))
				.setValue(NORTH, this.canConnectTo(blockstate))
				.setValue(EAST, this.canConnectTo(blockstate1))
				.setValue(SOUTH, this.canConnectTo(blockstate2))
				.setValue(WEST, this.canConnectTo(blockstate3))
				.setValue(UP, this.canConnectTo(blockstate4))
				.setValue(DOWN, this.canConnectTo(blockstate5));
	}

	private boolean canConnectTo(BlockState state) {
		return state.getBlock() instanceof RatCageBlock || state.getBlock() instanceof RatTubeBlock;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (player.isCrouching() || player.getItemInHand(hand).getItem() instanceof BlockItem || player.getItemInHand(hand).getItem() instanceof RatTubeItem) {
			return InteractionResult.PASS;
		} else {
			Direction side = hit.getDirection();
			BooleanProperty changing;
			BooleanProperty[] allOpenVars = new BooleanProperty[]{OPEN_DOWN, OPEN_EAST, OPEN_NORTH, OPEN_SOUTH, OPEN_UP, OPEN_WEST};
			changing = switch (side) {
				case NORTH -> OPEN_NORTH;
				case SOUTH -> OPEN_SOUTH;
				case EAST -> OPEN_EAST;
				case WEST -> OPEN_WEST;
				case DOWN -> OPEN_DOWN;
				default -> OPEN_UP;
			};
			boolean alreadyOpened = false;
			for (BooleanProperty opened : allOpenVars) {
				if (state.getValue(opened)) {
					alreadyOpened = true;
				}
			}
			if (!alreadyOpened && canBeOpenNextToBlock(level.getBlockState(pos.relative(side)))) {
				level.setBlockAndUpdate(pos, state.setValue(changing, true));
				updateTEOpening(level, pos, side, true);

			} else {
				level.setBlockAndUpdate(pos, state.setValue(OPEN_NORTH, false)
						.setValue(OPEN_EAST, false)
						.setValue(OPEN_SOUTH, false)
						.setValue(OPEN_WEST, false)
						.setValue(OPEN_UP, false)
						.setValue(OPEN_DOWN, false));
				updateTEOpening(level, pos, side, false);
			}
			return InteractionResult.SUCCESS;
		}
	}

	private void updateTEOpening(Level world, BlockPos pos, Direction side, boolean open) {
		if (world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof RatTubeBlockEntity tube) {
			tube.setEntranceData(side, open);
		}
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity living, ItemStack stack) {
		if (level.getBlockEntity(pos) != null && level.getBlockEntity(pos) instanceof RatTubeBlockEntity tube) {
			tube.setColor(getColorFromStack(stack));
		}
	}

	@Override
	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		BlockEntity tileentity = level.getBlockEntity(pos);
		if (tileentity instanceof RatTubeBlockEntity && !player.isCreative()) {
			Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), getTubeItem(level, pos));
		}
		super.playerWillDestroy(level, pos, state, player);
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter getter, BlockPos pos, Player player) {
		return getTubeItem(getter, pos);
	}

	private static ItemStack getTubeItem(BlockGetter level, BlockPos pos) {
		int color = 0;
		if (level.getBlockEntity(pos) != null && level.getBlockEntity(pos) instanceof RatTubeBlockEntity tube) {
			color = tube.getColor();
		}
		Item item = RatsItemRegistry.RAT_TUBES[Mth.clamp(color, 0, 15)].get();
		return new ItemStack(item);
	}

	private int getColorFromStack(ItemStack stack) {
		if (stack.getItem() instanceof RatTubeItem tube) {
			return tube.color.ordinal();
		}
		return 0;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RatTubeBlockEntity(pos, state);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		BooleanProperty notOpen;
		BooleanProperty connect;
		switch (facing) {
			case NORTH -> {
				notOpen = OPEN_NORTH;
				connect = NORTH;
			}
			case SOUTH -> {
				notOpen = OPEN_SOUTH;
				connect = SOUTH;
			}
			case EAST -> {
				notOpen = OPEN_EAST;
				connect = EAST;
			}
			case WEST -> {
				notOpen = OPEN_WEST;
				connect = WEST;
			}
			case DOWN -> {
				notOpen = OPEN_DOWN;
				connect = DOWN;
			}
			default -> {
				notOpen = OPEN_UP;
				connect = UP;
			}
		}
		BlockState newState = state;
		newState = newState.setValue(connect, this.canConnectTo(facingState));
		newState = newState.setValue(notOpen, false);
		return newState;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, RatsBlockEntityRegistry.RAT_TUBE.get(), RatTubeBlockEntity::tick);
	}

	@Override
	public @Nullable BlockPathTypes getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
		if (mob instanceof TamedRat rat) {
			return rat.isInTube() || rat.isInCage() ? BlockPathTypes.WALKABLE : BlockPathTypes.BLOCKED;
		}
		return BlockPathTypes.BLOCKED;
	}
}
