package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.item.ItemRecord;
import net.minecraft.util.SoundEvent;

public class ItemRatDisc extends ItemRecord {

    protected ItemRatDisc(String name, String recordName, SoundEvent soundIn) {
        super(recordName, soundIn);
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats." + name);
        this.setRegistryName(RatsMod.MODID, name);
    }
}
