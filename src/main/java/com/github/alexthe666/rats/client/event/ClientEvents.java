package com.github.alexthe666.rats.client.event;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

public class ClientEvents {
    public static final ResourceLocation PLAGUE_HEART_TEXTURE = new ResourceLocation("rats:textures/gui/plague_hearts.png");
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
    public void onPlayerInteract(RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HEALTH || event.isCanceled() || !Minecraft.getInstance().player.isPotionActive(RatsMod.PLAGUE_POTION) || !RatConfig.plagueHearts) {
            return;
        }
        left_height = 39;
        int width = Minecraft.getInstance().mainWindow.getScaledWidth();
        int height = Minecraft.getInstance().mainWindow.getScaledHeight();
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

        IAttributeInstance attrMaxHealth = player.getAttribute(SharedMonsterAttributes.MAX_HEALTH);
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
            if (synesthesia && !renderer.isShaderActive()) {
                renderer.loadShader(SYNESTHESIA);
            }
            if (!synesthesia && renderer.isShaderActive() && renderer != null && renderer.getShaderGroup() != null && renderer.getShaderGroup().getShaderGroupName() != null && SYNESTHESIA.toString().equals(renderer.getShaderGroup().getShaderGroupName())) {
                renderer.stopUseShader();
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

}
