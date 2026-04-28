package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.server.inventory.RatUpgradeMenu;
import com.github.alexthe666.rats.server.inventory.container.RatUpgradeContainer;
import com.github.alexthe666.rats.server.items.upgrades.BaseRatUpgradeItem;
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

public class RatListUpgradeItem extends BaseRatUpgradeItem {

	public RatListUpgradeItem(Item.Properties properties, int rarity, int textLength) {
		super(properties, rarity, textLength);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!player.isShiftKeyDown()) {
			if (!level.isClientSide()) {
				((ServerPlayer) player).openMenu(new MenuProvider() {
					@Override
					public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player1) {
						return new RatUpgradeMenu(id, new RatUpgradeContainer(stack), player1.getInventory(), stack);
					}

					@Override
					public Component getDisplayName() {
						return RatListUpgradeItem.this.getName(stack);
					}
				});
			}
		}
		return InteractionResultHolder.success(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();

		if (tag.contains("Items", 9)) {
			NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
			ContainerHelper.loadAllItems(tag, nonnulllist, net.minecraft.core.RegistryAccess.EMPTY);
			int i = 0;
			for (ItemStack itemstack : nonnulllist) {
				if (!itemstack.isEmpty()) {
					if (i <= 4) {
						++i;
						tooltip.add(Component.literal(String.format("%s", itemstack.getDisplayName().getString())));
					}
				}
			}
		}
	}
}