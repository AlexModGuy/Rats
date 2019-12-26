package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelBlackDeath;
import com.github.alexthe666.rats.server.entity.EntityBlackDeath;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;

public class RenderBlackDeath<T extends EntityBlackDeath> extends MobRenderer<T, ModelBlackDeath<T>> {

    private static final ResourceLocation BLACK_DEATH_TEXTURE = new ResourceLocation("rats:textures/entity/black_death.png");

    public RenderBlackDeath() {
        super(Minecraft.getInstance().getRenderManager(), new ModelBlackDeath(), 0.5F);
        this.addLayer(new LayerBlackDeathMask(this));
        this.addLayer(new HeldItemLayer(this) {
            public void render(LivingEntity LivingEntityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
                if (((EntityBlackDeath) LivingEntityIn).isSummoning()) {
                    super.render(LivingEntityIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                }
            }

            protected void translateToHand(HandSide p_191361_1_) {
                ((ModelBlackDeath) this.getEntityModel()).getArm(p_191361_1_).postRender(0.0625F);
                GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.translatef(p_191361_1_ == HandSide.LEFT ? -0.1F : 0.1F, 0.1F, 0);
            }
        });
    }

    protected ResourceLocation getEntityTexture(EntityBlackDeath entity) {
        return BLACK_DEATH_TEXTURE;
    }
}
