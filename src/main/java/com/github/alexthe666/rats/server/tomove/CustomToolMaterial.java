package com.github.alexthe666.rats.server.tomove;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;

public class CustomToolMaterial implements IItemTier {
   private String name;
   private int harvestLevel;
   private int durability;
   private float damage;
   private float speed;
   private int enchantability;
    private Ingredient ingredient = null;

    public CustomToolMaterial(String name, int harvestLevel, int durability, float damage, float speed, int enchantability) {
        this.name = name;
        this.harvestLevel = harvestLevel;
        this.durability = durability;
        this.damage = damage;
        this.speed = speed;
        this.enchantability = enchantability;
    }

    public String getName() {
        return name;
    }

    @Override
    public int getMaxUses() {
        return durability;
    }

    @Override
    public float getEfficiency() {
        return speed;
    }

    @Override
    public float getAttackDamage() {
        return damage;
    }

    @Override
    public int getHarvestLevel() {
        return harvestLevel;
    }

    @Override
    public int getEnchantability() {
        return enchantability;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return ingredient == null ? Ingredient.EMPTY : ingredient;
    }

    public void setRepairMaterial(Ingredient ingredient){
        this.ingredient = ingredient;
    }
}
