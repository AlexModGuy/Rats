package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelFlyingDutchrat;
import com.github.alexthe666.rats.server.entity.EntityDutchrat;
import com.github.alexthe666.rats.server.entity.EntityPlagueBeast;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
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
            public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if(entitylivingbaseIn instanceof EntityDutchrat && !((EntityDutchrat) entitylivingbaseIn).hasThrownSword()){
                    super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }
            }
        });
    }

    protected void preRenderCallback(EntityDutchrat rat, MatrixStack stack, float partialTickTime) {
    }

    public ResourceLocation getEntityTexture(EntityDutchrat entity) {
        return DUTCHRAT_TEXTURE;
    }
}
