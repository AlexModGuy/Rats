package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.neoforge.registries.DeferredHolder;

public class RatsJukeboxSongRegistry {

	public static final ResourceKey<JukeboxSong> MICE_ON_VENUS = createKey("mice_on_venus");
	public static final ResourceKey<JukeboxSong> LIVING_MICE = createKey("living_mice");

	private static ResourceKey<JukeboxSong> createKey(String name) {
		return ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, name));
	}

	public static void bootstrap(BootstrapContext<JukeboxSong> context) {
		register(context, MICE_ON_VENUS, RatsSoundRegistry.MICE_ON_VENUS, 280, 13);
		register(context, LIVING_MICE, RatsSoundRegistry.LIVING_MICE, 188, 13);
	}

	private static void register(BootstrapContext<JukeboxSong> context, ResourceKey<JukeboxSong> key, DeferredHolder<SoundEvent, SoundEvent> soundEvent, int lengthInSeconds, int comparatorOutput) {
		context.register(key, new JukeboxSong(soundEvent.getDelegate(), Component.translatable(Util.makeDescriptionId("jukebox_song", key.location())), (float) lengthInSeconds, comparatorOutput));
	}
}

