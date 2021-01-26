package com.github.alexthe666.rats.server.world.structure;

import com.github.alexthe666.rats.server.world.gen.RatStructure;
import com.github.alexthe666.rats.server.world.gen.RatsStructureProcessor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class RatlantisAquaductPiece {

    public static void func_204760_a(TemplateManager p_204760_0_, BlockPos pos, Rotation rot, List<StructurePiece> components, Random p_204760_4_) {
        components.add(new RatlantisAquaductPiece.Piece(p_204760_0_, RatStructure.LARGE_AQUADUCT.structureLoc, pos, rot, p_204760_4_));
    }

    public static class Piece extends TemplateStructurePiece {
        private final Rotation rotation;
        private final ResourceLocation field_204756_e;
        private final Random random;

        public Piece(TemplateManager p_i48904_1_, ResourceLocation p_i48904_2_, BlockPos p_i48904_3_, Rotation p_i48904_4_, Random random) {
            super(RatlantisStructureRegistry.RAT_AQUADUCT_TYPE, 0);
            this.templatePosition = p_i48904_3_;
            this.rotation = p_i48904_4_;
            this.field_204756_e = p_i48904_2_;
            this.func_204754_a(p_i48904_1_);
            this.template = p_i48904_1_.getTemplate(p_i48904_2_);
            this.random = new Random();
        }

        public Piece(TemplateManager p_i50445_1_, CompoundNBT p_i50445_2_) {
            super(RatlantisStructureRegistry.RAT_AQUADUCT_TYPE, p_i50445_2_);
            this.field_204756_e = new ResourceLocation(p_i50445_2_.getString("Template"));
            this.rotation = Rotation.valueOf(p_i50445_2_.getString("Rot"));
            this.func_204754_a(p_i50445_1_);
            this.template = p_i50445_1_.getTemplate(RatStructure.LARGE_AQUADUCT.structureLoc);
            this.random = new Random();
        }

        protected void readAdditional(CompoundNBT p_143011_1_) {
            super.readAdditional(p_143011_1_);
            p_143011_1_.putString("Template", this.field_204756_e.toString());
            p_143011_1_.putString("Rot", this.rotation.name());
        }

        private void func_204754_a(TemplateManager p_204754_1_) {
            Template lvt_2_1_ = p_204754_1_.getTemplateDefaulted(this.field_204756_e);
            PlacementSettings lvt_3_1_ = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreStructureProcessor.AIR_AND_STRUCTURE_BLOCK);
            this.setup(lvt_2_1_, this.templatePosition, lvt_3_1_);
        }

        protected void handleDataMarker(String p_186175_1_, BlockPos p_186175_2_, IServerWorld p_186175_3_, Random p_186175_4_, MutableBoundingBox p_186175_5_) {
        }


        public boolean func_230383_a_(ISeedReader p_230383_1_, StructureManager p_230383_2_, ChunkGenerator p_230383_3_, Random p_230383_4_, MutableBoundingBox p_230383_5_, ChunkPos p_230383_6_, BlockPos p_230383_7_) {
            this.placeSettings.addProcessor(new RatsStructureProcessor(0.75F + random.nextFloat() * 0.75F));
            BlockPos inital = this.templatePosition.add(this.template.getSize().getX() / 2, 0, this.template.getSize().getZ() / 2);
            int lvt_8_1_ = p_230383_1_.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, inital.getX(), inital.getZ()) - random.nextInt(4);
            BlockPos pos = new BlockPos(inital.getX(), lvt_8_1_, inital.getZ());
            while(p_230383_1_.getBlockState(pos.down()).getMaterial().isLiquid() && pos.getY() > 3){
                pos = pos.down();
            }
            this.templatePosition = new BlockPos(this.templatePosition.getX(), pos.getY() - 3, this.templatePosition.getZ());
            return super.func_230383_a_(p_230383_1_, p_230383_2_, p_230383_3_, p_230383_4_, p_230383_5_, p_230383_6_, p_230383_7_);
        }
    }
}
