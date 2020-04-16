package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelNeoRatlantean;
import com.github.alexthe666.rats.server.entity.EntityNeoRatlantean;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class RenderNeoRatlantean extends MobRenderer<EntityNeoRatlantean, ModelNeoRatlantean<EntityNeoRatlantean>> {

    private static final ResourceLocation BLUE_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/neo_ratlantean_blue.png");
    private static final ResourceLocation BLACK_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/neo_ratlantean_black.png");
    private static final ResourceLocation BROWN_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/neo_ratlantean_brown.png");
    private static final ResourceLocation GREEN_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/neo_ratlantean_green.png");
    private static final ResourceLocation GLOW_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/neo_ratlantean_glow.png");

    public RenderNeoRatlantean() {
        super(Minecraft.getInstance().getRenderManager(), new ModelNeoRatlantean(), 0.65F);
        this.addLayer(new LayerGlowingOverlay(this, GLOW_TEXTURE));
    }

    protected void preRenderCallback(EntityNeoRatlantean rat, float partialTickTime) {
        if (!rat.onGround) {
            GL11.glTranslatef(0.0F, -0.2F, 0.0F);
        }
        GL11.glScaled(1.2F, 1.2F, 1.2F);
    }

    public ResourceLocation getEntityTexture(EntityNeoRatlantean entity) {
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
