package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.inventory.ContainerRat;
import com.github.alexthe666.rats.server.message.MessageRatCommand;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class GuiRat extends ContainerScreen<ContainerRat> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/gui/rat_inventory.png");
    private static final ResourceLocation TEXTURE_BACKDROP = new ResourceLocation("rats:textures/gui/rat_inventory_backdrop.png");
    private int currentDisplayCommand = 0;
    private EntityRat rat;
    private float mousePosx;
    private float mousePosY;

    public GuiRat(ContainerRat container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.rat = RatsMod.PROXY.getRefrencedRat();
    }

    public static void drawEntityOnScreen(int x, int y, int scale, float yaw, float pitch, LivingEntity entity) {
        float f = (float)Math.atan((double)(yaw / 40.0F));
        float f1 = (float)Math.atan((double)(pitch / 40.0F));
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)x, (float)y, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.translate(0.0D, 0.0D, 1000.0D);
        matrixstack.scale((float)scale, (float)scale, (float)scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        quaternion.multiply(quaternion1);
        matrixstack.rotate(quaternion);
        float f2 = entity.renderYawOffset;
        float f3 = entity.rotationYaw;
        float f4 = entity.rotationPitch;
        float f5 = entity.prevRotationYawHead;
        float f6 = entity.rotationYawHead;
        entity.renderYawOffset = 180.0F + f * 20.0F;
        entity.rotationYaw = 180.0F + f * 40.0F;
        entity.rotationPitch = -f1 * 20.0F;
        entity.rotationYawHead = entity.rotationYaw;
        entity.prevRotationYawHead = entity.rotationYaw;
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
        quaternion1.conjugate();
        entityrenderermanager.setCameraOrientation(quaternion1);
        entityrenderermanager.setRenderShadow(false);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        entityrenderermanager.renderEntityStatic(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
        irendertypebuffer$impl.finish();
        entityrenderermanager.setRenderShadow(true);
        entity.renderYawOffset = f2;
        entity.rotationYaw = f3;
        entity.rotationPitch = f4;
        entity.prevRotationYawHead = f5;
        entity.rotationYawHead = f6;
        RenderSystem.popMatrix();
    }


    public void init() {
        super.init();
        this.buttons.clear();
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        addButton(new ChangeCommandButton(1, i + 115, j + 54, false, (p_214132_1_) -> {
            currentDisplayCommand--;
            currentDisplayCommand = RatUtils.wrapCommand(currentDisplayCommand).ordinal();
        }));
        addButton(new ChangeCommandButton(2, i + 198, j + 54, true, (p_214132_1_) -> {
            currentDisplayCommand++;
            currentDisplayCommand = RatUtils.wrapCommand(currentDisplayCommand).ordinal();
        }));
        addButton(new CommandPressButton(3, i + 122, j + 52, (p_214132_1_) -> {
            rat.setCommand(RatCommand.values()[currentDisplayCommand]);
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageRatCommand(rat.getEntityId(), currentDisplayCommand));
        }));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = getTitle().getFormattedText().length() == 0 ? I18n.format("entity.rats.rat.name") : getTitle().getFormattedText();
        this.font.drawString(name, this.xSize / 2 - this.font.getStringWidth(name) / 2, 6, 4210752);

        String commandDesc = I18n.format("entity.rats.rat.command.current");
        this.font.drawString(commandDesc, this.xSize / 2 - this.font.getStringWidth(commandDesc) / 2 + 36, 19, 4210752);

        String command = I18n.format(rat.getCommand().getTranslateName());
        this.font.drawString(command, this.xSize / 2 - this.font.getStringWidth(command) / 2 + 36, 31, 0XFFFFFF);

        String statusDesc = I18n.format("entity.rats.rat.command.set");
        this.font.drawString(statusDesc, this.xSize / 2 - this.font.getStringWidth(statusDesc) / 2 + 36, 44, 4210752);
        RatCommand command1 = RatUtils.wrapCommand(currentDisplayCommand);
        String command2 = I18n.format(command1.getTranslateName());
        this.font.drawString(command2, this.xSize / 2 - this.font.getStringWidth(command2) / 2 + 36, 56, 0XFFFFFF);
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        if (mouseX > i + 116 && mouseX < i + 198 && mouseY > j + 22 && mouseY < j + 45) {
            String commandText = I18n.format(rat.getCommand().getTranslateDescription());
            renderTooltip(Arrays.asList(commandText), mouseX - i - 40, mouseY - j + 10, font);
        }
        if (mouseX > i + 116 && mouseX < i + 198 && mouseY > j + 53 && mouseY < j + 69) {
            String commandText = I18n.format(command1.getTranslateDescription());
            renderTooltip(Arrays.asList(commandText), mouseX - i - 40, mouseY - j + 10, font);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(TEXTURE_BACKDROP);
        this.blit(k, l, 0, 0, this.xSize, this.ySize);
        GlStateManager.pushMatrix();
        drawEntityOnScreen(k + 35, l + 60, 70, k + 51 - this.mousePosx, l + 75 - 50 - this.mousePosY, this.rat);
        GlStateManager.popMatrix();
        this.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        GlStateManager.translatef(0.0F, 0.0F, 65.0F);
        this.blit(k, l, 0, 0, this.xSize, this.ySize);
        this.blit(k + 9, l + 20, rat.isMale() ? 176 : 192, 0, 16, height);

    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.mousePosx = mouseX;
        this.mousePosY = mouseY;
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
