package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tile.rats.garbage_pile.desc"));
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return rand.nextInt(3) == 0 ? RatsItemRegistry.PLASTIC_WASTE : super.getItemDropped(state, rand, fortune);
    }

    @SideOnly(Side.CLIENT)
    public int getDustColor(IBlockState state)
    {
        return 0X79695B;
    }

    @Deprecated
    public boolean canEntitySpawn(IBlockState state, Entity entityIn){
        return entityIn instanceof EntityRat;
    }

}
