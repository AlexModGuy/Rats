package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockGarbage extends FallingBlock {
    public BlockGarbage() {
        super(Block.Properties.create(Material.ORGANIC).sound(SoundType.GROUND).hardnessAndResistance(0.7F, 1.0F));
        this.setRegistryName(RatsMod.MODID, "garbage_pile");
    }

    @Override
    public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
        if (random.nextFloat() <= 0.5) {
            EntityRat rat = new EntityRat(RatsEntityRegistry.RAT, worldIn);
            rat.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 0, 0);
            if (rat.canSpawn(worldIn, SpawnReason.NATURAL) && !rat.isEntityInsideOpaqueBlock() && rat.isNotColliding(worldIn)) {
                rat.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(pos), SpawnReason.NATURAL, null, null);
                if (!worldIn.isRemote) {
                    worldIn.addEntity(rat);
                }
            }
        }
    }


    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tile.rats.garbage_pile.desc"));
    }

    public int getDustColor(BlockState state) {
        return 0X79695B;
    }

    @Deprecated
    public boolean canEntitySpawn(BlockState state, Entity entityIn) {
        return entityIn instanceof EntityRat;
    }

}
