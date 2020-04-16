package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatTrap;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockRatTrap extends ContainerBlock implements IUsesTEISR {
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    private static final VoxelShape NS_AABB = Block.makeCuboidShape(3, 0, 0, 13, 4, 16);
    private static final VoxelShape EW_AABB = Block.makeCuboidShape(0, 0, 3, 16, 4, 13);

    protected BlockRatTrap() {
        super(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(1.0F, 0.0F));
        this.setRegistryName(RatsMod.MODID, "rat_trap");
        setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
        //GameRegistry.registerTileEntity(TileEntityRatTrap.class, "rats.rat_trap");
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityRatTrap) {
            if (!worldIn.isRemote && !((TileEntityRatTrap) tileentity).getBait().isEmpty()) {
                worldIn.addEntity(new ItemEntity(worldIn, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, ((TileEntityRatTrap) tileentity).getBait()));
            }
            worldIn.updateComparatorOutputLevel(pos, this);
        }
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return state.get(FACING).getAxis() == Direction.Axis.Z ? NS_AABB : EW_AABB;
    }

    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.down();
        return this.isValidGround(worldIn.getBlockState(blockpos), worldIn, blockpos);
    }

    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return state.isOpaqueCube(worldIn, pos);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    public boolean isFullCube(BlockState state) {
        return false;
    }


    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit) {
        ItemStack itemstack = playerIn.getHeldItem(hand);
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileEntityRatTrap) {
            TileEntityRatTrap ratTrap = (TileEntityRatTrap) tile;
            if (ratTrap.isShut) {
                ratTrap.isShut = false;
                worldIn.playSound(null, pos, SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN, SoundCategory.BLOCKS, 1F, 1F);
                return ActionResultType.SUCCESS;
            }
            if (ratTrap.getBait().isEmpty() && RatUtils.isRatFood(itemstack)) {
                ratTrap.setBaitStack(itemstack.copy());
                if (!playerIn.isCreative()) {
                    itemstack.setCount(0);
                }
                return ActionResultType.SUCCESS;
            }
            if (!ratTrap.getBait().isEmpty()) {
                if (!RatUtils.isRatFood(itemstack)) {

                }
                if (!worldIn.isRemote) {
                    worldIn.addEntity(new ItemEntity(worldIn, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, ratTrap.getBait()));
                }
                ratTrap.setBaitStack(ItemStack.EMPTY);
                return ActionResultType.SUCCESS;

            }
        }
        return ActionResultType.FAIL;
    }

    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        TileEntity tile = blockAccess.getTileEntity(pos);
        if (tile instanceof TileEntityRatTrap) {
            TileEntityRatTrap ratTrap = (TileEntityRatTrap) tile;
            return ratTrap.calculateRedstone();
        }
        return 0;
    }

    @Deprecated
    public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return getWeakPower(blockState, blockAccess, pos, side);
    }

    @Deprecated
    public boolean canProvidePower(BlockState state) {
        return true;
    }


    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileEntityRatTrap) {
            TileEntityRatTrap ratTrap = (TileEntityRatTrap) tile;
            return ratTrap.calculateRedstone();
        }
        return 0;
    }

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (worldIn.isBlockPowered(pos)) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof TileEntityRatTrap) {
                TileEntityRatTrap ratTrap = (TileEntityRatTrap) tile;
                ratTrap.onRedstonePulse();
            }
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityRatTrap();
    }
}
