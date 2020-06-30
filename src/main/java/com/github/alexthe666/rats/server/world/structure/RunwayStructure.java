package com.github.alexthe666.rats.server.world.structure;

import com.github.alexthe666.rats.RatsMod;
import com.mojang.serialization.Codec;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.function.Function;

public class RunwayStructure extends Structure<NoFeatureConfig> {


    public RunwayStructure(Codec<NoFeatureConfig> p_i231997_1_) {
        super(p_i231997_1_);
        this.setRegistryName("rats:runway");
    }

    public String getStructureName() {
        return RatsMod.MODID + ":Runway";
    }

    public int getSize() {
        return 4;
    }

    public IStartFactory getStartFactory() {
        return RunwayStructure.Start::new;
    }

    protected int getSeedModifier() {
        return 945328146;
    }

    public static class Start extends StructureStart {
        public Start(Structure<?> p_i225817_1_, int p_i225817_2_, int p_i225817_3_, MutableBoundingBox p_i225817_4_, int p_i225817_5_, long p_i225817_6_) {
            super(p_i225817_1_, p_i225817_2_, p_i225817_3_, p_i225817_4_, p_i225817_5_, p_i225817_6_);
        }

        public void func_230364_a_(ChunkGenerator p_230364_1_, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome p_230364_5_, IFeatureConfig p_230364_6_) {
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];
            BlockPos blockpos = new BlockPos(chunkX * 16, 64, chunkZ * 16);
            RunwayPiece.func_204760_a(templateManagerIn, blockpos, rotation, this.components, this.rand);
            this.recalculateStructureSize();
        }
    }

}
