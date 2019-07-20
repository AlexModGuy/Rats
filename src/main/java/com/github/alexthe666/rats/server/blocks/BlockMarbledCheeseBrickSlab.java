package com.github.alexthe666.rats.server.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class BlockMarbledCheeseBrickSlab {
    public static class Double extends BlockGenericSlab {
        public Double(String name, float hardness, float resistance, SoundType soundType) {
            super(name, hardness, resistance, soundType, Material.ROCK, RatsBlockRegistry.MARBLED_CHEESE_BRICK);
            this.setHarvestLevel("pickaxe", 0);
        }

        @Override
        public boolean isDouble() {
            return true;
        }

        @Override
        public Item getItem(){
            return Item.getItemFromBlock(RatsBlockRegistry.MARBLED_CHEESE_BRICK_SLAB);
        }

        @Override
        public ItemBlock getItemBlock() {
            return new ItemBlockGenericSlab(this, RatsBlockRegistry.MARBLED_CHEESE_BRICK_SLAB, RatsBlockRegistry.MARBLED_CHEESE_BRICK_DOUBLESLAB);
        }
    }

    public static class Half extends BlockGenericSlab {
        public Half(String name, float hardness, float resistance, SoundType soundType) {
            super(name, hardness, resistance, soundType, Material.ROCK, RatsBlockRegistry.MARBLED_CHEESE_BRICK_SLAB);
            this.setHarvestLevel("pickaxe", 0);
        }

        @Override
        public boolean isDouble() {
            return false;
        }

        @Override
        public Item getItem(){
            return Item.getItemFromBlock(RatsBlockRegistry.MARBLED_CHEESE_BRICK_SLAB);
        }

        @Override
        public ItemBlock getItemBlock() {
            return new ItemBlockGenericSlab(this, RatsBlockRegistry.MARBLED_CHEESE_BRICK_SLAB, RatsBlockRegistry.MARBLED_CHEESE_BRICK_DOUBLESLAB);
        }
    }
}
