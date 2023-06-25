package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.misc.PlagueDoctor;
import com.github.alexthe666.rats.server.entity.monster.PiedPiper;
import com.github.alexthe666.rats.server.entity.monster.PlagueBeast;
import com.github.alexthe666.rats.server.entity.monster.PlagueCloud;
import com.github.alexthe666.rats.server.entity.monster.boss.BlackDeath;
import com.github.alexthe666.rats.server.entity.monster.boss.RatKing;
import com.github.alexthe666.rats.server.entity.mount.RatBeastMount;
import com.github.alexthe666.rats.server.entity.mount.RatChickenMount;
import com.github.alexthe666.rats.server.entity.mount.RatGolemMount;
import com.github.alexthe666.rats.server.entity.mount.RatStriderMount;
import com.github.alexthe666.rats.server.entity.projectile.*;
import com.github.alexthe666.rats.server.entity.rat.DemonRat;
import com.github.alexthe666.rats.server.entity.rat.Rat;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
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

public class RatsEntityRegistry {

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RatsMod.MODID);

	public static final RegistryObject<EntityType<Rat>> RAT = registerEntity("rat", EntityType.Builder.of(Rat::new, RatsMod.RATS).canSpawnFarFromPlayer().sized(0.49F, 0.49F), 0x30333E, 0xDAABA1);
	public static final RegistryObject<EntityType<TamedRat>> TAMED_RAT = registerEntityNoEgg("tamed_rat", EntityType.Builder.of(TamedRat::new, RatsMod.RATS).sized(0.49F, 0.49F));
	public static final RegistryObject<EntityType<PiedPiper>> PIED_PIPER = registerEntity("pied_piper", EntityType.Builder.of(PiedPiper::new, MobCategory.MONSTER).sized(0.7F, 1.8F), 0xCABC42, 0x3B6063);
	public static final RegistryObject<EntityType<ThrownBlock>> THROWN_BLOCK = registerEntityNoEgg("thrown_block", EntityType.Builder.<ThrownBlock>of(ThrownBlock::new, MobCategory.MISC).sized(0.95F, 0.95F));
	public static final RegistryObject<EntityType<PlagueDoctor>> PLAGUE_DOCTOR = registerEntity("plague_doctor", EntityType.Builder.of(PlagueDoctor::new, MobCategory.CREATURE).sized(0.8F, 1.8F), 0x2A292A, 0x515359);
	public static final RegistryObject<EntityType<PurifyingLiquid>> PURIFYING_LIQUID = registerEntityNoEgg("purifying_liquid", EntityType.Builder.<PurifyingLiquid>of(PurifyingLiquid::new, MobCategory.MISC).sized(0.5F, 0.5F));
	public static final RegistryObject<EntityType<BlackDeath>> BLACK_DEATH = registerEntity("black_death", EntityType.Builder.of(BlackDeath::new, MobCategory.MONSTER).sized(0.6F, 2.1F).fireImmune(), 0x000000, 0x000000);
	public static final RegistryObject<EntityType<PlagueCloud>> PLAGUE_CLOUD = registerEntity("plague_cloud", EntityType.Builder.of(PlagueCloud::new, MobCategory.MONSTER).fireImmune().sized(1.2F, 1.2F), 0x000000, 0x52574D);
	public static final RegistryObject<EntityType<PlagueBeast>> PLAGUE_BEAST = registerEntity("plague_beast", EntityType.Builder.of(PlagueBeast::new, MobCategory.MONSTER).sized(1.85F, 1.2F), 0x000000, 0xECECEC);
	public static final RegistryObject<EntityType<PlagueShot>> PLAGUE_SHOT = registerEntityNoEgg("plague_shot", EntityType.Builder.<PlagueShot>of(PlagueShot::new, MobCategory.MISC).sized(0.5F, 0.5F));
	public static final RegistryObject<EntityType<RatCaptureNet>> RAT_CAPTURE_NET = registerEntityNoEgg("rat_capture_net", EntityType.Builder.<RatCaptureNet>of(RatCaptureNet::new, MobCategory.MISC).sized(0.5F, 0.5F));
	public static final RegistryObject<EntityType<RatDragonFire>> RAT_DRAGON_FIRE = registerEntityNoEgg("rat_dragon_fire", EntityType.Builder.<RatDragonFire>of(RatDragonFire::new, MobCategory.MISC).sized(0.5F, 0.5F));
	public static final RegistryObject<EntityType<RatArrow>> RAT_ARROW = registerEntityNoEgg("rat_arrow", EntityType.Builder.<RatArrow>of(RatArrow::new, MobCategory.MISC).sized(0.5F, 0.5F));
	public static final RegistryObject<EntityType<RatGolemMount>> RAT_MOUNT_GOLEM = registerEntityNoEgg("rat_mount_golem", EntityType.Builder.of(RatGolemMount::new, MobCategory.MISC).sized(1.25F, 2.75F));
	public static final RegistryObject<EntityType<RatChickenMount>> RAT_MOUNT_CHICKEN = registerEntityNoEgg("rat_mount_chicken", EntityType.Builder.of(RatChickenMount::new, MobCategory.MISC).sized(0.65F, 0.75F));
	public static final RegistryObject<EntityType<RatBeastMount>> RAT_MOUNT_BEAST = registerEntityNoEgg("rat_mount_beast", EntityType.Builder.of(RatBeastMount::new, MobCategory.MISC).sized(1.85F, 1.2F));
	public static final RegistryObject<EntityType<RatKing>> RAT_KING = registerEntity("rat_king", EntityType.Builder.of(RatKing::new, MobCategory.MONSTER).sized(2F, 0.5F), 0X30333E, 0X39342F);
	public static final RegistryObject<EntityType<RatShot>> RAT_SHOT = registerEntityNoEgg("rat_shot", EntityType.Builder.<RatShot>of(RatShot::new, MobCategory.MISC).sized(0.5F, 0.5F));
	public static final RegistryObject<EntityType<DemonRat>> DEMON_RAT = registerEntity("demon_rat", EntityType.Builder.of(DemonRat::new, MobCategory.MONSTER).sized(1.0F, 0.75F).fireImmune().canSpawnFarFromPlayer().clientTrackingRange(8), 0x6C191F, 0xFCD500);
	public static final RegistryObject<EntityType<RatStriderMount>> RAT_STRIDER_MOUNT = registerEntityNoEgg("rat_mount_strider", EntityType.Builder.of(RatStriderMount::new, MobCategory.MISC).fireImmune().sized(0.9F, 1.7F).clientTrackingRange(10));
	public static final RegistryObject<EntityType<SmallArrow>> SMALL_ARROW = registerEntityNoEgg("small_arrow", EntityType.Builder.<SmallArrow>of(SmallArrow::new, MobCategory.MISC).sized(0.39F, 0.39F));

	private static <E extends Mob> RegistryObject<EntityType<E>> registerEntity(String entityName, EntityType.Builder<E> builder, int baseEggColor, int overlayEggColor) {
		ResourceLocation nameLoc = new ResourceLocation(RatsMod.MODID, entityName);
		RegistryObject<EntityType<E>> ret = ENTITIES.register(entityName, () -> builder.build(nameLoc.toString()));
		RatsItemRegistry.ITEMS.register(entityName + "_spawn_egg", () -> new ForgeSpawnEggItem(ret, baseEggColor, overlayEggColor, new Item.Properties()));
		return ret;
	}

	private static <E extends Entity> RegistryObject<EntityType<E>> registerEntityNoEgg(String entityName, EntityType.Builder<E> builder) {
		return ENTITIES.register(entityName, () -> builder.build(new ResourceLocation(RatsMod.MODID, entityName).toString()));
	}
}
