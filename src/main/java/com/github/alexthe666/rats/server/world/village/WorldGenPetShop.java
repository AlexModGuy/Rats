package com.github.alexthe666.rats.server.world.village;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.world.gen.RatsVillageProcessor;
import com.github.alexthe666.rats.server.world.gen.WorldGenRatRuin;
import net.minecraft.entity.IEntityLivingData;
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

import java.util.*;

public class WorldGenPetShop extends WorldGenerator {

    private static final ResourceLocation STRUCTURE = new ResourceLocation(RatsMod.MODID, "village_petshop");
    public static final ResourceLocation UPSTAIRS_LOOT = new ResourceLocation(RatsMod.MODID, "pet_shop_upstairs");
    public static final ResourceLocation LOOT = new ResourceLocation(RatsMod.MODID, "pet_shop");
    private VillageComponentPetShop component;
    private Rotation rotation;
    private EnumFacing facing;
    public List<BlockPos> ocelotPos = new ArrayList<>();
    public List<BlockPos> wolfPos = new ArrayList<>();
    public List<BlockPos> rabbitPos = new ArrayList<>();
    public List<BlockPos> ratPos = new ArrayList<>();
    public List<BlockPos> parrotPos = new ArrayList<>();
    public List<BlockPos> villagerPos = new ArrayList<>();

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
        villagerPos.clear();
        rabbitPos.clear();
        MinecraftServer server = worldIn.getMinecraftServer();
        TemplateManager templateManager = worldIn.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = new PlacementSettings().setRotation(WorldGenRatRuin.getRotationFromFacing(facing)).setReplacedBlock(Blocks.AIR);
        Template template = templateManager.getTemplate(server, STRUCTURE);
        Biome biome = worldIn.getBiome(position);
        int spawnedVillager = 0;
        BlockPos genPos = position.up(2).offset(facing, template.getSize().getZ()).offset(facing.rotateYCCW(), template.getSize().getX());
        template.addBlocksToWorld(worldIn, genPos, new RatsVillageProcessor(position.up(3), this, settings, biome), settings, 2);
        //.offset(EnumFacing.NORTH, xSize).offset(EnumFacing.SOUTH, zSize)
        Iterator<BlockPos> ocelotItr = ocelotPos.iterator();
        while(ocelotItr.hasNext()){
            BlockPos pos = ocelotItr.next();
            if(worldIn.rand.nextInt(3) == 0){
                EntityOcelot ocelot = new EntityOcelot(worldIn);
                ocelot.enablePersistence();
                ocelot.setPositionAndRotation(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, rand.nextFloat() * 360, 0);
                worldIn.spawnEntity(ocelot);
            }
            ocelotItr.remove();
        }
        Iterator<BlockPos> ratItr = ratPos.iterator();
        while(ratItr.hasNext()){
            BlockPos pos = ratItr.next();
            EntityRat rat = new EntityRat(worldIn);
            rat.onInitialSpawn(worldIn.getDifficultyForLocation(pos), null);
            rat.setPlague(false);
            rat.enablePersistence();
            rat.setPositionAndRotation(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, rand.nextFloat() * 360, 0);
            worldIn.spawnEntity(rat);
            ratItr.remove();
        }

        Iterator<BlockPos> rabbitItr = rabbitPos.iterator();
        while(rabbitItr.hasNext()){
            BlockPos pos = rabbitItr.next();
            if(worldIn.rand.nextInt(3) == 0) {
                EntityAnimal rat = worldIn.rand.nextBoolean() ? new EntityRat(worldIn) : new EntityRabbit(worldIn);
                rat.enablePersistence();
                rat.setPositionAndRotation(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, rand.nextFloat() * 360, 0);
                worldIn.spawnEntity(rat);
            }
            rabbitItr.remove();
        }

        Iterator<BlockPos> wolfItr = wolfPos.iterator();
        while(wolfItr.hasNext()){
            BlockPos pos = wolfItr.next();
            if(worldIn.rand.nextInt(3) == 0) {
                EntityWolf wolf = new EntityWolf(worldIn);
                wolf.enablePersistence();
                wolf.setPositionAndRotation(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, rand.nextFloat() * 360, 0);
                worldIn.spawnEntity(wolf);
            }
            wolfItr.remove();
        }

        Iterator<BlockPos> parrotItr = parrotPos.iterator();
        while(parrotItr.hasNext()){
            BlockPos pos = parrotItr.next();
            EntityParrot parrot = new EntityParrot(worldIn);
            parrot.enablePersistence();
            parrot.setPositionAndRotation(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, rand.nextFloat() * 360, 0);
            worldIn.spawnEntity(parrot);
            parrotItr.remove();
        }
        Iterator<BlockPos> villageItr = villagerPos.iterator();
        while(villageItr.hasNext() && spawnedVillager < 1){
            BlockPos pos = villageItr.next();
            EntityVillager villager = new EntityVillager(worldIn);
            villager.onInitialSpawn(worldIn.getDifficultyForLocation(pos), null);
            villager.setProfession(RatsVillageRegistry.PET_SHOP_OWNER);
            villager.finalizeMobSpawn(worldIn.getDifficultyForLocation(new BlockPos(villager)), (IEntityLivingData)null, false);
            villager.setPositionAndRotation(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, rand.nextFloat() * 360, 0);
            worldIn.spawnEntity(villager);
            villageItr.remove();
            spawnedVillager++;
        }
        ocelotPos.clear();
        parrotPos.clear();
        wolfPos.clear();
        ratPos.clear();
        villagerPos.clear();
        rabbitPos.clear();
        return true;
    }
}
