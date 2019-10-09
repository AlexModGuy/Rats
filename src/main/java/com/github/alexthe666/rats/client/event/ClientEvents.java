package com.github.alexthe666.rats.client.event;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.render.entity.LayerPlague;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Mod.EventBusSubscriber
public class ClientEvents {
    public static final ResourceLocation PLAGUE_HEART_TEXTURE = new ResourceLocation("rats:textures/gui/plague_hearts.png");
    private int updateCounter = 0;
    private int playerHealth = 0;
    private int lastPlayerHealth = 0;
    private long healthUpdateCounter = 0;
    private long lastSystemTime = 0;
    private Random rand = new Random();
    public static int left_height = 39;
    public static int right_height = 39;
    private float synesthesiaProgress = 0;
    private float prevSynesthesiaProgress = 0;
    private float maxSynesthesiaProgress = 40;
    private static final ResourceLocation SYNESTHESIA = new ResourceLocation("rats:shaders/post/synesthesia.json");

    @SubscribeEvent
    public void onPlayerInteract(RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HEALTH || event.isCanceled() || !Minecraft.getMinecraft().player.isPotionActive(RatsMod.PLAGUE_POTION)) {
            return;
        }
        left_height = 39;
        ScaledResolution resolution = event.getResolution();
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();
        GlStateManager.enableBlend();
        EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().getRenderViewEntity();
        int health = MathHelper.ceil(player.getHealth());
        boolean highlight = healthUpdateCounter > (long)updateCounter && (healthUpdateCounter - (long)updateCounter) / 3L %2L == 1L;

        if (health < this.playerHealth && player.hurtResistantTime > 0)
        {
            this.lastSystemTime = Minecraft.getSystemTime();
            this.healthUpdateCounter = (long)(this.updateCounter + 20);
        }
        else if (health > this.playerHealth && player.hurtResistantTime > 0)
        {
            this.lastSystemTime = Minecraft.getSystemTime();
            this.healthUpdateCounter = (long)(this.updateCounter + 10);
        }

        if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L)
        {
            this.playerHealth = health;
            this.lastPlayerHealth = health;
            this.lastSystemTime = Minecraft.getSystemTime();
        }

        this.playerHealth = health;
        int healthLast = this.lastPlayerHealth;

        IAttributeInstance attrMaxHealth = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
        float healthMax = (float)attrMaxHealth.getAttributeValue();
        float absorb = MathHelper.ceil(player.getAbsorptionAmount());

        int healthRows = MathHelper.ceil((healthMax + absorb) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (healthRows - 2), 3);

        this.rand.setSeed((long)(updateCounter * 312871));

        int left = width / 2 - 91;
        int top = height - left_height;
        left_height += (healthRows * rowHeight);
        if (rowHeight != 10) left_height += 10 - rowHeight;

        int regen = -1;
        if (player.isPotionActive(MobEffects.REGENERATION))
        {
            regen = updateCounter % 25;
        }

        final int TOP = 0;
        int MARGIN = 0;
        int BACKGROUND = 9;
        float absorbRemaining = absorb;

        for (int i = MathHelper.ceil((healthMax + absorb) / 2.0F) - 1; i >= 0; --i)
        {
            //int b0 = (highlight ? 1 : 0);
            int row = MathHelper.ceil((float)(i + 1) / 10.0F) - 1;
            int x = left + i % 10 * 8;
            int y = top - row * rowHeight;

            if (health <= 4) y += rand.nextInt(2);
            if (i == regen) y -= 2;
            drawTexturedModalRect(x, y, MARGIN, BACKGROUND, 9, 9);
            if (highlight)
            {
                if (i * 2 + 1 < healthLast)
                    drawTexturedModalRect(x, y, MARGIN, TOP, 9, 9); //6
                else if (i * 2 + 1 == healthLast)
                    drawTexturedModalRect(x, y, MARGIN + 9, TOP, 9, 9); //7
            }

            if (absorbRemaining > 0.0F)
            {
                if (absorbRemaining == absorb && absorb % 2.0F == 1.0F)
                {
                    drawTexturedModalRect(x, y, MARGIN, TOP, 9, 9); //17
                    absorbRemaining -= 1.0F;
                }
                else
                {
                    drawTexturedModalRect(x, y, MARGIN + 9, TOP, 9, 9); //16
                    absorbRemaining -= 2.0F;
                }
            }
            else
            {
                if (i * 2 + 1 < health)
                    drawTexturedModalRect(x, y, MARGIN, TOP, 9, 9); //4
                else if (i * 2 + 1 == health)
                    drawTexturedModalRect(x, y, MARGIN + 9, TOP, 9, 9); //5
            }
        }

        GlStateManager.disableBlend();
        Minecraft.getMinecraft().profiler.endSection();
    }

    private void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(PLAGUE_HEART_TEXTURE);
        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x, y, textureX, textureY, width, height);
    }

    public static void initializePlagueLayer() {
        for (Map.Entry<Class<? extends Entity>, Render<? extends Entity>> entry : Minecraft.getMinecraft().getRenderManager().entityRenderMap.entrySet()) {
            Render render = entry.getValue();
            if (render instanceof RenderLivingBase && EntityLivingBase.class.isAssignableFrom(entry.getKey())) {
                ((RenderLivingBase) render).addLayer(new LayerPlague((RenderLivingBase) render));
            }
        }
        for(Map.Entry<String, RenderPlayer> entry :  Minecraft.getMinecraft().getRenderManager().getSkinMap().entrySet()){
            RenderPlayer render = entry.getValue();
            render.addLayer(new LayerPlague(render));
        }
        Field renderingRegistryField = ReflectionHelper.findField(RenderingRegistry.class, ObfuscationReflectionHelper.remapFieldNames(RenderingRegistry.class.getName(), "INSTANCE", "INSTANCE"));
        Field entityRendersField = ReflectionHelper.findField(RenderingRegistry.class, ObfuscationReflectionHelper.remapFieldNames(RenderingRegistry.class.getName(), "entityRenderers", "entityRenderers"));
        Field entityRendersOldField = ReflectionHelper.findField(RenderingRegistry.class, ObfuscationReflectionHelper.remapFieldNames(RenderingRegistry.class.getName(), "entityRenderersOld", "entityRenderersOld"));
        RenderingRegistry registry = null;
        try {
            Field modifier = Field.class.getDeclaredField("modifiers");
            modifier.setAccessible(true);
            registry = (RenderingRegistry) renderingRegistryField.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (registry != null) {
            Map<Class<? extends Entity>, IRenderFactory<? extends Entity>> entityRenders = null;
            Map<Class<? extends Entity>, Render<? extends Entity>> entityRendersOld = null;
            try {
                Field modifier1 = Field.class.getDeclaredField("modifiers");
                modifier1.setAccessible(true);
                entityRenders = (Map<Class<? extends Entity>, IRenderFactory<? extends Entity>>) entityRendersField.get(registry);
                entityRendersOld = (Map<Class<? extends Entity>, Render<? extends Entity>>) entityRendersOldField.get(registry);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (entityRenders != null) {
                for (Map.Entry<Class<? extends Entity>, IRenderFactory<? extends Entity>> entry : entityRenders.entrySet()) {
                    if (entry.getValue() != null) {
                        try {
                            Render render = entry.getValue().createRenderFor(Minecraft.getMinecraft().getRenderManager());
                            if (render != null && render instanceof RenderLivingBase && EntityLivingBase.class.isAssignableFrom(entry.getKey())) {
                                ((RenderLivingBase) render).addLayer(new LayerPlague((RenderLivingBase)render));
                            }
                        } catch (NullPointerException exp) {
                            RatsMod.logger.warn("Rats: Could not apply plague render layer to " + entry.getKey().getSimpleName() + ", someone isn't registering their renderer properly... <.<");
                        }
                    }

                }
            }
            if (entityRendersOld != null) {
                for (Map.Entry<Class<? extends Entity>, Render<? extends Entity>> entry : entityRendersOld.entrySet()) {
                    Render render = entry.getValue();
                    if (render instanceof RenderLivingBase && EntityLivingBase.class.isAssignableFrom(entry.getKey())) {
                        ((RenderLivingBase) render).addLayer(new LayerPlague((RenderLivingBase)render));
                    }
                }
            }
        }

    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() == Minecraft.getMinecraft().player) {
            EntityRenderer renderer = Minecraft.getMinecraft().entityRenderer;
            PotionEffect active = event.getEntityLiving().getActivePotionEffect(RatsMod.CONFIT_BYALDI_POTION);
            boolean synesthesia = active != null;
            if (synesthesia && !renderer.isShaderActive()) {
                renderer.loadShader(SYNESTHESIA);
            }
            if (!synesthesia && renderer.isShaderActive() && renderer != null && renderer.getShaderGroup() != null && renderer.getShaderGroup().getShaderGroupName() != null && SYNESTHESIA.toString().equals(renderer.getShaderGroup().getShaderGroupName())) {
                renderer.stopUseShader();
            }
            if (prevSynesthesiaProgress == 2 && synesthesia) {
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(RatsSoundRegistry.POTION_EFFECT_BEGIN, 1.0F));
            }
            if (prevSynesthesiaProgress == 38 && !synesthesia) {
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(RatsSoundRegistry.POTION_EFFECT_END, 1.0F));
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
    @SideOnly(Side.CLIENT)
    public void onGetFOVModifier(EntityViewRenderEvent.FOVModifier event) {
        if (event.getEntity() == Minecraft.getMinecraft().player && prevSynesthesiaProgress > 0) {
            float prog = (prevSynesthesiaProgress + (synesthesiaProgress - prevSynesthesiaProgress) * LLibrary.PROXY.getPartialTicks());
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
