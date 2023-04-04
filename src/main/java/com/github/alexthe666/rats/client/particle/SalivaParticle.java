package com.github.alexthe666.rats.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class SalivaParticle extends TextureSheetParticle {
	private final Fluid fluid;

	public SalivaParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Fluid fluid) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed);
		this.setSize(0.01F, 0.01F);
		this.gravity = 0.06F;
		this.fluid = fluid;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (!this.removed) {
			this.yd -= this.gravity;
			this.move(this.xd, this.yd, this.zd);
			if (!this.removed) {
				this.xd *= 0.98F;
				this.yd *= 0.98F;
				this.zd *= 0.98F;
				BlockPos blockpos = BlockPos.containing(this.x, this.y, this.z);
				FluidState state = this.level.getFluidState(blockpos);
				if (state.is(this.fluid) && this.y < (double) ((float) blockpos.getY() + state.getHeight(this.level, blockpos))) {
					this.remove();
				}
			}
		}
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	public record Provider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			SalivaParticle particle = new SalivaParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, Fluids.WATER);
			particle.pickSprite(this.sprite);
			return particle;
		}
	}
}