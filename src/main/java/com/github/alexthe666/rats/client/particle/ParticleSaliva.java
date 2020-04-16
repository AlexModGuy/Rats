package com.github.alexthe666.rats.client.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class ParticleSaliva extends SpriteTexturedParticle {
    private final Fluid fluid;
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/particle/rat_saliva.png");

    public ParticleSaliva(World p_i49197_1_, double p_i49197_2_, double p_i49197_4_, double p_i49197_6_, Fluid p_i49197_8_) {
        super(p_i49197_1_, p_i49197_2_, p_i49197_4_, p_i49197_6_);
        this.setSize(0.01F, 0.01F);
        this.particleGravity = 0.06F;
        this.fluid = p_i49197_8_;
    }

    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.func_217576_g();
        if (!this.isExpired) {
            this.motionY -= (double)this.particleGravity;
            this.move(this.motionX, this.motionY, this.motionZ);
            this.func_217577_h();
            if (!this.isExpired) {
                this.motionX *= (double)0.98F;
                this.motionY *= (double)0.98F;
                this.motionZ *= (double)0.98F;
                BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
                IFluidState ifluidstate = this.world.getFluidState(blockpos);
                if (ifluidstate.getFluid() == this.fluid && this.posY < (double)((float)blockpos.getY() + ifluidstate.getActualHeight(this.world, blockpos))) {
                    this.setExpired();
                }

            }
        }
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {
        Vec3d inerp = renderInfo.getProjectedView();
        if (age > this.getMaxAge()) {
            this.setExpired();
        }

        Vec3d vec3d = renderInfo.getProjectedView();
        float f = (float)(MathHelper.lerp((double)partialTicks, this.prevPosX, this.posX) - vec3d.getX());
        float f1 = (float)(MathHelper.lerp((double)partialTicks, this.prevPosY, this.posY) - vec3d.getY());
        float f2 = (float)(MathHelper.lerp((double)partialTicks, this.prevPosZ, this.posZ) - vec3d.getZ());
        Quaternion quaternion;
        if (this.particleAngle == 0.0F) {
            quaternion = renderInfo.getRotation();
        } else {
            quaternion = new Quaternion(renderInfo.getRotation());
            float f3 = MathHelper.lerp(partialTicks, this.prevParticleAngle, this.particleAngle);
            quaternion.multiply(Vector3f.ZP.rotation(f3));
        }

        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f1.transform(quaternion);
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f4 = this.getScale(partialTicks);

        for(int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.transform(quaternion);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }

        float f7 = this.getMinU();
        float f8 = this.getMaxU();
        float f5 = this.getMinV();
        float f6 = this.getMaxV();
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
        int j = this.getBrightnessForRender(partialTicks);
        buffer.pos((double)avector3f[0].getX(), (double)avector3f[0].getY(), (double)avector3f[0].getZ()).tex(f8, f6).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
        buffer.pos((double)avector3f[1].getX(), (double)avector3f[1].getY(), (double)avector3f[1].getZ()).tex(f8, f5).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
        buffer.pos((double)avector3f[2].getX(), (double)avector3f[2].getY(), (double)avector3f[2].getZ()).tex(f7, f5).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
        buffer.pos((double)avector3f[3].getX(), (double)avector3f[3].getY(), (double)avector3f[3].getZ()).tex(f7, f6).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();

    }

    protected void func_217576_g() {
        if (this.maxAge-- <= 0) {
            this.setExpired();
        }

    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.CUSTOM;
    }

    protected void func_217577_h() {
    }
}