package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.ModelFeralRatlantean;
import com.github.alexthe666.rats.client.model.ModelPinkie;
import com.github.alexthe666.rats.client.model.ModelRat;
import com.github.alexthe666.rats.server.entity.EntityFeralRatlantean;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class RenderFeralRatlantean extends RenderLiving<EntityFeralRatlantean> {

    private static final ResourceLocation BLUE_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/feral_ratlantean_blue.png");
    private static final ResourceLocation BLACK_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/feral_ratlantean_black.png");
    private static final ResourceLocation BROWN_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/feral_ratlantean_brown.png");
    private static final ResourceLocation GREEN_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/feral_ratlantean_green.png");

    public RenderFeralRatlantean() {
        super(Minecraft.getMinecraft().getRenderManager(), new ModelFeralRatlantean(), 0.5F);
        this.addLayer(new LayerFeralRatlanteanClothes(this));
        this.addLayer(new LayerFeralRatlanteanEyes(this));
    }

    protected void preRenderCallback(EntityFeralRatlantean rat, float partialTickTime) {
        GL11.glScaled(1.2F, 1.2F, 1.2F);
    }

    protected ResourceLocation getEntityTexture(EntityFeralRatlantean entity) {
       switch (entity.getColorVariant()){
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
