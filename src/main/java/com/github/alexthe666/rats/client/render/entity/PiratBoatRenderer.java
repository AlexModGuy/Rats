package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.entity.PiratBoatModel;
import com.github.alexthe666.rats.client.render.entity.layer.PiratBoatSailLayer;
import com.github.alexthe666.rats.server.entity.misc.PiratBoat;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class PiratBoatRenderer<T extends PiratBoat, M extends PiratBoatModel<T>> extends MobRenderer<T, M> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/boat/spruce.png");

	public PiratBoatRenderer(EntityRendererProvider.Context context, M model) {
		super(context, model, 0.0F);
		this.addLayer(new PiratBoatSailLayer<>(this));
	}

	@Override
	protected boolean isShaking(T entity) {
		return super.isShaking(entity);
	}

	@Override
	public void render(T entity, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		stack.pushPose();
		stack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
		stack.scale(-1.0F, -1.0F, 1.0F);
		stack.scale(1.2F, 1.2F, 1.2F);
		stack.translate(0.0D, -1.501F, 0.0D);
		stack.mulPose(Axis.YP.rotationDegrees(90.0F));
		this.getModel().prepareMobModel(entity, 0.0F, 0.0F, partialTicks);
		this.getModel().setupAnim(entity, 0.0F, 0.0F, entity.tickCount + partialTicks, 0.0F, 0.0F);
		float f = (float)entity.hurtTime - partialTicks;
		float f1 = Mth.abs(entity.getHealth() - entity.getMaxHealth()) - partialTicks;

		if (f > 0.0F) {
			stack.mulPose(Axis.YP.rotationDegrees(Mth.sin(f) * f * f1 / 10.0F));
		}
		Minecraft minecraft = Minecraft.getInstance();
		boolean flag = !entity.isInvisible();
		boolean flag1 = !flag && !entity.isInvisibleTo(Objects.requireNonNull(minecraft.player));
		boolean flag2 = minecraft.shouldEntityAppearGlowing(entity);
		RenderType rendertype = this.getRenderType(entity, flag, flag1, flag2);
		if (rendertype != null) {
			VertexConsumer vertexconsumer = buffer.getBuffer(rendertype);
			this.getModel().renderToBuffer(stack, vertexconsumer, light, OverlayTexture.pack(entity.deathTime > 0 ? entity.deathTime + 1 : 0, false), 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
		}
		VertexConsumer vertexconsumer1 = buffer.getBuffer(RenderType.waterMask());
		this.getModel().getWaterPatch().render(stack, vertexconsumer1, light, OverlayTexture.NO_OVERLAY);

		if (!entity.isSpectator()) {
			for (RenderLayer<T, M> renderlayer : this.layers) {
				renderlayer.render(stack, buffer, light, entity, 0.0F, 0.0F, partialTicks, entity.tickCount + partialTicks, 0.0F, 0.0F);
			}
		}
		stack.popPose();
	}

	public ResourceLocation getTextureLocation(PiratBoat entity) {
		return TEXTURE;
	}
}
