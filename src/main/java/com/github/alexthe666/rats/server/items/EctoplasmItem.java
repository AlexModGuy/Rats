package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.registry.RatlantisBlockRegistry;
import com.github.alexthe666.rats.registry.RatsParticleRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EctoplasmItem extends Item {
	public EctoplasmItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		BlockState state = context.getLevel().getBlockState(context.getClickedPos());
		if (state.is(BlockTags.SAPLINGS) && !state.is(RatlantisBlockRegistry.PIRAT_SAPLING.get())) {
			context.getLevel().setBlockAndUpdate(context.getClickedPos(), RatlantisBlockRegistry.PIRAT_SAPLING.get().defaultBlockState());
			context.getLevel().playSound(null, context.getClickedPos(), RatsSoundRegistry.SAPLING_TRANSFORM.get(), SoundSource.BLOCKS);
			if (!context.getPlayer().isCreative()) {
				context.getItemInHand().shrink(1);
			}
			if (context.getLevel().isClientSide()) {
				for (int i = 0; i < 15; i++) {
					context.getLevel().addParticle(RatsParticleRegistry.PIRAT_GHOST.get(),
							context.getClickedPos().getX() + context.getLevel().getRandom().nextFloat(),
							context.getClickedPos().getY() + 0.1F,
							context.getClickedPos().getZ() + context.getLevel().getRandom().nextFloat(),
							0.0D, 0.0D, 0.0);
				}
			}
			return InteractionResult.SUCCESS;
		}
		return super.useOn(context);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable("item.rats.ghost_pirat_ectoplasm.desc").withStyle(ChatFormatting.GRAY));
	}
}
