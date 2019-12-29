package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCraftingTable;
import com.github.alexthe666.rats.server.inventory.ContainerRatCraftingTable;
import com.github.alexthe666.rats.server.message.MessageIncreaseRatRecipe;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiRatCraftingTable extends ContainerScreen<ContainerRatCraftingTable> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/gui/rat_crafting_table.png");
    private final PlayerInventory playerInventory;
    private final IInventory tileFurnace;

    public GuiRatCraftingTable(ContainerRatCraftingTable container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.playerInventory = inv;
        this.tileFurnace = container.tileRatCraftingTable;
        this.ySize = 211;
    }

    public void init() {
        super.init();
        this.buttons.clear();
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        this.addButton(new ChangeCommandButton(1, i + 43, j + 56, false, (p_214132_1_) -> {
            TileEntityRatCraftingTable ratTable = (TileEntityRatCraftingTable) tileFurnace;
            ratTable.decreaseRecipe();
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageIncreaseRatRecipe(((TileEntityRatCraftingTable) tileFurnace).getPos().toLong(), false));
        }));
        this.addButton(new ChangeCommandButton(2, i + 198, j + 56, true, (p_214132_1_) -> {
            TileEntityRatCraftingTable ratTable = (TileEntityRatCraftingTable) tileFurnace;
            ratTable.increaseRecipe();
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageIncreaseRatRecipe(((TileEntityRatCraftingTable) tileFurnace).getPos().toLong(), true));
        }));
    }

    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.getTitle().getFormattedText();
        this.font.drawString(s, this.xSize / 2 - this.font.getStringWidth(s) / 2, 5, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8, this.ySize - 94 + 2, 4210752);
        this.font.drawString(net.minecraft.client.resources.I18n.format("container.rat_crafting_table.required"), 8, this.ySize - 163 + 2, 4210752);
        this.font.drawString(net.minecraft.client.resources.I18n.format("container.rat_crafting_table.input"), 8, this.ySize - 123 + 2, 4210752);
        int screenW = (this.width - 248) / 2;
        int screenH = (this.height - 166) / 2;
        List<ItemStack> drawnIngredients = new ArrayList<>();
        if (tileFurnace instanceof TileEntityRatCraftingTable) {
            IRecipe recipe = ((TileEntityRatCraftingTable) tileFurnace).getSelectedRecipe();
            int renderingIndex = 0;
            if (recipe != null) {
                for (int i = 0; i < recipe.getIngredients().size(); i++) {
                    Ingredient ingredient = (Ingredient) recipe.getIngredients().get(i);
                    ItemStack[] matches = ingredient.getMatchingStacks();
                    int index = 0;
                    if (matches.length > 1) {
                        index = playerInventory.player.ticksExisted / 20 % matches.length;
                    }
                    if (matches.length > 0) {
                        ItemStack drawn = matches[index].copy();
                        int count = 0;
                        if (!doesListContainStack(drawnIngredients, drawn)) {
                            if (!drawn.isEmpty() && drawn.getItem() != Items.AIR) {
                                for (int j = 0; j < recipe.getIngredients().size(); j++) {
                                    if (doesArrayContainStack(((Ingredient)recipe.getIngredients().get(j)).getMatchingStacks(), drawn)) {
                                        count++;
                                    }
                                }
                                drawn.setCount(count);
                                drawnIngredients.add(drawn);
                                GlStateManager.enableLighting();
                                GlStateManager.enableDepthTest();
                                RenderHelper.enableGUIStandardItemLighting();
                                GlStateManager.enableRescaleNormal();
                                this.drawRecipeItemStack(drawn, 8 + renderingIndex * 18, 60);
                                renderingIndex++;
                            }
                        }
                    }
                }
            }
        }
        if (mouseX > screenW + 32 && mouseX < screenW + 70 && mouseY > screenH - 15 && mouseY < screenH + 24) {
            String ratDesc = I18n.format("container.rat_crafting_table.rat_desc");
            net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(Arrays.asList(ratDesc), mouseX - screenW - 40, mouseY - screenH + 10, width, height, 120, font);
        }
        if (mouseX > screenW + 69 && mouseX < screenW + 87 && mouseY > screenH - 7 && mouseY < screenH + 15 && tileFurnace.getStackInSlot(0).isEmpty()) {
            String ratDesc = I18n.format("container.rat_crafting_table.input_desc");
            net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(Arrays.asList(ratDesc), mouseX - screenW - 40, mouseY - screenH + 10, width, height, 120, font);
        }
    }

    private boolean doesListContainStack(List<ItemStack> list, ItemStack stack) {
        for (ItemStack currentItem : list) {
            if (ItemStack.areItemsEqual(stack, currentItem)) {
                return true;
            }
        }
        return false;
    }

    private boolean doesArrayContainStack(ItemStack[] list, ItemStack stack) {
        for (ItemStack currentItem : list) {
            if (ItemStack.areItemsEqual(stack, currentItem)) {
                return true;
            }
        }
        return false;
    }

    private void drawRecipeItemStack(ItemStack stack, int x, int y) {
        //OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        if (font == null) font = font;
        this.itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRenderer.renderItemOverlayIntoGUI(font, stack, x, y, null);

    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);
        int l = container.getCookProgressScaled(64);
        this.blit(i + 54, j + 21, 0, 211, l, 16);
        if (tileFurnace instanceof TileEntityRatCraftingTable && ((TileEntityRatCraftingTable) tileFurnace).hasRat) {
            this.blit(i + 9, j, 176, 0, 21, 21);
        } else {
            this.blit(i + 8, j + 15, 198, 0, 21, 21);
        }
    }


    public boolean shouldRenderButtons() {
        return tileFurnace instanceof TileEntityRatCraftingTable && ((TileEntityRatCraftingTable) tileFurnace).hasMultipleRecipes();
    }
}