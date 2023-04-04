package com.github.alexthe666.rats.server.block.entity;

import com.github.alexthe666.rats.registry.RatsBlockEntityRegistry;
import com.github.alexthe666.rats.server.block.RatTubeBlock;
import com.github.alexthe666.rats.server.entity.rat.Rat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class RatTubeBlockEntity extends BlockEntity {

	private static final BooleanProperty[] allOpenVars = new BooleanProperty[]{RatTubeBlock.OPEN_DOWN, RatTubeBlock.OPEN_EAST, RatTubeBlock.OPEN_NORTH, RatTubeBlock.OPEN_SOUTH, RatTubeBlock.OPEN_UP, RatTubeBlock.OPEN_WEST};
	public Direction opening = null;
	public boolean isNode = false;
	private int color;

	public RatTubeBlockEntity(BlockPos pos, BlockState state) {
		super(RatsBlockEntityRegistry.RAT_TUBE.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, RatTubeBlockEntity te) {
		if (te.isOpen()) {
			float i = pos.getX() + 0.5F;
			float j = pos.getY() + 0.2F;
			float k = pos.getZ() + 0.5F;
			float d0 = 0.4F;
			for (Rat rat : level.getEntitiesOfClass(Rat.class, new AABB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0))) {
				rat.makeStuckInBlock(state, new Vec3(1.75F, 1, 1.75F));
				te.updateRat(rat);
			}
		}
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this, BlockEntity::getUpdateTag);
	}

	public CompoundTag getUpdateTag() {
		return this.saveWithId();
	}

	private void updateRat(Rat rat) {

        /*if(!isPathwayValid(rat.tubePathway)){
            rat.currentTubeNode = 0;
            rat.tubePathway = new AStar(new BlockPos(rat), target, 1000).getPath(world);
        }else{
            if(rat.tubePathway[rat.tubePathway.length - 1].distanceSq(rat.getX(), rat.getY(), rat.getZ()) < 2){
                rat.currentTubeNode = 0;
                rat.tubePathway = new AStar(new BlockPos(rat), target, 1000).getPath(world);
            }
            int max = rat.tubePathway.length - 1;
            if(rat.currentTubeNode < max && rat.getDistanceSq(rat.tubePathway[Mth.clamp(rat.currentTubeNode, 0, max)]) < 2){
                rat.currentTubeNode++;
            }
            if(rat.tubePathway != null && max > 0){
                BlockPos currentNode = rat.tubePathway[Mth.clamp(rat.currentTubeNode, 0, max)];
                rat.getMoveControl().setWantedPosition(currentNode.getX() + 0.5D, currentNode.getY() + 0.25D, currentNode.getZ() + 0.5D, 1.0D);
            }
        }*/
	}

	private boolean isOpen() {
		BlockState state = Objects.requireNonNull(this.getLevel()).getBlockState(this.getBlockPos());
		if (state.getBlock() instanceof RatTubeBlock) {
			for (BooleanProperty opened : allOpenVars) {
				if (state.getValue(opened)) {
					return true;
				}
			}
		}
		return false;
	}

	public void saveAdditional(CompoundTag compound) {
		compound.putBoolean("RatNode", isNode);
		compound.putInt("OpenSide", opening == null ? -1 : opening.ordinal());
		compound.putInt("TubeColor", color);
		super.saveAdditional(compound);
	}

	public void load(CompoundTag compound) {
		super.load(compound);
		isNode = compound.getBoolean("RatNode");
		int i = compound.getInt("OpenSide");
		if (i == -1) {
			opening = null;
		} else {
			opening = Direction.values()[Mth.clamp(i, 0, Direction.values().length - 1)];
		}
		color = compound.getInt("TubeColor");
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

	public int getColor() {
		return color;
	}

	public void setColor(int colorIn) {
		this.color = colorIn;
	}
}
