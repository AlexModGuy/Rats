package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockBrain extends BlockHorizontal {

    public BlockBrain() {
        super(Material.CLOTH);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
        this.setCreativeTab(RatsMod.TAB);
        this.setSoundType(SoundType.SLIME);
        this.setHardness(4.0F);
        this.setTranslationKey("rats.brain_block");
        this.setRegistryName(RatsMod.MODID, "brain_block");
    }

    public BlockState withRotation(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState withMirror(BlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    public BlockState getStateForPlacement(World worldIn, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().with(FACING, placer.getHorizontalFacing().getOpposite());
    }

    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().with(FACING, Direction.byHorizontalIndex(meta));
    }

    public int getMetaFromState(BlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    }

}