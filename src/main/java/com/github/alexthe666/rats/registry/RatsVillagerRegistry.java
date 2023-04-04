package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RatsVillagerRegistry {

	public static final DeferredRegister<PoiType> POIS = DeferredRegister.create(ForgeRegistries.POI_TYPES, RatsMod.MODID);
	public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, RatsMod.MODID);

	public static final RegistryObject<PoiType> RATLANTIS_PORTAL = POIS.register("ratlantis_portal", () -> new PoiType(ImmutableSet.copyOf(RatlantisBlockRegistry.RATLANTIS_PORTAL.get().getStateDefinition().getPossibleStates()), 1, 1));
	public static final RegistryObject<PoiType> TRASH_CAN = POIS.register("trash_can", () -> new PoiType(ImmutableSet.copyOf(RatsBlockRegistry.TRASH_CAN.get().getStateDefinition().getPossibleStates()), 1, 1));
	public static final RegistryObject<VillagerProfession> PET_SHOP_OWNER = PROFESSIONS.register("pet_shop_owner", () -> new VillagerProfession("pet_shop_owner", (poiType) -> RatConfig.villagePetShops && poiType.is(TRASH_CAN.getKey()), (poiType) -> RatConfig.villagePetShops && poiType.is(TRASH_CAN.getKey()), ImmutableSet.of(), ImmutableSet.of(), RatsSoundRegistry.TRASH_CAN.get()));
}
