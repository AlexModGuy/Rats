package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.server.entity.ratlantis.EntityRattlingGunBullet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.util.ResourceLocation;

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
