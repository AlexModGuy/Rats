package com.github.alexthe666.rats.server.world.gen;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LogBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FeatureRatlantisRuin extends Feature<NoFeatureConfig> {

    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.WEST, Direction.SOUTH};

    public FeatureRatlantisRuin(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i49873_1_) {
        super(p_i49873_1_);

    }

    public static boolean isPartOfRuin(BlockState state) {
        return state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_RAW || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_PILLAR
                || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_CHISELED || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_STAIRS || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_SLAB
                || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_BRICK || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_BRICK_CHISELED
                || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_STAIRS
                || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_BRICK_SLAB || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_TILE;
    }

    public static Rotation getRotationFromFacing(Direction facing) {
        switch (facing) {
            case EAST:
                return Rotation.CLOCKWISE_90;
            case SOUTH:
                return Rotation.CLOCKWISE_180;
            case WEST:
                return Rotation.COUNTERCLOCKWISE_90;
            default:
                return Rotation.NONE;
        }
    }

    public static BlockPos getGround(BlockPos pos, IWorld world) {
        return getGround(pos.getX(), pos.getZ(), world);
    }

    public static BlockPos getGround(int x, int z, IWorld world) {
        BlockPos skyPos = new BlockPos(x, world.getHeight(), z);
        while ((!world.getBlockState(skyPos).isOpaqueCube(world, skyPos) || canHeightSkipBlock(skyPos, world)) && skyPos.getY() > 1) {
            skyPos = skyPos.down();
        }
        return skyPos;
    }

    private static boolean canHeightSkipBlock(BlockPos pos, IWorld world) {
        BlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof LogBlock || !state.isSolid();
    }

    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos position, NoFeatureConfig config) {
        Direction facing = HORIZONTALS[rand.nextInt(HORIZONTALS.length - 1)];
        RatStructure model = RatStructure.HUT;
        int chance = rand.nextInt(99) + 1;
        BlockPos offsetPos = BlockPos.ZERO;
        Random random = new Random(worldIn.getSeed());
        if (chance < 10) {
            model = rand.nextBoolean() ? RatStructure.CHEESE_STATUETTE : RatStructure.GIANT_CHEESE;
        } else if (chance < 50) {
            switch (rand.nextInt(5)) {
                case 1:
                    model = RatStructure.PILLAR;
                    break;
                case 2:
                    model = RatStructure.PILLAR_LEANING;
                    offsetPos = new BlockPos(0, -2, 0);
                    break;
                case 3:
                    model = RatStructure.PILLAR_COLLECTION;
                    break;
                case 4:
                    model = RatStructure.PILLAR_THIN;
                    break;
                default:
                    model = RatStructure.PILLAR_LEANING;
                    offsetPos = new BlockPos(0, -2, 0);
                    break;
            }
        } else if (chance < 70) {
            switch (rand.nextInt(5)) {
                case 1:
                    model = RatStructure.TOWER;
                    break;
                case 2:
                    model = RatStructure.FORUM;
                    break;
                case 3:
                    model = RatStructure.HUT;
                    break;
                case 4:
                    model = RatStructure.PALACE;
                    break;
                default:
                    model = RatStructure.TEMPLE;
                    break;
            }
        } else if (chance < 96) {
            switch (rand.nextInt(4)) {
                case 1:
                    model = RatStructure.SPHINX;
                    break;
                case 2:
                    model = RatStructure.LINCOLN;
                    break;
                case 3:
                    model = RatStructure.CHEESE_STATUETTE;
                    break;
                default:
                    model = RatStructure.HEAD;
                    break;
            }
        } else {
            model = RatStructure.COLOSSUS;
        }
        ChunkPos chunkPos = new ChunkPos(position);
        BlockPos height = getGround(position, worldIn.getWorld());
        BlockState dirt = worldIn.getWorld().getBlockState(height.down(2));
        TemplateManager templateManager = ((ServerWorld)worldIn.getWorld()).getSaveHandler().getStructureTemplateManager();
        Template template = templateManager.getTemplate(model.structureLoc);
        if(template != null) {
            PlacementSettings settings = new PlacementSettings().setRotation(getRotationFromFacing(facing)).setRandom(random);
            settings.addProcessor(new RatsStructureProcessor(0.75F * rand.nextFloat() + 0.75F));
            BlockPos blockpos1 = template.getZeroPositionWithTransform(height, Mirror.NONE, getRotationFromFacing(facing));
            if (checkIfCanGenAt(worldIn.getWorld(), blockpos1, template.getSize().getX(), template.getSize().getZ(), facing)) {
                template.addBlocksToWorld(worldIn.getWorld(), blockpos1, settings, 2);
                System.out.println(blockpos1);
                System.out.println(blockpos1);
                for (BlockPos underPos : BlockPos.getAllInBox(
                        height.down().offset(facing, -template.getSize().getZ() / 2).offset(facing.rotateYCCW(), -template.getSize().getX() / 2),
                        height.down(3).offset(facing, template.getSize().getZ() / 2).offset(facing.rotateYCCW(), template.getSize().getX() / 2)
                ).collect(Collectors.toSet())) {
                    if (!worldIn.getBlockState(underPos).isOpaqueCube(worldIn, underPos) && !worldIn.isAirBlock(underPos.up())) {
                        worldIn.setBlockState(underPos, dirt, 2);
                        worldIn.setBlockState(underPos.down(), dirt, 2);
                        worldIn.setBlockState(underPos.down(2), dirt, 2);
                    }
                }

                for (BlockPos vinePos : BlockPos.getAllInBox(
                        height.offset(facing, -template.getSize().getZ() / 2).offset(facing.rotateYCCW(), -template.getSize().getX() / 2),
                        height.up(template.getSize().getY()).offset(facing, template.getSize().getZ() / 2).offset(facing.rotateYCCW(), template.getSize().getX() / 2)
                ).collect(Collectors.toSet())) {
                    if (worldIn.getBlockState(vinePos).isOpaqueCube(worldIn, vinePos)) {
                        for (Direction Direction : HORIZONTALS) {
                            if (!worldIn.getBlockState(vinePos.offset(Direction)).isOpaqueCube(worldIn, vinePos) && random.nextInt(8) == 0) {
                                Direction opposFacing = Direction.getOpposite();
                                BlockState BlockState = Blocks.VINE.getDefaultState().with(VineBlock.NORTH, Boolean.valueOf(opposFacing == net.minecraft.util.Direction.NORTH)).with(VineBlock.EAST, Boolean.valueOf(opposFacing == net.minecraft.util.Direction.EAST)).with(VineBlock.SOUTH, Boolean.valueOf(opposFacing == net.minecraft.util.Direction.SOUTH)).with(VineBlock.WEST, Boolean.valueOf(opposFacing == net.minecraft.util.Direction.WEST));
                                worldIn.setBlockState(vinePos.offset(Direction), BlockState, 2);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean checkIfCanGenAt(IWorld world, BlockPos middle, int x, int z, Direction facing) {
        return !isPartOfRuin(world.getBlockState(middle.offset(facing, z / 2))) && !isPartOfRuin(world.getBlockState(middle.offset(facing.getOpposite(), z / 2))) &&
                !isPartOfRuin(world.getBlockState(middle.offset(facing.rotateY(), x / 2))) && !isPartOfRuin(world.getBlockState(middle.offset(facing.rotateYCCW(), x / 2)));
    }
}