package com.github.alexthe666.rats.server.entity.tile;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RatsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)

public class RatsTileEntityRegistry {
    public static TileEntityType<TileEntityRatHole> RAT_HOLE = registerTileEntity(TileEntityType.Builder.create(TileEntityRatHole::new, RatsBlockRegistry.RAT_HOLE), "rat_hole");
    public static TileEntityType<TileEntityRatTrap> RAT_TRAP = registerTileEntity(TileEntityType.Builder.create(TileEntityRatTrap::new, RatsBlockRegistry.RAT_TRAP), "rat_trap");
    public static TileEntityType<TileEntityMilkCauldron> MILK_CAULDRON = registerTileEntity(TileEntityType.Builder.create(TileEntityMilkCauldron::new, RatsBlockRegistry.MILK_CAULDRON), "milk_cauldron");
    public static TileEntityType<TileEntityRatCageDecorated> RAT_CAGE_DECORATED = registerTileEntity(TileEntityType.Builder.create(TileEntityRatCageDecorated::new, RatsBlockRegistry.RAT_CAGE_DECORATED), "rat_cage_decorated");
    public static TileEntityType<TileEntityRatCageBreedingLantern> RAT_CAGE_BREEDING_LANTERN = registerTileEntity(TileEntityType.Builder.create(TileEntityRatCageBreedingLantern::new, RatsBlockRegistry.RAT_CAGE_BREEDING_LANTERN), "rat_cage_breeding_lantern");
    public static TileEntityType<TileEntityRatCraftingTable> RAT_CRAFTING_TABLE = registerTileEntity(TileEntityType.Builder.create(TileEntityRatCraftingTable::new, RatsBlockRegistry.RAT_CRAFTING_TABLE), "rat_crafting_table");
    public static TileEntityType<TileEntityRatlantisPortal> RATLANTIS_PORTAL = registerTileEntity(TileEntityType.Builder.create(TileEntityRatHole::new, RatsBlockRegistry.RATLANTIS_PORTAL), "ratlantis_portal");
    public static TileEntityType<TileEntityRatTube> RAT_TUBE = registerTileEntity(TileEntityType.Builder.create(TileEntityRatTube::new, RatsBlockRegistry.RAT_TUBE_COLOR), "rat_tube");
    public static TileEntityType<TileEntityUpgradeSeparator> UPGRADE_SEPERATOR = registerTileEntity(TileEntityType.Builder.create(TileEntityUpgradeSeparator::new, RatsBlockRegistry.UPGRADE_SEPARATOR), "upgrade_seperator");
    public static TileEntityType<TileEntityUpgradeCombiner> UPGRADE_COMBINER = registerTileEntity(TileEntityType.Builder.create(TileEntityUpgradeCombiner::new, RatsBlockRegistry.UPGRADE_COMBINER), "upgrade_combiner");
    public static TileEntityType<TileEntityAutoCurdler> AUTO_CURDLER = registerTileEntity(TileEntityType.Builder.create(TileEntityAutoCurdler::new, RatsBlockRegistry.AUTO_CURDLER), "auto_curdler");
    public static TileEntityType<TileEntityDutchratBell> DUTCHRAT_BELL = registerTileEntity(TileEntityType.Builder.create(TileEntityDutchratBell::new, RatsBlockRegistry.DUTCHRAT_BELL), "dutchrat_bell");
    public static TileEntityType<TileEntityRatlanteanAutomatonHead> AUTOMATON_HEAD = registerTileEntity(TileEntityType.Builder.create(TileEntityRatlanteanAutomatonHead::new, RatsBlockRegistry.MARBLED_CHEESE_RAT_HEAD), "ratlantean_automaton_head");

    public static TileEntityType registerTileEntity(TileEntityType.Builder builder, String entityName){
        ResourceLocation nameLoc = new ResourceLocation(RatsMod.MODID, entityName);
        return (TileEntityType) builder.build(null).setRegistryName(nameLoc);
    }

}
