package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelNeoRatlantean;
import com.github.alexthe666.rats.client.model.ModelPiratBoat;
import com.github.alexthe666.rats.server.entity.EntityNeoRatlantean;
import com.github.alexthe666.rats.server.entity.EntityPiratBoat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderPiratBoat extends RenderLiving<EntityPiratBoat> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/boat/boat_oak.png");

    public RenderPiratBoat() {
        super(Minecraft.getMinecraft().getRenderManager(), new ModelPiratBoat(), 0.65F);
        this.addLayer(new LayerPiratBoatSail(this));
    }

    protected void preRenderCallback(EntityPiratBoat rat, float partialTickTime) {
        GL11.glScaled(1.2F, 1.2F, 1.2F);
    }

    protected ResourceLocation getEntityTexture(EntityPiratBoat entity) {
       return TEXTURE;
    }
}
