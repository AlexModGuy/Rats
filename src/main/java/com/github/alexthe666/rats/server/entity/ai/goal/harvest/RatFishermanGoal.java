package com.github.alexthe666.rats.server.entity.ai.goal.harvest;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.player.ItemFishedEvent;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class RatFishermanGoal extends BaseRatHarvestGoal {
	private final TamedRat rat;
	private boolean hasReachedWater = false;
	private boolean playedThrownSound = false;
	private int fishingCooldown = 70;

	public RatFishermanGoal(TamedRat rat) {
		super(rat);
		this.rat = rat;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (!super.canUse() || !this.checkTheBasics(false, this.rat.getDepositPos().isPresent())) {
			return false;
		}
		this.resetTarget();
		return this.getTargetBlock() != null;
	}

	@Override
	public boolean canContinueToUse() {
		return this.checkTheBasics(false, false) && this.getTargetBlock() != null;
	}

	@Override
	public void stop() {
		super.stop();
		this.playedThrownSound = false;
		this.rat.crafting = false;
		this.rat.level().broadcastEntityEvent(this.rat, (byte) 86);
		this.hasReachedWater = false;
		this.fishingCooldown = 70 + this.rat.getRandom().nextInt(20);
	}

	@Override
	public void tick() {
		if (this.getTargetBlock() != null) {
			if (this.rat.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
				if (this.hasReachedWater) {
					this.rat.getNavigation().stop();
					if (!this.playedThrownSound) {
						this.rat.playSound(SoundEvents.FISHING_BOBBER_THROW, 1.0F, 0.5F);
						this.rat.gameEvent(GameEvent.ITEM_INTERACT_START);
						this.playedThrownSound = true;
					}
				}
				if (!this.hasReachedWater) {
					this.rat.getNavigation().moveTo(this.getTargetBlock().getX() + 0.5D, this.getTargetBlock().getY(), this.getTargetBlock().getZ() + 0.5D, 1.25D);
				}
				if (this.isShore(this.getTargetBlock(), this.rat.level())) {
					double distance = this.rat.getRatDistanceCenterSq(this.getTargetBlock().getX(), this.getTargetBlock().getY(), this.getTargetBlock().getZ());
					this.hasReachedWater = distance < this.rat.getRatHarvestDistance(-2.0D);
				} else {
					this.stop();
				}
			}
		}
		if (this.hasReachedWater) {
			this.rat.level().broadcastEntityEvent(this.rat, (byte) 85);
			this.rat.crafting = true;
			if (this.fishingCooldown > 0) {
				this.fishingCooldown--;
			}
			if (this.fishingCooldown == 0) {
				this.spawnFishingLoot();
				this.rat.level().broadcastEntityEvent(this.rat, (byte) 101);
				this.rat.playSound(SoundEvents.FISHING_BOBBER_SPLASH, 1.0F, 1.0F);
				this.rat.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
				this.stop();
			}
		}
	}

	private void resetTarget() {
		List<BlockPos> allBlocks = new ArrayList<>();
		int RADIUS = this.rat.getRadius();
		for (BlockPos pos : BlockPos.betweenClosedStream(this.rat.getSearchCenter().offset(-RADIUS, -RADIUS, -RADIUS), this.rat.getSearchCenter().offset(RADIUS, RADIUS, RADIUS)).map(BlockPos::immutable).toList()) {
			if (this.isShore(pos, this.rat.level())) {
				allBlocks.add(pos);
			}
		}
		if (!allBlocks.isEmpty()) {
			allBlocks.sort(this.getTargetSorter());
			this.setTargetBlock(allBlocks.get(0));
		}
	}

	private boolean isShore(BlockPos pos, Level world) {
		for (Direction facing : Direction.Plane.HORIZONTAL) {
			if (world.getBlockState(pos.relative(facing)).is(Blocks.WATER) && world.getBlockState(pos).isSolidRender(this.rat.level(), pos) && world.isEmptyBlock(pos.above())) {
				return true;
			}
		}
		return false;
	}

	public void spawnFishingLoot() {
		float luck = 0.1F;
		FakePlayer player = FakePlayerFactory.getMinecraft((ServerLevel) this.rat.level());
		player.setPos(this.rat.position());

		FishingHook hook = new FishingHook(player, this.rat.level(), this.rat.getRandom().nextInt(4), 0);
		hook.setPos(this.rat.position());
		LootParams params = (new LootParams.Builder((ServerLevel) this.rat.level()))
				.withParameter(LootContextParams.ORIGIN, this.rat.position())
				.withParameter(LootContextParams.TOOL, EnchantmentHelper.enchantItem(this.rat.getRandom(), new ItemStack(Items.FISHING_ROD), 100, true))
				.withParameter(LootContextParams.THIS_ENTITY, hook)
				.withParameter(LootContextParams.KILLER_ENTITY, player)
				.withLuck(luck + hook.luck)
				.create(LootContextParamSets.FISHING);
		List<ItemStack> result = this.rat.level().getServer().getLootData().getLootTable(BuiltInLootTables.FISHING).getRandomItems(params);
		ItemFishedEvent event = new ItemFishedEvent(result, 1, hook);
		MinecraftForge.EVENT_BUS.post(event);
		if (!event.isCanceled()) {
			this.holdItemHarvestedIfPossible(this.rat, result);
		}
	}
}
