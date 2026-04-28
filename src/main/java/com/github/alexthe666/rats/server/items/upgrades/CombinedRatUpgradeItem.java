package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.registry.RatsUpgradeConflictRegistry;
import com.github.alexthe666.rats.server.inventory.RatUpgradeMenu;
import com.github.alexthe666.rats.server.inventory.container.RatUpgradeContainer;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.CombinedUpgrade;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CombinedRatUpgradeItem extends BaseRatUpgradeItem implements CombinedUpgrade {

	public CombinedRatUpgradeItem(Item.Properties properties) {
		super(properties, 1, 1);
	}

	public static boolean canCombineWithUpgrade(ItemStack combiner, ItemStack stack) {
		if (stack.getItem() instanceof CombinedUpgrade) return false;
		CompoundTag tag = combiner.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		if (tag.contains("Items", 9)) {
			NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
			ContainerHelper.loadAllItems(tag, nonnulllist, net.minecraft.core.RegistryAccess.EMPTY);
			for (ItemStack contained : nonnulllist) {
				if (!(stack.getItem() instanceof BaseRatUpgradeItem) || stack.getItem() == contained.getItem() || RatsUpgradeConflictRegistry.doesConflict(contained, stack)) {
					return false;
				}
			}
		}
		return combiner.is(RatsItemRegistry.RAT_UPGRADE_JURY_RIGGED.get()) || combiner.is(RatsItemRegistry.RAT_UPGRADE_COMBINED.get()) || combiner.is(RatsItemRegistry.RAT_UPGRADE_COMBINED_CREATIVE.get());
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		if (stack.is(RatsItemRegistry.RAT_UPGRADE_COMBINED_CREATIVE.get())) {
			tooltip.add(Component.translatable("item.rats.rat_upgrade_combined.desc").withStyle(ChatFormatting.GRAY));
		}
		this.addTooltip(stack, tooltip);
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		if (stack.is(RatsItemRegistry.RAT_UPGRADE_COMBINED_CREATIVE.get())) {
			return true;
		}
		CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		boolean flag = false;
		if (tag.contains("Items", 9)) {
			NonNullList<ItemStack> nonnulllist = NonNullList.withSize(this.getUpgradeSlots(), ItemStack.EMPTY);
			ContainerHelper.loadAllItems(tag, nonnulllist, net.minecraft.core.RegistryAccess.EMPTY);
			flag = !nonnulllist.isEmpty();
		}
		return flag;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (this == RatsItemRegistry.RAT_UPGRADE_COMBINED_CREATIVE.get()) {
			//FIXME move this behavior to a right click on stack option.
			//basically, right clicking an upgrade into it or right clicking it on top of another upgrade will add the upgrade to it.
			//this is very similar to bundle logic, I think its better than a gui
			ItemStack stack = player.getItemInHand(hand);
			if (!player.isShiftKeyDown()) {
				if (!level.isClientSide()) {
					((ServerPlayer) player).openMenu(new MenuProvider() {
						@Override
						public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player1) {
							return new RatUpgradeMenu(id, new RatUpgradeContainer(stack), player.getInventory(), stack);
						}

						@Override
						public Component getDisplayName() {
							return CombinedRatUpgradeItem.this.getName(stack);
						}
					});
				}
				return InteractionResultHolder.success(stack);
			}
			return InteractionResultHolder.pass(stack);
		} else {
			return super.use(level, player, hand);
		}
	}

	@Override
	public int getUpgradeSlots() {
		return 27;
	}
}