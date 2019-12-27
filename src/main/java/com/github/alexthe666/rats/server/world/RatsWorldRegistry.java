package com.github.alexthe666.rats.server.world;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.server.world.gen.StructureRatRoadPieces;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class RatsWorldRegistry {

    public static DimensionType RATLANTIS_DIM;
    public static Biome RATLANTIS_BIOME = new BiomeRatlantis();

    public static void register() {
        if (!RatConfig.disableRatlantis) {
            RATLANTIS_DIM = DimensionType.register("Ratlantis", "_ratlantis", RatConfig.ratlantisDimensionId, WorldProviderRatlantis.class, false);
            DimensionManager.registerDimension(RatConfig.ratlantisDimensionId, RATLANTIS_DIM);
        }
        StructureRatRoadPieces.registerVillagePieces();
    }
}
