package com.github.alexthe666.rats.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SmokeParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class RatKingSmokeParticle extends SmokeParticle {
	protected RatKingSmokeParticle(ClientLevel p_107685_, double p_107686_, double p_107687_, double p_107688_, double p_107689_, double p_107690_, double p_107691_, float p_107692_, SpriteSet p_107693_) {
		super(p_107685_, p_107686_, p_107687_, p_107688_, p_107689_, p_107690_, p_107691_, p_107692_, p_107693_);
	}

	public float getQuadSize(float p_105642_) {
		return this.quadSize * Mth.clamp(((float)this.age + p_105642_) / (float)this.lifetime * 32.0F, 0.0F, 1.5F);
	}

	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public Provider(SpriteSet p_107696_) {
			this.sprites = p_107696_;
		}

		public Particle createParticle(SimpleParticleType p_107707_, ClientLevel p_107708_, double p_107709_, double p_107710_, double p_107711_, double p_107712_, double p_107713_, double p_107714_) {
			return new RatKingSmokeParticle(p_107708_, p_107709_, p_107710_, p_107711_, p_107712_, p_107713_, p_107714_, 1.0F, this.sprites);
		}
	}
}
