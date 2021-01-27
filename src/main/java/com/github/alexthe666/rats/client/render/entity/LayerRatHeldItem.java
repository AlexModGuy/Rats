package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.api.RatClientEvent;
import com.github.alexthe666.rats.client.model.ModelChristmasChest;
import com.github.alexthe666.rats.client.model.ModelRat;
import com.github.alexthe666.rats.server.blocks.RatlantisBlockRegistry;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.ItemRatUpgradeBucket;
import com.github.alexthe666.rats.server.items.RatlantisItemRegistry;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.common.MinecraftForge;

public class LayerRatHeldItem extends LayerRenderer<EntityRat, SegmentedModel<EntityRat>> {

    private static final RenderType TEXTURE_CHRISTMAS_CHEST = RenderType.getEntityCutoutNoCull(new ResourceLocation("textures/entity/chest/christmas.png"));
    private static ItemStack PLATTER_STACK = new ItemStack(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
    private static ItemStack AXE_STACK = new ItemStack(Items.STONE_AXE);
    private static ItemStack PICKAXE_STACK = new ItemStack(Items.STONE_PICKAXE);
    private static ItemStack IRON_AXE_STACK = new ItemStack(Items.IRON_AXE);
    private static ItemStack IRON_PICKAXE_STACK = new ItemStack(Items.IRON_PICKAXE);
    private static ItemStack IRON_HOE_STACK = new ItemStack(Items.IRON_HOE);
    private static ItemStack SHEARS_STACK = new ItemStack(Items.SHEARS);
    private static ItemStack TNT_STACK = new ItemStack(Blocks.TNT);
    private static ItemStack FISHING_ROD_STACK = new ItemStack(Items.FISHING_ROD);
    private static ItemStack FISHING_ROD_FUNGUS_STACK = new ItemStack(Items.WARPED_FUNGUS_ON_A_STICK);
    private static ItemStack WING_STACK = new ItemStack(RatsItemRegistry.FEATHERY_WING);
    private static ItemStack BEE_WING_STACK = new ItemStack(RatsItemRegistry.BEE_WING);
    private static ItemStack DRAGON_WING_STACK = new ItemStack(RatsItemRegistry.DRAGON_WING);
    private static ItemStack CARROT_STACK = new ItemStack(Blocks.FERN);
    private static ModelChristmasChest CHRISTMAS_CHEST_MODEL = new ModelChristmasChest();
    private final IEntityRenderer<EntityRat, SegmentedModel<EntityRat>> renderer;

    public LayerRatHeldItem(IEntityRenderer<EntityRat, SegmentedModel<EntityRat>> renderer) {
        super(renderer);
        this.renderer = renderer;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityRat entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
     if (!(this.renderer.getEntityModel() instanceof ModelRat)) {
            return;
        }
        ItemStack itemstack = entity.getHeldItem(Hand.MAIN_HAND);
        if (!itemstack.isEmpty()) {
            matrixStackIn.push();
            if (this.renderer.getEntityModel().isChild) {
                matrixStackIn.translate(0.0F, 0.625F, 0.0F);
                matrixStackIn.rotate(new Quaternion(Vector3f.XN, -20F, true));
                float f = 0.5F;
                matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            }
            Minecraft minecraft = Minecraft.getInstance();
            if (entity.shouldNotIdleAnimation()) {
                translateToHead(matrixStackIn);
                matrixStackIn.rotate(new Quaternion(Vector3f.ZP, 180F, true));
                matrixStackIn.rotate(new Quaternion(Vector3f.YP, 180F, true));
                matrixStackIn.rotate(new Quaternion(Vector3f.XP, 90F, true));
                matrixStackIn.translate(0F, 0.25F, 0.05F);
            } else {
                translateToHand(true, matrixStackIn);
                matrixStackIn.rotate(new Quaternion(Vector3f.ZP, 190F, true));
                matrixStackIn.rotate(new Quaternion(Vector3f.YP, 180F, true));
                matrixStackIn.rotate(new Quaternion(Vector3f.XP, 20F, true));
                matrixStackIn.translate(-0.155F, -0.025F, 0.125F);
                if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PLATTER)) {
                    matrixStackIn.translate(-0.15F, 0.25F, 0F);
                    if (itemstack.getItem() instanceof BlockItem) {
                        matrixStackIn.translate(0F, 0F, -0.075F);
                        matrixStackIn.rotate(new Quaternion(Vector3f.XP, -90F, true));
                    } else {
                        matrixStackIn.translate(0F, -0.1F, -0.075F);

                    }
                }
                if (entity.holdsItemInHandUpgrade()) {
                    matrixStackIn.translate(0.15F, -0.075F, 0);
                }
            }
            minecraft.getItemRenderer().renderItem(itemstack, ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }
        if (entity.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_BUCCANEER)) {
            matrixStackIn.push();
            ((ModelRat) this.renderer.getEntityModel()).body1.translateRotate(matrixStackIn);
            matrixStackIn.push();
            matrixStackIn.translate(0, -0.925F, 0.2F);
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(LayerPiratBoatSail.TEXTURE_PIRATE_CANNON));
            IVertexBuilder ivertexbuilder2 = bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(LayerPiratBoatSail.TEXTURE_PIRATE_CANNON_FIRE));
            LayerPiratBoatSail.MODEL_PIRAT_CANNON.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            if (entity.getVisualFlag()) {
                LayerPiratBoatSail.MODEL_PIRAT_CANNON.render(matrixStackIn, ivertexbuilder2, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            matrixStackIn.pop();
            matrixStackIn.pop();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CHRISTMAS)) {
            matrixStackIn.push();
            Minecraft minecraft = Minecraft.getInstance();
            translateToHand(false, matrixStackIn);
            matrixStackIn.rotate(new Quaternion(Vector3f.YP, 5F, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, 90F, true));
            matrixStackIn.push();
            matrixStackIn.translate(-0.025F, -0.2F, -0.05F);
            matrixStackIn.scale(0.35F, 0.35F, 0.35F);
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(TEXTURE_CHRISTMAS_CHEST);
            CHRISTMAS_CHEST_MODEL.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.pop();
            matrixStackIn.pop();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PLATTER)) {
            matrixStackIn.push();
            if (this.renderer.getEntityModel().isChild) {
                matrixStackIn.translate(0.0F, 0.625F, 0.0F);
                matrixStackIn.rotate(new Quaternion(Vector3f.XN, -20F, true));

                float f = 0.5F;
                matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            }
            Minecraft minecraft = Minecraft.getInstance();
            translateToHand(true, matrixStackIn);
            matrixStackIn.rotate(new Quaternion(Vector3f.ZP, 190F, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.YP, 180F, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, -70F, true));

            matrixStackIn.translate(-0.155F, -0.225F, 0.2F);
            matrixStackIn.scale(2F, 2F, 2F);
            minecraft.getItemRenderer().renderItem(PLATTER_STACK, ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BUCKET) || entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BIG_BUCKET) || entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MILKER)) {
            matrixStackIn.push();
            if (this.renderer.getEntityModel().isChild) {
                matrixStackIn.translate(0.0F, 0.625F, 0.0F);
                matrixStackIn.rotate(new Quaternion(Vector3f.XN, -20F, true));
                float f = 0.5F;
                matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            }
            Minecraft minecraft = Minecraft.getInstance();
            translateToHand(true, matrixStackIn);
            matrixStackIn.rotate(new Quaternion(Vector3f.ZP, 190F, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.YP, 180F, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, -40F, true));

            matrixStackIn.translate(-0.155F, -0.225F, 0.1F);
            matrixStackIn.scale(1.75F, 1.75F, 1.75F);
            minecraft.getItemRenderer().renderItem(ItemRatUpgradeBucket.getBucketFromFluid(entity.transportingFluid), ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CRAFTING)) {
            Minecraft minecraft = Minecraft.getInstance();
            matrixStackIn.push();
            translateToHand(true, matrixStackIn);
            matrixStackIn.rotate(new Quaternion(Vector3f.ZP, -90F, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.YP, -45F, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, -90F, true));
            minecraft.getItemRenderer().renderItem(AXE_STACK, ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
            matrixStackIn.push();
            translateToHand(false, matrixStackIn);
            matrixStackIn.rotate(new Quaternion(Vector3f.ZP, -90F, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.YP, -45F, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, -90F, true));
            minecraft.getItemRenderer().renderItem(PICKAXE_STACK, ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK)) {
            Minecraft minecraft = Minecraft.getInstance();
            matrixStackIn.push();
            translateToHand(false, matrixStackIn);
            matrixStackIn.rotate(new Quaternion(Vector3f.ZP, -90F, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.YP, -15F, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, -90F, true));
            minecraft.getItemRenderer().renderItem(IRON_AXE_STACK, ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER)) {
            Minecraft minecraft = Minecraft.getInstance();
            matrixStackIn.push();
            translateToHand(false, matrixStackIn);
            matrixStackIn.rotate(new Quaternion(Vector3f.ZP, -90F, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, -90F, true));
            minecraft.getItemRenderer().renderItem(new ItemStack(Items.DIAMOND_PICKAXE), ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BOW)) {
            Minecraft minecraft = Minecraft.getInstance();
            matrixStackIn.push();
            translateToHand(false, matrixStackIn);
            matrixStackIn.translate(0.01F, 0.1F, -0.02F);
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            minecraft.getItemRenderer().renderItem(new ItemStack(Items.BOW), ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CROSSBOW)) {
            Minecraft minecraft = Minecraft.getInstance();
            matrixStackIn.push();
            translateToHand(false, matrixStackIn);
            matrixStackIn.translate(0.01F, 0.05F, -0.1F);
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            minecraft.getItemRenderer().renderItem(new ItemStack(Items.CROSSBOW), ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER_ORE) || entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_QUARRY)) {
            Minecraft minecraft = Minecraft.getInstance();
            matrixStackIn.push();
            translateToHand(false, matrixStackIn);
            matrixStackIn.rotate(new Quaternion(Vector3f.ZP, -90F, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, -90F, true));
            minecraft.getItemRenderer().renderItem(IRON_PICKAXE_STACK, ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FARMER)) {
            Minecraft minecraft = Minecraft.getInstance();
            matrixStackIn.push();
            translateToHand(false, matrixStackIn);
            matrixStackIn.rotate(new Quaternion(Vector3f.ZP, -90F, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.YP, -15F, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, -90F, true));
            minecraft.getItemRenderer().renderItem(IRON_HOE_STACK, ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_SHEARS)) {
            Minecraft minecraft = Minecraft.getInstance();
            matrixStackIn.push();
            translateToHand(false, matrixStackIn);
            matrixStackIn.rotate(new Quaternion(Vector3f.ZP, -90F, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.YP, 15F, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, -90F, true));
            matrixStackIn.translate(0.1F, 0.0F, 0.0F);
            minecraft.getItemRenderer().renderItem(SHEARS_STACK, ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FISHERMAN)) {
            Minecraft minecraft = Minecraft.getInstance();
            matrixStackIn.push();
            translateToHand(false, matrixStackIn);
            matrixStackIn.rotate(new Quaternion(Vector3f.ZP, -180F, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.YP, 90F, true));
            matrixStackIn.translate(0.2F, 0, 0.0F);
            minecraft.getItemRenderer().renderItem(FISHING_ROD_STACK, ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_STRIDER_MOUNT)) {
            Minecraft minecraft = Minecraft.getInstance();
            matrixStackIn.push();
            translateToHand(false, matrixStackIn);
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, -30F, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.ZP, -180F, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.YP, 90F, true));
            matrixStackIn.translate(0.2F, 0, 0.0F);
            minecraft.getItemRenderer().renderItem(FISHING_ROD_FUNGUS_STACK, ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FLIGHT)) {
            matrixStackIn.push();
            Minecraft minecraft = Minecraft.getInstance();
            float wingAngle = entity.isOnGround()? 0 : MathHelper.sin(ageInTicks) * 30;
            float wingFold = entity.isOnGround()? -45 : 0;
            ((ModelRat) this.renderer.getEntityModel()).body1.translateRotate(matrixStackIn);
            ((ModelRat) this.renderer.getEntityModel()).body2.translateRotate(matrixStackIn);
            matrixStackIn.push();
            matrixStackIn.translate(0F, -0.1F, 0F);
            matrixStackIn.rotate(new Quaternion(Vector3f.ZN, wingAngle, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.YP, wingFold, true));
            matrixStackIn.translate(0.55F, 0, 0.2F);
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, -90, true));
            matrixStackIn.scale(2, 2, 1);
            minecraft.getItemRenderer().renderItem(WING_STACK, ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
            matrixStackIn.push();
            matrixStackIn.translate(0F, -0.1F, 0F);
            matrixStackIn.rotate(new Quaternion(Vector3f.ZP, wingAngle, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.YN, wingFold, true));
            matrixStackIn.translate(-0.55F, 0F, 0.2F);
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, -90, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.YP, 180, true));
            matrixStackIn.scale(2, 2, 1);
            minecraft.getItemRenderer().renderItem(WING_STACK, ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
            matrixStackIn.pop();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BEE)) {
            matrixStackIn.push();
            Minecraft minecraft = Minecraft.getInstance();
            float wingAngle = entity.isOnGround()? 0 : MathHelper.sin(ageInTicks) * 60;
            float wingFold = entity.isOnGround()? -45 : 0;
            ((ModelRat) this.renderer.getEntityModel()).body1.translateRotate(matrixStackIn);
            ((ModelRat) this.renderer.getEntityModel()).body2.translateRotate(matrixStackIn);
            matrixStackIn.push();
            matrixStackIn.translate(0F, -0.1F, 0F);
            matrixStackIn.rotate(new Quaternion(Vector3f.ZN, wingAngle, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.YP, wingFold, true));
            matrixStackIn.translate(0.75F, 0, 0.4F);
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, -90, true));
            matrixStackIn.scale(3, 3, 1);
            minecraft.getItemRenderer().renderItem(BEE_WING_STACK, ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
            matrixStackIn.push();
            matrixStackIn.translate(0F, -0.1F, 0F);
            matrixStackIn.rotate(new Quaternion(Vector3f.ZP, wingAngle, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.YN, wingFold, true));
            matrixStackIn.translate(-0.75F, 0F, 0.4F);
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, -90, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.YP, 180, true));
            matrixStackIn.scale(3, 3, 1);
            minecraft.getItemRenderer().renderItem(BEE_WING_STACK, ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
            matrixStackIn.pop();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DRAGON)) {
            matrixStackIn.push();
            Minecraft minecraft = Minecraft.getInstance();
            float wingAngle = entity.isOnGround()? 0 : MathHelper.sin(ageInTicks) * 30;
            float wingFold = entity.isOnGround()? -45 : 0;
            ((ModelRat) this.renderer.getEntityModel()).body1.translateRotate(matrixStackIn);
            ((ModelRat) this.renderer.getEntityModel()).body2.translateRotate(matrixStackIn);
            matrixStackIn.push();
            matrixStackIn.translate(0F, -0.1F, 0F);
            matrixStackIn.rotate(new Quaternion(Vector3f.ZN, wingAngle, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.YP, wingFold, true));
            matrixStackIn.translate(0.55F, 0, 0.2F);
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, -90, true));
            matrixStackIn.scale(2, 2, 1);
            minecraft.getItemRenderer().renderItem(DRAGON_WING_STACK, ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
            matrixStackIn.push();
            matrixStackIn.translate(0F, -0.1F, 0F);
            matrixStackIn.rotate(new Quaternion(Vector3f.ZP, wingAngle, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.YN, wingFold, true));
            matrixStackIn.translate(-0.55F, 0F, 0.2F);
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, -90, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.YP, 180, true));
            matrixStackIn.scale(2, 2, 1);
            minecraft.getItemRenderer().renderItem(DRAGON_WING_STACK, ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
            matrixStackIn.pop();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_TNT) || entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_TNT_SURVIVOR)) {
            Minecraft minecraft = Minecraft.getInstance();
            ((ModelRat) this.renderer.getEntityModel()).body1.translateRotate(matrixStackIn);
            matrixStackIn.push();
            matrixStackIn.translate(0F, 0.1F, 0.1F);
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, 180F, true));
            matrixStackIn.scale(2, 2, 2);
            minecraft.getItemRenderer().renderItem(TNT_STACK, ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }
        if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CARRAT)) {
            Minecraft minecraft = Minecraft.getInstance();
            ((ModelRat) this.renderer.getEntityModel()).body1.translateRotate(matrixStackIn);
            matrixStackIn.push();
            matrixStackIn.translate(0F, -0.05F, 0.5F);
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, 90F, true));
            matrixStackIn.scale(2, 2, 2);
            minecraft.getItemRenderer().renderItem(CARROT_STACK, ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }
        if (RatsMod.RATLANTIS_LOADED && entity.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_PSYCHIC)) {
            Minecraft minecraft = Minecraft.getInstance();
            matrixStackIn.push();
            translateToHead(matrixStackIn);
            matrixStackIn.translate(0F, 0.1F, 0.035F);
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, 180F, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.YP, 180F, true));
            matrixStackIn.scale(0.9F, 0.9F, 0.9F);
            minecraft.getItemRenderer().renderItem(new ItemStack(RatlantisBlockRegistry.BRAIN_BLOCK), ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }
        RatClientEvent.RatItemTranslation itemEvent = new RatClientEvent.RatItemTranslation(entity, (RenderRat) renderer, matrixStackIn);
        MinecraftForge.EVENT_BUS.post(itemEvent);

    }

    protected void translateToHead(MatrixStack matrixStack) {
        ((ModelRat) this.renderer.getEntityModel()).body1.translateRotate(matrixStack);
        ((ModelRat) this.renderer.getEntityModel()).body2.translateRotate(matrixStack);
        ((ModelRat) this.renderer.getEntityModel()).neck.translateRotate(matrixStack);
        ((ModelRat) this.renderer.getEntityModel()).head.translateRotate(matrixStack);
    }

    protected void translateToHand(boolean left, MatrixStack matrixStack) {
        ((ModelRat) this.renderer.getEntityModel()).body1.translateRotate(matrixStack);
        ((ModelRat) this.renderer.getEntityModel()).body2.translateRotate(matrixStack);
        if (left) {
            ((ModelRat) this.renderer.getEntityModel()).leftArm.translateRotate(matrixStack);
            ((ModelRat) this.renderer.getEntityModel()).leftHand.translateRotate(matrixStack);
        } else {
            ((ModelRat) this.renderer.getEntityModel()).rightArm.translateRotate(matrixStack);
            ((ModelRat) this.renderer.getEntityModel()).rightHand.translateRotate(matrixStack);
        }
    }
}
