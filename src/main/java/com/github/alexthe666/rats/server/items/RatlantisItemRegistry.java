package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.ratlantis.RatlantisEntityRegistry;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.FishBucketItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RatsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RatlantisItemRegistry {

    public static final Item RATLANTIS_RAT_SKULL = new ItemGeneric("ratlantis_rat_skull", RatsMod.getRatlantisTab());

    public static final Item AVIATOR_HAT = new ItemHat("aviator_hat", 13, RatsMod.getRatlantisTab());

    public static final Item RAT_TOGA = new ItemGeneric("rat_toga", RatsMod.getRatlantisTab(), 2);

    public static final Item RATGLOVE_PETALS = new ItemGeneric("ratglove_petals", RatsMod.getRatlantisTab());

    public static final Item FERAL_RAT_CLAW = new ItemGeneric("feral_rat_claw", RatsMod.getRatlantisTab());

    public static final Item FERAL_BAGH_NAKHS = new ItemBaghNakhs();

    public static final Item GEM_OF_RATLANTIS = new ItemGeneric("gem_of_ratlantis", RatsMod.getRatlantisTab());

    public static final Item ORATCHALCUM_INGOT = new ItemGeneric("oratchalcum_ingot", RatsMod.getRatlantisTab());

    public static final Item ORATCHALCUM_NUGGET = new ItemGeneric("oratchalcum_nugget", RatsMod.getRatlantisTab());

    public static final Item RATLANTIS_HELMET = new ItemRatlantisArmor(RatsItemRegistry.RATLANTIS_ARMOR_MATERIAL, EquipmentSlotType.HEAD, "ratlantis_helmet");

    public static final Item RATLANTIS_CHESTPLATE = new ItemRatlantisArmor(RatsItemRegistry.RATLANTIS_ARMOR_MATERIAL, EquipmentSlotType.CHEST, "ratlantis_chestplate");

    public static final Item RATLANTIS_LEGGINGS = new ItemRatlantisArmor(RatsItemRegistry.RATLANTIS_ARMOR_MATERIAL, EquipmentSlotType.LEGS, "ratlantis_leggings");

    public static final Item RATLANTIS_BOOTS = new ItemRatlantisArmor(RatsItemRegistry.RATLANTIS_ARMOR_MATERIAL, EquipmentSlotType.FEET, "ratlantis_boots");

    public static final Item RATLANTIS_SWORD = new ItemRatlantisSword();

    public static final Item RATLANTIS_PICKAXE = new ItemRatlantisTool.Pickaxe();

    public static final Item RATLANTIS_AXE = new ItemRatlantisTool.Axe();

    public static final Item RATLANTIS_SHOVEL = new ItemRatlantisTool.Shovel();

    public static final Item RATLANTIS_HOE = new ItemRatlantisTool.Hoe();

    public static final Item RATLANTIS_BOW = new ItemRatlantisBow();

    public static final Item ARCANE_TECHNOLOGY = new ItemGeneric("arcane_technology", RatsMod.getRatlantisTab(), 2);

    public static final Item ANCIENT_SAWBLADE = new ItemGeneric("ancient_sawblade", RatsMod.getRatlantisTab());

    public static final Item RATLANTEAN_FLAME = new ItemRatlanteanFlame();

    public static final Item VIAL_OF_SENTIENCE = new ItemVialOfSentience();

    public static final Item PSIONIC_RAT_BRAIN = new ItemGeneric("psionic_rat_brain", RatsMod.getRatlantisTab(), 2);

    public static final Item PIRAT_CUTLASS = new ItemPiratCutlass(false);

    public static final Item CHEESE_CANNONBALL = new ItemGeneric("cheese_cannonball", RatsMod.getRatlantisTab(), 1);

    public static final Item GHOST_PIRAT_HAT = new ItemHat("ghost_pirat_hat", 9, RatsMod.getRatlantisTab());

    public static final Item GHOST_PIRAT_ECTOPLASM = new ItemGeneric("ghost_pirat_ectoplasm", RatsMod.getRatlantisTab());

    public static final Item GHOST_PIRAT_CUTLASS = new ItemPiratCutlass(true);

    public static final Item DUTCHRAT_WHEEL = new ItemGeneric("dutchrat_wheel", RatsMod.getRatlantisTab());

    public static final Item MILITARY_HAT = new ItemHat("military_hat", 12, RatsMod.getRatlantisTab());

    public static final Item BIPLANE_WING = new ItemGeneric("biplane_wing", RatsMod.getRatlantisTab());

    public static final Item RATFISH = new ItemGenericFood(1, 0.35F, false, false, false, "ratfish", RatsMod.getRatlantisTab());

    public static final Item RATFISH_BUCKET = new FishBucketItem(RatlantisEntityRegistry.RATFISH, Fluids.WATER, (new Item.Properties()).maxStackSize(1).group(RatsMod.getRatlantisTab())).setRegistryName("ratfish_bucket");

    public static final Item RATBOT_BARREL = new ItemGeneric("ratbot_barrel", RatsMod.getRatlantisTab());

    public static final Item CHARGED_RATBOT_BARREL = new ItemGeneric("charged_ratbot_barrel", RatsMod.getRatlantisTab());

    public static final Item RATTLING_GUN = new ItemRattlingGun();

    public static final Item IDOL_OF_RATLANTIS = new ItemGeneric("idol_of_ratlantis", RatsMod.getRatlantisTab(), 1);

    public static final Item RAT_UPGRADE_ARCHEOLOGIST = new ItemRatUpgrade("rat_upgrade_archeologist", 2, 1);

    public static final Item RAT_UPGRADE_AUTOMATON_MOUNT = new ItemRatUpgradeMount("rat_upgrade_automaton_mount", 2, 3, RatlantisEntityRegistry.RAT_MOUNT_AUTOMATON);

    public static final Item RAT_UPGRADE_BIPLANE_MOUNT = new ItemRatUpgradeMount("rat_upgrade_biplane_mount", 2, 3, RatlantisEntityRegistry.RAT_MOUNT_BIPLANE);

    public static final Item RAT_UPGRADE_BASIC_RATLANTEAN = new ItemRatUpgrade("rat_upgrade_basic_ratlantean");

    public static final Item RAT_UPGRADE_FERAL_BITE = new ItemRatUpgrade("rat_upgrade_feral_bite");

    public static final Item RAT_UPGRADE_BUCCANEER = new ItemRatUpgrade("rat_upgrade_buccaneer", 1, 2);

    public static final Item RAT_UPGRADE_RATINATOR = new ItemRatUpgrade("rat_upgrade_ratinator", 2, 3);

    public static final Item RAT_UPGRADE_PSYCHIC = new ItemRatUpgrade("rat_upgrade_psychic", 2, 3);

    public static final Item RAT_UPGRADE_ETHEREAL = new ItemRatUpgrade("rat_upgrade_ethereal", 1, 2);

    public static final Item RAT_UPGRADE_NONBELIEVER = new ItemRatUpgrade("rat_upgrade_nonbeliever", 4, 4);

}
