package com.github.alexthe666.rats.server.world.gen;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.init.Biomes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class StructureRatRoadPieces {

    public static void registerVillagePieces() {
        MapGenStructureIO.registerStructure(MapGenRatRoad.Start.class, "RatRoadStart");
        MapGenStructureIO.registerStructureComponent(House.class, "RatRoadHouse");
        MapGenStructureIO.registerStructureComponent(Start.class, "RatRoadStart");
        MapGenStructureIO.registerStructureComponent(Path.class, "RatRoadPath");
        MapGenStructureIO.registerStructureComponent(Well.class, "RatRoadWell");
    }

    public static List<StructureRatRoadPieces.PieceWeight> getStructureVillageWeightedPieceList(Random random, int size) {
        List<StructureRatRoadPieces.PieceWeight> list = Lists.newArrayList();
        list.add(new PieceWeight(Road.class, 100, 30));
        return list;
    }

    private static int updatePieceWeight(List<StructureRatRoadPieces.PieceWeight> p_75079_0_) {
        boolean flag = false;
        int i = 0;
        for (PieceWeight StructureRatRoadPieces$pieceweight : p_75079_0_) {
            if (StructureRatRoadPieces$pieceweight.villagePiecesLimit > 0 && StructureRatRoadPieces$pieceweight.villagePiecesSpawned < StructureRatRoadPieces$pieceweight.villagePiecesLimit) {
                flag = true;
            }

            i += StructureRatRoadPieces$pieceweight.villagePieceWeight;
        }

        return flag ? i : -1;
    }

    private static Village findAndCreateComponentFactory(Start start, PieceWeight weight, List<StructureComponent> structureComponents, Random rand, int structureMinX, int structureMinY, int structureMinZ, Direction facing, int componentType) {
        Class<? extends StructureRatRoadPieces.Village> oclass = weight.villagePieceClass;
        Object village = House.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
        return (Village) village;
    }

    private static Village generateComponent(Start start, List<StructureComponent> structureComponents, Random rand, int structureMinX, int structureMinY, int structureMinZ, Direction facing, int componentType) {
        int i = updatePieceWeight(start.structureVillageWeightedPieceList);

        if (i <= 0) {
            return null;
        } else {
            int j = 0;

            while (j < 5) {
                ++j;
                int k = rand.nextInt(i);

                for (PieceWeight StructureRatRoadPieces$pieceweight : start.structureVillageWeightedPieceList) {
                    k -= StructureRatRoadPieces$pieceweight.villagePieceWeight;

                    if (k < 0) {
                        if (!StructureRatRoadPieces$pieceweight.canSpawnMoreVillagePiecesOfType(componentType) || StructureRatRoadPieces$pieceweight == start.structVillagePieceWeight && start.structureVillageWeightedPieceList.size() > 1) {
                            break;
                        }

                        Village StructureRatRoadPieces$village = findAndCreateComponentFactory(start, StructureRatRoadPieces$pieceweight, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);

                        if (StructureRatRoadPieces$village != null) {
                            ++StructureRatRoadPieces$pieceweight.villagePiecesSpawned;
                            start.structVillagePieceWeight = StructureRatRoadPieces$pieceweight;

                            if (!StructureRatRoadPieces$pieceweight.canSpawnMoreVillagePieces()) {
                                start.structureVillageWeightedPieceList.remove(StructureRatRoadPieces$pieceweight);
                            }

                            return StructureRatRoadPieces$village;
                        }
                    }
                }
            }
            return null;
        }
    }

    private static StructureComponent generateAndAddComponent(Start start, List<StructureComponent> structureComponents, Random rand, int structureMinX, int structureMinY, int structureMinZ, Direction facing, int componentType) {
        if (componentType > 50) {
            return null;
        } else if (Math.abs(structureMinX - start.getBoundingBox().minX) <= 112 && Math.abs(structureMinZ - start.getBoundingBox().minZ) <= 112) {
            StructureComponent structurecomponent = generateComponent(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType + 1);

            if (structurecomponent != null) {
                structureComponents.add(structurecomponent);
                start.pendingHouses.add(structurecomponent);
                return structurecomponent;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private static StructureComponent generateAndAddRoadPiece(Start start, List<StructureComponent> p_176069_1_, Random rand, int p_176069_3_, int p_176069_4_, int p_176069_5_, Direction facing, int length) {
        if (length > 3 + start.terrainType) {
            return null;
        } else if (Math.abs(p_176069_3_ - start.getBoundingBox().minX) <= 112 && Math.abs(p_176069_5_ - start.getBoundingBox().minZ) <= 112) {
            StructureBoundingBox structureboundingbox = Path.findPieceBox(start, p_176069_1_, rand, p_176069_3_, p_176069_4_, p_176069_5_, facing);

            if (structureboundingbox != null && structureboundingbox.minY > 0) {
                StructureComponent structurecomponent = new Path(start, length, rand, structureboundingbox, facing);
                p_176069_1_.add(structurecomponent);
                start.pendingRoads.add(structurecomponent);
                return structurecomponent;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static class Path extends Road {
        private int length;

        public Path() {
        }

        public Path(Start start, int type, Random rand, StructureBoundingBox bb, Direction facing) {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = bb;
            this.length = Math.max(bb.getXSize(), bb.getZSize());
        }

        public static StructureBoundingBox findPieceBox(Start start, List<StructureComponent> builtStructures, Random rand, int x, int y, int z, Direction facing) {
            for (int i = 3 * MathHelper.getInt(rand, 6, 12); i >= 3; i -= 3) {
                StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 5, 1, i, facing);
                if (StructureComponent.findIntersecting(builtStructures, structureboundingbox) == null) {
                    return structureboundingbox;
                }
            }
            return null;
        }

        protected void writeStructureToNBT(CompoundNBT tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setInt("Length", this.length);
        }

        protected void readStructureFromNBT(CompoundNBT tagCompound) {
            super.readStructureFromNBT(tagCompound);
            this.length = tagCompound.getInt("Length");
        }

        public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
            int children = 0;
            Direction Direction = this.getCoordBaseMode();
            if (children < rand.nextInt(3) && rand.nextInt(3) > 0 && Direction != null) {
                switch (Direction) {
                    case NORTH:
                    default:
                        generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.getBoundingBox().minX - 1, this.getBoundingBox().minY, this.getBoundingBox().minZ, net.minecraft.util.Direction.WEST, this.getComponentType());
                        break;
                    case SOUTH:
                        generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, net.minecraft.util.Direction.WEST, this.getComponentType());
                        break;
                    case WEST:
                        generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, net.minecraft.util.Direction.NORTH, this.getComponentType());
                        break;
                    case EAST:
                        generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, net.minecraft.util.Direction.NORTH, this.getComponentType());
                }
            }
            if (children < rand.nextInt(3) && rand.nextInt(3) > 0 && Direction != null) {
                switch (Direction) {
                    case NORTH:
                    default:
                        generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, net.minecraft.util.Direction.EAST, this.getComponentType());
                        break;
                    case SOUTH:
                        generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, net.minecraft.util.Direction.EAST, this.getComponentType());
                        break;
                    case WEST:
                        generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, net.minecraft.util.Direction.SOUTH, this.getComponentType());
                        break;
                    case EAST:
                        generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, net.minecraft.util.Direction.SOUTH, this.getComponentType());
                }
            }
        }

        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
            Direction facing = this.getCoordBaseMode();

            for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i) {
                for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j) {
                    BlockPos blockpos = new BlockPos(i, 64, j);

                    if (structureBoundingBoxIn.isVecInside(blockpos)) {
                        blockpos = worldIn.getTopSolidOrLiquidBlock(blockpos).down();

                        if (blockpos.getY() < worldIn.getSeaLevel()) {
                            blockpos = new BlockPos(blockpos.getX(), worldIn.getSeaLevel() - 1, blockpos.getZ());
                        }

                        while (blockpos.getY() >= worldIn.getSeaLevel() - 1) {
                            BlockState BlockState4 = worldIn.getBlockState(blockpos);
                            if (BlockState4.getBlock() == Blocks.GRASS) {
                                worldIn.setBlockState(blockpos, RatStructure.getRandomCrackedBlock(BlockState4, randomIn), 2);
                                break;
                            }
                            blockpos = blockpos.down();
                        }
                    }
                }
            }

            return true;
        }

        @Override
        protected void readStructureFromNBT(CompoundNBT tagCompound, TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound);
        }
    }

    public static class PieceWeight {
        public final int villagePieceWeight;
        public Class<? extends StructureRatRoadPieces.Village> villagePieceClass;
        public int villagePiecesSpawned;
        public int villagePiecesLimit;

        public PieceWeight(Class<? extends StructureRatRoadPieces.Village> structure, int weight, int limit) {
            this.villagePieceClass = structure;
            this.villagePieceWeight = weight;
            this.villagePiecesLimit = limit;
        }

        public boolean canSpawnMoreVillagePiecesOfType(int componentType) {
            return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
        }

        public boolean canSpawnMoreVillagePieces() {
            return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
        }
    }

    public abstract static class Road extends Village {
        public Road() {
        }

        protected Road(Start start, int type) {
            super(start, type);
        }
    }

    public static class Start extends Well {
        public BiomeProvider worldChunkMngr;
        /**
         * World terrain type, 0 for normal, 1 for flap map
         */
        public int terrainType;
        public PieceWeight structVillagePieceWeight;
        public List<StructureRatRoadPieces.PieceWeight> structureVillageWeightedPieceList;
        public List<StructureComponent> pendingHouses = Lists.newArrayList();
        public List<StructureComponent> pendingRoads = Lists.newArrayList();
        public Biome biome;

        public Start() {
        }

        public Start(BiomeProvider chunkManagerIn, int p_i2104_2_, Random rand, int p_i2104_4_, int p_i2104_5_, List<StructureRatRoadPieces.PieceWeight> p_i2104_6_, int p_i2104_7_) {
            super(null, 0, rand, p_i2104_4_, p_i2104_5_);
            this.worldChunkMngr = chunkManagerIn;
            this.structureVillageWeightedPieceList = p_i2104_6_;
            this.terrainType = p_i2104_7_;
            Biome biome = chunkManagerIn.getBiome(new BlockPos(p_i2104_4_, 0, p_i2104_5_), Biomes.DEFAULT);
            this.biome = biome;
            this.startPiece = this;
            this.func_189924_a(this.field_189928_h);
            this.field_189929_i = rand.nextInt(50) == 0;
        }
    }

    public abstract static class Village extends StructureComponent {
        protected int averageGroundLvl = -1;
        protected int field_189928_h;
        protected boolean field_189929_i;
        protected Start startPiece;
        /**
         * The number of villagers that have been spawned in this component.
         */
        private int villagersSpawned;

        public Village() {
        }

        protected Village(Start start, int type) {
            super(type);

            if (start != null) {
                this.field_189928_h = start.field_189928_h;
                this.field_189929_i = start.field_189929_i;
                startPiece = start;
            }
        }

        protected static boolean canVillageGoDeeper(StructureBoundingBox structurebb) {
            return structurebb != null && structurebb.minY > 10;
        }

        /**
         * (abstract) Helper method to write subclass data to NBT
         */
        protected void writeStructureToNBT(CompoundNBT tagCompound) {
            tagCompound.setInt("HPos", this.averageGroundLvl);
            tagCompound.setInt("VCount", this.villagersSpawned);
            tagCompound.setByte("Type", (byte) this.field_189928_h);
            tagCompound.setBoolean("Zombie", this.field_189929_i);
        }

        /**
         * (abstract) Helper method to read subclass data from NBT
         */
        protected void readStructureFromNBT(CompoundNBT tagCompound) {
            this.averageGroundLvl = tagCompound.getInt("HPos");
            this.villagersSpawned = tagCompound.getInt("VCount");
            this.field_189928_h = tagCompound.getByte("Type");

            if (tagCompound.getBoolean("Desert")) {
                this.field_189928_h = 1;
            }

            this.field_189929_i = tagCompound.getBoolean("Zombie");
        }

        /**
         * Gets the next village component, with the bounding box shifted -1 in the X and Z direction.
         */
        protected StructureComponent getNextComponentNN(Start start, List<StructureComponent> structureComponents, Random rand, int p_74891_4_, int p_74891_5_) {
            Direction Direction = this.getCoordBaseMode();

            if (Direction != null) {
                switch (Direction) {
                    case NORTH:
                    default:
                        return generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX - 1, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ + p_74891_5_, net.minecraft.util.Direction.WEST, this.getComponentType());
                    case SOUTH:
                        return generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX - 1, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ + p_74891_5_, net.minecraft.util.Direction.WEST, this.getComponentType());
                    case WEST:
                        return generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX + p_74891_5_, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ - 1, net.minecraft.util.Direction.NORTH, this.getComponentType());
                    case EAST:
                        return generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX + p_74891_5_, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ - 1, net.minecraft.util.Direction.NORTH, this.getComponentType());
                }
            } else {
                return null;
            }
        }

        /**
         * Gets the next village component, with the bounding box shifted +1 in the X and Z direction.
         */
        protected StructureComponent getNextComponentPP(Start start, List<StructureComponent> structureComponents, Random rand, int p_74894_4_, int p_74894_5_) {
            Direction Direction = this.getCoordBaseMode();

            if (Direction != null) {
                switch (Direction) {
                    case NORTH:
                    default:
                        return generateAndAddComponent(start, structureComponents, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74894_4_, this.boundingBox.minZ + p_74894_5_, net.minecraft.util.Direction.EAST, this.getComponentType());
                    case SOUTH:
                        return generateAndAddComponent(start, structureComponents, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74894_4_, this.boundingBox.minZ + p_74894_5_, net.minecraft.util.Direction.EAST, this.getComponentType());
                    case WEST:
                        return generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX + p_74894_5_, this.boundingBox.minY + p_74894_4_, this.boundingBox.maxZ + 1, net.minecraft.util.Direction.SOUTH, this.getComponentType());
                    case EAST:
                        return generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX + p_74894_5_, this.boundingBox.minY + p_74894_4_, this.boundingBox.maxZ + 1, net.minecraft.util.Direction.SOUTH, this.getComponentType());
                }
            } else {
                return null;
            }
        }

        /**
         * Discover the y coordinate that will serve as the ground level of the supplied BoundingBox. (A median of
         * all the levels in the BB's horizontal rectangle).
         */
        protected int getAverageGroundLevel(World worldIn, StructureBoundingBox structurebb) {
            int i = 0;
            int j = 0;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for (int k = this.boundingBox.minZ; k <= this.boundingBox.maxZ; ++k) {
                for (int l = this.boundingBox.minX; l <= this.boundingBox.maxX; ++l) {
                    blockpos$mutableblockpos.setPos(l, 64, k);

                    if (structurebb.isVecInside(blockpos$mutableblockpos)) {
                        i += Math.max(worldIn.getTopSolidOrLiquidBlock(blockpos$mutableblockpos).getY(), worldIn.provider.getAverageGroundLevel() - 1);
                        ++j;
                    }
                }
            }

            if (j == 0) {
                return -1;
            } else {
                return i / j;
            }
        }

        /**
         * Spawns a number of villagers in this component. Parameters: world, component bounding box, x offset, y
         * offset, z offset, number of villagers
         */
        protected void spawnVillagers(World worldIn, StructureBoundingBox structurebb, int x, int y, int z, int count) {
            if (this.villagersSpawned < count) {
                for (int i = this.villagersSpawned; i < count; ++i) {
                    int j = this.getXWithOffset(x + i, z);
                    int k = this.getYWithOffset(y);
                    int l = this.getZWithOffset(x + i, z);

                    if (!structurebb.isVecInside(new BlockPos(j, k, l))) {
                        break;
                    }

                    ++this.villagersSpawned;
                }
            }
        }

        @Deprecated // Use Forge version below.
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {
            return currentVillagerProfession;
        }

        @SuppressWarnings("deprecation")
        protected net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession chooseForgeProfession(int count, net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession prof) {
            return net.minecraftforge.fml.common.registry.VillagerRegistry.getById(chooseProfession(count, net.minecraftforge.fml.common.registry.VillagerRegistry.getId(prof)));
        }

        @SuppressWarnings("deprecation")
        protected BlockState getBiomeSpecificBlockState(BlockState blockstateIn) {
            net.minecraftforge.event.terraingen.BiomeEvent.GetVillageBlockID event = new net.minecraftforge.event.terraingen.BiomeEvent.GetVillageBlockID(startPiece == null ? null : startPiece.biome, blockstateIn);
            net.minecraftforge.common.MinecraftForge.TERRAIN_GEN_BUS.post(event);
            if (event.getResult() == net.minecraftforge.fml.common.eventhandler.Event.Result.DENY)
                return event.getReplacement();
            if (this.field_189928_h == 1) {
                if (blockstateIn.getBlock() == Blocks.LOG || blockstateIn.getBlock() == Blocks.LOG2) {
                    return Blocks.SANDSTONE.getDefaultState();
                }

                if (blockstateIn.getBlock() == Blocks.COBBLESTONE) {
                    return Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.DEFAULT.getMetadata());
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
            } else if (this.field_189928_h == 3) {
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
            } else if (this.field_189928_h == 2) {
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

            return blockstateIn;
        }

        protected void replaceAirAndLiquidDownwards(World worldIn, BlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn) {
            BlockState BlockState = this.getBiomeSpecificBlockState(blockstateIn);
            super.replaceAirAndLiquidDownwards(worldIn, BlockState, x, y, z, boundingboxIn);
        }

        protected void func_189924_a(int p_189924_1_) {
            this.field_189928_h = p_189924_1_;
        }
    }

    public static class Well extends Village {

        private static final WorldGenCanopyTree TREE_GEN = new WorldGenCanopyTree(true);

        public Well() {
        }

        public Well(Start start, int type, Random rand, int x, int z) {
            super(start, type);
            this.setCoordBaseMode(Direction.Plane.HORIZONTAL.random(rand));

            if (this.getCoordBaseMode().getAxis() == Direction.Axis.Z) {
                this.boundingBox = new StructureBoundingBox(x, 64, z, x + 3, 64, z + 3);
            } else {
                this.boundingBox = new StructureBoundingBox(x, 64, z, x + 3, 64, z + 3);
            }
        }

        public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
            generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.maxY - 4, this.boundingBox.minZ + 2, Direction.WEST, this.getComponentType());
            generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 2, Direction.EAST, this.getComponentType());
            generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.minX + 2, this.boundingBox.maxY - 4, this.boundingBox.minZ, Direction.NORTH, this.getComponentType());
            generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.minX + 2, this.boundingBox.maxY - 4, this.boundingBox.maxZ + 1, Direction.SOUTH, this.getComponentType());
        }

        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

                if (this.averageGroundLvl < 0) {
                    return true;
                }

                this.boundingBox.offset(1, this.averageGroundLvl - this.boundingBox.maxY - 1, 1);
            }
            return true;
        }

        @Override
        protected void readStructureFromNBT(CompoundNBT tagCompound, TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound);
        }
    }

    public static class House extends Village {
        private boolean isTallHouse;
        private int tablePosition;

        public House() {
        }

        public House(Start start, int type, Random rand, StructureBoundingBox structurebb, Direction facing) {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = structurebb;
            this.isTallHouse = rand.nextBoolean();
            this.tablePosition = rand.nextInt(3);
        }

        public static House createPiece(Start start, List<StructureComponent> p_175853_1_, Random rand, int p_175853_3_, int p_175853_4_, int p_175853_5_, Direction facing, int p_175853_7_) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175853_3_, p_175853_4_, p_175853_5_, 0, 0, 0, 1, 1, 1, facing);
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175853_1_, structureboundingbox) == null ? new House(start, p_175853_7_, rand, structureboundingbox, facing) : null;
        }

        protected void writeStructureToNBT(CompoundNBT tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setInt("T", this.tablePosition);
            tagCompound.setBoolean("C", this.isTallHouse);
        }

        protected void readStructureFromNBT(CompoundNBT tagCompound) {
            super.readStructureFromNBT(tagCompound);
            this.tablePosition = tagCompound.getInt("T");
            this.isTallHouse = tagCompound.getBoolean("C");
        }

        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

                if (this.averageGroundLvl < 0) {
                    return true;
                }

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY, 0);
            }
            this.replaceAirAndLiquidDownwards(worldIn, Blocks.DIRT.getDefaultState(), 0, -1, 0, structureBoundingBoxIn);
            this.spawnVillagers(worldIn, structureBoundingBoxIn, 0, 1, 0, 1);
            return true;
        }

        @Override
        protected void readStructureFromNBT(CompoundNBT tagCompound, TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound);
        }
    }
}