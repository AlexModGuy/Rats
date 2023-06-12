package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.client.model.entity.RatModel;
import com.github.alexthe666.rats.client.render.entity.layer.RatHeldItemLayer;
import com.github.alexthe666.rats.registry.RatsLootRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.HoldsItemUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.TickRatUpgrade;
import com.github.alexthe666.rats.server.misc.RatsDateFetcher;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

import java.util.List;

public class ChristmasRatUpgradeItem extends BaseRatUpgradeItem implements TickRatUpgrade, HoldsItemUpgrade {
	public ChristmasRatUpgradeItem(Properties properties) {
		super(properties, 2, 3);
	}

	@Override
	public boolean isRatHoldingFood(TamedRat rat) {
		return false;
	}

	@Override
	public void tick(TamedRat rat) {
		if (!rat.level().isClientSide()) {
			if (RatsDateFetcher.isStartOfHour() && rat.randomEffectCooldown == 0) {
				//ensure we only fire the event once in the span of a minute. 1200 would work but I dont care
				rat.randomEffectCooldown = 2000;
				this.tryGiftgiving(rat);
				rat.level().broadcastEntityEvent(rat, (byte) 126);
			}
		}
	}

	private void tryGiftgiving(TamedRat rat) {
		ItemStack heldItem = rat.getMainHandItem();
		if (!rat.level().isClientSide()) {
			LootParams.Builder builder = new LootParams.Builder((ServerLevel) rat.level());
			builder.withLuck(1.0F);
			LootContextParamSet.Builder lootparameterset$builder = new LootContextParamSet.Builder();
			List<ItemStack> result = rat.level().getServer().getLootData().getLootTable(RatsLootRegistry.CHRISTMAS_GIFTS).getRandomItems(builder.create(lootparameterset$builder.build()));
			if (RatsDateFetcher.isChristmasDay()) {
				for (int i = 0; i < 5; i++) {
					result.addAll(rat.level().getServer().getLootData().getLootTable(RatsLootRegistry.CHRISTMAS_GIFTS).getRandomItems(builder.create(lootparameterset$builder.build())));
				}
			}
			if (!result.isEmpty()) {
				for (ItemStack stack : result) {
					if (heldItem.isEmpty()) {
						rat.setItemInHand(InteractionHand.MAIN_HAND, stack.copy());
					} else {
						if (!rat.tryDepositItemInContainers(stack.copy())) {
							if (!rat.level().isClientSide()) {
								rat.spawnAtLocation(stack.copy(), 0.25F);
							}
						}
					}
				}
			}
		}

	}

	@Override
	public void renderHeldItem(TamedRat rat, RatModel<?> model, PoseStack stack, MultiBufferSource buffer, int light, float ageInTicks) {
		stack.pushPose();
		this.translateToHand(model, false, stack);
		stack.mulPose(Axis.ZP.rotationDegrees(-10.0F));
		stack.mulPose(Axis.YP.rotationDegrees(1.0F));
		stack.mulPose(Axis.XP.rotationDegrees(90.0F));
		stack.pushPose();
		stack.translate(-0.025F, -0.2F, -0.05F);
		stack.scale(0.35F, 0.35F, 0.35F);
		VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(new ResourceLocation("textures/entity/chest/christmas.png")));
		RatHeldItemLayer.CHRISTMAS_CHEST_MODEL.renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.popPose();
		stack.popPose();
	}
}
