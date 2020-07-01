package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelCube;
import com.github.alexthe666.rats.server.entity.EntityThrownBlock;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class RenderThrownBlock extends EntityRenderer<EntityThrownBlock> {
    private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/psychic.png");
    private static final ModelCube MODEL_CUBE = new ModelCube(1.1F);

    public RenderThrownBlock() {
        super(Minecraft.getInstance().getRenderManager());
        this.shadowSize = 0.5F;
    }

    public void render(EntityThrownBlock entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        float f = entity.ticksExisted + partialTicks;
        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        matrixStackIn.push();
        BlockState state = entity.getHeldBlockState();
        if(state != null){
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
            matrixStackIn.push();
            float f4 = 0.75F;
            matrixStackIn.translate(-0.5D, 0, 0.5D);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90.0F));
            blockrendererdispatcher.renderBlock(state, matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY);
            matrixStackIn.pop();
        }
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.scale(1F, -1F, 1F);
        matrixStackIn.translate(0F, -0.5F, 0F);
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