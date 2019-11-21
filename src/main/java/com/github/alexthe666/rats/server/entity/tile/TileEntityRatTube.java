package com.github.alexthe666.rats.server.entity.tile;

import com.github.alexthe666.rats.server.blocks.BlockRatTube;
import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class TileEntityRatTube extends TileEntity implements ITickable {

    private static PropertyBool[] allOpenVars = new PropertyBool[]{BlockRatTube.OPEN_DOWN, BlockRatTube.OPEN_EAST, BlockRatTube.OPEN_NORTH, BlockRatTube.OPEN_SOUTH, BlockRatTube.OPEN_UP, BlockRatTube.OPEN_WEST};
    public EnumFacing opening = null;
    public boolean isNode = false;

    @Override
    public void update() {
        if (isOpen()) {
            float i = this.getPos().getX() + 0.5F;
            float j = this.getPos().getY() + 0.2F;
            float k = this.getPos().getZ() + 0.5F;
            float d0 = 0.4F;
            for (EntityRat rat : world.getEntitiesWithinAABB(EntityRat.class, new AxisAlignedBB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0))) {
                rat.motionX *= 1.75F;
                rat.motionZ *= 1.75F;
                this.updateRat(rat);
            }
        }
    }

    private Vec3d offsetTubePos(){
        if(this.getWorld().getBlockState(this.pos).getBlock() instanceof BlockRatTube) {
            IBlockState actualState = this.getBlockType().getActualState(this.getWorld().getBlockState(this.pos), this.getWorld(), this.pos);
            BlockPos pos = new BlockPos(0, 0, 0);
            if (actualState.getValue(BlockRatTube.UP)) {
                pos = pos.offset(EnumFacing.UP);
            }
            if (actualState.getValue(BlockRatTube.DOWN)) {
                pos = pos.offset(EnumFacing.DOWN);
            }
            if (actualState.getValue(BlockRatTube.EAST)) {
                pos = pos.offset(EnumFacing.EAST);
            }
            if (actualState.getValue(BlockRatTube.WEST)) {
                pos = pos.offset(EnumFacing.WEST);
            }
            if (actualState.getValue(BlockRatTube.NORTH)) {
                pos = pos.offset(EnumFacing.NORTH);
            }
            if (actualState.getValue(BlockRatTube.SOUTH)) {
                pos = pos.offset(EnumFacing.SOUTH);
            }
            return new Vec3d(pos.getX() * 0.25D, pos.getY() * 0.25D, pos.getZ() * 0.25D);
        }
        return Vec3d.ZERO;
    }

    private void updateRat(EntityRat rat) {

        /*if(!isPathwayValid(rat.tubePathway)){
            rat.currentTubeNode = 0;
            rat.tubePathway = new AStar(new BlockPos(rat), target, 1000).getPath(world);
        }else{
            if(rat.tubePathway[rat.tubePathway.length - 1].distanceSq(rat.posX, rat.posY, rat.posZ) < 2){
                rat.currentTubeNode = 0;
                rat.tubePathway = new AStar(new BlockPos(rat), target, 1000).getPath(world);
            }
            int max = rat.tubePathway.length - 1;
            if(rat.currentTubeNode < max && rat.getDistanceSq(rat.tubePathway[MathHelper.clamp(rat.currentTubeNode, 0, max)]) < 2){
                rat.currentTubeNode++;
            }
            if(rat.tubePathway != null && max > 0){
                BlockPos currentNode = rat.tubePathway[MathHelper.clamp(rat.currentTubeNode, 0, max)];
                rat.getMoveHelper().setMoveTo(currentNode.getX() + 0.5D, currentNode.getY() + 0.25D, currentNode.getZ() + 0.5D, 1.0D);
            }
        }*/
    }

    private boolean isOpen() {
        IBlockState state = world.getBlockState(this.getPos());
        if (state.getBlock() instanceof BlockRatTube) {
            for (PropertyBool opened : allOpenVars) {
                if (state.getValue(opened)) {
                    return true;
                }
            }
        }
        return false;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("Node", isNode);
        compound.setInteger("OpenSide", opening == null ? -1 : opening.ordinal());
        return super.writeToNBT(compound);
    }

    public void readFromNBT(NBTTagCompound compound) {
        isNode = compound.getBoolean("Node");
        int i = compound.getInteger("OpenSide");
        if (i == -1) {
            opening = null;
        } else {
            opening = EnumFacing.values()[MathHelper.clamp(i, 0, EnumFacing.values().length - 1)];
        }
        super.readFromNBT(compound);
    }

    public void setEntranceData(EnumFacing side, boolean open) {
        if (open) {
            opening = side;
            isNode = true;
        } else {
            opening = null;
            isNode = false;
        }
    }
}
