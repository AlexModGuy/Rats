package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelFlyingDutchrat;
import com.github.alexthe666.rats.server.entity.EntityDutchrat;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class LayerDutchratHelmet extends LayerRenderer<EntityDutchrat, ModelFlyingDutchrat<EntityDutchrat>> {
     private final IEntityRenderer<EntityDutchrat, ModelFlyingDutchrat<EntityDutchrat>> ratRenderer;
    private static final BipedModel backup = new BipedModel();
    public LayerDutchratHelmet(IEntityRenderer<EntityDutchrat, ModelFlyingDutchrat<EntityDutchrat>> ratRendererIn) {
        super(ratRendererIn);
        this.ratRenderer = ratRendererIn;
    }

    public void render(EntityDutchrat rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GL11.glPushMatrix();
        ((ModelFlyingDutchrat) this.ratRenderer.getEntityModel()).body1.postRender(0.0625F);
        ((ModelFlyingDutchrat) this.ratRenderer.getEntityModel()).neck.postRender(0.0625F);
        ((ModelFlyingDutchrat) this.ratRenderer.getEntityModel()).head.postRender(0.0625F);
        GL11.glTranslatef(0, -0.77F, 0);
        ItemStack itemstack = rat.getItemStackFromSlot(EquipmentSlotType.HEAD);
        if (itemstack.getItem() instanceof ArmorItem) {
            ArmorItem armoritem = (ArmorItem) itemstack.getItem();
            BipedModel a = getArmorModelHook(rat, itemstack, EquipmentSlotType.HEAD, backup);
            this.bindTexture(LayerRatHelmet.getArmorResource(rat, itemstack, EquipmentSlotType.HEAD, null));
            a.render(rat, limbSwing, limbSwingAmount, ageInTicks, 0, 0, scale);
        }
        GL11.glPopMatrix();
    }

    protected BipedModel<?> getArmorModelHook(LivingEntity entity, ItemStack itemStack, EquipmentSlotType slot, BipedModel model) {
        return net.minecraftforge.client.ForgeHooksClient.getArmorModel(entity, itemStack, slot, model);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
