package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

public class LayerPlague<T extends LivingEntity> extends LayerRenderer<T, EntityModel<T>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/model/plague_overlay.png");
    private final IEntityRenderer<T, EntityModel<T>> renderer;

    public LayerPlague(IEntityRenderer<T, EntityModel<T>> renderer) {
        super(renderer);
        this.renderer = renderer;
    }

    @Override
    public void render(T LivingEntityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (LivingEntityIn.isPotionActive(RatsMod.PLAGUE_POTION) && !(LivingEntityIn instanceof EntityRat)) {
            GlStateManager.pushMatrix();
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();
            GlStateManager.enableNormalize();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.renderer.bindTexture(TEXTURE);
            GlStateManager.matrixMode(5890);
            GlStateManager.scalef(this.renderer.getEntityModel().textureWidth / 16F, this.renderer.getEntityModel().textureHeight / 16F, 1.0F);
            GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
            if (this.renderer.getEntityModel() instanceof BipedModel) {
                BipedModel biped = (BipedModel) this.renderer.getEntityModel();
            }
            this.renderer.getEntityModel().render(LivingEntityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
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
