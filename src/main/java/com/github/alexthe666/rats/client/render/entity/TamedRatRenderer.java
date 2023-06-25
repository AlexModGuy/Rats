package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.events.ModClientEvents;
import com.github.alexthe666.rats.client.model.entity.PinkieModel;
import com.github.alexthe666.rats.client.model.entity.RatModel;
import com.github.alexthe666.rats.client.render.entity.layer.TamedRatEyesLayer;
import com.github.alexthe666.rats.client.render.entity.layer.TamedRatOverlayLayer;
import com.github.alexthe666.rats.registry.RatVariantRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesTextureUpgrade;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import com.github.alexthe666.rats.server.misc.RatVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class TamedRatRenderer extends AbstractRatRenderer<TamedRat> {

	private static final RatModel<TamedRat> RAT_MODEL = new RatModel<>(0.0F);
	private static final PinkieModel<TamedRat> PINKIE_MODEL = new PinkieModel<>();
	private static final ResourceLocation PINKIE_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/rat/baby.png");

	public TamedRatRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.addLayer(new TamedRatOverlayLayer(this));
		this.addLayer(new TamedRatEyesLayer(this));
	}

	@Override
	protected boolean shouldShowName(TamedRat entity) {
		return ModClientEvents.shouldRenderNameplates() && super.shouldShowName(entity);
	}

	@Override
	public void render(TamedRat entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		if (entity.isBaby()) {
			this.model = PINKIE_MODEL;
		} else {
			this.model = RAT_MODEL;
		}
		super.render(entity, entityYaw, partialTicks, stack, buffer, light);
	}

	@Override
	public ResourceLocation getTextureLocation(TamedRat entity) {
		if (entity.isBaby()) {
			return PINKIE_TEXTURE;
		} else {
			AtomicReference<String> upgradeTex = new AtomicReference<>("");

			RatUpgradeUtils.forEachUpgrade(entity, item -> item instanceof ChangesTextureUpgrade, stack -> upgradeTex.set(((ChangesTextureUpgrade) stack.getItem()).getTexture().toString()));

			if (!upgradeTex.get().equals("")) {
				return new ResourceLocation(upgradeTex.get());
			}

			if (entity.hasCustomName()) {
				for (RatVariant variant : RatVariantRegistry.RAT_VARIANT_REGISTRY.get()) {
					if (Objects.requireNonNull(entity.getCustomName()).getString().equalsIgnoreCase(variant.getName())) {
						return variant.getTexture();
					}
				}
			}

			return super.getTextureLocation(entity);
		}
	}
}
