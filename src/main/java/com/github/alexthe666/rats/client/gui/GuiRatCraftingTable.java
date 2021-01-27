package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCraftingTable;
import com.github.alexthe666.rats.server.inventory.ContainerRatCraftingTable;
import com.github.alexthe666.rats.server.message.MessageIncreaseRatRecipe;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

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
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }


    protected void init() {
        super.init();
        this.buttons.clear();
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        this.addButton(new ChangeCommandButton(1, i + 43, j + 56, false, (p_214132_1_) -> {
            if(RatsMod.PROXY.getRefrencedTE() instanceof TileEntityRatCraftingTable) {
                TileEntityRatCraftingTable ratTable = (TileEntityRatCraftingTable) RatsMod.PROXY.getRefrencedTE();
                ratTable.decreaseRecipe();
                RatsMod.NETWORK_WRAPPER.sendToServer(new MessageIncreaseRatRecipe(((TileEntityRatCraftingTable) RatsMod.PROXY.getRefrencedTE()).getPos().toLong(), false));
            }
        }));
        this.addButton(new ChangeCommandButton(2, i + 198, j + 56, true, (p_214132_1_) -> {
            if(RatsMod.PROXY.getRefrencedTE() instanceof TileEntityRatCraftingTable) {
                TileEntityRatCraftingTable ratTable = (TileEntityRatCraftingTable) RatsMod.PROXY.getRefrencedTE();
                ratTable.increaseRecipe();
                RatsMod.NETWORK_WRAPPER.sendToServer(new MessageIncreaseRatRecipe(((TileEntityRatCraftingTable) RatsMod.PROXY.getRefrencedTE()).getPos().toLong(), true));
            }
        }));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack p_230450_1_, float p_230450_2_, int mouseX, int mouseY) {
        this.renderBackground(p_230450_1_);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(p_230450_1_, i, j, 0, 0, this.xSize, this.ySize);
        int l = container.getCookProgressScaled(64);
        this.blit(p_230450_1_, i + 54, j + 21, 0, 211, l, 16);
        if (RatsMod.PROXY.getRefrencedTE() instanceof TileEntityRatCraftingTable && ((TileEntityRatCraftingTable) RatsMod.PROXY.getRefrencedTE()).hasRat) {
            this.blit(p_230450_1_, i + 9, j, 176, 0, 21, 21);
        } else {
            this.blit(p_230450_1_, i + 8, j + 15, 198, 0, 21, 21);
        }
    }

    protected void func_230451_b_(MatrixStack stackIn, int mouseX, int mouseY) {
        FontRenderer font = this.font;
        String s = this.getTitle().getString();
        font.drawString(stackIn, s, this.xSize / 2 - font.getStringWidth(s) / 2, 5, 4210752);
        font.drawString(stackIn, this.playerInventory.getDisplayName().getString(), 8, this.ySize - 94 + 2, 4210752);
        font.drawString(stackIn, net.minecraft.client.resources.I18n.format("container.rat_crafting_table.required"), 8, this.ySize - 163 + 2, 4210752);
        font.drawString(stackIn, net.minecraft.client.resources.I18n.format("container.rat_crafting_table.input"), 8, this.ySize - 123 + 2, 4210752);
        int screenW = (this.width - 248) / 2;
        int screenH = (this.height - 166) / 2;
        List<ItemStack> drawnIngredients = new ArrayList<>();
        if (RatsMod.PROXY.getRefrencedTE() instanceof TileEntityRatCraftingTable) {
            IRecipe recipe = ((TileEntityRatCraftingTable) RatsMod.PROXY.getRefrencedTE()).getSelectedRecipe();
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
            TranslationTextComponent ratDesc = new TranslationTextComponent("container.rat_crafting_table.rat_desc");
            List<TranslationTextComponent> list = Arrays.asList(ratDesc);
            renderWrappedToolTip(stackIn, list, mouseX - screenW - 40, mouseY - screenH + 10, font);
        }
        if (mouseX > screenW + 69 && mouseX < screenW + 87 && mouseY > screenH - 7 && mouseY < screenH + 15 && tileFurnace.getStackInSlot(0).isEmpty()) {
            TranslationTextComponent ratDesc = new TranslationTextComponent("container.rat_crafting_table.input_desc");
            List<TranslationTextComponent> list = Arrays.asList(ratDesc);
            renderWrappedToolTip(stackIn, list, mouseX - screenW - 40, mouseY - screenH + 10, font);
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
        RenderSystem.translatef(0.0F, 0.0F, 32.0F);
        this.itemRenderer.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = this.font;
        this.itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRenderer.renderItemOverlays(font, stack, x, y);
        this.itemRenderer.zLevel = 0.0F;

    }

    public boolean shouldRenderButtons() {
        return RatsMod.PROXY.getRefrencedTE() instanceof TileEntityRatCraftingTable && ((TileEntityRatCraftingTable) RatsMod.PROXY.getRefrencedTE()).hasMultipleRecipes();
    }
}