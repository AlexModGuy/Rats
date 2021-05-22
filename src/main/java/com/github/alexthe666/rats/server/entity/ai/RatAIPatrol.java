package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.ai.goal.FleeSunGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;

public class RatAIPatrol extends Goal {
    private EntityRat rat;
    private BlockPos nextNode;
    private int nodeIndex = 0;


    public RatAIPatrol(EntityRat rat) {
        super();
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        this.rat = rat;
    }

    public boolean shouldExecute() {
        return rat.isTamed() && rat.isPatrolCommand() && !rat.patrolNodes.isEmpty() && (rat.getAttackTarget() == null || !rat.getAttackTarget().isAlive());
    }

    public void tick() {
        if(nextNode == null){
            if(nodeIndex >= 0 && nodeIndex < rat.patrolNodes.size()){
                nextNode = rat.patrolNodes.get(nodeIndex);
            }
        }
        if(this.rat.getDistanceSq(Vector3d.copyCentered(nextNode)) <= 2.5F * rat.getRatDistanceModifier()){
            nodeIndex++;
            nextNode = null;
            if(nodeIndex > rat.patrolNodes.size() - 1){
                nodeIndex = 0;
            }
            try{//just in case thread is separated
                nextNode = rat.patrolNodes.get(nodeIndex);
            }catch (Exception e){
                nodeIndex = 0;
            }
        }else if(nextNode != null){
            this.rat.getNavigator().tryMoveToXYZ((double) ((float) this.nextNode.getX()) + 0.5D, (double) (this.nextNode.getY() + 1), (double) ((float) this.nextNode.getZ()) + 0.5D, 1.0F);
        }
    }
}

