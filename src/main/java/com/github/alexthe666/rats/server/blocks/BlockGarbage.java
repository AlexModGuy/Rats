package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatlantisPortal;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockGarbage extends BlockFalling {
    public BlockGarbage() {
        super(Material.GROUND);
        this.setHardness(0.7F);
        this.setSoundType(SoundType.GROUND);
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.garbage_pile");
        this.setRegistryName(RatsMod.MODID, "garbage_pile");
        this.setTickRandomly(true);
    }

    @Override
    public void updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
        super.updateTick(world, pos, state, rand);
        if(rand.nextFloat() <= 0.5){
            EntityRat rat = new EntityRat(world);
            rat.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 0, 0);
            if(rat.getCanSpawnHere() && !rat.isEntityInsideOpaqueBlock() && rat.isNotColliding()){
                rat.onInitialSpawn(world.getDifficultyForLocation(pos), null);
                if(!world.isRemote){
                    world.spawnEntity(rat);
                }
            }
        }
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tile.rats.garbage_pile.desc"));
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return rand.nextInt(3) == 0 ? RatsItemRegistry.PLASTIC_WASTE : super.getItemDropped(state, rand, fortune);
    }

    @SideOnly(Side.CLIENT)
    public int getDustColor(IBlockState state) {
        return 0X79695B;
    }

    @Deprecated
    public boolean canEntitySpawn(IBlockState state, Entity entityIn) {
        return entityIn instanceof EntityRat;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if(rand.nextFloat() <= 0.01F){
            double d0 = (double) ((float) pos.getX() + rand.nextFloat());
            double d1 = (double) ((float) pos.getY() + 2 + rand.nextFloat());
            double d2 = (double) ((float) pos.getZ() + rand.nextFloat());
            if(worldIn.isRemote){
                RatsMod.PROXY.spawnParticle("fly", d0, d1, d2, 0, 0, 0);
            }
        }
    }
}
