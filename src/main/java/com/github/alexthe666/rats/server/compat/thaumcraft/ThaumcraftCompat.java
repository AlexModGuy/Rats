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
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.PLASTIC_WASTE), new AspectList().add(Aspect.CRAFT, 5).add(Aspect.MAN, 5).add(Aspect.ENTROPY, 10));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.RAW_PLASTIC), new AspectList().add(Aspect.CRAFT, 10).add(Aspect.MAN, 10).add(Aspect.ORDER, 5));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.CONTAMINATED_FOOD), new AspectList().add(RAT, 10).add(Aspect.DEATH, 5).add(Aspect.ENTROPY, 5).add(Aspect.TRAP, 5));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.TOKEN_FRAGMENT), new AspectList().add(RAT, 10).add(Aspect.DESIRE, 5));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.TOKEN_PIECE), new AspectList().add(RAT, 10).add(Aspect.DESIRE, 25));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.CHUNKY_CHEESE_TOKEN), new AspectList().add(RAT, 10).add(Aspect.DESIRE, 100));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.ARCHEOLOGIST_HAT), new AspectList().add(Aspect.CRAFT, 10).add(Aspect.MAN, 10).add(Aspect.SENSES, 5).add(Aspect.ELDRITCH, 5));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.RAT_TOGA), new AspectList().add(Aspect.CRAFT, 10).add(RAT, 10).add(Aspect.SENSES, 25).add(Aspect.ELDRITCH, 5));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.RATGLOVE_PETALS), new AspectList().add(Aspect.PLANT, 10).add(RAT, 10).add(Aspect.SENSES, 4));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.FERAL_RAT_CLAW), new AspectList().add(Aspect.BEAST, 25).add(RAT, 10).add(Aspect.DEATH, 5));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.GEM_OF_RATLANTIS), new AspectList().add(RAT, 25).add(Aspect.DESIRE, 10).add(Aspect.CRYSTAL, 15));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.ARCANE_TECHNOLOGY), new AspectList().add(RAT, 15).add(Aspect.ELDRITCH, 10).add(Aspect.MECHANISM, 15));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.RATLANTEAN_FLAME), new AspectList().add(RAT, 15).add(Aspect.ELDRITCH, 10).add(Aspect.MAGIC, 15).add(Aspect.DEATH, 15).add(Aspect.SOUL, 15));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.PSIONIC_RAT_BRAIN), new AspectList().add(RAT, 50).add(Aspect.ELDRITCH, 50).add(Aspect.MAGIC, 20).add(Aspect.LIFE, 15).add(Aspect.SENSES, 100).add(Aspect.MIND, 100));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.PIRAT_HAT), new AspectList().add(RAT, 5).add(Aspect.WATER, 5).add(Aspect.PROTECT, 2));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.PIRAT_CUTLASS), new AspectList().add(RAT, 5).add(Aspect.WATER, 5).add(Aspect.AVERSION, 2));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.CHEESE_CANNONBALL), new AspectList().add(RAT, 5).add(Aspect.WATER, 5).add(Aspect.DEATH, 6));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.MUSIC_DISC_LIVING_MICE), new AspectList().add(RAT, 5).add(Aspect.MAN, 5).add(Aspect.SENSES, 6));
        evt.register.registerObjectTag(new ItemStack(RatsItemRegistry.MUSIC_DISC_MICE_ON_VENUS), new AspectList().add(RAT, 5).add(Aspect.MAN, 5).add(Aspect.SENSES, 6));
        //blocks
        evt.register.registerObjectTag(new ItemStack(RatsBlockRegistry.BLOCK_OF_CHEESE), new AspectList().add(Aspect.LIFE, 15).add(Aspect.EARTH, 5).add(Aspect.CRAFT, 15).add(RAT, 15));
        evt.register.registerObjectTag(new ItemStack(RatsBlockRegistry.MILK_CAULDRON), new AspectList().add(Aspect.LIFE, 5).add(Aspect.METAL, 5).add(Aspect.CRAFT, 5).add(RAT, 5));
        evt.register.registerObjectTag(new ItemStack(RatsBlockRegistry.CHEESE_CAULDRON), new AspectList().add(Aspect.LIFE, 5).add(Aspect.METAL, 5).add(Aspect.CRAFT, 5).add(RAT, 5));
        evt.register.registerObjectTag(new ItemStack(RatsBlockRegistry.RAT_TRAP), new AspectList().add(Aspect.TRAP, 40).add(RAT, 15).add(Aspect.DEATH, 15).add(Aspect.DESIRE, 5).add(Aspect.METAL, 5).add(Aspect.CRAFT, 5));
        evt.register.registerObjectTag(new ItemStack(RatsBlockRegistry.RAT_CAGE), new AspectList().add(RAT, 20).add(Aspect.PROTECT, 10).add(Aspect.LIFE, 5).add(Aspect.DESIRE, 5).add(Aspect.METAL, 5).add(Aspect.CRAFT, 5));
        evt.register.registerObjectTag(new ItemStack(RatsBlockRegistry.RAT_CAGE_DECORATED), new AspectList().add(RAT, 20).add(Aspect.PROTECT, 10).add(Aspect.LIFE, 5).add(Aspect.DESIRE, 5).add(Aspect.METAL, 5).add(Aspect.CRAFT, 5));
        evt.register.registerObjectTag(new ItemStack(RatsBlockRegistry.RAT_CAGE_BREEDING_LANTERN), new AspectList().add(RAT, 20).add(Aspect.PROTECT, 10).add(Aspect.LIFE, 5).add(Aspect.DESIRE, 5).add(Aspect.METAL, 5).add(Aspect.CRAFT, 5));
        evt.register.registerObjectTag(new ItemStack(RatsBlockRegistry.RAT_CRAFTING_TABLE), new AspectList().add(RAT, 20).add(Aspect.CRAFT, 10).add(Aspect.MECHANISM, 5));
        evt.register.registerObjectTag(new ItemStack(RatsBlockRegistry.RAT_HOLE), new AspectList().add(RAT, 20).add(Aspect.ENTROPY, 10));
        evt.register.registerObjectTag(new ItemStack(RatsBlockRegistry.GARBAGE_PILE), new AspectList().add(RAT, 5).add(Aspect.ENTROPY, 10).add(Aspect.DEATH, 2));
        evt.register.registerObjectTag(new ItemStack(RatsBlockRegistry.RATGLOVE_FLOWER), new AspectList().add(RAT, 5).add(Aspect.PLANT, 10));
        evt.register.registerObjectTag(new ItemStack(RatsBlockRegistry.MARBLED_CHEESE_RAW), new AspectList().add(RAT, 5).add(Aspect.EARTH, 5));
        evt.register.registerObjectTag(new ItemStack(RatsBlockRegistry.MARBLED_CHEESE), new AspectList().add(RAT, 5).add(Aspect.EARTH, 5));
        evt.register.registerObjectTag(new ItemStack(RatsBlockRegistry.RATLANTIS_PORTAL), new AspectList().add(RAT, 50).add(Aspect.ELDRITCH, 15).add(Aspect.SENSES, 15).add(Aspect.DESIRE, 15));
        evt.register.registerObjectTag(new ItemStack(RatsBlockRegistry.COMPRESSED_RAT), new AspectList().add(RAT, 100).add(Aspect.ELDRITCH, 5).add(Aspect.DESIRE, 5));
        evt.register.registerObjectTag(new ItemStack(RatsBlockRegistry.JACK_O_RATERN), new AspectList().add(RAT, 15).add(Aspect.PLANT, 5).add(Aspect.LIGHT, 5));
        evt.register.registerObjectTag(new ItemStack(RatsBlockRegistry.UPGRADE_COMBINER), new AspectList().add(RAT, 25).add(Aspect.MECHANISM, 15).add(Aspect.SENSES, 15).add(Aspect.SOUL, 15));
        //entities
        ThaumcraftApi.registerEntityTag("rat", new AspectList().add(RAT, 20).add(Aspect.AVERSION, 5).add(Aspect.MIND, 5).add(Aspect.DESIRE, 5).add(Aspect.BEAST, 10));
        ThaumcraftApi.registerEntityTag("illager_piper", new AspectList().add(RAT, 40).add(Aspect.MAGIC, 15).add(Aspect.MAN, 15).add(Aspect.MIND, 5).add(Aspect.DESIRE, 10).add(Aspect.SENSES, 10));
        ThaumcraftApi.registerEntityTag("ratlantean_spirit", new AspectList().add(RAT, 20).add(Aspect.SOUL, 15).add(Aspect.MIND, 5).add(Aspect.UNDEAD, 15));
        ThaumcraftApi.registerEntityTag("marbled_cheese_golem", new AspectList().add(RAT, 40).add(Aspect.MECHANISM, 35).add(Aspect.MIND, 5));
        ThaumcraftApi.registerEntityTag("feral_ratlantean", new AspectList().add(RAT, 30).add(Aspect.BEAST, 25).add(Aspect.MIND, 5));
        ThaumcraftApi.registerEntityTag("pirat", new AspectList().add(RAT, 30).add(Aspect.WATER, 20).add(Aspect.ENTROPY, 5));
        ThaumcraftApi.registerEntityTag("pirat_boat", new AspectList().add(RAT, 5).add(Aspect.WATER, 20).add(Aspect.ENTROPY, 5));
        ThaumcraftApi.registerEntityTag("neo_ratlantean", new AspectList().add(RAT, 55).add(Aspect.MIND, 50).add(Aspect.ELDRITCH, 45));

    }

}
