package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatsToolMaterialRegistry;
import com.github.alexthe666.rats.server.entity.misc.RatProtector;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RatlantisSwordItem extends SwordItem {

	public RatlantisSwordItem(Item.Properties properties) {
		super(RatsToolMaterialRegistry.RATLANTIS, 3, -2.4F, properties);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
		if (entity instanceof LivingEntity living && living.isAlive() && !(living instanceof RatProtector) && player.swingTime == 0) {
			RatProtector protector = new RatProtector(RatlantisEntityRegistry.RAT_PROTECTOR.get(), entity.level());
			protector.moveTo(player.getX(), player.getY() + 1.0D, player.getZ(), player.getYRot(), player.getXRot());
			protector.setTarget(living);
			entity.level().addFreshEntity(protector);
		}
		return super.onLeftClickEntity(stack, player, entity);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		tooltip.add(Component.translatable(this.getDescriptionId() + ".desc0").withStyle(ChatFormatting.YELLOW));
		tooltip.add(Component.translatable(this.getDescriptionId() + ".desc1").withStyle(ChatFormatting.GRAY));
	}
}
