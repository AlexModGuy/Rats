package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageCheeseStaffRat {

    public int entityId;
    public boolean clear;
    public boolean openGUI;
    public int cheeseStaff;

    public MessageCheeseStaffRat(int entityId, boolean clear, boolean openGUI) {
        this.clear = clear;
        this.entityId = entityId;
        this.openGUI = openGUI;
        this.cheeseStaff = 0;
    }

    public MessageCheeseStaffRat(int entityId, boolean clear, boolean openGUI, int cheeseStaff) {
        this.clear = clear;
        this.entityId = entityId;
        this.openGUI = openGUI;
        this.cheeseStaff = cheeseStaff;
    }

    public static MessageCheeseStaffRat read(PacketBuffer packetBuffer) {
        return new MessageCheeseStaffRat(packetBuffer.readInt(), packetBuffer.readBoolean(), packetBuffer.readBoolean(), packetBuffer.readInt());
    }

    public static void write(MessageCheeseStaffRat message, PacketBuffer buf) {
        buf.writeInt(message.entityId);
        buf.writeBoolean(message.clear);
        buf.writeBoolean(message.openGUI);
        buf.writeInt(message.cheeseStaff);
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageCheeseStaffRat message, Supplier<NetworkEvent.Context> context) {
            ((NetworkEvent.Context) context.get()).setPacketHandled(true);
            context.get().enqueueWork(() -> {
                if (message.clear) {
                    RatsMod.PROXY.setRefrencedRat(null);
                } else {
                    RatsMod.PROXY.handlePacketCheeseStaffRat(message.entityId, message.clear);
                    if (message.openGUI) {
                        if (message.cheeseStaff == 0) {
                            RatsMod.PROXY.openCheeseStaffGui();
                        } else if (message.cheeseStaff == 1) {
                            RatsMod.PROXY.openCheeseStaffGui();
                        } else {
                            {
                                RatsMod.PROXY.openPatrolStaffGui();
                            }
                        }
                    }
                }
            });
        }

    }
}
