package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatQuarry;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BlockRatQuarry extends ContainerBlock {

    public BlockRatQuarry() {
        super(Block.Properties.create(Material.WOOD).sound(SoundType.SLIME).hardnessAndResistance(2.0F, 0.0F));
        this.setRegistryName(RatsMod.MODID, "rat_quarry");
    }

    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityRatQuarry) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityRatQuarry) tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityRatQuarry();
    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!player.isSneaking()){
            if(worldIn.isRemote){
                RatsMod.PROXY.setRefrencedTE(worldIn.getTileEntity(pos));
            }else{
                INamedContainerProvider inamedcontainerprovider = this.getContainer(state, worldIn, pos);
                if (inamedcontainerprovider != null) {
                    player.openContainer(inamedcontainerprovider);
                }
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("block.rats.rat_quarry.desc0").func_240699_a_(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("block.rats.rat_quarry.desc1").func_240699_a_(TextFormatting.GRAY));
    }
}
