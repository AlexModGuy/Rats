package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRat;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;

public class LayerRatEyes extends LayerRenderer<EntityRat, ModelRat<EntityRat>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/rat/rat_eye_glow.png");
    private static final ResourceLocation TEXTURE_PLAGUE = new ResourceLocation("rats:textures/entity/rat/rat_eye_plague.png");
    private static final ResourceLocation TEXTURE_ENDER = new ResourceLocation("rats:textures/entity/rat/rat_eye_ender_upgrade.png");
    private static final ResourceLocation TEXTURE_RATINATOR = new ResourceLocation("rats:textures/entity/rat/rat_eye_ratinator_upgrade.png");
    private static final ResourceLocation TEXTURE_NONBELIEVER = new ResourceLocation("rats:textures/entity/rat/rat_eye_nonbeliever_upgrade.png");
    private static final ResourceLocation TEXTURE_DRAGON = new ResourceLocation("rats:textures/entity/rat/rat_eye_dragon_upgrade.png");
    private final IEntityRenderer<EntityRat, ModelRat<EntityRat>> ratRenderer;

    public LayerRatEyes(IEntityRenderer<EntityRat, ModelRat<EntityRat>> ratRendererIn) {
        super(ratRendererIn);
        this.ratRenderer = ratRendererIn;
    }

    public void render(EntityRat rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!(ratRenderer.getEntityModel() instanceof ModelRat)) {
            return;
        }
        long roundedTime = rat.world.getDayTime() % 24000;
        boolean night = roundedTime >= 13000 && roundedTime <= 22000;
        BlockPos ratPos = rat.getLightPosition();
        int i = rat.world.getLightFor(LightType.SKY, ratPos);
        int j = rat.world.getLightFor(LightType.BLOCK, ratPos);
        int brightness;
        if (night) {
            brightness = j;
        } else {
            brightness = Math.max(i, j);
        }
        if (brightness < 7 || rat.shouldEyesGlow()) {
            if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DRAGON)) {
                this.ratRenderer.bindTexture(TEXTURE_DRAGON);
            } else if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_NONBELIEVER)) {
                this.ratRenderer.bindTexture(TEXTURE_NONBELIEVER);
            } else if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ENDER)) {
                this.ratRenderer.bindTexture(TEXTURE_ENDER);
            } else if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_RATINATOR)) {
                this.ratRenderer.bindTexture(TEXTURE_RATINATOR);
            } else if (rat.hasPlague()) {
                this.ratRenderer.bindTexture(TEXTURE_PLAGUE);
            } else {
                this.ratRenderer.bindTexture(TEXTURE);
            }
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            GlStateManager.disableLighting();
            GlStateManager.depthFunc(514);
            GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 0.0F);
            GlStateManager.enableLighting();
            GameRenderer gamerenderer = Minecraft.getInstance().gameRenderer;
            gamerenderer.setupFogColor(true);
            this.ratRenderer.getEntityModel().render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            gamerenderer.setupFogColor(false);
            this.ratRenderer.func_217758_e(rat);
            GlStateManager.disableBlend();
            GlStateManager.depthFunc(515);
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}