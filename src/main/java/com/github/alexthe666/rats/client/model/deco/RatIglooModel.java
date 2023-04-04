package com.github.alexthe666.rats.client.model.deco;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;

public class RatIglooModel<T extends Entity> extends EntityModel<T> {

	private final ModelPart root;

	public RatIglooModel(ModelPart root) {
		this.root = root;
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition partdefinition = mesh.getRoot();

		partdefinition.addOrReplaceChild("igloo", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-5.0F, -8.0F, -5.0F, 10.0F, 8.0F, 10.0F)
						.texOffs(30, 0)
						.addBox(-2.5F, -5.0F, -8.0F, 5.0F, 5.0F, 3.0F)
						.texOffs(0, 19)
						.addBox(-5.0F, -9.0F, -5.0F, 10.0F, 1.0F, 10.0F),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer consumer, int light, int overlay, float red, float green, float blue, float alpha) {
		this.root.render(stack, consumer, light, overlay, red, green, blue, alpha);
	}
}
