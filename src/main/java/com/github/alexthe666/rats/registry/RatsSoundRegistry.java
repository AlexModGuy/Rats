package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class RatsSoundRegistry {

	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, RatsMod.MODID);

	public static final RegistryObject<SoundEvent> AIR_RAID_SIREN = createSoundEvent("block.rats.air_raid.siren");
	public static final RegistryObject<SoundEvent> BIPLANE_DEATH = createSoundEvent("entity.rats.biplane.death");
	public static final RegistryObject<SoundEvent> BIPLANE_HURT = createSoundEvent("entity.rats.biplane.hurt");
	public static final RegistryObject<SoundEvent> BIPLANE_LOOP = createSoundEvent("entity.rats.biplane.loop");
	public static final RegistryObject<SoundEvent> BIPLANE_SHOOT = createSoundEvent("entity.rats.biplane.shoot");
	public static final RegistryObject<SoundEvent> BLACK_DEATH_DIE = createSoundEvent("entity.rats.black_death.death");
	public static final RegistryObject<SoundEvent> BLACK_DEATH_HURT = createSoundEvent("entity.rats.black_death.hurt");
	public static final RegistryObject<SoundEvent> BLACK_DEATH_IDLE = createSoundEvent("entity.rats.black_death.ambient");
	public static final RegistryObject<SoundEvent> BLACK_DEATH_SUMMON = createSoundEvent("entity.rats.black_death.summon");
	public static final RegistryObject<SoundEvent> BLUE_CHEESE_MADE = createSoundEvent("block.rats.cheese_cauldron.blue_cheese_made");
	public static final RegistryObject<SoundEvent> CHEESE_CAULDRON_EMPTY = createSoundEvent("block.rats.cheese_cauldron.empty");
	public static final RegistryObject<SoundEvent> CHEESE_MADE = createSoundEvent("block.rats.cheese_cauldron.cheese_made");
	public static final RegistryObject<SoundEvent> DUTCHRAT_BELL = createSoundEvent("block.rats.dutchrat_bell.ring");
	public static final RegistryObject<SoundEvent> DUTCHRAT_DIE = createSoundEvent("entity.rats.dutchrat.death");
	public static final RegistryObject<SoundEvent> DUTCHRAT_HURT = createSoundEvent("entity.rats.dutchrat.hurt");
	public static final RegistryObject<SoundEvent> DUTCHRAT_IDLE = createSoundEvent("entity.rats.dutchrat.ambient");
	public static final RegistryObject<SoundEvent> DUTCHRAT_LAUGH = createSoundEvent("entity.rats.dutchrat.laugh");
	public static final RegistryObject<SoundEvent> DYE_SPONGE_USED = createSoundEvent("item.rats.dye_sponge.used");
	public static final RegistryObject<SoundEvent> ESSENCE_APPLIED = createSoundEvent("item.rats.ratbow_essence.used");
	public static final RegistryObject<SoundEvent> LASER = createSoundEvent("entity.rats.laser.shoot");
	public static final RegistryObject<SoundEvent> LIVING_MICE = createSoundEvent("music_disc.rats.living_mice");
	public static final RegistryObject<SoundEvent> MICE_ON_VENUS = createSoundEvent("music_disc.rats.mice_on_venus");
	public static final RegistryObject<SoundEvent> NEORATLANTEAN_DIE = createSoundEvent("entity.rats.neoratlantean.death");
	public static final RegistryObject<SoundEvent> NEORATLANTEAN_HURT = createSoundEvent("entity.rats.neoratlantean.hurt");
	public static final RegistryObject<SoundEvent> NEORATLANTEAN_IDLE = createSoundEvent("entity.rats.neoratlantean.ambient");
	public static final RegistryObject<SoundEvent> NEORATLANTEAN_LOOP = createSoundEvent("entity.rats.neoratlantean.loop");
	public static final RegistryObject<SoundEvent> NEORATLANTEAN_SUMMON = createSoundEvent("entity.rats.neoratlantean.summon");
	public static final RegistryObject<SoundEvent> NETHER_CHEESE_MADE = createSoundEvent("block.rats.cheese_cauldron.nether_cheese_made");
	public static final RegistryObject<SoundEvent> PIED_PIPER_DEATH = createSoundEvent("entity.rats.pied_piper.death");
	public static final RegistryObject<SoundEvent> PIED_PIPER_HURT = createSoundEvent("entity.rats.pied_piper.hurt");
	public static final RegistryObject<SoundEvent> PIPER_LOOP = createSoundEvent("entity.rats.pied_piper.loop");
	public static final RegistryObject<SoundEvent> PIRAT_SHOOT = createSoundEvent("entity.rats.pirat.shoot");
	public static final RegistryObject<SoundEvent> PLAGUE_CLOUD_DEATH = createSoundEvent("entity.rats.plague_cloud.death");
	public static final RegistryObject<SoundEvent> PLAGUE_CLOUD_HURT = createSoundEvent("entity.rats.plague_cloud.hurt");
	public static final RegistryObject<SoundEvent> PLAGUE_CLOUD_SHOOT = createSoundEvent("entity.rats.plague_cloud.shoot");
	public static final RegistryObject<SoundEvent> PLAGUE_DOCTOR_DISAPPEAR = createSoundEvent("entity.rats.plague_doctor.disappear");
	public static final RegistryObject<SoundEvent> PLAGUE_DOCTOR_DRINK = createSoundEvent("entity.rats.plague_doctor.drink");
	public static final RegistryObject<SoundEvent> PLAGUE_DOCTOR_DRINK_POTION = createSoundEvent("entity.rats.plague_doctor.drink_potion");
	public static final RegistryObject<SoundEvent> PLAGUE_DOCTOR_REAPPEAR = createSoundEvent("entity.rats.plague_doctor.reappear");
	public static final RegistryObject<SoundEvent> PLAGUE_DOCTOR_SUMMON = createSoundEvent("entity.rats.plague_doctor.summon");
	public static final RegistryObject<SoundEvent> PLAGUE_DOCTOR_THROW = createSoundEvent("entity.rats.plague_doctor.throw");
	public static final RegistryObject<SoundEvent> PLAGUE_SPREAD = createSoundEvent("effect.rats.plague_spread");
	public static final RegistryObject<SoundEvent> POTION_EFFECT_BEGIN = createSoundEvent("effect.rats.potion_effect.begin");
	public static final RegistryObject<SoundEvent> POTION_EFFECT_END = createSoundEvent("effect.rats.potion_effect.end");
	public static final RegistryObject<SoundEvent> RATBOT_DEATH = createSoundEvent("entity.rats.ratlantean_ratbot.death");
	public static final RegistryObject<SoundEvent> RATBOT_HURT = createSoundEvent("entity.rats.ratlantean_ratbot.hurt");
	public static final RegistryObject<SoundEvent> RATFISH_AMBIENT = createSoundEvent("entity.rats.ratfish.ambient");
	public static final RegistryObject<SoundEvent> RATFISH_DEATH = createSoundEvent("entity.rats.ratfish.death");
	public static final RegistryObject<SoundEvent> RATFISH_FLOP = createSoundEvent("entity.rats.ratfish.flop");
	public static final RegistryObject<SoundEvent> RATFISH_HURT = createSoundEvent("entity.rats.ratfish.hurt");
	public static final RegistryObject<SoundEvent> RATLANTEAN_AUTOMATON_DIE = createSoundEvent("entity.rats.ratlantean_automaton.death");
	public static final RegistryObject<SoundEvent> RATLANTEAN_AUTOMATON_HURT = createSoundEvent("entity.rats.ratlantean_automaton.hurt");
	public static final RegistryObject<SoundEvent> RATLANTEAN_AUTOMATON_IDLE = createSoundEvent("entity.rats.ratlantean_automaton.ambient");
	public static final RegistryObject<SoundEvent> RATLANTEAN_FLAME_SHOOT = createSoundEvent("item.rats.ratlantean_flame.shoot");
	public static final RegistryObject<SoundEvent> RATLANTEAN_RATBOT_IDLE = createSoundEvent("entity.rats.ratlantean_ratbot.ambient");
	public static final RegistryObject<SoundEvent> RATLANTEAN_SPIRIT_DIE = createSoundEvent("entity.rats.ratlantean_spirit.death");
	public static final RegistryObject<SoundEvent> RATLANTEAN_SPIRIT_HURT = createSoundEvent("entity.rats.ratlantean_spirit.hurt");
	public static final RegistryObject<SoundEvent> RATLANTEAN_SPIRIT_IDLE = createSoundEvent("entity.rats.ratlantean_spirit.ambient");
	public static final RegistryObject<SoundEvent> RATTLING_GUN_SHOOT = createSoundEvent("entity.rats.rattling_gun.shoot");
	public static final RegistryObject<SoundEvent> RAT_BEAST_GROWL = createSoundEvent("entity.rats.rat_beast.growl");
	public static final RegistryObject<SoundEvent> RAT_CRAFT = createSoundEvent("entity.rats.rat.craft");
	public static final RegistryObject<SoundEvent> RAT_DIE = createSoundEvent("entity.rats.rat.death");
	public static final RegistryObject<SoundEvent> RAT_DIG = createSoundEvent("entity.rats.rat.dig");
	public static final RegistryObject<SoundEvent> RAT_DRINK = createSoundEvent("entity.rats.rat.drink");
	public static final RegistryObject<SoundEvent> RAT_EAT = createSoundEvent("entity.rats.rat.eat");
	public static final RegistryObject<SoundEvent> RAT_FLUTE = createSoundEvent("item.rats.rat_flute.normal");
	public static final RegistryObject<SoundEvent> RAT_FLUTE_NO_FUNNY = createSoundEvent("item.rats.rat_flute.no_funny");
	public static final RegistryObject<SoundEvent> RAT_GROWL = createSoundEvent("entity.rats.rat.growl");
	public static final RegistryObject<SoundEvent> RAT_HURT = createSoundEvent("entity.rats.rat.hurt");
	public static final RegistryObject<SoundEvent> RAT_IDLE = createSoundEvent("entity.rats.rat.ambient");
	public static final RegistryObject<SoundEvent> RAT_KING_SUMMON = createSoundEvent("entity.rats.rat_king.summon");
	public static final RegistryObject<SoundEvent> RAT_KING_SHOOT = createSoundEvent("entity.rats.rat_king.shoot");
	public static final RegistryObject<SoundEvent> RAT_MAKE_COIN = createSoundEvent("entity.rats.rat.coin");
	public static final RegistryObject<SoundEvent> RAT_NET_THROW = createSoundEvent("item.rats.rat_net.throw");
	public static final RegistryObject<SoundEvent> RAT_NUGGET_ORE = createSoundEvent("item.rats.rat_nugget.ore");
	public static final RegistryObject<SoundEvent> RAT_PIRATE = createSoundEvent("entity.rats.rat.pirate");
	public static final RegistryObject<SoundEvent> RAT_POOP = createSoundEvent("entity.rats.rat.poop");
	public static final RegistryObject<SoundEvent> RAT_SANTA = createSoundEvent("entity.rats.rat.santa");
	public static final RegistryObject<SoundEvent> RAT_SHOOT = createSoundEvent("entity.rats.rat.shoot");
	public static final RegistryObject<SoundEvent> RAT_TELEPORT = createSoundEvent("entity.rats.rat.teleport");
	public static final RegistryObject<SoundEvent> RAT_TRANSFER = createSoundEvent("entity.rats.rat.transfer");
	public static final RegistryObject<SoundEvent> RAT_TRAP_CLOSE = createSoundEvent("block.rats.rat_trap.close");
	public static final RegistryObject<SoundEvent> RAT_TRAP_OPEN = createSoundEvent("block.rats.rat_trap.open");
	public static final RegistryObject<SoundEvent> RAT_TRAP_ADD_BAIT = createSoundEvent("block.rats.rat_trap.add_bait");
	public static final RegistryObject<SoundEvent> RAT_TRAP_REMOVE_BAIT = createSoundEvent("block.rats.rat_trap.remove_bait");
	public static final RegistryObject<SoundEvent> RAT_WHISTLE = createSoundEvent("item.rats.rat_whistle.whistle");
	public static final RegistryObject<SoundEvent> SAPLING_TRANSFORM = createSoundEvent("item.rats.ectoplasm.transform");
	public static final RegistryObject<SoundEvent> TRASH_CAN = createSoundEvent("block.rats.trash_can.open");
	public static final RegistryObject<SoundEvent> TRASH_CAN_EMPTY = createSoundEvent("block.rats.trash_can.empty");
	public static final RegistryObject<SoundEvent> TRASH_CAN_FILL = createSoundEvent("block.rats.trash_can.fill");

	private static RegistryObject<SoundEvent> createSoundEvent(final String soundName) {
		return SOUNDS.register(soundName, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(RatsMod.MODID, soundName)));
	}

	public static SoundEvent getFluteSound() {
		return RatConfig.funnyFluteSound ? RAT_FLUTE.get() : RAT_FLUTE_NO_FUNNY.get();
	}
}
