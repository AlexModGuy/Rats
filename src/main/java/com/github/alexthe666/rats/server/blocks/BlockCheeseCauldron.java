package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class BlockCheeseCauldron extends Block {

    public BlockCheeseCauldron() {
        super(Block.Properties.create(Material.IRON).sound(SoundType.STONE).hardnessAndResistance(2.0F, 0.0F));
        this.setRegistryName(RatsMod.MODID, "cauldron_cheese");
    }

    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    public boolean isFullCube(BlockState state) {
        return false;
    }

    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit) {
        worldIn.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
        worldIn.playSound(null, pos, SoundEvents.ENTITY_SLIME_SQUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);
        worldIn.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
        if (!playerIn.inventory.addItemStackToInventory(new ItemStack(RatsBlockRegistry.BLOCK_OF_CHEESE))) {
            playerIn.dropItem(new ItemStack(RatsBlockRegistry.BLOCK_OF_CHEESE), false);
        }
        return true;
    }
}
