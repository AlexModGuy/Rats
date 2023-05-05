package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.server.entity.projectile.PurifyingLiquid;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PurifyingLiquidItem extends LoreTagItem {

	private final boolean nether;

	public PurifyingLiquidItem(Item.Properties properties, boolean nether) {
		super(properties, 1);
		this.nether = nether;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
		PurifyingLiquid entitypotion = new PurifyingLiquid(level, player, this.nether);
		Vec3 vec3 = player.getViewVector(1.0F);

		entitypotion.shoot(vec3.x(), vec3.y(), vec3.z(), 1.0F, 0.5F);
		level.addFreshEntity(entitypotion);
		if (!player.isCreative()) {
			itemstack.shrink(1);
		}
		return InteractionResultHolder.success(itemstack);
	}
}

