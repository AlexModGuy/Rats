package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.server.items.upgrades.interfaces.StatBoostingUpgrade;
import com.github.alexthe666.rats.server.misc.RatsLangConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class StatBoostingRatUpgradeItem extends BaseRatUpgradeItem implements StatBoostingUpgrade {

	private final Supplier<Map<Attribute, Double>> statChanges;
	private final boolean regens;

	public StatBoostingRatUpgradeItem(Properties properties, int rarity, Supplier<Map<Attribute, Double>> statChanges) {
		this(properties, rarity, 0, statChanges, false);
	}

	public StatBoostingRatUpgradeItem(Properties properties, int rarity, int textLength, Supplier<Map<Attribute, Double>> statChanges, boolean regens) {
		super(properties, rarity, textLength);
		this.statChanges = statChanges;
		this.regens = regens;
	}

	@Override
	public Map<Attribute, Double> getAttributeBoosts() {
		return this.statChanges.get();
	}

	@Override
	public boolean regeneratesHealth() {
		return this.regens;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		this.statChanges.get().forEach((attribute, aDouble) -> {
			if (aDouble > 0.0D) {
				tooltip.add(Component.translatable(RatsLangConstants.RAT_UPGRADE_STAT_BOOST, aDouble.toString(), Component.translatable(attribute.getDescriptionId())).withStyle(ChatFormatting.AQUA));
			}
		});

		if (this.regens) {
			tooltip.add(Component.translatable(RatsLangConstants.RAT_UPGRADE_REGENS).withStyle(ChatFormatting.AQUA));
		}
	}
}
