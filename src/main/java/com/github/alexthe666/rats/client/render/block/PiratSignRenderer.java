package com.github.alexthe666.rats.client.render.block;

import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.state.properties.WoodType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class PiratSignRenderer extends SignRenderer {
	public PiratSignRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	// Note: renderSign is package-private in SignRenderer, so we can't override it
	// The custom sign rendering should be handled differently in 1.21
}







