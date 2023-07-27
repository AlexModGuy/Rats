package com.github.alexthe666.rats.client.util;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EntityRenderingUtil {
	private static final Map<ResourceLocation, Entity> ENTITY_MAP = new HashMap<>();
	private static final Set<ResourceLocation> IGNORED_ENTITIES = new HashSet<>();

	@Nullable
	public static LivingEntity fetchEntity(@Nullable ResourceLocation entityName, @Nullable Level level) {
		if (entityName != null && level != null && !IGNORED_ENTITIES.contains(entityName)) {
			EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(entityName);
			if (type != null) {
				Entity entity;
				if (type == EntityType.PLAYER) {
					entity = Minecraft.getInstance().player;
				} else {
					entity = ENTITY_MAP.computeIfAbsent(entityName, t -> ForgeRegistries.ENTITY_TYPES.getValue(t).create(level));
				}
				if (entity instanceof LivingEntity living) {
					return living;
				} else {
					addEntityToBlacklist(entityName);
				}
			}
		}
		return null;
	}

	public static int getAdjustedMobScale(ResourceLocation entityName) {
		LivingEntity entity = fetchEntity(entityName, Minecraft.getInstance().level);
		if (entity != null) {
			int scale = 35;
			float height = entity.getBbHeight();
			float width = entity.getBbWidth();
			if (height > 2.0F || width > 2.0F) {
				scale = (int) (64 / Math.max(height, width));
			}
			return scale;
		}
		return 0;
	}

	public static void addEntityToBlacklist(ResourceLocation entityName) {
		IGNORED_ENTITIES.add(entityName);
		ENTITY_MAP.remove(entityName);
	}

	public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, @Nullable LivingEntity entity, boolean rotating) {
		if (entity != null) {
			float rotate = (Minecraft.getInstance().getPartialTick() + Minecraft.getInstance().player.tickCount) * 2F;
			float f = (float) Math.atan(mouseX / 40.0F);
			float f1 = (float) Math.atan(mouseY / 40.0F);
			PoseStack posestack = RenderSystem.getModelViewStack();
			posestack.pushPose();
			posestack.translate(posX, posY, 1050.0D);
			posestack.scale(1.0F, 1.0F, -1.0F);
			RenderSystem.applyModelViewMatrix();
			PoseStack matrixstack = new PoseStack();
			matrixstack.translate(0.0D, 0.0D, 1000.0D);
			matrixstack.scale((float) scale, (float) scale, (float) scale);
			Quaternionf quaternion = Axis.ZP.rotationDegrees(180.0F);
			Quaternionf quaternion1 = Axis.XP.rotationDegrees(f1 * 20.0F);
			Quaternionf quaternion2 = Axis.YP.rotationDegrees(rotate);
			quaternion.mul(quaternion1);
			matrixstack.mulPose(quaternion);
			if (rotating) matrixstack.mulPose(quaternion2);
			float f2 = entity.yBodyRot;
			float f3 = entity.getYRot();
			float f4 = entity.getXRot();
			float f5 = entity.yHeadRotO;
			float f6 = entity.yHeadRot;
			entity.yBodyRot = 180.0F + f * 20.0F;
			entity.setYRot(180.0F + f * 40.0F);
			entity.setXRot(-f1 * 20.0F);
			entity.yHeadRot = entity.getYRot();
			entity.yHeadRotO = entity.getYRot();
			Lighting.setupForEntityInInventory();
			EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
			quaternion1.conjugate();
			dispatcher.overrideCameraOrientation(quaternion1);
			dispatcher.setRenderShadow(false);
			MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();
			RenderSystem.runAsFancy(() -> dispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, source, 15728880));
			source.endBatch();
			dispatcher.setRenderShadow(true);
			entity.yBodyRot = f2;
			entity.setYRot(f3);
			entity.setXRot(f4);
			entity.yHeadRotO = f5;
			entity.yHeadRot = f6;
			posestack.popPose();
			RenderSystem.applyModelViewMatrix();
			Lighting.setupFor3DItems();
		}
	}
}
