package com.github.alexthe666.rats.client.model.entity;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.rats.server.entity.misc.RattlingGun;
import com.google.common.collect.ImmutableList;

public class RattlingGunBaseModel<T extends RattlingGun> extends AdvancedEntityModel<T> {
	public final AdvancedModelBox base1;
	public final AdvancedModelBox pivot;
	public final AdvancedModelBox backleg;
	public final AdvancedModelBox leftleg;
	public final AdvancedModelBox rightleg;
	public final AdvancedModelBox base2;

	public RattlingGunBaseModel() {
		this.texWidth = 64;
		this.texHeight = 64;
		this.backleg = new AdvancedModelBox(this, 56, 13);
		this.backleg.setRotationPoint(0.0F, 0.0F, 2.0F);
		this.backleg.addBox(-1.0F, 0.0F, -2.0F, 2, 15, 2, 0.0F);
		this.setRotateAngle(this.backleg, 0.5235987755982988F, 0.0F, 0.0F);
		this.leftleg = new AdvancedModelBox(this, 56, 13);
		this.leftleg.setRotationPoint(2.0F, 0.0F, -2.0F);
		this.leftleg.addBox(-1.0F, 0.0F, -2.0F, 2, 15, 2, 0.0F);
		this.setRotateAngle(this.leftleg, 0.5235987755982988F, 2.2689280275926285F, 0.0F);
		this.base1 = new AdvancedModelBox(this, 0, 48);
		this.base1.setRotationPoint(0.0F, 10.0F, 0.0F);
		this.base1.addBox(-2.0F, 0.0F, -2.0F, 4, 3, 4, 0.0F);
		this.base2 = new AdvancedModelBox(this, 2, 24);
		this.base2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.base2.addBox(-3.0F, -1.0F, -3.0F, 6, 1, 6, 0.0F);
		this.pivot = new AdvancedModelBox(this, 26, 21);
		this.pivot.setRotationPoint(0.0F, 9.0F, 0.0F);
		this.pivot.addBox(-2.0F, -4.0F, -2.0F, 4, 6, 4, 0.0F);
		this.rightleg = new AdvancedModelBox(this, 56, 13);
		this.rightleg.setRotationPoint(-2.0F, 0.0F, -2.0F);
		this.rightleg.addBox(-1.0F, 0.0F, -2.0F, 2, 15, 2, 0.0F);
		this.setRotateAngle(this.rightleg, 0.5235987755982988F, -2.2689280275926285F, 0.0F);
		this.base1.addChild(this.backleg);
		this.base1.addChild(this.leftleg);
		this.base1.addChild(this.base2);
		this.base1.addChild(this.rightleg);
		this.updateDefaultPose();
	}

	@Override
	public Iterable<BasicModelPart> parts() {
		return ImmutableList.of(this.base1, this.pivot);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.resetToDefaultPose();
	}

	public void setRotateAngle(AdvancedModelBox box, float x, float y, float z) {
		box.rotateAngleX = x;
		box.rotateAngleY = y;
		box.rotateAngleZ = z;
	}

	@Override
	public Iterable<AdvancedModelBox> getAllParts() {
		return ImmutableList.of(this.base1,
				this.pivot,
				this.backleg,
				this.leftleg,
				this.rightleg,
				this.base2
		);
	}
}
