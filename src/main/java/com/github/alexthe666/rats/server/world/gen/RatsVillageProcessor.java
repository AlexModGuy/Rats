package com.github.alexthe666.rats.server.world.gen;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.world.village.WorldGenPetShop;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.block.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nullable;
import java.util.Random;

public class RatsVillageProcessor extends StructureProcessor {

    private Biome biome;
    private BlockPos blockPos;
    private PlacementSettings settings;
    private WorldGenPetShop petShop;

    public RatsVillageProcessor(BlockPos pos, WorldGenPetShop worldGenPetShop, PlacementSettings settings, Biome biome) {
        this.biome = biome;
        this.blockPos = pos;
        this.settings = settings;
        this.petShop = worldGenPetShop;
    }

    @Nullable
    public Template.BlockInfo process(IWorldReader worldIn, BlockPos pos, Template.BlockInfo blockInfoIn, Template.BlockInfo blockInfoIn2, PlacementSettings settings) {
        Random random = settings.getRandom(pos);
        if (blockInfoIn.state.getBlock() instanceof ChestBlock) {
            ResourceLocation loot = blockInfoIn.state.getBlock() == Blocks.TRAPPED_CHEST ? WorldGenPetShop.UPSTAIRS_LOOT : WorldGenPetShop.LOOT;
            CompoundNBT tag = blockInfoIn.nbt == null ? new CompoundNBT() : blockInfoIn.nbt;
            tag.putString("LootTable", loot.toString());
            tag.putLong("LootTableSeed", random.nextLong());
            Template.BlockInfo newInfo = new Template.BlockInfo(pos, Blocks.CHEST.getDefaultState(), tag);
            return newInfo;
        } else if (blockInfoIn.state.getBlock() == Blocks.DIAMOND_BLOCK) {//spawn ocelot
            if (petShop.wolfPos.size() == 0 || petShop.wolfPos.size() < 2 && random.nextInt(1) == 0) {
                if (!petShop.wolfPos.contains(pos)) {
                    petShop.wolfPos.add(pos);
                }
            }
            //worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
            return new Template.BlockInfo(pos, Blocks.AIR.getDefaultState(), null);
        } else if (blockInfoIn.state.getBlock() == Blocks.GOLD_BLOCK) {//spawn rat cage
            if (!petShop.ratPos.contains(pos)) {
                petShop.ratPos.add(pos);
            }
            return new Template.BlockInfo(pos, RatsBlockRegistry.RAT_CAGE.getDefaultState(), null);
        } else if (blockInfoIn.state.getBlock() == Blocks.QUARTZ_BLOCK) {//spawn rat or rabbit cage
            if (!petShop.rabbitPos.contains(pos)) {
                petShop.rabbitPos.add(pos);
            }
            return new Template.BlockInfo(pos, RatsBlockRegistry.RAT_CAGE.getDefaultState(), null);
        } else if (blockInfoIn.state.getBlock() == Blocks.IRON_BLOCK) {//spawn wolf cage
            if (petShop.ocelotPos.size() == 0 || petShop.ocelotPos.size() < 2 && random.nextInt(3) == 0) {
                if (!petShop.ocelotPos.contains(pos)) {
                    petShop.ocelotPos.add(pos);
                }
            }
            return new Template.BlockInfo(pos, RatsBlockRegistry.RAT_CAGE.getDefaultState(), null);
        } else if (blockInfoIn.state.getBlock() == Blocks.EMERALD_BLOCK) {//spawn parrot cage
            if (!petShop.parrotPos.contains(pos)) {
                petShop.parrotPos.add(pos);
            }
            return new Template.BlockInfo(pos, RatsBlockRegistry.RAT_CAGE.getDefaultState(), null);
        } else if (blockInfoIn.state.getBlock() == Blocks.COAL_BLOCK) {//spawn shopkeeper
            if (!petShop.villagerPos.contains(pos)) {
                petShop.villagerPos.add(pos);
            }
            //worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
            return new Template.BlockInfo(pos, Blocks.AIR.getDefaultState(), null);
        } else if (blockInfoIn.state.getBlock() == Blocks.LAPIS_BLOCK) {//spawn ocelot
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

    @Override
    protected IStructureProcessorType getType() {
        return IStructureProcessorType.BLOCK_ROT;
    }

    @Override
    protected <T> Dynamic<T> serialize0(DynamicOps<T> ops) {
        return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(ops.createString("rat_village_processor"), ops.createFloat(0))));
    }
}