package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.server.capability.SelectedRat;
import com.github.alexthe666.rats.server.capability.SelectedRatCapability;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RatsCapabilityRegistry {
	public static final Capability<SelectedRatCapability> SELECTED_RAT = CapabilityManager.get(new CapabilityToken<>() {
	});

	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.register(SelectedRat.class);
	}

	public static void attachCap(AttachCapabilitiesEvent<Entity> e) {
		if (e.getObject() instanceof Player player) {
			e.addCapability(SelectedRat.ID, new ICapabilityProvider() {

				final LazyOptional<SelectedRat> inst = LazyOptional.of(() -> {
					SelectedRatCapability i = new SelectedRatCapability();
					i.setPlayer(player);
					return i;
				});

				@Nonnull
				@Override
				public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
					return SELECTED_RAT.orEmpty(capability, inst.cast());
				}
			});
		}
	}
}
