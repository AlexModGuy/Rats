package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.rat.RatCommand;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.inventory.RatMenu;
import com.github.alexthe666.rats.server.message.RatCommandPacket;
import com.github.alexthe666.rats.server.message.RatsNetworkHandler;
import com.github.alexthe666.rats.server.misc.RatUtils;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import org.joml.Quaternionf;

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
		this.passEvents = false;
	}

	public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, LivingEntity entity, boolean rotating) {
		float rotate = (Minecraft.getInstance().getPartialTick() + Minecraft.getInstance().player.tickCount) * 2F;
		float f = (float) Math.atan(mouseX / 40.0F);
		float f1 = (float) Math.atan(mouseY / 40.0F);
		PoseStack posestack = RenderSystem.getModelViewStack();
		posestack.pushPose();
		posestack.translate(posX, posY, 1050.0D);
		posestack.scale(1.0F, 1.0F, -1.0F);
		RenderSystem.applyModelViewMatrix();
		PoseStack matrixstack = new PoseStack();
		matrixstack.translate(0.0D, 0.0D, 1000.0D);
		matrixstack.scale((float) scale, (float) scale, (float) scale);
		Quaternionf quaternion = Axis.ZP.rotationDegrees(180.0F);
		Quaternionf quaternion1 = Axis.XP.rotationDegrees(f1 * 20.0F);
		Quaternionf quaternion2 = Axis.YP.rotationDegrees(rotate);
		quaternion.mul(quaternion1);
		matrixstack.mulPose(quaternion);
		if (rotating) matrixstack.mulPose(quaternion2);
		float f2 = entity.yBodyRot;
		float f3 = entity.getYRot();
		float f4 = entity.getXRot();
		float f5 = entity.yHeadRotO;
		float f6 = entity.yHeadRot;
		entity.yBodyRot = 180.0F + f * 20.0F;
		entity.setYRot(180.0F + f * 40.0F);
		entity.setXRot(-f1 * 20.0F);
		entity.yHeadRot = entity.getYRot();
		entity.yHeadRotO = entity.getYRot();
		Lighting.setupForEntityInInventory();
		EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		quaternion1.conjugate();
		dispatcher.overrideCameraOrientation(quaternion1);
		dispatcher.setRenderShadow(false);
		MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();
		RenderSystem.runAsFancy(() -> dispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, source, 15728880));
		source.endBatch();
		dispatcher.setRenderShadow(true);
		entity.yBodyRot = f2;
		entity.setYRot(f3);
		entity.setXRot(f4);
		entity.yHeadRotO = f5;
		entity.yHeadRot = f6;
		posestack.popPose();
		RenderSystem.applyModelViewMatrix();
		Lighting.setupFor3DItems();
	}

	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(stack);
		super.render(stack, mouseX, mouseY, partialTicks);
		this.renderTooltip(stack, mouseX, mouseY);
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
	protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
		this.renderBackground(stack);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE_BACKDROP);
		int k = (this.width - this.imageWidth) / 2;
		int l = (this.height - this.imageHeight) / 2;
		blit(stack, k - 8, l, 0, 0, this.imageWidth, this.imageHeight);
		drawEntityOnScreen(k + 42, l + 64, 70, k + 51 - (float) mouseX, l + 75 - 50 - (float) mouseY, this.rat, false);
		RenderSystem.setShaderTexture(0, TEXTURE);
		blit(stack, k - 8, l, 0, 0, this.imageWidth, this.imageHeight);
		blit(stack, k + 52, l + 20, this.rat.isMale() ? 0 : 16, 209, 16, 16);
	}

	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
		Component name = this.getTitle().getString().length() == 0 ? Component.translatable("entity.rats.rat") : this.getTitle();
		this.font.draw(stack, name, this.imageWidth / 2.0F - this.font.width(name) / 2.0F, 6, 4210752);

		Component commandDesc = Component.translatable("entity.rats.rat.command.current");
		this.font.draw(stack, commandDesc, this.imageWidth / 2.0F - this.font.width(commandDesc) / 2.0F + 38, 19, 4210752);

		Component command = Component.translatable(rat.getCommand().getTranslateName());
		this.font.draw(stack, command, this.imageWidth / 2.0F - this.font.width(command) / 2.0F + 36, 31, 0XFFFFFF);

		Component statusDesc = Component.translatable("entity.rats.rat.command.set");
		this.font.draw(stack, statusDesc, this.imageWidth / 2.0F - this.font.width(statusDesc) / 2.0F + 36, 44, 4210752);
		RatCommand command1 = RatUtils.wrapCommand(currentDisplayCommand);
		Component command2 = Component.translatable(command1.getTranslateName());
		this.font.draw(stack, command2, this.imageWidth / 2.0F - this.font.width(command2) / 2.0F + 36, 56, 0XFFFFFF);
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
			this.renderTooltip(stack, Lists.transform(convertedList, Component::getVisualOrderText), mouseX - i - 30, mouseY - j + 10);
		}
		if (mouseX > i + 116 && mouseX < i + 198 && mouseY > j + 53 && mouseY < j + 69) {
			MutableComponent commandText = Component.translatable(command1.getTranslateDescription());
			this.renderTooltip(stack, this.font.split(commandText, 110), mouseX - i - 30, mouseY - j + 10);
		}
	}
}
