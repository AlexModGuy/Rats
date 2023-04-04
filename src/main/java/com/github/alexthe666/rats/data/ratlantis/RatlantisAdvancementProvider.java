package com.github.alexthe666.rats.data.ratlantis;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RatlantisAdvancementProvider extends ForgeAdvancementProvider {

	public RatlantisAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> providers, ExistingFileHelper helper) {
		super(output, providers, helper, List.of(new RatlantisAdvancementGenerator()));
	}
}
