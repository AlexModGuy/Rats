package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRatGolemMount;
import com.github.alexthe666.rats.server.entity.EntityRatGolemMount;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class LayerRatGolemMountCracks extends LayerRenderer<EntityRatGolemMount, ModelRatGolemMount<EntityRatGolemMount>> {
    private static final Map<EntityRatGolemMount.Cracks, ResourceLocation> field_229134_a_ = ImmutableMap.of(EntityRatGolemMount.Cracks.LOW, new ResourceLocation("textures/entity/iron_golem/iron_golem_crackiness_low.png"), EntityRatGolemMount.Cracks.MEDIUM, new ResourceLocation("textures/entity/iron_golem/iron_golem_crackiness_medium.png"), EntityRatGolemMount.Cracks.HIGH, new ResourceLocation("textures/entity/iron_golem/iron_golem_crackiness_high.png"));

    public LayerRatGolemMountCracks(IEntityRenderer<EntityRatGolemMount, ModelRatGolemMount<EntityRatGolemMount>> p_i226040_1_) {
        super(p_i226040_1_);
    }

    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityRatGolemMount entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entitylivingbaseIn.isInvisible()) {
            EntityRatGolemMount.Cracks EntityRatGolemMount$cracks = entitylivingbaseIn.getCracks();
            if (EntityRatGolemMount$cracks != EntityRatGolemMount.Cracks.NONE) {
                ResourceLocation resourcelocation = field_229134_a_.get(EntityRatGolemMount$cracks);
                renderCutoutModel(this.getEntityModel(), resourcelocation, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}