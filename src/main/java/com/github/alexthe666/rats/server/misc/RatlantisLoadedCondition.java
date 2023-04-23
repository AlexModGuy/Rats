package com.github.alexthe666.rats.server.misc;

import com.github.alexthe666.rats.RatsMod;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class RatlantisLoadedCondition implements ICondition {
	@Override
	public ResourceLocation getID() {
		return new ResourceLocation(RatsMod.MODID, "ratlantis_loaded_or_simulated");
	}

	@Override
	public boolean test(IContext context) {
		return RatsMod.RATLANTIS_DATAPACK_ENABLED;
	}

	public static class Serializer implements IConditionSerializer<RatlantisLoadedCondition> {
		@Override
		public void write(JsonObject json, RatlantisLoadedCondition value) {
		}

		@Override
		public RatlantisLoadedCondition read(JsonObject json) {
			return new RatlantisLoadedCondition();
		}

		@Override
		public ResourceLocation getID() {
			return new ResourceLocation(RatsMod.MODID, "ratlantis_loaded_or_simulated");
		}
	}
}
