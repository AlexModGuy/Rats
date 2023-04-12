package com.github.alexthe666.rats.server.block.entity;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.registry.RatsBlockEntityRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class RatCageBreedingLanternBlockEntity extends DecoratedRatCageBlockEntity {

	private int breedingCooldown = 0;

	public RatCageBreedingLanternBlockEntity(BlockPos pos, BlockState state) {
		super(RatsBlockEntityRegistry.RAT_CAGE_BREEDING_LANTERN.get(), pos, state);
	}

	@Override
	public ItemStack getContainedItem() {
		return new ItemStack(RatsItemRegistry.RAT_BREEDING_LANTERN.get());
	}

	@Override
	public void setContainedItem(ItemStack stack) {
	}

	public int getBreedingCooldown() {
		return this.breedingCooldown;
	}

	@Override
	public void saveAdditional(CompoundTag compound) {
		compound.putInt("BreedingCooldown", this.breedingCooldown);
		super.saveAdditional(compound);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		this.breedingCooldown = compound.getInt("BreedingCooldown");
	}


	public static void tick(Level level, BlockPos pos, BlockState state, RatCageBreedingLanternBlockEntity te) {
		float f = ((float)RatConfig.ratBreedingCooldown - te.breedingCooldown) / (float)RatConfig.ratBreedingCooldown;
		float f1 = f * 0.6F + 0.4F;
		float f2 = Math.max(0.0F, f * f * 0.7F - 0.5F);
		float f3 = Math.max(0.0F, f * f * 0.6F - 0.7F);
		float i = pos.getX() + 0.5F;
		float j = pos.getY() + 0.5F;
		float k = pos.getZ() + 0.5F;
		if (te.breedingCooldown <= 0) {
			double d0 = 2.0F;
			List<TamedRat> rats = level.getEntitiesOfClass(TamedRat.class, new AABB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0), rat -> !rat.isBaby() && (rat.isInCage() || rat.isInTube()));
			if (rats.size() < RatConfig.ratCageCramming && rats.size() > 0) {
				List<TamedRat> males = new ArrayList<>();
				List<TamedRat> females = new ArrayList<>();
				for (TamedRat rat : rats) {
					if (rat.breedCooldown == 0) {
						if (rat.isMale()) {
							males.add(rat);
						} else {
							females.add(rat);
						}
					}
				}
				if (males.size() > 0 && females.size() > 0) {
					TamedRat male = males.get(0);
					TamedRat female = females.get(0);
					if (males.size() > 1) {
						male = males.get(level.getRandom().nextInt(males.size() - 1));
					}
					if (females.size() > 1) {
						female = females.get(level.getRandom().nextInt(females.size() - 1));
					}
					male.getLevel().broadcastEntityEvent(male, (byte) 83);
					female.getLevel().broadcastEntityEvent(female, (byte) 83);
					for (int baby = 0; baby < female.getRandom().nextInt(RatConfig.maxRatLitterSize) + 1; baby++) {
						female.createBabiesFrom(female, male);
					}
					te.breedingCooldown = RatConfig.ratBreedingCooldown;
					male.breedCooldown = RatConfig.ratBreedingCooldown;
					female.breedCooldown = RatConfig.ratBreedingCooldown;
				}
			}
			if (level.isClientSide()) {
				level.addParticle(DustParticleOptions.REDSTONE, i + level.getRandom().nextDouble() - 0.5D, j + level.getRandom().nextDouble() - 0.5D, k + level.getRandom().nextDouble() - 0.5D, f1, f2, f3);
			}
		} else {
			te.breedingCooldown--;
		}
	}
}
