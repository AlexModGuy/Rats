package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatTube;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.openal.AL;

import javax.annotation.Nullable;
import java.util.*;

public class BlockRatTube extends BlockContainer implements ICustomRendered {

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
    public static final PropertyBool[] ALL_OPEN_PROPS = new PropertyBool[]{OPEN_DOWN, OPEN_UP, OPEN_NORTH, OPEN_SOUTH, OPEN_WEST, OPEN_EAST};

    private static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.2F, 0.8F, 0.2F, 0.8F, 0.8F, 0.8F);
    private static final AxisAlignedBB UP_AABB_CONNECT_1 = new AxisAlignedBB(0.2F, 0.8F, 0.2F, 0.8F, 1F, 0.2F);
    private static final AxisAlignedBB UP_AABB_CONNECT_2 = new AxisAlignedBB(0.2F, 0.8F, 0.2F, 0.2F, 1F, 0.8F);
    private static final AxisAlignedBB UP_AABB_CONNECT_3 = new AxisAlignedBB(0.8F, 0.8F, 0.2F, 0.8F, 1F, 0.8F);
    private static final AxisAlignedBB UP_AABB_CONNECT_4 = new AxisAlignedBB(0.2F, 0.8F, 0.8F, 0.8F, 1F, 0.8F);
    //AABB maps to stop mem leaks
    private static Map<EnumFacing, AxisAlignedBB> AABB_MAP = new HashMap<>();
    private static Map<EnumFacing, AxisAlignedBB> AABB_CONNECTOR_1_MAP = new HashMap<>();
    private static Map<EnumFacing, AxisAlignedBB> AABB_CONNECTOR_2_MAP = new HashMap<>();
    private static Map<EnumFacing, AxisAlignedBB> AABB_CONNECTOR_3_MAP = new HashMap<>();
    private static Map<EnumFacing, AxisAlignedBB> AABB_CONNECTOR_4_MAP = new HashMap<>();

    private static final AxisAlignedBB INTERACT_AABB_CENTER = new AxisAlignedBB(0.2F, 0.2F, 0.2F, 0.8F, 0.8F, 0.8F);
    private static final AxisAlignedBB INTERACT_AABB_UP = new AxisAlignedBB(0.2F, 0.8F, 0.2F, 0.8F, 1F, 0.8F);
    private static final AxisAlignedBB INTERACT_AABB_DOWN = new AxisAlignedBB(0.2F, 0F, 0.2F, 0.8F, 0.2F, 0.8F);
    private static final AxisAlignedBB INTERACT_AABB_SOUTH = new AxisAlignedBB(0.2F, 0.2F, 0.8F, 0.8F, 0.8F, 1F);
    private static final AxisAlignedBB INTERACT_AABB_NORTH = new AxisAlignedBB(0.2F, 0.2F, 0F, 0.8F, 0.8F, 0.2F);
    private static final AxisAlignedBB INTERACT_AABB_WEST = new AxisAlignedBB(0F, 0.2F, 0.2F, 0.2F, 0.8F, 0.8F);
    private static final AxisAlignedBB INTERACT_AABB_EAST = new AxisAlignedBB(0.8F, 0.2F, 0.2F, 1F, 0.8F, 0.8F);

    public BlockRatTube(String name) {
        super(Material.GLASS);
        this.setHardness(1.0F);
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.rat_tube_" + name);
        this.setRegistryName(RatsMod.MODID, "rat_tube_" + name);
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

    public List<AxisAlignedBB> compileAABBList(IBlockAccess worldIn, BlockPos pos, IBlockState state){
        List<AxisAlignedBB> aabbs = new ArrayList<>();
        aabbs.add(INTERACT_AABB_CENTER);
        if (state.getBlock() instanceof BlockRatTube) {
            IBlockState actualState = getActualState(state, worldIn, pos);
            if (actualState.getValue(UP)) {
                aabbs.add(INTERACT_AABB_UP);
            }
            if (actualState.getValue(DOWN)) {
                aabbs.add(INTERACT_AABB_DOWN);
            }
            if (actualState.getValue(EAST)) {
                aabbs.add(INTERACT_AABB_EAST);
            }
            if (actualState.getValue(WEST)) {
                aabbs.add(INTERACT_AABB_WEST);
            }
            if (actualState.getValue(NORTH)) {
                aabbs.add(INTERACT_AABB_NORTH);
            }
            if (actualState.getValue(SOUTH)) {
                aabbs.add(INTERACT_AABB_SOUTH);
            }
        }
        return aabbs;
    }


    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        if (state.getBlock() instanceof BlockRatTube) {
            IBlockState actualState = getActualState(state, worldIn, pos);
            if (!actualState.getValue(UP) && !actualState.getValue(OPEN_UP)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB, EnumFacing.UP, AABB_MAP));
            }
            if (!actualState.getValue(DOWN) && !actualState.getValue(OPEN_DOWN)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB, EnumFacing.DOWN, AABB_MAP));
            }
            if (!actualState.getValue(NORTH) && !actualState.getValue(OPEN_NORTH)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB, EnumFacing.NORTH, AABB_MAP));
            }
            if (!actualState.getValue(SOUTH) && !actualState.getValue(OPEN_SOUTH)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB, EnumFacing.SOUTH, AABB_MAP));
            }
            if (!actualState.getValue(EAST) && !actualState.getValue(OPEN_EAST)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB, EnumFacing.EAST, AABB_MAP));
            }
            if (!actualState.getValue(WEST) && !actualState.getValue(OPEN_WEST)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB, EnumFacing.WEST, AABB_MAP));
            }

        if (actualState.getValue(UP)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_1, EnumFacing.UP, AABB_CONNECTOR_1_MAP));
            addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_2, EnumFacing.UP, AABB_CONNECTOR_2_MAP));
            addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_3, EnumFacing.UP, AABB_CONNECTOR_3_MAP));
            addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_4, EnumFacing.UP, AABB_CONNECTOR_4_MAP));
        }
        if (actualState.getValue(DOWN)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_1, EnumFacing.DOWN, AABB_CONNECTOR_1_MAP));
            addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_2, EnumFacing.DOWN, AABB_CONNECTOR_2_MAP));
            addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_3, EnumFacing.DOWN, AABB_CONNECTOR_3_MAP));
            addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_4, EnumFacing.DOWN, AABB_CONNECTOR_4_MAP));
        }
        if (actualState.getValue(NORTH)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_1, EnumFacing.NORTH, AABB_CONNECTOR_1_MAP));
            addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_2, EnumFacing.NORTH, AABB_CONNECTOR_2_MAP));
            addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_3, EnumFacing.NORTH, AABB_CONNECTOR_3_MAP));
            addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_4, EnumFacing.NORTH, AABB_CONNECTOR_4_MAP));
        }
        if (actualState.getValue(SOUTH)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_1, EnumFacing.SOUTH, AABB_CONNECTOR_1_MAP));
            addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_2, EnumFacing.SOUTH, AABB_CONNECTOR_2_MAP));
            addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_3, EnumFacing.SOUTH, AABB_CONNECTOR_3_MAP));
            addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_4, EnumFacing.SOUTH, AABB_CONNECTOR_4_MAP));
        }
        if (actualState.getValue(EAST)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_1, EnumFacing.EAST, AABB_CONNECTOR_1_MAP));
            addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_2, EnumFacing.EAST, AABB_CONNECTOR_2_MAP));
            addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_3, EnumFacing.EAST, AABB_CONNECTOR_3_MAP));
            addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_4, EnumFacing.EAST, AABB_CONNECTOR_4_MAP));
        }
        if (actualState.getValue(WEST)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_1, EnumFacing.WEST, AABB_CONNECTOR_1_MAP));
            addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_2, EnumFacing.WEST, AABB_CONNECTOR_2_MAP));
            addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_3, EnumFacing.WEST, AABB_CONNECTOR_3_MAP));
            addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_4, EnumFacing.WEST, AABB_CONNECTOR_4_MAP));
        }
        }
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        List<AxisAlignedBB> aabbs = compileAABBList(source, pos, state);
        AxisAlignedBB bb = new AxisAlignedBB(0.5, 0.5, 0.5, 0.5, 0.5, 0.5);
        for(AxisAlignedBB box : aabbs){
            bb = bb.union(box);
        }
        return bb;
    }


    public AxisAlignedBB rotateWithMap(AxisAlignedBB aabb, EnumFacing facing, Map<EnumFacing, AxisAlignedBB> checkMap) {
        if (checkMap.get(facing) == null) {
            AxisAlignedBB newAABB = rotateAABB(aabb, facing);
            checkMap.put(facing, newAABB);
            return newAABB;
        }else{
            return checkMap.get(facing);
        }
    }


    public static AxisAlignedBB rotateAABB(AxisAlignedBB aabb, EnumFacing facing) {
        switch (facing) {
            case UP:
                return aabb;
            case DOWN:
                return new AxisAlignedBB(aabb.minX, 1D - aabb.maxY, aabb.minZ, aabb.maxX, 1D - aabb.minY, aabb.maxZ);
            case NORTH:
                return new AxisAlignedBB(aabb.minX, aabb.minZ, 1D - aabb.maxY, aabb.maxX, aabb.maxZ, 1D - aabb.minY);
            case SOUTH:
                return new AxisAlignedBB(aabb.minX, aabb.minZ, aabb.minY, aabb.maxX, aabb.maxZ, aabb.maxY);
            case EAST:
                return new AxisAlignedBB(aabb.minY, aabb.minX, aabb.minZ, aabb.maxY, aabb.maxX, aabb.maxZ);
            case WEST:
                return new AxisAlignedBB(1D - aabb.maxY, aabb.minX, aabb.minZ, 1D - aabb.minY, aabb.maxX, aabb.maxZ);
        }
        return aabb;
    }


    @Deprecated
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        worldIn.scheduleUpdate(pos, this, 1);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        for (EnumFacing side : EnumFacing.values()) {
            PropertyBool changing;
            IBlockState sideState = worldIn.getBlockState(pos.offset(side));
            switch (side) {
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
            if (state.getValue(changing) && !canBeOpenNextToBlock(worldIn, sideState)) {
                worldIn.setBlockState(pos, state.withProperty(changing, false));
                updateTEOpening(worldIn, pos, side, false);

            }
        }
    }

    private boolean canBeOpenNextToBlock(World worldIn, IBlockState sideState) {
        return !(sideState.getBlock() instanceof BlockRatTube);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {
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
                .withProperty(DOWN, canFenceConnectTo(worldIn, pos, EnumFacing.DOWN)) ;
    }

    private boolean canFenceConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        BlockPos other = pos.offset(facing);
        return world.getBlockState(other).getBlock() instanceof BlockRatCage || world.getBlockState(other).getBlock() instanceof BlockRatTube;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public IBlockState getStateFromMeta(int meta) {
        if(meta > 0){
            PropertyBool openSide = ALL_OPEN_PROPS[MathHelper.clamp(meta - 1, 0, ALL_OPEN_PROPS.length - 1)];
            return this.getDefaultState().withProperty(openSide, true);
        }
        return super.getStateFromMeta(meta);
    }


    public int getMetaFromState(IBlockState state) {
        for(int i = 0; i < ALL_OPEN_PROPS.length; i++){
            if(state.getValue(ALL_OPEN_PROPS[i])){
                return i + 1;
            }
        }
        return 0;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (playerIn.isSneaking() || playerIn.getHeldItem(hand).getItem() instanceof ItemBlock) {
            return false;
        } else {
            PropertyBool changing;
            PropertyBool[] allOpenVars = new PropertyBool[]{OPEN_DOWN, OPEN_EAST, OPEN_NORTH, OPEN_SOUTH, OPEN_UP, OPEN_WEST};
            switch (side) {
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
            for (PropertyBool opened : allOpenVars) {
                if (state.getValue(opened)) {
                    alreadyOpened = true;
                }
            }
            if (!alreadyOpened && canBeOpenNextToBlock(worldIn, worldIn.getBlockState(pos.offset(side)))) {
                worldIn.setBlockState(pos, state.withProperty(changing, true));
                updateTEOpening(worldIn, pos, side, true);

            } else {
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

    private void updateTEOpening(World world, BlockPos pos, EnumFacing side, boolean open) {
        if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityRatTube) {
            TileEntityRatTube te = (TileEntityRatTube) world.getTileEntity(pos);
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
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return true;
    }

    public boolean isOpenAtAll(IBlockState state) {
        return getMetaFromState(state) > 0;
    }
}
