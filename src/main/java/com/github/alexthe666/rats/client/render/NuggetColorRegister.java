package com.github.alexthe666.rats.client.render;

import com.github.alexthe666.rats.server.items.ItemRatNuggetOre;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class NuggetColorRegister {

    public static final ItemStack FALLBACK_STACK = new ItemStack(Items.IRON_INGOT);
    public static Map<String, Integer> TEXTURES_TO_COLOR = new HashMap<>();

    public static int getNuggetColor(ItemStack stack) {
        ItemStack poopStack = ItemRatNuggetOre.getIngot(stack, FALLBACK_STACK);
        String poopName = poopStack.getDisplayName().getFormattedText();
        if (TEXTURES_TO_COLOR.get(poopName) != null) {
            return TEXTURES_TO_COLOR.get(poopName).intValue();
        } else {
            BufferedImage texture = null;
            int color = 0XFFFFFF;
            try {
                BufferedImage bufferedimage = getBufferedImage(getTextureAtlas(poopStack));
                Color texColour = getAverageColour(bufferedimage);
                color = texColour.getRGB();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            TEXTURES_TO_COLOR.put(poopName, color);
            return color;
        }
    }

    private static Color getAverageColour(BufferedImage image) {
        float red = 0;
        float green = 0;
        float blue = 0;
        float count = 0;
        for (float i = image.getMinX(); i < image.getMinX() + image.getWidth(); i++)
            for (float j = image.getMinY(); j < image.getMinY() + image.getHeight(); j++) {
                Color c = new Color(image.getRGB((int) i, (int) j));
                if (c.getAlpha() != 255 || c.getRed() <= 50 && c.getBlue() <= 50 && c.getGreen() <= 50) {
                    continue;
                }
                red += c.getRed();
                green += c.getGreen();
                blue += c.getBlue();
                count++;
            }
        //Average color
        return new Color((int) (red / count), (int) (green / count), (int) (blue / count));
    }

    private static TextureAtlasSprite getTextureAtlas(ItemStack oreStack) {
        return Minecraft.getInstance().getItemRenderer().getItemModelMesher().getParticleIcon(oreStack.getItem());
    }

    private static BufferedImage getBufferedImage(TextureAtlasSprite textureAtlasSprite) {
        final int iconWidth = textureAtlasSprite.getWidth();
        final int iconHeight = textureAtlasSprite.getHeight();
        final int frameCount = textureAtlasSprite.getFrameCount();
        if (iconWidth <= 0 || iconHeight <= 0 || frameCount <= 0) {
            return null;
        }
        BufferedImage bufferedImage = new BufferedImage(iconWidth, iconHeight * frameCount, BufferedImage.TYPE_4BYTE_ABGR);
        for (int i = 0; i < frameCount; i++) {
            int[][] frame = new int[iconWidth][iconHeight];
            for(int x = 0; x < iconWidth; x++){
                for(int y = 0; y < iconHeight; y++){
                    frame[x][y] = textureAtlasSprite.getPixelRGBA(i, x, y);
                }
            }
            int[] largest = frame[0];
            bufferedImage.setRGB(0, i * iconHeight, iconWidth, iconHeight, largest, 0, 0);
        }
        return bufferedImage;
    }

}
