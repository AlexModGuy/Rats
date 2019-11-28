package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.server.advancements.RatCageDecoTrigger;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCageDecorated;
import com.github.alexthe666.rats.server.items.IRatCageDecoration;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockRatCageDecorated extends BlockRatCage implements ITileEntityProvider {

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    public static final RatCageDecoTrigger DECO_TRIGGER = CriteriaTriggers.register(new RatCageDecoTrigger());

    public BlockRatCageDecorated() {
        super("rat_cage_decorated");
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(NORTH, Integer.valueOf(0))
                .with(EAST, Integer.valueOf(0))
                .with(SOUTH, Integer.valueOf(0))
                .with(WEST, Integer.valueOf(0))
                .with(UP, Integer.valueOf(0))
                .with(DOWN, Integer.valueOf(0))
                .with(FACING, Direction.NORTH)
        );
        //GameRegistry.registerTileEntity(TileEntityRatCageDecorated.class, "rats.rat_cage_decorated");
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand) {
        if (!worldIn.isAreaLoaded(pos, 1)) return;
        if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityRatCageDecorated) {
            TileEntityRatCageDecorated te = (TileEntityRatCageDecorated) worldIn.getTileEntity(pos);
            if (te.getContainedItem() != null && te.getContainedItem().getItem() instanceof IRatCageDecoration && !((IRatCageDecoration) te.getContainedItem().getItem()).canStay(worldIn, pos, this)) {
                ItemEntity ItemEntity = new ItemEntity(worldIn, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, te.getContainedItem());
                if (!worldIn.isRemote && worldIn.getBlockState(pos).getBlock() == RatsBlockRegistry.RAT_CAGE) {
                    worldIn.addEntity(ItemEntity);
                }
                te.setContainedItem(ItemStack.EMPTY);
                worldIn.setBlockState(pos, RatsBlockRegistry.RAT_CAGE.getDefaultState());
            }
        }
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }


    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN, FACING);
    }

    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityRatCageDecorated) {
            TileEntityRatCageDecorated te = (TileEntityRatCageDecorated) worldIn.getTileEntity(pos);
            if (te.getContainedItem() != null && !te.getContainedItem().isEmpty()) {
                ItemEntity ItemEntity = new ItemEntity(worldIn, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, te.getContainedItem());
                if (!worldIn.isRemote) {
                    worldIn.addEntity(ItemEntity);
                }
            }
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityRatCageDecorated();
    }
}
