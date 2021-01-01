package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageCheeseStaffSync {

    public int entityId;
    public long posLg;
    public int facingID;
    public int control;
    public int extraData;

    public MessageCheeseStaffSync(int entityId, long pos, int facing, int control) {
        this.entityId = entityId;
        this.posLg = pos;
        this.facingID = facing;
        this.control = control;
        this.extraData = 0;
    }

    public MessageCheeseStaffSync(int entityId, BlockPos pos, Direction facing, int control) {
        this.entityId = entityId;
        this.posLg = pos.toLong();
        this.facingID = facing.ordinal();
        this.control = control;
        this.extraData = 0;
    }

    public MessageCheeseStaffSync(int entityId, long pos, int facing, int control, int extraData) {
        this.entityId = entityId;
        this.posLg = pos;
        this.facingID = facing;
        this.control = control;
        this.extraData = extraData;
    }


    public MessageCheeseStaffSync(int entityId, BlockPos pos, Direction facing, int control, int extraData) {
        this.entityId = entityId;
        this.posLg = pos.toLong();
        this.facingID = facing.ordinal();
        this.control = control;
        this.extraData = extraData;
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageCheeseStaffSync message, Supplier<NetworkEvent.Context> context) {
            ((NetworkEvent.Context)context.get()).setPacketHandled(true);
            PlayerEntity player = context.get().getSender();
            if(player != null) {
                Entity e = player.world.getEntityByID(message.entityId);
                if (e instanceof EntityRat) {
                    EntityRat rat = (EntityRat) e;
                    switch (message.control) {
                        case 0://deposit
                            rat.setDepositPos(BlockPos.fromLong(message.posLg));
                            rat.depositFacing = Direction.values()[message.facingID];
                            break;
                        case 1://pickup
                            rat.setPickupPos(BlockPos.fromLong(message.posLg));
                            break;
                        case 2://set homepoint
                            rat.setHomePosAndDistance(BlockPos.fromLong(message.posLg), 32);
                            break;
                        case 3://detach homepoint
                            rat.detachHome();
                            break;
                        case 4://set radius home point
                            rat.setSearchRadiusCenter(BlockPos.fromLong(message.posLg));
                            break;
                        case 5://set radius scale
                            rat.setSearchRadius(message.extraData);
                            break;
                        case 6://reset radius
                            rat.setSearchRadiusCenter(null);
                            rat.setSearchRadius(RatConfig.defaultRatRadius);
                            break;
                    }
                }
            }
        }
    }

    public static MessageCheeseStaffSync read(PacketBuffer packetBuffer) {
        return new MessageCheeseStaffSync(packetBuffer.readInt(), packetBuffer.readLong(), packetBuffer.readInt(), packetBuffer.readInt(), packetBuffer.readInt());
    }

    public static void write(MessageCheeseStaffSync message, PacketBuffer buf) {
        buf.writeInt(message.entityId);
        buf.writeLong(message.posLg);
        buf.writeInt(message.facingID);
        buf.writeInt(message.control);
        buf.writeInt(message.extraData);
    }

}
