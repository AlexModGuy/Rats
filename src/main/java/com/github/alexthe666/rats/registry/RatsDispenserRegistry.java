package com.github.alexthe666.rats.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import com.github.alexthe666.rats.server.block.RatTrapBlock;
import com.github.alexthe666.rats.server.block.RatlanteanAutomatonHeadBlock;
import com.github.alexthe666.rats.server.block.entity.RatTrapBlockEntity;
import com.github.alexthe666.rats.server.entity.misc.PlagueDoctor;
import com.github.alexthe666.rats.server.entity.monster.boss.BlackDeath;
import com.github.alexthe666.rats.server.entity.projectile.CheeseCannonball;
import com.github.alexthe666.rats.server.entity.projectile.PurifyingLiquid;
import com.github.alexthe666.rats.server.entity.projectile.RatCaptureNet;
import com.github.alexthe666.rats.server.entity.projectile.VialOfSentience;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
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
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

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
				Level level = source.level();
				Direction direction = source.state().getValue(DispenserBlock.FACING);
				BlockPos blockpos = source.pos().relative(direction);
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
				BlockPos blockpos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));

				for (TamedRat rat : source.level().getEntitiesOfClass(TamedRat.class, new AABB(blockpos), LivingEntity::isAlive)) {
					if (rat.applySpecialDyeIfPossible(stack)) {
						return stack;
					}
				}
				return super.execute(source, stack);
			}

			@Override
			protected void playSound(BlockSource source) {
				if (this.isSuccess()) {
					source.level().playSound(null, source.pos(), RatsSoundRegistry.ESSENCE_APPLIED.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
				} else {
					super.playSound(source);
				}
			}
		});

		// 1.21: Registry.getValues removed; use stream().
		for (Item item : BuiltInRegistries.ITEM.stream().filter(item -> item instanceof DyeItem).toList()) {
			DispenserBlock.registerBehavior(item, new OptionalDispenseItemBehavior() {
				@Override
				protected ItemStack execute(BlockSource source, ItemStack stack) {
					BlockPos blockpos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));

					for (TamedRat rat : source.level().getEntitiesOfClass(TamedRat.class, new AABB(blockpos), LivingEntity::isAlive)) {
						if (rat.applyNormalDyeIfPossible(stack)) {
							return stack;
						}
					}
					return super.execute(source, stack);
				}

				@Override
				protected void playSound(BlockSource source) {
					if (this.isSuccess()) {
						source.level().playSound(null, source.pos(), RatsSoundRegistry.ESSENCE_APPLIED.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
					} else {
						super.playSound(source);
					}
				}
			});
		}

		// 1.21.1 dropped/relocated AbstractProjectileDispenseBehavior. The original 1.20.1 build
		// dispensed CHEESE_CANNONBALL / RAT_CAPTURE_NET / PURIFYING_LIQUID / CRIMSON_FLUID /
		// VIAL_OF_SENTIENCE as projectiles via that helper. They are still functional as throwable
		// items in the player's hand; only the dispenser-block convenience is missing. Reauthor by
		// extending DefaultDispenseItemBehavior + manually spawning the projectile entity if/when
		// dispenser support becomes a requested feature.

		DispenserBlock.registerBehavior(RatsItemRegistry.PLAGUE_DOCTORATE.get(), new OptionalDispenseItemBehavior() {
			@Override
			protected ItemStack execute(BlockSource source, ItemStack stack) {
				BlockPos blockpos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));

				for (Villager villager : source.level().getEntitiesOfClass(Villager.class, new AABB(blockpos), villager -> villager.isAlive() && !villager.isBaby() && (villager.getVillagerData().getProfession() == VillagerProfession.NITWIT || villager.getVillagerData().getProfession() == VillagerProfession.NONE))) {
					PlagueDoctor doctor = new PlagueDoctor(RatsEntityRegistry.PLAGUE_DOCTOR.get(), source.level());
					doctor.copyPosition(villager);
					villager.discard();
					doctor.setWillDespawn(false);
					EventHooks.finalizeMobSpawn(doctor, source.level(), source.level().getCurrentDifficultyAt(source.pos()), MobSpawnType.CONVERSION, null);
					source.level().addFreshEntity(doctor);
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
					source.level().playSound(null, source.pos(), RatsSoundRegistry.PLAGUE_DOCTOR_SUMMON.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
				} else {
					super.playSound(source);
				}
			}
		});

		DispenserBlock.registerBehavior(RatsItemRegistry.PLAGUE_TOME.get(), new OptionalDispenseItemBehavior() {
			@Override
			protected ItemStack execute(BlockSource source, ItemStack stack) {
				BlockPos blockpos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));

				for (PlagueDoctor doctor : source.level().getEntitiesOfClass(PlagueDoctor.class, new AABB(blockpos), doctor -> doctor.isAlive() && !doctor.isBaby() && !doctor.willDespawn())) {
					BlackDeath death = new BlackDeath(RatsEntityRegistry.BLACK_DEATH.get(), source.level());
					death.copyPosition(doctor);
					doctor.discard();
					EventHooks.finalizeMobSpawn(death, source.level(), source.level().getCurrentDifficultyAt(source.pos()), MobSpawnType.CONVERSION, null);
					source.level().addFreshEntity(death);
					death.setNoAi(doctor.isNoAi());
					if (doctor.hasCustomName()) {
						death.setCustomName(doctor.getCustomName());
					}
					for (ServerPlayer player : source.level().getEntitiesOfClass(ServerPlayer.class, new AABB(blockpos).inflate(16.0F))) {
						RatsAdvancementsRegistry.BLACK_DEATH_SUMMONED.get().trigger(player);
					}
				}
				return super.execute(source, stack);
			}

			@Override
			protected void playSound(BlockSource source) {
				if (this.isSuccess()) {
					source.level().playSound(null, source.pos(), RatsSoundRegistry.BLACK_DEATH_SUMMON.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
				} else {
					super.playSound(source);
				}
			}
		});

		DispenserBlock.registerBehavior(RatsItemRegistry.CHEESE.get(), new OptionalDispenseItemBehavior() {
			@Override
			protected ItemStack execute(BlockSource source, ItemStack stack) {
				Level level = source.level();
				Direction direction = source.state().getValue(DispenserBlock.FACING);
				BlockPos blockpos = source.pos().relative(direction);
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
