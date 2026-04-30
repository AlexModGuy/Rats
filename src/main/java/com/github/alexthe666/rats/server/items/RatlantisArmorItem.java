package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.client.model.RatsModelLayers;
import com.github.alexthe666.rats.client.model.hats.RatlantisArmorModel;
import com.github.alexthe666.rats.registry.RatsArmorMaterialRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class RatlantisArmorItem extends ArmorItem {

	public RatlantisArmorItem(Holder<ArmorMaterial> material, Type type, Item.Properties properties) {
		super(material, type, properties.durability(RatsArmorMaterialRegistry.durabilityFor(material, type)));
	}

	// 1.21: per-stack override for the armor texture path; NeoForge calls this from HumanoidArmorLayer
	// before falling back to ArmorMaterial.Layer.texture(). Returns rats:textures/model/armor/ratlantis_armor_{0,1}.png
	// (singular `model` — matches the existing asset layout from 1.20.1).
	@Override
	public net.minecraft.resources.ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
		return net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(
			"rats", "textures/model/armor/" + (slot == EquipmentSlot.LEGS ? "ratlantis_armor_1" : "ratlantis_armor_0") + ".png");
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		tooltip.add(Component.translatable("item.rats.ratlantis_armor.desc0").withStyle(ChatFormatting.YELLOW));
		tooltip.add(Component.translatable("item.rats.ratlantis_armor.desc1").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.rats.ratlantis_armor.desc2").withStyle(ChatFormatting.GRAY));
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
				EntityModelSet models = Minecraft.getInstance().getEntityModels();
				ModelPart root = models.bakeLayer(slot == EquipmentSlot.LEGS ? RatsModelLayers.RATLANTIS_ARMOR_INNER : RatsModelLayers.RATLANTIS_ARMOR_OUTER);
				return new RatlantisArmorModel(root);
			}
		});
	}
}
