package com.github.alexthe666.rats.server.events;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import com.github.alexthe666.rats.server.entity.misc.PiratBoat;
import com.github.alexthe666.rats.server.entity.misc.PlagueDoctor;
import com.github.alexthe666.rats.server.entity.misc.RatProtector;
import com.github.alexthe666.rats.server.entity.misc.Ratfish;
import com.github.alexthe666.rats.server.entity.monster.*;
import com.github.alexthe666.rats.server.entity.monster.boss.*;
import com.github.alexthe666.rats.server.entity.mount.*;
import com.github.alexthe666.rats.server.entity.rat.DemonRat;
import com.github.alexthe666.rats.server.entity.rat.Rat;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RatsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(RatsEntityRegistry.RAT.get(), Rat.createAttributes().build());
		event.put(RatsEntityRegistry.TAMED_RAT.get(), TamedRat.createAttributes().build());
		event.put(RatsEntityRegistry.PIED_PIPER.get(), PiedPiper.createAttributes().build());
		event.put(RatsEntityRegistry.PLAGUE_DOCTOR.get(), PlagueDoctor.createAttributes().build());
		event.put(RatsEntityRegistry.BLACK_DEATH.get(), BlackDeath.createAttributes().build());
		event.put(RatsEntityRegistry.PLAGUE_CLOUD.get(), PlagueCloud.createAttributes().build());
		event.put(RatsEntityRegistry.PLAGUE_BEAST.get(), PlagueBeast.createAttributes().build());
		event.put(RatsEntityRegistry.RAT_MOUNT_CHICKEN.get(), RatChickenMount.createAttributes().build());
		event.put(RatsEntityRegistry.RAT_MOUNT_BEAST.get(), RatBeastMount.createAttributes().build());
		event.put(RatsEntityRegistry.RAT_KING.get(), RatKing.createAttributes().build());
		event.put(RatsEntityRegistry.DEMON_RAT.get(), DemonRat.createAttributes().build());
		event.put(RatsEntityRegistry.RAT_STRIDER_MOUNT.get(), RatStriderMount.createAttributes().build());
		event.put(RatsEntityRegistry.RAT_MOUNT_GOLEM.get(), RatGolemMount.createAttributes().build());
		event.put(RatlantisEntityRegistry.RATLANTEAN_SPIRIT.get(), RatlanteanSpirit.createAttributes().build());
		event.put(RatlantisEntityRegistry.RATLANTEAN_AUTOMATON.get(), RatlanteanAutomaton.createAttributes().build());
		event.put(RatlantisEntityRegistry.FERAL_RATLANTEAN.get(), FeralRatlantean.createAttributes().build());
		event.put(RatlantisEntityRegistry.NEO_RATLANTEAN.get(), NeoRatlantean.createAttributes().build());
		event.put(RatlantisEntityRegistry.PIRAT.get(), Pirat.createAttributes().build());
		event.put(RatlantisEntityRegistry.PIRAT_BOAT.get(), PiratBoat.createAttributes().build());
		event.put(RatlantisEntityRegistry.GHOST_PIRAT.get(), GhostPirat.createAttributes().build());
		event.put(RatlantisEntityRegistry.DUTCHRAT.get(), Dutchrat.createAttributes().build());
		event.put(RatlantisEntityRegistry.RATFISH.get(), Ratfish.createAttributes().build());
		event.put(RatlantisEntityRegistry.RATLANTEAN_RATBOT.get(), RatlanteanRatbot.createAttributes().build());
		event.put(RatlantisEntityRegistry.RAT_MOUNT_AUTOMATON.get(), RatAutomatonMount.createAttributes().build());
		event.put(RatlantisEntityRegistry.RAT_BARON.get(), RatBaron.createAttributes().build());
		event.put(RatlantisEntityRegistry.RAT_BARON_PLANE.get(), RatBaronPlane.createAttributes().build());
		event.put(RatlantisEntityRegistry.RAT_MOUNT_BIPLANE.get(), RatBiplaneMount.createAttributes().build());
		event.put(RatlantisEntityRegistry.RAT_PROTECTOR.get(), RatProtector.createAttributes().build());
	}

	@SubscribeEvent
	public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
		event.register(RatsEntityRegistry.RAT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Rat::checkRatSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
		event.register(RatsEntityRegistry.PIED_PIPER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PiedPiper::checkPiperSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
		event.register(RatsEntityRegistry.DEMON_RAT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DemonRat::canDemonRatSpawnOn, SpawnPlacementRegisterEvent.Operation.REPLACE);
		event.register(RatlantisEntityRegistry.FERAL_RATLANTEAN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FeralRatlantean::checkRatlanteanSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
		event.register(RatlantisEntityRegistry.RATLANTEAN_SPIRIT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, RatlanteanSpirit::checkSpiritSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
		event.register(RatlantisEntityRegistry.PIRAT.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Pirat::checkPiratSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
		event.register(RatlantisEntityRegistry.GHOST_PIRAT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GhostPirat::checkGhostPiratSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
		event.register(RatlantisEntityRegistry.RATFISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractFish::checkSurfaceWaterAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
		event.register(RatlantisEntityRegistry.RATLANTEAN_RATBOT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, RatlanteanRatbot::checkBotSpawnRule, SpawnPlacementRegisterEvent.Operation.REPLACE);
	}
}
