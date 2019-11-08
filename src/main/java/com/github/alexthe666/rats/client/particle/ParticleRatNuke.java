package com.github.alexthe666.rats.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class ParticleRatNuke extends Particle {

    private static final ResourceLocation TEXTURE_0 = new ResourceLocation("rats:textures/particle/nuke_0.png");
    private static final ResourceLocation TEXTURE_1 = new ResourceLocation("rats:textures/particle/nuke_1.png");
    private static final ResourceLocation TEXTURE_2 = new ResourceLocation("rats:textures/particle/nuke_2.png");
    private static final ResourceLocation TEXTURE_3 = new ResourceLocation("rats:textures/particle/nuke_3.png");
    private static final ResourceLocation TEXTURE_4 = new ResourceLocation("rats:textures/particle/nuke_4.png");
    private static final ResourceLocation TEXTURE_5 = new ResourceLocation("rats:textures/particle/nuke_5.png");
    private static final ResourceLocation TEXTURE_6 = new ResourceLocation("rats:textures/particle/nuke_6.png");
    private static final ResourceLocation TEXTURE_7 = new ResourceLocation("rats:textures/particle/nuke_7.png");
    private static final ResourceLocation TEXTURE_8 = new ResourceLocation("rats:textures/particle/nuke_8.png");
    private boolean type;

    public ParticleRatNuke(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float colorR, float colorG, float colorB) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, colorR, colorG, colorB);
        this.particleMaxAge = (int) (6.0D / (Math.random() * 0.8D + 0.2D));
        type = worldIn.rand.nextBoolean();
    }

    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        if (particleAge > 25) {
            this.setExpired();
        }
        particleScale = 0.05F * (this.particleMaxAge);
        particleAlpha = 0.125F * (this.particleMaxAge - this.particleAge);
        float f3 = (float) (this.posX - interpPosX);
        float f4 = (float) (this.posY - interpPosY);
        float f5 = (float) (this.posZ - interpPosZ);
        float width = particleScale * 0.09F;
        int i = this.getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        Vec3d[] avec3d = new Vec3d[]{new Vec3d((double) (-rotationX * width - rotationXY * width), (double) (-rotationZ * width), (double) (-rotationYZ * width - rotationXZ * width)), new Vec3d((double) (-rotationX * width + rotationXY * width), (double) (rotationZ * width), (double) (-rotationYZ * width + rotationXZ * width)), new Vec3d((double) (rotationX * width + rotationXY * width), (double) (rotationZ * width), (double) (rotationYZ * width + rotationXZ * width)), new Vec3d((double) (rotationX * width - rotationXY * width), (double) (-rotationZ * width), (double) (rotationYZ * width - rotationXZ * width))};
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.depthMask(false);
        float f8 = (float) Math.PI / 2 + this.particleAngle + (this.particleAngle - this.prevParticleAngle) * partialTicks;
        float f9 = MathHelper.cos(f8 * 0.5F);
        float f10 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.x;
        float f11 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.y;
        float f12 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.z;
        Vec3d vec3d = new Vec3d((double) f10, (double) f11, (double) f12);

        for (int l = 0; l < 4; ++l) {
            avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale((double) (f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale((double) (2.0F * f9)));
        }
        if (type) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_0);
        } else {
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_1);
        }
        GlStateManager.disableLighting();
        double currentMinU = 0.25D * particleTextureIndexX;
        double currentMaxU = currentMinU + 0.25D;
        double currentMinV = 0.25D * particleTextureIndexY;
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

    public void setParticleTextureIndex(int particleTextureIndex) {
    }

    public int getBrightnessForRender(float partialTick) {
        return 240;
    }

    public int getFXLayer() {
        return 3;
    }

    public void onUpdate() {
        super.onUpdate();
        this.motionY *= 1.015D;

    }
}