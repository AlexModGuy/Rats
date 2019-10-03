package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRat;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerRatPlague implements LayerRenderer<EntityRat> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/rat/rat_plague_overlay.png");
    private static final ResourceLocation TEXTURE_LUMBERJACK = new ResourceLocation("rats:textures/entity/rat/rat_lumberjack_upgrade.png");
    private static final ResourceLocation TEXTURE_TOGA = new ResourceLocation("rats:textures/entity/rat/toga.png");
    private static final ResourceLocation TEXTURE_RATINATOR = new ResourceLocation("rats:textures/entity/rat/rat_ratinator_upgrade.png");
    private static final ResourceLocation TEXTURE_PSYCHIC = new ResourceLocation("rats:textures/entity/ratlantis/psychic.png");
    private static final ModelRat RAT_MODEL = new ModelRat(0.5F);
    private final RenderRat ratRenderer;

    public LayerRatPlague(RenderRat ratRendererIn) {
        this.ratRenderer = ratRendererIn;
    }

    public void doRenderLayer(EntityRat rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!(ratRenderer.getMainModel() instanceof ModelRat)) {
            return;
        }
        if (rat.hasPlague()) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.ratRenderer.bindTexture(TEXTURE);
            this.ratRenderer.getMainModel().render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.disableBlend();
            GlStateManager.disableNormalize();
        }
        if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PSYCHIC)) {
            GlStateManager.pushMatrix();
            boolean flag = true;
            GlStateManager.depthMask(!flag);
            this.ratRenderer.bindTexture(TEXTURE_PSYCHIC);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f = (float) rat.ticksExisted + partialTicks;
            GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            RAT_MODEL.render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(flag);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
        if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_RATINATOR)) {
            this.ratRenderer.bindTexture(TEXTURE_RATINATOR);
            this.ratRenderer.getMainModel().render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
        if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK)) {
            this.ratRenderer.bindTexture(TEXTURE_LUMBERJACK);
            this.ratRenderer.getMainModel().render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
        if (rat.hasToga()) {
            this.ratRenderer.bindTexture(TEXTURE_TOGA);
            this.ratRenderer.getMainModel().render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}