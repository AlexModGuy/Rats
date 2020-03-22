package com.github.alexthe666.rats.server.world.village;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

public class RatsVillageRegistry {
    public static final VillagerRegistry.VillagerProfession PET_SHOP_OWNER = new VillagerRegistry.VillagerProfession("rats:pet_shop_owner", "rats:textures/entity/villager_pet_shop_owner.png", "rats:textures/entity/zombie_villager_pet_shop_owner.png");
    public static final VillagerRegistry.VillagerProfession PLAGUE_DOCTOR = new VillagerRegistry.VillagerProfession("rats:plague_doctor", "rats:textures/entity/villager_pet_shop_owner.png", "rats:textures/entity/zombie_villager_pet_shop_owner.png");

    public static void register() {
        if (RatsMod.CONFIG_OPTIONS.villageGarbageHeaps) {
            MapGenStructureIO.registerStructureComponent(VillageComponentGarbageHeap.class, "garbage_heap");
            VillagerRegistry.instance().registerVillageCreationHandler(new VillageCreatorGarbageHeap());
        }
        if (RatsMod.CONFIG_OPTIONS.villagePetShops) {
            MapGenStructureIO.registerStructureComponent(VillageComponentPetShop.class, "pet_shop");
            VillagerRegistry.instance().registerVillageCreationHandler(new VillageCreatorPetShop());
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(PET_SHOP_OWNER, "pet_shop_owner");
            career.addTrade(1, new EntityVillager.ListItemForEmeralds(Items.BONE, new EntityVillager.PriceInfo(1, 7)));
            career.addTrade(1, new EntityVillager.EmeraldForItems(Items.FISH, new EntityVillager.PriceInfo(1, 7)));
            career.addTrade(1, new EntityVillager.ListItemForEmeralds(Items.EGG, new EntityVillager.PriceInfo(1, 4)));
            career.addTrade(2, new EntityVillager.EmeraldForItems(RatsItemRegistry.RAW_RAT, new EntityVillager.PriceInfo(1, 7)));
            career.addTrade(2, new EntityVillager.ListItemForEmeralds(RatsItemRegistry.CHEESE, new EntityVillager.PriceInfo(3, 10)));
            career.addTrade(2, new EntityVillager.ListItemForEmeralds(RatsItemRegistry.COOKED_RAT, new EntityVillager.PriceInfo(4, 6)));
            career.addTrade(2, new EntityVillager.ItemAndEmeraldToItem(Item.getItemFromBlock(RatsBlockRegistry.GARBAGE_PILE), new EntityVillager.PriceInfo(1, 6), RatsItemRegistry.PLASTIC_WASTE, new EntityVillager.PriceInfo(1, 4)));
            career.addTrade(2, new EntityVillager.ListItemForEmeralds(RatsItemRegistry.FEATHERY_WING, new EntityVillager.PriceInfo(1, 2)));
            career.addTrade(3, new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(RatsBlockRegistry.RAT_CAGE), new EntityVillager.PriceInfo(2, 5)));
            career.addTrade(3, new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(RatsBlockRegistry.GARBAGE_PILE), new EntityVillager.PriceInfo(1, 7)));
            career.addTrade(3, new EntityVillager.EmeraldForItems(RatsItemRegistry.RAW_PLASTIC, new EntityVillager.PriceInfo(1, 7)));
            career.addTrade(3, new EntityVillager.EmeraldForItems(RatsItemRegistry.RAT_FLUTE, new EntityVillager.PriceInfo(1, 4)));
            career.addTrade(4, new EntityVillager.ListItemForEmeralds(new ItemStack(RatsBlockRegistry.RAT_TUBE_COLOR[0]), new EntityVillager.PriceInfo(4, 8)));
            career.addTrade(4, new EntityVillager.EmeraldForItems(Item.getItemFromBlock(RatsBlockRegistry.RAT_CAGE_BREEDING_LANTERN), new EntityVillager.PriceInfo(1, 4)));
            career.addTrade(4, new EntityVillager.EmeraldForItems(RatsItemRegistry.RAT_SEED_BOWL, new EntityVillager.PriceInfo(1, 4)));
            career.addTrade(4, new EntityVillager.ListItemForEmeralds(RatsItemRegistry.RAT_HAMMOCKS[0], new EntityVillager.PriceInfo(1, 5)));
            career.addTrade(5, new EntityVillager.ListItemForEmeralds(RatsItemRegistry.RAT_IGLOOS[0], new EntityVillager.PriceInfo(1, 6)));
            career.addTrade(5, new EntityVillager.ListItemForEmeralds(RatsItemRegistry.RAT_UPGRADE_BASIC, new EntityVillager.PriceInfo(1, 25)));
        }
        if (RatsMod.CONFIG_OPTIONS.villagePlagueDoctors) {
            MapGenStructureIO.registerStructureComponent(VillageComponentPlagueDoctor.class, "plague_doctor_hut");
            VillagerRegistry.instance().registerVillageCreationHandler(new VillageCreatorPlagueDoctor());
        }
         VillagerRegistry.VillagerCareer doctor = new VillagerRegistry.VillagerCareer(PLAGUE_DOCTOR, "plague_doctor");
        doctor.addTrade(1, new EntityVillager.EmeraldForItems(Items.BONE, new EntityVillager.PriceInfo(1, 25)));
        doctor.addTrade(1, new EntityVillager.ListItemForEmeralds(Items.ROTTEN_FLESH, new EntityVillager.PriceInfo(3, 17)));
        doctor.addTrade(1, new EntityVillager.EmeraldForItems(Items.POISONOUS_POTATO, new EntityVillager.PriceInfo(2, 4)));
        doctor.addTrade(1, new EntityVillager.EmeraldForItems(Items.POISONOUS_POTATO, new EntityVillager.PriceInfo(2, 4)));
        doctor.addTrade(1, new EntityVillager.ItemAndEmeraldToItem(Item.getItemFromBlock(Blocks.RED_FLOWER), new EntityVillager.PriceInfo(1, 2), RatsItemRegistry.HERB_BUNDLE, new EntityVillager.PriceInfo(1, 5)));
        doctor.addTrade(2, new EntityVillager.ListItemForEmeralds(RatsItemRegistry.TREACLE, new EntityVillager.PriceInfo(1, 4)));
        doctor.addTrade(2, new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(RatsBlockRegistry.GARBAGE_PILE), new EntityVillager.PriceInfo(1, 2)));
        doctor.addTrade(3, new EntityVillager.ListItemForEmeralds(RatsItemRegistry.PLAGUE_LEECH, new EntityVillager.PriceInfo(3, 3)));
        doctor.addTrade(4, new EntityVillager.ListItemForEmeralds(RatsItemRegistry.PLAGUE_STEW, new EntityVillager.PriceInfo(1, 8)));
        doctor.addTrade(4, new EntityVillager.ListItemForEmeralds(RatsItemRegistry.PLAGUE_ESSENCE, new EntityVillager.PriceInfo(1, 4)));
        doctor.addTrade(5, new EntityVillager.ListItemForEmeralds(RatsItemRegistry.PLAGUE_DOCTOR_MASK, new EntityVillager.PriceInfo(1, 15)));
        doctor.addTrade(5, new EntityVillager.ListItemForEmeralds(RatsItemRegistry.PURIFYING_LIQUID, new EntityVillager.PriceInfo(1, 12)));
    }
}
