package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.client.model.entity.RatModel;
import com.github.alexthe666.rats.registry.RatlantisBlockRegistry;
import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import com.github.alexthe666.rats.registry.RatsParticleRegistry;
import com.github.alexthe666.rats.server.entity.projectile.ThrownBlock;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.entity.misc.LaserPortal;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.HoldsItemUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.TickRatUpgrade;
import com.github.alexthe666.rats.server.message.RatsNetworkHandler;
import com.github.alexthe666.rats.server.message.SyncThrownBlockPacket;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PsychicRatUpgradeItem extends StatBoostingRatUpgradeItem implements TickRatUpgrade, HoldsItemUpgrade {
	public PsychicRatUpgradeItem(Properties properties) {
		super(properties, 2, 2, Map::of, true);
	}

	@Override
	public void renderHeldItem(TamedRat rat, RatModel<?> model, PoseStack stack, MultiBufferSource buffer, int light, float ageInTicks) {
		if (!rat.hasItemInSlot(EquipmentSlot.HEAD)) {
			stack.pushPose();
			this.translateToHead(model, stack);
			stack.translate(0F, 0.1F, 0.035F);
			stack.mulPose(Axis.XP.rotationDegrees(180F));
			stack.mulPose(Axis.YP.rotationDegrees(180F));
			stack.scale(0.9F, 0.9F, 0.9F);
			Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(RatlantisBlockRegistry.BRAIN_BLOCK.get()), ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, stack, buffer, null, rat.getId());
			stack.popPose();
		}
	}

	@Override
	public boolean isFakeHandRender() {
		return true;
	}

	@Override
	public void tick(TamedRat rat) {
		if (rat.getLevel().isClientSide() && rat.getRandom().nextInt(5) == 0) {
			float sitAddition = 0.125f * (rat.sitProgress / 20F);
			float radius = 0.45F - sitAddition;
			float angle = (0.01745329251F * (rat.yBodyRot));
			double extraX = (double) (radius * Mth.sin((float) (Math.PI + angle))) + rat.getX();
			double extraZ = (double) (radius * Mth.cos(angle)) + rat.getZ();
			double extraY = 0.12 + rat.getY() + sitAddition;
			float particleRand = 0.4F;
			rat.getLevel().addParticle(RatsParticleRegistry.LIGHTNING.get(),
					extraX + (double) (rat.getRandom().nextFloat() * particleRand * 2) - (double) particleRand,
					extraY,
					extraZ + (double) (rat.getRandom().nextFloat() * particleRand * 2) - (double) particleRand,
					0F, 0.0F, 0F);
		}
		if (rat.rangedAttackCooldown == 0 && rat.getTarget() != null) {
			if (rat.getRandom().nextBoolean() && RatConfig.ratPsychicThrowsBlocks) {
				rat.rangedAttackCooldown = 50;
				BlockPos ourPos = rat.blockPosition();
				int searchRange = 10;
				List<BlockPos> listOfAll = new ArrayList<>();
				for (BlockPos pos : BlockPos.betweenClosedStream(ourPos.offset(-searchRange, -searchRange, -searchRange), ourPos.offset(searchRange, searchRange, searchRange)).map(BlockPos::immutable).toList()) {
					BlockState state = rat.getLevel().getBlockState(pos);
					if (!rat.getLevel().isEmptyBlock(pos) && state.canEntityDestroy(rat.getLevel(), pos, rat)) {
						listOfAll.add(pos);
					}
				}
				if (listOfAll.size() > 0) {
					BlockPos pos = listOfAll.get(rat.getRandom().nextInt(listOfAll.size()));
					ThrownBlock thrownBlock = new ThrownBlock(RatsEntityRegistry.THROWN_BLOCK.get(), rat.getLevel(), rat.getLevel().getBlockState(pos), rat);
					thrownBlock.setPos(pos.getX() + 0.5D, pos.getY() + 2.5D, pos.getZ() + 0.5D);
					thrownBlock.dropBlock = false;
					if (!rat.getLevel().isClientSide()) {
						rat.getLevel().addFreshEntity(thrownBlock);
					}
					RatsNetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new SyncThrownBlockPacket(thrownBlock.getId(), pos.asLong()));
				} else {
					rat.rangedAttackCooldown += 5;
				}
			} else {
				rat.rangedAttackCooldown = 100;
				int bounds = 5;
				for (int i = 0; i < rat.getRandom().nextInt(2) + 1; i++) {
					LaserPortal laserPortal = new LaserPortal(RatlantisEntityRegistry.LASER_PORTAL.get(), rat.getLevel(), rat.getTarget().getX() + rat.getRandom().nextInt(bounds * 2) - bounds, rat.getY() + 2, rat.getTarget().getZ() + rat.getRandom().nextInt(bounds * 2) - bounds, rat);
					rat.getLevel().addFreshEntity(laserPortal);
				}
			}
		}
	}
}
