package com.github.alexthe666.rats.client.model.entity;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.rats.server.entity.misc.RattlingGun;
import com.google.common.collect.ImmutableList;

public class RattlingGunModel<T extends RattlingGun> extends AdvancedEntityModel<T> {
	public final AdvancedModelBox base1;
	public final AdvancedModelBox pivot;
	public final AdvancedModelBox backleg;
	public final AdvancedModelBox leftleg;
	public final AdvancedModelBox rightleg;
	public final AdvancedModelBox base2;
	public final AdvancedModelBox centerThingy;
	public final AdvancedModelBox behind;
	public final AdvancedModelBox gun1;
	public final AdvancedModelBox handle1;
	public final AdvancedModelBox seatConnector1;
	public final AdvancedModelBox handle2;
	public final AdvancedModelBox handle3;
	public final AdvancedModelBox seatConnector2;
	public final AdvancedModelBox seat;
	public final AdvancedModelBox seatBack;
	public final AdvancedModelBox barrel1;
	public final AdvancedModelBox barrel2;
	public final AdvancedModelBox barrel3;
	public final AdvancedModelBox barrel4;
	public final AdvancedModelBox barrel5;
	public final AdvancedModelBox gun2;
	public final AdvancedModelBox gun3;

	public RattlingGunModel() {
		this.texWidth = 64;
		this.texHeight = 64;
		this.barrel2 = new AdvancedModelBox(this, 0, 31);
		this.barrel2.setRotationPoint(2.3F, 0.0F, -2.0F);
		this.barrel2.addBox(-0.5F, -0.5F, -16.0F, 1, 1, 16, 0.0F);
		this.barrel3 = new AdvancedModelBox(this, 0, 31);
		this.barrel3.setRotationPoint(-2.3F, 0.0F, -2.0F);
		this.barrel3.addBox(-0.5F, -0.5F, -16.0F, 1, 1, 16, 0.0F);
		this.backleg = new AdvancedModelBox(this, 56, 13);
		this.backleg.setRotationPoint(0.0F, 0.0F, 2.0F);
		this.backleg.addBox(-1.0F, 0.0F, -2.0F, 2, 15, 2, 0.0F);
		this.setRotateAngle(this.backleg, 0.5235987755982988F, 0.0F, 0.0F);
		this.seatConnector2 = new AdvancedModelBox(this, 26, 48);
		this.seatConnector2.setRotationPoint(0.0F, 0.5F, 8.0F);
		this.seatConnector2.addBox(-8.0F, -0.5F, 0.0F, 10, 1, 3, 0.0F);
		this.leftleg = new AdvancedModelBox(this, 56, 13);
		this.leftleg.setRotationPoint(2.0F, 0.0F, -2.0F);
		this.leftleg.addBox(-1.0F, 0.0F, -2.0F, 2, 15, 2, 0.0F);
		this.setRotateAngle(this.leftleg, 0.5235987755982988F, 2.2689280275926285F, 0.0F);
		this.barrel5 = new AdvancedModelBox(this, 0, 31);
		this.barrel5.setRotationPoint(0.0F, -2.3F, -2.0F);
		this.barrel5.addBox(-0.5F, -0.5F, -16.0F, 1, 1, 16, 0.0F);
		this.base1 = new AdvancedModelBox(this, 0, 48);
		this.base1.setRotationPoint(0.0F, 10.0F, 0.0F);
		this.base1.addBox(-2.0F, 0.0F, -2.0F, 4, 3, 4, 0.0F);
		this.seatBack = new AdvancedModelBox(this, 0, 0);
		this.seatBack.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.seatBack.addBox(-3.0F, -8.0F, -0.5F, 6, 8, 1, 0.0F);
		this.handle2 = new AdvancedModelBox(this, 42, 13);
		this.handle2.setRotationPoint(-5.0F, 0.0F, 0.0F);
		this.handle2.addBox(-1.0F, -1.0F, -1.0F, 1, 2, 6, 0.0F);
		this.gun3 = new AdvancedModelBox(this, 0, 31);
		this.gun3.setRotationPoint(0.0F, 0.0F, -11.0F);
		this.gun3.addBox(-3.0F, -3.0F, -1.0F, 6, 6, 1, 0.0F);
		this.base2 = new AdvancedModelBox(this, 2, 24);
		this.base2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.base2.addBox(-3.0F, -1.0F, -3.0F, 6, 1, 6, 0.0F);
		this.barrel4 = new AdvancedModelBox(this, 0, 31);
		this.barrel4.setRotationPoint(0.0F, 2.3F, -2.0F);
		this.barrel4.addBox(-0.5F, -0.5F, -16.0F, 1, 1, 16, 0.0F);
		this.seatConnector1 = new AdvancedModelBox(this, 6, 48);
		this.seatConnector1.setRotationPoint(0.0F, 3.0F, 5.0F);
		this.seatConnector1.addBox(-2.5F, 0.0F, -2.0F, 5, 1, 10, 0.0F);
		this.centerThingy = new AdvancedModelBox(this, 34, 31);
		this.centerThingy.setRotationPoint(0.0F, -8.0F, 0.0F);
		this.centerThingy.addBox(-4.0F, -4.0F, -1.0F, 8, 8, 2, 0.0F);
		this.barrel1 = new AdvancedModelBox(this, 0, 0);
		this.barrel1.setRotationPoint(0.0F, 0.0F, -2.0F);
		this.barrel1.addBox(-1.5F, -1.5F, -18.0F, 3, 3, 18, 0.0F);
		this.handle3 = new AdvancedModelBox(this, 51, 2);
		this.handle3.setRotationPoint(-1.0F, 0.0F, 4.0F);
		this.handle3.addBox(-3.0F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
		this.gun1 = new AdvancedModelBox(this, 18, 31);
		this.gun1.setRotationPoint(0.0F, 0.0F, -1.0F);
		this.gun1.addBox(-3.0F, -3.0F, -2.0F, 6, 6, 2, 0.0F);
		this.seat = new AdvancedModelBox(this, 40, 52);
		this.seat.setRotationPoint(-7.0F, -0.5F, 3.0F);
		this.seat.addBox(-2.5F, -1.0F, -6.0F, 5, 1, 6, 0.0F);
		this.behind = new AdvancedModelBox(this, 32, 0);
		this.behind.setRotationPoint(0.0F, 0.0F, 2.0F);
		this.behind.addBox(-3.0F, -3.0F, -1.0F, 6, 6, 7, 0.0F);
		this.gun2 = new AdvancedModelBox(this, 0, 31);
		this.gun2.setRotationPoint(0.0F, 0.0F, -15.0F);
		this.gun2.addBox(-3.0F, -3.0F, -1.0F, 6, 6, 1, 0.0F);
		this.pivot = new AdvancedModelBox(this, 26, 21);
		this.pivot.setRotationPoint(0.0F, 9.0F, 0.0F);
		this.pivot.addBox(-2.0F, -4.0F, -2.0F, 4, 6, 4, 0.0F);
		this.rightleg = new AdvancedModelBox(this, 56, 13);
		this.rightleg.setRotationPoint(-2.0F, 0.0F, -2.0F);
		this.rightleg.addBox(-1.0F, 0.0F, -2.0F, 2, 15, 2, 0.0F);
		this.setRotateAngle(this.rightleg, 0.5235987755982988F, -2.2689280275926285F, 0.0F);
		this.handle1 = new AdvancedModelBox(this, 51, 0);
		this.handle1.setRotationPoint(-3.0F, 0.0F, 2.0F);
		this.handle1.addBox(-5.0F, -0.5F, -0.5F, 5, 1, 1, 0.0F);
		this.gun1.addChild(this.barrel2);
		this.gun1.addChild(this.barrel3);
		this.base1.addChild(this.backleg);
		this.seatConnector1.addChild(this.seatConnector2);
		this.base1.addChild(this.leftleg);
		this.gun1.addChild(this.barrel5);
		this.seat.addChild(this.seatBack);
		this.handle1.addChild(this.handle2);
		this.gun1.addChild(this.gun3);
		this.base1.addChild(this.base2);
		this.gun1.addChild(this.barrel4);
		this.behind.addChild(this.seatConnector1);
		this.pivot.addChild(this.centerThingy);
		this.gun1.addChild(this.barrel1);
		this.handle2.addChild(this.handle3);
		this.centerThingy.addChild(this.gun1);
		this.seatConnector2.addChild(this.seat);
		this.centerThingy.addChild(this.behind);
		this.gun1.addChild(this.gun2);
		this.base1.addChild(this.rightleg);
		this.behind.addChild(this.handle1);
		this.updateDefaultPose();
	}

	@Override
	public Iterable<BasicModelPart> parts() {
		return ImmutableList.of(this.pivot);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.resetToDefaultPose();
		if (entity.isFiring()) {
			this.gun1.rotateAngleZ = ageInTicks;
			this.handle1.rotateAngleX = ageInTicks * 0.5F;
		} else {
			this.gun1.rotateAngleZ = 0;
			this.handle1.rotateAngleX = 0;

		}
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
				this.base2,
				this.centerThingy,
				this.behind,
				this.gun1,
				this.handle1,
				this.seatConnector1,
				this.handle2,
				this.handle3,
				this.seatConnector2,
				this.seat,
				this.seatBack,
				this.barrel1,
				this.barrel2,
				this.barrel3,
				this.barrel4,
				this.barrel5,
				this.gun2,
				this.gun3
		);
	}
}
