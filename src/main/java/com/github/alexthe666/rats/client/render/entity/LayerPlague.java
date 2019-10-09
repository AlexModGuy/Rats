package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class LayerPlague implements LayerRenderer<EntityLivingBase> {

    private RenderLivingBase renderer;
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/model/plague_overlay.png");

    public LayerPlague(RenderLivingBase renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entitylivingbaseIn.isPotionActive(RatsMod.PLAGUE_POTION) && !(entitylivingbaseIn instanceof EntityRat)) {
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();
            GlStateManager.enableNormalize();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.renderer.bindTexture(TEXTURE);
            GlStateManager.matrixMode(5890);
            GlStateManager.scale(this.renderer.getMainModel().textureWidth / 16F, this.renderer.getMainModel().textureHeight / 16F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
            if(this.renderer.getMainModel() instanceof ModelBiped){
                ModelBiped biped = (ModelBiped)this.renderer.getMainModel();
                biped.bipedHeadwear.isHidden = true;
                if(this.renderer.getMainModel() instanceof ModelPlayer) {
                    ModelPlayer player = (ModelPlayer)this.renderer.getMainModel();
                    player.bipedBodyWear.isHidden = true;
                    player.bipedLeftArmwear.isHidden = true;
                    player.bipedLeftLegwear.isHidden = true;
                    player.bipedRightArmwear.isHidden = true;
                    player.bipedRightLegwear.isHidden = true;
                }
            }
            this.renderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.disableBlend();
            GlStateManager.disableNormalize();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
