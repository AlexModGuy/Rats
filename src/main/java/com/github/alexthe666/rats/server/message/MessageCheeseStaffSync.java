package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.server.entity.EntityRat;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageCheeseStaffSync extends AbstractMessage<MessageCheeseStaffSync> {

    public int entityId;
    public long posLg;
    public int facingID;
    public int control;

    public MessageCheeseStaffSync() {

    }

    public MessageCheeseStaffSync(int entityId, BlockPos pos, Direction facing, int control) {
        this.entityId = entityId;
        this.posLg = pos.toLong();
        this.facingID = facing.ordinal();
        this.control = control;
    }

    @Override
    public void onClientReceived(Minecraft client, MessageCheeseStaffSync message, PlayerEntity player, MessageContext messageContext) {
        Entity e = player.world.getEntityByID(message.entityId);
        if (e instanceof EntityRat) {
            EntityRat rat = (EntityRat) e;
            switch (message.control) {
                case 0://deposit
                    rat.depositPos = BlockPos.fromLong(message.posLg);
                    rat.depositFacing = Direction.values()[message.facingID];
                    break;
                case 1://pickup
                    rat.pickupPos = BlockPos.fromLong(message.posLg);
                    break;
                case 2://set homepoint
                    rat.setHomePosAndDistance(BlockPos.fromLong(message.posLg), 32);
                    break;
                case 3://detach homepoint
                    rat.detachHome();
                    break;
            }
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageCheeseStaffSync message, PlayerEntity player, MessageContext messageContext) {
        Entity e = player.world.getEntityByID(message.entityId);
        if (e instanceof EntityRat) {
            EntityRat rat = (EntityRat) e;
            switch (message.control) {
                case 0://deposit
                    rat.depositPos = BlockPos.fromLong(message.posLg);
                    rat.depositFacing = Direction.values()[message.facingID];
                    break;
                case 1://pickup
                    rat.pickupPos = BlockPos.fromLong(message.posLg);
                    break;
                case 2://set homepoint
                    rat.setHomePosAndDistance(BlockPos.fromLong(message.posLg), 32);
                    break;
                case 3://detach homepoint
                    rat.detachHome();
                    break;
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
        posLg = buf.readLong();
        facingID = buf.readInt();
        control = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeLong(posLg);
        buf.writeInt(facingID);
        buf.writeInt(control);
    }

}
