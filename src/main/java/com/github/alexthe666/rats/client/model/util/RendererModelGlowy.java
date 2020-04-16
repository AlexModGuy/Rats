package com.github.alexthe666.rats.client.model.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class RendererModelGlowy extends ModelRenderer {

    public RendererModelGlowy(Model model) {
        super(model);
    }

    public RendererModelGlowy(Model model, int texOffX, int texOffY) {
        super(model, texOffX, texOffY);
    }

    public RendererModelGlowy(int textureWidthIn, int textureHeightIn, int textureOffsetXIn, int textureOffsetYIn) {
        super(textureWidthIn, textureHeightIn, textureOffsetXIn, textureOffsetYIn);
    }

    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn) {
        super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);

    }
}
