package com.github.alexthe666.rats.client.render.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

public class LayerBasicOverlay<T extends LivingEntity> extends LayerRenderer<T, EntityModel<T>> {
    private final IEntityRenderer<T, EntityModel<T>> ratRenderer;
    private final ResourceLocation texture;

    public LayerBasicOverlay(IEntityRenderer<T, EntityModel<T>> ratRendererIn, ResourceLocation texture) {
        super(ratRendererIn);
        this.ratRenderer = ratRendererIn;
        this.texture = texture;
    }

    public void render(T rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.ratRenderer.bindTexture(texture);
        this.ratRenderer.getEntityModel().render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }

    public boolean shouldCombineTextures() {
        return true;
    }
}
