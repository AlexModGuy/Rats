package com.github.alexthe666.rats.server.loot;

import com.github.alexthe666.rats.registry.RatsLootRegistry;
import com.github.alexthe666.rats.server.entity.mount.RatMountBase;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public record RatKilledAndHasUpgradeCondition(Item upgrade) implements LootItemCondition {

	public static final MapCodec<RatKilledAndHasUpgradeCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			BuiltInRegistries.ITEM.byNameCodec().fieldOf("upgrade").forGetter(RatKilledAndHasUpgradeCondition::upgrade)
	).apply(instance, RatKilledAndHasUpgradeCondition::new));

	@Override
	public LootItemConditionType getType() {
		return RatsLootRegistry.KILLER_HAS_UPGRADE.get();
	}

	@Override
	public boolean test(LootContext context) {
		if (!context.hasParam(LootContextParams.ATTACKING_ENTITY)) return false;
		if (context.getParam(LootContextParams.ATTACKING_ENTITY) instanceof RatMountBase base) {
			return base.getRat() != null && RatUpgradeUtils.hasUpgrade(base.getRat(), this.upgrade());
		}
		return context.getParam(LootContextParams.ATTACKING_ENTITY) instanceof TamedRat rat && RatUpgradeUtils.hasUpgrade(rat, this.upgrade());
	}

	public static LootItemCondition.Builder hasUpgrade(Item upgrade) {
		return () -> new RatKilledAndHasUpgradeCondition(upgrade);
	}
}
