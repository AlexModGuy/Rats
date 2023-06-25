package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.message.RatsNetworkHandler;
import com.github.alexthe666.rats.server.message.SyncRatStaffPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class CheeseStaffScreen extends Screen {

	private final TamedRat rat;
	private final BlockPos pos;
	private final Direction clickedFace;

	public CheeseStaffScreen(TamedRat rat, BlockPos pos, Direction clickedFace) {
		super(Component.translatable("cheese_staff"));
		this.rat = rat;
		this.pos = pos;
		this.clickedFace = clickedFace;
		this.init();
	}

	protected void init() {
		super.init();
		this.clearWidgets();
		int i = (this.width) / 2;
		int j = (this.height - 166) / 2;
		Component topText = Component.translatable("entity.rats.rat.staff.mark_block_deposit", this.getPosName()).append(Component.literal(" ").append(Component.translatable("rats.direction." + this.clickedFace.getName())));
		int maxLength = Math.max(150, Minecraft.getInstance().font.width(topText.getString()) + 20);
		this.addRenderableWidget(Button.builder(topText, button -> {
			RatsNetworkHandler.CHANNEL.sendToServer(new SyncRatStaffPacket(this.rat.getId(), this.pos, this.clickedFace, 0));
			Minecraft.getInstance().setScreen(null);
			this.init();
		}).bounds(i - maxLength / 2, j + 60, maxLength, 20).build());
		this.addRenderableWidget(Button.builder(Component.translatable("entity.rats.rat.staff.mark_block_pickup", this.getPosName()), button -> {
			RatsNetworkHandler.CHANNEL.sendToServer(new SyncRatStaffPacket(this.rat.getId(), this.pos, Direction.UP, 1));
			Minecraft.getInstance().setScreen(null);
			this.init();
		}).bounds(i - maxLength / 2, j + 85, maxLength, 20).build());
		this.addRenderableWidget(Button.builder(Component.translatable("entity.rats.rat.staff.set_home_point", getPosName()), button -> {
			this.rat.setHomePoint(GlobalPos.of(Minecraft.getInstance().player.level().dimension(), this.pos));
			RatsNetworkHandler.CHANNEL.sendToServer(new SyncRatStaffPacket(this.rat.getId(), this.pos, Direction.UP, 2));
			this.init();
		}).bounds(i - maxLength / 2, j + 110, maxLength, 20).build());
		this.addRenderableWidget(Button.builder(Component.translatable("entity.rats.rat.staff.un_set_home_point"), button -> {
			this.rat.setHomePoint(null);
			RatsNetworkHandler.CHANNEL.sendToServer(new SyncRatStaffPacket(this.rat.getId(), this.pos, Direction.UP, 3));
			this.init();
		}).bounds(i - maxLength / 2, j + 135, maxLength, 20).build());
		this.addRenderableWidget(Button.builder(Component.translatable("entity.rats.rat.staff.un_set_transport_pos"), button -> {
			this.rat.setPickupPos(null);
			this.rat.setDepositPos(null);
			RatsNetworkHandler.CHANNEL.sendToServer(new SyncRatStaffPacket(this.rat.getId(), this.pos, Direction.UP, 7));
			this.init();
		}).bounds(i - maxLength / 2, j + 160, maxLength, 20).build());
		((Button) this.renderables.get(0)).visible = !this.isNoInventoryAtPos();
		((Button) this.renderables.get(1)).visible = !this.isNoInventoryAtPos();
		((Button) this.renderables.get(2)).visible = this.rat.getHomePoint().isEmpty() || !this.rat.getHomePoint().get().pos().equals(this.pos);
		((Button) this.renderables.get(3)).visible = this.rat.getHomePoint().isPresent();
		((Button) this.renderables.get(4)).visible = this.rat.getDepositPos().isPresent() || this.rat.getPickupPos().isPresent();
	}

	private String getPosName() {
		if (this.pos != null) {
			BlockState state = this.rat.level().getBlockState(this.pos);
			List<Component> namelist = null;
			ItemStack pick = state.getBlock().getCloneItemStack(this.rat.level(), this.pos, state);
			try {
				namelist = pick.getTooltipLines(Minecraft.getInstance().player, TooltipFlag.Default.NORMAL);
			} catch (Throwable ignored) {
			}
			if (namelist != null && !namelist.isEmpty()) {
				return namelist.get(0).getString();
			}
		}
		return "";
	}

	private boolean isNoInventoryAtPos() {
		if (this.pos != null) {
			Level level = this.rat.level();
			return level.getBlockEntity(this.pos) == null;
		}
		return true;
	}

	@Override
	public void render(GuiGraphics graphics, int x, int y, float partialTicks) {
		this.renderBackground(graphics);
		super.render(graphics, x, y, partialTicks);
		int i = (this.width - 248) / 2 + 10;
		int j = (this.height - 166) / 2 + 8;
		if (this.rat != null) {
			RatScreen.drawEntityOnScreen(i + 114, j + 40, 70, 0, 0, this.rat, true);
		}

	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
