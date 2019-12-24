package com.github.alexthe666.rats.server.tomove;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class AnimationMessage  {

    private int entityID;
    private int index;

    public AnimationMessage(int entityID, int index) {
        this.entityID = entityID;
        this.index = index;
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(AnimationMessage message, Supplier<NetworkEvent.Context> context) {
            ((NetworkEvent.Context)context.get()).setPacketHandled(true);
            PlayerEntity player = Minecraft.getInstance().player;
            if(player != null) {
                IAnimatedEntity entity = (IAnimatedEntity) player.world.getEntityByID(message.entityID);
                if (entity != null) {
                    if (message.index == -1) {
                        entity.setAnimation(IAnimatedEntity.NO_ANIMATION);
                    } else {
                        entity.setAnimation(entity.getAnimations()[message.index]);
                    }
                    entity.setAnimationTick(0);
                }
            }
        }
    }

    public static AnimationMessage read(PacketBuffer buf) {
        return new AnimationMessage(buf.readInt(), buf.readInt());
    }

    public static void write(AnimationMessage message, PacketBuffer buf) {
        buf.writeInt(message.entityID);
        buf.writeInt(message.index);
    }
}
