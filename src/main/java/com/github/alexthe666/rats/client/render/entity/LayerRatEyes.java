package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRat;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;

public class LayerRatEyes extends LayerRenderer<EntityRat, ModelRat<EntityRat>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/rat/rat_eye_glow.png");
    private static final ResourceLocation TEXTURE_PLAGUE = new ResourceLocation("rats:textures/entity/rat/rat_eye_plague.png");
    private static final ResourceLocation TEXTURE_ENDER = new ResourceLocation("rats:textures/entity/rat/rat_eye_ender_upgrade.png");
    private static final ResourceLocation TEXTURE_RATINATOR = new ResourceLocation("rats:textures/entity/rat/rat_eye_ratinator_upgrade.png");
    private static final ResourceLocation TEXTURE_NONBELIEVER = new ResourceLocation("rats:textures/entity/rat/rat_eye_nonbeliever_upgrade.png");
    private static final ResourceLocation TEXTURE_DRAGON = new ResourceLocation("rats:textures/entity/rat/rat_eye_dragon_upgrade.png");
    private final IEntityRenderer<EntityRat, ModelRat<EntityRat>> ratRenderer;

    public LayerRatEyes(IEntityRenderer<EntityRat, ModelRat<EntityRat>> ratRendererIn) {
        super(ratRendererIn);
        this.ratRenderer = ratRendererIn;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityRat rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!(ratRenderer.getEntityModel() instanceof ModelRat)) {
            return;
        }
        long roundedTime = rat.world.getDayTime() % 24000;
        boolean night = roundedTime >= 13000 && roundedTime <= 22000;
        BlockPos ratPos = rat.getLightPosition();
        int i = rat.world.getLightFor(LightType.SKY, ratPos);
        int j = rat.world.getLightFor(LightType.BLOCK, ratPos);
        int brightness;
        if (night) {
            brightness = j;
        } else {
            brightness = Math.max(i, j);
        }
        if (brightness < 7 || rat.shouldEyesGlow()) {
            ResourceLocation tex = null;
            if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DRAGON)) {
                tex = TEXTURE_DRAGON;
            } else if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_NONBELIEVER)) {
                tex = TEXTURE_NONBELIEVER;
            } else if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ENDER)) {
                tex = TEXTURE_ENDER;
            } else if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_RATINATOR)) {
                tex = TEXTURE_RATINATOR;
            } else if (rat.hasPlague()) {
                tex = TEXTURE_PLAGUE;
            } else {
                tex = TEXTURE;
            }
            if(tex != null){
                IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEyes(tex));
                this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}