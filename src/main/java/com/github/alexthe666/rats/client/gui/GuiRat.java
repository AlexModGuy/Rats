package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.inventory.ContainerRat;
import com.github.alexthe666.rats.server.message.MessageRatCommand;
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
import net.minecraft.util.text.TranslationTextComponent;
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


    protected void func_231160_c_() {
        super.func_231160_c_();
        this.field_230710_m_.clear();
        int i = (this.field_230708_k_ - 248) / 2;
        int j = (this.field_230709_l_ - 166) / 2;
        func_230480_a_(new ChangeCommandButton(1, i + 115, j + 54, false, (p_214132_1_) -> {
            currentDisplayCommand--;
            currentDisplayCommand = RatUtils.wrapCommand(currentDisplayCommand).ordinal();
        }));
        func_230480_a_(new ChangeCommandButton(2, i + 198, j + 54, true, (p_214132_1_) -> {
            currentDisplayCommand++;
            currentDisplayCommand = RatUtils.wrapCommand(currentDisplayCommand).ordinal();
        }));
        func_230480_a_(new CommandPressButton(3, i + 122, j + 52, (p_214132_1_) -> {
            rat.setCommand(RatCommand.values()[currentDisplayCommand]);
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageRatCommand(rat.getEntityId(), currentDisplayCommand));
        }));
    }

    @Override
    protected void func_230450_a_(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        this.func_230446_a_(p_230450_1_);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mousePosx = p_230450_3_;
        this.mousePosY = p_230450_4_;
        int k = (this.field_230708_k_ - this.xSize) / 2;
        int l = (this.field_230709_l_ - this.ySize) / 2;
        this.getMinecraft().getTextureManager().bindTexture(TEXTURE_BACKDROP);
        this.func_238474_b_(p_230450_1_, k, l, 0, 0, this.xSize, this.ySize);
        drawEntityOnScreen(k + 35, l + 60, 70, k + 51 - this.mousePosx, l + 75 - 50 - this.mousePosY, this.rat);
        this.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        this.func_238474_b_(p_230450_1_, k, l, 0, 0, this.xSize, this.ySize);
        this.func_238474_b_(p_230450_1_, k + 9, l + 20, rat.isMale() ? 176 : 192, 0, 16, field_230709_l_);
        drawGuiContainerForegroundLayer(p_230450_1_, p_230450_3_, p_230450_4_);
    }

    protected void drawGuiContainerForegroundLayer(MatrixStack stackIn, int mouseX, int mouseY) {
        String name = func_231171_q_().getString().length() == 0 ? I18n.format("entity.rats.rat") : func_231171_q_().getString();
        this.field_230712_o_.func_238405_a_(stackIn,  name, this.xSize / 2 - this.field_230712_o_.getStringWidth(name) / 2, 6, 4210752);

        String commandDesc = I18n.format("entity.rats.rat.command.current");
        this.field_230712_o_.func_238405_a_(stackIn,  commandDesc, this.xSize / 2 - this.field_230712_o_.getStringWidth(commandDesc) / 2 + 36, 19, 4210752);

        String command = I18n.format(rat.getCommand().getTranslateName());
        this.field_230712_o_.func_238405_a_(stackIn,  command, this.xSize / 2 - this.field_230712_o_.getStringWidth(command) / 2 + 36, 31, 0XFFFFFF);

        String statusDesc = I18n.format("entity.rats.rat.command.set");
        this.field_230712_o_.func_238405_a_(stackIn,  statusDesc, this.xSize / 2 - this.field_230712_o_.getStringWidth(statusDesc) / 2 + 36, 44, 4210752);
        RatCommand command1 = RatUtils.wrapCommand(currentDisplayCommand);
        String command2 = I18n.format(command1.getTranslateName());
        this.field_230712_o_.func_238405_a_(stackIn, command2, this.xSize / 2 - this.field_230712_o_.getStringWidth(command2) / 2 + 36, 56, 0XFFFFFF);
        int i = (this.field_230708_k_ - 248) / 2;
        int j = (this.field_230709_l_ - 166) / 2;
        if (mouseX > i + 116 && mouseX < i + 198 && mouseY > j + 22 && mouseY < j + 45) {
            IFormattableTextComponent commandText = new TranslationTextComponent(rat.getCommand().getTranslateDescription());
            func_238654_b_(stackIn, Arrays.asList(commandText), mouseX - i - 40, mouseY - j + 10, field_230712_o_);
        }
        if (mouseX > i + 116 && mouseX < i + 198 && mouseY > j + 53 && mouseY < j + 69) {
            IFormattableTextComponent commandText = new TranslationTextComponent(command1.getTranslateDescription());
            func_238654_b_(stackIn, Arrays.asList(commandText), mouseX - i - 40, mouseY - j + 10, field_230712_o_);
        }
    }
}
