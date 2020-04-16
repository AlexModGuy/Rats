package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelCube;
import com.github.alexthe666.rats.server.entity.EntityPlagueShot;
import com.github.alexthe666.rats.server.entity.EntityThrownBlock;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class RenderThrownBlock extends EntityRenderer<EntityThrownBlock> {
    private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/psychic.png");
    private static final ModelCube MODEL_CUBE = new ModelCube(1.1F);

    public RenderThrownBlock() {
        super(Minecraft.getInstance().getRenderManager());
        this.shadowSize = 0.5F;
    }

    public void render(EntityThrownBlock entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        float f = 0.0625F;
        matrixStackIn.push();
        matrixStackIn.scale(1.5F, -1.5F, 1.5F);
        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        matrixStackIn.translate(0F, -1.5F, 0F);
        matrixStackIn.rotate(new Quaternion(Vector3f.YP, yaw - 180, true));
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEnergySwirl(LIGHTNING_TEXTURE, f * 0.01F, f * 0.01F));
        MODEL_CUBE.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);

    }

    public ResourceLocation getEntityTexture(EntityThrownBlock entity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }
}