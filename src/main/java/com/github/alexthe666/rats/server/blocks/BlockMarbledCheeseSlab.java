package com.github.alexthe666.rats.server.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockMarbledCheeseSlab {
    public static class Double extends BlockGenericSlab {
        public Double(String name, float hardness, float resistance, SoundType soundType) {
            super(name, hardness, resistance, soundType, Material.ROCK, RatsBlockRegistry.MARBLED_CHEESE);
            this.setHarvestLevel("pickaxe", 0);
        }

        @Override
        public Item getItem(){
            return Item.getItemFromBlock(RatsBlockRegistry.MARBLED_CHEESE_SLAB);
        }

        @Override
        public boolean isDouble() {
            return true;
        }

        @Override
        public ItemBlock getItemBlock() {
            return new ItemBlockGenericSlab(this, RatsBlockRegistry.MARBLED_CHEESE_SLAB, RatsBlockRegistry.MARBLED_CHEESE_DOUBLESLAB);
        }
    }

    public static class Half extends BlockGenericSlab {
        public Half(String name, float hardness, float resistance, SoundType soundType) {
            super(name, hardness, resistance, soundType, Material.ROCK, RatsBlockRegistry.MARBLED_CHEESE_SLAB);
            this.setHarvestLevel("pickaxe", 0);
        }

        @Override
        public Item getItem(){
            return Item.getItemFromBlock(RatsBlockRegistry.MARBLED_CHEESE_SLAB);
        }

        @Override
        public boolean isDouble() {
            return false;
        }

        @Override
        public ItemBlock getItemBlock() {
            return new ItemBlockGenericSlab(this, RatsBlockRegistry.MARBLED_CHEESE_SLAB, RatsBlockRegistry.MARBLED_CHEESE_DOUBLESLAB);
        }
    }
}
