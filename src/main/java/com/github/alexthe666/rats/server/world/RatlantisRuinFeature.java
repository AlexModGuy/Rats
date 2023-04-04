package com.github.alexthe666.rats.server.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RatlantisRuinFeature extends Feature<RatlantisRuinConfiguration> {
	public RatlantisRuinFeature(Codec<RatlantisRuinConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<RatlantisRuinConfiguration> context) {
		RandomSource random = context.random();
		WorldGenLevel level = context.level();
		BlockPos blockpos = context.origin();
		Rotation rotation = Rotation.getRandom(random);
		RatlantisRuinConfiguration config = context.config();

		StructureTemplateManager structuretemplatemanager = level.getLevel().getServer().getStructureManager();
		StructureTemplate structuretemplate = null;

		//shuffle the map. This makes it so the last entries are actually used more often
		List<ResourceLocation> shuffledList = new ArrayList<>(config.ruins().keySet());
		Collections.shuffle(shuffledList);

		for (ResourceLocation entry : shuffledList) {
			if (random.nextFloat() < config.ruins().get(entry)) {
				structuretemplate = structuretemplatemanager.getOrCreate(entry);
				break;
			}
		}
		if (structuretemplate == null) structuretemplate = structuretemplatemanager.getOrCreate(config.defaultRuin());

		ChunkPos chunkpos = new ChunkPos(blockpos);
		BoundingBox boundingbox = new BoundingBox(chunkpos.getMinBlockX() - 16, level.getMinBuildHeight(), chunkpos.getMinBlockZ() - 16, chunkpos.getMaxBlockX() + 16, level.getMaxBuildHeight(), chunkpos.getMaxBlockZ() + 16);
		StructurePlaceSettings structureplacesettings = new StructurePlaceSettings().setRotation(rotation).setBoundingBox(boundingbox).setRandom(random);
		Vec3i vec3i = structuretemplate.getSize(rotation);
		BlockPos blockpos1 = blockpos.offset(-vec3i.getX() / 2, 0, -vec3i.getZ() / 2);
		BlockPos blockpos2 = structuretemplate.getZeroPositionWithTransform(blockpos1, Mirror.NONE, rotation);

		if (!level.getBlockState(blockpos2.below()).isSolidRender(level, blockpos2)) return false;

		structureplacesettings.clearProcessors();
		config.processor().get().list().forEach(structureplacesettings::addProcessor);
		return structuretemplate.placeInWorld(level, blockpos2, blockpos2, structureplacesettings, random, 20);
	}
}
