package com.github.alexthe666.rats.server.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class RatExplosion extends Explosion {
    /**
     * whether or not the explosion sets fire to blocks around it
     */
    private final boolean causesFire;
    /**
     * whether or not this explosion spawns smoke particles
     */
    private final boolean damagesTerrain;
    private final Random random;
    private final World world;
    private final double x;
    private final double y;
    private final double z;
    private final Entity exploder;
    private final float size;
    /**
     * A list of ChunkPositions of blocks affected by this explosion
     */
    private final List<BlockPos> affectedBlockPositions;
    /**
     * Maps players to the knockback vector applied by the explosion, to send to the client
     */
    private final Map<PlayerEntity, Vec3d> playerKnockbackMap;
    private final Vec3d position;

    @OnlyIn(Dist.CLIENT)
    public RatExplosion(World worldIn, Entity entityIn, double x, double y, double z, float size, List<BlockPos> affectedPositions) {
        this(worldIn, entityIn, x, y, z, size, false, true, affectedPositions);
    }

    @OnlyIn(Dist.CLIENT)
    public RatExplosion(World worldIn, Entity entityIn, double x, double y, double z, float size, boolean causesFire, boolean damagesTerrain, List<BlockPos> affectedPositions) {
        this(worldIn, entityIn, x, y, z, size, causesFire, damagesTerrain);
        this.affectedBlockPositions.addAll(affectedPositions);
    }

    public RatExplosion(World worldIn, Entity entityIn, double x, double y, double z, float size, boolean causesFire, boolean damagesTerrain) {
        super(worldIn, entityIn, x, y, z, size, causesFire, damagesTerrain);
        this.random = new Random();
        this.affectedBlockPositions = Lists.newArrayList();
        this.playerKnockbackMap = Maps.newHashMap();
        this.world = worldIn;
        this.exploder = entityIn;
        this.size = size;
        this.x = x;
        this.y = y;
        this.z = z;
        this.causesFire = causesFire;
        this.damagesTerrain = damagesTerrain;
        this.position = new Vec3d(this.x, this.y, this.z);
    }

    public void doExplosionA() {
        Set<BlockPos> set = Sets.newHashSet();
        int i = 16;

        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                for (int l = 0; l < 16; ++l) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d0 = (double) ((float) j / 15.0F * 2.0F - 1.0F);
                        double d1 = (double) ((float) k / 15.0F * 2.0F - 1.0F);
                        double d2 = (double) ((float) l / 15.0F * 2.0F - 1.0F);
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 = d0 / d3;
                        d1 = d1 / d3;
                        d2 = d2 / d3;
                        float f = this.size * (0.7F + this.world.rand.nextFloat() * 0.6F);
                        double d4 = this.x;
                        double d6 = this.y;
                        double d8 = this.z;

                        for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                            BlockPos blockpos = new BlockPos(d4, d6, d8);
                            BlockState BlockState = this.world.getBlockState(blockpos);

                            if (BlockState.getMaterial() != Material.AIR) {
                                float f2 = this.exploder != null ? this.exploder.getExplosionResistance(this, this.world, blockpos, BlockState) : BlockState.getBlock().getExplosionResistance(world, blockpos, null, this);
                                f -= (f2 + 0.3F) * 0.3F;
                            }

                            if (f > 0.0F && (this.exploder == null || this.exploder.canExplosionDestroyBlock(this, this.world, blockpos, BlockState, f))) {
                                set.add(blockpos);
                            }

                            d4 += d0 * 0.30000001192092896D;
                            d6 += d1 * 0.30000001192092896D;
                            d8 += d2 * 0.30000001192092896D;
                        }
                    }
                }
            }
        }

        this.affectedBlockPositions.addAll(set);
        float f3 = this.size * 2.0F;
        int k1 = MathHelper.floor(this.x - (double) f3 - 1.0D);
        int l1 = MathHelper.floor(this.x + (double) f3 + 1.0D);
        int i2 = MathHelper.floor(this.y - (double) f3 - 1.0D);
        int i1 = MathHelper.floor(this.y + (double) f3 + 1.0D);
        int j2 = MathHelper.floor(this.z - (double) f3 - 1.0D);
        int j1 = MathHelper.floor(this.z + (double) f3 + 1.0D);
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB((double) k1, (double) i2, (double) j2, (double) l1, (double) i1, (double) j1));
        net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.world, this, list, f3);
        Vec3d vec3d = new Vec3d(this.x, this.y, this.z);

        for (int k2 = 0; k2 < list.size(); ++k2) {
            Entity entity = list.get(k2);

            if (!entity.isImmuneToExplosions() && (exploder == null || !entity.isOnSameTeam(exploder))) {
                double d12 = entity.getDistance(this.x, this.y, this.z) / (double) f3;

                if (d12 <= 1.0D) {
                    double d5 = entity.posX - this.x;
                    double d7 = entity.posY + (double) entity.getEyeHeight() - this.y;
                    double d9 = entity.posZ - this.z;
                    double d13 = (double) MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);

                    if (d13 != 0.0D) {
                        d5 = d5 / d13;
                        d7 = d7 / d13;
                        d9 = d9 / d13;
                        double d14 = (double) this.world.getBlockDensity(vec3d, entity.getBoundingBox());
                        double d10 = (1.0D - d12) * d14;
                        entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), (float) ((int) ((d10 * d10 + d10) / 2.0D * 7.0D * (double) f3 + 1.0D)));
                        double d11 = d10;

                        if (entity instanceof LivingEntity) {
                            d11 = EnchantmentProtection.getBlastDamageReduction((LivingEntity) entity, d10);
                        }

                        entity.motionX += d5 * d11;
                        entity.motionY += d7 * d11;
                        entity.motionZ += d9 * d11;

                        if (entity instanceof PlayerEntity) {
                            PlayerEntity PlayerEntity = (PlayerEntity) entity;

                            if (!PlayerEntity.isSpectator() && (!PlayerEntity.isCreative() || !PlayerEntity.capabilities.isFlying)) {
                                this.playerKnockbackMap.put(PlayerEntity, new Vec3d(d5 * d10, d7 * d10, d9 * d10));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Does the second part of the explosion (sound, particles, drop spawn)
     */
    public void doExplosionB(boolean addParticles) {
        this.world.playSound(null, this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);

        if (this.size >= 2.0F && this.damagesTerrain) {
            this.world.addParticle(ParticleTypes.EXPLOSION_HUGE, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
        } else {
            this.world.addParticle(ParticleTypes.EXPLOSION_LARGE, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
        }

        if (this.damagesTerrain) {
            for (BlockPos blockpos : this.affectedBlockPositions) {
                BlockState BlockState = this.world.getBlockState(blockpos);
                Block block = BlockState.getBlock();

                if (addParticles) {
                    double d0 = (double) ((float) blockpos.getX() + this.world.rand.nextFloat());
                    double d1 = (double) ((float) blockpos.getY() + this.world.rand.nextFloat());
                    double d2 = (double) ((float) blockpos.getZ() + this.world.rand.nextFloat());
                    double d3 = d0 - this.x;
                    double d4 = d1 - this.y;
                    double d5 = d2 - this.z;
                    double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                    d3 = d3 / d6;
                    d4 = d4 / d6;
                    d5 = d5 / d6;
                    double d7 = 0.5D / (d6 / (double) this.size + 0.1D);
                    d7 = d7 * (double) (this.world.rand.nextFloat() * this.world.rand.nextFloat() + 0.3F);
                    d3 = d3 * d7;
                    d4 = d4 * d7;
                    d5 = d5 * d7;
                    this.world.addParticle(ParticleTypes.EXPLOSION_NORMAL, (d0 + this.x) / 2.0D, (d1 + this.y) / 2.0D, (d2 + this.z) / 2.0D, d3, d4, d5);
                    this.world.addParticle(ParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5);
                }

                if (BlockState.getMaterial() != Material.AIR) {
                    if (block.canDropFromExplosion(this)) {
                        block.dropBlockAsItemWithChance(this.world, blockpos, this.world.getBlockState(blockpos), 1.0F / this.size, 0);
                    }

                    block.onBlockExploded(this.world, blockpos, this);
                }
            }
        }

        if (this.causesFire) {
            for (BlockPos blockpos1 : this.affectedBlockPositions) {
                if (this.world.getBlockState(blockpos1).getMaterial() == Material.AIR && this.world.getBlockState(blockpos1.down()).isFullBlock() && this.random.nextInt(3) == 0) {
                    this.world.setBlockState(blockpos1, Blocks.FIRE.getDefaultState());
                }
            }
        }
    }

    public Map<PlayerEntity, Vec3d> getPlayerKnockbackMap() {
        return this.playerKnockbackMap;
    }

    /**
     * Returns either the entity that placed the explosive block, the entity that caused the explosion or null.
     */
    @Nullable
    public LivingEntity getExplosivePlacedBy() {
        if (this.exploder == null) {
            return null;
        } else if (this.exploder instanceof EntityTNTPrimed) {
            return ((EntityTNTPrimed) this.exploder).getTntPlacedBy();
        } else {
            return this.exploder instanceof LivingEntity ? (LivingEntity) this.exploder : null;
        }
    }

    public void clearAffectedBlockPositions() {
        this.affectedBlockPositions.clear();
    }

    public List<BlockPos> getAffectedBlockPositions() {
        return this.affectedBlockPositions;
    }

    public Vec3d getPosition() {
        return this.position;
    }
}
