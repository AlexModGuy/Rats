package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelFeralRatlantean;
import com.github.alexthe666.rats.server.entity.EntityPlagueBeast;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderPlagueBeast extends MobRenderer<EntityPlagueBeast, ModelFeralRatlantean<EntityPlagueBeast>> {

    private static final ResourceLocation BLUE_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/feral_ratlantean_blue.png");
    private static final ResourceLocation BLACK_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/feral_ratlantean_black.png");
    private static final ResourceLocation BROWN_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/feral_ratlantean_brown.png");
    private static final ResourceLocation GREEN_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/feral_ratlantean_green.png");
    private static final ResourceLocation PLAGUE_TEXTURE = new ResourceLocation("rats:textures/entity/plague_beast_overlay.png");
    private static final ResourceLocation EYE_TEXTURE = new ResourceLocation("rats:textures/entity/plague_beast_eyes.png");

    public RenderPlagueBeast() {
        super(Minecraft.getInstance().getRenderManager(), new ModelFeralRatlantean(), 0.5F);
        this.addLayer(new LayerBasicOverlay(this, PLAGUE_TEXTURE));
        this.addLayer(new LayerGlowingOverlay(this, EYE_TEXTURE));
    }

    protected void preRenderCallback(EntityPlagueBeast rat, MatrixStack stack, float partialTickTime) {

        stack.scale(1.2F, 1.2F, 1.2F);
    }

    public ResourceLocation getEntityTexture(EntityPlagueBeast entity) {
        switch (entity.getColorVariant()) {
            case 1:
                return BLACK_TEXTURE;
            case 2:
                return BROWN_TEXTURE;
            case 3:
                return GREEN_TEXTURE;
            default:
                return BLUE_TEXTURE;
        }
    }
}
