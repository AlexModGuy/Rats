package com.github.alexthe666.rats.server.events;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.EntityIllagerPiper;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.message.MessageRatDismount;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.Iterator;
import java.util.Random;

@Mod.EventBusSubscriber
public class ServerEvents {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        if(event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND).getItem() == RatsItemRegistry.CHEESE_STICK || event.getEntityPlayer().getHeldItem(EnumHand.OFF_HAND).getItem() == RatsItemRegistry.CHEESE_STICK){
            event.setUseBlock(Event.Result.DENY);
        }
        if(event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND).getItem() == RatsItemRegistry.CHUNKY_CHEESE_TOKEN || event.getEntityPlayer().getHeldItem(EnumHand.OFF_HAND).getItem() == RatsItemRegistry.CHUNKY_CHEESE_TOKEN){
            if(!event.getEntityPlayer().isCreative()){
                event.getItemStack().shrink(1);
            }
            boolean canBuild = true;
            BlockPos pos = event.getPos().offset(event.getFace());
            for(int i = 0; i < 4; i++){
                IBlockState state = event.getWorld().getBlockState(pos.up(i));
                if(state.getBlockHardness(event.getWorld(), pos.up(i)) == -1.0F){
                    canBuild = false;
                }
            }
            if(canBuild) {
                event.getEntityPlayer().playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, 1, 1);
                event.getWorld().setBlockState(pos, RatsBlockRegistry.MARBLED_CHEESE_RAW.getDefaultState());
                event.getWorld().setBlockState(pos.up(), RatsBlockRegistry.RATLANTIS_PORTAL.getDefaultState());
                event.getWorld().setBlockState(pos.up(2), RatsBlockRegistry.RATLANTIS_PORTAL.getDefaultState());
                event.getWorld().setBlockState(pos.up(3), RatsBlockRegistry.MARBLED_CHEESE_RAW.getDefaultState());
            }
        }
        if (RatsMod.CONFIG_OPTIONS.cheesemaking && event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.CAULDRON && isMilk(event.getItemStack())) {
            if (event.getWorld().getBlockState(event.getPos()).getValue(BlockCauldron.LEVEL) == 0) {
                event.getWorld().setBlockState(event.getPos(), RatsBlockRegistry.MILK_CAULDRON.getDefaultState());
                if(!event.getWorld().isRemote){
                    CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)event.getEntityPlayer(), event.getPos(), new ItemStack(RatsBlockRegistry.MILK_CAULDRON));
                }
                event.getEntityPlayer().playSound(SoundEvents.ITEM_BUCKET_EMPTY, 1, 1);
                if (!event.getEntityPlayer().isCreative()) {
                    if(event.getItemStack().getItem() == Items.MILK_BUCKET){
                        event.getItemStack().shrink(1);
                        event.getEntityPlayer().addItemStackToInventory(new ItemStack(Items.BUCKET));
                    }else if(isMilk(event.getItemStack())){
                        IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(event.getItemStack());
                        fluidHandler.drain(1000, true);
                    }
                }
                event.setCanceled(true);
            }
        }
    }

    private boolean isMilk(ItemStack stack) {
        if(stack.getItem() == Items.MILK_BUCKET){
            return true;
        }
        FluidStack fluidStack = FluidUtil.getFluidContained(stack);
        if(fluidStack != null && fluidStack.amount >= 1000 && (fluidStack.getFluid().getUnlocalizedName().contains("milk") || fluidStack.getFluid().getUnlocalizedName().contains("Milk"))){
            return true;
        }
        return false;
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
        if(event.getEntity() instanceof EntityPlayer){
            event.getEntity().width = 0.1F;
            event.getEntity().height = 0.1F;
        }
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