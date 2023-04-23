package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.server.entity.misc.RattlingGun;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class RattlingGunItem extends LoreTagItem {

	public RattlingGunItem(Item.Properties properties) {
		super(properties, 1);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		RattlingGun entity = new RattlingGun(RatlantisEntityRegistry.RATTLING_GUN.get(), context.getLevel());
		Player player = context.getPlayer();
		BlockPos offset = context.getClickedPos().relative(context.getClickedFace());
		if (context.getLevel().getBlockState(offset).getMaterial().isReplaceable()) {
			entity.moveTo(offset.getX() + 0.5D, offset.getY(), offset.getZ() + 0.5D, player.getYRot(), 0);
			float yaw = Mth.wrapDegrees(player.getYRot() + 180F);
			entity.yRotO = yaw;
			entity.setYRot(yaw);
			if (!player.isCreative()) {
				context.getItemInHand().shrink(1);
			}
			if (!context.getLevel().isClientSide()) {
				context.getLevel().addFreshEntity(entity);
			}

			return InteractionResult.SUCCESS;
		}
		return InteractionResult.FAIL;
	}
}
