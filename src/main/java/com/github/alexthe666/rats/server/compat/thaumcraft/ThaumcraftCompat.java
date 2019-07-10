package com.github.alexthe666.rats.server.compat.thaumcraft;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.AspectRegistryEvent;

public class ThaumcraftCompat {
    private static boolean registered = false;
    private static ThaumcraftCompat INSTANCE = new ThaumcraftCompat();
    private static final Aspect RAT = new Aspect("rattus", 0X3B3E49, new Aspect[]{Aspect.ENTROPY, Aspect.BEAST}, new ResourceLocation("rats:textures/thaumcraft_rat_icon.png"), 1);

    public static void register() {
        if (!registered) {
            registered = true;
            MinecraftForge.EVENT_BUS.register(INSTANCE);
        } else {
            throw new RuntimeException("You can only call ThaumcraftCompat.register() once");
        }
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public void aspectRegistrationEvent(AspectRegistryEvent evt) {
        //items
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.CHEESE), new AspectList().add(Aspect.LIFE, 5).add(Aspect.EARTH, 5).add(Aspect.BEAST, 5).add(RAT, 5));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.RAT_PELT), new AspectList().add(Aspect.DEATH, 5).add(Aspect.BEAST, 5).add(RAT, 5));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.RAT_FLUTE), new AspectList().add(Aspect.MAGIC, 10).add(Aspect.CRAFT, 10).add(Aspect.SENSES, 10).add(RAT, 15));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.RAW_RAT), new AspectList().add(Aspect.DEATH, 5).add(Aspect.BEAST, 5).add(RAT, 5));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.COOKED_RAT), new AspectList().add(Aspect.LIFE, 5).add(Aspect.BEAST, 5).add(RAT, 5));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.FEATHERY_WING), new AspectList().add(Aspect.FLIGHT, 25).add(Aspect.BEAST, 5));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.CHEESE_STICK), new AspectList().add(Aspect.CRAFT, 10).add(RAT, 10));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.CREATIVE_CHEESE), new AspectList().add(Aspect.MAGIC, 10).add(RAT, 10));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.ASSORTED_VEGETABLES), new AspectList().add(Aspect.LIFE, 10).add(Aspect.EARTH, 5).add(RAT, 2));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.CONFIT_BYALDI), new AspectList().add(Aspect.LIFE, 50).add(Aspect.CRAFT, 5).add(RAT, 50));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.STRING_CHEESE), new AspectList().add(Aspect.LIFE, 10).add(Aspect.CRAFT, 5).add(RAT, 10));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.CHEF_TOQUE), new AspectList().add(Aspect.MAN, 10).add(Aspect.CRAFT, 5).add(RAT, 2));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.PIPER_HAT), new AspectList().add(Aspect.MAN, 10).add(Aspect.CRAFT, 5).add(RAT, 2).add(Aspect.MAGIC, 2));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.RAT_UPGRADE_BASIC), new AspectList().add(Aspect.CRAFT, 15).add(RAT, 15).add(Aspect.ORDER, 2));
        //blocks
        evt.register.registerObjectTag(new ItemStack(RatsBlockRegistry.BLOCK_OF_CHEESE), new AspectList().add(Aspect.LIFE, 15).add(Aspect.EARTH, 5).add(Aspect.CRAFT, 15).add(RAT, 15));
        evt.register.registerObjectTag(new ItemStack(RatsBlockRegistry.MILK_CAULDRON), new AspectList().add(Aspect.LIFE, 5).add(Aspect.METAL, 5).add(Aspect.CRAFT, 5).add(RAT, 5));
        evt.register.registerObjectTag(new ItemStack(RatsBlockRegistry.CHEESE_CAULDRON), new AspectList().add(Aspect.LIFE, 5).add(Aspect.METAL, 5).add(Aspect.CRAFT, 5).add(RAT, 5));
        evt.register.registerObjectTag(new ItemStack(RatsBlockRegistry.RAT_TRAP), new AspectList().add(Aspect.TRAP, 40).add(RAT, 15).add(Aspect.DEATH, 15).add(Aspect.DESIRE, 5).add(Aspect.METAL, 5).add(Aspect.CRAFT, 5));
        evt.register.registerObjectTag(new ItemStack(RatsBlockRegistry.RAT_CAGE), new AspectList().add(RAT, 20).add(Aspect.PROTECT, 10).add(Aspect.LIFE, 5).add(Aspect.DESIRE, 5).add(Aspect.METAL, 5).add(Aspect.CRAFT, 5));
        evt.register.registerObjectTag(new ItemStack(RatsBlockRegistry.RAT_CRAFTING_TABLE), new AspectList().add(RAT, 20).add(Aspect.CRAFT, 10).add(Aspect.MECHANISM, 5));
        evt.register.registerObjectTag(new ItemStack(RatsBlockRegistry.RAT_HOLE), new AspectList().add(RAT, 20).add(Aspect.ENTROPY, 10));
        //entities
        ThaumcraftApi.registerEntityTag("rat", new AspectList().add(RAT, 20).add(Aspect.AVERSION, 5).add(Aspect.MIND, 5).add(Aspect.DESIRE, 5).add(Aspect.BEAST, 10));
        ThaumcraftApi.registerEntityTag("illager_piper", new AspectList().add(RAT, 40).add(Aspect.MAGIC, 15).add(Aspect.MAN, 15).add(Aspect.MIND, 5).add(Aspect.DESIRE, 10).add(Aspect.SENSES, 10));

    }

}
