package com.github.alexthe666.rats.compat.jei;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CauldronRecipeCategory implements IRecipeCategory<CauldronInfoHolder> {
	public static final int WIDTH = 170;
	public static final int HEIGHT = 75;
	private final IDrawable background;
	private final IDrawable icon;
	private final IDrawable arrowIcon;

	public CauldronRecipeCategory(IGuiHelper helper) {
		ResourceLocation location = new ResourceLocation(RatsMod.MODID, "textures/gui/cauldron_jei.png");
		this.background = helper.createDrawable(location, 0, 0, WIDTH, HEIGHT);
		this.arrowIcon = helper.drawableBuilder(location, 170, 0, 24, 16)
				.buildAnimated(RatConfig.milkCauldronTime, IDrawableAnimated.StartDirection.LEFT, false);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RatsItemRegistry.CHEESE.get()));
	}

	@Override
	public RecipeType<CauldronInfoHolder> getRecipeType() {
		return RatsRecipeTypes.CAULDRON;
	}

	@Override
	public Component getTitle() {
		return Component.translatable("gui.rats.jei.cheesemaking");
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
	public void setRecipe(IRecipeLayoutBuilder builder, CauldronInfoHolder recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 35, 32).addIngredient(VanillaTypes.ITEM_STACK, new ItemStack(recipe.additionStack()));
		builder.addSlot(RecipeIngredientRole.CATALYST, 77, 32).addIngredient(VanillaTypes.ITEM_STACK, new ItemStack(recipe.cauldron()));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 128, 32).addIngredient(VanillaTypes.ITEM_STACK, new ItemStack(recipe.result()));
		builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addIngredient(VanillaTypes.ITEM_STACK, new ItemStack(recipe.cauldronContents()));
	}

	@Override
	public void draw(CauldronInfoHolder recipe, IRecipeSlotsView view, PoseStack stack, double mouseX, double mouseY) {
		if (recipe.additionStack().asItem() == Items.AIR) {
			this.arrowIcon.draw(stack, 95, 31);
			Component text = Component.literal(RatConfig.milkCauldronTime / 20 + "s");
			Minecraft.getInstance().font.draw(stack, text, 105 - (text.getString().length() * 2), 50, 0xFF808080);
		}
	}
}
