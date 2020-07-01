package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelPlagueDoctor;
import com.github.alexthe666.rats.server.entity.EntityPlagueDoctor;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.util.ResourceLocation;

public class RenderPlagueDoctor extends MobRenderer<EntityPlagueDoctor, ModelPlagueDoctor<EntityPlagueDoctor>> {
    private static final ResourceLocation VILLAGER_TEXTURES = new ResourceLocation("rats:textures/entity/plague_doctor.png");

    public RenderPlagueDoctor() {
        super(Minecraft.getInstance().getRenderManager(), new ModelPlagueDoctor(0.0F), 0.5F);
        this.addLayer(new CrossedArmsItemLayer<>(this));
        //this.addLayer(new LayerCustomHead(this.getMainModel().villagerHead));
    }


    public ResourceLocation getEntityTexture(EntityPlagueDoctor entity) {
        return VILLAGER_TEXTURES;
    }

    protected void preRenderCallback(EntityPlagueDoctor LivingEntityIn, MatrixStack matrixStack, float partialTickTime) {
        float f = 0.9375F;
        if (LivingEntityIn.getGrowingAge() < 0) {
            f = (float) ((double) f * 0.5D);
            this.shadowSize = 0.25F;
        } else {
            this.shadowSize = 0.5F;
        }
        matrixStack.scale(f, f, f);
    }
}