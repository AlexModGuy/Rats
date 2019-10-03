package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.BlockRatCage;
import com.github.alexthe666.rats.server.blocks.BlockRatHole;
import com.github.alexthe666.rats.server.blocks.BlockRatTube;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.ai.*;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCraftingTable;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatHole;
import com.github.alexthe666.rats.server.items.ItemRatCombinedUpgrade;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.message.MessageDancingRat;
import com.github.alexthe666.rats.server.message.MessageSyncThrownBlock;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import com.github.alexthe666.rats.server.recipes.RatsRecipeRegistry;
import com.github.alexthe666.rats.server.recipes.SharedRecipe;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityXPOrb;
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
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
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
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
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
    private static final DataParameter<Boolean> IS_MALE = EntityDataManager.createKey(EntityRat.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> TOGA = EntityDataManager.createKey(EntityRat.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PLAGUE = EntityDataManager.createKey(EntityRat.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> VISUAL_FLAG = EntityDataManager.createKey(EntityRat.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> COMMAND = EntityDataManager.createKey(EntityRat.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> COLOR_VARIANT = EntityDataManager.createKey(EntityRat.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> DANCING = EntityDataManager.createKey(EntityRat.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> DANCE_MOVES = EntityDataManager.createKey(EntityRat.class, DataSerializers.VARINT);
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
            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD};
    public float sitProgress;
    public float holdProgress;
    public float deadInTrapProgress;
    public boolean isDeadInTrap;
    public BlockPos fleePos;
    public boolean holdInMouth = true;
    public int wildTrust = 0;
    public ContainerHorseChest ratInventory;
    public BlockPos depositPos;
    public EnumFacing depositFacing = EnumFacing.UP;
    public BlockPos pickupPos;
    public BlockPos tubeTarget = null;
    public int tubeTicks;
    public boolean prevInTube;
    public boolean climbingTube = false;
    public boolean waterBased = false;
    public boolean crafting = false;
    public int cookingProgress = 0;
    public int breedCooldown = 0;
    public float flyingPitch;
    public float prevFlyingPitch;
    public BlockPos jukeboxPos;
    public boolean isFleeing = false;
    /*
       0 = tamed navigator
       1 = wild navigator
       2 = flight navigator
       3 = tube navigator
     */
    protected int navigatorType;
    private int tubeCooldown = 0;
    private boolean inTube;
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
    private EntityAIBase aiHarvest;
    private int rangedAttackCooldownCannon = 0;
    private int rangedAttackCooldownLaser = 0;
    private int rangedAttackCooldownPsychic = 0;
    private int visualCooldown = 0;

    public EntityRat(World worldIn) {
        super(worldIn);
        this.setPathPriority(PathNodeType.RAIL, 1000F);
        switchNavigator(1);
        this.setSize(0.49F, 0.49F);
        initInventory();
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

    protected void initEntityAI() {
        aiHarvest = new RatAIHarvestCrops(this);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.45D, false));
        this.tasks.addTask(2, new RatAIFleeMobs(this, new Predicate<Entity>() {
            public boolean apply(@Nullable Entity entity) {
                return entity.isEntityAlive() && (entity instanceof EntityPlayer && ((EntityPlayer) entity).getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() != RatsItemRegistry.PIPER_HAT) || entity instanceof EntityOcelot;
            }
        }, 10.0F, 0.8D, 1.33D));
        this.tasks.addTask(3, new RatAIFollowOwner(this, 1.33D, 3.0F, 1.0F));
        this.tasks.addTask(4, new RatAIWander(this, 1.0D));
        this.tasks.addTask(4, new RatAIWanderFlight(this));
        this.tasks.addTask(5, new RatAIFleeSun(this, 1.66D));
        this.tasks.addTask(5, this.aiSit = new RatAISit(this));
        this.tasks.addTask(6, new RatAIRaidChests(this));
        this.tasks.addTask(6, new RatAIRaidCrops(this));
        this.tasks.addTask(6, new RatAIEnterTrap(this));
        this.tasks.addTask(6, new RatPickupFromInventory(this));
        this.tasks.addTask(6, new RatDepositInInventory(this));
        this.tasks.addTask(6, new RatAIFleePosition(this));
        this.tasks.addTask(6, new RatAIAttackMelee(this, 1.5D, false));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityLivingBase.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(0, new RatAITargetItems(this, false));
        this.targetTasks.addTask(1, new RatAIHuntPrey(this, new Predicate<EntityLivingBase>() {
            public boolean apply(@Nullable EntityLivingBase entity) {
                if (EntityRat.this.hasPlague()) {
                    return entity instanceof EntityPlayer && !entity.isOnSameTeam(EntityRat.this);
                } else {
                    if (entity instanceof EntityTameable && ((EntityTameable) entity).isTamed()) {
                        return false;
                    }
                    return entity != null && !(entity instanceof EntityRat) && !entity.isOnSameTeam(EntityRat.this) && (!(entity instanceof EntityPlayer) || EntityRat.this.hasPlague()) && !entity.isChild();
                }
            }
        }));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(3, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(4, new RatAIHurtByTarget(this, false));
    }

    protected void setupHarvestAI() {
        this.tasks.removeTask(this.aiHarvest);
        if (this.aiHarvest == null) {
            aiHarvest = new RatAIHarvestCrops(this);
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK) && !(aiHarvest instanceof RatAIHarvestTrees)) {
            aiHarvest = new RatAIHarvestTrees(this);
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER) && !(aiHarvest instanceof RatAIHarvestMine)) {
            aiHarvest = new RatAIHarvestMine(this);
        }
        this.tasks.addTask(4, this.aiHarvest);
    }

    @Nullable
    public EntityLivingBase getOwner() {
        try {
            UUID uuid = this.getOwnerId();
            EntityLivingBase player = uuid == null ? null : this.world.getPlayerEntityByUUID(uuid);
            if (player != null) {
                return player;
            } else {
                if (!world.isRemote) {
                    Entity entity = world.getMinecraftServer().getWorld(this.dimension).getEntityFromUuid(uuid);
                    if (entity instanceof EntityLivingBase) {
                        return (EntityLivingBase) entity;
                    }
                }
            }
        } catch (IllegalArgumentException var2) {
            return null;
        }
        return null;
    }

    protected boolean canDespawn() {
        if (RatsMod.CONFIG_OPTIONS.ratsSpawnLikeMonsters) {
            return !this.isTamed() && !this.isChild();
        } else {
            return super.canDespawn();
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

                if (this.canDespawn() && d3 > 5000.0D) {
                    this.setDead();
                }
                if (this.idleTime > 300 && this.rand.nextInt(400) == 0 && d3 > 256.0D && this.canDespawn()) {
                    this.setDead();
                } else if (d3 < 256.0D) {
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
                    return this.isValidLightLevel() && 0.5F - this.world.getLightBrightness(pos) >= 0.0F && iblockstate.canEntitySpawn(this);
                }
            } else {
                spawnRoll /= 2;
                return (spawnRoll == 0 || rand.nextInt(spawnRoll) == 0) && super.getCanSpawnHere();
            }
        }
        return false;
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
        compound.setInteger("DigCooldown", digCooldown);
        compound.setInteger("BreedCooldown", breedCooldown);
        compound.setInteger("Command", this.getCommandInteger());
        compound.setInteger("ColorVariant", this.getColorVariant());
        compound.setBoolean("Plague", this.hasPlague());
        compound.setBoolean("VisualFlag", this.getVisualFlag());
        compound.setBoolean("Dancing", this.isDancing());
        compound.setBoolean("Toga", this.hasToga());
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
    }

    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        digCooldown = compound.getInteger("DigCooldown");
        breedCooldown = compound.getInteger("BreedCooldown");
        wildTrust = compound.getInteger("WildTrust");
        this.setCommandInteger(compound.getInteger("Command"));
        this.setPlague(compound.getBoolean("Plague"));
        this.setDancing(compound.getBoolean("Dancing"));
        this.setVisualFlag(compound.getBoolean("VisualFlag"));
        this.setToga(compound.getBoolean("Toga"));
        this.setMale(compound.getBoolean("IsMale"));
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
                amount = (amount + 1.0F) / 2.0F;
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

    public RatCommand getCommand() {
        return RatCommand.values()[MathHelper.clamp(getCommandInteger(), 0, RatCommand.values().length - 1)];
    }

    public void setCommand(RatCommand command) {
        setCommandInteger(command.ordinal());
    }

    public EntitySenses getSenses() {
        return this.getEntitySenses();
    }

    public boolean isHoldingFood() {
        return !this.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && RatUtils.isRatFood(this.getHeldItem(EnumHand.MAIN_HAND));
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float) ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
        if (flag) {
            this.applyEnchantments(this, entityIn);
            if (this.hasPlague() && entityIn instanceof EntityLivingBase) {
                ((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(RatsMod.PLAGUE_POTION, 6000));
            }
            if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FERAL_BITE)) {
                entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 5F);
                ((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(RatsMod.PLAGUE_POTION, 600));
                ((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.POISON, 600));
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
        prevInTube = inTube;
        this.setRatStatus(RatStatus.IDLE);
        if (!this.inTube() && this.getNavigator().getPath() != null) {
            if (this.getNavigator().getPath().getFinalPathPoint() != null) {
                BlockPos endPoint = new BlockPos(this.getNavigator().getPath().getFinalPathPoint().x, this.getNavigator().getPath().getFinalPathPoint().y, this.getNavigator().getPath().getFinalPathPoint().z);
                //world.setBlockState(endPoint.down(), Blocks.EMERALD_BLOCK.getDefaultState());
            }
        }
        if (this.inTubeGenerally()) {
            if (tubeCooldown < 40) {
                tubeCooldown++;
            }
        } else {
            if (tubeCooldown > -20) {
                tubeCooldown--;
            }
        }
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
        } else if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FLIGHT)) {
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
        } else {
            this.flyingPitch = 0;
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
                || this.getAnimation() == ANIMATION_EAT || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PLATTER) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER));
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
                this.getHeldItem(EnumHand.MAIN_HAND).shrink(1);
                int healAmount = 1;
                if (this.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemFood) {
                    healAmount = ((ItemFood) this.getHeldItem(EnumHand.MAIN_HAND).getItem()).getHealAmount(this.getHeldItem(EnumHand.MAIN_HAND));
                }
                this.heal(healAmount);
                eatingTicks = 0;
            }
        }
        if (isHoldingFood() && (this.getRNG().nextInt(20) == 0 || eatingTicks > 0) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CHEF) && this.getCommand() != RatCommand.TRANSPORT && this.getCommand() != RatCommand.GATHER && this.getCommand() != RatCommand.HARVEST) {
            if (this.getCommand() != RatCommand.HUNT || this.getHealth() < this.getMaxHealth()) {
                this.setAnimation(ANIMATION_EAT);
                this.setRatStatus(RatStatus.EATING);
            }
        }
        if (this.hasPlague()) {
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
            /*if (this.getCommand() != RatCommand.SIT && this.getCommand() != RatCommand.WANDER) {
                this.setCommand(RatCommand.WANDER);
            }*/
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
        if (!world.isRemote && this.getRatStatus() == RatStatus.IDLE && this.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && this.getAnimation() == NO_ANIMATION && this.getRNG().nextInt(350) == 0) {
            this.setAnimation(this.getRNG().nextBoolean() ? ANIMATION_IDLE_SNIFF : ANIMATION_IDLE_SCRATCH);
        }
        if (!world.isRemote && this.isTamed() && this.getOwner() instanceof EntityIllagerPiper) {
            EntityIllagerPiper piper = (EntityIllagerPiper) this.getOwner();
            if (piper.getAttackTarget() != null) {
                this.setAttackTarget(piper.getAttackTarget());
            }
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_NONBELIEVER)) {
            if (this.getHealth() < this.getMaxHealth() && this.ticksExisted % 30 == 0) {
                this.heal(1.0F);
            }
        }
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PSYCHIC)) {
            if (this.getHealth() < this.getMaxHealth() && this.ticksExisted % 30 == 0) {
                this.heal(1.0F);
            }
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
        if (visualCooldown > 0) {
            visualCooldown--;
        }
        prevUpgrade = this.getUpgradeSlot();
        inTube = inTubeLogic();
        if (this.inTube) {
            tubeTicks++;
        } else {
            tubeTicks = 0;
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (this.isDancing() && this.getAnimation() != this.getDanceAnimation()) {
            this.setAnimation(this.getDanceAnimation());
        }
        if (this.isDancing() && (this.jukeboxPos == null || this.jukeboxPos.distanceSq(this.posX, this.posY, this.posZ) > 15.0D * 15.0D || this.world.getBlockState(this.jukeboxPos).getBlock() != Blocks.JUKEBOX)) {
            this.setDancing(false);
        }
    }

    private boolean shouldSitDuringAnimation() {
        return !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PLATTER) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER);
    }

    public void createBabiesFrom(EntityRat mother, EntityRat father) {
        for (int i = 0; i < 1; i++) {//change later
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
            } else if (father.isTamed()) {
                baby.setTamed(true);
                baby.setOwnerId(father.getOwnerId());
            }
            world.spawnEntity(baby);
        }
    }

    public boolean canBeCollidedWith() {
        return (!this.isRiding() || !(this.getRidingEntity() instanceof EntityPlayer)) && !crafting && !this.inTube();
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
            if (RatUtils.isRatFood(stack)) {
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
            this.getNavigator().clearPath();
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
            } else if (entityIn instanceof EntityLivingBase) {
                if (((EntityLivingBase) entityIn).getActivePotionEffect(MobEffects.POISON) != null) {
                    ((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.POISON, 100));
                }
            }
        }
    }

    public boolean isInRatHole() {
        return RatUtils.isRatHoleInBoundingBox(this.getEntityBoundingBox().grow(0.5D, 0.5D, 0.5D), world);
    }

    private void findDigTarget() {
        if (this.getNavigator() instanceof RatPathNavigate) {
            if (((RatPathNavigate) this.getNavigator()).targetPosition != null) {
                BlockPos target = ((RatPathNavigate) this.getNavigator()).targetPosition;
                if (world.getTileEntity(target) != null) {
                    finalDigPathPoint = ((RatPathNavigate) this.getNavigator()).targetPosition;
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
            this.getNavigator().clearPath();
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
        return world.getBlockState(this.getPosition()).getBlock() instanceof BlockRatCage;
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
        this.attackEntityFrom(DamageSource.IN_WALL, Float.MAX_VALUE);
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
        if (!isDead && this.isTamed() && this.getOwner() != null && this.getOwner() instanceof EntityIllagerPiper) {
            EntityIllagerPiper illagerPiper = (EntityIllagerPiper) this.getOwner();
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
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
            double extraZ = (double) (radius * MathHelper.cos(angle));
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
            this.world.setEntityState(this, (byte) 83);
            this.setTamedBy(player);
            return true;
        }
        if (itemstack.interactWithEntity(player, this, hand)) {
            return true;
        }
        if (!super.processInteract(player, hand)) {
            if (this.isTamed() && !this.isChild() && (isOwner(player) || player.isCreative())) {
                if (itemstack.getItem() == RatsItemRegistry.CHEESE_STICK) {
                    itemstack.getTagCompound().setUniqueId("RatUUID", this.getPersistentID());
                    player.swingArm(hand);
                    player.sendStatusMessage(new TextComponentTranslation("entity.rat.staff.bind", this.getName()), true);
                    return true;
                } else if (!player.isSneaking()) {
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
        setupHarvestAI();
    }

    public void fall(float distance, float damageMultiplier) {
        if (!this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FLIGHT) && !this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER) && !this.inTube()) {
            super.fall(distance, damageMultiplier);
        }
    }

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
        if (!this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FLIGHT) && !this.inTube()) {
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
        } else {
            super.handleStatusUpdate(id);
        }
    }

    protected void playEffect(int type) {
        if (type == 2) {
            for (int j = 0; j < 5; ++j) {
                double d6 = (double) (j) / 5D;
                float f = (this.rand.nextFloat() - 0.5F) * 0.2F;
                float f1 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                float f2 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                double d3 = this.prevPosX + (this.posX - this.prevPosX) * d6 + (rand.nextDouble() - 0.5D) * (double) this.width * 2.0D;
                double d4 = this.prevPosY + (this.posY - this.prevPosY) * d6 + rand.nextDouble() * (double) this.height;
                double d5 = this.prevPosZ + (this.posZ - this.prevPosZ) * d6 + (rand.nextDouble() - 0.5D) * (double) this.width * 2.0D;
                world.spawnParticle(EnumParticleTypes.PORTAL, d3, d4, d5, (double) f, (double) f1, (double) f2);
            }
        } else {
            EnumParticleTypes enumparticletypes = EnumParticleTypes.SMOKE_NORMAL;

            if (type == 1) {
                enumparticletypes = EnumParticleTypes.HEART;
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
        return livingdata;
    }

    @SideOnly(Side.CLIENT)
    public String[] getVariantTexturePaths() {
        return RAT_TEXTURES;
    }

    @SideOnly(Side.CLIENT)
    public String getRatTexture() {
        return RAT_TEXTURES[MathHelper.clamp(this.getColorVariant(), 0, RAT_TEXTURES.length - 1)];
    }

    public boolean shouldHunt() {
        return this.getCommand() == RatCommand.HUNT && this.getHealth() >= this.getMaxHealth() / 2F || !this.isTamed() && this.hasPlague();
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
        return this.hasPlague() || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_NONBELIEVER) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_RATINATOR) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ENDER);
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LOOT;
    }

    protected SoundEvent getAmbientSound() {
        if (this.hasPlague() && this.getAttackTarget() != null) {
            return RatsSoundRegistry.RAT_PLAGUE;
        }
        if (!this.hasPlague() && this.getHealth() < this.getMaxHealth() / 2D || this.isChild()) {
            return RatsSoundRegistry.RAT_IDLE;
        }
        return super.getAmbientSound();
    }

    protected SoundEvent getDeathSound() {
        return RatsSoundRegistry.RAT_DIE;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return RatsSoundRegistry.RAT_HURT;
    }

    public boolean onHearFlute(EntityPlayer player, RatCommand ratCommand) {
        if (this.isTamed() && this.isOwner(player) && !this.isChild()) {
            this.setCommand(ratCommand);
            return true;
        }
        return false;
    }

    private boolean doBreedingSurvey() {
        int ratCount = 0;
        double dist = 1.5F;
        for (EntityRat rat : world.getEntitiesWithinAABB(EntityRat.class, new AxisAlignedBB(this.posX - dist, this.posY - dist, this.posZ - dist, this.posX + dist, this.posY + dist, this.posZ + dist))) {
            if (rat.isInCage()) {
                ratCount++;
            }
        }
        return ratCount < RatsMod.CONFIG_OPTIONS.ratCageCramming;
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
        BlockPos pos = new BlockPos(this);
        IBlockState state = world.getBlockState(pos);
        boolean above = world.getBlockState(pos.up()).getBlock() instanceof BlockRatTube;
        if (state.getBlock() instanceof BlockRatTube) {
            List<AxisAlignedBB> aabbs = ((BlockRatTube) state.getBlock()).compileAABBList(world, pos, state);
            for (AxisAlignedBB box : aabbs) {
                if (box.offset(pos).contains(this.getPositionVector().add(0, this.height / 2, 0)) || box.offset(pos).contains(this.getPositionVector().add(0, 0, 0)) && above) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean inTubeGenerally() {
        BlockPos pos = new BlockPos(this);
        IBlockState state = world.getBlockState(pos);
        boolean above = world.getBlockState(pos.up()).getBlock() instanceof BlockRatTube;
        return state.getBlock() instanceof BlockRatTube || above;
    }


    public boolean isAIDisabled() {
        return super.isAIDisabled();
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
        if (stack.getItem() instanceof ItemRatCombinedUpgrade) {
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

    private void onUpgradeChanged() {
        setupHarvestAI();
        boolean flagHealth = false;
        boolean flagArmor = false;
        boolean flagAttack = false;
        boolean flagSpeed = false;
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
        if (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_RATINATOR)) {
            tryIncreaseStat(SharedMonsterAttributes.MAX_HEALTH, 30D);
            tryIncreaseStat(SharedMonsterAttributes.ARMOR, 80D);
            flagHealth = true;
            flagArmor = true;
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
        return super.isPotionApplicable(potioneffectIn);
    }

    public boolean isEntityInvulnerable(DamageSource source) {
        if (source.isFireDamage() && (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ASBESTOS) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION))) {
            return true;
        }
        if ((source.isMagicDamage() || source == DamageSource.WITHER) && (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_POISON) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION))) {
            return true;
        }
        if ((source == DamageSource.IN_WALL || source == DamageSource.DROWN) && (this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_POISON) || this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION))) {
            return true;
        }
        if (source == DamageSource.FALL && this.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION)) {
            return true;
        }
        return super.isEntityInvulnerable(source);
    }

    protected void setupTamedAI() {
        Arrays.fill(this.inventoryArmorDropChances, 1.0F);
        Arrays.fill(this.inventoryHandsDropChances, 1.0F);
    }

    public boolean shouldBeSuckedIntoTube() {
        if (tubeTicks < 15 && !prevInTube && !inTube()) {
            return tubeCooldown < 0;
        }
        return false;
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
}
