package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.ratlantis.RatlanteanFlame;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RatlanteanFlameItem extends LoreTagItem {

	public RatlanteanFlameItem(Item.Properties properties) {
		super(properties, 1);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (!player.isCreative()) {
			itemstack.shrink(1);
		}
		level.playSound(null, player.getX(), player.getY(), player.getZ(), RatsSoundRegistry.RATLANTEAN_FLAME_SHOOT.get(), SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
		if (!level.isClientSide()) {
			Vec3 vec3d = player.getViewVector(1.0F);
			RatlanteanFlame flame = new RatlanteanFlame(level, player, vec3d.x, vec3d.y, vec3d.z);
			flame.shoot(player, player.getXRot(), player.getYRot(), 1.5F, 0F);
			level.addFreshEntity(flame);
		}
		return InteractionResultHolder.success(itemstack);
	}
}
