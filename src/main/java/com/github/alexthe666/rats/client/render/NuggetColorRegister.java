package com.github.alexthe666.rats.client.render;

import com.github.alexthe666.rats.server.items.ItemRatNuggetOre;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class NuggetColorRegister {

    public static final ItemStack FALLBACK_STACK = new ItemStack(Items.IRON_INGOT);
    public static Map<String, Integer> TEXTURES_TO_COLOR = new HashMap<>();

    public static int getNuggetColor(ItemStack stack) {
        ItemStack poopStack = ItemRatNuggetOre.getIngot(stack, FALLBACK_STACK, null);
        String poopName = poopStack.getDisplayName().getFormattedText();
        if (TEXTURES_TO_COLOR.get(poopName) != null) {
            return TEXTURES_TO_COLOR.get(poopName).intValue();
        } else {
            IInventory iinventory = new Inventory(poopStack);
            FurnaceRecipe irecipe = Minecraft.getInstance().world.getRecipeManager().getRecipe(IRecipeType.SMELTING, iinventory, Minecraft.getInstance().world).orElse(null);
            ItemStack burntItem = poopStack;
            if(irecipe != null && !irecipe.getRecipeOutput().isEmpty()){
                burntItem = irecipe.getRecipeOutput().copy();
            }
            BufferedImage texture = null;
            int color = 0XFFFFFF;
            try {
                Color texColour = getAverageColour(getTextureAtlas(burntItem));
                color = texColour.getRGB();
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
        int uMax = image.getWidth();
        int vMax = image.getHeight();
        for (float i = 0; i < uMax; i++)
            for (float j = 0; j < vMax; j++) {
                int alpha = image.getPixelRGBA(0, (int) i, (int) j) >> 24 & 0xFF;
                if (alpha != 255) {
                    continue;
                }
                red += image.getPixelRGBA(0, (int) i, (int) j) >> 0 & 0xFF;
                green += image.getPixelRGBA(0, (int) i, (int) j) >> 8 & 0xFF;
                blue += image.getPixelRGBA(0, (int) i, (int) j) >> 16 & 0xFF;
                count++;
            }
        //Average color
        return new Color((int) (red / count), (int) (green / count), (int) (blue / count));
    }

    private static TextureAtlasSprite getTextureAtlas(ItemStack oreStack) {
        return Minecraft.getInstance().getItemRenderer().getItemModelMesher().getParticleIcon(oreStack.getItem());
    }
}
