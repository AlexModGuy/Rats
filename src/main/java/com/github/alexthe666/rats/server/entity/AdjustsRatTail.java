package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;

/**
 * Implement this interface to directly manipulate a rat's tail when they ride this entity.
 */
public interface AdjustsRatTail {

	void adjustRatTailRotation(AbstractRat rat, AdvancedModelBox upperTail, AdvancedModelBox lowerTail);

	//same method as StaticRatModel.progressRotation
	default void progressRotation(AdvancedModelBox model, float progress, float rotX, float rotY, float rotZ, float divisor) {
		model.rotateAngleX += progress * (rotX - model.defaultRotationX) / divisor;
		model.rotateAngleY += progress * (rotY - model.defaultRotationY) / divisor;
		model.rotateAngleZ += progress * (rotZ - model.defaultRotationZ) / divisor;
	}
}
