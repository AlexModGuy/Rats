package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.server.advancements.RatCageDecoTrigger;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCageDecorated;
import com.github.alexthe666.rats.server.items.IRatCageDecoration;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockRatCageDecorated extends BlockRatCage implements ITileEntityProvider {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final RatCageDecoTrigger DECO_TRIGGER = CriteriaTriggers.register(new RatCageDecoTrigger());

    public BlockRatCageDecorated() {
        super("rat_cage_decorated");
        this.setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(NORTH, Integer.valueOf(0))
                .withProperty(EAST, Integer.valueOf(0))
                .withProperty(SOUTH, Integer.valueOf(0))
                .withProperty(WEST, Integer.valueOf(0))
                .withProperty(UP, Integer.valueOf(0))
                .withProperty(DOWN, Integer.valueOf(0))
                .withProperty(FACING, EnumFacing.NORTH)
        );
        this.hasTileEntity = true;
        this.setCreativeTab(null);
        GameRegistry.registerTileEntity(TileEntityRatCageDecorated.class, "rats.rat_cage_decorated");
    }

    @Deprecated
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        worldIn.scheduleUpdate(pos, this, 1);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isAreaLoaded(pos, 1)) return;
        if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityRatCageDecorated) {
            TileEntityRatCageDecorated te = (TileEntityRatCageDecorated) worldIn.getTileEntity(pos);
            if (te.getContainedItem() != null && te.getContainedItem().getItem() instanceof IRatCageDecoration && !((IRatCageDecoration) te.getContainedItem().getItem()).canStay(worldIn, pos, this)) {
                EntityItem entityItem = new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, te.getContainedItem());
                if (!worldIn.isRemote && worldIn.getBlockState(pos).getBlock() == RatsBlockRegistry.RAT_CAGE) {
                    worldIn.spawnEntity(entityItem);
                }
                te.setContainedItem(ItemStack.EMPTY);
                worldIn.setBlockState(pos, RatsBlockRegistry.RAT_CAGE.getDefaultState());
            }
        }
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
        return new BlockStateContainer(this, NORTH, SOUTH, EAST, WEST, UP, DOWN, FACING);
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityRatCageDecorated) {
            TileEntityRatCageDecorated te = (TileEntityRatCageDecorated) worldIn.getTileEntity(pos);
            if (te.getContainedItem() != null && !te.getContainedItem().isEmpty()) {
                EntityItem entityItem = new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, te.getContainedItem());
                if (!worldIn.isRemote) {
                    worldIn.spawnEntity(entityItem);
                }
            }
        }
        worldIn.removeTileEntity(pos);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityRatCageDecorated();
    }
}
