package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.server.blocks.BlockRatTube;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatTube;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class RatUtils {

    public static final Predicate<Entity> UNTAMED_RAT_SELECTOR = new Predicate<Entity>() {
        public boolean apply(@Nullable Entity p_apply_1_) {
            return p_apply_1_ instanceof EntityRat && !((EntityRat) p_apply_1_).isTamed();
        }
    };
    public static final ResourceLocation PIRAT_ONLY_BLOCKS = new ResourceLocation("rats", "pirat_blocks");

    public static boolean isMilk(ItemStack stack) {
        if (stack.getItem() == Items.MILK_BUCKET) {
            return true;
        }
        LazyOptional<FluidStack> fluidStack = FluidUtil.getFluidContained(stack);
        return fluidStack.orElse(null) != null && fluidStack.orElse(null).getAmount() >= 1000 && (fluidStack.orElse(null).getFluid().getRegistryName().getPath().contains("milk") || fluidStack.orElse(null).getFluid().getRegistryName().getPath().contains("Milk"));
    }

    public static boolean isRatFood(ItemStack stack) {
        return (stack.getItem().isFood() || isSeeds(stack) || stack.getItem() == Items.WHEAT) && stack.getItem() != RatsItemRegistry.RAW_RAT && stack.getItem() != RatsItemRegistry.COOKED_RAT;
    }

    public static boolean shouldRaidItem(ItemStack stack) {
        return isRatFood(stack) && stack.getItem() != RatsItemRegistry.CONTAMINATED_FOOD;
    }

    public static boolean isSeeds(ItemStack stack) {
        Item item = stack.getItem();
        return false;
        /*
        TODO: Seeds
        if (item instanceof ItemSeeds && item != Items.NETHER_WART) {
            return true;
        }
        NonNullList<ItemStack> listAllseed = OreDictionary.getOres("listAllseed");
        NonNullList<ItemStack> listAllSeeds = OreDictionary.getOres("listAllSeeds");
        NonNullList<ItemStack> seed = OreDictionary.getOres("seed");
        NonNullList<ItemStack> seeds = OreDictionary.getOres("seeds");
        return listAllseed.contains(stack) || listAllSeeds.contains(stack) || seed.contains(stack) || seeds.contains(stack);
         */
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
            if (shouldRaidItem(stack) && rat.canRatPickupItem(stack)) {
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

    public static int getContaminatedSlot(EntityRat rat, IInventory inventory, Random random) {
        List<Integer> items = new ArrayList<Integer>();
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.isEmpty() || stack.getItem() == RatsItemRegistry.CONTAMINATED_FOOD) {
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
                    BlockState BlockState1 = world.getBlockState(blockpos$pooledmutableblockpos.setPos(l3, i4, j4));
                    if (BlockState1.getBlock() == RatsBlockRegistry.RAT_HOLE || BlockState1.getBlock() == RatsBlockRegistry.RAT_CAGE) {
                        blockpos$pooledmutableblockpos.close();
                        return true;
                    }
                }
            }
        }

        blockpos$pooledmutableblockpos.close();
        return false;
    }

    @Nullable
    public static RayTraceResult rayTraceBlocksIgnoreRatholes(World world, Vec3d start, Vec3d end, boolean stopOnLiquid, Entity entity) {
        //TODO: Redo ray trace code
        return world.rayTraceBlocks(new RayTraceContext(start, end, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, entity));
        //rayTraceBlocksIgnoreRatholes(world, start, end, stopOnLiquid, false, true);
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
                BlockState BlockState = world.getBlockState(blockpos);
                Block block = BlockState.getBlock();

               /* if ((!ignoreBlockWithoutBoundingBox || BlockState.getCollisionShape(world, blockpos) != VoxelShapes.empty()) && block.canCollideCheck(BlockState, stopOnLiquid) && block != RatsBlockRegistry.RAT_HOLE && block != RatsBlockRegistry.RAT_CAGE) {
                    RayTraceResult raytraceresult = null; //BlockState.collisionRayTrace(world, blockpos, vec31, vec32);

                    if (raytraceresult != null) {
                        return raytraceresult;
                    }
                }*/

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

                    Direction Direction;

                    if (d3 < d4 && d3 < d5) {
                        Direction = i > l ? net.minecraft.util.Direction.WEST : net.minecraft.util.Direction.EAST;
                        vec31 = new Vec3d(d0, vec31.y + d7 * d3, vec31.z + d8 * d3);
                    } else if (d4 < d5) {
                        Direction = j > i1 ? net.minecraft.util.Direction.DOWN : net.minecraft.util.Direction.UP;
                        vec31 = new Vec3d(vec31.x + d6 * d4, d1, vec31.z + d8 * d4);
                    } else {
                        Direction = k > j1 ? net.minecraft.util.Direction.NORTH : net.minecraft.util.Direction.SOUTH;
                        vec31 = new Vec3d(vec31.x + d6 * d5, vec31.y + d7 * d5, d2);
                    }

                    l = MathHelper.floor(vec31.x) - (Direction == net.minecraft.util.Direction.EAST ? 1 : 0);
                    i1 = MathHelper.floor(vec31.y) - (Direction == net.minecraft.util.Direction.UP ? 1 : 0);
                    j1 = MathHelper.floor(vec31.z) - (Direction == net.minecraft.util.Direction.SOUTH ? 1 : 0);
                    blockpos = new BlockPos(l, i1, j1);
                    BlockState BlockState1 = world.getBlockState(blockpos);
                    Block block1 = BlockState1.getBlock();

                   /* if ((!ignoreBlockWithoutBoundingBox || BlockState1.getMaterial() == Material.PORTAL || BlockState1.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB) && block1 != RatsBlockRegistry.RAT_HOLE && block1 != RatsBlockRegistry.RAT_CAGE) {
                        if (block1.canCollideCheck(BlockState1, stopOnLiquid)) {
                            RayTraceResult raytraceresult1 = BlockState1.collisionRayTrace(world, blockpos, vec31, vec32);

                            if (raytraceresult1 != null) {
                                return raytraceresult1;
                            }
                        } else {
                            raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, Direction, blockpos);
                        }
                    }*/
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
        return entity instanceof OcelotEntity || entity instanceof CatEntity || entity instanceof FoxEntity;
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
        return cheese.getItem() == RatsItemRegistry.CHEESE; //TODO: Cheese
        // || OreDictionary.getOres("foodCheese", false).contains(cheese);
    }

    @Nullable
    public static Vec3d findRandomCageOrTubeTarget(EntityRat rat, int xz, int y) {
        return generateRandomCageOrTubePos(rat, xz, y, null, false);
    }

    @Nullable
    public static Vec3d generateRandomCageOrTubePos(EntityRat rat, int searchWidth, int searchHeight, @Nullable Vec3d positionVector, boolean water) {
        PathNavigator pathnavigate = rat.getNavigator();
        Random random = rat.getRNG();
        boolean flag;

        if (rat.detachHome()) {
            double d0 = rat.getHomePosition().distanceSq((double) MathHelper.floor(rat.posX), (double) MathHelper.floor(rat.posY), (double) MathHelper.floor(rat.posZ), true) + 4.0D;
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
                if (rat.detachHome() && searchWidth > 1) {
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
        PathNavigator pathnavigate = rat.getNavigator();
        Random random = rat.getRNG();
        boolean flag;

        if (rat.detachHome()) {
            double d0 = rat.getHomePosition().distanceSq((double) MathHelper.floor(rat.posX), (double) MathHelper.floor(rat.posY), (double) MathHelper.floor(rat.posZ), true) + 4.0D;
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
                if (rat.detachHome() && searchWidth > 1) {
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
        PathNavigator pathnavigate = rat.getNavigator();
        Random random = rat.getRNG();
        boolean flag;

        if (rat.detachHome()) {
            double d0 = rat.getHomePosition().distanceSq((double) MathHelper.floor(rat.posX), (double) MathHelper.floor(rat.posY), (double) MathHelper.floor(rat.posZ), true) + 4.0D;
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
                if (rat.detachHome() && searchWidth > 1) {
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

    public static BlockPos findLowestRatCage(BlockPos pos, CreatureEntity rat) {
        if (rat.world.getBlockState(pos.down()).getBlock() != RatsBlockRegistry.RAT_CAGE && !(rat.world.getBlockState(pos.down()).getBlock() instanceof BlockRatTube)) {
            return pos;
        } else {
            BlockPos blockpos;
            for (blockpos = pos.down(); blockpos.getY() > 0 && rat.world.getBlockState(blockpos).getBlock() != RatsBlockRegistry.RAT_CAGE && !(rat.world.getBlockState(blockpos).getBlock() instanceof BlockRatTube); blockpos = blockpos.down()) {
            }
            return blockpos;
        }
    }

    public static BlockPos findLowestWater(BlockPos pos, CreatureEntity rat) {
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
        BlockState blockState = world.getBlockState(pos);
        float hardness = blockState.getBlockHardness(world, pos);
        return hardness != -1.0F && hardness <= RatConfig.ratStrengthThreshold
                && blockState.getBlock().canEntityDestroy(blockState, world, pos, rat) && WitherEntity.canDestroyBlock(blockState);
    }

    public static boolean isRatTube(IWorldReader world, BlockPos offset) {
        return world.getTileEntity(offset) instanceof TileEntityRatTube;
    }

    public static boolean isConnectedToRatTube(IWorldReader world, BlockPos pos) {
        if (world.isAirBlock(pos)) {
            for (Direction facing : Direction.values()) {
                if (isRatTube(world, pos.offset(facing))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isOpenRatTube(IBlockReader world, EntityRat rat, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockRatTube) {
            for (int i = 0; i < Direction.values().length; i++) {
                BooleanProperty bool = BlockRatTube.ALL_OPEN_PROPS[i];
                if (state.get(bool) && rat != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isLinkedToTube(IWorldReader world, BlockPos offset) {
        return isRatTube(world, offset) || isConnectedToRatTube(world, offset);
    }

    public static BlockPos offsetTubeEntrance(IWorldReader worldIn, BlockPos pos) {
        BlockState state = worldIn.getBlockState(pos);
        if (state.getBlock() instanceof BlockRatTube) {
            for (int i = 0; i < Direction.values().length; i++) {
                BooleanProperty bool = BlockRatTube.ALL_OPEN_PROPS[i];
                if (state.get(bool)) {
                    return pos.offset(Direction.values()[i]);
                }
            }
        }
        return pos;
    }

    public static Vec3d generateRandomWaterPos(CreatureEntity p_191379_0_, int p_191379_1_, int p_191379_2_, @Nullable Vec3d p_191379_3_, boolean p_191379_4_) {
        PathNavigator pathnavigate = p_191379_0_.getNavigator();
        Random random = p_191379_0_.getRNG();
        boolean flag;

        if (p_191379_0_.detachHome()) {
            double d0 = p_191379_0_.getHomePosition().distanceSq((double) MathHelper.floor(p_191379_0_.posX), (double) MathHelper.floor(p_191379_0_.posY), (double) MathHelper.floor(p_191379_0_.posZ), true) + 4.0D;
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
                if (p_191379_0_.detachHome() && p_191379_1_ > 1) {
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

    private static BlockPos moveAboveSolid(BlockPos p_191378_0_, CreatureEntity p_191378_1_) {
        if (!p_191378_1_.world.getBlockState(p_191378_0_).getMaterial().isSolid()) {
            return p_191378_0_;
        } else {
            BlockPos blockpos;

            for (blockpos = p_191378_0_.up(); blockpos.getY() < p_191378_1_.world.getHeight() && p_191378_1_.world.getBlockState(blockpos).getMaterial().isSolid(); blockpos = blockpos.up()) {
            }

            return blockpos;
        }
    }

    private static boolean isWaterDestination(BlockPos p_191380_0_, CreatureEntity p_191380_1_) {
        return p_191380_1_.world.getBlockState(p_191380_0_).getMaterial() == Material.WATER;
    }

    public static boolean canSpawnInDimension(IWorld world) {
        if (RatConfig.blacklistedRatDimensions.length > 0) {
            for (int i = 0; i < RatConfig.blacklistedRatDimensions.length; i++) {
                if (RatConfig.blacklistedRatDimensions[i] == world.getDimension().getType().getId()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isCow(Entity entity) {
        String s = entity.getType().getTranslationKey();
        if (s == null) {
            s = "generic";
        }
        return entity instanceof CowEntity || s.contains("cow");
    }

    public static class TubeSorter implements Comparator<Direction> {
        private final EntityRat theEntity;

        public TubeSorter(EntityRat theEntityIn) {
            this.theEntity = theEntityIn;
        }

        public int compare(Direction p_compare_1_, Direction p_compare_2_) {
            BlockPos pos1 = new BlockPos(theEntity).offset(p_compare_1_);
            BlockPos pos2 = new BlockPos(theEntity).offset(p_compare_2_);
            double d0 = this.theEntity.tubeTarget.distanceSq(pos1.getX(), pos1.getY(), pos1.getZ(), true);
            double d1 = this.theEntity.tubeTarget.distanceSq(pos2.getX(), pos2.getY(), pos2.getZ(), true);
            return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
        }
    }
}
