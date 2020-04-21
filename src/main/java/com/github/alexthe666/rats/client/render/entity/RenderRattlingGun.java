package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRattlingGun;
import com.github.alexthe666.rats.client.model.ModelRattlingGunBase;
import com.github.alexthe666.rats.server.entity.EntityPlagueShot;
import com.github.alexthe666.rats.server.entity.EntityRattlingGun;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class RenderRattlingGun extends EntityRenderer<EntityRattlingGun> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/rattling_gun.png");
    private static final ResourceLocation TEXTURE_FIRING = new ResourceLocation("rats:textures/entity/rattling_gun_firing.png");
    public static final ModelRattlingGun GUN_MODEL = new ModelRattlingGun<>();
    public static final ModelRattlingGunBase GUN_BASE_MODEL = new ModelRattlingGunBase<>();

    public RenderRattlingGun() {
        super(Minecraft.getInstance().getRenderManager());
    }

    protected void preRenderCallback(EntityRattlingGun rat, float partialTickTime) {
    }

    public void render(EntityRattlingGun entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(TEXTURE));
        matrixStackIn.push();
        matrixStackIn.push();
        matrixStackIn.translate(0, 1.6F, 0);
        matrixStackIn.rotate(new Quaternion(Vector3f.XP, 180, true));
        GUN_BASE_MODEL.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        //IVertexBuilder ivertexbuilder2 = bufferIn.getBuffer(RenderType.getEyes(TEXTURE_FIRING));
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(entity.rotationYaw));
        GUN_MODEL.resetToDefaultPose();
        if(!entity.isFiring()){
            GUN_MODEL.gun1.rotateAngleZ = 0;
            GUN_MODEL.handle1.rotateAngleX = 0;
        }else{
            GUN_MODEL.setRotationAngles(entity, 0, 0, entity.ticksExisted + partialTicks, 0, 0);
        }

        GUN_MODEL.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();

        if(entity.isFiring()) {
            matrixStackIn.push();
            matrixStackIn.translate(0, 1.6F, 0);
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, 180, true));
            IVertexBuilder ivertexbuilder2 = bufferIn.getBuffer(RenderType.getEyes(TEXTURE_FIRING));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(entity.rotationYaw));
            GUN_MODEL.setRotationAngles(entity, 0, 0, entity.ticksExisted + partialTicks, 0, 0);
            GUN_MODEL.render(matrixStackIn, ivertexbuilder2, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.pop();
        }
        matrixStackIn.pop();
    }

    private float interpolateValue(float start, float end, float pct) {
        return start + (end - start) * pct;
    }

    public ResourceLocation getEntityTexture(EntityRattlingGun entity) {
        return TEXTURE;
    }
}
