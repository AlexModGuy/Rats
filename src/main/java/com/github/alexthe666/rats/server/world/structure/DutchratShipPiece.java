package com.github.alexthe666.rats.server.world.structure;

import com.github.alexthe666.rats.server.world.gen.RatsDutchratShipProcessor;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.StructureMode;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class DutchratShipPiece {
    private static final BlockPos STRUCTURE_OFFSET = new BlockPos(0, 0, 0);
    private static final ResourceLocation PART_1 = new ResourceLocation("rats:flying_dutchrat_1");
    private static final ResourceLocation PART_2 = new ResourceLocation("rats:flying_dutchrat_2");

    public static void func_204760_a(TemplateManager p_204760_0_, BlockPos p_204760_1_, Rotation p_204760_2_, List<StructurePiece> p_204760_3_, Random p_204760_4_) {
        p_204760_3_.add(new DutchratShipPiece.Piece(p_204760_0_, PART_1, p_204760_1_, p_204760_2_, p_204760_4_));
    }

    public static class Piece extends TemplateStructurePiece {
        private final Rotation rotation;
        private final ResourceLocation field_204756_e;
        private final Random random;
        private final TemplateManager manager;


        public Piece(TemplateManager p_i48904_1_, ResourceLocation p_i48904_2_, BlockPos p_i48904_3_, Rotation p_i48904_4_, Random random) {
            super(RatlantisStructureRegistry.FLYING_DUTCHRAT_TYPE, 0);
            this.templatePosition = p_i48904_3_;
            this.rotation = p_i48904_4_;
            this.field_204756_e = p_i48904_2_;
            this.func_204754_a(p_i48904_1_);
            this.template = p_i48904_1_.getTemplate(p_i48904_2_);
            this.random = new Random();
            this.manager = p_i48904_1_;
        }

        public Piece(TemplateManager p_i50445_1_, CompoundNBT p_i50445_2_) {
            super(RatlantisStructureRegistry.FLYING_DUTCHRAT_TYPE, p_i50445_2_);
            this.field_204756_e = new ResourceLocation(p_i50445_2_.getString("Template"));
            this.rotation = Rotation.valueOf(p_i50445_2_.getString("Rot"));
            this.func_204754_a(p_i50445_1_);
            this.template = p_i50445_1_.getTemplate(PART_1);
            this.random = new Random();
            this.manager = p_i50445_1_;
        }

        protected void readAdditional(CompoundNBT p_143011_1_) {
            super.readAdditional(p_143011_1_);
            p_143011_1_.putString("Template", this.field_204756_e.toString());
            p_143011_1_.putString("Rot", this.rotation.name());
        }

        private void func_204754_a(TemplateManager p_204754_1_) {
            Template lvt_2_1_ = p_204754_1_.getTemplateDefaulted(this.field_204756_e);
            PlacementSettings lvt_3_1_ = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreStructureProcessor.AIR_AND_STRUCTURE_BLOCK).addProcessor(new RatsDutchratShipProcessor());
            this.setup(lvt_2_1_, this.templatePosition, lvt_3_1_);
        }

        protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb) {
            if(function.equals("load_second_half")){
                PlacementSettings settings = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreStructureProcessor.AIR_AND_STRUCTURE_BLOCK).addProcessor(new RatsDutchratShipProcessor());
                Template secondTemplate = manager.getTemplateDefaulted(PART_2);
                if(secondTemplate.func_237146_a_((IServerWorld) worldIn, pos, pos, settings, rand, 2)){
                    for(Template.BlockInfo template$blockinfo : secondTemplate.func_215381_a(pos, settings, Blocks.STRUCTURE_BLOCK)) {
                        if (template$blockinfo.nbt != null) {
                            StructureMode structuremode = StructureMode.valueOf(template$blockinfo.nbt.getString("mode"));
                            if (structuremode == StructureMode.DATA) {
                                this.handleDataMarker(template$blockinfo.nbt.getString("metadata"), template$blockinfo.pos, worldIn, random, getBoundingBox());
                            }
                        }
                    }
                }
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
            }
        }

        public boolean func_230383_a_(ISeedReader p_230383_1_, StructureManager p_230383_2_, ChunkGenerator p_230383_3_, Random p_230383_4_, MutableBoundingBox p_230383_5_, ChunkPos p_230383_6_, BlockPos p_230383_7_) {
            return super.func_230383_a_(p_230383_1_, p_230383_2_, p_230383_3_, p_230383_4_, p_230383_5_, p_230383_6_, p_230383_7_);

        }
    }
}

