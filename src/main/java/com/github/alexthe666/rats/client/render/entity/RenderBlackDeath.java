package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelBlackDeath;
import com.github.alexthe666.rats.server.entity.EntityBlackDeath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;

public class RenderBlackDeath extends RenderLiving<EntityBlackDeath> {

    private static final ResourceLocation BLACK_DEATH_TEXTURE = new ResourceLocation("rats:textures/entity/black_death.png");

    public RenderBlackDeath() {
        super(Minecraft.getMinecraft().getRenderManager(), new ModelBlackDeath(), 0.5F);
        this.addLayer(new LayerBlackDeathMask(this));
        this.addLayer(new LayerHeldItem(this) {
            public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
                if (((EntityBlackDeath) entitylivingbaseIn).isSummoning()) {
                    super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                }
            }

            protected void translateToHand(EnumHandSide p_191361_1_) {
                ((ModelBlackDeath) this.livingEntityRenderer.getMainModel()).getArm(p_191361_1_).postRender(0.0625F);
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(p_191361_1_ == EnumHandSide.LEFT ? -0.1F : 0.1F, 0.1F, 0);
            }
        });
    }

    protected ResourceLocation getEntityTexture(EntityBlackDeath entity) {
        return BLACK_DEATH_TEXTURE;
    }
}
