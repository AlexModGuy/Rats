package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatlantisPortal;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.stream.Collectors;

public class BlockRatlantisPortal extends ContainerBlock implements IUsesTEISR {

    protected BlockRatlantisPortal() {
        super(Block.Properties.create(Material.PORTAL).sound(SoundType.GROUND).hardnessAndResistance(-1.0F).func_235838_a_((p) -> 15).doesNotBlockMovement());
        this.setRegistryName(RatsMod.MODID, "ratlantis_portal");
        //GameRegistry.registerTileEntity(TileEntityRatlantisPortal.class, "rats.ratlantis_portal");
    }

    public BlockRenderType getRenderType(BlockState p_149645_1_) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public RegistryKey<World> getRatlantisDimension(){
        ResourceLocation resourcelocation = new ResourceLocation("rats:ratlantis");
        RegistryKey<World> registrykey = RegistryKey.func_240903_a_(Registry.field_239699_ae_, resourcelocation);
        return registrykey;
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entity) {
        if (!RatConfig.disableRatlantis && !worldIn.isRemote) {
            MinecraftServer server = worldIn.getServer();
            if ((!entity.isBeingRidden()) && (entity.getPassengers().isEmpty())) {
                boolean inOverworld = entity.world.func_234923_W_() != getRatlantisDimension();
                if ((entity instanceof ServerPlayerEntity)) {
                    ServerPlayerEntity thePlayer = (ServerPlayerEntity) entity;
                    if (thePlayer.timeUntilPortal > 0) {
                        thePlayer.timeUntilPortal = 10;
                    }
                    else if (inOverworld) {
                        thePlayer.timeUntilPortal = 10;
                        ServerWorld dimWorld = server.getWorld(getRatlantisDimension());
                        if(dimWorld != null){
                            teleportEntity(thePlayer, dimWorld, pos, true);
                        }
                    } else {
                        thePlayer.timeUntilPortal = 10;
                        ServerWorld dimWorld = server.getWorld(World.field_234918_g_);
                        if(dimWorld != null){
                            teleportEntity(thePlayer, dimWorld, pos, false);
                        }
                    }
                }
                if (!(entity instanceof PlayerEntity)) {
                    if (inOverworld) {
                        entity.timeUntilPortal = 10;
                        ServerWorld dimWorld = server.getWorld(getRatlantisDimension());
                        if(dimWorld != null){
                            teleportEntity(entity, dimWorld, pos, true);
                        }
                    } else {
                        ServerWorld dimWorld = server.getWorld(World.field_234918_g_);
                        entity.timeUntilPortal = 10;
                        if(dimWorld != null){
                            teleportEntity(entity, dimWorld, pos, false);
                        }
                    }
                }
            }
        }
    }


    private Entity teleportEntity(Entity entity, ServerWorld endpointWorld, BlockPos endpoint, boolean ratlantis) {
        if(ratlantis){
            endpoint = new BlockPos(0, 112, 0);
            placeInPortal(entity, endpointWorld);
        }else{
            if (entity instanceof PlayerEntity && ((PlayerEntity) entity).getBedPosition().isPresent()) {
                BlockPos bedPos = ((PlayerEntity) entity).getBedPosition().get();
                endpoint = bedPos;
                entity.setLocationAndAngles(bedPos.getX() + 0.5D, bedPos.getY() + 1.5D, bedPos.getZ() + 0.5D, 0.0F, 0.0F);
            } else {
                BlockPos height = entity.world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, new BlockPos(entity.getPositionVec()));
                endpoint = height;
                entity.setLocationAndAngles(height.getX() + 0.5D, height.getY() + 0.5D, height.getZ() + 0.5D, entity.rotationYaw, 0.0F);
            }
        }
        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            player.teleport(endpointWorld, endpoint.getX() + 0.5D, endpoint.getY() + 0.5D, endpoint.getZ() + 0.5D, entity.rotationYaw, entity.rotationPitch);
            return player;
        }

        entity.detach();
        entity.func_241206_a_(endpointWorld);
        Entity teleportedEntity = entity.getType().create(endpointWorld);
        if (teleportedEntity == null) {
            return entity;
        }
        teleportedEntity.copyDataFromOld(entity);
        teleportedEntity.setLocationAndAngles(endpoint.getX() + 0.5D, endpoint.getY() + 0.5D, endpoint.getZ() + 0.5D, entity.rotationYaw, entity.rotationPitch);
        teleportedEntity.setRotationYawHead(entity.rotationYaw);
        endpointWorld.addFromAnotherDimension(teleportedEntity);
        return teleportedEntity;
    }

    public void placeInPortal(Entity entity, ServerWorld serverWorld) {
        entity.setPositionAndRotation(0, 110, 0, 0, 0);
        entity.setMotion(0, 0, 0);
        BlockPos portalBottom = new BlockPos(1, 111, 1);
        for (BlockPos pos : BlockPos.getAllInBox(portalBottom.add(-2, 0, -2), portalBottom.add(2, 0, 2)).map(BlockPos::toImmutable).collect(Collectors.toList())) {
            serverWorld.setBlockState(pos, RatsBlockRegistry.MARBLED_CHEESE_TILE.getDefaultState());
            serverWorld.setBlockState(pos.up(4), RatsBlockRegistry.MARBLED_CHEESE_TILE.getDefaultState());
        }
        for (int i = 1; i < 4; i++) {
            serverWorld.setBlockState(portalBottom.add(2, 0, 2).up(i), RatsBlockRegistry.MARBLED_CHEESE_PILLAR.getDefaultState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y));
            serverWorld.setBlockState(portalBottom.add(2, 0, -2).up(i), RatsBlockRegistry.MARBLED_CHEESE_PILLAR.getDefaultState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y));
            serverWorld.setBlockState(portalBottom.add(-2, 0, 2).up(i), RatsBlockRegistry.MARBLED_CHEESE_PILLAR.getDefaultState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y));
            serverWorld.setBlockState(portalBottom.add(-2, 0, -2).up(i), RatsBlockRegistry.MARBLED_CHEESE_PILLAR.getDefaultState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y));
        }
        serverWorld.setBlockState(portalBottom, RatsBlockRegistry.MARBLED_CHEESE_RAW.getDefaultState());
        serverWorld.setBlockState(portalBottom.up(), RatsBlockRegistry.RATLANTIS_PORTAL.getDefaultState());
        serverWorld.setBlockState(portalBottom.up(2), RatsBlockRegistry.RATLANTIS_PORTAL.getDefaultState());
        serverWorld.setBlockState(portalBottom.up(3), RatsBlockRegistry.MARBLED_CHEESE_RAW.getDefaultState());
    }


    public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand) {
        if (!this.canSurviveAt(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        }
    }

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!this.canSurviveAt(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        }
    }

    public boolean canSurviveAt(World world, BlockPos pos) {
        return (world.getBlockState(pos.up()).getBlock() == RatsBlockRegistry.RATLANTIS_PORTAL || world.getBlockState(pos.up()).getBlock() == RatsBlockRegistry.MARBLED_CHEESE_RAW) &&
                (world.getBlockState(pos.down()).getBlock() == RatsBlockRegistry.RATLANTIS_PORTAL || world.getBlockState(pos.down()).getBlock() == RatsBlockRegistry.MARBLED_CHEESE_RAW);
    }

    public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
        Block block = adjacentBlockState.getBlock();
        return block != RatsBlockRegistry.RATLANTIS_PORTAL;
    }

    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    public boolean isFullCube(BlockState state) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityRatlantisPortal) {
            int i = 4;
            for (int j = 0; j < i; ++j) {
                double d0 = (double) ((float) pos.getX() + rand.nextFloat());
                double d1 = (double) ((float) pos.getY() + rand.nextFloat());
                double d2 = (double) ((float) pos.getZ() + rand.nextFloat());
                double d3 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
                double d4 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
                double d5 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
                int k = rand.nextInt(2) * 2 - 1;
                worldIn.addParticle(ParticleTypes.END_ROD, d0, d1, d2, d3, d4, d5);
            }
        }
    }

    public ItemStack getItem(World worldIn, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityRatlantisPortal();
    }
}