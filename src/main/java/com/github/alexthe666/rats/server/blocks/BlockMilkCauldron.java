package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityMilkCauldron;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockMilkCauldron extends ContainerBlock {
    private static final VoxelShape INSIDE = makeCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.or(makeCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), makeCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE), IBooleanFunction.ONLY_FIRST);

    public BlockMilkCauldron() {
        super(Block.Properties.create(Material.IRON).sound(SoundType.STONE).hardnessAndResistance(2.0F, 0.0F));
        this.setRegistryName(RatsMod.MODID, "cauldron_milk");
        //GameRegistry.registerTileEntity(TileEntityMilkCauldron.class, "rats.cauldron_milk");
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return INSIDE;
    }

    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    public boolean isFullCube(BlockState state) {
        return false;
    }

    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        float f = (float) pos.getY() + (6.0F + 3) / 16.0F;
        if (!worldIn.isRemote && entityIn.isBurning() && entityIn.getBoundingBox().minY <= (double) f) {
            entityIn.extinguish();
        }
    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit) {
        ItemStack itemstack = playerIn.getHeldItem(hand);
        if (itemstack.isEmpty()) {
            return ActionResultType.SUCCESS;
        } else {
            int i = 0;
            Item item = itemstack.getItem();

            if (item == Items.BUCKET) {
                if (!worldIn.isRemote) {
                    if (!playerIn.isCreative()) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            playerIn.setHeldItem(hand, new ItemStack(Items.MILK_BUCKET));
                        } else if (!playerIn.inventory.addItemStackToInventory(new ItemStack(Items.MILK_BUCKET))) {
                            playerIn.dropItem(new ItemStack(Items.MILK_BUCKET), false);
                        }
                    }
                    worldIn.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
                    worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }


    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityMilkCauldron();
    }
}
