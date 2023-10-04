package com.github.alexthe666.rats.data;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.data.tags.RatsItemTags;
import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.registry.RatsEffectRegistry;
import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.advancements.BlackDeathSummonedTrigger;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.function.Consumer;

public class RatsAdvancementGenerator implements ForgeAdvancementProvider.AdvancementGenerator {

	@Override
	public void generate(HolderLookup.Provider registries, Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
		this.registerMainAdvancements(consumer);
	}

	private void registerMainAdvancements(Consumer<Advancement> consumer) {
		Advancement root = Advancement.Builder.advancement().display(
						RatsBlockRegistry.BLOCK_OF_CHEESE.get(),
						Component.translatable("advancement.rats.root.title"),
						Component.translatable("advancement.rats.root.desc"),
						new ResourceLocation(RatsMod.MODID, "textures/block/block_of_cheese.png"),
						FrameType.TASK,
						false, false, false)
				.addCriterion("tick", new PlayerTrigger.TriggerInstance(CriteriaTriggers.TICK.getId(), ContextAwarePredicate.ANY))
				.save(consumer, "rats:root");

		Advancement trash = Advancement.Builder.advancement().parent(root).display(
						RatsBlockRegistry.TRASH_CAN.get(),
						Component.translatable("advancement.rats.trash_can.title"),
						Component.translatable("advancement.rats.trash_can.desc"),
						null, FrameType.TASK, true, false, false)
				.addCriterion("trash_can", InventoryChangeTrigger.TriggerInstance.hasItems(RatsBlockRegistry.TRASH_CAN.get()))
				.save(consumer, "rats:trash_can");

		Advancement garbage = Advancement.Builder.advancement().parent(trash).display(
						RatsBlockRegistry.GARBAGE_PILE.get(),
						Component.translatable("advancement.rats.garbage_pile.title"),
						Component.translatable("advancement.rats.garbage_pile.desc"),
						null, FrameType.TASK, true, false, false)
				.addCriterion("garbage_pile", InventoryChangeTrigger.TriggerInstance.hasItems(RatsBlockRegistry.GARBAGE_PILE.get()))
				.save(consumer, "rats:garbage_pile");

		Advancement ball = Advancement.Builder.advancement().parent(garbage).display(
						RatsItemRegistry.FILTH_CORRUPTION.get(),
						Component.translatable("advancement.rats.ball_of_filth.title"),
						Component.translatable("advancement.rats.ball_of_filth.desc"),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("filth", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.FILTH_CORRUPTION.get()))
				.save(consumer, "rats:ball_of_filth");

		Advancement.Builder.advancement().parent(ball).display(
						RatsItemRegistry.TANGLED_RAT_TAILS.get(),
						Component.translatable("advancement.rats.rat_king.title"),
						Component.translatable("advancement.rats.rat_king.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("kill_king", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(RatsEntityRegistry.RAT_KING.get())))
				.save(consumer, "rats:defeat_rat_king");

		Advancement piper = Advancement.Builder.advancement().parent(root).display(
						RatsItemRegistry.PIPER_HAT.get(),
						Component.translatable("advancement.rats.piper.title"),
						Component.translatable("advancement.rats.piper.desc"),
						null, FrameType.TASK, true, false, false)
				.requirements(RequirementsStrategy.OR)
				.addCriterion("piper_hat", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.PIPER_HAT.get()))
				.addCriterion("hurt_piper", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntity(EntityPredicate.Builder.entity().of(RatsEntityRegistry.PIED_PIPER.get()).build()))
				.addCriterion("killed_piper", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(RatsEntityRegistry.PIED_PIPER.get()).build()))
				.save(consumer, "rats:piper");

		Advancement.Builder.advancement().parent(piper).display(
						RatsItemRegistry.MUSIC_DISC_MICE_ON_VENUS.get(),
						Component.translatable("advancement.rats.rat_music_disc.title"),
						Component.translatable("advancement.rats.rat_music_disc.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.requirements(RequirementsStrategy.OR)
				.addCriterion("mice_on_venus", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.MUSIC_DISC_MICE_ON_VENUS.get()))
				.addCriterion("living_mice", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.MUSIC_DISC_LIVING_MICE.get()))
				.save(consumer, "rats:rat_music_disc");

		Advancement plague = Advancement.Builder.advancement().parent(root).display(
						RatsItemRegistry.PLAGUE_ESSENCE.get(),
						Component.translatable("advancement.rats.plague.title"),
						Component.translatable("advancement.rats.plague.desc"),
						null, FrameType.TASK, true, false, false)
				.addCriterion("catch_plague", EffectsChangedTrigger.TriggerInstance.hasEffects(MobEffectsPredicate.effects().and(RatsEffectRegistry.PLAGUE.get())))
				.save(consumer, "rats:plague");

		Advancement plague_doctor = Advancement.Builder.advancement().parent(plague).display(
						RatsItemRegistry.PLAGUE_DOCTOR_MASK.get(),
						Component.translatable("advancement.rats.plague_doctor.title"),
						Component.translatable("advancement.rats.plague_doctor.desc"),
						null, FrameType.TASK, true, false, false)
				.addCriterion("meet_doctor", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item(), EntityPredicate.wrap(EntityPredicate.Builder.entity().of(RatsEntityRegistry.PLAGUE_DOCTOR.get()).build())))
				.save(consumer, "rats:plague_doctor");

		Advancement black_death = Advancement.Builder.advancement().parent(plague_doctor).display(
						RatsItemRegistry.BLACK_DEATH_MASK.get(),
						Component.translatable("advancement.rats.black_death.title"),
						Component.translatable("advancement.rats.black_death.desc"),
						null, FrameType.GOAL, true, true, false)
				.requirements(RequirementsStrategy.OR)
				.addCriterion("summoned", BlackDeathSummonedTrigger.TriggerInstance.summoned())
				.addCriterion("hurt_death", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntity(EntityPredicate.Builder.entity().of(RatsEntityRegistry.BLACK_DEATH.get()).build()))
				.addCriterion("killed_death", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(RatsEntityRegistry.BLACK_DEATH.get()).build()))
				.save(consumer, "rats:black_death");

		Advancement.Builder.advancement().parent(black_death).display(
						RatsItemRegistry.PLAGUE_SCYTHE.get(),
						Component.translatable("advancement.rats.defeat_black_death.title"),
						Component.translatable("advancement.rats.defeat_black_death.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("killed_death", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(RatsEntityRegistry.BLACK_DEATH.get()).build()))
				.save(consumer, "rats:defeat_black_death");

		Advancement.Builder.advancement().parent(plague_doctor).display(
						RatsItemRegistry.PLAGUE_LEECH.get(),
						Component.translatable("advancement.rats.plague_cure.title"),
						Component.translatable("advancement.rats.plague_cure.desc"),
						null, FrameType.TASK, true, false, false)
				.requirements(RequirementsStrategy.OR)
				.addCriterion("discover_herbs", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.HERB_BUNDLE.get()))
				.addCriterion("discover_treacle", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.TREACLE.get()))
				.addCriterion("discover_leech", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.PLAGUE_LEECH.get()))
				.addCriterion("discover_stew", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.PLAGUE_STEW.get()))
				.addCriterion("discover_liquid", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.PURIFYING_LIQUID.get()))
				.save(consumer, "rats:plague_cure");

		Advancement.Builder.advancement().parent(root).display(
						RatsBlockRegistry.RAT_TRAP.get(),
						Component.translatable("advancement.rats.rat_trap.title"),
						Component.translatable("advancement.rats.rat_trap.desc"),
						null, FrameType.TASK, true, false, false)
				.addCriterion("make_trap", InventoryChangeTrigger.TriggerInstance.hasItems(RatsBlockRegistry.RAT_TRAP.get()))
				.save(consumer, "rats:rat_trap");

		Advancement.Builder.advancement().parent(root).display(
						RatsItemRegistry.CONTAMINATED_FOOD.get(),
						Component.translatable("advancement.rats.contaminated_food.title"),
						Component.translatable("advancement.rats.contaminated_food.desc"),
						null, FrameType.TASK, true, false, false)
				.addCriterion("find_contaminated_food", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.CONTAMINATED_FOOD.get()))
				.save(consumer, "rats:contaminated_food");

		Advancement milk = Advancement.Builder.advancement().parent(root).display(
						RatsBlockRegistry.MILK_CAULDRON.get(),
						Component.translatable("advancement.rats.milk_cauldron.title"),
						Component.translatable("advancement.rats.milk_cauldron.desc"),
						null, FrameType.TASK, true, false, false)
				.addCriterion("curdle_milk", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(RatsBlockRegistry.MILK_CAULDRON.get()).build()), ItemPredicate.Builder.item().of(Items.MILK_BUCKET)))
				.save(consumer, "rats:milk_cauldron");

		Advancement cheese = Advancement.Builder.advancement().parent(milk).display(
						RatsItemRegistry.CHEESE.get(),
						Component.translatable("advancement.rats.cheese.title"),
						Component.translatable("advancement.rats.cheese.desc"),
						null, FrameType.TASK, true, false, false)
				.addCriterion("get_cheese", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.CHEESE.get()))
				.save(consumer, "rats:cheese");

		Advancement nether_cheese = Advancement.Builder.advancement().parent(cheese).display(
						RatsItemRegistry.NETHER_CHEESE.get(),
						Component.translatable("advancement.rats.nether_cheese.title"),
						Component.translatable("advancement.rats.nether_cheese.desc"),
						null, FrameType.TASK, true, false, false)
				.addCriterion("get_nether_cheese", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.NETHER_CHEESE.get()))
				.save(consumer, "rats:nether_cheese");

		Advancement.Builder.advancement().parent(nether_cheese).display(
						RatsItemRegistry.RAT_UPGRADE_DEMON.get(),
						Component.translatable("advancement.rats.rat_upgrade_demon.title"),
						Component.translatable("advancement.rats.rat_upgrade_demon.desc"),
						null, FrameType.TASK, true, false, false)
				.addCriterion("get_upgrade", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.RAT_UPGRADE_DEMON.get()))
				.save(consumer, "rats:rat_upgrade_demon");

		Advancement tame = Advancement.Builder.advancement().parent(cheese).display(
						RatsItemRegistry.RAT_UPGRADE_HEALTH.get(),
						Component.translatable("advancement.rats.tame_rat.title"),
						Component.translatable("advancement.rats.tame_rat.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("tame_rat", TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().of(RatsEntityRegistry.TAMED_RAT.get()).build()))
				.save(consumer, "rats:tame_rat");

		Advancement cage = Advancement.Builder.advancement().parent(tame).display(
						RatsBlockRegistry.RAT_CAGE.get(),
						Component.translatable("advancement.rats.rat_cage.title"),
						Component.translatable("advancement.rats.rat_cage.desc"),
						null, FrameType.TASK, true, false, false)
				.addCriterion("get_cage", InventoryChangeTrigger.TriggerInstance.hasItems(RatsBlockRegistry.RAT_CAGE.get()))
				.save(consumer, "rats:rat_cage");

		Advancement.Builder.advancement().parent(cage).display(
						RatsItemRegistry.RAT_WATER_BOTTLE.get(),
						Component.translatable("advancement.rats.rat_cage_decoration.title"),
						Component.translatable("advancement.rats.rat_cage_decoration.desc"),
						null, FrameType.TASK, true, false, false)
				.requirements(RequirementsStrategy.OR)
				.addCriterion("decorate_with_bottle", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(RatsBlockRegistry.RAT_CAGE_DECORATED.get()).build()), ItemPredicate.Builder.item().of(RatsItemRegistry.RAT_WATER_BOTTLE.get())))
				.addCriterion("decorate_with_bowl", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(RatsBlockRegistry.RAT_CAGE_DECORATED.get()).build()), ItemPredicate.Builder.item().of(RatsItemRegistry.RAT_SEED_BOWL.get())))
				.addCriterion("decorate_with_igloo", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(RatsBlockRegistry.RAT_CAGE_DECORATED.get()).build()), ItemPredicate.Builder.item().of(RatsItemTags.IGLOOS)))
				.addCriterion("decorate_with_hammock", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(RatsBlockRegistry.RAT_CAGE_DECORATED.get()).build()), ItemPredicate.Builder.item().of(RatsItemTags.HAMMOCKS)))
				.addCriterion("decorate_with_lantern", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(RatsBlockRegistry.RAT_CAGE_BREEDING_LANTERN.get()).build()), ItemPredicate.Builder.item().of(RatsItemRegistry.RAT_BREEDING_LANTERN.get())))
				.addCriterion("decorate_with_wheel", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(RatsBlockRegistry.RAT_CAGE_WHEEL.get()).build()), ItemPredicate.Builder.item().of(RatsItemRegistry.RAT_WHEEL.get())))
				.save(consumer, "rats:rat_cage_decoration");

		Advancement staff = Advancement.Builder.advancement().parent(tame).display(
						RatsItemRegistry.CHEESE_STICK.get(),
						Component.translatable("advancement.rats.cheese_stick.title"),
						Component.translatable("advancement.rats.cheese_stick.desc"),
						null, FrameType.TASK, true, false, false)
				.addCriterion("get_stick", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.CHEESE_STICK.get()))
				.save(consumer, "rats:cheese_stick");

		Advancement.Builder.advancement().parent(staff).display(
						RatsItemRegistry.RAT_UPGRADE_PLATTER.get(),
						Component.translatable("advancement.rats.rat_upgrade_platter.title"),
						Component.translatable("advancement.rats.rat_upgrade_platter.desc"),
						null, FrameType.TASK, true, false, false)
				.addCriterion("get_upgrade", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.RAT_UPGRADE_PLATTER.get()))
				.save(consumer, "rats:rat_upgrade_platter");

		Advancement.Builder.advancement().parent(tame).display(
						RatsItemRegistry.RATBOW_ESSENCE.get(),
						Component.translatable("advancement.rats.ratbow_essence.title"),
						Component.translatable("advancement.rats.ratbow_essence.desc"),
						null, FrameType.TASK, true, false, false)
				.addCriterion("use_essence", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of(RatsItemRegistry.RATBOW_ESSENCE.get()), EntityPredicate.wrap(EntityPredicate.Builder.entity().of(RatsEntityRegistry.TAMED_RAT.get()).build())))
				.save(consumer, "rats:ratbow_essence");

		Advancement basic = Advancement.Builder.advancement().parent(tame).display(
						RatsItemRegistry.RAT_UPGRADE_BASIC.get(),
						Component.translatable("advancement.rats.rat_upgrade_basic.title"),
						Component.translatable("advancement.rats.rat_upgrade_basic.desc"),
						null, FrameType.TASK, true, false, false)
				.addCriterion("get_upgrade", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.save(consumer, "rats:rat_upgrade_basic");

		Advancement strength = Advancement.Builder.advancement().parent(basic).display(
						RatsItemRegistry.RAT_UPGRADE_STRENGTH.get(),
						Component.translatable("advancement.rats.rat_upgrade_strength.title"),
						Component.translatable("advancement.rats.rat_upgrade_strength.desc"),
						null, FrameType.TASK, true, false, false)
				.addCriterion("get_upgrade", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.RAT_UPGRADE_STRENGTH.get()))
				.save(consumer, "rats:rat_upgrade_strength");

		Advancement warrior = Advancement.Builder.advancement().parent(strength).display(
						RatsItemRegistry.RAT_UPGRADE_WARRIOR.get(),
						Component.translatable("advancement.rats.rat_upgrade_warrior.title"),
						Component.translatable("advancement.rats.rat_upgrade_warrior.desc"),
						null, FrameType.GOAL, true, false, false)
				.addCriterion("get_upgrade", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.RAT_UPGRADE_WARRIOR.get()))
				.save(consumer, "rats:rat_upgrade_warrior");

		Advancement.Builder.advancement().parent(warrior).display(
						RatsItemRegistry.RAT_UPGRADE_GOD.get(),
						Component.translatable("advancement.rats.rat_upgrade_god.title"),
						Component.translatable("advancement.rats.rat_upgrade_god.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("get_upgrade", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.RAT_UPGRADE_GOD.get()))
				.save(consumer, "rats:rat_upgrade_god");

		Advancement.Builder.advancement().parent(basic).display(
						RatsItemRegistry.RAT_UPGRADE_FLIGHT.get(),
						Component.translatable("advancement.rats.rat_upgrade_flight.title"),
						Component.translatable("advancement.rats.rat_upgrade_flight.desc"),
						null, FrameType.GOAL, true, false, false)
				.addCriterion("get_upgrade", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.RAT_UPGRADE_FLIGHT.get()))
				.save(consumer, "rats:rat_upgrade_flight");

		Advancement crafting = Advancement.Builder.advancement().parent(basic).display(
						RatsItemRegistry.RAT_UPGRADE_CRAFTING.get(),
						Component.translatable("advancement.rats.rat_upgrade_crafting.title"),
						Component.translatable("advancement.rats.rat_upgrade_crafting.desc"),
						null, FrameType.GOAL, true, false, false)
				.addCriterion("get_upgrade", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.RAT_UPGRADE_CRAFTING.get()))
				.save(consumer, "rats:rat_upgrade_crafting");

		Advancement.Builder.advancement().parent(crafting).display(
						RatsBlockRegistry.RAT_CRAFTING_TABLE.get(),
						Component.translatable("advancement.rats.rat_crafting_table.title"),
						Component.translatable("advancement.rats.rat_crafting_table.desc"),
						null, FrameType.GOAL, true, false, false)
				.addCriterion("get_table", InventoryChangeTrigger.TriggerInstance.hasItems(RatsBlockRegistry.RAT_CRAFTING_TABLE.get()))
				.save(consumer, "rats:rat_crafting_table");

		Advancement chef = Advancement.Builder.advancement().parent(basic).display(
						RatsItemRegistry.RAT_UPGRADE_CHEF.get(),
						Component.translatable("advancement.rats.rat_upgrade_chef.title"),
						Component.translatable("advancement.rats.rat_upgrade_chef.desc"),
						null, FrameType.GOAL, true, false, false)
				.addCriterion("get_upgrade", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.RAT_UPGRADE_CHEF.get()))
				.save(consumer, "rats:rat_upgrade_chef");

		Advancement.Builder.advancement().parent(chef).display(
						RatsItemRegistry.CHEF_TOQUE.get(),
						Component.translatable("advancement.rats.rat_cooking.title"),
						Component.translatable("advancement.rats.rat_cooking.desc"),
						null, FrameType.CHALLENGE, true, true, true)
				.requirements(RequirementsStrategy.OR)
				.addCriterion("eat_byaldi", ConsumeItemTrigger.TriggerInstance.usedItem(RatsItemRegistry.CONFIT_BYALDI.get()))
				.addCriterion("eat_knishes", ConsumeItemTrigger.TriggerInstance.usedItem(RatsItemRegistry.POTATO_KNISHES.get()))
				.save(consumer, "rats:rat_cooking");

		Advancement.Builder.advancement().parent(basic).display(
						RatsItemRegistry.RAT_UPGRADE_SCULKED.get(),
						Component.translatable("advancement.rats.rat_upgrade_sculked.title"),
						Component.translatable("advancement.rats.rat_upgrade_sculked.desc"),
						null, FrameType.GOAL, true, false, false)
				.addCriterion("get_upgrade", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.RAT_UPGRADE_SCULKED.get()))
				.save(consumer, "rats:rat_upgrade_sculked");

		Advancement.Builder.advancement().parent(basic).display(
						RatsItemRegistry.RAT_UPGRADE_IDOL.get(),
						Component.translatable("advancement.rats.rat_upgrade_idol.title"),
						Component.translatable("advancement.rats.rat_upgrade_idol.desc"),
						null, FrameType.GOAL, true, false, false)
				.addCriterion("get_upgrade", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.RAT_UPGRADE_IDOL.get()))
				.save(consumer, "rats:rat_upgrade_idol");

		Advancement.Builder.advancement().parent(basic).display(
						RatsItemRegistry.RAT_UPGRADE_PICKPOCKET.get(),
						Component.translatable("advancement.rats.rat_upgrade_pickpocket.title"),
						Component.translatable("advancement.rats.rat_upgrade_pickpocket.desc"),
						null, FrameType.GOAL, true, false, false)
				.addCriterion("get_upgrade", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.RAT_UPGRADE_PICKPOCKET.get()))
				.save(consumer, "rats:rat_upgrade_pickpocket");

		Advancement.Builder.advancement().parent(root).display(
						RatsItemRegistry.PARTY_HAT.get(),
						Component.translatable("advancement.rats.all_hats.title"),
						Component.translatable("advancement.rats.all_hats.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("has_chef_toque", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.CHEF_TOQUE.get()))
				.addCriterion("has_piper_hat", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.PIPER_HAT.get()))
				.addCriterion("has_archeologist_hat", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.ARCHEOLOGIST_HAT.get()))
				.addCriterion("has_farmer_hat", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.FARMER_HAT.get()))
				.addCriterion("has_fisherman_hat", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.FISHERMAN_HAT.get()))
				.addCriterion("has_fez", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.RAT_FEZ.get()))
				.addCriterion("has_top_hat", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.TOP_HAT.get()))
				.addCriterion("has_santa_hat", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.SANTA_HAT.get()))
				.addCriterion("has_halo", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.HALO_HAT.get()))
				.addCriterion("has_party_hat", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.PARTY_HAT.get()))
				.addCriterion("has_crown", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.RAT_KING_CROWN.get()))
				.addCriterion("has_plague_doctor_mask", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.PLAGUE_DOCTOR_MASK.get()))
				.addCriterion("has_black_death_mask", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.BLACK_DEATH_MASK.get()))
				.addCriterion("has_exterminator_hat", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.EXTERMINATOR_HAT.get()))
				.save(consumer, "rats:all_hats");
	}
}
