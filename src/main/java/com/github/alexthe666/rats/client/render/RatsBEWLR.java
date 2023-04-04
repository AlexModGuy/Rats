package com.github.alexthe666.rats.client.render;

import com.github.alexthe666.rats.server.block.entity.RatHoleBlockEntity;
import com.github.alexthe666.rats.server.items.RatsBlockItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RatsBEWLR extends BlockEntityWithoutLevelRenderer {

	public RatsBEWLR() {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		if (stack.getItem() instanceof RatsBlockItem bi) {
			Block block = bi.getBlock();
			if (block instanceof EntityBlock be) {
				BlockEntity entity = be.newBlockEntity(BlockPos.ZERO, block.defaultBlockState());
				if (entity != null && !(entity instanceof RatHoleBlockEntity)) {
					Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(entity, ms, buffer, light, overlay);
				}
			}
		}
	}
}