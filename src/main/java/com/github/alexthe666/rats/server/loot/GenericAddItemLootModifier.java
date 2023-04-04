package com.github.alexthe666.rats.server.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GenericAddItemLootModifier extends LootModifier {

	public static final Codec<GenericAddItemLootModifier> CODEC = RecordCodecBuilder.create(inst -> LootModifier.codecStart(inst).and(
					inst.group(
							Codec.unboundedMap(ForgeRegistries.ITEMS.getCodec(), Codec.INT).fieldOf("items").forGetter(m -> m.items),
							Codec.BOOL.fieldOf("replacePool").orElse(false).forGetter(m -> m.makeNewPool)))
			.apply(inst, GenericAddItemLootModifier::new));

	private final Map<Item, Integer> items;
	private final boolean makeNewPool;

	private GenericAddItemLootModifier(LootItemCondition[] conditions, Map<Item, Integer> items, boolean makeNewPool) {
		super(conditions);
		this.items = items;
		this.makeNewPool = makeNewPool;
	}

	public GenericAddItemLootModifier(LootItemCondition[] conditions, boolean makeNewPool, ItemStack... stacks) {
		super(conditions);
		this.items = new HashMap<>();
		for (ItemStack stack : stacks) {
			this.items.put(stack.getItem(), stack.getCount());
		}
		this.makeNewPool = makeNewPool;
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		if (this.makeNewPool) {
			generatedLoot.clear();
			if (this.items.size() > 1) {
				int chosenEntry = context.getRandom().nextInt(this.items.size());
				Item chosenItem = new ArrayList<>(this.items.keySet()).get(chosenEntry);
				generatedLoot.add(new ItemStack(chosenItem, this.items.get(chosenItem)));
			} else {
				for (Map.Entry<Item, Integer> entry : this.items.entrySet()) {
					generatedLoot.add(new ItemStack(entry.getKey(), entry.getValue()));
				}
			}
		} else {
			for (Map.Entry<Item, Integer> entry : this.items.entrySet()) {
				generatedLoot.add(new ItemStack(entry.getKey(), entry.getValue()));
			}
		}
		return generatedLoot;
	}

	@Override
	public Codec<? extends IGlobalLootModifier> codec() {
		return CODEC;
	}
}
