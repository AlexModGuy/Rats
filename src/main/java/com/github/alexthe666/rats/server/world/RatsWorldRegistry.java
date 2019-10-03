package com.github.alexthe666.rats.server.world;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.world.gen.StructureRatRoadPieces;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.DimensionManager;

public class RatsWorldRegistry {

    public static DimensionType RATLANTIS_DIM;
    public static Biome RATLANTIS_BIOME = new BiomeRatlantis();

    public static void register() {
        if (!RatsMod.CONFIG_OPTIONS.disableRatlantis) {
            RATLANTIS_DIM = DimensionType.register("Ratlantis", "_ratlantis", RatsMod.CONFIG_OPTIONS.ratlantisDimensionId, WorldProviderRatlantis.class, false);
            DimensionManager.registerDimension(RatsMod.CONFIG_OPTIONS.ratlantisDimensionId, RATLANTIS_DIM);
        }
        StructureRatRoadPieces.registerVillagePieces();
    }
}
