package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.Direction;
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

public class ItemRatSack extends Item {

    public ItemRatSack() {
        super();
        this.setTranslationKey("rats.rat_sack");
        this.setRegistryName(RatsMod.MODID, "rat_sack");

        this.setCreativeTab(RatsMod.TAB);
        this.addPropertyOverride(new ResourceLocation("rat_count"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                return Math.min(3, ItemRatSack.getRatsInStack(stack));
            }
        });
        this.setMaxStackSize(1);
    }

    public static int getRatsInStack(ItemStack stack) {
        int ratCount = 0;
        if (stack.getTagCompound() != null) {
            for (String tagInfo : stack.getTagCompound().getKeySet()) {
                if (tagInfo.contains("Rat")) {
                    ratCount++;
                }
            }
        }
        return ratCount;
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
        //tooltip.add(I18n.format("item.rats.rat_sack.capacity", 10));
        int ratCount = 0;
        List<String> ratNames = new ArrayList<>();
        if (stack.getTagCompound() != null) {
            for (String tagInfo : stack.getTagCompound().getKeySet()) {
                if (tagInfo.contains("Rat")) {
                    NBTTagCompound ratTag = stack.getTagCompound().getCompoundTag(tagInfo);
                    ratCount++;
                    String ratName = I18n.format("entity.rat.name");
                    if (!ratTag.getString("CustomName").isEmpty()) {
                        ratName = ratTag.getString("CustomName");
                    }
                    ratNames.add(ratName);
                }
            }
        }
        tooltip.add(I18n.format("item.rats.rat_sack.contains", ratCount));
        if (!ratNames.isEmpty()) {
            for (int i = 0; i < ratNames.size(); i++) {
                if (i < 3) {
                    tooltip.add(I18n.format("item.rats.rat_sack.contain_rat", ratNames.get(i)));
                } else {
                    break;
                }
            }
            if (ratNames.size() > 3) {
                tooltip.add(I18n.format("item.rats.rat_sack.and_more"));
            }
        }
    }

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, Direction facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() == RatsItemRegistry.RAT_SACK && getRatsInStack(stack) > 0) {
            int ratCount = 0;
            if (stack.getTagCompound() != null) {
                for (String tagInfo : stack.getTagCompound().getKeySet()) {
                    if (tagInfo.contains("Rat")) {
                        ratCount++;
                        NBTTagCompound ratTag = stack.getTagCompound().getCompoundTag(tagInfo);
                        EntityRat rat = new EntityRat(worldIn);
                        BlockPos offset = pos.offset(facing);
                        rat.readEntityFromNBT(ratTag);
                        rat.setLocationAndAngles(offset.getX() + 0.5D, offset.getY(), offset.getZ() + 0.5D, 0, 0);
                        if (!worldIn.isRemote) {
                            worldIn.addEntity(rat);
                        }
                    }
                }
            }
            if (ratCount > 0) {
                player.swingArm(hand);
                player.sendStatusMessage(new TextComponentTranslation("entity.rat.sack.release", ratCount), true);
                stack.setTagCompound(new NBTTagCompound());
            }
        }
        return EnumActionResult.PASS;
    }

    public ItemStack onItemUseFinish(World worldIn, EntityLivingBase entityLiving) {
        return new ItemStack(RatsItemRegistry.RAT_SACK);
    }
}
