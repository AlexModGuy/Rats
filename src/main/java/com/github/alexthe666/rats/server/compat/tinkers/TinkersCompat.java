package com.github.alexthe666.rats.server.compat.tinkers;

import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraftforge.common.MinecraftForge;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.HarvestLevels;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerTraits;
import slimeknights.tconstruct.tools.traits.TraitTasty;

public class TinkersCompat {
    public static final Material CHEESE_MATERIAL = new Material("cheese", 0XF2C132);
    public static final Material STRING_CHEESE = new Material("string_cheese", 0XF2C132);
    private static boolean registered = false;
    private static final TinkersCompat INSTANCE = new TinkersCompat();
    public static final AbstractTrait RAT_TRAIT = new TraitRatty();

    public static void register() {
        if (!registered) {
            registered = true;
            MinecraftForge.EVENT_BUS.register(INSTANCE);
            init();
        } else {
            throw new RuntimeException("You can only call TinkersCompat.register() once");
        }
    }

    public static void init() {
        TinkerMaterials.materials.add(CHEESE_MATERIAL);
        TinkerMaterials.materials.add(STRING_CHEESE);
        TinkerRegistry.integrate(CHEESE_MATERIAL);
        TinkerRegistry.integrate(STRING_CHEESE);
        CHEESE_MATERIAL.addTrait(RAT_TRAIT);
        CHEESE_MATERIAL.addTrait(TinkerTraits.tasty);
        CHEESE_MATERIAL.addItem("foodCheese", 1, Material.VALUE_Ingot);
        CHEESE_MATERIAL.addItem(RatsItemRegistry.CHEESE, 1, Material.VALUE_Ingot);
        CHEESE_MATERIAL.setRepresentativeItem(RatsItemRegistry.CHEESE);
        CHEESE_MATERIAL.setCraftable(true);
        CHEESE_MATERIAL.setCastable(false);
        TinkerRegistry.addMaterialStats(CHEESE_MATERIAL,
                new HeadMaterialStats(300, 4.00f, 2.00f, HarvestLevels.IRON),
                new HandleMaterialStats(1.5F, 40),
                new ExtraMaterialStats(150));
        TinkerRegistry.addMaterialStats(CHEESE_MATERIAL, new BowMaterialStats(1.5F, 2f, -0.5F));
        STRING_CHEESE.addItem(RatsItemRegistry.STRING_CHEESE, 1, Material.VALUE_Ingot);
        STRING_CHEESE.setRepresentativeItem(RatsItemRegistry.STRING_CHEESE);
        BowStringMaterialStats bowstring = new BowStringMaterialStats(1.25f);
        TinkerRegistry.addMaterialStats(STRING_CHEESE, bowstring);

    }

    public static void post() {
    }
}
