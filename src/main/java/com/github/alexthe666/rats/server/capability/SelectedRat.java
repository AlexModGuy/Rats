package com.github.alexthe666.rats.server.capability;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public interface SelectedRat {

	ResourceLocation ID = new ResourceLocation(RatsMod.MODID, "cap_selected_rat");

	void setPlayer(Player player);

	void clearSelectedRat();

	TamedRat getSelectedRat();

	void setSelectedRat(TamedRat rat);
}
