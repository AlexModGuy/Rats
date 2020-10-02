package com.github.alexthe666.rats.api;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event;

/*
    Parent for all rat common events.
 */
public class RatServerEvent extends Event {
    private EntityRat rat;

    public RatServerEvent(EntityRat rat) {
        this.rat = rat;
    }

    public EntityRat getRat() {
        return rat;
    }

    /*
        True if rat is riding a special mount (strider, golem, biplane, etc.).
        False if it should not take control (player, rattling gun, etc.)
     */
    @HasResult
    public static class IsRidingSpecialMount extends RatServerEvent {

        private boolean ridingSpecial;

        public IsRidingSpecialMount(EntityRat rat, boolean ridingSpecial) {
            super(rat);
            this.ridingSpecial = ridingSpecial;
        }

        public boolean isRidingSpecialMount() {
            return ridingSpecial;
        }

        public void setRidingSpecialMount(boolean ridingSpecial) {
            this.ridingSpecial = ridingSpecial;
        }
    }

    /*
        If a custom mount upgrade, set to ALLOW and make sure
        you return the entity type expected to ride.
     */
    @HasResult
    public static class GetMountEntityType extends RatServerEvent {

        private EntityType mountType;

        public GetMountEntityType(EntityRat rat, EntityType mountType) {
            super(rat);
            this.mountType = mountType;
        }

        public EntityType getMountType() {
            return mountType;
        }

        public void setMountType(EntityType mountType) {
            this.mountType = mountType;
        }
    }


    /*
        Determines the position of the rat's tail when riding a mount.
     */
    @HasResult
    public static class GetRatTailBehavior extends RatServerEvent {

        private int tailBehavior;

        public GetRatTailBehavior(EntityRat rat, int tailBehavior) {
            super(rat);
            this.tailBehavior = tailBehavior;
        }

        public int getRatTailBehavior() {
            return tailBehavior;
        }

        public void setRatTailBehavior(int tailBehavior) {
            this.tailBehavior = tailBehavior;
        }
    }

    /*
        Reach distance modifier for rats when depositing items or
        picking them up. Defaults to 1, change based on rat mount.
     */
    @HasResult
    public static class GetRatReachDistance extends RatServerEvent {

        private double distance;

        public GetRatReachDistance(EntityRat rat, double distance) {
            super(rat);
            this.distance = distance;
        }

        public double getReachDistance() {
            return distance;
        }

        public void setReachDistance(double distance) {
            this.distance = distance;
        }
    }
}
