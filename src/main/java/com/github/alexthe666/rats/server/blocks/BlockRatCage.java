package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCageBreedingLantern;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCageDecorated;
import com.github.alexthe666.rats.server.items.IRatCageDecoration;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockRatCage extends Block {

    public static final PropertyInteger NORTH = PropertyInteger.create("north", 0, 2);
    public static final PropertyInteger EAST = PropertyInteger.create("east", 0, 2);
    public static final PropertyInteger SOUTH = PropertyInteger.create("south", 0, 2);
    public static final PropertyInteger WEST = PropertyInteger.create("west", 0, 2);
    public static final PropertyInteger UP = PropertyInteger.create("up", 0, 2);
    public static final PropertyInteger DOWN = PropertyInteger.create("down", 0, 2);

    private static final AxisAlignedBB BOTTOM_AABB = new AxisAlignedBB(0F, 0F, 0F, 1F, 0.125F, 1F);
    private static final AxisAlignedBB TOP_AABB = new AxisAlignedBB(0F, 1F, 0F, 1F, 1F, 1F);
    private static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 0F);
    private static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0F, 0F, 1F, 1F, 1F, 1F);
    private static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(1F, 0F, 0F, 1F, 1F, 1F);
    private static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0F, 0F, 0F, 0F, 1F, 1F);

    public BlockRatCage(String name) {
        super(Material.IRON, MapColor.STONE);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.METAL);
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats." + name);
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(NORTH, Integer.valueOf(0))
                .withProperty(EAST, Integer.valueOf(0))
                .withProperty(SOUTH, Integer.valueOf(0))
                .withProperty(WEST, Integer.valueOf(0))
                .withProperty(UP, Integer.valueOf(0))
                .withProperty(DOWN, Integer.valueOf(0))
        );
        this.setRegistryName(RatsMod.MODID, name);
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tile.rats.rat_cage.desc0"));
        tooltip.add(I18n.format("tile.rats.rat_cage.desc1"));
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }


    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(NORTH, canFenceConnectTo(worldIn, pos, EnumFacing.NORTH))
                .withProperty(SOUTH, canFenceConnectTo(worldIn, pos, EnumFacing.SOUTH))
                .withProperty(EAST, canFenceConnectTo(worldIn, pos, EnumFacing.EAST))
                .withProperty(WEST, canFenceConnectTo(worldIn, pos, EnumFacing.WEST))
                .withProperty(UP, canFenceConnectTo(worldIn, pos, EnumFacing.UP))
                .withProperty(DOWN, canFenceConnectTo(worldIn, pos, EnumFacing.DOWN));
    }

    public int canFenceConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        BlockPos other = pos.offset(facing);
        if (world.getBlockState(other).getBlock() instanceof BlockRatTube) {
            return 2;
        }
        return world.getBlockState(other).getBlock() instanceof BlockRatCage ? 1 : 0;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(RatsBlockRegistry.RAT_CAGE);
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(RatsBlockRegistry.RAT_CAGE);
    }


    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FULL_BLOCK_AABB;
    }

    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        if (state.getBlock() instanceof BlockRatCage) {
            IBlockState actualState = getActualState(state, worldIn, pos);
            if (actualState.getValue(UP) == 0) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, TOP_AABB);
            }
            if (actualState.getValue(DOWN) == 0) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, BOTTOM_AABB);
            }
            if (actualState.getValue(NORTH) == 0) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
            }
            if (actualState.getValue(SOUTH) == 0) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
            }
            if (actualState.getValue(EAST) == 0) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
            }
            if (actualState.getValue(WEST) == 0) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
            }
        }
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn.getHeldItem(hand).getItem() instanceof IRatCageDecoration && !this.hasTileEntity(state)) {
            if (((IRatCageDecoration) playerIn.getHeldItem(hand).getItem()).canStay(worldIn, pos, this)) {
                EnumFacing limitedFacing = playerIn.getHorizontalFacing().getOpposite();
                if (!worldIn.isRemote) {
                    BlockRatCageDecorated.DECO_TRIGGER.trigger((EntityPlayerMP) playerIn, playerIn);
                }
                if (playerIn.getHeldItem(hand).getItem() == RatsItemRegistry.RAT_BREEDING_LANTERN) {
                    worldIn.setBlockState(pos, RatsBlockRegistry.RAT_CAGE_BREEDING_LANTERN.getDefaultState().withProperty(BlockRatCageDecorated.FACING, limitedFacing));
                    TileEntityRatCageBreedingLantern decorated = new TileEntityRatCageBreedingLantern();
                    ItemStack added = new ItemStack(playerIn.getHeldItem(hand).getItem(), 1, playerIn.getHeldItem(hand).getMetadata());
                    decorated.setContainedItem(added);
                    worldIn.setTileEntity(pos, decorated);
                    if (!playerIn.isCreative()) {
                        playerIn.getHeldItem(hand).shrink(1);
                    }
                } else {
                    worldIn.setBlockState(pos, RatsBlockRegistry.RAT_CAGE_DECORATED.getDefaultState().withProperty(BlockRatCageDecorated.FACING, limitedFacing));
                    TileEntityRatCageDecorated decorated = new TileEntityRatCageDecorated();
                    ItemStack added = new ItemStack(playerIn.getHeldItem(hand).getItem(), 1, playerIn.getHeldItem(hand).getMetadata());
                    decorated.setContainedItem(added);
                    worldIn.setTileEntity(pos, decorated);
                    if (!playerIn.isCreative()) {
                        playerIn.getHeldItem(hand).shrink(1);
                    }
                }

                return true;
            }
        }
        if (this.hasTileEntity(state)) {
            ItemStack stack = this.getContainedItem(worldIn, pos);
            boolean clearIt = true;
            if (stack != ItemStack.EMPTY) {
                clearIt = playerIn.isSneaking();
            }
            if (clearIt) {
                worldIn.setBlockState(pos, RatsBlockRegistry.RAT_CAGE.getDefaultState());
            }
        }
        if (playerIn.getHeldItem(hand).isEmpty()) {
            boolean ridingRats = false;
            if (!playerIn.getPassengers().isEmpty()) {
                for (Entity entity : playerIn.getPassengers()) {
                    if (entity instanceof EntityRat) {
                        ridingRats = true;
                        break;
                    }
                }
            }
            if (ridingRats) {
                int ratCount = 0;
                for (Entity entity : playerIn.getPassengers()) {
                    if (entity instanceof EntityRat && !((EntityRat) entity).isChild()) {
                        EntityRat rat = (EntityRat) entity;
                        rat.dismountRidingEntity();
                        rat.setPosition(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
                        rat.getNavigator().clearPath();
                        rat.getMoveHelper().action = EntityMoveHelper.Action.WAIT;
                        ratCount++;
                    }
                }
                playerIn.sendStatusMessage(new TextComponentTranslation("entity.rat.cage.deposit", ratCount), true);
                return true;
            } else {
                int ratCount = 0;
                List<EntityRat> list = worldIn.getEntitiesWithinAABB(EntityRat.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1));
                for (EntityRat rat : list) {
                    if (!rat.isChild()) {
                        rat.setPosition(playerIn.posX, playerIn.posY, playerIn.posZ);
                    }
                    ratCount++;
                }
                playerIn.sendStatusMessage(new TextComponentTranslation("entity.rat.cage.withdrawal", ratCount), true);
                return true;
            }
        }
        return false;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    public ItemStack getContainedItem(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te instanceof TileEntityRatCageDecorated) {
            return ((TileEntityRatCageDecorated) te).getContainedItem();
        }
        return ItemStack.EMPTY;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.SOLID;
    }
}
