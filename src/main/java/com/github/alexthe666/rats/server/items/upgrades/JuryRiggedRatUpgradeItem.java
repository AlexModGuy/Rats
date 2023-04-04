package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.server.inventory.container.RatUpgradeContainer;
import com.github.alexthe666.rats.server.inventory.JuryRiggedRatUpgradeMenu;
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

public class JuryRiggedRatUpgradeItem extends BaseRatUpgradeItem {

	public JuryRiggedRatUpgradeItem(Item.Properties properties) {
		super(properties, 1, 2);
	}

	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		tooltip.add(Component.translatable("item.rats.rat_upgrade_combined.desc").withStyle(ChatFormatting.GRAY));
		CompoundTag tag = stack.getTag();

		if (tag != null && tag.contains("Items", 9)) {
			NonNullList<ItemStack> nonnulllist = NonNullList.withSize(2, ItemStack.EMPTY);
			ContainerHelper.loadAllItems(tag, nonnulllist);
			int i = 0;
			int j = 0;
			for (ItemStack itemstack : nonnulllist) {
				if (!itemstack.isEmpty()) {
					++j;
					if (i <= 4) {
						++i;
						tooltip.add(Component.literal(String.format("%s", itemstack.getDisplayName().getString())));
					}
				}
			}
		}
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		boolean flag = false;
		if (tag != null && tag.contains("Items", 9)) {
			NonNullList<ItemStack> nonnulllist = NonNullList.withSize(2, ItemStack.EMPTY);
			ContainerHelper.loadAllItems(tag, nonnulllist);
			flag = !nonnulllist.get(0).isEmpty() && !nonnulllist.get(1).isEmpty();
		}
		return flag;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		CompoundTag tag = stack.getTag();
		boolean flag = false;
		if (tag != null && tag.contains("Items", 9)) {
			NonNullList<ItemStack> nonnulllist = NonNullList.withSize(2, ItemStack.EMPTY);
			ContainerHelper.loadAllItems(tag, nonnulllist);
			flag = !nonnulllist.get(0).isEmpty() && !nonnulllist.get(1).isEmpty();
		}
		if (!player.isShiftKeyDown() && !flag) {
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
}