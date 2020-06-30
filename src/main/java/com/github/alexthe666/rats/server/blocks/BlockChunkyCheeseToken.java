package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityToken;
import com.github.alexthe666.rats.server.entity.tile.TileEntityTrashCan;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BlockChunkyCheeseToken extends ContainerBlock {

    private static final VoxelShape AABB = Block.makeCuboidShape(4, 4, 4, 12, 12, 12);

    public BlockChunkyCheeseToken() {
        super(Properties.create(Material.IRON).sound(SoundType.METAL).notSolid().variableOpacity().hardnessAndResistance(2.0F, 1000.0F));
        this.setRegistryName(RatsMod.MODID, "chunky_cheese_token");
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("block.rats.chunky_cheese_token.desc0").func_240699_a_(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("block.rats.chunky_cheese_token.desc1").func_240699_a_(TextFormatting.GRAY));
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AABB;
    }

    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    public boolean isFullCube(BlockState state) {
        return false;
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityToken();
    }
}
