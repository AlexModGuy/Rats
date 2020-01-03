package com.github.alexthe666.rats.server.world.village;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityPlagueDoctor;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import com.github.alexthe666.rats.server.world.gen.RatsPlagueHutProcessor;
import com.github.alexthe666.rats.server.world.gen.FeatureRatlantisRuin;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class WorldGenPlagueDoctor {

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

    public boolean generate(World worldIn, Random rand, BlockPos position) {
        if (worldIn == null) {
            return false;
        }
        villagerPos = null;

        TemplateManager templateManager = ((ServerWorld)worldIn).getStructureTemplateManager();
        PlacementSettings settings = new PlacementSettings().setRotation(FeatureRatlantisRuin.getRotationFromFacing(facing));
        Template template = templateManager.getTemplate(STRUCTURE);
        Biome biome = worldIn.getBiome(position);
        BlockPos genPos = position.offset(facing, template.getSize().getZ() / 2).offset(facing.rotateYCCW(), template.getSize().getX() / 2);
        settings.addProcessor(new RatsPlagueHutProcessor(position.up(2), this, settings, biome));
        template.addBlocksToWorld(worldIn, genPos, settings, 2);
        if (villagerPos != null) {
            EntityPlagueDoctor doctor = new EntityPlagueDoctor(RatsEntityRegistry.PLAGUE_DOCTOR, worldIn);
            doctor.setLocationAndAngles(villagerPos.getX() + 0.5D, villagerPos.getY(), villagerPos.getZ() + 0.5D, 0, 0);
            doctor.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(villagerPos), SpawnReason.CHUNK_GENERATION, null, null);
            if (!worldIn.isRemote) {
                worldIn.addEntity(doctor);
            }
        }
        return true;
    }
}
