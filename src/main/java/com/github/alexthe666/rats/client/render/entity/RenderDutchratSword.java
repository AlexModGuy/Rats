package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.server.entity.ratlantis.EntityDutchratSword;
import com.github.alexthe666.rats.server.items.RatlantisItemRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

public class RenderDutchratSword extends EntityRenderer<EntityDutchratSword> {

    private static final ItemStack PIRAT_SWORD = new ItemStack(RatlantisItemRegistry.GHOST_PIRAT_CUTLASS);

    public RenderDutchratSword() {
        super(Minecraft.getInstance().getRenderManager());
    }

    public void render(EntityDutchratSword entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevRotationYaw, entityIn.rotationYaw) - 90.0F));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch)));
        matrixStackIn.translate(0, 0.5F, 0);
        matrixStackIn.scale(3F, 3F, 3F);
        matrixStackIn.rotate(new Quaternion(Vector3f.YP, 90F, true));
        matrixStackIn.rotate(new Quaternion(Vector3f.ZP, (entityIn.ticksExisted + partialTicks) * 10F, true));
        matrixStackIn.translate(0, -0.15F, 0);
        Minecraft.getInstance().getItemRenderer().renderItem(PIRAT_SWORD, ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
        matrixStackIn.pop();
    }

    private float interpolateValue(float start, float end, float pct) {
        return start + (end - start) * pct;
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(EntityDutchratSword entity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }
}
