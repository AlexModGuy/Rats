package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRat;
import com.github.alexthe666.rats.server.entity.EntityFeralRatlantean;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerFeralRatlanteanClothes implements LayerRenderer<EntityFeralRatlantean> {
    private static final ResourceLocation TEXTURE_TOGA = new ResourceLocation("rats:textures/entity/ratlantis/feral_ratlantean_clothes.png");
    private final RenderFeralRatlantean ratRenderer;

    public LayerFeralRatlanteanClothes(RenderFeralRatlantean ratRendererIn) {
        this.ratRenderer = ratRendererIn;
    }

    public void doRenderLayer(EntityFeralRatlantean rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (rat.hasToga()) {
            this.ratRenderer.bindTexture(TEXTURE_TOGA);
            this.ratRenderer.getMainModel().render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
