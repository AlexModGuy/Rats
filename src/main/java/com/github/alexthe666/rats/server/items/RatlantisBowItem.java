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
import net.minecraft.world.level.Level;

import java.util.List;

// 1.21: Power/Punch/Flame enchant scaling is now driven by the enchantment-effect-component system on hit;
// instead of manually copying levels from the bow to the arrow at fire time, we just thread the bow stack through
// as AbstractArrow.firedFromWeapon (5th ctor arg). Vanilla's EnchantmentHelper.modifyDamage/modifyKnockback/etc.
// then read effects off that weapon when the arrow hits, applying the right damage/knockback/fire automatically.
public class RatlantisBowItem extends BowItem {

	public RatlantisBowItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 36000;
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int useTicks) {
		if (entity instanceof Player player) {
			ItemStack itemstack = player.getProjectile(stack);
			int i = this.getUseDuration(stack, entity) - useTicks;
			if (i < 0) return;

			if (!itemstack.isEmpty() || player.getAbilities().instabuild) {
				if (itemstack.isEmpty()) {
					itemstack = new ItemStack(Items.ARROW);
				}
				float f = getPowerForTime(i);
				if (!((double) f < 0.1D)) {
					if (!level.isClientSide) {
						// Pass the bow as the firedFromWeapon so vanilla applies Power/Punch/Flame enchant effects on hit.
						AbstractArrow arrow = new RatlantisArrow(level, player, stack.copy());
						arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);
						if (f == 1.0F) {
							arrow.setCritArrow(true);
						}
						stack.hurtAndBreak(1, player, player.getUsedItemHand() == net.minecraft.world.InteractionHand.MAIN_HAND ? net.minecraft.world.entity.EquipmentSlot.MAINHAND : net.minecraft.world.entity.EquipmentSlot.OFFHAND);
						level.addFreshEntity(arrow);
					}
					level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
					if (!player.getAbilities().instabuild) {
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
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		tooltip.add(Component.translatable("item.rats.ratlantis_bow.desc0").withStyle(ChatFormatting.YELLOW));
		tooltip.add(Component.translatable("item.rats.ratlantis_bow.desc1").withStyle(ChatFormatting.GRAY));
	}
}
