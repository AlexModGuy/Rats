package com.github.alexthe666.rats.client.model.block;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.rats.server.block.TrashCanBlock;
import com.github.alexthe666.rats.server.block.entity.TrashCanBlockEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

public class TrashCanModel<T extends Entity> extends AdvancedEntityModel<T> {
	public final AdvancedModelBox can;
	public final AdvancedModelBox lid;
	public final AdvancedModelBox trash;
	public final AdvancedModelBox handle1;
	public final AdvancedModelBox handle2;
	public final AdvancedModelBox handle3;

	public TrashCanModel() {
		this.texWidth = 64;
		this.texHeight = 64;
		this.lid = new AdvancedModelBox(this, 0, 32);
		this.lid.setRotationPoint(0.0F, 6.0F, 7.0F);
		this.lid.addBox(-8.0F, -2.0F, -15.0F, 16, 2, 16, -0.01F);
		this.handle1 = new AdvancedModelBox(this, 0, 0);
		this.handle1.setRotationPoint(0.0F, -4.0F, -7.0F);
		this.handle1.addBox(-2.0F, -0.5F, -0.5F, 4, 1, 1, 0.0F);
		this.handle3 = new AdvancedModelBox(this, 0, 4);
		this.handle3.setRotationPoint(2.0F, 0.0F, 0.0F);
		this.handle3.addBox(-1.0F, 0.5F, -0.5F, 1, 2, 1, 0.0F);
		this.can = new AdvancedModelBox(this, 0, 0);
		this.can.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.can.addBox(-7.0F, -18.0F, -7.0F, 14, 18, 14, 0.0F);
		this.trash = new AdvancedModelBox(this, 0, 50);
		this.trash.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.trash.addBox(-7.0F, 0.0F, -7.0F, 14, 0, 14, 0.0F);
		this.handle2 = new AdvancedModelBox(this, 0, 4);
		this.handle2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.handle2.addBox(-2.0F, 0.5F, -0.5F, 1, 2, 1, 0.0F);
		this.lid.addChild(this.handle1);
		this.handle1.addChild(this.handle3);
		this.handle1.addChild(this.handle2);
		this.updateDefaultPose();
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.resetToDefaultPose();
	}

	@Override
	public Iterable<BasicModelPart> parts() {
		return ImmutableList.of(this.lid, this.can, this.trash);
	}

	@Override
	public Iterable<AdvancedModelBox> getAllParts() {
		return ImmutableList.of(this.lid, this.can, this.trash, this.handle1, this.handle2, this.handle3);
	}

	public void animate(TrashCanBlockEntity trashCan) {
		this.resetToDefaultPose();
		if (trashCan.getBlockState().getValue(TrashCanBlock.LEVEL) <= 0) {
			this.trash.showModel = false;
			this.trash.rotationPointY = 24;
		} else {
			this.trash.showModel = true;
			this.trash.rotationPointY = 24 - (2.5F * trashCan.getBlockState().getValue(TrashCanBlock.LEVEL));
		}
		float openProgress = trashCan.prevLidProgress + (trashCan.lidProgress - trashCan.prevLidProgress) * Minecraft.getInstance().getPartialTick();
		this.lid.rotateAngleX += (float) Math.toRadians(-70D * (openProgress / 20F));
	}
}
