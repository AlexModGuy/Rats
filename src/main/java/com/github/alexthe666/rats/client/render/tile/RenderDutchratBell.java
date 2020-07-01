package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.client.render.type.RatsRenderType;
import com.github.alexthe666.rats.server.entity.tile.TileEntityDutchratBell;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderDutchratBell extends TileEntityRenderer<TileEntityDutchratBell> {

    private final ModelRenderer modelRenderer = new ModelRenderer(32, 32, 0, 0);
    private static final RenderType TEXTURE = RatsRenderType.getGlowingTranslucent(new ResourceLocation("rats:textures/entity/ratlantis/dutchrat_bell.png"));

    public RenderDutchratBell(TileEntityRendererDispatcher p_i226005_1_) {
        super(p_i226005_1_);
        this.modelRenderer.addBox(-3.0F, -6.0F, -3.0F, 6.0F, 7.0F, 6.0F);
        this.modelRenderer.setRotationPoint(8.0F, 12.0F, 8.0F);
        ModelRenderer modelrenderer = new ModelRenderer(32, 32, 0, 13);
        modelrenderer.addBox(4.0F, 4.0F, 4.0F, 8.0F, 2.0F, 8.0F);
        modelrenderer.setRotationPoint(-8.0F, -12.0F, -8.0F);
        this.modelRenderer.addChild(modelrenderer);
    }

    public void render(TileEntityDutchratBell tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn){
        float f = (float)tileEntityIn.field_213943_a + partialTicks;
        float f1 = 0.0F;
        float f2 = 0.0F;
        if (tileEntityIn.field_213944_b) {
            float f3 = MathHelper.sin(f / (float)Math.PI) / (4.0F + f / 3.0F);
            if (tileEntityIn.field_213945_c == Direction.NORTH) {
                f1 = -f3;
            } else if (tileEntityIn.field_213945_c == Direction.SOUTH) {
                f1 = f3;
            } else if (tileEntityIn.field_213945_c == Direction.EAST) {
                f2 = -f3;
            } else if (tileEntityIn.field_213945_c == Direction.WEST) {
                f2 = f3;
            }
        }

        this.modelRenderer.rotateAngleX = f1;
        this.modelRenderer.rotateAngleZ = f2;
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(TEXTURE);
        this.modelRenderer.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 0.5F);
    }
}