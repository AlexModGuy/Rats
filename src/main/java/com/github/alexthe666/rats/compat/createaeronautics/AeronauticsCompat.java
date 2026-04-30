package com.github.alexthe666.rats.compat.createaeronautics;

import com.github.alexthe666.rats.RatsMod;

// Soft compat with Create: Aeronautics. The integration is intentionally minimal because
// Aeronautics rides on Create's contraption framework — anything we did for Create
// AbstractContraptionEntity already covers airships. This file exists so future hooks (e.g. a
// "ratlantean automaton" airship-helmsman or pirat ship-crew behaviour) have a stable home.
public final class AeronauticsCompat {

	private static final String AIRSHIP_CONTRAPTION = "com.simibubi.create.aeronautics.contraption.AirshipContraption";
	private static final String AIRSHIP_CONTRAPTION_ALT = "com.simibubi.create_aeronautics.content.contraptions.AirshipContraption";

	private AeronauticsCompat() {}

	public static void init() {
		if (!hasAirshipContraption()) {
			RatsMod.LOGGER.debug("Rats/Aeronautics: AirshipContraption not on classpath; skipping airship hooks");
			return;
		}
		RatsMod.LOGGER.debug("Rats/Aeronautics: airship integration ready (rats persist on flying contraptions)");
	}

	private static boolean hasAirshipContraption() {
		ClassLoader cl = AeronauticsCompat.class.getClassLoader();
		for (String name : new String[]{AIRSHIP_CONTRAPTION, AIRSHIP_CONTRAPTION_ALT}) {
			try {
				Class.forName(name, false, cl);
				return true;
			} catch (ClassNotFoundException ignored) {
			}
		}
		return false;
	}
}
