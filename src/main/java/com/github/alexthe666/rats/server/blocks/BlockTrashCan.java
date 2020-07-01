package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityTrashCan;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTrashCan extends ContainerBlock implements IUsesTEISR {

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    private static final VoxelShape AABB = Block.makeCuboidShape(0, 0, 0, 16, 20, 16);

    public BlockTrashCan() {
        super(Properties.create(Material.IRON).sound(SoundType.METAL).notSolid().variableOpacity().hardnessAndResistance(2.0F, 0.0F));
        this.setRegistryName(RatsMod.MODID, "trash_can");
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("block.rats.trash_can.desc0").func_240699_a_(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("block.rats.trash_can.desc1").func_240699_a_(TextFormatting.GRAY));
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AABB;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if(!player.isSneaking()){
            boolean flag = false;
            ItemStack stack = player.getHeldItem(hand);
            TileEntity te = worldIn.getTileEntity(pos);
            if(te instanceof TileEntityTrashCan){
                TileEntityTrashCan trashCan = (TileEntityTrashCan)te;
                if(!trashCan.opened){
                    worldIn.playSound(player, pos, RatsSoundRegistry.TRASH_CAN, SoundCategory.BLOCKS, 0.5F, 0.75F + worldIn.rand.nextFloat() * 0.5F);
                }
                trashCan.opened = true;
                if(!stack.isEmpty() && stack.getItem() instanceof BlockItem){
                    if(trashCan.trashStored < 7){
                        if(!player.isCreative()){
                            stack.shrink(1);
                        }
                        trashCan.trashStored++;
                    }
                }
            }
            return ActionResultType.SUCCESS;
            }
        return ActionResultType.PASS;
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
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

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityTrashCan();
    }


}
