package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.entity.AbstractRatModel;
import com.github.alexthe666.rats.client.model.entity.BiplaneModel;
import com.github.alexthe666.rats.client.render.entity.layer.PartyHatLayer;
import com.github.alexthe666.rats.client.render.entity.layer.RatHeldItemLayer;
import com.github.alexthe666.rats.client.render.entity.layer.RatHelmetLayer;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.misc.RattlingGun;
import com.github.alexthe666.rats.server.entity.monster.boss.RatBaronPlane;
import com.github.alexthe666.rats.server.entity.mount.RatBiplaneMount;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public abstract class AbstractRatRenderer<T extends AbstractRat, M extends AbstractRatModel<T>> extends MobRenderer<T, M> {

	public AbstractRatRenderer(EntityRendererProvider.Context context, M model) {
		super(context, model, 0.15F);
		this.addLayer(new RatHelmetLayer<>(this, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))));
		this.addLayer(new RatHeldItemLayer<>(this));
		this.addLayer(new PartyHatLayer<>(this, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))));
	}

	@Override
	public boolean shouldRender(T rat, Frustum camera, double camX, double camY, double camZ) {
		if (rat.isPassenger() && rat.getVehicle() != null && rat.getVehicle().getPassengers().size() >= 1 && rat.getVehicle().getPassengers().get(0) == rat && rat.getVehicle() instanceof LivingEntity living) {
			if (living.getItemBySlot(EquipmentSlot.HEAD).is(RatsItemRegistry.CHEF_TOQUE.get())) {
				return false;
			}
		}
		return super.shouldRender(rat, camera, camX, camY, camZ);
	}

	@Override
	protected void scale(T rat, PoseStack stack, float partialTicks) {
		stack.scale(0.6F, 0.6F, 0.6F);
		if (rat.isPassenger() && rat.getVehicle() != null && rat.getVehicle().getPassengers().size() >= 1) {
			if (rat.getVehicle() != null) {
				if (rat.getVehicle() instanceof Player) {
					Entity riding = rat.getVehicle();
					if (riding.getPassengers().get(0) != null && riding.getPassengers().get(0) == rat) {
						EntityRenderer<?> playerRender = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(riding);
						if (playerRender instanceof LivingEntityRenderer<?, ?> renderer && renderer.getModel() instanceof HumanoidModel<?> human) {
							human.getHead().translateAndRotate(stack);
							stack.translate(0.0F, -0.7F, 0.25F);
						}
					}
				}
				if (rat.getVehicle() instanceof RattlingGun) {
					Entity riding = rat.getVehicle();
					if (riding.getPassengers().get(0) != null && riding.getPassengers().get(0) == rat) {
						EntityRenderer<?> playerRender = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(riding);
						if (playerRender instanceof LivingEntityRenderer<?, ?> renderer && renderer.getModel() instanceof HumanoidModel) {
							RattlingGunRenderer.GUN_MODEL.pivot.translateRotate(stack);
						}
					}
				}
				if (rat.getVehicle() instanceof RatBaronPlane || rat.getVehicle() instanceof RatBiplaneMount) {
					Entity riding = rat.getVehicle();
					if (riding.getPassengers().get(0) != null && riding.getPassengers().get(0) == rat) {
						EntityRenderer<?> playerRender = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(riding);
						if (playerRender instanceof LivingEntityRenderer<?, ?> renderer && renderer.getModel() instanceof BiplaneModel<?> plane) {
							stack.translate(0.0F, -0.1F, 0.45F);
							plane.body1.translateRotate(stack);
						}
					}
				}
			}
		}
	}

	@Override
	protected float getFlipDegrees(T rat) {
		return rat.isDeadInTrap() ? 0 : 90;
	}

	@Override
	public ResourceLocation getTextureLocation(T rat) {
		return rat.getColorVariant().getTexture();
	}
}
