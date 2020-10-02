package com.github.alexthe666.rats.server.misc;

import com.github.alexthe666.rats.server.blocks.RatlantisBlockRegistry;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.items.RatlantisItemRegistry;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class TabRatlantis extends ItemGroup {

    public TabRatlantis() {
        super("rats.ratlantis");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(RatlantisBlockRegistry.CHUNKY_CHEESE_TOKEN);
    }
}
