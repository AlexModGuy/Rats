package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelFlyingDutchrat;
import com.github.alexthe666.rats.server.entity.EntityDutchrat;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;

public class RenderDutchrat extends MobRenderer<EntityDutchrat, ModelFlyingDutchrat<EntityDutchrat>> {

    private static final ResourceLocation DUTCHRAT_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/dutchrat.png");

    public RenderDutchrat() {
        super(Minecraft.getInstance().getRenderManager(), new ModelFlyingDutchrat(), 0.5F);
        this.addLayer(new LayerDutchratGlow(this));
        this.addLayer(new LayerDutchratHelmet(this));
        this.addLayer(new HeldItemLayer(this) {
            public void render(LivingEntity LivingEntityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
                if(LivingEntityIn instanceof EntityDutchrat && !((EntityDutchrat) LivingEntityIn).hasThrownSword()){
                    super.render(LivingEntityIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                }
            }

            protected void translateToHand(HandSide p_191361_1_) {
                ((ModelFlyingDutchrat) this.getEntityModel()).body1.postRender(0.0625F);
                ((ModelFlyingDutchrat) this.getEntityModel()).rightArm1.postRender(0.0625F);
                ((ModelFlyingDutchrat) this.getEntityModel()).rightArm2.postRender(0.0625F);
                ((ModelFlyingDutchrat) this.getEntityModel()).paw.postRender(0.0625F);
                GlStateManager.rotatef(90.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.translatef(0.1F, -0.75F, 0);
                GlStateManager.disableLighting();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.scalef(1.5F, 1.5F, 1.5F);
                GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240F, 0.0F);
                GlStateManager.enableLighting();
            }
        });
    }

    protected void preRenderCallback(EntityRat rat, float partialTickTime) {
        //GL11.glScaled(2.0F, 2.0F, 2.0F);
    }

    protected ResourceLocation getEntityTexture(EntityDutchrat entity) {
        return DUTCHRAT_TEXTURE;
    }
}
