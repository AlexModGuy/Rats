package com.github.alexthe666.rats.data.ratlantis;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

// Datagen note (1.21): see RatsAdvancementGenerator. The Ratlantis advancements were
// generated for the 1.20.1 builder API; pre-generated JSON in
// src/generated/resources/data/rats/advancements/ratlantis/ is the runtime source
// of truth. Will be re-implemented in a focused datagen pass.
public class RatlantisAdvancementGenerator implements AdvancementProvider.AdvancementGenerator {

    @Override
    public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer, ExistingFileHelper helper) {
        // intentionally empty — see file header
    }
}
