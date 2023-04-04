package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.RatCaptureNet;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RatCaptureNetItem extends LoreTagItem {

	public RatCaptureNetItem(Item.Properties properties) {
		super(properties, 1);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		level.playSound(null, player.getX(), player.getY(), player.getZ(), RatsSoundRegistry.RAT_NET_THROW.get(), SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
		RatCaptureNet entitypotion = new RatCaptureNet(level, player);
		Vec3 vec3 = player.getViewVector(1.0F);

		entitypotion.shoot(vec3.x(), vec3.y(), vec3.z(), 1.0F, 0.5F);
		level.addFreshEntity(entitypotion);
		return InteractionResultHolder.success(itemstack);
	}
}

