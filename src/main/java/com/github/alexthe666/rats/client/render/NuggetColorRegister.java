package com.github.alexthe666.rats.client.render;

import com.github.alexthe666.rats.server.items.OreRatNuggetItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.model.data.ModelData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NuggetColorRegister {

	public static final ItemStack FALLBACK_STACK = new ItemStack(Items.IRON_INGOT);
	public static final Map<String, Integer> TEXTURES_TO_COLOR = new HashMap<>();

	public static int getNuggetColor(ItemStack stack) {
		ItemStack poopStack = OreRatNuggetItem.getStoredItem(stack, FALLBACK_STACK);
		String poopName = poopStack.getDisplayName().getString();
		if (TEXTURES_TO_COLOR.get(poopName) != null) {
			return TEXTURES_TO_COLOR.get(poopName);
		} else {
			int color = 0XFFFFFF;
			try {
				Color texColour = getAverageColour(getTextureAtlas(poopStack));
				color = texColour.getARGB();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			TEXTURES_TO_COLOR.put(poopName, color);
			return color;
		}
	}

	private static Color getAverageColour(TextureAtlasSprite image) {
		float red = 0;
		float green = 0;
		float blue = 0;
		float count = 0;
		int uMax = image.contents().width();
		int vMax = image.contents().height();
		for (float i = 0; i < uMax; i++)
			for (float j = 0; j < vMax; j++) {
				int alpha = image.getPixelRGBA(0, (int) i, (int) j) >> 24 & 0xFF;
				if (alpha != 255) {
					continue;
				}
				red += image.getPixelRGBA(0, (int) i, (int) j) & 0xFF;
				green += image.getPixelRGBA(0, (int) i, (int) j) >> 8 & 0xFF;
				blue += image.getPixelRGBA(0, (int) i, (int) j) >> 16 & 0xFF;
				count++;
			}
		//Average color
		return new Color((int) (red / count), (int) (green / count), (int) (blue / count));
	}

	private static TextureAtlasSprite getTextureAtlas(ItemStack oreStack) {
		return Objects.requireNonNull(Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(oreStack.getItem())).getParticleIcon(ModelData.EMPTY);
	}

	//java.awt bad
	//awt crashes macs, so here's a color holder
	public static class Color {
		private final int value;

		public Color(int r, int g, int b) {
			this(r, g, b, 255);
		}

		public Color(int r, int g, int b, int a) {
			this.value = ((a & 0xFF) << 24) |
					((r & 0xFF) << 16) |
					((g & 0xFF) << 8) |
					((b & 0xFF));
		}

		public int getARGB() {
			return this.value;
		}
	}
}
