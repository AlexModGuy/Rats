package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.server.blocks.RatlantisBlockRegistry;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.tile.*;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RatsTEISR extends ItemStackTileEntityRenderer {

    private TileEntityRatHole teRatHole = new TileEntityRatHole();
    private TileEntityRatTrap teRatTrap = new TileEntityRatTrap();
    private TileEntityAutoCurdler teAutoCurdler = new TileEntityAutoCurdler();
    private TileEntityRatlantisPortal teRatlantisPortal = new TileEntityRatlantisPortal();
    private TileEntityRatlanteanAutomatonHead teHead = new TileEntityRatlanteanAutomatonHead();
    private TileEntityTrashCan trashCan = new TileEntityTrashCan();
    private RenderRatHole renderRatHole = new RenderRatHole(TileEntityRendererDispatcher.instance);
    private RenderRatTrap renderRatTrap = new RenderRatTrap(TileEntityRendererDispatcher.instance);
    private RenderAutoCurdler renderAutoCurdler = new RenderAutoCurdler(TileEntityRendererDispatcher.instance);
    private RenderRatlantisPortal renderRatlantisPortal = new RenderRatlantisPortal(TileEntityRendererDispatcher.instance);
    private RenderRatlanteanAutomatonHead renderRatlanteanAutomatonHead = new RenderRatlanteanAutomatonHead(TileEntityRendererDispatcher.instance);
    private RenderTrashCan renderTrashCan = new RenderTrashCan(TileEntityRendererDispatcher.instance);
    //private RenderUpgradeCombiner renderUpgradeCombiner = new RenderUpgradeCombiner();
    //private RenderUpgradeSeparator renderUpgradeSeparator = new RenderUpgradeSeparator();

    @Override
    public void func_239207_a_(ItemStack itemStackIn, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (itemStackIn.getItem() == Item.getItemFromBlock(RatsBlockRegistry.RAT_HOLE)) {
            renderRatHole.render(teRatHole, 0, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
        if (itemStackIn.getItem() == Item.getItemFromBlock(RatsBlockRegistry.RAT_TRAP)) {
            matrixStackIn.scale(1.2F, 1.2F, 1.2F);
            renderRatTrap.render(teRatTrap, 0, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
        if (itemStackIn.getItem() == Item.getItemFromBlock(RatsBlockRegistry.AUTO_CURDLER)) {
            renderAutoCurdler.render(teAutoCurdler, 0, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
        if (itemStackIn.getItem() == Item.getItemFromBlock(RatlantisBlockRegistry.RATLANTIS_PORTAL)) {
            renderRatlantisPortal.render(teRatlantisPortal, 0, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
        if (itemStackIn.getItem() == Item.getItemFromBlock(RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD)) {
            renderRatlanteanAutomatonHead.render(teHead, 0, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
        if (itemStackIn.getItem() == Item.getItemFromBlock(RatsBlockRegistry.TRASH_CAN)) {
            renderTrashCan.render(trashCan, 0, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
    }
}