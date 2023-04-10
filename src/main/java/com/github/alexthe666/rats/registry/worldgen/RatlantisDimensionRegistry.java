package com.github.alexthe666.rats.registry.worldgen;

import com.github.alexthe666.rats.RatsMod;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;
import java.util.OptionalLong;

public class RatlantisDimensionRegistry {
	public static final ResourceLocation DIMENSION = new ResourceLocation(RatsMod.MODID, "ratlantis");
	public static final ResourceKey<Level> DIMENSION_KEY = ResourceKey.create(Registries.DIMENSION, DIMENSION);

	public static final ResourceKey<ConfiguredWorldCarver<?>> RATLANTIS_CAVES = ResourceKey.create(Registries.CONFIGURED_CARVER, new ResourceLocation(RatsMod.MODID, "ratlantis_caves"));
	public static final ResourceKey<NoiseGeneratorSettings> RATLANTIS_NOISE_GEN = ResourceKey.create(Registries.NOISE_SETTINGS, new ResourceLocation(RatsMod.MODID, "ratlantis_noise_gen"));
	public static final ResourceKey<DimensionType> RATLANTIS_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, new ResourceLocation(RatsMod.MODID, "ratlantis_type"));
	public static final ResourceKey<LevelStem> RATLANTIS_LEVEL_STEM = ResourceKey.create(Registries.LEVEL_STEM, DIMENSION);

	private static DimensionType ratlantisType() {
		return new DimensionType(
				OptionalLong.empty(),
				true,
				false,
				false,
				true,
				1.0D,
				true,
				false,
				0,
				256,
				256,
				BlockTags.INFINIBURN_OVERWORLD,
				new ResourceLocation("overworld"),
				0.0F,
				new DimensionType.MonsterSettings(false, false, UniformInt.of(0, 7), 7)
		);
	}

	public static NoiseGeneratorSettings ratlantisNoise(HolderGetter<DensityFunction> functions, HolderGetter<NormalNoise.NoiseParameters> noises) {
		DensityFunction densityfunction = NoiseRouterData.getFunction(functions, NoiseRouterData.SHIFT_X);
		DensityFunction densityfunction1 = NoiseRouterData.getFunction(functions, NoiseRouterData.SHIFT_Z);
		return new NoiseGeneratorSettings(
				NoiseSettings.create(0, 256, 1, 2),
				Blocks.STONE.defaultBlockState(),
				Blocks.WATER.defaultBlockState(),
				new NoiseRouter(
						DensityFunctions.zero(), //barrier
						DensityFunctions.zero(), //fluid level floodedness
						DensityFunctions.zero(), //fluid level spread
						DensityFunctions.zero(), //lava
						DensityFunctions.shiftedNoise2d(densityfunction, densityfunction1, 0.25D, noises.getOrThrow(Noises.TEMPERATURE)), //temperature
						DensityFunctions.shiftedNoise2d(densityfunction, densityfunction1, 0.25D, noises.getOrThrow(Noises.VEGETATION)), //vegetation
						DensityFunctions.zero(), //continents
						DensityFunctions.cache2d(DensityFunctions.endIslands(0L)), //erosion
						DensityFunctions.zero(), //depth
						DensityFunctions.zero(), //ridges
						DensityFunctions.add(
								DensityFunctions.constant(-0.234275D),
								DensityFunctions.mul(
										DensityFunctions.yClampedGradient(16, 32, 0.0D, 1.0D),
										DensityFunctions.add(
												DensityFunctions.constant(0.234375D),
												DensityFunctions.add(
														DensityFunctions.constant(-23.4375D),
														DensityFunctions.mul(
																DensityFunctions.yClampedGradient(0, 312, 1.0D, 0.0D),
																DensityFunctions.add(
																		DensityFunctions.constant(23.4375D),
																		DensityFunctions.add(
																				DensityFunctions.constant(-0.703125D),
																				DensityFunctions.cache2d(DensityFunctions.endIslands(0L))
																		)
																)
														)
												))
								)
						), //initial density
						DensityFunctions.mul(
								DensityFunctions.constant(0.64D),
								DensityFunctions.interpolated(
										DensityFunctions.blendDensity(
												DensityFunctions.add(
														DensityFunctions.constant(0.554375D),
														DensityFunctions.mul(
																DensityFunctions.yClampedGradient(0, 87, 0.25D, 3.6D),
																DensityFunctions.add(
																		DensityFunctions.constant(1.234375D),
																		DensityFunctions.add(
																				DensityFunctions.constant(-2.75D),
																				DensityFunctions.mul(
																						DensityFunctions.yClampedGradient(0, 175, 1.0D, 0.0D),
																						DensityFunctions.add(
																								DensityFunctions.constant(1.75D),
																								new DensityFunctions.HolderHolder(functions.getOrThrow(NoiseRouterData.SLOPED_CHEESE_AMPLIFIED))
																						)
																				)
																		)
																)
														)
												)
										)
								)
						).squeeze(), //final density
						DensityFunctions.zero(), //vein toggle
						DensityFunctions.zero(), //vein ridged
						DensityFunctions.zero() //vein gap
				),
				createSurfaceRules(),
				List.of(),
				63,
				false,
				true,
				false,
				false
		);
	}

	public static void bootstrapNoise(BootstapContext<NoiseGeneratorSettings> context) {
		context.register(RATLANTIS_NOISE_GEN, ratlantisNoise(context.lookup(Registries.DENSITY_FUNCTION), context.lookup(Registries.NOISE)));
	}

	public static void bootstrapType(BootstapContext<DimensionType> context) {
		context.register(RATLANTIS_DIM_TYPE, ratlantisType());
	}

	public static void bootstrapCarver(BootstapContext<ConfiguredWorldCarver<?>> context) {
		context.register(RATLANTIS_CAVES, RatlantisFeatureRegistry.RATLANTIS_CAVES.get().configured(new CaveCarverConfiguration(
				0.15F,
				UniformHeight.of(VerticalAnchor.aboveBottom(8), VerticalAnchor.absolute(180)),
				UniformFloat.of(0.1F, 0.9F),
				VerticalAnchor.bottom(), //no lava
				CarverDebugSettings.of(false, Blocks.CRIMSON_BUTTON.defaultBlockState()),
				context.lookup(Registries.BLOCK).getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES),
				UniformFloat.of(0.7F, 1.4F),
				UniformFloat.of(0.8F, 1.3F),
				UniformFloat.of(-1.0F, -0.4F))));
	}

	public static void bootstrapLevelStem(BootstapContext<LevelStem> context) {
		HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
		HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
		HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);
		context.register(RATLANTIS_LEVEL_STEM, new LevelStem(dimTypes.getOrThrow(RatlantisDimensionRegistry.RATLANTIS_DIM_TYPE), new NoiseBasedChunkGenerator(RatlantisBiomeRegistry.buildBiomeSource(biomeRegistry), noiseGenSettings.getOrThrow(RatlantisDimensionRegistry.RATLANTIS_NOISE_GEN))));
	}

	private static final SurfaceRules.RuleSource BEDROCK = makeStateRule(Blocks.BEDROCK);
	private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
	private static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
	private static final SurfaceRules.RuleSource SAND = makeStateRule(Blocks.SAND);
	private static final SurfaceRules.RuleSource SANDSTONE = makeStateRule(Blocks.SANDSTONE);

	private static SurfaceRules.RuleSource makeStateRule(Block block) {
		return SurfaceRules.state(block.defaultBlockState());
	}

	private static SurfaceRules.RuleSource createSurfaceRules() {
		SurfaceRules.RuleSource overworldLike = SurfaceRules.sequence(
				SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
						SurfaceRules.sequence(
								SurfaceRules.sequence(
										SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, SANDSTONE),
										SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(-1, 0), GRASS_BLOCK), SAND),
								SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(-1, 0),
										SurfaceRules.sequence(GRASS_BLOCK)))),
				SurfaceRules.ifTrue(SurfaceRules.waterStartCheck(-6, -1),
						SurfaceRules.sequence(
								SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, DIRT))));

		ImmutableList.Builder<SurfaceRules.RuleSource> builder = ImmutableList.builder();

		builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK));
		builder.add(overworldLike);
		return SurfaceRules.sequence(builder.build().toArray(SurfaceRules.RuleSource[]::new));
	}
}
