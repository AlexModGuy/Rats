package com.github.alexthe666.rats.client.model.block;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Entity;

public class AutoCurdlerModel<T extends Entity> extends AdvancedEntityModel<T> {
	public final BasicModelPart bottom;
	public final BasicModelPart milkTank;

	@Override
	public Iterable<AdvancedModelBox> getAllParts() {
		return ImmutableList.of();
	}

	public AutoCurdlerModel() {
		this.textureHeight = 64;
		this.textureWidth = 64;
		this.bottom = new BasicModelPart(this, 0, 0);
		this.bottom.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.bottom.addBox(-7.0F, -8.0F, -7.0F, 14, 8, 14, 0.0F);
		this.milkTank = new BasicModelPart(this, 0, 22);
		this.milkTank.setRotationPoint(0.0F, -2.0F, 0.0F);
		this.milkTank.addBox(-5.0F, -16.0F, -5.0F, 10, 10, 10, 0.0F);
		this.bottom.addChild(this.milkTank);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public Iterable<BasicModelPart> parts() {
		return ImmutableList.of(this.bottom);
	}
}
