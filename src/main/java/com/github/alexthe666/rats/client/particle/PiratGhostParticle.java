package com.github.alexthe666.rats.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class PiratGhostParticle extends TextureSheetParticle {

	public PiratGhostParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed);
		this.alpha = 1.0F;
		this.lifetime = (int) (16.0D / (Math.random() * 0.8D + 0.2D));
	}


	@Override
	public int getLightColor(float f) {
		int i = super.getLightColor(f);
		int k = i >> 16 & 255;
		return 240 | k << 16;
	}

	@Override
	public void tick() {
		super.tick();
		this.yd *= 0.0D;

	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	public record Provider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			PiratGhostParticle particle = new PiratGhostParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
			particle.pickSprite(this.sprite);
			return particle;
		}
	}
}