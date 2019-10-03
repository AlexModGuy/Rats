package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelPiratCannon;
import com.github.alexthe666.rats.server.entity.EntityPiratBoat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerPiratBoatSail implements LayerRenderer<EntityPiratBoat> {
    protected static final ModelPiratCannon MODEL_PIRAT_CANNON = new ModelPiratCannon();
    protected static final ResourceLocation TEXTURE_PIRATE_CANNON = new ResourceLocation("rats:textures/entity/ratlantis/pirat_cannon.png");
    protected static final ResourceLocation TEXTURE_PIRATE_CANNON_FIRE = new ResourceLocation("rats:textures/entity/ratlantis/pirat_cannon_fire.png");
    private final RenderPiratBoat ratRenderer;

    public LayerPiratBoatSail(RenderPiratBoat ratRendererIn) {
        this.ratRenderer = ratRendererIn;
    }

    public void doRenderLayer(EntityPiratBoat rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(180F, 1F, 0F, 0F);
        GlStateManager.rotate(90F, 0F, 1F, 0F);
        GlStateManager.translate(0F, -0.8F, -0.9F);
        GlStateManager.scale(4F, 4F, 4F);
        Minecraft.getMinecraft().getItemRenderer().renderItem(rat, EntityPiratBoat.BANNER, ItemCameraTransforms.TransformType.GROUND);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.rotate(-90F, 0F, 1F, 0F);
        GlStateManager.translate(0, 0.1F, -0.6F);
        GlStateManager.scale(0.75F, 0.75F, 0.75F);
        ratRenderer.bindTexture(TEXTURE_PIRATE_CANNON);
        MODEL_PIRAT_CANNON.render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.popMatrix();

        if (rat.isFiring()) {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(-90F, 0F, 1F, 0F);
            GlStateManager.translate(0, 0.1F, -0.6F);
            GlStateManager.scale(0.75F, 0.75F, 0.75F);
            GlStateManager.disableLighting();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 0.0F);
            ratRenderer.bindTexture(TEXTURE_PIRATE_CANNON_FIRE);
            MODEL_PIRAT_CANNON.render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }

    }

    public boolean shouldCombineTextures() {
        return true;
    }
}
