package com.github.alexthe666.rats.server.entity.tile;

import com.github.alexthe666.rats.server.blocks.BlockRatTube;
import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.BooleanProperty;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class TileEntityRatTube extends TileEntity implements ITickableTileEntity {

    private static BooleanProperty[] allOpenVars = new BooleanProperty[]{BlockRatTube.OPEN_DOWN, BlockRatTube.OPEN_EAST, BlockRatTube.OPEN_NORTH, BlockRatTube.OPEN_SOUTH, BlockRatTube.OPEN_UP, BlockRatTube.OPEN_WEST};
    public Direction opening = null;
    public boolean isNode = false;
    private int color;

    public TileEntityRatTube() {
        super(RatsTileEntityRegistry.RAT_TUBE);
    }

    @Override
    public void tick() {
        if (isOpen()) {
            float i = this.getPos().getX() + 0.5F;
            float j = this.getPos().getY() + 0.2F;
            float k = this.getPos().getZ() + 0.5F;
            float d0 = 0.4F;
            for (EntityRat rat : world.getEntitiesWithinAABB(EntityRat.class, new AxisAlignedBB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0))) {
                rat.setMotionMultiplier(this.getBlockState(), new Vec3d(1.75F, 1, 1.75F));
                this.updateRat(rat);
            }
        }
    }

    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, -1, this.getUpdateTag());
    }

    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    private void updateRat(EntityRat rat) {

        /*if(!isPathwayValid(rat.tubePathway)){
            rat.currentTubeNode = 0;
            rat.tubePathway = new AStar(new BlockPos(rat), target, 1000).getPath(world);
        }else{
            if(rat.tubePathway[rat.tubePathway.length - 1].distanceSq(rat.getPosX(), rat.getPosY(), rat.getPosZ()) < 2){
                rat.currentTubeNode = 0;
                rat.tubePathway = new AStar(new BlockPos(rat), target, 1000).getPath(world);
            }
            int max = rat.tubePathway.length - 1;
            if(rat.currentTubeNode < max && rat.getDistanceSq(rat.tubePathway[MathHelper.clamp(rat.currentTubeNode, 0, max)]) < 2){
                rat.currentTubeNode++;
            }
            if(rat.tubePathway != null && max > 0){
                BlockPos currentNode = rat.tubePathway[MathHelper.clamp(rat.currentTubeNode, 0, max)];
                rat.moveController.setMoveTo(currentNode.getX() + 0.5D, currentNode.getY() + 0.25D, currentNode.getZ() + 0.5D, 1.0D);
            }
        }*/
    }

    private boolean isOpen() {
        BlockState state = world.getBlockState(this.getPos());
        if (state.getBlock() instanceof BlockRatTube) {
            for (BooleanProperty opened : allOpenVars) {
                if (state.get(opened)) {
                    return true;
                }
            }
        }
        return false;
    }

    public CompoundNBT write(CompoundNBT compound) {
        compound.putBoolean("Node", isNode);
        compound.putInt("OpenSide", opening == null ? -1 : opening.ordinal());
        compound.putInt("TubeColor", color);
        return super.write(compound);
    }

    public void read(CompoundNBT compound) {
        isNode = compound.getBoolean("Node");
        int i = compound.getInt("OpenSide");
        if (i == -1) {
            opening = null;
        } else {
            opening = Direction.values()[MathHelper.clamp(i, 0, Direction.values().length - 1)];
        }
        color = compound.getInt("TubeColor");
        super.read(compound);
    }

    public void setEntranceData(Direction side, boolean open) {
        if (open) {
            opening = side;
            isNode = true;
        } else {
            opening = null;
            isNode = false;
        }
    }

    public int getColor(){
        return color;
    }

    public void setColor(int colorIn){
        this.color = colorIn;
    }
}
