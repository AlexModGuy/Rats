package com.github.alexthe666.rats.server.items;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;
import java.util.Map;

public class RatsUpgradeConflictRegistry {
    private static Map<Item, Item[]> REGISTERED_CONFLICTS = Maps.<Item, Item[]>newHashMap();
    private static Item[] HEALTH_INCREASES = new Item[]{RatsItemRegistry.RAT_UPGRADE_HEALTH, RatsItemRegistry.RAT_UPGRADE_WARRIOR, RatsItemRegistry.RAT_UPGRADE_GOD};
    private static Item[] ARMOR_INCREASES = new Item[]{RatsItemRegistry.RAT_UPGRADE_ARMOR, RatsItemRegistry.RAT_UPGRADE_WARRIOR, RatsItemRegistry.RAT_UPGRADE_GOD};
    private static Item[] ATTACK_INCREASES = new Item[]{RatsItemRegistry.RAT_UPGRADE_STRENGTH, RatsItemRegistry.RAT_UPGRADE_WARRIOR, RatsItemRegistry.RAT_UPGRADE_GOD};

    public static void init() {
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_BASIC, new Item[0]);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_SPEED, new Item[0]);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_PLATTER, new Item[0]);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_HEALTH, HEALTH_INCREASES);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_ARMOR, ArrayUtils.add(ARMOR_INCREASES, RatsItemRegistry.RAT_UPGRADE_RATINATOR));
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_STRENGTH, ATTACK_INCREASES);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_WARRIOR, ArrayUtils.addAll(HEALTH_INCREASES, ArrayUtils.addAll(ARMOR_INCREASES, ATTACK_INCREASES)));
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_GOD, ArrayUtils.addAll(HEALTH_INCREASES, ArrayUtils.addAll(ARMOR_INCREASES, ATTACK_INCREASES)));
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_CHEF, new Item[0]);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_CRAFTING, new Item[0]);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_BLACKLIST, new Item[]{RatsItemRegistry.RAT_UPGRADE_WHITELIST});
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_WHITELIST, new Item[]{RatsItemRegistry.RAT_UPGRADE_BLACKLIST});
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_FLIGHT, new Item[0]);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_ENDER, new Item[0]);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK, new Item[]{RatsItemRegistry.RAT_UPGRADE_MINER});
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_MINER, new Item[]{RatsItemRegistry.RAT_UPGRADE_LUMBERJACK});
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN, new Item[0]);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_COMBINED, new Item[]{RatsItemRegistry.RAT_UPGRADE_COMBINED_CREATIVE});
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_COMBINED_CREATIVE, new Item[]{RatsItemRegistry.RAT_UPGRADE_COMBINED});
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_FERAL_BITE, new Item[0]);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_RATINATOR, new Item[]{RatsItemRegistry.RAT_UPGRADE_ARMOR});
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_PSYCHIC, new Item[0]);
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_ASBESTOS, new Item[]{RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION});
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_POISON, new Item[]{RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION});
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_UNDERWATER, new Item[]{RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION});
        REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION, new Item[]{RatsItemRegistry.RAT_UPGRADE_ASBESTOS, RatsItemRegistry.RAT_UPGRADE_POISON, RatsItemRegistry.RAT_UPGRADE_UNDERWATER});
    }

    public static boolean doesConflict(Item first, Item second){
        Item[] arr = REGISTERED_CONFLICTS.get(first);
        if(arr != null && arr.length > 0) {
            for (Item item : arr) {
                if (item == second) {
                    return true;
                }
            }
        }
        return false;
    }
}
