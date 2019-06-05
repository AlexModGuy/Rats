package com.github.alexthe666.rats.server.events;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ClientEvent {
    private static final ResourceLocation SYNESTHESIA = new ResourceLocation("rats:shaders/post/synesthesia.json");

    @SubscribeEvent
    public void onPlayerInteract(LivingEvent.LivingUpdateEvent event) {
        EntityRenderer renderer = Minecraft.getMinecraft().entityRenderer;
        if (event.getEntityLiving().isPotionActive(RatsMod.CONFIT_BYALDI_POTION) && !renderer.isShaderActive()) {
            renderer.loadShader(SYNESTHESIA);
        }
        if (!event.getEntityLiving().isPotionActive(RatsMod.CONFIT_BYALDI_POTION) && renderer.isShaderActive()) {
            renderer.stopUseShader();
        }
    }
}