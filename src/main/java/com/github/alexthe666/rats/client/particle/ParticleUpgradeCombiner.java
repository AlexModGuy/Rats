package com.github.alexthe666.rats.client.particle;

import com.github.alexthe666.rats.client.particle.ParticleRatGhost;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleUpgradeCombiner extends ParticleRedstone {

    public ParticleUpgradeCombiner(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float colorR, float colorG, float colorB) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, colorR, colorG, colorB);
        this.particleAlpha = 0.5F;
        this.particleMaxAge = (int) (7.0D / (Math.random() * 0.8D + 0.2D));
        this.particleScale *= 0.5D;
    }

    public int getBrightnessForRender(float f) {
        int i = super.getBrightnessForRender(f);
        int j = 240;
        int k = i >> 16 & 255;
        return 240 | k << 16;
    }

    public void onUpdate() {
        super.onUpdate();
        this.motionY *= 1.015D;

    }
}