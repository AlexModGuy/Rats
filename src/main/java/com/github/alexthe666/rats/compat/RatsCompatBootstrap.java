package com.github.alexthe666.rats.compat;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.compat.carryon.CarryOnCompat;
import com.github.alexthe666.rats.compat.create.CreateCompat;
import com.github.alexthe666.rats.compat.createaeronautics.AeronauticsCompat;
import net.neoforged.fml.ModList;

// Central entry point that runs every soft-dep compat layer at FMLCommonSetupEvent. Each compat
// layer below is wrapped in a ModList.isLoaded() check so the mod still loads cleanly when the
// integrating mod is absent. Jade and Carry On integrate via their own discoverable hooks
// (@WailaPlugin annotation and data tags respectively) so they don't need an entry here.
public final class RatsCompatBootstrap {

	private RatsCompatBootstrap() {}

	public static void init() {
		if (ModList.get().isLoaded("create")) {
			tryInit("Create", CreateCompat::init);
		}
		if (ModList.get().isLoaded("create_aeronautics")) {
			tryInit("Create: Aeronautics", AeronauticsCompat::init);
		}
		if (ModList.get().isLoaded("carryon")) {
			tryInit("Carry On", CarryOnCompat::init);
		}
	}

	// Each compat layer relies on reflection / class-load checks against an optional mod. If the
	// integrated mod ships a refactor that breaks our integration we want a clear log line, not a
	// crash that takes the whole mod down.
	private static void tryInit(String name, Runnable initializer) {
		try {
			initializer.run();
			RatsMod.LOGGER.info("Rats compat: {} integration enabled", name);
		} catch (Throwable t) {
			RatsMod.LOGGER.warn("Rats compat: {} integration failed to initialise; rats will still work standalone", name, t);
		}
	}
}
