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
    public static TileEntityType<TileEntityRatTube> RAT_TUBE = registerTileEntity(TileEntityType.Builder.create(TileEntityRatTube::new, RatsBlockRegistry.RAT_TUBE_COLOR), "rat_tube");
    public static TileEntityType<TileEntityUpgradeSeparator> UPGRADE_SEPERATOR = registerTileEntity(TileEntityType.Builder.create(TileEntityUpgradeSeparator::new, RatsBlockRegistry.UPGRADE_SEPARATOR), "upgrade_seperator");
    public static TileEntityType<TileEntityUpgradeCombiner> UPGRADE_COMBINER = registerTileEntity(TileEntityType.Builder.create(TileEntityUpgradeCombiner::new, RatsBlockRegistry.UPGRADE_COMBINER), "upgrade_combiner");
    public static TileEntityType<TileEntityAutoCurdler> AUTO_CURDLER = registerTileEntity(TileEntityType.Builder.create(TileEntityAutoCurdler::new, RatsBlockRegistry.AUTO_CURDLER), "auto_curdler");
    public static TileEntityType<TileEntityTrashCan> TRASH_CAN = registerTileEntity(TileEntityType.Builder.create(TileEntityTrashCan::new, RatsBlockRegistry.TRASH_CAN), "trash_can");
    public static TileEntityType<TileEntityRatAttractor> RAT_ATTRACTOR = registerTileEntity(TileEntityType.Builder.create(TileEntityRatAttractor::new, RatsBlockRegistry.RAT_ATTRACTOR), "rat_attractor");
    public static TileEntityType<TileEntityRatQuarry> RAT_QUARRY = registerTileEntity(TileEntityType.Builder.create(TileEntityRatQuarry::new, RatsBlockRegistry.RAT_QUARRY), "rat_quarry");
    public static TileEntityType<TileEntityRatCageWheel> RAT_CAGE_WHEEL = registerTileEntity(TileEntityType.Builder.create(TileEntityRatCageWheel::new, RatsBlockRegistry.RAT_CAGE_WHEEL), "rat_cage_wheel");

    public static TileEntityType registerTileEntity(TileEntityType.Builder builder, String entityName){
        ResourceLocation nameLoc = new ResourceLocation(RatsMod.MODID, entityName);
        return (TileEntityType) builder.build(null).setRegistryName(nameLoc);
    }

}
