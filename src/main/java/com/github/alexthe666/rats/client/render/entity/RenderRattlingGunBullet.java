package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.server.entity.EntityRatArrow;
import com.github.alexthe666.rats.server.entity.EntityRattlingGunBullet;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

public class RenderRattlingGunBullet  extends ArrowRenderer<EntityRattlingGunBullet> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/rattling_gun_bullet.png");

    public RenderRattlingGunBullet() {
        super(Minecraft.getInstance().getRenderManager());
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(EntityRattlingGunBullet entity) {
        return TEXTURE;
    }
}
