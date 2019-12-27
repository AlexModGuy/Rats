package com.github.alexthe666.rats.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.BasicParticleType;

public class RatsParticleRegistry {

    public static BasicParticleType PARTICLE_BLACK_DEATH = register(new BasicParticleType(false), "black_death");
    public static BasicParticleType PARTICLE_FLEA = register(new BasicParticleType(false), "flea");
    public static BasicParticleType PARTICLE_LIGHTNING = register(new BasicParticleType(false), "rat_lightning");
    public static BasicParticleType PARTICLE_RAT_GHOST = register(new BasicParticleType(false), "rat_ghost");
    public static BasicParticleType PARTICLE_SALIVA = register(new BasicParticleType(false), "rat_saliva");
    public static BasicParticleType PARTICLE_UPGRADE_COMBINER = register(new BasicParticleType(false), "upgrade_combiner");

    public static BasicParticleType register(BasicParticleType type, String name) {
        type.setRegistryName(name);
        return type;
    }

    public static void registerFactories(){
        ParticleManager manager = Minecraft.getInstance().particles;
    }
}
