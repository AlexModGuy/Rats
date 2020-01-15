package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.blocks.BlockRatTube;
import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class RatPathPathNavigateGround extends GroundPathNavigator {

    public BlockPos targetPosition;

    public RatPathPathNavigateGround(EntityRat LivingEntityIn, World worldIn) {
        super(LivingEntityIn, worldIn);
    }

    protected PathFinder getPathFinder(int p_179679_1_) {
        this.nodeProcessor = new RatWalkNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(true);
        this.nodeProcessor.setCanSwim(true);
        return new PathFinder(this.nodeProcessor, p_179679_1_);
    }

    public boolean canEntityStandOnPos(BlockPos pos) {
        return this.world.getBlockState(pos.down()).isSolid() || this.world.getBlockState(pos).getBlock() instanceof BlockRatTube;
    }

    public Path getPathToPos(BlockPos pos, int idk) {
        this.targetPosition = pos;
        if (entity.world.getBlockState(pos).getBlock() instanceof BlockRatTube) {
            BlockState state = entity.world.getBlockState(pos);
            for (int i = 0; i < Direction.values().length; i++) {
                BooleanProperty bool = BlockRatTube.ALL_OPEN_PROPS[i];
                if (state.get(bool) && entity.getHorizontalFacing().getOpposite() != Direction.values()[i]) {
                    return super.getPathToPos(pos.offset(Direction.values()[i]), idk);
                }
            }
        }
        return super.getPathToPos(pos, idk);
    }

    public Path getPathToEntityLiving(Entity entityIn, int idk) {
        this.targetPosition = new BlockPos(entityIn);
        return super.getPathToEntityLiving(entityIn, idk);
    }

    public boolean tryMoveToLivingEntity(Entity entityIn, double speedIn) {
        Path path = this.getPathToEntityLiving(entityIn, 0);

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

    public void tick() {
        if (!((EntityRat) entity).isTamed()) {
            if (!this.noPath()) {
                super.tick();
            } else {
                if (this.targetPosition != null) {
                    double d0 = 1.0F;

                    if (this.entity.getDistanceSq(this.targetPosition.getX(), this.targetPosition.getY(), this.targetPosition.getZ()) >= d0 && (this.entity.posY <= (double) this.targetPosition.getY() || this.entity.getDistanceSq(this.targetPosition.getX(), MathHelper.floor(this.entity.posY), this.targetPosition.getZ()) >= d0)) {
                        this.entity.getMoveHelper().setMoveTo((double) this.targetPosition.getX(), (double) this.targetPosition.getY(), (double) this.targetPosition.getZ(), this.speed);
                    } else {
                        this.targetPosition = null;
                    }
                }
            }
        } else {
            super.tick();
        }
    }


    public void clearPath() {
        super.clearPath();
    }


}
