package com.github.alexthe666.rats.client.model;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class RatsModelLayers {

	public static final ModelLayerLocation BLACK_DEATH = register("black_death");
	public static final ModelLayerLocation PIPER = register("pied_piper");
	public static final ModelLayerLocation PIRAT_BOAT = register("pirat_boat");
	public static final ModelLayerLocation PLAGUE_DOCTOR = register("plague_doctor");
	public static final ModelLayerLocation THROWN_BLOCK = register("thrown_block");

	public static final ModelLayerLocation HAMMOCK = register("hammock");
	public static final ModelLayerLocation IGLOO = register("igloo");
	public static final ModelLayerLocation SEED_BOWL = register("seed_bowl");
	public static final ModelLayerLocation WATER_BOTTLE = register("water_bottle");

	public static final ModelLayerLocation CHEF_TOQUE = register("chef_toque");
	public static final ModelLayerLocation PIPER_HAT = register("piper_hat");
	public static final ModelLayerLocation ARCHEOLOGIST_HAT = register("archeologist_hat");
	public static final ModelLayerLocation FARMER_HAT = register("farmer_hat");
	public static final ModelLayerLocation FEZ = register("fez");
	public static final ModelLayerLocation TOP_HAT = register("top_hat");
	public static final ModelLayerLocation SANTA_HAT = register("santa_hat");
	public static final ModelLayerLocation HALO = register("halo");
	public static final ModelLayerLocation PARTY_HAT = register("party_hat");
	public static final ModelLayerLocation PIRATE_HAT = register("pirate_hat");
	public static final ModelLayerLocation CROWN = register("crown");
	public static final ModelLayerLocation PLAGUE_DOCTOR_MASK = register("plague_doctor_mask");
	public static final ModelLayerLocation EXTERMINATOR_HAT = register("exterminator_hat");
	public static final ModelLayerLocation AVIATOR_HAT = register("aviator_hat");
	public static final ModelLayerLocation OFFICER_HAT = register("officer_hat");

	public static final ModelLayerLocation RATLANTIS_ARMOR_INNER = register("ratlantis_armor", "inner");
	public static final ModelLayerLocation RATLANTIS_ARMOR_OUTER = register("ratlantis_armor", "outer");

	private static ModelLayerLocation register(String name, String type) {
		return new ModelLayerLocation(new ResourceLocation(RatsMod.MODID, name), type);
	}

	private static ModelLayerLocation register(String name) {
		return new ModelLayerLocation(new ResourceLocation(RatsMod.MODID, name), "main");
	}

}
