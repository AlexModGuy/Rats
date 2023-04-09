package com.github.alexthe666.rats.client.render.block;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.RatsModelLayers;
import com.github.alexthe666.rats.client.model.deco.*;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.block.RatCageDecoratedBlock;
import com.github.alexthe666.rats.server.block.entity.DecoratedRatCageBlockEntity;
import com.github.alexthe666.rats.server.block.entity.RatCageWheelBlockEntity;
import com.github.alexthe666.rats.server.items.RatHammockItem;
import com.github.alexthe666.rats.server.items.RatIglooItem;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class DecoratedRatCageRenderer implements BlockEntityRenderer<DecoratedRatCageBlockEntity> {
	private final RatIglooModel<?> igloo;
	private final RatHammockModel<?> hammock;
	private final RatWaterBottleModel<?> water_bottle;
	private final RatSeedBowlModel<?> seed_bowl;
	private static final RatBreedingLanternModel<?> MODEL_RAT_BREEDING_LANTERN = new RatBreedingLanternModel<>();
	private static final RatWheelModel<?> MODEL_RAT_WHEEL = new RatWheelModel<>();
	private static final RenderType TEXTURE_RAT_IGLOO = RenderType.entityTranslucent(new ResourceLocation(RatsMod.MODID, "textures/model/rat_igloo.png"));
	private static final RenderType TEXTURE_RAT_HAMMOCK = RenderType.entityTranslucent(new ResourceLocation(RatsMod.MODID, "textures/model/rat_hammock_0.png"));
	private static final RenderType TEXTURE_RAT_WATER_BOTTLE = RenderType.entityTranslucent(new ResourceLocation(RatsMod.MODID, "textures/model/rat_water_bottle.png"));
	private static final RenderType TEXTURE_RAT_SEED_BOWL = RenderType.entityTranslucent(new ResourceLocation(RatsMod.MODID, "textures/model/rat_seed_bowl.png"));
	private static final RenderType TEXTURE_RAT_BREEDING_LANTERN = RenderType.entityTranslucent(new ResourceLocation(RatsMod.MODID, "textures/model/rat_breeding_lantern.png"));
	private static final RenderType TEXTURE_RAT_WHEEL = RenderType.entityTranslucent(new ResourceLocation(RatsMod.MODID, "textures/model/rat_wheel.png"));

	public DecoratedRatCageRenderer(BlockEntityRendererProvider.Context context) {
		this.igloo = new RatIglooModel<>(context.bakeLayer(RatsModelLayers.IGLOO));
		this.hammock = new RatHammockModel<>(context.bakeLayer(RatsModelLayers.HAMMOCK));
		this.water_bottle = new RatWaterBottleModel<>(context.bakeLayer(RatsModelLayers.WATER_BOTTLE));
		this.seed_bowl = new RatSeedBowlModel<>(context.bakeLayer(RatsModelLayers.SEED_BOWL));
	}

	@Override
	public void render(DecoratedRatCageBlockEntity entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		BlockState blockstate = entity.getBlockState();
		stack.pushPose();
		stack.translate(0.5D, 1.5D, 0.5D);
		float f = blockstate.getValue(RatCageDecoratedBlock.FACING).getClockWise().toYRot() + 90;
		stack.mulPose(Axis.YP.rotationDegrees(-f));
		stack.mulPose(Axis.ZP.rotationDegrees(180));
		ItemStack containedItem = ItemStack.EMPTY;
		if (entity.getLevel() != null) {
			containedItem = entity.getContainedItem();
		}
		if (containedItem.getItem() instanceof RatIglooItem igloo) {
			DyeColor color = igloo.color;
			VertexConsumer consumer = buffer.getBuffer(TEXTURE_RAT_IGLOO);
			this.igloo.renderToBuffer(stack, consumer, light, overlay, color.getTextureDiffuseColors()[0], color.getTextureDiffuseColors()[1], color.getTextureDiffuseColors()[2], 1.0F);
		}
		if (containedItem.getItem() instanceof RatHammockItem hammock) {
			VertexConsumer consumer = buffer.getBuffer(TEXTURE_RAT_HAMMOCK);
			stack.pushPose();
			DyeColor color = hammock.color;
			this.hammock.renderToBuffer(stack, consumer, light, overlay, color.getTextureDiffuseColors()[0], color.getTextureDiffuseColors()[1], color.getTextureDiffuseColors()[2], 1.0F);
			stack.popPose();
		}

		if (containedItem.is(RatsItemRegistry.RAT_WATER_BOTTLE.get())) {
			VertexConsumer consumer = buffer.getBuffer(TEXTURE_RAT_WATER_BOTTLE);
			this.water_bottle.renderToBuffer(stack, consumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		}

		if (containedItem.is(RatsItemRegistry.RAT_SEED_BOWL.get())) {
			VertexConsumer consumer = buffer.getBuffer(TEXTURE_RAT_SEED_BOWL);
			this.seed_bowl.renderToBuffer(stack, consumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		}

		if (containedItem.is(RatsItemRegistry.RAT_WHEEL.get())) {
			VertexConsumer consumer = buffer.getBuffer(TEXTURE_RAT_WHEEL);
			if (entity instanceof RatCageWheelBlockEntity) {
				MODEL_RAT_WHEEL.animate((RatCageWheelBlockEntity) entity, partialTicks);
			}

			MODEL_RAT_WHEEL.renderToBuffer(stack, consumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		}

		if (containedItem.is(RatsItemRegistry.RAT_BREEDING_LANTERN.get())) {
			VertexConsumer consumer = buffer.getBuffer(TEXTURE_RAT_BREEDING_LANTERN);
			MODEL_RAT_BREEDING_LANTERN.renderToBuffer(stack, consumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
			MODEL_RAT_BREEDING_LANTERN.swingChain();
		}
		stack.popPose();
	}
}
