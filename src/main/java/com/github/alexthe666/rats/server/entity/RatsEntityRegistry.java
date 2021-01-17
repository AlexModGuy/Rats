package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.ratlantis.*;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;

public class RatsEntityRegistry {

    public static final EntityType<EntityRat> RAT = registerEntity(EntityType.Builder.create(EntityRat::new, EntityClassification.CREATURE).size(0.49F, 0.49F), "rat");
    public static final EntityType<EntityRatSpawner> RAT_SPAWNER = registerEntity(EntityType.Builder.create(EntityRatSpawner::new, EntityClassification.MONSTER).size(0.7F, 1.8F), "rat_spawner");
    public static final EntityType<EntityIllagerPiper> PIED_PIPER = registerEntity(EntityType.Builder.create(EntityIllagerPiper::new, EntityClassification.MONSTER).size(0.7F, 1.8F), "pied_piper");
    public static final EntityType<EntityThrownBlock> THROWN_BLOCK = registerEntity(EntityType.Builder.create(EntityThrownBlock::new, EntityClassification.MISC).size(0.95F, 0.95F).setCustomClientFactory(EntityThrownBlock::new), "thrown_block");
    public static final EntityType<EntityPlagueDoctor> PLAGUE_DOCTOR = registerEntity(EntityType.Builder.create(EntityPlagueDoctor::new, EntityClassification.CREATURE).size(0.8F, 1.8F), "plague_doctor");
    public static final EntityType<EntityPurifyingLiquid> PURIFYING_LIQUID = registerEntity(EntityType.Builder.create(EntityPurifyingLiquid::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntityPurifyingLiquid::new), "purifying_liquid");
    public static final EntityType<EntityBlackDeath> BLACK_DEATH = registerEntity(EntityType.Builder.create(EntityBlackDeath::new, EntityClassification.MONSTER).size(0.8F, 1.8F), "black_death");
    public static final EntityType<EntityPlagueCloud> PLAGUE_CLOUD = registerEntity(EntityType.Builder.create(EntityPlagueCloud::new, EntityClassification.MONSTER).size(1.2F, 1.2F), "plague_cloud");
    public static final EntityType<EntityPlagueBeast> PLAGUE_BEAST = registerEntity(EntityType.Builder.create(EntityPlagueBeast::new, EntityClassification.MONSTER).size(1.85F, 1.2F), "plague_beast");
    public static final EntityType<EntityPlagueShot> PLAGUE_SHOT = registerEntity(EntityType.Builder.create(EntityPlagueShot::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntityPlagueShot::new), "plague_shot");
    public static final EntityType<EntityRatCaptureNet> RAT_CAPTURE_NET = registerEntity(EntityType.Builder.create(EntityRatCaptureNet::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntityRatCaptureNet::new), "rat_capture_net");
    public static final EntityType<EntityRatDragonFire> RAT_DRAGON_FIRE = registerEntity(EntityType.Builder.create(EntityRatDragonFire::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntityRatDragonFire::new), "rat_dragon_fire");
    public static final EntityType<EntityRatArrow> RAT_ARROW = registerEntity(EntityType.Builder.create(EntityRatArrow::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntityRatArrow::new), "rat_arrow");
    public static final EntityType<EntityRatGolemMount> RAT_MOUNT_GOLEM = registerEntity(EntityType.Builder.create(EntityRatGolemMount::new, EntityClassification.MISC).size(1.25F, 2.75F), "rat_mount_golem");
    public static final EntityType<EntityRatChickenMount> RAT_MOUNT_CHICKEN = registerEntity(EntityType.Builder.create(EntityRatChickenMount::new, EntityClassification.MISC).size(0.65F, 0.75F), "rat_mount_chicken");
    public static final EntityType<EntityRatBeastMount> RAT_MOUNT_BEAST = registerEntity(EntityType.Builder.create(EntityRatBeastMount::new, EntityClassification.MISC).size(1.85F, 1.2F), "rat_mount_beast");
    public static final EntityType<EntityRatKing> RAT_KING = registerEntity(EntityType.Builder.create(EntityRatKing::new, EntityClassification.MONSTER).size(2F, 0.5F), "rat_king");
    public static final EntityType<EntityRatShot> RAT_SHOT = registerEntity(EntityType.Builder.create(EntityRatShot::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntityRatShot::new), "rat_shot");
    public static final EntityType<EntityDemonRat> DEMON_RAT = registerEntity(EntityType.Builder.create(EntityDemonRat::new, EntityClassification.MONSTER).size(1.0F, 0.75F).immuneToFire().func_225435_d().setUpdateInterval(8), "demon_rat");
    public static final EntityType<EntityRatStriderMount> RAT_STRIDER_MOUNT = registerEntity(EntityType.Builder.create(EntityRatStriderMount::new, EntityClassification.MISC).immuneToFire().size(0.9F, 1.7F).setUpdateInterval(10), "rat_mount_strider");

    static {


        EntitySpawnPlacementRegistry.register(RAT, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityRatSpawner::func_223325_c);
        EntitySpawnPlacementRegistry.register(RAT_SPAWNER, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityRat::canEntityTypeSpawn);
        EntitySpawnPlacementRegistry.register(PIED_PIPER, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityIllagerPiper::canSpawnOn);
        EntitySpawnPlacementRegistry.register(DEMON_RAT, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityDemonRat::canDemonRatSpawnOn);

    }

    private static final EntityType registerEntity(EntityType.Builder builder, String entityName) {
        ResourceLocation nameLoc = new ResourceLocation(RatsMod.MODID, entityName);
        return (EntityType) builder.build(entityName).setRegistryName(nameLoc);
    }

    public static void initializeAttributes() {

        GlobalEntityTypeAttributes.put(RAT, EntityRat.func_234290_eH_().create());
        GlobalEntityTypeAttributes.put(RAT_SPAWNER, EntityRat.func_234290_eH_().create());
        GlobalEntityTypeAttributes.put(PIED_PIPER, EntityIllagerPiper.func_234290_eH_().create());
        GlobalEntityTypeAttributes.put(PLAGUE_DOCTOR, EntityPlagueDoctor.func_234290_eH_().create());
        GlobalEntityTypeAttributes.put(BLACK_DEATH, EntityBlackDeath.func_234290_eH_().create());
        GlobalEntityTypeAttributes.put(PLAGUE_CLOUD, EntityPlagueCloud.func_234290_eH_().create());
        GlobalEntityTypeAttributes.put(PLAGUE_BEAST, EntityPlagueBeast.func_234290_eH_().create());
        GlobalEntityTypeAttributes.put(RAT_MOUNT_CHICKEN, EntityRatChickenMount.func_234290_eH_().create());
        GlobalEntityTypeAttributes.put(RAT_MOUNT_BEAST, EntityRatBeastMount.func_234290_eH_().create());
        GlobalEntityTypeAttributes.put(RAT_KING, EntityRatKing.func_234290_eH_().create());
        GlobalEntityTypeAttributes.put(DEMON_RAT, EntityDemonRat.func_234290_eH_().create());
        GlobalEntityTypeAttributes.put(RAT_STRIDER_MOUNT, EntityRatStriderMount.buildAttributes().create());
        GlobalEntityTypeAttributes.put(RAT_MOUNT_GOLEM, EntityRatGolemMount.buildAttributes().create());
    }
}
