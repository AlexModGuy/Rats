package com.github.alexthe666.rats.client.render.entity;


import com.github.alexthe666.rats.server.entity.EntityRatChickenMount;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.ChickenModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderRatChickenMount extends MobRenderer<EntityRatChickenMount, ChickenModel<EntityRatChickenMount>> {
    private static final ResourceLocation CHICKEN_TEXTURES = new ResourceLocation("textures/entity/chicken.png");

    public RenderRatChickenMount() {
        super(Minecraft.getInstance().getRenderManager(), new ChickenModel<>(), 0.3F);
    }

    protected ResourceLocation getEntityTexture(EntityRatChickenMount entity) {
        return CHICKEN_TEXTURES;
    }

    protected float handleRotationFloat(EntityRatChickenMount livingBase, float partialTicks) {
        float f = MathHelper.lerp(partialTicks, livingBase.oFlap, livingBase.wingRotation);
        float f1 = MathHelper.lerp(partialTicks, livingBase.oFlapSpeed, livingBase.destPos);
        return (MathHelper.sin(f) + 1.0F) * f1;
    }
}