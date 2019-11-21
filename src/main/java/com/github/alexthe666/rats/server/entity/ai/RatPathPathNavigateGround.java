package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.blocks.BlockRatTube;
import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RatPathPathNavigateGround extends PathNavigateGround {

    public BlockPos targetPosition;

    public RatPathPathNavigateGround(EntityRat entitylivingIn, World worldIn) {
        super(entitylivingIn, worldIn);
    }

    protected PathFinder getPathFinder() {
        this.nodeProcessor = new RatWalkNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(true);
        this.nodeProcessor.setCanSwim(true);
        return new RatPathFinder(this.nodeProcessor, (EntityRat) entity);
    }

    public boolean canEntityStandOnPos(BlockPos pos) {
        return this.world.getBlockState(pos.down()).isFullBlock() || this.world.getBlockState(pos).getBlock() instanceof BlockRatTube;
    }

    public Path getPathToPos(BlockPos pos) {
        this.targetPosition = pos;
        if (entity.world.getBlockState(pos) instanceof BlockRatTube) {
            IBlockState state = entity.world.getBlockState(pos);
            for (int i = 0; i < EnumFacing.values().length; i++) {
                PropertyBool bool = BlockRatTube.ALL_OPEN_PROPS[i];
                if (state.getValue(bool) && entity.getHorizontalFacing().getOpposite() != EnumFacing.values()[i]) {
                    return super.getPathToPos(pos.offset(EnumFacing.values()[i]));
                }
            }
        }
        return super.getPathToPos(pos);
    }

    public Path getPathToEntityLiving(Entity entityIn) {
        this.targetPosition = new BlockPos(entityIn);
        return super.getPathToEntityLiving(entityIn);
    }

    public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn) {
        Path path = this.getPathToEntityLiving(entityIn);

        if (path != null) {
            return this.setPath(path, speedIn);
        } else {
            this.targetPosition = new BlockPos(entityIn);
            this.speed = speedIn;
            return true;
        }
    }

    public boolean tryMoveToXYZ(double x, double y, double z, double speedIn) {
        boolean supe = super.tryMoveToXYZ(x, y, z, speedIn);
        this.targetPosition = new BlockPos(x, y, z);
        return supe;
    }

    public void onUpdateNavigation() {
        if(!((EntityRat) entity).isTamed()) {
            if (!this.noPath()) {
                super.onUpdateNavigation();
            } else {
                if (this.targetPosition != null) {
                    double d0 = (double) (this.entity.width * this.entity.width);

                    if (this.entity.getDistanceSqToCenter(this.targetPosition) >= d0 && (this.entity.posY <= (double) this.targetPosition.getY() || this.entity.getDistanceSqToCenter(new BlockPos(this.targetPosition.getX(), MathHelper.floor(this.entity.posY), this.targetPosition.getZ())) >= d0)) {
                        this.entity.getMoveHelper().setMoveTo((double) this.targetPosition.getX(), (double) this.targetPosition.getY(), (double) this.targetPosition.getZ(), this.speed);
                    } else {
                        this.targetPosition = null;
                    }
                }
            }
        }
    }


    public void clearPath() {
        super.clearPath();
    }


}
