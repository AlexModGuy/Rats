package com.github.alexthe666.rats.server.inventory;

import com.github.alexthe666.rats.client.gui.GuiRat;
import com.github.alexthe666.rats.client.gui.GuiRatCraftingTable;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCraftingTable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        Entity entity = world.getEntityByID(x);
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        if(ID == 1 && entity != null && entity instanceof EntityRat){
            return new ContainerRat((EntityRat) entity, player);
        }
        if(ID == 2 && tile != null && tile instanceof TileEntityRatCraftingTable){
            return new ContainerRatCraftingTable((TileEntityRatCraftingTable) tile, player);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        Entity entity = world.getEntityByID(x);
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        if(ID == 1 && entity != null && entity instanceof EntityRat){
            return new GuiRat((EntityRat) entity);
        }
        if(ID == 2 && tile != null && tile instanceof TileEntityRatCraftingTable){
            return new GuiRatCraftingTable((TileEntityRatCraftingTable) tile, player.inventory);
        }
        return null;
    }

}
