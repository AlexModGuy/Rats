package com.github.alexthe666.rats.client.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class ParticlePiratGhost extends SpriteTexturedParticle {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/particle/pirat_ghost.png");

    public ParticlePiratGhost(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float colorR, float colorG, float colorB) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, colorR, colorG, colorB);
        this.particleAlpha = 1F;
        this.maxAge = (int) (16.0D / (Math.random() * 0.8D + 0.2D));
    }

    public int getBrightnessForRender(float f) {
        int i = super.getBrightnessForRender(f);
        int j = 240;
        int k = i >> 16 & 255;
        return 240 | k << 16;
    }

    public void tick() {
        super.tick();
        this.motionY *= 0.0D;

    }

    public void renderParticle(BufferBuilder buffer, ActiveRenderInfo entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        if (age > this.getMaxAge()) {
            this.setExpired();
        }
        float f3 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - interpPosX);
        float f4 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - interpPosY);
        float f5 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - interpPosZ);
        float width = particleScale * 1;
        int i = this.getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        Vec3d[] avec3d = new Vec3d[]{new Vec3d((double) (-rotationX * width - rotationXY * width), (double) (-rotationZ * width), (double) (-rotationYZ * width - rotationXZ * width)), new Vec3d((double) (-rotationX * width + rotationXY * width), (double) (rotationZ * width), (double) (-rotationYZ * width + rotationXZ * width)), new Vec3d((double) (rotationX * width + rotationXY * width), (double) (rotationZ * width), (double) (rotationYZ * width + rotationXZ * width)), new Vec3d((double) (rotationX * width - rotationXY * width), (double) (-rotationZ * width), (double) (rotationYZ * width - rotationXZ * width))};
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.depthMask(true);
        float f8 = (float) Math.PI / 2 + this.particleAngle + (this.particleAngle - this.prevParticleAngle) * partialTicks;
        float f9 = MathHelper.cos(f8 * 0.5F);
        float f10 = MathHelper.sin(f8 * 0.5F) * (float) entityIn.getLookDirection().x;
        float f11 = MathHelper.sin(f8 * 0.5F) * (float) entityIn.getLookDirection().y;
        float f12 = MathHelper.sin(f8 * 0.5F) * (float) entityIn.getLookDirection().z;
        Vec3d vec3d = new Vec3d((double) f10, (double) f11, (double) f12);

        for (int l = 0; l < 4; ++l) {
            avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale((double) (f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale((double) (2.0F * f9)));
        }
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
        GlStateManager.disableLighting();
        double currentMinU = 0.25D;
        double currentMaxU = currentMinU + 0.25D;
        double currentMinV = 0.25D;
        double currentMaxV = currentMinV + 0.25D;
        float alpha = particleAlpha;
        GL11.glPushMatrix();
        buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
        buffer.pos((double) f3 + avec3d[0].x, (double) f4 + avec3d[0].y, (double) f5 + avec3d[0].z).tex(0, 1).color(1, 1, 1, alpha).lightmap(j, k).endVertex();
        buffer.pos((double) f3 + avec3d[1].x, (double) f4 + avec3d[1].y, (double) f5 + avec3d[1].z).tex(1, 1).color(1, 1, 1, alpha).lightmap(j, k).endVertex();
        buffer.pos((double) f3 + avec3d[2].x, (double) f4 + avec3d[2].y, (double) f5 + avec3d[2].z).tex(1, 0).color(1, 1, 1, alpha).lightmap(j, k).endVertex();
        buffer.pos((double) f3 + avec3d[3].x, (double) f4 + avec3d[3].y, (double) f5 + avec3d[3].z).tex(0, 0).color(1, 1, 1, alpha).lightmap(j, k).endVertex();
        Tessellator.getInstance().draw();
        GL11.glPopMatrix();
        GlStateManager.disableBlend();
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.CUSTOM;
    }
}