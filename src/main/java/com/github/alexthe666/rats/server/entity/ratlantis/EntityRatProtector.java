package com.github.alexthe666.rats.server.entity.ratlantis;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.ai.EtherealRatPathNavigate;
import com.github.alexthe666.rats.server.entity.ai.RatEtherealMoveHelper;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityRatProtector extends EntityRat {

    public EntityRatProtector(EntityType type, World worldIn) {
        super(type, worldIn);
        switchNavigator(5);
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float) ((int) this.getAttribute(Attributes.field_233823_f_).getValue()));
        if(flag){
            this.onKillCommand();

        }
        return flag;
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


    public static AttributeModifierMap.MutableAttribute func_234290_eH_() {
        return MobEntity.func_233666_p_()
                .func_233815_a_(Attributes.field_233818_a_, 2.0D)            //HEALTH
                .func_233815_a_(Attributes.field_233821_d_, 1.65D)           //SPEED
                .func_233815_a_(Attributes.field_233823_f_, 6.0D)            //ATTACK
                .func_233815_a_(Attributes.field_233819_b_, 32.0D);         //FOLLOW RANGE
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

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return RatsSoundRegistry.RATLANTEAN_SPIRIT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return null;
    }

    public void tick(){
        super.tick();
        if (navigatorType != 5) {
            switchNavigator(5);
        }
        noClip = true;
        if(!world.isRemote){
            if(this.getAttackTarget() == null || !this.getAttackTarget().isAlive()){
                this.onKillCommand();
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
