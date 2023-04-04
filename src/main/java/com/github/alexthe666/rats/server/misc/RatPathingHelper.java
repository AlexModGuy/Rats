package com.github.alexthe666.rats.server.misc;

import com.github.alexthe666.rats.data.tags.RatsBlockTags;
import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import com.github.alexthe666.rats.server.entity.rat.DiggingRat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;

public class RatPathingHelper {

	public static boolean canSeeOrDigToBlock(DiggingRat rat, BlockPos destination) {
		BlockHitResult result = clipWithConditions(rat.getLevel(), new ClipContext(rat.position(), Vec3.atCenterOf(destination), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, rat), !rat.isOnDiggingCooldown());
		return result.getType() == HitResult.Type.MISS;
	}

	public static boolean canSeeBlock(AbstractRat rat, BlockPos destination) {
		BlockHitResult result = clipWithConditions(rat.getLevel(), new ClipContext(rat.position(), Vec3.atCenterOf(destination), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, rat), false);
		return result.getType() == HitResult.Type.MISS;
	}

	public static BlockHitResult clipWithConditions(BlockGetter getter, ClipContext ctx, boolean ignoreDiggables) {
		return traverseBlocks(ctx.getFrom(), ctx.getTo(), ctx, (context, pos) -> {
			BlockState blockstate = getter.getBlockState(pos);
			FluidState fluidstate = getter.getFluidState(pos);
			Vec3 vec3 = context.getFrom();
			Vec3 vec31 = context.getTo();
			VoxelShape voxelshape = shouldIgnoreBlockDuringTracing(getter, pos, ignoreDiggables) ? Shapes.empty() : context.getBlockShape(blockstate, getter, pos);
			BlockHitResult blockhitresult = clipWithInteractionOverride(getter, vec3, vec31, pos, voxelshape, blockstate);
			VoxelShape voxelshape1 = context.getFluidShape(fluidstate, getter, pos);
			BlockHitResult blockhitresult1 = voxelshape1.clip(vec3, vec31, pos);
			double d0 = blockhitresult == null ? Double.MAX_VALUE : context.getFrom().distanceToSqr(blockhitresult.getLocation());
			double d1 = blockhitresult1 == null ? Double.MAX_VALUE : context.getFrom().distanceToSqr(blockhitresult1.getLocation());
			return d0 <= d1 ? blockhitresult : blockhitresult1;
		}, context -> {
			Vec3 vec3 = context.getFrom().subtract(context.getTo());
			return BlockHitResult.miss(context.getTo(), Direction.getNearest(vec3.x, vec3.y, vec3.z), BlockPos.containing(context.getTo()));
		});
	}

	@Nullable
	private static BlockHitResult clipWithInteractionOverride(BlockGetter getter, Vec3 toVec, Vec3 fromVec, BlockPos pos, VoxelShape shape, BlockState state) {
		BlockHitResult blockhitresult = shape.clip(toVec, fromVec, pos);
		if (blockhitresult != null) {
			BlockHitResult blockhitresult1 = state.getInteractionShape(getter, pos).clip(toVec, fromVec, pos);
			if (blockhitresult1 != null && blockhitresult1.getLocation().subtract(toVec).lengthSqr() < blockhitresult.getLocation().subtract(toVec).lengthSqr()) {
				return blockhitresult.withDirection(blockhitresult1.getDirection());
			}
		}

		return blockhitresult;
	}

	private static <T, C> T traverseBlocks(Vec3 toVec, Vec3 fromVec, C context, BiFunction<C, BlockPos, T> posCtxFunction, Function<C, T> ctxFunction) {
		if (toVec.equals(fromVec)) {
			return ctxFunction.apply(context);
		} else {
			double d0 = Mth.lerp(-1.0E-7D, fromVec.x, toVec.x);
			double d1 = Mth.lerp(-1.0E-7D, fromVec.y, toVec.y);
			double d2 = Mth.lerp(-1.0E-7D, fromVec.z, toVec.z);
			double d3 = Mth.lerp(-1.0E-7D, toVec.x, fromVec.x);
			double d4 = Mth.lerp(-1.0E-7D, toVec.y, fromVec.y);
			double d5 = Mth.lerp(-1.0E-7D, toVec.z, fromVec.z);
			int i = Mth.floor(d3);
			int j = Mth.floor(d4);
			int k = Mth.floor(d5);
			BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(i, j, k);
			T t = posCtxFunction.apply(context, blockpos$mutableblockpos);
			if (t != null) {
				return t;
			} else {
				double d6 = d0 - d3;
				double d7 = d1 - d4;
				double d8 = d2 - d5;
				int l = Mth.sign(d6);
				int i1 = Mth.sign(d7);
				int j1 = Mth.sign(d8);
				double d9 = l == 0 ? Double.MAX_VALUE : (double)l / d6;
				double d10 = i1 == 0 ? Double.MAX_VALUE : (double)i1 / d7;
				double d11 = j1 == 0 ? Double.MAX_VALUE : (double)j1 / d8;
				double d12 = d9 * (l > 0 ? 1.0D - Mth.frac(d3) : Mth.frac(d3));
				double d13 = d10 * (i1 > 0 ? 1.0D - Mth.frac(d4) : Mth.frac(d4));
				double d14 = d11 * (j1 > 0 ? 1.0D - Mth.frac(d5) : Mth.frac(d5));

				while(d12 <= 1.0D || d13 <= 1.0D || d14 <= 1.0D) {
					if (d12 < d13) {
						if (d12 < d14) {
							i += l;
							d12 += d9;
						} else {
							k += j1;
							d14 += d11;
						}
					} else if (d13 < d14) {
						j += i1;
						d13 += d10;
					} else {
						k += j1;
						d14 += d11;
					}

					T t1 = posCtxFunction.apply(context, blockpos$mutableblockpos.set(i, j, k));
					if (t1 != null) {
						return t1;
					}
				}

				return ctxFunction.apply(context);
			}
		}
	}

	private static boolean canDigBlock(BlockGetter getter, BlockPos pos) {
		return getter.getBlockState(pos).is(RatsBlockTags.DIGGABLE_BLOCKS) && getter.getBlockState(pos).isSolidRender(getter, pos);
	}

	private static boolean shouldIgnoreBlockDuringTracing(BlockGetter getter, BlockPos pos, boolean ignoreDiggables) {
		return getter.getBlockState(pos).is(RatsBlockRegistry.RAT_HOLE.get()) || (ignoreDiggables && canDigBlock(getter, pos));
	}
}
