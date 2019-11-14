package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.inventory.ContainerRat;
import com.github.alexthe666.rats.server.message.MessageRatCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;

@SideOnly(Side.CLIENT)
public class GuiRat extends GuiContainer {
    protected static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/gui/rat_inventory.png");
    private static final ResourceLocation TEXTURE_BACKDROP = new ResourceLocation("rats:textures/gui/rat_inventory_backdrop.png");
    public ChangeCommandButton previousCommand;
    public ChangeCommandButton nextCommand;
    public CommandPressButton pressButton;
    private int currentDisplayCommand = 0;
    private EntityRat rat;
    private float mousePosx;
    private float mousePosY;

    public GuiRat(EntityRat rat) {
        super(new ContainerRat(rat, Minecraft.getMinecraft().player));
        this.rat = rat;
        this.allowUserInput = false;
    }

    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityRat entity) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, 30);
        GlStateManager.scale((-scale), scale, scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = entity.renderYawOffset;
        float f3 = entity.rotationYaw;
        float f4 = entity.rotationPitch;
        float f5 = entity.prevRotationYawHead;
        float f6 = entity.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float) Math.atan(mouseY / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
        entity.renderYawOffset = (float) Math.atan(mouseX / 40.0F) * 20.0F;
        entity.rotationYaw = (float) Math.atan(mouseX / 40.0F) * 40.0F;
        entity.rotationPitch = -((float) Math.atan(mouseY / 40.0F)) * 20.0F;
        entity.rotationYawHead = entity.rotationYaw;
        entity.prevRotationYawHead = entity.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        entity.renderYawOffset = f2;
        entity.rotationYaw = f3;
        entity.rotationPitch = f4;
        entity.prevRotationYawHead = f5;
        entity.rotationYawHead = f6;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        this.buttonList.add(this.previousCommand = new ChangeCommandButton(1, i + 115, j + 54, false));
        this.buttonList.add(this.nextCommand = new ChangeCommandButton(2, i + 198, j + 54, true));
        this.buttonList.add(this.pressButton = new CommandPressButton(3, i + 122, j + 52));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) {
            currentDisplayCommand--;
            currentDisplayCommand = RatUtils.wrapCommand(currentDisplayCommand).ordinal();
            //rat.setCommand(RatCommand.values()[currentDisplayCommand]);
            //RatsMod.NETWORK_WRAPPER.sendToServer(new MessageRatCommand(rat.getEntityId(), currentRatCommand));
        }
        if (button.id == 2) {
            currentDisplayCommand++;
            currentDisplayCommand = RatUtils.wrapCommand(currentDisplayCommand).ordinal();
            //rat.setCommand(RatCommand.values()[currentRatCommand]);
            //RatsMod.NETWORK_WRAPPER.sendToServer(new MessageRatCommand(rat.getEntityId(), currentRatCommand));
        }
        if (button.id == 3) {
            rat.setCommand(RatCommand.values()[currentDisplayCommand]);
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageRatCommand(rat.getEntityId(), currentDisplayCommand));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = rat.getCustomNameTag().length() == 0 ? I18n.format("entity.rat.name") : rat.getCustomNameTag();
        this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 4210752);

        String commandDesc = I18n.format("entity.rat.command.current");
        this.fontRenderer.drawString(commandDesc, this.xSize / 2 - this.fontRenderer.getStringWidth(commandDesc) / 2 + 36, 19, 4210752);

        String command = I18n.format(rat.getCommand().getTranslateName());
        this.fontRenderer.drawString(command, this.xSize / 2 - this.fontRenderer.getStringWidth(command) / 2 + 36, 31, 0XFFFFFF, true);

        String statusDesc = I18n.format("entity.rat.command.set");
        this.fontRenderer.drawString(statusDesc, this.xSize / 2 - this.fontRenderer.getStringWidth(statusDesc) / 2 + 36, 44, 4210752);
        RatCommand command1 = RatUtils.wrapCommand(currentDisplayCommand);
        String command2 = I18n.format(command1.getTranslateName());
        this.fontRenderer.drawString(command2, this.xSize / 2 - this.fontRenderer.getStringWidth(command2) / 2 + 36, 56, 0XFFFFFF, true);
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        if (mouseX > i + 116 && mouseX < i + 198 && mouseY > j + 22 && mouseY < j + 45) {
            String commandText = I18n.format(rat.getCommand().getTranslateDescription());
            net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(Arrays.asList(commandText), mouseX - i - 40, mouseY - j + 10, width, height, 120, fontRenderer);
        }
        if (mouseX > i + 116 && mouseX < i + 198 && mouseY > j + 53 && mouseY < j + 69) {
            String commandText = I18n.format(command1.getTranslateDescription());
            net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(Arrays.asList(commandText), mouseX - i - 40, mouseY - j + 10, width, height, 120, fontRenderer);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE_BACKDROP);
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        GlStateManager.pushMatrix();
        drawEntityOnScreen(k + 35, l + 60, 70, k + 51 - this.mousePosx, l + 75 - 50 - this.mousePosY, this.rat);
        GlStateManager.popMatrix();
        this.mc.getTextureManager().bindTexture(TEXTURE);
        GlStateManager.translate(0.0F, 0.0F, 65.0F);
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(k + 9, l + 20, rat.isMale() ? 176 : 192, 0, 16, height);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.mousePosx = mouseX;
        this.mousePosY = mouseY;
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
