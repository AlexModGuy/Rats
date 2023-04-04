package com.github.alexthe666.rats.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class FlyParticle extends SimpleAnimatedParticle {

	private final double spawnX;
	private final double spawnZ;

	protected FlyParticle(ClientLevel level, double x, double y, double z, SpriteSet set) {
		super(level, x, y, z, set, 0.0F);
		this.spawnX = x;
		this.spawnZ = z;
		this.lifetime = (int) (100 + (Math.random() * 60.0D));
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;

		if (this.age++ >= this.lifetime) {
			this.remove();
		} else {
			if (this.age > this.lifetime - 10) {
				this.scale(Mth.abs(this.age - this.lifetime) * 0.1F);
			}

			this.move(this.xd, this.yd, this.zd);
			this.xd *= 0.99F;
			this.zd *= 0.99F;

			float radius = 0.65F;
			float angle = (0.01745329251F * (this.age));
			float speed = 0.05F;
			double extraX = (double) (radius * Mth.sin((float) (Math.PI + angle))) + this.spawnX - this.x;
			double extraZ = (double) (radius * Mth.cos(angle)) + this.spawnZ - this.z;
			this.xd += extraX * speed;
			this.zd += extraZ * speed;
			this.yd = 2 * (this.random.nextFloat() - 0.5F) * speed;

			if (!this.onGround) {
				this.setSprite(this.sprites.get(this.age % 2 + 1, 2));
			} else {
				this.setSprite(this.sprites.get(1, 2));
			}
		}
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	public record Provider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			FlyParticle particle = new FlyParticle(level, x, y, z, this.sprite());
			particle.setSpriteFromAge(this.sprite());
			return particle;
		}
	}
}
