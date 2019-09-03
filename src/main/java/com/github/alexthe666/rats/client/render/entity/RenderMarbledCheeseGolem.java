package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.ModelMarbledCheeseGolem;
import com.github.alexthe666.rats.client.model.ModelPinkie;
import com.github.alexthe666.rats.client.model.ModelRat;
import com.github.alexthe666.rats.server.entity.EntityMarbleCheeseGolem;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Map;

public class RenderMarbledCheeseGolem extends RenderLiving<EntityMarbleCheeseGolem> {

    private static final ResourceLocation MARBLED_CHEESE_GOLEM_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/marble_cheese_golem.png");

    public RenderMarbledCheeseGolem() {
        super(Minecraft.getMinecraft().getRenderManager(), new ModelMarbledCheeseGolem(), 0.95F);
        this.addLayer(new LayerCheeseGolemGlow(this));
    }

    protected void preRenderCallback(EntityMarbleCheeseGolem rat, float partialTickTime) {
        GL11.glScalef(1.2F, 1.2F, 1.2F);
        GL11.glTranslatef(0, -0.7F, 0);
    }

    protected ResourceLocation getEntityTexture(EntityMarbleCheeseGolem entity) {
        return MARBLED_CHEESE_GOLEM_TEXTURE;
    }
}
