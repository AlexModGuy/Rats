package com.github.alexthe666.rats.server.world.gen;

import com.github.alexthe666.rats.server.world.village.WorldGenPlagueDoctor;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.block.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

import java.util.Random;

public class RatsPlagueHutProcessor extends StructureProcessor {

    private Biome biome;
    private BlockPos blockPos;
    private PlacementSettings settings;
    private WorldGenPlagueDoctor hut;
    private int seed;

    public RatsPlagueHutProcessor(BlockPos pos, WorldGenPlagueDoctor worldGenPetShop, PlacementSettings settings, Biome biome) {
        this.biome = biome;
        this.blockPos = pos;
        this.settings = settings;
        this.hut = worldGenPetShop;
    }

    public Template.BlockInfo process(IWorldReader worldIn, BlockPos pos, Template.BlockInfo blockInfoIn, Template.BlockInfo blockInfoIn2, PlacementSettings settings) {
        if (blockInfoIn.state.getBlock() instanceof ChestBlock) {
            ResourceLocation loot = WorldGenPlagueDoctor.LOOT;
            Random rand = new Random(seed + pos.toLong());
            CompoundNBT tag = blockInfoIn.nbt == null ? new CompoundNBT() : blockInfoIn.nbt;
            tag.putString("LootTable", loot.toString());
            tag.putLong("LootTableSeed", rand.nextLong());
            Template.BlockInfo newInfo = new Template.BlockInfo(pos, Blocks.CHEST.getDefaultState(), tag);
            return newInfo;
        } else if (blockInfoIn.state.getBlock() == Blocks.DIAMOND_BLOCK) {//spawn ocelot
            if (hut != null) {
                hut.villagerPos = pos;
            }
            //worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
            return new Template.BlockInfo(pos, Blocks.AIR.getDefaultState(), null);
        }
        {
            BlockState state = getBiomeSpecificBlockState(blockInfoIn.state, biome);
            if (state != blockInfoIn.state) {
                return new Template.BlockInfo(pos, state, null);
            } else {
                return blockInfoIn;
            }
        }
    }

    @Override
    protected IStructureProcessorType getType() {
        return IStructureProcessorType.BLOCK_ROT;
    }

    @Override
    protected <T> Dynamic<T> serialize0(DynamicOps<T> ops) {
        return new Dynamic<>(ops, ops.mergeInto(this.serialize0(ops).getValue(), ops.createString("rats_plague_hut_processor"), ops.createString(Registry.STRUCTURE_PROCESSOR.getKey(this.getType()).toString())));
    }

    @SuppressWarnings("deprecation")
    protected BlockState getBiomeSpecificBlockState(BlockState blockstateIn, Biome biome) {
        net.minecraftforge.event.terraingen.BiomeEvent.GetVillageBlockID event = new net.minecraftforge.event.terraingen.BiomeEvent.GetVillageBlockID(biome, blockstateIn);
        MinecraftForge.EVENT_BUS.post(event);
        int structureType = 0;
        if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.DRY)) {
            structureType = 1;
        } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA)) {
            structureType = 2;
        } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.CONIFEROUS)) {
            structureType = 3;
        }

        if (event.getResult() == Event.Result.DENY)
            return event.getReplacement();
        if (blockstateIn.getBlock() instanceof DoorBlock && blockstateIn.get(DoorBlock.HALF) == DoubleBlockHalf.LOWER) {
            Direction facing = blockstateIn.get(DoorBlock.FACING);
            return biomeDoor(structureType).with(DoorBlock.FACING, facing);
        }
        if (blockstateIn.getBlock() instanceof DoorBlock && blockstateIn.get(DoorBlock.HALF) == DoubleBlockHalf.UPPER) {
            Direction facing = blockstateIn.get(DoorBlock.FACING);
            return biomeDoor(structureType).with(DoorBlock.FACING, facing).with(DoorBlock.HALF, DoubleBlockHalf.UPPER);
        }
        if (structureType == 1) {
            if (blockstateIn.getBlock() == Blocks.OAK_LOG || blockstateIn.getBlock() == Blocks.DARK_OAK_LOG) {
                return Blocks.SANDSTONE.getDefaultState();
            }
            if (blockstateIn.getBlock() == Blocks.COBBLESTONE) {
                return Blocks.SANDSTONE.getDefaultState();
            }
            if (blockstateIn.getBlock() == Blocks.DIRT) {
                return Blocks.SAND.getDefaultState();
            }
            if (blockstateIn.getBlock() == Blocks.GRASS) {
                return Blocks.SANDSTONE.getDefaultState();
            }
            if (blockstateIn.getBlock() == Blocks.OAK_PLANKS) {
                return Blocks.CUT_SANDSTONE.getDefaultState();
            }

            if (blockstateIn.getBlock() == Blocks.OAK_STAIRS) {
                return Blocks.SANDSTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, blockstateIn.get(StairsBlock.FACING));
            }

            if (blockstateIn.getBlock() == Blocks.STONE_STAIRS) {
                return Blocks.SANDSTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, blockstateIn.get(StairsBlock.FACING));
            }

            if (blockstateIn.getBlock() == Blocks.GRAVEL) {
                return Blocks.SANDSTONE.getDefaultState();
            }
        } else if (structureType == 3) {
            if (blockstateIn.getBlock() == Blocks.OAK_LOG || blockstateIn.getBlock() == Blocks.DARK_OAK_LOG) {
                return Blocks.SPRUCE_LOG.getDefaultState().with(LogBlock.AXIS, blockstateIn.get(LogBlock.AXIS));
            }
            if (blockstateIn.getBlock() == Blocks.OAK_PLANKS) {
                return Blocks.SPRUCE_PLANKS.getDefaultState();
            }

            if (blockstateIn.getBlock() == Blocks.OAK_STAIRS) {
                return Blocks.SPRUCE_STAIRS.getDefaultState().with(StairsBlock.FACING, blockstateIn.get(StairsBlock.FACING));
            }

            if (blockstateIn.getBlock() == Blocks.OAK_FENCE) {
                return Blocks.SPRUCE_FENCE.getDefaultState();
            }
        } else if (structureType == 2) {
            if (blockstateIn.getBlock() == Blocks.OAK_LOG || blockstateIn.getBlock() == Blocks.DARK_OAK_LOG) {
                return Blocks.ACACIA_LOG.getDefaultState().with(LogBlock.AXIS, blockstateIn.get(LogBlock.AXIS));
            }

            if (blockstateIn.getBlock() == Blocks.OAK_PLANKS) {
                return Blocks.ACACIA_PLANKS.getDefaultState();
            }

            if (blockstateIn.getBlock() == Blocks.OAK_STAIRS) {
                return Blocks.ACACIA_STAIRS.getDefaultState().with(StairsBlock.FACING, blockstateIn.get(StairsBlock.FACING));
            }

            if (blockstateIn.getBlock() == Blocks.COBBLESTONE) {
                return Blocks.ACACIA_LOG.getDefaultState();
            }

            if (blockstateIn.getBlock() == Blocks.OAK_FENCE) {
                return Blocks.ACACIA_FENCE.getDefaultState();
            }
        }
        if (blockstateIn.getBlock() == Blocks.GRASS && structureType != 1) {
            return Blocks.GRASS_PATH.getDefaultState();
        }
        return blockstateIn;
    }

    protected BlockState biomeDoor(int structureType) {
        switch (structureType) {
            case 2:
                return Blocks.ACACIA_DOOR.getDefaultState();
            case 3:
                return Blocks.SPRUCE_DOOR.getDefaultState();
            default:
                return Blocks.OAK_DOOR.getDefaultState();
        }
    }

}