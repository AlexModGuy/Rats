package com.github.alexthe666.rats.client.model;

import com.google.common.collect.Maps;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class StateMapperGeneric extends StateMapperBase {

    private String loc;

    public StateMapperGeneric(String loc) {
        this.loc = loc;
    }

    protected ModelResourceLocation getModelResourceLocation(BlockState state) {
        Map<IProperty<?>, Comparable<?>> map = Maps.newLinkedHashMap(state.getProperties());
        String s = String.format("rats:" + loc);
        return new ModelResourceLocation(s, this.getPropertyString(map));
    }

}
