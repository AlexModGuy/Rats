package com.github.alexthe666.rats.client.model.deco;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class RatHammockModel<T extends Entity> extends EntityModel<T> {

	private final ModelPart root;

	public RatHammockModel(ModelPart root) {
		this.root = root;
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition partdefinition = mesh.getRoot();

		PartDefinition hammock = partdefinition.addOrReplaceChild("hammock", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-6.5F, -12.0F, -4.0F, 13.0F, 8.0F, 8.0F, new CubeDeformation(-0.25F))
						.texOffs(24, 16)
						.addBox(-2.5F, -4.0F, -4.0F, 5.0F, 1.0F, 8.0F, new CubeDeformation(0.01F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		hammock.addOrReplaceChild("left", CubeListBuilder.create()
						.texOffs(0, 16)
						.addBox(0.0F, 0.0F, -4.0F, 4.0F, 1.0F, 8.0F),
				PartPose.offsetAndRotation(-6.0F, -5.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		hammock.addOrReplaceChild("right", CubeListBuilder.create()
						.texOffs(0, 16)
						.addBox(-4.0F, 0.0F, -4.0F, 4.0F, 1.0F, 8.0F),
				PartPose.offsetAndRotation(6.0F, -5.0F, 0.0F, 0.0F, 0.0F, -0.2618F));


		return LayerDefinition.create(mesh, 64, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer consumer, int light, int overlay, float red, float green, float blue, float alpha) {
		this.root.render(stack, consumer, light, overlay, red, green, blue, alpha);
	}
}
