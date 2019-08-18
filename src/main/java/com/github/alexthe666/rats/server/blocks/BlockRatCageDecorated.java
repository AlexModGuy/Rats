package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCageDecorated;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockRatCageDecorated extends BlockRatCage implements ITileEntityProvider {

    public BlockRatCageDecorated() {
        super();
        this.setTranslationKey("rats.rat_cage_decorated");
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(NORTH, Integer.valueOf(0))
                .withProperty(EAST, Integer.valueOf(0))
                .withProperty(SOUTH, Integer.valueOf(0))
                .withProperty(WEST, Integer.valueOf(0))
                .withProperty(UP, Integer.valueOf(0))
                .withProperty(DOWN, Integer.valueOf(0))
        );
        this.setRegistryName(RatsMod.MODID, "rat_cage_decorated");
        this.hasTileEntity = true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityRatCageDecorated();
    }
}
