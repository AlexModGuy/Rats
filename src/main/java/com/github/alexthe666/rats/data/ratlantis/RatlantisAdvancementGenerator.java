package com.github.alexthe666.rats.data.ratlantis;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.*;
import com.google.common.collect.Maps;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.function.Consumer;

public class RatlantisAdvancementGenerator implements ForgeAdvancementProvider.AdvancementGenerator {
	@Override
	public void generate(HolderLookup.Provider provider, Consumer<Advancement> consumer, ExistingFileHelper helper) {
		this.registerRatlantisAdvancements(consumer);
	}

	private void registerRatlantisAdvancements(Consumer<Advancement> consumer) {
		Advancement.Builder.advancement().parent(Advancement.Builder.advancement().build(new ResourceLocation("rats:rat_upgrade_god"))).display(
						RatlantisItemRegistry.RAT_UPGRADE_NONBELIEVER.get(),
						Component.translatable("advancement.ratlantis.rat_upgrade_nonbeliever.title"),
						Component.translatable("advancement.ratlantis.rat_upgrade_nonbeliever.desc"),
						null, FrameType.CHALLENGE, true, true, true)
				.addCriterion("get_upgrade", InventoryChangeTrigger.TriggerInstance.hasItems(RatlantisItemRegistry.RAT_UPGRADE_NONBELIEVER.get()))
				.save(consumer, "ratlantis:rat_upgrade_nonbeliever");

		Advancement root = Advancement.Builder.advancement().display(
						RatsBlockRegistry.MARBLED_CHEESE_RAW.get(),
						Component.translatable("advancement.ratlantis.root.title"),
						Component.translatable("advancement.ratlantis.root.desc"),
						new ResourceLocation(RatsMod.MODID, "textures/block/marbled_cheese.png"),
						FrameType.TASK, false, false, false)
				.addCriterion("tick", new PlayerTrigger.TriggerInstance(CriteriaTriggers.TICK.getId(), ContextAwarePredicate.ANY))
				.save(consumer, "ratlantis:root");

		Advancement token = Advancement.Builder.advancement().parent(root).display(
						RatlantisBlockRegistry.CHUNKY_CHEESE_TOKEN.get(),
						Component.translatable("advancement.ratlantis.token.title"),
						Component.translatable("advancement.ratlantis.token.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("get_token", InventoryChangeTrigger.TriggerInstance.hasItems(RatlantisBlockRegistry.CHUNKY_CHEESE_TOKEN.get()))
				.save(consumer, "ratlantis:token");

		Advancement.Builder.advancement().parent(token).display(
						RatlantisItemRegistry.RAT_UPGRADE_ARCHEOLOGIST.get(),
						Component.translatable("advancement.ratlantis.rat_upgrade_archeologist.title"),
						Component.translatable("advancement.ratlantis.rat_upgrade_archeologist.desc"),
						null, FrameType.TASK, true, false, false)
				.addCriterion("get_upgrade", InventoryChangeTrigger.TriggerInstance.hasItems(RatlantisItemRegistry.RAT_UPGRADE_ARCHEOLOGIST.get()))
				.save(consumer, "ratlantis:rat_upgrade_archeologist");

		Advancement portal = Advancement.Builder.advancement().parent(token).display(
						RatlantisBlockRegistry.RATLANTIS_PORTAL.get(),
						Component.translatable("advancement.ratlantis.ratlantis.title"),
						Component.translatable("advancement.ratlantis.ratlantis.desc"),
						null, FrameType.GOAL, true, false, false)
				.addCriterion("visit_ratlantis", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(RatsMod.MODID, "ratlantis"))))
				.save(consumer, "ratlantis:ratlantis");

		Advancement pirat = Advancement.Builder.advancement().parent(portal).display(
						RatsItemRegistry.PIRAT_HAT.get(),
						Component.translatable("advancement.ratlantis.pirat.title"),
						Component.translatable("advancement.ratlantis.pirat.desc"),
						null, FrameType.TASK, true, false, false)
				.requirements(RequirementsStrategy.OR)
				.addCriterion("hurt_pirat", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntity(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.PIRAT.get()).build()))
				.addCriterion("killed_pirat", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.PIRAT.get()).build()))
				.addCriterion("hurt_by_pirat", EntityHurtPlayerTrigger.TriggerInstance.entityHurtPlayer(DamagePredicate.Builder.damageInstance().sourceEntity(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.PIRAT.get()).build())))
				.addCriterion("killed_by_pirat", KilledTrigger.TriggerInstance.entityKilledPlayer(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.PIRAT.get()).build()))
				.save(consumer, "ratlantis:pirat");

		Advancement ghost_pirat = Advancement.Builder.advancement().parent(pirat).display(
						RatlantisItemRegistry.GHOST_PIRAT_HAT.get(),
						Component.translatable("advancement.ratlantis.ghost_pirat.title"),
						Component.translatable("advancement.ratlantis.ghost_pirat.desc"),
						null, FrameType.GOAL, true, false, false)
				.requirements(RequirementsStrategy.OR)
				.addCriterion("hurt_ghost_pirat", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntity(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.GHOST_PIRAT.get()).build()))
				.addCriterion("killed_ghost_pirat", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.GHOST_PIRAT.get()).build()))
				.addCriterion("hurt_by_ghost_pirat", EntityHurtPlayerTrigger.TriggerInstance.entityHurtPlayer(DamagePredicate.Builder.damageInstance().sourceEntity(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.GHOST_PIRAT.get()).build())))
				.addCriterion("killed_by_ghost_pirat", KilledTrigger.TriggerInstance.entityKilledPlayer(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.GHOST_PIRAT.get()).build()))
				.save(consumer, "ratlantis:ghost_pirat");

		Advancement.Builder.advancement().parent(ghost_pirat).display(
						RatlantisItemRegistry.DUTCHRAT_WHEEL.get(),
						Component.translatable("advancement.ratlantis.dutchrat_wheel.title"),
						Component.translatable("advancement.ratlantis.dutchrat_wheel.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("kill_dutchrat", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.DUTCHRAT.get())))
				.save(consumer, "ratlantis:defeat_dutchrat");

		Advancement.Builder.advancement().parent(pirat).display(
						RatlantisItemRegistry.RAT_UPGRADE_BUCCANEER.get(),
						Component.translatable("advancement.ratlantis.rat_upgrade_buccaneer.title"),
						Component.translatable("advancement.ratlantis.rat_upgrade_buccaneer.desc"),
						null, FrameType.GOAL, true, false, false)
				.addCriterion("get_upgrade", InventoryChangeTrigger.TriggerInstance.hasItems(RatlantisItemRegistry.RAT_UPGRADE_BUCCANEER.get()))
				.save(consumer, "ratlantis:rat_upgrade_buccaneer");

		Advancement petals = Advancement.Builder.advancement().parent(portal).display(
						RatlantisItemRegistry.RATGLOVE_PETALS.get(),
						Component.translatable("advancement.ratlantis.ratglove_petals.title"),
						Component.translatable("advancement.ratlantis.ratglove_petals.desc"),
						null, FrameType.TASK, true, false, false)
				.addCriterion("get_petals", InventoryChangeTrigger.TriggerInstance.hasItems(RatlantisItemRegistry.RATGLOVE_PETALS.get()))
				.save(consumer, "ratlantis:ratglove_petals");

		Advancement gem = Advancement.Builder.advancement().parent(petals).display(
						RatlantisItemRegistry.GEM_OF_RATLANTIS.get(),
						Component.translatable("advancement.ratlantis.gem_of_ratlantis.title"),
						Component.translatable("advancement.ratlantis.gem_of_ratlantis.desc"),
						null, FrameType.TASK, true, false, false)
				.addCriterion("get_gem", InventoryChangeTrigger.TriggerInstance.hasItems(RatlantisItemRegistry.GEM_OF_RATLANTIS.get()))
				.save(consumer, "ratlantis:gem_of_ratlantis");

		Advancement core = Advancement.Builder.advancement().parent(gem).display(
						RatlantisBlockRegistry.MARBLED_CHEESE_GOLEM_CORE.get(),
						Component.translatable("advancement.ratlantis.marbled_cheese_golem_core.title"),
						Component.translatable("advancement.ratlantis.marbled_cheese_golem_core.desc"),
						null, FrameType.TASK, true, false, false)
				.addCriterion("get_core", InventoryChangeTrigger.TriggerInstance.hasItems(RatlantisBlockRegistry.MARBLED_CHEESE_GOLEM_CORE.get()))
				.save(consumer, "ratlantis:marbled_cheese_golem_core");

		Advancement automaton = Advancement.Builder.advancement().parent(core).display(
						RatlantisItemRegistry.ARCANE_TECHNOLOGY.get(),
						Component.translatable("advancement.ratlantis.marbled_cheese_golem.title"),
						Component.translatable("advancement.ratlantis.marbled_cheese_golem.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("killed_automaton", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.RATLANTEAN_AUTOMATON.get())))
				.save(consumer, "ratlantis:marbled_cheese_golem");

		Advancement.Builder.advancement().parent(automaton).display(
						RatsBlockRegistry.UPGRADE_COMBINER.get(),
						Component.translatable("advancement.ratlantis.upgrade_combiner.title"),
						Component.translatable("advancement.ratlantis.upgrade_combiner.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("get_combiner", InventoryChangeTrigger.TriggerInstance.hasItems(RatsBlockRegistry.UPGRADE_COMBINER.get()))
				.save(consumer, "ratlantis:upgrade_combiner");

		Advancement ingot = Advancement.Builder.advancement().parent(gem).display(
						RatlantisItemRegistry.ORATCHALCUM_INGOT.get(),
						Component.translatable("advancement.ratlantis.oratchalcum_ingot.title"),
						Component.translatable("advancement.ratlantis.oratchalcum_ingot.desc"),
						null, FrameType.GOAL, true, false, false)
				.addCriterion("get_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(RatlantisItemRegistry.ORATCHALCUM_INGOT.get()))
				.save(consumer, "ratlantis:oratchalcum_ingot");

		Advancement.Builder.advancement().parent(ingot).display(
						RatlantisItemRegistry.RATLANTIS_HELMET.get(),
						Component.translatable("advancement.ratlantis.ratlantis_armor.title"),
						Component.translatable("advancement.ratlantis.ratlantis_armor.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("get_helm", InventoryChangeTrigger.TriggerInstance.hasItems(RatlantisItemRegistry.RATLANTIS_HELMET.get()))
				.addCriterion("get_chest", InventoryChangeTrigger.TriggerInstance.hasItems(RatlantisItemRegistry.RATLANTIS_CHESTPLATE.get()))
				.addCriterion("get_pants", InventoryChangeTrigger.TriggerInstance.hasItems(RatlantisItemRegistry.RATLANTIS_LEGGINGS.get()))
				.addCriterion("get_boots", InventoryChangeTrigger.TriggerInstance.hasItems(RatlantisItemRegistry.RATLANTIS_BOOTS.get()))
				.save(consumer, "ratlantis:ratlantis_armor");

		Advancement ratbot = Advancement.Builder.advancement().parent(portal).display(
						RatlantisItemRegistry.CHARGED_RATBOT_BARREL.get(),
						Component.translatable("advancement.ratlantis.ratlantean_ratbot.title"),
						Component.translatable("advancement.ratlantis.ratlantean_ratbot.desc"),
						null, FrameType.TASK, true, false, false)
				.requirements(RequirementsStrategy.OR)
				.addCriterion("hurt_ratlantean_ratbot", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntity(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.RATLANTEAN_RATBOT.get()).build()))
				.addCriterion("killed_ratlantean_ratbot", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.RATLANTEAN_RATBOT.get()).build()))
				.addCriterion("hurt_by_ratlantean_ratbot", EntityHurtPlayerTrigger.TriggerInstance.entityHurtPlayer(DamagePredicate.Builder.damageInstance().sourceEntity(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.RATLANTEAN_RATBOT.get()).build())))
				.addCriterion("killed_by_ratlantean_ratbot", KilledTrigger.TriggerInstance.entityKilledPlayer(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.RATLANTEAN_RATBOT.get()).build()))
				.save(consumer, "ratlantis:ratlantean_ratbot");

		Advancement.Builder.advancement().parent(ratbot).display(
						RatlantisItemRegistry.RAT_UPGRADE_RATINATOR.get(),
						Component.translatable("advancement.ratlantis.rat_upgrade_ratinator.title"),
						Component.translatable("advancement.ratlantis.rat_upgrade_ratinator.desc"),
						null, FrameType.GOAL, true, false, false)
				.addCriterion("get_upgrade", InventoryChangeTrigger.TriggerInstance.hasItems(RatlantisItemRegistry.RAT_UPGRADE_RATINATOR.get()))
				.save(consumer, "ratlantis:rat_upgrade_ratinator");

		Advancement.Builder.advancement().parent(portal).display(
						RatlantisItemRegistry.RATLANTEAN_FLAME.get(),
						Component.translatable("advancement.ratlantis.ratlantean_spirit.title"),
						Component.translatable("advancement.ratlantis.ratlantean_spirit.desc"),
						null, FrameType.TASK, true, false, false)
				.requirements(RequirementsStrategy.OR)
				.addCriterion("hurt_ratlantean_spirit", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntity(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.RATLANTEAN_SPIRIT.get()).build()))
				.addCriterion("killed_ratlantean_spirit", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.RATLANTEAN_SPIRIT.get()).build()))
				.addCriterion("hurt_by_ratlantean_spirit", EntityHurtPlayerTrigger.TriggerInstance.entityHurtPlayer(DamagePredicate.Builder.damageInstance().sourceEntity(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.RATLANTEAN_SPIRIT.get()).build())))
				.addCriterion("killed_by_ratlantean_spirit", KilledTrigger.TriggerInstance.entityKilledPlayer(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.RATLANTEAN_SPIRIT.get()).build()))
				.save(consumer, "ratlantis:ratlantean_spirit");

		Advancement ratlantean = Advancement.Builder.advancement().parent(portal).display(
						RatlantisItemRegistry.FERAL_RAT_CLAW.get(),
						Component.translatable("advancement.ratlantis.feral_ratlantean.title"),
						Component.translatable("advancement.ratlantis.feral_ratlantean.desc"),
						null, FrameType.TASK, true, false, false)
				.requirements(RequirementsStrategy.OR)
				.addCriterion("hurt_feral_ratlantean", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntity(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.FERAL_RATLANTEAN.get()).build()))
				.addCriterion("killed_feral_ratlantean", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.FERAL_RATLANTEAN.get()).build()))
				.addCriterion("hurt_by_feral_ratlantean", EntityHurtPlayerTrigger.TriggerInstance.entityHurtPlayer(DamagePredicate.Builder.damageInstance().sourceEntity(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.FERAL_RATLANTEAN.get()).build())))
				.addCriterion("killed_by_feral_ratlantean", KilledTrigger.TriggerInstance.entityKilledPlayer(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.FERAL_RATLANTEAN.get()).build()))
				.save(consumer, "ratlantis:feral_ratlantean");

		Advancement vial = Advancement.Builder.advancement().parent(ratlantean).display(
						RatlantisItemRegistry.VIAL_OF_SENTIENCE.get(),
						Component.translatable("advancement.ratlantis.vial_of_sentience.title"),
						Component.translatable("advancement.ratlantis.vial_of_sentience.desc"),
						null, FrameType.GOAL, true, false, false)
				.addCriterion("get_vial", InventoryChangeTrigger.TriggerInstance.hasItems(RatlantisItemRegistry.VIAL_OF_SENTIENCE.get()))
				.save(consumer, "ratlantis:vial_of_sentience");

		Advancement neo = Advancement.Builder.advancement().parent(vial).display(
						RatlantisItemRegistry.PSIONIC_RAT_BRAIN.get(),
						Component.translatable("advancement.ratlantis.neoratlantean.title"),
						Component.translatable("advancement.ratlantis.neoratlantean.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("killed_neoratlantean", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.NEO_RATLANTEAN.get())))
				.save(consumer, "ratlantis:neoratlantean");

		Advancement.Builder.advancement().parent(neo).display(
						RatlantisItemRegistry.RAT_UPGRADE_PSYCHIC.get(),
						Component.translatable("advancement.ratlantis.rat_upgrade_psychic.title"),
						Component.translatable("advancement.ratlantis.rat_upgrade_psychic.desc"),
						null, FrameType.GOAL, true, false, false)
				.addCriterion("get_upgrade", InventoryChangeTrigger.TriggerInstance.hasItems(RatlantisItemRegistry.RAT_UPGRADE_PSYCHIC.get()))
				.save(consumer, "ratlantis:rat_upgrade_psychic");

		Advancement baron = Advancement.Builder.advancement().parent(portal).display(
						RatlantisItemRegistry.BIPLANE_WING.get(),
						Component.translatable("advancement.ratlantis.defeat_rat_baron.title"),
						Component.translatable("advancement.ratlantis.defeat_rat_baron.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.requirements(RequirementsStrategy.OR)
				.addCriterion("killed_baron", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.RAT_BARON.get())))
				.addCriterion("killed_plane", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(RatlantisEntityRegistry.RAT_BARON_PLANE.get())))
				.save(consumer, "ratlantis:defeat_rat_baron");

		Advancement.Builder.advancement().parent(baron).display(
						RatlantisItemRegistry.RAT_UPGRADE_BIPLANE_MOUNT.get(),
						Component.translatable("advancement.ratlantis.rat_upgrade_mount_biplane.title"),
						Component.translatable("advancement.ratlantis.rat_upgrade_mount_biplane.desc"),
						null, FrameType.GOAL, true, false, false)
				.addCriterion("get_upgrade", InventoryChangeTrigger.TriggerInstance.hasItems(RatlantisItemRegistry.RAT_UPGRADE_BIPLANE_MOUNT.get()))
				.save(consumer, "ratlantis:rat_upgrade_mount_biplane");

		Advancement dummy = new Advancement(new ResourceLocation(RatsMod.MODID, "root"), null, null, AdvancementRewards.EMPTY, Maps.newHashMap(), new String[0][0], false);

		Advancement.Builder.advancement().parent(dummy).display(
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

				.addCriterion("has_pirat_hat", InventoryChangeTrigger.TriggerInstance.hasItems(RatsItemRegistry.PIRAT_HAT.get()))
				.addCriterion("has_ghost_pirat_hat", InventoryChangeTrigger.TriggerInstance.hasItems(RatlantisItemRegistry.GHOST_PIRAT_HAT.get()))
				.addCriterion("has_aviator_hat", InventoryChangeTrigger.TriggerInstance.hasItems(RatlantisItemRegistry.AVIATOR_HAT.get()))
				.addCriterion("has_officer_hat", InventoryChangeTrigger.TriggerInstance.hasItems(RatlantisItemRegistry.MILITARY_HAT.get()))
				.save(consumer, "rats:all_hats");
	}
}
