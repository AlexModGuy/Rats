package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelPiratBoat;
import com.github.alexthe666.rats.server.entity.EntityPiratBoat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class RenderPiratBoat extends MobRenderer<EntityPiratBoat, ModelPiratBoat<EntityPiratBoat>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/boat/spruce.png");

    public RenderPiratBoat() {
        super(Minecraft.getInstance().getRenderManager(), new ModelPiratBoat(), 0.65F);
        this.addLayer(new LayerPiratBoatSail(this));
    }

    protected void preRenderCallback(EntityPiratBoat rat, float partialTickTime) {
        GL11.glScaled(1.2F, 1.2F, 1.2F);
    }

    public ResourceLocation getEntityTexture(EntityPiratBoat entity) {
        return TEXTURE;
    }
}
