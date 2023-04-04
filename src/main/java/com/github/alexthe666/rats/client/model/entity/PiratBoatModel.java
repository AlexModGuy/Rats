package com.github.alexthe666.rats.client.model.entity;

import com.github.alexthe666.rats.server.entity.ratlantis.PiratBoat;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class PiratBoatModel<T extends PiratBoat> extends ListModel<T> {

	private final ModelPart leftPaddle;
	private final ModelPart rightPaddle;
	private final ModelPart waterPatch;
	private final ImmutableList<ModelPart> parts;

	public PiratBoatModel(ModelPart root) {
		this.leftPaddle = root.getChild("left_paddle");
		this.rightPaddle = root.getChild("right_paddle");
		this.waterPatch = root.getChild("water_patch");

		ImmutableList.Builder<ModelPart> builder = new ImmutableList.Builder<>();
		builder.add(
				root.getChild("bottom"),
				root.getChild("back"),
				root.getChild("front"),
				root.getChild("right"),
				root.getChild("left"),
				this.leftPaddle, this.rightPaddle);

		this.parts = builder.build();
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 0).addBox(-14.0F, -9.0F, -20.0F, 28.0F, 16.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 3.0F, 1.0F, ((float) Math.PI / 2F), 0.0F, 0.0F));
		partdefinition.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 19).addBox(-13.0F, 10.0F, -1.0F, 18.0F, 6.0F, 2.0F), PartPose.offsetAndRotation(-15.0F, 4.0F, 4.0F, 0.0F, ((float) Math.PI * 1.5F), 0.0F));
		partdefinition.addOrReplaceChild("front", CubeListBuilder.create().texOffs(0, 27).addBox(-8.0F, 10.0F, -1.0F, 16.0F, 6.0F, 2.0F), PartPose.offsetAndRotation(15.0F, 4.0F, 0.0F, 0.0F, ((float) Math.PI / 2F), 0.0F));
		partdefinition.addOrReplaceChild("right", CubeListBuilder.create().texOffs(0, 35).addBox(-14.0F, 10.0F, -1.0F, 28.0F, 6.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 4.0F, -9.0F, 0.0F, (float) Math.PI, 0.0F));
		partdefinition.addOrReplaceChild("left", CubeListBuilder.create().texOffs(0, 43).addBox(-14.0F, 10.0F, -1.0F, 28.0F, 6.0F, 2.0F), PartPose.offset(0.0F, 4.0F, 9.0F));
		partdefinition.addOrReplaceChild("left_paddle", CubeListBuilder.create().texOffs(62, 0).addBox(-1.0F, 0.0F, -5.0F, 2.0F, 2.0F, 18.0F).addBox(-1.001F, -3.0F, 8.0F, 1.0F, 6.0F, 7.0F), PartPose.offsetAndRotation(3.0F, 12.0F, 9.0F, 0.0F, 0.0F, 0.19634955F));
		partdefinition.addOrReplaceChild("right_paddle", CubeListBuilder.create().texOffs(62, 20).addBox(-1.0F, 0.0F, -5.0F, 2.0F, 2.0F, 18.0F).addBox(0.001F, -3.0F, 8.0F, 1.0F, 6.0F, 7.0F), PartPose.offsetAndRotation(3.0F, 12.0F, -9.0F, 0.0F, (float) Math.PI, 0.19634955F));
		partdefinition.addOrReplaceChild("water_patch", CubeListBuilder.create().texOffs(0, 0).addBox(-14.0F, -9.0F, -20.0F, 28.0F, 16.0F, 3.0F), PartPose.offsetAndRotation(0.0F, -3.0F, 1.0F, ((float) Math.PI / 2F), 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void setupAnim(PiratBoat boat, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.animatePaddle(boat, 0, this.leftPaddle, limbSwing);
		this.animatePaddle(boat, 1, this.rightPaddle, limbSwing);
	}

	private void animatePaddle(PiratBoat boat, int paddleIndex, ModelPart part, float limbSwing) {
		float f = boat.getRowingTime(paddleIndex, limbSwing);
		part.xRot = Mth.clampedLerp((-(float) Math.PI / 3F), -0.2617994F, (Mth.sin(-f) + 1.0F) / 2.0F);
		part.yRot = Mth.clampedLerp((-(float) Math.PI / 4F), ((float) Math.PI / 4F), (Mth.sin(-f + 1.0F) + 1.0F) / 2.0F);
		if (paddleIndex == 1) {
			part.yRot = (float) Math.PI - part.yRot;
		}
	}

	public ModelPart getWaterPatch() {
		return this.waterPatch;
	}

	@Override
	public Iterable<ModelPart> parts() {
		return this.parts;
	}
}
