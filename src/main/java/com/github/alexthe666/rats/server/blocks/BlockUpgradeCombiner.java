package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityUpgradeCombiner;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockUpgradeCombiner extends ContainerBlock implements IUsesTEISR {
    protected static final VoxelShape AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);

    public BlockUpgradeCombiner() {
        super(Block.Properties.create(Material.ROCK).sound(SoundType.WOOD).notSolid().variableOpacity().hardnessAndResistance(5.0F, 0.0F).lightValue(4));
        //GameRegistry.registerTileEntity(TileEntityUpgradeCombiner.class, "rats.upgrade_combiner");
        this.setRegistryName("rats:upgrade_combiner");
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AABB;
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

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityUpgradeCombiner();
    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!player.isShiftKeyDown()){
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
}
