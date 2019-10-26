package com.github.alexthe666.rats.server.compat.tinkers;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;

import javax.annotation.Nullable;

public class TraitPlagueShot extends AbstractTrait {

    public TraitPlagueShot() {
        super("plague_shot", TextFormatting.GRAY);
    }

    @Override
    public String getLocalizedName() {
        return Util.translate(String.format(LOC_Name, "plague_shot"));
    }

    @Override
    public String getLocalizedDesc() {
        return Util.translate(String.format(LOC_Desc, "plague_shot"));
    }

    public boolean isToolWithTrait(ItemStack itemStack) {
        return super.isToolWithTrait(itemStack);
    }
}
