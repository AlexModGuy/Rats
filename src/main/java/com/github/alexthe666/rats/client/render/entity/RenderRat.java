package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.ModelPinkie;
import com.github.alexthe666.rats.client.model.ModelRat;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class RenderRat extends RenderLiving<EntityRat> {

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
        super(Minecraft.getMinecraft().getRenderManager(), RAT_MODEL, 0.15F);
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
        boolean flag1 = !flag && !rat.isInvisibleToPlayer(Minecraft.getMinecraft().player);
        if (flag || flag1) {
            if (!this.bindEntityTexture(rat)) {
                return;
            }
            if (flag1) {
                GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }
            if (rat.isChild()) {
                mainModel = PINKIE_MODEL;
            } else {
                mainModel = RAT_MODEL;
            }
            this.mainModel.render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            if (flag1) {
                GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }
        }
    }


    public boolean shouldRender(EntityRat rat, ICamera camera, double camX, double camY, double camZ) {
        if (rat.isRiding() && rat.getRidingEntity() != null && rat.getRidingEntity().getPassengers().size() >= 1 && rat.getRidingEntity().getPassengers().get(0) == rat && rat.getRidingEntity() instanceof EntityLivingBase) {
            if (((EntityLivingBase) rat.getRidingEntity()).getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == RatsItemRegistry.CHEF_TOQUE) {
                return false;
            }
        }
        return super.shouldRender(rat, camera, camX, camY, camZ);
    }

    protected void preRenderCallback(EntityRat rat, float partialTickTime) {
        GL11.glScaled(0.6F, 0.6F, 0.6F);
        if (rat.isRiding() && rat.getRidingEntity() != null && rat.getRidingEntity().getPassengers().size() >= 1 && rat.getRidingEntity() instanceof PlayerEntity) {
            Entity riding = rat.getRidingEntity();
            if (riding.getPassengers().get(0) != null && riding.getPassengers().get(0) == rat) {
                Render playerRender = Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(riding);
                if (playerRender instanceof RenderLivingBase && ((RenderLivingBase) playerRender).getMainModel() instanceof ModelBiped) {
                    ((ModelBiped) ((RenderLivingBase) playerRender).getMainModel()).bipedHead.postRender(0.0625F);
                    GlStateManager.translate(0.0F, -0.7F, 0.25);
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
            if (!entity.getCustomNameTag().isEmpty()) {
                if (entity.getCustomNameTag().contains("julian") || entity.getCustomNameTag().contains("Julian")) {
                    return JULIAN;
                }
                if (entity.getCustomNameTag().contains("shizuka") || entity.getCustomNameTag().contains("Shizuka")) {
                    return SHIZUKA;
                }
                if (entity.getCustomNameTag().contains("sharva") || entity.getCustomNameTag().contains("Sharva")) {
                    return SHARVA;
                }
            }
            ResourceLocation resourcelocation = LAYERED_LOCATION_CACHE.get(s);
            if (resourcelocation == null) {
                resourcelocation = new ResourceLocation(s);
                //Minecraft.getMinecraft().getTextureManager().loadTexture(resourcelocation, new LayeredTexture(entity.getVariantTexturePaths()));
                LAYERED_LOCATION_CACHE.put(s, resourcelocation);
            }
            return resourcelocation;
        }
    }

    protected float getDeathMaxRotation(EntityRat rat) {
        return rat.isDeadInTrap ? 0 : 90;
    }

}
