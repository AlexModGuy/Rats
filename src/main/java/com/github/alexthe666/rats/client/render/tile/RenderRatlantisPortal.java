package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.server.entity.tile.TileEntityRatlantisPortal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.Sys;

import java.nio.FloatBuffer;
import java.util.Random;

public class RenderRatlantisPortal extends TileEntitySpecialRenderer<TileEntityRatlantisPortal> {
    private static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation("rats:textures/environment/ratlantis_sky_portal.png");
    private static final ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation("rats:textures/environment/ratlantis_portal.png");
    private static final Random RANDOM = new Random(31100L);
    private static final FloatBuffer MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer buffer = GLAllocation.createDirectFloatBuffer(16);

    public void render(TileEntityRatlantisPortal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        RANDOM.setSeed(31100L);
        GlStateManager.getFloat(2982, MODELVIEW);
        GlStateManager.getFloat(2983, PROJECTION);
        double d0 = x * x + y * y + z * z;
        int i = this.getPasses(d0);
        float f = this.getOffset();
        boolean flag = false;

        for (int j = 0; j < i; ++j) {
            GlStateManager.pushMatrix();
            float f1 = 2.0F / (float) (18 - j);

            if (j == 0) {
                Minecraft.getMinecraft().getTextureManager().bindTexture(END_SKY_TEXTURE);
                f1 = 0.15F;
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            }

            if (j >= 1) {
                Minecraft.getMinecraft().getTextureManager().bindTexture(END_PORTAL_TEXTURE);
                flag = true;
                Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            }

            if (j == 1) {
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            }

            GlStateManager.texGen(GlStateManager.TexGen.S, 9216);
            GlStateManager.texGen(GlStateManager.TexGen.T, 9216);
            GlStateManager.texGen(GlStateManager.TexGen.R, 9216);
            GlStateManager.texGen(GlStateManager.TexGen.S, 9474, this.getBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GlStateManager.texGen(GlStateManager.TexGen.T, 9474, this.getBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GlStateManager.texGen(GlStateManager.TexGen.R, 9474, this.getBuffer(0.0F, 0.0F, 1.0F, 0.0F));
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.5F, 0.5F, 0.0F);
            GlStateManager.scale(0.5F, 0.5F, 1.0F);
            float f2 = (float) (j + 1);
            float time = (Minecraft.getMinecraft().player.ticksExisted - 1 + (1 * partialTicks)) % 800.0F / 800.0F;
            if (te == null) {
                time = (Minecraft.getMinecraft().frameTimer.getIndex() - 1 + (1 * partialTicks)) % 3200F / 3200F;
            }
            GlStateManager.translate(17.0F / f2 * time * RANDOM.nextGaussian(), (2.0F + f2 / 1.5F) * time, 0.0F);
            GlStateManager.rotate(180, 0.0F, 0.0F, 1.0F);
            float scal = 4.5F - f2 / 4.0F;
            GlStateManager.scale(scal, scal, 1.0F);
            GlStateManager.multMatrix(PROJECTION);
            GlStateManager.multMatrix(MODELVIEW);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
            float f3 = 1;//(RANDOM.nextFloat() * 0.5F + 0.1F) * f1;
            float f4 = 0.98F;//(RANDOM.nextFloat() * 0.5F + 0.4F) * f1;
            float f5 = 0.79F;//(RANDOM.nextFloat() * 0.5F + 0.5F) * f1;
            float alpha2 = 0.7F;
            if (te == null || te.shouldRenderFace(EnumFacing.SOUTH)) {
                bufferbuilder.pos(x, y, z + 1.0D).color(f3, f4, f5, alpha2).endVertex();
                bufferbuilder.pos(x + 1.0D, y, z + 1.0D).color(f3, f4, f5, alpha2).endVertex();
                bufferbuilder.pos(x + 1.0D, y + 1.0D, z + 1.0D).color(f3, f4, f5, alpha2).endVertex();
                bufferbuilder.pos(x, y + 1.0D, z + 1.0D).color(f3, f4, f5, alpha2).endVertex();
            }

            if (te == null || te.shouldRenderFace(EnumFacing.NORTH)) {
                bufferbuilder.pos(x, y + 1.0D, z).color(f3, f4, f5, alpha2).endVertex();
                bufferbuilder.pos(x + 1.0D, y + 1.0D, z).color(f3, f4, f5, alpha2).endVertex();
                bufferbuilder.pos(x + 1.0D, y, z).color(f3, f4, f5, alpha2).endVertex();
                bufferbuilder.pos(x, y, z).color(f3, f4, f5, alpha2).endVertex();
            }

            if (te == null || te.shouldRenderFace(EnumFacing.EAST)) {
                bufferbuilder.pos(x + 1.0D, y + 1.0D, z).color(f3, f4, f5, alpha2).endVertex();
                bufferbuilder.pos(x + 1.0D, y + 1.0D, z + 1.0D).color(f3, f4, f5, alpha2).endVertex();
                bufferbuilder.pos(x + 1.0D, y, z + 1.0D).color(f3, f4, f5, alpha2).endVertex();
                bufferbuilder.pos(x + 1.0D, y, z).color(f3, f4, f5, alpha2).endVertex();
            }

            if (te == null || te.shouldRenderFace(EnumFacing.WEST)) {
                bufferbuilder.pos(x, y, z).color(f3, f4, f5, alpha2).endVertex();
                bufferbuilder.pos(x, y, z + 1.0D).color(f3, f4, f5, alpha2).endVertex();
                bufferbuilder.pos(x, y + 1.0D, z + 1.0D).color(f3, f4, f5, alpha2).endVertex();
                bufferbuilder.pos(x, y + 1.0D, z).color(f3, f4, f5, alpha2).endVertex();
            }

            if (te == null || te.shouldRenderFace(EnumFacing.DOWN)) {
                bufferbuilder.pos(x, y, z).color(f3, f4, f5, alpha2).endVertex();
                bufferbuilder.pos(x + 1.0D, y, z).color(f3, f4, f5, alpha2).endVertex();
                bufferbuilder.pos(x + 1.0D, y, z + 1.0D).color(f3, f4, f5, alpha2).endVertex();
                bufferbuilder.pos(x, y, z + 1.0D).color(f3, f4, f5, alpha2).endVertex();
            }

            if (te == null || te.shouldRenderFace(EnumFacing.UP)) {
                bufferbuilder.pos(x, y + (double) f, z + 1.0D).color(f3, f4, f5, alpha2).endVertex();
                bufferbuilder.pos(x + 1.0D, y + (double) f, z + 1.0D).color(f3, f4, f5, alpha2).endVertex();
                bufferbuilder.pos(x + 1.0D, y + (double) f, z).color(f3, f4, f5, alpha2).endVertex();
                bufferbuilder.pos(x, y + (double) f, z).color(f3, f4, f5, alpha2).endVertex();
            }

            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
            Minecraft.getMinecraft().getTextureManager().bindTexture(END_SKY_TEXTURE);

        }
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        GlStateManager.disableBlend();
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        if (flag) {
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        }
        GlStateManager.popMatrix();
    }

    protected int getPasses(double p_191286_1_) {
        int i;
        if (p_191286_1_ > 36864.0D) {
            i = 1;
        } else if (p_191286_1_ > 25600.0D) {
            i = 3;
        } else if (p_191286_1_ > 16384.0D) {
            i = 5;
        } else if (p_191286_1_ > 9216.0D) {
            i = 7;
        } else if (p_191286_1_ > 4096.0D) {
            i = 9;
        } else if (p_191286_1_ > 1024.0D) {
            i = 11;
        } else if (p_191286_1_ > 576.0D) {
            i = 13;
        } else if (p_191286_1_ > 256.0D) {
            i = 14;
        } else {
            i = 15;
        }

        return i;
    }

    protected float getOffset() {
        return 1.0F;
    }

    private FloatBuffer getBuffer(float p_147525_1_, float p_147525_2_, float p_147525_3_, float p_147525_4_) {
        this.buffer.clear();
        this.buffer.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
        this.buffer.flip();
        return this.buffer;
    }
}
