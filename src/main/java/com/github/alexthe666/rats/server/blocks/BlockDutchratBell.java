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
    }

    @Override
    public boolean ring(World worldIn, BlockState state, @Nullable TileEntity p_220130_3_, BlockRayTraceResult p_220130_4_, @Nullable PlayerEntity ringingPlayer, boolean p_220130_6_) {
        Direction direction = p_220130_4_.getFace();
        BlockPos blockpos = p_220130_4_.getPos();
        boolean flag = !p_220130_6_ || this.func_220129_a(state, direction, p_220130_4_.getHitVec().y - (double)blockpos.getY());
        if (!worldIn.isRemote && p_220130_3_ instanceof TileEntityDutchratBell && flag) {
            ((TileEntityDutchratBell)p_220130_3_).func_213939_a(direction);
            this.playRingSound(worldIn, blockpos);
            if (ringingPlayer != null) {
                ringingPlayer.addStat(Stats.BELL_RING);
            }

            return true;
        } else {
            return true;
        }
    }

    private boolean func_220129_a(BlockState p_220129_1_, Direction p_220129_2_, double p_220129_3_) {
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
