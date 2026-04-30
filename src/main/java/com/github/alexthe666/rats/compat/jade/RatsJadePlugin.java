package com.github.alexthe666.rats.compat.jade;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatsEffectRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;

// Jade tooltip provider for tamed rats. Surfaces the rat's owner, equipped upgrades, and active
// plague status — info that is otherwise hidden behind the rat-staff GUI. Loaded automatically
// by Jade via the @WailaPlugin annotation; if the player doesn't have Jade installed this class
// is simply never instantiated.
@WailaPlugin
public class RatsJadePlugin implements IWailaPlugin {

	private static final ResourceLocation TAMED_RAT_INFO = ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "tamed_rat_info");

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerEntityComponent(TamedRatProvider.INSTANCE, TamedRat.class);
	}

	private enum TamedRatProvider implements IEntityComponentProvider {
		INSTANCE;

		@Override
		public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
			Entity entity = accessor.getEntity();
			if (!(entity instanceof TamedRat rat)) return;

			// Plague indicator — useful so the player doesn't accidentally pick up a plague-carrier
			// before they've cured it.
			if (rat.hasEffect(RatsEffectRegistry.PLAGUE)) {
				tooltip.add(Component.translatable("entity.rats.rat.plague").withStyle(ChatFormatting.DARK_GREEN, ChatFormatting.ITALIC));
			}

			// Owner name (only shown when the player who's looking is not the owner — own rats are
			// already obvious from the highlight tint).
			if (rat.getOwner() != null && !rat.getOwner().equals(accessor.getPlayer())) {
				tooltip.add(Component.translatable("entity.rats.rat.owner_label",
								Component.literal(rat.getOwner().getName().getString()).withStyle(ChatFormatting.YELLOW))
						.withStyle(ChatFormatting.GRAY));
			}

			// Upgrades — iterate the equipment slots that double as upgrade carriers and show each
			// non-empty entry's display name. Capped at 6 lines so the Jade box stays compact on
			// heavily-loadout rats.
			int shown = 0;
			for (EquipmentSlot slot : RatUpgradeUtils.UPGRADE_SLOTS) {
				if (shown >= 6) break;
				ItemStack upgrade = rat.getItemBySlot(slot);
				if (upgrade.isEmpty()) continue;
				Component name = upgrade.get(DataComponents.CUSTOM_NAME);
				if (name == null) name = upgrade.getHoverName();
				tooltip.add(Component.literal("• ").append(name).withStyle(ChatFormatting.AQUA));
				shown++;
			}
		}

		@Override
		public ResourceLocation getUid() {
			return TAMED_RAT_INFO;
		}
	}
}
