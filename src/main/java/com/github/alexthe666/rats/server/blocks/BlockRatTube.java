package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatTube;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;

public class BlockRatTube extends ContainerBlock implements ICustomRendered {

    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");

    public static final BooleanProperty OPEN_NORTH = BooleanProperty.create("open_north");
    public static final BooleanProperty OPEN_EAST = BooleanProperty.create("open_east");
    public static final BooleanProperty OPEN_SOUTH = BooleanProperty.create("open_south");
    public static final BooleanProperty OPEN_WEST = BooleanProperty.create("open_west");
    public static final BooleanProperty OPEN_UP = BooleanProperty.create("open_up");
    public static final BooleanProperty OPEN_DOWN = BooleanProperty.create("open_down");
    public static final BooleanProperty[] ALL_OPEN_PROPS = new BooleanProperty[]{OPEN_DOWN, OPEN_UP, OPEN_NORTH, OPEN_SOUTH, OPEN_WEST, OPEN_EAST};

    private static final VoxelShape UP_AABB = Block.makeCuboidShape(3, 13, 3, 13, 13, 13);
    private static final VoxelShape UP_AABB_CONNECT_1 = Block.makeCuboidShape(3, 13, 3, 13, 16, 3);
    private static final VoxelShape UP_AABB_CONNECT_2 = Block.makeCuboidShape(3, 13, 3, 3, 16, 13);
    private static final VoxelShape UP_AABB_CONNECT_3 = Block.makeCuboidShape(13, 13, 3, 13, 16, 13);
    private static final VoxelShape UP_AABB_CONNECT_4 = Block.makeCuboidShape(3, 13, 13, 13, 16, 13);
    private static final VoxelShape INTERACT_AABB_CENTER = Block.makeCuboidShape(3, 3, 3, 13, 13, 13);
    private static final VoxelShape INTERACT_AABB_UP = Block.makeCuboidShape(3, 13, 3, 13, 16, 13);
    private static final VoxelShape INTERACT_AABB_DOWN = Block.makeCuboidShape(3, 0F, 3, 13, 3, 13);
    private static final VoxelShape INTERACT_AABB_SOUTH = Block.makeCuboidShape(3, 3, 13, 13, 13, 16);
    private static final VoxelShape INTERACT_AABB_NORTH = Block.makeCuboidShape(3, 3, 0F, 13, 13, 3);
    private static final VoxelShape INTERACT_AABB_WEST = Block.makeCuboidShape(0F, 3, 3, 3, 13, 13);
    private static final VoxelShape INTERACT_AABB_EAST = Block.makeCuboidShape(13, 3, 3, 16, 13, 13);
    //AABB maps to stop mem leaks
    private static Map<Direction, VoxelShape> AABB_MAP = new HashMap<>();
    private static Map<Direction, VoxelShape> AABB_CONNECTOR_1_MAP = new HashMap<>();
    private static Map<Direction, VoxelShape> AABB_CONNECTOR_2_MAP = new HashMap<>();
    private static Map<Direction, VoxelShape> AABB_CONNECTOR_3_MAP = new HashMap<>();
    private static Map<Direction, VoxelShape> AABB_CONNECTOR_4_MAP = new HashMap<>();

    public BlockRatTube(String name) {
        super(Block.Properties.create(Material.GLASS).sound(SoundType.WOOD).hardnessAndResistance(0.9F, 0.0F));
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
        //GameRegistry.registerTileEntity(TileEntityRatTube.class, "rat_tube");
    }

    public static VoxelShape rotateAABB(VoxelShape aabb, Direction facing) {
        switch (facing) {
            case UP:
                return aabb;
            case DOWN:
                return Block.makeCuboidShape(aabb.getBoundingBox().minX, 16 - aabb.getBoundingBox().maxY, aabb.getBoundingBox().minZ, aabb.getBoundingBox().maxX, 16 - aabb.getBoundingBox().minY, aabb.getBoundingBox().maxZ);
            case NORTH:
                return Block.makeCuboidShape(aabb.getBoundingBox().minX, aabb.getBoundingBox().minZ, 16 - aabb.getBoundingBox().maxY, aabb.getBoundingBox().maxX, aabb.getBoundingBox().maxZ, 16 - aabb.getBoundingBox().minY);
            case SOUTH:
                return Block.makeCuboidShape(aabb.getBoundingBox().minX, aabb.getBoundingBox().minZ, aabb.getBoundingBox().minY, aabb.getBoundingBox().maxX, aabb.getBoundingBox().maxZ, aabb.getBoundingBox().maxY);
            case EAST:
                return Block.makeCuboidShape(aabb.getBoundingBox().minY, aabb.getBoundingBox().minX, aabb.getBoundingBox().minZ, aabb.getBoundingBox().maxY, aabb.getBoundingBox().maxX, aabb.getBoundingBox().maxZ);
            case WEST:
                return Block.makeCuboidShape(16 - aabb.getBoundingBox().maxY, aabb.getBoundingBox().minX, aabb.getBoundingBox().minZ, 16 - aabb.getBoundingBox().minY, aabb.getBoundingBox().maxX, aabb.getBoundingBox().maxZ);
        }
        return aabb;
    }

    public List<VoxelShape> compileAABBList(IWorldReader reader, BlockPos pos, BlockState state) {
        List<VoxelShape> aabbs = new ArrayList<>();
        aabbs.add(INTERACT_AABB_CENTER);
        if (state.getBlock() instanceof BlockRatTube) {
            BlockState actualState = getActualState(state, reader, pos);
            if (actualState.get(UP)) {
                aabbs.add(INTERACT_AABB_UP);
            }
            if (actualState.get(DOWN)) {
                aabbs.add(INTERACT_AABB_DOWN);
            }
            if (actualState.get(EAST)) {
                aabbs.add(INTERACT_AABB_EAST);
            }
            if (actualState.get(WEST)) {
                aabbs.add(INTERACT_AABB_WEST);
            }
            if (actualState.get(NORTH)) {
                aabbs.add(INTERACT_AABB_NORTH);
            }
            if (actualState.get(SOUTH)) {
                aabbs.add(INTERACT_AABB_SOUTH);
            }
        }
        return aabbs;
    }

    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        VoxelShape shape = VoxelShapes.empty();
        if (state.getBlock() instanceof BlockRatTube) {
            BlockState actualState = getActualState(state, worldIn, pos);
            if (!actualState.get(UP) && !actualState.get(OPEN_UP)) {
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB, Direction.UP, AABB_MAP), IBooleanFunction.ONLY_FIRST).simplify();
            }
            if (!actualState.get(DOWN) && !actualState.get(OPEN_DOWN)) {
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB, Direction.DOWN, AABB_MAP), IBooleanFunction.ONLY_FIRST).simplify();
            }
            if (!actualState.get(NORTH) && !actualState.get(OPEN_NORTH)) {
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB, Direction.NORTH, AABB_MAP), IBooleanFunction.ONLY_FIRST).simplify();
            }
            if (!actualState.get(SOUTH) && !actualState.get(OPEN_SOUTH)) {
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB, Direction.SOUTH, AABB_MAP), IBooleanFunction.ONLY_FIRST).simplify();
            }
            if (!actualState.get(EAST) && !actualState.get(OPEN_EAST)) {
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB, Direction.EAST, AABB_MAP), IBooleanFunction.ONLY_FIRST).simplify();
            }
            if (!actualState.get(WEST) && !actualState.get(OPEN_WEST)) {
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB, Direction.WEST, AABB_MAP), IBooleanFunction.ONLY_FIRST).simplify();
            }

            if (actualState.get(UP)) {
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB_CONNECT_1, Direction.UP, AABB_CONNECTOR_1_MAP), IBooleanFunction.ONLY_FIRST).simplify();
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB_CONNECT_2, Direction.UP, AABB_CONNECTOR_2_MAP), IBooleanFunction.ONLY_FIRST).simplify();
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB_CONNECT_3, Direction.UP, AABB_CONNECTOR_3_MAP), IBooleanFunction.ONLY_FIRST).simplify();
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB_CONNECT_4, Direction.UP, AABB_CONNECTOR_4_MAP), IBooleanFunction.ONLY_FIRST).simplify();
            }
            if (actualState.get(DOWN)) {
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB_CONNECT_1, Direction.DOWN, AABB_CONNECTOR_1_MAP), IBooleanFunction.ONLY_FIRST).simplify();
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB_CONNECT_2, Direction.DOWN, AABB_CONNECTOR_2_MAP), IBooleanFunction.ONLY_FIRST).simplify();
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB_CONNECT_3, Direction.DOWN, AABB_CONNECTOR_3_MAP), IBooleanFunction.ONLY_FIRST).simplify();
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB_CONNECT_4, Direction.DOWN, AABB_CONNECTOR_4_MAP), IBooleanFunction.ONLY_FIRST).simplify();
            }
            if (actualState.get(NORTH)) {
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB_CONNECT_1, Direction.NORTH, AABB_CONNECTOR_1_MAP), IBooleanFunction.ONLY_FIRST).simplify();
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB_CONNECT_2, Direction.NORTH, AABB_CONNECTOR_2_MAP), IBooleanFunction.ONLY_FIRST).simplify();
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB_CONNECT_3, Direction.NORTH, AABB_CONNECTOR_3_MAP), IBooleanFunction.ONLY_FIRST).simplify();
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB_CONNECT_4, Direction.NORTH, AABB_CONNECTOR_4_MAP), IBooleanFunction.ONLY_FIRST).simplify();
            }
            if (actualState.get(SOUTH)) {
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB_CONNECT_1, Direction.SOUTH, AABB_CONNECTOR_1_MAP), IBooleanFunction.ONLY_FIRST).simplify();
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB_CONNECT_2, Direction.SOUTH, AABB_CONNECTOR_2_MAP), IBooleanFunction.ONLY_FIRST).simplify();
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB_CONNECT_3, Direction.SOUTH, AABB_CONNECTOR_3_MAP), IBooleanFunction.ONLY_FIRST).simplify();
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB_CONNECT_4, Direction.SOUTH, AABB_CONNECTOR_4_MAP), IBooleanFunction.ONLY_FIRST).simplify();
            }
            if (actualState.get(EAST)) {
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB_CONNECT_1, Direction.EAST, AABB_CONNECTOR_1_MAP), IBooleanFunction.ONLY_FIRST).simplify();
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB_CONNECT_2, Direction.EAST, AABB_CONNECTOR_2_MAP), IBooleanFunction.ONLY_FIRST).simplify();
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB_CONNECT_3, Direction.EAST, AABB_CONNECTOR_3_MAP), IBooleanFunction.ONLY_FIRST).simplify();
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB_CONNECT_4, Direction.EAST, AABB_CONNECTOR_4_MAP), IBooleanFunction.ONLY_FIRST).simplify();
            }
            if (actualState.get(WEST)) {
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB_CONNECT_1, Direction.WEST, AABB_CONNECTOR_1_MAP), IBooleanFunction.ONLY_FIRST).simplify();
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB_CONNECT_2, Direction.WEST, AABB_CONNECTOR_2_MAP), IBooleanFunction.ONLY_FIRST).simplify();
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB_CONNECT_3, Direction.WEST, AABB_CONNECTOR_3_MAP), IBooleanFunction.ONLY_FIRST).simplify();
                shape = VoxelShapes.combineAndSimplify(shape, rotateWithMap(UP_AABB_CONNECT_4, Direction.WEST, AABB_CONNECTOR_4_MAP), IBooleanFunction.ONLY_FIRST).simplify();
            }
        }
        return shape;
    }

    public VoxelShape getBoundingBox(BlockState state, IWorldReader source, BlockPos pos) {
        List<VoxelShape> aabbs = compileAABBList(source, pos, state);
        VoxelShape bb = Block.makeCuboidShape(0.5, 0.5, 0.5, 0.5, 0.5, 0.5);
        for (VoxelShape box : aabbs) {
            bb = VoxelShapes.combineAndSimplify(bb, box, IBooleanFunction.ONLY_FIRST).simplify();
        }
        return bb;
    }

    public VoxelShape rotateWithMap(VoxelShape aabb, Direction facing, Map<Direction, VoxelShape> checkMap) {
        if (checkMap.get(facing) == null) {
            VoxelShape newAABB = rotateAABB(aabb, facing);
            checkMap.put(facing, newAABB);
            return newAABB;
        } else {
            return checkMap.get(facing);
        }
    }

    @Deprecated
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        // worldIn.scheduleUpdate(pos, this, 1);
    }

    public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand) {
        for (Direction side : Direction.values()) {
            BooleanProperty changing;
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
            if (state.get(changing) && !canBeOpenNextToBlock(worldIn, sideState)) {
                worldIn.setBlockState(pos, state.with(changing, false));
                updateTEOpening(worldIn, pos, side, false);

            }
        }
    }

    private boolean canBeOpenNextToBlock(World worldIn, BlockState sideState) {
        return !(sideState.getBlock() instanceof BlockRatTube);
    }

    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }


    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN, OPEN_NORTH, OPEN_SOUTH, OPEN_EAST, OPEN_WEST, OPEN_UP, OPEN_DOWN);
    }

    public BlockState getActualState(BlockState state, IBlockReader iblockreader, BlockPos blockpos) {
        BlockPos blockpos1 = blockpos.north();
        BlockPos blockpos2 = blockpos.east();
        BlockPos blockpos3 = blockpos.south();
        BlockPos blockpos4 = blockpos.west();
        BlockPos blockpos5 = blockpos.up();
        BlockPos blockpos6 = blockpos.down();
        BlockState blockstate = iblockreader.getBlockState(blockpos1);
        BlockState blockstate1 = iblockreader.getBlockState(blockpos2);
        BlockState blockstate2 = iblockreader.getBlockState(blockpos3);
        BlockState blockstate3 = iblockreader.getBlockState(blockpos4);
        BlockState blockstate4 = iblockreader.getBlockState(blockpos5);
        BlockState blockstate5 = iblockreader.getBlockState(blockpos6);
        return state
                .with(NORTH, Boolean.valueOf(this.canFenceConnectTo(blockstate, blockstate.func_224755_d(iblockreader, blockpos1, Direction.SOUTH), Direction.SOUTH)))
                .with(EAST, Boolean.valueOf(this.canFenceConnectTo(blockstate1, blockstate1.func_224755_d(iblockreader, blockpos2, Direction.WEST), Direction.WEST)))
                .with(SOUTH, Boolean.valueOf(this.canFenceConnectTo(blockstate2, blockstate2.func_224755_d(iblockreader, blockpos3, Direction.NORTH), Direction.NORTH)))
                .with(WEST, Boolean.valueOf(this.canFenceConnectTo(blockstate3, blockstate3.func_224755_d(iblockreader, blockpos4, Direction.EAST), Direction.EAST)))
                .with(UP, Boolean.valueOf(this.canFenceConnectTo(blockstate4, blockstate4.func_224755_d(iblockreader, blockpos5, Direction.DOWN), Direction.DOWN)))
                .with(DOWN, Boolean.valueOf(this.canFenceConnectTo(blockstate5, blockstate5.func_224755_d(iblockreader, blockpos6, Direction.UP), Direction.UP)));

    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        IBlockReader iblockreader = context.getWorld();
        BlockPos blockpos = context.getPos();
        IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
        BlockPos blockpos1 = blockpos.north();
        BlockPos blockpos2 = blockpos.east();
        BlockPos blockpos3 = blockpos.south();
        BlockPos blockpos4 = blockpos.west();
        BlockPos blockpos5 = blockpos.up();
        BlockPos blockpos6 = blockpos.down();
        BlockState blockstate = iblockreader.getBlockState(blockpos1);
        BlockState blockstate1 = iblockreader.getBlockState(blockpos2);
        BlockState blockstate2 = iblockreader.getBlockState(blockpos3);
        BlockState blockstate3 = iblockreader.getBlockState(blockpos4);
        BlockState blockstate4 = iblockreader.getBlockState(blockpos5);
        BlockState blockstate5 = iblockreader.getBlockState(blockpos6);
        return super.getStateForPlacement(context)
                .with(NORTH, Boolean.valueOf(this.canFenceConnectTo(blockstate, blockstate.func_224755_d(iblockreader, blockpos1, Direction.SOUTH), Direction.SOUTH)))
                .with(EAST, Boolean.valueOf(this.canFenceConnectTo(blockstate1, blockstate1.func_224755_d(iblockreader, blockpos2, Direction.WEST), Direction.WEST)))
                .with(SOUTH, Boolean.valueOf(this.canFenceConnectTo(blockstate2, blockstate2.func_224755_d(iblockreader, blockpos3, Direction.NORTH), Direction.NORTH)))
                .with(WEST, Boolean.valueOf(this.canFenceConnectTo(blockstate3, blockstate3.func_224755_d(iblockreader, blockpos4, Direction.EAST), Direction.EAST)))
                .with(UP, Boolean.valueOf(this.canFenceConnectTo(blockstate4, blockstate4.func_224755_d(iblockreader, blockpos5, Direction.DOWN), Direction.DOWN)))
                .with(DOWN, Boolean.valueOf(this.canFenceConnectTo(blockstate5, blockstate5.func_224755_d(iblockreader, blockpos6, Direction.UP), Direction.UP)));
    }

    private boolean canFenceConnectTo(BlockState p_220111_1_, boolean p_220111_2_, Direction p_220111_3_) {
        return p_220111_1_.getBlock() instanceof BlockRatCage || p_220111_1_.getBlock() instanceof BlockRatTube;
    }

    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    public boolean isFullCube(BlockState state) {
        return false;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit) {
        if (playerIn.isSneaking() || playerIn.getHeldItem(hand).getItem() instanceof BlockItem) {
            return false;
        } else {
            Direction side = hit.getFace();
            BooleanProperty changing;
            BooleanProperty[] allOpenVars = new BooleanProperty[]{OPEN_DOWN, OPEN_EAST, OPEN_NORTH, OPEN_SOUTH, OPEN_UP, OPEN_WEST};
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
            for (BooleanProperty opened : allOpenVars) {
                if (state.get(opened)) {
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

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tile.rats.rat_tube.desc0"));
        tooltip.add(I18n.format("tile.rats.rat_tube.desc1"));
        tooltip.add(I18n.format("tile.rats.rat_tube.desc2"));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityRatTube();
    }
}
