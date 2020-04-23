package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityAutoCurdler;
import com.github.alexthe666.rats.server.message.MessageAutoCurdlerFluid;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nullable;
import java.util.List;

public class BlockAutoCurdler extends ContainerBlock implements IUsesTEISR {

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    private static final VoxelShape AABB = Block.makeCuboidShape(0, 0, 0, 16, 18, 16);

    public BlockAutoCurdler() {
        super(Block.Properties.create(Material.IRON).sound(SoundType.METAL).notSolid().variableOpacity().hardnessAndResistance(2.0F, 0.0F));
        this.setRegistryName(RatsMod.MODID, "auto_curdler");
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
        //GameRegistry.registerTileEntity(TileEntityAutoCurdler.class, "rats.auto_curdler");
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("block.rats.auto_curdler.desc0").applyTextStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("block.rats.auto_curdler.desc1").applyTextStyle(TextFormatting.GRAY));
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
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if(!player.isShiftKeyDown()){
            boolean flag = false;
            ItemStack stack = player.getHeldItem(hand);
            if(TileEntityAutoCurdler.isMilk(stack) && worldIn.getTileEntity(pos) instanceof TileEntityAutoCurdler){
                TileEntityAutoCurdler te = (TileEntityAutoCurdler)worldIn.getTileEntity(pos);
                FluidStack fluidStack = getFluidStack(stack);
                if (fluidStack != null && !worldIn.isRemote) {
                    IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(stack).orElse(null);
                    if(fluidHandler != null) {
                        FluidStack drain = fluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
                        if (drain.getAmount() > 0 || stack.getItem() == Items.MILK_BUCKET) {
                            if (te.tank.fill(fluidStack.copy(), IFluidHandler.FluidAction.SIMULATE) != 0) {
                                te.tank.fill(fluidStack.copy(), IFluidHandler.FluidAction.EXECUTE);
                                if (!player.isCreative()) {
                                    fluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
                                    if (stack.getItem() == Items.MILK_BUCKET) {
                                        stack.shrink(1);
                                        player.addItemStackToInventory(new ItemStack(Items.BUCKET));
                                    }
                                }
                                flag = true;
                            }
                        }
                        if (flag) {
                            RatsMod.sendMSGToAll(new MessageAutoCurdlerFluid(pos.toLong(), te.tank.getFluid()));
                        }
                    }
                }
                return ActionResultType.SUCCESS;
            }
            if(!flag) {
                if (worldIn.isRemote) {
                    RatsMod.PROXY.setRefrencedTE(worldIn.getTileEntity(pos));
                } else {
                    INamedContainerProvider inamedcontainerprovider = this.getContainer(state, worldIn, pos);
                    if (inamedcontainerprovider != null) {
                        player.openContainer(inamedcontainerprovider);
                    }
                }
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    public static FluidStack getFluidStack(ItemStack stack){
        FluidStack fluidStack = FluidUtil.getFluidContained(stack).orElse(FluidStack.EMPTY);
        if(fluidStack.isEmpty() && stack.getItem() == Items.MILK_BUCKET){
            return new FluidStack(RatsFluidRegistry.MILK_FLUID, 1000);
        }
        return fluidStack;
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
        return new TileEntityAutoCurdler();
    }


}
