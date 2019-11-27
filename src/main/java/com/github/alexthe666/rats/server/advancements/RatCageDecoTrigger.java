package com.github.alexthe666.rats.server.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RatCageDecoTrigger implements ICriterionTrigger<RatCageDecoTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation("rats:rat_cage_decoration");
    private final Map<PlayerAdvancements, RatCageDecoTrigger.Listeners> listeners = Maps.newHashMap();

    public ResourceLocation getId() {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<RatCageDecoTrigger.Instance> listener) {
        RatCageDecoTrigger.Listeners ratCageDecoTrigger$listeners = this.listeners.get(playerAdvancementsIn);

        if (ratCageDecoTrigger$listeners == null) {
            ratCageDecoTrigger$listeners = new RatCageDecoTrigger.Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, ratCageDecoTrigger$listeners);
        }

        ratCageDecoTrigger$listeners.add(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<RatCageDecoTrigger.Instance> listener) {
        RatCageDecoTrigger.Listeners ratCageDecoTrigger$listeners = this.listeners.get(playerAdvancementsIn);

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

    public RatCageDecoTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        EntityPredicate entitypredicate = EntityPredicate.deserialize(json.get("entity"));
        return new RatCageDecoTrigger.Instance(entitypredicate);
    }

    public void trigger(ServerPlayerEntity player, Entity entity) {
        RatCageDecoTrigger.Listeners ratCageDecoTrigger$listeners = this.listeners.get(player.getAdvancements());
        if (ratCageDecoTrigger$listeners != null) {
            ratCageDecoTrigger$listeners.trigger(player, entity);
        }
    }

    public static class Instance extends CriterionInstance {
        private final EntityPredicate entity;

        public Instance(EntityPredicate entity) {
            super(RatCageDecoTrigger.ID);
            this.entity = entity;
        }

        public boolean test(ServerPlayerEntity player, Entity entity) {
            return this.entity.test(player, entity);
        }
    }

    static class Listeners {
        private final PlayerAdvancements playerAdvancements;
        private final Set<Listener<RatCageDecoTrigger.Instance>> listeners = Sets.newHashSet();

        public Listeners(PlayerAdvancements playerAdvancementsIn) {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public boolean isEmpty() {
            return this.listeners.isEmpty();
        }

        public void add(ICriterionTrigger.Listener<RatCageDecoTrigger.Instance> listener) {
            this.listeners.add(listener);
        }

        public void remove(ICriterionTrigger.Listener<RatCageDecoTrigger.Instance> listener) {
            this.listeners.remove(listener);
        }

        public void trigger(ServerPlayerEntity player, Entity entity) {
            List<Listener<RatCageDecoTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<RatCageDecoTrigger.Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test(player, entity)) {
                    if (list == null) {
                        list = Lists.newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null) {
                for (ICriterionTrigger.Listener<RatCageDecoTrigger.Instance> listener1 : list) {
                    listener1.grantCriterion(this.playerAdvancements);
                }
            }
        }
    }
}