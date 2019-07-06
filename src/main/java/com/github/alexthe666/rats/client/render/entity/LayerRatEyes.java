package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRat;
import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;

public class LayerRatEyes implements LayerRenderer<EntityRat> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/rat/rat_eye_glow.png");
    private static final ResourceLocation TEXTURE_PLAGUE = new ResourceLocation("rats:textures/entity/rat/rat_eye_plague.png");
    private final RenderRat ratRenderer;

    public LayerRatEyes(RenderRat ratRendererIn) {
        this.ratRenderer = ratRendererIn;
    }

    public void doRenderLayer(EntityRat rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(!(ratRenderer.getMainModel() instanceof ModelRat)){
            return;
        }
        long roundedTime = rat.world.getWorldTime() % 24000;
        boolean night = roundedTime >= 13000 && roundedTime <= 22000;
        BlockPos ratPos = rat.getLightPosition();
        int i = rat.world.getLightFor(EnumSkyBlock.SKY, ratPos);
        int j = rat.world.getLightFor(EnumSkyBlock.BLOCK, ratPos);
        int brightness;
        if(night){
            brightness = j;
        }else{
            brightness = Math.max(i, j);
        }
        if (brightness < 7) {
            if(rat.hasPlague()){
                this.ratRenderer.bindTexture(TEXTURE_PLAGUE);
            }else{
                this.ratRenderer.bindTexture(TEXTURE);
            }
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            GlStateManager.disableLighting();
            GlStateManager.depthFunc(514);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 0.0F);
            GlStateManager.enableLighting();
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            this.ratRenderer.getMainModel().render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
            this.ratRenderer.setLightmap(rat);
            GlStateManager.disableBlend();
            GlStateManager.depthFunc(515);
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}