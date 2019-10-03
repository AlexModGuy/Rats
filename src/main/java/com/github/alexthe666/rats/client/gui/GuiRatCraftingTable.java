package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCraftingTable;
import com.github.alexthe666.rats.server.inventory.ContainerRatCraftingTable;
import com.github.alexthe666.rats.server.message.MessageIncreaseRatRecipe;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiRatCraftingTable extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/gui/rat_crafting_table.png");
    private final InventoryPlayer playerInventory;
    private final IInventory tileFurnace;
    public ChangeCommandButton previousCommand;
    public ChangeCommandButton nextCommand;

    public GuiRatCraftingTable(IInventory furnaceInv, InventoryPlayer playerInv) {
        super(new ContainerRatCraftingTable(furnaceInv, playerInv.player));
        this.playerInventory = playerInv;
        this.tileFurnace = furnaceInv;
        this.ySize = 211;
    }

    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        this.buttonList.add(this.previousCommand = new ChangeCommandButton(1, i + 43, j + 56, false));
        this.buttonList.add(this.nextCommand = new ChangeCommandButton(2, i + 198, j + 56, true));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        TileEntityRatCraftingTable ratTable = (TileEntityRatCraftingTable) tileFurnace;
        if (button.id == 1) {
            ratTable.decreaseRecipe();
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageIncreaseRatRecipe(((TileEntityRatCraftingTable) tileFurnace).getPos().toLong(), false));
        }
        if (button.id == 2) {
            ratTable.increaseRecipe();
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageIncreaseRatRecipe(((TileEntityRatCraftingTable) tileFurnace).getPos().toLong(), true));
        }
    }


    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.tileFurnace.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 5, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 94 + 2, 4210752);
        this.fontRenderer.drawString(net.minecraft.client.resources.I18n.format("container.rat_crafting_table.required"), 8, this.ySize - 163 + 2, 4210752);
        this.fontRenderer.drawString(net.minecraft.client.resources.I18n.format("container.rat_crafting_table.input"), 8, this.ySize - 123 + 2, 4210752);
        int screenW = (this.width - 248) / 2;
        int screenH = (this.height - 166) / 2;
        List<ItemStack> drawnIngredients = new ArrayList<>();
        if (tileFurnace instanceof TileEntityRatCraftingTable) {
            IRecipe recipe = ((TileEntityRatCraftingTable) tileFurnace).getSelectedRecipe();
            int renderingIndex = 0;
            if (recipe != null) {
                for (int i = 0; i < recipe.getIngredients().size(); i++) {
                    Ingredient ingredient = recipe.getIngredients().get(i);
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
                                    if (doesArrayContainStack(recipe.getIngredients().get(j).getMatchingStacks(), drawn)) {
                                        count++;
                                    }
                                }
                                drawn.setCount(count);
                                drawnIngredients.add(drawn);
                                GlStateManager.enableLighting();
                                GlStateManager.enableDepth();
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
            net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(Arrays.asList(ratDesc), mouseX - screenW - 40, mouseY - screenH + 10, width, height, 120, fontRenderer);
        }
        if (mouseX > screenW + 69 && mouseX < screenW + 87 && mouseY > screenH - 7 && mouseY < screenH + 15 && tileFurnace.getStackInSlot(0).isEmpty()) {
            String ratDesc = I18n.format("container.rat_crafting_table.input_desc");
            net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(Arrays.asList(ratDesc), mouseX - screenW - 40, mouseY - screenH + 10, width, height, 120, fontRenderer);
        }
    }

    private boolean doesListContainStack(List<ItemStack> list, ItemStack stack) {
        for (ItemStack currentItem : list) {
            if (OreDictionary.itemMatches(stack, currentItem, false)) {
                return true;
            }
        }
        return false;
    }

    private boolean doesArrayContainStack(ItemStack[] list, ItemStack stack) {
        for (ItemStack currentItem : list) {
            if (OreDictionary.itemMatches(stack, currentItem, false)) {
                return true;
            }
        }
        return false;
    }

    private void drawRecipeItemStack(ItemStack stack, int x, int y) {
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = fontRenderer;
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y, null);

    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        int l = this.getCookProgressScaled(64);
        this.drawTexturedModalRect(i + 54, j + 21, 0, 211, l, 16);
        if (((TileEntityRatCraftingTable) tileFurnace).hasRat) {
            this.drawTexturedModalRect(i + 9, j, 176, 0, 21, 21);
        } else {
            this.drawTexturedModalRect(i + 8, j + 15, 198, 0, 21, 21);
        }
    }

    private int getCookProgressScaled(int pixels) {
        int i = this.tileFurnace.getField(0);
        int j = 200;
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    public boolean shouldRenderButtons() {
        return ((TileEntityRatCraftingTable) tileFurnace).hasMultipleRecipes();
    }
}