package com.github.alexthe666.rats.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import com.github.alexthe666.rats.RatsMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class RatsParticleRegistry {
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, RatsMod.MODID);

	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLACK_DEATH = PARTICLES.register("black_death", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DUTCHRAT_SMOKE = PARTICLES.register("dutchrat_smoke", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLEA = PARTICLES.register("flea", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLY = PARTICLES.register("fly", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LIGHTNING = PARTICLES.register("rat_lightning", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MILK_BUBBLE = PARTICLES.register("milk_bubble", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PIRAT_GHOST = PARTICLES.register("pirat_ghost", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RAT_GHOST = PARTICLES.register("rat_ghost", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RAT_KING_SMOKE = PARTICLES.register("rat_king_smoke", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RUNNING_RAT = PARTICLES.register("running_rat", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SALIVA = PARTICLES.register("saliva", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> UPGRADE_COMBINER = PARTICLES.register("upgrade_combiner", () -> new SimpleParticleType(false));
}
