package com.github.alexthe666.rats.client.particle;

import com.github.alexthe666.rats.client.model.entity.StaticRatModel;
import com.github.alexthe666.rats.server.misc.RatVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class RunningRatParticle extends Particle {

	private final StaticRatModel<?> model = new StaticRatModel<>(0.0F, false);
	private final RenderType renderType = RenderType.entityCutoutNoCull(RatVariant.getRandomVariant(RandomSource.create(), false).getTexture());
	private final Vec3 headingTo;
	private int oldAge;

	public RunningRatParticle(ClientLevel level, double x, double y, double z, double headingToX, double headingToY, double headingToZ) {
		super(level, x, y, z, 0.0D, 0.0D, 0.0D);
		this.headingTo = new Vec3(headingToX, headingToY, headingToZ);
		this.xo = x;
		this.yo = y;
		this.zo = z;
		this.gravity = 0.0F;
		this.lifetime = 600;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.lifetime-- <= 0) {
			this.remove();
		}
		this.age++;

		if (!this.onGround) {
			this.move(0.0D, -0.1D, 0.0D);
		} else {
			if (this.stoppedByCollision) {
				this.move(0.0D, 1.0D, 0.0D);
				this.stoppedByCollision = false;
			}
		}

		if (new AABB(BlockPos.containing(this.x, this.y, this.z)).inflate(0.5D).contains(this.headingTo) || this.headingTo.equals(Vec3.ZERO)) {
			this.remove();
		} else {
			Vec3 destVec = new Vec3(this.headingTo.x() - this.x, this.headingTo.y() - this.y, this.headingTo.z() - this.z).normalize().scale(0.3F);
			this.move(destVec.x(), 0.0F, destVec.z());
		}
		this.oldAge = this.age;
	}

	@Override
	public void move(double p_107246_, double p_107247_, double p_107248_) {
		if (!this.stoppedByCollision) {
			double d0 = p_107246_;
			double d1 = p_107247_;
			double d2 = p_107248_;
			if (this.hasPhysics && (p_107246_ != 0.0D || p_107247_ != 0.0D || p_107248_ != 0.0D) && p_107246_ * p_107246_ + p_107247_ * p_107247_ + p_107248_ * p_107248_ < Mth.square(100.0D)) {
				Vec3 vec3 = Entity.collideBoundingBox(null, new Vec3(p_107246_, p_107247_, p_107248_), this.getBoundingBox(), this.level, List.of());
				p_107246_ = vec3.x;
				p_107247_ = vec3.y;
				p_107248_ = vec3.z;
			}

			if (p_107246_ != 0.0D || p_107247_ != 0.0D || p_107248_ != 0.0D) {
				this.setBoundingBox(this.getBoundingBox().move(p_107246_, p_107247_, p_107248_));
				this.setLocationFromBoundingbox();
			}

			if ((Math.abs(p_107246_) < (double) 1.0E-5F && Math.abs(d0) < (double) 1.0E-5F) || (Math.abs(p_107248_) < (double) 1.0E-5F && Math.abs(d2) < (double) 1.0E-5F)) {
				this.stoppedByCollision = true;
			}

			this.onGround = d1 != p_107247_ && d1 < 0.0D;
			if (d0 != p_107246_) {
				this.xd = 0.0D;
			}

			if (d2 != p_107248_) {
				this.zd = 0.0D;
			}
		}
	}

	@Override
	public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
		Vec3 vec = camera.getPosition();
		float f = (float) (Mth.lerp(partialTicks, this.xo, this.x) - vec.x());
		float f1 = (float) (Mth.lerp(partialTicks, this.yo, this.y) - vec.y());
		float f2 = (float) (Mth.lerp(partialTicks, this.zo, this.z) - vec.z());

		PoseStack posestack = new PoseStack();
		posestack.pushPose();
		posestack.translate(f, f1, f2);
		posestack.translate(0.0D, 0.95D, 0.0D);
		posestack.scale(0.6F, -0.6F, -0.6F);
		this.getYRotD().ifPresent(aFloat -> posestack.mulPose(Axis.YP.rotationDegrees(aFloat)));
		MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();
		VertexConsumer vertexconsumer = source.getBuffer(this.renderType);
		this.model.setupAnim(null, Mth.lerp(partialTicks, this.oldAge, this.age) * 0.35F, 1, Mth.lerp(partialTicks, this.oldAge, this.age), partialTicks, 0);
		this.model.renderToBuffer(posestack, vertexconsumer, this.getLightColor(partialTicks), OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		source.endBatch();
		posestack.popPose();
	}

	protected Optional<Float> getYRotD() {
		double d0 = this.headingTo.x() - this.x;
		double d1 = this.headingTo.z() - this.z;
		return !(Math.abs(d1) > (double) 1.0E-5F) && !(Math.abs(d0) > (double) 1.0E-5F) ? Optional.empty() : Optional.of((float) (Mth.atan2(d1, d0) * (double) (180F / (float) Math.PI)) - 90.0F);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.CUSTOM;
	}

	public static class Provider implements ParticleProvider<SimpleParticleType> {
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new RunningRatParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
		}
	}
}
