package com.github.alexthe666.rats.client.event;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.ModelStaticRat;
import com.github.alexthe666.rats.client.render.entity.RenderRatProtector;
import com.github.alexthe666.rats.client.render.type.RatsRenderType;
import com.github.alexthe666.rats.server.events.CommonEvents;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.VertexBuilderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ClientEvents {
    public static final ResourceLocation PLAGUE_HEART_TEXTURE = new ResourceLocation("rats:textures/gui/plague_hearts.png");
    private static final ResourceLocation RADIUS_TEXTURE = new ResourceLocation("rats:textures/entity/rat/rat_radius.png");
    private static final ResourceLocation SYNESTHESIA = new ResourceLocation("rats:shaders/post/synesthesia.json");
    public static int left_height = 39;
    public static int right_height = 39;
    private int updateCounter = 0;
    private int playerHealth = 0;
    private int lastPlayerHealth = 0;
    private long healthUpdateCounter = 0;
    private long lastSystemTime = 0;
    private Random rand = new Random();
    private float synesthesiaProgress = 0;
    private float prevSynesthesiaProgress = 0;
    private float maxSynesthesiaProgress = 40;

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HEALTH || event.isCanceled() || !Minecraft.getInstance().player.isPotionActive(RatsMod.PLAGUE_POTION) || !RatConfig.plagueHearts) {
            return;
        }
        left_height = 39;
        int width = Minecraft.getInstance().getMainWindow().getScaledWidth();
        int height = Minecraft.getInstance().getMainWindow().getScaledHeight();
        GlStateManager.enableBlend();
        PlayerEntity player = (PlayerEntity) Minecraft.getInstance().getRenderViewEntity();
        int health = MathHelper.ceil(player.getHealth());
        boolean highlight = healthUpdateCounter > (long) updateCounter && (healthUpdateCounter - (long) updateCounter) / 3L % 2L == 1L;

        if (health < this.playerHealth && player.hurtResistantTime > 0) {
            this.lastSystemTime = Util.milliTime();
            this.healthUpdateCounter = (long) (this.updateCounter + 20);
        } else if (health > this.playerHealth && player.hurtResistantTime > 0) {
            this.lastSystemTime = Util.milliTime();
            this.healthUpdateCounter = (long) (this.updateCounter + 10);
        }

        if (Util.milliTime() - this.lastSystemTime > 1000L) {
            this.playerHealth = health;
            this.lastPlayerHealth = health;
            this.lastSystemTime = Util.milliTime();
        }

        this.playerHealth = health;
        int healthLast = this.lastPlayerHealth;

        ModifiableAttributeInstance attrMaxHealth = player.getAttribute(Attributes.field_233818_a_);
        float healthMax = (float) attrMaxHealth.getValue();
        float absorb = MathHelper.ceil(player.getAbsorptionAmount());

        int healthRows = MathHelper.ceil((healthMax + absorb) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (healthRows - 2), 3);

        this.rand.setSeed((long) (updateCounter * 312871));

        int left = width / 2 - 91;
        int top = height - left_height;
        left_height += (healthRows * rowHeight);
        if (rowHeight != 10) left_height += 10 - rowHeight;

        int regen = -1;
        if (player.isPotionActive(Effects.REGENERATION)) {
            regen = updateCounter % 25;
        }

        final int TOP = 0;
        int MARGIN = 0;
        int BACKGROUND = 9;
        float absorbRemaining = absorb;

        for (int i = MathHelper.ceil((healthMax + absorb) / 2.0F) - 1; i >= 0; --i) {
            //int b0 = (highlight ? 1 : 0);
            int row = MathHelper.ceil((float) (i + 1) / 10.0F) - 1;
            int x = left + i % 10 * 8;
            int y = top - row * rowHeight;

            if (health <= 4) y += rand.nextInt(2);
            if (i == regen) y -= 2;
            drawTexturedModalRect(x, y, MARGIN, BACKGROUND, 9, 9);
            if (highlight) {
                if (i * 2 + 1 < healthLast)
                    drawTexturedModalRect(x, y, MARGIN, TOP, 9, 9); //6
                else if (i * 2 + 1 == healthLast)
                    drawTexturedModalRect(x, y, MARGIN + 9, TOP, 9, 9); //7
            }

            if (absorbRemaining > 0.0F) {
                if (absorbRemaining == absorb && absorb % 2.0F == 1.0F) {
                    drawTexturedModalRect(x, y, MARGIN, TOP, 9, 9); //17
                    absorbRemaining -= 1.0F;
                } else {
                    drawTexturedModalRect(x, y, MARGIN + 9, TOP, 9, 9); //16
                    absorbRemaining -= 2.0F;
                }
            } else {
                if (i * 2 + 1 < health)
                    drawTexturedModalRect(x, y, MARGIN, TOP, 9, 9); //4
                else if (i * 2 + 1 == health)
                    drawTexturedModalRect(x, y, MARGIN + 9, TOP, 9, 9); //5
            }
        }

        GlStateManager.disableBlend();
        Minecraft.getInstance().getProfiler().endSection();
    }

    private void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        Minecraft.getInstance().getTextureManager().bindTexture(PLAGUE_HEART_TEXTURE);
        Minecraft.getInstance().ingameGUI.blit(x, y, textureX, textureY, width, height);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() == Minecraft.getInstance().player) {
            GameRenderer renderer = Minecraft.getInstance().gameRenderer;
            EffectInstance active = event.getEntityLiving().getActivePotionEffect(RatsMod.CONFIT_BYALDI_POTION);
            boolean synesthesia = active != null;
            try{
                if (synesthesia && renderer.getShaderGroup() == null) {
                    renderer.loadShader(SYNESTHESIA);
                }
                if (!synesthesia && renderer != null && renderer.getShaderGroup() != null && renderer.getShaderGroup().getShaderGroupName() != null && SYNESTHESIA.toString().equals(renderer.getShaderGroup().getShaderGroupName())) {
                    renderer.stopUseShader();
                }
            }catch (Exception e){
                RatsMod.LOGGER.warn("Game tried to crash when applying shader");
            }

            if (prevSynesthesiaProgress == 2 && synesthesia) {
                Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(RatsSoundRegistry.POTION_EFFECT_BEGIN, 1.0F));
            }
            if (prevSynesthesiaProgress == 38 && !synesthesia) {
                Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(RatsSoundRegistry.POTION_EFFECT_END, 1.0F));
            }
            prevSynesthesiaProgress = synesthesiaProgress;
            if (synesthesia && synesthesiaProgress < maxSynesthesiaProgress) {
                synesthesiaProgress += 2F;
            } else if (!synesthesia && synesthesiaProgress > 0.0F) {
                synesthesiaProgress -= 2F;
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onGetFOVModifier(EntityViewRenderEvent.FOVModifier event) {
        if (prevSynesthesiaProgress > 0) {
            float prog = (prevSynesthesiaProgress + (synesthesiaProgress - prevSynesthesiaProgress) * Minecraft.getInstance().getRenderPartialTicks());
            float renderProg;
            if (prevSynesthesiaProgress > synesthesiaProgress) {
                renderProg = (float) Math.sin(prog / maxSynesthesiaProgress * Math.PI) * 40F;
            } else {
                renderProg = -(float) Math.sin(prog / maxSynesthesiaProgress * Math.PI) * 40F;
            }
            event.setFOV(event.getFOV() + renderProg);
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND).getItem() == RatsItemRegistry.RADIUS_STICK) {
            if (RatsMod.PROXY.getRefrencedRat() != null) {
                Vector3d renderCenter = new Vector3d(RatsMod.PROXY.getRefrencedRat().getSearchCenter()).add(0.5, 0.5, 0.5);
                double renderRadius = RatsMod.PROXY.getRefrencedRat().getSearchRadius();
                AxisAlignedBB aabb = new AxisAlignedBB(-renderRadius, -renderRadius, -renderRadius, renderRadius, renderRadius, renderRadius);
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableBlend();
                Minecraft.getInstance().getTextureManager().bindTexture(RADIUS_TEXTURE);
                GlStateManager.disableCull();
                GlStateManager.depthMask(false);
                Entity viewEntity = Minecraft.getInstance().player;
                ActiveRenderInfo activerenderinfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
                Vector3d viewPosition = activerenderinfo.getProjectedView();
                double px = viewPosition.x;
                double py = viewPosition.y;
                double pz = viewPosition.z;
                MatrixStack stack = event.getMatrixStack();
                stack.push();
                stack.translate(-px, -py, -pz);
                stack.translate(renderCenter.x, renderCenter.y, renderCenter.z);
                stack.push();
                renderMovingAABB(aabb, event.getPartialTicks(), stack);
                stack.pop();
                stack.pop();
                GlStateManager.depthMask(true);
                GlStateManager.enableCull();
                GlStateManager.disableBlend();
            }
        }
    }

    public static void renderMovingAABB(AxisAlignedBB boundingBox, float partialTicks, MatrixStack stack) {
        Tessellator tessellator = Tessellator.getInstance();
        IVertexBuilder vertexbuffer = tessellator.getBuffer().getVertexBuilder();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f3 = (float) (System.currentTimeMillis() % 3000L) / 3000.0F;
        Matrix4f matrix4f = stack.getLast().getMatrix();
        float maxX = (float)boundingBox.maxX * 0.125F;
        float minX = (float)boundingBox.minX * 0.125F;
        float maxY = (float)boundingBox.maxY * 0.125F;
        float minY = (float)boundingBox.minY * 0.125F;
        float maxZ = (float)boundingBox.maxZ * 0.125F;
        float minZ = (float)boundingBox.minZ * 0.125F;
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX,  (float)boundingBox.maxY,  (float)boundingBox.minZ).tex(f3 + minX - maxX, f3 + maxY - minY).color(255, 255, 255, 255).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(matrix4f,  (float)boundingBox.maxX,  (float)boundingBox.maxY,  (float)boundingBox.minZ).tex(f3 + maxX - minX, f3 + maxY - minY).color(255, 255, 255, 255).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(matrix4f,  (float)boundingBox.maxX,  (float)boundingBox.minY,  (float)boundingBox.minZ).tex(f3 + maxX - minX, f3 + minY - maxY).color(255, 255, 255, 255).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(matrix4f,  (float)boundingBox.minX,  (float)boundingBox.minY,  (float)boundingBox.minZ).tex(f3 + minX - maxX, f3 + minY - maxY).color(255, 255, 255, 255).normal(0.0F, 0.0F, -1.0F).endVertex();

        vertexbuffer.pos(matrix4f,  (float)boundingBox.minX,  (float)boundingBox.minY,  (float)boundingBox.maxZ).tex(f3 + minX - maxX, f3 + minY - maxY).color(255, 255, 255, 255).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX,  (float)boundingBox.minY,  (float)boundingBox.maxZ).tex(f3 + maxX - minX, f3 + minY - maxY).color(255, 255, 255, 255).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX,  (float)boundingBox.maxY,  (float)boundingBox.maxZ).tex(f3 + maxX - minX, f3 + maxY - minY).color(255, 255, 255, 255).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX,  (float)boundingBox.maxY,  (float)boundingBox.maxZ).tex(f3 + minX - maxX, f3 + maxY - minY).color(255, 255, 255, 255).normal(0.0F, 0.0F, 1.0F).endVertex();

        vertexbuffer.pos(matrix4f, (float)boundingBox.minX,  (float)boundingBox.minY,  (float)boundingBox.minZ).tex(f3 + minX - maxX, f3 + minY - maxY).color(255, 255, 255, 255).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX,  (float)boundingBox.minY,  (float)boundingBox.minZ).tex(f3 + maxX - minX, f3 + minY - maxY).color(255, 255, 255, 255).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX,  (float)boundingBox.minY,  (float)boundingBox.maxZ).tex(f3 + maxX - minX, f3 + maxZ - minZ).color(255, 255, 255, 255).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX,  (float)boundingBox.minY,  (float)boundingBox.maxZ).tex(f3 + minX - maxX, f3 + maxZ - minZ).color(255, 255, 255, 255).normal(0.0F, -1.0F, 0.0F).endVertex();

        vertexbuffer.pos(matrix4f, (float)boundingBox.minX,  (float)boundingBox.maxY,  (float)boundingBox.maxZ).tex(f3 + minX - maxX, f3 + minY - maxY).color(255, 255, 255, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX,  (float)boundingBox.maxY,  (float)boundingBox.maxZ).tex(f3 + maxX - minX, f3 + minY - maxY).color(255, 255, 255, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX,  (float)boundingBox.maxY,  (float)boundingBox.minZ).tex(f3 + maxX - minX, f3 + maxZ - minZ).color(255, 255, 255, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX,  (float)boundingBox.maxY,  (float)boundingBox.minZ).tex(f3 + minX - maxX, f3 + maxZ - minZ).color(255, 255, 255, 255).normal(0.0F, 1.0F, 0.0F).endVertex();

        vertexbuffer.pos(matrix4f, (float)boundingBox.minX,  (float)boundingBox.minY,  (float)boundingBox.maxZ).tex(f3 + minX - maxX, f3 + minY - maxY).color(255, 255, 255, 255).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX,  (float)boundingBox.maxY,  (float)boundingBox.maxZ).tex(f3 + minX - maxX, f3 + maxY - minY).color(255, 255, 255, 255).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX,  (float)boundingBox.maxY,  (float)boundingBox.minZ).tex(f3 + maxX - minX, f3 + maxY - minY).color(255, 255, 255, 255).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX,  (float)boundingBox.minY,  (float)boundingBox.minZ).tex(f3 + maxX - minX, f3 + minY - maxY).color(255, 255, 255, 255).normal(-1.0F, 0.0F, 0.0F).endVertex();

        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX,  (float)boundingBox.minY,  (float)boundingBox.minZ).tex(f3 + minX - maxX, f3 + minY - maxY).color(255, 255, 255, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX,  (float)boundingBox.maxY,  (float)boundingBox.minZ).tex(f3 + minX - maxX, f3 + maxY - minY).color(255, 255, 255, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX,  (float)boundingBox.maxY,  (float)boundingBox.maxZ).tex(f3 + maxX - minX, f3 + maxY - minY).color(255, 255, 255, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX,  (float)boundingBox.minY,  (float)boundingBox.maxZ).tex(f3 + maxX - minX, f3 + minY - maxY).color(255, 255, 255, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
        tessellator.draw();
    }

    private static final ModelStaticRat RAT_MODEL = new ModelStaticRat(0);
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onLivingRender(RenderLivingEvent.Post event) {
        MatrixStack matrixStackIn = event.getMatrixStack();
        int protectorCount = CommonEvents.getProtectorCount(event.getEntity());
        IVertexBuilder textureBuilder = event.getBuffers().getBuffer(RatsRenderType.getGlowingTranslucent(RenderRatProtector.BASE_TEXTURE));
        for(int i = 0; i < protectorCount; i++){
           float tick = (float)(event.getEntity().ticksExisted - 1) + event.getPartialRenderTick();
            float offsetRot = 30 + 360 * (i / (float)protectorCount);
            float bob = (float)((Math.sin((double)(tick * 0.1F)) * 0.2F + Math.cos(tick * 0.4F + i)) * 0.2);
            float scale = 0.4F;
            float rotation = MathHelper.wrapDegrees((tick * 8) % 360.0F + offsetRot);
            matrixStackIn.push();
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(rotation));
            matrixStackIn.translate(0.0D, (double)(event.getEntity().getHeight() + 0.5D + bob), (double)(event.getEntity().getWidth() + 0.5F));
            matrixStackIn.push();
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(75.0F));
            matrixStackIn.scale(scale, scale, scale);
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));
            float f = (event.getEntity().ticksExisted + event.getPartialRenderTick()) * 0.5F;
            float f1 = 1;
            RAT_MODEL.setRotationAngles(event.getEntity(), f, f1, event.getEntity().ticksExisted + event.getPartialRenderTick(), event.getPartialRenderTick(), 0);
            RAT_MODEL.render(event.getMatrixStack(), textureBuilder, event.getLight(), OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.pop();
            matrixStackIn.pop();

        }
    }



}
