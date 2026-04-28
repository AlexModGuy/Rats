package com.github.alexthe666.rats.server.capability;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.message.UpdateSelectedRatPacket;
import com.mojang.serialization.Codec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.Nullable;

// Replaces the old SELECTED_RAT capability + ICapabilityProvider machinery
// that was deleted in NeoForge 21.x. Storage is a per-Player AttachmentType
// holding the currently-selected TamedRat by entity id (-1 == none).
public final class SelectedRat {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, RatsMod.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> SELECTED_RAT_ID =
            ATTACHMENTS.register("selected_rat", () ->
                    AttachmentType.builder(() -> -1)
                            .serialize(Codec.INT)
                            .copyOnDeath()
                            .build());

    private SelectedRat() {}

    @Nullable
    public static TamedRat get(LivingEntity entity) {
        int id = entity.getData(SELECTED_RAT_ID.get());
        if (id < 0) return null;
        Entity e = entity.level().getEntity(id);
        return e instanceof TamedRat rat ? rat : null;
    }

    public static boolean has(LivingEntity entity) {
        return get(entity) != null;
    }

    public static void set(Player player, @Nullable TamedRat rat) {
        int id = rat == null ? -1 : rat.getId();
        player.setData(SELECTED_RAT_ID.get(), id);
        if (!player.level().isClientSide()) {
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(
                    player, new UpdateSelectedRatPacket(player.getId(), id));
        }
    }

    public static void setLocal(LivingEntity entity, int ratId) {
        entity.setData(SELECTED_RAT_ID.get(), ratId);
    }

    public static void clear(Player player) {
        set(player, null);
    }
}
