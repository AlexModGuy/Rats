package com.github.alexthe666.rats.server.world.structure;

import com.github.alexthe666.rats.server.world.gen.RatsStructureProcessor;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;

public class RatlantisStructureRegistry {
    public static final IStructurePieceType RAT_RUINS_TYPE =  Registry.register(Registry.STRUCTURE_PIECE, "rats:ratlantis_ruins", RatlantisRuinsPiece.Piece::new);
    public static final IStructureProcessorType RAT_RUINS_PROCESSOR = Registry.register(Registry.STRUCTURE_PROCESSOR, "ratlantis_ruins", RatsStructureProcessor::new);

}
