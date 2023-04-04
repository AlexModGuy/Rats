package com.github.alexthe666.rats.client.model.deco;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.rats.server.block.entity.RatCageWheelBlockEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Entity;

public class RatWheelModel<T extends Entity> extends AdvancedEntityModel<T> {
	public final AdvancedModelBox axle;
	public final AdvancedModelBox groundBaseL;
	public final AdvancedModelBox groundBaseR;
	public final AdvancedModelBox wheel1;
	public final AdvancedModelBox wheel2;

	public RatWheelModel() {
		this.texWidth = 64;
		this.texHeight = 64;
		this.wheel2 = new AdvancedModelBox(this, 0, 24);
		this.wheel2.setPos(0.0F, 4.5F, -5.0F);
		this.wheel2.addBox(-5.5F, 0.0F, -7.0F, 11.0F, 10.0F, 14.0F, 0.0F);
		this.setRotateAngle(this.wheel2, 1.5707964F, 0.0F, 0.0F);
		this.groundBaseL = new AdvancedModelBox(this, 0, 0);
		this.groundBaseL.mirror = true;
		this.groundBaseL.setPos(0.0F, 20.5F, 0.0F);
		this.groundBaseL.addBox(6.5F, -5.5F, -1.5F, 1.0F, 8.0F, 3.0F, 0.0F);
		this.wheel1 = new AdvancedModelBox(this, 0, 0);
		this.wheel1.setPos(0.0F, -4.5F, 0.0F);
		this.wheel1.addBox(-5.5F, 0.0F, -7.0F, 11.0F, 10.0F, 14.0F, 0.0F);
		this.axle = new AdvancedModelBox(this, 0, 50);
		this.axle.setPos(0.0F, 15.0F, 0.0F);
		this.axle.addBox(-8.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, 0.0F);
		this.groundBaseR = new AdvancedModelBox(this, 0, 0);
		this.groundBaseR.setPos(0.0F, 20.5F, 0.0F);
		this.groundBaseR.addBox(-7.5F, -5.5F, -1.5F, 1.0F, 8.0F, 3.0F, 0.0F);
		this.wheel1.addChild(this.wheel2);
		this.axle.addChild(this.wheel1);
		this.updateDefaultPose();
	}

	public void animate(RatCageWheelBlockEntity wheel, float partialTicks) {
		this.resetToDefaultPose();
		float rot = wheel.prevWheelRot + (wheel.wheelRot - wheel.prevWheelRot) * partialTicks;
		this.axle.rotateAngleX = (float) Math.toRadians(rot);
		this.wheel1.setScale(0.9F, 0.9F, 0.9F);
		this.wheel1.setShouldScaleChildren(true);
	}

	@Override
	public void setupAnim(T t, float v, float v1, float v2, float v3, float v4) {
	}

	@Override
	public Iterable<BasicModelPart> parts() {
		return ImmutableList.of(this.axle, this.groundBaseL, this.groundBaseR);
	}

	@Override
	public Iterable<AdvancedModelBox> getAllParts() {
		return ImmutableList.of(this.axle, this.groundBaseL, this.groundBaseR, this.wheel1, this.wheel2);
	}

	public void setRotateAngle(AdvancedModelBox box, float x, float y, float z) {
		box.rotateAngleX = x;
		box.rotateAngleY = y;
		box.rotateAngleZ = z;
	}
}
