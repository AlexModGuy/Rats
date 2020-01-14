package com.github.alexthe666.rats.server.misc;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(RatsMod.MODID)
public class RatsSoundRegistry {

    @ObjectHolder("potion_effect_begin")
    public static final SoundEvent POTION_EFFECT_BEGIN = createSoundEvent("potion_effect_begin");
    @ObjectHolder("potion_effect_end")
    public static final SoundEvent POTION_EFFECT_END = createSoundEvent("potion_effect_end");
    @ObjectHolder("rat_idle")
    public static final SoundEvent RAT_IDLE = createSoundEvent("rat_idle");
    @ObjectHolder("rat_hurt")
    public static final SoundEvent RAT_HURT = createSoundEvent("rat_hurt");
    @ObjectHolder("rat_die")
    public static final SoundEvent RAT_DIE = createSoundEvent("rat_die");
    @ObjectHolder("rat_dig")
    public static final SoundEvent RAT_DIG = createSoundEvent("rat_dig");
    @ObjectHolder("rat_plague")
    public static final SoundEvent RAT_PLAGUE = createSoundEvent("rat_plague");
    @ObjectHolder("rat_flute")
    public static final SoundEvent RAT_FLUTE = createSoundEvent("rat_flute");
    @ObjectHolder("piper_loop")
    public static final SoundEvent PIPER_LOOP = createSoundEvent("piper_loop");
    @ObjectHolder("rat_laser")
    public static final SoundEvent LASER = createSoundEvent("rat_laser");
    @ObjectHolder("ratlantean_spirit_idle")
    public static final SoundEvent RATLANTEAN_SPIRIT_IDLE = createSoundEvent("ratlantean_spirit_idle");
    @ObjectHolder("ratlantean_spirit_hurt")
    public static final SoundEvent RATLANTEAN_SPIRIT_HURT = createSoundEvent("ratlantean_spirit_hurt");
    @ObjectHolder("ratlantean_spirit_die")
    public static final SoundEvent RATLANTEAN_SPIRIT_DIE = createSoundEvent("ratlantean_spirit_die");
    @ObjectHolder("ratlantean_automaton_idle")
    public static final SoundEvent RATLANTEAN_AUTOMATON_IDLE = createSoundEvent("ratlantean_automaton_idle");
    @ObjectHolder("ratlantean_automaton_hurt")
    public static final SoundEvent RATLANTEAN_AUTOMATON_HURT = createSoundEvent("ratlantean_automaton_hurt");
    @ObjectHolder("ratlantean_automaton_die")
    public static final SoundEvent RATLANTEAN_AUTOMATON_DIE = createSoundEvent("ratlantean_automaton_die");
    @ObjectHolder("neoratlantean_idle")
    public static final SoundEvent NEORATLANTEAN_IDLE = createSoundEvent("neoratlantean_idle");
    @ObjectHolder("neoratlantean_hurt")
    public static final SoundEvent NEORATLANTEAN_HURT = createSoundEvent("neoratlantean_hurt");
    @ObjectHolder("neoratlantean_die")
    public static final SoundEvent NEORATLANTEAN_DIE = createSoundEvent("neoratlantean_die");
    @ObjectHolder("neoratlantean_loop")
    public static final SoundEvent NEORATLANTEAN_LOOP = createSoundEvent("neoratlantean_loop");
    @ObjectHolder("neoratlantean_summon")
    public static final SoundEvent NEORATLANTEAN_SUMMON = createSoundEvent("neoratlantean_summon");
    @ObjectHolder("mice_on_venus")
    public static final SoundEvent MICE_ON_VENUS = createSoundEvent("mice_on_venus");
    @ObjectHolder("living_mice")
    public static final SoundEvent LIVING_MICE = createSoundEvent("living_mice");
    @ObjectHolder("black_death_idle")
    public static final SoundEvent BLACK_DEATH_IDLE = createSoundEvent("black_death_idle");
    @ObjectHolder("black_death_hurt")
    public static final SoundEvent BLACK_DEATH_HURT = createSoundEvent("black_death_hurt");
    @ObjectHolder("black_death_die")
    public static final SoundEvent BLACK_DEATH_DIE = createSoundEvent("black_death_die");
    @ObjectHolder("rat_poop")
    public static final SoundEvent RAT_POOP = createSoundEvent("rat_poop");
    @ObjectHolder("rat_santa")
    public static final SoundEvent RAT_SANTA = createSoundEvent("rat_santa");
    @ObjectHolder("dutchrat_bell")
    public static final SoundEvent DUTCHRAT_BELL = createSoundEvent("dutchrat_bell");
    @ObjectHolder("dutchrat_idle")
    public static final SoundEvent DUTCHRAT_IDLE = createSoundEvent("dutchrat_idle");
    @ObjectHolder("dutchrat_hurt")
    public static final SoundEvent DUTCHRAT_HURT = createSoundEvent("dutchrat_hurt");
    @ObjectHolder("dutchrat_die")
    public static final SoundEvent DUTCHRAT_DIE = createSoundEvent("dutchrat_die");


    private static SoundEvent createSoundEvent(final String soundName) {
        final ResourceLocation soundID = new ResourceLocation(RatsMod.MODID, soundName);
        return new SoundEvent(soundID).setRegistryName(soundID);
    }
}
