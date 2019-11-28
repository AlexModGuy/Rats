package com.github.alexthe666.rats.server.world.gen;

import com.github.alexthe666.rats.server.world.village.WorldGenPlagueDoctor;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nullable;
import java.util.Random;

public class RatsPlagueHutProcessor implements ITemplateProcessor {

    private Biome biome;
    private BlockPos blockPos;
    private PlacementSettings settings;
    private WorldGenPlagueDoctor hut;

    public RatsPlagueHutProcessor(BlockPos pos, WorldGenPlagueDoctor worldGenPetShop, PlacementSettings settings, Biome biome) {
        this.biome = biome;
        this.blockPos = pos;
        this.settings = settings;
        this.hut = worldGenPetShop;
    }

    @Nullable
    public Template.BlockInfo processBlock(World worldIn, BlockPos pos, Template.BlockInfo blockInfoIn) {
        if (blockInfoIn.blockState.getBlock() instanceof BlockChest) {
            ResourceLocation loot = WorldGenPlagueDoctor.LOOT;
            Random rand = new Random(worldIn.getSeed() + pos.toLong());
            CompoundNBT tag = blockInfoIn.tileentityData == null ? new CompoundNBT() : blockInfoIn.tileentityData;
            tag.setString("LootTable", loot.toString());
            tag.setLong("LootTableSeed", rand.nextLong());
            Template.BlockInfo newInfo = new Template.BlockInfo(pos, Blocks.CHEST.getDefaultState(), tag);
            return newInfo;
        } else if (blockInfoIn.blockState.getBlock() == Blocks.DIAMOND_BLOCK) {//spawn ocelot
            if (hut != null) {
                hut.villagerPos = pos;
            }
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
            return new Template.BlockInfo(pos, Blocks.AIR.getDefaultState(), null);
        }
        {
            BlockState state = getBiomeSpecificBlockState(blockInfoIn.blockState, biome);
            if (state != blockInfoIn.blockState) {
                return new Template.BlockInfo(pos, state, null);
            } else {
                return blockInfoIn;
            }
        }
    }

    @SuppressWarnings("deprecation")
    protected BlockState getBiomeSpecificBlockState(BlockState blockstateIn, Biome biome) {
        net.minecraftforge.event.terraingen.BiomeEvent.GetVillageBlockID event = new net.minecraftforge.event.terraingen.BiomeEvent.GetVillageBlockID(biome, blockstateIn);
        net.minecraftforge.common.MinecraftForge.TERRAIN_GEN_BUS.post(event);
        int structureType = 0;
        if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.DRY)) {
            structureType = 1;
        } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA)) {
            structureType = 2;
        } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.CONIFEROUS)) {
            structureType = 3;
        }

        if (event.getResult() == net.minecraftforge.fml.common.eventhandler.Event.Result.DENY)
            return event.getReplacement();
        if (blockstateIn.getBlock() instanceof BlockDoor && blockstateIn.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER) {
            Direction facing = blockstateIn.getValue(BlockDoor.FACING);
            return biomeDoor(structureType).with(BlockDoor.FACING, facing);
        }
        if (blockstateIn.getBlock() instanceof BlockDoor && blockstateIn.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.UPPER) {
            Direction facing = blockstateIn.getValue(BlockDoor.FACING);
            return biomeDoor(structureType).with(BlockDoor.FACING, facing).with(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER);
        }
        if (structureType == 1) {
            if (blockstateIn.getBlock() == Blocks.LOG || blockstateIn.getBlock() == Blocks.LOG2) {
                return Blocks.SANDSTONE.getDefaultState();
            }
            if (blockstateIn.getBlock() == Blocks.COBBLESTONE) {
                return Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.DEFAULT.getMetadata());
            }
            if (blockstateIn.getBlock() == Blocks.DIRT) {
                return Blocks.SAND.getDefaultState();
            }
            if (blockstateIn.getBlock() == Blocks.GRASS) {
                return Blocks.SANDSTONE.getDefaultState();
            }
            if (blockstateIn.getBlock() == Blocks.PLANKS) {
                return Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata());
            }

            if (blockstateIn.getBlock() == Blocks.OAK_STAIRS) {
                return Blocks.SANDSTONE_STAIRS.getDefaultState().with(BlockStairs.FACING, blockstateIn.getValue(BlockStairs.FACING));
            }

            if (blockstateIn.getBlock() == Blocks.STONE_STAIRS) {
                return Blocks.SANDSTONE_STAIRS.getDefaultState().with(BlockStairs.FACING, blockstateIn.getValue(BlockStairs.FACING));
            }

            if (blockstateIn.getBlock() == Blocks.GRAVEL) {
                return Blocks.SANDSTONE.getDefaultState();
            }
        } else if (structureType == 3) {
            if (blockstateIn.getBlock() == Blocks.LOG || blockstateIn.getBlock() == Blocks.LOG2) {
                return Blocks.LOG.getDefaultState().with(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE).with(BlockLog.LOG_AXIS, blockstateIn.getValue(BlockLog.LOG_AXIS));
            }

            if (blockstateIn.getBlock() == Blocks.PLANKS) {
                return Blocks.PLANKS.getDefaultState().with(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE);
            }

            if (blockstateIn.getBlock() == Blocks.OAK_STAIRS) {
                return Blocks.SPRUCE_STAIRS.getDefaultState().with(BlockStairs.FACING, blockstateIn.getValue(BlockStairs.FACING));
            }

            if (blockstateIn.getBlock() == Blocks.OAK_FENCE) {
                return Blocks.SPRUCE_FENCE.getDefaultState();
            }
        } else if (structureType == 2) {
            if (blockstateIn.getBlock() == Blocks.LOG || blockstateIn.getBlock() == Blocks.LOG2) {
                return Blocks.LOG2.getDefaultState().with(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).with(BlockLog.LOG_AXIS, blockstateIn.getValue(BlockLog.LOG_AXIS));
            }

            if (blockstateIn.getBlock() == Blocks.PLANKS) {
                return Blocks.PLANKS.getDefaultState().with(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA);
            }

            if (blockstateIn.getBlock() == Blocks.OAK_STAIRS) {
                return Blocks.ACACIA_STAIRS.getDefaultState().with(BlockStairs.FACING, blockstateIn.getValue(BlockStairs.FACING));
            }

            if (blockstateIn.getBlock() == Blocks.COBBLESTONE) {
                return Blocks.LOG2.getDefaultState().with(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).with(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y);
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