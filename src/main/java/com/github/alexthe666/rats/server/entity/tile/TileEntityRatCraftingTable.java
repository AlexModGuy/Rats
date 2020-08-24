package com.github.alexthe666.rats.server.entity.tile;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.inventory.ContainerEmpty;
import com.github.alexthe666.rats.server.inventory.ContainerRatCraftingTable;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.message.MessageUpdateTileSlots;
import com.google.common.base.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.*;

public class TileEntityRatCraftingTable extends LockableTileEntity implements ITickableTileEntity, ISidedInventory {

    private static final Predicate<ItemStack> EMPTY_ITEM_PREDICATE = new Predicate<ItemStack>() {
        public boolean apply(@Nullable ItemStack itemStack) {
            return itemStack != null && !itemStack.isEmpty();
        }
    };
    private static final int[] SLOTS_TOP = new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static final int[] SLOTS_BOTTOM = new int[]{1};
    private static final IRecipeSerializer[] RECIPES_TO_SCAN = new IRecipeSerializer[]{IRecipeSerializer.CRAFTING_SHAPED, IRecipeSerializer.CRAFTING_SHAPED};
    private static List<IRecipe> EMPTY_LIST = new ArrayList<>();
    public int prevCookTime;
    public boolean hasRat;
    public int cookTime;
    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
            net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN);
    private NonNullList<ItemStack> inventory = NonNullList.withSize(11, ItemStack.EMPTY);
    private int totalCookTime = 200;
    public final IIntArray furnaceData = new IIntArray() {
        public int get(int index) {
            switch (index) {
                case 2:
                    return TileEntityRatCraftingTable.this.cookTime;
                case 3:
                    return TileEntityRatCraftingTable.this.totalCookTime;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch (index) {
                case 2:
                    TileEntityRatCraftingTable.this.cookTime = value;
                    break;
                case 3:
                    TileEntityRatCraftingTable.this.totalCookTime = value;
            }

        }

        public int size() {
            return 4;
        }
    };
    private String furnaceCustomName;
    private boolean canSwapRecipe;
    private List<IRecipe> currentApplicableRecipes = new ArrayList<>();
    private IRecipe selectedRecipe = null;
    private int selectedRecipeIndex = 0;
    private boolean forceUpdateRecipes = false;

    public TileEntityRatCraftingTable() {
        super(RatsTileEntityRegistry.RAT_CRAFTING_TABLE);
    }

    public static boolean hasIngredients(IRecipe recipe, NonNullList<ItemStack> stacks) {
        Map<Ingredient, Integer> ingredients = new HashMap<>();
        for (Map.Entry<Ingredient, Integer> ing : compressRecipe(recipe).entrySet()) {
            ingredients.put(ing.getKey(), ing.getValue());
        }
        Iterator<Ingredient> itr = ingredients.keySet().iterator();
        while (itr.hasNext()) {
            Ingredient ingredient = itr.next();
            ItemStack[] matches = ingredient.getMatchingStacks();
            int count = 0;
            int removedCount = 0;
            int maxCount = ingredients.get(ingredient);
            for (ItemStack stack : stacks) {
                if (doesArrayContainStack(matches, stack)) {
                    count += stack.getCount();
                }
                if (count >= maxCount) {
                    itr.remove();
                    break;
                }
            }
        }
        return ingredients.isEmpty();
    }

    public static NonNullList<ItemStack> consumeIngredients(IRecipe recipe, NonNullList<ItemStack> stacks, NonNullList<ItemStack> inv) {
        Map<Ingredient, Integer> ingredients = new HashMap<>();
        CraftingInventory inventoryCrafting = new CraftingInventory(new ContainerEmpty(103, null), 3, 3);
        for (Map.Entry<Ingredient, Integer> ing : compressRecipe(recipe).entrySet()) {
            ingredients.put(ing.getKey(), ing.getValue());
        }
        Iterator<Ingredient> itr = ingredients.keySet().iterator();
        NonNullList<ItemStack> removedItems = NonNullList.create();
        while (itr.hasNext()) {
            Ingredient ingredient = itr.next();
            ItemStack[] matches = ingredient.getMatchingStacks();
            int removedCount = 0;
            int maxCount = ingredients.get(ingredient);
            for (ItemStack stack : stacks) {
                if (doesArrayContainStack(matches, stack) && removedCount < maxCount) {
                    removedCount += Math.min(stack.getCount(), maxCount);
                    ItemStack copyStack = stack.copy();
                    copyStack.setCount(removedCount);
                    removedItems.add(copyStack);
                    stack.shrink(removedCount);
                }
                if (removedCount >= maxCount) {
                    itr.remove();
                    break;
                }
            }
        }
        for (int i = 0; i < stacks.size(); i++) {
            inv.set(i + 2, stacks.get(i));
        }
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            ItemStack[] matching = ((Ingredient)recipe.getIngredients().get(i)).getMatchingStacks();
            if(matching.length > 0){
                inventoryCrafting.setInventorySlotContents(i, matching[0]);
            }
        }
        NonNullList<ItemStack> remainWEmpties = recipe.getRemainingItems(inventoryCrafting);
        NonNullList<ItemStack> remain = NonNullList.create();
        for (ItemStack remaining : remainWEmpties) {
            if (!remaining.isEmpty()) {
                remain.add(remaining);
            }
        }
        return remain;
    }

    public static Map<Ingredient, Integer> compressRecipe(IRecipe recipe) {
        List<ItemStack> countedIngredients = new ArrayList<>();
        Map<Ingredient, Integer> ingredients = new HashMap<>();
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            Ingredient ingredient = (Ingredient) recipe.getIngredients().get(i);
            ItemStack[] matches = ingredient.getMatchingStacks();
            int index = 0;
            if (matches.length > 0) {
                ItemStack counted = matches[index].copy();
                int count = 0;
                if (!doesListContainStack(countedIngredients, counted)) {
                    if (!counted.isEmpty() && counted.getItem() != Items.AIR) {
                        for (int j = 0; j < recipe.getIngredients().size(); j++) {
                            if (doesArrayContainStack(((Ingredient) recipe.getIngredients().get(j)).getMatchingStacks(), counted)) {
                                count++;
                            }
                        }
                        counted.setCount(count);
                        ingredients.put(ingredient, count);
                        countedIngredients.add(counted);
                    }
                }
            }
        }
        return ingredients;
    }

    private static boolean doesArrayContainStack(ItemStack[] list, ItemStack stack) {
        for (ItemStack currentItem : list) {
            if (ItemStack.areItemsEqual(stack, currentItem)) {
                return true;
            }
        }
        return false;
    }

    private static boolean doesListContainStack(List<ItemStack> list, ItemStack stack) {
        for (ItemStack currentItem : list) {
            if (ItemStack.areItemsEqual(stack, currentItem)) {
                return true;
            }
        }
        return false;
    }

    private List<IRecipe> findMatchingRecipesFor(ItemStack stack) {
        List<IRecipe> matchingRecipes = EMPTY_LIST;
        if (!stack.isEmpty()) {
            matchingRecipes = new ArrayList<>();
            RecipeManager manager = this.world.getRecipeManager();
            for (IRecipe<?> irecipe : manager.getRecipes()) {
                if (irecipe.getType() == IRecipeType.CRAFTING && irecipe.canFit(3, 3) && irecipe.getRecipeOutput().isItemEqual(stack)) {
                    matchingRecipes.add(irecipe);
                }
            }
        }
        return matchingRecipes;
    }

    public int getSizeInventory() {
        return this.inventory.size();
    }

    public boolean isEmpty() {
        for (ItemStack itemstack : this.inventory) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public ItemStack getStackInSlot(int index) {
        return this.inventory.get(index);
    }

    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.inventory, index, count);
    }

    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.inventory, index);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return new int[]{1};
        } else {
            return SLOTS_TOP;
        }
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return true;
    }

    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.inventory.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.inventory.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag) {
            this.totalCookTime = 200;
            this.cookTime = 0;
            this.forceUpdateRecipes = true;
            this.selectedRecipe = null;
            this.selectedRecipeIndex = 0;
            this.markDirty();
        }
        if (!world.isRemote) {
            RatsMod.sendMSGToAll(new MessageUpdateTileSlots(this.getPos().toLong(), this.getUpdateTag()));
        }
    }

    public int getCookTime(ItemStack stack) {
        return 200;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isUsableByPlayer(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void openInventory(PlayerEntity player) {

    }

    @Override
    public void closeInventory(PlayerEntity player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index != 1;
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

    @Override
    public void tick() {
        boolean flag = false;
        hasRat = false;
        this.prevCookTime = cookTime;
        if (forceUpdateRecipes) {
            this.currentApplicableRecipes.clear();
            this.currentApplicableRecipes = findMatchingRecipesFor(this.inventory.get(0));
            forceUpdateRecipes = false;
        }
        for (EntityRat rat : world.getEntitiesWithinAABB(EntityRat.class, new AxisAlignedBB(pos.getX(), (double) pos.getY() + 1, pos.getZ(), (double) pos.getX() + 1, (double) pos.getY() + 2, (double) pos.getZ() + 1))) {
            if (rat.isTamed() && rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CRAFTING)) {
                hasRat = true;
            }
        }
        if (!this.currentApplicableRecipes.isEmpty()) {
            selectedRecipeIndex = MathHelper.clamp(selectedRecipeIndex, 0, currentApplicableRecipes.size() - 1);
            if (this.currentApplicableRecipes.size() <= 1) {
                selectedRecipe = currentApplicableRecipes.get(0);
            } else {
                selectedRecipe = this.currentApplicableRecipes.get(selectedRecipeIndex);
            }
        } else {
            this.currentApplicableRecipes = findMatchingRecipesFor(getStackInSlot(0));
        }
        if (selectedRecipe != null && (this.getStackInSlot(1).isEmpty() || this.getStackInSlot(1).isItemEqual(selectedRecipe.getRecipeOutput()) && this.getStackInSlot(1).getCount() + selectedRecipe.getRecipeOutput().getCount() <= selectedRecipe.getRecipeOutput().getMaxStackSize() && this.getStackInSlot(1).isStackable())) {
            NonNullList<ItemStack> stacks = NonNullList.create();
            for (int i = 2; i < 11; i++) {
                stacks.add(inventory.get(i));
            }
            if (hasIngredients(selectedRecipe, stacks) && hasRat) {
                cookTime++;
                flag = true;
                if (cookTime >= 200) {
                    cookTime = 0;
                    if (this.getStackInSlot(1).isItemEqual(selectedRecipe.getRecipeOutput()) && this.getStackInSlot(1).getCount() < 64) {
                        this.getStackInSlot(1).grow(selectedRecipe.getRecipeOutput().getCount());
                    } else if (this.getStackInSlot(1).isEmpty()) {
                        this.setInventorySlotContents(1, selectedRecipe.getRecipeOutput().copy());
                    }
                    NonNullList<ItemStack> remainingItems = consumeIngredients(selectedRecipe, stacks, inventory);
                    for (ItemStack stack : remainingItems) {
                        boolean depositied = false;
                        for (int i = 2; i < 11; i++) {
                            if (stack.isStackable() && inventory.get(i).isItemEqual(stack) && inventory.get(i).getCount() + stack.getCount() <= inventory.get(i).getMaxStackSize() && !depositied) {
                                int remainingSize = inventory.get(i).getMaxStackSize() - inventory.get(i).getCount();
                                int addToSize = Math.min(remainingSize, stack.getCount());
                                stack.shrink(addToSize);
                                inventory.get(i).grow(addToSize);
                                if (stack.getCount() <= 0) {
                                    depositied = true;
                                    break;
                                }
                            }
                            if (inventory.get(i).isEmpty() && !depositied) {
                                depositied = true;
                                this.setInventorySlotContents(i, stack);
                                break;
                            }
                        }
                        if (!depositied && !world.isRemote) {
                            InventoryHelper.spawnItemStack(world, this.getPos().getX() + 0.5, this.getPos().getY() + 1.5, this.getPos().getZ() + 0.5, stack);
                        }
                    }
                }
            }
        }
        if (!flag && cookTime > 0) {
            cookTime = 0;
        }
    }

    public void increaseRecipe() {
        if (!this.currentApplicableRecipes.isEmpty() && this.currentApplicableRecipes.size() > 1) {
            selectedRecipeIndex++;
            if (selectedRecipeIndex > this.currentApplicableRecipes.size() - 1) {
                selectedRecipeIndex = 0;
            }
            selectedRecipe = this.currentApplicableRecipes.get(selectedRecipeIndex);
        }
    }

    public void decreaseRecipe() {
        if (!this.currentApplicableRecipes.isEmpty() && this.currentApplicableRecipes.size() > 1) {
            selectedRecipeIndex--;
            if (selectedRecipeIndex < 0) {
                selectedRecipeIndex = this.currentApplicableRecipes.size() - 1;
            }
            selectedRecipe = this.currentApplicableRecipes.get(selectedRecipeIndex);
        }
    }

    public String getGuiID() {
        return "rats:rat_crafting_table";
    }

    public int getFieldCount() {
        return 1;
    }

    public int getField(int id) {
        return this.cookTime;
    }

    public void setField(int id, int value) {
        this.cookTime = value;
    }

    public void func_230337_a_(BlockState state, CompoundNBT compound) {
        super.func_230337_a_(state, compound);
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.inventory);
        this.cookTime = compound.getInt("CookTime");
        this.totalCookTime = compound.getInt("CookTimeTotal");
        if (!compound.getString("CustomName").isEmpty()) {
            this.furnaceCustomName = compound.getString("CustomName");
        }
        this.forceUpdateRecipes = true;
        this.selectedRecipe = null;
        this.selectedRecipeIndex = compound.getInt("SelectedRecipeIndex");
    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("CookTime", (short) this.cookTime);
        compound.putInt("CookTimeTotal", (short) this.totalCookTime);
        ItemStackHelper.saveAllItems(compound, this.inventory);

        if (this.hasCustomName()) {
            compound.putString("CustomName", this.furnaceCustomName);
        }
        compound.putInt("SelectedRecipeIndex", this.selectedRecipeIndex);
        return compound;
    }

    public IRecipe getSelectedRecipe() {
        return selectedRecipe;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.rat_crafting_table");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (!this.removed && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == Direction.DOWN)
                return handlers[1].cast();
            else
                return handlers[0].cast();
        }
        return super.getCapability(capability, facing);
    }

    public boolean hasMultipleRecipes() {
        return currentApplicableRecipes.size() > 1;
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
        return new ContainerRatCraftingTable(id, this, playerInventory, furnaceData);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        func_230337_a_(this.getBlockState(), packet.getNbtCompound());
    }

    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }
}
