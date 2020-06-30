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
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.function.Function;

public class DutchratShipStructure extends Structure<NoFeatureConfig> {
    public DutchratShipStructure(Codec<NoFeatureConfig> p_i51440_1_) {
        super(p_i51440_1_);
        this.setRegistryName("rats:dutchrat_ship");
    }

    public String getStructureName() {
        return RatsMod.MODID + ":DutchratShip";
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

        public void func_230364_a_(ChunkGenerator p_230364_1_, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome p_230364_5_, IFeatureConfig p_230364_6_) {
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];
            BlockPos blockpos = new BlockPos(chunkX * 16, 90 + rand.nextInt(40), chunkZ * 16);
            DutchratShipPiece.func_204760_a(templateManagerIn, blockpos, rotation, this.components, this.rand);
            this.recalculateStructureSize();
        }
    }

}
