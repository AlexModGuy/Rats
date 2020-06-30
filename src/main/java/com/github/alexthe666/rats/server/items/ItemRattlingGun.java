package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRattlingGun;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRattlingGun extends Item {

    public ItemRattlingGun() {
        super(new Item.Properties().group(RatsMod.TAB));
        this.setRegistryName(RatsMod.MODID, "rattling_gun");
    }

    public ActionResultType onItemUse(ItemUseContext context) {
        EntityRattlingGun entity = new EntityRattlingGun(RatsEntityRegistry.RATTLING_GUN, context.getWorld());
        PlayerEntity player = context.getPlayer();
        BlockPos offset = context.getPos().offset(context.getFace());
        if(context.getWorld().getBlockState(offset).getMaterial().isReplaceable()){
            entity.setLocationAndAngles(offset.getX() + 0.5D, offset.getY() + 0, offset.getZ() + 0.5D, player.rotationYaw, 0);
            float yaw = MathHelper.wrapDegrees(player.rotationYaw + 180F);
            entity.prevRotationYaw = yaw;
            entity.rotationYaw = yaw;
            entity.rotationYawHead = yaw;
            entity.renderYawOffset = yaw;
            entity.prevRenderYawOffset = yaw;
            if (!player.isCreative()) {
                context.getItem().shrink(1);
            }
            if (!context.getWorld().isRemote) {
                context.getWorld().addEntity(entity);
            }

            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".desc").func_240699_a_(TextFormatting.GRAY));
    }
}
