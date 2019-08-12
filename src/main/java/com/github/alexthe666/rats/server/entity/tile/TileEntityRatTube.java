package com.github.alexthe666.rats.server.entity.tile;

import com.github.alexthe666.rats.server.blocks.BlockRatTube;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.pathfinding.AStar;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileEntityRatTube extends TileEntity implements ITickable {

    public EnumFacing opening = null;
    public boolean isNode = false;

    @Override
    public void update() {
        float i = this.getPos().getX() + 0.5F;
        float j = this.getPos().getY() + 0.5F;
        float k = this.getPos().getZ() + 0.5F;
        float d0 = 0.65F;
        for (EntityRat rat : world.getEntitiesWithinAABB(EntityRat.class, new AxisAlignedBB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0))) {
            if(!rat.prevInTube && rat.inTube()){
                rat.setPosition(i, j - 0.25F, k);
            }
            this.updateRat(rat);
        }
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

    private boolean isPathwayValid(BlockPos[] pathway){
        return pathway != null && pathway.length > 0;
    }

    private BlockPos generateTubeTarget(EntityRat rat) {
        for(int i = 0; i < 20; i++){
            BlockPos pos = new BlockPos(rat).add(rat.getRNG().nextInt(30) - 15, rat.getRNG().nextInt(20) - 10, rat.getRNG().nextInt(30) - 15);
            if(rat.world.getTileEntity(pos) instanceof TileEntityRatTube){
                return pos;
            }
        }
        return rat.getPosition();
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("Node", isNode);
        compound.setInteger("OpenSide", opening == null ? -1 : opening.ordinal());
        return super.writeToNBT(compound);
    }

    public void readFromNBT(NBTTagCompound compound) {
        isNode = compound.getBoolean("Node");
        int i = compound.getInteger("OpenSide");
        if(i == -1){
            opening = null;
        }else{
            opening = EnumFacing.values()[MathHelper.clamp(i, 0, EnumFacing.values().length - 1)];
        }
        super.readFromNBT(compound);
    }

    public void setEntranceData(EnumFacing side, boolean open) {
        if(open){
            opening = side;
            isNode = true;
        }else{
            opening = null;
            isNode = false;
        }
    }
}
