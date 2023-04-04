package com.github.alexthe666.rats.server.entity;

public interface RatSummoner {

	boolean encirclesSummoner();

	boolean reabsorbRats();

	float reabsorbedRatHealAmount();

	int getRatsSummoned();

	void setRatsSummoned(int i);

	default float getRadius() {
		return 5;
	}
}
