package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCageBreedingLantern;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCageDecorated;
import com.github.alexthe666.rats.server.items.IRatCageDecoration;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
    private static final VoxelShape TOP_AABB = Block.makeCuboidShape(0F, 15F, 0F, 16F, 16F, 16F);
    private static final VoxelShape NORTH_AABB = Block.makeCuboidShape(0F, 0F, 0F, 16F, 16F, 1F);
    private static final VoxelShape SOUTH_AABB = Block.makeCuboidShape(0F, 0F, 15F, 16F, 16F, 16F);
    private static final VoxelShape EAST_AABB = Block.makeCuboidShape(15F, 0F, 0F, 16F, 16F, 16F);
    private static final VoxelShape WEST_AABB = Block.makeCuboidShape(0F, 0F, 0F, 1F, 16F, 16F);

    public BlockRatCage(String name) {
        super(Block.Properties.create(Material.IRON).sound(SoundType.METAL).variableOpacity().notSolid().hardnessAndResistance(2.0F, 0.0F));
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

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("block.rats.rat_cage.desc0").func_240699_a_(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("block.rats.rat_cage.desc1").func_240699_a_(TextFormatting.GRAY));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, WEST, SOUTH, DOWN, UP);
    }


    public BlockState getStateForPlacement(BlockItemUseContext context) {
        IBlockReader iblockreader = context.getWorld();
        BlockPos blockpos = context.getPos();
        FluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
        BlockPos blockpos1 = blockpos.north();
        BlockPos blockpos2 = blockpos.east();
        BlockPos blockpos3 = blockpos.south();
        BlockPos blockpos4 = blockpos.west();
        BlockState blockstate = iblockreader.getBlockState(blockpos1);
        BlockState blockstate1 = iblockreader.getBlockState(blockpos2);
        BlockState blockstate2 = iblockreader.getBlockState(blockpos3);
        BlockState blockstate3 = iblockreader.getBlockState(blockpos4);
        return super.getStateForPlacement(context).with(NORTH, Integer.valueOf(this.canFenceConnectTo(blockstate, false, Direction.SOUTH))).with(EAST, Integer.valueOf(this.canFenceConnectTo(blockstate1, false, Direction.WEST))).with(SOUTH, Integer.valueOf(this.canFenceConnectTo(blockstate2, false, Direction.NORTH))).with(WEST, Integer.valueOf(this.canFenceConnectTo(blockstate3, false, Direction.EAST)));
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

    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        VoxelShape shape1 = Block.makeCuboidShape(0, 0, 0, 0, 0, 0);
        if (state.getBlock() instanceof BlockRatCage) {
            if (state.get(UP) == 0) {
                shape1 = VoxelShapes.combine(shape1, TOP_AABB, IBooleanFunction.OR);
            }
            if (state.get(DOWN) == 0) {
                shape1 = VoxelShapes.combine(shape1, BOTTOM_AABB, IBooleanFunction.OR);
            }
            if (state.get(NORTH) == 0) {
                shape1 = VoxelShapes.combine(shape1, NORTH_AABB, IBooleanFunction.OR);
            }
            if (state.get(SOUTH) == 0) {
                shape1 = VoxelShapes.combine(shape1, SOUTH_AABB, IBooleanFunction.OR);
            }
            if (state.get(WEST) == 0) {
                shape1 = VoxelShapes.combine(shape1, WEST_AABB, IBooleanFunction.OR);
            }
            if (state.get(EAST) == 0) {
                shape1 = VoxelShapes.combine(shape1, EAST_AABB, IBooleanFunction.OR);
            }
        }
        return shape1;
    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit) {
        if (playerIn.getHeldItem(hand).getItem() instanceof IRatCageDecoration && !this.hasTileEntity(state)) {
            if (((IRatCageDecoration) playerIn.getHeldItem(hand).getItem()).canStay(worldIn, pos, this)) {
                Direction limitedFacing = playerIn.getHorizontalFacing().getOpposite();
                if (!worldIn.isRemote) {
                    //  BlockRatCageDecorated.DECO_TRIGGER.trigger((ServerPlayerEntity) playerIn, playerIn);
                }
                if (playerIn.getHeldItem(hand).getItem() == RatsItemRegistry.RAT_BREEDING_LANTERN) {
                    BlockState pre = worldIn.getBlockState(pos);
                    BlockState decorated = RatsBlockRegistry.RAT_CAGE_BREEDING_LANTERN.getDefaultState().with(BlockRatCageDecorated.FACING, limitedFacing);
                    decorated = decorated.with(NORTH, pre.get(NORTH)).with(EAST, pre.get(EAST)).with(SOUTH, pre.get(SOUTH)).with(WEST, pre.get(WEST)).with(UP, pre.get(UP)).with(DOWN, pre.get(DOWN));
                    worldIn.setBlockState(pos, decorated, 3);
                    TileEntityRatCageBreedingLantern te = new TileEntityRatCageBreedingLantern();
                    ItemStack added = new ItemStack(playerIn.getHeldItem(hand).getItem(), 1);
                    te.setContainedItem(added);
                    worldIn.setTileEntity(pos, te);
                    if (!playerIn.isCreative()) {
                        playerIn.getHeldItem(hand).shrink(1);
                    }
                } else {
                    BlockState pre = worldIn.getBlockState(pos);
                    BlockState decorated = RatsBlockRegistry.RAT_CAGE_DECORATED.getDefaultState().with(BlockRatCageDecorated.FACING, limitedFacing);
                    decorated = decorated.with(NORTH, pre.get(NORTH)).with(EAST, pre.get(EAST)).with(SOUTH, pre.get(SOUTH)).with(WEST, pre.get(WEST)).with(UP, pre.get(UP)).with(DOWN, pre.get(DOWN));
                    worldIn.setBlockState(pos, decorated, 3);
                    TileEntityRatCageDecorated te = new TileEntityRatCageDecorated();
                    ItemStack added = new ItemStack(playerIn.getHeldItem(hand).getItem(), 1);
                    te.setContainedItem(added);
                    worldIn.setTileEntity(pos, te);
                    if (!playerIn.isCreative()) {
                        playerIn.getHeldItem(hand).shrink(1);
                    }
                }

                return ActionResultType.SUCCESS;
            }
        }
        if (this.hasTileEntity(state)) {
            ItemStack stack = this.getContainedItem(worldIn, pos);
            boolean clearIt = true;
            if (stack != ItemStack.EMPTY) {
                clearIt = playerIn.isSneaking();
            }
            if (clearIt) {
                BlockState pre = worldIn.getBlockState(pos);
                BlockState decorated = RatsBlockRegistry.RAT_CAGE.getDefaultState();
                decorated = decorated.with(NORTH, pre.get(NORTH)).with(EAST, pre.get(EAST)).with(SOUTH, pre.get(SOUTH)).with(WEST, pre.get(WEST)).with(UP, pre.get(UP)).with(DOWN, pre.get(DOWN));
                worldIn.setBlockState(pos, decorated, 3);
                worldIn.setBlockState(pos, decorated);
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
                playerIn.sendStatusMessage(new TranslationTextComponent("entity.rats.rat.cage.deposit", ratCount), true);
                return ActionResultType.SUCCESS;
            } else {
                int ratCount = 0;
                List<EntityRat> list = worldIn.getEntitiesWithinAABB(EntityRat.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1));
                for (EntityRat rat : list) {
                    if (!rat.isChild()) {
                        rat.setPosition(playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ());
                    }
                    ratCount++;
                }
                playerIn.sendStatusMessage(new TranslationTextComponent("entity.rats.rat.cage.withdrawal", ratCount), true);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    public boolean isFullCube(BlockState state) {
        return false;
    }

    public ItemStack getContainedItem(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te instanceof TileEntityRatCageDecorated) {
            return ((TileEntityRatCageDecorated) te).getContainedItem();
        }
        return ItemStack.EMPTY;
    }

    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        IntegerProperty connect = null;
        switch (facing) {
            case NORTH:
                connect = NORTH;
                break;
            case SOUTH:
                connect = SOUTH;
                break;
            case EAST:
                connect = EAST;
                break;
            case WEST:
                connect = WEST;
                break;
            case DOWN:
                connect = DOWN;
                break;
            default:
                connect = UP;
                break;
        }
        return stateIn.with(connect, Integer.valueOf(this.canFenceConnectTo(facingState, false, facing.getOpposite())));
    }

}
