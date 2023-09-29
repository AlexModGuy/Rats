package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.client.util.EntityRenderingUtil;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.message.RatsNetworkHandler;
import com.github.alexthe666.rats.server.message.SyncRatStaffPacket;
import com.github.alexthe666.rats.server.misc.RatsLangConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class RadiusStaffScreen extends Screen {

	private final TamedRat rat;
	private int sliderValue;
	private final BlockPos pos;

	public RadiusStaffScreen(TamedRat rat, BlockPos pos) {
		super(Component.translatable(RatsItemRegistry.RADIUS_STICK.get().getDescriptionId()));
		this.rat = rat;
		this.pos = pos;
		this.sliderValue = rat.getRadius();
	}

	protected void init() {
		super.init();
		this.renderables.clear();
		int i = (this.width) / 2;
		int j = (this.height - 166) / 2;
		Component topText = Component.translatable(RatsLangConstants.RAT_STAFF_SET_RADIUS, this.pos.toShortString());
		Component secondText = Component.translatable(RatsLangConstants.RAT_STAFF_RESET_RADIUS);
		int maxLength = Math.max(150, Minecraft.getInstance().font.width(topText.getString()) + 20);
		this.addRenderableWidget(Button.builder(topText, button -> {
			RatsNetworkHandler.CHANNEL.sendToServer(new SyncRatStaffPacket(this.rat.getId(), this.pos, Direction.UP, 4, 0));
			this.rat.setRadiusCenter(GlobalPos.of(Minecraft.getInstance().player.level().dimension(), this.pos));
			this.sliderValue = this.rat.getRadius();
		}).bounds(i - maxLength / 2, j + 60, maxLength, 20).build());
		this.addRenderableWidget(new AbstractSliderButton(i - 150 / 2, j + 85, 150, 20, Component.translatable(RatsLangConstants.RAT_STAFF_RADIUS, RadiusStaffScreen.this.sliderValue), 0.0D) {
			{
				this.updateMessage();
				RadiusStaffScreen.this.sliderValue = RadiusStaffScreen.this.rat.getRadius();
			}

			@Override
			protected void updateMessage() {
				this.setMessage(Component.translatable(RatsLangConstants.RAT_STAFF_RADIUS, RadiusStaffScreen.this.sliderValue));
			}

			@Override
			protected void applyValue() {
				RadiusStaffScreen.this.sliderValue = Mth.floor(Mth.clampedLerp(0, RatConfig.maxRatRadius, this.value));
				RatsNetworkHandler.CHANNEL.sendToServer(new SyncRatStaffPacket(rat.getId(), BlockPos.ZERO, Direction.UP, 5, RadiusStaffScreen.this.sliderValue));
				RadiusStaffScreen.this.rat.setRadius(Math.round(RadiusStaffScreen.this.sliderValue));
			}
		});
		this.addRenderableWidget(Button.builder(secondText, button -> {
			RatsNetworkHandler.CHANNEL.sendToServer(new SyncRatStaffPacket(this.rat.getId(), BlockPos.ZERO, Direction.UP, 6, 0));
			this.sliderValue = RatConfig.defaultRatRadius;
			this.rat.setRadiusCenter(null);
			this.rat.setRadius(RatConfig.defaultRatRadius);
		}).bounds(i - maxLength / 2, j + 110, maxLength, 20).build());
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(graphics);

		super.render(graphics, mouseX, mouseY, partialTicks);
		int i = (this.width - 248) / 2 + 10;
		int j = (this.height - 166) / 2 + 8;
		if (this.rat != null) {
			EntityRenderingUtil.drawEntityOnScreen(graphics, i + 114, j + 40, 70, 0, 0, this.rat, true);
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}

