package com.github.alexthe666.rats.server.world.village;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.world.gen.RatsVillageProcessor;
import com.github.alexthe666.rats.server.world.gen.WorldGenRatRuin;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class WorldGenGarbageHeap extends WorldGenerator {

    private static final ResourceLocation STRUCTURE = new ResourceLocation(RatsMod.MODID, "village_garbage_heap");
    private VillageComponentGarbageHeap component;
    private Rotation rotation;
    private EnumFacing facing;
    public WorldGenGarbageHeap(VillageComponentGarbageHeap component, EnumFacing facing){
        this.component = component;
        this.facing = facing;
        switch(facing){
            case SOUTH:
                rotation = Rotation.CLOCKWISE_180;
                break;
            case EAST:
                rotation = Rotation.CLOCKWISE_90;
                break;
            case WEST:
                rotation = Rotation.COUNTERCLOCKWISE_90;
                break;
            default:
                rotation = Rotation.NONE;
                break;
        }
    }
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        if (worldIn == null) {
            return false;
        }
        MinecraftServer server = worldIn.getMinecraftServer();
        TemplateManager templateManager = worldIn.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = new PlacementSettings().setRotation(WorldGenRatRuin.getRotationFromFacing(facing)).setReplacedBlock(Blocks.AIR);
        Template template = templateManager.getTemplate(server, STRUCTURE);
        Biome biome = worldIn.getBiome(position);
        BlockPos genPos = position.down(3).offset(facing, template.getSize().getZ()).offset(facing.rotateYCCW(), template.getSize().getX());
        template.addBlocksToWorld(worldIn, genPos, new RatsVillageProcessor(position.up(3), null, settings, biome), settings, 2);
        return true;
    }
}
