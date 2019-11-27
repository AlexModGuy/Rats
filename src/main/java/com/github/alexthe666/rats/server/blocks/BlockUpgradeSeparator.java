package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityUpgradeCombiner;
import com.github.alexthe666.rats.server.entity.tile.TileEntityUpgradeSeparator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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

public class BlockUpgradeSeparator extends BlockContainer {
    protected static final AxisAlignedBB AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);

    public BlockUpgradeSeparator() {
        super(Material.ROCK, MapColor.WHITE_STAINED_HARDENED_CLAY);
        this.setHardness(5.0F);
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.upgrade_separator");
        this.setRegistryName(RatsMod.MODID, "upgrade_separator");
        this.setLightLevel(0.6F);
        GameRegistry.registerTileEntity(TileEntityUpgradeSeparator.class, "rats.upgrade_separator");
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

    public EnumBlockRenderType getRenderType(BlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tile.rats.upgrade_separator.desc"));
    }
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityUpgradeSeparator();
    }
}
