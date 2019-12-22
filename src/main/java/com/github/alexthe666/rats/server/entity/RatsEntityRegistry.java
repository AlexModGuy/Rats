package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;

public class RatsEntityRegistry {

    public static EntityType<EntityRat> RAT = registerEntity(EntityType.Builder.create(EntityRat::new, EntityClassification.CREATURE).size(0.49F, 0.49F), "rat");
    public static EntityType<EntityIllagerPiper> PIED_PIPER = registerEntity(EntityType.Builder.create(EntityIllagerPiper::new, EntityClassification.MONSTER).size(0.7F, 1.8F), "pied_piper");
    public static EntityType<EntityRatlanteanSpirit> RATLANTEAN_SPIRIT = registerEntity(EntityType.Builder.create(EntityRatlanteanSpirit::new, EntityClassification.MONSTER).size(0.5F, 0.5F), "ratlantean_spirit");
    public static EntityType<EntityRatlanteanFlame> RATLANTEAN_FLAME = registerEntity(EntityType.Builder.create(EntityRatlanteanFlame::new, EntityClassification.MISC).size(0.5F, 0.5F), "ratlantean_spirit_flame");
    public static EntityType<EntityMarbleCheeseGolem> RATLANTEAN_AUTOMATON = registerEntity(EntityType.Builder.create(EntityMarbleCheeseGolem::new, EntityClassification.MONSTER).size(2F, 3.5F), "ratlantean_automaton");
    public static EntityType<EntityGolemBeam> RATLANTEAN_AUTOMATON_BEAM = registerEntity(EntityType.Builder.create(EntityGolemBeam::new, EntityClassification.MISC).size(0.5F, 0.5F), "ratlantean_automaton_beam");
    public static EntityType<EntityFeralRatlantean> FERAL_RATLANTEAN = registerEntity(EntityType.Builder.create(EntityFeralRatlantean::new, EntityClassification.MONSTER).size(1.85F, 1.2F), "feral_ratlantean");
    public static EntityType<EntityNeoRatlantean> NEO_RATLANTEAN = registerEntity(EntityType.Builder.create(EntityNeoRatlantean::new, EntityClassification.MONSTER).size(0.8F, 1.3F), "neo_ratlantean");
    public static EntityType<EntityLaserBeam> LASER_BEAM = registerEntity(EntityType.Builder.create(EntityLaserBeam::new, EntityClassification.MISC).size(0.5F, 0.5F), "laser_beam");
    public static EntityType<EntityLaserPortal> LASER_PORTAL = registerEntity(EntityType.Builder.create(EntityLaserPortal::new, EntityClassification.MISC).size(0.95F, 0.95F), "laser_portal");
    public static EntityType<EntityThrownBlock> THROWN_BLOCK = registerEntity(EntityType.Builder.create(EntityThrownBlock::new, EntityClassification.MISC).size(0.95F, 0.95F), "thrown_block");
    public static EntityType<EntityVialOfSentience> VIAL_OF_SENTIENCE = registerEntity(EntityType.Builder.create(EntityVialOfSentience::new, EntityClassification.MISC).size(0.25F, 0.25F), "vial_of_sentience");
    public static EntityType<EntityPiratBoat> PIRAT_BOAT = registerEntity(EntityType.Builder.create(EntityPiratBoat::new, EntityClassification.MISC).size(1.75F, 0.8F), "pirat_boat");
    public static EntityType<EntityPirat> PIRAT = registerEntity(EntityType.Builder.create(EntityPirat::new, EntityClassification.MONSTER).size(0.49F, 0.49F), "pirat");
    public static EntityType<EntityCheeseCannonball> CHEESE_CANNONBALL = registerEntity(EntityType.Builder.create(EntityCheeseCannonball::new, EntityClassification.MISC).size(0.5F, 0.5F), "cheese_cannonball");
    public static EntityType<EntityPlagueDoctor> PLAGUE_DOCTOR = registerEntity(EntityType.Builder.create(EntityPlagueDoctor::new, EntityClassification.CREATURE).size(0.8F, 1.8F), "plague_doctor");
    public static EntityType<EntityPurifyingLiquid> PURIFYING_LIQUID = registerEntity(EntityType.Builder.create(EntityPurifyingLiquid::new, EntityClassification.MISC).size(0.5F, 0.5F), "purifying_liquid");
    public static EntityType<EntityBlackDeath> BLACK_DEATH = registerEntity(EntityType.Builder.create(EntityBlackDeath::new, EntityClassification.MONSTER).size(0.8F, 1.8F), "black_death");
    public static EntityType<EntityPlagueCloud> PLAGUE_CLOUD = registerEntity(EntityType.Builder.create(EntityPlagueCloud::new, EntityClassification.MONSTER).size(0.8F, 0.8F), "plague_cloud");
    public static EntityType<EntityPlagueBeast> PLAGUE_BEAST = registerEntity(EntityType.Builder.create(EntityPlagueBeast::new, EntityClassification.MONSTER).size(1.85F, 1.2F), "plague_beast");
    public static EntityType<EntityPlagueShot> PLAGUE_SHOT = registerEntity(EntityType.Builder.create(EntityPlagueShot::new, EntityClassification.MISC).size(0.5F, 0.5F), "plague_shot");
    public static EntityType<EntityRatCaptureNet> RAT_CAPTURE_NET = registerEntity(EntityType.Builder.create(EntityRatCaptureNet::new, EntityClassification.MISC).size(0.5F, 0.5F), "rat_capture_net");
    public static EntityType<EntityRatDragonFire> RAT_DRAGON_FIRE = registerEntity(EntityType.Builder.create(EntityRatDragonFire::new, EntityClassification.MISC).size(0.5F, 0.5F), "rat_dragon_fire");
    public static EntityType<EntityRatArrow> RAT_ARROW = registerEntity(EntityType.Builder.create(EntityRatArrow::new, EntityClassification.MISC).size(0.5F, 0.5F), "rat_arrow");

    public static EntityType registerEntity(EntityType.Builder builder, String entityName){
        ResourceLocation nameLoc = new ResourceLocation(RatsMod.MODID, entityName);
        return (EntityType) builder.build(entityName).setRegistryName(nameLoc);
    }
}
