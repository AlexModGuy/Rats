package com.github.alexthe666.rats.server.entity;

import com.google.common.collect.Maps;
import net.minecraft.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class RatColorUtil {

    private static final Map<DyeColor, float[]> DYE_TO_RGB = Maps.newEnumMap(Arrays.stream(DyeColor.values()).collect(Collectors.toMap((DyeColor p_200204_0_) -> {
        return p_200204_0_;
    }, RatColorUtil::createRatColor)));

    @OnlyIn(Dist.CLIENT)
    public static float[] getDyeRgb(DyeColor dyeColor) {
        return DYE_TO_RGB.get(dyeColor);
    }

    private static float[] createRatColor(DyeColor dyeColorIn) {
        if (dyeColorIn == DyeColor.WHITE) {
            return new float[]{0.9019608F, 0.9019608F, 0.9019608F};
        } else {
            float[] afloat = dyeColorIn.getColorComponentValues();
            float f = 0.75F;
            return new float[]{afloat[0] * 0.75F, afloat[1] * 0.75F, afloat[2] * 0.75F};
        }
    }
}
