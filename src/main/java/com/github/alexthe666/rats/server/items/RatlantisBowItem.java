package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.server.entity.projectile.RatlantisArrow;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RatlantisBowItem extends BowItem {

	public RatlantisBowItem(Item.Properties properties) {
		super(properties);
	}

	public int getUseDuration(ItemStack stack) {
		return 36000;
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int useTicks) {
		if (entity instanceof Player player) {
			boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getTagEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
			ItemStack itemstack = player.getProjectile(stack);

			int i = this.getUseDuration(stack) - useTicks;
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, level, player, i, !itemstack.isEmpty() || flag);
			if (i < 0) return;

			if (!itemstack.isEmpty() || flag) {
				if (itemstack.isEmpty()) {
					itemstack = new ItemStack(Items.ARROW);
				}

				float f = getPowerForTime(i);
				if (!((double) f < 0.1D)) {
					boolean flag1 = player.getAbilities().instabuild || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem) itemstack.getItem()).isInfinite(itemstack, stack, player));
					if (!level.isClientSide) {
						ArrowItem arrowitem = (ArrowItem) (itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
						AbstractArrow arrow = arrowitem == Items.ARROW ? new RatlantisArrow(level, player) : arrowitem.createArrow(level, itemstack, player);
						arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);
						if (f == 1.0F) {
							arrow.setCritArrow(true);
						}

						int j = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
						if (j > 0) {
							arrow.setBaseDamage(arrow.getBaseDamage() + (double) j * 0.5D + 0.5D);
						}

						int k = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
						if (k > 0) {
							arrow.setKnockback(k);
						}

						if (EnchantmentHelper.getTagEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
							arrow.setSecondsOnFire(100);
						}

						stack.hurtAndBreak(1, player, user -> user.broadcastBreakEvent(player.getUsedItemHand()));
						if (flag1 || player.getAbilities().instabuild && (itemstack.is(Items.SPECTRAL_ARROW) || itemstack.is(Items.TIPPED_ARROW))) {
							arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
						}

						level.addFreshEntity(arrow);
					}

					level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
					if (!flag1 && !player.getAbilities().instabuild) {
						itemstack.shrink(1);
						if (itemstack.isEmpty()) {
							player.getInventory().removeItem(itemstack);
						}
					}

					player.awardStat(Stats.ITEM_USED.get(this));
				}
			}
		}
	}

	public static float getPowerForTime(int time) {
		float f = (float) time / 10.0F;
		f = (f * f + f * 2.0F) / 3.0F;
		if (f > 1.0F) {
			f = 1.0F;
		}

		return f;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		tooltip.add(Component.translatable("item.rats.ratlantis_bow.desc0").withStyle(ChatFormatting.YELLOW));
		tooltip.add(Component.translatable("item.rats.ratlantis_bow.desc1").withStyle(ChatFormatting.GRAY));
	}

}
