package com.github.alexthe666.rats.client.particle;

import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ParticleRatGhost extends SpriteTexturedParticle {

    public ParticleRatGhost(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float colorR, float colorG, float colorB) {
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
        this.motionY *= 1.015D;

    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.CUSTOM;
    }
}