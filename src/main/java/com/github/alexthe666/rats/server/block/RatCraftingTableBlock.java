package com.github.alexthe666.rats.server.block;

import com.github.alexthe666.rats.registry.RatsBlockEntityRegistry;
import com.github.alexthe666.rats.server.block.entity.RatCraftingTableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class RatCraftingTableBlock extends BaseEntityBlock {

	public RatCraftingTableBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		BlockEntity be = level.getBlockEntity(pos);
		if (be instanceof RatCraftingTableBlockEntity table) {
			table.bufferHandler.ifPresent(handler -> {
				for (int i = 0; i < handler.getSlots(); i++) {
					Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), handler.getStackInSlot(i));
				}
			});
			level.updateNeighbourForOutputSignal(pos, this);
		}
		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RatCraftingTableBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, RatsBlockEntityRegistry.RAT_CRAFTING_TABLE.get(), RatCraftingTableBlockEntity::tick);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
		BlockEntity be = level.getBlockEntity(pos);
		if (stack.hasCustomHoverName() && be instanceof RatCraftingTableBlockEntity table) {
			table.setCustomName(stack.getDisplayName());
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!player.isCrouching()) {
			if (level.isClientSide()) {
				return InteractionResult.SUCCESS;
			} else {
				BlockEntity be = level.getBlockEntity(pos);
				if (be instanceof RatCraftingTableBlockEntity table) {
					NetworkHooks.openScreen((ServerPlayer) player, table, pos);
					player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
					return InteractionResult.CONSUME;
				}
			}
		}
		return InteractionResult.FAIL;
	}
}
