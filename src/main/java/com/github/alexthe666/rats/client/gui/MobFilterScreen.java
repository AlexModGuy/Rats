package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.util.EntityRenderingUtil;
import com.github.alexthe666.rats.server.items.upgrades.MobFilterUpgradeItem;
import com.github.alexthe666.rats.server.message.RatsNetworkHandler;
import com.github.alexthe666.rats.server.message.UpdateMobFilterPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class MobFilterScreen extends Screen {

	private static final ResourceLocation TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/gui/container/mob_filter.png");
	private static final ResourceLocation TEXTURE_BACKDROP = new ResourceLocation(RatsMod.MODID, "textures/gui/container/mob_filter_backdrop.png");

	private static final int SCROLL_X_START = 218;
	private static final int SCROLL_Y_START = 21;
	private static final int MAX_MOB_BUTTONS = 5;

	//position helpers
	private final int imageWidth = 240;
	private final int imageHeight = 124;
	protected int leftPos;
	protected int topPos;

	//stuff from the itemstack
	private final ItemStack stack;
	private final boolean isWhitelist;
	private final List<String> selectedMobs;

	//screen fields
	@Nullable
	private ResourceLocation hoveredEntityName;
	private int startIndex;
	private final List<Pair<String, Component>> allMobs;
	private final List<Pair<String, Component>> filteredMobs = new ArrayList<>();
	private CheckBox whitelistSelected;
	private CheckBox selectedMobsShown;
	private EditBox searchBar;
	private float scrollOffs;
	private boolean scrolling;
	private final Set<TagKey<EntityType<?>>> visibleTags = new HashSet<>();

	public MobFilterScreen(ItemStack stack) {
		super(Component.translatable("gui.rats.mob_filter_screen"));
		this.allMobs = RatsMod.MOB_CACHE;
		this.stack = stack;
		this.isWhitelist = MobFilterUpgradeItem.isWhitelist(stack);
		this.selectedMobs = MobFilterUpgradeItem.getSelectedMobs(stack);
	}

	@Override
	protected void init() {
		this.filteredMobs.clear();
		this.filteredMobs.addAll(this.allMobs);
		this.leftPos = (this.width - this.imageWidth) / 2;
		this.topPos = (this.height - this.imageHeight) / 2;

		//blacklist/whitelist checkbox
		this.whitelistSelected = new CheckBox(this.leftPos + 7, this.topPos + 20, Component.translatable("gui.rats.isWhitelist"), this.isWhitelist, box -> {});
		this.addRenderableWidget(this.whitelistSelected);

		//selected mobs checkbox
		this.selectedMobsShown = new CheckBox(this.leftPos + 7, this.topPos + 60, Component.translatable("gui.rats.selectedMobs"), false, box -> {
			this.searchBar.setValue("");
			this.refreshSearchResults();
		});
		this.addRenderableWidget(this.selectedMobsShown);

		//search bar
		this.searchBar = new EditBox(this.font, this.leftPos + 121, this.topPos + 8, 85, 12, Component.translatable("itemGroup.search"));
		this.searchBar.setMaxLength(50);
		this.searchBar.setBordered(false);
		this.searchBar.setTextColor(16777215);
		this.addWidget(this.searchBar);
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(graphics);
		//render background behind entity
		graphics.blit(TEXTURE_BACKDROP, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
		//render entity, scissor out everything around the backdrop
		graphics.enableScissor(this.leftPos + 28, this.topPos + 23, this.leftPos + 28 + 90, this.topPos + 23 + 92);
		LivingEntity entity = EntityRenderingUtil.fetchEntity(this.hoveredEntityName, Minecraft.getInstance().level);
		int scale = EntityRenderingUtil.getAdjustedMobScale(this.hoveredEntityName);
		if (entity != null) {
			EntityRenderingUtil.drawEntityOnScreen(this.leftPos + 70, this.topPos + 110, scale, this.leftPos + 69 - (float) mouseX, this.topPos + 110 - (entity.getEyeHeight() * scale) - (float) mouseY, entity, false);
		}
		graphics.disableScissor();
		//render normal GUI
		graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
		super.render(graphics, mouseX, mouseY, partialTicks);
		//draw title
		graphics.drawString(this.font, this.title, this.leftPos + 7, this.topPos + 7, 4210752, false);
		//draw whitelist/blacklist icon under checkbox
		graphics.blit(TEXTURE, this.leftPos + 8, this.topPos + 35, this.whitelistSelected.selected ? 44 : 30, 125, 12, 14);
		//draw selected mob icon
		graphics.blit(TEXTURE, this.leftPos + 8, this.topPos + 75, 58, 125, 12, 12);
		//search bar!
		this.searchBar.render(graphics, mouseX, mouseY, partialTicks);
		//scrollbar!
		int k = (int) (81.0F * this.scrollOffs);
		graphics.blit(TEXTURE, this.leftPos + SCROLL_X_START, this.topPos + SCROLL_Y_START + k, this.scrolling ? 12 : 0, 153, 12, 15);
		//render entity names for selecting. Mob names that are too big will scroll across the area.
		this.renderEntityNames(graphics, this.leftPos + 123, this.topPos + 26, this.startIndex + MAX_MOB_BUTTONS);

		//show a tooltip with the entity's id and any selected tags if hovering over its name
		if (this.isHovering(121, 24, 90, 90, mouseX, mouseY) && !this.filteredMobs.isEmpty()) {
			int y = (mouseY - 90) / 18;
			this.hoveredEntityName = ResourceLocation.tryParse(this.filteredMobs.get(Mth.clamp(this.startIndex + y, 0, Math.max(0, this.filteredMobs.size() - 1))).getFirst());
			if (hoveredEntityName != null && this.startIndex + y < this.filteredMobs.size()) {
				graphics.blit(TEXTURE, this.leftPos + 121, this.topPos + 26 + (y * 18), 0, 168, 90, 15);
				List<FormattedCharSequence> tooltipParts = new ArrayList<>();
				tooltipParts.add(Component.literal(this.hoveredEntityName.toString()).getVisualOrderText());
				this.visibleTags.forEach(key -> {
					if (ForgeRegistries.ENTITY_TYPES.getValue(this.hoveredEntityName).is(key)) {
						tooltipParts.add(Component.literal("#" + key.location()).withStyle(ChatFormatting.DARK_PURPLE).getVisualOrderText());
					}

				});
				graphics.renderTooltip(this.font, tooltipParts, mouseX, mouseY);

			}
		}

		//render a tooltip for rendering over the whitelist checkbox
		if (this.whitelistSelected.isHovered()) {
			graphics.renderTooltip(this.font, Component.literal("Toggle Whitelist"), mouseX, mouseY);
		}

		//render a tooltip for rendering over the selected mob filter checkbox
		if (this.selectedMobsShown.isHovered()) {
			graphics.renderTooltip(this.font, Component.literal("Show Selected Mobs"), mouseX, mouseY);
		}
	}

	private void renderEntityNames(GuiGraphics graphics, int startX, int startY, int startIndex) {
		for (int i = this.startIndex; i < startIndex && i < this.filteredMobs.size(); ++i) {
			int j = i - this.startIndex;
			int y = startY + j * 18;
			this.renderScrollingString(graphics, this.filteredMobs.get(i).getSecond(), startX, y, startX + 88, y + 15, this.selectedMobs.contains(this.filteredMobs.get(i).getFirst()) ? 0X4CFF00 : 0xFFFFFF);
		}
	}

	protected void renderScrollingString(GuiGraphics graphics, Component text, int startX, int startY, int width, int height, int color) {
		int i = this.font.width(text);
		int j = (startY + height - 9) / 2 + 1;
		int k = width - startX;
		if (i > k) {
			int l = i - k + 2;
			double d0 = (double) Util.getMillis() / 1000.0D;
			double d1 = Math.max((double)l * 0.5D, 3.0D);
			double d2 = Math.sin((Math.PI / 2D) * Math.cos((Math.PI) * d0 / d1)) / 2.0D + 0.5D;
			double d3 = Mth.lerp(d2, 0.0D, l);
			graphics.enableScissor(startX - 1, startY, width - 1, height);
			graphics.drawString(this.font, text, startX - (int)d3, j, color, false);
			graphics.disableScissor();
		} else {
			graphics.drawString(this.font, text, startX, j, color, false);
		}

	}

	@Override
	public void tick() {
		this.searchBar.tick();
	}

	private int getOffscreenRows() {
		return this.filteredMobs.size() - MAX_MOB_BUTTONS;
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragSizeX, double dragSizeY) {
		if (this.scrolling && this.filteredMobs.size() > MAX_MOB_BUTTONS) {
			int i = this.topPos + SCROLL_Y_START;
			int j = i + 96;
			this.scrollOffs = ((float) mouseY - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
			this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
			this.startIndex = (int) ((double) (this.scrollOffs * (float) this.getOffscreenRows()) + 0.5D);
			return true;
		} else {
			return super.mouseDragged(mouseX, mouseY, button, dragSizeX, dragSizeY);
		}
	}

	@Override
	public boolean mouseScrolled(double x, double y, double direction) {
		if (this.filteredMobs.size() > MAX_MOB_BUTTONS) {
			int i = this.getOffscreenRows();
			this.scrollOffs = (float) ((double) this.scrollOffs - direction / (double) i);
			this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
			this.startIndex = (int) ((double) (this.scrollOffs * (float) i) + 0.5D);
		}

		return true;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (button == 0) {
			this.scrolling = false;
		}
		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		int index = this.startIndex + MAX_MOB_BUTTONS;

		this.searchBar.setFocused(this.isHovering(119, 6, 96, 12, mouseX, mouseY));

		if (button == 0 && this.isHovering(SCROLL_X_START, SCROLL_Y_START, 12, 96, mouseX, mouseY)) {
			this.scrolling = true;
		}

		for (int l = this.startIndex; l < Math.min(this.filteredMobs.size(), index); ++l) {
			int selectedIndex = l - this.startIndex;
			if (this.isHovering(121, 24 + selectedIndex * 18, 90, 15, mouseX, mouseY)) {
				Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
				if (this.selectedMobs.contains(this.filteredMobs.get(l).getFirst())) {
					this.selectedMobs.remove(this.filteredMobs.get(l).getFirst());
				} else {
					this.selectedMobs.add(this.filteredMobs.get(l).getFirst());
				}
				return true;
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean charTyped(char character, int amount) {
		String s = this.searchBar.getValue();
		if (character == GLFW.GLFW_KEY_GRAVE_ACCENT) {
			RatsMod.LOGGER.debug(this.filteredMobs);
			return true;
		}
		if (this.isValidCharacter(character) && this.searchBar.charTyped(character, amount)) {
			if (!Objects.equals(s, this.searchBar.getValue())) {
				this.refreshSearchResults();
			}

			return true;
		}
		return false;
	}

	private boolean isValidCharacter(char character) {
		return Character.isAlphabetic(character) || Character.isDigit(character) || character == '#' || character == ':';
	}

	@Override
	public boolean keyPressed(int key, int value, int modifier) {
		String s = this.searchBar.getValue();
		if (this.searchBar.keyPressed(key, value, modifier)) {
			if (!Objects.equals(s, this.searchBar.getValue())) {
				this.refreshSearchResults();
			}

			return true;
		} else {
			return key != GLFW.GLFW_KEY_ESCAPE || super.keyPressed(key, value, modifier);
		}
	}

	private void refreshSearchResults() {
		this.scrollOffs = 0.0F;
		this.startIndex = 0;
		this.filteredMobs.clear();
		this.visibleTags.clear();
		if (this.selectedMobsShown.selected) {
			this.selectedMobs.forEach(s -> this.filteredMobs.add(Pair.of(s, ForgeRegistries.ENTITY_TYPES.getValue(ResourceLocation.tryParse(s)).getDescription())));
		} else {
			if (this.searchBar.getValue().startsWith("#")) {
				String tagName = this.searchBar.getValue().substring(1).trim();
				List<TagKey<EntityType<?>>> tags = BuiltInRegistries.ENTITY_TYPE.getTagNames().filter(key -> key.location().toString().contains(tagName)).toList();
				if (!tags.isEmpty()) {
					tags.forEach(key -> {
						this.filteredMobs.addAll(this.allMobs.stream().filter(pair -> ForgeRegistries.ENTITY_TYPES.tags().getTag(key).contains(ForgeRegistries.ENTITY_TYPES.getValue(ResourceLocation.tryParse(pair.getFirst())))).toList());
						if (!this.visibleTags.contains(key)) this.visibleTags.add(key);
					});
				}
			} else {
				this.filteredMobs.addAll(this.allMobs.stream().filter(s -> s.getFirst().toLowerCase(Locale.ROOT).contains(this.searchBar.getValue()) || s.getSecond().getString().toLowerCase(Locale.ROOT).contains(this.searchBar.getValue())).toList());
			}
		}
	}

	protected boolean isHovering(int startX, int startY, int xSize, int ySize, double mouseX, double mouseY) {
		startX += this.leftPos;
		startY += this.topPos;
		return mouseX >= (startX - 1) && mouseX < (startX + xSize + 1) && mouseY >= (startY - 1) && mouseY < (startY + ySize + 1);
	}

	@Override
	public void onClose() {
		super.onClose();
		MobFilterUpgradeItem.setWhitelist(this.stack, this.whitelistSelected.selected);
		MobFilterUpgradeItem.setMobs(this.stack, this.selectedMobs);
		RatsNetworkHandler.CHANNEL.sendToServer(new UpdateMobFilterPacket(this.stack, this.isWhitelist, this.selectedMobs));
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	public static class CheckBox extends AbstractButton {
		private boolean selected;
		private CheckBox.OnPress press;

		public CheckBox(int x, int y, Component message, boolean selected, CheckBox.OnPress press) {
			super(x, y, 12, 12, message);
			this.selected = selected;
			this.press = press;
		}

		@Override
		public void onPress() {
			this.selected = !this.selected;
			this.press.onPress(this);
		}

		@Override
		protected void updateWidgetNarration(NarrationElementOutput output) {
			output.add(NarratedElementType.TITLE, this.createNarrationMessage());
			if (this.active) {
				if (this.isFocused()) {
					output.add(NarratedElementType.USAGE, Component.translatable("narration.checkbox.usage.focused"));
				} else {
					output.add(NarratedElementType.USAGE, Component.translatable("narration.checkbox.usage.hovered"));
				}
			}
		}

		@Override
		public void renderWidget(GuiGraphics graphics, int x, int y, float partialTicks) {
			RenderSystem.enableDepthTest();
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
			RenderSystem.enableBlend();
			graphics.blit(TEXTURE, this.getX(), this.getY(), this.isHovered() ? 14 : 0, this.selected ? 138 : 124, 14, 14);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		}

		public interface OnPress {
			void onPress(CheckBox box);
		}
	}
}
