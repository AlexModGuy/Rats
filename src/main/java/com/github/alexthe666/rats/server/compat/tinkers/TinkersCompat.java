package com.github.alexthe666.rats.server.compat.tinkers;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.EntityPlagueShot;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.HarvestLevels;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.shared.TinkerFluids;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerTraits;

public class TinkersCompat {
    public static final Material CHEESE_MATERIAL = new Material("cheese", 0XF2C132);
    public static final Material STRING_CHEESE = new Material("string_cheese", 0XF2C132);
    public static final Material PLAGUE_METAL_MATERIAL = new Material("plague_metal", 0X515450);
    public static final AbstractTrait RAT_TRAIT = new TraitRatty();
    public static final TraitPlagueShot PLAGUE_TRAIT = new TraitPlagueShot();
    public static final AbstractTrait DISEASED_TRAIT = new TraitDisease();
    private static final TinkersCompat INSTANCE = new TinkersCompat();
    private static boolean registered = false;

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
        TinkerMaterials.materials.add(PLAGUE_METAL_MATERIAL);
        TinkerRegistry.integrate(CHEESE_MATERIAL).preInit();
        TinkerRegistry.integrate(STRING_CHEESE).preInit();
        TinkerRegistry.integrate(PLAGUE_METAL_MATERIAL).preInit();
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

        PLAGUE_METAL_MATERIAL.addTrait(DISEASED_TRAIT);
        PLAGUE_METAL_MATERIAL.addTrait(PLAGUE_TRAIT);
        PLAGUE_METAL_MATERIAL.addItem(RatsItemRegistry.PLAGUE_ESSENCE, 1, Material.VALUE_Ingot);
        PLAGUE_METAL_MATERIAL.setRepresentativeItem(RatsItemRegistry.PLAGUE_ESSENCE);
        PLAGUE_METAL_MATERIAL.setCraftable(true);
        PLAGUE_METAL_MATERIAL.setCastable(false);
        TinkerRegistry.addMaterialStats(PLAGUE_METAL_MATERIAL,
                new HeadMaterialStats(780, 1.00f, 6.00f, HarvestLevels.IRON),
                new HandleMaterialStats(1.1F, -10),
                new ExtraMaterialStats(180));
        TinkerRegistry.addMaterialStats(PLAGUE_METAL_MATERIAL, new BowMaterialStats(2F, 3F, 1.5F));

    }

    public static void post() {
        TinkerRegistry.registerBasinCasting(new CastingRecipe(new ItemStack(RatsBlockRegistry.BLOCK_OF_CHEESE), TinkerFluids.milk, 1000, RatsMod.CONFIG_OPTIONS.milkCauldronTime));
    }

    public static boolean onPlayerSwing(EntityLivingBase swinger, ItemStack stack){
        if(PLAGUE_TRAIT.isToolWithTrait(stack)){
            float totalDmg = ToolHelper.getActualDamage(stack, swinger);
            EntityPlagueShot shot = new EntityPlagueShot(swinger.world, swinger, totalDmg * 0.5F);
            shot.shoot(swinger, swinger.rotationPitch, swinger.rotationYaw, 0.0F, 0.8F, 1.0F);
            if (!swinger.world.isRemote) {
                swinger.world.spawnEntity(shot);
            }
            return true;
        }
        return false;
    }
}
