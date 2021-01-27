package com.github.alexthe666.rats.server.world.structure;

import com.mojang.serialization.Codec;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class DutchratShipStructure extends Structure<NoFeatureConfig> {
    public DutchratShipStructure(Codec<NoFeatureConfig> p_i51440_1_) {
        super(p_i51440_1_);
        this.setRegistryName("rats:dutchrat_ship");
    }

    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    public int getSize() {
        return 4;
    }

    public Structure.IStartFactory getStartFactory() {
        return DutchratShipStructure.Start::new;
    }

    protected int getSeedModifier() {
        return 123456789;
    }

    public static class Start extends StructureStart {
        public Start(Structure<?> p_i225817_1_, int p_i225817_2_, int p_i225817_3_, MutableBoundingBox p_i225817_4_, int p_i225817_5_, long p_i225817_6_) {
            super(p_i225817_1_, p_i225817_2_, p_i225817_3_, p_i225817_4_, p_i225817_5_, p_i225817_6_);
        }

        @Override
        public void func_230364_a_(DynamicRegistries p_230364_1_, ChunkGenerator p_230364_2_, TemplateManager p_230364_3_, int p_230364_4_, int p_230364_5_, Biome p_230364_6_, IFeatureConfig p_230364_7_) {
            Rotation rotation = Rotation.randomRotation(this.rand);
            BlockPos blockpos = new BlockPos(p_230364_4_ * 16, 90, p_230364_5_ * 16);
            DutchratShipPiece.func_204760_a(p_230364_3_, blockpos, rotation, this.components, this.rand);
            this.recalculateStructureSize();
        }
    }

}
