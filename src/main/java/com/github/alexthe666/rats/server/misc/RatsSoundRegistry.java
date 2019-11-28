package com.github.alexthe666.rats.server.misc;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(RatsMod.MODID)
public class RatsSoundRegistry {

    @GameRegistry.ObjectHolder("potion_effect_begin")
    public static final SoundEvent POTION_EFFECT_BEGIN = createSoundEvent("potion_effect_begin");
    @GameRegistry.ObjectHolder("potion_effect_end")
    public static final SoundEvent POTION_EFFECT_END = createSoundEvent("potion_effect_end");
    @GameRegistry.ObjectHolder("rat_idle")
    public static final SoundEvent RAT_IDLE = createSoundEvent("rat_idle");
    @GameRegistry.ObjectHolder("rat_hurt")
    public static final SoundEvent RAT_HURT = createSoundEvent("rat_hurt");
    @GameRegistry.ObjectHolder("rat_die")
    public static final SoundEvent RAT_DIE = createSoundEvent("rat_die");
    @GameRegistry.ObjectHolder("rat_dig")
    public static final SoundEvent RAT_DIG = createSoundEvent("rat_dig");
    @GameRegistry.ObjectHolder("rat_plague")
    public static final SoundEvent RAT_PLAGUE = createSoundEvent("rat_plague");
    @GameRegistry.ObjectHolder("rat_flute")
    public static final SoundEvent RAT_FLUTE = createSoundEvent("rat_flute");
    @GameRegistry.ObjectHolder("piper_loop")
    public static final SoundEvent PIPER_LOOP = createSoundEvent("piper_loop");
    @GameRegistry.ObjectHolder("rat_laser")
    public static final SoundEvent LASER = createSoundEvent("rat_laser");
    @GameRegistry.ObjectHolder("ratlantean_spirit_idle")
    public static final SoundEvent RATLANTEAN_SPIRIT_IDLE = createSoundEvent("ratlantean_spirit_idle");
    @GameRegistry.ObjectHolder("ratlantean_spirit_hurt")
    public static final SoundEvent RATLANTEAN_SPIRIT_HURT = createSoundEvent("ratlantean_spirit_hurt");
    @GameRegistry.ObjectHolder("ratlantean_spirit_die")
    public static final SoundEvent RATLANTEAN_SPIRIT_DIE = createSoundEvent("ratlantean_spirit_die");
    @GameRegistry.ObjectHolder("ratlantean_automaton_idle")
    public static final SoundEvent RATLANTEAN_AUTOMATON_IDLE = createSoundEvent("ratlantean_automaton_idle");
    @GameRegistry.ObjectHolder("ratlantean_automaton_hurt")
    public static final SoundEvent RATLANTEAN_AUTOMATON_HURT = createSoundEvent("ratlantean_automaton_hurt");
    @GameRegistry.ObjectHolder("ratlantean_automaton_die")
    public static final SoundEvent RATLANTEAN_AUTOMATON_DIE = createSoundEvent("ratlantean_automaton_die");
    @GameRegistry.ObjectHolder("neoratlantean_idle")
    public static final SoundEvent NEORATLANTEAN_IDLE = createSoundEvent("neoratlantean_idle");
    @GameRegistry.ObjectHolder("neoratlantean_hurt")
    public static final SoundEvent NEORATLANTEAN_HURT = createSoundEvent("neoratlantean_hurt");
    @GameRegistry.ObjectHolder("neoratlantean_die")
    public static final SoundEvent NEORATLANTEAN_DIE = createSoundEvent("neoratlantean_die");
    @GameRegistry.ObjectHolder("neoratlantean_loop")
    public static final SoundEvent NEORATLANTEAN_LOOP = createSoundEvent("neoratlantean_loop");
    @GameRegistry.ObjectHolder("neoratlantean_summon")
    public static final SoundEvent NEORATLANTEAN_SUMMON = createSoundEvent("neoratlantean_summon");
    @GameRegistry.ObjectHolder("mice_on_venus")
    public static final SoundEvent MICE_ON_VENUS = createSoundEvent("mice_on_venus");
    @GameRegistry.ObjectHolder("living_mice")
    public static final SoundEvent LIVING_MICE = createSoundEvent("living_mice");
    @GameRegistry.ObjectHolder("black_death_idle")
    public static final SoundEvent BLACK_DEATH_IDLE = createSoundEvent("black_death_idle");
    @GameRegistry.ObjectHolder("black_death_hurt")
    public static final SoundEvent BLACK_DEATH_HURT = createSoundEvent("black_death_hurt");
    @GameRegistry.ObjectHolder("black_death_die")
    public static final SoundEvent BLACK_DEATH_DIE = createSoundEvent("black_death_die");
    @GameRegistry.ObjectHolder("rat_poop")
    public static final SoundEvent RAT_POOP = createSoundEvent("rat_poop");
    @GameRegistry.ObjectHolder("rat_santa")
    public static final SoundEvent RAT_SANTA = createSoundEvent("rat_santa");


    private static SoundEvent createSoundEvent(final String soundName) {
        final ResourceLocation soundID = new ResourceLocation(RatsMod.MODID, soundName);
        return new SoundEvent(soundID).setRegistryName(soundID);
    }
}
