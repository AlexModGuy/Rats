package com.github.alexthe666.rats.server.entity.tile;

import com.github.alexthe666.rats.server.blocks.RatlantisBlockRegistry;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityToken extends TileEntity implements ITickableTileEntity {

    public int ticksExisted;
    public float ratRotation;
    public float ratRotationPrev;
    public float tRot;

    public TileEntityToken() {
        super(RatlantisTileEntityRegistry.TOKEN);
    }

    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("TicksExisted", ticksExisted);
        return super.write(compound);
    }

    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        ticksExisted = compound.getInt("TicksExisted");
    }

    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.add(-3, -4, -3), pos.add(3, 4, 3));
    }

    @Override
    public void tick() {
        ticksExisted++;
        this.ratRotationPrev = this.ratRotation;
        PlayerEntity PlayerEntity = this.world.getClosestPlayer((float) this.pos.getX() + 0.5F, (float) this.pos.getY() + 0.5F, (float) this.pos.getZ() + 0.5F, 10.0D, false);
        if (PlayerEntity != null) {
            double d0 = PlayerEntity.getPosX() - (double) ((float) this.pos.getX() + 0.5F);
            double d1 = PlayerEntity.getPosZ() - (double) ((float) this.pos.getZ() + 0.5F);
            this.tRot = (float) MathHelper.atan2(d1, d0);
        } else {
            this.tRot += 0.04F;
        }

        while (this.ratRotation >= (float) Math.PI) {
            this.ratRotation -= ((float) Math.PI * 2F);
        }

        while (this.ratRotation < -(float) Math.PI) {
            this.ratRotation += ((float) Math.PI * 2F);
        }
        float f2;

        for (f2 = this.tRot - this.ratRotation; f2 >= (float) Math.PI; f2 -= ((float) Math.PI * 2F)) {
        }
        while (f2 < -(float) Math.PI) {
            f2 += ((float) Math.PI * 2F);
        }

        this.ratRotation += f2 * 0.4F;

        if (ticksExisted >= 592) {
            BlockPos pos = this.getPos();
            world.createExplosion(null, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ(), 3, Explosion.Mode.NONE);
            world.setBlockState(pos.up(3), RatsBlockRegistry.MARBLED_CHEESE_RAW.getDefaultState());
            world.setBlockState(pos.up(2), RatlantisBlockRegistry.RATLANTIS_PORTAL.getDefaultState());
            world.setBlockState(pos.up(1), RatlantisBlockRegistry.RATLANTIS_PORTAL.getDefaultState());
            world.setBlockState(pos, RatsBlockRegistry.MARBLED_CHEESE_RAW.getDefaultState());
        }

    }
}
