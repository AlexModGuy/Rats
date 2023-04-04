package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RatsParticleRegistry {
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, RatsMod.MODID);

	public static final RegistryObject<SimpleParticleType> BLACK_DEATH = PARTICLES.register("black_death", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> DUTCHRAT_SMOKE = PARTICLES.register("dutchrat_smoke", () -> new SimpleParticleType(true));
	public static final RegistryObject<SimpleParticleType> FLEA = PARTICLES.register("flea", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> FLY = PARTICLES.register("fly", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> LIGHTNING = PARTICLES.register("rat_lightning", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> MILK_BUBBLE = PARTICLES.register("milk_bubble", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> PIRAT_GHOST = PARTICLES.register("pirat_ghost", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> RAT_GHOST = PARTICLES.register("rat_ghost", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> RAT_KING_SMOKE = PARTICLES.register("rat_king_smoke", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> RUNNING_RAT = PARTICLES.register("running_rat", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> SALIVA = PARTICLES.register("saliva", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> UPGRADE_COMBINER = PARTICLES.register("upgrade_combiner", () -> new SimpleParticleType(false));
}
