package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatAttractor;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockRatAttractor extends ContainerBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty CONNECTED_UP = BooleanProperty.create("up");
    private static final VoxelShape AABB = Block.makeCuboidShape(4, 0, 4, 12, 16, 12);
    private static final VoxelShape AABB_NONE = Block.makeCuboidShape(4, 0, 4, 12, 8, 12);

    protected BlockRatAttractor() {
        super(Properties.create(Material.IRON).sound(SoundType.LANTERN).notSolid().variableOpacity().tickRandomly().hardnessAndResistance(2.0F, 0.0F));
        this.setRegistryName(RatsMod.MODID, "rat_attractor");
        this.setDefaultState(this.stateContainer.getBaseState().with(POWERED, Boolean.valueOf(false)).with(CONNECTED_UP, Boolean.valueOf(false)));
    }

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if(!worldIn.isRemote){
            this.updateState(state, worldIn, pos, blockIn);
        }
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return state.get(CONNECTED_UP) ? AABB : AABB_NONE;
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("block.rats.rat_attractor.desc0").func_240699_a_(TextFormatting.GRAY));
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public int tickRate(IWorldReader worldIn) {
        return 2;
    }


    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if(!worldIn.isRemote){
            this.updateState(state, worldIn, pos, state.getBlock());
        }
    }

    public void updateState(BlockState state, World worldIn, BlockPos pos, Block blockIn) {
        boolean flag = state.get(POWERED);
        boolean flag1 = worldIn.isBlockPowered(pos);

        boolean flag2 = state.get(CONNECTED_UP);
        boolean flag3 = !worldIn.getBlockState(pos.up()).isAir();

        if (flag1 != flag) {
            worldIn.setBlockState(pos, state.with(POWERED, Boolean.valueOf(flag1)).with(CONNECTED_UP, Boolean.valueOf(flag3)), 3);
            worldIn.notifyNeighborsOfStateChange(pos.down(), this);
        }
        if(flag2 != flag3){
            worldIn.setBlockState(pos, state.with(CONNECTED_UP, Boolean.valueOf(flag3)), 3);
        }

    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(POWERED, CONNECTED_UP);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityRatAttractor();
    }
}
