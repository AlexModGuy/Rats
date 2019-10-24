package com.github.alexthe666.rats.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleSpell;
import net.minecraft.client.particle.ParticleWaterWake;
import net.minecraft.world.World;

public class ParticleBlackDeath extends ParticleSpell {

    public ParticleBlackDeath(World world, double x, double y, double z, float motX, float motY, float motZ) {
        super(world, x, y, z, motX, motY, motZ);
        this.setBaseSpellTextureIndex(144);
        float f = world.rand.nextFloat() * 0.5F + 0.35F;
        this.setRBGColorF(0.0F, 0.01F * f, 0.0F);
    }
}
