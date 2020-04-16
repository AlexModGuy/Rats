package com.github.alexthe666.rats.client.render.entity;
import com.github.alexthe666.rats.client.model.ModelRatBeastMount;
import com.github.alexthe666.rats.server.entity.EntityRatBeastMount;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderRatBeastMount extends MobRenderer<EntityRatBeastMount, ModelRatBeastMount<EntityRatBeastMount>> {

    private static final ResourceLocation BLUE_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/feral_ratlantean_blue.png");
    private static final ResourceLocation BLACK_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/feral_ratlantean_black.png");
    private static final ResourceLocation BROWN_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/feral_ratlantean_brown.png");
    private static final ResourceLocation GREEN_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/feral_ratlantean_green.png");
    private static final ResourceLocation EYE_TEXTURE = new ResourceLocation("rats:textures/entity/plague_beast_eyes.png");

    public RenderRatBeastMount() {
        super(Minecraft.getInstance().getRenderManager(), new ModelRatBeastMount(), 0.5F);
        this.addLayer(new LayerGlowingOverlay(this, EYE_TEXTURE));
    }

    protected void preRenderCallback(EntityRatBeastMount rat, float partialTickTime) {
        GL11.glScaled(1.2F, 1.2F, 1.2F);
    }

    public ResourceLocation getEntityTexture(EntityRatBeastMount entity) {
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
