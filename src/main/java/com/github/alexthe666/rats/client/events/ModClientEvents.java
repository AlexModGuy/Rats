package com.github.alexthe666.rats.client.events;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.gui.*;
import com.github.alexthe666.rats.client.model.CubeModel;
import com.github.alexthe666.rats.client.model.RatsModelLayers;
import com.github.alexthe666.rats.client.model.deco.RatHammockModel;
import com.github.alexthe666.rats.client.model.deco.RatIglooModel;
import com.github.alexthe666.rats.client.model.deco.RatSeedBowlModel;
import com.github.alexthe666.rats.client.model.deco.RatWaterBottleModel;
import com.github.alexthe666.rats.client.model.entity.*;
import com.github.alexthe666.rats.client.model.hats.*;
import com.github.alexthe666.rats.client.particle.*;
import com.github.alexthe666.rats.client.render.NuggetColorRegister;
import com.github.alexthe666.rats.client.render.RatsRenderType;
import com.github.alexthe666.rats.client.render.block.*;
import com.github.alexthe666.rats.client.render.entity.*;
import com.github.alexthe666.rats.client.render.entity.layer.PartyHatLayer;
import com.github.alexthe666.rats.client.render.entity.layer.PlagueLayer;
import com.github.alexthe666.rats.registry.*;
import com.github.alexthe666.rats.server.block.entity.RatTubeBlockEntity;
import com.github.alexthe666.rats.server.entity.misc.PiratWoodBoat;
import com.github.alexthe666.rats.server.items.*;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.FoliageColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

//the mod event class mostly stores registry event things, such as registering renderers, item and block colors, shaders, and layer definitions.
//for Forge events, use ForgeClientEvents
@Mod.EventBusSubscriber(modid = RatsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {

	private static ShaderInstance rendertypeRatlantisPortalShader;

	public static boolean shouldRenderNameplates() {
		return Minecraft.getInstance().screen == null || !(Minecraft.getInstance().screen instanceof RatScreen) && !(Minecraft.getInstance().screen instanceof CheeseStaffScreen);
	}

	public static void openMobFilterScreen(ItemStack upgrade) {
		Minecraft.getInstance().setScreen(new MobFilterScreen(upgrade));
	}

	@SubscribeEvent
	public static void setupShaders(RegisterShadersEvent event) throws IOException {
		ResourceProvider provider = event.getResourceProvider();
		event.registerShader(new ShaderInstance(provider, new ResourceLocation(RatsMod.MODID, "rendertype_ratlantis_portal"), DefaultVertexFormat.POSITION_COLOR), instance -> rendertypeRatlantisPortalShader = instance);
	}

	public static ShaderInstance getRendertypeRatlantisPortalShader() {
		return rendertypeRatlantisPortalShader;
	}

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ItemProperties.register(RatsItemRegistry.RAT_SACK.get(), new ResourceLocation("rat_count"), (stack, level, entity, i) -> Math.min(3, RatSackItem.getRatsInSack(stack)));

			ItemProperties.register(RatlantisItemRegistry.RATLANTIS_BOW.get(), new ResourceLocation("pull"), (stack, level, living, i) -> {
				if (living == null) {
					return 0.0F;
				} else {
					return living.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration() - living.getUseItemRemainingTicks()) / 10.0F;
				}
			});

			ItemProperties.register(RatlantisItemRegistry.RATLANTIS_BOW.get(), new ResourceLocation("pulling"), (stack, level, living, i) -> living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0F : 0.0F);

			ItemProperties.register(RatsItemRegistry.RATBOW_ESSENCE.get(), new ResourceLocation(RatsMod.MODID, "special"), (stack, level, entity, i) -> {
				if (stack.hasCustomHoverName()) {
					RatsRenderType.GlintType type = RatsRenderType.GlintType.getGlintBasedOnKeyword(stack.getHoverName().getString());
					return type != null && type.changesItemTexture() ? type.ordinal() + 1 : 0;
				}
				return 0;
			});

		});

		MenuScreens.register(RatsMenuRegistry.RAT_CRAFTING_TABLE_CONTAINER.get(), RatCraftingTableScreen::new);
		MenuScreens.register(RatsMenuRegistry.RAT_UPGRADE_CONTAINER.get(), RatUpgradeScreen::new);
		MenuScreens.register(RatsMenuRegistry.RAT_UPGRADE_JR_CONTAINER.get(), JuryRiggedRatUpgradeScreen::new);
		MenuScreens.register(RatsMenuRegistry.UPGRADE_COMBINER_CONTAINER.get(), UpgradeCombinerScreen::new);
		MenuScreens.register(RatsMenuRegistry.AUTO_CURDLER_CONTAINER.get(), AutoCurdlerScreen::new);
	}

	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		for(PiratWoodBoat.Type boatType : PiratWoodBoat.Type.values()) {
			event.registerLayerDefinition(PiratWoodBoatRenderer.createBoatModelName(boatType), BoatModel::createBodyModel);
			event.registerLayerDefinition(PiratWoodBoatRenderer.createChestBoatModelName(boatType), ChestBoatModel::createBodyModel);
		}

		event.registerLayerDefinition(RatsModelLayers.BLACK_DEATH, BlackDeathModel::create);
		event.registerLayerDefinition(RatsModelLayers.PIPER, PiedPiperModel::create);
		event.registerLayerDefinition(RatsModelLayers.PIRAT_BOAT, PiratBoatModel::create);
		event.registerLayerDefinition(RatsModelLayers.PLAGUE_DOCTOR, PlagueDoctorModel::create);
		event.registerLayerDefinition(RatsModelLayers.RAT_STRIDER_MOUNT, RatStriderMountModel::create);
		event.registerLayerDefinition(RatsModelLayers.THROWN_BLOCK, CubeModel::create);

		event.registerLayerDefinition(RatsModelLayers.HAMMOCK, RatHammockModel::create);
		event.registerLayerDefinition(RatsModelLayers.IGLOO, RatIglooModel::create);
		event.registerLayerDefinition(RatsModelLayers.SEED_BOWL, RatSeedBowlModel::create);
		event.registerLayerDefinition(RatsModelLayers.WATER_BOTTLE, RatWaterBottleModel::create);

		event.registerLayerDefinition(RatsModelLayers.CHEF_TOQUE, ChefToqueModel::create);
		event.registerLayerDefinition(RatsModelLayers.PIPER_HAT, PiperHatModel::create);
		event.registerLayerDefinition(RatsModelLayers.ARCHEOLOGIST_HAT, ArcheologistHatModel::create);
		event.registerLayerDefinition(RatsModelLayers.FARMER_HAT, FarmerHatModel::create);
		event.registerLayerDefinition(RatsModelLayers.FEZ, RatFezModel::create);
		event.registerLayerDefinition(RatsModelLayers.TOP_HAT, TopHatModel::create);
		event.registerLayerDefinition(RatsModelLayers.SANTA_HAT, SantaHatModel::create);
		event.registerLayerDefinition(RatsModelLayers.HALO, HaloHatModel::create);
		event.registerLayerDefinition(RatsModelLayers.PARTY_HAT, PartyHatModel::create);
		event.registerLayerDefinition(RatsModelLayers.PIRATE_HAT, PiratHatModel::create);
		event.registerLayerDefinition(RatsModelLayers.CROWN, CrownModel::create);
		event.registerLayerDefinition(RatsModelLayers.PLAGUE_DOCTOR_MASK, PlagueDoctorMaskModel::create);
		event.registerLayerDefinition(RatsModelLayers.EXTERMINATOR_HAT, ExterminatorHatModel::create);
		event.registerLayerDefinition(RatsModelLayers.AVIATOR_HAT, AviatorHatModel::create);
		event.registerLayerDefinition(RatsModelLayers.OFFICER_HAT, MilitaryHatModel::create);

		event.registerLayerDefinition(RatsModelLayers.RATLANTIS_ARMOR_OUTER, () -> RatlantisArmorModel.create(LayerDefinitions.OUTER_ARMOR_DEFORMATION));
		event.registerLayerDefinition(RatsModelLayers.RATLANTIS_ARMOR_INNER, () -> RatlantisArmorModel.create(LayerDefinitions.INNER_ARMOR_DEFORMATION));
	}

	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(RatsEntityRegistry.RAT.get(), RatRenderer::new);
		event.registerEntityRenderer(RatsEntityRegistry.TAMED_RAT.get(), TamedRatRenderer::new);
		event.registerEntityRenderer(RatsEntityRegistry.PIED_PIPER.get(), PiedPiperRenderer::new);
		event.registerEntityRenderer(RatsEntityRegistry.THROWN_BLOCK.get(), ThrownBlockRenderer::new);
		event.registerEntityRenderer(RatsEntityRegistry.PLAGUE_DOCTOR.get(), PlagueDoctorRenderer::new);
		event.registerEntityRenderer(RatsEntityRegistry.PURIFYING_LIQUID.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(RatsEntityRegistry.BLACK_DEATH.get(), BlackDeathRenderer::new);
		event.registerEntityRenderer(RatsEntityRegistry.PLAGUE_CLOUD.get(), context -> new RatlateanSpiritRenderer<>(context, true));
		event.registerEntityRenderer(RatsEntityRegistry.PLAGUE_BEAST.get(), PlagueBeastRenderer::new);
		event.registerEntityRenderer(RatsEntityRegistry.PLAGUE_SHOT.get(), PlagueShotRenderer::new);
		event.registerEntityRenderer(RatsEntityRegistry.RAT_CAPTURE_NET.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(RatsEntityRegistry.RAT_DRAGON_FIRE.get(), NothingRenderer::new);
		event.registerEntityRenderer(RatsEntityRegistry.RAT_ARROW.get(), RatArrowRenderer::new);
		event.registerEntityRenderer(RatsEntityRegistry.RAT_MOUNT_GOLEM.get(), RatGolemMountRenderer::new);
		event.registerEntityRenderer(RatsEntityRegistry.RAT_MOUNT_CHICKEN.get(), RatChickenMountRenderer::new);
		event.registerEntityRenderer(RatsEntityRegistry.RAT_MOUNT_BEAST.get(), RatBeastMountRenderer::new);
		event.registerEntityRenderer(RatsEntityRegistry.RAT_KING.get(), RatKingRenderer::new);
		event.registerEntityRenderer(RatsEntityRegistry.RAT_SHOT.get(), RatShotRenderer::new);
		event.registerEntityRenderer(RatsEntityRegistry.DEMON_RAT.get(), DemonRatRenderer::new);
		event.registerEntityRenderer(RatsEntityRegistry.RAT_STRIDER_MOUNT.get(), RatStriderMountRenderer::new);
		event.registerEntityRenderer(RatsEntityRegistry.SMALL_ARROW.get(), SmallArrowRenderer::new);

		event.registerBlockEntityRenderer(RatsBlockEntityRegistry.RAT_HOLE.get(), RatHoleRenderer::new);
		event.registerBlockEntityRenderer(RatsBlockEntityRegistry.RAT_TRAP.get(), RatTrapRenderer::new);
		event.registerBlockEntityRenderer(RatsBlockEntityRegistry.AUTO_CURDLER.get(), AutoCurdlerRenderer::new);
		event.registerBlockEntityRenderer(RatsBlockEntityRegistry.RAT_CAGE_DECORATED.get(), DecoratedRatCageRenderer::new);
		event.registerBlockEntityRenderer(RatsBlockEntityRegistry.RAT_CAGE_BREEDING_LANTERN.get(), DecoratedRatCageRenderer::new);
		event.registerBlockEntityRenderer(RatsBlockEntityRegistry.RAT_CAGE_WHEEL.get(), DecoratedRatCageRenderer::new);
		event.registerBlockEntityRenderer(RatsBlockEntityRegistry.UPGRADE_COMBINER.get(), UpgradeCombinerRenderer::new);
		event.registerBlockEntityRenderer(RatsBlockEntityRegistry.UPGRADE_SEPERATOR.get(), UpgradeSeparatorRenderer::new);
		event.registerBlockEntityRenderer(RatsBlockEntityRegistry.TRASH_CAN.get(), TrashCanRenderer::new);


		event.registerEntityRenderer(RatlantisEntityRegistry.DUTCHRAT.get(), DutchratRenderer::new);
		event.registerEntityRenderer(RatlantisEntityRegistry.DUTCHRAT_SWORD.get(), DutchratSwordRenderer::new);
		event.registerEntityRenderer(RatlantisEntityRegistry.RATFISH.get(), RatfishRenderer::new);
		event.registerEntityRenderer(RatlantisEntityRegistry.RATTLING_GUN.get(), RattlingGunRenderer::new);
		event.registerEntityRenderer(RatlantisEntityRegistry.RATTLING_GUN_BULLET.get(), RattlingGunBulletRenderer::new);
		event.registerEntityRenderer(RatlantisEntityRegistry.RATLANTEAN_RATBOT.get(), RatlanteanRatbotRenderer::new);
		event.registerEntityRenderer(RatlantisEntityRegistry.PIRAT.get(), PiratRenderer::new);
		event.registerEntityRenderer(RatlantisEntityRegistry.RAT_MOUNT_AUTOMATON.get(), RatAutomatonMountRenderer::new);
		event.registerEntityRenderer(RatlantisEntityRegistry.GHOST_PIRAT.get(), GhostPiratRenderer::new);
		event.registerEntityRenderer(RatlantisEntityRegistry.RAT_BARON.get(), RatBaronRenderer::new);
		event.registerEntityRenderer(RatlantisEntityRegistry.RAT_BARON_PLANE.get(), RatBaronPlaneRenderer::new);
		event.registerEntityRenderer(RatlantisEntityRegistry.RAT_MOUNT_BIPLANE.get(), RatBiplaneMountRenderer::new);
		event.registerEntityRenderer(RatlantisEntityRegistry.RAT_PROTECTOR.get(), RatProtectorRenderer::new);
		event.registerEntityRenderer(RatlantisEntityRegistry.RATLANTIS_ARROW.get(), RatlantisArrowRenderer::new);
		event.registerEntityRenderer(RatlantisEntityRegistry.RATLANTEAN_SPIRIT.get(), context -> new RatlateanSpiritRenderer<>(context, false));
		event.registerEntityRenderer(RatlantisEntityRegistry.RATLANTEAN_FLAME.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(RatlantisEntityRegistry.RATLANTEAN_AUTOMATON.get(), RatlanteanAutomatonRenderer::new);
		event.registerEntityRenderer(RatlantisEntityRegistry.RATLANTEAN_AUTOMATON_BEAM.get(), GolemBeamRenderer::new);
		event.registerEntityRenderer(RatlantisEntityRegistry.FERAL_RATLANTEAN.get(), FeralRatlanteanRenderer::new);
		event.registerEntityRenderer(RatlantisEntityRegistry.NEO_RATLANTEAN.get(), NeoRatlanteanRenderer::new);
		event.registerEntityRenderer(RatlantisEntityRegistry.LASER_BEAM.get(), LaserBeamRenderer::new);
		event.registerEntityRenderer(RatlantisEntityRegistry.LASER_PORTAL.get(), LaserPortalRenderer::new);
		event.registerEntityRenderer(RatlantisEntityRegistry.VIAL_OF_SENTIENCE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(RatlantisEntityRegistry.PIRAT_BOAT.get(), context -> new PiratBoatRenderer<>(context, new PiratBoatModel<>(context.bakeLayer(RatsModelLayers.PIRAT_BOAT))));
		event.registerEntityRenderer(RatlantisEntityRegistry.CHEESE_CANNONBALL.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(RatlantisEntityRegistry.BOAT.get(), context -> new PiratWoodBoatRenderer(context, false));
		event.registerEntityRenderer(RatlantisEntityRegistry.CHEST_BOAT.get(), context -> new PiratWoodBoatRenderer(context, true));

		event.registerBlockEntityRenderer(RatlantisBlockEntityRegistry.RATLANTIS_PORTAL.get(), RatlantisPortalRenderer::new);
		event.registerBlockEntityRenderer(RatlantisBlockEntityRegistry.DUTCHRAT_BELL.get(), DutchratBellRenderer::new);
		event.registerBlockEntityRenderer(RatlantisBlockEntityRegistry.AUTOMATON_HEAD.get(), RatlanteanAutomatonHeadRenderer::new);
		event.registerBlockEntityRenderer(RatlantisBlockEntityRegistry.TOKEN.get(), RatlantisTokenRenderer::new);
		event.registerBlockEntityRenderer(RatlantisBlockEntityRegistry.PIRAT_SIGN.get(), PiratSignRenderer::new);
		event.registerBlockEntityRenderer(RatlantisBlockEntityRegistry.PIRAT_HANGING_SIGN.get(), PiratHangingSignRenderer::new);
	}

	@SubscribeEvent
	public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(RatsParticleRegistry.BLACK_DEATH.get(), BlackDeathParticle.Provider::new);
		event.registerSpriteSet(RatsParticleRegistry.DUTCHRAT_SMOKE.get(), DutchratSmokeParticle.Provider::new);
		event.registerSpriteSet(RatsParticleRegistry.FLEA.get(), FleaParticle.Provider::new);
		event.registerSpriteSet(RatsParticleRegistry.FLY.get(), FlyParticle.Provider::new);
		event.registerSpriteSet(RatsParticleRegistry.LIGHTNING.get(), LightningParticle.Provider::new);
		event.registerSpriteSet(RatsParticleRegistry.MILK_BUBBLE.get(), MilkBubbleParticle.Provider::new);
		event.registerSpriteSet(RatsParticleRegistry.PIRAT_GHOST.get(), PiratGhostParticle.Provider::new);
		event.registerSpriteSet(RatsParticleRegistry.RAT_GHOST.get(), RatGhostParticle.Provider::new);
		event.registerSpriteSet(RatsParticleRegistry.RAT_KING_SMOKE.get(), RatKingSmokeParticle.Provider::new);
		event.registerSpecial(RatsParticleRegistry.RUNNING_RAT.get(), new RunningRatParticle.Provider());
		event.registerSpriteSet(RatsParticleRegistry.SALIVA.get(), SalivaParticle.Provider::new);
		event.registerSpriteSet(RatsParticleRegistry.UPGRADE_COMBINER.get(), UpgradeCombinerParticle.Provider::new);
	}

	@SubscribeEvent
	public static void onBlockColors(RegisterColorHandlersEvent.Block event) {
		event.register((state, level, pos, tint) -> {
			int meta = 0;
			if (level != null && pos != null && level.getBlockEntity(pos) instanceof RatTubeBlockEntity tube) {
				meta = tube.getColor();
			}
			DyeColor color = DyeColor.byId(meta);
			return color.getFireworkColor();
		}, RatsBlockRegistry.RAT_TUBE_COLOR.get());


		event.register((state, level, pos, tint) -> level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.get(0.5D, 1.0D), RatlantisBlockRegistry.MARBLED_CHEESE_GRASS.get());
	}

	@SubscribeEvent
	public static void onItemColors(RegisterColorHandlersEvent.Item event) {
		event.register((stack, tint) -> FoliageColor.get(0.5D, 1.0D), RatlantisBlockRegistry.MARBLED_CHEESE_GRASS.get().asItem());

		for (RegistryObject<Item> item : RatsItemRegistry.RAT_TUBES) {
			event.register((stack, tint) -> ((RatTubeItem) item.get()).color.getFireworkColor(), item.get());
		}
		for (RegistryObject<Item> item : RatsItemRegistry.RAT_IGLOOS) {
			event.register((stack, tint) -> ((RatIglooItem) item.get()).color.getFireworkColor(), item.get());
		}
		for (RegistryObject<Item> item : RatsItemRegistry.RAT_HAMMOCKS) {
			event.register((stack, tint) -> ((RatHammockItem) item.get()).color.getFireworkColor(), item.get());
		}
		event.register((stack, tint) -> {
			if (tint == 1) {
				return NuggetColorRegister.getNuggetColor(stack);
			} else {
				return -1;
			}
		}, RatsItemRegistry.RAT_NUGGET_ORE.get());

		event.register((stack, tintIndex) -> {
					int colorToUse;
					if (tintIndex == 0) {
						colorToUse = stack.getItem() instanceof PartyHatItem hat
								? hat.getColor(stack)
								: 0x25C9E7;
					} else {
						colorToUse = stack.getItem() instanceof PartyHatItem hat
								? invertColor(hat.getColor(stack))
								: invertColor(0x25C9E7);
					}
					return colorToUse;
				},
				RatsItemRegistry.PARTY_HAT.get());
	}

	private static int invertColor(int color) {
		int a = (color >> 24) & 0xff;
		int r = (color >> 16) & 0xff;
		int g = (color >> 8) & 0xff;
		int b = color & 0xff;

		r = 255 - r;
		g = 255 - g;
		b = 255 - b;
		return (a & 0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
	}

	private static Field field_EntityRenderersEvent$AddLayers_renderers;

	@SubscribeEvent
	@SuppressWarnings("unchecked")
	public static void attachRenderLayers(EntityRenderersEvent.AddLayers event) {
		if (field_EntityRenderersEvent$AddLayers_renderers == null) {
			try {
				field_EntityRenderersEvent$AddLayers_renderers = EntityRenderersEvent.AddLayers.class.getDeclaredField("renderers");
				field_EntityRenderersEvent$AddLayers_renderers.setAccessible(true);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		if (field_EntityRenderersEvent$AddLayers_renderers != null) {
			event.getSkins().forEach(renderer -> {
				LivingEntityRenderer<Player, EntityModel<Player>> skin = event.getSkin(renderer);
				attachRenderLayers(Objects.requireNonNull(skin));
			});
			try {
				((Map<EntityType<?>, EntityRenderer<?>>) field_EntityRenderersEvent$AddLayers_renderers.get(event))
						.values().stream()
						.filter(LivingEntityRenderer.class::isInstance)
						.map(LivingEntityRenderer.class::cast)
						.forEach(ModClientEvents::attachRenderLayers);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private static <T extends LivingEntity, M extends EntityModel<T>> void attachRenderLayers(LivingEntityRenderer<T, M> renderer) {
		renderer.addLayer(new PlagueLayer<>(renderer));
		if (renderer.getModel() instanceof HumanoidModel<?>) {
			renderer.addLayer(new PartyHatLayer<>(renderer, new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))));
		}
	}
}
