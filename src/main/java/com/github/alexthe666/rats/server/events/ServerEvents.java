package com.github.alexthe666.rats.server.events;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.EntityIllagerPiper;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.message.MessageRatDismount;
import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;
import java.util.Random;

@Mod.EventBusSubscriber
public class ServerEvents {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        if(event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND).getItem() == RatsItemRegistry.CHEESE_STICK || event.getEntityPlayer().getHeldItem(EnumHand.OFF_HAND).getItem() == RatsItemRegistry.CHEESE_STICK){
            event.setUseBlock(Event.Result.DENY);
        }
        if (RatsMod.CONFIG_OPTIONS.cheesemaking && event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.CAULDRON && event.getItemStack().getItem() == Items.MILK_BUCKET) {
            if (event.getWorld().getBlockState(event.getPos()).getValue(BlockCauldron.LEVEL) == 0) {
                event.getWorld().setBlockState(event.getPos(), RatsBlockRegistry.MILK_CAULDRON.getDefaultState());
                event.getEntityPlayer().playSound(SoundEvents.ITEM_BUCKET_EMPTY, 1, 1);
                if (!event.getEntityPlayer().isCreative()) {
                    event.getItemStack().shrink(1);
                    event.getEntityPlayer().setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BUCKET));
                }
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerInteractWithEntity(PlayerInteractEvent.EntityInteract event) {
        if(event.getTarget() instanceof EntityOcelot){
            EntityOcelot ocelot = (EntityOcelot)event.getTarget();
            Item heldItem = event.getEntityPlayer().getHeldItem(event.getHand()).getItem();
            Random random = event.getWorld().rand;
            if(ocelot.getHealth() < ocelot.getMaxHealth()) {
                if (heldItem == RatsItemRegistry.RAW_RAT) {
                    ocelot.heal(4);
                    event.getWorld().playSound(null, ocelot.posX, ocelot.posY, ocelot.posZ, SoundEvents.ENTITY_LLAMA_EAT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    event.getWorld().playSound(null, ocelot.posX, ocelot.posY, ocelot.posZ, SoundEvents.ENTITY_CAT_AMBIENT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    for (int i = 0; i < 3; i++) {
                        event.getWorld().spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, ocelot.posX + random.nextDouble() - random.nextDouble(), ocelot.posY + 0.5 + random.nextDouble() - random.nextDouble(), ocelot.posZ + random.nextDouble() - random.nextDouble(), 0, 0, 0);
                    }
                }
                if (heldItem == RatsItemRegistry.COOKED_RAT) {
                    ocelot.heal(8);
                    event.getWorld().playSound(null, ocelot.posX, ocelot.posY, ocelot.posZ, SoundEvents.ENTITY_LLAMA_EAT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    event.getWorld().playSound(null, ocelot.posX, ocelot.posY, ocelot.posZ, SoundEvents.ENTITY_CAT_AMBIENT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    for (int i = 0; i < 3; i++) {
                        event.getWorld().spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, ocelot.posX + random.nextDouble() - random.nextDouble(), ocelot.posY + 0.5 + random.nextDouble() - random.nextDouble(), ocelot.posZ + random.nextDouble() - random.nextDouble(), 0, 0, 0);
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() != null && RatUtils.isPredator(event.getEntity()) && event.getEntity() instanceof EntityAnimal) {
            EntityAnimal animal = (EntityAnimal) event.getEntity();
            animal.targetTasks.addTask(5, new EntityAINearestAttackableTarget(animal, EntityRat.class, true));
        }
    }

    @SubscribeEvent
    public void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getEntityPlayer().isSneaking() && !event.getEntityPlayer().getPassengers().isEmpty()) {
            for(Entity passenger : event.getEntityPlayer().getPassengers()){
                if(passenger instanceof EntityRat){
                    passenger.dismountRidingEntity();
                    passenger.setPosition(event.getEntityPlayer().posX, event.getEntityPlayer().posY, event.getEntityPlayer().posZ);
                    RatsMod.NETWORK_WRAPPER.sendToServer(new MessageRatDismount(passenger.getEntityId()));
                }
            }
        }
    }

    @SubscribeEvent
    public void onGatherCollisionBoxes(GetCollisionBoxesEvent event) {
        if (event.getEntity() != null && event.getEntity() instanceof EntityRat) {
            Iterator<AxisAlignedBB> itr = event.getCollisionBoxesList().iterator();
            while (itr.hasNext()) {
                AxisAlignedBB aabb = itr.next();
                BlockPos pos = new BlockPos(aabb.minX, aabb.minY, aabb.minZ);
                if (((EntityRat) event.getEntity()).canPhaseThroughBlock(event.getWorld(), pos)) {
                    itr.remove();
                }
            }
        }
    }

    @SubscribeEvent
    public void onDrops(LivingDropsEvent event) {
        if (event.getEntityLiving() instanceof EntityIllagerPiper && event.getSource().getTrueSource() instanceof EntityPlayer && event.getEntityLiving().world.rand.nextFloat() < RatsMod.CONFIG_OPTIONS.piperHatDropRate + (RatsMod.CONFIG_OPTIONS.piperHatDropRate / 2) * event.getLootingLevel()) {
            event.getDrops().add(new EntityItem(event.getEntity().world, event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, new ItemStack(RatsItemRegistry.PIPER_HAT)));
        }
    }


}