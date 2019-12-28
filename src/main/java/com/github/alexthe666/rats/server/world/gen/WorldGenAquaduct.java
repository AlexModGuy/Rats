package com.github.alexthe666.rats.server.world.gen;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LogBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
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

public class WorldGenAquaduct extends Feature<NoFeatureConfig> {

    public Direction facing;
    int aquaDist = 0;
    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.WEST, Direction.SOUTH};
    public WorldGenAquaduct(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i49873_1_, Direction facing) {
        super(p_i49873_1_);
        this.facing = facing;
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
        BlockPos skyPos = new BlockPos(x, 256, z);
        while ((!world.getBlockState(skyPos).isOpaqueCube(world, skyPos) || canHeightSkipBlock(skyPos, world)) && skyPos.getY() > 1) {
            skyPos = skyPos.down();
        }
        return skyPos;
    }

    private static boolean canHeightSkipBlock(BlockPos pos, IWorld world) {
        BlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof LogBlock || !state.isSolid();
    }

    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        pos = pos.add(rand.nextInt(8) - 4, 1, rand.nextInt(8) - 4);
        BlockPos height = getGround(pos, worldIn);
        for (int i = 0; i < 2 + rand.nextInt(5); i++) {
            generateAquaduct(worldIn, facing, height.offset(facing.rotateY(), aquaDist));
        }
        return true;
    }

    private void generateAquaduct(IWorld worldIn, Direction facing, BlockPos height) {
        BlockState dirt = worldIn.getBlockState(height.down(2));
        if (!dirt.isSolid()) {
            dirt = Blocks.SAND.getDefaultState();
        }
        Random random = new Random(worldIn.getSeed());
        ServerWorld serverworld = (ServerWorld)worldIn;
        TemplateManager templateManager = serverworld.getStructureTemplateManager();
        Template template = templateManager.getTemplate(RatStructure.LARGE_AQUADUCT.structureLoc);
        PlacementSettings settings = new PlacementSettings().setRotation(getRotationFromFacing(facing));
        settings.addProcessor(new RatsStructureProcessor(0.75F * random.nextFloat() + 0.75F));
        BlockPos pos = height.offset(facing, template.getSize().getZ() / 2).offset(facing.rotateYCCW(), template.getSize().getX() / 2);
        template.addBlocksToWorld(worldIn, pos, settings, 2);
        for (BlockPos underPos : BlockPos.getAllInBox(
                height.down().offset(facing, -template.getSize().getZ() / 2).offset(facing.rotateYCCW(), -template.getSize().getX() / 2),
                height.down(3).offset(facing, template.getSize().getZ() / 2).offset(facing.rotateYCCW(), template.getSize().getX() / 2)
        ).collect(Collectors.toSet())) {
            if (!worldIn.getBlockState(underPos).isOpaqueCube(worldIn, underPos) && !worldIn.isAirBlock(underPos.up())) {
                worldIn.setBlockState(underPos, dirt, 3);
                worldIn.setBlockState(underPos.down(), dirt, 3);
                worldIn.setBlockState(underPos.down(2), dirt, 3);
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
        aquaDist += template.getSize().getX() - 1;
    }
}
