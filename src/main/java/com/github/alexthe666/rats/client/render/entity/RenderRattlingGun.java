package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRattlingGun;
import com.github.alexthe666.rats.server.entity.EntityRattlingGun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderRattlingGun extends MobRenderer<EntityRattlingGun, ModelRattlingGun<EntityRattlingGun>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/rattling_gun.png");

    public RenderRattlingGun() {
        super(Minecraft.getInstance().getRenderManager(), new ModelRattlingGun(), 0.65F);
    }

    protected void preRenderCallback(EntityRattlingGun rat, float partialTickTime) {
    }

    protected ResourceLocation getEntityTexture(EntityRattlingGun entity) {
        return TEXTURE;
    }
}
