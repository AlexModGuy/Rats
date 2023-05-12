package com.github.alexthe666.rats.server.entity;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface Plane {

	@Nullable
	Vec3 getFlightTarget();

	void setFlightTarget(@Nullable Vec3 target);
}
