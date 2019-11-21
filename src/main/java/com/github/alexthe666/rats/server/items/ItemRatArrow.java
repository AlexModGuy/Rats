package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.EntityRatArrow;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemRatArrow extends ItemArrow {

    public ItemRatArrow() {
        super();
        this.setTranslationKey("rats.rat_arrow");
        this.setRegistryName(RatsMod.MODID, "rat_arrow");
        this.setCreativeTab(RatsMod.TAB);
        this.setMaxStackSize(1);
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        itemStack.setTagCompound(new NBTTagCompound());
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int f, boolean f1) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        List<String> ratNames = new ArrayList<>();
        if (stack.getTagCompound() != null) {
            NBTTagCompound ratTag = stack.getTagCompound().getCompoundTag("Rat");
            String ratName = I18n.format("entity.rat.name");
            if (!ratTag.getString("CustomName").isEmpty()) {
                ratName = ratTag.getString("CustomName");
            }
        }
    }


    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        NBTTagCompound ratTag = new NBTTagCompound();
        if(stack.getTagCompound() != null && stack.getTagCompound().getCompoundTag("Rat") != null){
            ratTag = stack.getTagCompound().getCompoundTag("Rat");
        }
        EntityRat rat = new EntityRat(worldIn);
        BlockPos offset = pos.offset(facing);
        rat.readEntityFromNBT(ratTag);
        rat.setLocationAndAngles(offset.getX() + 0.5D, offset.getY(), offset.getZ() + 0.5D, 0, 0);
        if (!worldIn.isRemote) {
            worldIn.spawnEntity(rat);
        }
        stack.shrink(1);
        player.setHeldItem(hand, new ItemStack(Items.ARROW));
        player.swingArm(hand);
        return EnumActionResult.SUCCESS;
    }

    public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
        EntityRatArrow arrow = new EntityRatArrow(worldIn, shooter, stack);
        return arrow;
    }

    public boolean isInfinite(ItemStack stack, ItemStack bow, net.minecraft.entity.player.EntityPlayer player) {
        return false;
    }
}
