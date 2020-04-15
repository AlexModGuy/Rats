package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityAutoCurdler;
import com.github.alexthe666.rats.server.message.MessageAutoCurdlerFluid;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class BlockAutoCurdler extends BlockContainer {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0625F, 0F, 0.0625F, 0.9375F, 1.125F, 0.9375F);

    public BlockAutoCurdler() {
        super(Material.IRON);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.METAL);
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.auto_curdler");
        this.setRegistryName(RatsMod.MODID, "auto_curdler");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        GameRegistry.registerTileEntity(TileEntityAutoCurdler.class, "rats.auto_curdler");
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tile.rats.auto_curdler.desc0"));
        tooltip.add(I18n.format("tile.rats.auto_curdler.desc1"));
    }


    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityAutoCurdler) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityAutoCurdler) tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(worldIn, pos, state);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (playerIn.isSneaking()) {
            return false;
        } else {
            ItemStack stack = playerIn.getHeldItem(hand);
            boolean flag = false;
            if(TileEntityAutoCurdler.isMilk(stack) && worldIn.getTileEntity(pos) instanceof TileEntityAutoCurdler){
                TileEntityAutoCurdler te = (TileEntityAutoCurdler)worldIn.getTileEntity(pos);
                FluidStack fluidStack = FluidUtil.getFluidContained(stack);
                if (fluidStack != null && !worldIn.isRemote) {
                    IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(stack);
                    if (fluidHandler.drain(Integer.MAX_VALUE, false) != null && fluidHandler.drain(Integer.MAX_VALUE, false).amount > 0) {
                        if (te.tank.fill(fluidStack.copy(), false) != 0) {
                            te.tank.fill(fluidStack.copy(), true);
                            if(!playerIn.isCreative()){
                                fluidHandler.drain(Integer.MAX_VALUE, true);
                                if (stack.getItem() == Items.MILK_BUCKET) {
                                    stack.shrink(1);
                                    playerIn.addItemStackToInventory(new ItemStack(Items.BUCKET));
                                }
                            }
                            flag = true;
                        }
                    }
                    if(flag){
                        RatsMod.NETWORK_WRAPPER.sendToAll(new MessageAutoCurdlerFluid(pos.toLong(), te.tank.getFluid()));
                    }
                }
                return true;
            }
            if(!flag){
                playerIn.openGui(RatsMod.INSTANCE, 5, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }
            return true;
        }
    }


    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityAutoCurdler();
    }


}
