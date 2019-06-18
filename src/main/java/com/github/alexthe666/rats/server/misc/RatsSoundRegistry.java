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

    private static SoundEvent createSoundEvent(final String soundName) {
        final ResourceLocation soundID = new ResourceLocation(RatsMod.MODID, soundName);
        return new SoundEvent(soundID).setRegistryName(soundID);
    }
}
