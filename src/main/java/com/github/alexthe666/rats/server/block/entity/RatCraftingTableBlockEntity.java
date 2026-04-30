package com.github.alexthe666.rats.server.block.entity;

import com.github.alexthe666.rats.registry.RatsBlockEntityRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.inventory.RatCraftingTableMenu;
import com.github.alexthe666.rats.server.inventory.container.CraftingContainerWrapper;
import com.github.alexthe666.rats.server.inventory.container.TableItemHandlers;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import com.github.alexthe666.rats.server.misc.RatsLangConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Clearable;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;
import net.minecraft.core.HolderLookup;

// 1.21: dropped vanilla RecipeHolder *interface* (net.minecraft.world.inventory.RecipeHolder gone).
// Internally we now hold RecipeHolder<CraftingRecipe> records (item.crafting.RecipeHolder) so
// StackedContents.getBiggestCraftableStack and award-recipes calls keep working.
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "unchecked", "unused"})
public class RatCraftingTableBlockEntity extends BlockEntity implements MenuProvider, Clearable {

	private static final Component DEFAULT_NAME = Component.translatable(RatsLangConstants.RAT_CRAFTING_TABLE);
	private Component customName;
	public int prevCookTime;
	private boolean hasRat;
	public boolean hasValidRecipe;
	private int cookTime;
	protected final StackedContents itemHelper = new StackedContents();
	protected Optional<RecipeHolder<CraftingRecipe>> guideRecipe = Optional.empty();
	protected Optional<RecipeHolder<CraftingRecipe>> recipeUsed = Optional.empty();
	protected List<RecipeHolder<CraftingRecipe>> possibleRecipes = List.of();
	public int totalCookTime = 200;
	private int selectedRecipeIndex = 0;
	private final ContainerData dataAccess = new ContainerData() {
		public int get(int index) {
			return switch (index) {
				case 0 -> RatCraftingTableBlockEntity.this.cookTime;
				case 1 -> RatCraftingTableBlockEntity.this.totalCookTime;
				default -> 0;
			};
		}

		public void set(int index, int value) {
			switch (index) {
				case 0 -> RatCraftingTableBlockEntity.this.cookTime = value;
				case 1 -> RatCraftingTableBlockEntity.this.totalCookTime = value;
			}

		}

		public int getCount() {
			return 2;
		}
	};

	public final IItemHandlerModifiable bufferHandler = new TableItemHandlers.BufferHandler(this);
	public final IItemHandlerModifiable matrixHandler = new TableItemHandlers.MatrixHandler(this);
	public final IItemHandlerModifiable resultHandler = new TableItemHandlers.ResultHandler(this);

	protected final IItemHandlerModifiable combinedHandler =
			new CombinedInvWrapper(this.matrixHandler, this.bufferHandler);
	public final CraftingContainer matrixWrapper =
			new CraftingContainerWrapper(this.matrixHandler);

	public RatCraftingTableBlockEntity(BlockPos pos, BlockState state) {
		super(RatsBlockEntityRegistry.RAT_CRAFTING_TABLE.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, RatCraftingTableBlockEntity te) {
		te.hasRat = false;
		te.totalCookTime = 200;

		for (TamedRat rat : level.getEntitiesOfClass(TamedRat.class, new AABB(pos.getX(), (double) pos.getY() + 1, pos.getZ(), (double) pos.getX() + 1, (double) pos.getY() + 2, (double) pos.getZ() + 1))) {
			if (RatUpgradeUtils.hasUpgrade(rat, RatsItemRegistry.RAT_UPGRADE_CRAFTING.get())) {
				te.hasRat = true;
				if (RatUpgradeUtils.hasUpgrade(rat, RatsItemRegistry.RAT_UPGRADE_SPEED.get())) {
					te.totalCookTime = 100;
				}
			}
		}

		if (!level.isClientSide()) {
			te.prevCookTime = te.cookTime;

			if (te.getRecipeUsed() != null && te.hasRat && te.cookTime < te.totalCookTime) {
				te.cookTime++;
			} else {
				te.cookTime = Mth.clamp(te.cookTime - 2, 0, te.totalCookTime);
			}
			if (te.cookTime >= te.totalCookTime) {
				te.cookTime = 0;
				// 1.21: CraftingRecipe.assemble takes CraftingInput, not CraftingContainer.
				net.minecraft.world.item.crafting.CraftingInput craftingInput = makeCraftingInput(te.matrixWrapper);
				ItemStack addStack = te.recipeUsed.map(r -> r.value().assemble(craftingInput, level.registryAccess())).orElse(ItemStack.EMPTY);
				if (!addStack.isEmpty()) {
					IItemHandlerModifiable rh = te.resultHandler;
					rh.setStackInSlot(0, addStack.copyWithCount(addStack.getCount() + rh.getStackInSlot(0).getCount()));
					te.consumeIngredients(null);
				}
				te.updateRecipe();
			}
		}
	}

	public boolean hasRat() {
		return this.hasRat;
	}

	public int getCookTime() {
		return this.cookTime;
	}

	public void updateHelper() {
		((TableItemHandlers.BufferHandler) this.bufferHandler).fillStackedContents(this.itemHelper);
		this.checkIfRecipeIsValid(this.recipeUsed, this.itemHelper);
	}

	public void updateRecipe() {
		AtomicBoolean flag = new AtomicBoolean(true);
		if (this.getLevel() != null) {
			{
				// 1.21: RecipeManager.getRecipesFor takes (RecipeType, RecipeInput, Level) and returns List<RecipeHolder<T>>.
				net.minecraft.world.item.crafting.CraftingInput input = makeCraftingInput(this.matrixWrapper);
				this.possibleRecipes = this.getLevel().getRecipeManager()
						.getRecipesFor(net.minecraft.world.item.crafting.RecipeType.CRAFTING, input, this.getLevel());
				if (this.possibleRecipes.isEmpty()) {
					flag.set(false);
				} else {
					this.selectedRecipeIndex = Mth.clamp(this.selectedRecipeIndex, 0, this.possibleRecipes.size() - 1);
					this.guideRecipe = Optional.of(this.possibleRecipes.get(this.selectedRecipeIndex));
					if (!this.checkIfResultFits(this.getLevel(), this.guideRecipe)) {
						flag.set(false);
					}
					this.recipeUsed = Optional.of(this.possibleRecipes.get(this.selectedRecipeIndex))
							.filter(r -> this.setRecipeUsed(this.getLevel(), null, r));
				}
			}
			if (flag.get()) {
				this.checkIfRecipeIsValid(this.recipeUsed, this.itemHelper);
				if (!this.hasValidRecipe)
					this.setRecipeUsed((RecipeHolder<CraftingRecipe>) null);
			} else {
				this.guideRecipe = Optional.empty();
				this.setRecipeUsed((RecipeHolder<CraftingRecipe>) null);
			}
		}
	}

	private boolean checkIfResultFits(Level level, Optional<RecipeHolder<CraftingRecipe>> recipe) {
		if (recipe.isPresent()) {
			ItemStack checkStack = this.resultHandler.getStackInSlot(0);
			ItemStack resultStack = recipe.get().value().getResultItem(level.registryAccess());
			return (ItemStack.isSameItemSameComponents(checkStack, resultStack) && checkStack.getCount() + resultStack.getCount() <= checkStack.getMaxStackSize()) || checkStack.isEmpty();
		}
		return false;
	}

	private void checkIfRecipeIsValid(Optional<RecipeHolder<CraftingRecipe>> recipe, StackedContents helper) {
		this.hasValidRecipe = recipe.isPresent() && helper.getBiggestCraftableStack(recipe.get(), null) > 0;
	}

	// 1.21: CraftingRecipe.assemble/getRemainingItems/getMatching take CraftingInput, not the legacy
	// CraftingContainer. This adapter wraps our matrix container into a CraftingInput so all those
	// recipe APIs type-check.
	private static net.minecraft.world.item.crafting.CraftingInput makeCraftingInput(net.minecraft.world.inventory.CraftingContainer container) {
		java.util.List<ItemStack> items = new java.util.ArrayList<>(container.getContainerSize());
		for (int i = 0; i < container.getContainerSize(); i++) {
			items.add(container.getItem(i));
		}
		return net.minecraft.world.item.crafting.CraftingInput.of(container.getWidth(), container.getHeight(), items);
	}

	@Override
	public void clearContent() {
		IItemHandlerModifiable h = this.combinedHandler;
		for (int i = 0; i < h.getSlots(); i++) {
			h.setStackInSlot(i, ItemStack.EMPTY);
		}
		this.updateRecipe();
	}

	public void incrementSelectedRecipe(boolean negative) {
		if (negative) {
			this.selectedRecipeIndex--;
		} else {
			this.selectedRecipeIndex++;
		}
		this.updateRecipe();
	}

	public void setRecipeUsed(@Nullable RecipeHolder<CraftingRecipe> recipe) {
		this.recipeUsed = Optional.ofNullable(recipe);
	}

	public boolean setRecipeUsed(Level level, @Nullable ServerPlayer player, RecipeHolder<CraftingRecipe> recipe) {
		return !level.getGameRules().getBoolean(GameRules.RULE_LIMITED_CRAFTING) || recipe.value().isSpecial();
	}

	@Nullable
	public RecipeHolder<CraftingRecipe> getRecipeUsed() {
		return this.recipeUsed.orElse(null);
	}

	public Optional<CraftingRecipe> getGuideRecipe() {
		return this.guideRecipe.map(RecipeHolder::value);
	}

	public List<RecipeHolder<CraftingRecipe>> getPossibleRecipes() {
		return this.possibleRecipes;
	}

	@Override
	public void onLoad() {
		super.onLoad();
		this.updateRecipe();
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		((INBTSerializable<CompoundTag>) this.bufferHandler).deserializeNBT(registries, tag.getCompound("Buffer"));
		((INBTSerializable<CompoundTag>) this.matrixHandler).deserializeNBT(registries, tag.getCompound("Matrix"));
		((INBTSerializable<CompoundTag>) this.resultHandler).deserializeNBT(registries, tag.getCompound("Result"));
		if (tag.contains("CustomName", 8)) {
			this.customName = Component.Serializer.fromJson(tag.getString("CustomName"), registries);
		}
		this.cookTime = tag.getInt("CookTime");
		this.selectedRecipeIndex = tag.getInt("SelectedRecipe");
	}

	@Override
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.put("Buffer", ((INBTSerializable<CompoundTag>) this.bufferHandler).serializeNBT(registries));
		tag.put("Matrix", ((INBTSerializable<CompoundTag>) this.matrixHandler).serializeNBT(registries));
		tag.put("Result", ((INBTSerializable<CompoundTag>) this.resultHandler).serializeNBT(registries));
		if (this.hasCustomName()) {
			tag.putString("CustomName", Component.Serializer.toJson(this.customName, registries));
		}
		tag.putInt("CookTime", this.cookTime);
		tag.putInt("SelectedRecipe", this.selectedRecipeIndex);
	}

	@Nonnull
	@Override
	public Component getDisplayName() {
		return this.hasCustomName() ? this.customName : DEFAULT_NAME;
	}

	public boolean hasCustomName() {
		return this.customName != null;
	}

	public void setCustomName(@Nullable Component name) {
		this.customName = name;
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
		return new RatCraftingTableMenu(id, inventory, this, this.dataAccess);
	}

	public IItemHandlerModifiable itemHandler(@Nullable Direction side) {
		if (this.remove) return null;
		return side == Direction.DOWN ? this.resultHandler : this.bufferHandler;
	}

	public void consumeIngredients(@Nullable Player player) {
		// Skip the entire pass when the recipe is unusable. getRemainingItems can read the matrix
		// state and produce inconsistent container items if we run it after the matrix has already
		// been mutated by a prior tick.
		if (!this.hasValidRecipe) return;
		this.recipeUsed.ifPresent(holder -> {
			CraftingRecipe recipe = holder.value();
			NonNullList<ItemStack> remainingStacks = recipe.getRemainingItems(makeCraftingInput(this.matrixWrapper));

			if (this.hasValidRecipe) {
				IItemHandlerModifiable h = this.bufferHandler;
				recipe.getIngredients().forEach(i -> {
					for (int j = 0; j < h.getSlots(); j++) {
						if (i.test(h.getStackInSlot(j))) {
							h.extractItem(j, 1, false);
							break;
						}
					}
				});
			}

			// Handle container items
			IntStream.range(0, remainingStacks.size())
					.mapToObj(i -> {
						ItemStack stack = remainingStacks.get(i);
						return this.hasValidRecipe ? stack : this.matrixHandler.insertItem(i, stack, false);
					}) // Insert back the corresponding matrix slot if crafted from there
					.filter(stack -> !stack.isEmpty())
					.map(stack -> ItemHandlerHelper.insertItemStacked(this.bufferHandler, stack, false))
					.filter(stack -> !stack.isEmpty())
					.forEach(stack -> {
						if (player != null) {
							ItemHandlerHelper.giveItemToPlayer(player, stack);
						} else {
							this.outputStack(stack);
						}
					});
		});
	}

	private void outputStack(ItemStack stack) {
		ItemStack newStack = ItemHandlerHelper.insertItemStacked(this.bufferHandler, stack, false);

		if (!newStack.isEmpty() && this.getLevel() != null) {
			ItemEntity item = new ItemEntity(this.getLevel(), this.getBlockPos().getX() + 0.5F, this.getBlockPos().getY() + 1.0D, this.getBlockPos().getZ() + 0.5F, newStack);
			item.setDeltaMovement(Vec3.ZERO);
			item.setNoPickUpDelay();
			item.setExtendedLifetime();
			this.getLevel().addFreshEntity(item);
		}
	}
}
