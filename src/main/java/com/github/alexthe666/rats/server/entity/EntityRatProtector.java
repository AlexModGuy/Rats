package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.ai.*;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityRatProtector extends EntityRat {

    public EntityRatProtector(EntityType type, World worldIn) {
        super(type, worldIn);
        switchNavigator(5);
    }

    protected void switchNavigator(int type) {
        this.moveController = new RatEtherealMoveHelper(this);
        this.navigator = new EtherealRatPathNavigate(this, world);
        this.navigatorType = 5;
    }

    public boolean hasNoGravity() {
        return true;
    }

    protected int getCommandInteger() {
        return 0;
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.65D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
    }

    public float getBrightness() {
        return 240;
    }

    protected void onDeathUpdate() {
        ++this.deathTime;
        if (this.deathTime == 10) {
            this.remove(false);
            if (world.isRemote) {
                for(int i = 0; i < 15; i++){
                    RatsMod.PROXY.addParticle("rat_ghost", this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2F) - (double) this.getWidth(),
                            this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()),
                            this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2F) - (double) this.getWidth(),
                            0.92F, 0.82, 0.0F);
                }
            }
        }

    }

    protected SoundEvent getAmbientSound() {
        return RatsSoundRegistry.RATLANTEAN_SPIRIT_IDLE;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return RatsSoundRegistry.RATLANTEAN_SPIRIT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return RatsSoundRegistry.RATLANTEAN_SPIRIT_DIE;
    }

    public void tick(){
        super.tick();
        if (navigatorType != 5) {
            switchNavigator(5);
        }
        noClip = true;
        if(!world.isRemote){
            if(this.getAttackTarget() == null || !this.getAttackTarget().isAlive()){
                this.remove();
            }else{
                LivingEntity target = this.getAttackTarget();
                this.getMoveHelper().setMoveTo(target.getPosX(), target.getPosY() + target.getHeight()/2, target.getPosZ(), 1);
            }
        }
    }

    public boolean isTamed() {
        return true;
    }

    public boolean shouldHuntAnimal() {
        return false;
    }

    public boolean shouldHuntMonster() {
        return false;
    }

    public boolean canBeTamed() {
        return false;
    }

}
