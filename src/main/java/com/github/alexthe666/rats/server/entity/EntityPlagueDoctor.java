package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.ai.PlagueDoctorAIFollowGolem;
import com.github.alexthe666.rats.server.entity.ai.PlagueDoctorAILookAtTradePlayer;
import com.github.alexthe666.rats.server.entity.ai.PlagueDoctorAITradePlayer;
import com.github.alexthe666.rats.server.entity.ai.PlagueDoctorAIVillagerInteract;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.world.village.RatsVillageRegistry;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Set;

import static com.github.alexthe666.rats.server.advancements.RatsAdvancementRegistry.PLAGUE_DOCTOR_TRIGGER;

public class EntityPlagueDoctor extends EntityAgeable implements IRangedAttackMob, INpc, IMerchant {

    public static final ResourceLocation LOOT = LootTableList.register(new ResourceLocation("rats", "plague_doctor"));
    private static final Set<Item> TEMPTATION_ITEMS = Sets.newHashSet(Item.getItemFromBlock(Blocks.RED_FLOWER));
    private static final com.google.common.base.Predicate<Entity> PLAGUE_PREDICATE = new com.google.common.base.Predicate<Entity>() {
        public boolean apply(@Nullable Entity entity) {
            return entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPotionActive(RatsMod.PLAGUE_POTION) || entity instanceof EntityRat && ((EntityRat) entity).hasPlague() || entity instanceof IPlagueLegion;
        }
    };
    private final InventoryBasic villagerInventory;
    Village village;
    private boolean isMating;
    private boolean isPlaying;
    @Nullable
    private PlayerEntity buyingPlayer;
    @Nullable
    private MerchantRecipeList buyingList;
    private int timeUntilReset;
    private boolean needsInitilization;
    private boolean isWillingToMate;
    private int wealth;
    private java.util.UUID lastBuyingPlayer;
    private int careerId;
    private int careerLevel;
    private boolean isLookingForHome;
    private boolean areAdditionalTasksSet;
    private int randomTickDivider;

    public EntityPlagueDoctor(World worldIn) {
        super(worldIn);
        this.villagerInventory = new InventoryBasic("Items", false, 8);
        this.setSize(0.6F, 1.95F);
        ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
        this.setCanPickUpLoot(true);
    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new PlagueDoctorAITradePlayer(this));
        this.tasks.addTask(1, new PlagueDoctorAILookAtTradePlayer(this));
        this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0D, 60, 10.0F));
        this.tasks.addTask(2, new EntityAIMoveIndoors(this));
        this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(4, new EntityAITempt(this, 1.2D, false, TEMPTATION_ITEMS));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
        this.tasks.addTask(7, new PlagueDoctorAIFollowGolem(this));
        this.tasks.addTask(9, new EntityAIWatchClosest2(this, PlayerEntity.class, 3.0F, 1.0F));
        this.tasks.addTask(9, new PlagueDoctorAIVillagerInteract(this));
        this.tasks.addTask(9, new EntityAIWanderAvoidWater(this, 0.6D));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityZombieVillager.class, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 0, false, false, PLAGUE_PREDICATE));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (this.getAttackTarget() != null) {
            if (this.getAttackTarget() instanceof EntityRat) {
                if (!((EntityRat) this.getAttackTarget()).hasPlague()) {
                    this.setAttackTarget(null);
                }
            } else if (this.getAttackTarget() instanceof EntityZombieVillager) {
                if (((EntityZombieVillager) this.getAttackTarget()).isConverting()) {
                    this.setAttackTarget(null);
                }
            } else if (!this.getAttackTarget().isPotionActive(RatsMod.PLAGUE_POTION) && !(this.getAttackTarget() instanceof IPlagueLegion)) {
                this.setAttackTarget(null);
            }
        }
    }

    public boolean isPotionApplicable(PotionEffect potioneffectIn) {
        return potioneffectIn.getPotion() != RatsMod.PLAGUE_POTION && super.isPotionApplicable(potioneffectIn);
    }

    protected void updateAITasks() {
        if (--this.randomTickDivider <= 0) {
            BlockPos blockpos = new BlockPos(this);
            this.world.getVillageCollection().addToVillagerPositionList(blockpos);
            this.randomTickDivider = 70 + this.rand.nextInt(50);
            this.village = this.world.getVillageCollection().getNearestVillage(blockpos, 32);

            if (this.village == null) {
                this.detachHome();
            } else {
                BlockPos blockpos1 = this.village.getCenter();
                this.setHomePosAndDistance(blockpos1, this.village.getVillageRadius());

                if (this.isLookingForHome) {
                    this.isLookingForHome = false;
                    this.village.setDefaultPlayerReputation(5);
                }
            }
        }

        if (!this.isTrading() && this.timeUntilReset > 0) {
            --this.timeUntilReset;

            if (this.timeUntilReset <= 0) {
                if (this.needsInitilization) {
                    for (MerchantRecipe merchantrecipe : this.buyingList) {
                        if (merchantrecipe.isRecipeDisabled()) {
                            merchantrecipe.increaseMaxTradeUses(this.rand.nextInt(6) + this.rand.nextInt(6) + 2);
                        }
                    }

                    this.populateBuyingList();
                    this.needsInitilization = false;

                    if (this.village != null && this.lastBuyingPlayer != null) {
                        this.world.setEntityState(this, (byte) 14);
                        this.village.modifyPlayerReputation(this.lastBuyingPlayer, 1);
                    }
                }

                this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 0));
            }
        }

        super.updateAITasks();
    }

    public boolean processInteract(PlayerEntity player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        boolean flag = itemstack.getItem() == Items.NAME_TAG;
        if (!world.isRemote) {
            PLAGUE_DOCTOR_TRIGGER.trigger((ServerPlayerEntity) player, this);
        }
        if (flag) {
            itemstack.interactWithEntity(player, this, hand);
            return true;
        } else if (!this.holdingSpawnEggOfClass(itemstack, this.getClass()) && this.isEntityAlive() && !this.isTrading() && !this.isChild() && !player.isSneaking()) {
            if (this.buyingList == null) {
                this.populateBuyingList();
            }

            if (hand == EnumHand.MAIN_HAND) {
                player.addStat(StatList.TALKED_TO_VILLAGER);
            }

            if (!this.world.isRemote && !this.buyingList.isEmpty()) {
                this.setCustomer(player);
                player.displayVillagerTradeGui(this);
            } else if (this.buyingList.isEmpty()) {
                return super.processInteract(player, hand);
            }

            return true;
        } else {
            return super.processInteract(player, hand);
        }
    }

    public void writeEntityToNBT(CompoundNBT compound) {
        super.writeEntityToNBT(compound);
        compound.setInt("Riches", this.wealth);
        compound.setInt("Career", this.careerId);
        compound.setInt("CareerLevel", this.careerLevel);
        compound.setBoolean("Willing", this.isWillingToMate);

        if (this.buyingList != null) {
            compound.setTag("Offers", this.buyingList.getRecipiesAsTags());
        }

        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i) {
            ItemStack itemstack = this.villagerInventory.getStackInSlot(i);

            if (!itemstack.isEmpty()) {
                nbttaglist.appendTag(itemstack.writeToNBT(new CompoundNBT()));
            }
        }

        compound.setTag("Inventory", nbttaglist);
    }

    public void readEntityFromNBT(CompoundNBT compound) {
        super.readEntityFromNBT(compound);
        this.wealth = compound.getInt("Riches");
        this.careerId = compound.getInt("Career");
        this.careerLevel = compound.getInt("CareerLevel");
        this.isWillingToMate = compound.getBoolean("Willing");

        if (compound.hasKey("Offers", 10)) {
            CompoundNBT CompoundNBT = compound.getCompoundTag("Offers");
            this.buyingList = new MerchantRecipeList(CompoundNBT);
        }

        NBTTagList nbttaglist = compound.getTagList("Inventory", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            ItemStack itemstack = new ItemStack(nbttaglist.getCompoundTagAt(i));

            if (!itemstack.isEmpty()) {
                this.villagerInventory.addItem(itemstack);
            }
        }

        this.setCanPickUpLoot(true);
    }

    protected boolean canDespawn() {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return this.isTrading() ? SoundEvents.ENTITY_VILLAGER_TRADING : SoundEvents.ENTITY_VILLAGER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VILLAGER_DEATH;
    }

    public boolean isMating() {
        return this.isMating;
    }

    public void setMating(boolean mating) {
        this.isMating = mating;
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public void setPlaying(boolean playing) {
        this.isPlaying = playing;
    }

    /**
     * Hint to AI tasks that we were attacked by the passed EntityLivingBase and should retaliate. Is not guaranteed to
     * change our actual active target (for example if we are currently busy attacking someone else)
     */
    public void setRevengeTarget(@Nullable EntityLivingBase livingBase) {
        super.setRevengeTarget(livingBase);

        if (this.village != null && livingBase != null) {
            this.village.addOrRenewAgressor(livingBase);

            if (livingBase instanceof PlayerEntity) {
                int i = -1;

                if (this.isChild()) {
                    i = -3;
                }

                this.village.modifyPlayerReputation(livingBase.getUniqueID(), i);

                if (this.isEntityAlive()) {
                    this.world.setEntityState(this, (byte) 13);
                }
            }
        }
    }

    public void onDeath(DamageSource cause) {
        if (this.village != null) {
            Entity entity = cause.getTrueSource();

            if (entity != null) {
                if (entity instanceof PlayerEntity) {
                    this.village.modifyPlayerReputation(entity.getUniqueID(), -2);
                } else if (entity instanceof IMob) {
                    this.village.endMatingSeason();
                }
            } else {
                PlayerEntity PlayerEntity = this.world.getClosestPlayerToEntity(this, 16.0D);

                if (PlayerEntity != null) {
                    this.village.endMatingSeason();
                }
            }
        }

        super.onDeath(cause);
    }

    @Nullable
    public PlayerEntity getCustomer() {
        return this.buyingPlayer;
    }

    public void setCustomer(@Nullable PlayerEntity player) {
        this.buyingPlayer = player;
    }

    public boolean isTrading() {
        return this.buyingPlayer != null;
    }

    public boolean getIsWillingToMate(boolean updateFirst) {
        if (!this.isWillingToMate && updateFirst && this.hasEnoughFoodToBreed()) {
            boolean flag = false;

            for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i) {
                ItemStack itemstack = this.villagerInventory.getStackInSlot(i);

                if (!itemstack.isEmpty()) {
                    if (itemstack.getItem() == Items.BREAD && itemstack.getCount() >= 3) {
                        flag = true;
                        this.villagerInventory.decrStackSize(i, 3);
                    } else if ((itemstack.getItem() == Items.POTATO || itemstack.getItem() == Items.CARROT) && itemstack.getCount() >= 12) {
                        flag = true;
                        this.villagerInventory.decrStackSize(i, 12);
                    }
                }

                if (flag) {
                    this.world.setEntityState(this, (byte) 18);
                    this.isWillingToMate = true;
                    break;
                }
            }
        }

        return this.isWillingToMate;
    }

    public void setIsWillingToMate(boolean isWillingToMate) {
        this.isWillingToMate = isWillingToMate;
    }

    public void useRecipe(MerchantRecipe recipe) {
        recipe.incrementToolUses();
        this.livingSoundTime = -this.getTalkInterval();
        this.playSound(SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
        int i = 3 + this.rand.nextInt(4);

        if (recipe.getToolUses() == 1 || this.rand.nextInt(5) == 0) {
            this.timeUntilReset = 40;
            this.needsInitilization = true;
            this.isWillingToMate = true;

            if (this.buyingPlayer != null) {
                this.lastBuyingPlayer = this.buyingPlayer.getUniqueID();
            } else {
                this.lastBuyingPlayer = null;
            }

            i += 5;
        }

        if (recipe.getItemToBuy().getItem() == Items.EMERALD) {
            this.wealth += recipe.getItemToBuy().getCount();
        }

        if (recipe.getRewardsExp()) {
            this.world.addEntity(new EntityXPOrb(this.world, this.posX, this.posY + 0.5D, this.posZ, i));
        }

    }

    /**
     * Notifies the merchant of a possible merchantrecipe being fulfilled or not. Usually, this is just a sound byte
     * being played depending if the suggested itemstack is not null.
     */
    public void verifySellingItem(ItemStack stack) {
        if (!this.world.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20) {
            this.livingSoundTime = -this.getTalkInterval();
            this.playSound(stack.isEmpty() ? SoundEvents.ENTITY_VILLAGER_NO : SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
        }
    }

    @Nullable
    public MerchantRecipeList getRecipes(PlayerEntity player) {
        if (this.buyingList == null) {
            this.populateBuyingList();
        }

        return this.buyingList;
    }

    private void populateBuyingList() {
        if (this.careerId != 0 && this.careerLevel != 0) {
            ++this.careerLevel;
        } else {
            this.careerId = this.getProfessionForge().getRandomCareer(this.rand) + 1;
            this.careerLevel = 1;
        }

        if (this.buyingList == null) {
            this.buyingList = new MerchantRecipeList();
        }

        int i = this.careerId - 1;
        int j = this.careerLevel - 1;
        java.util.List<EntityVillager.ITradeList> trades = this.getProfessionForge().getCareer(i).getTrades(j);

        if (trades != null) {
            for (EntityVillager.ITradeList entityvillager$itradelist : trades) {
                entityvillager$itradelist.addMerchantRecipe(this, this.buyingList, this.rand);
            }
        }
    }

    private VillagerRegistry.VillagerProfession getProfessionForge() {
        return RatsVillageRegistry.PLAGUE_DOCTOR;
    }

    @SideOnly(Side.CLIENT)
    public void setRecipes(@Nullable MerchantRecipeList recipeList) {
    }

    public World getWorld() {
        return this.world;
    }

    public BlockPos getPos() {
        return new BlockPos(this);
    }

    public float getEyeHeight() {
        return this.isChild() ? 0.81F : 1.62F;
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 12) {
            this.spawnParticles(EnumParticleTypes.HEART);
        } else if (id == 13) {
            this.spawnParticles(EnumParticleTypes.VILLAGER_ANGRY);
        } else if (id == 14) {
            this.spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
        } else {
            super.handleStatusUpdate(id);
        }
    }

    @SideOnly(Side.CLIENT)
    private void spawnParticles(EnumParticleTypes particleType) {
        for (int i = 0; i < 5; ++i) {
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            this.world.spawnParticle(particleType, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 1.0D + (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
        }
    }

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory.
     * <p>
     * The livingdata parameter is used to pass data between all instances during a pack spawn. It will be null on the
     * first call. Subclasses may check if it's null, and then create a new one and return it if so, initializing all
     * entities in the pack with the contained data.
     *
     * @param difficulty The current local difficulty
     * @param livingdata Shared spawn data. Will usually be null. (See return value for more information)
     * @return The IEntityLivingData to pass to this method for other instances of this entity class within the same
     * pack
     */
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        return this.finalizeMobSpawn(difficulty, livingdata, true);
    }

    public IEntityLivingData finalizeMobSpawn(DifficultyInstance p_190672_1_, @Nullable IEntityLivingData p_190672_2_, boolean p_190672_3_) {
        p_190672_2_ = super.onInitialSpawn(p_190672_1_, p_190672_2_);
        this.populateBuyingList();
        return p_190672_2_;
    }

    public void setLookingForHome() {
        this.isLookingForHome = true;
    }

    public EntityPlagueDoctor createChild(EntityAgeable ageable) {
        EntityPlagueDoctor entityvillager = new EntityPlagueDoctor(this.world);
        entityvillager.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityvillager)), null);
        return entityvillager;
    }

    public boolean canBeLeashedTo(PlayerEntity player) {
        return true;
    }

    public InventoryBasic getVillagerInventory() {
        return this.villagerInventory;
    }

    protected void updateEquipmentIfNeeded(ItemEntity itemEntity) {
        ItemStack itemstack = itemEntity.getItem();
        Item item = itemstack.getItem();

        if (this.canVillagerPickupItem(item)) {
            ItemStack itemstack1 = this.villagerInventory.addItem(itemstack);

            if (itemstack1.isEmpty()) {
                itemEntity.setDead();
            } else {
                itemstack.setCount(itemstack1.getCount());
            }
        }
    }

    private boolean canVillagerPickupItem(Item itemIn) {
        return itemIn == Items.BREAD || itemIn == Items.POTATO || itemIn == Items.CARROT || itemIn == Items.WHEAT || itemIn == Items.WHEAT_SEEDS || itemIn == Items.BEETROOT || itemIn == Items.BEETROOT_SEEDS;
    }

    public boolean hasEnoughFoodToBreed() {
        return this.hasEnoughItems(1);
    }

    /**
     * Used by {@link net.minecraft.entity.ai.EntityAIVillagerInteract EntityAIVillagerInteract} to check if the
     * villager can give some items from an inventory to another villager.
     */
    public boolean canAbondonItems() {
        return this.hasEnoughItems(2);
    }

    public boolean wantsMoreFood() {
        return !this.hasEnoughItems(5);
    }

    private boolean hasEnoughItems(int multiplier) {
        for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i) {
            ItemStack itemstack = this.villagerInventory.getStackInSlot(i);

            if (!itemstack.isEmpty()) {
                if (itemstack.getItem() == Items.BREAD && itemstack.getCount() >= 3 * multiplier || itemstack.getItem() == Items.POTATO && itemstack.getCount() >= 12 * multiplier || itemstack.getItem() == Items.CARROT && itemstack.getCount() >= 12 * multiplier || itemstack.getItem() == Items.BEETROOT && itemstack.getCount() >= 12 * multiplier) {
                    return true;
                }

                if (itemstack.getItem() == Items.WHEAT && itemstack.getCount() >= 9 * multiplier) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns true if villager has seeds, potatoes or carrots in inventory
     */
    public boolean isFarmItemInInventory() {
        for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i) {
            ItemStack itemstack = this.villagerInventory.getStackInSlot(i);

            if (!itemstack.isEmpty() && (itemstack.getItem() == Items.WHEAT_SEEDS || itemstack.getItem() == Items.POTATO || itemstack.getItem() == Items.CARROT || itemstack.getItem() == Items.BEETROOT_SEEDS)) {
                return true;
            }
        }

        return false;
    }

    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
        if (super.replaceItemInInventory(inventorySlot, itemStackIn)) {
            return true;
        } else {
            int i = inventorySlot - 300;

            if (i >= 0 && i < this.villagerInventory.getSizeInventory()) {
                this.villagerInventory.setInventorySlotContents(i, itemStackIn);
                return true;
            } else {
                return false;
            }
        }
    }


    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        double d0 = target.posY + (double) target.getEyeHeight() - 1.100000023841858D;
        double d1 = target.posX + target.motionX - this.posX;
        double d2 = d0 - this.posY;
        double d3 = target.posZ + target.motionZ - this.posZ;
        float f = MathHelper.sqrt(d1 * d1 + d3 * d3);
        EntityPurifyingLiquid entitypotion = new EntityPurifyingLiquid(this.world, this, new ItemStack(RatsItemRegistry.PURIFYING_LIQUID));
        entitypotion.rotationPitch -= -20.0F;
        entitypotion.shoot(d1, d2 + (double) (f * 0.2F), d3, 0.75F, 8.0F);
        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_THROW, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
        this.world.addEntity(entitypotion);
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LOOT;
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {

    }

    public void onStruckByLightning(EntityLightningBolt lightningBolt) {
        if (!this.world.isRemote && !this.isDead) {
            EntityBlackDeath entitywitch = new EntityBlackDeath(this.world);
            entitywitch.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            entitywitch.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitywitch)), null);
            entitywitch.setNoAI(this.isAIDisabled());
            if (this.hasCustomName()) {
                entitywitch.setCustomNameTag(this.getCustomNameTag());
                entitywitch.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
            }
            this.world.addEntity(entitywitch);
            this.setDead();
        }
    }
}
