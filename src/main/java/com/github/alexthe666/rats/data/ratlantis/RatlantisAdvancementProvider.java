package com.github.alexthe666.rats.data.ratlantis;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RatlantisAdvancementProvider extends AdvancementProvider {

	public RatlantisAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> providers, ExistingFileHelper helper) {
		super(output, providers, helper, List.of(new RatlantisAdvancementGenerator()));
	}
}
