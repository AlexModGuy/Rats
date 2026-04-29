package com.github.alexthe666.rats.server.block;

import com.github.alexthe666.rats.registry.RatsBlockEntityRegistry;
import com.github.alexthe666.rats.server.block.entity.AutoCurdlerBlockEntity;
import com.github.alexthe666.rats.server.message.UpdateCurdlerFluidPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("deprecation")
public class AutoCurdlerBlock extends BaseEntityBlock {
	public static final com.mojang.serialization.MapCodec<AutoCurdlerBlock> CODEC = simpleCodec(AutoCurdlerBlock::new);

	@Override
	protected com.mojang.serialization.MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}


	public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
	private static final VoxelShape AABB_BASE = Block.box(1, 0, 1, 15, 8, 15);
	private static final VoxelShape AABB_TANK = Block.box(3, 8, 3, 13, 18, 13);
	private static final VoxelShape AABB = Shapes.or(AABB_BASE, AABB_TANK);

	public AutoCurdlerBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable("block.rats.auto_curdler.desc0").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("block.rats.auto_curdler.desc1").withStyle(ChatFormatting.GRAY));
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		BlockEntity be = level.getBlockEntity(pos);
		if (be instanceof AutoCurdlerBlockEntity curdler) {
			Containers.dropContents(level, pos, curdler);
			level.updateNeighbourForOutputSignal(pos, this);
		}
		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return AABB;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (player.isShiftKeyDown()) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}
		if (AutoCurdlerBlockEntity.isMilk(stack) && level.getBlockEntity(pos) instanceof AutoCurdlerBlockEntity te) {
			// 1.21: FluidUtil.getFluidHandler returns Optional<IFluidHandlerItem> directly (no LazyOptional/.resolve()).
			IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(stack).orElse(null);
			if (!level.isClientSide() && fluidHandler != null) {
				FluidStack fluidStack = FluidUtil.getFluidContained(stack).orElse(FluidStack.EMPTY);
				FluidStack drain = fluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
				if (drain.getAmount() > 0 || stack.is(Items.MILK_BUCKET)) {
					if (te.getTank().fill(fluidStack.copy(), IFluidHandler.FluidAction.SIMULATE) != 0) {
						int amount = te.getTank().fill(fluidStack.copy(), IFluidHandler.FluidAction.EXECUTE);
						level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
						if (!player.isCreative()) {
							fluidHandler.drain(amount, IFluidHandler.FluidAction.EXECUTE);
							ItemStack container = fluidHandler.getContainer();
							if (stack != container) {
								stack.shrink(1);
								player.getInventory().add(container);
							} else if (stack.is(Items.MILK_BUCKET)) {
								stack.shrink(1);
								player.getInventory().add(new ItemStack(Items.BUCKET));
							}
						}
						PacketDistributor.sendToAllPlayers(new UpdateCurdlerFluidPacket(pos.asLong(), te.getTank().getFluid()));
					}
				}
			}
			return ItemInteractionResult.sidedSuccess(level.isClientSide());
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (player.isShiftKeyDown()) {
			return InteractionResult.PASS;
		}
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		player.openMenu(this.getMenuProvider(state, level, pos));
		return InteractionResult.CONSUME;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new AutoCurdlerBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, RatsBlockEntityRegistry.AUTO_CURDLER.get(), AutoCurdlerBlockEntity::tick);
	}
}
