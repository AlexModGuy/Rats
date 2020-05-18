package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.ClientProxy;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.message.MessageCheeseStaffSync;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.Slider;

public class GuiRadiusStaff extends Screen {

    protected static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/gui/cheese_staff.png");
    private final Slider.ISlider sliderResponder;
    private final EntityRat rat;
    private int sliderValue;
    private int prevSliderValue = sliderValue;

    public GuiRadiusStaff(EntityRat rat) {
        super(new TranslationTextComponent("radius_staff"));
        this.rat = rat;
        sliderValue = rat.getSearchRadius();
        prevSliderValue = sliderValue;
        init();
        sliderResponder = new Slider.ISlider() {
            @Override
            public void onChangeSliderValue(Slider slider){
                GuiRadiusStaff.this.setSliderValue(0, (float)slider.sliderValue);
            }
        };
    }

    public static void drawEntityOnScreen(int x, int y, int scale, float yaw, float pitch, LivingEntity entity) {
        float f = (float)Math.atan((double)(yaw / 40.0F));
        float f1 = (float)Math.atan((double)(pitch / 40.0F));
        float rotate = (Minecraft.getInstance().getRenderPartialTicks() + Minecraft.getInstance().player.ticksExisted) * 2F;
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)x, (float)y, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.translate(0.0D, 0.0D, 1000.0D);
        matrixstack.scale((float)scale, (float)scale, (float)scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        Quaternion quaternion2 = Vector3f.YP.rotationDegrees(rotate);
        quaternion.multiply(quaternion1);
        matrixstack.rotate(quaternion);
        matrixstack.rotate(quaternion2);
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

    private void setSliderValue(int id, float value) {
        value = value * RatConfig.maxRatRadius;
        this.sliderValue = (int)Math.round(value);
        if(prevSliderValue != sliderValue){
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageCheeseStaffSync(rat.getEntityId(), BlockPos.ZERO, Direction.UP, 5, sliderValue));
            rat.setSearchRadius((int)Math.round(sliderValue));
        }
        prevSliderValue = sliderValue;
    }

    public void init() {
        super.init();
        this.buttons.clear();
        int i = (this.width) / 2;
        int j = (this.height - 166) / 2;
        String topText = I18n.format("entity.rat.staff.set_radius_loc", getPosName());
        String secondText = I18n.format("entity.rat.staff.reset_radius");
        int maxLength = Math.max(150, Minecraft.getInstance().fontRenderer.getStringWidth(topText) + 20);
        this.addButton(new Button(i - maxLength / 2, j + 60, maxLength, 20, topText, (p_214132_1_) -> {
            BlockPos pos = ClientProxy.refrencedPos;
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageCheeseStaffSync(rat.getEntityId(), pos, Direction.UP, 4, 0));
            rat.setSearchRadiusCenter(pos);
        }));
        this.addButton(new Slider(i - 150 / 2, j + 85, 150, 20, I18n.format("entity.rat.staff.radius") + ": ", "", 1, RatConfig.maxRatRadius, sliderValue, false, true, (p_214132_1_) -> {

        }, sliderResponder){

        });
        this.addButton(new Button(i - maxLength / 2, j + 110, maxLength, 20, secondText, (p_214132_1_) -> {
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageCheeseStaffSync(rat.getEntityId(), BlockPos.ZERO, Direction.UP, 6, 0));
            rat.setSearchRadiusCenter(null);
            rat.setSearchRadius(RatConfig.defaultRatRadius);
        }));
    }

    private String getPosName() {
        return "(" + ClientProxy.refrencedPos.getX() + ", " + ClientProxy.refrencedPos.getY() + ", " + ClientProxy.refrencedPos.getZ() + ")";
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (getMinecraft() != null) {
            try {
                this.renderBackground();
                super.render(mouseX, mouseY, partialTicks);
                int i = (this.width - 248) / 2 + 10;
                int j = (this.height - 166) / 2 + 8;
                GlStateManager.pushMatrix();
                GlStateManager.translatef(0, 0, 10F);
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                drawEntityOnScreen(i + 114, j + 40, 70, 0, 0, this.rat);
                GlStateManager.popMatrix();
            } catch (Exception e) {
                Minecraft.getInstance().displayGuiScreen(null);
            }
        }


    }

    public boolean isPauseScreen() {
        return false;
    }

}

