package com.github.alexthe666.rats.server.misc;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.registry.RatVariantRegistry;
import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.block.RatTubeBlock;
import com.github.alexthe666.rats.server.entity.rat.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings("deprecation")
public class RatUtils {

	public static final Predicate<LivingEntity> UNTAMED_RAT_SELECTOR = entity -> entity instanceof AbstractRat rat && !rat.isTame();

	public static boolean isRidingOrBeingRiddenBy(Entity us, Entity entity) {
		for (Entity selected : us.getPassengers()) {
			if (selected.equals(entity)) {
				return true;
			}

			if (isRidingOrBeingRiddenBy(selected, entity)) {
				return true;
			}
		}

		return false;
	}

	public static BlockPos getPositionRelativetoWater(TamedRat rat, Level level, double x, double z, RandomSource rng) {
		BlockPos pos;
		BlockPos topY = BlockPos.containing(x, rat.getY(), z);
		BlockPos bottomY = BlockPos.containing(x, rat.getY(), z);
		while (level.getBlockState(topY).getMaterial() == Material.WATER && topY.getY() < level.getMaxBuildHeight()) {
			topY = topY.above();
		}
		while (level.getBlockState(bottomY).getMaterial() == Material.WATER && bottomY.getY() > level.getMinBuildHeight()) {
			bottomY = bottomY.below();
		}
		for (int tries = 0; tries < 5; tries++) {
			pos = BlockPos.containing(x, bottomY.getY() + 1 + rng.nextInt(Math.max(1, topY.getY() - bottomY.getY() - 2)), z);
			if (level.getBlockState(pos).getMaterial() == Material.WATER) {
				return pos;
			}
		}
		return rat.blockPosition();
	}

	public static boolean isRatFood(ItemStack stack) {
		return (stack.getItem().isEdible() || stack.is(Tags.Items.SEEDS) || stack.is(Items.WHEAT)) && !stack.is(RatsItemRegistry.RAW_RAT.get()) && !stack.is(RatsItemRegistry.COOKED_RAT.get());
	}

	public static boolean shouldRaidItem(ItemStack stack) {
		return isRatFood(stack) && !stack.is(RatsItemRegistry.CONTAMINATED_FOOD.get());
	}

	public static boolean doesContainFood(Container inventory) {
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			if (isRatFood(inventory.getItem(i))) {
				return true;
			}
		}
		return false;
	}

	public static ItemStack getFoodFromInventory(Container inventory, RandomSource random) {
		List<ItemStack> items = new ArrayList<>();
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if (shouldRaidItem(stack)) {
				items.add(stack);
			}
		}
		if (items.isEmpty()) {
			return ItemStack.EMPTY;
		} else if (items.size() == 1) {
			return items.get(0);
		} else {
			return items.get(random.nextInt(items.size() - 1));
		}
	}

	public static int getContaminatedSlot(Container inventory, RandomSource random) {
		List<Integer> items = new ArrayList<>();
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if (stack.isEmpty() || stack.is(RatsItemRegistry.CONTAMINATED_FOOD.get())) {
				items.add(i);
			}
		}
		if (items.isEmpty()) {
			return -1;
		} else if (items.size() == 1) {
			return items.get(0);
		} else {
			return items.get(random.nextInt(items.size() - 1));
		}
	}

	public static int getItemSlotFromItemHandler(TamedRat rat, IItemHandler handler, RandomSource random) {
		List<Integer> items = new ArrayList<>();
		for (int i = 0; i < handler.getSlots(); i++) {
			ItemStack stack = handler.extractItem(i, handler.getSlotLimit(i), true);
			if (rat.canRatPickupItem(stack)) {
				items.add(i);
			}
		}
		if (items.isEmpty()) {
			return -1;
		} else if (items.size() == 1) {
			return items.get(0);
		} else {
			return items.get(random.nextInt(items.size()));
		}
	}

	public static boolean isPredator(Entity entity) {
		return entity instanceof Ocelot || entity instanceof Cat || entity instanceof Fox;
	}

	public static RatCommand wrapCommand(int newCommand) {
		int length = RatCommand.values().length;
		if (newCommand >= length) {
			newCommand = 0;
		}
		if (newCommand < 0) {
			newCommand = length - 1;
		}
		return RatCommand.values()[newCommand];
	}

	public static BlockPos findLowestWater(BlockPos pos, PathfinderMob rat) {
		if (rat.getLevel().getBlockState(pos).getMaterial() == Material.WATER) {
			return pos;
		} else {
			BlockPos blockpos;
			do {
				blockpos = pos.below();
			} while (blockpos.getY() > rat.getLevel().getMinBuildHeight() && rat.getLevel().getBlockState(blockpos).getMaterial() != Material.WATER);
			return blockpos;
		}
	}

	public static boolean canRatBreakBlock(Level level, BlockPos pos, DiggingRat rat) {
		BlockState blockState = level.getBlockState(pos);
		if (level.getBlockEntity(pos) != null) {
			return false;
		}
		float hardness = blockState.getDestroySpeed(level, pos);
		return hardness >= 0.0F && hardness <= RatConfig.ratStrengthThreshold && ForgeHooks.canEntityDestroy(level, pos, rat);
	}

	public static boolean canRatPlaceBlock(Level level, BlockPos pos, DiggingRat rat) {
		return ForgeEventFactory.getMobGriefingEvent(level, rat) && !ForgeEventFactory.onBlockPlace(rat, BlockSnapshot.create(level.dimension(), level, pos), Direction.UP);
	}

	public static boolean isBlockProtected(Level level, BlockPos pos, DiggingRat rat) {
		return !ForgeEventFactory.getMobGriefingEvent(level, rat) || !ForgeEventFactory.onEntityDestroyBlock(rat, pos, level.getBlockState(pos));
	}

	public static boolean isOpenRatTube(BlockGetter getter, BlockPos pos) {
		BlockState state = getter.getBlockState(pos);
		if (state.getBlock() instanceof RatTubeBlock) {
			for (int i = 0; i < Direction.values().length; i++) {
				BooleanProperty bool = RatTubeBlock.ALL_OPEN_PROPS[i];
				if (state.getValue(bool)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isCow(Entity entity) {
		String s = entity.getType().getDescriptionId();
		return entity instanceof Cow || s.contains("cow");
	}

	public static void polinateAround(Level level, BlockPos position) {
		int RADIUS = 10;
		List<BlockPos> allBlocks = new ArrayList<>();
		for (BlockPos pos : BlockPos.betweenClosedStream(position.offset(-RADIUS, -RADIUS, -RADIUS), position.offset(RADIUS, RADIUS, RADIUS)).map(BlockPos::immutable).toList()) {
			if (canPlantBeBonemealed(level, pos, level.getBlockState(pos))) {
				allBlocks.add(pos);
			}
		}
		if (!allBlocks.isEmpty()) {
			for (BlockPos pos : allBlocks) {
				BlockState block = level.getBlockState(pos);
				if (block.getBlock() instanceof BonemealableBlock igrowable) {
					if (igrowable.isValidBonemealTarget(level, pos, block, level.isClientSide()) && level.getRandom().nextInt(3) == 0) {
						if (!level.isClientSide()) {
							level.levelEvent(2005, pos, 0);
							igrowable.performBonemeal((ServerLevel) level, level.getRandom(), pos, block);
						}
					}
				}
			}
		}
	}

	private static boolean canPlantBeBonemealed(Level level, BlockPos pos, BlockState BlockState) {
		if (BlockState.getBlock() instanceof BonemealableBlock igrowable && !(BlockState.getBlock() instanceof TallGrassBlock) && !(BlockState.getBlock() instanceof GrassBlock)) {
			if (igrowable.isValidBonemealTarget(level, pos, BlockState, level.isClientSide())) {
				if (!level.isClientSide()) {
					//  igrowable.grow(level, level.rand, target, BlockState);
					return igrowable.isBonemealSuccess(level, level.getRandom(), pos, BlockState);
				}
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static void accelerateTick(Level level, BlockPos pos) {
		BlockState blockState = level.getBlockState(pos);
		Block block = blockState.getBlock();
		if (!level.isClientSide() && level instanceof ServerLevel) {
			if (block.isRandomlyTicking(blockState) && level.getRandom().nextInt(40) == 0) {
				block.randomTick(blockState, (ServerLevel) level, pos, level.getRandom());
			}
		}
		BlockEntity entity = level.getBlockEntity(pos);
		if (entity != null) {
			BlockEntityTicker<BlockEntity> ticker = blockState.getTicker(level, (BlockEntityType<BlockEntity>) entity.getType());
			if (ticker != null)
				ticker.tick(level, pos, blockState, entity);
		}
	}

	public static TamedRat tameRat(Rat rat, Level level) {
		TamedRat newRat = new TamedRat(RatsEntityRegistry.TAMED_RAT.get(), level);
		CompoundTag tag = new CompoundTag();
		rat.addAdditionalSaveData(tag);
		newRat.setToga(rat.hasToga());
		newRat.moveTo(rat.blockPosition(), rat.getYRot(), rat.getXRot());
		ForgeEventFactory.onFinalizeSpawn(newRat, (ServerLevelAccessor) level, level.getCurrentDifficultyAt(rat.blockPosition()), MobSpawnType.EVENT, null, null);
		newRat.readAdditionalSaveData(tag);
		newRat.setColorVariant(rat.getColorVariant());
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			newRat.setItemSlot(slot, rat.getItemBySlot(slot));
		}
		level.addFreshEntity(newRat);
		newRat.getLevel().broadcastEntityEvent(rat, (byte) 83);
		newRat.setYRot(rat.getYRot());
		rat.discard();
		return newRat;
	}

	//helper method to convert the old rat variant integer system to the new registry
	public static RatVariant convertOldRatVariant(int variant) {
		return switch (variant) {
			case 1 -> RatVariantRegistry.BLACK.get();
			case 2 -> RatVariantRegistry.BROWN.get();
			case 3 -> RatVariantRegistry.GREEN.get();
			case 4 -> RatVariantRegistry.ALBINO.get();
			case 5 -> RatVariantRegistry.HOODED.get();
			case 6 -> RatVariantRegistry.BROWN_HOODED.get();
			case 7 -> RatVariantRegistry.GRAY_HOODED.get();
			case 8 -> RatVariantRegistry.SIAMESE.get();
			case 9 -> RatVariantRegistry.WHITE.get();
			case 10 -> RatVariantRegistry.YELLOW_HOODED.get();
			case 11, 12 -> RatVariantRegistry.BROWN_UNDERCOAT.get();
			default -> RatVariantRegistry.BLUE.get();
		};
	}
}
