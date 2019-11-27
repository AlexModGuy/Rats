package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityMarbleCheeseGolem;
import com.google.common.base.Predicate;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.pattern.BlockMaterialMatcher;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.BlockStateMatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import java.util.List;

public class BlockMarbledCheeseRatHead extends HorizontalBlock {

    private static final VoxelShape HALF_AABB = Block.makeCuboidShape(0F, 0F, 0F, 16, 8, 16);
    private static final Predicate<BlockState> IS_MARBLE = new Predicate<BlockState>() {
        public boolean apply(@Nullable BlockState p_apply_1_) {
            return p_apply_1_ != null && (p_apply_1_.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_RAW
                    || p_apply_1_.getBlock() == RatsBlockRegistry.MARBLED_CHEESE
                    || p_apply_1_.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_PILLAR
                    || p_apply_1_.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_TILE
                    || p_apply_1_.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_CHISELED
                    || p_apply_1_.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_BRICK
                    || p_apply_1_.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_BRICK_CHISELED
                    || p_apply_1_.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY
                    || p_apply_1_.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED
            );
        }
    };
    private BlockPattern golemBasePattern;
    private BlockPattern golemPattern;

    public BlockMarbledCheeseRatHead() {
        super(Block.Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(2.5F, 0));
        this.setDefaultState(this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH));
        this.setRegistryName(RatsMod.MODID, "marbled_cheese_rat_head");
    }

    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    public boolean isFullCube(BlockState state) {
        return false;
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return HALF_AABB;
    }

    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (oldState.getBlock() != state.getBlock()) {
            this.trySpawnGolem(worldIn, pos);
        }
    }
    public boolean canDispenserPlace(World worldIn, BlockPos pos) {
        return this.getGolemBasePattern().match(worldIn, pos) != null;
    }

    private void trySpawnGolem(World worldIn, BlockPos pos) {
        BlockPattern.PatternHelper blockpattern$patternhelper = this.getGolemPattern().match(worldIn, pos);

        if (blockpattern$patternhelper != null) {
            for (int j = 0; j < this.getGolemPattern().getPalmLength(); ++j) {
                for (int k = 0; k < this.getGolemPattern().getThumbLength(); ++k) {
                    worldIn.setBlockState(blockpattern$patternhelper.translateOffset(j, k, 0).getPos(), Blocks.AIR.getDefaultState(), 2);
                }
            }

            BlockPos blockpos = blockpattern$patternhelper.translateOffset(1, 2, 0).getPos();
            EntityMarbleCheeseGolem entityirongolem = new EntityMarbleCheeseGolem(worldIn);
            entityirongolem.setLocationAndAngles((double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.05D, (double) blockpos.getZ() + 0.5D, 0.0F, 0.0F);
            worldIn.addEntity(entityirongolem);
            for(ServerPlayerEntity serverplayerentity1 : worldIn.getEntitiesWithinAABB(ServerPlayerEntity.class, entityirongolem.getBoundingBox().grow(5.0D))) {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayerentity1, entityirongolem);
            }

            for(int i1 = 0; i1 < this.getGolemPattern().getPalmLength(); ++i1) {
                for(int j1 = 0; j1 < this.getGolemPattern().getThumbLength(); ++j1) {
                    CachedBlockInfo cachedblockinfo1 = blockpattern$patternhelper.translateOffset(i1, j1, 0);
                    worldIn.notifyNeighbors(cachedblockinfo1.getPos(), Blocks.AIR);
                }
            }
        }
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }



    protected BlockPattern getGolemBasePattern() {
        if (this.golemBasePattern == null) {
            this.golemBasePattern = BlockPatternBuilder.start().aisle("~ ~", "#X#", "~#~").where('#', CachedBlockInfo.hasState(IS_MARBLE)).where('~', CachedBlockInfo.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).where('X', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(RatsBlockRegistry.MARBLED_CHEESE_GOLEM_CORE))).build();
        }

        return this.golemBasePattern;
    }

    protected BlockPattern getGolemPattern() {
        if (this.golemPattern == null) {
            this.golemPattern = BlockPatternBuilder.start().aisle("~^~", "#X#", "~#~").where('^', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(RatsBlockRegistry.MARBLED_CHEESE_RAT_HEAD))).where('#', CachedBlockInfo.hasState(IS_MARBLE)).where('~', CachedBlockInfo.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).where('X', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(RatsBlockRegistry.MARBLED_CHEESE_GOLEM_CORE))).build();
        }

        return this.golemPattern;
    }
}