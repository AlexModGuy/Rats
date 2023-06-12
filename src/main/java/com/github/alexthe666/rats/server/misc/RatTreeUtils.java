package com.github.alexthe666.rats.server.misc;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MangroveRootsBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RatTreeUtils {

	//stores the top of the tree, both as a position and how tall the tree itself is
	public static final HashMap<BlockPos, Integer> highestleaf = new HashMap<>();

	public static int calculateLogAmount(Level level, BlockPos pos) {
		highestleaf.put(pos, 0);

		int leafcount = 8;
		int logcount = 0;
		int prevleafcount = -1;
		int prevlogcount = -1;

		int highesty = 0;
		for (int y = 1; y <= 30; y += 1) {
			if (prevleafcount == leafcount && prevlogcount == logcount) {
				break;
			}
			prevleafcount = leafcount;
			prevlogcount = logcount;

			Iterator<BlockPos> it = BlockPos.betweenClosedStream(pos.getX() - 2, pos.getY() + (y - 1), pos.getZ() - 2, pos.getX() + 2, pos.getY() + (y - 1), pos.getZ() + 2).iterator();
			while (it.hasNext()) {
				BlockPos currentPos = it.next();
				BlockState currentState = level.getBlockState(currentPos);
				if (isTreeLeaf(currentState)) {
					leafcount -= 1;
					if (currentPos.getY() > highesty) {
						highesty = currentPos.getY();
					}
				} else if (isTreeLog(currentState)) {
					logcount += 1;
				}
			}
		}

		highestleaf.put(pos.immutable(), highesty);

		if (leafcount < 0) {
			return logcount;
		}
		return -1;
	}

	//collects all stump blocks on the tree. This helps figure out where to place saplings.
	public static List<BlockPos> getAllStumpBlocks(Level level, BlockPos pos, BlockState logtype) {
		CopyOnWriteArrayList<BlockPos> bottomlogs = new CopyOnWriteArrayList<>();
		BlockState blockbelow = level.getBlockState(pos.below());
		if (blockbelow.is(BlockTags.DIRT)) {
			Iterator<BlockPos> it = BlockPos.betweenClosedStream(pos.getX() - 1, pos.getY(), pos.getZ() - 1, pos.getX() + 1, pos.getY(), pos.getZ() + 1).iterator();
			while (it.hasNext()) {
				BlockPos npos = it.next();
				BlockState block = level.getBlockState(npos);
				if (logtype.is(block.getBlock()) || areEqualLogTypes(logtype, block)) {
					bottomlogs.add(npos.immutable());
				}
			}
		}
		return bottomlogs;
	}

	//a small method to pull the sapling from the leaves loot table. Returns null if it doesnt find anything
	@Nullable
	public static Block getSaplingFromLeaves(ServerLevel level, Block leaves) {
		try {
			LootTable loot = level.getServer().getLootData().getLootTable(leaves.getLootTable());
			LootParams.Builder context = new LootParams.Builder(level).withParameter(LootContextParams.TOOL, createMaxHoe()).withParameter(LootContextParams.BLOCK_STATE, leaves.defaultBlockState()).withParameter(LootContextParams.ORIGIN, Vec3.ZERO).withLuck(Float.MAX_VALUE);
			for (int i = 0; i < 25; i++) {
				ObjectArrayList<ItemStack> lootStacks = loot.getRandomItems(context.create(LootContextParamSets.BLOCK));
				for (ItemStack stack : lootStacks) {
					if (ForgeRegistries.ITEMS.tags().getTag(ItemTags.SAPLINGS).contains(stack.getItem()) || Block.byItem(stack.getItem()) instanceof SaplingBlock) {
						return Block.byItem(stack.getItem());
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	private static ItemStack createMaxHoe() {
		ItemStack hoe = new ItemStack(Items.NETHERITE_HOE);
		hoe.enchant(Enchantments.BLOCK_FORTUNE, Byte.MAX_VALUE);
		return hoe;
	}

	//collects a list of positions where logs in, so you can queue them to be destroyed
	public static List<BlockPos> getLogsToBreak(Level level, BlockPos pos, List<BlockPos> logsToBreak, BlockState logType) {
		List<BlockPos> checkAround = new ArrayList<>();

		boolean isMangrove = isMangroveRootOrLog(logType);
		int downY = pos.getY() - 1;

		List<BlockPos> aroundLogs = new ArrayList<>();
		for (BlockPos posToCheck : BlockPos.betweenClosed(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1)) {
			aroundLogs.add(posToCheck.immutable());
		}

		for (BlockPos aroundLogPos : aroundLogs) {
			if (logsToBreak.contains(aroundLogPos)) {
				continue;
			}

			BlockState logstate = level.getBlockState(aroundLogPos);
			if (logstate.equals(logType) || areEqualLogTypes(logType, logstate)) {
				if (!isMangrove || aroundLogPos.getY() != downY) {
					checkAround.add(aroundLogPos);
				}
				logsToBreak.add(aroundLogPos);
			}
		}

		if (checkAround.size() == 0) {
			return logsToBreak;
		}

		for (BlockPos capos : checkAround) {
			for (BlockPos logpos : getLogsToBreak(level, capos, logsToBreak, logType)) {
				if (!logsToBreak.contains(logpos)) {
					logsToBreak.add(logpos.immutable());
				}
			}
		}

		BlockPos up = pos.above(2);
		return getLogsToBreak(level, up.immutable(), logsToBreak, logType);
	}

	public static Pair<Integer, Integer> getHorizontalAndVerticalValue(int logcount) {
		int h = 5; // horizontal
		int v = 5; // vertical
		if (logcount >= 20) {
			h = 8;
			v = 8;
		} else if (logcount >= 15) {
			h = 7;
			v = 7;
		} else if (logcount >= 10) {
			h = 6;
		}

		return new Pair<>(h, v);
	}


	public static Pair<Boolean, List<BlockPos>> isConnectedToLogs(Level level, BlockPos startpos) {
		List<BlockPos> recursiveList = getBlocksNextToEachOtherMaterial(level, startpos, 6);
		for (BlockPos connectedpos : recursiveList) {
			BlockState connectedblock = level.getBlockState(connectedpos);
			if (isTreeLog(connectedblock)) {
				return new Pair<>(true, recursiveList);
			}
		}
		return new Pair<>(false, recursiveList);
	}

	public static boolean isTreeLog(BlockState block) {
		return block.is(BlockTags.LOGS) || isTreeRoot(block);
	}

	public static boolean areEqualLogTypes(BlockState one, BlockState two) {
		if (!isTreeLog(one) || !isTreeLog(two)) {
			return false;
		}

		if (isMangroveRootOrLog(one) && isMangroveRootOrLog(two)) {
			return true;
		}

		return one.is(two.getBlock());
	}

	public static boolean isTreeRoot(BlockState block) {
		return block.getBlock() instanceof MangroveRootsBlock;
	}

	public static boolean isMangroveRootOrLog(BlockState block) {
		return block.getBlock() instanceof MangroveRootsBlock || block.is(BlockTags.MANGROVE_LOGS);
	}

	public static boolean isTreeLeaf(BlockState block) {
		return block.is(BlockTags.LEAVES) || block.is(BlockTags.WART_BLOCKS) || block.is(Blocks.SHROOMLIGHT);
	}

	private static final HashMap<BlockPos, Integer> rgnbmcount = new HashMap<>();

	public static List<BlockPos> getBlocksNextToEachOtherMaterial(Level level, BlockPos startpos, int maxDistance) {
		List<BlockPos> checkedblocks = new ArrayList<>();
		List<BlockPos> theblocksaround = new ArrayList<>();
		if (level.getBlockState(startpos).is(BlockTags.LOGS) || level.getBlockState(startpos).is(BlockTags.LEAVES)) {
			theblocksaround.add(startpos);
			checkedblocks.add(startpos);
		}

		rgnbmcount.put(startpos.immutable(), 0);
		recursiveGetNextBlocksMaterial(level, startpos, startpos, theblocksaround, checkedblocks, maxDistance);
		return theblocksaround;
	}

	private static void recursiveGetNextBlocksMaterial(Level level, BlockPos startpos, BlockPos pos, List<BlockPos> theblocksaround, List<BlockPos> checkedblocks, int maxDistance) {
		int rgnbmc = rgnbmcount.get(startpos);
		if (rgnbmc > 100) {
			return;
		}
		rgnbmcount.put(startpos, rgnbmc + 1);

		for (Direction dir : Direction.values()) {
			BlockPos pba = pos.relative(dir);
			if (checkedblocks.contains(pba)) {
				continue;
			}
			checkedblocks.add(pba);

			if (level.getBlockState(pba).is(BlockTags.LOGS) || level.getBlockState(pba).is(BlockTags.LEAVES)) {
				if (!theblocksaround.contains(pba)) {
					theblocksaround.add(pba);
					if (startpos.closerThan(pba, maxDistance)) {
						recursiveGetNextBlocksMaterial(level, startpos, pba, theblocksaround, checkedblocks, maxDistance);
					}
				}
			}
		}
	}
}
