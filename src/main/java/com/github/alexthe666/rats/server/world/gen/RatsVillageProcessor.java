package com.github.alexthe666.rats.server.world.gen;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.world.village.WorldGenPetShop;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
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

public class RatsVillageProcessor implements ITemplateProcessor {

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
    public Template.BlockInfo processBlock(World worldIn, BlockPos pos, Template.BlockInfo blockInfoIn) {
        if (blockInfoIn.blockState.getBlock() instanceof BlockChest) {
            ResourceLocation loot = blockInfoIn.blockState.getBlock() == Blocks.TRAPPED_CHEST ? WorldGenPetShop.UPSTAIRS_LOOT : WorldGenPetShop.LOOT;
            Random rand = new Random(worldIn.getSeed() + pos.toLong());
            NBTTagCompound tag = blockInfoIn.tileentityData == null ? new NBTTagCompound() : blockInfoIn.tileentityData;
            tag.setString("LootTable", loot.toString());
            tag.setLong("LootTableSeed", rand.nextLong());
            Template.BlockInfo newInfo = new Template.BlockInfo(pos, Blocks.CHEST.getDefaultState(), tag);
            return newInfo;
        } else if (blockInfoIn.blockState.getBlock() == Blocks.DIAMOND_BLOCK) {//spawn ocelot
            if (petShop.wolfPos.size() == 0 || petShop.wolfPos.size() < 2 && worldIn.rand.nextInt(1) == 0) {
                if (!petShop.wolfPos.contains(pos)) {
                    petShop.wolfPos.add(pos);
                }
            }
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
            return new Template.BlockInfo(pos, Blocks.AIR.getDefaultState(), null);
        } else if (blockInfoIn.blockState.getBlock() == Blocks.GOLD_BLOCK) {//spawn rat cage
            if (!petShop.ratPos.contains(pos)) {
                petShop.ratPos.add(pos);
            }
            return new Template.BlockInfo(pos, RatsBlockRegistry.RAT_CAGE.getDefaultState(), null);
        } else if (blockInfoIn.blockState.getBlock() == Blocks.QUARTZ_BLOCK) {//spawn rat or rabbit cage
            if (!petShop.rabbitPos.contains(pos)) {
                petShop.rabbitPos.add(pos);
            }
            return new Template.BlockInfo(pos, RatsBlockRegistry.RAT_CAGE.getDefaultState(), null);
        } else if (blockInfoIn.blockState.getBlock() == Blocks.IRON_BLOCK) {//spawn wolf cage
            if (petShop.ocelotPos.size() == 0 || petShop.ocelotPos.size() < 2 && worldIn.rand.nextInt(3) == 0) {
                if (!petShop.ocelotPos.contains(pos)) {
                    petShop.ocelotPos.add(pos);
                }
            }
            return new Template.BlockInfo(pos, RatsBlockRegistry.RAT_CAGE.getDefaultState(), null);
        } else if (blockInfoIn.blockState.getBlock() == Blocks.EMERALD_BLOCK) {//spawn parrot cage
            if (!petShop.parrotPos.contains(pos)) {
                petShop.parrotPos.add(pos);
            }
            return new Template.BlockInfo(pos, RatsBlockRegistry.RAT_CAGE.getDefaultState(), null);
        } else if (blockInfoIn.blockState.getBlock() == Blocks.COAL_BLOCK) {//spawn shopkeeper
            if (!petShop.villagerPos.contains(pos)) {
                petShop.villagerPos.add(pos);
            }
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
            return new Template.BlockInfo(pos, Blocks.AIR.getDefaultState(), null);
        } else if (blockInfoIn.blockState.getBlock() == Blocks.LAPIS_BLOCK) {//spawn ocelot
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
            return new Template.BlockInfo(pos, Blocks.AIR.getDefaultState(), null);
        }
        {
            IBlockState state = getBiomeSpecificBlockState(blockInfoIn.blockState, biome);
            if (state != blockInfoIn.blockState) {
                return new Template.BlockInfo(pos, state, null);
            } else {
                return blockInfoIn;
            }
        }
    }

    @SuppressWarnings("deprecation")
    protected IBlockState getBiomeSpecificBlockState(IBlockState blockstateIn, Biome biome) {
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
            EnumFacing facing = blockstateIn.getValue(BlockDoor.FACING);
            return biomeDoor(structureType).withProperty(BlockDoor.FACING, facing);
        }
        if (blockstateIn.getBlock() instanceof BlockDoor && blockstateIn.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.UPPER) {
            EnumFacing facing = blockstateIn.getValue(BlockDoor.FACING);
            return biomeDoor(structureType).withProperty(BlockDoor.FACING, facing).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER);
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
                return Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, blockstateIn.getValue(BlockStairs.FACING));
            }

            if (blockstateIn.getBlock() == Blocks.STONE_STAIRS) {
                return Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, blockstateIn.getValue(BlockStairs.FACING));
            }

            if (blockstateIn.getBlock() == Blocks.GRAVEL) {
                return Blocks.SANDSTONE.getDefaultState();
            }
        } else if (structureType == 3) {
            if (blockstateIn.getBlock() == Blocks.LOG || blockstateIn.getBlock() == Blocks.LOG2) {
                return Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLog.LOG_AXIS, blockstateIn.getValue(BlockLog.LOG_AXIS));
            }

            if (blockstateIn.getBlock() == Blocks.PLANKS) {
                return Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE);
            }

            if (blockstateIn.getBlock() == Blocks.OAK_STAIRS) {
                return Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, blockstateIn.getValue(BlockStairs.FACING));
            }

            if (blockstateIn.getBlock() == Blocks.OAK_FENCE) {
                return Blocks.SPRUCE_FENCE.getDefaultState();
            }
        } else if (structureType == 2) {
            if (blockstateIn.getBlock() == Blocks.LOG || blockstateIn.getBlock() == Blocks.LOG2) {
                return Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockLog.LOG_AXIS, blockstateIn.getValue(BlockLog.LOG_AXIS));
            }

            if (blockstateIn.getBlock() == Blocks.PLANKS) {
                return Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA);
            }

            if (blockstateIn.getBlock() == Blocks.OAK_STAIRS) {
                return Blocks.ACACIA_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, blockstateIn.getValue(BlockStairs.FACING));
            }

            if (blockstateIn.getBlock() == Blocks.COBBLESTONE) {
                return Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y);
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

    protected IBlockState biomeDoor(int structureType) {
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