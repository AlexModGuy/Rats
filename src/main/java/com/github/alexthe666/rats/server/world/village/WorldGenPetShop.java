package com.github.alexthe666.rats.server.world.village;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.world.gen.RatsVillageProcessor;
import net.minecraft.entity.passive.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WorldGenPetShop extends WorldGenerator {

    private static final ResourceLocation STRUCTURE = new ResourceLocation(RatsMod.MODID, "village_pet_shop");
    private VillageComponentPetShop component;
    private Rotation rotation;
    private EnumFacing facing;
    public List<BlockPos> ocelotPos = new ArrayList<>();
    public List<BlockPos> wolfPos = new ArrayList<>();
    public List<BlockPos> rabbitPos = new ArrayList<>();
    public List<BlockPos> ratPos = new ArrayList<>();
    public List<BlockPos> parrotPos = new ArrayList<>();

    public WorldGenPetShop(VillageComponentPetShop component, EnumFacing facing){
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
        ocelotPos.clear();
        parrotPos.clear();
        wolfPos.clear();
        ratPos.clear();
        rabbitPos.clear();
        MinecraftServer server = worldIn.getMinecraftServer();
        TemplateManager templateManager = worldIn.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = new PlacementSettings().setRotation(rotation).setMirror(Mirror.NONE).setReplacedBlock(Blocks.AIR);
        Template template = templateManager.getTemplate(server, STRUCTURE);
        Biome biome = worldIn.getBiome(position);
        int xSize = template.getSize().getX() / 2;
        int zSize = template.getSize().getZ() / 2;
        template.addBlocksToWorld(worldIn, position.up(2), new RatsVillageProcessor(position.up(3), this, settings, null, biome), settings, 2);
        //.offset(EnumFacing.NORTH, xSize).offset(EnumFacing.SOUTH, zSize)
        for(BlockPos pos : ocelotPos){
            EntityOcelot ocelot = new EntityOcelot(worldIn);
            //ocelot.enablePersistence();
            ocelot.setPositionAndRotation(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, rand.nextFloat() * 360, 0);
            worldIn.spawnEntity(ocelot);
        }
        for(BlockPos pos : ratPos){
            EntityRat rat = new EntityRat(worldIn);
            //rat.enablePersistence();
            rat.setPlague(false);
            rat.setPositionAndRotation(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, rand.nextFloat() * 360, 0);
            worldIn.spawnEntity(rat);
        }
        for(BlockPos pos : rabbitPos){
            EntityAnimal rat = worldIn.rand.nextBoolean() ? new EntityRat(worldIn) : new EntityRabbit(worldIn);
            //rat.enablePersistence();
            rat.setPositionAndRotation(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, rand.nextFloat() * 360, 0);
            worldIn.spawnEntity(rat);
        }
        for(BlockPos pos : wolfPos){
            EntityWolf wolf = new EntityWolf(worldIn);
            //wolf.enablePersistence();
            wolf.setPositionAndRotation(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, rand.nextFloat() * 360, 0);
            worldIn.spawnEntity(wolf);
        }
        for(BlockPos pos : parrotPos){
            EntityParrot parrot = new EntityParrot(worldIn);
            parrot.setVariant(rand.nextInt(5));
            //parrot.enablePersistence();
            parrot.setPositionAndRotation(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, rand.nextFloat() * 360, 0);
            worldIn.spawnEntity(parrot);
        }
        ocelotPos.clear();
        parrotPos.clear();
        wolfPos.clear();
        ratPos.clear();
        rabbitPos.clear();
        return true;
    }
}
