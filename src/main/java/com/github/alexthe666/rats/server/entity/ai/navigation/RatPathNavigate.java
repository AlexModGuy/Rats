package com.github.alexthe666.rats.server.entity.ai.navigation;

import com.github.alexthe666.rats.server.blocks.BlockRatTube;
import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class RatPathNavigate extends GroundPathNavigator {

    public BlockPos targetPosition;

    public RatPathNavigate(MobEntity LivingEntityIn, World worldIn) {
        super(LivingEntityIn, worldIn);
    }


    protected PathFinder getPathFinder(int p_179679_1_) {
        this.nodeProcessor = new RatWalkNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(true);
        this.nodeProcessor.setCanSwim(true);
        return new PathFinder(this.nodeProcessor, p_179679_1_);
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

    public Path getPathToEntity(Entity entityIn, int i) {
        this.targetPosition = new BlockPos(entityIn.getPositionVec());
        return super.getPathToEntity(entityIn, i);
    }

    public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn) {
        Path path = this.getPathToEntity(entityIn, 0);

        if (path != null) {
            return this.setPath(path, speedIn);
        } else {
            this.targetPosition = new BlockPos(entityIn.getPositionVec());
            this.speed = speedIn;
            return true;
        }
    }

    public void clearPath() {
        super.clearPath();
    }

    public void tick() {
        ++this.totalTicks;
        ((EntityRat) this.entity).setTubeTarget(this.targetPosition);

        if (this.tryUpdatePath) {
            this.updatePath();
        }
        if (!this.noPath()) {
            if (this.canNavigate()) {
                this.pathFollow();
            } else if (this.currentPath != null && this.currentPath.getCurrentPathIndex() < this.currentPath.getCurrentPathLength()) {
                Vector3d vec3d = this.getEntityPosition();
                Vector3d vec3d1 = this.currentPath.getVectorFromIndex(this.entity, this.currentPath.getCurrentPathIndex());

                if (vec3d.y > vec3d1.y && !this.entity.func_233570_aj_()&& MathHelper.floor(vec3d.x) == MathHelper.floor(vec3d1.x) && MathHelper.floor(vec3d.z) == MathHelper.floor(vec3d1.z)) {
                    this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
                }
            }
            this.world.getProfiler().endSection();
            if (!this.noPath()) {
                Vector3d vec3d2 = this.currentPath.getPosition(this.entity);
                this.entity.getMoveHelper().setMoveTo(vec3d2.x, vec3d2.y, vec3d2.z, this.speed);

            }
        } else if (targetPosition != null) {
            double d0 = 1;
            if (this.entity.getDistanceSq(this.targetPosition.getX(), this.targetPosition.getY(), this.targetPosition.getZ()) >= d0 && (this.entity.getPosY() <= (double) this.targetPosition.getY() || this.entity.getDistanceSq(this.targetPosition.getX(), MathHelper.floor(this.entity.getPosY()), this.targetPosition.getZ()) >= d0)) {
                this.entity.getMoveHelper().setMoveTo((double) this.targetPosition.getX(), (double) this.targetPosition.getY(), (double) this.targetPosition.getZ(), this.speed);
            } else {
                this.targetPosition = null;
            }
        }
    }

    public boolean canEntityStandOnPos(BlockPos pos) {
        if (this.world.getBlockState(pos).getBlock() instanceof BlockRatTube) {
            BlockState state = this.world.getBlockState(pos);
            return true;
        }
        return this.world.getBlockState(pos.down()).isSolid();
    }
}
