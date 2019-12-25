package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCageBreedingLantern;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCageDecorated;
import com.github.alexthe666.rats.server.items.IRatCageDecoration;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockRatCage extends Block {

    public static final IntegerProperty NORTH = IntegerProperty.create("north", 0, 2);
    public static final IntegerProperty EAST = IntegerProperty.create("east", 0, 2);
    public static final IntegerProperty SOUTH = IntegerProperty.create("south", 0, 2);
    public static final IntegerProperty WEST = IntegerProperty.create("west", 0, 2);
    public static final IntegerProperty UP = IntegerProperty.create("up", 0, 2);
    public static final IntegerProperty DOWN = IntegerProperty.create("down", 0, 2);

    private static final VoxelShape BOTTOM_AABB = Block.makeCuboidShape(0F, 0F, 0F, 16F, 2F, 16F);
    private static final VoxelShape TOP_AABB = Block.makeCuboidShape(0F, 16F, 0F, 16F, 16F, 16F);
    private static final VoxelShape NORTH_AABB = Block.makeCuboidShape(0F, 0F, 0F, 16F, 16F, 0F);
    private static final VoxelShape SOUTH_AABB = Block.makeCuboidShape(0F, 0F, 16F, 16F, 16F, 16F);
    private static final VoxelShape EAST_AABB = Block.makeCuboidShape(1F, 0F, 0F, 16F, 16F, 16F);
    private static final VoxelShape WEST_AABB = Block.makeCuboidShape(0F, 0F, 0F, 0F, 16F, 16F);

    public BlockRatCage(String name) {
        super(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.0F, 0.0F));
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(NORTH, Integer.valueOf(0))
                .with(EAST, Integer.valueOf(0))
                .with(SOUTH, Integer.valueOf(0))
                .with(WEST, Integer.valueOf(0))
                .with(UP, Integer.valueOf(0))
                .with(DOWN, Integer.valueOf(0))
        );
        this.setRegistryName(RatsMod.MODID, name);
    }

    public BlockRatCage(String name, Block.Properties props) {
        super(props);
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(NORTH, Integer.valueOf(0))
                .with(EAST, Integer.valueOf(0))
                .with(SOUTH, Integer.valueOf(0))
                .with(WEST, Integer.valueOf(0))
                .with(UP, Integer.valueOf(0))
                .with(DOWN, Integer.valueOf(0))
        );
        this.setRegistryName(RatsMod.MODID, name);
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tile.rats.rat_cage.desc0"));
        tooltip.add(I18n.format("tile.rats.rat_cage.desc1"));
    }

    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }


    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, WEST, SOUTH, DOWN, UP);
    }


    public BlockState getStateForPlacement(BlockItemUseContext context) {
        IBlockReader iblockreader = context.getWorld();
        BlockPos blockpos = context.getPos();
        IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
        BlockPos blockpos1 = blockpos.north();
        BlockPos blockpos2 = blockpos.east();
        BlockPos blockpos3 = blockpos.south();
        BlockPos blockpos4 = blockpos.west();
        BlockState blockstate = iblockreader.getBlockState(blockpos1);
        BlockState blockstate1 = iblockreader.getBlockState(blockpos2);
        BlockState blockstate2 = iblockreader.getBlockState(blockpos3);
        BlockState blockstate3 = iblockreader.getBlockState(blockpos4);
        return super.getStateForPlacement(context).with(NORTH, Integer.valueOf(this.canFenceConnectTo(blockstate, blockstate.func_224755_d(iblockreader, blockpos1, Direction.SOUTH), Direction.SOUTH))).with(EAST, Integer.valueOf(this.canFenceConnectTo(blockstate1, blockstate1.func_224755_d(iblockreader, blockpos2, Direction.WEST), Direction.WEST))).with(SOUTH, Integer.valueOf(this.canFenceConnectTo(blockstate2, blockstate2.func_224755_d(iblockreader, blockpos3, Direction.NORTH), Direction.NORTH))).with(WEST, Integer.valueOf(this.canFenceConnectTo(blockstate3, blockstate3.func_224755_d(iblockreader, blockpos4, Direction.EAST), Direction.EAST)));
    }

    public int canFenceConnectTo(BlockState p_220111_1_, boolean p_220111_2_, Direction p_220111_3_) {
        if (p_220111_1_.getBlock() instanceof BlockRatTube) {
            return 2;
        }
        return p_220111_1_.getBlock() instanceof BlockRatCage ? 1 : 0;
    }

    public Item getItemDropped(BlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(RatsBlockRegistry.RAT_CAGE);
    }

    public ItemStack getItem(World worldIn, BlockPos pos, BlockState state) {
        return new ItemStack(RatsBlockRegistry.RAT_CAGE);
    }

    private VoxelShape getShape(BlockState state) {
        VoxelShape shape1 = Block.makeCuboidShape(0, 0, 0, 0, 0, 0);
        if (state.getBlock() instanceof BlockRatHole) {
            if (state.get(UP) == 0) {
                shape1 = VoxelShapes.combineAndSimplify(shape1, TOP_AABB, IBooleanFunction.ONLY_FIRST).simplify();
            }
            if (state.get(DOWN) == 0) {
                shape1 = VoxelShapes.combineAndSimplify(shape1, BOTTOM_AABB, IBooleanFunction.ONLY_FIRST).simplify();
            }
            if (state.get(NORTH) == 0) {
                shape1 = VoxelShapes.combineAndSimplify(shape1, NORTH_AABB, IBooleanFunction.ONLY_FIRST).simplify();
            }
            if (state.get(SOUTH) == 0) {
                shape1 = VoxelShapes.combineAndSimplify(shape1, SOUTH_AABB, IBooleanFunction.ONLY_FIRST).simplify();
            }
            if (state.get(WEST) == 0) {
                shape1 = VoxelShapes.combineAndSimplify(shape1, WEST_AABB, IBooleanFunction.ONLY_FIRST).simplify();
            }
            if (state.get(EAST) == 0) {
                shape1 = VoxelShapes.combineAndSimplify(shape1, EAST_AABB, IBooleanFunction.ONLY_FIRST).simplify();
            }
        }
        return shape1;
    }

    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit) {
        if (playerIn.getHeldItem(hand).getItem() instanceof IRatCageDecoration && !this.hasTileEntity(state)) {
            if (((IRatCageDecoration) playerIn.getHeldItem(hand).getItem()).canStay(worldIn, pos, this)) {
                Direction limitedFacing = playerIn.getHorizontalFacing().getOpposite();
                if (!worldIn.isRemote) {
                    //  BlockRatCageDecorated.DECO_TRIGGER.trigger((ServerPlayerEntity) playerIn, playerIn);
                }
                if (playerIn.getHeldItem(hand).getItem() == RatsItemRegistry.RAT_BREEDING_LANTERN) {
                    worldIn.setBlockState(pos, RatsBlockRegistry.RAT_CAGE_BREEDING_LANTERN.getDefaultState().with(BlockRatCageDecorated.FACING, limitedFacing), 2);
                    TileEntityRatCageBreedingLantern decorated = new TileEntityRatCageBreedingLantern();
                    ItemStack added = new ItemStack(playerIn.getHeldItem(hand).getItem(), 1);
                    decorated.setContainedItem(added);
                    worldIn.setTileEntity(pos, decorated);
                    if (!playerIn.isCreative()) {
                        playerIn.getHeldItem(hand).shrink(1);
                    }
                } else {
                    worldIn.setBlockState(pos, RatsBlockRegistry.RAT_CAGE_DECORATED.getDefaultState().with(BlockRatCageDecorated.FACING, limitedFacing), 2);
                    TileEntityRatCageDecorated decorated = new TileEntityRatCageDecorated();
                    ItemStack added = new ItemStack(playerIn.getHeldItem(hand).getItem(), 1);
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
                        rat.stopRiding();
                        rat.setPosition(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
                        rat.getNavigator().clearPath();
                        //rat.moveController.action = MovementController.Action.WAIT;
                        ratCount++;
                    }
                }
                playerIn.sendStatusMessage(new TranslationTextComponent("entity.rat.cage.deposit", ratCount), true);
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
                playerIn.sendStatusMessage(new TranslationTextComponent("entity.rat.cage.withdrawal", ratCount), true);
                return true;
            }
        }
        return false;
    }

    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    public boolean isFullCube(BlockState state) {
        return false;
    }

    public int getMetaFromState(BlockState state) {
        return 0;
    }

    public ItemStack getContainedItem(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te instanceof TileEntityRatCageDecorated) {
            return ((TileEntityRatCageDecorated) te).getContainedItem();
        }
        return ItemStack.EMPTY;
    }

}
