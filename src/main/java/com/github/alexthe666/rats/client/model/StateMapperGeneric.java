package com.github.alexthe666.rats.client.model;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class StateMapperGeneric extends StateMapperBase {

    private String loc;

    public StateMapperGeneric(String loc) {
        this.loc = loc;
    }

    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        Map<IProperty<?>, Comparable<?>> map = Maps.<IProperty<?>, Comparable<?>>newLinkedHashMap(state.getProperties());
        String s = String.format("rats:" + loc);
        return new ModelResourceLocation(s, this.getPropertyString(map));
    }

}
