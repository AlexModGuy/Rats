package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.server.inventory.container.RatUpgradeContainer;
import com.github.alexthe666.rats.server.inventory.JuryRiggedRatUpgradeMenu;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.CombinedUpgrade;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JuryRiggedRatUpgradeItem extends BaseRatUpgradeItem implements CombinedUpgrade {

	public JuryRiggedRatUpgradeItem(Item.Properties properties) {
		super(properties, 1, 2);
	}

	@Override
	public int getUpgradeSlots() {
		return 2;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		tooltip.add(Component.translatable("item.rats.rat_upgrade_combined.desc").withStyle(ChatFormatting.GRAY));
		this.addTooltip(stack, tooltip);
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return this.isUpgradeLocked(stack);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (!player.isShiftKeyDown() && !this.isUpgradeLocked(stack)) {
			if (!level.isClientSide()) {
				NetworkHooks.openScreen((ServerPlayer) player, new MenuProvider() {
					@Override
					public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player1) {
						return new JuryRiggedRatUpgradeMenu(id, new RatUpgradeContainer(stack), player1.getInventory(), stack);
					}

					@Override
					public Component getDisplayName() {
						return JuryRiggedRatUpgradeItem.this.getName(stack);
					}
				});
				return InteractionResultHolder.success(stack);
			}
		}
		return InteractionResultHolder.pass(stack);
	}

	private boolean isUpgradeLocked(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		if (tag != null && tag.contains("Items", 9)) {
			NonNullList<ItemStack> nonnulllist = NonNullList.withSize(this.getUpgradeSlots(), ItemStack.EMPTY);
			ContainerHelper.loadAllItems(tag, nonnulllist);
			return !nonnulllist.get(0).isEmpty() && !nonnulllist.get(1).isEmpty();
		}
		return false;
	}
}