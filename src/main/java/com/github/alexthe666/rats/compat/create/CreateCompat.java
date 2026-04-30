package com.github.alexthe666.rats.compat.create;

import com.github.alexthe666.rats.RatsMod;

// Soft compat with Create. Most of our cross-mod usefulness is already free: rats and rat blocks
// expose standard NeoForge IItemHandler / IFluidHandler capabilities, so Create's funnels, belts,
// chutes and depots already pull from / push to rat tubes and rat-cage inventories without any
// further glue.
//
// What this class is for:
// 1. Watch for Create being present and run integration that is *not* purely capability-driven:
//    e.g. tagging behaviours, registering recipe-display extensions, or wiring contraption-safe
//    behaviours for tamed rats riding moving Create assemblies.
// 2. Use reflection / class-load probes against Create's classes so we don't need to add Create
//    as a hard compileOnly dep. If Create's package layout shifts in a future release, the failure
//    is a logged warning here instead of a hard load crash.
public final class CreateCompat {

	private static final String CONTRAPTION_ENTITY = "com.simibubi.create.content.contraptions.AbstractContraptionEntity";

	private CreateCompat() {}

	public static void init() {
		if (!hasContraptionEntity()) {
			RatsMod.LOGGER.debug("Rats/Create: AbstractContraptionEntity not on classpath; skipping contraption hooks (Create may have moved its packages)");
			return;
		}
		// Future hooks (rat-on-contraption survival, depot pickup-target prioritisation, etc) plug
		// in here. The init path is currently no-op because rats already work with Create via the
		// standard NeoForge capability surface.
		RatsMod.LOGGER.debug("Rats/Create: contraption class detected; rat-on-contraption ride is allowed");
	}

	private static boolean hasContraptionEntity() {
		try {
			Class.forName(CONTRAPTION_ENTITY, false, CreateCompat.class.getClassLoader());
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
}
