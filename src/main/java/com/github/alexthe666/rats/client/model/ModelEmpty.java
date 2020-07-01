package com.github.alexthe666.rats.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelEmpty<T extends Entity> extends AdvancedEntityModel<T>{

    public ModelEmpty() {
        super();
        this.textureWidth = 16;
        this.textureHeight = 16;
        this.updateDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of();
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of();
    }

    public void setRotationAngles(Entity rat, float f, float f1, float f2, float f3, float f4) {

    }
}
