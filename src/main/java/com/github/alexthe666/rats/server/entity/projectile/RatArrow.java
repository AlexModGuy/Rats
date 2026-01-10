package com.github.alexthe666.rats.server.entity.projectile;

import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.RatCommand;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;

public class RatArrow extends AbstractArrow {

	private final ItemStack stack;

	public RatArrow(EntityType<? extends AbstractArrow> type, Level level) {
		super(type, level);
		this.stack = new ItemStack(RatsItemRegistry.RAT_ARROW.get());
	}

	public RatArrow(EntityType<? extends AbstractArrow> type, Level level, LivingEntity shooter, ItemStack stack) {
		super(type, shooter, level, stack.copy(), null);
		this.stack = stack;
	}

	@Override
	protected ItemStack getDefaultPickupItem() {
		return new ItemStack(RatsItemRegistry.RAT_ARROW.get());
	}

	private void spawnRat(@Nullable Entity entity, BlockPos pos) {
		if (this.pickup == Pickup.ALLOWED) {
			TamedRat rat = new TamedRat(RatsEntityRegistry.TAMED_RAT.get(), this.level());
			CompoundTag ratTag = new CompoundTag();
			CustomData customData = this.stack.get(DataComponents.CUSTOM_DATA);
			if (customData != null) {
				CompoundTag tag = customData.copyTag();
				if (tag.contains("Rat") && !tag.getCompound("Rat").isEmpty()) {
					ratTag = tag.getCompound("Rat");
				}
			}
			rat.readAdditionalSaveData(ratTag);
			if (!ratTag.getString("CustomName").isEmpty()) {
				rat.setCustomName(Component.Serializer.fromJson(ratTag.getString("CustomName"), this.level().registryAccess()));
			}
			if (ratTag.isEmpty()) {
				EventHooks.finalizeMobSpawn(rat, (ServerLevelAccessor) this.level(), this.level().getCurrentDifficultyAt(rat.blockPosition()), MobSpawnType.EVENT, null);
				if (this.getOwner() instanceof Player player) {
					rat.tame(player);
				}
			}
			rat.setCommand(RatCommand.WANDER);
			rat.setPos(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
			if (!this.level().isClientSide()) {
				this.level().addFreshEntity(rat);
			}
			if (entity instanceof LivingEntity living && !rat.isAlliedTo(entity)) {
				rat.setTarget(living);
			}
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		// Let parent handle all the damage, knockback, enchantment effects, and piercing logic
		super.onHitEntity(result);
		
		// Spawn rat at the hit location
		Entity entity = result.getEntity();
		if (!this.level().isClientSide()) {
			this.spawnRat(entity, entity.blockPosition());
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		if (this.inGround) {
			if (!this.level().isClientSide()) {
				this.spawnRat(null, result.getBlockPos().relative(result.getDirection()));
				this.spawnAtLocation(this.getPickupItem(), 0.0F);
			}
			this.discard();
		}
	}
}







