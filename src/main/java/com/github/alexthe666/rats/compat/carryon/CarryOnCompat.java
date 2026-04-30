package com.github.alexthe666.rats.compat.carryon;

import com.github.alexthe666.rats.RatsMod;

// Soft compat with Carry On. Carry On controls pickup-ability via its own data tags
// (carryon:block_blacklist / carryon:entity_blacklist), so this compat layer's job is to register
// behaviour-style hooks that Carry On exposes at runtime — not the tag list itself.
//
// The tag list lives under data/carryon/tags/ in a separate ratlantis_carryon datapack you can
// ship alongside the mod (or that the user can enable manually). Boss rats (RatKing, BlackDeath,
// Dutchrat, etc) should land in carryon:entity_blacklist so a player can't trivially pick up an
// active boss and walk it home.
public final class CarryOnCompat {

	private CarryOnCompat() {}

	public static void init() {
		RatsMod.LOGGER.debug("Rats/CarryOn: integration available; tag-driven blacklisting handled via data/carryon/tags");
	}
}
