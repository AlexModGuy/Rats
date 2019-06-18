package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelIllagerPiper;
import com.github.alexthe666.rats.server.entity.EntityIllagerPiper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelIllager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderIllagerPiper extends RenderLiving<EntityIllagerPiper> {
    private static final ResourceLocation ILLUSIONIST = new ResourceLocation("rats:textures/entity/illager_piper.png");

    public RenderIllagerPiper() {
        super(Minecraft.getMinecraft().getRenderManager(), new ModelIllagerPiper(), 0.5F);
        this.addLayer(new LayerHeldItem(this) {
            protected void translateToHand(EnumHandSide side) {
                ModelIllagerPiper model = ((ModelIllagerPiper) this.livingEntityRenderer.getMainModel());
                model.getArm(side).postRender(0.0625F);
                GL11.glTranslatef(0.2F, 0.2F, 0.1F);
                model.nose.rotateAngleX = (float)Math.toRadians(-10);
            }
        });
    }

    protected ResourceLocation getEntityTexture(EntityIllagerPiper entity) {
        return ILLUSIONIST;
    }

    protected void preRenderCallback(EntityIllagerPiper entitylivingbaseIn, float partialTickTime) {
        float f = 0.9375F;
        GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
    }

    public void renderName(EntityIllagerPiper entity, double x, double y, double z) {
        super.renderName(entity, x, y, z);
    }

    protected boolean isVisible(EntityIllagerPiper p_193115_1_) {
        return true;
    }
}