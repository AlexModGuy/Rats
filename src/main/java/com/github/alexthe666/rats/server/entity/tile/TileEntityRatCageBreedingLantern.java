package com.github.alexthe666.rats.server.entity.tile;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileEntityRatCageBreedingLantern extends TileEntityRatCageDecorated implements ITickableTileEntity {

    public int breedingCooldown = 0;
    private Random random;

    public TileEntityRatCageBreedingLantern() {
        super(RatsTileEntityRegistry.RAT_CAGE_BREEDING_LANTERN);
        random = new Random();
    }

    public ItemStack getContainedItem() {
        return new ItemStack(RatsItemRegistry.RAT_BREEDING_LANTERN);
    }

    public void setContainedItem(ItemStack stack) {
    }

    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("BreedingCooldown", breedingCooldown);
        return super.write(compound);
    }

    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        breedingCooldown = compound.getInt("BreedingCooldown");
    }


    @Override
    public void tick() {
        float f = (24000.0F - breedingCooldown) / 24000.0F;
        float f1 = f * 0.6F + 0.4F;
        float f2 = Math.max(0.0F, f * f * 0.7F - 0.5F);
        float f3 = Math.max(0.0F, f * f * 0.6F - 0.7F);
        float i = this.getPos().getX() + 0.5F;
        float j = this.getPos().getY() + 0.5F;
        float k = this.getPos().getZ() + 0.5F;
        if (breedingCooldown <= 0) {
            double d0 = 1.5F;
            List<EntityRat> rats = world.getEntitiesWithinAABB(EntityRat.class, new AxisAlignedBB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0));
            if (rats.size() < RatConfig.ratCageCramming && rats.size() > 0) {
                List<EntityRat> males = new ArrayList<>();
                List<EntityRat> females = new ArrayList<>();
                for (EntityRat rat : rats) {
                    if (!rat.isChild() && rat.isInCage() && rat.breedCooldown == 0) {
                        if (rat.isMale()) {
                            males.add(rat);
                        } else {
                            females.add(rat);
                        }
                    }
                }
                if (males.size() > 0 && females.size() > 0) {
                    EntityRat male = males.get(0);
                    EntityRat female = females.get(0);
                    if (males.size() > 1) {
                        male = males.get(random.nextInt(males.size() - 1));
                    }
                    if (females.size() > 1) {
                        female = females.get(random.nextInt(females.size() - 1));
                    }
                    male.world.setEntityState(male, (byte) 83);
                    female.world.setEntityState(female, (byte) 83);
                    female.createBabiesFrom(female, male);
                    breedingCooldown = 24000;
                    male.breedCooldown = 24000;
                    female.breedCooldown = 24000;
                }
            }
        } else {
            breedingCooldown--;
        }
        if(world.isRemote){
            world.addParticle(RedstoneParticleData.REDSTONE_DUST, i + random.nextDouble() - 0.5D, j + random.nextDouble() - 0.5D, k + random.nextDouble() - 0.5D, f1, f2, f3);
        }
    }
}
