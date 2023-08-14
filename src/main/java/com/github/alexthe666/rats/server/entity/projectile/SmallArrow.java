package com.github.alexthe666.rats.server.entity.projectile;

import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class SmallArrow extends AbstractArrow {
	public SmallArrow(EntityType<? extends AbstractArrow> type, Level level) {
		super(type, level);
	}

	public SmallArrow(Level level, LivingEntity shooter) {
		super(RatsEntityRegistry.SMALL_ARROW.get(), shooter, level);
	}

	protected boolean canHitEntity(Entity entity) {
		Entity shooter = this.getOwner();
		return super.canHitEntity(entity) && (shooter == null || !this.isAlliedToOwner(entity));
	}

	private boolean isAlliedToOwner(Entity entity) {
		if (this.getOwner() instanceof OwnableEntity ownable && ownable.getOwner() == entity) return true;
		return entity instanceof OwnableEntity ownable && ownable.getOwner() == entity;
	}

	protected ItemStack getPickupItem() {
		return new ItemStack(Items.ARROW);
	}
}
