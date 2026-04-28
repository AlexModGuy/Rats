package com.github.alexthe666.rats.client.util;

import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.world.item.JukeboxSong;

public class RatRecordSoundInstance extends AbstractTickableSoundInstance {

	private final TamedRat rat;
	private final Item recordItem;

	public RatRecordSoundInstance(TamedRat rat, Item recordItem) {
		super(resolveSound(recordItem), SoundSource.RECORDS, RandomSource.create());
		this.attenuation = Attenuation.LINEAR;
		this.looping = false;
		this.rat = rat;
		this.recordItem = recordItem;
	}

	private static SoundEvent resolveSound(Item item) {
		ItemStack stack = new ItemStack(item);
		JukeboxPlayable playable = stack.get(DataComponents.JUKEBOX_PLAYABLE);
		if (playable != null && Minecraft.getInstance().level != null) {
			var songHolder = playable.song().unwrap(Minecraft.getInstance().level.registryAccess());
			if (songHolder.isPresent()) {
				JukeboxSong song = songHolder.get().value();
				return song.soundEvent().value();
			}
		}
		return SoundEvents.MUSIC_DISC_13.value();
	}

	@Override
	public void tick() {
		if (!this.rat.isRemoved() && RatUpgradeUtils.hasUpgrade(this.rat, RatsItemRegistry.RAT_UPGRADE_DJ.get()) && this.rat.getMainHandItem().is(this.recordItem)) {
			this.x = this.rat.getX();
			this.y = this.rat.getY();
			this.z = this.rat.getZ();
			this.volume = 5.0F;
		} else {
			Minecraft.getInstance().getSoundManager().stop(this);
		}
	}
}
