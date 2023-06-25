package com.github.alexthe666.rats.client.model.entity;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.rats.server.entity.monster.boss.RatlanteanAutomaton;
import com.github.alexthe666.rats.server.entity.mount.RatAutomatonMount;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;

public class RatlanteanAutomatonModel<T extends Mob & IAnimatedEntity> extends AdvancedEntityModel<T> {
	public final AdvancedModelBox body;
	public final AdvancedModelBox armLeft1;
	public final AdvancedModelBox armRight1;
	public final AdvancedModelBox thruster;
	public final AdvancedModelBox upperbody;
	public final AdvancedModelBox ear1;
	public final AdvancedModelBox ear2;
	public final AdvancedModelBox armLeft2;
	public final AdvancedModelBox drillArm1;
	public final AdvancedModelBox drillArm2;
	public final AdvancedModelBox drilArm3;
	public final AdvancedModelBox blade;
	public final AdvancedModelBox armRight2;
	public final AdvancedModelBox cannon;

	public final AdvancedModelBox headBase;
	public final AdvancedModelBox snout;
	public final AdvancedModelBox nose;

	public final AdvancedModelBox riderPlate;
	public final AdvancedModelBox riderPlatePivot;

	private final ModelAnimator animator;
	private final boolean mount;

	public RatlanteanAutomatonModel(boolean mount) {
		this.mount = mount;
		this.texWidth = 128;
		this.texHeight = 96;

		this.upperbody = new AdvancedModelBox(this, 0, 34);
		this.upperbody.setRotationPoint(0.0F, -5.0F, 0.0F);
		this.upperbody.addBox(-11.0F, -5.0F, -6.0F, 22, 10, 12, 0.0F);
		this.body = new AdvancedModelBox(this, 0, 0);
		this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.body.addBox(-6.0F, -3.5F, -5.0F, 12, 16, 10, 0.0F);
		this.ear2 = new AdvancedModelBox(this, 34, 0);
		this.ear2.mirror = true;
		this.ear2.setRotationPoint(-7.0F, -3.0F, 5.0F);
		this.ear2.addBox(-7.0F, -6.0F, 0.0F, 7, 7, 1, 0.0F);
		this.setRotateAngle(ear2, -0.2617993877991494F, 0.9075712110370513F, 0.0F);
		this.drilArm3 = new AdvancedModelBox(this, 0, 0);
		this.drilArm3.setRotationPoint(0.0F, 11.0F, 0.0F);
		this.drilArm3.addBox(-0.5F, -0.5F, -0.5F, 4, 1, 1, 0.0F);
		this.setRotateAngle(drilArm3, 0.0F, 0.0F, -0.13962634015954636F);
		this.cannon = new AdvancedModelBox(this, 68, 47);
		this.cannon.setRotationPoint(0.0F, 20.0F, 0.0F);
		this.cannon.addBox(-3.0F, 0.0F, -4.0F, 6, 14, 7, 0.0F);
		this.setRotateAngle(cannon, -0.3490658503988659F, -0.13321877203096466F, -0.31359068978645194F);
		this.armRight1 = new AdvancedModelBox(this, 82, 0);
		this.armRight1.mirror = true;
		this.armRight1.setRotationPoint(-17.0F, -4.0F, 0.0F);
		this.armRight1.addBox(-3.0F, -4.0F, -2.5F, 5, 18, 5, 0.0F);
		this.setRotateAngle(armRight1, -0.05235987755982988F, 0.2617993877991494F, 0.22689280275926282F);
		this.thruster = new AdvancedModelBox(this, 36, 18);
		this.thruster.setRotationPoint(0.0F, 12.0F, 0.0F);
		this.thruster.addBox(-5.0F, -1.0F, -4.0F, 10, 8, 8, 0.0F);
		this.setRotateAngle(thruster, 0.3490658503988659F, 0.0F, 0.0F);
		this.drillArm2 = new AdvancedModelBox(this, 0, 26);
		this.drillArm2.setRotationPoint(0.0F, 17.0F, 0.0F);
		this.drillArm2.addBox(-1.0F, 0.0F, -1.0F, 1, 12, 2, 0.0F);
		this.setRotateAngle(drillArm2, 0.0F, 0.0F, 0.13962634015954636F);
		this.armLeft2 = new AdvancedModelBox(this, 107, 26);
		this.armLeft2.setRotationPoint(0.0F, 18.0F, -2.0F);
		this.armLeft2.addBox(-1.5F, -1.0F, -2.5F, 3, 18, 5, 0.0F);
		this.setRotateAngle(armLeft2, -1.0471975511965976F, -0.08726646259971647F, 0.22689280275926282F);
		this.drillArm1 = new AdvancedModelBox(this, 0, 26);
		this.drillArm1.setRotationPoint(0.0F, 17.0F, 0.0F);
		this.drillArm1.addBox(0.0F, 0.0F, -1.0F, 1, 12, 2, 0.0F);
		this.setRotateAngle(drillArm1, 0.0F, 0.0F, -0.13962634015954636F);
		this.armLeft1 = new AdvancedModelBox(this, 82, 0);
		this.armLeft1.setRotationPoint(17.0F, -4.0F, 0.0F);
		this.armLeft1.addBox(-2.0F, -4.0F, -2.5F, 5, 18, 5, 0.0F);
		this.setRotateAngle(armLeft1, -0.05235987755982988F, -0.2617993877991494F, -0.22689280275926282F);
		this.blade = new AdvancedModelBox(this, 0, 42);
		this.blade.setRotationPoint(1.5F, 0.0F, 0.0F);
		this.blade.addBox(0.0F, -7.0F, -7.0F, 0, 14, 14, 0.0F);
		this.setRotateAngle(blade, 0.7853981633974483F, 0.0F, 0.0F);
		this.ear1 = new AdvancedModelBox(this, 34, 0);
		this.ear1.setRotationPoint(7.0F, -3.0F, 5.0F);
		this.ear1.addBox(0.0F, -6.0F, 0.0F, 7, 7, 1, 0.0F);
		this.setRotateAngle(ear1, -0.2617993877991494F, -0.9075712110370513F, 0.0F);
		this.armRight2 = new AdvancedModelBox(this, 107, 26);
		this.armRight2.setRotationPoint(0.0F, 18.0F, -2.0F);
		this.armRight2.addBox(-1.5F, -1.0F, -2.5F, 3, 18, 5, 0.0F);
		this.setRotateAngle(armRight2, -1.0471975511965976F, 0.08726646259971647F, -0.22689280275926282F);

		this.riderPlatePivot = new AdvancedModelBox(this, 34, 0);
		this.riderPlatePivot.setRotationPoint(0.0F, -15.0F, 0.0F);
		this.riderPlate = new AdvancedModelBox(this, 34, 0);
		this.riderPlate.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.riderPlate.addBox(-3.5F, -3.5F, 0.0F, 7, 7, 1, 0.3F);
		this.setRotateAngle(this.riderPlate, (float) Math.toRadians(90), 0F, 0.0F);

		this.headBase = new AdvancedModelBox(this, 44, 0);
		this.headBase.setRotationPoint(0.0F, -15.0F, 0.0F);
		this.headBase.addBox(-4.0F, -6.0F, -7.0F, 8, 7, 11, 0.0F);
		this.setRotateAngle(headBase, 0.22759093446006054F, 0.0F, 0.0F);
		this.nose = new AdvancedModelBox(this, 64, 18);
		this.nose.setRotationPoint(0.0F, -3.0F, -3.0F);
		this.nose.addBox(-1.5F, -2.0F, -4.0F, 3, 3, 4, 0.0F);
		this.setRotateAngle(nose, 0.17453292519943295F, 0.0F, 0.0F);
		this.snout = new AdvancedModelBox(this, 72, 23);
		this.snout.setRotationPoint(0.0F, 0.0F, -8.0F);
		this.snout.addBox(-3.0F, -4.0F, -5.0F, 6, 5, 6, 0.0F);

		this.headBase.addChild(this.ear2);
		this.snout.addChild(this.nose);
		this.headBase.addChild(this.ear1);
		this.headBase.addChild(this.snout);
		this.body.addChild(this.upperbody);
		this.drillArm2.addChild(this.drilArm3);
		this.armRight2.addChild(this.cannon);
		this.body.addChild(this.thruster);
		this.armLeft2.addChild(this.drillArm2);
		this.armLeft1.addChild(this.armLeft2);
		this.armLeft2.addChild(this.drillArm1);
		this.drilArm3.addChild(this.blade);
		this.armRight1.addChild(this.armRight2);
		this.riderPlatePivot.addChild(this.ear2);
		this.riderPlatePivot.addChild(this.ear1);
		this.riderPlatePivot.addChild(this.riderPlate);
		this.animator = ModelAnimator.create();
		this.updateDefaultPose();
	}

	@Override
	public Iterable<BasicModelPart> parts() {
		return ImmutableList.of(this.body, this.riderPlatePivot, this.headBase, this.armLeft1, this.armRight1);
	}

	public void animate(IAnimatedEntity entity) {
		this.resetToDefaultPose();
		this.animator.update(entity);
		this.animator.setAnimation(this.mount ? RatAutomatonMount.ANIMATION_MELEE : RatlanteanAutomaton.ANIMATION_MELEE);
		this.animator.startKeyframe(5);
		this.rotateFrom(this.body, 0, -20, 0);
		this.rotateFrom(this.armLeft1, -60, -20, 10);
		this.rotateFrom(this.armLeft2, -100, -5, 30);
		this.animator.endKeyframe();
		this.animator.startKeyframe(5);
		this.rotateFrom(this.armLeft1, 0, 40, -10);
		this.rotateFrom(this.armLeft2, -30, 40, -30);
		this.animator.endKeyframe();
		this.animator.resetKeyframe(5);
		this.animator.setAnimation(this.mount ? RatAutomatonMount.ANIMATION_RANGED : RatlanteanAutomaton.ANIMATION_RANGED);
		this.animator.startKeyframe(5);
		this.animator.move(this.armRight2, -1F, 0, 2.5F);
		this.rotateFrom(this.armRight1, -90, -10, 10);
		this.rotateFrom(this.armRight2, 10, 0, 0);
		this.rotateFrom(this.cannon, 0, 10, 5);
		this.animator.endKeyframe();
		this.animator.setStaticKeyframe(5);
		this.animator.resetKeyframe(5);
		this.animator.endKeyframe();
	}

	public void renderHead(PoseStack stack, VertexConsumer buffer, int light, int overlay, float red, float green, float blue, float alpha) {
		this.headBase.render(stack, buffer, light, overlay, red, green, blue, alpha);
	}

	public void setTERotationAngles(float tickCount) {
		this.resetToDefaultPose();
		this.ear1.rotationPointZ = 3;
		this.ear2.rotationPointZ = 3;
		float idleSpeed = 0.1F;
		float idleDegree = 0.7F;
		this.bob(this.headBase, idleSpeed, idleDegree, 0, 0, false, tickCount, 1);
		this.bob(this.ear2, idleSpeed, idleDegree, -1, 0, false, tickCount, 1);
		this.bob(this.ear1, idleSpeed, idleDegree, -1, 0, false, tickCount, 1);
		this.walk(this.headBase, idleSpeed * 0.4F, idleDegree * 0.1F, false, 0, 0, tickCount, 1);
		this.swing(this.ear1, idleSpeed * 0.4F, idleDegree * 0.4F, false, 0, 0, tickCount, 1);
		this.swing(this.ear2, idleSpeed * 0.4F, idleDegree * 0.4F, true, 0, 0, tickCount, 1);
	}

	public void setupAnim(T rat, float f, float f1, float f2, float f3, float f4) {
		this.animate(rat);
		this.blade.showModel = false;
		this.blade.rotateAngleX = (float) Math.toRadians(Mth.wrapDegrees(f2 * 50));
		float idleSpeed = 0.3F;
		float idleDegree = 0.7F;
		this.bob(this.body, idleSpeed, idleDegree, -1, 0, false, f2, 1);
		this.bob(this.headBase, idleSpeed, idleDegree, 0, 0, false, f2, 1);
		this.bob(this.ear2, idleSpeed, idleDegree, -1, 0, false, f2, 1);
		this.bob(this.ear1, idleSpeed, idleDegree, -1, 0, false, f2, 1);
		this.bob(this.armRight1, idleSpeed, idleDegree, 2, 0, false, f2, 1);
		this.bob(this.armRight2, idleSpeed, idleDegree, 2, 0, false, f2, 1);
		this.bob(this.armLeft1, idleSpeed, idleDegree, 3, 0, false, f2, 1);
		this.bob(this.armLeft2, idleSpeed, idleDegree, 3, 0, false, f2, 1);
		this.bob(this.cannon, idleSpeed, idleDegree, 3, 0, false, f2, 1);
		this.walk(this.headBase, idleSpeed * 0.4F, idleDegree * 0.1F, false, 0, 0, f2, 1);
		this.swing(this.ear1, idleSpeed * 0.4F, idleDegree * 0.4F, false, 0, 0, f2, 1);
		this.swing(this.ear2, idleSpeed * 0.4F, idleDegree * 0.4F, true, 0, 0, f2, 1);
		this.walk(this.armRight1, idleSpeed * 0.4F, idleDegree * 0.1F, false, 0, 0, f2, 1);
		this.walk(this.armRight2, idleSpeed * 0.4F, idleDegree * 0.1F, false, 1, -0.1F, f2, 1);
		this.walk(this.cannon, idleSpeed * 0.4F, idleDegree * -0.2F, false, 1, 0F, f2, 1);
		this.walk(this.armLeft1, idleSpeed * 0.4F, idleDegree * 0.1F, false, 0, 0, f2, 1);
		this.walk(this.armLeft2, idleSpeed * 0.4F, idleDegree * 0.1F, false, 1, -0.1F, f2, 1);
		this.walk(this.body, idleSpeed * 0.4F, idleDegree * 0.1F, false, 1, 0.1F, f2, 1);
		this.walk(this.thruster, idleSpeed * 0.4F, idleDegree * 0.2F, false, 1, 0.1F, f2, 1);
		this.faceTarget(f3, f4, 1, headBase);

		this.headBase.showModel = !this.mount;
		this.snout.showModel = !this.mount;
		this.nose.showModel = !this.mount;

		this.riderPlate.showModel = this.mount;
		this.riderPlatePivot.showModel = this.mount;
	}

	public void bob(AdvancedModelBox box, float speed, float degree, float offset, float weight, boolean bounce, float f, float f1) {
		float movementScale = box.getModel().getMovementScale();
		degree *= movementScale;
		speed *= movementScale;

		float bob = (float) (Math.sin(f * speed + offset) * f1 * degree - f1 * degree + f1 * weight);
		if (bounce) {
			bob = (float) -Math.abs((Math.sin(f * speed + offset) * f1 * degree + f1 * weight));
		}
		box.rotationPointY += bob;
	}


	public void setRotateAngle(AdvancedModelBox box, float x, float y, float z) {
		box.rotateAngleX = x;
		box.rotateAngleY = y;
		box.rotateAngleZ = z;
	}

	@Override
	public Iterable<AdvancedModelBox> getAllParts() {
		return ImmutableList.of(this.body,
				this.armLeft1,
				this.armRight1,
				this.thruster,
				this.upperbody,
				this.ear1,
				this.ear2,
				this.armLeft2,
				this.drillArm1,
				this.drillArm2,
				this.drilArm3,
				this.blade,
				this.armRight2,
				this.cannon,
				this.riderPlate,
				this.riderPlatePivot,
				this.headBase,
				this.nose,
				this.snout);
	}

	private void rotateFrom(AdvancedModelBox renderer, float degX, float degY, float degZ) {
		this.animator.rotate(renderer, (float) Math.toRadians(degX) - renderer.defaultRotationX, (float) Math.toRadians(degY) - renderer.defaultRotationY, (float) Math.toRadians(degZ) - renderer.defaultRotationZ);
	}
}
