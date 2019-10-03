package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityMarbleCheeseGolem;
import com.google.common.base.Predicate;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockMarbledCheeseRatHead extends BlockHorizontal {

    private static final AxisAlignedBB HALF_AABB = new AxisAlignedBB(0F, 0F, 0F, 1F, 0.5F, 1F);
    private static final Predicate<IBlockState> IS_MARBLE = new Predicate<IBlockState>() {
        public boolean apply(@Nullable IBlockState p_apply_1_) {
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
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setTickRandomly(true);
        this.setCreativeTab(RatsMod.TAB);
        this.setSoundType(SoundType.STONE);
        this.setHardness(2.5F);
        this.setTranslationKey("rats.marbled_cheese_rat_head");
        this.setRegistryName(RatsMod.MODID, "marbled_cheese_rat_head");
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return HALF_AABB;
    }

    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, HALF_AABB);
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        this.trySpawnGolem(worldIn, pos);
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
            worldIn.spawnEntity(entityirongolem);

            for (EntityPlayerMP entityplayermp1 : worldIn.getEntitiesWithinAABB(EntityPlayerMP.class, entityirongolem.getEntityBoundingBox().grow(5.0D))) {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayermp1, entityirongolem);
            }

            for (int j1 = 0; j1 < 120; ++j1) {
                worldIn.spawnParticle(EnumParticleTypes.SNOWBALL, (double) blockpos.getX() + worldIn.rand.nextDouble(), (double) blockpos.getY() + worldIn.rand.nextDouble() * 3.9D, (double) blockpos.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
            }

            for (int k1 = 0; k1 < this.getGolemPattern().getPalmLength(); ++k1) {
                for (int l1 = 0; l1 < this.getGolemPattern().getThumbLength(); ++l1) {
                    BlockWorldState blockworldstate1 = blockpattern$patternhelper.translateOffset(k1, l1, 0);
                    worldIn.notifyNeighborsRespectDebug(blockworldstate1.getPos(), Blocks.AIR, false);
                }
            }
        }
    }

    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }


    protected BlockPattern getGolemBasePattern() {
        if (this.golemBasePattern == null) {
            this.golemBasePattern = FactoryBlockPattern.start().aisle("~ ~", "#X#", "~#~").where('#', BlockWorldState.hasState(IS_MARBLE)).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).where('X', BlockWorldState.hasState(BlockStateMatcher.forBlock(RatsBlockRegistry.MARBLED_CHEESE_GOLEM_CORE))).build();
        }

        return this.golemBasePattern;
    }

    protected BlockPattern getGolemPattern() {
        if (this.golemPattern == null) {
            this.golemPattern = FactoryBlockPattern.start().aisle("~^~", "#X#", "~#~").where('^', BlockWorldState.hasState(BlockStateMatcher.forBlock(RatsBlockRegistry.MARBLED_CHEESE_RAT_HEAD))).where('#', BlockWorldState.hasState(IS_MARBLE)).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).where('X', BlockWorldState.hasState(BlockStateMatcher.forBlock(RatsBlockRegistry.MARBLED_CHEESE_GOLEM_CORE))).build();
        }

        return this.golemPattern;
    }
}