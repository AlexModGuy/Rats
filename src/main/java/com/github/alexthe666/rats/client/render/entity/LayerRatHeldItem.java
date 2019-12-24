package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRat;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.ItemRatUpgradeBucket;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class LayerRatHeldItem implements LayerRenderer<EntityRat> {
    protected static final ModelChest MODEL_CHEST = new ModelChest();
    private static final ResourceLocation TEXTURE_CHRISTMAS_CHEST = new ResourceLocation("textures/entity/chest/christmas.png");
    private static ItemStack PLATTER_STACK = new ItemStack(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
    private static ItemStack AXE_STACK = new ItemStack(Items.STONE_AXE);
    private static ItemStack PICKAXE_STACK = new ItemStack(Items.STONE_PICKAXE);
    private static ItemStack IRON_AXE_STACK = new ItemStack(Items.IRON_AXE);
    private static ItemStack IRON_HOE_STACK = new ItemStack(Items.IRON_HOE);
    private static ItemStack SHEARS_STACK = new ItemStack(Items.SHEARS);
    private static ItemStack TNT_STACK = new ItemStack(Blocks.TNT);
    private static ItemStack FISHING_ROD_STACK = new ItemStack(Items.FISHING_ROD);
    private static ItemStack WING_STACK = new ItemStack(RatsItemRegistry.FEATHERY_WING);
    private static ItemStack DRAGON_WING_STACK = new ItemStack(RatsItemRegistry.DRAGON_WING);
    private static ItemStack BRAIN_STACK = new ItemStack(RatsBlockRegistry.BRAIN_BLOCK);
    RenderRat renderer;

    public LayerRatHeldItem(RenderRat renderer) {
        this.renderer = renderer;
    }

    public void doRenderLayer(EntityRat entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!(this.renderer.getMainModel() instanceof ModelRat)) {
            return;
        }
        ItemStack itemstack = entity.getHeldItem(Hand.MAIN_HAND);
        if (!itemstack.isEmpty()) {
            GlStateManager.color(1.0F, 1.0F, 1.0F);
            GlStateManager.pushMatrix();

            if (this.renderer.getMainModel().isChild) {
                GlStateManager.translate(0.0F, 0.625F, 0.0F);
                GlStateManager.rotate(-20.0F, -1.0F, 0.0F, 0.0F);
                float f = 0.5F;
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
            }
            Minecraft minecraft = Minecraft.getMinecraft();
            if (entity.shouldNotIdleAnimation()) {
                translateToHead();
                GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.translate(0F, 0.25, 0.05F);
            } else {
                translateToHand(true);
                GlStateManager.rotate(190.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.translate(-0.155F, -0.025, 0.125F);
                if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PLATTER)) {
                    GlStateManager.translate(0F, 0.25F, 0F);
                    if (itemstack.getItem() instanceof ItemBlock) {
                        GlStateManager.rotate(-90, 1.0F, 0.0F, 0.0F);
                    } else {
                        GlStateManager.translate(0F, -0.1F, -0.075F);

                    }
                }
                if (entity.holdsItemInHandUpgrade()) {
                    GlStateManager.translate(0.15F, -0.075F, 0);
                }
            }
            minecraft.getItemRenderer().renderItem(entity, itemstack, ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BUCCANEER)) {
            GlStateManager.pushMatrix();
            ((ModelRat) this.renderer.getMainModel()).body1.postRender(0.0625F);
            GlStateManager.pushMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, -0.925F, 0.2F);
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            this.renderer.bindTexture(LayerPiratBoatSail.TEXTURE_PIRATE_CANNON);
            LayerPiratBoatSail.MODEL_PIRAT_CANNON.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.popMatrix();
            if (entity.getVisualFlag()) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0, -0.925F, 0.2F);
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
                GlStateManager.disableLighting();
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 0.0F);
                this.renderer.bindTexture(LayerPiratBoatSail.TEXTURE_PIRATE_CANNON_FIRE);
                LayerPiratBoatSail.MODEL_PIRAT_CANNON.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GlStateManager.enableLighting();
                GlStateManager.popMatrix();
            }
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CHRISTMAS)) {
            GlStateManager.pushMatrix();
            if (this.renderer.getMainModel().isChild) {
                GlStateManager.translate(0.0F, 0.625F, 0.0F);
                GlStateManager.rotate(-20.0F, -1.0F, 0.0F, 0.0F);
                float f = 0.5F;
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
            }
            Minecraft minecraft = Minecraft.getMinecraft();
            translateToHand(false);
            GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(10.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.15F, -0.4, -0.5F);
            GlStateManager.pushMatrix();
            GlStateManager.translate(-0.175F, 0.25F, 0.2F);
            GlStateManager.scale(0.35F, 0.35F, 0.35F);
            this.renderer.bindTexture(TEXTURE_CHRISTMAS_CHEST);
            MODEL_CHEST.renderAll();
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PLATTER)) {
            GlStateManager.pushMatrix();
            if (this.renderer.getMainModel().isChild) {
                GlStateManager.translate(0.0F, 0.625F, 0.0F);
                GlStateManager.rotate(-20.0F, -1.0F, 0.0F, 0.0F);
                float f = 0.5F;
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
            }
            Minecraft minecraft = Minecraft.getMinecraft();
            translateToHand(true);
            GlStateManager.rotate(190.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-70.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.translate(-0.155F, -0.225, 0.2F);
            GlStateManager.scale(2F, 2F, 2F);
            minecraft.getItemRenderer().renderItem(entity, PLATTER_STACK, ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BUCKET) || entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BIG_BUCKET) || entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MILKER)) {
            GlStateManager.pushMatrix();
            if (this.renderer.getMainModel().isChild) {
                GlStateManager.translate(0.0F, 0.625F, 0.0F);
                GlStateManager.rotate(-20.0F, -1.0F, 0.0F, 0.0F);
                float f = 0.5F;
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
            }
            Minecraft minecraft = Minecraft.getMinecraft();
            translateToHand(true);
            GlStateManager.rotate(190.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-40.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.translate(-0.155F, -0.225, 0.1F);
            GlStateManager.scale(1.75F, 1.75F, 1.75F);
            minecraft.getItemRenderer().renderItem(entity, ItemRatUpgradeBucket.getBucketFromFluid(entity.transportingFluid), ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CRAFTING)) {
            Minecraft minecraft = Minecraft.getMinecraft();
            GlStateManager.pushMatrix();
            translateToHand(true);
            GlStateManager.rotate(-90F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(-45.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            minecraft.getItemRenderer().renderItem(entity, AXE_STACK, ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            translateToHand(false);
            GlStateManager.rotate(-90F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(-45.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            minecraft.getItemRenderer().renderItem(entity, PICKAXE_STACK, ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK)) {
            Minecraft minecraft = Minecraft.getMinecraft();
            GlStateManager.pushMatrix();
            translateToHand(false);
            GlStateManager.rotate(-90F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(-15.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            minecraft.getItemRenderer().renderItem(entity, IRON_AXE_STACK, ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER)) {
            Minecraft minecraft = Minecraft.getMinecraft();
            GlStateManager.pushMatrix();
            translateToHand(false);
            GlStateManager.rotate(-90F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            minecraft.getItemRenderer().renderItem(entity, new ItemStack(Items.DIAMOND_PICKAXE), ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FARMER)) {
            Minecraft minecraft = Minecraft.getMinecraft();
            GlStateManager.pushMatrix();
            translateToHand(false);
            GlStateManager.rotate(-90F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(-15.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            minecraft.getItemRenderer().renderItem(entity, IRON_HOE_STACK, ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_SHEARS)) {
            Minecraft minecraft = Minecraft.getMinecraft();
            GlStateManager.pushMatrix();
            translateToHand(false);
            GlStateManager.rotate(-90F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(15.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.translate(0.1F, 0.0, 0.0F);
            minecraft.getItemRenderer().renderItem(entity, SHEARS_STACK, ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FISHERMAN)) {
            Minecraft minecraft = Minecraft.getMinecraft();
            GlStateManager.pushMatrix();
            translateToHand(false);
            GlStateManager.rotate(-180F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(90F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.2F, 0, 0.0F);
            minecraft.getItemRenderer().renderItem(entity, FISHING_ROD_STACK, ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FLIGHT)) {
            GlStateManager.pushMatrix();
            Minecraft minecraft = Minecraft.getMinecraft();
            float wingAngle = entity.onGround ? 0 : MathHelper.sin(ageInTicks) * 30;
            float wingFold = entity.onGround ? -45 : 0;
            ((ModelRat) this.renderer.getMainModel()).body1.postRender(0.0625F);
            ((ModelRat) this.renderer.getMainModel()).body2.postRender(0.0625F);
            GlStateManager.pushMatrix();
            GlStateManager.translate(0F, -0.1F, 0F);
            GlStateManager.rotate(wingAngle, 0.0F, 0.0F, -1.0F);
            GlStateManager.rotate(wingFold, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.55F, 0, 0.2F);
            GlStateManager.rotate(-90F, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(2, 2, 1);
            minecraft.getItemRenderer().renderItem(entity, WING_STACK, ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0F, -0.1F, 0F);
            GlStateManager.rotate(wingAngle, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(wingFold, 0.0F, -1.0F, 0.0F);
            GlStateManager.translate(-0.55F, 0F, 0.2F);
            GlStateManager.rotate(-90F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(180F, 0.0F, 1.0F, 0.0F);
            GlStateManager.scale(2, 2, 1);
            minecraft.getItemRenderer().renderItem(entity, WING_STACK, ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DRAGON)) {
            GlStateManager.pushMatrix();
            Minecraft minecraft = Minecraft.getMinecraft();
            float wingAngle = entity.onGround ? 0 : MathHelper.sin(ageInTicks) * 30;
            float wingFold = entity.onGround ? -45 : 0;
            ((ModelRat) this.renderer.getMainModel()).body1.postRender(0.0625F);
            ((ModelRat) this.renderer.getMainModel()).body2.postRender(0.0625F);
            GlStateManager.pushMatrix();
            GlStateManager.translate(0F, -0.1F, 0F);
            GlStateManager.rotate(wingAngle, 0.0F, 0.0F, -1.0F);
            GlStateManager.rotate(wingFold, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.55F, 0, 0.2F);
            GlStateManager.rotate(-90F, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(2, 2, 1);
            minecraft.getItemRenderer().renderItem(entity, DRAGON_WING_STACK, ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0F, -0.1F, 0F);
            GlStateManager.rotate(wingAngle, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(wingFold, 0.0F, -1.0F, 0.0F);
            GlStateManager.translate(-0.55F, 0F, 0.2F);
            GlStateManager.rotate(-90F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(180F, 0.0F, 1.0F, 0.0F);
            GlStateManager.scale(2, 2, 1);
            minecraft.getItemRenderer().renderItem(entity, DRAGON_WING_STACK, ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_TNT) || entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_TNT_SURVIVOR)) {
            Minecraft minecraft = Minecraft.getMinecraft();
            ((ModelRat) this.renderer.getMainModel()).body1.postRender(0.0625F);
            GlStateManager.pushMatrix();
            GlStateManager.translate(0F, 0.1F, 0.1F);
            GlStateManager.rotate(180, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(2, 2, 2);
            minecraft.getItemRenderer().renderItem(entity, TNT_STACK, ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PSYCHIC)) {
            Minecraft minecraft = Minecraft.getMinecraft();
            GlStateManager.pushMatrix();
            translateToHead();
            GlStateManager.translate(0F, 0.1F, 0.035F);
            GlStateManager.rotate(180, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(180, 0.0F, 1.0F, 0.0F);
            GlStateManager.scale(0.9F, 0.9F, 0.9F);
            minecraft.getItemRenderer().renderItem(entity, BRAIN_STACK, ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
        }
    }

    protected void translateToHead() {
        ((ModelRat) this.renderer.getMainModel()).body1.postRender(0.0625F);
        ((ModelRat) this.renderer.getMainModel()).body2.postRender(0.0625F);
        ((ModelRat) this.renderer.getMainModel()).neck.postRender(0.0625F);
        ((ModelRat) this.renderer.getMainModel()).head.postRender(0.0625F);
    }

    protected void translateToHand(boolean left) {
        ((ModelRat) this.renderer.getMainModel()).body1.postRender(0.0625F);
        ((ModelRat) this.renderer.getMainModel()).body2.postRender(0.0625F);
        if (left) {
            ((ModelRat) this.renderer.getMainModel()).leftArm.postRender(0.0625F);
            ((ModelRat) this.renderer.getMainModel()).leftHand.postRender(0.0625F);
        } else {
            ((ModelRat) this.renderer.getMainModel()).rightArm.postRender(0.0625F);
            ((ModelRat) this.renderer.getMainModel()).rightHand.postRender(0.0625F);
        }
    }


    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
