package com.github.alexthe666.rats.server.world;

import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CaveWorldCarver;
import com.mojang.serialization.Codec;

import java.util.Set;

public class RatsCaveCarver extends CaveWorldCarver {
	public RatsCaveCarver(Codec<CaveCarverConfiguration> codec) {
		super(codec);
		this.liquids = Set.of();
	}
}







