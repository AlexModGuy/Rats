package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.server.block.RatTrapBlock;
import com.github.alexthe666.rats.server.block.RatlanteanAutomatonHeadBlock;
import com.github.alexthe666.rats.server.block.entity.RatTrapBlockEntity;
import com.github.alexthe666.rats.server.entity.BlackDeath;
import com.github.alexthe666.rats.server.entity.PurifyingLiquid;
import com.github.alexthe666.rats.server.entity.RatCaptureNet;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.entity.ratlantis.CheeseCannonball;
import com.github.alexthe666.rats.server.entity.ratlantis.VialOfSentience;
import com.github.alexthe666.rats.server.entity.villager.PlagueDoctor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;

public class RatsDispenserRegistry {

	//- rattling gun
	//- plague scythe? idea is it would shoot plague clouds, taking 1 dura per shot
	//- cage deco?
	public static void init() {
		DispenseItemBehavior hatDispense = new OptionalDispenseItemBehavior() {
			protected ItemStack execute(BlockSource source, ItemStack stack) {
				this.setSuccess(ArmorItem.dispenseArmor(source, stack));
				return stack;
			}
		};

		DispenserBlock.registerBehavior(RatsItemRegistry.ARCHEOLOGIST_HAT.get(), hatDispense);
		DispenserBlock.registerBehavior(RatsItemRegistry.FARMER_HAT.get(), hatDispense);
		DispenserBlock.registerBehavior(RatsItemRegistry.FISHERMAN_HAT.get(), hatDispense);
		DispenserBlock.registerBehavior(RatsItemRegistry.RAT_FEZ.get(), hatDispense);
		DispenserBlock.registerBehavior(RatsItemRegistry.TOP_HAT.get(), hatDispense);
		DispenserBlock.registerBehavior(RatsItemRegistry.SANTA_HAT.get(), hatDispense);
		DispenserBlock.registerBehavior(RatsItemRegistry.HALO_HAT.get(), hatDispense);
		DispenserBlock.registerBehavior(RatsItemRegistry.PARTY_HAT.get(), hatDispense);
		DispenserBlock.registerBehavior(RatsItemRegistry.PIRAT_HAT.get(), hatDispense);
		DispenserBlock.registerBehavior(RatsItemRegistry.RAT_KING_CROWN.get(), hatDispense);
		DispenserBlock.registerBehavior(RatsItemRegistry.PLAGUE_DOCTOR_MASK.get(), hatDispense);
		DispenserBlock.registerBehavior(RatsItemRegistry.BLACK_DEATH_MASK.get(), hatDispense);
		DispenserBlock.registerBehavior(RatsItemRegistry.EXTERMINATOR_HAT.get(), hatDispense);

		DispenserBlock.registerBehavior(RatlantisItemRegistry.AVIATOR_HAT.get(), hatDispense);
		DispenserBlock.registerBehavior(RatlantisItemRegistry.GHOST_PIRAT_HAT.get(), hatDispense);
		DispenserBlock.registerBehavior(RatlantisItemRegistry.MILITARY_HAT.get(), hatDispense);

		DispenserBlock.registerBehavior(RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD.get(), new OptionalDispenseItemBehavior() {
			protected ItemStack execute(BlockSource source, ItemStack stack) {
				Level level = source.getLevel();
				Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
				BlockPos blockpos = source.getPos().relative(direction);
				if (level.isEmptyBlock(blockpos) && RatlanteanAutomatonHeadBlock.canSpawnGolem(level, blockpos)) {
					level.setBlock(blockpos, RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD.get().defaultBlockState().setValue(RatlanteanAutomatonHeadBlock.FACING, direction.getAxis() == Direction.Axis.Y ? Direction.NORTH : direction.getOpposite()), 3);
					level.gameEvent(null, GameEvent.BLOCK_PLACE, blockpos);
					RatlanteanAutomatonHeadBlock.trySpawnGolem(level, blockpos);

					stack.shrink(1);
					this.setSuccess(true);
				} else {
					this.setSuccess(ArmorItem.dispenseArmor(source, stack));
				}

				return stack;
			}
		});

		DispenserBlock.registerBehavior(RatsItemRegistry.RATBOW_ESSENCE.get(), new OptionalDispenseItemBehavior() {
			@Override
			protected ItemStack execute(BlockSource source, ItemStack stack) {
				BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));

				for (TamedRat rat : source.getLevel().getEntitiesOfClass(TamedRat.class, new AABB(blockpos), LivingEntity::isAlive)) {
					if (rat.applySpecialDyeIfPossible(stack)) {
						return stack;
					}
				}
				return super.execute(source, stack);
			}

			@Override
			protected void playSound(BlockSource source) {
				if (this.isSuccess()) {
					source.getLevel().playSound(null, source.getPos(), RatsSoundRegistry.ESSENCE_APPLIED.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
				} else {
					super.playSound(source);
				}
			}
		});

		for (Item item : ForgeRegistries.ITEMS.getValues().stream().filter(item -> item instanceof DyeItem).toList()) {
			DispenserBlock.registerBehavior(item, new OptionalDispenseItemBehavior() {
				@Override
				protected ItemStack execute(BlockSource source, ItemStack stack) {
					BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));

					for (TamedRat rat : source.getLevel().getEntitiesOfClass(TamedRat.class, new AABB(blockpos), LivingEntity::isAlive)) {
						if (rat.applyNormalDyeIfPossible(stack)) {
							return stack;
						}
					}
					return super.execute(source, stack);
				}

				@Override
				protected void playSound(BlockSource source) {
					if (this.isSuccess()) {
						source.getLevel().playSound(null, source.getPos(), RatsSoundRegistry.ESSENCE_APPLIED.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
					} else {
						super.playSound(source);
					}
				}
			});
		}

		DispenserBlock.registerBehavior(RatlantisItemRegistry.CHEESE_CANNONBALL.get(), new AbstractProjectileDispenseBehavior() {
			@Override
			protected Projectile getProjectile(Level level, Position position, ItemStack stack) {
				return new CheeseCannonball(RatlantisEntityRegistry.CHEESE_CANNONBALL.get(), level);
			}
		});

		DispenserBlock.registerBehavior(RatsItemRegistry.RAT_CAPTURE_NET.get(), new AbstractProjectileDispenseBehavior() {
			@Override
			protected Projectile getProjectile(Level level, Position position, ItemStack stack) {
				return new RatCaptureNet(RatsEntityRegistry.RAT_CAPTURE_NET.get(), level);
			}
		});

		DispenserBlock.registerBehavior(RatsItemRegistry.PURIFYING_LIQUID.get(), new AbstractProjectileDispenseBehavior() {
			@Override
			protected Projectile getProjectile(Level level, Position position, ItemStack stack) {
				return new PurifyingLiquid(level, position.x(), position.y(), position.z(), false);
			}
		});

		DispenserBlock.registerBehavior(RatsItemRegistry.CRIMSON_FLUID.get(), new AbstractProjectileDispenseBehavior() {
			@Override
			protected Projectile getProjectile(Level level, Position position, ItemStack stack) {
				return new PurifyingLiquid(level, position.x(), position.y(), position.z(), true);
			}
		});

		DispenserBlock.registerBehavior(RatlantisItemRegistry.VIAL_OF_SENTIENCE.get(), new AbstractProjectileDispenseBehavior() {
			@Override
			protected Projectile getProjectile(Level level, Position position, ItemStack stack) {
				return new VialOfSentience(RatlantisEntityRegistry.VIAL_OF_SENTIENCE.get(), level);
			}
		});

		DispenserBlock.registerBehavior(RatsItemRegistry.PLAGUE_DOCTORATE.get(), new OptionalDispenseItemBehavior() {
			@Override
			protected ItemStack execute(BlockSource source, ItemStack stack) {
				BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));

				for (Villager villager : source.getLevel().getEntitiesOfClass(Villager.class, new AABB(blockpos), villager -> villager.isAlive() && !villager.isBaby() && (villager.getVillagerData().getProfession() == VillagerProfession.NITWIT || villager.getVillagerData().getProfession() == VillagerProfession.NONE))) {
					PlagueDoctor doctor = new PlagueDoctor(RatsEntityRegistry.PLAGUE_DOCTOR.get(), source.getLevel());
					doctor.copyPosition(villager);
					villager.discard();
					doctor.setWillDespawn(false);
					ForgeEventFactory.onFinalizeSpawn(doctor, source.getLevel(), source.getLevel().getCurrentDifficultyAt(source.getPos()), MobSpawnType.CONVERSION, null, null);
					source.getLevel().addFreshEntity(doctor);
					doctor.setNoAi(villager.isNoAi());
					if (villager.hasCustomName()) {
						doctor.setCustomName(villager.getCustomName());
					}
					stack.shrink(1);
				}
				return super.execute(source, stack);
			}

			@Override
			protected void playSound(BlockSource source) {
				if (this.isSuccess()) {
					source.getLevel().playSound(null, source.getPos(), RatsSoundRegistry.PLAGUE_DOCTOR_SUMMON.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
				} else {
					super.playSound(source);
				}
			}
		});

		DispenserBlock.registerBehavior(RatsItemRegistry.PLAGUE_TOME.get(), new OptionalDispenseItemBehavior() {
			@Override
			protected ItemStack execute(BlockSource source, ItemStack stack) {
				BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));

				for (PlagueDoctor doctor : source.getLevel().getEntitiesOfClass(PlagueDoctor.class, new AABB(blockpos), doctor -> doctor.isAlive() && !doctor.isBaby() && !doctor.willDespawn())) {
					BlackDeath death = new BlackDeath(RatsEntityRegistry.BLACK_DEATH.get(), source.getLevel());
					death.copyPosition(doctor);
					doctor.discard();
					ForgeEventFactory.onFinalizeSpawn(death, source.getLevel(), source.getLevel().getCurrentDifficultyAt(source.getPos()), MobSpawnType.CONVERSION, null, null);
					source.getLevel().addFreshEntity(death);
					death.setNoAi(doctor.isNoAi());
					if (doctor.hasCustomName()) {
						death.setCustomName(doctor.getCustomName());
					}
					for (ServerPlayer player : source.getLevel().getEntitiesOfClass(ServerPlayer.class, new AABB(blockpos).inflate(16.0F))) {
						RatsAdvancementsRegistry.BLACK_DEATH_SUMMONED.trigger(player);
					}
				}
				return super.execute(source, stack);
			}

			@Override
			protected void playSound(BlockSource source) {
				if (this.isSuccess()) {
					source.getLevel().playSound(null, source.getPos(), RatsSoundRegistry.BLACK_DEATH_SUMMON.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
				} else {
					super.playSound(source);
				}
			}
		});

		DispenserBlock.registerBehavior(RatsItemRegistry.CHEESE.get(), new OptionalDispenseItemBehavior() {
			@Override
			protected ItemStack execute(BlockSource source, ItemStack stack) {
				Level level = source.getLevel();
				Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
				BlockPos blockpos = source.getPos().relative(direction);
				if (level.getBlockState(blockpos).is(RatsBlockRegistry.RAT_TRAP.get())) {
					if (level.getBlockEntity(blockpos) instanceof RatTrapBlockEntity trap && trap.getBait().isEmpty() && !level.getBlockState(blockpos).getValue(RatTrapBlock.SHUT)) {
						trap.setBaitStack(stack);
						trap.setChanged();
						level.sendBlockUpdated(blockpos, level.getBlockState(blockpos), level.getBlockState(blockpos), 1);
						stack.shrink(1);
					}
				}

				return stack;
			}
		});
	}
}
