package com.github.alexthe666.rats.client.model.entity;

import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.rats.server.entity.ratlantis.NeoRatlantean;
import com.google.common.collect.ImmutableList;

public class LaserPortalModel extends NeoRatlanteanModel<NeoRatlantean> {

	@Override
	public Iterable<BasicModelPart> parts() {
		return ImmutableList.of(this.floatyPivot);
	}

}
