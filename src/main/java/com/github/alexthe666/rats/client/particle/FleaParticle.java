package com.github.alexthe666.rats.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class FleaParticle extends TextureSheetParticle {

	public FleaParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed);
		this.xd *= 0.8D;
		this.yd *= 0.8D;
		this.zd *= 0.8D;
		this.yd = Math.random() * 0.1F + 0.1F;
		this.rCol = 1.0F;
		this.gCol = 1.0F;
		this.bCol = 1.0F;
		this.quadSize *= Math.random() * 0.5F + 0.2F;
		this.lifetime = (int) (16.0D / (Math.random() * 0.8D + 0.2D));
	}

	@Override
	public void tick() {
		super.tick();

		this.yd -= 0.02D;
		this.move(this.xd, this.yd, this.zd);
		this.xd *= 0.99D;
		this.yd *= 0.99D;
		this.zd *= 0.99D;

		if (this.onGround) {
			this.xd *= 0.7D;
			this.zd *= 0.7D;
		}
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	public record Provider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			FleaParticle particle = new FleaParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
			particle.pickSprite(this.sprite);
			return particle;
		}
	}
}