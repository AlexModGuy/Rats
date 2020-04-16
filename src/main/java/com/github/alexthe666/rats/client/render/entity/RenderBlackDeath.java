package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelBlackDeath;
import com.github.alexthe666.rats.server.entity.EntityBlackDeath;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;

public class RenderBlackDeath extends MobRenderer<EntityBlackDeath, ModelBlackDeath<EntityBlackDeath>> {

    private static final ResourceLocation BLACK_DEATH_TEXTURE = new ResourceLocation("rats:textures/entity/black_death.png");
    private static final ResourceLocation GLOW_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/black_death_overlay.png");

    public RenderBlackDeath() {
        super(Minecraft.getInstance().getRenderManager(), new ModelBlackDeath(), 0.5F);
        this.addLayer(new LayerGlowingOverlay(this, GLOW_TEXTURE));
        this.addLayer(new HeldItemLayer(this) {
            public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (((EntityBlackDeath) entitylivingbaseIn).isSummoning()) {
                    super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }
            }
        });
    }

    public ResourceLocation getEntityTexture(EntityBlackDeath entity) {
        return BLACK_DEATH_TEXTURE;
    }
}
