package com.github.alexthe666.rats.api;

import com.github.alexthe666.rats.client.render.entity.*;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;

/*
    Parent for all rat rendering events.
 */
@OnlyIn(Dist.CLIENT)
public class RatClientEvent extends Event {

    private EntityRat rat;
    private RenderRat ratRender;

    public RatClientEvent(EntityRat rat, RenderRat ratRender) {
        this.rat = rat;
        this.ratRender = ratRender;
    }

    public EntityRat getRat() {
        return rat;
    }

    public RenderRat getRatRender() {
        return ratRender;
    }

    /**
     * Called in {@link RenderRat#getEntityTexture)} before rat texture is set. A few rat
     * upgrades replace the texture directly here.
     */
    @HasResult
    public static class GetTexture extends RatClientEvent {

        private ResourceLocation texture;

        public GetTexture(EntityRat rat, RenderRat ratRender, ResourceLocation texture) {
            super(rat, ratRender);
            this.texture = texture;
        }

        public ResourceLocation getTexture() {
            return texture;
        }

        public void setTexture(ResourceLocation texture) {
            this.texture = texture;
        }
    }

    /**
     * Called in {@link LayerRatPlague#render)} before rat overlay texture is set. A few rat
     * upgrades set the texture directly here.
     */
    @HasResult
    public static class GetPlagueOverlayTexture extends RatClientEvent {

        private RenderType texture;

        public GetPlagueOverlayTexture(EntityRat rat, RenderRat ratRender) {
            super(rat, ratRender);
        }

        public RenderType getTexture() {
            return texture;
        }

        public void setTexture(RenderType texture) {
            this.texture = texture;
        }
    }

    /**
     * Called in {@link LayerRatEyes#render} before rat eye glow texture is set. A few rat
     * upgrades set the texture directly here. Make sure you check out {@link EntityRat#shouldEyesGlow()}
     * to make them glow as well.
     */
    @HasResult
    public static class GetEyesTexture extends RatClientEvent {

        private RenderType texture;

        public GetEyesTexture(EntityRat rat, RenderRat ratRender) {
            super(rat, ratRender);
        }

        public RenderType getTexture() {
            return texture;
        }

        public void setTexture(RenderType texture) {
            this.texture = texture;
        }
    }

    /**
     * Called in {@link LayerRatHelmet#render} before rat helmets
     * are rendered for shiny rat god/nonbeliever overlay.
     */
    public static class RatHelmetSheen extends RatClientEvent {

        public RatHelmetSheen(EntityRat rat, RenderRat ratRender) {
            super(rat, ratRender);
        }
    }

    /**
     * Called in {@link LayerRatHelmet#render} before render matrix operations
     * are applied to rat helmets.
     */
    public static class RatHelmetTranslation extends RatClientEvent {

        private MatrixStack matrixStack;

        public RatHelmetTranslation(EntityRat rat, RenderRat ratRender, MatrixStack matrixStack) {
            super(rat, ratRender);
            this.matrixStack = matrixStack;
        }

        public MatrixStack getMatrixStack() {
            return matrixStack;
        }

    }

    /**
     * Called in {@link LayerRatHeldItem#render} when certian upgrades
     * require rats to hold items.
     */
    public static class RatItemTranslation extends RatClientEvent {

        private MatrixStack matrixStack;

        public RatItemTranslation(EntityRat rat, RenderRat ratRender, MatrixStack matrixStack) {
            super(rat, ratRender);
            this.matrixStack = matrixStack;
        }

        public MatrixStack getMatrixStack() {
            return matrixStack;
        }

    }
}
