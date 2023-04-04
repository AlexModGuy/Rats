package com.github.alexthe666.rats.server.block.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.registry.RatsBlockEntityRegistry;
import com.github.alexthe666.rats.server.items.upgrades.CombinedRatUpgradeItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class UpgradeSeparatorBlockEntity extends BlockEntity {
	public float ratRotation;
	public float ratRotationPrev;

	public UpgradeSeparatorBlockEntity(BlockPos pos, BlockState state) {
		super(RatsBlockEntityRegistry.UPGRADE_SEPERATOR.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, UpgradeSeparatorBlockEntity te) {
		te.ratRotationPrev = te.ratRotation;
		te.ratRotation++;
		float i = pos.getX() + 0.5F;
		float j = pos.getY() + 0.75F;
		float k = pos.getZ() + 0.5F;
		double d0 = 0.5D;
		for (ItemEntity itemEntity : level.getEntitiesOfClass(ItemEntity.class, new AABB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0))) {
			ItemStack item = itemEntity.getItem();
			if (item.getItem() instanceof CombinedRatUpgradeItem) {
				CompoundTag CompoundNBT1 = item.getTag();
				int spawnedItem = 0;
				if (CompoundNBT1 != null && CompoundNBT1.contains("Items", 9)) {
					NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
					ContainerHelper.loadAllItems(CompoundNBT1, nonnulllist);
					for (ItemStack itemstack : nonnulllist) {
						if (!itemstack.isEmpty()) {
							ItemEntity splitEntity = new ItemEntity(level, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), itemstack.copy());
							if (!level.isClientSide()) {
								level.addFreshEntity(splitEntity);
							}
							spawnedItem++;
						}
					}
				}
				if (spawnedItem > 0) {
					itemEntity.playSound(SoundEvents.ITEM_BREAK, 1, 1);
					itemEntity.kill();
					ItemEntity splitEntity = new ItemEntity(level, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), new ItemStack(getFuel(), spawnedItem));
					if (!level.isClientSide()) {
						level.addFreshEntity(splitEntity);
					}
				}
			}
		}
	}

	public static Item getFuel() {
		if (RatsMod.RATLANTIS_DATAPACK_ENABLED) {
			return RatlantisItemRegistry.GEM_OF_RATLANTIS.get();
		} else {
			return Items.EMERALD;
		}
	}

	public AABB getRenderBoundingBox() {
		return new AABB(this.getBlockPos(), this.getBlockPos().offset(1, 2, 1));
	}
}
