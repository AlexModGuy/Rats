package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatTrap;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockRatLantern extends Block {

    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.1875D, 0.3125D, 0.1875D, 0.8125, 1D, 0.8125);

    public BlockRatLantern() {
        super(Material.CLOTH);
        this.setHardness(1.0F);
        this.setSoundType(SoundType.CLOTH);
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.rat_lantern");
        this.setRegistryName(RatsMod.MODID, "rat_lantern");
        this.setLightLevel(1.0F);
    }


    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos) && canStay(worldIn.getBlockState(pos.up()), worldIn, pos);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!canStay(worldIn.getBlockState(pos.up()), worldIn, pos.up())) {
            worldIn.destroyBlock(pos, true);
        }
    }

    protected boolean canStay(IBlockState state, World world, BlockPos pos) {
        return state.getBlock().canPlaceTorchOnTop(state, world, pos) || world.isSideSolid(pos.up(), EnumFacing.DOWN);
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
}
