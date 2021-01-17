package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockGarbage extends FallingBlock {

    public SpawnReason spawnReason;
    public double spawnRateModifier;

    public BlockGarbage(String name, double spawnRateModifier) {
        super(Block.Properties.create(Material.ORGANIC).sound(SoundType.GROUND).hardnessAndResistance(0.7F, 1.0F).tickRandomly());
        this.setRegistryName(RatsMod.MODID, name);
        this.spawnReason = SpawnReason.NATURAL;
        this.spawnRateModifier = spawnRateModifier;
    }

    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (random.nextFloat() <= RatConfig.garbageSpawnRate * spawnRateModifier) {
            EntityRat rat = new EntityRat(RatsEntityRegistry.RAT, worldIn);
            rat.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 0, 0);
            int i = worldIn.isThundering() ? worldIn.getNeighborAwareLightSubtracted(pos.up(), 10) : worldIn.getLight(pos.up());
            if (i <= random.nextInt(8) && !rat.isEntityInsideOpaqueBlock() && rat.isNotColliding(worldIn)) {
                rat.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(pos), spawnReason, null, null);
                onSpawnRat(rat);
                if (!worldIn.isRemote) {
                    worldIn.addEntity(rat);
                }
            }
        }
    }

    public void onSpawnRat(EntityRat rat){}

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".desc").mergeStyle(TextFormatting.GRAY));
    }

    public int getDustColor(BlockState state) {
        return 0X79695B;
    }

    @Deprecated
    public boolean canEntitySpawn(BlockState state, Entity entityIn) {
        return entityIn instanceof EntityRat;
    }

}
