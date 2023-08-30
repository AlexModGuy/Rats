package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.server.entity.RatMount;
import com.github.alexthe666.rats.server.misc.RatsLangConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MountRatUpgradeItem<T extends Mob & RatMount> extends BaseRatUpgradeItem {

	private final RegistryObject<EntityType<T>> entityType;

	public MountRatUpgradeItem(Item.Properties properties, int rarity, int textLength, RegistryObject<EntityType<T>> entityType) {
		super(properties, rarity, textLength);
		this.entityType = entityType;
	}

	public EntityType<?> getEntityType() {
		return this.entityType.get();
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		tooltip.add(Component.translatable(RatsLangConstants.MOUNT_RESPAWN_TIMER).withStyle(ChatFormatting.GRAY));
	}
}
