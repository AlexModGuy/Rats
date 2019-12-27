package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.ModelPinkie;
import com.github.alexthe666.rats.client.model.ModelRat;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class RenderRat extends MobRenderer<EntityRat, ModelRat<EntityRat>> {

    private static final Map<String, ResourceLocation> LAYERED_LOCATION_CACHE = Maps.newHashMap();
    private static final ModelRat RAT_MODEL = new ModelRat(0.0F);
    private static final ModelPinkie PINKIE_MODEL = new ModelPinkie();
    private static final ResourceLocation PINKIE_TEXTURE = new ResourceLocation("rats:textures/entity/rat/baby.png");
    private static final ResourceLocation ENDER_UPGRADE_TEXTURE = new ResourceLocation("rats:textures/entity/rat/rat_ender_upgrade.png");
    private static final ResourceLocation AQUATIC_UPGRADE_TEXTURE = new ResourceLocation("rats:textures/entity/rat/rat_aquatic_upgrade.png");
    private static final ResourceLocation DRAGON_UPGRADE_TEXTURE = new ResourceLocation("rats:textures/entity/rat/rat_dragon_upgrade.png");
    private static final ResourceLocation JULIAN = new ResourceLocation("rats:textures/entity/rat/patreon/rat_julian.png");
    private static final ResourceLocation SHIZUKA = new ResourceLocation("rats:textures/entity/rat/patreon/rat_shizuka.png");
    private static final ResourceLocation SHARVA = new ResourceLocation("rats:textures/entity/rat/patreon/rat_sharva.png");

    public RenderRat() {
        super(Minecraft.getInstance().getRenderManager(), RAT_MODEL, 0.15F);
        this.addLayer(new LayerRatPlague(this));
        this.addLayer(new LayerRatEyes(this));
        this.addLayer(new LayerRatHelmet(this));
        this.addLayer(new LayerRatHeldItem(this));
    }

    protected boolean canRenderName(EntityRat entity) {
        return RatsMod.PROXY.shouldRenderNameplates() && super.canRenderName(entity);
    }

    protected void renderModel(EntityRat rat, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        boolean flag = this.isVisible(rat);
        boolean flag1 = !flag && !rat.isInvisibleToPlayer(Minecraft.getInstance().player);
        if (flag || flag1) {
            if (!this.bindEntityTexture(rat)) {
                return;
            }

            if (flag1) {
                GlStateManager.setProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }
            EntityModel model;
            if (rat.isChild()) {
                model = PINKIE_MODEL;
            } else {
                model = RAT_MODEL;
            }
            this.entityModel.render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            if (flag1) {
                GlStateManager.unsetProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }
        }
    }


    public boolean shouldRender(EntityRat rat, ICamera camera, double camX, double camY, double camZ) {
        if (rat.isPassenger() && rat.getRidingEntity() != null && rat.getRidingEntity().getPassengers().size() >= 1 && rat.getRidingEntity().getPassengers().get(0) == rat && rat.getRidingEntity() instanceof LivingEntity) {
            if (((LivingEntity) rat.getRidingEntity()).getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == RatsItemRegistry.CHEF_TOQUE) {
                return false;
            }
        }
        return super.shouldRender(rat, camera, camX, camY, camZ);
    }

    protected void preRenderCallback(EntityRat rat, float partialTickTime) {
        GL11.glScaled(0.6F, 0.6F, 0.6F);
        if (rat.isPassenger() && rat.getRidingEntity() != null && rat.getRidingEntity().getPassengers().size() >= 1 && rat.getRidingEntity() instanceof PlayerEntity) {
            Entity riding = rat.getRidingEntity();
            if (riding.getPassengers().get(0) != null && riding.getPassengers().get(0) == rat) {
                EntityRenderer playerRender = Minecraft.getInstance().getRenderManager().getRenderer(riding);
                if (playerRender instanceof LivingRenderer && ((LivingRenderer) playerRender).getEntityModel() instanceof BipedModel) {
                    ((BipedModel) ((LivingRenderer) playerRender).getEntityModel()).bipedHead.postRender(0.0625F);
                    GlStateManager.translatef(0.0F, -0.7F, 0.25F);
                }
            }
        } else {
            float f7 = rat.prevFlyingPitch + (rat.flyingPitch - rat.prevFlyingPitch) * partialTickTime;
            GL11.glRotatef(rat.flyingPitch, 1, 0, 0);
        }

    }

    protected ResourceLocation getEntityTexture(EntityRat entity) {
        String s = entity.getRatTexture();
        if (entity.isChild()) {
            return PINKIE_TEXTURE;
        } else {
            if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DRAGON)) {
                return DRAGON_UPGRADE_TEXTURE;
            }
            if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ENDER)) {
                return ENDER_UPGRADE_TEXTURE;
            }
            if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_AQUATIC)) {
                return AQUATIC_UPGRADE_TEXTURE;
            }
            if (entity.getCustomName() != null) {
                String name = entity.getCustomName().getFormattedText();
                if (name.contains("julian") || name.contains("Julian")) {
                    return JULIAN;
                }
                if (name.contains("shizuka") || name.contains("Shizuka")) {
                    return SHIZUKA;
                }
                if (name.contains("sharva") || name.contains("Sharva")) {
                    return SHARVA;
                }
            }
            ResourceLocation resourcelocation = LAYERED_LOCATION_CACHE.get(s);
            if (resourcelocation == null) {
                resourcelocation = new ResourceLocation(s);
                //Minecraft.getInstance().getTextureManager().loadTexture(resourcelocation, new LayeredTexture(entity.getVariantTexturePaths()));
                LAYERED_LOCATION_CACHE.put(s, resourcelocation);
            }
            return resourcelocation;
        }
    }

    protected float getDeathMaxRotation(EntityRat rat) {
        return rat.isDeadInTrap ? 0 : 90;
    }

}
