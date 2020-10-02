package com.github.alexthe666.rats.server.items;

import com.google.common.collect.Maps;
import net.minecraft.item.Item;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Map;

public class RatsUpgradeConflictRegistry {
    private static Map<Item, Item[]> REGISTERED_CONFLICTS = Maps.newHashMap();
    private static Item[] HEALTH_INCREASES = new Item[]{RatsItemRegistry.RAT_UPGRADE_HEALTH, RatsItemRegistry.RAT_UPGRADE_WARRIOR, RatsItemRegistry.RAT_UPGRADE_GOD, RatlantisItemRegistry.RAT_UPGRADE_NONBELIEVER};
    private static Item[] ARMOR_INCREASES = new Item[]{RatsItemRegistry.RAT_UPGRADE_ARMOR, RatsItemRegistry.RAT_UPGRADE_WARRIOR, RatsItemRegistry.RAT_UPGRADE_GOD, RatlantisItemRegistry.RAT_UPGRADE_NONBELIEVER};
    private static Item[] ATTACK_INCREASES = new Item[]{RatsItemRegistry.RAT_UPGRADE_STRENGTH, RatsItemRegistry.RAT_UPGRADE_WARRIOR, RatsItemRegistry.RAT_UPGRADE_GOD, RatlantisItemRegistry.RAT_UPGRADE_NONBELIEVER};
    private static Item[] HARVEST_CONFLICTS = new Item[]{RatsItemRegistry.RAT_UPGRADE_LUMBERJACK, RatsItemRegistry.RAT_UPGRADE_MINER, RatsItemRegistry.RAT_UPGRADE_MINER_ORE, RatsItemRegistry.RAT_UPGRADE_QUARRY, RatsItemRegistry.RAT_UPGRADE_FARMER, RatsItemRegistry.RAT_UPGRADE_FISHERMAN, RatsItemRegistry.RAT_UPGRADE_MILKER, RatsItemRegistry.RAT_UPGRADE_SHEARS, RatsItemRegistry.RAT_UPGRADE_PLACER, RatsItemRegistry.RAT_UPGRADE_BREEDER};
    private static Item[] TRANSPORT_CONFLICTS = new Item[]{RatsItemRegistry.RAT_UPGRADE_BASIC_ENERGY, RatsItemRegistry.RAT_UPGRADE_ADVANCED_ENERGY, RatsItemRegistry.RAT_UPGRADE_ELITE_ENERGY, RatsItemRegistry.RAT_UPGRADE_EXTREME_ENERGY, RatsItemRegistry.RAT_UPGRADE_BUCKET, RatsItemRegistry.RAT_UPGRADE_BIG_BUCKET};
    private static Item[] EXPLOSION_CONFLICTS = new Item[]{RatsItemRegistry.RAT_UPGRADE_TNT, RatsItemRegistry.RAT_UPGRADE_TNT_SURVIVOR, RatlantisItemRegistry.RAT_UPGRADE_BUCCANEER};
    private static Item[] PROCESSING_CONFLICTS = new Item[]{RatsItemRegistry.RAT_UPGRADE_CHEF, RatlantisItemRegistry.RAT_UPGRADE_ARCHEOLOGIST, RatsItemRegistry.RAT_UPGRADE_GEMCUTTER, RatsItemRegistry.RAT_UPGRADE_CHRISTMAS, RatsItemRegistry.RAT_UPGRADE_ENCHANTER, RatsItemRegistry.RAT_UPGRADE_DISENCHANTER};
    private static Item[] MOUNT_CONFLICTS = new Item[]{RatsItemRegistry.RAT_UPGRADE_CHICKEN_MOUNT, RatsItemRegistry.RAT_UPGRADE_GOLEM_MOUNT, RatlantisItemRegistry.RAT_UPGRADE_AUTOMATON_MOUNT, RatsItemRegistry.RAT_UPGRADE_BEAST_MOUNT};

    public static void init() {
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_BASIC, new Item[0]);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_SPEED, new Item[0]);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_PLATTER, new Item[0]);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_HEALTH, HEALTH_INCREASES);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_ARMOR, ArrayUtils.add(ARMOR_INCREASES, RatlantisItemRegistry.RAT_UPGRADE_RATINATOR));
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_STRENGTH, ATTACK_INCREASES);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_WARRIOR, ArrayUtils.addAll(HEALTH_INCREASES, ArrayUtils.addAll(ARMOR_INCREASES, ATTACK_INCREASES)));
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_GOD, ArrayUtils.addAll(HEALTH_INCREASES, ArrayUtils.addAll(ARMOR_INCREASES, ATTACK_INCREASES)));
        REGISTERED_CONFLICTS.put(RatlantisItemRegistry.RAT_UPGRADE_NONBELIEVER, ArrayUtils.addAll(HEALTH_INCREASES, ArrayUtils.addAll(ARMOR_INCREASES, ATTACK_INCREASES)));
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_CHEF, new Item[0]);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_CRAFTING, new Item[0]);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_BLACKLIST, new Item[]{RatsItemRegistry.RAT_UPGRADE_WHITELIST});
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_WHITELIST, new Item[]{RatsItemRegistry.RAT_UPGRADE_BLACKLIST});
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_FLIGHT, new Item[]{RatsItemRegistry.RAT_UPGRADE_BEE, RatsItemRegistry.RAT_UPGRADE_DRAGON});
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_BEE, new Item[]{RatsItemRegistry.RAT_UPGRADE_FLIGHT, RatsItemRegistry.RAT_UPGRADE_DRAGON});
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_ENDER, new Item[0]);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK, HARVEST_CONFLICTS);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_MINER, HARVEST_CONFLICTS);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_MINER_ORE, HARVEST_CONFLICTS);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_QUARRY, HARVEST_CONFLICTS);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_FARMER, HARVEST_CONFLICTS);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_FISHERMAN, HARVEST_CONFLICTS);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_MILKER, HARVEST_CONFLICTS);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_SHEARS, HARVEST_CONFLICTS);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_PLACER, HARVEST_CONFLICTS);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_BREEDER, HARVEST_CONFLICTS);
        REGISTERED_CONFLICTS.put(RatlantisItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN, new Item[0]);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_COMBINED, new Item[]{RatsItemRegistry.RAT_UPGRADE_COMBINED_CREATIVE, RatsItemRegistry.RAT_UPGRADE_JURY_RIGGED});
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_COMBINED_CREATIVE, new Item[]{RatsItemRegistry.RAT_UPGRADE_COMBINED, RatsItemRegistry.RAT_UPGRADE_JURY_RIGGED});
        REGISTERED_CONFLICTS.put(RatlantisItemRegistry.RAT_UPGRADE_FERAL_BITE, new Item[0]);
        REGISTERED_CONFLICTS.put(RatlantisItemRegistry.RAT_UPGRADE_RATINATOR, new Item[]{RatsItemRegistry.RAT_UPGRADE_ARMOR});
        REGISTERED_CONFLICTS.put(RatlantisItemRegistry.RAT_UPGRADE_PSYCHIC, new Item[0]);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_ASBESTOS, new Item[]{RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION});
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_POISON, new Item[]{RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION});
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_UNDERWATER, new Item[]{RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION});
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION, new Item[]{RatsItemRegistry.RAT_UPGRADE_ASBESTOS, RatsItemRegistry.RAT_UPGRADE_POISON, RatsItemRegistry.RAT_UPGRADE_UNDERWATER});
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_BASIC_ENERGY, TRANSPORT_CONFLICTS);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_ADVANCED_ENERGY, TRANSPORT_CONFLICTS);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_ELITE_ENERGY, TRANSPORT_CONFLICTS);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_EXTREME_ENERGY, TRANSPORT_CONFLICTS);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_BUCKET, TRANSPORT_CONFLICTS);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_BIG_BUCKET, TRANSPORT_CONFLICTS);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_TNT, EXPLOSION_CONFLICTS);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_TNT_SURVIVOR, EXPLOSION_CONFLICTS);
        REGISTERED_CONFLICTS.put(RatlantisItemRegistry.RAT_UPGRADE_BUCCANEER, EXPLOSION_CONFLICTS);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_ENCHANTER, new Item[]{RatsItemRegistry.RAT_UPGRADE_DISENCHANTER});
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_DISENCHANTER, new Item[]{RatsItemRegistry.RAT_UPGRADE_ENCHANTER});
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_CHRISTMAS, PROCESSING_CONFLICTS);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_DRAGON, new Item[]{RatsItemRegistry.RAT_UPGRADE_FLIGHT,RatsItemRegistry.RAT_UPGRADE_BEE, RatsItemRegistry.RAT_UPGRADE_ASBESTOS});
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_NO_FLUTE, new Item[0]);
        REGISTERED_CONFLICTS.put(RatlantisItemRegistry.RAT_UPGRADE_ETHEREAL, new Item[0]);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_ANGEL, new Item[0]);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_CARRAT, new Item[0]);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_JURY_RIGGED, new Item[]{RatsItemRegistry.RAT_UPGRADE_COMBINED, RatsItemRegistry.RAT_UPGRADE_COMBINED_CREATIVE});
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_CHICKEN_MOUNT, MOUNT_CONFLICTS);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_GOLEM_MOUNT, MOUNT_CONFLICTS);
        REGISTERED_CONFLICTS.put(RatlantisItemRegistry.RAT_UPGRADE_AUTOMATON_MOUNT, MOUNT_CONFLICTS);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_BEAST_MOUNT, MOUNT_CONFLICTS);
    }

    public static boolean doesConflict(Item first, Item second) {
        Item[] arr = REGISTERED_CONFLICTS.get(first);
        if (arr != null && arr.length > 0) {
            for (Item item : arr) {
                if (item == second) {
                    return true;
                }
            }
        }
        return false;
    }
}
