package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageCheeseStaffSync extends AbstractMessage<MessageCheeseStaffSync> {

    public int entityId;
    public long posLg;
    public int facingID;
    public int control;
    public int extraData;

    public MessageCheeseStaffSync() {

    }

    public MessageCheeseStaffSync(int entityId, BlockPos pos, EnumFacing facing, int control, int extraData) {
        this.entityId = entityId;
        this.posLg = pos.toLong();
        this.facingID = facing.ordinal();
        this.control = control;
        this.extraData = extraData;
    }

    @Override
    public void onClientReceived(Minecraft client, MessageCheeseStaffSync message, EntityPlayer player, MessageContext messageContext) {
        Entity e = player.world.getEntityByID(message.entityId);
        if (e instanceof EntityRat) {
            EntityRat rat = (EntityRat) e;
            switch (message.control) {
                case 0://deposit
                    rat.depositPos = BlockPos.fromLong(message.posLg);
                    rat.depositFacing = EnumFacing.values()[message.facingID];
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
                case 4://set radius home point
                    rat.setSearchRadiusCenter(BlockPos.fromLong(message.posLg));
                    break;
                case 5://set radius scale
                    rat.setSearchRadius(message.extraData);
                    break;
                case 6://reset radius
                    rat.setSearchRadiusCenter(null);
                    rat.setSearchRadius(RatsMod.CONFIG_OPTIONS.defaultRatRadius);
                    break;
            }
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageCheeseStaffSync message, EntityPlayer player, MessageContext messageContext) {
        Entity e = player.world.getEntityByID(message.entityId);
        if (e instanceof EntityRat) {
            EntityRat rat = (EntityRat) e;
            switch (message.control) {
                case 0://deposit
                    rat.depositPos = BlockPos.fromLong(message.posLg);
                    rat.depositFacing = EnumFacing.values()[message.facingID];
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
                case 4://set radius home point
                    rat.setSearchRadiusCenter(BlockPos.fromLong(message.posLg));
                    break;
                case 5://set radius scale
                    rat.setSearchRadius(message.extraData);
                    break;
                case 6://reset radius
                    rat.setSearchRadiusCenter(null);
                    rat.setSearchRadius(RatsMod.CONFIG_OPTIONS.defaultRatRadius);
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
        extraData = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeLong(posLg);
        buf.writeInt(facingID);
        buf.writeInt(control);
        buf.writeInt(extraData);
    }

}
