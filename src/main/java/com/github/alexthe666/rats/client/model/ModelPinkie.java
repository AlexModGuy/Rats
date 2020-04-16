package com.github.alexthe666.rats.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPinkie<T extends EntityRat> extends AdvancedEntityModel<T>{
    public AdvancedModelBox body;

    public ModelPinkie() {
        super();
        this.textureWidth = 16;
        this.textureHeight = 16;
        this.body = new AdvancedModelBox(this, 0, 0);
        this.body.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.body.addBox(-1.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(body);
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
    }

    public void setRotationAngles(EntityRat rat, float f, float f1, float f2, float f3, float f4) {
        float speedIdle = 0.35F;
        float degreeIdle = 0.15F;
        animate((IAnimatedEntity) rat, f, f1, f2, f3, f4);
        this.swing(this.body, speedIdle * 1.5F, degreeIdle * 1.5F, true, 0, 0F, rat.ticksExisted, 1);

    }
}
