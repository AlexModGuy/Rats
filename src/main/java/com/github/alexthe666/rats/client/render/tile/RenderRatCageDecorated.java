package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.client.model.*;
import com.github.alexthe666.rats.server.blocks.BlockAutoCurdler;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCageDecorated;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCageWheel;
import com.github.alexthe666.rats.server.items.ItemRatHammock;
import com.github.alexthe666.rats.server.items.ItemRatIgloo;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class RenderRatCageDecorated extends TileEntityRenderer<TileEntityRatCageDecorated> {
    private static final ModelRatIgloo MODEL_RAT_IGLOO = new ModelRatIgloo();
    private static final ModelRatHammock MODEL_RAT_HAMMOCK = new ModelRatHammock();
    private static final ModelRatWaterBottle MODEL_RAT_WATER_BOTTLE = new ModelRatWaterBottle();
    private static final ModelRatSeedBowl MODEL_RAT_SEED_BOWL = new ModelRatSeedBowl();
    private static final ModelRatBreedingLantern MODEL_RAT_BREEDING_LANTERN = new ModelRatBreedingLantern();
    private static final ModelRatWheel MODEL_RAT_WHEEL = new ModelRatWheel();
    private static final RenderType TEXTURE_RAT_IGLOO = RenderType.getEntityTranslucent(new ResourceLocation("rats:textures/model/rat_igloo.png"));
    private static final RenderType TEXTURE_RAT_HAMMOCK = RenderType.getEntityTranslucent(new ResourceLocation("rats:textures/model/rat_hammock_0.png"));
    private static final RenderType TEXTURE_RAT_WATER_BOTTLE = RenderType.getEntityTranslucent(new ResourceLocation("rats:textures/model/rat_water_bottle.png"));
    private static final RenderType TEXTURE_RAT_SEED_BOWL = RenderType.getEntityTranslucent(new ResourceLocation("rats:textures/model/rat_seed_bowl.png"));
    private static final RenderType TEXTURE_RAT_BREEDING_LANTERN = RenderType.getEntityTranslucent(new ResourceLocation("rats:textures/model/rat_breeding_lantern.png"));
    private static final RenderType TEXTURE_RAT_WHEEL = RenderType.getEntityTranslucent(new ResourceLocation("rats:textures/model/rat_wheel.png"));

    public RenderRatCageDecorated(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileEntityRatCageDecorated entity, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn){
        float rotation = 0;
        float shutProgress = 0;
        BlockState blockstate = entity.getBlockState();
        matrixStackIn.push();
        matrixStackIn.translate(0.5D, 1.5D, 0.5D);
        float f = blockstate.get(BlockAutoCurdler.FACING).rotateY().getHorizontalAngle() + 90;
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(180));
        ItemStack containedItem = ItemStack.EMPTY;
        if (entity != null && entity.getWorld() != null && entity instanceof TileEntityRatCageDecorated) {
            containedItem = entity.getContainedItem();
        }
        if (containedItem.getItem() instanceof ItemRatIgloo) {
            GlStateManager.enableBlend();
            GlStateManager.disableCull();
            DyeColor color = ((ItemRatIgloo) containedItem.getItem()).color;
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(TEXTURE_RAT_IGLOO);
            GlStateManager.color4f(color.getColorComponentValues()[0], color.getColorComponentValues()[1], color.getColorComponentValues()[2], 1.0F);
            MODEL_RAT_IGLOO.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, color.getColorComponentValues()[0], color.getColorComponentValues()[1], color.getColorComponentValues()[2], 1.0F);
            GlStateManager.enableCull();
            GlStateManager.disableBlend();
        }
        if (containedItem.getItem() instanceof ItemRatHammock) {
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(TEXTURE_RAT_HAMMOCK);
            matrixStackIn.push();
            DyeColor color = ((ItemRatHammock) containedItem.getItem()).color;
            GlStateManager.enableColorMaterial();
            MODEL_RAT_HAMMOCK.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, color.getColorComponentValues()[0], color.getColorComponentValues()[1], color.getColorComponentValues()[2], 1.0F);
            GlStateManager.disableColorMaterial();
            matrixStackIn.pop();


        }
        if (containedItem.getItem() == RatsItemRegistry.RAT_WATER_BOTTLE) {
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(TEXTURE_RAT_WATER_BOTTLE);
            MODEL_RAT_WATER_BOTTLE.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);

        }
        if (containedItem.getItem() == RatsItemRegistry.RAT_SEED_BOWL) {
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(TEXTURE_RAT_SEED_BOWL);
            MODEL_RAT_SEED_BOWL.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);

        }
        if (containedItem.getItem() == RatsItemRegistry.RAT_WHEEL) {
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(TEXTURE_RAT_WHEEL);
            if(entity instanceof TileEntityRatCageWheel){
                MODEL_RAT_WHEEL.animate((TileEntityRatCageWheel)entity, partialTicks);
            }
            MODEL_RAT_WHEEL.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);

        }
        if (containedItem.getItem() == RatsItemRegistry.RAT_BREEDING_LANTERN) {
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(TEXTURE_RAT_BREEDING_LANTERN);
            MODEL_RAT_BREEDING_LANTERN.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            MODEL_RAT_BREEDING_LANTERN.swingChain();
         }
        matrixStackIn.pop();
    }
}
