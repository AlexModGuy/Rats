package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.ratlantis.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RatlantisEntityRegistry {

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RatsMod.MODID);

	public static final RegistryObject<EntityType<RatlanteanSpirit>> RATLANTEAN_SPIRIT = registerEntity("ratlantean_spirit", EntityType.Builder.of(RatlanteanSpirit::new, RatsMod.RATS).fireImmune().sized(1.25F, 1.25F), 0XEDBD00, 0XFFE8AF);
	public static final RegistryObject<EntityType<RatlanteanFlame>> RATLANTEAN_FLAME = registerEntityNoEgg("ratlantean_spirit_flame", EntityType.Builder.<RatlanteanFlame>of(RatlanteanFlame::new, MobCategory.MISC).fireImmune().sized(0.5F, 0.5F));
	public static final RegistryObject<EntityType<RatlanteanAutomaton>> RATLANTEAN_AUTOMATON = registerEntity("ratlantean_automaton", EntityType.Builder.of(RatlanteanAutomaton::new, MobCategory.MONSTER).fireImmune().sized(2F, 4.0F), 0XE8E4D7, 0X72E955);
	public static final RegistryObject<EntityType<GolemBeam>> RATLANTEAN_AUTOMATON_BEAM = registerEntityNoEgg("ratlantean_automaton_beam", EntityType.Builder.<GolemBeam>of(GolemBeam::new, MobCategory.MISC).fireImmune().sized(0.5F, 0.5F));
	public static final RegistryObject<EntityType<FeralRatlantean>> FERAL_RATLANTEAN = registerEntity("feral_ratlantean", EntityType.Builder.of(FeralRatlantean::new, RatsMod.RATS).sized(1.85F, 1.2F), 0X30333E, 0XECECEC);
	public static final RegistryObject<EntityType<NeoRatlantean>> NEO_RATLANTEAN = registerEntity("neo_ratlantean", EntityType.Builder.of(NeoRatlantean::new, MobCategory.MONSTER).fireImmune().sized(0.8F, 1.3F), 0X30333E, 0X00EFEF);
	public static final RegistryObject<EntityType<LaserBeam>> LASER_BEAM = registerEntityNoEgg("laser_beam", EntityType.Builder.<LaserBeam>of(LaserBeam::new, MobCategory.MISC).sized(0.5F, 0.5F));
	public static final RegistryObject<EntityType<LaserPortal>> LASER_PORTAL = registerEntityNoEgg("laser_portal", EntityType.Builder.<LaserPortal>of(LaserPortal::new, MobCategory.MISC).sized(0.95F, 0.95F));
	public static final RegistryObject<EntityType<VialOfSentience>> VIAL_OF_SENTIENCE = registerEntityNoEgg("vial_of_sentience", EntityType.Builder.<VialOfSentience>of(VialOfSentience::new, MobCategory.MISC).sized(0.25F, 0.25F));
	public static final RegistryObject<EntityType<PiratBoat>> PIRAT_BOAT = registerEntityNoEgg("pirat_boat", EntityType.Builder.of(PiratBoat::new, MobCategory.MISC).sized(1.75F, 0.8F));
	public static final RegistryObject<EntityType<Pirat>> PIRAT = registerEntity("pirat", EntityType.Builder.of(Pirat::new, RatsMod.RATS).sized(0.49F, 0.49F), 0X30333E, 0XAF363A);
	public static final RegistryObject<EntityType<CheeseCannonball>> CHEESE_CANNONBALL = registerEntityNoEgg("cheese_cannonball", EntityType.Builder.<CheeseCannonball>of(CheeseCannonball::new, MobCategory.MISC).sized(0.5F, 0.5F));
	public static final RegistryObject<EntityType<GhostPirat>> GHOST_PIRAT = registerEntity("ghost_pirat", EntityType.Builder.of(GhostPirat::new, RatsMod.RATS).sized(1.0F, 0.75F), 0X54AA55, 0X7BD77D);
	public static final RegistryObject<EntityType<Dutchrat>> DUTCHRAT = registerEntity("dutchrat", EntityType.Builder.of(Dutchrat::new, MobCategory.MONSTER).sized(2.0F, 2.75F).fireImmune(), 0XBBF9BB, 0XD0E2B5);
	public static final RegistryObject<EntityType<DutchratSword>> DUTCHRAT_SWORD = registerEntityNoEgg("dutchrat_sword", EntityType.Builder.<DutchratSword>of(DutchratSword::new, MobCategory.MISC).fireImmune().sized(0.95F, 0.95F));
	public static final RegistryObject<EntityType<Ratfish>> RATFISH = registerEntity("ratfish", EntityType.Builder.of(Ratfish::new, MobCategory.WATER_CREATURE).sized(0.45F, 0.45F), 0X30333E, 0X7EA8BD);
	public static final RegistryObject<EntityType<RattlingGun>> RATTLING_GUN = registerEntityNoEgg("rattling_gun", EntityType.Builder.of(RattlingGun::new, MobCategory.MISC).sized(1.5F, 1.6F));
	public static final RegistryObject<EntityType<RattlingGunBullet>> RATTLING_GUN_BULLET = registerEntityNoEgg("rattling_gun_bullet", EntityType.Builder.<RattlingGunBullet>of(RattlingGunBullet::new, MobCategory.MISC).sized(0.25F, 0.25F));
	public static final RegistryObject<EntityType<RatlanteanRatbot>> RATLANTEAN_RATBOT = registerEntity("ratlantean_ratbot", EntityType.Builder.of(RatlanteanRatbot::new, RatsMod.RATS).fireImmune().sized(1.2F, 1.3F), 0XA3A3A3, 0XFF0000);
	public static final RegistryObject<EntityType<RatAutomatonMount>> RAT_MOUNT_AUTOMATON = registerEntityNoEgg("rat_mount_automaton", EntityType.Builder.of(RatAutomatonMount::new, MobCategory.MISC).fireImmune().sized(2F, 3.5F));
	public static final RegistryObject<EntityType<RatBaron>> RAT_BARON = registerEntity("rat_baron", EntityType.Builder.of(RatBaron::new, MobCategory.MONSTER).sized(0.5F, 0.5F), 0X9F0A03, 0XE3E5DA);
	public static final RegistryObject<EntityType<RatBaronPlane>> RAT_BARON_PLANE = registerEntityNoEgg("rat_baron_plane", EntityType.Builder.of(RatBaronPlane::new, MobCategory.MONSTER).fireImmune().sized(3.5F, 3.0F));
	public static final RegistryObject<EntityType<RatBiplaneMount>> RAT_MOUNT_BIPLANE = registerEntityNoEgg("rat_mount_biplane", EntityType.Builder.of(RatBiplaneMount::new, MobCategory.MISC).sized(3.5F, 2.3F));
	public static final RegistryObject<EntityType<RatProtector>> RAT_PROTECTOR = registerEntityNoEgg("rat_protector", EntityType.Builder.of(RatProtector::new, MobCategory.MISC).sized(0.5F, 0.5F));
	public static final RegistryObject<EntityType<RatlantisArrow>> RATLANTIS_ARROW = registerEntityNoEgg("ratlantis_arrow", EntityType.Builder.<RatlantisArrow>of(RatlantisArrow::new, MobCategory.MISC).sized(0.5F, 0.5F));


	private static <E extends Mob> RegistryObject<EntityType<E>> registerEntity(String entityName, EntityType.Builder<E> builder, int baseEggColor, int overlayEggColor) {
		ResourceLocation nameLoc = new ResourceLocation(RatsMod.MODID, entityName);
		RegistryObject<EntityType<E>> ret = ENTITIES.register(entityName, () -> builder.build(nameLoc.toString()));
		RatlantisItemRegistry.ITEMS.register(entityName + "_spawn_egg", () -> new ForgeSpawnEggItem(ret, baseEggColor, overlayEggColor, new Item.Properties()));
		return ret;
	}

	private static <E extends Entity> RegistryObject<EntityType<E>> registerEntityNoEgg(String entityName, EntityType.Builder<E> builder) {
		return ENTITIES.register(entityName, () -> builder.build(new ResourceLocation(RatsMod.MODID, entityName).toString()));
	}
}
