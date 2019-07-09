package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatHole;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockRatHole extends BlockContainer {

    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");

    private static final AxisAlignedBB TOP_AABB = new AxisAlignedBB(0F, 0.5F, 0F, 1F, 1F, 1F);
    private static final AxisAlignedBB NS_LEFT_AABB = new AxisAlignedBB(0F, 0F, 0F, 0.25F, 0.5F, 1F);
    private static final AxisAlignedBB NS_RIGHT_AABB = new AxisAlignedBB(0.75F, 0F, 0F, 1F, 0.5F, 1F);
    private static final AxisAlignedBB EW_LEFT_AABB = new AxisAlignedBB(0F, 0F, 0F, 1F, 0.5F, 0.25F);
    private static final AxisAlignedBB EW_RIGHT_AABB = new AxisAlignedBB(0, 0F, 0.75F, 1F, 0.5F, 1F);

    private static final AxisAlignedBB NORTH_CORNER_AABB = new AxisAlignedBB(0F, 0F, 0F, 0.25F, 0.5F, 0.25F);
    private static final AxisAlignedBB EAST_CORNER_AABB = new AxisAlignedBB(0.75F, 0F, 0F, 1F, 0.5F, 0.25F);
    private static final AxisAlignedBB SOUTH_CORNER_AABB = new AxisAlignedBB(0F, 01F, 0.75F, 0.25, 0.5F, 1F);
    private static final AxisAlignedBB WEST_CORNER_AABB = new AxisAlignedBB(0.75F, 0F, 0.75F, 1F, 0.5F, 1F);

    protected BlockRatHole() {
        super(Material.WOOD);
        this.setHardness(1.3F);
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.rat_hole");
        this.setRegistryName(RatsMod.MODID, "rat_hole");
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(NORTH, Boolean.valueOf(false))
                .withProperty(EAST, Boolean.valueOf(false))
                .withProperty(SOUTH, Boolean.valueOf(false))
                .withProperty(WEST, Boolean.valueOf(false))
        );

        GameRegistry.registerTileEntity(TileEntityRatHole.class, "rats.rat_hole");
    }

    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {

    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state){
        if(worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityRatHole){
            TileEntityRatHole te = (TileEntityRatHole)worldIn.getTileEntity(pos);
            NonNullList<ItemStack> ret = NonNullList.create();
            te.getImmitatedBlockState().getBlock().getDrops(ret, worldIn, pos, te.getImmitatedBlockState(), 1);
            for(ItemStack stack : ret){
                EntityItem entityItem = new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
                if(!worldIn.isRemote){
                    worldIn.spawnEntity(entityItem);
                }
            }
        }
        worldIn.removeTileEntity(pos);
    }


    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(NORTH, canFenceConnectTo(worldIn, pos, EnumFacing.NORTH))
                .withProperty(SOUTH, canFenceConnectTo(worldIn, pos, EnumFacing.SOUTH))
                .withProperty(EAST, canFenceConnectTo(worldIn, pos, EnumFacing.EAST))
                .withProperty(WEST, canFenceConnectTo(worldIn, pos, EnumFacing.WEST));
    }

    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, TOP_AABB);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_CORNER_AABB);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_CORNER_AABB);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_CORNER_AABB);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_CORNER_AABB);
        if (state.getBlock() instanceof BlockRatHole) {
            IBlockState actualState = getActualState(state, worldIn, pos);
            if (actualState.getValue(NORTH)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, EW_LEFT_AABB);
            }
            if (actualState.getValue(SOUTH)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, EW_RIGHT_AABB);
            }
            if (actualState.getValue(WEST)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, NS_LEFT_AABB);
            }
            if (actualState.getValue(EAST)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, NS_RIGHT_AABB);
            }
        }
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, NORTH, SOUTH, EAST, WEST);
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.AIR;
    }


    private boolean canFenceConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        BlockPos other = pos.offset(facing);
        return world.getBlockState(other).isOpaqueCube();
    }

    public int getMetaFromState(IBlockState state) {
        return 0;
    }


    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FULL_BLOCK_AABB;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, BlockPos pos, net.minecraft.client.particle.ParticleManager manager) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, net.minecraft.client.particle.ParticleManager manager) {
        return false;
    }

    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity){
        if(world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityRatHole) {
            TileEntityRatHole te = (TileEntityRatHole) world.getTileEntity(pos);
            return te.getImmitatedBlockState().getBlock().getSoundType();
        }
        return SoundType.WOOD;
    }


    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityRatHole();
    }
}
