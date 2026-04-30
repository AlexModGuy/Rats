package com.github.alexthe666.rats.data;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

// Datagen note (1.21): 1.20.1 advancement builder API (FrameType, ContextAwarePredicate.ANY,
// RequirementsStrategy, Consumer<Advancement>, .parent(Advancement), KilledTrigger
// .TriggerInstance.playerKilledEntity(EntityPredicate), etc.) was completely
// reworked into the codec-based AdvancementHolder/Optional<ContextAwarePredicate>
// /Criterion model in 1.20.5+. The pre-generated JSON in
// src/generated/resources/data/rats/advancements/ is what the game actually loads
// at runtime, so this generator is left as an empty stub for now and will be
// rewritten in a focused datagen pass before the next regeneration.
public class RatsAdvancementGenerator implements AdvancementProvider.AdvancementGenerator {

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> consumer, ExistingFileHelper fileHelper) {
        // intentionally empty — see file header
    }
}
