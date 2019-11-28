package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelPlagueDoctor;
import com.github.alexthe666.rats.server.entity.EntityPlagueDoctor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.util.ResourceLocation;

public class RenderPlagueDoctor extends RenderLiving<EntityPlagueDoctor> {
    private static final ResourceLocation VILLAGER_TEXTURES = new ResourceLocation("rats:textures/entity/plague_doctor.png");

    public RenderPlagueDoctor() {
        super(Minecraft.getMinecraft().getRenderManager(), new ModelPlagueDoctor(0.0F), 0.5F);
        this.addLayer(new LayerCustomHead(this.getMainModel().villagerHead));
    }

    public ModelVillager getMainModel() {
        return (ModelPlagueDoctor) super.getMainModel();
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityPlagueDoctor entity) {
        return VILLAGER_TEXTURES;
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    protected void preRenderCallback(EntityPlagueDoctor entitylivingbaseIn, float partialTickTime) {
        float f = 0.9375F;

        if (entitylivingbaseIn.getGrowingAge() < 0) {
            f = (float) ((double) f * 0.5D);
            this.shadowSize = 0.25F;
        } else {
            this.shadowSize = 0.5F;
        }

        GlStateManager.scale(f, f, f);
    }
}