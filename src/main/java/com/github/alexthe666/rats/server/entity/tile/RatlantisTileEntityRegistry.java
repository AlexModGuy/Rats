package com.github.alexthe666.rats.server.entity.tile;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.RatlantisBlockRegistry;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RatsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)

public class RatlantisTileEntityRegistry {
    public static TileEntityType<TileEntityRatlantisPortal> RATLANTIS_PORTAL = registerTileEntity(TileEntityType.Builder.create(TileEntityRatHole::new, RatlantisBlockRegistry.RATLANTIS_PORTAL), "ratlantis_portal");
    public static TileEntityType<TileEntityDutchratBell> DUTCHRAT_BELL = registerTileEntity(TileEntityType.Builder.create(TileEntityDutchratBell::new, RatlantisBlockRegistry.DUTCHRAT_BELL), "dutchrat_bell");
    public static TileEntityType<TileEntityRatlanteanAutomatonHead> AUTOMATON_HEAD = registerTileEntity(TileEntityType.Builder.create(TileEntityRatlanteanAutomatonHead::new, RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD), "ratlantean_automaton_head");
    public static TileEntityType<TileEntityToken> TOKEN = registerTileEntity(TileEntityType.Builder.create(TileEntityToken::new, RatlantisBlockRegistry.CHUNKY_CHEESE_TOKEN), "chunky_cheese_token");
    public static TileEntityType<TileEntityRatlantisReactor> RATLANTIS_REACTOR = registerTileEntity(TileEntityType.Builder.create(TileEntityRatlantisReactor::new, RatlantisBlockRegistry.RATLANTIS_REACTOR), "ratlantis_reactor");

    public static TileEntityType registerTileEntity(TileEntityType.Builder builder, String entityName){
        ResourceLocation nameLoc = new ResourceLocation(RatsMod.MODID, entityName);
        return (TileEntityType) builder.build(null).setRegistryName(nameLoc);
    }

}
