package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatlantisPortal;
import com.github.alexthe666.rats.server.world.RatlantisTeleporter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockRatlantisPortal extends BlockContainer {

    protected BlockRatlantisPortal() {
        super(Material.PORTAL);
        this.setLightLevel(1.0F);
        this.setTranslationKey("rats.ratlantis_portal");
        this.setRegistryName(RatsMod.MODID, "ratlantis_portal");
        this.setTickRandomly(true);
        this.setBlockUnbreakable();
        GameRegistry.registerTileEntity(TileEntityRatlantisPortal.class, "rats.ratlantis_portal");
    }

    @Override
    public void onEntityCollision(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!RatConfig.disableRatlantis) {
            if ((!entity.isBeingRidden()) && (entity.getPassengers().isEmpty())) {
                if ((entity instanceof EntityPlayerMP)) {
                    EntityPlayerMP thePlayer = (EntityPlayerMP) entity;
                    if (thePlayer.timeUntilPortal > 0) {
                        thePlayer.timeUntilPortal = 10;
                    } else if (thePlayer.dimension != RatConfig.ratlantisDimensionId) {
                        thePlayer.timeUntilPortal = 10;
                        thePlayer.server.getPlayerList().transferPlayerToDimension(thePlayer, RatConfig.ratlantisDimensionId, new RatlantisTeleporter(thePlayer.server.getWorld(RatConfig.ratlantisDimensionId)));
                    } else {
                        thePlayer.timeUntilPortal = 10;
                        thePlayer.server.getPlayerList().transferPlayerToDimension(thePlayer, RatConfig.ratlantisPortalExitDimension, new RatlantisTeleporter(thePlayer.server.getWorld(RatConfig.ratlantisPortalExitDimension)));
                    }
                }
                if (!(entity instanceof EntityPlayer)) {
                    if (entity.dimension != RatConfig.ratlantisDimensionId) {
                        entity.timeUntilPortal = 10;
                        entity.changeDimension(RatConfig.ratlantisDimensionId);
                    } else {
                        entity.timeUntilPortal = 10;
                        entity.changeDimension(RatConfig.ratlantisPortalExitDimension);
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

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!this.canSurviveAt(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        }
    }

    public boolean canSurviveAt(World world, BlockPos pos) {
        return (world.getBlockState(pos.up()).getBlock() == RatsBlockRegistry.RATLANTIS_PORTAL || world.getBlockState(pos.up()).getBlock() == RatsBlockRegistry.MARBLED_CHEESE_RAW) &&
                (world.getBlockState(pos.down()).getBlock() == RatsBlockRegistry.RATLANTIS_PORTAL || world.getBlockState(pos.down()).getBlock() == RatsBlockRegistry.MARBLED_CHEESE_RAW);
    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityRatlantisPortal();
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(BlockState blockState, IBlockAccess blockAccess, BlockPos pos, Direction side) {
        BlockState BlockState = blockAccess.getBlockState(pos.offset(side));
        Block block = BlockState.getBlock();
        return !BlockState.isOpaqueCube() && block != RatsBlockRegistry.RATLANTIS_PORTAL;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(BlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    public boolean isFullCube(BlockState state) {
        return false;
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
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
                worldIn.spawnParticle(EnumParticleTypes.END_ROD, d0, d1, d2, d3, d4, d5);
            }
        }
    }

    public ItemStack getItem(World worldIn, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }

    public MapColor getMapColor(BlockState state, IBlockAccess worldIn, BlockPos pos) {
        return MapColor.YELLOW;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face) {
        return BlockFaceShape.UNDEFINED;
    }
}