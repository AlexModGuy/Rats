package com.github.alexthe666.rats.server.compat.tinkers;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.client.MaterialRenderInfo;

public class TinkersCompatClient {

    @SideOnly(Side.CLIENT)
    public static void preInit() {
        //TinkerBook.INSTANCE.addTransformer(new RatsBookTransformer());
        //TinkerBook.INSTANCE.addRepository(new FileRepository("rats:tinkers/book"));
        MaterialRenderInfo renderInfo = new MaterialRenderInfo.BlockTexture(new ResourceLocation("rats:blocks/cheese"));
        TinkersCompat.CHEESE_MATERIAL.setRenderInfo(renderInfo);
    }
}
