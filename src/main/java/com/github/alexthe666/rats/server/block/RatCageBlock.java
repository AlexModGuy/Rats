package com.github.alexthe666.rats.server.block;

import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.block.entity.DecoratedRatCageBlockEntity;
import com.github.alexthe666.rats.server.block.entity.RatCageBreedingLanternBlockEntity;
import com.github.alexthe666.rats.server.block.entity.RatCageWheelBlockEntity;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.RatCageDecoration;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("deprecation")
public class RatCageBlock extends Block {

	public static final IntegerProperty NORTH = IntegerProperty.create("north", 0, 2);
	public static final IntegerProperty EAST = IntegerProperty.create("east", 0, 2);
	public static final IntegerProperty SOUTH = IntegerProperty.create("south", 0, 2);
	public static final IntegerProperty WEST = IntegerProperty.create("west", 0, 2);
	public static final IntegerProperty UP = IntegerProperty.create("up", 0, 2);
	public static final IntegerProperty DOWN = IntegerProperty.create("down", 0, 2);

	private static final VoxelShape BOTTOM_AABB = Block.box(0F, 0F, 0F, 16F, 2F, 16F);
	private static final VoxelShape TOP_AABB = Block.box(0F, 15F, 0F, 16F, 16F, 16F);
	private static final VoxelShape NORTH_AABB = Block.box(0F, 0F, 0F, 16F, 16F, 1F);
	private static final VoxelShape SOUTH_AABB = Block.box(0F, 0F, 15F, 16F, 16F, 16F);
	private static final VoxelShape EAST_AABB = Block.box(15F, 0F, 0F, 16F, 16F, 16F);
	private static final VoxelShape WEST_AABB = Block.box(0F, 0F, 0F, 1F, 16F, 16F);

	public RatCageBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any()
				.setValue(NORTH, 0)
				.setValue(EAST, 0)
				.setValue(SOUTH, 0)
				.setValue(WEST, 0)
				.setValue(UP, 0)
				.setValue(DOWN, 0)
		);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter getter, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable("block.rats.rat_cage.desc0").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("block.rats.rat_cage.desc1").withStyle(ChatFormatting.GRAY));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, WEST, SOUTH, DOWN, UP);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return Objects.requireNonNull(super.getStateForPlacement(context))
				.setValue(NORTH, this.canFenceConnectTo(context.getLevel().getBlockState(context.getClickedPos().north())))
				.setValue(EAST, this.canFenceConnectTo(context.getLevel().getBlockState(context.getClickedPos().east())))
				.setValue(SOUTH, this.canFenceConnectTo(context.getLevel().getBlockState(context.getClickedPos().south())))
				.setValue(WEST, this.canFenceConnectTo(context.getLevel().getBlockState(context.getClickedPos().west())));
	}

	public int canFenceConnectTo(BlockState state) {
		if (state.getBlock() instanceof RatTubeBlock) {
			return 2;
		}
		return state.getBlock() instanceof RatCageBlock ? 1 : 0;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		VoxelShape shape1 = Block.box(0, 0, 0, 0, 0, 0);
		if (state.getBlock() instanceof RatCageBlock) {
			if (state.getValue(UP) == 0) {
				shape1 = Shapes.join(shape1, TOP_AABB, BooleanOp.OR);
			}
			if (state.getValue(DOWN) == 0) {
				shape1 = Shapes.join(shape1, BOTTOM_AABB, BooleanOp.OR);
			}
			if (state.getValue(NORTH) == 0) {
				shape1 = Shapes.join(shape1, NORTH_AABB, BooleanOp.OR);
			}
			if (state.getValue(SOUTH) == 0) {
				shape1 = Shapes.join(shape1, SOUTH_AABB, BooleanOp.OR);
			}
			if (state.getValue(WEST) == 0) {
				shape1 = Shapes.join(shape1, WEST_AABB, BooleanOp.OR);
			}
			if (state.getValue(EAST) == 0) {
				shape1 = Shapes.join(shape1, EAST_AABB, BooleanOp.OR);
			}
		}
		return shape1;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (player.getItemInHand(hand).getItem() instanceof RatCageDecoration decoration && level.getBlockEntity(pos) == null) {
			if (decoration.canStay(level, pos, this)) {
				Direction limitedFacing = player.getDirection().getOpposite();
				if (player.getItemInHand(hand).is(RatsItemRegistry.RAT_BREEDING_LANTERN.get())) {
					BlockState pre = level.getBlockState(pos);
					BlockState decorated = RatsBlockRegistry.RAT_CAGE_BREEDING_LANTERN.get().withPropertiesOf(pre);
					level.setBlockAndUpdate(pos, decorated.setValue(RatCageDecoratedBlock.FACING, limitedFacing));
					RatCageBreedingLanternBlockEntity te = new RatCageBreedingLanternBlockEntity(pos, decorated);
					ItemStack added = new ItemStack(player.getItemInHand(hand).getItem(), 1);
					te.setContainedItem(added);
					level.setBlockEntity(te);
					if (!player.isCreative()) {
						player.getItemInHand(hand).shrink(1);
					}
				} else if (player.getItemInHand(hand).is(RatsItemRegistry.RAT_WHEEL.get())) {
					BlockState pre = level.getBlockState(pos);
					BlockState decorated = RatsBlockRegistry.RAT_CAGE_WHEEL.get().withPropertiesOf(pre);
					level.setBlockAndUpdate(pos, decorated.setValue(RatCageDecoratedBlock.FACING, limitedFacing));
					RatCageWheelBlockEntity te = new RatCageWheelBlockEntity(pos, decorated);
					ItemStack added = new ItemStack(player.getItemInHand(hand).getItem(), 1);
					te.setContainedItem(added);
					level.setBlockEntity(te);
					if (!player.isCreative()) {
						player.getItemInHand(hand).shrink(1);
					}
				} else {
					BlockState pre = level.getBlockState(pos);
					BlockState decorated = RatsBlockRegistry.RAT_CAGE_DECORATED.get().withPropertiesOf(pre);
					level.setBlockAndUpdate(pos, decorated.setValue(RatCageDecoratedBlock.FACING, limitedFacing));
					DecoratedRatCageBlockEntity te = new DecoratedRatCageBlockEntity(pos, decorated);
					ItemStack added = new ItemStack(player.getItemInHand(hand).getItem(), 1);
					te.setContainedItem(added);
					level.setBlockEntity(te);
					if (!player.isCreative()) {
						player.getItemInHand(hand).shrink(1);
					}
				}

				return InteractionResult.SUCCESS;
			}
		}
		if (level.getBlockEntity(pos) != null) {
			ItemStack stack = this.getContainedItem(level, pos);
			if (!stack.isEmpty() && player.isShiftKeyDown()) {
				BlockState pre = level.getBlockState(pos);
				BlockState decorated = RatsBlockRegistry.RAT_CAGE.get().defaultBlockState();
				decorated = decorated.getBlock().withPropertiesOf(pre);
				level.setBlock(pos, decorated, 3);
				level.setBlockAndUpdate(pos, decorated);
			}
		}
		if (player.getItemInHand(hand).isEmpty() && !player.isShiftKeyDown()) {
			boolean ridingRats = false;
			if (!player.getPassengers().isEmpty()) {
				for (Entity entity : player.getPassengers()) {
					if (entity instanceof TamedRat) {
						ridingRats = true;
						break;
					}
				}
			}
			int ratCount = 0;
			if (ridingRats) {
				for (Entity entity : player.getPassengers()) {
					if (entity instanceof TamedRat rat && !rat.isBaby()) {
						rat.stopRiding();
						rat.setPos(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
						rat.getNavigation().stop();
						ratCount++;
					}
				}
				player.displayClientMessage(Component.translatable("entity.rats.rat.cage.deposit", ratCount), true);
			} else {
				List<TamedRat> list = level.getEntitiesOfClass(TamedRat.class, new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1), rat -> !rat.isBaby() && rat.isOwnedBy(player));
				for (TamedRat rat : list) {
					rat.setPos(player.getX(), player.getY(), player.getZ());
					ratCount++;
				}
				player.displayClientMessage(Component.translatable("entity.rats.rat.cage.withdrawal", ratCount), true);
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	public ItemStack getContainedItem(Level level, BlockPos pos) {
		BlockEntity be = level.getBlockEntity(pos);
		if (be instanceof DecoratedRatCageBlockEntity decorated) {
			return decorated.getContainedItem();
		}
		return ItemStack.EMPTY;
	}

	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor accessor, BlockPos currentPos, BlockPos facingPos) {
		IntegerProperty connect = switch (facing) {
			case NORTH -> NORTH;
			case SOUTH -> SOUTH;
			case EAST -> EAST;
			case WEST -> WEST;
			case DOWN -> DOWN;
			default -> UP;
		};
		return state.setValue(connect, this.canFenceConnectTo(facingState));
	}

}
