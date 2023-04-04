package com.github.alexthe666.rats.client.model.block;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Entity;

public class RatTrapModel<T extends Entity> extends AdvancedEntityModel<T> {
	public final AdvancedModelBox bottom;
	public final AdvancedModelBox hingeMain;
	public final AdvancedModelBox spring;
	public final AdvancedModelBox hingeTop;
	public final AdvancedModelBox hinge2;

	public RatTrapModel() {
		this.texWidth = 64;
		this.texHeight = 32;
		this.hingeTop = new AdvancedModelBox(this, 0, 27);
		this.hingeTop.setRotationPoint(-0.5F, 0.0F, 7.0F);
		this.hingeTop.addBox(-9.0F, -0.5F, 0.0F, 10, 1, 1, 0.0F);
		this.hinge2 = new AdvancedModelBox(this, 0, 19);
		this.hinge2.setRotationPoint(-9.0F, 0.0F, 0.0F);
		this.hinge2.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 7, 0.0F);
		this.hingeMain = new AdvancedModelBox(this, 0, 19);
		this.hingeMain.setRotationPoint(4.5F, 22.0F, 0.0F);
		this.hingeMain.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 7, 0.0F);
		this.spring = new AdvancedModelBox(this, 0, 16);
		this.spring.setRotationPoint(0.0F, -2.0F, 0.0F);
		this.spring.addBox(-4.0F, -1.0F, 0.0F, 8, 1, 2, 0.0F);
		this.bottom = new AdvancedModelBox(this, 0, 0);
		this.bottom.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.bottom.addBox(-4.0F, -2.0F, -7.0F, 8, 2, 14, 0.0F);
		this.hingeMain.addChild(this.hingeTop);
		this.hingeMain.addChild(this.hinge2);
		this.bottom.addChild(this.spring);
		this.updateDefaultPose();
	}

	public Iterable<AdvancedModelBox> getAllParts() {
		return ImmutableList.of(this.bottom, this.hingeMain, this.spring, this.hingeTop, this.hinge2);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	public void animateHinge(float shutProgress) {
		this.resetToDefaultPose();
		this.hingeMain.rotateAngleX = (float) (shutProgress * Math.PI / 6.0F);
	}

	@Override
	public Iterable<BasicModelPart> parts() {
		return ImmutableList.of(this.hingeMain, this.bottom);
	}
}
