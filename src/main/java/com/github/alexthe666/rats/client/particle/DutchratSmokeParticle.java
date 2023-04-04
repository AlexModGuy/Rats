package com.github.alexthe666.rats.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class DutchratSmokeParticle extends TextureSheetParticle {

	protected final SpriteSet sprites;

	public DutchratSmokeParticle(ClientLevel level, double x, double y, double z, double life, SpriteSet set) {
		super(level, x, y, z, 0.0D, 0.0D, 0.0D);
		this.sprites = set;
		this.lifetime = (int) life;
		this.setColor(0.0F, 0.75F, 0.0F);
		this.scale(4.5F);
		this.setSpriteFromAge(set);
	}

	@Override
	protected int getLightColor(float color) {
		return 240;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime) {
			this.remove();
		}

		if (this.age <= 5 || (this.lifetime % 12 == 0 && this.age >= this.lifetime - 7)) {
			this.setSprite(this.sprites.get(this.age % 12, 12));
		}
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	public record Provider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new DutchratSmokeParticle(level, x, y, z, xSpeed, this.sprite());
		}
	}
}
