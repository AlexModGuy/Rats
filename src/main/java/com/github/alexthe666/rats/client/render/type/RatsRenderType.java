package com.github.alexthe666.rats.client.render.type;

import com.github.alexthe666.rats.client.render.tile.RenderRatlantisPortal;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.EndPortalTileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RatsRenderType extends RenderType {
    public static final RenderType GREEN_ENTITY_GLINT = makeType("green_glint", DefaultVertexFormats.POSITION_TEX, 7, 256, RenderType.State.getBuilder().texture(new RenderState.TextureState(new ResourceLocation("rats:textures/entity/rat/green_glint.png"), true, false)).writeMask(COLOR_WRITE).cull(CULL_DISABLED).depthTest(DEPTH_EQUAL).transparency(GLINT_TRANSPARENCY).texturing(ENTITY_GLINT_TEXTURING).build(false));

    public RatsRenderType(String p_i225992_1_, VertexFormat p_i225992_2_, int p_i225992_3_, int p_i225992_4_, boolean p_i225992_5_, boolean p_i225992_6_, Runnable p_i225992_7_, Runnable p_i225992_8_) {
        super(p_i225992_1_, p_i225992_2_, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, p_i225992_7_, p_i225992_8_);
    }

    public static RenderType getRatlantisPortal(int iterationIn) {
        RenderState.TransparencyState renderstate$transparencystate;
        RenderState.TextureState renderstate$texturestate;
        if (iterationIn <= 1) {
            renderstate$transparencystate = TRANSLUCENT_TRANSPARENCY;
            renderstate$texturestate = new RenderState.TextureState(RenderRatlantisPortal.END_SKY_TEXTURE, false, false);
        } else {
            renderstate$transparencystate = ADDITIVE_TRANSPARENCY;
            renderstate$texturestate = new RenderState.TextureState(RenderRatlantisPortal.END_PORTAL_TEXTURE, false, false);
        }

        return makeType("ratlantis_portal", DefaultVertexFormats.POSITION_COLOR, 7, 256, false, true, RenderType.State.getBuilder().transparency(renderstate$transparencystate).texture(renderstate$texturestate).texturing(new RatlantisPortalTexturingState(iterationIn)).build(false));
    }

    public static RenderType getGlowingTranslucent(ResourceLocation locationIn) {
        RenderState.TextureState renderstate$texturestate = new RenderState.TextureState(locationIn, false, false);
        return makeType("eyes", DefaultVertexFormats.ENTITY, 7, 256, false, true,
                RenderType.State.getBuilder().texture(renderstate$texturestate)
                        .transparency(TRANSLUCENT_TRANSPARENCY)
                        .writeMask(COLOR_DEPTH_WRITE)
                        .alpha(DEFAULT_ALPHA)
                        .overlay(OVERLAY_ENABLED)
                        .fog(BLACK_FOG).build(false)
        );
    }

    @OnlyIn(Dist.CLIENT)
    public static final class RatlantisPortalTexturingState extends RenderState.TexturingState {
        private final int iteration;

        public RatlantisPortalTexturingState(int p_i225986_1_) {
            super("portal_texturing", () -> {
                RenderSystem.matrixMode(5890);
                RenderSystem.pushMatrix();
                RenderSystem.loadIdentity();
                RenderSystem.translatef(0.5F, 0.5F, 0.0F);
                RenderSystem.scalef(0.5F, 0.5F, 1.0F);
                RenderSystem.translatef(17.0F / (float)p_i225986_1_, (2.0F + (float)p_i225986_1_ / 0.5F) * ((float)(Util.milliTime() % 80000L) / 80000.0F), 0.0F);
               // RenderSystem.rotatef(((float)(p_i225986_1_ * p_i225986_1_) * 4321.0F + (float)p_i225986_1_ * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);
                RenderSystem.scalef(4.5F - (float)p_i225986_1_ / 4.0F, 4.5F - (float)p_i225986_1_ / 4.0F, 1.0F);
                RenderSystem.mulTextureByProjModelView();
                RenderSystem.matrixMode(5888);
                RenderSystem.setupEndPortalTexGen();
            }, () -> {
                RenderSystem.matrixMode(5890);
                RenderSystem.popMatrix();
                RenderSystem.matrixMode(5888);
                RenderSystem.clearTexGen();
            });
            this.iteration = p_i225986_1_;
        }

        public boolean equals(Object p_equals_1_) {
            if (this == p_equals_1_) {
                return true;
            } else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
                RatlantisPortalTexturingState renderstate$portaltexturingstate = (RatlantisPortalTexturingState)p_equals_1_;
                return this.iteration == renderstate$portaltexturingstate.iteration;
            } else {
                return false;
            }
        }

        public int hashCode() {
            return Integer.hashCode(this.iteration);
        }
    }
}
