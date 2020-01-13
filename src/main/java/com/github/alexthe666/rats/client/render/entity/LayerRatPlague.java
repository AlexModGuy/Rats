package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRat;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerRatPlague extends LayerRenderer<EntityRat, ModelRat<EntityRat>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/rat/rat_plague_overlay.png");
    private static final ResourceLocation TEXTURE_LUMBERJACK = new ResourceLocation("rats:textures/entity/rat/rat_lumberjack_upgrade.png");
    private static final ResourceLocation TEXTURE_TOGA = new ResourceLocation("rats:textures/entity/rat/toga.png");
    private static final ResourceLocation TEXTURE_RATINATOR = new ResourceLocation("rats:textures/entity/rat/rat_ratinator_upgrade.png");
    private static final ResourceLocation TEXTURE_PSYCHIC = new ResourceLocation("rats:textures/entity/ratlantis/psychic.png");
    private ResourceLocation TEXTURE_GHOST = new ResourceLocation("rats:textures/entity/ratlantis/ghost_pirat_overlay.png");
    private static final ModelRat RAT_MODEL = new ModelRat(0.5F);
    private final IEntityRenderer<EntityRat, ModelRat<EntityRat>> ratRenderer;

    public LayerRatPlague(IEntityRenderer<EntityRat, ModelRat<EntityRat>> ratRendererIn) {
        super(ratRendererIn);
        this.ratRenderer = ratRendererIn;
    }

    public void render(EntityRat rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!(ratRenderer.getEntityModel() instanceof ModelRat)) {
            return;
        }
        if (rat.hasPlague()) {
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.ratRenderer.bindTexture(TEXTURE);
            this.ratRenderer.getEntityModel().render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.disableBlend();
            GlStateManager.disableNormalize();
        }
        if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BASIC_ENERGY) && rat.getHeldRF() > 0) {//rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PSYCHIC)) {
            float alpha = rat.getHeldRF() / Math.max(1, (float) rat.getRFTransferRate());
            GlStateManager.pushMatrix();
            boolean flag = true;
            GlStateManager.depthMask(!flag);
            this.ratRenderer.bindTexture(TEXTURE_PSYCHIC);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f = (float) rat.ticksExisted + partialTicks;
            GlStateManager.translatef(f * 0.01F, f * 0.01F, 0.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            GlStateManager.color4f(0.5F, 0.5F, 0.5F, alpha);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            GameRenderer gamerenderer = Minecraft.getInstance().gameRenderer;
            gamerenderer.setupFogColor(false);
            RAT_MODEL.render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            gamerenderer.setupFogColor(true);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(flag);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
        if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_RATINATOR)) {
            this.ratRenderer.bindTexture(TEXTURE_RATINATOR);
            this.ratRenderer.getEntityModel().render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
        if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK)) {
            this.ratRenderer.bindTexture(TEXTURE_LUMBERJACK);
            this.ratRenderer.getEntityModel().render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
        if (rat.hasToga()) {
            this.ratRenderer.bindTexture(TEXTURE_TOGA);
            this.ratRenderer.getEntityModel().render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
        if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ETHEREAL)) {
            boolean flag = rat.isInvisible();
            GlStateManager.depthMask(!flag);
            this.bindTexture(TEXTURE_GHOST);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f = (float)rat.ticksExisted + partialTicks;
            GlStateManager.translatef(f * 0.01F, f * 0.01F, 0.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            float f1 = 0.5F;
            GlStateManager.color4f(0.5F, 0.5F, 0.5F, 0.8F);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            this.getEntityModel().setModelAttributes(this.ratRenderer.getEntityModel());
            GameRenderer gamerenderer = Minecraft.getInstance().gameRenderer;
            gamerenderer.setupFogColor(true);
            this.ratRenderer.getEntityModel().renderNoWiskers(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            gamerenderer.setupFogColor(false);
            this.ratRenderer.func_217758_e(rat);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}