package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.RatsModelLayers;
import com.github.alexthe666.rats.client.model.hats.*;
import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.monster.GhostPirat;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class HatItem extends ArmorItem {

	private final int loreLines;

	public HatItem(Item.Properties properties, ArmorMaterial material, int loreLines) {
		super(material, Type.HELMET, properties);
		this.loreLines = loreLines;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		if (stack.is(RatsItemRegistry.BLACK_DEATH_MASK.get())) {
			tooltip.add(Component.translatable("item.rats.plague_doctor_mask.desc").withStyle(ChatFormatting.GRAY));
		}
		if (this.loreLines > 0) {
			for (int i = 0; i < this.loreLines; i++) {
				tooltip.add(Component.translatable(this.getDescriptionId() + ".desc" + (this.loreLines == 1 ? "" : i)).withStyle(ChatFormatting.GRAY));
			}
		}
	}

	@Override
	public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
		return stack.is(RatsItemRegistry.RAT_KING_CROWN.get());
	}

	@Override
	public boolean isEnderMask(ItemStack stack, Player player, EnderMan enderMan) {
		return stack.is(RatsItemRegistry.BLACK_DEATH_MASK.get()) || stack.is(RatsItemRegistry.PLAGUE_DOCTOR_MASK.get());
	}

	/**
	 * Override this to add a custom transformation to the hat when on a rat's head.
	 *
	 * @param rat   the rat to render the hat on
	 * @param stack a PoseStack to allow translation, rotation, and scaling
	 */
	public void transformOnHead(AbstractRat rat, PoseStack stack) {
		if (this == RatsItemRegistry.CHEF_TOQUE.get()) {
			stack.mulPose(Axis.XN.rotationDegrees(25.0F));
			stack.translate(0.0F, 0.1F, 0.3F);
		}
		if (this == RatsItemRegistry.PIPER_HAT.get()) {
			stack.mulPose(Axis.XN.rotationDegrees(10.0F));
			stack.translate(0.0F, 0.0F, 0.1F);
		}
		if (this == RatsItemRegistry.PIRAT_HAT.get()) {
			stack.mulPose(Axis.XN.rotationDegrees(5.0F));
			stack.translate(0.0F, -0.125F, 0.0F);
			stack.scale(1.425F, 1.425F, 1.425F);
		}
		if (this == RatlantisItemRegistry.GHOST_PIRAT_HAT.get()) {
			float piratScale = rat instanceof GhostPirat ? 1.1F : 1.425F;
			float piratTranslate = rat instanceof GhostPirat ? 0.05F : -0.125F;
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.3F);
			stack.mulPose(Axis.XN.rotationDegrees(5.0F));
			stack.translate(0.0F, piratTranslate, 0.0F);
			stack.scale(piratScale, piratScale, piratScale);
		}
		if (this == RatsItemRegistry.ARCHEOLOGIST_HAT.get()) {
			stack.mulPose(Axis.XN.rotationDegrees(5.0F));
			stack.translate(0.0F, -0.1F, 0.0F);
			stack.scale(1.425F, 1.425F, 1.425F);
		}
		if (this == RatsItemRegistry.FARMER_HAT.get() || this == RatsItemRegistry.FISHERMAN_HAT.get()) {
			stack.mulPose(Axis.XN.rotationDegrees(5.0F));
			stack.translate(0.0F, -0.1F, 0.0F);
			stack.scale(1.425F, 1.425F, 1.425F);
		}
		if (this == RatsItemRegistry.PLAGUE_DOCTOR_MASK.get() || this == RatsItemRegistry.BLACK_DEATH_MASK.get()) {
			stack.mulPose(Axis.XN.rotationDegrees(15.0F));
			stack.translate(0.0F, -0.1F, 0.0F);
			stack.scale(1.5F, 1.2F, 1.5F);
		}
		if (this == RatsItemRegistry.RAT_FEZ.get()) {
			stack.translate(-0.05F, -0.15F, -0.1F);
			stack.scale(1.425F, 1.425F, 1.425F);
		}
		if (this == RatsItemRegistry.TOP_HAT.get()) {
			stack.mulPose(Axis.XN.rotationDegrees(5.0F));
			stack.translate(0.0F, -0.125F, 0.0F);
			stack.scale(1.425F, 1.425F, 1.425F);
		}
		if (this == RatsItemRegistry.SANTA_HAT.get()) {
			stack.mulPose(Axis.XN.rotationDegrees(5.0F));
			stack.translate(0.0F, 0.0F, 0.1F);
			stack.scale(1.25F, 1.25F, 1.25F);
		}
		if (this == RatlantisItemRegistry.MILITARY_HAT.get()) {
			stack.mulPose(Axis.XN.rotationDegrees(5.0F));
			stack.translate(0.0F, -0.1F, 0.0F);
			stack.scale(1.425F, 1.425F, 1.425F);
		}
		if (this == RatsItemRegistry.RAT_KING_CROWN.get()) {
			stack.mulPose(Axis.XN.rotationDegrees(5.0F));
			stack.translate(0.0F, -0.05F, 0.0F);
			stack.scale(1.25F, 1.25F, 1.25F);
		}
		if (this == RatlantisItemRegistry.AVIATOR_HAT.get()) {
			stack.scale(1.25F, 1.25F, 1.25F);
			stack.translate(0, -0.035F, 0.01F);
		}
	}

	public float getRatOffsetOnHead() {
		if (this == RatsItemRegistry.TOP_HAT.get()) {
			return -0.85F;
		} else if (this == RatlantisItemRegistry.MILITARY_HAT.get() || this == RatlantisItemRegistry.GHOST_PIRAT_HAT.get() || this == RatsItemRegistry.PIRAT_HAT.get()) {
			return -0.45F;
		}
		return 0.0F;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		String item = ForgeRegistries.ITEMS.getKey(this).getPath();
		if (!item.equals("air")) {
			return new ResourceLocation(RatsMod.MODID, "textures/model/hat/" + item + ".png").toString();
		}
		//hehe
		return "textures/particle/flea_0.png";
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
				return switch (ForgeRegistries.ITEMS.getKey(HatItem.this).getPath()) {
					case "chef_toque" ->
							new ChefToqueModel(Minecraft.getInstance().getEntityModels().bakeLayer(RatsModelLayers.CHEF_TOQUE));
					case "piper_hat" ->
							new PiperHatModel(Minecraft.getInstance().getEntityModels().bakeLayer(RatsModelLayers.PIPER_HAT));
					case "archeologist_hat" ->
							new ArcheologistHatModel(Minecraft.getInstance().getEntityModels().bakeLayer(RatsModelLayers.ARCHEOLOGIST_HAT));
					case "farmer_hat", "fisherman_hat" ->
							new FarmerHatModel(Minecraft.getInstance().getEntityModels().bakeLayer(RatsModelLayers.FARMER_HAT));
					case "rat_fez" ->
							new RatFezModel(Minecraft.getInstance().getEntityModels().bakeLayer(RatsModelLayers.FEZ));
					case "top_hat" ->
							new TopHatModel(Minecraft.getInstance().getEntityModels().bakeLayer(RatsModelLayers.TOP_HAT));
					case "santa_hat" ->
							new SantaHatModel(Minecraft.getInstance().getEntityModels().bakeLayer(RatsModelLayers.SANTA_HAT));
					case "halo_hat" ->
							new HaloHatModel(Minecraft.getInstance().getEntityModels().bakeLayer(RatsModelLayers.HALO));
					case "pirat_hat" ->
							new PiratHatModel(Minecraft.getInstance().getEntityModels().bakeLayer(RatsModelLayers.PIRATE_HAT));
					case "rat_king_crown" ->
							new CrownModel(Minecraft.getInstance().getEntityModels().bakeLayer(RatsModelLayers.CROWN));
					case "plague_doctor_mask", "black_death_mask" ->
							new PlagueDoctorMaskModel(Minecraft.getInstance().getEntityModels().bakeLayer(RatsModelLayers.PLAGUE_DOCTOR_MASK));
					case "exterminator_hat" ->
							new ExterminatorHatModel(Minecraft.getInstance().getEntityModels().bakeLayer(RatsModelLayers.EXTERMINATOR_HAT));
					case "aviator_hat" ->
							new AviatorHatModel(Minecraft.getInstance().getEntityModels().bakeLayer(RatsModelLayers.AVIATOR_HAT));
					case "ghost_pirat_hat" ->
							new GhostPiratHatModel(Minecraft.getInstance().getEntityModels().bakeLayer(RatsModelLayers.PIRATE_HAT));
					case "military_hat" ->
							new MilitaryHatModel(Minecraft.getInstance().getEntityModels().bakeLayer(RatsModelLayers.OFFICER_HAT));
					default -> original;
				};
			}
		});
	}
}
