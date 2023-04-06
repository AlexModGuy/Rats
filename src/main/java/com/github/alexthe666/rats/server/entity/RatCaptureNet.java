package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
		CompoundTag tag = new CompoundTag();
		if (!this.getLevel().isClientSide() && this.getOwner() != null) {
			AABB axisalignedbb = this.getBoundingBox().inflate(16);
			List<TamedRat> list = this.getLevel().getEntitiesOfClass(TamedRat.class, axisalignedbb);
			int capturedRat = 0;
			if (!list.isEmpty()) {
				for (TamedRat rat : list) {
					if (capturedRat >= 32) break;
					if (this.getOwner() instanceof LivingEntity owner && (rat.isOwnedBy(owner) || this.getOwner() instanceof Player player && player.isCreative())) {
						CompoundTag ratTag = new CompoundTag();
						rat.addAdditionalSaveData(ratTag);
						if (rat.hasCustomName()) {
							ratTag.putString("CustomName", Component.Serializer.toJson(rat.getCustomName()));
						}
						capturedRat++;
						this.getLevel().broadcastEntityEvent(rat, (byte) 86);
						tag.put("Rat_" + capturedRat, ratTag);
						rat.discard();
					}
				}
				this.playSound(SoundEvents.ITEM_PICKUP, 1, 1);
			}
			this.discard();
		}
		sack.setTag(tag);
		ItemEntity itemEntity = new ItemEntity(this.getLevel(), this.getX(), this.getY(), this.getZ(), sack);
		itemEntity.setInvulnerable(true);
		if (!this.getLevel().isClientSide()) {
			this.getLevel().addFreshEntity(itemEntity);
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
