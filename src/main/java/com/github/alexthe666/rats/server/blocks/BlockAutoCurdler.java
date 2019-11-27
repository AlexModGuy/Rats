package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityAutoCurdler;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class BlockAutoCurdler extends ContainerBlock {

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    private static final VoxelShape AABB = Block.makeCuboidShape(0, 0, 0, 16, 18, 16);

    public BlockAutoCurdler() {
        super(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.0F, 0.0F));
         this.setRegistryName(RatsMod.MODID, "auto_curdler");
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
        //GameRegistry.registerTileEntity(TileEntityAutoCurdler.class, "rats.auto_curdler");
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tile.rats.auto_curdler.desc0"));
        tooltip.add(I18n.format("tile.rats.auto_curdler.desc1"));
    }


    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityAutoCurdler) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityAutoCurdler) tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AABB;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit) {
        if (playerIn.isSneaking()) {
            return false;
        } else {
            //playerIn.openGui(RatsMod.INSTANCE, 5, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
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
        return BlockRenderType.MODEL;
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityAutoCurdler();
    }


}
