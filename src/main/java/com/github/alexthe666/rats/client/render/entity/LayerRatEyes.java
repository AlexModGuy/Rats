package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.api.RatClientEvent;
import com.github.alexthe666.rats.client.model.ModelRat;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

public class LayerRatEyes extends LayerRenderer<EntityRat, SegmentedModel<EntityRat>> {
    private static final RenderType TEXTURE = RenderType.getEyes(new ResourceLocation("rats:textures/entity/rat/rat_eye_glow.png"));
    private static final RenderType TEXTURE_PLAGUE = RenderType.getEyes(new ResourceLocation("rats:textures/entity/rat/rat_eye_plague.png"));
    private static final RenderType TEXTURE_DEMON = RenderType.getEyes(new ResourceLocation("rats:textures/entity/rat/demon_rat_eye.png"));
    private static final RenderType TEXTURE_ENDER = RenderType.getEyes(new ResourceLocation("rats:textures/entity/rat/rat_eye_ender_upgrade.png"));
    private static final RenderType TEXTURE_RATINATOR = RenderType.getEyes(new ResourceLocation("rats:textures/entity/rat/rat_eye_ratinator_upgrade.png"));
    private static final RenderType TEXTURE_NONBELIEVER = RenderType.getEyes(new ResourceLocation("rats:textures/entity/rat/rat_eye_nonbeliever_upgrade.png"));
    private static final RenderType TEXTURE_DRAGON = RenderType.getEyes(new ResourceLocation("rats:textures/entity/rat/rat_eye_dragon_upgrade.png"));
    private final IEntityRenderer<EntityRat, SegmentedModel<EntityRat>> ratRenderer;

    public LayerRatEyes(IEntityRenderer<EntityRat, SegmentedModel<EntityRat>> ratRendererIn) {
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
            RenderType tex = null;
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
            } else if (rat.getType() == RatsEntityRegistry.DEMON_RAT || rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DEMON)) {
                tex = TEXTURE_DEMON;
            } else {
                RatClientEvent.GetEyesTexture textureEvent = new RatClientEvent.GetEyesTexture(rat, (RenderRat) ratRenderer);
                MinecraftForge.EVENT_BUS.post(textureEvent);
                tex = TEXTURE;
                if(textureEvent.getResult() == Event.Result.ALLOW && textureEvent.getTexture() != null){
                    tex = textureEvent.getTexture();
                }
            }
            if(tex != null){
                IVertexBuilder ivertexbuilder = bufferIn.getBuffer(tex);
                this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}