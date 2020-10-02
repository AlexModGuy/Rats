package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.ModelBiplane;
import com.github.alexthe666.rats.client.model.ModelPinkie;
import com.github.alexthe666.rats.client.model.ModelRat;
import com.github.alexthe666.rats.api.RatClientEvent;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.ratlantis.EntityRatBaronPlane;
import com.github.alexthe666.rats.server.entity.ratlantis.EntityRatBiplaneMount;
import com.github.alexthe666.rats.server.entity.ratlantis.EntityRattlingGun;
import com.github.alexthe666.rats.server.items.RatlantisItemRegistry;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.LightType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class RenderRat extends MobRenderer<EntityRat, SegmentedModel<EntityRat>> {

    private static final Map<String, ResourceLocation> LAYERED_LOCATION_CACHE = Maps.newHashMap();
    private static final ModelRat RAT_MODEL = new ModelRat(0.0F);
    private static final ModelPinkie PINKIE_MODEL = new ModelPinkie();
    private static final ResourceLocation PINKIE_TEXTURE = new ResourceLocation("rats:textures/entity/rat/baby.png");
    private static final ResourceLocation ENDER_UPGRADE_TEXTURE = new ResourceLocation("rats:textures/entity/rat/rat_ender_upgrade.png");
    private static final ResourceLocation AQUATIC_UPGRADE_TEXTURE = new ResourceLocation("rats:textures/entity/rat/rat_aquatic_upgrade.png");
    private static final ResourceLocation DRAGON_UPGRADE_TEXTURE = new ResourceLocation("rats:textures/entity/rat/rat_dragon_upgrade.png");
    private static final ResourceLocation CARRAT_UPGRADE_TEXTURE = new ResourceLocation("rats:textures/entity/rat/rat_carrat_upgrade.png");
    private static final ResourceLocation ETHEREAL_UPGRADE_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/ghost_pirat.png");
    private static final ResourceLocation UNDEAD_UPGRADE_TEXTURE = new ResourceLocation("rats:textures/entity/rat/rat_skeleton.png");
    private static final ResourceLocation BEE_UPGRADE_TEXTURE = new ResourceLocation("rats:textures/entity/rat/rat_bee_upgrade.png");
    private static final ResourceLocation JULIAN = new ResourceLocation("rats:textures/entity/rat/patreon/rat_julian.png");
    private static final ResourceLocation SHIZUKA = new ResourceLocation("rats:textures/entity/rat/patreon/rat_shizuka.png");
    private static final ResourceLocation SHARVA = new ResourceLocation("rats:textures/entity/rat/patreon/rat_sharva.png");
    private static final ResourceLocation DINO = new ResourceLocation("rats:textures/entity/rat/patreon/rat_dino.png");
    private static final ResourceLocation RATATLA = new ResourceLocation("rats:textures/entity/rat/patreon/rat_ratatla.png");
    private static final ResourceLocation FRIAR = new ResourceLocation("rats:textures/entity/rat/patreon/rat_friar.png");
    private static final ResourceLocation RIDDLER = new ResourceLocation("rats:textures/entity/rat/patreon/rat_riddler.png");
    private static final ResourceLocation JOKER = new ResourceLocation("rats:textures/entity/rat/patreon/rat_joker.png");

    public RenderRat() {
        super(Minecraft.getInstance().getRenderManager(), RAT_MODEL, 0.15F);
        this.addLayer(new LayerRatPlague(this));
        this.addLayer(new LayerRatEyes(this));
        this.addLayer(new LayerRatHelmet(this));
        this.addLayer(new LayerRatHeldItem(this));
    }

    @Override
    public <E extends Entity> void renderLeash(EntityRat entityLivingIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, E leashHolder) {
        matrixStackIn.push();
        double d0 = (double)(MathHelper.lerp(partialTicks * 0.5F, leashHolder.rotationYaw, leashHolder.prevRotationYaw) * ((float)Math.PI / 180F));
        double d1 = (double)(MathHelper.lerp(partialTicks * 0.5F, leashHolder.rotationPitch, leashHolder.prevRotationPitch) * ((float)Math.PI / 180F));
        double d2 = Math.cos(d0);
        double d3 = Math.sin(d0);
        double d4 = Math.sin(d1);
        if (leashHolder instanceof HangingEntity) {
            d2 = 0.0D;
            d3 = 0.0D;
            d4 = -1.0D;
        }

        double d5 = Math.cos(d1);
        double d6 = MathHelper.lerp((double)partialTicks, leashHolder.prevPosX, leashHolder.getPosX()) - d2 * 0.7D - d3 * 0.5D * d5;
        double d7 = MathHelper.lerp((double)partialTicks, leashHolder.prevPosY + (double)leashHolder.getEyeHeight() * 1.5D, leashHolder.getPosY() + (double)leashHolder.getEyeHeight() * 1.5D) - d4 * 0.25D - 0.125D;
        double d8 = MathHelper.lerp((double)partialTicks, leashHolder.prevPosZ, leashHolder.getPosZ()) - d3 * 0.7D + d2 * 0.5D * d5;
        double d9 = (double)(MathHelper.lerp(partialTicks, entityLivingIn.renderYawOffset, entityLivingIn.prevRenderYawOffset) * ((float)Math.PI / 180F)) + (Math.PI / 2D);
        d2 = Math.cos(d9) * (double)entityLivingIn.getWidth() * 0.4D;
        d3 = Math.sin(d9) * (double)entityLivingIn.getWidth() * 0.4D;
        double d10 = MathHelper.lerp((double)partialTicks, entityLivingIn.prevPosX, entityLivingIn.getPosX()) + d2;
        double d11 = MathHelper.lerp((double)partialTicks, entityLivingIn.prevPosY, entityLivingIn.getPosY());
        double d12 = MathHelper.lerp((double)partialTicks, entityLivingIn.prevPosZ, entityLivingIn.getPosZ()) + d3;
        matrixStackIn.translate(d2, -(1.6D - (double)entityLivingIn.getHeight()) * 1D, d3);
        float f = (float)(d6 - d10);
        float f1 = (float)(d7 - d11);
        float f2 = (float)(d8 - d12);
        float f3 = 0.025F;
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getLeash());
        Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
        float f4 = MathHelper.fastInvSqrt(f * f + f2 * f2) * 0.025F / 2.0F;
        float f5 = f2 * f4;
        float f6 = f * f4;
        BlockPos blockpos = new BlockPos(entityLivingIn.getEyePosition(partialTicks));
        BlockPos blockpos1 = new BlockPos(leashHolder.getEyePosition(partialTicks));
        int i = this.getBlockLight(entityLivingIn, blockpos);
        int j = entityLivingIn.world.getLightFor(LightType.BLOCK, blockpos1);
        int k = entityLivingIn.world.getLightFor(LightType.SKY, blockpos);
        int l = entityLivingIn.world.getLightFor(LightType.SKY, blockpos1);
        renderSide(ivertexbuilder, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.025F, f5, f6);
        renderSide(ivertexbuilder, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.0F, f5, f6);
        matrixStackIn.pop();
    }


    protected boolean canRenderName(EntityRat entity) {
        return RatsMod.PROXY.shouldRenderNameplates() && super.canRenderName(entity);
    }

    public void render(EntityRat entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (entityIn.isChild()) {
            entityModel = PINKIE_MODEL;
        } else {
            entityModel = RAT_MODEL;
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }


    public boolean shouldRender(EntityRat rat, ClippingHelper camera, double camX, double camY, double camZ) {
        if (rat.isPassenger() && rat.getRidingEntity() != null && rat.getRidingEntity().getPassengers().size() >= 1 && rat.getRidingEntity().getPassengers().get(0) == rat && rat.getRidingEntity() instanceof LivingEntity) {
            if (((LivingEntity) rat.getRidingEntity()).getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == RatsItemRegistry.CHEF_TOQUE) {
                return false;
            }
        }
        return super.shouldRender(rat, camera, camX, camY, camZ);
    }

    protected void preRenderCallback(EntityRat rat, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.6F, 0.6F, 0.6F);
        if (rat.isPassenger() && rat.getRidingEntity() != null && rat.getRidingEntity().getPassengers().size() >= 1) {
            if(rat.getRidingEntity() != null) {
                if (rat.getRidingEntity() instanceof PlayerEntity) {
                    Entity riding = rat.getRidingEntity();
                    if (riding.getPassengers().get(0) != null && riding.getPassengers().get(0) == rat) {
                        EntityRenderer playerRender = Minecraft.getInstance().getRenderManager().getRenderer(riding);
                        if (playerRender instanceof LivingRenderer && ((LivingRenderer) playerRender).getEntityModel() instanceof BipedModel) {
                            ((BipedModel) ((LivingRenderer) playerRender).getEntityModel()).bipedHead.translateRotate(matrixStackIn);
                            matrixStackIn.translate(0.0F, -0.7F, 0.25F);
                        }
                    }
                }
                if (rat.getRidingEntity() instanceof EntityRattlingGun) {
                    Entity riding = rat.getRidingEntity();
                    if (riding.getPassengers().get(0) != null && riding.getPassengers().get(0) == rat) {
                        EntityRenderer playerRender = Minecraft.getInstance().getRenderManager().getRenderer(riding);
                        if (playerRender instanceof LivingRenderer && ((LivingRenderer) playerRender).getEntityModel() instanceof BipedModel) {
                            RenderRattlingGun.GUN_MODEL.pivot.translateRotate(matrixStackIn);
                            //GlStateManager.translatef(0.0F, -0.7F, 0.25F);
                        }
                    }
                }
                if (rat.getRidingEntity() instanceof EntityRatBaronPlane || rat.getRidingEntity() instanceof EntityRatBiplaneMount) {
                    Entity riding = rat.getRidingEntity();
                    if (riding.getPassengers().get(0) != null && riding.getPassengers().get(0) == rat) {
                        EntityRenderer playerRender = Minecraft.getInstance().getRenderManager().getRenderer(riding);
                        if (playerRender instanceof LivingRenderer && ((LivingRenderer) playerRender).getEntityModel() instanceof ModelBiplane) {
                            matrixStackIn.translate(0.0F, -0.1F, 0.45F);
                            ((ModelBiplane) ((LivingRenderer) playerRender).getEntityModel()).body1.translateRotate(matrixStackIn);
                        }
                    }
                }
            }
        } else {
            float f7 = rat.prevFlyingPitch + (rat.flyingPitch - rat.prevFlyingPitch) * partialTickTime;
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, rat.flyingPitch, true));
        }

    }

    public ResourceLocation getEntityTexture(EntityRat entity) {
        String s = entity.getRatTexture();
        if (entity.isChild()) {
            return PINKIE_TEXTURE;
        } else {
            if (entity.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_ETHEREAL)) {
                return ETHEREAL_UPGRADE_TEXTURE;
            }
            if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BEE)) {
                return BEE_UPGRADE_TEXTURE;
            }
            if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_UNDEAD)) {
                return UNDEAD_UPGRADE_TEXTURE;
            }
            if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CARRAT)) {
                return CARRAT_UPGRADE_TEXTURE;
            }
            if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DRAGON)) {
                return DRAGON_UPGRADE_TEXTURE;
            }
            if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ENDER)) {
                return ENDER_UPGRADE_TEXTURE;
            }
            if (entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_AQUATIC)) {
                return AQUATIC_UPGRADE_TEXTURE;
            }
            if (entity.hasCustomName()) {
                String str = entity.getCustomName().getString();
                if (str.contains("julian") || str.contains("Julian")) {
                    return JULIAN;
                }
                if (str.contains("shizuka") || str.contains("Shizuka")) {
                    return SHIZUKA;
                }
                if (str.contains("sharva") || str.contains("Sharva")) {
                    return SHARVA;
                }
                if (str.contains("dino") || str.contains("Dino")) {
                    return DINO;
                }
                if (str.contains("ratatla") || str.contains("Ratatla")) {
                    return RATATLA;
                }
                if (str.contains("friar") || str.contains("Friar")) {
                    return FRIAR;
                }
                if (str.contains("riddler") || str.contains("Riddler")) {
                    return RIDDLER;
                }
                if (str.contains("joker") || str.contains("Joker")) {
                    return JOKER;
                }
            }
            ResourceLocation resourcelocation = LAYERED_LOCATION_CACHE.get(s);
            if (resourcelocation == null) {
                resourcelocation = new ResourceLocation(s);
                //Minecraft.getInstance().getTextureManager().loadTexture(resourcelocation, new LayeredTexture(entity.getVariantTexturePaths()));
                LAYERED_LOCATION_CACHE.put(s, resourcelocation);
            }
            RatClientEvent.GetTexture textureEvent = new RatClientEvent.GetTexture(entity, this, resourcelocation);
            MinecraftForge.EVENT_BUS.post(textureEvent);
            return textureEvent.getResult() == Event.Result.ALLOW ? textureEvent.getTexture() : resourcelocation;
        }
    }

    protected float getDeathMaxRotation(EntityRat rat) {
        return rat.isDeadInTrap ? 0 : 90;
    }

}
