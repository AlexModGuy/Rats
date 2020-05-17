package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;

public class RatsEntityRegistry {

    public static final EntityType<EntityRat> RAT = registerEntity(EntityType.Builder.create(EntityRat::new, EntityClassification.CREATURE).size(0.49F, 0.49F), "rat");
    public static final EntityType<EntityRatSpawner> RAT_SPAWNER = registerEntity(EntityType.Builder.create(EntityRatSpawner::new, EntityClassification.MONSTER).size(0.7F, 1.8F), "rat_spawner");
    public static final EntityType<EntityIllagerPiper> PIED_PIPER = registerEntity(EntityType.Builder.create(EntityIllagerPiper::new, EntityClassification.MONSTER).size(0.7F, 1.8F), "pied_piper");
    public static final EntityType<EntityRatlanteanSpirit> RATLANTEAN_SPIRIT = registerEntity(EntityType.Builder.create(EntityRatlanteanSpirit::new, EntityClassification.MONSTER).size(0.5F, 0.5F), "ratlantean_spirit");
    public static final EntityType<EntityRatlanteanFlame> RATLANTEAN_FLAME = registerEntity(EntityType.Builder.create(EntityRatlanteanFlame::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntityRatlanteanFlame::new), "ratlantean_spirit_flame");
    public static final EntityType<EntityMarbleCheeseGolem> RATLANTEAN_AUTOMATON = registerEntity(EntityType.Builder.create(EntityMarbleCheeseGolem::new, EntityClassification.MONSTER).size(2F, 3.5F), "ratlantean_automaton");
    public static final EntityType<EntityGolemBeam> RATLANTEAN_AUTOMATON_BEAM = registerEntity(EntityType.Builder.create(EntityGolemBeam::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntityGolemBeam::new), "ratlantean_automaton_beam");
    public static final EntityType<EntityFeralRatlantean> FERAL_RATLANTEAN = registerEntity(EntityType.Builder.create(EntityFeralRatlantean::new, EntityClassification.MONSTER).size(1.85F, 1.2F), "feral_ratlantean");
    public static final EntityType<EntityNeoRatlantean> NEO_RATLANTEAN = registerEntity(EntityType.Builder.create(EntityNeoRatlantean::new, EntityClassification.MONSTER).size(0.8F, 1.3F), "neo_ratlantean");
    public static final EntityType<EntityLaserBeam> LASER_BEAM = registerEntity(EntityType.Builder.create(EntityLaserBeam::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntityLaserBeam::new), "laser_beam");
    public static final EntityType<EntityLaserPortal> LASER_PORTAL = registerEntity(EntityType.Builder.create(EntityLaserPortal::new, EntityClassification.MISC).size(0.95F, 0.95F).setCustomClientFactory(EntityLaserPortal::new), "laser_portal");
    public static final EntityType<EntityThrownBlock> THROWN_BLOCK = registerEntity(EntityType.Builder.create(EntityThrownBlock::new, EntityClassification.CREATURE).size(0.95F, 0.95F).setCustomClientFactory(EntityThrownBlock::new), "thrown_block");
    public static final EntityType<EntityVialOfSentience> VIAL_OF_SENTIENCE = registerEntity(EntityType.Builder.create(EntityVialOfSentience::new, EntityClassification.MISC).size(0.25F, 0.25F).setCustomClientFactory(EntityVialOfSentience::new), "vial_of_sentience");
    public static final EntityType<EntityPiratBoat> PIRAT_BOAT = registerEntity(EntityType.Builder.create(EntityPiratBoat::new, EntityClassification.MISC).size(1.75F, 0.8F), "pirat_boat");
    public static final EntityType<EntityPirat> PIRAT = registerEntity(EntityType.Builder.create(EntityPirat::new, EntityClassification.MONSTER).size(0.49F, 0.49F), "pirat");
    public static final EntityType<EntityCheeseCannonball> CHEESE_CANNONBALL = registerEntity(EntityType.Builder.create(EntityCheeseCannonball::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntityCheeseCannonball::new), "cheese_cannonball");
    public static final EntityType<EntityPlagueDoctor> PLAGUE_DOCTOR = registerEntity(EntityType.Builder.create(EntityPlagueDoctor::new, EntityClassification.CREATURE).size(0.8F, 1.8F), "plague_doctor");
    public static final EntityType<EntityPurifyingLiquid> PURIFYING_LIQUID = registerEntity(EntityType.Builder.create(EntityPurifyingLiquid::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntityPurifyingLiquid::new), "purifying_liquid");
    public static final EntityType<EntityBlackDeath> BLACK_DEATH = registerEntity(EntityType.Builder.create(EntityBlackDeath::new, EntityClassification.MONSTER).size(0.8F, 1.8F), "black_death");
    public static final EntityType<EntityPlagueCloud> PLAGUE_CLOUD = registerEntity(EntityType.Builder.create(EntityPlagueCloud::new, EntityClassification.MONSTER).size(0.8F, 0.8F), "plague_cloud");
    public static final EntityType<EntityPlagueBeast> PLAGUE_BEAST = registerEntity(EntityType.Builder.create(EntityPlagueBeast::new, EntityClassification.MONSTER).size(1.85F, 1.2F), "plague_beast");
    public static final EntityType<EntityPlagueShot> PLAGUE_SHOT = registerEntity(EntityType.Builder.create(EntityPlagueShot::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntityPlagueShot::new), "plague_shot");
    public static final EntityType<EntityRatCaptureNet> RAT_CAPTURE_NET = registerEntity(EntityType.Builder.create(EntityRatCaptureNet::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntityRatCaptureNet::new), "rat_capture_net");
    public static final EntityType<EntityRatDragonFire> RAT_DRAGON_FIRE = registerEntity(EntityType.Builder.create(EntityRatDragonFire::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntityRatDragonFire::new), "rat_dragon_fire");
    public static final EntityType<EntityRatArrow> RAT_ARROW = registerEntity(EntityType.Builder.create(EntityRatArrow::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntityRatArrow::new), "rat_arrow");
    public static final EntityType<EntityGhostPirat> GHOST_PIRAT = registerEntity(EntityType.Builder.create(EntityGhostPirat::new, EntityClassification.MONSTER).size(1.0F, 0.75F), "ghost_pirat");
    public static final EntityType<EntityDutchrat> DUTCHRAT = registerEntity(EntityType.Builder.create(EntityDutchrat::new, EntityClassification.MONSTER).size(2.0F, 2.75F), "dutchrat");
    public static final EntityType<EntityDutchratSword> DUTCHRAT_SWORD = registerEntity(EntityType.Builder.create(EntityDutchratSword::new, EntityClassification.MISC).size(0.95F, 0.95F).setCustomClientFactory(EntityDutchratSword::new), "dutchrat_sword");
    public static final EntityType<EntityRatfish> RATFISH = registerEntity(EntityType.Builder.create(EntityRatfish::new, EntityClassification.WATER_CREATURE).size(0.45F, 0.45F), "ratfish");
    public static final EntityType<EntityRattlingGun> RATTLING_GUN = registerEntity(EntityType.Builder.create(EntityRattlingGun::new, EntityClassification.CREATURE).size(1.5F, 1.6F).setCustomClientFactory(EntityRattlingGun::new), "rattling_gun");
    public static final EntityType<EntityRattlingGunBullet> RATTLING_GUN_BULLET = registerEntity(EntityType.Builder.create(EntityRattlingGunBullet::new, EntityClassification.MISC).size(0.25F, 0.25F), "rattling_gun_bullet");
    public static final EntityType<EntityRatlanteanRatbot> RATLANTEAN_RATBOT = registerEntity(EntityType.Builder.create(EntityRatlanteanRatbot::new, EntityClassification.MONSTER).size(1.2F, 1.3F), "ratlantean_ratbot");
    public static final EntityType<EntityRatGolemMount> RAT_MOUNT_GOLEM = registerEntity(EntityType.Builder.create(EntityRatGolemMount::new, EntityClassification.MISC).size(1.25F, 2.75F), "rat_mount_golem");
    public static final EntityType<EntityRatChickenMount> RAT_MOUNT_CHICKEN = registerEntity(EntityType.Builder.create(EntityRatChickenMount::new, EntityClassification.MISC).size(0.65F, 0.75F), "rat_mount_chicken");
    public static final EntityType<EntityRatBeastMount> RAT_MOUNT_BEAST = registerEntity(EntityType.Builder.create(EntityRatBeastMount::new, EntityClassification.MISC).size(1.85F, 1.2F), "rat_mount_beast");
    public static final EntityType<EntityRatAutomatonMount> RAT_MOUNT_AUTOMATON = registerEntity(EntityType.Builder.create(EntityRatAutomatonMount::new, EntityClassification.MISC).size(2F, 3.5F), "rat_mount_automaton");
    public static final EntityType<EntityRatKing> RAT_KING = registerEntity(EntityType.Builder.create(EntityRatKing::new, EntityClassification.MONSTER).size(2F, 0.5F), "rat_king");
    public static final EntityType<EntityRatShot> RAT_SHOT = registerEntity(EntityType.Builder.create(EntityRatShot::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntityRatShot::new), "rat_shot");
    public static final EntityType<EntityRatBaron> RAT_BARON = registerEntity(EntityType.Builder.create(EntityRatBaron::new, EntityClassification.MONSTER).size(0.5F, 0.5F), "rat_baron");
    public static final EntityType<EntityRatBaronPlane> RAT_BARON_PLANE = registerEntity(EntityType.Builder.create(EntityRatBaronPlane::new, EntityClassification.MONSTER).size(3.5F, 3.0F), "rat_baron_plane");
    public static final EntityType<EntityRatBiplaneMount> RAT_MOUNT_BIPLANE = registerEntity(EntityType.Builder.create(EntityRatBiplaneMount::new, EntityClassification.MISC).size(3.5F, 2.3F), "rat_mount_biplane");


    private static final EntityType registerEntity(EntityType.Builder builder, String entityName){
        ResourceLocation nameLoc = new ResourceLocation(RatsMod.MODID, entityName);
        return (EntityType) builder.build(entityName).setRegistryName(nameLoc);
    }

    static{
        EntitySpawnPlacementRegistry.register(RAT, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityRatSpawner::func_223325_c);
        EntitySpawnPlacementRegistry.register(RAT_SPAWNER, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityRat::canEntityTypeSpawn);
        EntitySpawnPlacementRegistry.register(PIED_PIPER, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityIllagerPiper::canSpawnOn);
        EntitySpawnPlacementRegistry.register(FERAL_RATLANTEAN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityFeralRatlantean::canSpawn);
        EntitySpawnPlacementRegistry.register(RATLANTEAN_SPIRIT, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityRatlanteanSpirit::func_223315_a);
        EntitySpawnPlacementRegistry.register(PIRAT, EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityPirat::canSpawn);
        EntitySpawnPlacementRegistry.register(GHOST_PIRAT, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityGhostPirat::canSpawn);
        EntitySpawnPlacementRegistry.register(RATFISH, EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AbstractFishEntity::func_223363_b);
        EntitySpawnPlacementRegistry.register(RATLANTEAN_RATBOT, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityRatlanteanRatbot::canSpawn);

    }
}
