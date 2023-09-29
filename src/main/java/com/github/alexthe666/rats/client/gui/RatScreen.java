package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.util.EntityRenderingUtil;
import com.github.alexthe666.rats.server.entity.rat.RatCommand;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.inventory.RatMenu;
import com.github.alexthe666.rats.server.message.RatCommandPacket;
import com.github.alexthe666.rats.server.message.RatsNetworkHandler;
import com.github.alexthe666.rats.server.misc.RatUtils;
import com.github.alexthe666.rats.server.misc.RatsLangConstants;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class RatScreen extends AbstractContainerScreen<RatMenu> {
	protected static final ResourceLocation TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/gui/container/rat_inventory.png");
	private static final ResourceLocation TEXTURE_BACKDROP = new ResourceLocation(RatsMod.MODID, "textures/gui/container/rat_inventory_backdrop.png");
	private int currentDisplayCommand = 0;
	private final TamedRat rat;

	public RatScreen(RatMenu container, Inventory inv, TamedRat rat) {
		super(container, inv, rat.getDisplayName());
		this.rat = rat;
		this.imageWidth = 192;
		this.imageHeight = 166;
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(graphics);
		super.render(graphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(graphics, mouseX, mouseY);
	}

	@Override
	protected void init() {
		super.init();
		this.renderables.clear();
		int i = (this.width - 248) / 2;
		int j = (this.height - 166) / 2;
		if (!this.rat.isBaby()) {
			this.addRenderableWidget(new ChangeCommandButton(i + 116, j + 54, false, button -> {
				this.currentDisplayCommand--;
				this.currentDisplayCommand = RatUtils.wrapCommand(this.currentDisplayCommand).ordinal();
			}));
			this.addRenderableWidget(new ChangeCommandButton(i + 199, j + 54, true, button -> {
				this.currentDisplayCommand++;
				this.currentDisplayCommand = RatUtils.wrapCommand(this.currentDisplayCommand).ordinal();
			}));
			this.addRenderableWidget(new CommandPressButton(i + 123, j + 52, button -> {
				this.rat.setCommand(RatCommand.values()[this.currentDisplayCommand]);
				RatsNetworkHandler.CHANNEL.sendToServer(new RatCommandPacket(this.rat.getId(), this.currentDisplayCommand));
			}));
		}
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
		this.renderBackground(graphics);
		int k = (this.width - this.imageWidth) / 2;
		int l = (this.height - this.imageHeight) / 2;
		graphics.blit(TEXTURE_BACKDROP, k - 8, l, 0, 0, this.imageWidth, this.imageHeight);
		EntityRenderingUtil.drawEntityOnScreen(graphics, k + 42, l + 64, 70, k + 51 - (float) mouseX, l + 75 - 50 - (float) mouseY, this.rat, false);
		graphics.blit(TEXTURE, k - 8, l, 0, 0, this.imageWidth, this.imageHeight);
		graphics.blit(TEXTURE, k + 52, l + 20, this.rat.isMale() ? 0 : 16, 209, 16, 16);
	}

	@Override
	protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
		graphics.drawString(this.font, this.getTitle(), this.imageWidth / 2 - this.font.width(this.getTitle()) / 2, 6, 4210752, false);

		Component commandDesc = Component.translatable(RatsLangConstants.RAT_CURRENT_COMMAND);
		graphics.drawString(this.font, commandDesc, this.imageWidth / 2 - this.font.width(commandDesc) / 2 + 38, 19, 4210752, false);

		Component command = Component.translatable(rat.getCommand().getTranslateName());
		graphics.drawString(this.font, command, this.imageWidth / 2 - this.font.width(command) / 2 + 36, 31, 0XFFFFFF, false);

		Component statusDesc = Component.translatable(RatsLangConstants.RAT_COMMAND_SET);
		graphics.drawString(this.font, statusDesc, this.imageWidth / 2 - this.font.width(statusDesc) / 2 + 36, 44, 4210752, false);
		RatCommand command1 = RatUtils.wrapCommand(currentDisplayCommand);
		Component command2 = Component.translatable(command1.getTranslateName());
		graphics.drawString(this.font, command2, this.imageWidth / 2 - this.font.width(command2) / 2 + 36, 56, 0XFFFFFF, false);
		int i = (this.width - 248) / 2;
		int j = (this.height - 166) / 2;
		if (mouseX > i + 116 && mouseX < i + 198 && mouseY > j + 22 && mouseY < j + 45) {
			MutableComponent commandText = Component.translatable(this.rat.getCommand().getTranslateDescription());
			String[] everySpace = commandText.getString().split(" ");
			int currentStrLength = 0;
			StringBuilder builtString = new StringBuilder();
			ArrayList<String> list = new ArrayList<>();
			for (String s : everySpace) {
				builtString.append(s).append(" ");
				currentStrLength += this.font.width(s + " ");
				if (currentStrLength >= 95) {
					list.add(builtString.toString());
					builtString = new StringBuilder();
					currentStrLength = 0;
				}
			}
			List<MutableComponent> convertedList = new ArrayList<>();
			for (String str : list) {
				convertedList.add(Component.literal(str));
			}
			graphics.renderTooltip(this.font, Lists.transform(convertedList, Component::getVisualOrderText), mouseX - i - 30, mouseY - j + 10);
		}
		if (mouseX > i + 116 && mouseX < i + 198 && mouseY > j + 53 && mouseY < j + 69) {
			MutableComponent commandText = Component.translatable(command1.getTranslateDescription());
			graphics.renderTooltip(this.font, this.font.split(commandText, 110), mouseX - i - 30, mouseY - j + 10);
		}
	}
}
