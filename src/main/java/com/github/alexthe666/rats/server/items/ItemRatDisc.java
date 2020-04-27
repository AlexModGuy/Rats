package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.item.Rarity;
import net.minecraft.util.SoundEvent;

public class ItemRatDisc extends MusicDiscItem {

    protected ItemRatDisc(String name, SoundEvent soundIn) {
        super(13, soundIn, (new Item.Properties()).maxStackSize(1).group(RatsMod.TAB).rarity(Rarity.RARE));
        this.setRegistryName(RatsMod.MODID, name);
    }
}
