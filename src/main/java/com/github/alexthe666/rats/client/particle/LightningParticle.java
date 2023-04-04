package com.github.alexthe666.rats.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class LightningParticle extends TextureSheetParticle {

	public LightningParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed);
		this.alpha = 1.0F;
		this.lifetime = (int) (6.0D / (Math.random() * 0.8D + 0.2D));
		this.xd *= 0.125D;
		this.yd *= 0.125D;
		this.zd *= 0.125D;
	}

	@Override
	public void tick() {
		super.tick();
		this.yd *= 1.015D;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	public record Provider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			LightningParticle particle = new LightningParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
			particle.pickSprite(this.sprite);
			return particle;
		}
	}
}