package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatTube;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

public class BlockRatTube extends ContainerBlock implements ICustomRendered {

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

    private static final AxisAlignedBB UP_AABB = Block.makeCuboidShape(0.2F, 0.8F, 0.2F, 0.8F, 0.8F, 0.8F);
    private static final AxisAlignedBB UP_AABB_CONNECT_1 = Block.makeCuboidShape(0.2F, 0.8F, 0.2F, 0.8F, 1F, 0.2F);
    private static final AxisAlignedBB UP_AABB_CONNECT_2 = Block.makeCuboidShape(0.2F, 0.8F, 0.2F, 0.2F, 1F, 0.8F);
    private static final AxisAlignedBB UP_AABB_CONNECT_3 = Block.makeCuboidShape(0.8F, 0.8F, 0.2F, 0.8F, 1F, 0.8F);
    private static final AxisAlignedBB UP_AABB_CONNECT_4 = Block.makeCuboidShape(0.2F, 0.8F, 0.8F, 0.8F, 1F, 0.8F);
    private static final AxisAlignedBB INTERACT_AABB_CENTER = Block.makeCuboidShape(0.2F, 0.2F, 0.2F, 0.8F, 0.8F, 0.8F);
    private static final AxisAlignedBB INTERACT_AABB_UP = Block.makeCuboidShape(0.2F, 0.8F, 0.2F, 0.8F, 1F, 0.8F);
    private static final AxisAlignedBB INTERACT_AABB_DOWN = Block.makeCuboidShape(0.2F, 0F, 0.2F, 0.8F, 0.2F, 0.8F);
    private static final AxisAlignedBB INTERACT_AABB_SOUTH = Block.makeCuboidShape(0.2F, 0.2F, 0.8F, 0.8F, 0.8F, 1F);
    private static final AxisAlignedBB INTERACT_AABB_NORTH = Block.makeCuboidShape(0.2F, 0.2F, 0F, 0.8F, 0.8F, 0.2F);
    private static final AxisAlignedBB INTERACT_AABB_WEST = Block.makeCuboidShape(0F, 0.2F, 0.2F, 0.2F, 0.8F, 0.8F);
    private static final AxisAlignedBB INTERACT_AABB_EAST = Block.makeCuboidShape(0.8F, 0.2F, 0.2F, 1F, 0.8F, 0.8F);
    //AABB maps to stop mem leaks
    private static Map<Direction, AxisAlignedBB> AABB_MAP = new HashMap<>();
    private static Map<Direction, AxisAlignedBB> AABB_CONNECTOR_1_MAP = new HashMap<>();
    private static Map<Direction, AxisAlignedBB> AABB_CONNECTOR_2_MAP = new HashMap<>();
    private static Map<Direction, AxisAlignedBB> AABB_CONNECTOR_3_MAP = new HashMap<>();
    private static Map<Direction, AxisAlignedBB> AABB_CONNECTOR_4_MAP = new HashMap<>();

    public BlockRatTube(String name) {
        super(Material.GLASS);
        this.setHardness(1.0F);
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.rat_tube_" + name);
        this.setRegistryName(RatsMod.MODID, "rat_tube_" + name);
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(NORTH, Boolean.valueOf(false))
                .with(EAST, Boolean.valueOf(false))
                .with(SOUTH, Boolean.valueOf(false))
                .with(WEST, Boolean.valueOf(false))
                .with(UP, Boolean.valueOf(false))
                .with(DOWN, Boolean.valueOf(false))
                .with(OPEN_NORTH, Boolean.valueOf(false))
                .with(OPEN_EAST, Boolean.valueOf(false))
                .with(OPEN_SOUTH, Boolean.valueOf(false))
                .with(OPEN_WEST, Boolean.valueOf(false))
                .with(OPEN_UP, Boolean.valueOf(false))
                .with(OPEN_DOWN, Boolean.valueOf(false))
        );
        this.setTickRandomly(true);
        GameRegistry.registerTileEntity(TileEntityRatTube.class, "rat_tube");
    }

    public static AxisAlignedBB rotateAABB(AxisAlignedBB aabb, Direction facing) {
        switch (facing) {
            case UP:
                return aabb;
            case DOWN:
                return Block.makeCuboidShape(aabb.minX, 1D - aabb.maxY, aabb.minZ, aabb.maxX, 1D - aabb.minY, aabb.maxZ);
            case NORTH:
                return Block.makeCuboidShape(aabb.minX, aabb.minZ, 1D - aabb.maxY, aabb.maxX, aabb.maxZ, 1D - aabb.minY);
            case SOUTH:
                return Block.makeCuboidShape(aabb.minX, aabb.minZ, aabb.minY, aabb.maxX, aabb.maxZ, aabb.maxY);
            case EAST:
                return Block.makeCuboidShape(aabb.minY, aabb.minX, aabb.minZ, aabb.maxY, aabb.maxX, aabb.maxZ);
            case WEST:
                return Block.makeCuboidShape(1D - aabb.maxY, aabb.minX, aabb.minZ, 1D - aabb.minY, aabb.maxX, aabb.maxZ);
        }
        return aabb;
    }

    public List<AxisAlignedBB> compileAABBList(IBlockAccess worldIn, BlockPos pos, BlockState state) {
        List<AxisAlignedBB> aabbs = new ArrayList<>();
        aabbs.add(INTERACT_AABB_CENTER);
        if (state.getBlock() instanceof BlockRatTube) {
            BlockState actualState = getActualState(state, worldIn, pos);
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

    public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        if (state.getBlock() instanceof BlockRatTube) {
            BlockState actualState = getActualState(state, worldIn, pos);
            if (!actualState.getValue(UP) && !actualState.getValue(OPEN_UP)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB, Direction.UP, AABB_MAP));
            }
            if (!actualState.getValue(DOWN) && !actualState.getValue(OPEN_DOWN)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB, Direction.DOWN, AABB_MAP));
            }
            if (!actualState.getValue(NORTH) && !actualState.getValue(OPEN_NORTH)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB, Direction.NORTH, AABB_MAP));
            }
            if (!actualState.getValue(SOUTH) && !actualState.getValue(OPEN_SOUTH)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB, Direction.SOUTH, AABB_MAP));
            }
            if (!actualState.getValue(EAST) && !actualState.getValue(OPEN_EAST)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB, Direction.EAST, AABB_MAP));
            }
            if (!actualState.getValue(WEST) && !actualState.getValue(OPEN_WEST)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB, Direction.WEST, AABB_MAP));
            }

            if (actualState.getValue(UP)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_1, Direction.UP, AABB_CONNECTOR_1_MAP));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_2, Direction.UP, AABB_CONNECTOR_2_MAP));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_3, Direction.UP, AABB_CONNECTOR_3_MAP));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_4, Direction.UP, AABB_CONNECTOR_4_MAP));
            }
            if (actualState.getValue(DOWN)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_1, Direction.DOWN, AABB_CONNECTOR_1_MAP));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_2, Direction.DOWN, AABB_CONNECTOR_2_MAP));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_3, Direction.DOWN, AABB_CONNECTOR_3_MAP));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_4, Direction.DOWN, AABB_CONNECTOR_4_MAP));
            }
            if (actualState.getValue(NORTH)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_1, Direction.NORTH, AABB_CONNECTOR_1_MAP));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_2, Direction.NORTH, AABB_CONNECTOR_2_MAP));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_3, Direction.NORTH, AABB_CONNECTOR_3_MAP));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_4, Direction.NORTH, AABB_CONNECTOR_4_MAP));
            }
            if (actualState.getValue(SOUTH)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_1, Direction.SOUTH, AABB_CONNECTOR_1_MAP));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_2, Direction.SOUTH, AABB_CONNECTOR_2_MAP));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_3, Direction.SOUTH, AABB_CONNECTOR_3_MAP));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_4, Direction.SOUTH, AABB_CONNECTOR_4_MAP));
            }
            if (actualState.getValue(EAST)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_1, Direction.EAST, AABB_CONNECTOR_1_MAP));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_2, Direction.EAST, AABB_CONNECTOR_2_MAP));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_3, Direction.EAST, AABB_CONNECTOR_3_MAP));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_4, Direction.EAST, AABB_CONNECTOR_4_MAP));
            }
            if (actualState.getValue(WEST)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_1, Direction.WEST, AABB_CONNECTOR_1_MAP));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_2, Direction.WEST, AABB_CONNECTOR_2_MAP));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_3, Direction.WEST, AABB_CONNECTOR_3_MAP));
                addCollisionBoxToList(pos, entityBox, collidingBoxes, rotateWithMap(UP_AABB_CONNECT_4, Direction.WEST, AABB_CONNECTOR_4_MAP));
            }
        }
    }

    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
        List<AxisAlignedBB> aabbs = compileAABBList(source, pos, state);
        AxisAlignedBB bb = Block.makeCuboidShape(0.5, 0.5, 0.5, 0.5, 0.5, 0.5);
        for (AxisAlignedBB box : aabbs) {
            bb = bb.union(box);
        }
        return bb;
    }

    public AxisAlignedBB rotateWithMap(AxisAlignedBB aabb, Direction facing, Map<Direction, AxisAlignedBB> checkMap) {
        if (checkMap.get(facing) == null) {
            AxisAlignedBB newAABB = rotateAABB(aabb, facing);
            checkMap.put(facing, newAABB);
            return newAABB;
        } else {
            return checkMap.get(facing);
        }
    }

    @Deprecated
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        worldIn.scheduleUpdate(pos, this, 1);
    }

    public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand) {
        for (Direction side : Direction.values()) {
            PropertyBool changing;
            BlockState sideState = worldIn.getBlockState(pos.offset(side));
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
                worldIn.setBlockState(pos, state.with(changing, false));
                updateTEOpening(worldIn, pos, side, false);

            }
        }
    }

    private boolean canBeOpenNextToBlock(World worldIn, BlockState sideState) {
        return !(sideState.getBlock() instanceof BlockRatTube);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public EnumBlockRenderType getRenderType(BlockState state) {
        return EnumBlockRenderType.MODEL;
    }


    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, NORTH, SOUTH, EAST, WEST, UP, DOWN,
                OPEN_NORTH, OPEN_SOUTH, OPEN_EAST, OPEN_WEST, OPEN_UP, OPEN_DOWN);
    }

    public BlockState getActualState(BlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.with(NORTH, canFenceConnectTo(worldIn, pos, Direction.NORTH))
                .with(SOUTH, canFenceConnectTo(worldIn, pos, Direction.SOUTH))
                .with(EAST, canFenceConnectTo(worldIn, pos, Direction.EAST))
                .with(WEST, canFenceConnectTo(worldIn, pos, Direction.WEST))
                .with(UP, canFenceConnectTo(worldIn, pos, Direction.UP))
                .with(DOWN, canFenceConnectTo(worldIn, pos, Direction.DOWN));
    }

    private boolean canFenceConnectTo(IBlockAccess world, BlockPos pos, Direction facing) {
        BlockPos other = pos.offset(facing);
        return world.getBlockState(other).getBlock() instanceof BlockRatCage || world.getBlockState(other).getBlock() instanceof BlockRatTube;
    }

    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    public boolean isFullCube(BlockState state) {
        return false;
    }

    public BlockState getStateFromMeta(int meta) {
        if (meta > 0) {
            PropertyBool openSide = ALL_OPEN_PROPS[MathHelper.clamp(meta - 1, 0, ALL_OPEN_PROPS.length - 1)];
            return this.getDefaultState().with(openSide, true);
        }
        return super.getStateFromMeta(meta);
    }


    public int getMetaFromState(BlockState state) {
        for (int i = 0; i < ALL_OPEN_PROPS.length; i++) {
            if (state.getValue(ALL_OPEN_PROPS[i])) {
                return i + 1;
            }
        }
        return 0;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, BlockState state, EntityPlayer playerIn, EnumHand hand, Direction side, float hitX, float hitY, float hitZ) {
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
                worldIn.setBlockState(pos, state.with(changing, true));
                updateTEOpening(worldIn, pos, side, true);

            } else {
                worldIn.setBlockState(pos, state.with(OPEN_NORTH, Boolean.valueOf(false))
                        .with(OPEN_EAST, Boolean.valueOf(false))
                        .with(OPEN_SOUTH, Boolean.valueOf(false))
                        .with(OPEN_WEST, Boolean.valueOf(false))
                        .with(OPEN_UP, Boolean.valueOf(false))
                        .with(OPEN_DOWN, Boolean.valueOf(false)));
                updateTEOpening(worldIn, pos, side, false);
            }
            return true;
        }
    }

    private void updateTEOpening(World world, BlockPos pos, Direction side, boolean open) {
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
    public boolean shouldSideBeRendered(BlockState blockState, IBlockAccess blockAccess, BlockPos pos, Direction side) {
        return true;
    }

    public boolean isOpenAtAll(BlockState state) {
        return getMetaFromState(state) > 0;
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tile.rats.rat_tube.desc0"));
        tooltip.add(I18n.format("tile.rats.rat_tube.desc1"));
        tooltip.add(I18n.format("tile.rats.rat_tube.desc2"));
    }

}
