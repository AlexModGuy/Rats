package com.github.alexthe666.rats.client.model;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;

public class ModelPinkie extends AdvancedModelBase {
    public AdvancedModelRenderer body;

    public ModelPinkie() {
        this.textureWidth = 16;
        this.textureHeight = 16;
        this.body = new AdvancedModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.body.addBox(-1.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F);
        this.updateDefaultPose();
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        animate((IAnimatedEntity) entity, f, f1, f2, f3, f4, f5);
        this.body.render(f5);
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.resetToDefaultPose();
        setRotationAngles(f, f1, f2, f3, f4, f5, (EntityRat) entity);
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, EntityRat rat) {
        float speedIdle = 0.35F;
        float degreeIdle = 0.15F;
        this.swing(this.body, speedIdle * 1.5F, degreeIdle * 1.5F, true, 0, 0F, rat.ticksExisted, 1);

    }
}
