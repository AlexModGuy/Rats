package com.github.alexthe666.rats.client.particle;

import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ParticleSaliva  extends SpriteTexturedParticle {
    private final Fluid fluid;

    public ParticleSaliva(World p_i49197_1_, double p_i49197_2_, double p_i49197_4_, double p_i49197_6_, Fluid p_i49197_8_) {
        super(p_i49197_1_, p_i49197_2_, p_i49197_4_, p_i49197_6_);
        this.setSize(0.01F, 0.01F);
        this.particleGravity = 0.06F;
        this.fluid = p_i49197_8_;
    }

    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public int getBrightnessForRender(float partialTick) {
        return this.fluid.isIn(FluidTags.LAVA) ? 240 : super.getBrightnessForRender(partialTick);
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
                if (ifluidstate.getFluid() == this.fluid && this.posY < (double)((float)blockpos.getY() + ifluidstate.func_215679_a(this.world, blockpos))) {
                    this.setExpired();
                }

            }
        }
    }

    protected void func_217576_g() {
        if (this.maxAge-- <= 0) {
            this.setExpired();
        }

    }

    protected void func_217577_h() {
    }
}