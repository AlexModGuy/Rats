package com.github.alexthe666.rats.server.inventory;

import com.github.alexthe666.rats.client.gui.GuiRat;
import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        Entity entity = world.getEntityByID(x);
        if(ID == 1 && entity != null && entity instanceof EntityRat){
            return new ContainerRat((EntityRat) entity, player);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        Entity entity = world.getEntityByID(x);
        if(ID == 1 && entity != null && entity instanceof EntityRat){
            return new GuiRat((EntityRat) entity);
        }
        return null;
    }

}
