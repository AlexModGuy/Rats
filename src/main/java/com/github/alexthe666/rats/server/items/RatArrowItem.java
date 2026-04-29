package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import com.github.alexthe666.rats.server.entity.projectile.RatArrow;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class RatArrowItem extends ArrowItem {

	public RatArrowItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		ItemStack stack = context.getPlayer().getItemInHand(context.getHand());
		CompoundTag stored = stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
		CompoundTag ratTag = stored.contains("Rat") ? stored.getCompound("Rat") : new CompoundTag();
		TamedRat rat = new TamedRat(RatsEntityRegistry.TAMED_RAT.get(), context.getLevel());
		BlockPos offset = context.getClickedPos().relative(context.getClickedFace());
		rat.readAdditionalSaveData(ratTag);
		if (!ratTag.getString("CustomName").isEmpty()) {
			rat.setCustomName(Component.Serializer.fromJson(ratTag.getString("CustomName"), net.minecraft.core.RegistryAccess.EMPTY));
		}
		rat.moveTo(offset.getX() + 0.5D, offset.getY(), offset.getZ() + 0.5D, 0, 0);
		if (!context.getLevel().isClientSide()) {
			context.getLevel().addFreshEntity(rat);
		}
		stack.shrink(1);
		context.getPlayer().setItemInHand(context.getHand(), new ItemStack(Items.ARROW));
		context.getPlayer().swing(context.getHand());
		return InteractionResult.SUCCESS;
	}

	// 1.21: ArrowItem.createArrow now takes (Level, LivingEntity shooter, ItemStack arrow, @Nullable ItemStack weapon).
	@Override
	public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter, @org.jetbrains.annotations.Nullable ItemStack weapon) {
		return new RatArrow(RatsEntityRegistry.RAT_ARROW.get(), level, shooter, stack);
	}

	// PORT-STUB: ArrowItem.isInfinite removed in 1.21; infinite-arrow override is now driven by tags/stack components.
	@SuppressWarnings("unused")
	public boolean isInfinite(ItemStack stack, ItemStack bow, Player player) {
		return false;
	}
}
