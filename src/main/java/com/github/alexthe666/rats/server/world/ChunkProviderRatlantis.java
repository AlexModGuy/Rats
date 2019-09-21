package com.github.alexthe666.rats.server.world;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.world.gen.MapGenRatRoad;
import com.github.alexthe666.rats.server.world.gen.WorldGenAquaduct;
import com.github.alexthe666.rats.server.world.gen.WorldGenRatRuin;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChunkProviderRatlantis implements IChunkGenerator {
    private Random rand;
    private World world;
    private Biome[] biomesForGeneration;
    private MapGenBase caveGenerator = new MapGenCaves();
    private MapGenStronghold strongholdGenerator = new MapGenStronghold();
    private MapGenRatRoad ratRoadGenerator = new MapGenRatRoad();
    public double width;
    public int height;
    public PerlinNoise perlin1;
    public PerlinNoise perlin2;
    protected static final NoiseGeneratorPerlin PATH_PERLIN = new NoiseGeneratorPerlin(new Random(2345L), 1);
    public ChunkProviderRatlantis(World par1World, long par2) {
        world = par1World;
        rand = new Random(par2);
        perlin1 = new PerlinNoise(par2);
        perlin2 = new PerlinNoise(par2 + 100L);
        width = 5D;
        height = 100;
    }

    public void generateTerrain(int chunkX, int chunkZ, ChunkPrimer chunkprimer) {
        int x = chunkX << 4;
        int z = chunkZ << 4;
        for (int i = x; i < x + 16; i++) {
            for (int k = z; k < z + 16; k++) {
                float dis = perlin2.turbulence2(i / 150F, k / 150F, 10) * 300 + 200;
                System.out.println(dis);
                float heightBase = height - (dis / (float) width) + (perlin1.turbulence2(i / 50F, k / 50F, 4F) * 5F);
                for (int y = 0; y < 256; y++) {
                    Block i4 = Blocks.AIR;
                    if (heightBase > 67) {
                        if (y < heightBase - 3) {
                            i4 = Blocks.STONE;
                        } else if (y < heightBase - 1) {
                            i4 = Blocks.DIRT;
                        } else if (y < heightBase) {
                            if(isPath(i, k)){
                                i4 = RatsBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED;
                            }else{
                                i4 = Blocks.GRASS;
                            }
                        }
                    } else {
                        if (y < heightBase - 6 + rand.nextInt(3)) {
                            i4 = Blocks.STONE;
                        } else if (y < heightBase - 3) {
                            i4 = Blocks.SANDSTONE;
                        } else if (y < heightBase) {
                            i4 = Blocks.SAND;
                        } else if (y <= 64) {
                            i4 = Blocks.WATER;
                        }
                    }
                    chunkprimer.setBlockState(i - x, y, k - z, i4.getDefaultState());
                }
            }
        }
    }

    private boolean isPath(int i, int k) {
        return false;
    }

    public Chunk generateChunk(int chunkX, int chunkZ) {
        this.rand.setSeed((long) chunkX * 341873128712L + (long) chunkZ * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        this.biomesForGeneration = this.world.getBiomeProvider().getBiomes(this.biomesForGeneration, chunkX * 16, chunkZ * 16, 16, 16);
        this.generateTerrain(chunkX, chunkZ, chunkprimer);
        this.caveGenerator.generate(this.world, chunkX, chunkZ, chunkprimer);
        this.strongholdGenerator.generate(this.world, chunkX, chunkZ, chunkprimer);
        this.ratRoadGenerator.generate(this.world, chunkX, chunkZ, chunkprimer);
        Chunk chunk = new Chunk(this.world, chunkprimer, chunkX, chunkZ);
        byte[] biomeIds = chunk.getBiomeArray();
        for (int var6 = 0; var6 < biomeIds.length; ++var6) {
            biomeIds[var6] = (byte) Biome.getIdForBiome(this.biomesForGeneration[var6]);
        }
        chunk.generateSkylightMap();
        return chunk;
    }

    public void populate(int chunkX, int chunkZ) {
        BlockFalling.fallInstantly = true;
        BlockPos blockpos = new BlockPos(chunkX * 16, 0, chunkZ * 16);
        Biome biome = this.world.getBiome(blockpos.add(16, 0, 16));
        rand.setSeed(world.getSeed());
        long randLongX = (rand.nextLong() / 2L) * 2L + 1L;
        long randLongZ = (rand.nextLong() / 2L) * 2L + 1L;
        rand.setSeed(((long) chunkX * randLongX + (long) chunkZ * randLongZ) ^ world.getSeed());
        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(this, world, rand, chunkX, chunkZ, false));
        this.strongholdGenerator.generateStructure(this.world, this.rand, new ChunkPos(chunkX, chunkZ));
        this.ratRoadGenerator.generateStructure(this.world, this.rand, new ChunkPos(chunkX, chunkZ));
        boolean flag = false;
        if (rand.nextInt(8) == 0 && net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, chunkX, chunkZ, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE)) {
            int i1 = this.rand.nextInt(16) + 8;
            int j1 = this.rand.nextInt(256);
            int k1 = this.rand.nextInt(16) + 8;
            (new WorldGenLakes(Blocks.WATER)).generate(this.world, this.rand, blockpos.add(i1, j1, k1));
        }
        if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, chunkX, chunkZ, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.DUNGEON)) {
            for (int j2 = 0; j2 < 8; ++j2) {
                int i3 = this.rand.nextInt(16) + 8;
                int l3 = this.rand.nextInt(256);
                int l1 = this.rand.nextInt(16) + 8;
                (new WorldGenDungeons()).generate(this.world, this.rand, blockpos.add(i3, l3, l1));
            }
        }
        addStructures(this.world, this.rand, blockpos);
        biome.decorate(this.world, this.rand, blockpos);
        if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, chunkX, chunkZ, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ANIMALS))
            WorldEntitySpawner.performWorldGenSpawning(this.world, biome, chunkX * 16 + 8, chunkZ * 16 + 8, 16, 16, this.rand);
        blockpos = blockpos.add(8, 0, 8);

        if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, chunkX, chunkZ, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE)) {
            for (int k2 = 0; k2 < 16; ++k2) {
                for (int j3 = 0; j3 < 16; ++j3) {
                    BlockPos blockpos1 = this.world.getPrecipitationHeight(blockpos.add(k2, 0, j3));
                    BlockPos blockpos2 = blockpos1.down();

                    if (this.world.canBlockFreezeWater(blockpos2)) {
                        this.world.setBlockState(blockpos2, Blocks.ICE.getDefaultState(), 2);
                    }

                    if (this.world.canSnowAt(blockpos1, true)) {
                        this.world.setBlockState(blockpos1, Blocks.SNOW_LAYER.getDefaultState(), 2);
                    }
                }
            }
        }
        BlockFalling.fallInstantly = false;
    }

    private int drawnPaths = 0;
    private void addStructures(World world, Random rand, BlockPos blockpos) {
        if(rand.nextInt(4) == 0 && world.getBlockState(world.getHeight(blockpos).down()).isOpaqueCube()){
            new WorldGenRatRuin(EnumFacing.HORIZONTALS[rand.nextInt(EnumFacing.HORIZONTALS.length - 1)]).generate(world, rand, blockpos);
        }
        if(rand.nextInt(40) == 0 && world.getBlockState(world.getHeight(blockpos).down()).getBlock() instanceof BlockLiquid){
            new WorldGenAquaduct(EnumFacing.HORIZONTALS[rand.nextInt(EnumFacing.HORIZONTALS.length - 1)]).generate(world, rand, blockpos);
        }
    }

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z) {
        return false;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        Biome biome = this.world.getBiome(pos);
        return biome.getSpawnableList(creatureType);
    }

    @Nullable
    @Override
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
        return null;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) {

    }

    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
        return false;
    }
}
