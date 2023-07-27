package com.github.alexthe666.rats.client.model.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;

public class PinkieModel<T extends TamedRat> extends AbstractRatModel<T> {
	public final AdvancedModelBox body;

	public PinkieModel() {
		this.texWidth = 16;
		this.texHeight = 16;
		this.body = new AdvancedModelBox(this, "body");
		this.body.setTextureOffset(0, 0);
		this.body.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.body.addBox(-1.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F);
		this.updateDefaultPose();
	}

	@Override
	public Iterable<AdvancedModelBox> getAllParts() {
		return ImmutableList.of(this.body);
	}

	@Override
	public Iterable<BasicModelPart> parts() {
		return ImmutableList.of(this.body);
	}

	@Override
	public void setupAnim(TamedRat rat, float f, float f1, float f2, float f3, float f4) {
		float speedIdle = 0.35F;
		float degreeIdle = 0.15F;
		this.resetToDefaultPose();
		this.swing(this.body, speedIdle * 1.5F, degreeIdle * 1.5F, true, 0, 0F, rat.tickCount, 1);

	}

	@Override
	public void translateToHead(PoseStack stack) {
		this.body.translateRotate(stack);
	}

	@Override
	public void translateToBody(PoseStack stack) {
		this.body.translateRotate(stack);
	}
}
