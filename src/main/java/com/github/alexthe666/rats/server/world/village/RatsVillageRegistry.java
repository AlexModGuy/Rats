package com.github.alexthe666.rats.server.world.village;

import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

public class RatsVillageRegistry {

    public static void register(){
        MapGenStructureIO.registerStructureComponent(VillageComponentGarbageHeap.class, "garbage_heap");
        VillagerRegistry.instance().registerVillageCreationHandler(new VillageCreatorGarbageHeap());
        MapGenStructureIO.registerStructureComponent(VillageComponentPetShop.class, "pet_shop");
        VillagerRegistry.instance().registerVillageCreationHandler(new VillageCreatorPetShop());
    }
}
