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

	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
		tooltip.add(Component.translatable("block.rats.rat_tube.desc0").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("block.rats.rat_tube.desc1").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("block.rats.rat_tube.desc2").withStyle(ChatFormatting.GRAY));
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		InteractionResult actionresulttype = this.tryPlace(new BlockPlaceContext(context));
		return actionresulttype != InteractionResult.SUCCESS && (this.components().has(net.minecraft.core.component.DataComponents.FOOD)) ? this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult() : actionresulttype;
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
		// 1.21: BlockStateTag NBT was replaced with DataComponents.BLOCK_STATE (BlockItemStateProperties).
		// If a stack carries that component (only applies when this item was duplicated by /give-style commands),
		// fold those property values into the state we're about to place; otherwise keep the placement default.
		net.minecraft.world.item.component.BlockItemStateProperties props = stack.get(net.minecraft.core.component.DataComponents.BLOCK_STATE);
		if (props == null || props.isEmpty()) return state;
		StateDefinition<Block, BlockState> def = state.getBlock().getStateDefinition();
		for (java.util.Map.Entry<String, String> entry : props.properties().entrySet()) {
			Property<?> property = def.getProperty(entry.getKey());
			if (property != null) {
				state = remapProperties(state, property, entry.getValue());
			}
		}
		return state;
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
		// 1.21: BlockEntityTag NBT was replaced with DataComponents.BLOCK_ENTITY_DATA. We seed the placed
		// tube's color from the item's dye color baked into RatTubeItem; carry over any custom BE data the
		// stack might have (e.g. when picked up with middle-click in creative).
		BlockEntity be = level.getBlockEntity(pos);
		if (be instanceof com.github.alexthe666.rats.server.block.entity.RatTubeBlockEntity tube) {
			tube.setColor(this.color.getId());
			net.minecraft.world.item.component.CustomData customData = stack.getOrDefault(net.minecraft.core.component.DataComponents.BLOCK_ENTITY_DATA, net.minecraft.world.item.component.CustomData.EMPTY);
			if (!customData.isEmpty()) {
				CompoundTag tag = customData.copyTag();
				tag.remove("x"); tag.remove("y"); tag.remove("z"); tag.remove("id");
				if (!tag.isEmpty()) {
					CompoundTag full = be.saveWithoutMetadata(level.registryAccess());
					full.merge(tag);
					be.loadWithComponents(full, level.registryAccess());
				}
			}
			be.setChanged();
		}
	}
}
