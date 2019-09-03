package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelIllagerPiper;
import com.github.alexthe666.rats.client.model.ModelRatlanteanSpirit;
import com.github.alexthe666.rats.server.entity.EntityIllagerPiper;
import com.github.alexthe666.rats.server.entity.EntityRatlanteanSpirit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderRatlateanSpirit extends RenderLiving<EntityRatlanteanSpirit> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/rat/ratlantean_spirit.png");

    public RenderRatlateanSpirit() {
        super(Minecraft.getMinecraft().getRenderManager(), new ModelRatlanteanSpirit(), 0.5F);
    }

    protected ResourceLocation getEntityTexture(EntityRatlanteanSpirit entity) {
        return TEXTURE;
    }

    protected void preRenderCallback(EntityRatlanteanSpirit entitylivingbaseIn, float partialTickTime) {
        doPortalEffect(entitylivingbaseIn, partialTickTime);
        GlStateManager.scale(1.5F, 1.5F, 1.5F);
    }

    public void doPortalEffect(Entity entity, float partialTicks) {
    }
}