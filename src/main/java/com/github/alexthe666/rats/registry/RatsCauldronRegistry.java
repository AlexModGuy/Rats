package com.github.alexthe666.rats.registry;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Map;

public interface RatsCauldronRegistry extends CauldronInteraction {

	Map<Item, CauldronInteraction> MILK = CauldronInteraction.newInteractionMap();
	Map<Item, CauldronInteraction> CHEESE = CauldronInteraction.newInteractionMap();
	Map<Item, CauldronInteraction> BLUE_CHEESE = CauldronInteraction.newInteractionMap();
	Map<Item, CauldronInteraction> NETHER_CHEESE = CauldronInteraction.newInteractionMap();

	static void init() {
		EMPTY.put(Items.MILK_BUCKET, (state, level, pos, player, hand, stack) -> {
			if (!level.isClientSide()) {
				player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
				player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BUCKET)));
				player.awardStat(Stats.USE_CAULDRON);
				level.setBlockAndUpdate(pos, RatsBlockRegistry.MILK_CAULDRON.get().defaultBlockState());
				level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
				level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
			}
			return InteractionResult.sidedSuccess(level.isClientSide());
		});

		MILK.put(Items.BUCKET, (state, level, pos, player, hand, stack) -> {
			if (!level.isClientSide()) {
				player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
				player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.MILK_BUCKET)));
				player.awardStat(Stats.USE_CAULDRON);
				level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
				level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
				level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
			}
			return InteractionResult.sidedSuccess(level.isClientSide());
		});

		CHEESE.put(Items.SUGAR, (state, level, pos, player, hand, stack) -> {
			if (!level.isClientSide()) {
				player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
				level.setBlockAndUpdate(pos, RatsBlockRegistry.BLUE_CHEESE_CAULDRON.get().defaultBlockState());
				player.awardStat(Stats.USE_CAULDRON);
				if (!player.isCreative()) stack.shrink(1);
				level.playSound(null, pos, RatsSoundRegistry.BLUE_CHEESE_MADE.get(), SoundSource.BLOCKS);
				level.playSound(null, pos, RatsSoundRegistry.CHEESE_MADE.get(), SoundSource.BLOCKS, 1.0F, 0.75F);
				level.gameEvent(null, GameEvent.BLOCK_PLACE, pos);
			} else {
				for (int i = 0; i < 10; i++) {
					level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.SUGAR)),
							pos.getX() + level.getRandom().nextFloat(),
							pos.getY() + 0.9375D,
							pos.getZ() + level.getRandom().nextFloat(),
							0.0D, level.getRandom().nextFloat() * 0.25D + 0.1F, 0.0D);
				}
			}
			return InteractionResult.sidedSuccess(level.isClientSide());
		});

		CHEESE.put(Items.LAVA_BUCKET, (state, level, pos, player, hand, stack) -> {
			if (!level.isClientSide()) {
				player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
				level.setBlockAndUpdate(pos, RatsBlockRegistry.NETHER_CHEESE_CAULDRON.get().defaultBlockState());
				player.awardStat(Stats.USE_CAULDRON);
				player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BUCKET)));
				level.playSound(null, pos, RatsSoundRegistry.NETHER_CHEESE_MADE.get(), SoundSource.BLOCKS);
				level.playSound(null, pos, RatsSoundRegistry.CHEESE_MADE.get(), SoundSource.BLOCKS, 1.0F, 0.5F);
				level.gameEvent(null, GameEvent.BLOCK_PLACE, pos);
			} else {
				for (int i = 0; i < 10; i++) {
					level.addParticle(ParticleTypes.LAVA,
							pos.getX() + level.getRandom().nextFloat(),
							pos.getY() + 0.9375D,
							pos.getZ() + level.getRandom().nextFloat(),
							level.getRandom().nextFloat(), level.getRandom().nextFloat(), level.getRandom().nextFloat());
				}
			}
			return InteractionResult.sidedSuccess(level.isClientSide());
		});
	}
}
