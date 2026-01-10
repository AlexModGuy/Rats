package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.block.entity.*;
import com.github.alexthe666.rats.server.capability.SelectedRat;
import com.github.alexthe666.rats.server.capability.SelectedRatCapability;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;

public class RatsCapabilityRegistry {
	public static final EntityCapability<SelectedRat, Void> SELECTED_RAT = 
		EntityCapability.createVoid(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "selected_rat"), SelectedRat.class);

	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		// Player capability
		event.registerEntity(SELECTED_RAT, EntityType.PLAYER, (player, ctx) -> {
			SelectedRatCapability cap = new SelectedRatCapability();
			cap.setPlayer(player);
			return cap;
		});

		// Entity capabilities
		event.registerEntity(Capabilities.ItemHandler.ENTITY, RatsEntityRegistry.TAMED_RAT.get(), (entity, ctx) -> {
			if (entity.isAlive() && entity.getItemHandler() != null) {
				return entity.getItemHandler();
			}
			return null;
		});

		// Block Entity capabilities - AutoCurdler
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, RatsBlockEntityRegistry.AUTO_CURDLER.get(), (blockEntity, side) -> {
			return blockEntity.getItemHandler(side);
		});
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, RatsBlockEntityRegistry.AUTO_CURDLER.get(), (blockEntity, side) -> blockEntity.getTank());

		// Block Entity capabilities - UpgradeCombiner
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, RatsBlockEntityRegistry.UPGRADE_COMBINER.get(), (blockEntity, side) -> {
			return blockEntity.getItemHandler(side != null ? side : Direction.NORTH);
		});

		// Block Entity capabilities - RatQuarry
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, RatsBlockEntityRegistry.RAT_QUARRY.get(), (blockEntity, side) -> {
			return blockEntity.getItemHandler(side);
		});

		// Block Entity capabilities - RatCraftingTable
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, RatsBlockEntityRegistry.RAT_CRAFTING_TABLE.get(), (blockEntity, side) -> {
			return blockEntity.getItemHandler(side);
		});
	}
}







