package com.github.alexthe666.rats.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;

public class LaserPortalModel extends ModelNeoRatlantean {

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(floatyPivot);
    }

}
