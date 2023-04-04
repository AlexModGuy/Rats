package com.github.alexthe666.rats.client.render.block;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.render.RatsRenderType;
import com.github.alexthe666.rats.server.block.entity.DutchratBellBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class DutchratBellRenderer implements BlockEntityRenderer<DutchratBellBlockEntity> {

	private final ModelPart bellBody;
	private static final RenderType TEXTURE = RatsRenderType.getGlowingTranslucent(new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantis/dutchrat_bell.png"));

	public DutchratBellRenderer(BlockEntityRendererProvider.Context context) {
		this.bellBody = context.bakeLayer(ModelLayers.BELL).getChild("bell_body");
	}

	public void render(DutchratBellBlockEntity bell, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		float f = (float) bell.ticks + partialTicks;
		float f1 = 0.0F;
		float f2 = 0.0F;
		if (bell.shaking) {
			float f3 = Mth.sin(f / (float) Math.PI) / (4.0F + f / 3.0F);
			if (bell.clickDirection == Direction.NORTH) {
				f1 = -f3;
			} else if (bell.clickDirection == Direction.SOUTH) {
				f1 = f3;
			} else if (bell.clickDirection == Direction.EAST) {
				f2 = -f3;
			} else if (bell.clickDirection == Direction.WEST) {
				f2 = f3;
			}
		}

		this.bellBody.xRot = f1;
		this.bellBody.zRot = f2;
		VertexConsumer consumer = buffer.getBuffer(TEXTURE);
		this.bellBody.render(stack, consumer, light, overlay, 1.0F, 1.0F, 1.0F, 0.5F);
	}
}