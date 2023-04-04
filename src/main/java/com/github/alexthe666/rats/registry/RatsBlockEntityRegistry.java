package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.block.entity.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RatsBlockEntityRegistry {

	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RatsMod.MODID);

	public static final RegistryObject<BlockEntityType<RatHoleBlockEntity>> RAT_HOLE = BLOCK_ENTITIES.register("rat_hole", () -> BlockEntityType.Builder.of(RatHoleBlockEntity::new, RatsBlockRegistry.RAT_HOLE.get()).build(null));
	public static final RegistryObject<BlockEntityType<RatTrapBlockEntity>> RAT_TRAP = BLOCK_ENTITIES.register("rat_trap", () -> BlockEntityType.Builder.of(RatTrapBlockEntity::new, RatsBlockRegistry.RAT_TRAP.get()).build(null));
	public static final RegistryObject<BlockEntityType<MilkCauldronBlockEntity>> MILK_CAULDRON = BLOCK_ENTITIES.register("milk_cauldron", () -> BlockEntityType.Builder.of(MilkCauldronBlockEntity::new, RatsBlockRegistry.MILK_CAULDRON.get()).build(null));
	public static final RegistryObject<BlockEntityType<DecoratedRatCageBlockEntity>> RAT_CAGE_DECORATED = BLOCK_ENTITIES.register("rat_cage_decorated", () -> BlockEntityType.Builder.of(DecoratedRatCageBlockEntity::new, RatsBlockRegistry.RAT_CAGE_DECORATED.get()).build(null));
	public static final RegistryObject<BlockEntityType<RatCageBreedingLanternBlockEntity>> RAT_CAGE_BREEDING_LANTERN = BLOCK_ENTITIES.register("rat_cage_breeding_lantern", () -> BlockEntityType.Builder.of(RatCageBreedingLanternBlockEntity::new, RatsBlockRegistry.RAT_CAGE_BREEDING_LANTERN.get()).build(null));
	public static final RegistryObject<BlockEntityType<RatCraftingTableBlockEntity>> RAT_CRAFTING_TABLE = BLOCK_ENTITIES.register("rat_crafting_table", () -> BlockEntityType.Builder.of(RatCraftingTableBlockEntity::new, RatsBlockRegistry.RAT_CRAFTING_TABLE.get()).build(null));
	public static final RegistryObject<BlockEntityType<RatTubeBlockEntity>> RAT_TUBE = BLOCK_ENTITIES.register("rat_tube", () -> BlockEntityType.Builder.of(RatTubeBlockEntity::new, RatsBlockRegistry.RAT_TUBE_COLOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<UpgradeSeparatorBlockEntity>> UPGRADE_SEPERATOR = BLOCK_ENTITIES.register("upgrade_separator", () -> BlockEntityType.Builder.of(UpgradeSeparatorBlockEntity::new, RatsBlockRegistry.UPGRADE_SEPARATOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<UpgradeCombinerBlockEntity>> UPGRADE_COMBINER = BLOCK_ENTITIES.register("upgrade_combiner", () -> BlockEntityType.Builder.of(UpgradeCombinerBlockEntity::new, RatsBlockRegistry.UPGRADE_COMBINER.get()).build(null));
	public static final RegistryObject<BlockEntityType<AutoCurdlerBlockEntity>> AUTO_CURDLER = BLOCK_ENTITIES.register("auto_curdler", () -> BlockEntityType.Builder.of(AutoCurdlerBlockEntity::new, RatsBlockRegistry.AUTO_CURDLER.get()).build(null));
	public static final RegistryObject<BlockEntityType<TrashCanBlockEntity>> TRASH_CAN = BLOCK_ENTITIES.register("trash_can", () -> BlockEntityType.Builder.of(TrashCanBlockEntity::new, RatsBlockRegistry.TRASH_CAN.get()).build(null));
	public static final RegistryObject<BlockEntityType<RatAttractorBlockEntity>> RAT_ATTRACTOR = BLOCK_ENTITIES.register("rat_attractor", () -> BlockEntityType.Builder.of(RatAttractorBlockEntity::new, RatsBlockRegistry.RAT_ATTRACTOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<RatQuarryBlockEntity>> RAT_QUARRY = BLOCK_ENTITIES.register("rat_quarry", () -> BlockEntityType.Builder.of(RatQuarryBlockEntity::new, RatsBlockRegistry.RAT_QUARRY.get()).build(null));
	public static final RegistryObject<BlockEntityType<RatCageWheelBlockEntity>> RAT_CAGE_WHEEL = BLOCK_ENTITIES.register("rat_cage_wheel", () -> BlockEntityType.Builder.of(RatCageWheelBlockEntity::new, RatsBlockRegistry.RAT_CAGE_WHEEL.get()).build(null));
}
