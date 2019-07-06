package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatTrap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class BlockRatTrap extends BlockContainer {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    private static final AxisAlignedBB NS_AABB = new AxisAlignedBB(0.1875F, 0F, 0F, 0.8125F, 0.15F, 1F);
    private static final AxisAlignedBB EW_AABB = new AxisAlignedBB(0F, 0F, 0.1875, 1, 0.15F, 0.8125F);

    protected BlockRatTrap() {
        super(Material.WOOD);
        this.setHardness(1.0F);
        this.setTickRandomly(true);
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.rat_trap");
        this.setRegistryName(RatsMod.MODID, "rattrap");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        GameRegistry.registerTileEntity(TileEntityRatTrap.class, "rats.rat_trap");
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityRatTrap) {
            if (!worldIn.isRemote && !((TileEntityRatTrap) tileentity).getBait().isEmpty()) {
                worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, ((TileEntityRatTrap) tileentity).getBait()));
            }
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(worldIn, pos, state);
    }

    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        if (entityIn != null && entityIn instanceof EntityRat) {
            return;
        } else {
            super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
        }
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        IBlockState actualState = getActualState(state, source, pos);
        if (actualState.getBlock() instanceof BlockRatTrap) {
            if (actualState.getValue(FACING).getAxis() == EnumFacing.Axis.X) {
                return EW_AABB;
            } else {
                return NS_AABB;
            }
        }
        return NS_AABB;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos) && worldIn.isSideSolid(pos.down(), EnumFacing.UP);
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

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }


    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = playerIn.getHeldItem(hand);
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileEntityRatTrap) {
            TileEntityRatTrap ratTrap = (TileEntityRatTrap) tile;
            if (ratTrap.isShut) {
                ratTrap.isShut = false;
                worldIn.playSound(null, pos, SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN, SoundCategory.BLOCKS, 1F, 1F);
                return true;
            }
            if (ratTrap.getBait().isEmpty() && RatUtils.isRatFood(itemstack)) {
                ratTrap.setBaitStack(itemstack.copy());
                if (!playerIn.isCreative()) {
                    itemstack.setCount(0);
                }
                return true;
            }
            if (!ratTrap.getBait().isEmpty()) {
                if (!RatUtils.isRatFood(itemstack)) {

                }
                if (!worldIn.isRemote) {
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, ratTrap.getBait()));
                }
                ratTrap.setBaitStack(ItemStack.EMPTY);
                return true;

            }
        }
        return false;
    }

    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        TileEntity tile = blockAccess.getTileEntity(pos);
        if (tile instanceof TileEntityRatTrap) {
            TileEntityRatTrap ratTrap = (TileEntityRatTrap) tile;
            return ratTrap.calculateRedstone();
        }
        return 0;
    }

    @Deprecated
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return getWeakPower(blockState, blockAccess, pos, side);
    }

    @Deprecated
    public boolean canProvidePower(IBlockState state) {
        return true;
    }


    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileEntityRatTrap) {
            TileEntityRatTrap ratTrap = (TileEntityRatTrap) tile;
            return ratTrap.calculateRedstone();
        }
        return 0;
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (worldIn.isBlockPowered(pos)) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof TileEntityRatTrap) {
                TileEntityRatTrap ratTrap = (TileEntityRatTrap) tile;
                ratTrap.onRedstonePulse();
            }
        }
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityRatTrap();
    }
}
