package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RatsVillagerRegistry {

	public static final DeferredRegister<PoiType> POIS = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, RatsMod.MODID);
	public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(Registries.VILLAGER_PROFESSION, RatsMod.MODID);

	public static final DeferredHolder<PoiType, PoiType> RATLANTIS_PORTAL = POIS.register("ratlantis_portal", () -> new PoiType(ImmutableSet.copyOf(RatlantisBlockRegistry.RATLANTIS_PORTAL.get().getStateDefinition().getPossibleStates()), 1, 1));
	public static final DeferredHolder<PoiType, PoiType> TRASH_CAN = POIS.register("trash_can", () -> new PoiType(ImmutableSet.copyOf(RatsBlockRegistry.TRASH_CAN.get().getStateDefinition().getPossibleStates()), 1, 1));
	public static final DeferredHolder<VillagerProfession, VillagerProfession> PET_SHOP_OWNER = PROFESSIONS.register("pet_shop_owner", () -> new VillagerProfession("pet_shop_owner", (poiType) -> RatConfig.villagePetShops && poiType.is(TRASH_CAN.getKey()), (poiType) -> RatConfig.villagePetShops && poiType.is(TRASH_CAN.getKey()), ImmutableSet.of(), ImmutableSet.of(), RatsSoundRegistry.TRASH_CAN.get()));
}







