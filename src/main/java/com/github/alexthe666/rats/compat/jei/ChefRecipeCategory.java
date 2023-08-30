package com.github.alexthe666.rats.compat.jei;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.misc.RatsLangConstants;
import com.github.alexthe666.rats.server.recipes.ChefRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ChefRecipeCategory implements IRecipeCategory<ChefRecipe> {

	protected static final ResourceLocation TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/gui/jei/chef_rat_jei.png");
	private final IDrawable background;
	private final IDrawable icon;
	private final IDrawable arrow;

	public ChefRecipeCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 3, 4, 170, 79);
		this.icon = helper.createDrawableItemStack(new ItemStack(RatsItemRegistry.CONFIT_BYALDI.get()));
		this.arrow = helper.drawableBuilder(TEXTURE, 176, 0, 24, 16)
				.buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public RecipeType<ChefRecipe> getRecipeType() {
		return RatsRecipeTypes.CHEF;
	}

	@Override
	public Component getTitle() {
		return Component.translatable(RatsLangConstants.CHEF_JEI);
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public IDrawable getIcon() {
		return this.icon;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, ChefRecipe recipe, IFocusGroup ingredients) {
		builder.addSlot(RecipeIngredientRole.INPUT, 43, 49).addIngredients(recipe.getIngredients().get(0));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 114, 49).addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
	}

	@Override
	public void draw(ChefRecipe recipe, IRecipeSlotsView view, GuiGraphics graphics, double mouseX, double mouseY) {
		this.arrow.draw(graphics, 71, 49);
	}
}
