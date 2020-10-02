package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelPiratBoat;
import com.github.alexthe666.rats.client.model.ModelPiratCannon;
import com.github.alexthe666.rats.server.entity.ratlantis.EntityPiratBoat;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class LayerPiratBoatSail extends LayerRenderer<EntityPiratBoat, ModelPiratBoat<EntityPiratBoat>> {
    protected static final ModelPiratCannon MODEL_PIRAT_CANNON = new ModelPiratCannon();
    protected static final ResourceLocation TEXTURE_PIRATE_CANNON = new ResourceLocation("rats:textures/entity/ratlantis/pirat_cannon.png");
    protected static final ResourceLocation TEXTURE_PIRATE_CANNON_FIRE = new ResourceLocation("rats:textures/entity/ratlantis/pirat_cannon_fire.png");
    private final IEntityRenderer<EntityPiratBoat, ModelPiratBoat<EntityPiratBoat>> ratRenderer;
    private final RenderType renderType;
    private final RenderType renderTypeFire;

    public LayerPiratBoatSail(IEntityRenderer<EntityPiratBoat, ModelPiratBoat<EntityPiratBoat>> ratRendererIn) {
        super(ratRendererIn);
        this.renderType = RenderType.getEntityCutoutNoCull(TEXTURE_PIRATE_CANNON);
        this.renderTypeFire = RenderType.getEyes(TEXTURE_PIRATE_CANNON_FIRE);
        this.ratRenderer = ratRendererIn;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityPiratBoat entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.push();
        matrixStackIn.rotate(new Quaternion(Vector3f.XP, 180F, true));
        matrixStackIn.rotate(new Quaternion(Vector3f.YP, 90F, true));
        matrixStackIn.translate(0F, -0.8F, -0.9F);
        matrixStackIn.scale(4F, 4F, 4F);
        Minecraft.getInstance().getItemRenderer().renderItem(entitylivingbaseIn.banner, ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.rotate(new Quaternion(Vector3f.YN, 90F, true));
        matrixStackIn.translate(0, 0.1F, -0.6F);
        matrixStackIn.scale(0.75F, 0.75F, 0.75F);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(renderType);
        MODEL_PIRAT_CANNON.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();

        if(entitylivingbaseIn.isFiring()){
            matrixStackIn.push();
            matrixStackIn.rotate(new Quaternion(Vector3f.YN, 90F, true));
            matrixStackIn.translate(0, 0.1F, -0.6F);
            matrixStackIn.scale(0.75F, 0.75F, 0.75F);
            IVertexBuilder ivertexbuilder2 = bufferIn.getBuffer(renderTypeFire);
            MODEL_PIRAT_CANNON.render(matrixStackIn, ivertexbuilder2, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.pop();
        }
    }

    public boolean shouldCombineTextures() {
        return true;
    }
}
