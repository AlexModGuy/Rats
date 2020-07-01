package com.github.alexthe666.rats.server.world.gen;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.ChestBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import java.util.Random;

public class RatsDutchratShipProcessor extends StructureProcessor {
    public static final ResourceLocation LOOT = new ResourceLocation(RatsMod.MODID, "chest/dutchrat_ship");

    public Template.BlockInfo process(IWorldReader worldIn, BlockPos pos, Template.BlockInfo blockInfoIn, Template.BlockInfo blockInfoIn2, PlacementSettings settings) {
        if (blockInfoIn2.state.getBlock() instanceof ChestBlock) {
            Random rand = settings.getRandom(blockInfoIn2.pos);
            ResourceLocation loot = LOOT;
            CompoundNBT tag = blockInfoIn2.nbt == null ? new CompoundNBT() : blockInfoIn2.nbt;
            tag.putString("LootTable", loot.toString());
            tag.putLong("LootTableSeed", rand.nextLong());
            Template.BlockInfo newInfo = new Template.BlockInfo(blockInfoIn2.pos, blockInfoIn2.state, tag);
            return newInfo;
        }else if (blockInfoIn2.state.getBlock() instanceof BarrelBlock) {
            Random rand = settings.getRandom(blockInfoIn2.pos);
            ResourceLocation loot = LOOT;
            CompoundNBT tag = blockInfoIn.nbt == null ? new CompoundNBT() : blockInfoIn2.nbt;
            tag.putString("LootTable", loot.toString());
            tag.putLong("LootTableSeed", rand.nextLong());
            Template.BlockInfo newInfo = new Template.BlockInfo(blockInfoIn2.pos, blockInfoIn2.state, tag);
            return newInfo;
        }else{
            return blockInfoIn2;
        }
    }

    @Override
    protected IStructureProcessorType getType() {
        return IStructureProcessorType.BLOCK_ROT;
    }

}
