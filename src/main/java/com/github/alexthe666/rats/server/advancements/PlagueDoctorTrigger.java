package com.github.alexthe666.rats.server.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlagueDoctorTrigger implements ICriterionTrigger<PlagueDoctorTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation("rats:plague_doctor");
    private final Map<PlayerAdvancements, PlagueDoctorTrigger.Listeners> listeners = Maps.newHashMap();

    public ResourceLocation getId() {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, Listener<PlagueDoctorTrigger.Instance> listener) {
        PlagueDoctorTrigger.Listeners ratCageDecoTrigger$listeners = this.listeners.get(playerAdvancementsIn);

        if (ratCageDecoTrigger$listeners == null) {
            ratCageDecoTrigger$listeners = new PlagueDoctorTrigger.Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, ratCageDecoTrigger$listeners);
        }

        ratCageDecoTrigger$listeners.add(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, Listener<PlagueDoctorTrigger.Instance> listener) {
        PlagueDoctorTrigger.Listeners ratCageDecoTrigger$listeners = this.listeners.get(playerAdvancementsIn);

        if (ratCageDecoTrigger$listeners != null) {
            ratCageDecoTrigger$listeners.remove(listener);

            if (ratCageDecoTrigger$listeners.isEmpty()) {
                this.listeners.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn) {
        this.listeners.remove(playerAdvancementsIn);
    }

    public PlagueDoctorTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        EntityPredicate entitypredicate = EntityPredicate.deserialize(json.get("entity"));
        return new PlagueDoctorTrigger.Instance(entitypredicate);
    }

    public void trigger(EntityPlayerMP player, Entity entity) {
        PlagueDoctorTrigger.Listeners ratCageDecoTrigger$listeners = this.listeners.get(player.getAdvancements());
        if (ratCageDecoTrigger$listeners != null) {
            ratCageDecoTrigger$listeners.trigger(player, entity);
        }
    }

    public static class Instance extends AbstractCriterionInstance {
        private final EntityPredicate entity;

        public Instance(EntityPredicate entity) {
            super(PlagueDoctorTrigger.ID);
            this.entity = entity;
        }

        public boolean test(EntityPlayerMP player, Entity entity) {
            return this.entity.test(player, entity);
        }
    }

    static class Listeners {
        private final PlayerAdvancements playerAdvancements;
        private final Set<Listener<PlagueDoctorTrigger.Instance>> listeners = Sets.newHashSet();

        public Listeners(PlayerAdvancements playerAdvancementsIn) {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public boolean isEmpty() {
            return this.listeners.isEmpty();
        }

        public void add(Listener<PlagueDoctorTrigger.Instance> listener) {
            this.listeners.add(listener);
        }

        public void remove(Listener<PlagueDoctorTrigger.Instance> listener) {
            this.listeners.remove(listener);
        }

        public void trigger(EntityPlayerMP player, Entity entity) {
            List<Listener<PlagueDoctorTrigger.Instance>> list = null;

            for (Listener<PlagueDoctorTrigger.Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test(player, entity)) {
                    if (list == null) {
                        list = Lists.newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null) {
                for (Listener<PlagueDoctorTrigger.Instance> listener1 : list) {
                    listener1.grantCriterion(this.playerAdvancements);
                }
            }
        }
    }
}