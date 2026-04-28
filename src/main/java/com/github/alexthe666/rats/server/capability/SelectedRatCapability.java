package com.github.alexthe666.rats.server.capability;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.message.UpdateSelectedRatPacket;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class SelectedRatCapability implements SelectedRat {

	private Player host;
	@Nullable
	private TamedRat rat;

	@Override
	public void setPlayer(Player player) {
		this.host = player;
	}

	@Override
	public void clearSelectedRat() {
		this.rat = null;
	}

	@Nullable
	@Override
	public TamedRat getSelectedRat() {
		return this.rat;
	}

	@Override
	public void setSelectedRat(@Nullable TamedRat rat) {
		this.rat = rat;
		this.sendPacket();
	}

	private void sendPacket() {
		if (!this.host.level().isClientSide()) {
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(this.host, new UpdateSelectedRatPacket(this.host, this));
		}
	}
}
