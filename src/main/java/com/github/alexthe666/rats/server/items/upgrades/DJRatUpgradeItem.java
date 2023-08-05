package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.client.model.entity.RatModel;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.HoldsItemUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.TickRatUpgrade;
import com.github.alexthe666.rats.server.message.RatsNetworkHandler;
import com.github.alexthe666.rats.server.message.UpdateRatMusicPacket;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.PacketDistributor;

public class DJRatUpgradeItem extends BaseRatUpgradeItem implements HoldsItemUpgrade, TickRatUpgrade {

	private ItemStack heldRecord = ItemStack.EMPTY;

	public DJRatUpgradeItem(Properties properties) {
		super(properties, 1, 1);
	}

	@Override
	public boolean shouldDepositItem(TamedRat rat, ItemStack stack) {
		return !(stack.getItem() instanceof RecordItem);
	}

	@Override
	public void renderHeldItem(TamedRat rat, RatModel<?> model, PoseStack stack, MultiBufferSource buffer, int light, float ageInTicks) {
		model.translateToBody(stack);
		stack.scale(-0.35F, -0.35F, 0.35F);
		stack.translate(-0.5F, -0.5F, -0.65F);
		Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Blocks.JUKEBOX.defaultBlockState(), stack, buffer, light, OverlayTexture.NO_OVERLAY);
	}

	@Override
	public boolean isFakeHandRender() {
		return true;
	}

	@Override
	public void tick(TamedRat rat) {
		ItemStack oldStack = this.heldRecord;
		this.heldRecord = rat.getMainHandItem();
		if (this.heldRecord.getItem() instanceof RecordItem record) {
			if (rat.level().isClientSide()) {
				if (ItemStack.isSameItemSameTags(oldStack, this.heldRecord)) {
					if (rat.tickCount % 10 == 0) {
						rat.level().addParticle(ParticleTypes.NOTE, rat.getX(), rat.getEyeY() + 0.25D, rat.getZ(), rat.getRandom().nextInt(4) / 24.0F, 0.0F, 0.0F);
					}
				}
			} else {
				if (!ItemStack.isSameItemSameTags(oldStack, this.heldRecord)) {
					RatsNetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> rat.level().getChunkAt(rat.blockPosition())), new UpdateRatMusicPacket(rat.getId(), record));
				}
			}
		}
	}
}
