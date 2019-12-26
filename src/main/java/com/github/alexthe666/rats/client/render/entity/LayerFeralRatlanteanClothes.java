package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelFeralRatlantean;
import com.github.alexthe666.rats.server.entity.EntityFeralRatlantean;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerFeralRatlanteanClothes extends LayerRenderer<EntityFeralRatlantean, ModelFeralRatlantean<EntityFeralRatlantean>> {
    private static final ResourceLocation TEXTURE_TOGA = new ResourceLocation("rats:textures/entity/ratlantis/feral_ratlantean_clothes.png");
    private final IEntityRenderer<EntityFeralRatlantean, ModelFeralRatlantean<EntityFeralRatlantean>> ratRenderer;

    public LayerFeralRatlanteanClothes(IEntityRenderer<EntityFeralRatlantean, ModelFeralRatlantean<EntityFeralRatlantean>> ratRendererIn) {
        super(ratRendererIn);
        this.ratRenderer = ratRendererIn;
    }

    public void render(EntityFeralRatlantean rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (rat.hasToga()) {
            this.ratRenderer.bindTexture(TEXTURE_TOGA);
            this.ratRenderer.getEntityModel().render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
