package com.github.alexthe666.rats.server.world;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.EntityRatlanteanSpirit;
import com.github.alexthe666.rats.server.world.gen.WorldGenMarblePile;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeHills;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BiomeRatlantis extends Biome {

    private static final IBlockState JUNGLE_LOG = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
    private static final IBlockState JUNGLE_LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
    private static final IBlockState OAK_LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
    private final WorldGenerator cheeseOre = new WorldGenMinable(RatsBlockRegistry.BLOCK_OF_CHEESE.getDefaultState(), 9);
    private final WorldGenerator cheeseMarbleOre = new WorldGenMinable(RatsBlockRegistry.MARBLED_CHEESE_RAW.getDefaultState(), 25);


    public BiomeRatlantis() {
        super(new BiomeProperties("ratlantis").setTemperature(0.95F).setRainfall(0.9F));
        this.setRegistryName("ratlantis");
        this.decorator.treesPerChunk = 30;
        this.decorator.grassPerChunk = 25;
        this.decorator.flowersPerChunk = 4;
        this.spawnableCreatureList.clear();
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityParrot.class, 60, 1, 2));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityRatlanteanSpirit.class, 60, 1, 3));
        addFlower(RatsBlockRegistry.RATGLOVE_FLOWER.getDefaultState(), 30);
    }

    public WorldGenerator getRandomWorldGenForGrass(Random rand) {
        return rand.nextInt(4) == 0 ? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        if (rand.nextInt(10) == 0) {
            return BIG_TREE_FEATURE;
        } else if (rand.nextInt(2) == 0) {
            return new WorldGenShrub(JUNGLE_LOG, OAK_LEAF);
        } else {
            return (WorldGenAbstractTree) (rand.nextInt(3) == 0 ? new WorldGenMegaJungle(false, 10, 20, JUNGLE_LOG, JUNGLE_LEAF) : new WorldGenTrees(false, 4 + rand.nextInt(7), JUNGLE_LOG, JUNGLE_LEAF, true));
        }
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return 0XFFC62A;
    }

    public void decorate(World worldIn, Random rand, BlockPos pos) {
        super.decorate(worldIn, rand, pos);
        net.minecraftforge.common.MinecraftForge.ORE_GEN_BUS.post(new net.minecraftforge.event.terraingen.OreGenEvent.Pre(worldIn, rand, pos));
        WorldGenerator emeralds = new EmeraldGenerator();
        if (net.minecraftforge.event.terraingen.TerrainGen.generateOre(worldIn, rand, emeralds, pos, net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.EMERALD))
            emeralds.generate(worldIn, rand, pos);

        int i = rand.nextInt(16) + 8;
        int j = rand.nextInt(16) + 8;
        int height = worldIn.getHeight(pos.add(i, 0, j)).getY() * 2; // could == 0, which crashes nextInt
        if (height < 1) height = 1;
        int k = rand.nextInt(height);
        for (int melons = 0; melons < 3; melons++) {
            (new WorldGenMarblePile()).generate(worldIn, rand, pos.add(i, k, j));
        }
        for (int j1 = 0; j1 < 7; ++j1) {
            int k1 = rand.nextInt(16);
            int l1 = rand.nextInt(100);
            int i2 = rand.nextInt(16);
            this.cheeseMarbleOre.generate(worldIn, rand, pos.add(k1, l1, i2));
        }
        for (int j1 = 0; j1 < 7; ++j1) {
            int k1 = rand.nextInt(16);
            int l1 = rand.nextInt(100);
            int i2 = rand.nextInt(16);
            this.cheeseOre.generate(worldIn, rand, pos.add(k1, l1, i2));
        }
    }

    private static class EmeraldGenerator extends WorldGenerator {
        @Override
        public boolean generate(World worldIn, Random rand, BlockPos pos) {
            int count = 3 + rand.nextInt(6);
            for (int i = 0; i < count; i++) {
                int offset = net.minecraftforge.common.ForgeModContainer.fixVanillaCascading ? 8 : 0; // MC-114332
                BlockPos blockpos = pos.add(rand.nextInt(16) + offset, rand.nextInt(28) + 4, rand.nextInt(16) + offset);
                net.minecraft.block.state.IBlockState state = worldIn.getBlockState(blockpos);
                if (state.getBlock().isReplaceableOreGen(state, worldIn, blockpos, net.minecraft.block.state.pattern.BlockMatcher.forBlock(Blocks.STONE))) {
                    worldIn.setBlockState(blockpos, Blocks.EMERALD_ORE.getDefaultState(), 16 | 2);
                }
            }
            return true;
        }
    }

}
