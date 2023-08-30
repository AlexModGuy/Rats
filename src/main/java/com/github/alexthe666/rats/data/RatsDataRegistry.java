package com.github.alexthe666.rats.data;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.data.loot.RatsLootTables;
import com.github.alexthe666.rats.data.ratlantis.RatlantisAdvancementProvider;
import com.github.alexthe666.rats.data.ratlantis.RatlantisRecipes;
import com.github.alexthe666.rats.data.ratlantis.RatlantisWorldGenerator;
import com.github.alexthe666.rats.data.ratlantis.loot.RatlantisLootTables;
import com.github.alexthe666.rats.data.ratlantis.tags.RatlantisBlockTags;
import com.github.alexthe666.rats.data.ratlantis.tags.RatlantisEntityTags;
import com.github.alexthe666.rats.data.ratlantis.tags.RatlantisItemTags;
import com.github.alexthe666.rats.data.tags.*;
import com.github.alexthe666.rats.server.misc.RatsLangConstants;
import net.minecraft.DetectedVersion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = RatsMod.MODID)
public class RatsDataRegistry {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = event.getGenerator().getPackOutput();
		CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
		ExistingFileHelper helper = event.getExistingFileHelper();

		generator.addProvider(event.includeClient(), new BlockModelGenerator(output, helper));
		generator.addProvider(event.includeClient(), new ItemModelGenerator(output, helper));
		generator.addProvider(event.includeClient(), new RatsLangGenerator(output));

		generator.addProvider(event.includeServer(), new RatsBannerPatternTags(output, provider, helper));
		generator.addProvider(event.includeServer(), new RatsBiomeTags(output, provider, helper));
		RatsBlockTags tags = new RatsBlockTags(output, provider, helper);
		generator.addProvider(event.includeServer(), tags);
		generator.addProvider(event.includeServer(), new RatsEntityTags(output, provider, helper));
		generator.addProvider(event.includeServer(), new RatsItemTags(output, provider, tags.contentsGetter(), helper));
		generator.addProvider(event.includeServer(), new RatsPoiTags(output, provider, helper));

		generator.addProvider(event.includeServer(), new RatsAdvancementProvider(output, provider, helper));
		generator.addProvider(event.includeServer(), new BiomeModifierGenerator(output, provider));
		generator.addProvider(event.includeServer(), new RatsLootTables(output));
		generator.addProvider(event.includeServer(), new RatsLootModifierGenerator(output));
		generator.addProvider(event.includeServer(), new RatsRecipes(output));
		generator.addProvider(true, new PackMetadataGenerator(output).add(PackMetadataSection.TYPE, new PackMetadataSection(
				Component.translatable(RatsLangConstants.RATS_PACK),
				DetectedVersion.BUILT_IN.getPackVersion(PackType.CLIENT_RESOURCES),
				Arrays.stream(PackType.values()).collect(Collectors.toMap(Function.identity(), DetectedVersion.BUILT_IN::getPackVersion)))));

		DataGenerator.PackGenerator ratlantisPack = generator.getBuiltinDatapack(event.includeServer(), "ratlantis");

		ratlantisPack.addProvider(ratOutput -> new RatlantisAdvancementProvider(ratOutput, provider, helper));
		ratlantisPack.addProvider(ratOutput -> RatlantisWorldGenerator.addProviders(ratlantisPack, ratOutput, provider, helper));
		TagsProvider<Block> ratlantisBlockTags = ratlantisPack.addProvider(ratOutput -> new RatlantisBlockTags(ratOutput, provider, helper));
		ratlantisPack.addProvider(ratOutput -> new RatlantisEntityTags(ratOutput, provider, helper));
		ratlantisPack.addProvider(ratOutput -> new RatlantisItemTags(ratOutput, provider, ratlantisBlockTags.contentsGetter(), helper));
		ratlantisPack.addProvider(RatlantisLootTables::new);
		ratlantisPack.addProvider(RatlantisRecipes::new);
		ratlantisPack.addProvider(ratOutput -> new PackMetadataGenerator(ratOutput).add(PackMetadataSection.TYPE, new PackMetadataSection(
				Component.translatable(RatsLangConstants.RATLANTIS_PACK),
				DetectedVersion.BUILT_IN.getPackVersion(PackType.CLIENT_RESOURCES),
				Arrays.stream(PackType.values()).collect(Collectors.toMap(Function.identity(), DetectedVersion.BUILT_IN::getPackVersion)))));
	}
}
