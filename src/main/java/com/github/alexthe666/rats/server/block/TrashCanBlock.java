package com.github.alexthe666.rats.server.block;

import com.github.alexthe666.rats.data.tags.RatsBlockTags;
import com.github.alexthe666.rats.registry.RatsBlockEntityRegistry;
import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.registry.RatsParticleRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.block.entity.TrashCanBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.*;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("deprecation")
public class TrashCanBlock extends BaseEntityBlock implements WorldlyContainerHolder {

	public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
	public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 7);
	public static final BooleanProperty OPEN = BooleanProperty.create("open");
	private static final VoxelShape OUTER_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 18.0D, 15.0D);
	private static final VoxelShape INNER_SHAPE = Block.box(1.1D, 1.0D, 1.1D, 14.9D, 18.0D, 14.9D);
	private static final VoxelShape COLLISION_SHAPE_OUTER = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 18.0D, 15.0D);
	private static final VoxelShape COLLISION_SHAPE_INNER = Block.box(3.0D, 1.0D, 3.0D, 13.0D, 18.0D, 13.0D);
	private static final VoxelShape LID = Block.box(0.0D, 18.0D, 0.0D, 16.0D, 20.0D, 16.0D);
	private static final VoxelShape CLOSED_SHAPE = Shapes.or(OUTER_SHAPE, LID).optimize();

	public TrashCanBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(OPEN, false).setValue(LEVEL, 0));
	}

	//the actual shape of the block. This is what the dirt interacts with.
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return state.getValue(OPEN) ? Shapes.join(OUTER_SHAPE, INNER_SHAPE, BooleanOp.ONLY_FIRST) : CLOSED_SHAPE;
	}

	//the player collision shape. This takes the overhang into account so the player has more limited movement.
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return context instanceof EntityCollisionContext ctx && ctx.getEntity() != null ? state.getValue(OPEN) ? Shapes.join(COLLISION_SHAPE_OUTER, COLLISION_SHAPE_INNER, BooleanOp.ONLY_FIRST) : CLOSED_SHAPE : state.getShape(getter, pos, context);
	}

	//the shape that appears when selecting the block. The normal shape looks kinda jank since it's only the walls, so we'll make this just one solid block.
	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return state.getValue(OPEN) ? OUTER_SHAPE : CLOSED_SHAPE;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
		if (!state.is(newState.getBlock())) {
			if (state.getValue(LEVEL) == 7 && !level.isClientSide()) {
				ItemEntity item = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.75D, pos.getZ() + 0.5D, new ItemStack(RatsBlockRegistry.GARBAGE_PILE.get()));
				item.setDefaultPickUpDelay();
				level.addFreshEntity(item);
			}
		}
		super.onRemove(state, level, pos, newState, moving);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);
		BlockEntity te = level.getBlockEntity(pos);

		if (!player.isCrouching()) {
			if (state.getValue(OPEN)) {
				if (state.getValue(LEVEL) == 7) {
					if (!level.isClientSide()) {
						ItemEntity item = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.75D, pos.getZ() + 0.5D, new ItemStack(RatsBlockRegistry.GARBAGE_PILE.get()));
						item.setDefaultPickUpDelay();
						level.addFreshEntity(item);
					}
					level.setBlockAndUpdate(pos, state.setValue(LEVEL, 0));
					level.playSound(null, pos, RatsSoundRegistry.TRASH_CAN_EMPTY.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
					return InteractionResult.sidedSuccess(level.isClientSide());
				} else if (state.getValue(LEVEL) < 7 && stack.getItem() instanceof BlockItem bi) {
					if (Objects.requireNonNull(ForgeRegistries.BLOCKS.tags()).getTag(RatsBlockTags.TRASH_CAN_BLACKLIST).contains(bi.getBlock())) {
						player.displayClientMessage(Component.literal("This block can't be used here.").withStyle(ChatFormatting.RED), true);
						return InteractionResult.CONSUME;
					}
					if (!player.isCreative()) {
						stack.shrink(1);
					}
					level.setBlockAndUpdate(pos, state.setValue(LEVEL, state.getValue(LEVEL) + 1));
					for (int i = 0; i < 100; i++) {
						level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, RatsBlockRegistry.GARBAGE_PILE.get().defaultBlockState()),
								pos.getX() + 0.4D + (level.getRandom().nextDouble() * 0.2D), pos.getY() + (((state.getValue(LEVEL) * 2) - 1.0D) * 0.1D), pos.getZ() + 0.4D + (level.getRandom().nextDouble() * 0.2D),
								0.0D, 0.0D, 0.0D);
					}
					level.playSound(null, pos, RatsSoundRegistry.TRASH_CAN_FILL.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
					return InteractionResult.SUCCESS;
				}
			}
		}
		if (player.isCrouching() || stack.isEmpty()) {
			if (te instanceof TrashCanBlockEntity trashCan) {
				if (trashCan.lidProgress == 0.0F || trashCan.lidProgress == 20.0F) {
					level.playSound(player, pos, RatsSoundRegistry.TRASH_CAN.get(), SoundSource.BLOCKS, 0.5F, 0.75F + level.getRandom().nextFloat() * 0.5F);

					level.setBlockAndUpdate(pos, state.setValue(OPEN, !state.getValue(OPEN)));
					return InteractionResult.SUCCESS;
				}
				return InteractionResult.PASS;
			}
		}


		return InteractionResult.PASS;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (state.getValue(OPEN) && state.getValue(LEVEL) > 0 && random.nextFloat() <= state.getValue(LEVEL) * 0.0025F) {
			double d0 = pos.getX() + random.nextFloat();
			double d1 = pos.getY() + 1.0D + random.nextFloat();
			double d2 = pos.getZ() + random.nextFloat();
			level.addParticle(RatsParticleRegistry.FLY.get(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable("block.rats.trash_can.desc0").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("block.rats.trash_can.desc1").withStyle(ChatFormatting.GRAY));
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType type) {
		return false;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, OPEN, LEVEL);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TrashCanBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, RatsBlockEntityRegistry.TRASH_CAN.get(), TrashCanBlockEntity::tick);
	}

	@Override
	public WorldlyContainer getContainer(BlockState state, LevelAccessor accessor, BlockPos pos) {
		return state.getValue(LEVEL) == 7 ? new OutputContainer(state, accessor, pos, new ItemStack(RatsBlockRegistry.GARBAGE_PILE.get())) : new InputContainer(state, accessor, pos);
	}

	static class InputContainer extends SimpleContainer implements WorldlyContainer {
		private final BlockState state;
		private final LevelAccessor level;
		private final BlockPos pos;
		private boolean changed;

		public InputContainer(BlockState state, LevelAccessor accessor, BlockPos pos) {
			super(1);
			this.state = state;
			this.level = accessor;
			this.pos = pos;
		}

		@Override
		public int getMaxStackSize() {
			return 1;
		}

		@Override
		public int[] getSlotsForFace(Direction direction) {
			return direction != Direction.DOWN ? new int[]{0} : new int[0];
		}

		@Override
		public boolean canPlaceItemThroughFace(int amount, ItemStack stack, @Nullable Direction direction) {
			return !this.changed && this.state.getValue(OPEN) && direction != Direction.DOWN && stack.getItem() instanceof BlockItem;
		}

		@Override
		public boolean canTakeItemThroughFace(int amount, ItemStack stack, Direction direction) {
			return false;
		}

		@Override
		public void setChanged() {
			ItemStack itemstack = this.getItem(0);
			if (!itemstack.isEmpty()) {
				this.changed = true;
				this.level.setBlock(this.pos, this.state.cycle(LEVEL), 3);
				this.removeItemNoUpdate(0);
			}

		}
	}

	static class OutputContainer extends SimpleContainer implements WorldlyContainer {
		private final BlockState state;
		private final LevelAccessor level;
		private final BlockPos pos;
		private boolean changed;

		public OutputContainer(BlockState state, LevelAccessor accessor, BlockPos pos, ItemStack stack) {
			super(stack);
			this.state = state;
			this.level = accessor;
			this.pos = pos;
		}

		@Override
		public int getMaxStackSize() {
			return 1;
		}

		@Override
		public int[] getSlotsForFace(Direction direction) {
			return direction == Direction.DOWN ? new int[]{0} : new int[0];
		}

		@Override
		public boolean canPlaceItemThroughFace(int amount, ItemStack stack, @Nullable Direction direction) {
			return false;
		}

		@Override
		public boolean canTakeItemThroughFace(int amount, ItemStack stack, Direction direction) {
			return !this.changed && direction == Direction.DOWN;
		}

		@Override
		public void setChanged() {
			this.level.setBlock(this.pos, this.state.setValue(LEVEL, 0), 3);
			this.changed = true;
		}
	}
}
