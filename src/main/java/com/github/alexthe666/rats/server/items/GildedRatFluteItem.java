package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.projectile.RatShot;
import com.github.alexthe666.rats.registry.RatVariantRegistry;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class GildedRatFluteItem extends LoreTagItem {

	public GildedRatFluteItem(Item.Properties properties) {
		super(properties, 1);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.TOOT_HORN;
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
		return repair.is(RatsItemRegistry.TANGLED_RAT_TAILS.get());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getTag() != null) {
			RatShot ratShot = new RatShot(RatsEntityRegistry.RAT_SHOT.get(), level, player);
			ratShot.setColorVariant(RatVariantRegistry.getRandomVariant(player.getRandom(), false));
			Vec3 vector3d = player.getViewVector(1.0F);
			ratShot.shoot(vector3d.x(), vector3d.y(), vector3d.z(), 1.0F, 1.5F);
			level.addFreshEntity(ratShot);
			stack.hurtAndBreak(1, player, user -> user.broadcastBreakEvent(hand));
			player.swing(hand);
			player.getCooldowns().addCooldown(this, 10);
			level.playSound(player, player.blockPosition(), RatsSoundRegistry.getFluteSound(), SoundSource.PLAYERS, 0.5F, 0.75F);
		}
		return InteractionResultHolder.success(stack);
	}
}
