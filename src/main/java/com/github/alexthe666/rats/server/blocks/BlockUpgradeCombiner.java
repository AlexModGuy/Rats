package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityUpgradeCombiner;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class BlockUpgradeCombiner extends BlockContainer {
    protected static final AxisAlignedBB AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);

    public BlockUpgradeCombiner() {
        super(Material.ROCK, MapColor.WHITE_STAINED_HARDENED_CLAY);
        this.setHardness(5.0F);
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.upgrade_combiner");
        this.setRegistryName(RatsMod.MODID, "upgrade_combiner");
        this.setLightLevel(0.6F);
        GameRegistry.registerTileEntity(TileEntityUpgradeCombiner.class, "rats.upgrade_combiner");
    }

    public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB);
    }

    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    public boolean isFullCube(BlockState state) {
        return false;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face) {
        return face == Direction.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, BlockState state, EntityPlayer playerIn, EnumHand hand, Direction facing, float hitX, float hitY, float hitZ) {
        if (playerIn.isSneaking()) {
            return false;
        } else {
            playerIn.openGui(RatsMod.INSTANCE, 4, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
    }

    public EnumBlockRenderType getRenderType(BlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityUpgradeCombiner();
    }
}
