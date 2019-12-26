package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRat;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.ItemRatUpgradeBucket;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.model.ChestModel;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class LayerRatHeldItem extends LayerRenderer<EntityRat, ModelRat<EntityRat>> {
    protected static final ChestModel MODEL_CHEST = new ChestModel();
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
    private final IEntityRenderer<EntityRat, ModelRat<EntityRat>> renderer;

    public LayerRatHeldItem(IEntityRenderer<EntityRat, ModelRat<EntityRat>> renderer) {
        super(renderer);
        this.renderer = renderer;
    }

    public void render(EntityRat entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!(this.renderer.getEntityModel() instanceof ModelRat)) {
            return;
        }
        ItemStack itemstack = entity.getHeldItem(Hand.MAIN_HAND);
        if (!itemstack.isEmpty()) {
            GlStateManager.color3f(1.0F, 1.0F, 1.0F);
            GlStateManager.pushMatrix();

            if (this.renderer.getEntityModel().isChild) {
                GlStateManager.translatef(0.0F, 0.625F, 0.0F);
                GlStateManager.rotatef(-20.0F, -1.0F, 0.0F, 0.0F);
                float f = 0.5F;
                GlStateManager.scalef(0.5F, 0.5F, 0.5F);
            }
            Minecraft minecraft = Minecraft.getInstance();
            if (entity.shouldNotIdleAnimation()) {
                translateToHead();
                GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(90.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.translatef(0F, 0.25F, 0.05F);
            } else {
                translateToHand(true);
                GlStateManager.rotatef(190.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(20.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.translatef(-0.155F, -0.025F, 0.125F);
                if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PLATTER)) {
                    GlStateManager.translatef(0F, 0.25F, 0F);
                    if (itemstack.getItem() instanceof BlockItem) {
                        GlStateManager.rotatef(-90, 1.0F, 0.0F, 0.0F);
                    } else {
                        GlStateManager.translatef(0F, -0.1F, -0.075F);

                    }
                }
                if (entity.holdsItemInHandUpgrade()) {
                    GlStateManager.translatef(0.15F, -0.075F, 0);
                }
            }
            minecraft.getItemRenderer().renderItem(itemstack, entity, ItemCameraTransforms.TransformType.GROUND, false);
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BUCCANEER)) {
            GlStateManager.pushMatrix();
            ((ModelRat) this.renderer.getEntityModel()).body1.postRender(0.0625F);
            GlStateManager.pushMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.translatef(0, -0.925F, 0.2F);
            GlStateManager.scalef(0.5F, 0.5F, 0.5F);
            this.renderer.bindTexture(LayerPiratBoatSail.TEXTURE_PIRATE_CANNON);
            LayerPiratBoatSail.MODEL_PIRAT_CANNON.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.popMatrix();
            if (entity.getVisualFlag()) {
                GlStateManager.pushMatrix();
                GlStateManager.translatef(0, -0.925F, 0.2F);
                GlStateManager.scalef(0.5F, 0.5F, 0.5F);
                GlStateManager.disableLighting();
                GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 61680.0F, 0.0F);
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
            if (this.renderer.getEntityModel().isChild) {
                GlStateManager.translatef(0.0F, 0.625F, 0.0F);
                GlStateManager.rotatef(-20.0F, -1.0F, 0.0F, 0.0F);
                float f = 0.5F;
                GlStateManager.scalef(0.5F, 0.5F, 0.5F);
            }
            Minecraft minecraft = Minecraft.getInstance();
            translateToHand(false);
            GlStateManager.rotatef(-80.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotatef(10.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translatef(0.15F, -0.4F, -0.5F);
            GlStateManager.pushMatrix();
            GlStateManager.translatef(-0.175F, 0.25F, 0.2F);
            GlStateManager.scalef(0.35F, 0.35F, 0.35F);
            this.renderer.bindTexture(TEXTURE_CHRISTMAS_CHEST);
            MODEL_CHEST.renderAll();
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PLATTER)) {
            GlStateManager.pushMatrix();
            if (this.renderer.getEntityModel().isChild) {
                GlStateManager.translatef(0.0F, 0.625F, 0.0F);
                GlStateManager.rotatef(-20.0F, -1.0F, 0.0F, 0.0F);
                float f = 0.5F;
                GlStateManager.scalef(0.5F, 0.5F, 0.5F);
            }
            Minecraft minecraft = Minecraft.getInstance();
            translateToHand(true);
            GlStateManager.rotatef(190.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef(-70.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.translatef(-0.155F, -0.225F, 0.2F);
            GlStateManager.scalef(2F, 2F, 2F);
            minecraft.getItemRenderer().renderItem(PLATTER_STACK, entity, ItemCameraTransforms.TransformType.GROUND, false);
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BUCKET) || entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BIG_BUCKET) || entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MILKER)) {
            GlStateManager.pushMatrix();
            if (this.renderer.getEntityModel().isChild) {
                GlStateManager.translatef(0.0F, 0.625F, 0.0F);
                GlStateManager.rotatef(-20.0F, -1.0F, 0.0F, 0.0F);
                float f = 0.5F;
                GlStateManager.scalef(0.5F, 0.5F, 0.5F);
            }
            Minecraft minecraft = Minecraft.getInstance();
            translateToHand(true);
            GlStateManager.rotatef(190.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef(-40.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.translatef(-0.155F, -0.225F, 0.1F);
            GlStateManager.scalef(1.75F, 1.75F, 1.75F);
            minecraft.getItemRenderer().renderItem(ItemRatUpgradeBucket.getBucketFromFluid(entity.transportingFluid), entity, ItemCameraTransforms.TransformType.GROUND, false);
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CRAFTING)) {
            Minecraft minecraft = Minecraft.getInstance();
            GlStateManager.pushMatrix();
            translateToHand(true);
            GlStateManager.rotatef(-90F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotatef(-45.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef(-90.0F, 1.0F, 0.0F, 0.0F);
            minecraft.getItemRenderer().renderItem(AXE_STACK, entity, ItemCameraTransforms.TransformType.GROUND, false);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            translateToHand(false);
            GlStateManager.rotatef(-90F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotatef(-45.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef(-90.0F, 1.0F, 0.0F, 0.0F);
            minecraft.getItemRenderer().renderItem(PICKAXE_STACK, entity, ItemCameraTransforms.TransformType.GROUND, false);
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK)) {
            Minecraft minecraft = Minecraft.getInstance();
            GlStateManager.pushMatrix();
            translateToHand(false);
            GlStateManager.rotatef(-90F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotatef(-15.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef(-90.0F, 1.0F, 0.0F, 0.0F);
            minecraft.getItemRenderer().renderItem(IRON_AXE_STACK, entity, ItemCameraTransforms.TransformType.GROUND, false);
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER)) {
            Minecraft minecraft = Minecraft.getInstance();
            GlStateManager.pushMatrix();
            translateToHand(false);
            GlStateManager.rotatef(-90F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotatef(-90.0F, 1.0F, 0.0F, 0.0F);
            minecraft.getItemRenderer().renderItem(new ItemStack(Items.DIAMOND_PICKAXE), entity, ItemCameraTransforms.TransformType.GROUND, false);
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FARMER)) {
            Minecraft minecraft = Minecraft.getInstance();
            GlStateManager.pushMatrix();
            translateToHand(false);
            GlStateManager.rotatef(-90F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotatef(-15.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef(-90.0F, 1.0F, 0.0F, 0.0F);
            minecraft.getItemRenderer().renderItem(IRON_HOE_STACK, entity, ItemCameraTransforms.TransformType.GROUND, false);
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_SHEARS)) {
            Minecraft minecraft = Minecraft.getInstance();
            GlStateManager.pushMatrix();
            translateToHand(false);
            GlStateManager.rotatef(-90F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotatef(15.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef(-90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.translatef(0.1F, 0.0F, 0.0F);
            minecraft.getItemRenderer().renderItem(SHEARS_STACK, entity, ItemCameraTransforms.TransformType.GROUND, false);
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FISHERMAN)) {
            Minecraft minecraft = Minecraft.getInstance();
            GlStateManager.pushMatrix();
            translateToHand(false);
            GlStateManager.rotatef(-180F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotatef(90F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translatef(0.2F, 0, 0.0F);
            minecraft.getItemRenderer().renderItem(FISHING_ROD_STACK, entity, ItemCameraTransforms.TransformType.GROUND, false);
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FLIGHT)) {
            GlStateManager.pushMatrix();
            Minecraft minecraft = Minecraft.getInstance();
            float wingAngle = entity.onGround ? 0 : MathHelper.sin(ageInTicks) * 30;
            float wingFold = entity.onGround ? -45 : 0;
            ((ModelRat) this.renderer.getEntityModel()).body1.postRender(0.0625F);
            ((ModelRat) this.renderer.getEntityModel()).body2.postRender(0.0625F);
            GlStateManager.pushMatrix();
            GlStateManager.translatef(0F, -0.1F, 0F);
            GlStateManager.rotatef(wingAngle, 0.0F, 0.0F, -1.0F);
            GlStateManager.rotatef(wingFold, 0.0F, 1.0F, 0.0F);
            GlStateManager.translatef(0.55F, 0, 0.2F);
            GlStateManager.rotatef(-90F, 1.0F, 0.0F, 0.0F);
            GlStateManager.scalef(2, 2, 1);
            minecraft.getItemRenderer().renderItem(WING_STACK, entity, ItemCameraTransforms.TransformType.GROUND, false);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.translatef(0F, -0.1F, 0F);
            GlStateManager.rotatef(wingAngle, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotatef(wingFold, 0.0F, -1.0F, 0.0F);
            GlStateManager.translatef(-0.55F, 0F, 0.2F);
            GlStateManager.rotatef(-90F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotatef(180F, 0.0F, 1.0F, 0.0F);
            GlStateManager.scalef(2, 2, 1);
            minecraft.getItemRenderer().renderItem(WING_STACK, entity, ItemCameraTransforms.TransformType.GROUND, false);
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DRAGON)) {
            GlStateManager.pushMatrix();
            Minecraft minecraft = Minecraft.getInstance();
            float wingAngle = entity.onGround ? 0 : MathHelper.sin(ageInTicks) * 30;
            float wingFold = entity.onGround ? -45 : 0;
            ((ModelRat) this.renderer.getEntityModel()).body1.postRender(0.0625F);
            ((ModelRat) this.renderer.getEntityModel()).body2.postRender(0.0625F);
            GlStateManager.pushMatrix();
            GlStateManager.translatef(0F, -0.1F, 0F);
            GlStateManager.rotatef(wingAngle, 0.0F, 0.0F, -1.0F);
            GlStateManager.rotatef(wingFold, 0.0F, 1.0F, 0.0F);
            GlStateManager.translatef(0.55F, 0, 0.2F);
            GlStateManager.rotatef(-90F, 1.0F, 0.0F, 0.0F);
            GlStateManager.scalef(2, 2, 1);
            minecraft.getItemRenderer().renderItem(DRAGON_WING_STACK, entity, ItemCameraTransforms.TransformType.GROUND, false);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.translatef(0F, -0.1F, 0F);
            GlStateManager.rotatef(wingAngle, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotatef(wingFold, 0.0F, -1.0F, 0.0F);
            GlStateManager.translatef(-0.55F, 0F, 0.2F);
            GlStateManager.rotatef(-90F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotatef(180F, 0.0F, 1.0F, 0.0F);
            GlStateManager.scalef(2, 2, 1);
            minecraft.getItemRenderer().renderItem(DRAGON_WING_STACK, entity, ItemCameraTransforms.TransformType.GROUND, false);
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_TNT) || entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_TNT_SURVIVOR)) {
            Minecraft minecraft = Minecraft.getInstance();
            ((ModelRat) this.renderer.getEntityModel()).body1.postRender(0.0625F);
            GlStateManager.pushMatrix();
            GlStateManager.translatef(0F, 0.1F, 0.1F);
            GlStateManager.rotatef(180, 1.0F, 0.0F, 0.0F);
            GlStateManager.scalef(2, 2, 2);
            minecraft.getItemRenderer().renderItem(TNT_STACK, entity, ItemCameraTransforms.TransformType.GROUND, false);
            GlStateManager.popMatrix();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PSYCHIC)) {
            Minecraft minecraft = Minecraft.getInstance();
            GlStateManager.pushMatrix();
            translateToHead();
            GlStateManager.translatef(0F, 0.1F, 0.035F);
            GlStateManager.rotatef(180, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotatef(180, 0.0F, 1.0F, 0.0F);
            GlStateManager.scalef(0.9F, 0.9F, 0.9F);
            minecraft.getItemRenderer().renderItem(BRAIN_STACK, entity, ItemCameraTransforms.TransformType.GROUND, false);
            GlStateManager.popMatrix();
        }
    }

    protected void translateToHead() {
        ((ModelRat) this.renderer.getEntityModel()).body1.postRender(0.0625F);
        ((ModelRat) this.renderer.getEntityModel()).body2.postRender(0.0625F);
        ((ModelRat) this.renderer.getEntityModel()).neck.postRender(0.0625F);
        ((ModelRat) this.renderer.getEntityModel()).head.postRender(0.0625F);
    }

    protected void translateToHand(boolean left) {
        ((ModelRat) this.renderer.getEntityModel()).body1.postRender(0.0625F);
        ((ModelRat) this.renderer.getEntityModel()).body2.postRender(0.0625F);
        if (left) {
            ((ModelRat) this.renderer.getEntityModel()).leftArm.postRender(0.0625F);
            ((ModelRat) this.renderer.getEntityModel()).leftHand.postRender(0.0625F);
        } else {
            ((ModelRat) this.renderer.getEntityModel()).rightArm.postRender(0.0625F);
            ((ModelRat) this.renderer.getEntityModel()).rightHand.postRender(0.0625F);
        }
    }


    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
