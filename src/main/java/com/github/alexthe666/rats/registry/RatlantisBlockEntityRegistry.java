package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.block.entity.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RatlantisBlockEntityRegistry {

	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RatsMod.MODID);

	public static final RegistryObject<BlockEntityType<PiratSignBlockEntity>> PIRAT_SIGN = BLOCK_ENTITIES.register("pirat_sign", () -> BlockEntityType.Builder.of(PiratSignBlockEntity::new, RatlantisBlockRegistry.PIRAT_SIGN.get(), RatlantisBlockRegistry.PIRAT_WALL_SIGN.get()).build(null));
	public static final RegistryObject<BlockEntityType<PiratHangingSignBlockEntity>> PIRAT_HANGING_SIGN = BLOCK_ENTITIES.register("pirat_hanging_sign", () -> BlockEntityType.Builder.of(PiratHangingSignBlockEntity::new, RatlantisBlockRegistry.PIRAT_HANGING_SIGN.get(), RatlantisBlockRegistry.PIRAT_WALL_HANGING_SIGN.get()).build(null));

	public static final RegistryObject<BlockEntityType<RatlantisPortalBlockEntity>> RATLANTIS_PORTAL = BLOCK_ENTITIES.register("ratlantis_portal", () -> BlockEntityType.Builder.of(RatlantisPortalBlockEntity::new, RatlantisBlockRegistry.RATLANTIS_PORTAL.get()).build(null));
	public static final RegistryObject<BlockEntityType<DutchratBellBlockEntity>> DUTCHRAT_BELL = BLOCK_ENTITIES.register("dutchrat_bell", () -> BlockEntityType.Builder.of(DutchratBellBlockEntity::new, RatlantisBlockRegistry.DUTCHRAT_BELL.get()).build(null));
	public static final RegistryObject<BlockEntityType<RatlanteanAutomatonHeadBlockEntity>> AUTOMATON_HEAD = BLOCK_ENTITIES.register("ratlantean_automaton_head", () -> BlockEntityType.Builder.of(RatlanteanAutomatonHeadBlockEntity::new, RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD.get()).build(null));
	public static final RegistryObject<BlockEntityType<RatlantisTokenBlockEntity>> TOKEN = BLOCK_ENTITIES.register("chunky_cheese_token", () -> BlockEntityType.Builder.of(RatlantisTokenBlockEntity::new, RatlantisBlockRegistry.CHUNKY_CHEESE_TOKEN.get()).build(null));
	public static final RegistryObject<BlockEntityType<RatlantisReactorBlockEntity>> RATLANTIS_REACTOR = BLOCK_ENTITIES.register("ratlantis_reactor", () -> BlockEntityType.Builder.of(RatlantisReactorBlockEntity::new, RatlantisBlockRegistry.RATLANTIS_REACTOR.get()).build(null));
}
