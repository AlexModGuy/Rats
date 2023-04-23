package com.github.alexthe666.rats.client.render.block;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.RatlanteanAutomatonModel;
import com.github.alexthe666.rats.server.block.RatlanteanAutomatonHeadBlock;
import com.github.alexthe666.rats.server.block.entity.RatlanteanAutomatonHeadBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RatlanteanAutomatonHeadRenderer implements BlockEntityRenderer<RatlanteanAutomatonHeadBlockEntity> {
	private static final RatlanteanAutomatonModel<?> AUTOMATON_MODEL = new RatlanteanAutomatonModel<>(false);
	private static final RenderType GOLEM_TEXTURE = RenderType.entityCutout(new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantean_automaton/ratlantean_automaton.png"));
	private static final RenderType GLOW_TEXTURE = RenderType.eyes(new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantean_automaton/ratlantean_automaton_glow.png"));

	public RatlanteanAutomatonHeadRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(RatlanteanAutomatonHeadBlockEntity te, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		if (te.getLevel() != null) {
			if (te.getLevel().getBlockState(te.getBlockPos()).getBlock() instanceof RatlanteanAutomatonHeadBlock) {
				float rotation = te.getLevel().getBlockState(te.getBlockPos()).getValue(RatlanteanAutomatonHeadBlock.FACING).toYRot();
				float tickCount = te.tickCount + partialTicks;
				this.renderHead(rotation, tickCount, stack, buffer, light, overlay);
			}
		} else {
			this.renderHead(0, Minecraft.getInstance().player.tickCount + partialTicks, stack, buffer, light, overlay);
		}
	}

	private void renderHead(float rotation, float tickCount, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		stack.pushPose();
		stack.translate(0.5F, -0.5F, 0.5F);
		stack.mulPose(Axis.XP.rotationDegrees(180));
		stack.mulPose(Axis.YP.rotationDegrees(rotation));
		VertexConsumer consumer = buffer.getBuffer(GOLEM_TEXTURE);
		AUTOMATON_MODEL.setTERotationAngles(tickCount);
		AUTOMATON_MODEL.renderHead(stack, consumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		VertexConsumer ivertexbuilder2 = buffer.getBuffer(GLOW_TEXTURE);
		AUTOMATON_MODEL.renderHead(stack, ivertexbuilder2, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.popPose();
	}
}
