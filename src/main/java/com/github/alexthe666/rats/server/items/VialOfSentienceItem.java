package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.server.entity.projectile.VialOfSentience;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class VialOfSentienceItem extends LoreTagItem {

	public VialOfSentienceItem(Item.Properties properties) {
		super(properties, 1);
	}

	public boolean isFoil(ItemStack stack) {
		return true;
	}

	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
		VialOfSentience vial = new VialOfSentience(level, player);
		Vec3 vec3 = player.getViewVector(1.0F);
		vial.shoot(vec3.x(), vec3.y(), vec3.z(), 1.0F, 0.5F);
		level.addFreshEntity(vial);
		return InteractionResultHolder.success(itemstack);
	}
}
