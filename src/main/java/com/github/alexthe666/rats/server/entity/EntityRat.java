package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.api.RatServerEvent;
import com.github.alexthe666.rats.server.blocks.BlockRatCage;
import com.github.alexthe666.rats.server.blocks.BlockRatHole;
import com.github.alexthe666.rats.server.blocks.BlockRatTube;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.ai.*;
import com.github.alexthe666.rats.server.entity.ai.navigation.*;
import com.github.alexthe666.rats.server.entity.ratlantis.*;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCraftingTable;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatHole;
import com.github.alexthe666.rats.server.inventory.ContainerRat;
import com.github.alexthe666.rats.server.inventory.RatInvListener;
import com.github.alexthe666.rats.server.items.*;
import com.github.alexthe666.rats.server.message.MessageCheeseStaffRat;
import com.github.alexthe666.rats.server.message.MessageDancingRat;
import com.github.alexthe666.rats.server.message.MessageSyncThrownBlock;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import com.github.alexthe666.rats.server.pathfinding.IPassabilityNavigator;
import com.github.alexthe666.rats.server.pathfinding.RatAdvancedPathNavigate;
import com.github.alexthe666.rats.server.pathfinding.pathjobs.ICustomSizeNavigator;
import com.github.alexthe666.rats.server.recipes.RatsRecipeRegistry;
import com.github.alexthe666.rats.server.recipes.SharedRecipe;
import com.google.common.base.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntitySenses;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EntityRat extends TameableEntity implements IAnimatedEntity, IRatlantean, IPassabilityNavigator, ICustomSizeNavigator {

    public static final Animation ANIMATION_EAT = Animation.create(10);
    public static final Animation ANIMATION_IDLE_SCRATCH = Animation.create(25);
    public static final Animation ANIMATION_IDLE_SNIFF = Animation.create(20);
    public static final Animation ANIMATION_DANCE_0 = Animation.create(35);
    public static final Animation ANIMATION_DANCE_1 = Animation.create(30);
    public static final ResourceLocation CHRISTMAS_LOOT = new ResourceLocation("rats", "christmas_rat_gifts");
    protected static final DataParameter<Optional<UUID>> MONSTER_OWNER_UNIQUE_ID = EntityDataManager.createKey(EntityRat.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<Boolean> SITTING = EntityDataManager.createKey(EntityRat.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_MALE = EntityDataManager.createKey(EntityRat.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> TOGA = EntityDataManager.createKey(EntityRat.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PLAGUE = EntityDataManager.createKey(EntityRat.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> VISUAL_FLAG = EntityDataManager.createKey(EntityRat.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> TAMED_BY_MONSTER = EntityDataManager.createKey(EntityRat.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> COMMAND = EntityDataManager.createKey(EntityRat.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> COLOR_VARIANT = EntityDataManager.createKey(EntityRat.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> DANCING = EntityDataManager.createKey(EntityRat.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> DANCE_MOVES = EntityDataManager.createKey(EntityRat.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> HELD_RF = EntityDataManager.createKey(EntityRat.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> RESPAWN_COUNTDOWN = EntityDataManager.createKey(EntityRat.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> HAS_CUSTOM_RADIUS = EntityDataManager.createKey(EntityRat.class, DataSerializers.BOOLEAN);
    private static final DataParameter<BlockPos> RADIUS_CENTER = EntityDataManager.createKey(EntityRat.class, DataSerializers.BLOCK_POS);
    private static final DataParameter<Integer> SEARCH_RADIUS = EntityDataManager.createKey(EntityRat.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> DYED = EntityDataManager.createKey(EntityRat.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_IN_WHEEL = EntityDataManager.createKey(EntityRat.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Byte> DYE_COLOR = EntityDataManager.createKey(EntityRat.class, DataSerializers.BYTE);
    private static final DataParameter<Optional<BlockPos>> PICKUP_POS = EntityDataManager.createKey(EntityRat.class, DataSerializers.OPTIONAL_BLOCK_POS);
    private static final DataParameter<Optional<BlockPos>> DEPOSIT_POS = EntityDataManager.createKey(EntityRat.class, DataSerializers.OPTIONAL_BLOCK_POS);
    private static final ResourceLocation PLAGUE_RAT_LOOT_TABLE = new ResourceLocation("rats", "entities/plague_rat");
    private static final String[] RAT_TEXTURES = new String[]{
            "rats:textures/entity/rat/rat_blue.png",
            "rats:textures/entity/rat/rat_black.png",
            "rats:textures/entity/rat/rat_brown.png",
            "rats:textures/entity/rat/rat_green.png",
            "rats:textures/entity/rat/tamed/rat_albino.png",
            "rats:textures/entity/rat/tamed/rat_hooded.png",
            "rats:textures/entity/rat/tamed/rat_hooded_brown.png",
            "rats:textures/entity/rat/tamed/rat_hooded_gray.png",
            "rats:textures/entity/rat/tamed/rat_siamese.png",
            "rats:textures/entity/rat/tamed/rat_white.png",
            "rats:textures/entity/rat/tamed/rat_hooded_yellow.png",
            "rats:textures/entity/rat/tamed/rat_brown_undercoat.png",
            "rats:textures/entity/rat/tamed/rat_dark_undercoat.png"
    };
    private static final SoundEvent[] CRAFTING_SOUNDS = new SoundEvent[]{SoundEvents.BLOCK_ANVIL_USE, SoundEvents.BLOCK_WOOD_BREAK, SoundEvents.ENTITY_LLAMA_EAT, SoundEvents.BLOCK_LADDER_HIT, SoundEvents.ENTITY_HORSE_SADDLE,
            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, SoundEvents.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR};
    public float sitProgress;
    public float holdProgress;
    public float deadInTrapProgress;
    public float sleepProgress;
    public boolean isDeadInTrap;
    public BlockPos fleePos;
    public boolean holdInMouth = true;
    public int wildTrust = 0;
    public Direction depositFacing = Direction.UP;
    public int cheeseFeedings = 0;
    public boolean climbingTube = false;
    public boolean waterBased = false;
    public boolean crafting = false;
    public int cookingProgress = 0;
    public int coinCooldown = 0;
    public int breedCooldown = 0;
    public float flyingPitch;
    public float prevFlyingPitch;
    public BlockPos jukeboxPos;
    public boolean isFleeing = false;
    public FluidStack transportingFluid = FluidStack.EMPTY;
    public int mountRespawnCooldown = 0;
    public Goal aiHarvest;
    public Goal aiPickup;
    public Goal aiDeposit;
    public Goal aiAttack;
    protected Inventory ratInventory;
    /*
       0 = tamed navigator
       1 = wild navigator
       2 = flight navigator
       3 = tube navigator
       4 = aquatic navigator
       5 = ethereal navigator
     */
    protected int navigatorType;
    protected int breakingTime;
    protected int previousBreakProgress = -1;
    private boolean inTube;
    private boolean inCage;
    private int animationTick;
    private Animation currentAnimation;
    private RatStatus status = RatStatus.IDLE;
    private BlockPos finalDigPathPoint = null;
    private BlockPos diggingPos = null;
    private int digCooldown = 0;
    private int eatingTicks = 0;
    public boolean refreshUpgrades = true;
    private int eatenItems = 0;
    private int rangedAttackCooldownCannon = 0;
    private int rangedAttackCooldownLaser = 0;
    private int rangedAttackCooldownPsychic = 0;
    private int rangedAttackCooldownDragon = 0;
    private int visualCooldown = 0;
    private int poopCooldown = 0;
    private int randomEffectCooldown = 0;

    public EntityRat(EntityType<? extends EntityRat> rat, World worldIn) {
        super(rat, worldIn);
        this.setPathPriority(PathNodeType.RAIL, 1000F);
        switchNavigator(1);
        initInventory();
    }

    public static BlockPos getPositionRelativetoGround(EntityRat rat, World world, double x, double z, Random rng) {
        if (rat.detachHome()) {
            x = rat.getHomePosition().getX() + rng.nextInt((int) rat.getMaximumHomeDistance()) - rat.getMaximumHomeDistance() / 2;
            z = rat.getHomePosition().getZ() + rng.nextInt((int) rat.getMaximumHomeDistance()) - rat.getMaximumHomeDistance() / 2;
        }
        BlockPos pos = new BlockPos(x, rat.getPosY(), z);
        while ((world.isAirBlock(pos.down()) || world.getBlockState(pos.down()).getBlock() instanceof BlockRatCage) && pos.getY() > 0) {
            pos = pos.down();
        }
        if (rat.isInCage()) {
            return pos.up(rat.getRNG().nextInt(3));
        } else {
            return pos.up(3 + rat.getRNG().nextInt(3));
        }
    }

    public static BlockPos getPositionRelativetoWater(EntityRat rat, World world, double x, double z, Random rng) {
        BlockPos pos;
        BlockPos topY = new BlockPos(x, rat.getPosY(), z);
        BlockPos bottomY = new BlockPos(x, rat.getPosY(), z);
        while (world.getBlockState(topY).getMaterial() == Material.WATER && topY.getY() < world.getHeight()) {
            topY = topY.up();
        }
        while (world.getBlockState(bottomY).getMaterial() == Material.WATER && bottomY.getY() > 0) {
            bottomY = bottomY.down();
        }
        for (int tries = 0; tries < 5; tries++) {
            pos = new BlockPos(x, bottomY.getY() + 1 + rng.nextInt(Math.max(1, topY.getY() - bottomY.getY() - 2)), z);
            if (world.getBlockState(pos).getMaterial() == Material.WATER) {
                return pos;
            }
        }
        return rat.getOnPosition();
    }

    public static boolean canEntityTypeSpawn(EntityType<? extends MobEntity> p_223315_0_, IWorld p_223315_1_, SpawnReason p_223315_2_, BlockPos p_223315_3_, Random p_223315_4_) {
        if (RatConfig.ratOverworldOnly && ((ServerWorld) (p_223315_1_)).getDimensionKey() != World.OVERWORLD) {
            return false;
        }
        BlockPos blockpos = p_223315_3_.down();
        if (p_223315_1_.getDifficulty() != Difficulty.PEACEFUL || p_223315_4_.nextInt(32) == 0) {
            return p_223315_2_ == SpawnReason.SPAWNER || spawnCheck(p_223315_1_, p_223315_3_, p_223315_4_);
        }
        return false;
    }

    private static boolean spawnCheck(IWorld world, BlockPos pos, Random rand) {
        int spawnRoll = RatConfig.ratSpawnDecrease;
        if (RatUtils.canSpawnInDimension(world)) {
            if (RatConfig.ratsSpawnLikeMonsters) {
                if (world.getDifficulty() == Difficulty.PEACEFUL) {
                    spawnRoll *= 2;
                }
                if (spawnRoll == 0 || rand.nextInt(spawnRoll) == 0) {
                    return isValidLightLevel(world, rand, pos);
                }
            } else {
                spawnRoll /= 2;
                return (spawnRoll == 0 || rand.nextInt(spawnRoll) == 0);
            }
        }
        return false;
    }

    protected static boolean isValidLightLevel(IWorld world, Random rand, BlockPos pos) {
        if (world.getLightFor(LightType.SKY, pos) > rand.nextInt(32)) {
            return false;
        } else {
            int i = ((ServerWorld) world).isThundering() ? world.getNeighborAwareLightSubtracted(pos, 10) : world.getLight(pos);
            return i <= rand.nextInt(8);
        }
    }

    public static AttributeModifierMap.MutableAttribute func_234290_eH_() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 8)        //HEALTH
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D)                //SPEED
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 1)       //ATTACK
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 128D);
    }

    protected void registerGoals() {
        aiHarvest = new RatAIHarvestCrops(this);
        aiPickup = new RatAIPickupFromInventory(this);
        aiDeposit = new RatAIDepositInInventory(this);
        aiAttack =  new RatAIAttackMelee(this, 1.45D, true);
        this.goalSelector.addGoal(0, aiAttack);
        this.goalSelector.addGoal(1, new RatAISwimming(this));
        this.goalSelector.addGoal(2, new RatAIFleeMobs(this, new Predicate<Entity>() {
            public boolean apply(@Nullable Entity entity) {
                return entity.isAlive() && ((entity instanceof PlayerEntity && ((PlayerEntity) entity).getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() != RatsItemRegistry.PIPER_HAT) && !((PlayerEntity) entity).isCreative()) || entity instanceof OcelotEntity || entity instanceof CatEntity;
            }
        }, 10.0F, 0.8D, 1.225D));
        this.goalSelector.addGoal(3, new RatAIFollowOwner(this, 1.225D, 3.0F, 1.0F));
        this.goalSelector.addGoal(6, new RatAIFleeSun(this, 1.225D));
        this.goalSelector.addGoal(6, new RatAISit(this));
        this.goalSelector.addGoal(7, new RatAIWander(this, 1.0D));
        this.goalSelector.addGoal(7, new RatAIWanderFlight(this));
        this.goalSelector.addGoal(7, new RatAIWanderAquatic(this));
        this.goalSelector.addGoal(8, new RatAIRaidChests(this));
        this.goalSelector.addGoal(8, new RatAIRaidCrops(this));
        this.goalSelector.addGoal(8, new RatAIEnterTrap(this));
        this.goalSelector.addGoal(8, new RatAIFleePosition(this));
        this.goalSelector.addGoal(9, new LookAtGoal(this, LivingEntity.class, 6.0F));
        this.goalSelector.addGoal(9, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(0, new RatAITargetItems(this, true));
        this.targetSelector.addGoal(1, new RatAIOwnerHurtByTarget(this));
        this.targetSelector.addGoal(2, new RatAIOwnerHurtTarget(this));
        this.targetSelector.addGoal(3, new RatAIHuntPrey(this, new Predicate<LivingEntity>() {
            public boolean apply(@Nullable LivingEntity entity) {
                if (EntityRat.this.hasPlague()) {
                    return entity instanceof PlayerEntity && !entity.isOnSameTeam(EntityRat.this) && entity.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() != RatsItemRegistry.BLACK_DEATH_MASK && entity.world.getDifficulty() != Difficulty.PEACEFUL;
                } else {
                    if (entity instanceof TameableEntity && ((TameableEntity) entity).isTamed()) {
                        return false;
                    }
                    if (EntityRat.this.shouldHuntMonster()) {
                        return entity instanceof IMob;
                    } else {
                        return entity != null && !(entity instanceof EntityRat) && !(entity instanceof EntityRatMountBase) && !(entity instanceof PlayerEntity) && !entity.isChild();
                    }
                }
            }
        }));
        this.targetSelector.addGoal(4, new RatAIHurtByTarget(this, CatEntity.class, OcelotEntity.class));
    }

    protected void setupDynamicAI() {
        this.goalSelector.removeGoal(this.aiHarvest);
        this.goalSelector.removeGoal(this.aiDeposit);
        this.goalSelector.removeGoal(this.aiPickup);
        this.goalSelector.removeGoal(this.aiAttack);
        boolean flag = false;
        aiHarvest = new RatAIHarvestCrops(this);
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK) && !(aiHarvest instanceof RatAIHarvestTrees)) {
            aiHarvest = new RatAIHarvestTrees(this);
            flag = true;
        }
        if ((this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER_ORE)) && !(aiHarvest instanceof RatAIHarvestMine)) {
            aiHarvest = new RatAIHarvestMine(this);
            flag = true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_QUARRY) && !(aiHarvest instanceof RatAIHarvestQuarry)) {
            aiHarvest = new RatAIHarvestQuarry(this);
            flag = true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FARMER) && !(aiHarvest instanceof RatAIHarvestFarmer)) {
            aiHarvest = new RatAIHarvestFarmer(this);
            flag = true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FISHERMAN) && !(aiHarvest instanceof RatAIHarvestFisherman)) {
            aiHarvest = new RatAIHarvestFisherman(this);
            flag = true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MILKER) && !(aiHarvest instanceof RatAIHarvestMilk)) {
            aiHarvest = new RatAIHarvestMilk(this);
            flag = true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_SHEARS) && !(aiHarvest instanceof RatAIHarvestShears)) {
            aiHarvest = new RatAIHarvestShears(this);
            flag = true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PLACER) && !(aiHarvest instanceof RatAIHarvestPlacer)) {
            aiHarvest = new RatAIHarvestPlacer(this);
            flag = true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BREEDER) && !(aiHarvest instanceof RatAIHarvestBreeder)) {
            aiHarvest = new RatAIHarvestBreeder(this);
            flag = true;
        }
        if (this.aiHarvest == null || !flag) {
            aiHarvest = new RatAIHarvestCrops(this);
        }
        aiDeposit = new RatAIDepositInInventory(this);
        aiPickup = new RatAIPickupFromInventory(this);
        aiAttack = new RatAIAttackMelee(this, 1.45, true);
        if (this.getMBTransferRate() > 0) {
            aiDeposit = new RatAIPickupFluid(this);
            aiPickup = new RatAIDepositFluid(this);
        } else if (this.getRFTransferRate() > 0) {
            aiDeposit = new RatAIPickupEnergy(this);
            aiPickup = new RatAIDepositEnergy(this);
        }
        forEachUpgrade((stack) -> {
            stack.onInitalizeAI(this);
        });
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BOW) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CROSSBOW)) {
            aiAttack = new RatAIAttackBow(this, 1.0D, 20, 15.0F);
        }
        this.goalSelector.addGoal(0, this.aiAttack);
        this.goalSelector.addGoal(4, this.aiHarvest);
        this.goalSelector.addGoal(5, this.aiDeposit);
        this.goalSelector.addGoal(5, this.aiPickup);
    }

    @Nullable
    public LivingEntity getMonsterOwner() {
        try {
            UUID uuid = this.getMonsterOwnerID();
            if (!world.isRemote) {
                Entity entity = world.getServer().getWorld(this.world.getDimensionKey()).getEntityByUuid(uuid);
                if (entity instanceof LivingEntity) {
                    return (LivingEntity) entity;
                }
            }
        } catch (IllegalArgumentException var2) {
            return null;
        }
        return null;
    }

    @Nullable
    public LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerId();
            LivingEntity player = uuid == null ? null : this.world.getPlayerByUuid(uuid);
            if (player != null) {
                return player;
            }
        } catch (IllegalArgumentException var2) {
            return null;
        }
        return null;
    }

    public boolean isNoDespawnRequired() {
        return this.isTamed() || this.isChild() || super.isNoDespawnRequired();
    }

    public boolean canDespawn(double distanceToClosestPlayer) {
        if (RatConfig.ratsSpawnLikeMonsters) {
            return !this.isTamed() && !this.isChild();
        } else {
            return super.canDespawn(distanceToClosestPlayer);
        }
    }

    @Override
    public void checkDespawn() {
        if (!this.isNoDespawnRequired() && !this.preventDespawn()) {
            Entity entity = this.world.getClosestPlayer(this, -1.0D);
            net.minecraftforge.eventbus.api.Event.Result result = net.minecraftforge.event.ForgeEventFactory.canEntityDespawn(this);
            if (result == net.minecraftforge.eventbus.api.Event.Result.DENY) {
                idleTime = 0;
                entity = null;
            } else if (result == net.minecraftforge.eventbus.api.Event.Result.ALLOW) {
                this.remove();
                entity = null;
            }
            if (entity != null) {
                double d0 = entity.getDistanceSq(this);

                if (this.canDespawn(d0) && d0 > (RatConfig.ratDespawnFarDistance * RatConfig.ratDespawnFarDistance)) {
                    this.remove();
                }
                double closeDist = RatConfig.ratDespawnCloseDistance * RatConfig.ratDespawnCloseDistance;
                if (this.idleTime > 600 && this.rand.nextInt(RatConfig.ratDespawnRandomChance) == 0 && d0 > closeDist && this.canDespawn(d0)) {
                    this.remove();
                } else if (d0 < closeDist) {
                    this.idleTime = 0;
                }
            }
        }
    }

    @Override
    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
        int spawnRoll = RatConfig.ratSpawnDecrease;
        if (RatUtils.canSpawnInDimension(world)) {
            if (RatConfig.ratsSpawnLikeMonsters) {
                if (world.getDifficulty() == Difficulty.PEACEFUL) {
                    spawnRoll *= 2;
                }
                if (spawnRoll == 0 || rand.nextInt(spawnRoll) == 0) {
                    BlockPos pos = new BlockPos(this.getPositionVec());
                    BlockState BlockState = this.world.getBlockState((pos).down());
                    return this.isValidLightLevel() && BlockState.canEntitySpawn(world, pos.down(), RatsEntityRegistry.RAT);
                }
            } else {
                spawnRoll /= 2;
                return (spawnRoll == 0 || rand.nextInt(spawnRoll) == 0) && super.canSpawn(worldIn, spawnReasonIn);
            }
        }
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_AQUATIC) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_UNDERWATER);
    }

    public boolean isValidLightLevel() {
        BlockPos blockpos = new BlockPos(this.getPosX(), this.getBoundingBox().minY, this.getPosZ());
        if (this.world.getLightFor(LightType.SKY, blockpos) > this.rand.nextInt(32)) {
            return false;
        } else {
            int i = this.world.isThundering() ? this.world.getNeighborAwareLightSubtracted(blockpos, 10) : this.world.getLight(blockpos);
            return i <= this.rand.nextInt(8);
        }
    }

    public boolean isOnLadder() {
        if (this.inTube()) {
            return climbingTube;
        }

        return super.isOnLadder();
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        return null;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(IS_MALE, Boolean.valueOf(false));
        this.dataManager.register(TOGA, Boolean.valueOf(false));
        this.dataManager.register(PLAGUE, Boolean.valueOf(false));
        this.dataManager.register(SITTING, Boolean.valueOf(false));
        this.dataManager.register(VISUAL_FLAG, Boolean.valueOf(false));
        this.dataManager.register(TAMED_BY_MONSTER, Boolean.valueOf(false));
        this.dataManager.register(COMMAND, Integer.valueOf(0));
        this.dataManager.register(COLOR_VARIANT, Integer.valueOf(0));
        this.dataManager.register(DANCING, Boolean.valueOf(false));
        this.dataManager.register(DANCE_MOVES, Integer.valueOf(0));
        this.dataManager.register(HELD_RF, Integer.valueOf(0));
        this.dataManager.register(RESPAWN_COUNTDOWN, Integer.valueOf(0));
        this.dataManager.register(RADIUS_CENTER, new BlockPos(this.getPositionVec()));
        this.dataManager.register(HAS_CUSTOM_RADIUS, Boolean.valueOf(false));
        this.dataManager.register(SEARCH_RADIUS, RatConfig.defaultRatRadius);
        this.dataManager.register(DYED, Boolean.valueOf(false));
        this.dataManager.register(DYE_COLOR, Byte.valueOf((byte) 0));
        this.dataManager.register(MONSTER_OWNER_UNIQUE_ID, Optional.empty());
        this.dataManager.register(DEPOSIT_POS, Optional.empty());
        this.dataManager.register(PICKUP_POS, Optional.empty());
        this.dataManager.register(IS_IN_WHEEL, Boolean.valueOf(false));

    }

    protected void switchNavigator(int type) {
        if (type == 1) {//cage or wild
            this.moveController = new RatMoveHelper(this);
            this.navigator = new RatAdvancedPathNavigate(this, world);
            this.navigatorType = 1;
        } else if (type == 0) {//tamed
            this.moveController = new RatMoveHelper(this);
            this.navigator = new RatAdvancedPathNavigate(this, world);
            this.navigatorType = 0;
        } else if (type == 2) {//flying
            this.moveController = new RatFlyingMoveHelper(this);
            this.navigator = new FlyingRatPathNavigate(this, world);
            this.navigatorType = 2;
        } else if (type == 3) {//tube
            this.moveController = new RatTubeMoveHelper(this);
            this.navigator = new RatAdvancedPathNavigate(this, world);
            this.navigatorType = 3;
        } else if (type == 4) {//aquatic
            this.moveController = new RatAquaticMoveHelper(this);
            this.navigator = new AquaticRatPathNavigate(this, world);
            this.navigatorType = 4;
        } else if (type == 5) {//ethereal
            this.moveController = new RatEtherealMoveHelper(this);
            this.navigator = new EtherealRatPathNavigate(this, world);
            this.navigatorType = 5;
        }
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putFloat("HomeDistance", getMaximumHomeDistance());
        if (getHomePosition() != null) {
            compound.putInt("HomePosX", getHomePosition().getX());
            compound.putInt("HomePosY", getHomePosition().getY());
            compound.putInt("HomePosZ", getHomePosition().getZ());
        }
        if (getSearchRadiusCenter() != null) {
            compound.putInt("RadiusPosX", getSearchRadiusCenter().getX());
            compound.putInt("RadiusPosY", getSearchRadiusCenter().getY());
            compound.putInt("RadiusPosZ", getSearchRadiusCenter().getZ());
        }
        compound.putBoolean("CustomSearchZone", hasCustomSearchZone());
        compound.putInt("SearchRadius", getSearchRadius());
        compound.putInt("CookingProgress", cookingProgress);
        compound.putInt("DigCooldown", digCooldown);
        compound.putInt("BreedCooldown", breedCooldown);
        compound.putInt("CoinCooldown", coinCooldown);
        compound.putInt("CheeseFeedings", cheeseFeedings);
        compound.putInt("MountCooldown", mountRespawnCooldown);
        compound.putInt("TransportingRF", this.getHeldRF());
        compound.putInt("RespawnCountdown", this.getRespawnCountdown());
        compound.putInt("Command", this.getCommandInteger());
        compound.putInt("ColorVariant", this.getColorVariant());
        compound.putBoolean("Plague", this.hasPlague());
        compound.putBoolean("TamedByMonster", this.wasTamedByMonster());
        compound.putBoolean("VisualFlag", this.getVisualFlag());
        compound.putBoolean("Dancing", this.isDancing());
        compound.putBoolean("Toga", this.hasToga());
        compound.putBoolean("IsMale", this.isMale());
        compound.putInt("WildTrust", wildTrust);
        compound.putBoolean("Dyed", this.isDyed());
        compound.putByte("DyeColor", (byte) this.getDyeColor());
        if (ratInventory != null) {
            ListNBT nbttaglist = new ListNBT();
            for (int i = 0; i < ratInventory.getSizeInventory(); ++i) {
                ItemStack itemstack = ratInventory.getStackInSlot(i);
                if (!itemstack.isEmpty()) {
                    CompoundNBT CompoundNBT = new CompoundNBT();
                    CompoundNBT.putByte("Slot", (byte) i);
                    itemstack.write(CompoundNBT);
                    nbttaglist.add(CompoundNBT);
                }
            }
            compound.put("Items", nbttaglist);
        }
        compound.putInt("EatenItems", eatenItems);
        if (this.getPickupPos() != null) {
            compound.putInt("PickupPosX", this.getPickupPos().getX());
            compound.putInt("PickupPosY", this.getPickupPos().getY());
            compound.putInt("PickupPosZ", this.getPickupPos().getZ());
        }
        if (this.getDepositPos() != null) {
            compound.putInt("DepositPosX", this.getDepositPos().getX());
            compound.putInt("DepositPosY", this.getDepositPos().getY());
            compound.putInt("DepositPosZ", this.getDepositPos().getZ());
            compound.putInt("DepositFacing", depositFacing.ordinal());
        }
        compound.putInt("RandomEffectCooldown", randomEffectCooldown);
        if (transportingFluid != null) {
            CompoundNBT fluidTag = new CompoundNBT();
            transportingFluid.writeToNBT(fluidTag);
            compound.put("TransportingFluid", fluidTag);
        }
        if (this.hasCustomName()) {
            compound.putString("CustomName", ITextComponent.Serializer.toJson(this.getCustomName()));
        }
        if (this.getMonsterOwnerID() == null) {
            compound.putString("MonsterOwnerUUID", "");
        } else {
            compound.putString("MonsterOwnerUUID", this.getMonsterOwnerID().toString());
        }

    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("HomePosX") && compound.contains("HomePosY") && compound.contains("HomePosZ")) {
            setHomePosAndDistance(new BlockPos(compound.getInt("HomePosX"), compound.getInt("HomePosY"), compound.getInt("HomePosZ")), compound.getInt("HomeDistance"));
        }
        if (compound.contains("RadiusPosX") && compound.contains("RadiusPosY") && compound.contains("RadiusPosZ")) {
            setSearchRadiusCenter(new BlockPos(compound.getInt("RadiusPosX"), compound.getInt("RadiusPosY"), compound.getInt("RadiusPosZ")));
        }
        this.setCustomSearchZone(compound.getBoolean("CustomSearchZone"));
        this.setSearchRadius(compound.getInt("SearchRadius"));
        cookingProgress = compound.getInt("CookingProgress");
        digCooldown = compound.getInt("DigCooldown");
        breedCooldown = compound.getInt("BreedCooldown");
        coinCooldown = compound.getInt("CoinCooldown");
        wildTrust = compound.getInt("WildTrust");
        eatenItems = compound.getInt("EatenItems");
        mountRespawnCooldown = compound.getInt("MountCooldown");
        cheeseFeedings = compound.getInt("CheeseFeedings");
        randomEffectCooldown = compound.getInt("RandomEffectCooldown");
        this.setHeldRF(compound.getInt("TransportingRF"));
        this.setRespawnCountdown(compound.getInt("RespawnCountdown"));
        this.setCommandInteger(compound.getInt("Command"));
        this.setPlague(compound.getBoolean("Plague"));
        this.setTamedByMonster(compound.getBoolean("TamedByMonster"));
        this.setDancing(compound.getBoolean("Dancing"));
        this.setVisualFlag(compound.getBoolean("VisualFlag"));
        this.setToga(compound.getBoolean("Toga"));
        this.setMale(compound.getBoolean("IsMale"));
        this.setColorVariant(compound.getInt("ColorVariant"));
        this.setDyed(compound.getBoolean("Dyed"));
        this.setDyeColor((compound.getByte("DyeColor")));
        if (ratInventory != null) {
            ListNBT nbttaglist = compound.getList("Items", 10);
            this.initInventory();
            for (int i = 0; i < nbttaglist.size(); ++i) {
                CompoundNBT CompoundNBT = nbttaglist.getCompound(i);
                int j = CompoundNBT.getByte("Slot") & 255;
                if (j < ratInventory.getSizeInventory()) {
                    ItemStack itemstack = ItemStack.read(CompoundNBT);
                    ratInventory.setInventorySlotContents(j, itemstack);
                }
            }
        } else {
            ListNBT nbttaglist = compound.getList("Items", 10);
            this.initInventory();
            for (int i = 0; i < nbttaglist.size(); ++i) {
                CompoundNBT CompoundNBT = nbttaglist.getCompound(i);
                int j = CompoundNBT.getByte("Slot") & 255;
                ItemStack itemstack = ItemStack.read(CompoundNBT);
                ratInventory.setInventorySlotContents(j, itemstack);
            }
        }
        if (compound.contains("PickupPosX") && compound.contains("PickupPosY") && compound.contains("PickupPosZ")) {
            this.setPickupPos(new BlockPos(compound.getInt("PickupPosX"), compound.getInt("PickupPosY"), compound.getInt("PickupPosZ")));
        }
        if (compound.contains("DepositPosX") && compound.contains("DepositPosY") && compound.contains("DepositPosZ")) {
            this.setDepositPos(new BlockPos(compound.getInt("DepositPosX"), compound.getInt("DepositPosY"), compound.getInt("DepositPosZ")));
            if (compound.contains("DepositFacing")) {
                depositFacing = Direction.values()[compound.getInt("DepositFacing")];
            }
        }
        if (compound.contains("TransportingFluid")) {
            CompoundNBT fluidTag = compound.getCompound("TransportingFluid");
            if (!fluidTag.isEmpty()) {
                transportingFluid = FluidStack.loadFluidStackFromNBT(fluidTag);
            }
        }

        if (compound.contains("CustomName", 8) && !compound.getString("CustomName").startsWith("TextComponent")) {
            this.setCustomName(ITextComponent.Serializer.getComponentFromJson(compound.getString("CustomName")));
        }
        String s = "";
        if (compound.contains("MonsterOwnerUUID", 8)) {
            s = compound.getString("MonsterOwnerUUID");
        }
        if (!s.isEmpty()) {
            try {
                this.setMonsterOwnerUniqueId(UUID.fromString(s));
                this.setTamedByMonster(true);
            } catch (Throwable var4) {
                this.setTamedByMonster(false);
            }
        }

    }

    @Override
    public boolean isPushedByWater() {
        return !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_QUARRY) && super.isPushedByWater();
    }

    protected float getWaterSlowDown() {
        return this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_QUARRY) ? 1F : 0.8F;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source) || source == DamageSource.IN_WALL && this.isPassenger()) {
            return false;
        } else {
            Entity entity = source.getTrueSource();

            if (entity != null && !(entity instanceof PlayerEntity) && !(entity instanceof ArrowEntity)) {
                amount = (amount + 1.0F) / 2.0F;
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    protected int getCommandInteger() {
        return Integer.valueOf(this.dataManager.get(COMMAND).intValue());
    }

    private void setCommandInteger(int command) {
        this.dataManager.set(COMMAND, Integer.valueOf(command));
        if (command == RatCommand.SIT.ordinal()) {
            this.setSitting(true);
        } else {
            this.setSitting(false);
        }
    }

    public void setSitting(boolean sit) {
        this.dataManager.set(SITTING, Boolean.valueOf(sit));
    }

    public boolean isSitting() {
        return this.dataManager.get(SITTING).booleanValue();
    }

    public int getColorVariant() {
        return Integer.valueOf(this.dataManager.get(COLOR_VARIANT).intValue());
    }

    public void setColorVariant(int color) {
        this.dataManager.set(COLOR_VARIANT, Integer.valueOf(color));
    }

    public void setToga(boolean plague) {
        this.dataManager.set(TOGA, Boolean.valueOf(plague));
    }

    public boolean hasToga() {
        return this.dataManager.get(TOGA).booleanValue();
    }

    public void setPlague(boolean plague) {
        this.dataManager.set(PLAGUE, Boolean.valueOf(plague));
    }

    public boolean hasPlague() {
        return this.dataManager.get(PLAGUE).booleanValue();
    }


    public void setTamedByMonster(boolean plague) {
        this.dataManager.set(TAMED_BY_MONSTER, Boolean.valueOf(plague));
    }

    public boolean wasTamedByMonster() {
        return this.dataManager.get(TAMED_BY_MONSTER).booleanValue();
    }


    public boolean isMale() {
        return this.dataManager.get(IS_MALE).booleanValue();
    }

    public void setMale(boolean male) {
        this.dataManager.set(IS_MALE, Boolean.valueOf(male));
    }

    public boolean getVisualFlag() {
        return this.dataManager.get(VISUAL_FLAG).booleanValue();
    }

    public void setVisualFlag(boolean flag) {
        this.dataManager.set(VISUAL_FLAG, Boolean.valueOf(flag));
    }

    public boolean isDancing() {
        return this.dataManager.get(DANCING).booleanValue();
    }

    public void setDancing(boolean dancing) {
        this.dataManager.set(DANCING, Boolean.valueOf(dancing));
    }

    public int getDanceMoves() {
        return Integer.valueOf(this.dataManager.get(DANCE_MOVES).intValue());
    }

    public void setDanceMoves(int moves) {
        this.dataManager.set(DANCE_MOVES, Integer.valueOf(moves));
    }

    public int getHeldRF() {
        return Integer.valueOf(this.dataManager.get(HELD_RF).intValue());
    }

    public void setHeldRF(int rf) {
        this.dataManager.set(HELD_RF, Integer.valueOf(rf));
    }

    public int getRespawnCountdown() {
        return Integer.valueOf(this.dataManager.get(RESPAWN_COUNTDOWN).intValue());
    }

    public void setRespawnCountdown(int respawn) {
        this.dataManager.set(RESPAWN_COUNTDOWN, Integer.valueOf(respawn));
    }

    public boolean isInWheel() {
        return this.dataManager.get(IS_IN_WHEEL).booleanValue();
    }

    public void setInWheel(boolean wheel) {
        this.dataManager.set(IS_IN_WHEEL, Boolean.valueOf(wheel));
    }

    @Nullable
    public UUID getMonsterOwnerID() {
        return (UUID) ((Optional) this.dataManager.get(MONSTER_OWNER_UNIQUE_ID)).orElse(null);
    }

    public void setMonsterOwnerUniqueId(@Nullable UUID p_184754_1_) {
        this.dataManager.set(MONSTER_OWNER_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
    }

    public boolean isDyed() {
        return this.dataManager.get(DYED).booleanValue();
    }

    public void setDyed(boolean dyed) {
        this.dataManager.set(DYED, Boolean.valueOf(dyed));
    }

    public int getDyeColor() {
        return this.dataManager.get(DYE_COLOR);
    }

    public void setDyeColor(int color) {
        this.dataManager.set(DYE_COLOR, (byte) (color));
    }

    public RatCommand getCommand() {
        return RatCommand.values()[MathHelper.clamp(getCommandInteger(), 0, RatCommand.values().length - 1)];
    }

    public void setCommand(RatCommand command) {
        if(!world.isRemote && command.ordinal() != this.getCommandInteger()){
            this.getNavigator().clearPath();
        }
        setCommandInteger(command.ordinal());
    }

    public BlockPos getPickupPos() {
        return this.dataManager.get(PICKUP_POS).orElse(null);
    }

    public void setPickupPos(BlockPos pos) {
        this.dataManager.set(PICKUP_POS, Optional.ofNullable(pos));
    }

    public BlockPos getDepositPos() {
        return this.dataManager.get(DEPOSIT_POS).orElse(null);
    }

    public void setDepositPos(BlockPos pos) {
        this.dataManager.set(DEPOSIT_POS, Optional.ofNullable(pos));
    }


    public boolean isFollowing() {
        return getCommandInteger() == 2;
    }

    public boolean isTargetCommand() {
        return getCommandInteger() == 4 || getCommandInteger() == 5;
    }

    public EntitySenses getSenses() {
        return this.getEntitySenses();
    }

    public boolean isHoldingFood() {
        boolean check = forEachUpgradeBool((stack) -> {
            return stack.isRatHoldingFood(this);
        }, false);
        return check || !this.getHeldItem(Hand.MAIN_HAND).isEmpty() && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CHEF) && ((RatUtils.isRatFood(this.getHeldItem(Hand.MAIN_HAND)) || (hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ORE_DOUBLING) && ItemRatUpgradeOreDoubling.isProcessable(this.getHeldItemMainhand()))));
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getRidingEntity() != null && getMountEntityType() != null && this.getRidingEntity().getType() == getMountEntityType()) {
            if (this.getRidingEntity() instanceof EntityRatMountBase) {
                if (entityIn instanceof LivingEntity) {
                    ((EntityRatMountBase) this.getRidingEntity()).setAttackTarget((LivingEntity) entityIn);
                }
                return ((EntityRatMountBase) this.getRidingEntity()).attackEntityAsMob(entityIn);
            }
        }
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float) ((int) this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if (flag) {
            this.applyEnchantments(this, entityIn);
            if (this.hasPlague() && entityIn instanceof LivingEntity && rollForPlague((LivingEntity) entityIn)) {
                ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(RatsMod.PLAGUE_POTION, 6000));
            }
            if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DEMON)) {
                entityIn.setFire(10);
            }
            if (this.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_FERAL_BITE)) {
                entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 5F);
                ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(RatsMod.PLAGUE_POTION, 600));
                ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(Effects.POISON, 600));
            }
            if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BEE)) {
                ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(Effects.POISON, 1200, 1));
            }
            if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_TNT)) {
                Explosion.Mode explosion$mode = world.getGameRules().getBoolean(GameRules.MOB_GRIEFING) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
                Explosion explosion = new Explosion(this.world, null, this.getPosX(), this.getPosY() + (double) (this.getHeight() / 16.0F), this.getPosZ(), 4.0F, false, explosion$mode);
                explosion.doExplosionA();
                explosion.doExplosionB(true);
            }
            if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_TNT_SURVIVOR)) {
                Explosion explosion = new RatExplosion(this.world, this, this.getPosX(), this.getPosY() + (double) (this.getHeight() / 16.0F), this.getPosZ(), 4.0F, false, world.getGameRules().getBoolean(GameRules.MOB_GRIEFING));
                explosion.doExplosionA();
                explosion.doExplosionB(true);
            }
            forEachUpgrade((stack) -> stack.onPostAttack(this, entityIn));
        }
        return flag;
    }

    public int getTotalArmorValue() {
        if (this.isTamed()) {
            return super.getTotalArmorValue() * 3;
        } else {
            return super.getTotalArmorValue();
        }
    }

    @Override
    public void livingTick() {
        this.setRatStatus(RatStatus.IDLE);
        if (this.getRespawnCountdown() > 0) {
            this.setRespawnCountdown(this.getRespawnCountdown() - 1);
        }
        super.livingTick();
        if(refreshUpgrades){
            this.onUpgradeChanged();
            refreshUpgrades = false;
        }
        this.prevFlyingPitch = flyingPitch;
        if (this.inTube()) {
            if (navigatorType != 3) {
                switchNavigator(3);
            }
            if (climbingTube) {
                this.setMotion(this.getMotion().x, this.getMotion().y + 0.1D, this.getMotion().z);
            } else if (!this.onGround && this.getMotion().y < 0.0D) {
                this.setMotion(this.getMotion().x, this.getMotion().scale(0.6).y, this.getMotion().z);
            }
            double ydist = prevPosY - this.getPosY();//down 0.4 up -0.38
            double planeDist = (Math.abs(this.getMotion().x) + Math.abs(this.getMotion().z)) * 12F;
            this.flyingPitch += (float) (ydist) * 100;
            this.flyingPitch = MathHelper.clamp(this.flyingPitch, -90, 90);
            float plateau = 2;
            if (this.flyingPitch > plateau) {
                this.flyingPitch -= planeDist * Math.abs(this.flyingPitch) / 90;
            }
            if (this.flyingPitch < -plateau) {
                this.flyingPitch += planeDist * Math.abs(this.flyingPitch) / 90;
            }
            if (this.flyingPitch > 2F) {
                this.flyingPitch -= onGround ? Math.max(flyingPitch, 10) : 1F;
            }
            if (this.flyingPitch < -2F) {
                this.flyingPitch += onGround ? Math.max(flyingPitch, 10) : 1F;
            }
            if (this.flyingPitch < 1F && flyingPitch > -1F && onGround) {
                this.flyingPitch = 0;
            }
        } else if (this.hasFlightUpgrade() && !this.isInWater()) {
            if (navigatorType != 2) {
                switchNavigator(2);
            }
            if (canMove()) {
                if (this.moveController.getY() > this.getPosY() && !this.isPassenger()) {
                    this.setMotion(this.getMotion().x, this.getMotion().y + 0.08D, this.getMotion().z);
                }
            } else if (!onGround) {
                this.setMotion(this.getMotion().x, this.getMotion().y - 0.08D, this.getMotion().z);
            }
            if (!this.onGround) {
                double ydist = prevPosY - this.getPosY();//down 0.4 up -0.38
                double planeDist = (Math.abs(this.getMotion().x) + Math.abs(this.getMotion().z)) * 12F;
                this.flyingPitch += (float) (ydist) * 20;
                this.flyingPitch = MathHelper.clamp(this.flyingPitch, -90, 90);
                float plateau = 2;
                if (this.flyingPitch > plateau) {
                    this.flyingPitch -= planeDist * Math.abs(this.flyingPitch) / 90;
                }
                if (this.flyingPitch < -plateau) {
                    this.flyingPitch += planeDist * Math.abs(this.flyingPitch) / 90;
                }
                if (this.flyingPitch > 2F) {
                    this.flyingPitch -= 1F;
                } else if (this.flyingPitch < -2F) {
                    this.flyingPitch += 1F;
                }
            } else {
                this.flyingPitch = 0;
            }
        } else if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_AQUATIC) && this.isInWater()) {
            if (navigatorType != 4) {
                switchNavigator(4);
            }
            if (canMove()) {
                if (this.moveController.getY() > this.getPosY()) {
                    this.setMotion(this.getMotion().x, this.getMotion().y + 0.08D, this.getMotion().z);
                }
            } else if (!onGround) {
                this.setMotion(this.getMotion().x, this.getMotion().y - 0.08D, this.getMotion().z);
            }
            if (!this.onGround) {
                double ydist = prevPosY - this.getPosY();//down 0.4 up -0.38
                double planeDist = (Math.abs(this.getMotion().x) + Math.abs(this.getMotion().z)) * 12F;
                this.flyingPitch += (float) (ydist) * 20;
                this.flyingPitch = MathHelper.clamp(this.flyingPitch, -90, 90);
                float plateau = 2;
                if (this.flyingPitch > plateau) {
                    this.flyingPitch -= planeDist * Math.abs(this.flyingPitch) / 90;
                }
                if (this.flyingPitch < -plateau) {
                    this.flyingPitch += planeDist * Math.abs(this.flyingPitch) / 90;
                }
                if (this.flyingPitch > 2F) {
                    this.flyingPitch -= 1F;

                } else if (this.flyingPitch < -2F) {
                    this.flyingPitch += 1F;
                }
            } else {
                this.flyingPitch = 0;
            }
        } else if (this.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_ETHEREAL)) {
            if (!this.inTube()) {
                this.flyingPitch = 0;
            }
            if (navigatorType != 5) {
                switchNavigator(5);
            }
            noClip = true;
        } else {
            if (!this.inTube()) {
                this.flyingPitch = 0;
            }
            boolean wildNavigate = !this.isTamed() || this.isInCage();
            if (wildNavigate && navigatorType != 1) {
                switchNavigator(1);
            }
            if (!wildNavigate && navigatorType != 0) {
                switchNavigator(0);
            }
        }
        if (breedCooldown > 0) {
            breedCooldown--;
        }
        if (this.isMoving()) {
            this.setRatStatus(RatStatus.MOVING);
        }
        forEachUpgrade((stack) -> stack.onRatUpdate(this));
        boolean sitting = isSitting() || this.isPassenger() || this.isDancing() || (this.getAnimation() == ANIMATION_IDLE_SCRATCH || this.getAnimation() == ANIMATION_IDLE_SNIFF) && shouldSitDuringAnimation();
        float sitInc = this.getAnimation() == ANIMATION_IDLE_SCRATCH || this.getAnimation() == ANIMATION_IDLE_SNIFF ? 5 : 1F;
        boolean holdingInHands = !sitting && (!this.getHeldItem(Hand.MAIN_HAND).isEmpty() && (!this.holdInMouth || cookingProgress > 0)
                || this.getAnimation() == ANIMATION_EAT || this.holdsItemInHandUpgrade() || this.getMBTransferRate() > 0);
        if (sitting && sitProgress < 20.0F) {
            sitProgress += sitInc;
        } else if (!sitting && sitProgress > 0.0F) {
            sitProgress -= sitInc;
        }
        if (holdingInHands && holdProgress < 5.0F) {
            holdProgress += 0.5F;
        } else if (!holdingInHands && holdProgress > 0.0F) {
            holdProgress -= 0.5F;
        }
        boolean inTrap = isDeadInTrap;
        if (inTrap && deadInTrapProgress < 5.0F) {
            deadInTrapProgress += 1F;
        } else if (!inTrap && deadInTrapProgress > 0.0F) {
            deadInTrapProgress -= 1F;
        }
        if (digCooldown <= 0 && RatConfig.ratsDigBlocks && this.canDigHoles()) {
            findDigTarget();
            digTarget();
        }
        if (this.getCommand() == RatCommand.SIT && !this.isSitting()) {
            this.setSitting(true);
        }
        if (this.isSitting() && this.getCommand() != RatCommand.SIT) {
            this.setSitting(false);
        }
        if(this.isInWheel()){
            if(world.getBlockState(this.getPosition()).getBlock() != RatsBlockRegistry.RAT_CAGE_WHEEL){
                this.setInWheel(false);
            }
        }
        if (this.getAnimation() == ANIMATION_EAT && isHoldingFood()) {
            eatingTicks++;
            eatItem(this.getHeldItem(Hand.MAIN_HAND), 3);
            if (eatingTicks >= 40) {
                ItemStack pooStack = new ItemStack(RatsItemRegistry.RAT_NUGGET);
                if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ORE_DOUBLING) && ItemRatUpgradeOreDoubling.isProcessable(this.getHeldItem(Hand.MAIN_HAND))) {
                    pooStack = new ItemStack(RatsItemRegistry.RAT_NUGGET_ORE, 2);
                    CompoundNBT poopTag = new CompoundNBT();
                    CompoundNBT oreTag = new CompoundNBT();
                    ItemRatUpgradeOreDoubling.getProcessedOre(this.getHeldItem(Hand.MAIN_HAND)).write(oreTag);
                    poopTag.put("OreItem", oreTag);
                    pooStack.setTag(poopTag);
                }
                this.getHeldItem(Hand.MAIN_HAND).shrink(1);
                if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ORE_DOUBLING) || rand.nextFloat() <= 0.05F && this.isTamed()) {
                    if (RatConfig.ratFartNoises) {
                        this.playSound(RatsSoundRegistry.RAT_POOP, 0.5F + rand.nextFloat() * 0.5F, 1.0F + rand.nextFloat() * 0.5F);
                    }
                    if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ORE_DOUBLING)) {
                        if (this.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
                            ItemStack oneStack = pooStack.copy();
                            oneStack.setCount(1);
                            this.setHeldItem(Hand.MAIN_HAND, oneStack);
                            pooStack.shrink(1);
                        }
                    }
                    if (pooStack.getCount() > 0) {
                        if (!world.isRemote) {
                            this.entityDropItem(pooStack, 0.0F);
                        }
                    }
                }
                int healAmount = 1;
                if (this.getHeldItem(Hand.MAIN_HAND).getItem().isFood()) {
                    healAmount = this.getHeldItem(Hand.MAIN_HAND).getItem().getFood().getHealing();
                }
                this.heal(healAmount);
                eatingTicks = 0;
            }
        }
        if (!world.isRemote) {
            if (isHoldingFood() && (this.getRNG().nextInt(20) == 0 || eatingTicks > 0) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CHEF) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CHRISTMAS)
                    && (this.getCommand() != RatCommand.GATHER && this.getCommand() != RatCommand.HARVEST)
                    && (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ORE_DOUBLING) || this.getCommand() != RatCommand.TRANSPORT && this.shouldDepositItem(getHeldItemMainhand()))) {
                if (this.getCommand() != RatCommand.HARVEST || this.getCommand() != RatCommand.HUNT_ANIMALS || this.getCommand() != RatCommand.HUNT_MONSTERS || this.getHealth() < this.getMaxHealth()) {
                    this.setAnimation(ANIMATION_EAT);
                    this.setRatStatus(RatStatus.EATING);
                }
            }
        }
        if (this.hasPlague() && rand.nextFloat() < 0.3F) {
            double d0 = 0D;
            double d1 = this.rand.nextGaussian() * 0.05D + 0.5D;
            double d2 = 0D;
            this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CHEF) && !this.getHeldItemMainhand().isEmpty()) {
            this.tryCooking();
            if (cookingProgress > 0) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                if (cookingProgress == 99) {
                    this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                    this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                    this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                } else {
                    this.world.addParticle(ParticleTypes.SMOKE, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                    if (rand.nextFloat() < 0.125F) {
                        this.world.addParticle(ParticleTypes.FLAME, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                    }
                }
            }
        }
        if (this.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_ARCHEOLOGIST) && !this.getHeldItemMainhand().isEmpty()) {
            this.tryArcheology();
            if (cookingProgress > 0) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                if (cookingProgress == 99) {
                    this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                    this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                    this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                } else {
                    this.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, RatsBlockRegistry.GARBAGE_PILE.getDefaultState()), this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                    if (rand.nextFloat() < 0.125F) {
                        this.world.addParticle(ParticleTypes.ENCHANT, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                    }
                }
            }
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_GEMCUTTER) && !this.getHeldItemMainhand().isEmpty()) {
            this.tryGemcutter();
            if (cookingProgress > 0) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                if (cookingProgress == 99) {
                    this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                    this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                    this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                } else {
                    this.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, Blocks.DIAMOND_ORE.getDefaultState()), this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                }
            }
        }
        if ((this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ENCHANTER) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DISENCHANTER)) && !this.getHeldItemMainhand().isEmpty()) {
            this.tryEnchanting(this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DISENCHANTER));
            if (cookingProgress > 0) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                if (cookingProgress == 999) {
                    this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                    this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                    this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                } else {
                    this.world.addParticle(ParticleTypes.ENCHANT, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                    this.world.addParticle(ParticleTypes.ENCHANT, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                }
            }
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CHRISTMAS)) {
            this.tryGiftgiving();
            if (cookingProgress > 0) {
                if (cookingProgress == 71999) {
                    this.world.setEntityState(this, (byte) 126);
                }
            }
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ARISTOCRAT)) {
            if (this.coinCooldown <= 0) {
                this.coinCooldown = this.rand.nextInt(6000) + 6000;
                if (!world.isRemote) {
                    this.entityDropItem(new ItemStack(RatsItemRegistry.TINY_COIN, 1 + rand.nextInt(2)), 0.0F);
                }
                this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, this.getSoundVolume(), this.getSoundPitch());
            } else {
                coinCooldown--;
            }
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ENDER)) {
            if (!world.isRemote) {
                if (this.getNavigator().getPath() != null && this.getNavigator().getPath().getFinalPathPoint() != null && !this.isPassenger()) {
                    Vector3d target = new Vector3d(this.getNavigator().getPath().getFinalPathPoint().x, this.getNavigator().getPath().getFinalPathPoint().y, this.getNavigator().getPath().getFinalPathPoint().z);
                    if (this.getRatDistanceCenterSq(target.x, target.y, target.z) > 20 || !this.isDirectPathBetweenPoints(target)) {
                        this.attemptTeleport(target.x, target.y, target.z);
                    }
                }
            } else {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                this.world.addParticle(ParticleTypes.PORTAL, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);

            }
        }
        if (world.isRemote && this.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_FERAL_BITE) && this.getRNG().nextInt(5) == 0) {
            float sitAddition = 0.125f * (sitProgress / 20F);
            float radius = 0.3F - sitAddition;
            float angle = (0.01745329251F * (this.renderYawOffset));
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + getPosX();
            double extraZ = (double) (radius * MathHelper.cos(angle)) + getPosZ();
            double extraY = 0.125 + getPosY() + sitAddition;
            float particleRand = 0.1F;
            RatsMod.PROXY.addParticle("saliva", extraX + (double) (this.rand.nextFloat() * particleRand * 2) - (double) particleRand,
                    extraY,
                    extraZ + (double) (this.rand.nextFloat() * particleRand * 2) - (double) particleRand,
                    0F, 0.0F, 0F);
        }
        if (world.isRemote && this.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_PSYCHIC) && this.getRNG().nextInt(5) == 0) {
            float sitAddition = 0.125f * (sitProgress / 20F);
            float radius = 0.45F - sitAddition;
            float angle = (0.01745329251F * (this.renderYawOffset));
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + getPosX();
            double extraZ = (double) (radius * MathHelper.cos(angle)) + getPosZ();
            double extraY = 0.12 + getPosY() + sitAddition;
            float particleRand = 0.4F;
            RatsMod.PROXY.addParticle("rat_lightning", extraX + (double) (this.rand.nextFloat() * particleRand * 2) - (double) particleRand,
                    extraY,
                    extraZ + (double) (this.rand.nextFloat() * particleRand * 2) - (double) particleRand,
                    0F, 0.0F, 0F);
        }
        if (this.isInCage()) {
            if (this.getAttackTarget() != null) {
                this.setAttackTarget(null);
            }
        }
        if (this.getAttackTarget() != null && this.isRidingSpecialMount() && RatUtils.isRidingOrBeingRiddenBy(this.getAttackTarget(), this)) {
            this.setAttackTarget(null);
        }
        if (this.getAttackTarget() != null && this.getAttackTarget().getEntityId() == this.getEntityId()) {
            this.setAttackTarget(null);
        }
        if (this.isTamed() && this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CRAFTING)) {
            TileEntity te = world.getTileEntity(new BlockPos(this.getPositionVec()).down());
            if (te != null && te instanceof TileEntityRatCraftingTable && !world.isRemote) {
                TileEntityRatCraftingTable ratCraftingTable = (TileEntityRatCraftingTable) te;
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                if (ratCraftingTable.getField(0) > 0) {
                    crafting = true;
                    world.setEntityState(this, (byte) 85);
                    ItemStack stack = ratCraftingTable.getStackInSlot(0);
                    if (stack.isEmpty()) {
                        this.world.addParticle(ParticleTypes.SMOKE, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                    } else {
                        this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, stack), this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY(), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                        this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, stack), this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY(), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                    }
                    if (ratCraftingTable.prevCookTime % 20 == 0) {
                        this.playSound(CRAFTING_SOUNDS[rand.nextInt(CRAFTING_SOUNDS.length - 1)], 0.6F, 0.75F + rand.nextFloat());
                    }
                } else {
                    crafting = false;
                    world.setEntityState(this, (byte) 86);
                }
                if (ratCraftingTable.prevCookTime == 199) {
                    for (int i = 0; i < 4; i++) {
                        this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                    }
                    this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1, 1);
                }
            }
        }
        if (!world.isRemote && this.getRatStatus() == RatStatus.IDLE && !isInWheel() && this.getHeldItem(Hand.MAIN_HAND).isEmpty() && this.getAnimation() == NO_ANIMATION && this.getRNG().nextInt(350) == 0 && this.shouldNotIdleAnimation()) {
            this.setAnimation(this.getRNG().nextBoolean() ? ANIMATION_IDLE_SNIFF : ANIMATION_IDLE_SCRATCH);
        }
        if (!world.isRemote && this.getMonsterOwnerID() != null && this.getMonsterOwner() instanceof ISummonsRats && this.getMonsterOwner() instanceof MobEntity) {
            MobEntity mob = (MobEntity) this.getMonsterOwner();
            ISummonsRats summonsRats = (ISummonsRats) this.getMonsterOwner();
            if (mob.getAttackTarget() != null) {
                this.setAttackTarget(mob.getAttackTarget());
            }
            if (summonsRats.readsorbRats()) {
                if (this.getAttackTarget() == null || !this.getAttackTarget().isAlive()) {
                    this.getNavigator().tryMoveToEntityLiving(mob, 1.225F);
                }
                if (this.getDistance(mob) < mob.getWidth()) {
                    this.remove();
                    summonsRats.setRatsSummoned(summonsRats.getRatsSummoned() - 1);
                }
            } else if (summonsRats.encirclesSummoner()) {
                if (this.getAttackTarget() == null || !this.getAttackTarget().isAlive()) {
                    float radius = summonsRats.getRadius();
                    int maxRatStuff = 360 / Math.max(summonsRats.getRatsSummoned(), 1);
                    int ratIndex = this.getEntityId() % Math.max(summonsRats.getRatsSummoned(), 1);
                    float angle = (0.01745329251F * (ratIndex * maxRatStuff + ticksExisted * 4.1F));
                    double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + mob.getPosX();
                    double extraZ = (double) (radius * MathHelper.cos(angle)) + mob.getPosZ();
                    BlockPos runToPos = new BlockPos(extraX, mob.getPosY(), extraZ);
                    int steps = 0;
                    while (world.getBlockState(runToPos).isOpaqueCube(world, runToPos) && steps < 10) {
                        runToPos = runToPos.up();
                        steps++;
                    }
                    this.getNavigator().tryMoveToXYZ(extraX, runToPos.getY(), extraZ, 1.225F);
                }
            }

        }

        if (this.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_NONBELIEVER)) {
            if (this.getHealth() < this.getMaxHealth() && this.ticksExisted % 30 == 0) {
                this.heal(1.0F);
            }
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_VOODOO) || this.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_PSYCHIC)) {
            if (this.getHealth() < this.getMaxHealth() && this.ticksExisted % 30 == 0) {
                this.heal(1.0F);
            }
        }
        if (this.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_PSYCHIC)) {
            if (rangedAttackCooldownPsychic == 0 && this.getAttackTarget() != null) {
                if (rand.nextBoolean()) {
                    rangedAttackCooldownPsychic = 50;
                    BlockPos ourPos = new BlockPos(this.getPositionVec());
                    int searchRange = 10;
                    List<BlockPos> listOfAll = new ArrayList<>();
                    for (BlockPos pos : BlockPos.getAllInBox(ourPos.add(-searchRange, -searchRange, -searchRange), ourPos.add(searchRange, searchRange, searchRange)).map(BlockPos::toImmutable).collect(Collectors.toList())) {
                        BlockState state = world.getBlockState(pos);
                        if (!world.isAirBlock(pos) && WitherEntity.canDestroyBlock(state)) {
                            listOfAll.add(pos);
                        }
                    }
                    if (listOfAll.size() > 0) {
                        BlockPos pos = listOfAll.get(rand.nextInt(listOfAll.size()));
                        EntityThrownBlock thrownBlock = new EntityThrownBlock(RatsEntityRegistry.THROWN_BLOCK, world, world.getBlockState(pos), this);
                        thrownBlock.setPosition(pos.getX() + 0.5D, pos.getY() + 2.5D, pos.getZ() + 0.5D);
                        thrownBlock.dropBlock = false;
                        if (!world.isRemote) {
                            world.addEntity(thrownBlock);
                        }
                        RatsMod.sendMSGToAll(new MessageSyncThrownBlock(thrownBlock.getEntityId(), pos.toLong()));
                    } else {
                        rangedAttackCooldownPsychic = 5;
                    }
                } else {
                    rangedAttackCooldownPsychic = 100;
                    int bounds = 5;
                    for (int i = 0; i < rand.nextInt(2) + 1; i++) {
                        EntityLaserPortal laserPortal = new EntityLaserPortal(RatlantisEntityRegistry.LASER_PORTAL, world, this.getAttackTarget().getPosX() + this.rand.nextInt(bounds * 2) - bounds, this.getPosY() + 2, this.getAttackTarget().getPosZ() + this.rand.nextInt(bounds * 2) - bounds, this);
                        world.addEntity(laserPortal);
                    }
                }
            }
        }
        if (this.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_BUCCANEER)) {
            if (this.getVisualFlag() && visualCooldown == 0) {
                this.setVisualFlag(false);
            }
            if (rangedAttackCooldownCannon == 0 && this.getAttackTarget() != null) {
                rangedAttackCooldownCannon = 60;
                EntityCheeseCannonball cannonball = new EntityCheeseCannonball(RatlantisEntityRegistry.CHEESE_CANNONBALL, world, this);
                //cannonball.ignoreEntity = this;
                double extraY = 0.6 + getPosY();
                double d0 = this.getAttackTarget().getPosY() + (double) this.getAttackTarget().getEyeHeight() - 1.100000023841858D;
                double d1 = this.getAttackTarget().getPosX() - this.getPosX();
                double d3 = this.getAttackTarget().getPosZ() - this.getPosZ();
                double d2 = d0 - extraY;
                float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
                cannonball.setPosition(getPosX(), extraY, getPosZ());
                cannonball.shoot(d1, d2 + (double) f, d3, 0.75F, 0.4F);
                this.setVisualFlag(true);
                visualCooldown = 4;
                this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 3.0F, 2.3F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
                if (!world.isRemote) {
                    this.world.addEntity(cannonball);
                }
            }
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DRAGON)) {
            if (this.getVisualFlag() && visualCooldown == 0) {
                this.setVisualFlag(false);
            }
            if (rangedAttackCooldownDragon == 0 && this.getAttackTarget() != null) {
                rangedAttackCooldownDragon = 5;
                float radius = 0.3F;
                float angle = (0.01745329251F * (this.renderYawOffset));
                double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + getPosX();
                double extraZ = (double) (radius * MathHelper.cos(angle)) + getPosZ();
                double extraY = 0.2 + getPosY();
                double targetRelativeX = this.getAttackTarget().getPosX() - extraX;
                double targetRelativeY = this.getAttackTarget().getPosY() + this.getAttackTarget().getHeight() / 2 - extraY;
                double targetRelativeZ = this.getAttackTarget().getPosZ() - extraZ;
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1.0F, 1.25F + rand.nextFloat() * 0.5F);
                EntityRatDragonFire beam = new EntityRatDragonFire(RatsEntityRegistry.RAT_DRAGON_FIRE, this, world, targetRelativeX, targetRelativeY, targetRelativeZ);
                beam.setPosition(extraX, extraY, extraZ);
                if (!world.isRemote) {
                    world.addEntity(beam);
                }
            }
            this.extinguish();
        }
        if (this.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_RATINATOR)) {
            if (rangedAttackCooldownLaser == 0 && this.getAttackTarget() != null) {
                rangedAttackCooldownLaser = 10;
                float radius = 0.3F;
                for (int i = 0; i < 2; i++) {
                    float angle = (0.01745329251F * (this.renderYawOffset + (i == 0 ? 90 : -90)));
                    double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + getPosX();
                    double extraZ = (double) (radius * MathHelper.cos(angle)) + getPosZ();
                    double extraY = 0.2 + getPosY();
                    double targetRelativeX = this.getAttackTarget().getPosX() - extraX;
                    double targetRelativeY = this.getAttackTarget().getPosY() + this.getAttackTarget().getHeight() / 2 - extraY;
                    double targetRelativeZ = this.getAttackTarget().getPosZ() - extraZ;
                    this.playSound(RatsSoundRegistry.LASER, 1.0F, 0.75F + rand.nextFloat() * 0.5F);
                    EntityLaserBeam beam = new EntityLaserBeam(RatlantisEntityRegistry.LASER_BEAM, world, this);
                    beam.setRGB(1.0F, 0.0F, 0.0F);
                    beam.setDamage(2.0F);
                    beam.setPosition(extraX, extraY, extraZ);
                    beam.shoot(targetRelativeX, targetRelativeY, targetRelativeZ, 2.0F, 0.4F);
                    if (!world.isRemote) {
                        world.addEntity(beam);
                    }
                }
            }
        }
        if (rangedAttackCooldownCannon > 0) {
            rangedAttackCooldownCannon--;
        }
        if (rangedAttackCooldownLaser > 0) {
            rangedAttackCooldownLaser--;
        }
        if (rangedAttackCooldownPsychic > 0) {
            rangedAttackCooldownPsychic--;
        }
        if (rangedAttackCooldownDragon > 0) {
            rangedAttackCooldownDragon--;
        }
        if (visualCooldown > 0) {
            visualCooldown--;
        }
        if (!world.isRemote) {
            inCage = inCageLogic();
        }
        if (this.isTamed()) {
            inTube = inTubeLogic();
        }
        if (poopCooldown > 0) {
            poopCooldown--;
        }
        if (this.getHeldRF() > 0 && rand.nextFloat() < 0.1F && this.getRFTransferRate() > 0) {
            this.playSound(RatsSoundRegistry.NEORATLANTEAN_IDLE, this.getSoundVolume(), 0.75F + rand.nextFloat() * 0.5F);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (this.isDancing() && this.getAnimation() != this.getDanceAnimation()) {
            this.setAnimation(this.getDanceAnimation());
        }
        if (this.isDancing() && (this.jukeboxPos == null || this.jukeboxPos.distanceSq(this.getPosX(), this.getPosY(), this.getPosZ(), true) > 15.0D * 15.0D || this.world.getBlockState(this.jukeboxPos).getBlock() != Blocks.JUKEBOX)) {
            this.setDancing(false);
        }
        if (noClip && !this.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_ETHEREAL)) {
            noClip = false;
        }
        if (getMountEntityType() != null && !this.isPassenger() && mountRespawnCooldown == 0) {
            Entity entity = getMountEntityType().create(this.world);
            entity.copyLocationAndAnglesFrom(this);
            if (!world.isRemote) {
                world.addEntity(entity);
            }
            for (int k = 0; k < 20; ++k) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                this.world.addParticle(ParticleTypes.POOF, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d2, d0, d1);
            }
            if (entity instanceof StriderEntity) {
                mountRespawnCooldown = 1000;
            }
            if (entity instanceof MobEntity && !(entity instanceof StriderEntity) && world instanceof ServerWorld) {
                ((MobEntity) entity).onInitialSpawn(((ServerWorld) world), world.getDifficultyForLocation(new BlockPos(this.getPositionVec())), SpawnReason.MOB_SUMMONED, null, null);
            }
            this.startRiding(entity, true);
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BEE) && this.randomEffectCooldown == 0) {
            this.randomEffectCooldown = 500 + rand.nextInt(500);
            if (rand.nextInt(3) == 0) {
                RatUtils.polinateAround(world, this.getPosition());
            }
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_TICK_ACCELERATOR)) {
            RatUtils.accelerateTick(world, this.getPosition().up());
            RatUtils.accelerateTick(world, this.getPosition());
            RatUtils.accelerateTick(world, this.getPosition().down());
        }
        if (mountRespawnCooldown > 0) {
            mountRespawnCooldown--;
        }
        if (randomEffectCooldown > 0) {
            randomEffectCooldown--;
        }
        if(isJumping && !world.isRemote && world.getBlockState(this.getPosition().up()).getBlock() == RatsBlockRegistry.RAT_QUARRY_PLATFORM && world.isAirBlock(this.getPosition().up(2))){
            this.setPosition(this.getPosX(), this.getPosY() + 1, this.getPosZ());
            this.getNavigator().clearPath();
        }
    }

    public boolean canDigHoles() {
        return !this.isTamed();
    }

    private boolean inCageLogic() {
        return world.getBlockState(this.getPosition()).getBlock() instanceof BlockRatCage;
    }

    private boolean shouldSitDuringAnimation() {
        boolean bool = forEachUpgradeBool((stack) -> stack.shouldSitAnimation(this), false);
        return bool || !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PLATTER) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_QUARRY) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER_ORE) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FARMER) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FISHERMAN) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CHRISTMAS);
    }

    public void createBabiesFrom(EntityRat mother, EntityRat father) {
        for (int i = 0; i < 1; i++) {
            EntityRat baby = new EntityRat(RatsEntityRegistry.RAT, this.world);
            baby.setMale(this.rand.nextBoolean());
            int babyColor = 0;
            if (father.getColorVariant() <= 3 && mother.getColorVariant() <= 3) {
                if (rand.nextInt(6) == 0) {
                    babyColor = 4 + rand.nextInt(RAT_TEXTURES.length - 5);
                } else {
                    babyColor = rand.nextInt(4);
                }
            } else {
                babyColor = rand.nextInt(RAT_TEXTURES.length);
            }
            baby.setColorVariant(babyColor);
            baby.setPosition(mother.getPosX(), mother.getPosY(), mother.getPosZ());
            baby.setGrowingAge(-24000);
            if (mother.isTamed()) {
                baby.setTamed(true);
                baby.setOwnerId(mother.getOwnerId());
            } else if (father.isTamed()) {
                baby.setTamed(true);
                baby.setOwnerId(father.getOwnerId());
            }
            world.addEntity(baby);
        }
    }

    public boolean canBeCollidedWith() {
        return (!this.isPassenger() || !(this.getRidingEntity() instanceof PlayerEntity));
    }

    public ItemStack getCookingResultFor(ItemStack stack) {
        IInventory iinventory = new Inventory(stack);
        FurnaceRecipe irecipe = this.world.getRecipeManager().getRecipe(IRecipeType.SMELTING, iinventory, this.world).orElse(null);

        SharedRecipe recipe = RatsRecipeRegistry.getRatChefRecipe(stack);
        if (recipe != null) {
            return recipe.getOutput().copy();
        }
        if (irecipe != null && !irecipe.getRecipeOutput().isEmpty()) {
            ItemStack burntItem = irecipe.getRecipeOutput().copy();
            return burntItem;
        }
        return ItemStack.EMPTY;
    }

    public ItemStack getArcheologyResultFor(ItemStack stack) {
        SharedRecipe recipe = RatsRecipeRegistry.getArcheologistRecipe(stack);
        if (recipe != null) {
            return recipe.getOutput().copy();
        }
        return ItemStack.EMPTY;
    }

    public ItemStack getGemcutterResultFor(ItemStack stack) {
        SharedRecipe recipe = RatsRecipeRegistry.getGemcutterRecipe(stack);
        if (recipe != null) {
            return recipe.getOutput().copy();
        }
        return ItemStack.EMPTY;
    }

    private void tryArcheology() {
        ItemStack heldItem = this.getHeldItemMainhand();
        ItemStack burntItem = getArcheologyResultFor(heldItem);
        if (burntItem.isEmpty()) {
            cookingProgress = 0;
        } else {
            cookingProgress++;
            if (cookingProgress == 100) {
                heldItem.shrink(1);
                if (heldItem.isEmpty()) {
                    this.setHeldItem(Hand.MAIN_HAND, burntItem);
                } else {
                    if (!this.tryDepositItemInContainers(burntItem)) {
                        if (!world.isRemote) {
                            this.entityDropItem(burntItem, 0.25F);
                        }
                    }
                }
                cookingProgress = 0;
            }
        }
    }

    private void tryCooking() {
        ItemStack heldItem = this.getHeldItemMainhand();
        ItemStack burntItem = getCookingResultFor(heldItem);
        if (burntItem.isEmpty()) {
            cookingProgress = 0;
        } else {
            cookingProgress++;
            if (cookingProgress == 100) {
                heldItem.shrink(1);
                if (heldItem.isEmpty()) {
                    this.setHeldItem(Hand.MAIN_HAND, burntItem);
                } else {
                    if (!this.tryDepositItemInContainers(burntItem)) {
                        if (!world.isRemote) {
                            this.entityDropItem(burntItem, 0.25F);
                        }
                    }
                }
                cookingProgress = 0;
            }
        }
    }

    private void tryGemcutter() {
        ItemStack heldItem = this.getHeldItemMainhand();
        ItemStack burntItem = getGemcutterResultFor(heldItem);
        if (burntItem.isEmpty()) {
            cookingProgress = 0;
        } else {
            cookingProgress++;
            if (cookingProgress == 100) {
                heldItem.shrink(1);
                if (heldItem.isEmpty()) {
                    this.setHeldItem(Hand.MAIN_HAND, burntItem);
                } else {
                    if (!this.tryDepositItemInContainers(burntItem)) {
                        if (!world.isRemote) {
                            this.entityDropItem(burntItem, 0.25F);
                        }
                    }
                }
                cookingProgress = 0;
            }
        }
    }

    private void tryEnchanting(boolean disenchant) {
        ItemStack heldItem = this.getHeldItemMainhand();
        ItemStack burntItem = ItemStack.EMPTY;
        if (heldItem.getItem() == Items.BOOK && !disenchant) {
            burntItem = heldItem.copy();
        }
        if (heldItem.getItem() == Items.ENCHANTED_BOOK && disenchant) {
            burntItem = new ItemStack(Items.BOOK, heldItem.getCount());
        }
        if (heldItem.isEnchantable() && !disenchant && !heldItem.isEnchanted()) {
            burntItem = heldItem.copy();
        }
        if (disenchant && heldItem.isEnchanted()) {
            burntItem = heldItem.copy();
            burntItem.getEnchantmentTagList().clear();
        }
        if (burntItem.isEmpty()) {
            cookingProgress = 0;
        } else {
            cookingProgress++;
            if (cookingProgress == 1000) {
                heldItem.shrink(1);
                if (!disenchant) {
                    float power = 0;
                    BlockPos position = this.getPosition();
                    for (int j = -1; j <= 1; ++j) {
                        for (int k = -1; k <= 1; ++k) {
                            if ((j != 0 || k != 0) && this.world.isAirBlock(position.add(k, 0, j)) && this.world.isAirBlock(position.add(k, 1, j))) {
                                power += getPower(world, position.add(k * 2, 0, j * 2));
                                power += getPower(world, position.add(k * 2, 1, j * 2));
                                if (k != 0 && j != 0) {
                                    power += getPower(world, position.add(k * 2, 0, j));
                                    power += getPower(world, position.add(k * 2, 1, j));
                                    power += getPower(world, position.add(k, 0, j * 2));
                                    power += getPower(world, position.add(k, 1, j * 2));
                                }
                            }
                        }
                    }
                    burntItem = EnchantmentHelper.addRandomEnchantment(this.getRNG(), burntItem, (int) (2.0F + (float) this.getRNG().nextInt(2) + power), false);
                }
                if (heldItem.isEmpty()) {
                    this.setHeldItem(Hand.MAIN_HAND, burntItem);
                } else {
                    if (!this.tryDepositItemInContainers(burntItem)) {
                        if (!world.isRemote) {
                            this.entityDropItem(burntItem, 0.25F);
                        }
                    }
                }
                cookingProgress = 0;
            }
        }
    }

    private float getPower(net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos) {
        return world.getBlockState(pos).getEnchantPowerBonus(world, pos);
    }

    private void tryGiftgiving() {
        ItemStack heldItem = this.getHeldItemMainhand();
        boolean held = false;
        int luck = 1;
        if (!world.isRemote) {
            LootContext.Builder lootcontext$builder = new LootContext.Builder((ServerWorld) this.world);
            lootcontext$builder.withLuck((float) luck); // Forge: add player & looted entity to LootContext
            LootParameterSet.Builder lootparameterset$builder = new LootParameterSet.Builder();
            List<ItemStack> result = this.world.getServer().getLootTableManager().getLootTableFromLocation(CHRISTMAS_LOOT).generate(lootcontext$builder.build(lootparameterset$builder.build()));
            if (result.isEmpty()) {
                cookingProgress = 0;
            } else {
                cookingProgress++;
                if (cookingProgress == 72000) {
                    for (ItemStack stack : result) {
                        if (heldItem.isEmpty() && !held) {
                            this.setHeldItem(Hand.MAIN_HAND, stack.copy());
                            held = true;
                        } else {
                            if (!this.tryDepositItemInContainers(stack.copy())) {
                                if (!world.isRemote) {
                                    this.entityDropItem(stack.copy(), 0.25F);
                                }
                            }
                        }
                    }

                    cookingProgress = 0;
                }
            }
        }
    }

    private boolean tryDepositItemInContainers(ItemStack burntItem) {
        if (world.getTileEntity(new BlockPos(this.getPositionVec())) != null) {
            TileEntity te = world.getTileEntity(new BlockPos(this.getPositionVec()));
            LazyOptional<IItemHandler> handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP);
            if (handler.orElse(null) != null) {
                if (ItemHandlerHelper.insertItem(handler.orElse(null), burntItem, true).isEmpty()) {
                    ItemHandlerHelper.insertItem(handler.orElse(null), burntItem, false);
                    return true;
                }
            }
        }
        return false;
    }

    protected void eatItem(ItemStack stack, int eatingParticleCount) {
        if (!stack.isEmpty()) {
            if (stack.getUseAction() == UseAction.DRINK) {
                this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }
            if (RatUtils.isRatFood(stack) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ORE_DOUBLING)) {
                for (int i = 0; i < eatingParticleCount; ++i) {
                    Vector3d vec3d = new Vector3d(((double) this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
                    vec3d = vec3d.rotatePitch(-this.rotationPitch * 0.017453292F);
                    vec3d = vec3d.rotateYaw(-this.rotationYaw * 0.017453292F);
                    double d0 = (double) (-this.rand.nextFloat()) * 0.6D - 0.3D;
                    Vector3d vec3d1 = new Vector3d(((double) this.rand.nextFloat() - 0.5D) * 0.3D, d0, 0.1D);
                    vec3d1 = vec3d1.rotatePitch(-this.rotationPitch * 0.017453292F);
                    vec3d1 = vec3d1.rotateYaw(-this.rotationYaw * 0.017453292F);
                    vec3d1 = vec3d1.add(this.getPosX(), this.getPosY() + 0.25D, this.getPosZ());
                    this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, stack), vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05D, vec3d.z);

                }
                this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.25F + 0.25F * (float) this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.3F);
            }
        }
    }

    private void digTarget() {
        if (diggingPos != null) {
            ++this.breakingTime;
            int i = (int) ((float) this.breakingTime / 160.0F * 10.0F);
            //this.moveController.action = MovementController.Action.WAIT;
            if (this.getNavigator().getPath() != null) {
                this.getNavigator().clearPath();
            }
            this.setMotion(0, 0, 0);
            if (breakingTime % 40 == 0) {
                this.playSound(RatsSoundRegistry.RAT_DIG, this.getSoundVolume(), this.getSoundPitch());
            }
            if (i != this.previousBreakProgress) {
                this.world.sendBlockBreakProgress(this.getEntityId(), diggingPos, i);
                this.previousBreakProgress = i;

            }

            if (this.breakingTime == 160) {
                this.breakingTime = 0;
                this.previousBreakProgress = -1;
                BlockState prevState = world.getBlockState(diggingPos);
                double d1 = (finalDigPathPoint == null ? this.getPosX() : finalDigPathPoint.getX()) - diggingPos.getX();
                double d2 = (finalDigPathPoint == null ? this.getPosZ() : finalDigPathPoint.getZ()) - diggingPos.getZ();
                float rotation = -((float) MathHelper.atan2(d1, d2)) * (180F / (float) Math.PI);
                Direction facing = Direction.byHorizontalIndex(MathHelper.floor((double) (rotation * 4.0F / 360.0F) + 0.5D) & 3);
                world.setBlockState(diggingPos, RatsBlockRegistry.RAT_HOLE.getDefaultState());
                if (world.getBlockState(diggingPos).getBlock() instanceof BlockRatHole) {
                    TileEntity tileentity1 = world.getTileEntity(diggingPos);
                    if (tileentity1 instanceof TileEntityRatHole) {
                        ((TileEntityRatHole) tileentity1).setImmitatedBlockState(prevState);
                    }
                }
                digCooldown = 3000;
                diggingPos = null;
            }
            if (diggingPos != null && this.getRatDistanceCenterSq(diggingPos.getX(), diggingPos.getY(), diggingPos.getZ()) > 2F) {
                this.breakingTime = 0;
                this.previousBreakProgress = -1;
                this.world.sendBlockBreakProgress(this.getEntityId(), diggingPos, 0);
                diggingPos = null;
            }
        } else {
            this.breakingTime = 0;
            this.previousBreakProgress = -1;
        }

    }

    protected void collideWithEntity(Entity entityIn) {
        if (!isInRatHole() && !crafting && !inTube()) {
            entityIn.applyEntityCollision(this);
        }
        if (this.hasPlague()) {
            if (entityIn instanceof EntityRat && !((EntityRat) entityIn).isTamed()) {
                ((EntityRat) entityIn).setPlague(true);
            } else if (entityIn instanceof LivingEntity && rollForPlague((LivingEntity) entityIn)) {
                if (((LivingEntity) entityIn).getActivePotionEffect(RatsMod.PLAGUE_POTION) != null) {
                    ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(RatsMod.PLAGUE_POTION, 6000));
                }
            }
        }
    }

    private boolean rollForPlague(LivingEntity target) {
        boolean mask = target.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == RatsItemRegistry.PLAGUE_DOCTOR_MASK || target.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == RatsItemRegistry.BLACK_DEATH_MASK;
        if (mask) {
            return rand.nextFloat() < 0.3F;
        }
        return true;
    }

    public boolean isInRatHole() {
        return RatUtils.isRatHoleInBoundingBox(this.getBoundingBox().grow(0.5D, 0.5D, 0.5D), world);
    }

    private void findDigTarget() {
        if (this.getNavigator() instanceof RatPathPathNavigateGround) {
            if (((RatPathPathNavigateGround) this.getNavigator()).targetPosition != null) {
                BlockPos target = ((RatPathPathNavigateGround) this.getNavigator()).targetPosition.down();
                if (world.getTileEntity(target) != null) {
                    finalDigPathPoint = target;
                }
            }
            if (!this.collidedHorizontally || !this.getNavigator().noPath()) {
                return;
            }
            if (finalDigPathPoint != null) {
                BlockPos digPos = rayTraceBlockPos(finalDigPathPoint);
                if (digPos != null && this.getRatDistanceCenterSq(digPos.getX(), digPos.getY(), digPos.getZ()) < 2) {
                    if (world.getTileEntity(digPos) == null) {
                        Material material = world.getBlockState(digPos).getMaterial();
                        if (RatUtils.canRatBreakBlock(world, digPos, this) && canDigBlock(world, digPos) && digPos.getY() == (int) Math.round(this.getPosY())) {
                            diggingPos = digPos;
                        }
                    }
                }
            }
        }
    }

    private boolean canDigBlock(World world, BlockPos pos) {
        return world.getBlockState(pos).isOpaqueCube(world, pos);
    }

    public BlockPos rayTraceBlockPos(BlockPos targetPos) {
        RayTraceResult rayTrace = RatUtils.rayTraceBlocksIgnoreRatholes(world, this.getPositionVec().add(0, this.getEyeHeight(), 0), new Vector3d(targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5), false, this);
        if (rayTrace instanceof BlockRayTraceResult) {
            BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) rayTrace;
            BlockPos pos = blockRayTraceResult.getPos();
            BlockPos sidePos = blockRayTraceResult.getPos().offset(blockRayTraceResult.getFace());
            if (!world.isAirBlock(sidePos)) {
                return sidePos;
            } else if (!world.isAirBlock(pos)) {
                return pos;
            } else {
                return null;
            }
        }
        return null;
    }

    public void updateAIgoalSelector() {
        if (this.moveController.isUpdating()) {
            double d0 = this.moveController.getSpeed();
            if (d0 == 0.6D) {
                this.setSneaking(true);
                this.setSprinting(false);
            } else if (d0 >= 1.1D && d0 < 2) {
                this.setSneaking(false);
                this.setSprinting(true);
            } else {
                this.setSneaking(false);
                this.setSprinting(false);
            }
        } else {
            this.setSneaking(false);
            this.setSprinting(false);
        }
    }

    protected void createRunningParticles() {
    }

    public void travel(Vector3d vec3d) {
        if (!this.canMove()) {
            if (this.getNavigator().getPath() != null) {
                this.getNavigator().clearPath();
            }
            vec3d = Vector3d.ZERO;
        }
        super.travel(vec3d);
    }

    public void openGUI(PlayerEntity playerEntity) {
        if (!this.world.isRemote && (!this.isBeingRidden() || this.isPassenger(playerEntity))) {
            NetworkHooks.openGui((ServerPlayerEntity) playerEntity, new INamedContainerProvider() {
                @Override
                public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
                    return new ContainerRat(p_createMenu_1_, ratInventory, p_createMenu_2_, EntityRat.this);
                }

                @Override
                public ITextComponent getDisplayName() {
                    return new TranslationTextComponent("entity.rats.rat");
                }
            });
        }
        RatsMod.PROXY.setRefrencedRat(this);
    }

    public boolean canMove() {
        return this.diggingPos == null && !this.isSitting() && !this.isInWheel() && this.getCommand().freeMove && !this.isChild();
    }

    public boolean isInCage() {
        return inCage;
    }

    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_EAT, ANIMATION_IDLE_SCRATCH, ANIMATION_IDLE_SNIFF, ANIMATION_DANCE_0, ANIMATION_DANCE_1};
    }

    public Animation getDanceAnimation() {
        switch (this.getDanceMoves()) {
            case 0:
                return ANIMATION_DANCE_0;
            default:
                return NO_ANIMATION;
        }
    }

    public boolean canPhaseThroughBlock(IWorld world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() instanceof FenceBlock
                || world.getBlockState(pos).getBlock() instanceof LeavesBlock && this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK);
    }

    public void setKilledInTrap() {
        isDeadInTrap = true;
        this.attackEntityFrom(DamageSource.IN_WALL, Float.MAX_VALUE);
    }

    protected boolean canDropLoot() {
        return super.canDropLoot() && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ANGEL);
    }

    protected boolean func_230282_cS_() {
        return canDropLoot();
    }

    protected void dropInventory() {
        super.dropInventory();
        if (!this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ANGEL)) {
            for(EquipmentSlotType slot : EquipmentSlotType.values()){
                if(!this.getItemStackFromSlot(slot).isEmpty()){
                    this.entityDropItem(this.getItemStackFromSlot(slot));
                }
            }
        }

    }

    protected void onDeathUpdate() {
        ++this.deathTime;
        int maxDeathTime = isDeadInTrap ? 60 : 20;
        if (this.deathTime == maxDeathTime) {
            if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT))) {
                int i = this.getExperiencePoints(this.attackingPlayer);
                i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
                while (i > 0) {
                    int j = ExperienceOrbEntity.getXPSplit(i);
                    i -= j;
                    this.world.addEntity(new ExperienceOrbEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ(), j));
                }

                if (this.hasToga()) {
                    this.entityDropItem(new ItemStack(RatlantisItemRegistry.RAT_TOGA), 0.0F);
                    if (this.world.getDimensionKey().getRegistryName().getPath().equals("ratlantis")) {
                        boolean flag = false;
                        if (!flag && rand.nextFloat() < 0.01F) {
                            this.entityDropItem(new ItemStack(Items.DIAMOND), 0.0F);
                            flag = true;
                        }
                        if (!flag && rand.nextFloat() < 0.6F) {
                            this.entityDropItem(new ItemStack(RatsItemRegistry.CHEESE, 1 + rand.nextInt(3)), 0.0F);
                            flag = true;
                        }
                        if (!flag && rand.nextFloat() < 0.3F) {
                            this.entityDropItem(new ItemStack(Items.GOLD_INGOT), 0.0F);
                            flag = true;
                        }
                        if (!flag && rand.nextFloat() < 0.3F) {
                            this.entityDropItem(new ItemStack(Items.PRISMARINE_CRYSTALS), 0.0F);
                            flag = true;
                        }
                        if (!flag && rand.nextFloat() < 0.3F) {
                            this.entityDropItem(new ItemStack(Items.BOOK), 0.0F);
                            flag = true;
                        }
                    }
                }
            }
            spawnAngelCopy();
            this.remove();
            for (int k = 0; k < 20; ++k) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                this.world.addParticle(ParticleTypes.CLOUD, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d2, d0, d1);
            }
        }
    }

    public void remove() {
        if (!isAlive() && this.isTamed() && this.getMonsterOwner() != null && this.getMonsterOwner() instanceof ISummonsRats) {
            ISummonsRats illagerPiper = (ISummonsRats) this.getMonsterOwner();
            illagerPiper.setRatsSummoned(illagerPiper.getRatsSummoned() - 1);
            this.setOwnerId(null);
            this.setTamedByMonster(false);
        }
        super.remove();
    }

    public void spawnAngelCopy() {
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ANGEL)) {
            EntityRat copy = new EntityRat(RatsEntityRegistry.RAT, world);
            CompoundNBT nbt = new CompoundNBT();
            this.writeAdditional(nbt);
            nbt.putBoolean("NoAI", false);
            nbt.putShort("HurtTime", (short) 0);
            nbt.putInt("HurtByTimestamp", 0);
            nbt.putShort("DeathTime", (short) 0);
            copy.readAdditional(nbt);
            copy.setHealth(copy.getMaxHealth());
            copy.copyLocationAndAnglesFrom(this);
            copy.setRespawnCountdown(1200);
            world.addEntity(copy);
        }
    }

    public void updateRidden() {
        Entity entity = this.getRidingEntity();
        if (entity != null && (!entity.isAlive() || entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() <= 0.0F)) {
            this.stopRiding();
        } else {
            this.setMotion(0, 0, 0);
            this.tick();
            if (this.isPassenger()) {
                if (this.getRidingEntity() instanceof EntityRatMountBase || this.getRidingEntity() instanceof EntityRatBaronPlane || this.getRidingEntity() instanceof EntityRattlingGun) {
                    this.rotationYaw = this.renderYawOffset;
                    super.updateRidden();
                } else {
                    this.updateRiding(entity);
                }
            }
        }
    }

    public boolean writeUnlessRemoved(CompoundNBT compound) {
        String s = this.getEntityString();
        compound.putString("id", s);
        this.writeWithoutTypeId(compound);
        return true;
    }

    public void removePassengers() {

    }

    public void updateRiding(Entity riding) {
        if (riding != null && riding.isPassenger(this) && riding instanceof PlayerEntity) {
            int i = riding.getPassengers().indexOf(this);
            float radius = (i == 0 ? 0F : 0.4F) + (((PlayerEntity) riding).isElytraFlying() ? 2 : 0);
            float angle = (0.01745329251F * ((PlayerEntity) riding).renderYawOffset) + (i == 2 ? -92.5F : i == 1 ? 92.5F : 0);
            double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
            double extraZ = radius * MathHelper.cos(angle);
            double extraY = (riding.isSneaking() ? 1.1D : 1.4D);
            this.rotationYaw = ((PlayerEntity) riding).rotationYawHead;
            this.rotationYawHead = ((PlayerEntity) riding).rotationYawHead;
            this.prevRotationYaw = ((PlayerEntity) riding).rotationYawHead;
            this.setPosition(riding.getPosX() + extraX, riding.getPosY() + extraY, riding.getPosZ() + extraZ);
            if (((PlayerEntity) riding).isElytraFlying()) {
                this.stopRiding();
            }
        }
        if (riding != null && riding.isPassenger(this) && riding instanceof StriderEntity) {
            StriderEntity strider = (StriderEntity) riding;
            riding.extinguish();
            this.setPosition(riding.getPosX(), riding.getPosY() + strider.getMountedYOffset() + 0.15F, riding.getPosZ());
            strider.boost();
            //strider.getAttribute(Attributes.field_233821_d_).setBaseValue(0.2D);
            if (!this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_STRIDER_MOUNT) && this.getType() != RatsEntityRegistry.DEMON_RAT) {
                for (int k = 0; k < 20; ++k) {
                    double d2 = this.rand.nextGaussian() * 0.02D;
                    double d0 = this.rand.nextGaussian() * 0.02D;
                    double d1 = this.rand.nextGaussian() * 0.02D;
                    this.world.addParticle(ParticleTypes.POOF, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d2, d0, d1);
                }
                strider.remove();
            }
        }
    }

    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (this.getRespawnCountdown() > 0) {
            return ActionResultType.PASS;
        }
        if (itemstack.getItem() == RatsItemRegistry.FILTH_CORRUPTION) {
            this.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1F, 1.5F);
            this.remove();
            EntityRatKing ratKing = new EntityRatKing(RatsEntityRegistry.RAT_KING, this.world);
            ratKing.copyLocationAndAnglesFrom(this);
            if (!world.isRemote) {
                ratKing.onInitialSpawn((IServerWorld) world, world.getDifficultyForLocation(this.getPosition()), SpawnReason.CONVERSION, null, null);
            }
            world.addEntity(ratKing);
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            return ActionResultType.SUCCESS;
        }
        if (itemstack.getItem() == RatlantisItemRegistry.RAT_TOGA) {
            if (!this.hasToga()) {
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
            } else {
                if (!world.isRemote) {
                    this.entityDropItem(new ItemStack(RatlantisItemRegistry.RAT_TOGA), 0.0F);
                }
            }
            this.setToga(!this.hasToga());
            this.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1F, 1.5F);
        }
        if (itemstack.getItem() == RatsItemRegistry.CREATIVE_CHEESE && this.canBeTamed()) {
            this.setTamed(true);
            this.world.setEntityState(this, (byte) 83);
            this.setTamedBy(player);
            return ActionResultType.SUCCESS;
        }
        if (itemstack.interactWithEntity(player, this, hand) != ActionResultType.PASS) {
            return ActionResultType.SUCCESS;
        }
        if (super.func_230254_b_(player, hand) != ActionResultType.SUCCESS) {
            if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CARRAT)) {
                if (player.getFoodStats().needFood()) {
                    player.getFoodStats().addStats(1, 0.1F);
                    player.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1.0F, 1.0F);
                    for (int i = 0; i < 8; i++) {
                        double d0 = this.rand.nextGaussian() * 0.02D;
                        double d1 = this.rand.nextGaussian() * 0.02D;
                        double d2 = this.rand.nextGaussian() * 0.02D;
                        this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(Items.CARROT)), this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight() * 2.0F) - (double) this.getHeight(), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                    }
                    return ActionResultType.SUCCESS;
                }
            }
            if (this.isTamed() && !this.isChild() && isOwner(player)) {
                if (itemstack.getItem() == Item.getItemFromBlock(RatsBlockRegistry.DYE_SPONGE)) {
                    this.setDyed(false);
                    this.setDyeColor(0);
                    for (int i = 0; i < 8; i++) {
                        double d0 = this.rand.nextGaussian() * 0.02D;
                        double d1 = this.rand.nextGaussian() * 0.02D;
                        double d2 = this.rand.nextGaussian() * 0.02D;
                        this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(Item.getItemFromBlock(RatsBlockRegistry.DYE_SPONGE))), this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight() * 2.0F) - (double) this.getHeight(), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                    }
                    this.playSound(SoundEvents.ITEM_BUCKET_FILL, getSoundVolume(), getSoundPitch());
                    return ActionResultType.SUCCESS;
                }
                if (itemstack.getItem() == RatsItemRegistry.RATBOW_ESSENCE) {
                    if (!this.isDyed()) {
                        this.setDyed(true);
                    }
                    if (this.getDyeColor() != 100) {
                        this.setDyeColor(100);
                        for (int i = 0; i < 8; i++) {
                            double d0 = this.rand.nextGaussian() * 0.02D;
                            double d1 = this.rand.nextGaussian() * 0.02D;
                            double d2 = this.rand.nextGaussian() * 0.02D;
                            this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(RatsItemRegistry.RATBOW_ESSENCE)), this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight() * 2.0F) - (double) this.getHeight(), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                        }
                        this.playSound(SoundEvents.ENTITY_SLIME_ATTACK, getSoundVolume(), getSoundPitch());
                        itemstack.shrink(1);
                        return ActionResultType.SUCCESS;
                    }
                }
                if (itemstack.getItem() instanceof DyeItem) {
                    DyeItem dyeItem = (DyeItem) itemstack.getItem();
                    if (!this.isDyed()) {
                        this.setDyed(true);
                    }
                    if (this.getDyeColor() != dyeItem.getDyeColor().getId()) {
                        this.setDyeColor(dyeItem.getDyeColor().getId());
                        for (int i = 0; i < 8; i++) {
                            double d0 = this.rand.nextGaussian() * 0.02D;
                            double d1 = this.rand.nextGaussian() * 0.02D;
                            double d2 = this.rand.nextGaussian() * 0.02D;
                            this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(dyeItem)), this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight() * 2.0F) - (double) this.getHeight(), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                        }
                        this.playSound(SoundEvents.ENTITY_SLIME_ATTACK, getSoundVolume(), getSoundPitch());
                        itemstack.shrink(1);
                        return ActionResultType.SUCCESS;
                    }
                }
                if (itemstack.getItem() == RatsItemRegistry.RAT_SACK) {
                    CompoundNBT compound = itemstack.getTag();
                    if (compound == null) {
                        compound = new CompoundNBT();
                        itemstack.setTag(compound);
                    }
                    CompoundNBT ratTag = new CompoundNBT();
                    this.writeAdditional(ratTag);
                    int currentRat = ItemRatSack.getRatsInStack(itemstack) + 1;
                    compound.put("Rat_" + currentRat, ratTag);
                    this.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1, 1);
                    this.remove();
                    player.swingArm(hand);
                    return ActionResultType.SUCCESS;
                } else if (itemstack.getItem() == RatsItemRegistry.CHEESE_STICK || itemstack.getItem() == RatsItemRegistry.RADIUS_STICK) {
                    itemstack.getTag().putUniqueId("RatUUID", this.getUniqueID());
                    player.swingArm(hand);
                    if(!world.isRemote && player instanceof ServerPlayerEntity){
                        RatsMod.sendNonLocal(new MessageCheeseStaffRat(this.getEntityId(), false, false), (ServerPlayerEntity)player);
                    }
                    player.sendStatusMessage(new TranslationTextComponent("entity.rats.rat.staff.bind", this.getName()), true);
                    return ActionResultType.SUCCESS;
                } else if (itemstack.getItem() == Items.ARROW) {
                    itemstack.shrink(1);
                    ItemStack ratArrowStack = new ItemStack(RatsItemRegistry.RAT_ARROW);
                    CompoundNBT compound = new CompoundNBT();
                    CompoundNBT ratTag = new CompoundNBT();
                    this.writeAdditional(ratTag);
                    compound.put("Rat", ratTag);
                    ratArrowStack.setTag(compound);
                    if (itemstack.isEmpty()) {
                        player.setHeldItem(hand, ratArrowStack);
                    } else if (!player.inventory.addItemStackToInventory(ratArrowStack)) {
                        player.dropItem(ratArrowStack, false);
                    }
                    this.playSound(RatsSoundRegistry.RAT_HURT, 1, 1);
                    player.swingArm(hand);
                    this.remove();
                    return ActionResultType.SUCCESS;
                } else if (!player.isSneaking() && this.canBeTamed()) {
                    openGUI(player);
                    return ActionResultType.SUCCESS;
                } else {
                    if (player.getPassengers().size() < 3) {
                        player.sendStatusMessage(new TranslationTextComponent("entity.rats.rat.dismount_instructions"), true);
                        this.startRiding(player, true);
                    }
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.PASS;
    }

    public void setTamed(boolean tamed) {
        if (tamed) {
            Arrays.fill(this.inventoryArmorDropChances, 1.0F);
            Arrays.fill(this.inventoryHandsDropChances, 1.0F);
        }
        super.setTamed(tamed);
    }

    public int getTalkInterval() {
        if (this.hasPlague() && this.isTamed()) {
            return 200;
        }
        return super.getTalkInterval();
    }


    public boolean canBeTamed() {
        return !this.hasPlague();
    }

    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
        if (slotIn == EquipmentSlotType.MAINHAND) {
            return ratInventory.getStackInSlot(0);
        } else if (slotIn == EquipmentSlotType.HEAD) {
            return ratInventory.getStackInSlot(1);
        } else if (slotIn == EquipmentSlotType.OFFHAND) {
            return ratInventory.getStackInSlot(2);
        }else if (slotIn == EquipmentSlotType.CHEST) {
            return ratInventory.getStackInSlot(3);
        }else if (slotIn == EquipmentSlotType.LEGS) {
            return ratInventory.getStackInSlot(4);
        }else if (slotIn == EquipmentSlotType.FEET) {
            return ratInventory.getStackInSlot(5);
        }
        return super.getItemStackFromSlot(slotIn);
    }

    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {
        if (slotIn == EquipmentSlotType.MAINHAND) {
            ratInventory.setInventorySlotContents(0, stack);
        } else if (slotIn == EquipmentSlotType.HEAD) {
            ratInventory.setInventorySlotContents(1, stack);
        } else if (slotIn == EquipmentSlotType.OFFHAND) {
            ratInventory.setInventorySlotContents(2, stack);
        } else if (slotIn == EquipmentSlotType.CHEST) {
            ratInventory.setInventorySlotContents(3, stack);
        } else if (slotIn == EquipmentSlotType.LEGS) {
            ratInventory.setInventorySlotContents(4, stack);
        } else if (slotIn == EquipmentSlotType.FEET) {
            ratInventory.setInventorySlotContents(5, stack);
        } else {
            super.getItemStackFromSlot(slotIn);
        }
    }

    private void initInventory() {
        ratInventory = new Inventory(6);
        ratInventory.addListener(new RatInvListener(this));
        //ratInventory.setCustomName(this.getName());
        if (ratInventory != null) {
            refreshUpgrades = true;
            for (int j = 0; j < ratInventory.getSizeInventory(); ++j) {
                ItemStack itemstack = ratInventory.getStackInSlot(j);
                if (!itemstack.isEmpty()) {
                    ratInventory.setInventorySlotContents(j, itemstack.copy());
                }
            }
        }
        setupDynamicAI();
    }

    public boolean onLivingFall(float distance, float damageMultiplier) {
        if (!this.hasFlightUpgrade() && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_QUARRY) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER_ORE) && !this.inTube() && !this.isPassenger()) {
            return super.onLivingFall(distance, damageMultiplier);
        }
        return false;
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        if (!this.hasFlightUpgrade() && !this.inTube()) {
            super.updateFallState(y, onGroundIn, state, pos);
        }
    }

    public RatStatus getRatStatus() {
        return status;
    }

    public void setRatStatus(RatStatus status) {
        if (this.status.canBeOverriden(this)) {
            this.status = status;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 82) {
            this.playEffect(0);
        } else if (id == 83) {
            this.playEffect(1);
        } else if (id == 84) {
            this.playEffect(2);
        } else if (id == 85) {
            this.crafting = true;
        } else if (id == 86) {
            this.crafting = false;
        } else if (id == 101) {
            this.playEffect(3);
        } else if (id == 125) {
            //snowflake
        } else if (id == 126) {
            this.playEffect(5);
        } else {
            super.handleStatusUpdate(id);
        }
    }

    protected void playEffect(int type) {
        if (type == 3) {
            for (int j = 0; j < 5; ++j) {
                double d6 = (double) (j) / 5D;
                float f = (this.rand.nextFloat() - 0.5F) * 0.2F;
                float f1 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                float f2 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                double d3 = this.prevPosX + (this.getPosX() - this.prevPosX) * d6 + (rand.nextDouble() - 0.5D) * this.getWidth() * 2.0D;
                double d4 = this.prevPosY + (this.getPosY() - this.prevPosY) * d6 + rand.nextDouble() * (double) this.getHeight();
                double d5 = this.prevPosZ + (this.getPosZ() - this.prevPosZ) * d6 + (rand.nextDouble() - 0.5D) * this.getWidth() * 2.0D;
                world.addParticle(ParticleTypes.SPLASH, d3, d4, d5, f, f1, f2);
            }
        } else if (type == 2) {
            for (int j = 0; j < 5; ++j) {
                double d6 = (double) (j) / 5D;
                float f = (this.rand.nextFloat() - 0.5F) * 0.2F;
                float f1 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                float f2 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                double d3 = this.prevPosX + (this.getPosX() - this.prevPosX) * d6 + (rand.nextDouble() - 0.5D) * this.getWidth() * 2.0D;
                double d4 = this.prevPosY + (this.getPosY() - this.prevPosY) * d6 + rand.nextDouble() * (double) this.getHeight();
                double d5 = this.prevPosZ + (this.getPosZ() - this.prevPosZ) * d6 + (rand.nextDouble() - 0.5D) * this.getWidth() * 2.0D;
                world.addParticle(ParticleTypes.PORTAL, d3, d4, d5, f, f1, f2);
            }
        } else {
            BasicParticleType p = ParticleTypes.SMOKE;

            if (type == 1) {
                p = ParticleTypes.HEART;
            }
            if (type == 5) {
                p = ParticleTypes.ITEM_SNOWBALL;
            }
            for (int i = 0; i < 9; ++i) {
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                double d2 = this.rand.nextGaussian() * 0.02D;
                this.world.addParticle(p, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + 0.5D + (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
            }
        }
    }

    public boolean isMoving() {
        return Math.abs(this.getMotion().x) >= 0.05D || Math.abs(this.getMotion().z) >= 0.05D;
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setColorVariant(this.getRNG().nextInt(4));
        this.setMale(this.getRNG().nextBoolean());
        if (this.getRNG().nextInt(15) == 0 && this.world.getDifficulty() != Difficulty.PEACEFUL && RatConfig.plagueRats && reason != SpawnReason.CONVERSION) {
            this.setPlague(true);
        }
        if (this.getItemStackFromSlot(EquipmentSlotType.HEAD).isEmpty()) {
            LocalDate localdate = LocalDate.now();
            int i = localdate.get(ChronoField.DAY_OF_MONTH);
            int j = localdate.get(ChronoField.MONTH_OF_YEAR);
            if (j == 10 && i > 10 && this.rand.nextFloat() <= 0.25F) {
                this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Blocks.PUMPKIN));
                this.inventoryArmorDropChances[EquipmentSlotType.HEAD.getIndex()] = 1.0F;
            }
            if ((j == 11 && i > 15 || j == 12 || j == 1 && i <= 10) && this.rand.nextFloat() <= 0.25F) {
                this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(RatsItemRegistry.SANTA_HAT));
                this.inventoryArmorDropChances[EquipmentSlotType.HEAD.getIndex()] = 1.0F;
            }
        }
        return spawnDataIn;
    }

    @OnlyIn(Dist.CLIENT)
    public String getRatTexture() {
        return RAT_TEXTURES[MathHelper.clamp(this.getColorVariant(), 0, RAT_TEXTURES.length - 1)];
    }

    public ITextComponent getName() {
        if (this.hasPlague()) {
            return new TranslationTextComponent("entity.rats.plague_rat");
        }
        return super.getName(); // Forge: Use getter to allow overriding by mods
    }

    public boolean shouldHuntAnimal() {
        return this.getCommandInteger() == 3;
    }

    public boolean shouldHuntMonster() {
        return this.getCommandInteger() == 7;
    }

    public boolean shouldWander() {
        int cmd = this.getCommandInteger();
        return cmd != 1 && cmd != 4 && cmd != 5 && cmd != 6;
    }

    public boolean shouldEyesGlow() {
        return this.hasPlague() || this.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_NONBELIEVER) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DRAGON) || this.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_RATINATOR) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ENDER) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_AQUATIC) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DEMON);
    }

    protected SoundEvent getAmbientSound() {
        if (this.hasPlague() && this.getAttackTarget() != null) {
            return RatsSoundRegistry.RAT_PLAGUE;
        }
        if (!this.hasPlague() && this.isTamed() && this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CHRISTMAS)) {
            return RatsSoundRegistry.RAT_SANTA;
        }
        if (!this.hasPlague() && this.getHealth() <= this.getMaxHealth() / 2D || this.isChild()) {
            return RatsSoundRegistry.RAT_IDLE;
        }
        if (RatsMod.ICEANDFIRE_LOADED && this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DRAGON)) {
            SoundEvent possibleDragonSound = Registry.SOUND_EVENT.getOrDefault(new ResourceLocation("iceandfire", "firedragon_child_idle"));
            if (possibleDragonSound != null) {
                return possibleDragonSound;
            }
        }
        return super.getAmbientSound();
    }

    protected SoundEvent getDeathSound() {
        if (RatsMod.ICEANDFIRE_LOADED && this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DRAGON)) {
            SoundEvent possibleDragonSound = Registry.SOUND_EVENT.getOrDefault(new ResourceLocation("iceandfire", "firedragon_child_death"));
            if (possibleDragonSound != null) {
                return possibleDragonSound;
            }
        }
        return RatsSoundRegistry.RAT_DIE;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        if (RatsMod.ICEANDFIRE_LOADED && this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DRAGON)) {
            SoundEvent possibleDragonSound = Registry.SOUND_EVENT.getOrDefault(new ResourceLocation("iceandfire", "firedragon_child_hurt"));
            if (possibleDragonSound != null) {
                return possibleDragonSound;
            }
        }
        return RatsSoundRegistry.RAT_HURT;
    }

    public boolean onHearFlute(PlayerEntity player, RatCommand ratCommand) {
        if (this.isTamed() && this.isOwner(player) && !this.isChild() && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_NO_FLUTE)) {
            this.setCommand(ratCommand);
            return true;
        }
        return false;
    }

    public boolean canRatPickupItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if ((this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BLACKLIST) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_WHITELIST)) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER)) {
            CompoundNBT nbttagcompound1;
            if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BLACKLIST)) {
                nbttagcompound1 = this.getUpgrade(RatsItemRegistry.RAT_UPGRADE_BLACKLIST).getTag();
            } else {
                nbttagcompound1 = this.getUpgrade(RatsItemRegistry.RAT_UPGRADE_WHITELIST).getTag();
            }
            String ourItemID = stack.getItem().getRegistryName().toString();
            if (nbttagcompound1 != null && nbttagcompound1.contains("Items", 9)) {
                ListNBT nbttaglist = nbttagcompound1.getList("Items", 10);
                if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BLACKLIST)) {
                    for (int i = 0; i < nbttaglist.size(); ++i) {
                        String itemID = nbttaglist.getCompound(i).getString("id");
                        if (ourItemID != null && ourItemID.equals(itemID)) {
                            return false;
                        }
                    }
                    return true;
                } else {
                    //whitelist
                    for (int i = 0; i < nbttaglist.size(); ++i) {
                        String itemID = nbttaglist.getCompound(i).getString("id");
                        if (ourItemID != null && ourItemID.equals(itemID)) {
                            return true;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public boolean attemptTeleport(double x, double y, double z) {
        double d0 = this.getPosX();
        double d1 = this.getPosY();
        double d2 = this.getPosZ();
        this.setPosition(x, y, z);
        this.world.setEntityState(this, (byte) 84);
        boolean flag = false;
        BlockPos blockpos = new BlockPos(this.getPositionVec());
        World world = this.world;
        Random random = this.getRNG();

        if (world.isBlockLoaded(blockpos)) {
            boolean flag1 = false;

            while (!flag1 && blockpos.getY() > 0) {
                BlockPos blockpos1 = blockpos.down();
                BlockState BlockState = world.getBlockState(blockpos1);

                if (BlockState.getMaterial().blocksMovement()) {
                    flag1 = true;
                } else {
                    this.setPosition(this.getPosX(), this.getPosY() - 1, this.getPosZ());
                    blockpos = blockpos1;
                }
            }

            if (flag1) {
                this.setPositionAndUpdate(this.getPosX(), this.getPosY(), this.getPosZ());

                if (world.hasNoCollisions(this) && !world.containsAnyLiquid(this.getBoundingBox())) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            this.setPositionAndUpdate(d0, d1, d2);
            return false;
        } else {
            int i = 128;
            this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            return true;
        }
    }

    public boolean isDirectPathBetweenPoints(Vector3d target) {
        RayTraceResult rayTrace = RatUtils.rayTraceBlocksIgnoreRatholes(world, getPositionVec(), target.add(0.5, 0.5, 0.5), false, this);
        if (rayTrace instanceof BlockRayTraceResult) {
            BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) rayTrace;
            BlockPos pos = blockRayTraceResult.getPos();
            BlockPos sidePos = blockRayTraceResult.getPos().offset(blockRayTraceResult.getFace());
            if (!world.isAirBlock(pos) || !world.isAirBlock(sidePos)) {
                return true;
            } else {
                return rayTrace.getType() == RayTraceResult.Type.MISS;
            }
        }
        return true;
    }

    public BlockPos getLightPosition() {
        BlockPos pos = new BlockPos(this.getPositionVec());
        if (!world.getBlockState(pos).isSolid()) {
            return pos.up();
        }
        return pos;
    }

    public boolean inTube() {
        return inTube;
    }

    private boolean inTubeLogic() {
        if (this.isTamed()) {
            BlockPos pos = new BlockPos(this.getPositionVec());
            BlockState state = world.getBlockState(pos);
            boolean above = world.getBlockState(pos.up()).getBlock() instanceof BlockRatTube;
            if (state.getBlock() instanceof BlockRatTube) {
                List<VoxelShape> aabbs = ((BlockRatTube) state.getBlock()).compileVoxelList(world, pos, state);
                AxisAlignedBB bb = new AxisAlignedBB(0.5, 0.5, 0.5, 0.5, 0.5, 0.5);
                for (VoxelShape box : aabbs) {
                    bb = bb.union(box.getBoundingBox());
                }
                bb = bb.grow(0.05F, 0, 0.05F).offset(pos);
                return bb.contains(this.getPositionVec().add(0, this.getHeight() / 2, 0)) || bb.contains(this.getPositionVec()) && above;
            }
        }
        return false;
    }
    public boolean isAIDisabled() {
        return super.isAIDisabled() || this.getRespawnCountdown() > 0;
    }

    public void forEachUpgrade(Consumer<ItemRatUpgrade> function) {
        for(EquipmentSlotType slot : RatUtils.UPGRADE_SLOTS){
            ItemStack stack = getItemStackFromSlot(slot);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ItemRatUpgradeCombined) {
                    CompoundNBT CompoundNBT1 = stack.getTag();
                    if (CompoundNBT1 != null && CompoundNBT1.contains("Items", 9)) {
                        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
                        ItemStackHelper.loadAllItems(CompoundNBT1, nonnulllist);
                        for (ItemStack stack1 : nonnulllist) {
                            if (stack1.getItem() instanceof ItemRatUpgrade) {
                                function.accept((ItemRatUpgrade) stack1.getItem());
                            }
                        }
                    }
                } else if (stack.getItem() instanceof ItemRatUpgradeJuryRigged) {
                    CompoundNBT CompoundNBT1 = stack.getTag();
                    if (CompoundNBT1 != null && CompoundNBT1.contains("Items", 9)) {
                        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(2, ItemStack.EMPTY);
                        ItemStackHelper.loadAllItems(CompoundNBT1, nonnulllist);
                        for (ItemStack stack1 : nonnulllist) {
                            if (stack1.getItem() instanceof ItemRatUpgrade) {
                                function.accept((ItemRatUpgrade) stack1.getItem());
                            }
                        }
                    }
                } else if (stack.getItem() instanceof ItemRatUpgrade) {
                    function.accept((ItemRatUpgrade) stack.getItem());
                }
            }
        }
    }

    public boolean forEachUpgradeBool(Function<ItemRatUpgrade, Boolean> function, boolean _default) {
        for(EquipmentSlotType slot : RatUtils.UPGRADE_SLOTS) {
            ItemStack stack = getItemStackFromSlot(slot);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ItemRatUpgradeCombined) {
                    CompoundNBT CompoundNBT1 = stack.getTag();
                    if (CompoundNBT1 != null && CompoundNBT1.contains("Items", 9)) {
                        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
                        ItemStackHelper.loadAllItems(CompoundNBT1, nonnulllist);
                        for (ItemStack stack1 : nonnulllist) {
                            if (stack1.getItem() instanceof ItemRatUpgrade) {
                                return function.apply((ItemRatUpgrade) stack1.getItem());
                            }
                        }
                    }
                } else if (stack.getItem() instanceof ItemRatUpgradeJuryRigged) {
                    CompoundNBT CompoundNBT1 = stack.getTag();
                    if (CompoundNBT1 != null && CompoundNBT1.contains("Items", 9)) {
                        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(2, ItemStack.EMPTY);
                        ItemStackHelper.loadAllItems(CompoundNBT1, nonnulllist);
                        for (ItemStack stack1 : nonnulllist) {
                            if (stack1.getItem() instanceof ItemRatUpgrade) {
                                return function.apply((ItemRatUpgrade) stack1.getItem());
                            }
                        }
                    }
                } else if (stack.getItem() instanceof ItemRatUpgrade) {
                    return function.apply((ItemRatUpgrade) stack.getItem());
                }
            }
        }
        return _default;
    }

    public ItemStack getUpgrade(Item item) {
        for(EquipmentSlotType slot : RatUtils.UPGRADE_SLOTS) {
            ItemStack stack = getItemStackFromSlot(slot);
            if (!stack.isEmpty()) {
                if (stack.getItem() == item) {
                    return stack;
                }
                if (stack.getItem() instanceof ItemRatUpgradeCombined) {
                    CompoundNBT CompoundNBT1 = stack.getTag();
                    if (CompoundNBT1 != null && CompoundNBT1.contains("Items", 9)) {
                        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
                        ItemStackHelper.loadAllItems(CompoundNBT1, nonnulllist);
                        for (ItemStack stack1 : nonnulllist) {
                            if (stack1.getItem() == item) {
                                return stack1;
                            }
                        }
                    }
                }
                if (stack.getItem() instanceof ItemRatUpgradeJuryRigged) {
                    CompoundNBT CompoundNBT1 = stack.getTag();
                    if (CompoundNBT1 != null && CompoundNBT1.contains("Items", 9)) {
                        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(2, ItemStack.EMPTY);
                        ItemStackHelper.loadAllItems(CompoundNBT1, nonnulllist);
                        for (ItemStack stack1 : nonnulllist) {
                            if (stack1.getItem() == item) {
                                return stack1;
                            }
                        }
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public boolean hasUpgrade(Item item) {
        if(!isTamed() || !hasAnyUpgrades()){
            return false;
        }
        return getUpgrade(item) != ItemStack.EMPTY;
    }

    public boolean holdsItemInHandUpgrade() {
        boolean bool = forEachUpgradeBool((stack) -> stack.shouldHoldItemInHands(this), false);

        return !isInWheel() && (bool || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PLATTER) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER_ORE) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_QUARRY) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FARMER) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FISHERMAN) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_SHEARS) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CHRISTMAS) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_STRIDER_MOUNT)
                || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BOW) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CROSSBOW));
    }

    public boolean shouldNotIdleAnimation() {
        boolean bool = forEachUpgradeBool((stack) -> stack.shouldNotIdleAnimation(this), false);
        return !bool && this.holdInMouth && this.getAnimation() != EntityRat.ANIMATION_EAT && this.cookingProgress <= 0
                && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PLATTER) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK)
                && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER_ORE)
                && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FARMER) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_QUARRY)
                && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FISHERMAN) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_SHEARS)
                && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_STRIDER_MOUNT);
    }

    private boolean hasAnyUpgrades(){
        for(EquipmentSlotType slot : RatUtils.UPGRADE_SLOTS){
            if(!this.getItemStackFromSlot(slot).isEmpty()){
                return true;
            }
        }
        return false;
    }

    private void onUpgradeChanged() {
        setupDynamicAI();
        boolean flagHealth = false;
        boolean flagArmor = false;
        boolean flagAttack = false;
        boolean flagSpeed = false;
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ARISTOCRAT) && this.coinCooldown == 0) {
            this.coinCooldown = this.rand.nextInt(6000) + 6000;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_SPEED)) {
            tryIncreaseStat(Attributes.MOVEMENT_SPEED, 0.5D);
            flagSpeed = true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DEMON)) {
            tryIncreaseStat(Attributes.MAX_HEALTH, 40D);
            tryIncreaseStat(Attributes.ATTACK_DAMAGE, 4D);
            flagHealth = true;
            flagAttack = true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_GOD)) {
            tryIncreaseStat(Attributes.MAX_HEALTH, 500D);
            tryIncreaseStat(Attributes.ATTACK_DAMAGE, 50D);
            tryIncreaseStat(Attributes.ARMOR, 50D);
            flagHealth = true;
            flagArmor = true;
            flagAttack = true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_WARRIOR)) {
            tryIncreaseStat(Attributes.ATTACK_DAMAGE, 40D);
            tryIncreaseStat(Attributes.ATTACK_DAMAGE, 12D);
            tryIncreaseStat(Attributes.ARMOR, 10D);
            flagHealth = true;
            flagArmor = true;
            flagAttack = true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_HEALTH)) {
            tryIncreaseStat(Attributes.MAX_HEALTH, 25D);
            flagHealth = true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ARMOR)) {
            tryIncreaseStat(Attributes.ARMOR, 10D);
            flagArmor = true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_STRENGTH)) {
            tryIncreaseStat(Attributes.ATTACK_DAMAGE, 5D);
            flagAttack = true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_VOODOO)) {
            tryIncreaseStat(Attributes.MAX_HEALTH, 100D);
            flagHealth = true;
        }
        if (this.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_RATINATOR)) {
            tryIncreaseStat(Attributes.ARMOR, 30D);
            tryIncreaseStat(Attributes.MAX_HEALTH, 80D);
            flagHealth = true;
            flagArmor = true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DRAGON)) {
            tryIncreaseStat(Attributes.MAX_HEALTH, 50D);
            tryIncreaseStat(Attributes.ARMOR, 15D);
            tryIncreaseStat(Attributes.ATTACK_DAMAGE, 8D);
            flagHealth = true;
            flagArmor = true;
            flagAttack = true;
        }
        if (this.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_NONBELIEVER)) {
            tryIncreaseStat(Attributes.MAX_HEALTH, 1000D);
            tryIncreaseStat(Attributes.ATTACK_DAMAGE, 100D);
            tryIncreaseStat(Attributes.ARMOR, 100D);
            flagHealth = true;
            flagArmor = true;
            flagAttack = true;
        }
        if (!flagHealth) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0D);
        }
        if (!flagArmor) {
            this.getAttribute(Attributes.ARMOR).setBaseValue(0.0D);
        }
        if (!flagAttack) {
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(1.0D);
        }
        if (!flagSpeed) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        }
        forEachUpgrade((stack) -> {
            stack.onUpgradeChanged(this);
        });
        if (this.getHeldRF() > this.getRFTransferRate()) {
            this.setHeldRF(0);
        }
        this.heal(this.getMaxHealth());
    }

    public void tryIncreaseStat(Attribute stat, double value) {
        double prev = this.getAttribute(stat).getValue();
        if (prev < value) {
            this.getAttribute(stat).setBaseValue(value);
        }
    }

    public boolean isPotionApplicable(EffectInstance potioneffectIn) {
        if (potioneffectIn.getPotion() == Effects.POISON && (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_POISON) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION))) {
            return false;
        }
        if (potioneffectIn.getPotion() == RatsMod.PLAGUE_POTION) {
            return false;
        }
        return super.isPotionApplicable(potioneffectIn);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (this.getRespawnCountdown() > 0) {
            return true;
        }
        if (source.isFireDamage() && (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ASBESTOS) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DRAGON) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DEMON))) {
            return true;
        }
        if ((source.isMagicDamage() || source == DamageSource.WITHER) && (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_POISON) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION))) {
            return true;
        }
        if ((source == DamageSource.IN_WALL || source == DamageSource.DROWN) && (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_QUARRY) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_POISON) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_UNDERWATER))) {
            return true;
        }
        if(source == DamageSource.IN_WALL && (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK))){
            return true;
        }
        if (source == DamageSource.FALL && (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_TNT_SURVIVOR) || this.hasFlightUpgrade())) {
            return true;
        }
        if (source.isExplosion() && this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_TNT_SURVIVOR)) {
            return true;
        }
        if(forEachUpgradeBool((stack) -> stack.isRatInvulnerableTo(this, source), false)){
            return true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CREATIVE)) {
            return source.getTrueSource() == null || source.getTrueSource() instanceof LivingEntity && !isOwner((LivingEntity) source.getTrueSource());
        }
        return super.isInvulnerableTo(source);
    }

    public boolean isInvulnerable() {
        return super.isInvulnerable() || this.getRespawnCountdown() > 0;
    }

    @OnlyIn(Dist.CLIENT)
    public void setPartying(BlockPos pos, boolean isPartying) {
        int moves = this.rand.nextInt(4);
        if (!this.isDancing() && isPartying) {
            this.setDanceMoves(moves);
        }
        this.setDancing(isPartying);
        this.jukeboxPos = pos;
        if (world.isRemote) {
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageDancingRat(this.getEntityId(), isPartying, pos.toLong(), moves));
        }
    }

    public boolean shouldDepositItem(ItemStack item) {
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CHEF) && !this.getCookingResultFor(item).isEmpty()) {
            return false;
        }
        if (this.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_ARCHEOLOGIST) && !this.getArcheologyResultFor(item).isEmpty()) {
            return false;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_GEMCUTTER) && !this.getGemcutterResultFor(item).isEmpty()) {
            return false;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FARMER) && (item.getItem() == Items.BONE_MEAL || item.getItem() instanceof BlockNamedItem)) {
            return false;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BREEDER) && this.getCommand() == RatCommand.HARVEST) {
            return false;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PLACER) && this.getCommand() == RatCommand.HARVEST) {
            return false;
        }
        boolean bool = forEachUpgradeBool((stack) -> stack.shouldDepositItem(this, item), true);

        return (!this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ORE_DOUBLING) || !ItemRatUpgradeOreDoubling.isProcessable(item)) && bool;
    }

    public int getRFTransferRate() {
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_EXTREME_ENERGY)) {
            return ItemRatUpgradeEnergy.getRFTransferRate(RatsItemRegistry.RAT_UPGRADE_EXTREME_ENERGY) * 1000;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ELITE_ENERGY)) {
            return ItemRatUpgradeEnergy.getRFTransferRate(RatsItemRegistry.RAT_UPGRADE_ELITE_ENERGY) * 1000;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ADVANCED_ENERGY)) {
            return ItemRatUpgradeEnergy.getRFTransferRate(RatsItemRegistry.RAT_UPGRADE_ADVANCED_ENERGY) * 1000;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BASIC_ENERGY)) {
            return ItemRatUpgradeEnergy.getRFTransferRate(RatsItemRegistry.RAT_UPGRADE_BASIC_ENERGY) * 1000;
        }
        return 0;
    }

    public int getMBTransferRate() {
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BUCKET)) {
            return ItemRatUpgradeBucket.getMbTransferRate(RatsItemRegistry.RAT_UPGRADE_BUCKET);
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MILKER)) {
            return ItemRatUpgradeBucket.getMbTransferRate(RatsItemRegistry.RAT_UPGRADE_MILKER);
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BIG_BUCKET)) {
            return ItemRatUpgradeBucket.getMbTransferRate(RatsItemRegistry.RAT_UPGRADE_BIG_BUCKET);
        }
        return 0;
    }

    public boolean isOnSameTeam(Entity entityIn) {
        if (entityIn != null) {
            LivingEntity livingentity = this.getOwner();
            if (entityIn == livingentity) {
                return true;
            }
            if (entityIn instanceof EntityRatMountBase && ((EntityRatMountBase) entityIn).getRat() != null) {
                return this.isOnSameTeam(((EntityRatMountBase) entityIn).getRat());
            }
            if (entityIn instanceof TameableEntity) {
                return ((TameableEntity) entityIn).isOwner(livingentity);
            }
            if (livingentity != null) {
                return livingentity.isOnSameTeam(entityIn);
            }
            return super.isOnSameTeam(entityIn);
        }
        return false;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean hasNoGravity() {
        return hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_ETHEREAL) || super.hasNoGravity();
    }

    public float getBrightness() {
        if (this.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_ETHEREAL)) {
            return 240;
        } else {
            return super.getBrightness();
        }
    }

    public boolean isAttackCommand() {
        return getCommandInteger() == 0 || getCommandInteger() == 2 || getCommandInteger() == 3 || getCommandInteger() == 7;
    }

    public boolean hasCustomSearchZone() {
        return this.dataManager.get(HAS_CUSTOM_RADIUS).booleanValue();
    }

    public void setCustomSearchZone(boolean customSearchZone) {
        this.dataManager.set(HAS_CUSTOM_RADIUS, Boolean.valueOf(customSearchZone));
    }

    public int getSearchRadius() {
        return Integer.valueOf(this.dataManager.get(SEARCH_RADIUS).intValue());
    }

    public void setSearchRadius(int radius) {
        this.dataManager.set(SEARCH_RADIUS, Integer.valueOf(radius));
    }

    public BlockPos getSearchRadiusCenter() {
        return this.dataManager.get(RADIUS_CENTER);
    }

    public void setSearchRadiusCenter(BlockPos radius) {
        if (radius == null) {
            this.setCustomSearchZone(false);
        } else {
            this.setCustomSearchZone(true);
            this.dataManager.set(RADIUS_CENTER, radius);
        }
    }

    public BlockPos getSearchCenter() {
        if (getSearchRadiusCenter() == null || !this.hasCustomSearchZone()) {
            return this.getPosition();
        } else {
            return getSearchRadiusCenter();
        }
    }

    @Nullable
    private EntityType getMountEntityType() {
        EntityType type = null;
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_STRIDER_MOUNT)) {
            type = RatsEntityRegistry.RAT_STRIDER_MOUNT;
        }
        if (this.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_BIPLANE_MOUNT)) {
            type = RatlantisEntityRegistry.RAT_MOUNT_BIPLANE;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CHICKEN_MOUNT)) {
            type = RatsEntityRegistry.RAT_MOUNT_CHICKEN;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_GOLEM_MOUNT)) {
            type = RatsEntityRegistry.RAT_MOUNT_GOLEM;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BEAST_MOUNT)) {
            type = RatsEntityRegistry.RAT_MOUNT_BEAST;
        }
        if (this.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_AUTOMATON_MOUNT)) {
            type = RatlantisEntityRegistry.RAT_MOUNT_AUTOMATON;
        }
        RatServerEvent.GetMountEntityType event = new RatServerEvent.GetMountEntityType(this, type);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getResult() == Event.Result.ALLOW ? event.getMountType() : type;
    }

    public double getRatDistanceCenterSq(double x, double y, double z) {
        double d0 = this.getPosX() - x - 0.5D;
        double d1 = this.getPosY() - y - 0.5D;
        double d2 = this.getPosZ() - z - 0.5D;
        if (this.getRidingEntity() != null && getMountEntityType() != null && this.getRidingEntity().getType() == getMountEntityType()) {
            d0 = this.getRidingEntity().getPosX() - x - 0.5D;
            d1 = this.getRidingEntity().getPosY() - y - 0.5D;
            d2 = this.getRidingEntity().getPosZ() - z - 0.5D;
        }
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public double getRatDistanceSq(double x, double y, double z) {
        double d0 = this.getPosX() - x;
        double d1 = this.getPosY() - y;
        double d2 = this.getPosZ() - z;
        if (this.getRidingEntity() != null && getMountEntityType() != null && this.getRidingEntity().getType() == getMountEntityType()) {
            d0 = this.getRidingEntity().getPosX() - x;
            d1 = this.getRidingEntity().getPosY() - y;
            d2 = this.getRidingEntity().getPosZ() - z;
        }
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    @Override
    public double getDistanceSq(Vector3d p_195048_1_) {
        double d0 = this.getPosX() - p_195048_1_.x;
        double d1 = this.getPosY() - p_195048_1_.y;
        double d2 = this.getPosZ() - p_195048_1_.z;
        if (this.getRidingEntity() != null && getMountEntityType() != null && this.getRidingEntity().getType() == getMountEntityType()) {
            d0 = this.getRidingEntity().getPosX() - p_195048_1_.x;
            d1 = this.getRidingEntity().getPosY() - p_195048_1_.y;
            d2 = this.getRidingEntity().getPosZ() - p_195048_1_.z;
        }
        return d0 * d0 + d1 * d1 + d2 * d2;
    }


    public boolean isRidingSpecialMount() {
        boolean ret = false;
        if (this.getRidingEntity() != null && getMountEntityType() != null) {
            ret = this.getRidingEntity().getType().equals(getMountEntityType());
        }
        RatServerEvent.IsRidingSpecialMount event = new RatServerEvent.IsRidingSpecialMount(this, ret);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getResult() == Event.Result.ALLOW ? event.isRidingSpecialMount() : ret;
    }

    public int getTailBehaviorForMount() {
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_GOLEM_MOUNT) && isRidingSpecialMount()) {
            return 1;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CHICKEN_MOUNT) && isRidingSpecialMount()) {
            return 2;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BEAST_MOUNT) && isRidingSpecialMount()) {
            return 3;
        }
        if (this.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_AUTOMATON_MOUNT) && isRidingSpecialMount()) {
            return 4;
        }
        if (this.isPassenger() && this.getRidingEntity() instanceof EntityRattlingGun) {
            return 5;
        }
        if (this.isPassenger() && this.getRidingEntity() instanceof EntityRatBaronPlane || this.getRidingEntity() instanceof EntityRatBiplaneMount) {
            return 3;
        }
        if (this.getRidingEntity() instanceof StriderEntity) {
            return 2;
        }
        RatServerEvent.GetRatTailBehavior tailEvent = new RatServerEvent.GetRatTailBehavior(this, 1);
        MinecraftForge.EVENT_BUS.post(tailEvent);
        return tailEvent.getResult() == Event.Result.ALLOW ? tailEvent.getRatTailBehavior() : 0;//normal (down + riding)
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return !isRidingSpecialMount() || !RatUtils.isRidingOrBeingRiddenBy(this, target);
    }

    public double getRatDistanceModifier() {
        if (this.isRidingSpecialMount()) {
            Entity entity = this.getRidingEntity();
            if (entity != null) {
                if (entity instanceof EntityRatBiplaneMount) {
                    return 3.95D;
                }
                return 1.5D;
            }

        }
        RatServerEvent.GetRatReachDistance reachDistance = new RatServerEvent.GetRatReachDistance(this, 1);
        MinecraftForge.EVENT_BUS.post(reachDistance);
        return reachDistance.getResult() == Event.Result.ALLOW ? reachDistance.getReachDistance() : 1D;
    }

    public PathNavigator getNavigator() {
        if (this.ratInventory != null && this.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_BIPLANE_MOUNT) && isRidingSpecialMount()) {
            return this.navigator;
        } else {
            return super.getNavigator();
        }
    }

    @Override
    protected ResourceLocation getLootTable() {
        return this.hasPlague() ? PLAGUE_RAT_LOOT_TABLE : this.getType().getLootTable();
    }

    public boolean isNotColliding(IWorldReader worldIn) {
        return super.isNotColliding(worldIn);
    }

    public boolean hasFlightUpgrade() {
        boolean bool = forEachUpgradeBool((stack) -> stack.canFly(this), false);
        return bool || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FLIGHT) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DRAGON) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BEE) || this.hasUpgrade(RatlantisItemRegistry.RAT_UPGRADE_BIPLANE_MOUNT) && this.isRidingSpecialMount();
    }

    @Override
    public int maxSearchNodes() {
        return 50;
    }

    @Override
    public boolean isBlockPassable(BlockState state, BlockPos pos, BlockPos entityPos) {
        return false;
    }

    @Override
    public boolean isSmallerThanBlock() {
        return !this.isRidingSpecialMount();
    }

    @Override
    public float getXZNavSize() {
        return 1;
    }

    @Override
    public int getYNavSize() {
        return 1;
    }

}
