package com.github.alexthe666.rats.server.world.gen;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class WorldGenAquaduct extends WorldGenerator {

    public EnumFacing facing;
    int aquaDist = 0;

    public WorldGenAquaduct(EnumFacing facing) {
        super(false);
        this.facing = facing;

    }

    public static Rotation getRotationFromFacing(EnumFacing facing) {
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

    public static BlockPos getGround(BlockPos pos, World world) {
        return getGround(pos.getX(), pos.getZ(), world);
    }

    public static BlockPos getGround(int x, int z, World world) {
        BlockPos skyPos = new BlockPos(x, 256, z);
        while ((!world.getBlockState(skyPos).isOpaqueCube() || canHeightSkipBlock(skyPos, world)) && skyPos.getY() > 1) {
            skyPos = skyPos.down();
        }
        return skyPos;
    }

    private static boolean canHeightSkipBlock(BlockPos pos, World world) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof BlockLog || state.getBlock() instanceof BlockLiquid;
    }

    public boolean generate(World worldIn, Random rand, BlockPos position) {
        position = position.add(rand.nextInt(8) - 4, 1, rand.nextInt(8) - 4);
        BlockPos height = getGround(position, worldIn);
        for (int i = 0; i < 2 + rand.nextInt(5); i++) {
            generateAquaduct(worldIn, facing, height.offset(facing.rotateY(), aquaDist));
        }
        return true;
    }

    private void generateAquaduct(World worldIn, EnumFacing facing, BlockPos height) {
        MinecraftServer server = worldIn.getMinecraftServer();
        IBlockState dirt = worldIn.getBlockState(height.down(2));
        if (!dirt.isFullBlock()) {
            dirt = Blocks.SAND.getDefaultState();
        }
        TemplateManager templateManager = worldIn.getSaveHandler().getStructureTemplateManager();
        Template template = templateManager.getTemplate(server, RatStructure.LARGE_AQUADUCT.structureLoc);
        PlacementSettings settings = new PlacementSettings().setRotation(getRotationFromFacing(facing));
        BlockPos pos = height.offset(facing, template.getSize().getZ() / 2).offset(facing.rotateYCCW(), template.getSize().getX() / 2);
        settings.setReplacedBlock(Blocks.AIR);
        template.addBlocksToWorld(worldIn, pos, new RatsStructureProcessor(0.75F * worldIn.rand.nextFloat() + 0.75F), settings, 2);
        for (BlockPos underPos : BlockPos.getAllInBox(
                height.down().offset(facing, -template.getSize().getZ() / 2).offset(facing.rotateYCCW(), -template.getSize().getX() / 2),
                height.down(3).offset(facing, template.getSize().getZ() / 2).offset(facing.rotateYCCW(), template.getSize().getX() / 2)
        )) {
            if (!worldIn.getBlockState(underPos).isOpaqueCube() && !worldIn.isAirBlock(underPos.up())) {
                worldIn.setBlockState(underPos, dirt);
                worldIn.setBlockState(underPos.down(), dirt);
                worldIn.setBlockState(underPos.down(2), dirt);
            }
        }
        for (BlockPos vinePos : BlockPos.getAllInBox(
                height.offset(facing, -template.getSize().getZ() / 2).offset(facing.rotateYCCW(), -template.getSize().getX() / 2),
                height.up(template.getSize().getY()).offset(facing, template.getSize().getZ() / 2).offset(facing.rotateYCCW(), template.getSize().getX() / 2)
        )) {
            if (worldIn.getBlockState(vinePos).isOpaqueCube()) {
                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL.facings()) {
                    if (!worldIn.getBlockState(vinePos.offset(enumfacing)).isOpaqueCube() && worldIn.rand.nextInt(8) == 0) {
                        EnumFacing opposFacing = enumfacing.getOpposite();
                        IBlockState iblockstate = Blocks.VINE.getDefaultState().withProperty(BlockVine.NORTH, Boolean.valueOf(opposFacing == EnumFacing.NORTH)).withProperty(BlockVine.EAST, Boolean.valueOf(opposFacing == EnumFacing.EAST)).withProperty(BlockVine.SOUTH, Boolean.valueOf(opposFacing == EnumFacing.SOUTH)).withProperty(BlockVine.WEST, Boolean.valueOf(opposFacing == EnumFacing.WEST));
                        worldIn.setBlockState(vinePos.offset(enumfacing), iblockstate, 2);
                    }
                }
            }
        }
        aquaDist += template.getSize().getX() - 1;
    }
}
