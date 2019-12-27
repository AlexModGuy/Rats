package com.github.alexthe666.rats.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ParticleBlackDeath {

    @OnlyIn(Dist.CLIENT)
    public static class DeathFactory<T extends BasicParticleType>  implements IParticleFactory<T> {
        private final IAnimatedSprite spriteSet;

        public DeathFactory(IAnimatedSprite p_i50842_1_) {
            this.spriteSet = p_i50842_1_;
        }

        public Particle makeParticle(BasicParticleType typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Particle spellparticle = new SpellParticle.Factory(this.spriteSet).makeParticle(typeIn, worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            float f = worldIn.rand.nextFloat() * 0.5F + 0.35F;
            spellparticle.setColor(0.0F, 0.01F * f, 0.0F);
            return spellparticle;
        }
    }
}
