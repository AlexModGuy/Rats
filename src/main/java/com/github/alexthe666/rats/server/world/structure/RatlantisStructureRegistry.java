package com.github.alexthe666.rats.server.world.structure;

import com.github.alexthe666.rats.server.world.gen.FeatureMarblePile;
import com.github.alexthe666.rats.server.world.gen.RatsStructureProcessor;
import com.mojang.serialization.Codec;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;

public class RatlantisStructureRegistry {

    public static IStructurePieceType RAT_RUINS_TYPE;
    public static IStructurePieceType RAT_AQUADUCT_TYPE;
    public static IStructurePieceType FLYING_DUTCHRAT_TYPE;
    public static IStructurePieceType RUNWAY_TYPE;
    public static IStructureProcessorType RAT_RUINS_PROCESSOR;
    public static Feature<NoFeatureConfig> MARBLE_PILE;
    public static Feature<NoFeatureConfig> SEWER;

    public static void register(){
        RAT_RUINS_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "rats:ratlantis_ruins", RatlantisRuinsPiece.Piece::new);
        RAT_AQUADUCT_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "rats:ratlantis_aquaduct", RatlantisAquaductPiece.Piece::new);
        FLYING_DUTCHRAT_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "rats:flying_dutchrat", DutchratShipPiece.Piece::new);
        RUNWAY_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "rats:runway", RunwayPiece.Piece::new);

        MARBLE_PILE = Registry.register(Registry.FEATURE, "marble_pile", new FeatureMarblePile(NoFeatureConfig.field_236558_a_));
    }


}
