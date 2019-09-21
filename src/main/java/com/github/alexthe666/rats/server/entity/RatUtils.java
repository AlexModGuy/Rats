package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.BlockRatTube;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatTube;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class RatUtils {

    public static boolean isRatFood(ItemStack stack) {
        return (stack.getItem() instanceof ItemFood || isSeeds(stack) || stack.getItem() == Items.WHEAT) && stack.getItem() != RatsItemRegistry.RAW_RAT && stack.getItem() != RatsItemRegistry.COOKED_RAT;
    }

    public static boolean isSeeds(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ItemSeeds && item != Items.NETHER_WART) {
            return true;
        }
        NonNullList<ItemStack> listAllseed = OreDictionary.getOres("listAllseed");
        NonNullList<ItemStack> listAllSeeds = OreDictionary.getOres("listAllSeeds");
        NonNullList<ItemStack> seed = OreDictionary.getOres("seed");
        NonNullList<ItemStack> seeds = OreDictionary.getOres("seeds");
        return listAllseed.contains(stack) || listAllSeeds.contains(stack) || seed.contains(stack) || seeds.contains(stack);
    }

    public static boolean doesContainFood(IInventory inventory) {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            if (isRatFood(inventory.getStackInSlot(i))) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack getFoodFromInventory(EntityRat rat, IInventory inventory, Random random) {
        List<ItemStack> items = new ArrayList<ItemStack>();
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (isRatFood(stack) && rat.canRatPickupItem(stack)) {
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

    public static int getItemSlotFromItemHandler(EntityRat rat, IItemHandler handler, Random random) {
        List<Integer> items = new ArrayList<Integer>();
        if (handler == null) {
            return -1;
        }
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

    public static boolean isItemHandlerFull(IItemHandler handler, ItemStack mergeStack) {
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack itemstack = handler.getStackInSlot(i);
            if (itemstack.isEmpty() || mergeStack != null && !mergeStack.isEmpty() && itemstack.isItemEqual(mergeStack) && itemstack.getCount() + mergeStack.getCount() < itemstack.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRatHoleInBoundingBox(AxisAlignedBB bb, World world) {
        int j2 = MathHelper.floor(bb.minX);
        int k2 = MathHelper.ceil(bb.maxX);
        int l2 = MathHelper.floor(bb.minY);
        int i3 = MathHelper.ceil(bb.maxY);
        int j3 = MathHelper.floor(bb.minZ);
        int k3 = MathHelper.ceil(bb.maxZ);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        for (int l3 = j2; l3 < k2; ++l3) {
            for (int i4 = l2; i4 < i3; ++i4) {
                for (int j4 = j3; j4 < k3; ++j4) {
                    IBlockState iblockstate1 = world.getBlockState(blockpos$pooledmutableblockpos.setPos(l3, i4, j4));
                    if (iblockstate1.getBlock() == RatsBlockRegistry.RAT_HOLE || iblockstate1.getBlock() == RatsBlockRegistry.RAT_CAGE) {
                        blockpos$pooledmutableblockpos.release();
                        return true;
                    }
                }
            }
        }

        blockpos$pooledmutableblockpos.release();
        return false;
    }

    @Nullable
    public static RayTraceResult rayTraceBlocksIgnoreRatholes(World world, Vec3d start, Vec3d end, boolean stopOnLiquid) {
        return rayTraceBlocksIgnoreRatholes(world, start, end, stopOnLiquid, false, true);
    }

    @Nullable
    public static RayTraceResult rayTraceBlocksIgnoreRatholes(World world, Vec3d vec31, Vec3d vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
        if (!Double.isNaN(vec31.x) && !Double.isNaN(vec31.y) && !Double.isNaN(vec31.z)) {
            if (!Double.isNaN(vec32.x) && !Double.isNaN(vec32.y) && !Double.isNaN(vec32.z)) {
                int i = MathHelper.floor(vec32.x);
                int j = MathHelper.floor(vec32.y);
                int k = MathHelper.floor(vec32.z);
                int l = MathHelper.floor(vec31.x);
                int i1 = MathHelper.floor(vec31.y);
                int j1 = MathHelper.floor(vec31.z);
                BlockPos blockpos = new BlockPos(l, i1, j1);
                IBlockState iblockstate = world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if ((!ignoreBlockWithoutBoundingBox || iblockstate.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB) && block.canCollideCheck(iblockstate, stopOnLiquid) && block != RatsBlockRegistry.RAT_HOLE && block != RatsBlockRegistry.RAT_CAGE) {
                    RayTraceResult raytraceresult = iblockstate.collisionRayTrace(world, blockpos, vec31, vec32);

                    if (raytraceresult != null) {
                        return raytraceresult;
                    }
                }

                RayTraceResult raytraceresult2 = null;
                int k1 = 200;

                while (k1-- >= 0) {
                    if (Double.isNaN(vec31.x) || Double.isNaN(vec31.y) || Double.isNaN(vec31.z)) {
                        return null;
                    }

                    if (l == i && i1 == j && j1 == k) {
                        return returnLastUncollidableBlock ? raytraceresult2 : null;
                    }

                    boolean flag2 = true;
                    boolean flag = true;
                    boolean flag1 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l) {
                        d0 = (double) l + 1.0D;
                    } else if (i < l) {
                        d0 = (double) l + 0.0D;
                    } else {
                        flag2 = false;
                    }

                    if (j > i1) {
                        d1 = (double) i1 + 1.0D;
                    } else if (j < i1) {
                        d1 = (double) i1 + 0.0D;
                    } else {
                        flag = false;
                    }

                    if (k > j1) {
                        d2 = (double) j1 + 1.0D;
                    } else if (k < j1) {
                        d2 = (double) j1 + 0.0D;
                    } else {
                        flag1 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = vec32.x - vec31.x;
                    double d7 = vec32.y - vec31.y;
                    double d8 = vec32.z - vec31.z;

                    if (flag2) {
                        d3 = (d0 - vec31.x) / d6;
                    }

                    if (flag) {
                        d4 = (d1 - vec31.y) / d7;
                    }

                    if (flag1) {
                        d5 = (d2 - vec31.z) / d8;
                    }

                    if (d3 == -0.0D) {
                        d3 = -1.0E-4D;
                    }

                    if (d4 == -0.0D) {
                        d4 = -1.0E-4D;
                    }

                    if (d5 == -0.0D) {
                        d5 = -1.0E-4D;
                    }

                    EnumFacing enumfacing;

                    if (d3 < d4 && d3 < d5) {
                        enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                        vec31 = new Vec3d(d0, vec31.y + d7 * d3, vec31.z + d8 * d3);
                    } else if (d4 < d5) {
                        enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                        vec31 = new Vec3d(vec31.x + d6 * d4, d1, vec31.z + d8 * d4);
                    } else {
                        enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        vec31 = new Vec3d(vec31.x + d6 * d5, vec31.y + d7 * d5, d2);
                    }

                    l = MathHelper.floor(vec31.x) - (enumfacing == EnumFacing.EAST ? 1 : 0);
                    i1 = MathHelper.floor(vec31.y) - (enumfacing == EnumFacing.UP ? 1 : 0);
                    j1 = MathHelper.floor(vec31.z) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
                    blockpos = new BlockPos(l, i1, j1);
                    IBlockState iblockstate1 = world.getBlockState(blockpos);
                    Block block1 = iblockstate1.getBlock();

                    if ((!ignoreBlockWithoutBoundingBox || iblockstate1.getMaterial() == Material.PORTAL || iblockstate1.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB) && block1 != RatsBlockRegistry.RAT_HOLE && block1 != RatsBlockRegistry.RAT_CAGE) {
                        if (block1.canCollideCheck(iblockstate1, stopOnLiquid)) {
                            RayTraceResult raytraceresult1 = iblockstate1.collisionRayTrace(world, blockpos, vec31, vec32);

                            if (raytraceresult1 != null) {
                                return raytraceresult1;
                            }
                        } else {
                            raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, enumfacing, blockpos);
                        }
                    }
                }

                return returnLastUncollidableBlock ? raytraceresult2 : null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Nullable
    public static RayTraceResult rayTraceBlocksIgnoreOurRattube(World world, Vec3d vec31, Vec3d vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
        if (!Double.isNaN(vec31.x) && !Double.isNaN(vec31.y) && !Double.isNaN(vec31.z)) {
            if (!Double.isNaN(vec32.x) && !Double.isNaN(vec32.y) && !Double.isNaN(vec32.z)) {
                int i = MathHelper.floor(vec32.x);
                int j = MathHelper.floor(vec32.y);
                int k = MathHelper.floor(vec32.z);
                int l = MathHelper.floor(vec31.x);
                int i1 = MathHelper.floor(vec31.y);
                int j1 = MathHelper.floor(vec31.z);
                BlockPos blockpos = new BlockPos(l, i1, j1);
                IBlockState iblockstate = world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if ((!ignoreBlockWithoutBoundingBox || iblockstate.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB) && block.canCollideCheck(iblockstate, stopOnLiquid) && !(block instanceof BlockRatTube)) {
                    RayTraceResult raytraceresult = iblockstate.collisionRayTrace(world, blockpos, vec31, vec32);
                    if (raytraceresult != null) {
                        return raytraceresult;
                    }
                }

                RayTraceResult raytraceresult2 = null;
                int k1 = 200;

                while (k1-- >= 0) {
                    if (Double.isNaN(vec31.x) || Double.isNaN(vec31.y) || Double.isNaN(vec31.z)) {
                        return null;
                    }

                    if (l == i && i1 == j && j1 == k) {
                        return returnLastUncollidableBlock ? raytraceresult2 : null;
                    }

                    boolean flag2 = true;
                    boolean flag = true;
                    boolean flag1 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l) {
                        d0 = (double) l + 1.0D;
                    } else if (i < l) {
                        d0 = (double) l + 0.0D;
                    } else {
                        flag2 = false;
                    }

                    if (j > i1) {
                        d1 = (double) i1 + 1.0D;
                    } else if (j < i1) {
                        d1 = (double) i1 + 0.0D;
                    } else {
                        flag = false;
                    }

                    if (k > j1) {
                        d2 = (double) j1 + 1.0D;
                    } else if (k < j1) {
                        d2 = (double) j1 + 0.0D;
                    } else {
                        flag1 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = vec32.x - vec31.x;
                    double d7 = vec32.y - vec31.y;
                    double d8 = vec32.z - vec31.z;

                    if (flag2) {
                        d3 = (d0 - vec31.x) / d6;
                    }

                    if (flag) {
                        d4 = (d1 - vec31.y) / d7;
                    }

                    if (flag1) {
                        d5 = (d2 - vec31.z) / d8;
                    }

                    if (d3 == -0.0D) {
                        d3 = -1.0E-4D;
                    }

                    if (d4 == -0.0D) {
                        d4 = -1.0E-4D;
                    }

                    if (d5 == -0.0D) {
                        d5 = -1.0E-4D;
                    }

                    EnumFacing enumfacing;

                    if (d3 < d4 && d3 < d5) {
                        enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                        vec31 = new Vec3d(d0, vec31.y + d7 * d3, vec31.z + d8 * d3);
                    } else if (d4 < d5) {
                        enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                        vec31 = new Vec3d(vec31.x + d6 * d4, d1, vec31.z + d8 * d4);
                    } else {
                        enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        vec31 = new Vec3d(vec31.x + d6 * d5, vec31.y + d7 * d5, d2);
                    }

                    l = MathHelper.floor(vec31.x) - (enumfacing == EnumFacing.EAST ? 1 : 0);
                    i1 = MathHelper.floor(vec31.y) - (enumfacing == EnumFacing.UP ? 1 : 0);
                    j1 = MathHelper.floor(vec31.z) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
                    blockpos = new BlockPos(l, i1, j1);
                    IBlockState iblockstate1 = world.getBlockState(blockpos);
                    Block block1 = iblockstate1.getBlock();

                    if ((!ignoreBlockWithoutBoundingBox || iblockstate1.getMaterial() == Material.PORTAL || iblockstate1.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB)) {
                        if (block1.canCollideCheck(iblockstate1, stopOnLiquid)) {
                            RayTraceResult raytraceresult1 = iblockstate1.collisionRayTrace(world, blockpos, vec31, vec32);

                            if (raytraceresult1 != null) {
                                return raytraceresult1;
                            }
                        } else {
                            raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, enumfacing, blockpos);
                        }
                    }
                }

                return returnLastUncollidableBlock ? raytraceresult2 : null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static boolean isPredator(Entity entity) {
        return entity instanceof EntityOcelot;
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

    public static boolean isCheese(ItemStack cheese) {
        return cheese.getItem() == RatsItemRegistry.CHEESE || OreDictionary.getOres("foodCheese", false).contains(cheese);
    }

    @Nullable
    public static Vec3d findRandomCageOrTubeTarget(EntityRat rat, int xz, int y) {
        return generateRandomCageOrTubePos(rat, xz, y, null, false);
    }

    @Nullable
    public static Vec3d generateRandomCageOrTubePos(EntityRat rat, int searchWidth, int searchHeight, @Nullable Vec3d positionVector, boolean water) {
        PathNavigate pathnavigate = rat.getNavigator();
        Random random = rat.getRNG();
        boolean flag;

        if (rat.hasHome()) {
            double d0 = rat.getHomePosition().distanceSq((double) MathHelper.floor(rat.posX), (double) MathHelper.floor(rat.posY), (double) MathHelper.floor(rat.posZ)) + 4.0D;
            double d1 = (double) (rat.getMaximumHomeDistance() + (float) searchWidth);
            flag = d0 < d1 * d1;
        } else {
            flag = false;
        }

        boolean flag1 = false;
        float f = -99999.0F;
        int k1 = 0;
        int i = 0;
        int j = 0;

        for (int k = 0; k < 10; ++k) {
            int l = random.nextInt(2 * searchWidth + 1) - searchWidth;
            int i1 = random.nextInt(2 * searchHeight + 1) - searchHeight;
            int j1 = random.nextInt(2 * searchWidth + 1) - searchWidth;

            if (positionVector == null || (double) l * positionVector.x + (double) j1 * positionVector.z >= 0.0D) {
                if (rat.hasHome() && searchWidth > 1) {
                    BlockPos blockpos = rat.getHomePosition();

                    if (rat.posX > (double) blockpos.getX()) {
                        l -= random.nextInt(searchWidth / 2);
                    } else {
                        l += random.nextInt(searchWidth / 2);
                    }

                    if (rat.posZ > (double) blockpos.getZ()) {
                        j1 -= random.nextInt(searchWidth / 2);
                    } else {
                        j1 += random.nextInt(searchWidth / 2);
                    }
                }

                BlockPos blockpos1 = new BlockPos((double) l + rat.posX, (double) i1 + rat.posY, (double) j1 + rat.posZ);

                if ((!flag || rat.isWithinHomeDistanceFromPosition(blockpos1)) && (rat.world.getBlockState(blockpos1).getBlock() == RatsBlockRegistry.RAT_CAGE || rat.world.getBlockState(blockpos1).getBlock() instanceof BlockRatTube)) {
                    blockpos1 = findLowestRatCage(blockpos1, rat);
                    float f1 = rat.getBlockPathWeight(blockpos1);

                    if (f1 > f) {
                        f = f1;
                        k1 = l;
                        i = i1;
                        j = j1;
                        flag1 = true;
                    }
                }
            }
        }

        if (flag1) {
            return new Vec3d((double) k1 + rat.posX, (double) i + rat.posY, (double) j + rat.posZ);
        } else {
            return null;
        }
    }

    @Nullable
    public static Vec3d generateRandomCagePos(EntityRat rat, int searchWidth, int searchHeight, @Nullable Vec3d positionVector, boolean water) {
        PathNavigate pathnavigate = rat.getNavigator();
        Random random = rat.getRNG();
        boolean flag;

        if (rat.hasHome()) {
            double d0 = rat.getHomePosition().distanceSq((double) MathHelper.floor(rat.posX), (double) MathHelper.floor(rat.posY), (double) MathHelper.floor(rat.posZ)) + 4.0D;
            double d1 = (double) (rat.getMaximumHomeDistance() + (float) searchWidth);
            flag = d0 < d1 * d1;
        } else {
            flag = false;
        }

        boolean flag1 = false;
        float f = -99999.0F;
        int k1 = 0;
        int i = 0;
        int j = 0;

        for (int k = 0; k < 10; ++k) {
            int l = random.nextInt(2 * searchWidth + 1) - searchWidth;
            int i1 = random.nextInt(2 * searchHeight + 1) - searchHeight;
            int j1 = random.nextInt(2 * searchWidth + 1) - searchWidth;

            if (positionVector == null || (double) l * positionVector.x + (double) j1 * positionVector.z >= 0.0D) {
                if (rat.hasHome() && searchWidth > 1) {
                    BlockPos blockpos = rat.getHomePosition();

                    if (rat.posX > (double) blockpos.getX()) {
                        l -= random.nextInt(searchWidth / 2);
                    } else {
                        l += random.nextInt(searchWidth / 2);
                    }

                    if (rat.posZ > (double) blockpos.getZ()) {
                        j1 -= random.nextInt(searchWidth / 2);
                    } else {
                        j1 += random.nextInt(searchWidth / 2);
                    }
                }

                BlockPos blockpos1 = new BlockPos((double) l + rat.posX, (double) i1 + rat.posY, (double) j1 + rat.posZ);

                if ((!flag || rat.isWithinHomeDistanceFromPosition(blockpos1)) && rat.world.getBlockState(blockpos1).getBlock() == RatsBlockRegistry.RAT_CAGE) {
                    blockpos1 = findLowestRatCage(blockpos1, rat);
                    float f1 = rat.getBlockPathWeight(blockpos1);

                    if (f1 > f) {
                        f = f1;
                        k1 = l;
                        i = i1;
                        j = j1;
                        flag1 = true;
                    }
                }
            }
        }

        if (flag1) {
            return new Vec3d((double) k1 + rat.posX, (double) i + rat.posY, (double) j + rat.posZ);
        } else {
            return null;
        }
    }

    @Nullable
    public static Vec3d generateRandomTubePos(EntityRat rat, int searchWidth, int searchHeight, @Nullable Vec3d positionVector, boolean water) {
        PathNavigate pathnavigate = rat.getNavigator();
        Random random = rat.getRNG();
        boolean flag;

        if (rat.hasHome()) {
            double d0 = rat.getHomePosition().distanceSq((double) MathHelper.floor(rat.posX), (double) MathHelper.floor(rat.posY), (double) MathHelper.floor(rat.posZ)) + 4.0D;
            double d1 = (double) (rat.getMaximumHomeDistance() + (float) searchWidth);
            flag = d0 < d1 * d1;
        } else {
            flag = false;
        }

        boolean flag1 = false;
        float f = -99999.0F;
        int k1 = 0;
        int i = 0;
        int j = 0;

        for (int k = 0; k < 10; ++k) {
            int l = random.nextInt(2 * searchWidth + 1) - searchWidth;
            int i1 = random.nextInt(2 * searchHeight + 1) - searchHeight;
            int j1 = random.nextInt(2 * searchWidth + 1) - searchWidth;

            if (positionVector == null || (double) l * positionVector.x + (double) j1 * positionVector.z >= 0.0D) {
                if (rat.hasHome() && searchWidth > 1) {
                    BlockPos blockpos = rat.getHomePosition();

                    if (rat.posX > (double) blockpos.getX()) {
                        l -= random.nextInt(searchWidth / 2);
                    } else {
                        l += random.nextInt(searchWidth / 2);
                    }

                    if (rat.posZ > (double) blockpos.getZ()) {
                        j1 -= random.nextInt(searchWidth / 2);
                    } else {
                        j1 += random.nextInt(searchWidth / 2);
                    }
                }

                BlockPos blockpos1 = new BlockPos((double) l + rat.posX, (double) i1 + rat.posY, (double) j1 + rat.posZ);

                if ((!flag || rat.isWithinHomeDistanceFromPosition(blockpos1)) && rat.world.getBlockState(blockpos1).getBlock() instanceof BlockRatTube) {
                    blockpos1 = findLowestRatCage(blockpos1, rat);
                    float f1 = rat.getBlockPathWeight(blockpos1);

                    if (f1 > f) {
                        f = f1;
                        k1 = l;
                        i = i1;
                        j = j1;
                        flag1 = true;
                    }
                }
            }
        }

        if (flag1) {
            return new Vec3d((double) k1 + rat.posX, (double) i + rat.posY, (double) j + rat.posZ);
        } else {
            return null;
        }
    }

    public static BlockPos findLowestRatCage(BlockPos pos, EntityCreature rat) {
        if (rat.world.getBlockState(pos.down()).getBlock() != RatsBlockRegistry.RAT_CAGE && !(rat.world.getBlockState(pos.down()).getBlock() instanceof BlockRatTube)) {
            return pos;
        } else {
            BlockPos blockpos;
            for (blockpos = pos.down(); blockpos.getY() > 0 && rat.world.getBlockState(blockpos).getBlock() != RatsBlockRegistry.RAT_CAGE && !(rat.world.getBlockState(blockpos).getBlock() instanceof BlockRatTube); blockpos = blockpos.down()) {
            }
            return blockpos;
        }
    }

    public static BlockPos findLowestWater(BlockPos pos, EntityCreature rat) {
        if (rat.world.getBlockState(pos).getMaterial() == Material.WATER) {
            return pos;
        } else {
            BlockPos blockpos;
            for (blockpos = pos.down(); blockpos.getY() > 0 && rat.world.getBlockState(blockpos).getMaterial() != Material.WATER; blockpos = blockpos.down()) {
            }
            return blockpos;
        }
    }


    public static boolean canRatBreakBlock(World world, BlockPos pos, EntityRat rat) {
        IBlockState blockState = world.getBlockState(pos);
        float hardness = blockState.getBlockHardness(world, pos);
        return hardness != -1.0F && hardness <= RatsMod.CONFIG_OPTIONS.ratStrengthThreshold
                && blockState.getBlock().canEntityDestroy(blockState, world, pos, rat) && net.minecraft.entity.boss.EntityWither.canDestroyBlock(blockState.getBlock());
    }

    public static boolean isRatTube(IBlockAccess world, BlockPos offset) {
        return world.getTileEntity(offset) instanceof TileEntityRatTube;
    }

    public static boolean isConnectedToRatTube(IBlockAccess world, BlockPos pos) {
        if (world.isAirBlock(pos)) {
            for (EnumFacing facing : EnumFacing.values()) {
                if (isRatTube(world, pos.offset(facing))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isOpenRatTube(IBlockAccess world, EntityRat rat, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockRatTube) {
            for (int i = 0; i < EnumFacing.values().length; i++) {
                PropertyBool bool = BlockRatTube.ALL_OPEN_PROPS[i];
                if (state.getValue(bool) && rat != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isLinkedToTube(IBlockAccess world, BlockPos offset) {
        return isRatTube(world, offset) || isConnectedToRatTube(world, offset);
    }

    public static BlockPos offsetTubeEntrance(IBlockAccess worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos);
        if (state.getBlock() instanceof BlockRatTube) {
            for (int i = 0; i < EnumFacing.values().length; i++) {
                PropertyBool bool = BlockRatTube.ALL_OPEN_PROPS[i];
                if (state.getValue(bool)) {
                    return pos.offset(EnumFacing.values()[i]);
                }
            }
        }
        return pos;
    }

    public static Vec3d generateRandomWaterPos(EntityCreature p_191379_0_, int p_191379_1_, int p_191379_2_, @Nullable Vec3d p_191379_3_, boolean p_191379_4_) {
        PathNavigate pathnavigate = p_191379_0_.getNavigator();
        Random random = p_191379_0_.getRNG();
        boolean flag;

        if (p_191379_0_.hasHome()) {
            double d0 = p_191379_0_.getHomePosition().distanceSq((double) MathHelper.floor(p_191379_0_.posX), (double) MathHelper.floor(p_191379_0_.posY), (double) MathHelper.floor(p_191379_0_.posZ)) + 4.0D;
            double d1 = (double) (p_191379_0_.getMaximumHomeDistance() + (float) p_191379_1_);
            flag = d0 < d1 * d1;
        } else {
            flag = false;
        }

        boolean flag1 = false;
        float f = -99999.0F;
        int k1 = 0;
        int i = 0;
        int j = 0;

        for (int k = 0; k < 10; ++k) {
            int l = random.nextInt(2 * p_191379_1_ + 1) - p_191379_1_;
            int i1 = random.nextInt(2 * p_191379_2_ + 1) - p_191379_2_;
            int j1 = random.nextInt(2 * p_191379_1_ + 1) - p_191379_1_;

            if (p_191379_3_ == null || (double) l * p_191379_3_.x + (double) j1 * p_191379_3_.z >= 0.0D) {
                if (p_191379_0_.hasHome() && p_191379_1_ > 1) {
                    BlockPos blockpos = p_191379_0_.getHomePosition();

                    if (p_191379_0_.posX > (double) blockpos.getX()) {
                        l -= random.nextInt(p_191379_1_ / 2);
                    } else {
                        l += random.nextInt(p_191379_1_ / 2);
                    }

                    if (p_191379_0_.posZ > (double) blockpos.getZ()) {
                        j1 -= random.nextInt(p_191379_1_ / 2);
                    } else {
                        j1 += random.nextInt(p_191379_1_ / 2);
                    }
                }

                BlockPos blockpos1 = new BlockPos((double) l + p_191379_0_.posX, (double) i1 + p_191379_0_.posY, (double) j1 + p_191379_0_.posZ);

                if ((!flag || p_191379_0_.isWithinHomeDistanceFromPosition(blockpos1)) && pathnavigate.canEntityStandOnPos(blockpos1)) {
                    if (!p_191379_4_) {
                        blockpos1 = moveAboveSolid(blockpos1, p_191379_0_);

                        if (isWaterDestination(blockpos1, p_191379_0_)) {
                            continue;
                        }
                    }

                    float f1 = p_191379_0_.getBlockPathWeight(blockpos1);

                    if (f1 > f) {
                        f = f1;
                        k1 = l;
                        i = i1;
                        j = j1;
                        flag1 = true;
                    }
                }
            }
        }

        if (flag1) {
            return new Vec3d((double) k1 + p_191379_0_.posX, (double) i + p_191379_0_.posY, (double) j + p_191379_0_.posZ);
        } else {
            return null;
        }
    }

    private static BlockPos moveAboveSolid(BlockPos p_191378_0_, EntityCreature p_191378_1_) {
        if (!p_191378_1_.world.getBlockState(p_191378_0_).getMaterial().isSolid()) {
            return p_191378_0_;
        } else {
            BlockPos blockpos;

            for (blockpos = p_191378_0_.up(); blockpos.getY() < p_191378_1_.world.getHeight() && p_191378_1_.world.getBlockState(blockpos).getMaterial().isSolid(); blockpos = blockpos.up()) {
                ;
            }

            return blockpos;
        }
    }

    private static boolean isWaterDestination(BlockPos p_191380_0_, EntityCreature p_191380_1_) {
        return p_191380_1_.world.getBlockState(p_191380_0_).getMaterial() == Material.WATER;
    }

    public static class TubeSorter implements Comparator<EnumFacing> {
        private final EntityRat theEntity;

        public TubeSorter(EntityRat theEntityIn) {
            this.theEntity = theEntityIn;
        }

        public int compare(EnumFacing p_compare_1_, EnumFacing p_compare_2_) {
            BlockPos pos1 = new BlockPos(theEntity).offset(p_compare_1_);
            BlockPos pos2 = new BlockPos(theEntity).offset(p_compare_2_);
            double d0 = this.theEntity.tubeTarget.getDistance(pos1.getX(), pos1.getY(), pos1.getZ());
            double d1 = this.theEntity.tubeTarget.getDistance(pos2.getX(), pos2.getY(), pos2.getZ());
            return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
        }
    }
}
