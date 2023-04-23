package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import com.github.alexthe666.rats.server.entity.projectile.RatArrow;
import com.github.alexthe666.rats.server.entity.rat.Rat;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
		CompoundTag ratTag = new CompoundTag();
		if (stack.getTag() != null) {
			ratTag = stack.getTag().getCompound("Rat");
		}
		Rat rat = new Rat(RatsEntityRegistry.RAT.get(), context.getLevel());
		BlockPos offset = context.getClickedPos().relative(context.getClickedFace());
		rat.readAdditionalSaveData(ratTag);
		rat.moveTo(offset.getX() + 0.5D, offset.getY(), offset.getZ() + 0.5D, 0, 0);
		if (!context.getLevel().isClientSide()) {
			context.getLevel().addFreshEntity(rat);
		}
		stack.shrink(1);
		context.getPlayer().setItemInHand(context.getHand(), new ItemStack(Items.ARROW));
		context.getPlayer().swing(context.getHand());
		return InteractionResult.SUCCESS;
	}

	@Override
	public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter) {
		return new RatArrow(RatsEntityRegistry.RAT_ARROW.get(), level, shooter, stack);
	}

	@Override
	public boolean isInfinite(ItemStack stack, ItemStack bow, Player player) {
		return false;
	}
}
