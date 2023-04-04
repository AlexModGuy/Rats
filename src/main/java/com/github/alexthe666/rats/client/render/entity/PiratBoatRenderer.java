package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.entity.PiratBoatModel;
import com.github.alexthe666.rats.client.render.entity.layer.PiratBoatSailLayer;
import com.github.alexthe666.rats.server.entity.ratlantis.PiratBoat;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class PiratBoatRenderer<T extends PiratBoat, M extends PiratBoatModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/boat/spruce.png");
	protected final List<RenderLayer<T, M>> layers = Lists.newArrayList();
	private final M model;

	public PiratBoatRenderer(EntityRendererProvider.Context context, M model) {
		super(context);
		this.model = model;
		this.addLayer(new PiratBoatSailLayer<>(this));
	}

	public final void addLayer(RenderLayer<T, M> layer) {
		this.layers.add(layer);
	}

	@Nullable
	protected RenderType getRenderType(T machine, boolean visible, boolean ghostly, boolean glowing) {
		ResourceLocation resourcelocation = this.getTextureLocation(machine);
		if (ghostly) {
			return RenderType.itemEntityTranslucentCull(resourcelocation);
		} else if (visible) {
			return this.model.renderType(resourcelocation);
		} else {
			return glowing ? RenderType.outline(resourcelocation) : null;
		}
	}

	//hacked together LivingEntityRenderer.render, allows us to add layers to a non-living entity
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

		Minecraft minecraft = Minecraft.getInstance();
		boolean flag = !entity.isInvisible();
		boolean flag1 = !flag && !entity.isInvisibleTo(Objects.requireNonNull(minecraft.player));
		boolean flag2 = minecraft.shouldEntityAppearGlowing(entity);
		RenderType rendertype = this.getRenderType(entity, flag, flag1, flag2);
		if (rendertype != null) {
			VertexConsumer vertexconsumer = buffer.getBuffer(rendertype);
			this.getModel().renderToBuffer(stack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
		}
		if (!entity.isUnderWater()) {
			VertexConsumer vertexconsumer1 = buffer.getBuffer(RenderType.waterMask());
			this.getModel().getWaterPatch().render(stack, vertexconsumer1, light, OverlayTexture.NO_OVERLAY);
		}
		if (!entity.isSpectator()) {
			for (RenderLayer<T, M> renderlayer : this.layers) {
				renderlayer.render(stack, buffer, light, entity, 0.0F, 0.0F, partialTicks, entity.tickCount + partialTicks, 0.0F, 0.0F);
			}
		}
		super.render(entity, yaw, partialTicks, stack, buffer, light);
		stack.popPose();
	}

	@Override
	public M getModel() {
		return this.model;
	}

	public ResourceLocation getTextureLocation(PiratBoat entity) {
		return TEXTURE;
	}
}
