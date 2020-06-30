package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.server.entity.tile.TileEntityDutchratBell;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.minecraft.block.BellBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.properties.BellAttachment;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.BellTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockDutchratBell extends BellBlock {

    public BlockDutchratBell() {
        super(Block.Properties.create(Material.IRON, MaterialColor.GOLD).hardnessAndResistance(5.0F).sound(SoundType.ANVIL));
        this.setRegistryName("rats:dutchrat_bell");
        this.setDefaultState(this.stateContainer.getBaseState().with(field_220133_a, Direction.NORTH).with(BlockStateProperties.BELL_ATTACHMENT, BellAttachment.FLOOR).with(POWERED, Boolean.valueOf(false)));
    }

    @Override
    public boolean func_226884_a_(World p_226884_1_, BlockState p_226884_2_, BlockRayTraceResult p_226884_3_, @Nullable PlayerEntity p_226884_4_, boolean p_226884_5_) {
        Direction direction = p_226884_3_.getFace();
        BlockPos blockpos = p_226884_3_.getPos();
        boolean flag = !p_226884_5_ || this.canRingFrom(p_226884_2_, direction, p_226884_3_.getHitVec().y - (double)blockpos.getY());
        if (flag) {
            boolean flag1 = this.func_226885_a_(p_226884_1_, blockpos, direction);
            if (flag1 && p_226884_4_ != null) {
                p_226884_4_.addStat(Stats.BELL_RING);
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean func_226885_a_(World p_226885_1_, BlockPos p_226885_2_, @Nullable Direction p_226885_3_) {
        TileEntity tileentity = p_226885_1_.getTileEntity(p_226885_2_);
        if (!p_226885_1_.isRemote && tileentity instanceof TileEntityDutchratBell) {
            if (p_226885_3_ == null) {
                p_226885_3_ = p_226885_1_.getBlockState(p_226885_2_).get(field_220133_a);
            }
            playRingSound(p_226885_1_, p_226885_2_);
            ((TileEntityDutchratBell)tileentity).func_213939_a(p_226885_3_);
            return true;
        } else {
            return false;
        }
    }

    private boolean canRingFrom(BlockState p_220129_1_, Direction p_220129_2_, double p_220129_3_) {
        if (p_220129_2_.getAxis() != Direction.Axis.Y && !(p_220129_3_ > (double)0.8124F)) {
            Direction direction = p_220129_1_.get(field_220133_a);
            BellAttachment bellattachment = p_220129_1_.get(BlockStateProperties.BELL_ATTACHMENT);
            switch(bellattachment) {
                case FLOOR:
                    return direction.getAxis() == p_220129_2_.getAxis();
                case SINGLE_WALL:
                case DOUBLE_WALL:
                    return direction.getAxis() != p_220129_2_.getAxis();
                case CEILING:
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    private void playRingSound(World worldIn, BlockPos pos) {
        if(worldIn.isDaytime()){
            worldIn.playSound((PlayerEntity)null, pos, SoundEvents.BLOCK_BELL_USE, SoundCategory.BLOCKS, 2.0F, 1.0F);

        }else{
            worldIn.playSound((PlayerEntity)null, pos, RatsSoundRegistry.DUTCHRAT_BELL, SoundCategory.BLOCKS, 2.0F, 1.0F);

        }
    }

    @Nullable
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityDutchratBell();
    }

}
