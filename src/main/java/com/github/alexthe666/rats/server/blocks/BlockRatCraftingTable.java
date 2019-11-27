package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCraftingTable;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;

public class BlockRatCraftingTable extends ContainerBlock {

    public BlockRatCraftingTable() {
        super(Block.Properties.create(Material.WOOD).sound(SoundType.SLIME).hardnessAndResistance(2.0F, 0.0F));
        this.setRegistryName(RatsMod.MODID, "rat_crafting_table");
        //        GameRegistry.registerTileEntity(TileEntityRatCraftingTable.class, "rats.rat_crafting_table");
    }

    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityRatCraftingTable) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityRatCraftingTable) tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
    }


    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit) {
        if (playerIn.isSneaking()) {
            return false;
        } else {
            //playerIn.openGui(RatsMod.INSTANCE, 2, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityRatCraftingTable();
    }


}
