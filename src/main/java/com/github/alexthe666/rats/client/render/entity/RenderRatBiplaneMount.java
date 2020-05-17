package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelBiplane;
import com.github.alexthe666.rats.server.entity.EntityRatBaronPlane;
import com.github.alexthe666.rats.server.entity.EntityRatBiplaneMount;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderRatBiplaneMount extends MobRenderer<EntityRatBiplaneMount, ModelBiplane<EntityRatBiplaneMount>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/rat/rat_biplane_upgrade.png");

    public RenderRatBiplaneMount() {
        super(Minecraft.getInstance().getRenderManager(), new ModelBiplane(), 1.65F);
    }

    protected void preRenderCallback(EntityRatBiplaneMount entity, MatrixStack matrixStackIn, float partialTickTime) {

    }

    public ResourceLocation getEntityTexture(EntityRatBiplaneMount entity) {
        return TEXTURE;
    }
}
