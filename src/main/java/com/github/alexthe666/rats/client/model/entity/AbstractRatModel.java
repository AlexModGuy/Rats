package com.github.alexthe666.rats.client.model.entity;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.PoseStack;

public abstract class AbstractRatModel<T extends Entity> extends AdvancedEntityModel<T> {

	public abstract void translateToHead(PoseStack stack);

	public abstract void translateToBody(PoseStack stack);
}







