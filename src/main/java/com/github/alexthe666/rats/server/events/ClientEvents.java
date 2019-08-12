package com.github.alexthe666.rats.server.events;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityIllagerPiper;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber
public class ClientEvents {
    private static final ResourceLocation SYNESTHESIA = new ResourceLocation("rats:shaders/post/synesthesia.json");

    @SubscribeEvent
    public void onPlayerInteract(LivingEvent.LivingUpdateEvent event) {
        //Minecraft.getMinecraft().debugRenderer.pathfinding.render(Minecraft.getMinecraft().getRenderPartialTicks(), 0);
        Field debugField = ReflectionHelper.findField(DebugRenderer.class, ObfuscationReflectionHelper.remapFieldNames(DebugRenderer.class.getName(), new String[]{"pathfindingEnabled", "pathfindingEnabled"}));
        try {
            debugField.set(Minecraft.getMinecraft().debugRenderer,true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        EntityRenderer renderer = Minecraft.getMinecraft().entityRenderer;
        if (event.getEntityLiving().isPotionActive(RatsMod.CONFIT_BYALDI_POTION) && !renderer.isShaderActive()) {
            renderer.loadShader(SYNESTHESIA);
        }
        if (!event.getEntityLiving().isPotionActive(RatsMod.CONFIT_BYALDI_POTION) && renderer.isShaderActive() && renderer.getShaderGroup().getShaderGroupName().equalsIgnoreCase(SYNESTHESIA.toString())) {
            renderer.stopUseShader();
        }
    }

}