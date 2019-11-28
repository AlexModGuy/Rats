package com.github.alexthe666.rats.server.world.village;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityPlagueDoctor;
import com.github.alexthe666.rats.server.world.gen.RatsPlagueHutProcessor;
import com.github.alexthe666.rats.server.world.gen.WorldGenRatRuin;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
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

public class WorldGenPlagueDoctor extends WorldGenerator {

    public static final ResourceLocation LOOT = new ResourceLocation(RatsMod.MODID, "plague_doctor_house");
    private static final ResourceLocation STRUCTURE = new ResourceLocation(RatsMod.MODID, "village_plague_doctor_house");
    public BlockPos villagerPos;
    private VillageComponentPlagueDoctor component;
    private Rotation rotation;
    private Direction facing;

    public WorldGenPlagueDoctor(VillageComponentPlagueDoctor component, Direction facing) {
        this.component = component;
        this.facing = facing;
        switch (facing) {
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
        villagerPos = null;
        MinecraftServer server = worldIn.getMinecraftServer();
        TemplateManager templateManager = worldIn.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = new PlacementSettings().setRotation(WorldGenRatRuin.getRotationFromFacing(facing)).setReplacedBlock(Blocks.AIR);
        Template template = templateManager.getTemplate(server, STRUCTURE);
        Biome biome = worldIn.getBiome(position);
        BlockPos genPos = position.offset(facing, template.getSize().getZ() / 2).offset(facing.rotateYCCW(), template.getSize().getX() / 2);
        template.addBlocksToWorld(worldIn, genPos, new RatsPlagueHutProcessor(position.up(2), this, settings, biome), settings, 2);
        if (villagerPos != null) {
            EntityPlagueDoctor doctor = new EntityPlagueDoctor(worldIn);
            doctor.setLocationAndAngles(villagerPos.getX() + 0.5D, villagerPos.getY(), villagerPos.getZ() + 0.5D, 0, 0);
            doctor.onInitialSpawn(worldIn.getDifficultyForLocation(villagerPos), null);
            if (!worldIn.isRemote) {
                worldIn.addEntity(doctor);
            }
        }
        return true;
    }
}
