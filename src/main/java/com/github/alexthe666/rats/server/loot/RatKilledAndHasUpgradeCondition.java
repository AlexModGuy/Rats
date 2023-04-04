package com.github.alexthe666.rats.server.loot;

import com.github.alexthe666.rats.registry.RatsLootRegistry;
import com.github.alexthe666.rats.server.entity.RatMountBase;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.registries.ForgeRegistries;

public record RatKilledAndHasUpgradeCondition(Item upgrade) implements LootItemCondition {

	@Override
	public LootItemConditionType getType() {
		return RatsLootRegistry.KILLER_HAS_UPGRADE.get();
	}

	@Override
	public boolean test(LootContext context) {
		if (!context.hasParam(LootContextParams.KILLER_ENTITY)) return false;
		if (context.getParam(LootContextParams.KILLER_ENTITY) instanceof RatMountBase base) {
			return base.getRat() != null && RatUpgradeUtils.hasUpgrade(base.getRat(), this.upgrade());
		}
		return context.getParam(LootContextParams.KILLER_ENTITY) instanceof TamedRat rat && RatUpgradeUtils.hasUpgrade(rat, this.upgrade());
	}

	public static LootItemCondition.Builder hasUpgrade(Item upgrade) {
		return () -> new RatKilledAndHasUpgradeCondition(upgrade);
	}

	public static class RatSerializer implements Serializer<RatKilledAndHasUpgradeCondition> {
		public void serialize(JsonObject object, RatKilledAndHasUpgradeCondition condition, JsonSerializationContext context) {
			object.addProperty("upgrade", ForgeRegistries.ITEMS.getKey(condition.upgrade()).toString());
		}

		public RatKilledAndHasUpgradeCondition deserialize(JsonObject object, JsonDeserializationContext context) {
			Item upgrade;
			try {
				upgrade = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(GsonHelper.getAsString(object, "upgrade")));
				return new RatKilledAndHasUpgradeCondition(upgrade);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
