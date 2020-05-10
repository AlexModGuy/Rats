package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.BlockRatCage;
import com.github.alexthe666.rats.server.blocks.BlockRatHole;
import com.github.alexthe666.rats.server.blocks.BlockRatTube;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.ai.*;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCraftingTable;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatHole;
import com.github.alexthe666.rats.server.items.*;
import com.github.alexthe666.rats.server.message.MessageDancingRat;
import com.github.alexthe666.rats.server.message.MessageSyncThrownBlock;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import com.github.alexthe666.rats.server.recipes.RatsRecipeRegistry;
import com.github.alexthe666.rats.server.recipes.SharedRecipe;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.*;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.*;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.*;

public class EntityRat extends EntityTameable implements IAnimatedEntity {

    public static final Animation ANIMATION_EAT = Animation.create(10);
    public static final Animation ANIMATION_IDLE_SCRATCH = Animation.create(25);
    public static final Animation ANIMATION_IDLE_SNIFF = Animation.create(20);
    public static final Animation ANIMATION_DANCE_0 = Animation.create(35);
    public static final Animation ANIMATION_DANCE_1 = Animation.create(30);
    public static final ResourceLocation LOOT = LootTableList.register(new ResourceLocation("rats", "rat"));
    public static final ResourceLocation CHRISTMAS_LOOT = LootTableList.register(new ResourceLocation("rats", "christmas_rat_gifts"));
    protected static final DataParameter<Optional<UUID>> MONSTER_OWNER_UNIQUE_ID = EntityDataManager.createKey(EntityRat.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<Boolean> IS_MALE = EntityDataManager.createKey(EntityRat.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> TOGA = EntityDataManager.createKey(EntityRat.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PLAGUE = EntityDataManager.createKey(EntityRat.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> VISUAL_FLAG = EntityDataManager.createKey(EntityRat.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> COMMAND = EntityDataManager.createKey(EntityRat.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> COLOR_VARIANT = EntityDataManager.createKey(EntityRat.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> DANCING = EntityDataManager.createKey(EntityRat.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> OWNER_NOT_PLAYER = EntityDataManager.createKey(EntityRat.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> DANCE_MOVES = EntityDataManager.createKey(EntityRat.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> HELD_RF = EntityDataManager.createKey(EntityRat.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> HAS_CUSTOM_RADIUS = EntityDataManager.createKey(EntityRat.class, DataSerializers.BOOLEAN);
    private static final DataParameter<BlockPos> RADIUS_CENTER = EntityDataManager.createKey(EntityRat.class, DataSerializers.BLOCK_POS);
    private static final DataParameter<Integer> SEARCH_RADIUS = EntityDataManager.createKey(EntityRat.class, DataSerializers.VARINT);
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
    private static final int CHRISTMAS_PRESENT_TIME = 24000;
    private static final SoundEvent[] CRAFTING_SOUNDS = new SoundEvent[]{SoundEvents.BLOCK_ANVIL_USE, SoundEvents.BLOCK_WOOD_BREAK, SoundEvents.ENTITY_LLAMA_EAT, SoundEvents.BLOCK_LADDER_HIT, SoundEvents.ENTITY_HORSE_SADDLE,
            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD};
    public float sitProgress;
    public float holdProgress;
    public float deadInTrapProgress;
    public float sleepProgress;
    public boolean isDeadInTrap;
    public BlockPos fleePos;
    public boolean holdInMouth = true;
    public int wildTrust = 0;
    public ContainerHorseChest ratInventory;
    public BlockPos depositPos;
    public EnumFacing depositFacing = EnumFacing.UP;
    public BlockPos pickupPos;
    public BlockPos tubeTarget = null;
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
    public FluidStack transportingFluid = null;
    /*
       0 = tamed navigator
       1 = wild navigator
       2 = flight navigator
       3 = tube navigator
       4 = aquatic navigator
     */
    protected int navigatorType;
    private boolean inTube;
    private boolean inCage;
    private int animationTick;
    private Animation currentAnimation;
    private RatStatus status = RatStatus.IDLE;
    private BlockPos finalDigPathPoint = null;
    private BlockPos diggingPos = null;
    private int breakingTime;
    private int previousBreakProgress = -1;
    private int digCooldown = 0;
    private int eatingTicks = 0;
    private ItemStack prevUpgrade = ItemStack.EMPTY;
    private int eatenItems = 0;
    private EntityAIBase aiHarvest;
    private EntityAIBase aiPickup;
    private EntityAIBase aiDeposit;
    private int rangedAttackCooldownCannon = 0;
    private int rangedAttackCooldownLaser = 0;
    private int rangedAttackCooldownPsychic = 0;
    private int rangedAttackCooldownDragon = 0;
    private int visualCooldown = 0;
    private int poopCooldown = 0;

    public EntityRat(World worldIn) {
        super(worldIn);
        this.setPathPriority(PathNodeType.RAIL, 1000F);
        switchNavigator(1);
        this.setSize(0.49F, 0.49F);
        initInventory();
        setupDynamicAI();
    }

    public static BlockPos getPositionRelativetoGround(EntityRat rat, World world, double x, double z, Random rng) {
        if (rat.hasHome()) {
            x = rat.getHomePosition().getX() + rng.nextInt((int) rat.getMaximumHomeDistance()) - rat.getMaximumHomeDistance() / 2;
            z = rat.getHomePosition().getZ() + rng.nextInt((int) rat.getMaximumHomeDistance()) - rat.getMaximumHomeDistance() / 2;
        }
        BlockPos pos = new BlockPos(x, rat.posY, z);
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
        BlockPos topY = new BlockPos(x, rat.posY, z);
        BlockPos bottomY = new BlockPos(x, rat.posY, z);
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
        return rat.getPosition();
    }

    protected void initEntityAI() {
        aiHarvest = new RatAIHarvestCrops(this);
        aiPickup = new RatAIPickupFromInventory(this);
        aiDeposit = new RatAIDepositInInventory(this);
        this.tasks.addTask(0, new RatAIAttackMelee(this, 1.45D, true));
        this.tasks.addTask(1, new RatAISwimming(this));
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.45D, false));
        this.tasks.addTask(2, new RatAIFleeMobs(this, new Predicate<Entity>() {
            public boolean apply(@Nullable Entity entity) {
                return entity.isEntityAlive() && (entity instanceof EntityPlayer && ((EntityPlayer) entity).getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() != RatsItemRegistry.PIPER_HAT) || entity instanceof EntityOcelot;
            }
        }, 10.0F, 0.8D, 1.33D));
        this.tasks.addTask(3, new RatAIFollowOwner(this, 1.33D, 3.0F, 1.0F));
        this.tasks.addTask(5, new RatAIFleeSun(this, 1.66D));
        this.tasks.addTask(5, this.aiSit = new RatAISit(this));
        this.tasks.addTask(6, new RatAIWander(this, 1.0D));
        this.tasks.addTask(6, new RatAIWanderFlight(this));
        this.tasks.addTask(6, new RatAIWanderAquatic(this));
        this.tasks.addTask(7, new RatAIRaidChests(this));
        this.tasks.addTask(7, new RatAIRaidCrops(this));
        this.tasks.addTask(7, new RatAIEnterTrap(this));
        this.tasks.addTask(7, new RatAIFleePosition(this));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityLivingBase.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(0, new RatAIHuntAnimals(this, new Predicate<EntityLivingBase>() {
            public boolean apply(@Nullable EntityLivingBase entity) {
                if (EntityRat.this.hasPlague()) {
                    return entity instanceof EntityPlayer && !entity.isOnSameTeam(EntityRat.this) && entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() != RatsItemRegistry.BLACK_DEATH_MASK && entity.world.getDifficulty() != EnumDifficulty.PEACEFUL;
                } else {
                    if (entity instanceof EntityTameable && ((EntityTameable) entity).isTamed()) {
                        return false;
                    }
                    if (EntityRat.this.shouldHuntMonsters()) {
                        return entity instanceof IMob;
                    } else if (EntityRat.this.shouldHuntAnimals()) {
                        return entity != null && !(entity instanceof EntityRat) && !(entity instanceof IMob) && !entity.isOnSameTeam(EntityRat.this) && (!(entity instanceof EntityPlayer) || EntityRat.this.hasPlague()) && !entity.isChild();
                    }
                    return false;
                }
            }
        }));
        this.targetTasks.addTask(1, new RatAITargetItems(this, false));
        this.targetTasks.addTask(2, new RatAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(3, new RatAIOwnerHurtTarget(this));
        this.targetTasks.addTask(4, new RatAIHurtByTarget(this, false));
    }

    protected void setupDynamicAI() {
        this.tasks.removeTask(this.aiHarvest);
        this.tasks.removeTask(this.aiDeposit);
        this.tasks.removeTask(this.aiPickup);
        aiHarvest = new RatAIHarvestCrops(this);
        aiDeposit = new RatAIDepositInInventory(this);
        aiPickup = new RatAIPickupFromInventory(this);
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK) && !(aiHarvest instanceof RatAIHarvestTrees)) {
            aiHarvest = new RatAIHarvestTrees(this);
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER) && !(aiHarvest instanceof RatAIHarvestMine)) {
            aiHarvest = new RatAIHarvestMine(this);
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FARMER) && !(aiHarvest instanceof RatAIHarvestFarmer)) {
            aiHarvest = new RatAIHarvestFarmer(this);
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FISHERMAN) && !(aiHarvest instanceof RatAIHarvestFisherman)) {
            aiHarvest = new RatAIHarvestFisherman(this);
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MILKER) && !(aiHarvest instanceof RatAIHarvestMilk)) {
            aiHarvest = new RatAIHarvestMilk(this);
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_SHEARS) && !(aiHarvest instanceof RatAIHarvestShears)) {
            aiHarvest = new RatAIHarvestShears(this);
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PLACER) && !(aiHarvest instanceof RatAIHarvestPlacer)) {
            aiHarvest = new RatAIHarvestPlacer(this);
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BREEDER) && !(aiHarvest instanceof RatAIHarvestBreeder)) {
            aiHarvest = new RatAIHarvestBreeder(this);
        }
        if (this.getMBTransferRate() > 0) {
            aiDeposit = new RatAIPickupFluid(this);
            aiPickup = new RatAIDepositFluid(this);
        } else if (this.getRFTransferRate() > 0) {
            aiDeposit = new RatAIPickupEnergy(this);
            aiPickup = new RatAIDepositEnergy(this);
        }
        this.tasks.addTask(3, this.aiHarvest);
        this.tasks.addTask(4, this.aiDeposit);
        this.tasks.addTask(4, this.aiPickup);
    }

    @Nullable
    public EntityLivingBase getOwner() {
        try {
            UUID uuid = this.getOwnerId();
            EntityLivingBase player = this.world.getPlayerEntityByUUID(uuid);
            if (player != null) {
                return player;
            }
        } catch (Exception var2) {
            return null;
        }
        return null;
    }

    @Nullable
    public EntityLivingBase getOwnerMonster() {
        try {
            UUID uuid = this.getMonsterOwnerId();
            if (!world.isRemote) {
                Entity entity = world.getMinecraftServer().getWorld(this.dimension).getEntityFromUuid(uuid);
                if (entity instanceof EntityLivingBase) {
                    return (EntityLivingBase) entity;
                }
            }
        } catch (IllegalArgumentException var2) {
            return null;
        }
        return null;
    }

    protected boolean canDespawn() {
        if (RatsMod.CONFIG_OPTIONS.ratsDespawn) {
            return (!this.isTamed() || this.isOwnerMonster()) && !this.isChild();
        } else {
            return false;
        }
    }

    protected void despawnEntity() {
        net.minecraftforge.fml.common.eventhandler.Event.Result result = null;
        if ((this.idleTime & 0x1F) == 0x1F && (result = net.minecraftforge.event.ForgeEventFactory.canEntityDespawn(this)) != net.minecraftforge.fml.common.eventhandler.Event.Result.DEFAULT) {
            if (result == net.minecraftforge.fml.common.eventhandler.Event.Result.DENY) {
                this.idleTime = 0;
            } else {
                this.setDead();
            }
        } else {
            Entity entity = this.world.getClosestPlayerToEntity(this, -1.0D);
            if (entity != null) {
                double d0 = entity.posX - this.posX;
                double d1 = entity.posY - this.posY;
                double d2 = entity.posZ - this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (this.canDespawn() && d3 > (RatsMod.CONFIG_OPTIONS.ratDespawnFarDistance * RatsMod.CONFIG_OPTIONS.ratDespawnFarDistance)) {
                    this.setDead();
                }
                double closeDist = RatsMod.CONFIG_OPTIONS.ratDespawnCloseDistance * RatsMod.CONFIG_OPTIONS.ratDespawnCloseDistance;
                if (this.idleTime > 300 && this.rand.nextInt(RatsMod.CONFIG_OPTIONS.ratDespawnRandomChance) == 0 && d3 > closeDist && this.canDespawn()) {
                    this.setDead();
                } else if (d3 < closeDist) {
                    this.idleTime = 0;
                }
            }
        }
    }

    public boolean getCanSpawnHere() {
        int spawnRoll = RatsMod.CONFIG_OPTIONS.ratSpawnDecrease;
        if (RatUtils.canSpawnInDimension(world)) {
            if (RatsMod.CONFIG_OPTIONS.ratsSpawnLikeMonsters) {
                if (world.getDifficulty() == EnumDifficulty.PEACEFUL) {
                    spawnRoll *= 2;
                }
                if (spawnRoll == 0 || rand.nextInt(spawnRoll) == 0) {
                    BlockPos pos = new BlockPos(this);
                    IBlockState iblockstate = this.world.getBlockState((pos).down());
                    return this.isValidLightLevel() && iblockstate.canEntitySpawn(this);
                }
            } else {
                spawnRoll = MathHelper.floor(spawnRoll * 0.25F);
                return (spawnRoll == 0 || rand.nextInt(spawnRoll) == 0) && getCanSpawnHereAnimal();
            }
        }
        return false;
    }

    public boolean getCanSpawnHereAnimal() {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.getEntityBoundingBox().minY);
        int k = MathHelper.floor(this.posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        return this.world.getLight(blockpos) > 8 && super.getCanSpawnHere();
    }

    @Override
    public boolean canBreatheUnderwater() {
        return this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_AQUATIC) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_UNDERWATER);
    }

    protected boolean isValidLightLevel() {
        BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
        if (this.world.getLightFor(EnumSkyBlock.SKY, blockpos) > this.rand.nextInt(32)) {
            return false;
        } else {
            int i = this.world.getLightFromNeighbors(blockpos);

            if (this.world.isThundering()) {
                int j = this.world.getSkylightSubtracted();
                this.world.setSkylightSubtracted(10);
                i = this.world.getLightFromNeighbors(blockpos);
                this.world.setSkylightSubtracted(j);
            }
            return i <= this.rand.nextInt(8);
        }
    }

    public boolean isOnLadder() {
        if (this.inTube()) {
            return climbingTube;
        }
        return super.isOnLadder();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(IS_MALE, Boolean.valueOf(false));
        this.dataManager.register(TOGA, Boolean.valueOf(false));
        this.dataManager.register(PLAGUE, Boolean.valueOf(false));
        this.dataManager.register(VISUAL_FLAG, Boolean.valueOf(false));
        this.dataManager.register(COMMAND, Integer.valueOf(0));
        this.dataManager.register(COLOR_VARIANT, Integer.valueOf(0));
        this.dataManager.register(DANCING, Boolean.valueOf(false));
        this.dataManager.register(DANCE_MOVES, Integer.valueOf(0));
        this.dataManager.register(HELD_RF, Integer.valueOf(0));
        this.dataManager.register(OWNER_NOT_PLAYER, Boolean.valueOf(false));
        this.dataManager.register(RADIUS_CENTER, this.getPosition());
        this.dataManager.register(HAS_CUSTOM_RADIUS, Boolean.valueOf(false));
        this.dataManager.register(SEARCH_RADIUS, RatsMod.CONFIG_OPTIONS.defaultRatRadius);
        this.dataManager.register(MONSTER_OWNER_UNIQUE_ID, Optional.absent());

    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
    }

    protected void switchNavigator(int type) {
        if (type == 1) {//cage or wild
            this.moveHelper = new EntityMoveHelper(this);
            this.navigator = new RatPathPathNavigateGround(this, world);
            this.navigatorType = 1;
        } else if (type == 0) {//tamed
            this.moveHelper = new EntityMoveHelper(this);
            this.navigator = new RatPathPathNavigateGround(this, world);
            this.navigatorType = 0;
        } else if (type == 2) {//flying
            this.moveHelper = new RatFlyingMoveHelper(this);
            this.navigator = new FlyingRatPathNavigate(this, world);
            this.navigatorType = 2;
        } else if (type == 3) {//tube
            this.moveHelper = new RatTubeMoveHelper(this);
            RatTubePathNavigate newNav = new RatTubePathNavigate(this, world);
            if (this.navigator.getPath() != null && this.navigator.getPath().getFinalPathPoint() != null) {
                PathPoint point = this.navigator.getPath().getFinalPathPoint();
                newNav.tryMoveToXYZ(point.x, point.y, point.z, 1.0F);
            }
            this.navigator = newNav;
            this.navigatorType = 3;
        } else if (type == 4) {//aquatic
            this.moveHelper = new RatAquaticMoveHelper(this);
            this.navigator = new AquaticRatPathNavigate(this, world);
            this.navigatorType = 4;
        }
    }

    protected PathNavigate createNavigator(World worldIn) {
        if (isTamed() && !this.isInCage()) {
            return super.createNavigator(worldIn);
        } else {
            return new RatPathNavigate(this, worldIn);
        }
    }

    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setFloat("HomeDistance", getMaximumHomeDistance());
        if (getHomePosition() != null) {
            compound.setInteger("HomePosX", getHomePosition().getX());
            compound.setInteger("HomePosY", getHomePosition().getY());
            compound.setInteger("HomePosZ", getHomePosition().getZ());
        }
        if (getSearchRadiusCenter() != null) {
            compound.setInteger("RadiusPosX", getSearchRadiusCenter().getX());
            compound.setInteger("RadiusPosY", getSearchRadiusCenter().getY());
            compound.setInteger("RadiusPosZ", getSearchRadiusCenter().getZ());
        }
        compound.setBoolean("CustomSearchZone", hasCustomSearchZone());
        compound.setInteger("SearchRadius", getSearchRadius());
        compound.setInteger("CookingProgress", cookingProgress);
        compound.setInteger("DigCooldown", digCooldown);
        compound.setInteger("BreedCooldown", breedCooldown);
        compound.setInteger("CoinCooldown", coinCooldown);
        compound.setInteger("CheeseFeedings", cheeseFeedings);
        compound.setInteger("TransportingRF", this.getHeldRF());
        compound.setInteger("Command", this.getCommandInteger());
        compound.setInteger("ColorVariant", this.getColorVariant());
        compound.setBoolean("Plague", this.hasPlague());
        compound.setBoolean("VisualFlag", this.getVisualFlag());
        compound.setBoolean("Dancing", this.isDancing());
        compound.setBoolean("Toga", this.hasToga());
        compound.setBoolean("OwnerMonster", this.isOwnerMonster());
        compound.setBoolean("IsMale", this.isMale());
        compound.setInteger("WildTrust", wildTrust);
        if (ratInventory != null) {
            NBTTagList nbttaglist = new NBTTagList();
            for (int i = 0; i < ratInventory.getSizeInventory(); ++i) {
                ItemStack itemstack = ratInventory.getStackInSlot(i);
                if (!itemstack.isEmpty()) {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.setByte("Slot", (byte) i);
                    itemstack.writeToNBT(nbttagcompound);
                    nbttaglist.appendTag(nbttagcompound);
                }
            }
            compound.setTag("Items", nbttaglist);
        }
        compound.setInteger("EatenItems", eatenItems);
        if (pickupPos != null) {
            compound.setInteger("PickupPosX", pickupPos.getX());
            compound.setInteger("PickupPosY", pickupPos.getY());
            compound.setInteger("PickupPosZ", pickupPos.getZ());
        }
        if (depositPos != null) {
            compound.setInteger("DepositPosX", depositPos.getX());
            compound.setInteger("DepositPosY", depositPos.getY());
            compound.setInteger("DepositPosZ", depositPos.getZ());
            compound.setInteger("DepositFacing", depositFacing.ordinal());
        }
        if (transportingFluid != null) {
            NBTTagCompound fluidTag = new NBTTagCompound();
            transportingFluid.writeToNBT(fluidTag);
            compound.setTag("TransportingFluid", fluidTag);
        }
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.getCustomNameTag());
        }
        if (this.getMonsterOwnerId() == null) {
            compound.setString("MonsterOwnerUUID", "");
        } else {
            compound.setString("MonsterOwnerUUID", this.getMonsterOwnerId().toString());
        }
    }

    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        /*
        compound.setFloat("HomeDistance", getMaximumHomeDistance());
        if(getHomePosition() != null){
            compound.setInteger("HomePosX", getHomePosition().getX());
            compound.setInteger("HomePosY", getHomePosition().getY());
            compound.setInteger("HomePosZ", getHomePosition().getZ());
        }
         */

        if (compound.hasKey("HomePosX") && compound.hasKey("HomePosY") && compound.hasKey("HomePosZ")) {
            setHomePosAndDistance(new BlockPos(compound.getInteger("HomePosX"), compound.getInteger("HomePosY"), compound.getInteger("HomePosZ")), compound.getInteger("HomeDistance"));
        }
        if (compound.hasKey("RadiusPosX") && compound.hasKey("RadiusPosY") && compound.hasKey("RadiusPosZ")) {
            setSearchRadiusCenter(new BlockPos(compound.getInteger("RadiusPosX"), compound.getInteger("RadiusPosY"), compound.getInteger("RadiusPosZ")));
        }
        this.setCustomSearchZone(compound.getBoolean("CustomSearchZone"));
        this.setSearchRadius(compound.getInteger("SearchRadius"));
        cookingProgress = compound.getInteger("CookingProgress");
        digCooldown = compound.getInteger("DigCooldown");
        breedCooldown = compound.getInteger("BreedCooldown");
        coinCooldown = compound.getInteger("CoinCooldown");
        wildTrust = compound.getInteger("WildTrust");
        eatenItems = compound.getInteger("EatenItems");
        cheeseFeedings = compound.getInteger("CheeseFeedings");
        this.setHeldRF(compound.getInteger("TransportingRF"));
        this.setCommandInteger(compound.getInteger("Command"));
        this.setPlague(compound.getBoolean("Plague"));
        this.setDancing(compound.getBoolean("Dancing"));
        this.setVisualFlag(compound.getBoolean("VisualFlag"));
        this.setToga(compound.getBoolean("Toga"));
        this.setMale(compound.getBoolean("IsMale"));
        this.setOwnerMonster(compound.getBoolean("OwnerMonster"));
        this.setColorVariant(compound.getInteger("ColorVariant"));
        if (ratInventory != null) {
            NBTTagList nbttaglist = compound.getTagList("Items", 10);
            this.initInventory();
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                int j = nbttagcompound.getByte("Slot") & 255;
                if (j <= 4) {
                    ratInventory.setInventorySlotContents(j, new ItemStack(nbttagcompound));
                }
            }
        } else {
            NBTTagList nbttaglist = compound.getTagList("Items", 10);
            this.initInventory();
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                int j = nbttagcompound.getByte("Slot") & 255;
                ratInventory.setInventorySlotContents(j, new ItemStack(nbttagcompound));
            }
        }
        if (compound.hasKey("PickupPosX") && compound.hasKey("PickupPosY") && compound.hasKey("PickupPosZ")) {
            pickupPos = new BlockPos(compound.getInteger("PickupPosX"), compound.getInteger("PickupPosY"), compound.getInteger("PickupPosZ"));
        }
        if (compound.hasKey("DepositPosX") && compound.hasKey("DepositPosY") && compound.hasKey("DepositPosZ")) {
            depositPos = new BlockPos(compound.getInteger("DepositPosX"), compound.getInteger("DepositPosY"), compound.getInteger("DepositPosZ"));
            if (compound.hasKey("DepositFacing")) {
                depositFacing = EnumFacing.values()[compound.getInteger("DepositFacing")];
            }
        }
        if (compound.hasKey("TransportingFluid")) {
            NBTTagCompound fluidTag = compound.getCompoundTag("TransportingFluid");
            if (!fluidTag.isEmpty()) {
                transportingFluid = FluidStack.loadFluidStackFromNBT(fluidTag);
            }
        }
        if (compound.hasKey("CustomName", 8)) {
            this.setCustomNameTag(compound.getString("CustomName"));
        }
        if (compound.hasKey("MonsterOwnerUUID", 8)) {
            String s = compound.getString("MonsterOwnerUUID");
            this.setMonsterOwnerId(UUID.fromString(s));
            this.setOwnerMonster(true);
        }

        setupDynamicAI();

    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source) || source == DamageSource.IN_WALL && this.isRiding()) {
            return false;
        } else {
            Entity entity = source.getTrueSource();

            if (this.aiSit != null) {
                this.aiSit.setSitting(false);
            }

            if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow)) {
                amount = (amount + 1.0F) / 3.0F;
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    private int getCommandInteger() {
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

    @Nullable
    public UUID getMonsterOwnerId() {
        return (UUID) ((Optional) this.dataManager.get(MONSTER_OWNER_UNIQUE_ID)).orNull();
    }

    public void setMonsterOwnerId(@Nullable UUID p_184754_1_) {
        this.dataManager.set(MONSTER_OWNER_UNIQUE_ID, Optional.fromNullable(p_184754_1_));
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

    public boolean isOwnerMonster() {
        return this.dataManager.get(OWNER_NOT_PLAYER).booleanValue();
    }

    public void setOwnerMonster(boolean monster) {
        this.dataManager.set(OWNER_NOT_PLAYER, Boolean.valueOf(monster));
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

    public RatCommand getCommand() {
        return RatCommand.values()[MathHelper.clamp(getCommandInteger(), 0, RatCommand.values().length - 1)];
    }

    public void setCommand(RatCommand command) {
        setCommandInteger(command.ordinal());
    }

    public boolean isFollowing() {
        return getCommandInteger() == 2;
    }

    public boolean isTargetCommand() {
        return getCommandInteger() == 4 || getCommandInteger() == 5;
    }

    public boolean isAttackCommand() {
        return getCommandInteger() == 0 || getCommandInteger() == 2 || getCommandInteger() == 3 || getCommandInteger() == 7;
    }

    public boolean isHarvestCommand() {
        return getCommandInteger() == 4 || getCommandInteger() == 5;
    }

    public boolean isItemTargetCommand() {
        return getCommandInteger() == 3 || getCommandInteger() == 4 || getCommandInteger() == 5 || getCommandInteger() == 7;
    }

    public EntitySenses getSenses() {
        return this.getEntitySenses();
    }

    public boolean isHoldingFood() {
        return !this.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && (RatUtils.isRatFood(this.getHeldItem(EnumHand.MAIN_HAND)) || hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ORE_DOUBLING) && ItemRatUpgradeOreDoubling.isProcessable(this.getHeldItemMainhand()));
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float) ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
        if (flag) {
            this.applyEnchantments(this, entityIn);
            if (this.hasPlague() && entityIn instanceof EntityLivingBase && rollForPlague((EntityLivingBase) entityIn)) {
                ((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(RatsMod.PLAGUE_POTION, 6000));
            }
            if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FERAL_BITE)) {
                entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 5F);
                ((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(RatsMod.PLAGUE_POTION, 600));
                ((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.POISON, 600));
            }
            if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_TNT)) {
                Explosion explosion = new Explosion(this.world, null, this.posX, this.posY + (double) (this.height / 16.0F), this.posZ, 4.0F, false, world.getGameRules().getBoolean("mobGriefing"));
                explosion.doExplosionA();
                explosion.doExplosionB(true);
            }
            if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_TNT_SURVIVOR)) {
                Explosion explosion = new RatNukeExplosion(this.world, this, this.posX, this.posY + (double) (this.height / 16.0F), this.posZ, 4.0F, false, world.getGameRules().getBoolean("mobGriefing"));
                explosion.doExplosionA();
                explosion.doExplosionB(true);
            }
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
    public void onLivingUpdate() {
        this.setRatStatus(RatStatus.IDLE);
        if (this.getUpgradeSlot() != prevUpgrade) {
            this.onUpgradeChanged();
        }
        super.onLivingUpdate();
        this.prevFlyingPitch = flyingPitch;
        if (this.inTube()) {
            if (navigatorType != 3) {
                switchNavigator(3);
            }
            if (climbingTube) {
                this.motionY += 0.1D;
            } else if (!this.onGround && this.motionY < 0.0D) {
                this.motionY *= 0.6D;
            }
            double ydist = prevPosY - this.posY;//down 0.4 up -0.38
            double planeDist = (Math.abs(motionX) + Math.abs(motionZ)) * 12F;
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
        } else if (this.hasFlight() && !this.isInWater()) {
            if (navigatorType != 2) {
                switchNavigator(2);
            }
            if (canMove()) {
                if (this.getMoveHelper().getY() > this.posY) {
                    this.motionY += 0.08F;
                }
            } else if (!onGround) {
                this.motionY -= 0.08F;
            }
            if (!this.onGround) {
                double ydist = prevPosY - this.posY;//down 0.4 up -0.38
                double planeDist = (Math.abs(motionX) + Math.abs(motionZ)) * 12F;
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
                if (this.getMoveHelper().getY() > this.posY) {
                    this.motionY += 0.08F;
                }
            } else if (!onGround) {
                this.motionY -= 0.08F;
            }
            if (!this.onGround) {
                double ydist = prevPosY - this.posY;//down 0.4 up -0.38
                double planeDist = (Math.abs(motionX) + Math.abs(motionZ)) * 12F;
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
        boolean sitting = isSitting() || this.isRiding() || this.isDancing() || (this.getAnimation() == ANIMATION_IDLE_SCRATCH || this.getAnimation() == ANIMATION_IDLE_SNIFF) && shouldSitDuringAnimation();
        float sitInc = this.getAnimation() == ANIMATION_IDLE_SCRATCH || this.getAnimation() == ANIMATION_IDLE_SNIFF ? 5 : 1F;
        boolean holdingInHands = !sitting && (!this.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && (!this.holdInMouth || cookingProgress > 0)
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
        if (digCooldown <= 0 && RatsMod.CONFIG_OPTIONS.ratsDigBlocks && !this.isTamed()) {
            findDigTarget();
            digTarget();
        }
        if (this.getCommand() == RatCommand.SIT && !this.isSitting()) {
            this.setSitting(true);
        }
        if (this.isSitting() && this.getCommand() != RatCommand.SIT) {
            this.setSitting(false);
        }
        if (this.getAnimation() == ANIMATION_EAT && isHoldingFood() && eatingTicks <= 40) {
            eatingTicks++;
            eatItem(this.getHeldItem(EnumHand.MAIN_HAND), 3);
            if (eatingTicks == 40) {
                ItemStack pooStack = new ItemStack(RatsItemRegistry.RAT_NUGGET);
                ItemStack eatenFood = this.getHeldItem(EnumHand.MAIN_HAND).copy();
                if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ORE_DOUBLING) && ItemRatUpgradeOreDoubling.isProcessable(this.getHeldItem(EnumHand.MAIN_HAND))) {
                    pooStack = new ItemStack(RatsItemRegistry.RAT_NUGGET_ORE, 2, RatsNuggetRegistry.getNuggetMeta(this.getHeldItem(EnumHand.MAIN_HAND)));
                    NBTTagCompound poopTag = new NBTTagCompound();
                    NBTTagCompound oreTag = new NBTTagCompound();
                    ItemRatUpgradeOreDoubling.getProcessedOre(this.getHeldItem(EnumHand.MAIN_HAND)).writeToNBT(oreTag);
                    NBTTagCompound ingotTag = new NBTTagCompound();
                    ItemRatUpgradeOreDoubling.getProcessedIngot(this.getHeldItem(EnumHand.MAIN_HAND)).writeToNBT(ingotTag);
                    poopTag.setTag("OreItem", oreTag);
                    poopTag.setTag("IngotItem", ingotTag);
                    pooStack.setTagCompound(poopTag);
                }
                this.getHeldItem(EnumHand.MAIN_HAND).shrink(1);
                if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ORE_DOUBLING) || rand.nextFloat() <= 0.1F) {
                    if (RatsMod.CONFIG_OPTIONS.ratFartNoises) {
                        this.playSound(RatsSoundRegistry.RAT_POOP, 0.5F + rand.nextFloat() * 0.5F, 1.0F + rand.nextFloat() * 0.5F);
                    }
                    if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ORE_DOUBLING)) {
                        if (this.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
                            ItemStack oneStack = pooStack.copy();
                            oneStack.setCount(1);
                            this.setHeldItem(EnumHand.MAIN_HAND, oneStack);
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
                if (eatenFood.getItem() instanceof ItemFood) {
                    healAmount = ((ItemFood) eatenFood.getItem()).getHealAmount(eatenFood);
                }
                this.heal(healAmount);
                eatingTicks = 0;
            }
        }
        if (isHoldingFood() && (this.getRNG().nextInt(20) == 0 || eatingTicks > 0) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CHEF) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CHRISTMAS) && (this.getCommand() != RatCommand.TRANSPORT && this.getCommand() != RatCommand.GATHER && this.getCommand() != RatCommand.HARVEST && this.getCommand() != RatCommand.HUNT_ANIMALS && this.getCommand() != RatCommand.HUNT_MONSTERS || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ORE_DOUBLING) && ItemRatUpgradeOreDoubling.isProcessable(this.getHeldItemMainhand())) && (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ORE_DOUBLING) || this.shouldDepositItem(getHeldItemMainhand()))) {
            this.setAnimation(ANIMATION_EAT);
            this.setRatStatus(RatStatus.EATING);
        }
        if (this.hasPlague() && rand.nextFloat() < 0.3F) {
            double d0 = 0D;
            double d1 = this.rand.nextGaussian() * 0.05D + 0.5D;
            double d2 = 0D;
            this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CHEF) && !this.getHeldItemMainhand().isEmpty()) {
            this.tryCooking();
            if (cookingProgress > 0) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                if (cookingProgress == 99) {
                    this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
                    this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
                    this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
                } else {
                    this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
                    if (rand.nextFloat() < 0.125F) {
                        this.world.spawnParticle(EnumParticleTypes.FLAME, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
                    }
                }
            }
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ARCHEOLOGIST) && !this.getHeldItemMainhand().isEmpty()) {
            this.tryArcheology();
            if (cookingProgress > 0) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                if (cookingProgress == 99) {
                    this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
                    this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
                    this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
                } else {
                    this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2, Block.getIdFromBlock(RatsBlockRegistry.GARBAGE_PILE));
                    if (rand.nextFloat() < 0.125F) {
                        this.world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
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
                    this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
                    this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
                    this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
                } else {
                    this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2, Block.getIdFromBlock(Blocks.DIAMOND_ORE));
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
                    this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
                    this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
                    this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
                } else {
                    this.world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
                    this.world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
                }
            }
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CHRISTMAS)) {
            this.tryGiftgiving();
            if (cookingProgress > 0) {
                if (cookingProgress == CHRISTMAS_PRESENT_TIME - 1) {
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
                if (this.getNavigator().getPath() != null && this.getNavigator().getPath().getFinalPathPoint() != null && !this.isRiding()) {
                    Vec3d target = new Vec3d(this.getNavigator().getPath().getFinalPathPoint().x, this.getNavigator().getPath().getFinalPathPoint().y, this.getNavigator().getPath().getFinalPathPoint().z);
                    if (this.getDistanceSq(target.x, target.y, target.z) > 20 || !this.isDirectPathBetweenPoints(target)) {
                        this.attemptTeleport(target.x, target.y, target.z);
                    }
                }
            } else {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);

            }
        }
        if (world.isRemote && this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FERAL_BITE) && this.getRNG().nextInt(5) == 0) {
            float sitAddition = 0.125f * (sitProgress / 20F);
            float radius = 0.3F - sitAddition;
            float angle = (0.01745329251F * (this.renderYawOffset));
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + posX;
            double extraZ = (double) (radius * MathHelper.cos(angle)) + posZ;
            double extraY = 0.125 + posY + sitAddition;
            float particleRand = 0.1F;
            RatsMod.PROXY.spawnParticle("saliva", extraX + (double) (this.rand.nextFloat() * particleRand * 2) - (double) particleRand,
                    extraY,
                    extraZ + (double) (this.rand.nextFloat() * particleRand * 2) - (double) particleRand,
                    0F, 0.0F, 0F);
        }
        if (world.isRemote && this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PSYCHIC) && this.getRNG().nextInt(5) == 0) {
            float sitAddition = 0.125f * (sitProgress / 20F);
            float radius = 0.45F - sitAddition;
            float angle = (0.01745329251F * (this.renderYawOffset));
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + posX;
            double extraZ = (double) (radius * MathHelper.cos(angle)) + posZ;
            double extraY = 0.12 + posY + sitAddition;
            float particleRand = 0.4F;
            RatsMod.PROXY.spawnParticle("rat_lightning", extraX + (double) (this.rand.nextFloat() * particleRand * 2) - (double) particleRand,
                    extraY,
                    extraZ + (double) (this.rand.nextFloat() * particleRand * 2) - (double) particleRand,
                    0F, 0.0F, 0F);
        }
        if (this.isInCage()) {
            if (this.getAttackTarget() != null) {
                this.setAttackTarget(null);
            }
        }
        if (this.isTamed() && this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CRAFTING)) {
            TileEntity te = world.getTileEntity(new BlockPos(this).down());
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
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
                    } else {
                        this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY, this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2, Item.getIdFromItem(stack.getItem()));
                        this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY, this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2, Item.getIdFromItem(stack.getItem()));
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
                        this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
                    }
                    this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1, 1);
                }
            }
        }
        if (!world.isRemote && this.getRatStatus() == RatStatus.IDLE && this.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && this.getAnimation() == NO_ANIMATION && this.getRNG().nextInt(350) == 0 && this.shouldNotIdleAnimation()) {
            this.setAnimation(this.getRNG().nextBoolean() ? ANIMATION_IDLE_SNIFF : ANIMATION_IDLE_SCRATCH);
        }
        if (!world.isRemote && this.isOwnerMonster() && this.getOwnerMonster() instanceof EntityIllagerPiper) {
            EntityIllagerPiper piper = (EntityIllagerPiper) this.getOwnerMonster();
            if (piper.getAttackTarget() != null) {
                this.setAttackTarget(piper.getAttackTarget());
            }
        }
        if (!world.isRemote && this.isOwnerMonster() && this.getOwnerMonster() instanceof EntityBlackDeath) {
            EntityBlackDeath death = (EntityBlackDeath) this.getOwnerMonster();
            if (death.getAttackTarget() != null && death.getAttackTarget().getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() != RatsItemRegistry.BLACK_DEATH_MASK) {
                this.setAttackTarget(death.getAttackTarget());
            }
            if (this.getAttackTarget() == null || this.getAttackTarget().isDead) {
                float radius = (float) 5 - (float) Math.sin(death.ticksExisted * 0.4D) * 0.5F;
                int maxRatStuff = 360 / Math.max(death.getRatsSummoned(), 1);
                int ratIndex = this.getEntityId() % Math.max(death.getRatsSummoned(), 1);
                float angle = (0.01745329251F * (ratIndex * maxRatStuff + ticksExisted * 4.1F));
                double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + death.posX;
                double extraZ = (double) (radius * MathHelper.cos(angle)) + death.posZ;
                BlockPos runToPos = new BlockPos(extraX, death.posY, extraZ);
                int steps = 0;
                while (world.getBlockState(runToPos).isOpaqueCube() && steps < 10) {
                    runToPos = runToPos.up();
                    steps++;
                }
                this.getNavigator().tryMoveToXYZ(extraX, runToPos.getY(), extraZ, 1.33F);
            }
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_NONBELIEVER)) {
            if (this.getHealth() < this.getMaxHealth() && this.ticksExisted % 30 == 0) {
                this.heal(1.0F);
            }
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_VOODOO) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PSYCHIC)) {
            if (this.getHealth() < this.getMaxHealth() && this.ticksExisted % 30 == 0) {
                this.heal(1.0F);
            }
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PSYCHIC)) {
            if (rangedAttackCooldownPsychic == 0 && this.getAttackTarget() != null) {
                if (rand.nextBoolean()) {
                    rangedAttackCooldownPsychic = 50;
                    BlockPos ourPos = new BlockPos(this);
                    int searchRange = 10;
                    List<BlockPos> listOfAll = new ArrayList<>();
                    for (BlockPos pos : BlockPos.getAllInBox(ourPos.add(-searchRange, -searchRange, -searchRange), ourPos.add(searchRange, searchRange, searchRange))) {
                        IBlockState state = world.getBlockState(pos);
                        if (!world.isAirBlock(pos) && EntityWither.canDestroyBlock(state.getBlock())) {
                            listOfAll.add(pos);
                        }
                    }
                    if (listOfAll.size() > 0) {
                        BlockPos pos = listOfAll.get(rand.nextInt(listOfAll.size()));
                        EntityThrownBlock thrownBlock = new EntityThrownBlock(world, world.getBlockState(pos), this);
                        thrownBlock.setPosition(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
                        thrownBlock.dropBlock = false;
                        if (!world.isRemote) {
                            world.spawnEntity(thrownBlock);
                        }
                        RatsMod.NETWORK_WRAPPER.sendToAll(new MessageSyncThrownBlock(thrownBlock.getEntityId(), pos.toLong()));
                    } else {
                        rangedAttackCooldownPsychic = 5;
                    }
                } else {
                    rangedAttackCooldownPsychic = 100;
                    int bounds = 5;
                    for (int i = 0; i < rand.nextInt(2) + 1; i++) {
                        EntityLaserPortal laserPortal = new EntityLaserPortal(world, this.getAttackTarget().posX + this.rand.nextInt(bounds * 2) - bounds, this.posY + 2, this.getAttackTarget().posZ + this.rand.nextInt(bounds * 2) - bounds, this);
                        world.spawnEntity(laserPortal);
                    }
                }
            }
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BUCCANEER)) {
            if (this.getVisualFlag() && visualCooldown == 0) {
                this.setVisualFlag(false);
            }
            if (rangedAttackCooldownCannon == 0 && this.getAttackTarget() != null) {
                rangedAttackCooldownCannon = 60;
                EntityCheeseCannonball cannonball = new EntityCheeseCannonball(world, this);
                cannonball.ignoreEntity = this;
                double extraY = 0.6 + posY;
                double d0 = this.getAttackTarget().posY + (double) this.getAttackTarget().getEyeHeight() - 1.100000023841858D;
                double d1 = this.getAttackTarget().posX - this.posX;
                double d3 = this.getAttackTarget().posZ - this.posZ;
                double d2 = d0 - extraY;
                float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
                cannonball.setPosition(posX, extraY, posZ);
                cannonball.shoot(d1, d2 + (double) f, d3, 0.75F, 0.4F);
                this.setVisualFlag(true);
                visualCooldown = 4;
                this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 3.0F, 2.3F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
                if (!world.isRemote) {
                    this.world.spawnEntity(cannonball);
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
                double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + posX;
                double extraZ = (double) (radius * MathHelper.cos(angle)) + posZ;
                double extraY = 0.2 + posY;
                double targetRelativeX = this.getAttackTarget().posX - extraX;
                double targetRelativeY = this.getAttackTarget().posY + this.getAttackTarget().height / 2 - extraY;
                double targetRelativeZ = this.getAttackTarget().posZ - extraZ;
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1.0F, 1.25F + rand.nextFloat() * 0.5F);
                EntityRatDragonFire beam = new EntityRatDragonFire(world, this, targetRelativeX, targetRelativeY, targetRelativeZ);
                beam.setPosition(extraX, extraY, extraZ);
                if (!world.isRemote) {
                    world.spawnEntity(beam);
                }
            }
            this.extinguish();
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_RATINATOR)) {
            if (rangedAttackCooldownLaser == 0 && this.getAttackTarget() != null) {
                rangedAttackCooldownLaser = 10;
                float radius = 0.3F;
                for (int i = 0; i < 2; i++) {
                    float angle = (0.01745329251F * (this.renderYawOffset + (i == 0 ? 90 : -90)));
                    double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + posX;
                    double extraZ = (double) (radius * MathHelper.cos(angle)) + posZ;
                    double extraY = 0.2 + posY;
                    double targetRelativeX = this.getAttackTarget().posX - extraX;
                    double targetRelativeY = this.getAttackTarget().posY + this.getAttackTarget().height / 2 - extraY;
                    double targetRelativeZ = this.getAttackTarget().posZ - extraZ;
                    this.playSound(RatsSoundRegistry.LASER, 1.0F, 0.75F + rand.nextFloat() * 0.5F);
                    EntityLaserBeam beam = new EntityLaserBeam(world, this);
                    beam.setRGB(1.0F, 0.0F, 0.0F);
                    beam.setDamage(2.0F);
                    beam.setPosition(extraX, extraY, extraZ);
                    beam.shoot(targetRelativeX, targetRelativeY, targetRelativeZ, 2.0F, 0.4F);
                    if (!world.isRemote) {
                        world.spawnEntity(beam);
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
        prevUpgrade = this.getUpgradeSlot();
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
        if (this.isDancing() && (this.jukeboxPos == null || this.jukeboxPos.distanceSq(this.posX, this.posY, this.posZ) > 15.0D * 15.0D || this.world.getBlockState(this.jukeboxPos).getBlock() != Blocks.JUKEBOX)) {
            this.setDancing(false);
        }
    }

    private boolean inCageLogic() {
        return world.getBlockState(this.getPosition()).getBlock() instanceof BlockRatCage;
    }

    private boolean shouldSitDuringAnimation() {
        return !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PLATTER) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FARMER) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FISHERMAN) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CHRISTMAS);
    }

    public void createBabiesFrom(EntityRat mother, EntityRat father) {
        for (int i = 0; i < 1; i++) {
            EntityRat baby = new EntityRat(this.world);
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
            baby.setPosition(mother.posX, mother.posY, mother.posZ);
            baby.setGrowingAge(-24000);
            if (mother.isTamed()) {
                baby.setTamed(true);
                baby.setOwnerId(mother.getOwnerId());
                baby.setOwnerMonster(false);
            } else if (father.isTamed()) {
                baby.setTamed(true);
                baby.setOwnerId(father.getOwnerId());
                baby.setOwnerMonster(false);
            }
            world.spawnEntity(baby);
        }
    }

    public boolean canBeCollidedWith() {
        return (!this.isRiding() || !(this.getRidingEntity() instanceof EntityPlayer));
    }

    public ItemStack getCookingResultFor(ItemStack stack) {
        ItemStack burntItem = FurnaceRecipes.instance().getSmeltingResult(stack).copy();
        SharedRecipe recipe = RatsRecipeRegistry.getRatChefRecipe(stack);
        if (recipe != null) {
            burntItem = recipe.getOutput().copy();
        }
        return burntItem;
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
                    this.setHeldItem(EnumHand.MAIN_HAND, burntItem);
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
                    this.setHeldItem(EnumHand.MAIN_HAND, burntItem);
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
                    this.setHeldItem(EnumHand.MAIN_HAND, burntItem);
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
            burntItem = new ItemStack(Items.BOOK, heldItem.getCount(), heldItem.getMetadata());
        }
        if (heldItem.isItemEnchantable() && !disenchant && !heldItem.isItemEnchanted()) {
            burntItem = heldItem.copy();
        }
        if (disenchant && heldItem.isItemEnchanted()) {
            burntItem = heldItem.copy();
            if (burntItem.getTagCompound() != null && burntItem.getTagCompound().hasKey("ench", 9)) {
                if (!burntItem.getTagCompound().getTagList("ench", 10).isEmpty()) {
                    burntItem.getTagCompound().setTag("ench", new NBTTagCompound());
                }
            }
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
                                power += net.minecraftforge.common.ForgeHooks.getEnchantPower(world, position.add(k * 2, 0, j * 2));
                                power += net.minecraftforge.common.ForgeHooks.getEnchantPower(world, position.add(k * 2, 1, j * 2));
                                if (k != 0 && j != 0) {
                                    power += net.minecraftforge.common.ForgeHooks.getEnchantPower(world, position.add(k * 2, 0, j));
                                    power += net.minecraftforge.common.ForgeHooks.getEnchantPower(world, position.add(k * 2, 1, j));
                                    power += net.minecraftforge.common.ForgeHooks.getEnchantPower(world, position.add(k, 0, j * 2));
                                    power += net.minecraftforge.common.ForgeHooks.getEnchantPower(world, position.add(k, 1, j * 2));
                                }
                            }
                        }
                    }
                    burntItem = EnchantmentHelper.addRandomEnchantment(this.getRNG(), burntItem, (int) (2.0F + (float) this.getRNG().nextInt(2) + power), false);
                }
                if (heldItem.isEmpty()) {
                    this.setHeldItem(EnumHand.MAIN_HAND, burntItem);
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

    private void tryGiftgiving() {
        ItemStack heldItem = this.getHeldItemMainhand();
        boolean held = false;
        int luck = 1;
        List<ItemStack> result = new ArrayList<>();
        if (!world.isRemote) {
            LootContext.Builder lootcontext$builder = new LootContext.Builder((net.minecraft.world.WorldServer) this.world);
            lootcontext$builder.withLuck((float) luck).withLootedEntity(this); // Forge: add player & looted entity to LootContext
            result = this.world.getLootTableManager().getLootTableFromLocation(CHRISTMAS_LOOT).generateLootForPools(this.getRNG(), lootcontext$builder.build());
            if (result.isEmpty()) {
                cookingProgress = 0;
            } else {
                cookingProgress++;
                if (cookingProgress >= CHRISTMAS_PRESENT_TIME) {
                    for (ItemStack stack : result) {
                        if (heldItem.isEmpty() && !held) {
                            this.setHeldItem(EnumHand.MAIN_HAND, stack.copy());
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
        if (world.getTileEntity(new BlockPos(this)) != null) {
            TileEntity te = world.getTileEntity(new BlockPos(this));
            IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
            if (handler != null) {
                if (ItemHandlerHelper.insertItem(handler, burntItem, true).isEmpty()) {
                    ItemHandlerHelper.insertItem(handler, burntItem, false);
                    return true;
                }
            }
        }
        return false;
    }

    protected void eatItem(ItemStack stack, int eatingParticleCount) {
        if (!stack.isEmpty()) {
            if (stack.getItemUseAction() == EnumAction.DRINK) {
                this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }
            if (RatUtils.isRatFood(stack) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ORE_DOUBLING)) {
                for (int i = 0; i < eatingParticleCount; ++i) {
                    Vec3d vec3d = new Vec3d(((double) this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
                    vec3d = vec3d.rotatePitch(-this.rotationPitch * 0.017453292F);
                    vec3d = vec3d.rotateYaw(-this.rotationYaw * 0.017453292F);
                    double d0 = (double) (-this.rand.nextFloat()) * 0.6D - 0.3D;
                    Vec3d vec3d1 = new Vec3d(((double) this.rand.nextFloat() - 0.5D) * 0.3D, d0, 0.1D);
                    vec3d1 = vec3d1.rotatePitch(-this.rotationPitch * 0.017453292F);
                    vec3d1 = vec3d1.rotateYaw(-this.rotationYaw * 0.017453292F);
                    vec3d1 = vec3d1.add(this.posX, this.posY + (double) this.getEyeHeight(), this.posZ);

                    if (stack.getHasSubtypes()) {
                        this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05D, vec3d.z, Item.getIdFromItem(stack.getItem()), stack.getMetadata());
                    } else {
                        this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05D, vec3d.z, Item.getIdFromItem(stack.getItem()));
                    }
                }
                this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.25F + 0.25F * (float) this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.3F);
            }
        }
    }

    private void digTarget() {
        if (diggingPos != null) {
            ++this.breakingTime;
            int i = (int) ((float) this.breakingTime / 160.0F * 10.0F);
            this.getMoveHelper().action = EntityMoveHelper.Action.WAIT;
            if (this.getNavigator().getPath() != null) {
                this.getNavigator().clearPath();
            }
            this.motionZ *= 0.0D;
            this.motionX *= 0.0D;
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
                IBlockState prevState = world.getBlockState(diggingPos);
                double d1 = (finalDigPathPoint == null ? this.posX : finalDigPathPoint.getX()) - diggingPos.getX();
                double d2 = (finalDigPathPoint == null ? this.posZ : finalDigPathPoint.getZ()) - diggingPos.getZ();
                float rotation = -((float) MathHelper.atan2(d1, d2)) * (180F / (float) Math.PI);
                EnumFacing facing = EnumFacing.byHorizontalIndex(MathHelper.floor((double) (rotation * 4.0F / 360.0F) + 0.5D) & 3);
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
            if (diggingPos != null && this.getDistanceSq(diggingPos) > 2F) {
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
            } else if (entityIn instanceof EntityLivingBase && rollForPlague((EntityLivingBase) entityIn)) {
                if (((EntityLivingBase) entityIn).getActivePotionEffect(RatsMod.PLAGUE_POTION) != null) {
                    ((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(RatsMod.PLAGUE_POTION, 6000));
                }
            }
        }
    }

    private boolean rollForPlague(EntityLivingBase target) {
        boolean mask = target.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == RatsItemRegistry.PLAGUE_DOCTOR_MASK || target.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == RatsItemRegistry.BLACK_DEATH_MASK;
        if (mask) {
            return rand.nextFloat() < 0.3F;
        }
        return true;
    }

    public boolean isInRatHole() {
        return RatUtils.isRatHoleInBoundingBox(this.getEntityBoundingBox().grow(0.5D, 0.5D, 0.5D), world);
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
                if (digPos != null && this.getDistanceSq(digPos) < 2) {
                    if (world.getTileEntity(digPos) == null) {
                        Material material = world.getBlockState(digPos).getMaterial();
                        if (RatUtils.canRatBreakBlock(world, digPos, this) && canDigBlock(world, digPos) && (material.isToolNotRequired() || material == Material.CRAFTED_SNOW) && digPos.getY() == (int) Math.round(this.posY)) {
                            diggingPos = digPos;
                        }
                    }
                }
            }
        }
    }

    private boolean canDigBlock(World world, BlockPos pos) {
        return world.getBlockState(pos).isOpaqueCube();
    }

    public BlockPos rayTraceBlockPos(BlockPos targetPos) {
        RayTraceResult rayTrace = RatUtils.rayTraceBlocksIgnoreRatholes(world, this.getPositionVector(), new Vec3d(targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5), false);
        if (rayTrace != null && rayTrace.hitVec != null) {
            BlockPos sidePos = rayTrace.getBlockPos();
            BlockPos pos = new BlockPos(rayTrace.hitVec);
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

    public void updateAITasks() {
        if (this.getMoveHelper().isUpdating()) {
            double d0 = this.getMoveHelper().getSpeed();
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

    public void travel(float strafe, float vertical, float forward) {
        if (!this.canMove()) {
            strafe = 0;
            vertical = 0;
            forward = 0;
            this.getMoveHelper().action = EntityMoveHelper.Action.WAIT;
            if (this.getNavigator().getPath() != null) {
                this.getNavigator().clearPath();
            }
        }
        super.travel(strafe, vertical, forward);
    }

    public void openGUI(EntityPlayer playerEntity) {
        if (!this.world.isRemote && (!this.isBeingRidden() || this.isPassenger(playerEntity))) {
            playerEntity.openGui(RatsMod.INSTANCE, 1, this.world, this.getEntityId(), 0, 0);
        }
    }

    public boolean canMove() {
        return this.diggingPos == null && !this.isSitting() && this.getCommand().freeMove && !this.isChild();
    }

    public boolean isInCage() {
        return inCage;
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
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

    public boolean canPhaseThroughBlock(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() instanceof BlockFence || world.getBlockState(pos).getBlock() instanceof BlockFenceGate;
    }

    public void setKilledInTrap() {
        isDeadInTrap = true;
        this.attackEntityFrom(RatsMod.ratTrapDamage, Float.MAX_VALUE);
    }

    protected void onDeathUpdate() {
        ++this.deathTime;
        int maxDeathTime = isDeadInTrap ? 60 : 20;
        if (this.deathTime == maxDeathTime) {
            if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot"))) {
                int i = this.getExperiencePoints(this.attackingPlayer);
                i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
                while (i > 0) {
                    int j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
                }
                if (rand.nextInt(RatsMod.CONFIG_OPTIONS.tokenDropRate) == 0) {
                    this.entityDropItem(new ItemStack(RatsItemRegistry.CHUNKY_CHEESE_TOKEN), 0.0F);
                }
                if (this.hasPlague() && rand.nextFloat() <= RatsMod.CONFIG_OPTIONS.plagueEssenceDropRate) {
                    this.entityDropItem(new ItemStack(RatsItemRegistry.PLAGUE_ESSENCE), 0.0F);
                }
                if (this.hasPlague() && rand.nextFloat() <= RatsMod.CONFIG_OPTIONS.plagueTomeDropRate) {
                    this.entityDropItem(new ItemStack(RatsItemRegistry.PLAGUE_TOME), 0.0F);
                }
                if (this.hasToga()) {
                    this.entityDropItem(new ItemStack(RatsItemRegistry.RAT_TOGA), 0.0F);
                    if (this.dimension == RatsMod.CONFIG_OPTIONS.ratlantisDimensionId) {
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

            this.setDead();
            for (int k = 0; k < 20; ++k) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d2, d0, d1);
            }
        }
    }

    public void setDead() {
        if (!isDead && this.isTamed() && this.getOwnerMonster() != null && this.getOwnerMonster() instanceof EntityIllagerPiper) {
            EntityIllagerPiper illagerPiper = (EntityIllagerPiper) this.getOwnerMonster();
            illagerPiper.setRatsSummoned(illagerPiper.getRatsSummoned() - 1);
        }
        if (!isDead && this.isTamed() && this.getOwnerMonster() != null && this.getOwnerMonster() instanceof EntityBlackDeath) {
            EntityBlackDeath illagerPiper = (EntityBlackDeath) this.getOwnerMonster();
            illagerPiper.setRatsSummoned(illagerPiper.getRatsSummoned() - 1);
        }
        this.isDead = true;
    }

    public void updateRidden() {
        Entity entity = this.getRidingEntity();
        if (entity != null && (entity.isDead || entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getHealth() <= 0.0F)) {
            this.dismountRidingEntity();
        } else {
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
            this.onUpdate();
            if (this.isRiding()) {
                this.updateRiding(entity);
            }
        }
    }

    public boolean writeToNBTOptional(NBTTagCompound compound) {
        String s = this.getEntityString();
        compound.setString("id", s);
        this.writeToNBT(compound);
        return true;
    }

    public void removePassengers() {

    }

    public void updateRiding(Entity riding) {
        if (riding != null && riding.isPassenger(this) && riding instanceof EntityPlayer) {
            int i = riding.getPassengers().indexOf(this);
            float radius = (i == 0 ? 0F : 0.4F) + (((EntityPlayer) riding).isElytraFlying() ? 2 : 0);
            float angle = (0.01745329251F * ((EntityPlayer) riding).renderYawOffset) + (i == 2 ? -92.5F : i == 1 ? 92.5F : 0);
            double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
            double extraZ = radius * MathHelper.cos(angle);
            double extraY = (riding.isSneaking() ? 1.1D : 1.4D);
            this.rotationYaw = ((EntityPlayer) riding).rotationYawHead;
            this.rotationYawHead = ((EntityPlayer) riding).rotationYawHead;
            this.prevRotationYaw = ((EntityPlayer) riding).rotationYawHead;
            this.setPosition(riding.posX + extraX, riding.posY + extraY, riding.posZ + extraZ);
            if (((EntityPlayer) riding).isElytraFlying()) {
                this.dismountRidingEntity();
            }
        }
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() == RatsItemRegistry.RAT_TOGA) {
            if (!this.hasToga()) {
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
            } else {
                if (!world.isRemote) {
                    this.entityDropItem(new ItemStack(RatsItemRegistry.RAT_TOGA), 0.0F);
                }
            }
            this.setToga(!this.hasToga());
            this.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1F, 1.5F);
        }
        if (itemstack.getItem() == RatsItemRegistry.CREATIVE_CHEESE && this.canBeTamed()) {
            this.setTamed(true);
            this.setOwnerMonster(false);
            this.world.setEntityState(this, (byte) 83);
            this.setTamedBy(player);
            return true;
        }
        if (itemstack.interactWithEntity(player, this, hand)) {
            return true;
        }
        if (!super.processInteract(player, hand)) {
            if (this.isTamed() && !this.isChild() && (isOwner(player) || player.isCreative())) {
                if (itemstack.getItem() == RatsItemRegistry.RAT_SACK) {
                    NBTTagCompound compound = itemstack.getTagCompound();
                    if (compound == null) {
                        compound = new NBTTagCompound();
                        itemstack.setTagCompound(compound);
                    }
                    NBTTagCompound ratTag = new NBTTagCompound();
                    this.writeEntityToNBT(ratTag);
                    int currentRat = ItemRatSack.getRatsInStack(itemstack) + 1;
                    compound.setTag("Rat_" + currentRat, ratTag);
                    this.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1, 1);
                    this.setDead();
                    player.swingArm(hand);
                    return true;
                } else if (itemstack.getItem() == RatsItemRegistry.CHEESE_STICK || itemstack.getItem() == RatsItemRegistry.RADIUS_STICK) {
                    RatsMod.PROXY.setRefrencedRat(this);
                    itemstack.getTagCompound().setUniqueId("RatUUID", this.getPersistentID());
                    player.swingArm(hand);
                    player.sendStatusMessage(new TextComponentTranslation("entity.rat.staff.bind", this.getName()), true);
                    return true;
                } else if (itemstack.getItem() == Items.ARROW) {
                    RatsMod.PROXY.setRefrencedRat(this);
                    itemstack.shrink(1);
                    ItemStack ratArrowStack = new ItemStack(RatsItemRegistry.RAT_ARROW);
                    NBTTagCompound compound = new NBTTagCompound();
                    NBTTagCompound ratTag = new NBTTagCompound();
                    this.writeEntityToNBT(ratTag);
                    compound.setTag("Rat", ratTag);
                    ratArrowStack.setTagCompound(compound);
                    if (itemstack.isEmpty()) {
                        player.setHeldItem(hand, ratArrowStack);
                    } else if (!player.inventory.addItemStackToInventory(ratArrowStack)) {
                        player.dropItem(ratArrowStack, false);
                    }
                    this.playSound(RatsSoundRegistry.RAT_HURT, 1, 1);
                    player.swingArm(hand);
                    this.setDead();
                    return true;
                } else if (!player.isSneaking() && this.canBeTamed()) {
                    openGUI(player);
                    return true;
                } else {
                    if (player.getPassengers().size() < 3) {
                        player.sendStatusMessage(new TextComponentTranslation("entity.rat.dismount_instructions"), true);
                        this.startRiding(player, true);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void setTamed(boolean tamed) {
        if (tamed) {
            Arrays.fill(this.inventoryArmorDropChances, 1.0F);
            Arrays.fill(this.inventoryHandsDropChances, 1.0F);
        }
        super.setTamed(tamed);
    }

    public int getTalkInterval() {
        if (this.hasPlague() || this.isTamed()) {
            return 200;
        }
        return super.getTalkInterval();
    }


    public boolean canBeTamed() {
        return !this.hasPlague();
    }

    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
        if (slotIn == EntityEquipmentSlot.MAINHAND) {
            return ratInventory.getStackInSlot(0);
        } else if (slotIn == EntityEquipmentSlot.HEAD) {
            return ratInventory.getStackInSlot(1);
        } else if (slotIn == EntityEquipmentSlot.OFFHAND) {
            return ratInventory.getStackInSlot(2);
        }
        return super.getItemStackFromSlot(slotIn);
    }

    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
        if (slotIn == EntityEquipmentSlot.MAINHAND) {
            ratInventory.setInventorySlotContents(0, stack);
        } else if (slotIn == EntityEquipmentSlot.HEAD) {
            ratInventory.setInventorySlotContents(1, stack);
        } else if (slotIn == EntityEquipmentSlot.OFFHAND) {
            ratInventory.setInventorySlotContents(2, stack);
        } else {
            super.getItemStackFromSlot(slotIn);
        }
    }

    private void initInventory() {
        ratInventory = new ContainerHorseChest("ratInventory", 4);
        ratInventory.setCustomName(this.getName());
        if (ratInventory != null) {
            for (int j = 0; j < ratInventory.getSizeInventory(); ++j) {
                ItemStack itemstack = ratInventory.getStackInSlot(j);
                if (!itemstack.isEmpty()) {
                    ratInventory.setInventorySlotContents(j, itemstack.copy());
                }
            }
        }
    }

    public void fall(float distance, float damageMultiplier) {
        if (!this.hasFlight() && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER) && !this.inTube()) {
            super.fall(distance, damageMultiplier);
        }
    }

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
        if (!this.hasFlight() && !this.inTube()) {
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

    @SideOnly(Side.CLIENT)
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
                double d3 = this.prevPosX + (this.posX - this.prevPosX) * d6 + (rand.nextDouble() - 0.5D) * (double) this.width * 2.0D;
                double d4 = this.prevPosY + (this.posY - this.prevPosY) * d6 + rand.nextDouble() * (double) this.height;
                double d5 = this.prevPosZ + (this.posZ - this.prevPosZ) * d6 + (rand.nextDouble() - 0.5D) * (double) this.width * 2.0D;
                world.spawnParticle(EnumParticleTypes.WATER_SPLASH, d3, d4, d5, f, f1, f2);
            }
        } else if (type == 2) {
            for (int j = 0; j < 5; ++j) {
                double d6 = (double) (j) / 5D;
                float f = (this.rand.nextFloat() - 0.5F) * 0.2F;
                float f1 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                float f2 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                double d3 = this.prevPosX + (this.posX - this.prevPosX) * d6 + (rand.nextDouble() - 0.5D) * (double) this.width * 2.0D;
                double d4 = this.prevPosY + (this.posY - this.prevPosY) * d6 + rand.nextDouble() * (double) this.height;
                double d5 = this.prevPosZ + (this.posZ - this.prevPosZ) * d6 + (rand.nextDouble() - 0.5D) * (double) this.width * 2.0D;
                world.spawnParticle(EnumParticleTypes.PORTAL, d3, d4, d5, f, f1, f2);
            }
        } else {
            EnumParticleTypes enumparticletypes = EnumParticleTypes.SMOKE_NORMAL;

            if (type == 1) {
                enumparticletypes = EnumParticleTypes.HEART;
            }
            if (type == 5) {
                enumparticletypes = EnumParticleTypes.SNOW_SHOVEL;
            }
            for (int i = 0; i < 9; ++i) {
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                double d2 = this.rand.nextGaussian() * 0.02D;
                this.world.spawnParticle(enumparticletypes, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
            }
        }
    }

    public boolean isMoving() {
        return Math.abs(motionX) >= 0.05D || Math.abs(motionZ) >= 0.05D;
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setColorVariant(this.getRNG().nextInt(4));
        this.setMale(this.getRNG().nextBoolean());
        if (this.getRNG().nextInt(15) == 0 && this.world.getDifficulty() != EnumDifficulty.PEACEFUL && RatsMod.CONFIG_OPTIONS.plagueRats) {
            this.setPlague(true);
        }
        if (this.dimension == RatsMod.CONFIG_OPTIONS.ratlantisDimensionId) {
            this.setToga(true);
        }
        if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
            Calendar calendar = this.world.getCurrentDate();
            if (calendar.get(2) + 1 == 10 && calendar.get(5) > 10 && this.rand.nextFloat() <= 0.25F) {
                this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Blocks.PUMPKIN));
                this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 1.0F;
            }
        }
        if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
            Calendar calendar = this.world.getCurrentDate();
            if ((calendar.get(2) + 1 == 11 && calendar.get(5) > 10 || calendar.get(2) + 1 == 12) && this.rand.nextFloat() <= 0.25F) {
                this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(RatsItemRegistry.SANTA_HAT));
                this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 1.0F;
            }
        }
        if (this.getRNG().nextFloat() < 0.025F) {
            this.setColorVariant(this.getRNG().nextBoolean() ? 9 : 4);
        }
        return livingdata;
    }

    @SideOnly(Side.CLIENT)
    public String getRatTexture() {
        return RAT_TEXTURES[MathHelper.clamp(this.getColorVariant(), 0, RAT_TEXTURES.length - 1)];
    }

    public boolean shouldHuntAnimals() {
        return this.getCommandInteger() == 3 || !this.isTamed() && this.hasPlague();
    }

    public boolean shouldHuntMonsters() {
        return this.getCommandInteger() == 7 || !this.isTamed() && this.hasPlague();
    }

    public String getName() {
        if (this.hasCustomName()) {
            return this.getCustomNameTag();
        } else {
            String s = EntityList.getEntityString(this);
            if (s == null) {
                s = "generic";
            }
            if (this.hasPlague()) {
                s = "plague_rat";
            }
            return I18n.translateToLocal("entity." + s + ".name");
        }
    }

    public boolean shouldEyesGlow() {
        return this.hasPlague() || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_NONBELIEVER) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DRAGON) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_RATINATOR) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ENDER) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_AQUATIC);
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LOOT;
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
        if (RatsMod.iafLoaded && this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DRAGON)) {
            SoundEvent possibleDragonSound = SoundEvent.REGISTRY.getObject(new ResourceLocation("iceandfire", "firedragon_child_idle"));
            if (possibleDragonSound != null) {
                return possibleDragonSound;
            }
        }
        return super.getAmbientSound();
    }

    protected SoundEvent getDeathSound() {
        if (RatsMod.iafLoaded && this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DRAGON)) {
            SoundEvent possibleDragonSound = SoundEvent.REGISTRY.getObject(new ResourceLocation("iceandfire", "firedragon_child_death"));
            if (possibleDragonSound != null) {
                return possibleDragonSound;
            }
        }
        return RatsSoundRegistry.RAT_DIE;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        if (RatsMod.iafLoaded && this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DRAGON)) {
            SoundEvent possibleDragonSound = SoundEvent.REGISTRY.getObject(new ResourceLocation("iceandfire", "firedragon_child_hurt"));
            if (possibleDragonSound != null) {
                return possibleDragonSound;
            }
        }
        return RatsSoundRegistry.RAT_HURT;
    }

    public boolean onHearFlute(EntityPlayer player, RatCommand ratCommand) {
        if (this.isTamed() && this.isOwner(player) && !this.isChild() && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_NO_FLUTE)) {
            this.setCommand(ratCommand);
            return true;
        }
        return false;
    }

    public boolean canRatPickupItem(ItemStack stack) {
        if ((this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BLACKLIST) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_WHITELIST)) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER)) {
            NBTTagCompound nbttagcompound1;
            if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BLACKLIST)) {
                nbttagcompound1 = this.getUpgrade(RatsItemRegistry.RAT_UPGRADE_BLACKLIST).getTagCompound();
            } else {
                nbttagcompound1 = this.getUpgrade(RatsItemRegistry.RAT_UPGRADE_WHITELIST).getTagCompound();
            }
            if (nbttagcompound1 != null && nbttagcompound1.hasKey("Items", 9)) {
                NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
                ItemStackHelper.loadAllItems(nbttagcompound1, nonnulllist);
                if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BLACKLIST)) {
                    for (ItemStack itemstack : nonnulllist) {
                        if (itemstack.isItemEqual(stack)) {
                            return false;
                        }
                    }
                    return true;
                } else {
                    //whitelist
                    for (ItemStack itemstack : nonnulllist) {
                        if (itemstack.isItemEqual(stack)) {
                            return true;
                        }
                    }
                    return false;
                }

            }
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FARMER) && this.getCommand() == RatCommand.HARVEST) {
            Item item = stack.getItem();
            return item instanceof ItemSeeds || item instanceof ItemSeedFood || item instanceof ItemBlock || item == Items.DYE && EnumDyeColor.byDyeDamage(stack.getMetadata()) == EnumDyeColor.WHITE;
        }
        return true;
    }

    public boolean attemptTeleport(double x, double y, double z) {
        double d0 = this.posX;
        double d1 = this.posY;
        double d2 = this.posZ;
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.world.setEntityState(this, (byte) 84);
        boolean flag = false;
        BlockPos blockpos = new BlockPos(this);
        World world = this.world;
        Random random = this.getRNG();

        if (world.isBlockLoaded(blockpos)) {
            boolean flag1 = false;

            while (!flag1 && blockpos.getY() > 0) {
                BlockPos blockpos1 = blockpos.down();
                IBlockState iblockstate = world.getBlockState(blockpos1);

                if (iblockstate.getMaterial().blocksMovement()) {
                    flag1 = true;
                } else {
                    --this.posY;
                    blockpos = blockpos1;
                }
            }

            if (flag1) {
                this.setPositionAndUpdate(this.posX, this.posY, this.posZ);

                if (world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(this.getEntityBoundingBox())) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            this.setPositionAndUpdate(d0, d1, d2);
            return false;
        } else {
            int i = 128;
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1, 1);
            return true;
        }
    }

    public boolean isDirectPathBetweenPoints(Vec3d target) {
        RayTraceResult rayTrace = RatUtils.rayTraceBlocksIgnoreRatholes(world, getPositionVector(), target.add(0.5, 0.5, 0.5), false);
        if (rayTrace != null && rayTrace.hitVec != null) {
            BlockPos sidePos = rayTrace.getBlockPos();
            BlockPos pos = new BlockPos(rayTrace.hitVec);
            if (!world.isAirBlock(pos) || !world.isAirBlock(sidePos)) {
                return true;
            } else {
                return rayTrace.typeOfHit == RayTraceResult.Type.MISS;
            }
        }
        return true;
    }

    public BlockPos getLightPosition() {
        BlockPos pos = new BlockPos(this);
        if (!world.getBlockState(pos).isFullBlock()) {
            return pos.up();
        }
        return pos;
    }

    public boolean inTube() {
        return inTube;
    }

    private boolean inTubeLogic() {
        if (this.isTamed()) {
            BlockPos pos = new BlockPos(this);
            IBlockState state = world.getBlockState(pos);
            boolean above = world.getBlockState(pos.up()).getBlock() instanceof BlockRatTube;
            if (state.getBlock() instanceof BlockRatTube) {
                List<AxisAlignedBB> aabbs = ((BlockRatTube) state.getBlock()).compileAABBList(world, pos, state);
                AxisAlignedBB bb = new AxisAlignedBB(0.5, 0.5, 0.5, 0.5, 0.5, 0.5);
                for (AxisAlignedBB box : aabbs) {
                    bb = bb.union(box);
                }
                bb = bb.grow(0.05F, 0, 0.05F).offset(pos);
                return bb.contains(this.getPositionVector().add(0, this.height / 2, 0)) || bb.contains(this.getPositionVector()) && above;
            }
        }
        return false;
    }

    public void setTubeTarget(BlockPos targetPosition) {
        tubeTarget = targetPosition;
    }

    public ItemStack getUpgradeSlot() {
        return getHeldItem(EnumHand.OFF_HAND);
    }

    public ItemStack getUpgrade(Item item) {
        ItemStack stack = getUpgradeSlot();
        if (stack.getItem() == item) {
            return stack;
        }
        if (stack.getItem() instanceof ItemRatCombinedUpgrade || stack.getItem() instanceof ItemRatUpgradeJuryRigged) {
            NBTTagCompound nbttagcompound1 = stack.getTagCompound();
            if (nbttagcompound1 != null && nbttagcompound1.hasKey("Items", 9)) {
                NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
                ItemStackHelper.loadAllItems(nbttagcompound1, nonnulllist);
                for (ItemStack stack1 : nonnulllist) {
                    if (stack1.getItem() == item) {
                        return stack1;
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public boolean hasUpgrade(Item item) {
        if (!this.getUpgradeSlot().isEmpty()) {
            return getUpgrade(item) != ItemStack.EMPTY;
        } else {
            return false;
        }
    }

    public boolean holdsItemInHandUpgrade() {
        return this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PLATTER) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FARMER) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FISHERMAN) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_SHEARS) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CHRISTMAS);
    }

    public boolean shouldNotIdleAnimation() {
        return this.holdInMouth && this.getAnimation() != EntityRat.ANIMATION_EAT && this.cookingProgress <= 0
                && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PLATTER) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK)
                && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FARMER) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FISHERMAN) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_SHEARS);
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
            tryIncreaseStat(SharedMonsterAttributes.MOVEMENT_SPEED, 0.5D);
            flagSpeed = true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_GOD)) {
            tryIncreaseStat(SharedMonsterAttributes.MAX_HEALTH, 500D);
            tryIncreaseStat(SharedMonsterAttributes.ARMOR, 50D);
            tryIncreaseStat(SharedMonsterAttributes.ATTACK_DAMAGE, 50D);
            flagHealth = true;
            flagArmor = true;
            flagAttack = true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_WARRIOR)) {
            tryIncreaseStat(SharedMonsterAttributes.MAX_HEALTH, 40D);
            tryIncreaseStat(SharedMonsterAttributes.ARMOR, 12D);
            tryIncreaseStat(SharedMonsterAttributes.ATTACK_DAMAGE, 10D);
            flagHealth = true;
            flagArmor = true;
            flagAttack = true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_HEALTH)) {
            tryIncreaseStat(SharedMonsterAttributes.MAX_HEALTH, 25D);
            flagHealth = true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ARMOR)) {
            tryIncreaseStat(SharedMonsterAttributes.ARMOR, 10D);
            flagArmor = true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_STRENGTH)) {
            tryIncreaseStat(SharedMonsterAttributes.ATTACK_DAMAGE, 5D);
            flagAttack = true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_VOODOO)) {
            tryIncreaseStat(SharedMonsterAttributes.MAX_HEALTH, 100D);
            flagHealth = true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_RATINATOR)) {
            tryIncreaseStat(SharedMonsterAttributes.MAX_HEALTH, 30D);
            tryIncreaseStat(SharedMonsterAttributes.ARMOR, 80D);
            flagHealth = true;
            flagArmor = true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DRAGON)) {
            tryIncreaseStat(SharedMonsterAttributes.MAX_HEALTH, 50D);
            tryIncreaseStat(SharedMonsterAttributes.ARMOR, 15D);
            tryIncreaseStat(SharedMonsterAttributes.ATTACK_DAMAGE, 8D);
            flagHealth = true;
            flagArmor = true;
            flagAttack = true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_NONBELIEVER)) {
            tryIncreaseStat(SharedMonsterAttributes.MAX_HEALTH, 1000D);
            tryIncreaseStat(SharedMonsterAttributes.ARMOR, 100D);
            tryIncreaseStat(SharedMonsterAttributes.ATTACK_DAMAGE, 100D);
            flagHealth = true;
            flagArmor = true;
            flagAttack = true;
        }
        if (!flagHealth) {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
        }
        if (!flagArmor) {
            this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0.0D);
        }
        if (!flagAttack) {
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
        }
        if (!flagSpeed) {
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        }
        if (this.getHeldRF() > this.getRFTransferRate()) {
            this.setHeldRF(0);
        }
        this.heal(this.getMaxHealth());
    }

    private void tryIncreaseStat(IAttribute stat, double value) {
        double prev = this.getEntityAttribute(stat).getAttributeValue();
        if (prev < value) {
            this.getEntityAttribute(stat).setBaseValue(value);
        }
    }

    public boolean isPotionApplicable(PotionEffect potioneffectIn) {
        if (potioneffectIn.getPotion() == MobEffects.POISON && (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_POISON) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION))) {
            return false;
        }
        if (potioneffectIn.getPotion() == RatsMod.PLAGUE_POTION) {
            return false;
        }
        return super.isPotionApplicable(potioneffectIn);
    }

    public boolean isEntityInvulnerable(DamageSource source) {
        if (source.isFireDamage() && (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ASBESTOS) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DRAGON))) {
            return true;
        }
        if ((source.isMagicDamage() || source == DamageSource.WITHER) && (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_POISON) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION))) {
            return true;
        }
        if (source == DamageSource.IN_WALL) {
            return true;
        }
        if ((source == DamageSource.DROWN) && (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_POISON) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION))) {
            return true;
        }
        if (source == DamageSource.FALL && (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_TNT_SURVIVOR))) {
            return true;
        }
        if (source.isExplosion() && this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_TNT_SURVIVOR)) {
            return true;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CREATIVE)) {
            return source.getTrueSource() == null || source.getTrueSource() instanceof EntityLivingBase && !isOwner((EntityLivingBase) source.getTrueSource());
        }
        return super.isEntityInvulnerable(source);
    }

    @SideOnly(Side.CLIENT)
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
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ARCHEOLOGIST) && !this.getArcheologyResultFor(item).isEmpty()) {
            return false;
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_GEMCUTTER) && !this.getGemcutterResultFor(item).isEmpty()) {
            return false;
        }
        return !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ORE_DOUBLING) || !ItemRatUpgradeOreDoubling.isProcessable(item);
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

    public boolean hasFlight() {
        return this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FLIGHT) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DRAGON);
    }

    public boolean isOnSameTeam(Entity entityIn) {
        if (entityIn instanceof EntityTameable) {
            EntityTameable tameable = (EntityTameable) entityIn;
            if (tameable.isTamed() && this.isTamed() && this.getOwnerId() != null && tameable.getOwnerId() != null && this.getOwnerId().equals(tameable.getOwnerId())) {
                return true;
            }
        }
        return super.isOnSameTeam(entityIn);
    }

    public BlockPos getSearchCenter() {
        if (getSearchRadiusCenter() == null || !this.hasCustomSearchZone()) {
            return this.getPosition();
        } else {
            return getSearchRadiusCenter();
        }
    }
}
