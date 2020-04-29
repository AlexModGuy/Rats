package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRat;
import com.github.alexthe666.rats.client.render.type.RatsRenderType;
import com.github.alexthe666.rats.server.entity.EntityGhostPirat;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.ItemChefToque;
import com.github.alexthe666.rats.server.items.ItemPiperHat;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.VertexBuilderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ArmorLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BannerItem;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class LayerRatHelmet<T extends EntityRat> extends LayerRenderer<T, ModelRat<T>> {
    private float alpha = 1.0F;
    private float colorR = 1.0F;
    private float colorG = 1.0F;
    private float colorB = 1.0F;
    private final IEntityRenderer<T, ModelRat<T>> renderer;
    private final BipedModel defaultBipedModel = new BipedModel(1.0F);
    private static final BipedModel backup = new BipedModel(1);
    public LayerRatHelmet(IEntityRenderer<T, ModelRat<T>> rendererIn) {
        super(rendererIn);
        this.renderer = rendererIn;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!(this.renderer.getEntityModel() instanceof ModelRat)) {
            return;
        }
        matrixStackIn.push();

        if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_GOD)) {
            IVertexBuilder vertexBuilder = ItemRenderer.getBuffer(bufferIn, RenderType.getEntityCutoutNoCull(renderer.getEntityTexture(rat)), false, true);
            renderer.getEntityModel().render(matrixStackIn, vertexBuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_NONBELIEVER)) {
            IVertexBuilder vertexBuilder = bufferIn.getBuffer(RatsRenderType.GREEN_ENTITY_GLINT);
            renderer.getEntityModel().render(matrixStackIn, vertexBuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }

        ItemStack itemstack = rat.getItemStackFromSlot(EquipmentSlotType.HEAD);
        if (itemstack.getItem() instanceof ArmorItem) {
            ArmorItem armoritem = (ArmorItem)itemstack.getItem();
            if (armoritem.getEquipmentSlot() == EquipmentSlotType.HEAD) {
                BipedModel a = defaultBipedModel;
                a = getArmorModelHook(rat, itemstack, EquipmentSlotType.HEAD, a);
                this.setModelSlotVisible(a, EquipmentSlotType.HEAD);
                boolean flag = false;
                ((ModelRat) this.renderer.getEntityModel()).body1.translateRotate(matrixStackIn);
                ((ModelRat) this.renderer.getEntityModel()).body2.translateRotate(matrixStackIn);
                ((ModelRat) this.renderer.getEntityModel()).neck.translateRotate(matrixStackIn);
                ((ModelRat) this.renderer.getEntityModel()).head.translateRotate(matrixStackIn);
                matrixStackIn.translate(0, -0.375F, -0.045F);
                matrixStackIn.scale(0.55F, 0.55F, 0.55F);
                if (itemstack.getItem() instanceof ItemChefToque) {
                    matrixStackIn.rotate(new Quaternion(Vector3f.XP, -25, true));
                    matrixStackIn.translate(0, 0.1F, 0.3F);
                }
                if (itemstack.getItem() instanceof ItemPiperHat) {
                    matrixStackIn.rotate(new Quaternion(Vector3f.XP, -10, true));
                    matrixStackIn.translate(0, 0.1F, 0.1F);
                }
                if (itemstack.getItem() == RatsItemRegistry.PIRAT_HAT) {
                    matrixStackIn.rotate(new Quaternion(Vector3f.XP, -5, true));
                    matrixStackIn.translate(0, -0.125F, 0);
                    matrixStackIn.scale(1.425F, 1.425F, 1.425F);
                }
                if(itemstack.getItem() == RatsItemRegistry.GHOST_PIRAT_HAT){
                    float piratScale = rat instanceof EntityGhostPirat ? 1.1F : 1.425F;
                    float piratTranslate = rat instanceof EntityGhostPirat ? 0.05F : -0.125F;
                    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.3F);
                    matrixStackIn.rotate(new Quaternion(Vector3f.XP, -5, true));
                    matrixStackIn.translate(0, piratTranslate, 0);
                    matrixStackIn.scale(piratScale, piratScale, piratScale);
                }
                if (itemstack.getItem() == RatsItemRegistry.ARCHEOLOGIST_HAT) {
                    matrixStackIn.rotate(new Quaternion(Vector3f.XP, -5, true));
                    matrixStackIn.scale(1.425F, 1.425F, 1.425F);
                }
                if (itemstack.getItem() == RatsItemRegistry.FARMER_HAT || itemstack.getItem() == RatsItemRegistry.FISHERMAN_HAT) {
                    matrixStackIn.rotate(new Quaternion(Vector3f.XP, -5, true));
                    matrixStackIn.scale(1.425F, 1.425F, 1.425F);
                }
                if (itemstack.getItem() == RatsItemRegistry.PLAGUE_DOCTOR_MASK || itemstack.getItem() == RatsItemRegistry.BLACK_DEATH_MASK) {
                    matrixStackIn.rotate(new Quaternion(Vector3f.XP, -15, true));
                    matrixStackIn.translate(0, 0.0F, 0);
                    matrixStackIn.scale(1.5F, 1.2F, 1.5F);
                }
                if (itemstack.getItem() == RatsItemRegistry.RAT_FEZ) {
                    matrixStackIn.translate(-0.15F, -0.05F, -0.1F);
                    matrixStackIn.scale(1.425F, 1.425F, 1.425F);
                }
                if (itemstack.getItem() == RatsItemRegistry.TOP_HAT) {
                    matrixStackIn.rotate(new Quaternion(Vector3f.XP, -5, true));
                    matrixStackIn.scale(1.425F, 1.425F, 1.425F);
                }
                if (itemstack.getItem() == RatsItemRegistry.SANTA_HAT) {
                    matrixStackIn.rotate(new Quaternion(Vector3f.XP, -5, true));
                    matrixStackIn.translate(0, 0, 0.1F);
                    matrixStackIn.scale(1.25F, 1.25F, 1.25F);
                }
                if (itemstack.getItem() == RatsItemRegistry.PARTY_HAT_1 || itemstack.getItem() == RatsItemRegistry.PARTY_HAT_2 || itemstack.getItem() == RatsItemRegistry.PARTY_HAT_3 || itemstack.getItem() == RatsItemRegistry.PARTY_HAT_4) {
                    matrixStackIn.translate(0, 0.075F, -0.05F);
                    matrixStackIn.scale(1.25F, 1.25F, 1.25F);
                }
                if (itemstack.getItem() == RatsItemRegistry.MILITARY_HAT) {
                    matrixStackIn.rotate(new Quaternion(Vector3f.XP, -5, true));
                    matrixStackIn.scale(1.425F, 1.425F, 1.425F);
                }
                if (itemstack.getItem() == RatsItemRegistry.RAT_KING_CROWN) {
                    matrixStackIn.rotate(new Quaternion(Vector3f.XP, -5, true));
                    matrixStackIn.translate(0, 0.075F, 0F);
                    matrixStackIn.scale(1.25F, 1.25F, 1.25F);
                }
                boolean flag1 = itemstack.hasEffect();
                if (armoritem instanceof net.minecraft.item.IDyeableArmorItem) { // Allow this for anything, not only cloth
                    int i = ((net.minecraft.item.IDyeableArmorItem)armoritem).getColor(itemstack);
                    float f = (float)(i >> 16 & 255) / 255.0F;
                    float f1 = (float)(i >> 8 & 255) / 255.0F;
                    float f2 = (float)(i & 255) / 255.0F;
                    renderArmor(matrixStackIn, bufferIn, packedLightIn, flag1, a, f, f1, f2, this.getArmorResource(rat, itemstack, EquipmentSlotType.HEAD, null));
                    renderArmor(matrixStackIn, bufferIn, packedLightIn, flag1, a, 1.0F, 1.0F, 1.0F, this.getArmorResource(rat, itemstack, EquipmentSlotType.HEAD, "overlay"));
                } else {
                    renderArmor(matrixStackIn, bufferIn, packedLightIn, flag1, a, 1.0F, 1.0F, 1.0F, this.getArmorResource(rat, itemstack, EquipmentSlotType.HEAD, null));
                }

            }
        }else if (itemstack.getItem() instanceof BannerItem) {
            ((ModelRat) this.renderer.getEntityModel()).body1.translateRotate(matrixStackIn);
            ((ModelRat) this.renderer.getEntityModel()).body2.translateRotate(matrixStackIn);
            matrixStackIn.translate(0, -0.5F, -0.2F);
            matrixStackIn.rotate(new Quaternion(Vector3f.ZP, 180, true));
            float sitProgress = rat.sitProgress / 20F;
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, sitProgress * -40, true));
            matrixStackIn.translate(0, 0, -sitProgress * 0.04F);
            matrixStackIn.scale(1.7F, 1.7F, 1.7F);
            Minecraft.getInstance().getItemRenderer().renderItem(itemstack, ItemCameraTransforms.TransformType.FIXED, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
        } else {
            ((ModelRat) this.renderer.getEntityModel()).body1.translateRotate(matrixStackIn);
            ((ModelRat) this.renderer.getEntityModel()).body2.translateRotate(matrixStackIn);
            ((ModelRat) this.renderer.getEntityModel()).neck.translateRotate(matrixStackIn);
            ((ModelRat) this.renderer.getEntityModel()).head.translateRotate(matrixStackIn);
            matrixStackIn.translate(0, 0.025F, -0.2F);
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, 180, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.YP, 180, true));
            matrixStackIn.scale(0.8F, 0.8F, 0.8F);
            Minecraft.getInstance().getItemRenderer().renderItem(itemstack, ItemCameraTransforms.TransformType.FIXED, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
        }
        matrixStackIn.pop();
    }

    private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();

    private void renderArmor(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, boolean glintIn, BipedModel modelIn, float red, float green, float blue, ResourceLocation armorResource) {
        IVertexBuilder ivertexbuilder = ItemRenderer.getBuffer(bufferIn, RenderType.getEntityCutoutNoCull(armorResource), false, glintIn);
        modelIn.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
    }

    public static ResourceLocation getArmorResource(net.minecraft.entity.Entity entity, ItemStack stack, EquipmentSlotType slot, @javax.annotation.Nullable String type) {
        ArmorItem item = (ArmorItem)stack.getItem();
        String texture = item.getArmorMaterial().getName();
        String domain = "minecraft";
        int idx = texture.indexOf(':');
        if (idx != -1)
        {
            domain = texture.substring(0, idx);
            texture = texture.substring(idx + 1);
        }
        String s1 = String.format("%s:textures/models/armor/%s_layer_%d%s.png", domain, texture, (1), type == null ? "" : String.format("_%s", type));

        s1 = net.minecraftforge.client.ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
        ResourceLocation resourcelocation = (ResourceLocation)ARMOR_TEXTURE_RES_MAP.get(s1);

        if (resourcelocation == null)
        {
            resourcelocation = new ResourceLocation(s1);
            ARMOR_TEXTURE_RES_MAP.put(s1, resourcelocation);
        }

        return resourcelocation;
    }

    protected void setModelSlotVisible(BipedModel p_188359_1_, EquipmentSlotType slotIn) {
        this.setModelVisible(p_188359_1_);
        switch (slotIn) {
            case HEAD:
                p_188359_1_.bipedHead.showModel = true;
                p_188359_1_.bipedHeadwear.showModel = true;
                break;
            case CHEST:
                p_188359_1_.bipedBody.showModel = true;
                p_188359_1_.bipedRightArm.showModel = true;
                p_188359_1_.bipedLeftArm.showModel = true;
                break;
            case LEGS:
                p_188359_1_.bipedBody.showModel = true;
                p_188359_1_.bipedRightLeg.showModel = true;
                p_188359_1_.bipedLeftLeg.showModel = true;
                break;
            case FEET:
                p_188359_1_.bipedRightLeg.showModel = true;
                p_188359_1_.bipedLeftLeg.showModel = true;
        }
    }

    protected void setModelVisible(BipedModel model) {
        model.setVisible(false);

    }


    protected BipedModel<?> getArmorModelHook(LivingEntity entity, ItemStack itemStack, EquipmentSlotType slot, BipedModel model) {
        return net.minecraftforge.client.ForgeHooksClient.getArmorModel(entity, itemStack, slot, model);
    }
}