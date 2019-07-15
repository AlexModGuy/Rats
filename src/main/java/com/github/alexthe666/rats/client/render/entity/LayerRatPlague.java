package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRat;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerRatPlague implements LayerRenderer<EntityRat> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/rat/rat_plague_overlay.png");
    private static final ResourceLocation TEXTURE_LUMBERJACK = new ResourceLocation("rats:textures/entity/rat/rat_lumberjack_upgrade.png");
    private final RenderRat ratRenderer;

    public LayerRatPlague(RenderRat ratRendererIn) {
        this.ratRenderer = ratRendererIn;
    }

    public void doRenderLayer(EntityRat rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(!(ratRenderer.getMainModel() instanceof ModelRat)){
            return;
        }
        if (rat.hasPlague()) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            this.ratRenderer.bindTexture(TEXTURE);
            this.ratRenderer.getMainModel().render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.disableBlend();
        }
        if (rat.getUpgrade().getItem() == RatsItemRegistry.RAT_UPGRADE_LUMBERJACK) {
            this.ratRenderer.bindTexture(TEXTURE_LUMBERJACK);
            this.ratRenderer.getMainModel().render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}