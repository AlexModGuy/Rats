package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RatTubeItem extends Item {
	public final DyeColor color;

	public RatTubeItem(Item.Properties properties, DyeColor color) {
		super(properties);
		this.color = color;
	}

	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
		tooltip.add(Component.translatable("block.rats.rat_tube.desc0").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("block.rats.rat_tube.desc1").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("block.rats.rat_tube.desc2").withStyle(ChatFormatting.GRAY));
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		InteractionResult actionresulttype = this.tryPlace(new BlockPlaceContext(context));
		return actionresulttype != InteractionResult.SUCCESS && this.isEdible() ? this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult() : actionresulttype;
	}

	public InteractionResult tryPlace(BlockPlaceContext context) {
		if (!context.canPlace()) {
			return InteractionResult.FAIL;
		} else {

			BlockState placedState = this.getStateForPlacement(context);
			if (placedState == null) {
				return InteractionResult.FAIL;
			} else if (!this.placeBlock(context, placedState)) {
				return InteractionResult.FAIL;
			} else {
				BlockPos pos = context.getClickedPos();
				Level level = context.getLevel();
				Player player = context.getPlayer();
				ItemStack stack = context.getItemInHand();
				BlockState currentState = level.getBlockState(pos);
				Block block = currentState.getBlock();
				if (block == placedState.getBlock()) {
					currentState = this.stateWithTag(pos, level, stack, currentState);
					this.setBlockEntityTag(level, player, pos, stack);
					block.setPlacedBy(level, pos, currentState, player, stack);
					if (player instanceof ServerPlayer) {
						CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, pos, stack);
					}
				}

				SoundType soundtype = currentState.getSoundType(level, pos, context.getPlayer());
				level.playSound(player, pos, currentState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
				stack.shrink(1);
				return InteractionResult.SUCCESS;
			}
		}
	}

	@Nullable
	protected BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState blockstate = RatsBlockRegistry.RAT_TUBE_COLOR.get().getStateForPlacement(context);
		return blockstate != null && this.canPlace(context, blockstate) ? blockstate : null;
	}

	private BlockState stateWithTag(BlockPos pos, Level level, ItemStack stack, BlockState state) {
		BlockState blockstate = state;
		CompoundTag compoundnbt = stack.getTag();
		if (compoundnbt != null) {
			CompoundTag compoundnbt1 = compoundnbt.getCompound("BlockStateTag");
			StateDefinition<Block, BlockState> statecontainer = state.getBlock().getStateDefinition();

			for (String s : compoundnbt1.getAllKeys()) {
				Property<?> iproperty = statecontainer.getProperty(s);
				if (iproperty != null) {
					String s1 = compoundnbt1.get(s).getAsString();
					blockstate = remapProperties(blockstate, iproperty, s1);
				}
			}
		}

		if (blockstate != state) {
			level.setBlock(pos, blockstate, 2);
		}

		return blockstate;
	}

	private static <T extends Comparable<T>> BlockState remapProperties(BlockState state, Property<T> property, String value) {
		return property.getValue(value).map(t -> state.setValue(property, t)).orElse(state);
	}

	protected boolean canPlace(BlockPlaceContext context, BlockState state) {
		return state.canSurvive(context.getLevel(), context.getClickedPos());
	}

	protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
		return context.getLevel().setBlock(context.getClickedPos(), state, 11);
	}

	public void setBlockEntityTag(Level level, @Nullable Player player, BlockPos pos, ItemStack stack) {
		MinecraftServer minecraftserver = level.getServer();
		if (minecraftserver != null) {
			CompoundTag compoundnbt = stack.getTagElement("BlockEntityTag");
			if (compoundnbt != null) {
				BlockEntity tileentity = level.getBlockEntity(pos);
				if (tileentity != null) {
					if (!level.isClientSide() && tileentity.onlyOpCanSetNbt() && (player == null || !player.canUseGameMasterBlocks())) {
						return;
					}

					CompoundTag compoundnbt1 = tileentity.serializeNBT();
					CompoundTag compoundnbt2 = compoundnbt1.copy();
					compoundnbt1.merge(compoundnbt);
					compoundnbt1.putInt("x", pos.getX());
					compoundnbt1.putInt("y", pos.getY());
					compoundnbt1.putInt("z", pos.getZ());
					if (!compoundnbt1.equals(compoundnbt2)) {
						tileentity.load(compoundnbt1);
						tileentity.setChanged();
					}
				}
			}

		}
	}
}
