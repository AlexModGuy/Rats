package com.github.alexthe666.rats.server.entity.tile;

import com.github.alexthe666.rats.server.blocks.BlockRatAttractor;
import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class TileEntityRatAttractor extends TileEntity implements ITickableTileEntity {
    public int ticksExisted = 0;

    public TileEntityRatAttractor() {
        super(RatsTileEntityRegistry.RAT_ATTRACTOR);
    }

    @Override
    public void tick() {
        boolean active = false;
        ticksExisted++;
        if(getBlockState().getBlock() instanceof BlockRatAttractor){
            ((BlockRatAttractor)getBlockState().getBlock()).updateState(getBlockState(), world, pos, getBlockState().getBlock());
            active = getBlockState().get(BlockRatAttractor.POWERED);
        }

        if(active){
            float i = this.getPos().getX() + 0.5F;
            float j = this.getPos().getY();
            float k = this.getPos().getZ() + 0.5F;
            if(world.isRemote){
                float f1 = 0.6F + 0.4F;
                float f2 = Math.max(0.0F, 0.7F - 0.5F);
                float f3 = Math.max(0.0F, 0.6F - 0.7F);
                world.addParticle(RedstoneParticleData.REDSTONE_DUST, i + (world.rand.nextDouble() - 0.5D) * 0.65D, j + 0.15D + (world.rand.nextDouble() - 0.5D), k + (world.rand.nextDouble() - 0.5D) * 0.65D, f1, f2, f3);
            }else{
                double d0 = 15;
                if(ticksExisted % 20 == 0 && world.rand.nextInt(3) == 0){
                    List<EntityRat> rats = world.getEntitiesWithinAABB(EntityRat.class, new AxisAlignedBB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0));
                    for (EntityRat rat : rats) {
                        if(!rat.isTamed() && rat.getAttackTarget() == null){
                            rat.getNavigator().tryMoveToXYZ(i, j, k, 1.0D);
                        }
                    }
                }
            }
        }

    }
}
