package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelEmpty;
import com.github.alexthe666.rats.client.model.ModelRatKing;
import com.github.alexthe666.rats.server.entity.EntityRatKing;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.Pose;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;

public class RenderRatKing extends MobRenderer<EntityRatKing, ModelEmpty<EntityRatKing>> {

    private static final ResourceLocation TEXTURE_EYES = new ResourceLocation("rats:textures/entity/rat/rat_eye_glow.png");
    private static final ResourceLocation TEXTURE_1 = new ResourceLocation("rats:textures/entity/rat/rat_black.png");
    private static final ResourceLocation TEXTURE_2 = new ResourceLocation("rats:textures/entity/rat/rat_blue.png");
    private static final ResourceLocation TEXTURE_3 = new ResourceLocation("rats:textures/entity/rat_brown.png");
    private static final ResourceLocation TEXTURE_4 = new ResourceLocation("rats:textures/entity/rat_green.png");
    private static final ModelRatKing MODEL_RAT = new ModelRatKing(0);

    public RenderRatKing() {
        super(Minecraft.getInstance().getRenderManager(), new ModelEmpty<>(), 1.0F);
        this.addLayer(new LayerRatKing(this));
    }

    @Override
    protected void applyRotations(EntityRatKing entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        Pose pose = entityLiving.getPose();
        if (entityLiving.deathTime > 0) {
            float f = ((float)entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f = MathHelper.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }

            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(f * this.getDeathMaxRotation(entityLiving)));
        } else if (entityLiving.isSpinAttacking()) {
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-90.0F - entityLiving.rotationPitch));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(((float)entityLiving.ticksExisted + partialTicks) * -75.0F));
        } else if (entityLiving.hasCustomName()) {
            String s = TextFormatting.getTextWithoutFormattingCodes(entityLiving.getName().getString());
            if (("Dinnerbone".equals(s) || "Grumm".equals(s))) {
                matrixStackIn.translate(0.0D, (double)(entityLiving.getHeight() + 0.1F), 0.0D);
                matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(180.0F));
            }
        }

    }

    public void render(EntityRatKing entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private float interpolateValue(float start, float end, float pct) {
        return start + (end - start) * pct;
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(EntityRatKing entity) {
        return TEXTURE_1;
    }
}
