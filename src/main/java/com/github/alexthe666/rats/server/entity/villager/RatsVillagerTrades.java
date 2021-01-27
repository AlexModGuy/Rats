package com.github.alexthe666.rats.server.entity.villager;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.*;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionBrewing;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RatsVillagerTrades {
    public static final Int2ObjectMap<VillagerTrades.ITrade[]> PLAGUE_DOCTOR_TRADES;
    public static final VillagerTrades.ITrade COMBINER_TRADE = new ItemsForEmeraldsAndItemsTrade(RatsItemRegistry.RAT_UPGRADE_GOD, 1, 40, RatsBlockRegistry.UPGRADE_COMBINER.asItem(), 1, 1, 30);
    public static final VillagerTrades.ITrade SEPERATOR_TRADE = new ItemsForEmeraldsAndItemsTrade(RatsItemRegistry.RAT_UPGRADE_JURY_RIGGED, 1, 4, RatsBlockRegistry.UPGRADE_SEPARATOR.asItem(), 1, 1, 30);
    public static final VillagerTrades.ITrade UPGRADE_COMBINED_TRADE = new ItemsForEmeraldsTrade(RatsItemRegistry.RAT_UPGRADE_COMBINED, 6, 1, 1);
    static {
        PLAGUE_DOCTOR_TRADES = createTrades(ImmutableMap.of(1,
                new VillagerTrades.ITrade[]{
                        new EmeraldForItemsTrade(RatsItemRegistry.RAW_RAT, 10, 1, 3),
                        new ItemsForEmeraldsTrade(Items.BONE, 3, 8, 9, 1),
                        new ItemsForEmeraldsTrade(Items.ROTTEN_FLESH, 2, 10, 9, 2),
                        new EmeraldForItemsTrade(Items.POISONOUS_POTATO, 1, 3, 15),
                        new EmeraldForItemsTrade(RatsItemRegistry.PLAGUE_ESSENCE, 3, 3, 15),
                        new EmeraldForItemsTrade(RatsItemRegistry.CONTAMINATED_FOOD, 1, 1, 3),
                        new ItemsForEmeraldsTrade(RatsItemRegistry.COOKED_RAT, 1, 5, 15, 10),
                        new ItemsForEmeraldsAndItemsTrade(Blocks.POPPY, 5, 3, RatsItemRegistry.HERB_BUNDLE, 3, 1, 5),
                        new ItemsForEmeraldsTrade(RatsBlockRegistry.DYE_SPONGE, 1, 1, 4, 4),
                        new ItemsForEmeraldsTrade(RatsItemRegistry.TREACLE, 2, 3, 10, 5),
                        new ItemsForEmeraldsTrade(RatsBlockRegistry.GARBAGE_PILE, 5, 4, 5, 3),
                        new ItemsForEmeraldsTrade(RatsBlockRegistry.RAT_CAGE, 2, 4, 4, 5),
                        new ItemsForEmeraldsTrade(RatsItemRegistry.PLAGUE_DOCTOR_MASK, 15, 1, 2, 10),
                        new ItemsForEmeraldsTrade(RatsItemRegistry.EXTERMINATOR_HAT, 5, 1, 2, 10),
                        new ItemsForEmeraldsTrade(RatsItemRegistry.RAT_SKULL, 3, 1, 15, 10),
                },
                //Only 3 of these appears per plague doctor
                2, new VillagerTrades.ITrade[]{
                        new ItemsForEmeraldsTrade(RatsItemRegistry.PLAGUE_LEECH, 3, 1, 15, 6),
                        new ItemsForEmeraldsTrade(RatsItemRegistry.PLAGUE_STEW, 7, 2, 8, 6),
                        new ItemsForEmeraldsTrade(RatsItemRegistry.RAT_SACK, 2, 1, 2, 5),
                        new ItemsForEmeraldsTrade(RatsItemRegistry.PURIFYING_LIQUID, 8, 2, 5, 7),
                        new ItemsForEmeraldsTrade(RatsItemRegistry.TOKEN_FRAGMENT, 3, 1, 20, 2),
                        new ItemsForEmeraldsTrade(RatsItemRegistry.RAT_UPGRADE_BASIC, 4, 2, 6, 10),
                        new ItemsForEmeraldsTrade(RatsItemRegistry.PLAGUE_ESSENCE, 2, 1, 10, 6),
                        new ItemsForEmeraldsTrade(ForgeRegistries.ITEMS.getValue(new ResourceLocation("rats:cheese_banner_pattern")), 1, 1, 5, 2),
                        new ItemsForEmeraldsTrade(ForgeRegistries.ITEMS.getValue(new ResourceLocation("rats:rat_banner_pattern")), 1, 1, 5, 2),
                        new ItemsForEmeraldsTrade(RatsItemRegistry.STRING_CHEESE, 2, 8, 5, 6),
                        new ItemsForEmeraldsTrade(RatsItemRegistry.GOLDEN_RAT_SKULL, 8, 2, 5, 6),
                        new ItemsForEmeraldsTrade(RatsItemRegistry.PLAGUE_TOME, 32, 1, 1, 20),
                }));
    }

    private static Int2ObjectMap<VillagerTrades.ITrade[]> createTrades(ImmutableMap<Integer, VillagerTrades.ITrade[]> p_221238_0_) {
        return new Int2ObjectOpenHashMap(p_221238_0_);
    }

    static class ItemsForEmeraldsAndItemsTrade implements VillagerTrades.ITrade {
        private final ItemStack field_221200_a;
        private final int field_221201_b;
        private final int field_221202_c;
        private final ItemStack field_221203_d;
        private final int field_221204_e;
        private final int field_221205_f;
        private final int field_221206_g;
        private final float field_221207_h;

        public ItemsForEmeraldsAndItemsTrade(IItemProvider p_i50533_1_, int p_i50533_2_, Item p_i50533_3_, int p_i50533_4_, int p_i50533_5_, int p_i50533_6_) {
            this(p_i50533_1_, p_i50533_2_, 1, p_i50533_3_, p_i50533_4_, p_i50533_5_, p_i50533_6_);
        }

        public ItemsForEmeraldsAndItemsTrade(IItemProvider p_i50534_1_, int p_i50534_2_, int p_i50534_3_, Item p_i50534_4_, int p_i50534_5_, int p_i50534_6_, int p_i50534_7_) {
            this.field_221200_a = new ItemStack(p_i50534_1_);
            this.field_221201_b = p_i50534_2_;
            this.field_221202_c = p_i50534_3_;
            this.field_221203_d = new ItemStack(p_i50534_4_);
            this.field_221204_e = p_i50534_5_;
            this.field_221205_f = p_i50534_6_;
            this.field_221206_g = p_i50534_7_;
            this.field_221207_h = 0.05F;
        }

        @Nullable
        public MerchantOffer getOffer(Entity p_221182_1_, Random p_221182_2_) {
            return new MerchantOffer(new ItemStack(Items.EMERALD, this.field_221202_c), new ItemStack(this.field_221200_a.getItem(), this.field_221201_b), new ItemStack(this.field_221203_d.getItem(), this.field_221204_e), this.field_221205_f, this.field_221206_g, this.field_221207_h);
        }
    }

    static class EnchantedBookForEmeraldsTrade implements VillagerTrades.ITrade {
        private final int field_221194_a;

        public EnchantedBookForEmeraldsTrade(int p_i50537_1_) {
            this.field_221194_a = p_i50537_1_;
        }

        public MerchantOffer getOffer(Entity trader, Random rand) {
            List<Enchantment> list = Registry.ENCHANTMENT.stream().filter(Enchantment::canVillagerTrade).collect(Collectors.toList());
            Enchantment enchantment = list.get(rand.nextInt(list.size()));
            int i = MathHelper.nextInt(rand, enchantment.getMinLevel(), enchantment.getMaxLevel());
            ItemStack itemstack = EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(enchantment, i));
            int j = 2 + rand.nextInt(5 + i * 10) + 3 * i;
            if (enchantment.isTreasureEnchantment()) {
                j *= 2;
            }

            if (j > 64) {
                j = 64;
            }

            return new MerchantOffer(new ItemStack(Items.EMERALD, j), new ItemStack(Items.BOOK), itemstack, 12, this.field_221194_a, 0.2F);
        }
    }

    static class ItemWithPotionForEmeraldsAndItemsTrade implements VillagerTrades.ITrade {
        private final ItemStack field_221219_a;
        private final int field_221220_b;
        private final int field_221221_c;
        private final int field_221222_d;
        private final int field_221223_e;
        private final Item field_221224_f;
        private final int field_221225_g;
        private final float field_221226_h;

        public ItemWithPotionForEmeraldsAndItemsTrade(Item p_i50526_1_, int p_i50526_2_, Item p_i50526_3_, int p_i50526_4_, int p_i50526_5_, int p_i50526_6_, int p_i50526_7_) {
            this.field_221219_a = new ItemStack(p_i50526_3_);
            this.field_221221_c = p_i50526_5_;
            this.field_221222_d = p_i50526_6_;
            this.field_221223_e = p_i50526_7_;
            this.field_221224_f = p_i50526_1_;
            this.field_221225_g = p_i50526_2_;
            this.field_221220_b = p_i50526_4_;
            this.field_221226_h = 0.05F;
        }

        public MerchantOffer getOffer(Entity p_221182_1_, Random p_221182_2_) {
            ItemStack lvt_3_1_ = new ItemStack(Items.EMERALD, this.field_221221_c);
            List<Potion> lvt_4_1_ = (List)Registry.POTION.stream().filter((p_221218_0_) -> {
                return !p_221218_0_.getEffects().isEmpty() && PotionBrewing.isBrewablePotion(p_221218_0_);
            }).collect(Collectors.toList());
            Potion lvt_5_1_ = (Potion)lvt_4_1_.get(p_221182_2_.nextInt(lvt_4_1_.size()));
            ItemStack lvt_6_1_ = PotionUtils.addPotionToItemStack(new ItemStack(this.field_221219_a.getItem(), this.field_221220_b), lvt_5_1_);
            return new MerchantOffer(lvt_3_1_, new ItemStack(this.field_221224_f, this.field_221225_g), lvt_6_1_, this.field_221222_d, this.field_221223_e, this.field_221226_h);
        }
    }

    static class EnchantedItemForEmeraldsTrade implements VillagerTrades.ITrade {
        private final ItemStack field_221195_a;
        private final int field_221196_b;
        private final int field_221197_c;
        private final int field_221198_d;
        private final float field_221199_e;

        public EnchantedItemForEmeraldsTrade(Item p_i50535_1_, int p_i50535_2_, int p_i50535_3_, int p_i50535_4_) {
            this(p_i50535_1_, p_i50535_2_, p_i50535_3_, p_i50535_4_, 0.05F);
        }

        public EnchantedItemForEmeraldsTrade(Item p_i50536_1_, int p_i50536_2_, int p_i50536_3_, int p_i50536_4_, float p_i50536_5_) {
            this.field_221195_a = new ItemStack(p_i50536_1_);
            this.field_221196_b = p_i50536_2_;
            this.field_221197_c = p_i50536_3_;
            this.field_221198_d = p_i50536_4_;
            this.field_221199_e = p_i50536_5_;
        }

        public MerchantOffer getOffer(Entity p_221182_1_, Random p_221182_2_) {
            int lvt_3_1_ = 5 + p_221182_2_.nextInt(15);
            ItemStack lvt_4_1_ = EnchantmentHelper.addRandomEnchantment(p_221182_2_, new ItemStack(this.field_221195_a.getItem()), lvt_3_1_, false);
            int lvt_5_1_ = Math.min(this.field_221196_b + lvt_3_1_, 64);
            ItemStack lvt_6_1_ = new ItemStack(Items.EMERALD, lvt_5_1_);
            return new MerchantOffer(lvt_6_1_, lvt_4_1_, this.field_221197_c, this.field_221198_d, this.field_221199_e);
        }
    }

    static class SuspiciousStewForEmeraldTrade implements VillagerTrades.ITrade {
        final Effect field_221214_a;
        final int field_221215_b;
        final int field_221216_c;
        private final float field_221217_d;

        public SuspiciousStewForEmeraldTrade(Effect p_i50527_1_, int p_i50527_2_, int p_i50527_3_) {
            this.field_221214_a = p_i50527_1_;
            this.field_221215_b = p_i50527_2_;
            this.field_221216_c = p_i50527_3_;
            this.field_221217_d = 0.05F;
        }

        @Nullable
        public MerchantOffer getOffer(Entity p_221182_1_, Random p_221182_2_) {
            ItemStack lvt_3_1_ = new ItemStack(Items.SUSPICIOUS_STEW, 1);
            SuspiciousStewItem.addEffect(lvt_3_1_, this.field_221214_a, this.field_221215_b);
            return new MerchantOffer(new ItemStack(Items.EMERALD, 1), lvt_3_1_, 12, this.field_221216_c, this.field_221217_d);
        }
    }

    static class ItemsForEmeraldsTrade implements VillagerTrades.ITrade {
        private final ItemStack stack;
        private final int emeraldCount;
        private final int itemCount;
        private final int maxUses;
        private final int exp;
        private final float multiplier;

        public ItemsForEmeraldsTrade(Block p_i50528_1_, int p_i50528_2_, int p_i50528_3_, int p_i50528_4_, int p_i50528_5_) {
            this(new ItemStack(p_i50528_1_), p_i50528_2_, p_i50528_3_, p_i50528_4_, p_i50528_5_);
        }

        public ItemsForEmeraldsTrade(Item p_i50529_1_, int p_i50529_2_, int p_i50529_3_, int p_i50529_4_) {
            this((ItemStack)(new ItemStack(p_i50529_1_)), p_i50529_2_, p_i50529_3_, 12, p_i50529_4_);
        }

        public ItemsForEmeraldsTrade(Item item, int emeralds, int items, int maxUses, int exp) {
            this(new ItemStack(item), emeralds, items, maxUses, exp);
        }

        public ItemsForEmeraldsTrade(ItemStack stack, int emeralds, int items, int maxUses, int exp) {
            this(stack, emeralds, items, maxUses, exp, 0.05F);
        }

        public ItemsForEmeraldsTrade(ItemStack stack, int emeralds, int items, int maxUses, int exp, float multi) {
            this.stack = stack;
            this.emeraldCount = emeralds;
            this.itemCount = items;
            this.maxUses = maxUses;
            this.exp = exp;
            this.multiplier = multi;
        }

        public MerchantOffer getOffer(Entity p_221182_1_, Random p_221182_2_) {
            return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCount), new ItemStack(this.stack.getItem(), this.itemCount), this.maxUses, this.exp, this.multiplier);
        }
    }

    static class EmeraldForItemsTrade implements VillagerTrades.ITrade {
        private final Item field_221183_a;
        private final int field_221184_b;
        private final int field_221185_c;
        private final int field_221186_d;
        private final float field_221187_e;

        public EmeraldForItemsTrade(IItemProvider p_i50539_1_, int p_i50539_2_, int p_i50539_3_, int p_i50539_4_) {
            this.field_221183_a = p_i50539_1_.asItem();
            this.field_221184_b = p_i50539_2_;
            this.field_221185_c = p_i50539_3_;
            this.field_221186_d = p_i50539_4_;
            this.field_221187_e = 0.05F;
        }

        public MerchantOffer getOffer(Entity p_221182_1_, Random p_221182_2_) {
            ItemStack lvt_3_1_ = new ItemStack(this.field_221183_a, this.field_221184_b);
            return new MerchantOffer(lvt_3_1_, new ItemStack(Items.EMERALD), this.field_221185_c, this.field_221186_d, this.field_221187_e);
        }
    }
}
