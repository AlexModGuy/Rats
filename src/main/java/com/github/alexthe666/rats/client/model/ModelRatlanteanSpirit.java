package com.github.alexthe666.rats.client.model;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.EntityRatlanteanSpirit;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.MobEffects;
import org.lwjgl.opengl.GL11;

public class ModelRatlanteanSpirit extends AdvancedModelBase {
    public AdvancedModelRenderer head;
    public AdvancedModelRenderer snout;
    public AdvancedModelRenderer leftEar;
    public AdvancedModelRenderer rightEar;
    public AdvancedModelRenderer leftEye;
    public AdvancedModelRenderer rightEye;
    public AdvancedModelRenderer nose;
    public AdvancedModelRenderer wisker2;
    public AdvancedModelRenderer wisker2_1;

    public ModelRatlanteanSpirit() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.nose = new AdvancedModelRenderer(this, 32, 26);
        this.nose.setRotationPoint(0.0F, -0.6F, -2.1F);
        this.nose.addBox(-0.5F, -0.3F, -1.0F, 1, 1, 2, 0.0F);
        this.rightEar = new AdvancedModelRenderer(this, 33, 20);
        this.rightEar.setRotationPoint(-1.5F, -0.5F, 0.0F);
        this.rightEar.addBox(-2.0F, -2.0F, 0.0F, 2, 2, 0, 0.0F);
        this.setRotateAngle(rightEar, 0.0F, 0.7853981633974483F, 0.0F);
        this.wisker2 = new AdvancedModelRenderer(this, 35, 0);
        this.wisker2.setRotationPoint(-1.0F, 0.0F, -1.0F);
        this.wisker2.addBox(-2.5F, -1.5F, 0.0F, 2, 3, 0, 0.0F);
        this.setRotateAngle(wisker2, 0.0F, 0.6108652381980153F, 0.0F);
        this.head = new AdvancedModelRenderer(this, 22, 20);
        this.head.setRotationPoint(0.0F, 20.0F, 1.0F);
        this.head.addBox(-2.0F, -1.5F, -2.5F, 4, 3, 3, 0.0F);
        this.setRotateAngle(head, 0.18657569703819382F, 0.0F, 0.0F);
        this.snout = new AdvancedModelRenderer(this, 22, 26);
        this.snout.setRotationPoint(0.0F, 0.5F, -2.5F);
        this.snout.addBox(-1.5F, -1.0F, -2.0F, 3, 2, 2, 0.0F);
        this.rightEye = new AdvancedModelRenderer(this, 37, 20);
        this.rightEye.setRotationPoint(-2.0F, -0.5F, -1.6F);
        this.rightEye.addBox(-0.5F, -0.7F, -0.5F, 1, 1, 1, 0.0F);
        this.wisker2_1 = new AdvancedModelRenderer(this, 35, 0);
        this.wisker2_1.mirror = true;
        this.wisker2_1.setRotationPoint(1.0F, 0.0F, -1.0F);
        this.wisker2_1.addBox(0.5F, -1.5F, 0.0F, 2, 3, 0, 0.0F);
        this.setRotateAngle(wisker2_1, 0.0F, -0.6108652381980153F, 0.0F);
        this.leftEar = new AdvancedModelRenderer(this, 33, 20);
        this.leftEar.mirror = true;
        this.leftEar.setRotationPoint(1.5F, -0.5F, 0.0F);
        this.leftEar.addBox(0.0F, -2.0F, 0.0F, 2, 2, 0, 0.0F);
        this.setRotateAngle(leftEar, 0.0F, -0.7853981633974483F, 0.0F);
        this.leftEye = new AdvancedModelRenderer(this, 37, 20);
        this.leftEye.setRotationPoint(2.0F, -0.5F, -1.6F);
        this.leftEye.addBox(-0.5F, -0.7F, -0.5F, 1, 1, 1, 0.0F);
        this.snout.addChild(this.nose);
        this.head.addChild(this.rightEar);
        this.snout.addChild(this.wisker2);
        this.head.addChild(this.snout);
        this.head.addChild(this.rightEye);
        this.snout.addChild(this.wisker2_1);
        this.head.addChild(this.leftEar);
        this.head.addChild(this.leftEye);
        this.updateDefaultPose();
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        animate((IAnimatedEntity) entity, f, f1, f2, f3, f4, f5);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.7F);
        GlStateManager.disableLighting();
        this.head.render(f5);
        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.resetToDefaultPose();
        setRotationAngles(f, f1, f2, f3, f4, f5, (EntityRatlanteanSpirit) entity);
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, EntityRatlanteanSpirit rat) {
        float speedIdle = 0.35F;
        float degreeIdle = 0.15F;
        this.faceTarget(f3, f4, 1, head);
        float ulatingScale = 0.9F + (float) Math.sin(f2 * 0.75F) * 0.1F;
        this.swing(this.wisker2, speedIdle, degreeIdle, false, 0, 0, f2, 1);
        this.swing(this.wisker2_1, speedIdle, degreeIdle, true, 0, 0, f2, 1);
        this.flap(this.wisker2, speedIdle, degreeIdle, false, 1, 0, f2, 1);
        this.flap(this.wisker2_1, speedIdle, degreeIdle, false, 1, 0, f2, 1);
        this.walk(this.wisker2, speedIdle, degreeIdle, false, 2, 0, f2, 1);
        this.walk(this.wisker2_1, speedIdle, degreeIdle, false, 2, 0, f2, 1);
        this.nose.setScale(ulatingScale, ulatingScale, ulatingScale);
        this.walk(this.head, speedIdle, degreeIdle, false, 0, 0, f, f1);
        this.faceTarget(f3, f4, 1, head);

    }

    public void setRotateAngle(AdvancedModelRenderer AdvancedModelRenderer, float x, float y, float z) {
        AdvancedModelRenderer.rotateAngleX = x;
        AdvancedModelRenderer.rotateAngleY = y;
        AdvancedModelRenderer.rotateAngleZ = z;
    }
}
