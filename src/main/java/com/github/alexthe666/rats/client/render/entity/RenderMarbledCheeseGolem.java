package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelMarbledCheeseGolem;
import com.github.alexthe666.rats.server.entity.EntityMarbleCheeseGolem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderMarbledCheeseGolem extends MobRenderer<EntityMarbleCheeseGolem, ModelMarbledCheeseGolem<EntityMarbleCheeseGolem>> {

    private static final ResourceLocation MARBLED_CHEESE_GOLEM_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/marble_cheese_golem.png");
    private static final ResourceLocation GLOW_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/marble_cheese_golem_glow.png");

    public RenderMarbledCheeseGolem() {
        super(Minecraft.getInstance().getRenderManager(), new ModelMarbledCheeseGolem(), 0.95F);
        this.addLayer(new LayerGlowingOverlay(this, GLOW_TEXTURE));
    }

    protected void preRenderCallback(EntityMarbleCheeseGolem rat, float partialTickTime) {
        GL11.glScalef(1.2F, 1.2F, 1.2F);
        GL11.glTranslatef(0, -0.7F, 0);
    }

    protected ResourceLocation getEntityTexture(EntityMarbleCheeseGolem entity) {
        return MARBLED_CHEESE_GOLEM_TEXTURE;
    }
}
