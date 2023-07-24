package com.github.alexthe666.rats.server.entity.projectile;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.RatSackItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class RatCaptureNet extends ThrowableItemProjectile {

	public RatCaptureNet(EntityType<? extends ThrowableItemProjectile> type, Level level) {
		super(type, level);
	}

	public RatCaptureNet(Level level, LivingEntity thrower) {
		super(RatsEntityRegistry.RAT_CAPTURE_NET.get(), thrower, level);
	}

	@Override
	protected void onHit(HitResult result) {
		ItemStack sack = new ItemStack(RatsItemRegistry.RAT_SACK.get());
		if (!this.level().isClientSide() && this.getOwner() != null) {
			AABB axisalignedbb = this.getBoundingBox().inflate(16);
			List<TamedRat> list = this.level().getEntitiesOfClass(TamedRat.class, axisalignedbb);
			int capturedRat = 0;
			if (!list.isEmpty()) {
				for (TamedRat rat : list) {
					if (capturedRat >= RatConfig.ratSackCapacity) break;
					if (this.getOwner() instanceof LivingEntity owner && (rat.isOwnedBy(owner) || this.getOwner() instanceof Player player && player.isCreative())) {
						capturedRat++;
						RatSackItem.packRatIntoSack(sack, rat, capturedRat);
						this.level().broadcastEntityEvent(rat, (byte) 86);
						rat.discard();
					}
				}
				this.playSound(SoundEvents.ITEM_PICKUP, 1, 1);
			}
			this.discard();
		}
		ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), sack);
		itemEntity.setInvulnerable(true);
		if (!this.level().isClientSide()) {
			this.level().addFreshEntity(itemEntity);
		}
	}

	@Override
	protected Item getDefaultItem() {
		return RatsItemRegistry.RAT_CAPTURE_NET.get();
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(RatsItemRegistry.RAT_CAPTURE_NET.get());
	}
}
