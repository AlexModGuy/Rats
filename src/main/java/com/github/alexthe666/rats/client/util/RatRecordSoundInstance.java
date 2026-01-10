package com.github.alexthe666.rats.client.util;

import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.world.item.JukeboxSong;

public class RatRecordSoundInstance extends AbstractTickableSoundInstance {

	private final TamedRat rat;
	private final ItemStack recordStack;

	public RatRecordSoundInstance(TamedRat rat, ItemStack recordStack) {
		super(getSoundFromStack(recordStack), SoundSource.RECORDS, RandomSource.create());
		this.attenuation = Attenuation.LINEAR;
		this.looping = false;
		this.rat = rat;
		this.recordStack = recordStack;
	}

	private static SoundEvent getSoundFromStack(ItemStack stack) {
		JukeboxPlayable playable = stack.get(DataComponents.JUKEBOX_PLAYABLE);
		if (playable != null) {
			// unwrap(HolderLookup.Provider) returns Optional<Holder<JukeboxSong>>
			return playable.song().unwrap(Minecraft.getInstance().level.registryAccess())
				.map(holder -> holder.value().soundEvent().value())
				.orElseGet(() -> SoundEvent.createVariableRangeEvent(stack.getItem().builtInRegistryHolder().key().location()));
		}
		return SoundEvent.createVariableRangeEvent(stack.getItem().builtInRegistryHolder().key().location());
	}

	@Override
	public void tick() {
		if (!this.rat.isRemoved() && RatUpgradeUtils.hasUpgrade(this.rat, RatsItemRegistry.RAT_UPGRADE_DJ.get()) && ItemStack.isSameItem(this.rat.getMainHandItem(), this.recordStack)) {
			this.x = this.rat.getX();
			this.y = this.rat.getY();
			this.z = this.rat.getZ();
			this.volume = 5.0F;
		} else {
			Minecraft.getInstance().getSoundManager().stop(this);
		}
	}
}







