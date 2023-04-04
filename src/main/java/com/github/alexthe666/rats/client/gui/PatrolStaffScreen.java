package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.message.RatsNetworkHandler;
import com.github.alexthe666.rats.server.message.SyncRatTagPacket;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class PatrolStaffScreen extends Screen {

	private final TamedRat rat;
	private final BlockPos pos;
	private final List<GlobalPos> nodes = new ArrayList<>();

	public PatrolStaffScreen(TamedRat rat, BlockPos pos) {
		super(Component.translatable("patrol_staff"));
		this.rat = rat;
		this.pos = pos;
		this.nodes.clear();
		this.nodes.addAll(rat.getPatrolNodes());
	}

	@Override
	protected void init() {
		super.init();
		this.renderables.clear();
		int i = this.width / 2;
		int j = (this.height - 166) / 2;
		Component addText = Component.translatable("entity.rats.rat.staff.add_patrol_node", this.pos.toShortString());
		Component removeText = Component.translatable("entity.rats.rat.staff.remove_patrol_node", this.pos.toShortString());
		Component removeAllText = Component.translatable("entity.rats.rat.staff.remove_all_patrol_nodes", this.pos.toShortString());
		int maxLength = Math.max(150, Minecraft.getInstance().font.width(addText.getString()) + 20);
		int removeIndex = -1;

		for (int nodeIndex = 0; nodeIndex < this.nodes.size(); ++nodeIndex) {
			if (this.pos.equals(this.nodes.get(nodeIndex).pos())) {
				removeIndex = nodeIndex;
			}
		}

		if (removeIndex == -1) {
			this.addRenderableWidget(Button.builder(addText, button -> {
				this.nodes.add(GlobalPos.of(Minecraft.getInstance().player.getLevel().dimension(), this.pos));
				this.rat.getPatrolNodes().add(GlobalPos.of(Minecraft.getInstance().player.getLevel().dimension(), this.pos));
				RatsNetworkHandler.CHANNEL.sendToServer(new SyncRatTagPacket(this.rat.getId(), this.nodes));
				Minecraft.getInstance().setScreen(null);
				this.init();
			}).bounds(i - maxLength / 2, j + 60, maxLength, 20).build());
		} else {
			this.addRenderableWidget(Button.builder(removeText, button -> {
				this.nodes.remove(GlobalPos.of(Minecraft.getInstance().player.getLevel().dimension(), this.pos));
				this.rat.getPatrolNodes().remove(GlobalPos.of(Minecraft.getInstance().player.getLevel().dimension(), this.pos));
				RatsNetworkHandler.CHANNEL.sendToServer(new SyncRatTagPacket(this.rat.getId(), this.nodes));
				Minecraft.getInstance().setScreen(null);
				this.init();
			}).bounds(i - maxLength / 2, j + 60, maxLength, 20).build());
		}

		if (this.nodes.size() > 0) {
			this.addRenderableWidget(Button.builder(removeAllText, button -> {
				this.nodes.clear();
				this.rat.getPatrolNodes().clear();
				RatsNetworkHandler.CHANNEL.sendToServer(new SyncRatTagPacket(this.rat.getId(), this.nodes));
				Minecraft.getInstance().setScreen(null);
				this.init();
			}).bounds(i - maxLength / 2, j + 110, maxLength, 20).build());
		}

	}

	@Override
	public void render(PoseStack stack, int x, int y, float partialTicks) {
		this.renderBackground(stack);
		super.render(stack, x, y, partialTicks);
		int i = (this.width - 248) / 2 + 10;
		int j = (this.height - 166) / 2 + 8;
		if (this.rat != null) {
			RatScreen.drawEntityOnScreen(i + 114, j + 40, 70, 0.0F, 0.0F, this.rat, true);
		}

	}

	private CompoundTag buildNewTag() {
		CompoundTag tag = new CompoundTag();
		this.rat.addAdditionalSaveData(tag);
		ListTag listnbt = new ListTag();

		this.nodes.forEach(pos -> GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, pos).resultOrPartial(s -> {}).ifPresent(listnbt::add));

		tag.put("PatrolNodesTag", listnbt);
		return tag;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}