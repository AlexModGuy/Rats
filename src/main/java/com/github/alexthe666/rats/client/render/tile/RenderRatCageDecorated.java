package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.client.model.*;
import com.github.alexthe666.rats.server.blocks.BlockRatCageBreedingLantern;
import com.github.alexthe666.rats.server.blocks.BlockRatCageDecorated;
import com.github.alexthe666.rats.server.blocks.BlockRatTrap;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCageDecorated;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatTrap;
import com.github.alexthe666.rats.server.items.ItemRatHammock;
import com.github.alexthe666.rats.server.items.ItemRatIgloo;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderRatCageDecorated extends TileEntitySpecialRenderer<TileEntityRatCageDecorated> {
    private static final ModelRatIgloo MODEL_RAT_IGLOO = new ModelRatIgloo();
    private static final ModelRatHammock MODEL_RAT_HAMMOCK = new ModelRatHammock();
    private static final ModelRatWaterBottle MODEL_RAT_WATER_BOTTLE = new ModelRatWaterBottle();
    private static final ModelRatSeedBowl MODEL_RAT_SEED_BOWL = new ModelRatSeedBowl();
    private static final ModelRatBreedingLantern MODEL_RAT_BREEDING_LANTERN = new ModelRatBreedingLantern();
    private static final ResourceLocation TEXTURE_RAT_IGLOO = new ResourceLocation("rats:textures/model/rat_igloo.png");
    private static final ResourceLocation TEXTURE_RAT_HAMMOCK = new ResourceLocation("rats:textures/model/rat_hammock_0.png");
    private static final ResourceLocation TEXTURE_RAT_WATER_BOTTLE = new ResourceLocation("rats:textures/model/rat_water_bottle.png");
    private static final ResourceLocation TEXTURE_RAT_SEED_BOWL = new ResourceLocation("rats:textures/model/rat_seed_bowl.png");
    private static final ResourceLocation TEXTURE_RAT_BREEDING_LANTERN = new ResourceLocation("rats:textures/model/rat_breeding_lantern.png");

    @Override
    public void render(TileEntityRatCageDecorated entity, double x, double y, double z, float f, int f1, float alpha) {
        float rotation = 0;
        float shutProgress = 0;
        ItemStack containedItem = ItemStack.EMPTY;
        if (entity != null && entity.getWorld() != null && entity instanceof TileEntityRatCageDecorated) {
            if(entity.getWorld().getBlockState(entity.getPos()).getBlock() instanceof BlockRatCageDecorated){
                if (entity.getWorld().getBlockState(entity.getPos()).getValue(BlockRatCageDecorated.FACING) == EnumFacing.NORTH) {
                    rotation = 180;
                }
                if (entity.getWorld().getBlockState(entity.getPos()).getValue(BlockRatCageDecorated.FACING) == EnumFacing.EAST) {
                    rotation = -90;
                }
                if (entity.getWorld().getBlockState(entity.getPos()).getValue(BlockRatCageDecorated.FACING) == EnumFacing.WEST) {
                    rotation = 90;
                }
            }
            if(entity.getWorld().getBlockState(entity.getPos()).getBlock() instanceof BlockRatCageBreedingLantern){
                if (entity.getWorld().getBlockState(entity.getPos()).getValue(BlockRatCageBreedingLantern.FACING) == EnumFacing.NORTH) {
                    rotation = 180;
                }
                if (entity.getWorld().getBlockState(entity.getPos()).getValue(BlockRatCageBreedingLantern.FACING) == EnumFacing.EAST) {
                    rotation = -90;
                }
                if (entity.getWorld().getBlockState(entity.getPos()).getValue(BlockRatCageBreedingLantern.FACING) == EnumFacing.WEST) {
                    rotation = 90;
                }
            }
            containedItem = entity.getContainedItem();
        }
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GL11.glPushMatrix();
        GL11.glRotatef(180, 1, 0, 0);
        GL11.glRotatef(rotation, 0, 1F, 0);
        GL11.glPushMatrix();
        if(containedItem.getItem() instanceof ItemRatIgloo){
            GlStateManager.enableBlend();
            GlStateManager.disableCull();
            GlStateManager.enableAlpha();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            EnumDyeColor color = ((ItemRatIgloo)containedItem.getItem()).color;
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_RAT_IGLOO);
            GlStateManager.color(color.getColorComponentValues()[0], color.getColorComponentValues()[1], color.getColorComponentValues()[2]);
            MODEL_RAT_IGLOO.render(null, 0, 0, 0, 0, 0, 0.0625F);
            GlStateManager.enableCull();
            GlStateManager.disableBlend();
        }
        if(containedItem.getItem() instanceof ItemRatHammock){
            GL11.glPushMatrix();
            GlStateManager.disableCull();
            Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("rats:textures/model/rat_hammock_0.png"));
            MODEL_RAT_HAMMOCK.renderString(0.0625F);
            GlStateManager.enableCull();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            EnumDyeColor color = ((ItemRatHammock)containedItem.getItem()).color;
            GlStateManager.enableColorMaterial();
            GlStateManager.color(color.getColorComponentValues()[0], color.getColorComponentValues()[1], color.getColorComponentValues()[2]);
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_RAT_HAMMOCK);
            MODEL_RAT_HAMMOCK.renderHammock(0.0625F);
            GlStateManager.disableColorMaterial();
            GL11.glPopMatrix();


        }
        if(containedItem.getItem() == RatsItemRegistry.RAT_WATER_BOTTLE){
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_RAT_WATER_BOTTLE);
            MODEL_RAT_WATER_BOTTLE.render(0.0625F);
        }
        if(containedItem.getItem() == RatsItemRegistry.RAT_SEED_BOWL){
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_RAT_SEED_BOWL);
            MODEL_RAT_SEED_BOWL.render(0.0625F);
        }
        if(containedItem.getItem() == RatsItemRegistry.RAT_BREEDING_LANTERN){
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_RAT_BREEDING_LANTERN);
            MODEL_RAT_BREEDING_LANTERN.render(0.0625F);
        }
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        GL11.glPopMatrix();

    }
}
