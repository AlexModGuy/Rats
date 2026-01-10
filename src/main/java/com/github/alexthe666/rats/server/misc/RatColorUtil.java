package com.github.alexthe666.rats.server.misc;

import com.google.common.collect.Maps;
import net.minecraft.world.item.DyeColor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class RatColorUtil {

	private static final Map<DyeColor, float[]> DYE_TO_RGB = Maps.newEnumMap(Arrays.stream(DyeColor.values()).collect(Collectors.toMap((DyeColor color) -> color, RatColorUtil::createRatColor)));

	public static float[] getDyeRgb(DyeColor color) {
		return DYE_TO_RGB.get(color);
	}

	private static float[] createRatColor(DyeColor color) {
		if (color == DyeColor.WHITE) {
			return new float[]{0.9019608F, 0.9019608F, 0.9019608F};
		} else {
			int packedColor = color.getTextureDiffuseColor();
			float r = ((packedColor >> 16) & 0xFF) / 255.0F;
			float g = ((packedColor >> 8) & 0xFF) / 255.0F;
			float b = (packedColor & 0xFF) / 255.0F;
			return new float[]{r * 0.75F, g * 0.75F, b * 0.75F};
		}
	}
}







