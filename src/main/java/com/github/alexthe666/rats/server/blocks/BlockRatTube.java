package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatTube;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockRatTube extends BlockContainer {

    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");

    public static final PropertyBool OPEN_NORTH = PropertyBool.create("open_north");
    public static final PropertyBool OPEN_EAST = PropertyBool.create("open_east");
    public static final PropertyBool OPEN_SOUTH = PropertyBool.create("open_south");
    public static final PropertyBool OPEN_WEST = PropertyBool.create("open_west");
    public static final PropertyBool OPEN_UP = PropertyBool.create("open_up");
    public static final PropertyBool OPEN_DOWN = PropertyBool.create("open_down");

    public BlockRatTube() {
        super(Material.GLASS);
        this.setHardness(1.0F);
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.rat_tube");
        this.setRegistryName(RatsMod.MODID, "rat_tube");
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(NORTH, Boolean.valueOf(false))
                .withProperty(EAST, Boolean.valueOf(false))
                .withProperty(SOUTH, Boolean.valueOf(false))
                .withProperty(WEST, Boolean.valueOf(false))
                .withProperty(UP, Boolean.valueOf(false))
                .withProperty(DOWN, Boolean.valueOf(false))
                .withProperty(OPEN_NORTH, Boolean.valueOf(false))
                .withProperty(OPEN_EAST, Boolean.valueOf(false))
                .withProperty(OPEN_SOUTH, Boolean.valueOf(false))
                .withProperty(OPEN_WEST, Boolean.valueOf(false))
                .withProperty(OPEN_UP, Boolean.valueOf(false))
                .withProperty(OPEN_DOWN, Boolean.valueOf(false))
        );
        this.setTickRandomly(true);
        GameRegistry.registerTileEntity(TileEntityRatTube.class, "rat_tube");
    }

    @Deprecated
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        worldIn.scheduleUpdate(pos, this, 1);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        PropertyBool[] allOpenVars = new PropertyBool[]{OPEN_DOWN, OPEN_EAST, OPEN_NORTH, OPEN_SOUTH, OPEN_UP, OPEN_WEST};
        for(EnumFacing side : EnumFacing.values()){
            PropertyBool changing;
            IBlockState sideState = worldIn.getBlockState(pos.offset(side));
            switch (side){
                case NORTH:
                    changing = OPEN_NORTH;
                    break;
                case SOUTH:
                    changing = OPEN_SOUTH;
                    break;
                case EAST:
                    changing = OPEN_EAST;
                    break;
                case WEST:
                    changing = OPEN_WEST;
                    break;
                case DOWN:
                    changing = OPEN_DOWN;
                    break;
                default:
                    changing = OPEN_UP;
                    break;
            }
            if(state.getValue(changing) && !canBeOpenNextToBlock(worldIn, sideState)){
                worldIn.setBlockState(pos, state.withProperty(changing, false));
                updateTEOpening(worldIn, pos, side, false);

            }
        }
    }

    private boolean canBeOpenNextToBlock(World worldIn, IBlockState sideState) {
        return sideState.getBlock() != this;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }


    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, NORTH, SOUTH, EAST, WEST, UP, DOWN,
        OPEN_NORTH, OPEN_SOUTH, OPEN_EAST, OPEN_WEST, OPEN_UP, OPEN_DOWN);
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(NORTH, canFenceConnectTo(worldIn, pos, EnumFacing.NORTH))
                .withProperty(SOUTH, canFenceConnectTo(worldIn, pos, EnumFacing.SOUTH))
                .withProperty(EAST, canFenceConnectTo(worldIn, pos, EnumFacing.EAST))
                .withProperty(WEST, canFenceConnectTo(worldIn, pos, EnumFacing.WEST))
                .withProperty(UP, canFenceConnectTo(worldIn, pos, EnumFacing.UP))
                .withProperty(DOWN, canFenceConnectTo(worldIn, pos, EnumFacing.DOWN));
    }

    private boolean canFenceConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        BlockPos other = pos.offset(facing);
        return world.getBlockState(other).getBlock() == this;
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

    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (playerIn.isSneaking() || playerIn.getHeldItem(hand).getItem() == Item.getItemFromBlock(this)) {
            return false;
        } else {
            PropertyBool changing;
            PropertyBool[] allOpenVars = new PropertyBool[]{OPEN_DOWN, OPEN_EAST, OPEN_NORTH, OPEN_SOUTH, OPEN_UP, OPEN_WEST};
            switch (side){
                case NORTH:
                    changing = OPEN_NORTH;
                    break;
                case SOUTH:
                    changing = OPEN_SOUTH;
                    break;
                case EAST:
                    changing = OPEN_EAST;
                    break;
                case WEST:
                    changing = OPEN_WEST;
                    break;
                case DOWN:
                    changing = OPEN_DOWN;
                    break;
                default:
                    changing = OPEN_UP;
                    break;
            }
            boolean alreadyOpened = false;
            for(PropertyBool opened : allOpenVars){
                if(state.getValue(opened)){
                    alreadyOpened = true;
                }
            }
            if(!alreadyOpened){
                worldIn.setBlockState(pos, state.withProperty(changing, true));
                updateTEOpening(worldIn, pos, side, true);

            }else{
                worldIn.setBlockState(pos, state.withProperty(OPEN_NORTH, Boolean.valueOf(false))
                        .withProperty(OPEN_EAST, Boolean.valueOf(false))
                        .withProperty(OPEN_SOUTH, Boolean.valueOf(false))
                        .withProperty(OPEN_WEST, Boolean.valueOf(false))
                        .withProperty(OPEN_UP, Boolean.valueOf(false))
                        .withProperty(OPEN_DOWN, Boolean.valueOf(false)));
                updateTEOpening(worldIn, pos, side, false);
            }
            return true;
        }
    }

    private void updateTEOpening(World world, BlockPos pos, EnumFacing side, boolean open){
        if(world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityRatTube){
            TileEntityRatTube te = (TileEntityRatTube)world.getTileEntity(pos);
            te.setEntranceData(side, open);
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityRatTube();
    }

    @Deprecated
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side){
        return true;
    }
}
