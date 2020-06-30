package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityDutchrat;
import com.github.alexthe666.rats.server.entity.EntityRatBaron;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.sound.SoundEvent;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockAirRaidSiren extends Block {

    private static final VoxelShape AABB = Block.makeCuboidShape(6, 0, 6, 10, 16, 10);

    public BlockAirRaidSiren() {
        super(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(5.0F, 1000.0F).variableOpacity());
        this.setRegistryName(RatsMod.MODID, "air_raid_siren");
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AABB;
    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
        this.playSound(player, worldIn, pos);
        if(!worldIn.isRemote){
            LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(worldIn);
            lightningboltentity.func_233576_c_(Vector3d.func_237492_c_(pos));
            lightningboltentity.func_233623_a_(true);
            worldIn.addEntity(lightningboltentity);
            worldIn.setBlockState(pos, Blocks.OAK_FENCE.getDefaultState());
            EntityRatBaron baron = new EntityRatBaron(RatsEntityRegistry.RAT_BARON, worldIn);
            baron.setPosition(pos.getX() + 0.5D, pos.getY() + 5D, pos.getZ() + 0.5D);
            baron.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(pos), SpawnReason.MOB_SUMMONED, null, null);
            worldIn.addEntity(baron);
        }
        for(int i = 0; i < 2; i++){
            Random rand = worldIn.rand;
            worldIn.addEntity(new ItemEntity(worldIn, pos.getX() + 0.5D + (rand.nextFloat() - 0.5D) * 3, pos.getY() - 1, pos.getZ() + 0.5D + (rand.nextFloat() - 0.5D) * 3, new ItemStack(Items.IRON_INGOT)));
        }
        return ActionResultType.SUCCESS;
    }

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isRemote) {
            boolean flag = worldIn.isBlockPowered(pos);
            if(flag){
                worldIn.playSound(null, pos, RatsSoundRegistry.AIR_RAID_SIREN, SoundCategory.BLOCKS, 1, 1);
                if(!worldIn.isRemote){
                    LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(worldIn);
                    lightningboltentity.func_233576_c_(Vector3d.func_237492_c_(pos));
                    lightningboltentity.func_233623_a_(true);
                    worldIn.addEntity(lightningboltentity);
                    worldIn.setBlockState(pos, Blocks.OAK_FENCE.getDefaultState());
                    EntityRatBaron baron = new EntityRatBaron(RatsEntityRegistry.RAT_BARON, worldIn);
                    baron.setPosition(pos.getX() + 0.5D, pos.getY() + 5D, pos.getZ() + 0.5D);
                    baron.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(pos), SpawnReason.MOB_SUMMONED, null, null);
                    worldIn.addEntity(baron);
                }
                for(int i = 0; i < 2; i++){
                    Random rand = worldIn.rand;
                    worldIn.addEntity(new ItemEntity(worldIn, pos.getX() + 0.5D + (rand.nextFloat() - 0.5D) * 3, pos.getY() - 1, pos.getZ() + 0.5D + (rand.nextFloat() - 0.5D) * 3, new ItemStack(Items.IRON_INGOT)));
                }
            }

        }
    }

    protected void playSound(@Nullable PlayerEntity player, World worldIn, BlockPos pos) {
        worldIn.playSound(player, pos, RatsSoundRegistry.AIR_RAID_SIREN, SoundCategory.BLOCKS, 1, 1);


    }

    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    public boolean isFullCube(BlockState state) {
        return false;
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }


}
