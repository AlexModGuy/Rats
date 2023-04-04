package com.github.alexthe666.rats.client.events;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.StaticRatModel;
import com.github.alexthe666.rats.client.render.RatsRenderType;
import com.github.alexthe666.rats.client.render.entity.RatProtectorRenderer;
import com.github.alexthe666.rats.client.util.RatsIconRenderUtil;
import com.github.alexthe666.rats.registry.*;
import com.github.alexthe666.rats.registry.worldgen.RatlantisDimensionRegistry;
import com.github.alexthe666.rats.server.block.entity.RatQuarryBlockEntity;
import com.github.alexthe666.rats.server.capability.SelectedRatCapability;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.events.ForgeEvents;
import com.github.alexthe666.rats.server.items.RatStaffItem;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = RatsMod.MODID, value = Dist.CLIENT)
public class ForgeClientEvents {

	public static final ResourceLocation PLAGUE_HEART_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/gui/plague_hearts.png");
	private static final ResourceLocation RADIUS_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/rat/rat_radius.png");
	private static final ResourceLocation QUARRY_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/model/quarry_radius.png");
	private static final ResourceLocation HOME_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/rat/rat_home.png");
	private static final ResourceLocation RAT_DEPOSIT_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/rat/rat_deposit.png");
	private static final ResourceLocation RAT_PICKUP_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/rat/rat_pickup.png");
	private static final ResourceLocation RAT_PATROL_NODE_TEXTURE = new ResourceLocation("rats:textures/entity/rat/rat_patrol.png");
	private static final ResourceLocation SYNESTHESIA = new ResourceLocation(RatsMod.MODID, "shaders/post/synesthesia.json");
	private static float synesthesiaProgress = 0;
	private static float prevSynesthesiaProgress = 0;
	private static final float MAX_SYNESTESIA = 40;
	private static final StaticRatModel<LivingEntity> RAT_MODEL = new StaticRatModel<>(0, false);

	@SubscribeEvent
	public static void adjustSynesthesiaFOV(ViewportEvent.ComputeFov event) {
		if (RatConfig.synesthesiaShader) {
			if (prevSynesthesiaProgress > 0) {
				float prog = (prevSynesthesiaProgress + (synesthesiaProgress - prevSynesthesiaProgress) * Minecraft.getInstance().getPartialTick());
				float renderProg;
				if (prevSynesthesiaProgress <= synesthesiaProgress) {
					renderProg = (float) Math.sin(prog / MAX_SYNESTESIA * Math.PI) * 40.0F;
				} else {
					renderProg = -(float) Math.sin(prog / MAX_SYNESTESIA * Math.PI) * 40.0F;
				}
				event.setFOV(event.getFOV() + renderProg);
			}
		}
	}

	@SubscribeEvent
	public static void changeRatlantisBowFov(ComputeFovModifierEvent event) {
		Player player = event.getPlayer();
		if (player.isUsingItem()) {
			if (player.getUseItem().is(RatlantisItemRegistry.RATLANTIS_BOW.get())) {
				float f = player.getTicksUsingItem() / 10.0F;
				f = f > 1.0F ? 1.0F : f * f;
				event.setNewFovModifier(event.getFovModifier() * (1.0F - f * 0.15F));
			}
		}
	}

	@SubscribeEvent
	public static void removeAutomatonHeadOutline(RenderHighlightEvent.Block event) {
		BlockState state = event.getCamera().getEntity().getLevel().getBlockState(event.getTarget().getBlockPos());
		if (state.is(RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD.get())) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
		if (RatConfig.synesthesiaShader) {
			if (event.getEntity() == Minecraft.getInstance().player) {
				GameRenderer renderer = Minecraft.getInstance().gameRenderer;
				MobEffectInstance active = event.getEntity().getEffect(RatsEffectRegistry.CONFIT_BYALDI.get());
				boolean synesthesia = active != null;
				try {
					if (synesthesia && renderer.currentEffect() == null) {
						renderer.loadEffect(SYNESTHESIA);
					}
					if (!synesthesia && renderer.currentEffect() != null && SYNESTHESIA.toString().equals(Objects.requireNonNull(renderer.currentEffect()).getName())) {
						renderer.shutdownEffect();
					}
				} catch (Exception e) {
					RatsMod.LOGGER.warn("Game tried to crash when applying shader");
				}

				if (prevSynesthesiaProgress == 2 && synesthesia) {
					event.getEntity().getLevel().playLocalSound(event.getEntity().blockPosition(), RatsSoundRegistry.POTION_EFFECT_BEGIN.get(), SoundSource.NEUTRAL, 16.0F, 1.0F, false);
				}
				if (prevSynesthesiaProgress == 38 && !synesthesia) {
					event.getEntity().getLevel().playLocalSound(event.getEntity().blockPosition(), RatsSoundRegistry.POTION_EFFECT_END.get(), SoundSource.NEUTRAL, 16.0F, 1.0F, false);
				}
				prevSynesthesiaProgress = synesthesiaProgress;
				if (synesthesia && synesthesiaProgress < MAX_SYNESTESIA) {
					synesthesiaProgress += 2F;
				} else if (!synesthesia && synesthesiaProgress > 0.0F) {
					synesthesiaProgress -= 2F;
				}
			}
		}
	}

	@SubscribeEvent
	public static void unrenderHatLayerWithMask(RenderLivingEvent.Pre<?, ?> event) {
		ItemStack stack = event.getEntity().getItemBySlot(EquipmentSlot.HEAD);
		boolean visible = !stack.is(RatsItemRegistry.BLACK_DEATH_MASK.get()) && !stack.is(RatsItemRegistry.PLAGUE_DOCTOR_MASK.get()) && !stack.is(RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD.get().asItem());

		if (!visible && event.getRenderer().getModel() instanceof HumanoidModel<?> humanoidModel && event.getRenderer().getModel() instanceof HeadedModel) {
			humanoidModel.hat.visible = false;
		}
	}

	@SubscribeEvent
	public static void addTooltipWhenRatlantisIsDisabled(ItemTooltipEvent event) {
		//only fire if actually loaded into a world, because otherwise the ratlantis flag won't be accurate
		if (event.getEntity() != null) {
			if (!RatsMod.RATLANTIS_DATAPACK_ENABLED) {
				if (!RatsMod.RATLANTIS_ITEMS.isEmpty() && RatsMod.RATLANTIS_ITEMS.contains(event.getItemStack().getItem())) {
					event.getToolTip().clear();
					event.getToolTip().add(Component.empty().append(event.getItemStack().getHoverName()).withStyle(event.getItemStack().getRarity().getStyleModifier()));
					event.getToolTip().add(Component.translatable("item.rats.ratlantis_disabled.desc0").withStyle(ChatFormatting.DARK_RED));
					event.getToolTip().add(Component.translatable("item.rats.ratlantis_disabled.desc1").withStyle(ChatFormatting.GRAY));
					event.getToolTip().add(Component.translatable("item.rats.ratlantis_disabled.desc2").withStyle(ChatFormatting.GRAY));
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
		Player player = Minecraft.getInstance().player;
		if (player == null || player.isCreative() || player.isSpectator()) return;
		if (event.getOverlay() != VanillaGuiOverlay.PLAYER_HEALTH.type() || event.isCanceled() || !player.hasEffect(RatsEffectRegistry.PLAGUE.get()) || !RatConfig.plagueHearts) {
			return;
		}
		PoseStack stack = event.getPoseStack();
		int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();
		int leftHeight = 39;
		RenderSystem.setShaderTexture(0, PLAGUE_HEART_TEXTURE);
		RenderSystem.enableBlend();
		int health = Mth.ceil(player.getHealth());
		Gui gui = Minecraft.getInstance().gui;
		boolean highlight = gui.healthBlinkTime > (long) gui.getGuiTicks() && (gui.healthBlinkTime - (long) gui.getGuiTicks()) / 3L % 2L == 1L;

		long j = Util.getMillis();
		if (health < gui.lastHealth && player.invulnerableTime > 0) {
			gui.lastHealthTime = j;
			gui.healthBlinkTime = gui.getGuiTicks() + 20;
		} else if (health > gui.lastHealth && player.invulnerableTime > 0) {
			gui.lastHealthTime = j;
			gui.healthBlinkTime = gui.getGuiTicks() + 10;
		}

		if (j - gui.lastHealthTime > 1000L) {
			gui.displayHealth = health;
			gui.lastHealthTime = j;
		}

		gui.lastHealth = health;
		int healthLast = gui.displayHealth;
		gui.random.setSeed(gui.getGuiTicks() * 312871L);

		AttributeInstance attrMaxHealth = player.getAttribute(Attributes.MAX_HEALTH);
		float healthMax = Math.max((float) attrMaxHealth.getValue(), Math.max(healthLast, health));
		int absorption = Mth.ceil(player.getAbsorptionAmount());

		int healthRows = Mth.ceil((healthMax + absorption) / 2.0F / 10.0F);
		int rowHeight = Math.max(10 - (healthRows - 2), 3);

		int left = width / 2 - 91;
		int top = height - leftHeight;

		int regen = -1;
		if (player.hasEffect(MobEffects.REGENERATION)) {
			regen = gui.getGuiTicks() % Mth.ceil(healthMax + 5.0F);
		}

		int healthAmount = Mth.ceil((double) healthMax / 2.0D);

		for (int currentHeart = healthAmount + absorption - 1; currentHeart >= 0; --currentHeart) {
			int x = left + currentHeart % 10 * 8;
			int y = top - currentHeart / 10 * rowHeight;
			if (health + absorption <= 4) {
				y += gui.random.nextInt(2);
			}

			if (currentHeart < healthAmount && currentHeart == regen) {
				y -= 2;
			}

			drawTexturedModalRect(stack, x, y, 0, 9);
			int fullHealth = currentHeart * 2;
			if (currentHeart >= healthAmount) {
				int k2 = fullHealth - health * 2;
				if (k2 < absorption) {
					boolean halfHeart = k2 + 1 == absorption;
					drawTexturedModalRect(stack, x, y, halfHeart ? 9 : 0, 0);
				}
			}

			if (highlight && fullHealth < healthLast) {
				boolean halfHeart = fullHealth + 1 == healthLast;
				drawTexturedModalRect(stack, x, y, halfHeart ? 9 : 0, 0);
			}

			if (fullHealth < health) {
				boolean halfHeart = fullHealth + 1 == health;
				drawTexturedModalRect(stack, x, y, halfHeart ? 9 : 0, 0);
			}
		}

		RenderSystem.disableBlend();
		Minecraft.getInstance().getProfiler().endTick();
	}

	private static void drawTexturedModalRect(PoseStack stack, int x, int y, int textureX, int textureY) {
		GuiComponent.blit(stack, x, y, textureX, textureY, 9, 9);
	}


	@SubscribeEvent
	public static void onFogColors(ViewportEvent.ComputeFogColor event) {
		ClientLevel level = Minecraft.getInstance().level;
		if (level != null && level.dimension().equals(RatlantisDimensionRegistry.DIMENSION_KEY)) {
			float f12 = Mth.clamp(Mth.cos(level.getTimeOfDay((float) event.getPartialTick()) * ((float) Math.PI * 2F)) * 2.0F + 0.5F, 0.0F, 1.0F);
			FluidState fluidstate = event.getCamera().getBlockAtCamera().getFluidState();
			if (!fluidstate.is(FluidTags.WATER)) {
				event.setRed((f12));
				event.setGreen((f12));
				event.setBlue(f12 * 0.7F);
			}
		}
	}

	@SubscribeEvent
	public static <T extends LivingEntity, M extends EntityModel<T>> void onLivingRender(RenderLivingEvent.Post<T, M> event) {
		if (event.getEntity() instanceof Player) {
			PoseStack stack = event.getPoseStack();
			int protectorCount = ForgeEvents.getProtectorCount(event.getEntity());
			VertexConsumer textureBuilder = event.getMultiBufferSource().getBuffer(RatsRenderType.getGlowingTranslucent(RatProtectorRenderer.BASE_TEXTURE));
			for (int i = 0; i < protectorCount; i++) {
				float tick = (float) (event.getEntity().tickCount - 1) + event.getPartialTick();
				float offsetRot = 30 + 360 * (i / (float) protectorCount);
				float bob = (float) ((Math.sin(tick * 0.1F) * 0.2F + Math.cos(tick * 0.4F + i)) * 0.2);
				float scale = 0.4F;
				float rotation = Mth.wrapDegrees((tick * 8) % 360.0F + offsetRot);
				stack.pushPose();
				stack.mulPose(Axis.YP.rotationDegrees(rotation));
				stack.translate(0.0D, event.getEntity().getBbHeight() + 0.5D + bob, event.getEntity().getBbWidth() + 0.5F);
				stack.pushPose();
				stack.mulPose(Axis.YP.rotationDegrees(90));
				stack.mulPose(Axis.XP.rotationDegrees(75.0F));
				stack.scale(scale, scale, scale);
				stack.mulPose(Axis.XP.rotationDegrees(90.0F));
				float f = (event.getEntity().tickCount + event.getPartialTick()) * 0.5F;
				float f1 = 1;
				RAT_MODEL.setupAnim(event.getEntity(), f, f1, event.getEntity().tickCount + event.getPartialTick(), event.getPartialTick(), 0);
				RAT_MODEL.renderToBuffer(event.getPoseStack(), textureBuilder, event.getPackedLight(), OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
				stack.popPose();
				stack.popPose();
			}
		}
	}

	@SubscribeEvent
	public static void onRenderWorld(RenderLevelStageEvent event) {
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
			if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.getCapability(RatsCapabilityRegistry.SELECTED_RAT).resolve().isPresent()) {
				TamedRat rat = Minecraft.getInstance().player.getCapability(RatsCapabilityRegistry.SELECTED_RAT).resolve().get().getSelectedRat();
				if (rat == null) return;
				ItemStack heldItem = Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND);
				Tesselator tessellator = Tesselator.getInstance();
				BufferBuilder buffer = tessellator.getBuilder();
				PoseStack stack = event.getPoseStack();
				float bob = 1.5F + 0.3F * (Mth.sin((event.getPartialTick() + Minecraft.getInstance().player.tickCount) * 0.1F) + 1F);
				final Vec3 viewPosition = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();
				double px = viewPosition.x;
				double py = viewPosition.y;
				double pz = viewPosition.z;
				if (heldItem.is(RatsItemRegistry.CHEESE_STICK.get())) {
					float finalBob = bob;
					rat.getDepositPos().ifPresent(pos -> {
						if (pos.dimension().equals(rat.getLevel().dimension())) {
							RatsIconRenderUtil.renderPOIIcon(RAT_DEPOSIT_TEXTURE, viewPosition, pos.pos(), finalBob, stack, buffer, tessellator);
						}
					});
					rat.getPickupPos().ifPresent(pos -> {
						if (pos.dimension().equals(rat.getLevel().dimension())) {
							RatsIconRenderUtil.renderPOIIcon(RAT_PICKUP_TEXTURE, viewPosition, pos.pos(), finalBob, stack, buffer, tessellator);
						}
					});
					rat.getHomePoint().ifPresent(pos -> {
						if (pos.dimension().equals(rat.getLevel().dimension())) {
							RatsIconRenderUtil.renderPOIIcon(HOME_TEXTURE, viewPosition, pos.pos(), finalBob, stack, buffer, tessellator);
						}
					});
					if (Minecraft.getInstance().hitResult != null && Minecraft.getInstance().hitResult.getType() == HitResult.Type.BLOCK) {
						BlockHitResult over = (BlockHitResult) Minecraft.getInstance().hitResult;
						if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.getBlockState(over.getBlockPos()).is(RatsBlockRegistry.RAT_QUARRY.get())) {
							if (Minecraft.getInstance().level.getBlockEntity(over.getBlockPos()) instanceof RatQuarryBlockEntity quarry) {
								BlockPos blockPos = quarry.getBlockPos().offset(-quarry.getRadius(), 0, -quarry.getRadius());
								AABB aabb = new AABB(0, -0.05, 0, 1 + quarry.getRadius() * 2, 0.1, 1 + quarry.getRadius() * 2);
								RatsIconRenderUtil.renderBox(QUARRY_TEXTURE, viewPosition, Vec3.atLowerCornerOf(blockPos), aabb, stack);
							}
						}
					}
				} else if (heldItem.is(RatsItemRegistry.RADIUS_STICK.get())) {
					BlockPos blockPos = rat.getSearchCenter();
					Vec3 renderCenter = new Vec3(blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D);
					double renderRadius = rat.getRadius();
					AABB aabb = new AABB(-renderRadius, -renderRadius, -renderRadius, renderRadius, renderRadius, renderRadius);
					RatsIconRenderUtil.renderBox(RADIUS_TEXTURE, viewPosition, renderCenter, aabb, stack);
				} else if (heldItem.is(RatsItemRegistry.PATROL_STICK.get())) {
					bob = 1.5F + 0.05F * (Mth.sin((event.getPartialTick() + (float) Minecraft.getInstance().player.tickCount) * 0.1F) + 1.0F);

					for (int i = 0; i < rat.getPatrolNodes().size(); ++i) {
						GlobalPos node = rat.getPatrolNodes().get(i);
						float r = 0.6F;
						float g = 0.1F;
						float b = 0.1F;
						GlobalPos prev;
						if (i > 0) {
							prev = rat.getPatrolNodes().get(i - 1);
						} else {
							prev = rat.getPatrolNodes().get(rat.getPatrolNodes().size() - 1);
							r = 0.5F;
							g = 0.3F;
						}

						if (node.dimension().equals(Minecraft.getInstance().player.getLevel().dimension()) && prev.dimension().equals(Minecraft.getInstance().player.getLevel().dimension())) {
							stack.pushPose();
							stack.translate(-px, -py, -pz);
							stack.translate((float) prev.pos().getX() + 0.5F, (float) prev.pos().getY() + bob - 0.25F, (float) prev.pos().getZ() + 0.5F);
							float pdx = (float) (node.pos().getX() - prev.pos().getX());
							float pdy = (float) (node.pos().getY() - prev.pos().getY());
							float pdz = (float) (node.pos().getZ() - prev.pos().getZ());
							buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
							Matrix4f matrix4f = stack.last().pose();
							buffer.vertex(matrix4f, pdx, pdy, pdz).color(r, g, b, 1.0F).endVertex();
							buffer.vertex(matrix4f, 0.0F, 0.0F, 0.0F).color(r, g, b, 1.0F).endVertex();
							tessellator.end();
							stack.popPose();
						}

						if (node.dimension().equals(Minecraft.getInstance().player.getLevel().dimension())) {
							RatsIconRenderUtil.renderPOIIcon(RAT_PATROL_NODE_TEXTURE, viewPosition, node.pos(), bob, stack, buffer, tessellator);
						}
					}
				}
			}
		}
	}

	public static boolean isRatSelectedOnStaff(TamedRat rat) {
		if (Minecraft.getInstance().player != null) {
			LocalPlayer player = Minecraft.getInstance().player;
			if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof RatStaffItem || player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof RatStaffItem) {
				LazyOptional<SelectedRatCapability> cap = player.getCapability(RatsCapabilityRegistry.SELECTED_RAT);
				return cap.resolve().isPresent() && Objects.equals(cap.resolve().get().getSelectedRat(), rat);
			}
		}
		return false;
	}
}
