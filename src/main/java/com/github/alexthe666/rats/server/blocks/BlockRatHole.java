package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.server.entity.tile.TileEntityRatHole;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class BlockRatHole extends ContainerBlock implements IUsesTEISR {

    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");

    private static final VoxelShape TOP_AABB = Block.makeCuboidShape(0, 8, 0, 16, 16, 16);
    private static final VoxelShape NS_LEFT_AABB = Block.makeCuboidShape(0, 0, 0, 4, 8, 16);
    private static final VoxelShape NS_RIGHT_AABB = Block.makeCuboidShape(12, 0, 0, 16, 8, 16);
    private static final VoxelShape EW_LEFT_AABB = Block.makeCuboidShape(0, 0, 0, 16, 8, 4);
    private static final VoxelShape EW_RIGHT_AABB =  Block.makeCuboidShape(0, 0, 12, 16, 8, 16);
    private static final VoxelShape NORTH_CORNER_AABB = Block.makeCuboidShape(0, 0, 0, 4, 8, 4);
    private static final VoxelShape EAST_CORNER_AABB = Block.makeCuboidShape(12, 0, 0, 16, 8, 4);
    private static final VoxelShape SOUTH_CORNER_AABB = Block.makeCuboidShape(0, 0, 12, 4, 8, 16);
    private static final VoxelShape WEST_CORNER_AABB = Block.makeCuboidShape(12, 0, 12, 16, 8, 16);
    private VoxelShape shape;

    protected BlockRatHole() {
        super(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(1.3F, 0.0F));
        setDefaultState(this.stateContainer.getBaseState()
                .with(NORTH, Boolean.valueOf(false))
                .with(EAST, Boolean.valueOf(false))
                .with(SOUTH, Boolean.valueOf(false))
                .with(WEST, Boolean.valueOf(false))
        );
        shape = VoxelShapes.combineAndSimplify(TOP_AABB, NORTH_CORNER_AABB, IBooleanFunction.OR).simplify();
        shape = VoxelShapes.combineAndSimplify(shape, SOUTH_CORNER_AABB, IBooleanFunction.OR).simplify();
        shape = VoxelShapes.combineAndSimplify(shape, EAST_CORNER_AABB, IBooleanFunction.OR).simplify();
        shape = VoxelShapes.combineAndSimplify(shape, WEST_CORNER_AABB, IBooleanFunction.OR).simplify();
        this.setRegistryName("rats:rat_hole");
        //GameRegistry.registerTileEntity(TileEntityRatHole.class, "rats.rat_hole");
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    public boolean isFullCube(BlockState state) {
        return false;
    }

    public int getMetaFromState(BlockState state) {
        return 0;
    }

    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityRatHole) {
            TileEntityRatHole te = (TileEntityRatHole) worldIn.getTileEntity(pos);
            NonNullList<ItemStack> ret = NonNullList.create();
            if (!worldIn.isRemote && worldIn instanceof ServerWorld) {
                te.getImmitatedBlockState().getBlock().getDrops(te.getImmitatedBlockState(), (ServerWorld) worldIn, pos, null);
                for (ItemStack stack : ret) {
                    ItemEntity ItemEntity = new ItemEntity(worldIn, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
                    worldIn.addEntity(ItemEntity);
                }
            }
        }
        worldIn.removeTileEntity(pos);
    }


    public BlockState getStateForPlacement(BlockItemUseContext context) {
        IBlockReader iblockreader = context.getWorld();
        BlockPos blockpos = context.getPos();
        IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
        BlockPos blockpos1 = blockpos.north();
        BlockPos blockpos2 = blockpos.east();
        BlockPos blockpos3 = blockpos.south();
        BlockPos blockpos4 = blockpos.west();
        BlockState blockstate = iblockreader.getBlockState(blockpos1);
        BlockState blockstate1 = iblockreader.getBlockState(blockpos2);
        BlockState blockstate2 = iblockreader.getBlockState(blockpos3);
        BlockState blockstate3 = iblockreader.getBlockState(blockpos4);
        return super.getStateForPlacement(context).with(NORTH, Boolean.valueOf(this.canFenceConnectTo(blockstate, blockstate.canBeConnectedTo(iblockreader, blockpos1, Direction.SOUTH), Direction.SOUTH))).with(EAST, Boolean.valueOf(this.canFenceConnectTo(blockstate1, blockstate1.canBeConnectedTo(iblockreader, blockpos2, Direction.WEST), Direction.WEST))).with(SOUTH, Boolean.valueOf(this.canFenceConnectTo(blockstate2, blockstate2.canBeConnectedTo(iblockreader, blockpos3, Direction.NORTH), Direction.NORTH))).with(WEST, Boolean.valueOf(this.canFenceConnectTo(blockstate3, blockstate3.canBeConnectedTo(iblockreader, blockpos4, Direction.EAST), Direction.EAST)));
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        VoxelShape shape1 = shape;
        if (state.getBlock() instanceof BlockRatHole) {
            if (state.get(NORTH)) {
                shape1 = VoxelShapes.combine(shape1, EW_LEFT_AABB, IBooleanFunction.OR);
            }
            if (state.get(SOUTH)) {
                shape1 = VoxelShapes.combine(shape1, EW_RIGHT_AABB, IBooleanFunction.OR);
            }
            if (state.get(WEST)) {
                shape1 = VoxelShapes.combine(shape1, NS_LEFT_AABB, IBooleanFunction.OR);
            }
            if (state.get(EAST)) {
                shape1 = VoxelShapes.combine(shape1, NS_RIGHT_AABB, IBooleanFunction.OR);
            }
        }
        return shape1;
    }


    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, WEST, SOUTH);
    }


    private boolean canFenceConnectTo(BlockState p_220111_1_, boolean p_220111_2_, Direction p_220111_3_) {
        Block block = p_220111_1_.getBlock();
        boolean flag = p_220111_1_.getMaterial() == this.material;
        return !cannotAttach(block) && p_220111_2_ && block != this;
    }

    public SoundType getSoundType(BlockState state) {
        /*if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityRatHole) {
            TileEntityRatHole te = (TileEntityRatHole) world.getTileEntity(pos);
            return te.getImmitatedBlockState().getBlock().getSoundType();
        }*/
        return SoundType.WOOD;
    }


    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityRatHole();
    }

    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        BooleanProperty connect = null;
        switch (facing) {
            case NORTH:
                connect = NORTH;
                break;
            case SOUTH:
                connect = SOUTH;
                break;
            case EAST:
                connect = EAST;
                break;
            case WEST:
                connect = WEST;
                break;
        }
        if(connect == null){
            return stateIn;
        }
        return stateIn.with(connect, Boolean.valueOf(this.canFenceConnectTo(facingState, facingState.canBeConnectedTo(worldIn, facingPos, facing.getOpposite()), facing.getOpposite())));
    }
}