package com.github.alexthe666.rats.server.events;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.*;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.message.MessageCheeseStaffRat;
import com.github.alexthe666.rats.server.message.MessageRatDismount;
import com.github.alexthe666.rats.server.message.MessageSwingArm;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = RatsMod.MODID)
public class CommonEvents {

    @SubscribeEvent
    public static void onPlayerInteractWithEntity(PlayerInteractEvent.EntityInteract event) {
        if (RatUtils.isPredator(event.getTarget()) && event.getTarget() instanceof AnimalEntity) {
            AnimalEntity ocelot = (AnimalEntity) event.getTarget();
            Item heldItem = event.getPlayer().getHeldItem(event.getHand()).getItem();
            Random random = event.getWorld().rand;
            if (ocelot.getHealth() < ocelot.getMaxHealth()) {
                if (heldItem == RatsItemRegistry.RAW_RAT) {
                    ocelot.heal(4);
                    event.getWorld().playSound(null, ocelot.getPosX(), ocelot.getPosY(), ocelot.getPosZ(), SoundEvents.ENTITY_LLAMA_EAT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    event.getWorld().playSound(null, ocelot.getPosX(), ocelot.getPosY(), ocelot.getPosZ(), SoundEvents.ENTITY_CAT_AMBIENT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    for (int i = 0; i < 3; i++) {
                        event.getWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, ocelot.getPosX() + random.nextDouble() - random.nextDouble(), ocelot.getPosY() + 0.5 + random.nextDouble() - random.nextDouble(), ocelot.getPosZ() + random.nextDouble() - random.nextDouble(), 0, 0, 0);
                    }
                }
                if (heldItem == RatsItemRegistry.COOKED_RAT) {
                    ocelot.heal(8);
                    event.getWorld().playSound(null, ocelot.getPosX(), ocelot.getPosY(), ocelot.getPosZ(), SoundEvents.ENTITY_LLAMA_EAT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    event.getWorld().playSound(null, ocelot.getPosX(), ocelot.getPosY(), ocelot.getPosZ(), SoundEvents.ENTITY_CAT_AMBIENT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    for (int i = 0; i < 3; i++) {
                        event.getWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, ocelot.getPosX() + random.nextDouble() - random.nextDouble(), ocelot.getPosY() + 0.5 + random.nextDouble() - random.nextDouble(), ocelot.getPosZ() + random.nextDouble() - random.nextDouble(), 0, 0, 0);
                    }
                }
            }
        }
        if(event.getTarget() instanceof SheepEntity && event.getPlayer().getHeldItem(event.getHand()).getItem() == Item.getItemFromBlock(RatsBlockRegistry.DYE_SPONGE)){
            SheepEntity sheep = (SheepEntity)event.getTarget();
            sheep.setFleeceColor(DyeColor.WHITE);
            for (int i = 0; i < 8; i++) {
                double d0 = sheep.getRNG().nextGaussian() * 0.02D;
                double d1 = sheep.getRNG().nextGaussian() * 0.02D;
                double d2 = sheep.getRNG().nextGaussian() * 0.02D;
                sheep.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(Item.getItemFromBlock(RatsBlockRegistry.DYE_SPONGE))), sheep.getPosX() + (double) (sheep.getRNG().nextFloat() * sheep.getWidth() * 2.0F) - (double) sheep.getWidth(), sheep.getPosY() + (double) (sheep.getRNG().nextFloat() * sheep.getHeight() * 2.0F) - (double) sheep.getHeight(), sheep.getPosZ() + (double) (sheep.getRNG().nextFloat() * sheep.getWidth() * 2.0F) - (double) sheep.getWidth(), d0, d1, d2);
            }
            sheep.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);

        }
        if (event.getTarget() instanceof AbstractVillagerEntity && !(event.getTarget() instanceof EntityPlagueDoctor)) {
            ItemStack heldItem = event.getPlayer().getHeldItem(event.getHand());
            if (heldItem.getItem() == RatsItemRegistry.PLAGUE_DOCTORATE && !((AbstractVillagerEntity) event.getTarget()).isChild()) {
                AbstractVillagerEntity villager = (AbstractVillagerEntity) event.getTarget();
                EntityPlagueDoctor doctor = new EntityPlagueDoctor(RatsEntityRegistry.PLAGUE_DOCTOR, event.getWorld());
                doctor.copyLocationAndAnglesFrom(villager);
                villager.remove();
                doctor.setWillDespawn(false);
                doctor.onInitialSpawn(event.getWorld(), event.getWorld().getDifficultyForLocation(event.getPos()), SpawnReason.MOB_SUMMONED, null, null);
                if (!event.getWorld().isRemote) {
                    event.getWorld().addEntity(doctor);
                }
                doctor.setNoAI(villager.isAIDisabled());
                if (villager.hasCustomName()) {
                    doctor.setCustomName(villager.getCustomName());
                }
                event.getPlayer().swingArm(event.getHand());
                if (!event.getPlayer().isCreative()) {
                    heldItem.shrink(1);
                }
            }
        }
        if (event.getTarget() instanceof EntityPlagueDoctor) {
            ItemStack heldItem = event.getPlayer().getHeldItem(event.getHand());
            if (heldItem.getItem() == RatsItemRegistry.PLAGUE_TOME && !((EntityPlagueDoctor) event.getTarget()).isChild()) {
                EntityBlackDeath entitywitch = new EntityBlackDeath(RatsEntityRegistry.BLACK_DEATH, event.getWorld());
                entitywitch.setLocationAndAngles(event.getTarget().getPosX(), event.getTarget().getPosY(), event.getTarget().getPosZ(), event.getTarget().rotationYaw, event.getTarget().rotationPitch);
                entitywitch.onInitialSpawn(event.getTarget().getEntityWorld(), event.getTarget().world.getDifficultyForLocation(new BlockPos(entitywitch)), SpawnReason.NATURAL, null, null);
                if (event.getTarget().hasCustomName()) {
                    entitywitch.setCustomName(event.getTarget().getCustomName());
                }
                event.getTarget().world.addEntity(entitywitch);
                event.getTarget().remove();
            }
        }
        if (event.getPlayer().isPotionActive(RatsMod.PLAGUE_POTION) && RatConfig.plagueSpread && !(event.getTarget() instanceof EntityRat)) {
            if (event.getTarget() instanceof LivingEntity && !((LivingEntity) event.getTarget()).isPotionActive(RatsMod.PLAGUE_POTION)) {
                ((LivingEntity) event.getTarget()).addPotionEffect(new EffectInstance(RatsMod.PLAGUE_POTION, 6000));
                event.getTarget().playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1.0F, 1.0F);
            }
        }
    }

    public static int getProtectorCount(LivingEntity entity){
        int protectors = 0;
        if(entity.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == RatsItemRegistry.RATLANTIS_HELMET){
            protectors++;
        }
        if(entity.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() == RatsItemRegistry.RATLANTIS_CHESTPLATE){
            protectors++;
        }
        if(entity.getItemStackFromSlot(EquipmentSlotType.LEGS).getItem() == RatsItemRegistry.RATLANTIS_LEGGINGS){
            protectors++;
        }
        if(entity.getItemStackFromSlot(EquipmentSlotType.FEET).getItem() == RatsItemRegistry.RATLANTIS_BOOTS){
            protectors++;
        }
        return protectors;
    }

    @SubscribeEvent
    public static void onHitEntity(LivingAttackEvent event) {
        if (event.getSource().getImmediateSource() instanceof LivingEntity && RatConfig.plagueSpread) {
            LivingEntity attacker = (LivingEntity) event.getSource().getImmediateSource();
            if (attacker.isPotionActive(RatsMod.PLAGUE_POTION) && !(event.getEntityLiving() instanceof EntityRat)) {
                if (!event.getEntityLiving().isPotionActive(RatsMod.PLAGUE_POTION)) {
                    event.getEntityLiving().addPotionEffect(new EffectInstance(RatsMod.PLAGUE_POTION, 6000));
                    event.getEntityLiving().playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1.0F, 1.0F);
                }
            }
        }
        int protectors = getProtectorCount(event.getEntityLiving());
        if(protectors > 0){
            if(event.getSource() != null && event.getSource().getTrueSource() != null){
                Entity trueSource = event.getSource().getTrueSource();
                if(trueSource.getDistance(event.getEntityLiving()) < 6D){
                    trueSource.attackEntityFrom(DamageSource.MAGIC, 2.0F * protectors);
                    Vector3d vec3d = trueSource.getMotion();
                    double strength = 0.3D * protectors;
                    Vector3d vec3d1 = (new Vector3d(event.getEntityLiving().getPosX() - trueSource.getPosX(), 0.0D, event.getEntityLiving().getPosZ() - trueSource.getPosZ())).normalize().scale((double)strength);
                    trueSource.setMotion(vec3d.x / 2.0D - vec3d1.x, trueSource.onGround ? Math.min(0.4D, vec3d.y / 2.0D + (double)strength) : vec3d.y, vec3d.z / 2.0D - vec3d1.z);

                }

            }
        }
    }

    @SubscribeEvent
    public static void onPlayerPunch(AttackEntityEvent event) {
        ItemStack itemstack = event.getPlayer().getHeldItem(Hand.MAIN_HAND);
        //TinkersCompatBridge.onPlayerSwing(event.getPlayer(), itemstack);
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() != null && event.getEntity() instanceof IronGolemEntity && RatConfig.golemsTargetRats) {
            IronGolemEntity golem = (IronGolemEntity) event.getEntity();
            golem.targetSelector.addGoal(4, new NearestAttackableTargetGoal(golem, EntityRat.class, 10, false, false, RatUtils.UNTAMED_RAT_SELECTOR));
        }
        if (event.getEntity() != null && RatUtils.isPredator(event.getEntity()) && event.getEntity() instanceof AnimalEntity) {
            AnimalEntity animal = (AnimalEntity) event.getEntity();
            animal.targetSelector.addGoal(5, new NearestAttackableTargetGoal(animal, EntityRat.class, true));
        }
        if (event.getEntity() != null && event.getEntity() instanceof HuskEntity) {
            if (((HuskEntity) event.getEntity()).getRNG().nextFloat() < RatConfig.archeologistHatSpawnRate) {
                event.getEntity().setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(RatsItemRegistry.ARCHEOLOGIST_HAT));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        ItemStack itemstack = event.getPlayer().getHeldItem(Hand.MAIN_HAND);
        /*if (TinkersCompatBridge.onPlayerSwing(event.getPlayer(), itemstack)) {
        }*/
        RatsCustomEvents.onPlayerSwing(event.getPlayer(), itemstack);
        RatsMod.NETWORK_WRAPPER.sendToServer(new MessageSwingArm());
        if (event.getPlayer().isSneaking() && !event.getPlayer().getPassengers().isEmpty()) {
            for (Entity passenger : event.getPlayer().getPassengers()) {
                if (passenger instanceof EntityRat) {
                    passenger.stopRiding();
                    passenger.setPosition(event.getPlayer().getPosX(), event.getPlayer().getPosY(), event.getPlayer().getPosZ());
                    RatsMod.NETWORK_WRAPPER.sendToServer(new MessageRatDismount(passenger.getEntityId()));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        ItemStack itemstack = event.getPlayer().getHeldItem(Hand.MAIN_HAND);
        //TinkersCompatBridge.onPlayerSwing(event.getPlayer(), itemstack);
    }

    @SubscribeEvent
    public static void onGatherCollisionBoxes(GetCollisionBoxesEvent event) {
        if (event.getEntity() instanceof EntityRat) {
            event.getCollisionBoxesList().removeIf(aabb -> ((EntityRat) event.getEntity()).canPhaseThroughBlock(event.getWorld(), new BlockPos(aabb.minX, aabb.minY, aabb.minZ)));
        }
    }

    @SubscribeEvent
    public static void onDrops(LivingDropsEvent event) {
        if (event.getEntityLiving() instanceof EntityIllagerPiper && event.getSource().getTrueSource() instanceof PlayerEntity && event.getEntityLiving().world.rand.nextFloat() < RatConfig.piperHatDropRate + (RatConfig.piperHatDropRate / 2) * event.getLootingLevel()) {
            event.getDrops().add(new ItemEntity(event.getEntity().world, event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY(), event.getEntityLiving().getPosZ(), new ItemStack(RatsItemRegistry.PIPER_HAT)));
        }
        if (event.getEntityLiving() instanceof CreeperEntity && ((CreeperEntity) event.getEntityLiving()).func_225509_J__()) {
            event.getDrops().add(new ItemEntity(event.getEntity().world, event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY(), event.getEntityLiving().getPosZ(), new ItemStack(RatsItemRegistry.CHARGED_CREEPER_CHUNK, event.getLootingLevel() + 1 + event.getEntityLiving().world.rand.nextInt(2))));
        }
        if (event.getSource().getTrueSource() instanceof EntityRat && ((EntityRat) event.getSource().getTrueSource()).hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ARISTOCRAT)) {
            event.getDrops().add(new ItemEntity(event.getEntity().world, event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY(), event.getEntityLiving().getPosZ(), new ItemStack(RatsItemRegistry.TINY_COIN)));
        }
    }

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        if (event.getEntityLiving().getActivePotionEffect(RatsMod.PLAGUE_POTION) != null) {
            EffectInstance effect = event.getEntityLiving().getActivePotionEffect(RatsMod.PLAGUE_POTION);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            AxisAlignedBB axisalignedbb = event.getEntityLiving().getBoundingBox().grow(RatConfig.ratVoodooDistance, RatConfig.ratVoodooDistance, RatConfig.ratVoodooDistance);
            List<EntityRat> list = event.getEntityLiving().world.getEntitiesWithinAABB(EntityRat.class, axisalignedbb);
            List<EntityRat> voodooRats = new ArrayList<>();
            int capturedRat = 0;
            if (!list.isEmpty()) {
                for (EntityRat rat : list) {
                    if (rat.isTamed() && (rat.isOwner(event.getEntityLiving())) && rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_VOODOO)) {
                        voodooRats.add(rat);
                    }
                }
                if (!voodooRats.isEmpty()) {
                    float damage = event.getAmount() / Math.max(1, voodooRats.size());
                    event.setCanceled(true);
                    for (EntityRat rat : voodooRats) {
                        rat.attackEntityFrom(event.getSource(), damage);
                    }
                }

            }
        }
    }

    private static final Map<ServerWorld, PlagueDoctorSpawner> PLAGUE_DOCTOR_SPAWNER_MAP = new HashMap<ServerWorld, PlagueDoctorSpawner>();

    @SubscribeEvent
    public static void onServerTick(TickEvent.WorldTickEvent tick){
        if(!tick.world.isRemote && tick.world instanceof ServerWorld){
            ServerWorld serverWorld = (ServerWorld)tick.world;
            if(PLAGUE_DOCTOR_SPAWNER_MAP.get(serverWorld) == null){
                PLAGUE_DOCTOR_SPAWNER_MAP.put(serverWorld, new PlagueDoctorSpawner(serverWorld));
            }
            PlagueDoctorSpawner spawner = PLAGUE_DOCTOR_SPAWNER_MAP.get(serverWorld);
            spawner.tick();
        }

    }

    @SubscribeEvent
    public static void onLivingSetAttackTarget(LivingSetAttackTargetEvent event) {
        if(event.getTarget() instanceof EntityRatMountBase) {
            EntityRat rat = ((EntityRatMountBase)event.getTarget()).getRat();
            if(rat != null){
                if(event.getEntityLiving() instanceof IMob && rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_UNDEAD)){
                    event.getEntityLiving().setRevengeTarget(null);
                    if(event.getEntityLiving() instanceof MobEntity){
                        ((MobEntity)event.getEntityLiving()).setAttackTarget(null);
                    }
                }
            }
        }
        if(event.getTarget() instanceof EntityRat){
            EntityRat rat = (EntityRat)event.getTarget();
            if(event.getEntityLiving() instanceof IMob && rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_UNDEAD)){
                event.getEntityLiving().setRevengeTarget(null);
                if(event.getEntityLiving() instanceof MobEntity){
                    ((MobEntity)event.getEntityLiving()).setAttackTarget(null);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().world.isRemote && (event.getEntityLiving().isPotionActive(RatsMod.PLAGUE_POTION) || event.getEntityLiving() instanceof EntityRat && ((EntityRat) event.getEntityLiving()).hasPlague())) {
            Random rand = event.getEntityLiving().getRNG();
            if (rand.nextInt(4) == 0) {
                int entitySize = 1;
                if (event.getEntityLiving().getBoundingBox().getAverageEdgeLength() > 0) {
                    entitySize = Math.max(1, (int) event.getEntityLiving().getBoundingBox().getAverageEdgeLength());
                }
                for (int i = 0; i < entitySize; i++) {
                    float motionX = rand.nextFloat() * 0.1F - 0.05F;
                    float motionZ = rand.nextFloat() * 0.1F - 0.05F;
                    RatsMod.PROXY.addParticle("flea", event.getEntityLiving().getPosX() + (double) (rand.nextFloat() * event.getEntityLiving().getWidth() * 2F) - (double) event.getEntityLiving().getWidth(),
                            event.getEntityLiving().getPosY() + (double) (rand.nextFloat() * event.getEntityLiving().getHeight()),
                            event.getEntityLiving().getPosZ() + (double) (rand.nextFloat() * event.getEntityLiving().getWidth() * 2F) - (double) event.getEntityLiving().getWidth(),
                            motionX, 0.0F, motionZ);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getPlayer().getHeldItem(Hand.MAIN_HAND).getItem() == RatsItemRegistry.CHEESE_STICK || event.getPlayer().getHeldItem(Hand.OFF_HAND).getItem() == RatsItemRegistry.CHEESE_STICK) {
            event.setCanceled(true);
            RatsMod.PROXY.setCheeseStaffContext(event.getPos(), event.getFace());
            Entity rat = null;
            ItemStack stack = event.getPlayer().getHeldItem(event.getHand());
            if (stack.getTag() != null && stack.getTag().hasUniqueId("RatUUID")) {
                if (!event.getWorld().isRemote() && event.getWorld() instanceof ServerWorld) {
                    rat = ((ServerWorld) event.getWorld()).getEntityByUuid(stack.getTag().getUniqueId("RatUUID"));
                }
            }
            event.getPlayer().swingArm(event.getHand());
            if (!event.getWorld().isRemote) {
                if (rat != null && rat instanceof EntityRat) {
                    RatsMod.sendMSGToAll(new MessageCheeseStaffRat(rat.getEntityId(), false));
                    EntityRat boundRat = (EntityRat) rat;
                    RatsMod.PROXY.setRefrencedRat(boundRat);
                    event.getPlayer().swingArm(event.getHand());
                }
            }
            if(event.getWorld().isRemote){
                RatsMod.PROXY.openCheeseStaffGui();
            }
        }
        if (RatConfig.cheesemaking && event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.CAULDRON && RatUtils.isMilk(event.getItemStack())) {
            if (event.getWorld().getBlockState(event.getPos()).get(CauldronBlock.LEVEL) == 0) {
                event.getWorld().setBlockState(event.getPos(), RatsBlockRegistry.MILK_CAULDRON.getDefaultState());
                if (!event.getWorld().isRemote) {
                    CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) event.getPlayer(), event.getPos(), new ItemStack(RatsBlockRegistry.MILK_CAULDRON));
                }
                event.getPlayer().playSound(SoundEvents.ITEM_BUCKET_EMPTY, 1, 1);
                if (!event.getPlayer().isCreative()) {
                    if (event.getItemStack().getItem() == Items.MILK_BUCKET) {
                        event.getItemStack().shrink(1);
                        event.getPlayer().addItemStackToInventory(new ItemStack(Items.BUCKET));
                    } else if (RatUtils.isMilk(event.getItemStack())) {
                        LazyOptional<IFluidHandlerItem> fluidHandler = FluidUtil.getFluidHandler(event.getItemStack());
                        if (fluidHandler.isPresent() && fluidHandler.orElse(null) != null) {
                            fluidHandler.orElse(null).drain(1000, IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                }
                event.setCanceled(true);
            }
        }
    }

   /* @SubscribeEvent
    public static void onChestGenerated(LootTableLoadEvent event) {
        //if (false){//RatConfig.addLoot) {
            if (event.getName().equals(LootTables.CHESTS_SIMPLE_DUNGEON) || event.getName().equals(LootTables.CHESTS_ABANDONED_MINESHAFT)
                    || event.getName().equals(LootTables.CHESTS_DESERT_PYRAMID) || event.getName().equals(LootTables.CHESTS_JUNGLE_TEMPLE)
                    || event.getName().equals(LootTables.CHESTS_STRONGHOLD_CORRIDOR) || event.getName().equals(LootTables.CHESTS_STRONGHOLD_CROSSING)
                    || event.getName().equals(LootTables.CHESTS_IGLOO_CHEST) || event.getName().equals(LootTables.CHESTS_WOODLAND_MANSION)
                    || event.getName().equals(LootTables.CHESTS_VILLAGE_VILLAGE_TOOLSMITH)) {
                ILootCondition.IBuilder chance = RandomChance.builder(0.4f);
                LootEntry.Builder item = ItemLootEntry.builder(RatsItemRegistry.CONTAMINATED_FOOD).quality(20).weight(20); //new ItemLootEntry(RatsItemRegistry.CONTAMINATED_FOOD, 20, 1, new ILootCondition[0], new ILootFunction[0], "rats:contaminated_food");
                LootPool.Builder builder = new LootPool.Builder().addEntry(item).acceptCondition(chance).rolls(new RandomValueRange(1, 5)).bonusRolls(0, 3);
                event.getTable().addPool(builder.build());
            }
            if (event.getName().equals(LootTables.CHESTS_SIMPLE_DUNGEON) || event.getName().equals(LootTables.CHESTS_ABANDONED_MINESHAFT)
                    || event.getName().equals(LootTables.CHESTS_DESERT_PYRAMID) || event.getName().equals(LootTables.CHESTS_JUNGLE_TEMPLE)
                    || event.getName().equals(LootTables.CHESTS_STRONGHOLD_CORRIDOR) || event.getName().equals(LootTables.CHESTS_STRONGHOLD_CROSSING)) {
                ILootCondition.IBuilder chance = RandomChance.builder(0.2f);
                LootEntry.Builder item = ItemLootEntry.builder(RatsItemRegistry.TOKEN_FRAGMENT).quality(10).weight(8);
                LootPool.Builder builder = new LootPool.Builder().addEntry(item).acceptCondition(chance).rolls(new RandomValueRange(1, 2)).bonusRolls(0, 1);
                event.getTable().addPool(builder.build());
            }
            if (event.getName().equals(LootTables.CHESTS_SIMPLE_DUNGEON) || event.getName().equals(LootTables.CHESTS_ABANDONED_MINESHAFT)
                    || event.getName().equals(LootTables.CHESTS_DESERT_PYRAMID) || event.getName().equals(LootTables.CHESTS_JUNGLE_TEMPLE)
                    || event.getName().equals(LootTables.CHESTS_STRONGHOLD_CORRIDOR) || event.getName().equals(LootTables.CHESTS_STRONGHOLD_CROSSING)) {
                ILootCondition.IBuilder chance = RandomChance.builder(0.05f);
                LootEntry.Builder item = ItemLootEntry.builder(RatsItemRegistry.RAT_UPGRADE_BASIC).quality(8).weight(3);
                LootPool.Builder builder = new LootPool.Builder().addEntry(item).acceptCondition(chance).rolls(new RandomValueRange(1, 1)).bonusRolls(0, 0);
                event.getTable().addPool(builder.build());
            }
        }
    }*/
}
