package com.github.alexthe666.rats.server.entity.tile;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.inventory.ContainerEmpty;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.google.common.base.Predicate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.*;

public class TileEntityRatCraftingTable extends TileEntity implements ITickable, ISidedInventory {

    private static final Predicate<ItemStack> EMPTY_ITEM_PREDICATE = new Predicate<ItemStack>() {
        public boolean apply(@Nullable ItemStack itemStack) {
            return itemStack != null && !itemStack.isEmpty();
        }
    };
    private static final int[] SLOTS_TOP = new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static final int[] SLOTS_BOTTOM = new int[]{1};
    private static List<IRecipe> EMPTY_LIST = new ArrayList<>();
    public int prevCookTime;
    public boolean hasRat;
    net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.Direction.UP);
    net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.Direction.DOWN);
    private NonNullList<ItemStack> inventory = NonNullList.withSize(11, ItemStack.EMPTY);
    private int cookTime;
    private int totalCookTime = 200;
    private String furnaceCustomName;
    private boolean canSwapRecipe;
    private List<IRecipe> currentApplicableRecipes = new ArrayList<>();
    private IRecipe selectedRecipe = null;
    private int selectedRecipeIndex = 0;

    private static List<IRecipe> findMatchingRecipesFor(ItemStack stack) {
        List<IRecipe> matchingRecipes = EMPTY_LIST;
        if (!stack.isEmpty()) {
            matchingRecipes = new ArrayList<>();
            List<IRecipe> allRecipes = new ArrayList<>(ForgeRegistries.RECIPES.getValues());
            for (IRecipe recipe : allRecipes) {
                if (recipe.canFit(3, 3) && recipe.getRecipeOutput().isItemEqual(stack)) {
                    matchingRecipes.add(recipe);
                }
            }
        }
        return matchingRecipes;
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
        InventoryCrafting inventoryCrafting = new InventoryCrafting(new ContainerEmpty(), 3, 3);
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
        for (int i = 0; i < removedItems.size(); i++) {
            inventoryCrafting.setInventorySlotContents(i, removedItems.get(i));
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
            Ingredient ingredient = recipe.getIngredients().get(i);
            ItemStack[] matches = ingredient.getMatchingStacks();
            int index = 0;
            if (matches.length > 0) {
                ItemStack counted = matches[index].copy();
                int count = 0;
                if (!doesListContainStack(countedIngredients, counted)) {
                    if (!counted.isEmpty() && counted.getItem() != Items.AIR) {
                        for (int j = 0; j < recipe.getIngredients().size(); j++) {
                            if (doesArrayContainStack(recipe.getIngredients().get(j).getMatchingStacks(), counted)) {
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
            if (OreDictionary.itemMatches(stack, currentItem, false)) {
                return true;
            }
        }
        return false;
    }

    private static boolean doesListContainStack(List<ItemStack> list, ItemStack stack) {
        for (ItemStack currentItem : list) {
            if (OreDictionary.itemMatches(stack, currentItem, false)) {
                return true;
            }
        }
        return false;
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
            this.currentApplicableRecipes.clear();
            this.currentApplicableRecipes = findMatchingRecipesFor(stack);
            this.selectedRecipe = null;
            this.selectedRecipeIndex = 0;
            this.markDirty();
        }
    }

    public int getCookTime(ItemStack stack) {
        return 200;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isUsableByPlayer(EntityPlayer player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

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
    public void update() {
        boolean flag = false;
        hasRat = false;
        this.prevCookTime = cookTime;
        for (EntityRat rat : world.getEntitiesWithinAABB(EntityRat.class, new AxisAlignedBB((double) pos.getX(), (double) pos.getY() + 1, (double) pos.getZ(), (double) pos.getX() + 1, (double) pos.getY() + 2, (double) pos.getZ() + 1))) {
            if (rat.isTamed() && rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CRAFTING)) {
                hasRat = true;
            }
        }
        if (!this.currentApplicableRecipes.isEmpty()) {
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

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerFurnace(playerInventory, this);
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

    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.inventory);
        this.cookTime = compound.getInteger("CookTime");
        this.totalCookTime = compound.getInteger("CookTimeTotal");

        if (compound.hasKey("CustomName", 8)) {
            this.furnaceCustomName = compound.getString("CustomName");
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("CookTime", (short) this.cookTime);
        compound.setInteger("CookTimeTotal", (short) this.totalCookTime);
        ItemStackHelper.saveAllItems(compound, this.inventory);

        if (this.hasCustomName()) {
            compound.setString("CustomName", this.furnaceCustomName);
        }
        return compound;
    }

    public IRecipe getSelectedRecipe() {
        return selectedRecipe;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.furnaceCustomName : "container.rat_crafting_table";
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(getName());
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    @javax.annotation.Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.Direction facing) {
        if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            if (facing == Direction.DOWN)
                return (T) handlerBottom;
            else
                return (T) handlerTop;
        return super.getCapability(capability, facing);
    }

    public boolean hasMultipleRecipes() {
        return currentApplicableRecipes.size() > 1;
    }
}
