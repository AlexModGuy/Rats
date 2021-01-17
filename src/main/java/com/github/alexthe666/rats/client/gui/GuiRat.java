package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.inventory.ContainerRat;
import com.github.alexthe666.rats.server.message.MessageRatCommand;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, LivingEntity p_228187_5_) {
        float f = (float)Math.atan((double)(mouseX / 40.0F));
        float f1 = (float)Math.atan((double)(mouseY / 40.0F));
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)posX, (float)posY, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.translate(0.0D, 0.0D, 1000.0D);
        matrixstack.scale((float)scale, (float)scale, (float)scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        quaternion.multiply(quaternion1);
        matrixstack.rotate(quaternion);
        float f2 = p_228187_5_.renderYawOffset;
        float f3 = p_228187_5_.rotationYaw;
        float f4 = p_228187_5_.rotationPitch;
        float f5 = p_228187_5_.prevRotationYawHead;
        float f6 = p_228187_5_.rotationYawHead;
        p_228187_5_.renderYawOffset = 180.0F + f * 20.0F;
        p_228187_5_.rotationYaw = 180.0F + f * 40.0F;
        p_228187_5_.rotationPitch = -f1 * 20.0F;
        p_228187_5_.rotationYawHead = p_228187_5_.rotationYaw;
        p_228187_5_.prevRotationYawHead = p_228187_5_.rotationYaw;
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
        quaternion1.conjugate();
        entityrenderermanager.setCameraOrientation(quaternion1);
        entityrenderermanager.setRenderShadow(false);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        RenderSystem.runAsFancy(() -> {
            entityrenderermanager.renderEntityStatic(p_228187_5_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
        });
        irendertypebuffer$impl.finish();
        entityrenderermanager.setRenderShadow(true);
        p_228187_5_.renderYawOffset = f2;
        p_228187_5_.rotationYaw = f3;
        p_228187_5_.rotationPitch = f4;
        p_228187_5_.prevRotationYawHead = f5;
        p_228187_5_.rotationYawHead = f6;
        RenderSystem.popMatrix();
    }


    protected void init() {
        super.init();
        this.buttons.clear();
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        addButton(new ChangeCommandButton(1, i + 124, j + 54, false, (p_214132_1_) -> {
            currentDisplayCommand--;
            currentDisplayCommand = RatUtils.wrapCommand(currentDisplayCommand).ordinal();
        }));
        addButton(new ChangeCommandButton(2, i + 207, j + 54, true, (p_214132_1_) -> {
            currentDisplayCommand++;
            currentDisplayCommand = RatUtils.wrapCommand(currentDisplayCommand).ordinal();
        }));
        addButton(new CommandPressButton(3, i + 131, j + 52, (p_214132_1_) -> {
            rat.setCommand(RatCommand.values()[currentDisplayCommand]);
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageRatCommand(rat.getEntityId(), currentDisplayCommand));
        }));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        this.renderBackground(p_230450_1_);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mousePosx = p_230450_3_;
        this.mousePosY = p_230450_4_;
        int k = (this.width - 192) / 2;
        int l = (this.height - this.ySize) / 2;
        this.getMinecraft().getTextureManager().bindTexture(TEXTURE_BACKDROP);
        this.blit(p_230450_1_, k, l, 0, 0, 192, this.ySize);
        drawEntityOnScreen(k + 50, l + 60, 70, k + 51 - this.mousePosx, l + 75 - 50 - this.mousePosY, this.rat);
        this.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        this.blit(p_230450_1_, k, l, 0, 0, 192, this.ySize);
        this.blit(p_230450_1_, k + 60, l + 20, rat.isMale() ? 0 : 16, 209, 16, 16);
    }

    protected void drawGuiContainerForegroundLayer(MatrixStack stackIn, int mouseX, int mouseY) {
        String name = getTitle().getString().length() == 0 ? I18n.format("entity.rats.rat") : getTitle().getString();
        this.font.drawString(stackIn,  name, 192 / 2 - this.font.getStringWidth(name) / 2, 6, 4210752);

        String commandDesc = I18n.format("entity.rats.rat.command.current");
        this.font.drawString(stackIn,  commandDesc, 192 / 2 - this.font.getStringWidth(commandDesc) / 2 + 38, 19, 4210752);

        String command = I18n.format(rat.getCommand().getTranslateName());
        this.font.drawStringWithShadow(stackIn,  command, 192 / 2 - this.font.getStringWidth(command) / 2 + 38, 31, 0XFFFFFF);

        String statusDesc = I18n.format("entity.rats.rat.command.set");
        this.font.drawString(stackIn,  statusDesc, 192 / 2 - this.font.getStringWidth(statusDesc) / 2 + 38, 44, 4210752);
        RatCommand command1 = RatUtils.wrapCommand(currentDisplayCommand);
        String command2 = I18n.format(command1.getTranslateName());
        this.font.drawStringWithShadow(stackIn, command2, 192 / 2 - this.font.getStringWidth(command2) / 2 + 38, 56, 0XFFFFFF);
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        if (mouseX > i + 116 && mouseX < i + 198 && mouseY > j + 22 && mouseY < j + 45) {
            IFormattableTextComponent commandText = new TranslationTextComponent(rat.getCommand().getTranslateDescription());
            String[] everySpace = commandText.getString().split(" ");
            int currentStrLength = 0;
            String builtString = "";
            ArrayList<String> list = new ArrayList<String>();
            for(int index = 0; index < everySpace.length; index++){
                builtString = builtString + everySpace[index] + " ";
                currentStrLength += font.getStringWidth(everySpace[index] + " ");
                if(currentStrLength >= 95){
                    list.add(builtString);
                    builtString = "";
                    currentStrLength = 0;
                }
            }
            List<IFormattableTextComponent> convertedList = new ArrayList<>();
            for(String str : list){
                convertedList.add(new StringTextComponent(str));
            }
            renderWrappedToolTip(stackIn, convertedList, mouseX - i - 40, mouseY - j + 10, font);
        }
        if (mouseX > i + 116 && mouseX < i + 198 && mouseY > j + 53 && mouseY < j + 69) {
            IFormattableTextComponent commandText = new TranslationTextComponent(command1.getTranslateDescription());
            String[] everySpace = commandText.getString().split(" ");
            int currentStrLength = 0;
            String builtString = "";
            ArrayList<String> list = new ArrayList<String>();
            for(int index = 0; index < everySpace.length; index++){
                builtString = builtString + everySpace[index] + " ";
                currentStrLength += font.getStringWidth(everySpace[index] + " ");
                if(currentStrLength >= 95){
                    list.add(builtString);
                    builtString = "";
                    currentStrLength = 0;
                }
            }
            List<IFormattableTextComponent> convertedList = new ArrayList<>();
            for(String str : list){
                convertedList.add(new StringTextComponent(str));
            }
            renderWrappedToolTip(stackIn, convertedList, mouseX - i - 40, mouseY - j + 10, font);
        }
    }
}
