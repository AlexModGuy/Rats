package com.github.alexthe666.rats.client.model.deco;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

public class RatBreedingLanternModel<T extends Entity> extends AdvancedEntityModel<T> {
	public final AdvancedModelBox top;
	public final AdvancedModelBox connector1;
	public final AdvancedModelBox connector2;
	public final AdvancedModelBox connector3;
	public final AdvancedModelBox heart1;
	public final AdvancedModelBox heart2;
	public final AdvancedModelBox heart3;
	public final AdvancedModelBox heart6;
	public final AdvancedModelBox heart4;
	public final AdvancedModelBox heart5;

	public RatBreedingLanternModel() {
		this.texWidth = 16;
		this.texHeight = 32;
		this.top = new AdvancedModelBox(this, 0, 0);
		this.top.setRotationPoint(0.0F, 6.0F, 0.0F);
		this.top.addBox(-2.0F, 1.0F, -2.0F, 4, 1, 4, 0.0F);
		this.connector2 = new AdvancedModelBox(this, 0, 0);
		this.connector2.setRotationPoint(0.0F, 2.0F, 0.0F);
		this.connector2.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
		this.heart1 = new AdvancedModelBox(this, 0, 5);
		this.heart1.setRotationPoint(0.0F, 2.0F, 0.0F);
		this.heart1.addBox(-1.5F, 0.0F, -1.5F, 3, 6, 3, 0.0F);
		this.heart4 = new AdvancedModelBox(this, 0, 20);
		this.heart4.setRotationPoint(0.0F, 1.0F, 0.5F);
		this.heart4.addBox(0.0F, 0.0F, -1.0F, 2, 3, 1, 0.0F);
		this.connector3 = new AdvancedModelBox(this, 0, 0);
		this.connector3.setRotationPoint(0.0F, 2.0F, 0.0F);
		this.connector3.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
		this.connector1 = new AdvancedModelBox(this, 0, 0);
		this.connector1.setRotationPoint(0.0F, 2.0F, 0.0F);
		this.connector1.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
		this.heart3 = new AdvancedModelBox(this, 0, 15);
		this.heart3.setRotationPoint(-1.5F, 1.0F, 0.0F);
		this.heart3.addBox(-3.0F, -2.0F, -1.0F, 3, 3, 2, 0.0F);
		this.heart6 = new AdvancedModelBox(this, 0, 24);
		this.heart6.setRotationPoint(0.0F, 6.0F, 0.0F);
		this.heart6.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
		this.heart5 = new AdvancedModelBox(this, 0, 20);
		this.heart5.mirror = true;
		this.heart5.setRotationPoint(0.0F, 1.0F, 0.5F);
		this.heart5.addBox(-2.0F, 0.0F, -1.0F, 2, 3, 1, 0.0F);
		this.heart2 = new AdvancedModelBox(this, 0, 15);
		this.heart2.mirror = true;
		this.heart2.setRotationPoint(1.5F, 1.0F, 0.0F);
		this.heart2.addBox(0.0F, -2.0F, -1.0F, 3, 3, 2, 0.0F);
		this.connector1.addChild(this.connector2);
		this.connector3.addChild(this.heart1);
		this.heart2.addChild(this.heart4);
		this.connector2.addChild(this.connector3);
		this.top.addChild(this.connector1);
		this.heart1.addChild(this.heart3);
		this.heart1.addChild(this.heart6);
		this.heart3.addChild(this.heart5);
		this.heart1.addChild(this.heart2);
		this.updateDefaultPose();
	}

	@Override
	public Iterable<AdvancedModelBox> getAllParts() {
		return ImmutableList.of(this.top,
				this.connector1, this.connector2, this.connector3,
				this.heart1, this.heart2, this.heart3, this.heart4, this.heart5, this.heart6);
	}

	public void swingChain() {
		this.resetToDefaultPose();
		float speedIdle = 0.1F;
		float degreeIdle = 0.05F;
		AdvancedModelBox[] connectors = new AdvancedModelBox[]{this.connector1, this.connector2, this.connector3};
		float swing = Minecraft.getInstance().player.tickCount;
		this.chainWave(connectors, speedIdle, degreeIdle, 1, swing, 1);
		this.chainFlap(connectors, speedIdle, degreeIdle, -1, swing, 1);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {


	}

	@Override
	public Iterable<BasicModelPart> parts() {
		return ImmutableList.of(this.top);
	}
}
