package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRat;
import com.github.alexthe666.rats.server.entity.EntityGhostPirat;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.ItemChefToque;
import com.github.alexthe666.rats.server.items.ItemPiperHat;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ArmorLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
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
public class LayerRatHelmet<T extends EntityRat, M extends ModelRat<T>> extends LayerRenderer<T, M> {
    private float alpha = 1.0F;
    private float colorR = 1.0F;
    private float colorG = 1.0F;
    private float colorB = 1.0F;
    private final IEntityRenderer<T, M> renderer;
    private final BipedModel defaultBipedModel = new BipedModel(1.0F);
    public LayerRatHelmet(IEntityRenderer<T, M> rendererIn) {
        super(rendererIn);
        this.renderer = rendererIn;
    }

    public void render(T rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.pushMatrix();

        if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_GOD)) {
            renderer.getEntityModel().render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            ArmorLayer.func_215338_a(this::bindTexture, rat, renderer.getEntityModel(), limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.color3f(1.0F, 1.0F, 1.0F);
        }
        if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_NONBELIEVER)) {
            renderer.getEntityModel().render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            renderEnchanted(this::bindTexture, rat, renderer.getEntityModel(), limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, 0, 1, 0);
            GlStateManager.color3f(1.0F, 1.0F, 1.0F);
        }

        ItemStack itemstack = rat.getItemStackFromSlot(EquipmentSlotType.HEAD);
        if (itemstack.getItem() instanceof ArmorItem) {
            ArmorItem armoritem = (ArmorItem)itemstack.getItem();
            if (armoritem.getEquipmentSlot() == EquipmentSlotType.HEAD) {
                BipedModel a = defaultBipedModel;
                a = getArmorModelHook(rat, itemstack, EquipmentSlotType.HEAD, a);
                this.setModelSlotVisible(a, EquipmentSlotType.HEAD);
                boolean flag = false;
                ((ModelRat) this.renderer.getEntityModel()).body1.postRender(0.0625F);
                ((ModelRat) this.renderer.getEntityModel()).body2.postRender(0.0625F);
                ((ModelRat) this.renderer.getEntityModel()).neck.postRender(0.0625F);
                ((ModelRat) this.renderer.getEntityModel()).head.postRender(0.0625F);
                GlStateManager.translatef(0, -0.375F, -0.045F);
                GlStateManager.rotatef(0, 1, 0, 0);
                GlStateManager.scalef(0.55F, 0.55F, 0.55F);
                if (itemstack.getItem() instanceof ItemChefToque) {
                    GlStateManager.rotatef(-25, 1, 0, 0);
                    GlStateManager.translatef(0, 0.1F, 0.3F);
                }
                if (itemstack.getItem() instanceof ItemPiperHat) {
                    GlStateManager.rotatef(-10, 1, 0, 0);
                    GlStateManager.translatef(0, 0.1F, 0.1F);
                }
                if (itemstack.getItem() == RatsItemRegistry.PIRAT_HAT) {
                    GlStateManager.rotatef(-5, 1, 0, 0);
                    GlStateManager.translatef(0, -0.125F, 0);
                    GlStateManager.scalef(1.425F, 1.425F, 1.425F);
                }
                if(itemstack.getItem() == RatsItemRegistry.GHOST_PIRAT_HAT){
                    float piratScale = rat instanceof EntityGhostPirat ? 1.1F : 1.425F;
                    float piratTranslate = rat instanceof EntityGhostPirat ? 0.05F : -0.125F;
                    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.3F);
                    GlStateManager.rotatef(-5, 1, 0, 0);
                    GlStateManager.translatef(0, piratTranslate, 0);
                    GlStateManager.scalef(piratScale, piratScale, piratScale);
                }
                if (itemstack.getItem() == RatsItemRegistry.ARCHEOLOGIST_HAT) {
                    GlStateManager.rotatef(-5, 1, 0, 0);
                    GlStateManager.scalef(1.425F, 1.425F, 1.425F);
                }
                if (itemstack.getItem() == RatsItemRegistry.FARMER_HAT || itemstack.getItem() == RatsItemRegistry.FISHERMAN_HAT) {
                    GlStateManager.rotatef(-5, 1, 0, 0);
                    GlStateManager.scalef(1.425F, 1.425F, 1.425F);
                }
                if (itemstack.getItem() == RatsItemRegistry.PLAGUE_DOCTOR_MASK || itemstack.getItem() == RatsItemRegistry.BLACK_DEATH_MASK) {
                    GlStateManager.rotatef(-15, 1, 0, 0);
                    GlStateManager.translatef(0, 0.0F, 0);
                    GlStateManager.scalef(1.5F, 1.2F, 1.5F);
                }
                if (itemstack.getItem() == RatsItemRegistry.RAT_FEZ) {
                    GlStateManager.translatef(-0.15F, -0.05F, -0.1F);
                    GlStateManager.scalef(1.425F, 1.425F, 1.425F);
                }
                if (itemstack.getItem() == RatsItemRegistry.TOP_HAT) {
                    GlStateManager.rotatef(-5, 1, 0, 0);
                    GlStateManager.scalef(1.425F, 1.425F, 1.425F);
                }
                if (itemstack.getItem() == RatsItemRegistry.SANTA_HAT) {
                    GlStateManager.rotatef(-5, 1, 0, 0);
                    GlStateManager.translatef(0, 0, 0.1F);
                    GlStateManager.scalef(1.25F, 1.25F, 1.25F);
                }
                this.bindTexture(this.getArmorResource(rat, itemstack, EquipmentSlotType.HEAD, null));
                if (armoritem instanceof net.minecraft.item.IDyeableArmorItem) { // Allow this for anything, not only cloth
                    int i = ((DyeableArmorItem)armoritem).getColor(itemstack);
                    float f = (float)(i >> 16 & 255) / 255.0F;
                    float f1 = (float)(i >> 8 & 255) / 255.0F;
                    float f2 = (float)(i & 255) / 255.0F;
                    GlStateManager.color4f(this.colorR * f, this.colorG * f1, this.colorB * f2, this.alpha);
                    a.render(rat, limbSwing, limbSwingAmount, ageInTicks, 0, 0, scale);
                    this.bindTexture(this.getArmorResource(rat, itemstack, EquipmentSlotType.HEAD, "overlay"));
                }
                GlStateManager.color4f(this.colorR, this.colorG, this.colorB, this.alpha);
                a.render(rat, limbSwing, limbSwingAmount, ageInTicks, 0, 0, scale);
                if (itemstack.hasEffect()) {
                    ArmorLayer.func_215338_a(this::bindTexture, rat, a, limbSwing, limbSwingAmount, partialTicks, ageInTicks, 0, 0, scale);
                    GlStateManager.color3f(1.0F, 1.0F, 1.0F);
                }

            }
        }else if (itemstack.getItem() instanceof BannerItem) {
            ((ModelRat) this.renderer.getEntityModel()).body1.postRender(0.0625F);
            ((ModelRat) this.renderer.getEntityModel()).body2.postRender(0.0625F);
            GlStateManager.translatef(0, -0.5F, -0.2F);
            GlStateManager.rotatef(180, 0, 0, 1);
            float sitProgress = rat.sitProgress / 20F;
            GlStateManager.rotatef(sitProgress * -40, 1, 0, 0);
            GlStateManager.translatef(0, 0, -sitProgress * 0.04F);
            GlStateManager.scalef(1.7F, 1.7F, 1.7F);
            Minecraft.getInstance().getItemRenderer().renderItem(itemstack, rat , ItemCameraTransforms.TransformType.FIXED, false);
        } else {
            ((ModelRat) this.renderer.getEntityModel()).body1.postRender(0.0625F);
            ((ModelRat) this.renderer.getEntityModel()).body2.postRender(0.0625F);
            ((ModelRat) this.renderer.getEntityModel()).neck.postRender(0.0625F);
            ((ModelRat) this.renderer.getEntityModel()).head.postRender(0.0625F);
            GlStateManager.translatef(0, 0.025F, -0.2F);
            GlStateManager.rotatef(180, 1, 0, 0);
            GlStateManager.rotatef(180, 0, 1, 0);
            GlStateManager.scalef(0.8F, 0.8F, 0.8F);
            Minecraft.getInstance().getItemRenderer().renderItem(itemstack, rat , ItemCameraTransforms.TransformType.FIXED, false);
        }
        GlStateManager.popMatrix();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();

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

    protected BipedModel<?> getArmorModelHook(LivingEntity entity, ItemStack itemStack, EquipmentSlotType slot, BipedModel model) {
        return net.minecraftforge.client.ForgeHooksClient.getArmorModel(entity, itemStack, slot, model);
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

    protected static final ResourceLocation ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    public static <T extends Entity> void renderEnchanted(Consumer<ResourceLocation> p_215338_0_, T p_215338_1_, EntityModel<T> p_215338_2_, float p_215338_3_, float p_215338_4_, float p_215338_5_, float p_215338_6_, float p_215338_7_, float p_215338_8_, float p_215338_9_, float r, float g, float b) {
        float f = (float)p_215338_1_.ticksExisted + p_215338_5_;
        p_215338_0_.accept(ENCHANTED_ITEM_GLINT_RES);
        GameRenderer gamerenderer = Minecraft.getInstance().gameRenderer;
        gamerenderer.setupFogColor(true);
        GlStateManager.enableBlend();
        GlStateManager.depthFunc(514);
        GlStateManager.depthMask(false);
        float f1 = 0.5F;
        GlStateManager.color4f(0.5F, 0.5F, 0.5F, 1.0F);

        for(int i = 0; i < 2; ++i) {
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
            float f2 = 0.76F;
            GlStateManager.color4f(r, g, b, 1.0F);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f3 = 0.33333334F;
            GlStateManager.scalef(0.33333334F, 0.33333334F, 0.33333334F);
            GlStateManager.rotatef(30.0F - (float)i * 60.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translatef(0.0F, f * (0.001F + (float)i * 0.003F) * 20.0F, 0.0F);
            GlStateManager.matrixMode(5888);
            p_215338_2_.render(p_215338_1_, p_215338_3_, p_215338_4_, p_215338_6_, p_215338_7_, p_215338_8_, p_215338_9_);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }

        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(515);
        GlStateManager.disableBlend();
        gamerenderer.setupFogColor(false);
    }
}