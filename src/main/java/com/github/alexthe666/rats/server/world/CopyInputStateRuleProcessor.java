package com.github.alexthe666.rats.server.world;

import com.github.alexthe666.rats.registry.worldgen.RatlantisFeatureRegistry;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CopyInputStateRuleProcessor extends StructureProcessor {

	public static final Codec<CopyInputStateRuleProcessor> CODEC = ProcessorRule.CODEC.listOf().fieldOf("rules").xmap(CopyInputStateRuleProcessor::new, config -> config.rules).codec();

	private final ImmutableList<ProcessorRule> rules;

	public CopyInputStateRuleProcessor(List<? extends ProcessorRule> list) {
		this.rules = ImmutableList.copyOf(list);
	}

	@Override
	public StructureTemplate.StructureBlockInfo process(LevelReader reader, BlockPos oldPos, BlockPos newPos, StructureTemplate.StructureBlockInfo oldInfo, StructureTemplate.StructureBlockInfo newInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
		RandomSource randomsource = RandomSource.create(Mth.getSeed(newInfo.pos()));
		BlockState blockstate = reader.getBlockState(newInfo.pos());

		for (ProcessorRule processorrule : this.rules) {
			if (processorrule.test(newInfo.state(), blockstate, oldInfo.pos(), newInfo.pos(), newPos, randomsource)) {
				return new StructureTemplate.StructureBlockInfo(newInfo.pos(), processorrule.getOutputState().getBlock().withPropertiesOf(oldInfo.state()), processorrule.getOutputTag(randomsource, newInfo.nbt()));
			}
		}

		return newInfo;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return RatlantisFeatureRegistry.COPY_STATE.get();
	}
}
