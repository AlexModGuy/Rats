package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatlantisPortal;
import com.github.alexthe666.rats.server.world.RatsWorldRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockRatlantisPortal extends ContainerBlock {

    protected BlockRatlantisPortal() {
        super(Block.Properties.create(Material.PORTAL).sound(SoundType.GROUND).hardnessAndResistance(-1.0F).lightValue(15));
        this.setRegistryName(RatsMod.MODID, "ratlantis_portal");
        //GameRegistry.registerTileEntity(TileEntityRatlantisPortal.class, "rats.ratlantis_portal");
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entity) {
        if (!RatConfig.disableRatlantis) {
            if ((!entity.isBeingRidden()) && (entity.getPassengers().isEmpty())) {
                if ((entity instanceof ServerPlayerEntity)) {
                    ServerPlayerEntity thePlayer = (ServerPlayerEntity) entity;
                    if (thePlayer.timeUntilPortal > 0) {
                        thePlayer.timeUntilPortal = 10;
                    } else if (thePlayer.dimension.getId() != RatConfig.ratlantisDimensionId) {
                        thePlayer.timeUntilPortal = 10;
                        thePlayer.changeDimension(RatsWorldRegistry.RATLANTIS_DIM);
                    } else {
                        thePlayer.timeUntilPortal = 10;
                        thePlayer.changeDimension(DimensionType.getById(RatConfig.ratlantisPortalExitDimension));
                    }
                }
                if (!(entity instanceof PlayerEntity)) {
                    if (entity.dimension.getId() != RatConfig.ratlantisDimensionId) {
                        entity.timeUntilPortal = 10;
                        entity.changeDimension(RatsWorldRegistry.RATLANTIS_DIM);
                    } else {
                        entity.timeUntilPortal = 10;
                        entity.changeDimension(DimensionType.getById(RatConfig.ratlantisPortalExitDimension));
                    }
                }
            }
        }
    }


    public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand) {
        if (!this.canSurviveAt(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        }
    }

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!this.canSurviveAt(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        }
    }

    public boolean canSurviveAt(World world, BlockPos pos) {
        return (world.getBlockState(pos.up()).getBlock() == RatsBlockRegistry.RATLANTIS_PORTAL || world.getBlockState(pos.up()).getBlock() == RatsBlockRegistry.MARBLED_CHEESE_RAW) &&
                (world.getBlockState(pos.down()).getBlock() == RatsBlockRegistry.RATLANTIS_PORTAL || world.getBlockState(pos.down()).getBlock() == RatsBlockRegistry.MARBLED_CHEESE_RAW);
    }

    public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
        Block block = adjacentBlockState.getBlock();
        return block != RatsBlockRegistry.RATLANTIS_PORTAL;
    }

    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    public boolean isFullCube(BlockState state) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityRatlantisPortal) {
            int i = 4;
            for (int j = 0; j < i; ++j) {
                double d0 = (double) ((float) pos.getX() + rand.nextFloat());
                double d1 = (double) ((float) pos.getY() + rand.nextFloat());
                double d2 = (double) ((float) pos.getZ() + rand.nextFloat());
                double d3 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
                double d4 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
                double d5 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
                int k = rand.nextInt(2) * 2 - 1;
                worldIn.addParticle(ParticleTypes.END_ROD, d0, d1, d2, d3, d4, d5);
            }
        }
    }

    public ItemStack getItem(World worldIn, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityRatlantisPortal();
    }
}