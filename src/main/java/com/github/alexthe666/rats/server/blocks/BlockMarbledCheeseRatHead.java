package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityMarbleCheeseGolem;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatlanteanAutomatonHead;
import com.google.common.base.Predicate;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.pattern.BlockMaterialMatcher;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.BlockStateMatcher;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockMarbledCheeseRatHead extends ContainerBlock implements IUsesTEISR {

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

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
        super(Block.Properties.create(Material.ROCK).sound(SoundType.STONE).func_235838_a_((p) -> 5).notSolid().variableOpacity().hardnessAndResistance(2.5F, 0));
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
        this.setRegistryName(RatsMod.MODID, "marbled_cheese_rat_head");

    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }


    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    public boolean isFullCube(BlockState state) {
        return false;
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
            EntityMarbleCheeseGolem entityirongolem = new EntityMarbleCheeseGolem(RatsEntityRegistry.RATLANTEAN_AUTOMATON, worldIn);
            entityirongolem.setLocationAndAngles((double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.05D, (double) blockpos.getZ() + 0.5D, 0.0F, 0.0F);
            worldIn.addEntity(entityirongolem);
            for (ServerPlayerEntity serverplayerentity1 : worldIn.getEntitiesWithinAABB(ServerPlayerEntity.class, entityirongolem.getBoundingBox().grow(5.0D))) {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayerentity1, entityirongolem);
            }

            for (int i1 = 0; i1 < this.getGolemPattern().getPalmLength(); ++i1) {
                for (int j1 = 0; j1 < this.getGolemPattern().getThumbLength(); ++j1) {
                    CachedBlockInfo cachedblockinfo1 = blockpattern$patternhelper.translateOffset(i1, j1, 0);
                    worldIn.notifyBlockUpdate(cachedblockinfo1.getPos(), cachedblockinfo1.getBlockState(), cachedblockinfo1.getBlockState(), 4);
                }
            }
        }
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
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

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityRatlanteanAutomatonHead();
    }
}