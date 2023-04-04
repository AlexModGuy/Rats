package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.client.model.RatsModelLayers;
import com.github.alexthe666.rats.client.model.hats.RatlantisArmorModel;
import net.minecraft.ChatFormatting;
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
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class RatlantisArmorItem extends ArmorItem {

	public RatlantisArmorItem(ArmorMaterial material, Type type, Item.Properties properties) {
		super(material, type, properties);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return "rats:textures/model/" + (slot == EquipmentSlot.LEGS ? "ratlantis_armor_1" : "ratlantis_armor_0") + ".png";
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
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
