package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.ClientProxy;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.message.MessageCheeseStaffSync;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiRadiusStaff extends Screen {

    protected static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/gui/cheese_staff.png");
    private final GuiSlider.ISlider sliderResponder;
    private final EntityRat rat;
    private int sliderValue;
    private int prevSliderValue = sliderValue;

    public GuiRadiusStaff(EntityRat rat) {
        super(new TranslationTextComponent("radius_staff"));
        this.rat = rat;
        sliderValue = rat.getSearchRadius();
        prevSliderValue = sliderValue;
        init();
        sliderResponder = new GuiSlider.ISlider() {
            @Override
            public void onChangeSliderValue(GuiSlider slider){
                GuiRadiusStaff.this.setSliderValue(0, (float)slider.sliderValue);
            }
        };
    }

    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityRat entity) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translatef(posX, posY, 30);
        GlStateManager.scalef((-scale), scale, scale);
        GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = entity.renderYawOffset;
        float f3 = entity.rotationYaw;
        float f4 = entity.rotationPitch;
        float f5 = entity.prevRotationYawHead;
        float f6 = entity.rotationYawHead;
        GlStateManager.rotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translatef(0.0F, 0.0F, 0.0F);
        EntityRendererManager rendermanager = Minecraft.getInstance().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        GlStateManager.rotatef(Minecraft.getInstance().player.ticksExisted % 360, 0, 1, 0);
        rendermanager.setRenderShadow(false);
        entity.renderYawOffset = 0;
        entity.rotationYaw = 0;
        entity.rotationPitch = 0;
        entity.prevRotationYawHead = 0;
        entity.rotationYawHead = 0;
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
        GlStateManager.activeTexture(GLX.GL_TEXTURE1);
        GlStateManager.disableTexture();
        GlStateManager.activeTexture(GLX.GL_TEXTURE0);
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
        this.addButton(new GuiSlider(i - 150 / 2, j + 85, 150, 20, I18n.format("entity.rat.staff.radius") + ": ", "", 1, RatConfig.maxRatRadius, sliderValue, false, true, (p_214132_1_) -> {

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
            } catch (Exception e) {

            }
        }

        super.render(mouseX, mouseY, partialTicks);
        int i = (this.width - 248) / 2 + 10;
        int j = (this.height - 166) / 2 + 8;
        GlStateManager.pushMatrix();
        GlStateManager.translatef(0, 0, 10F);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        drawEntityOnScreen(i + 114, j + 40, 70, 0, 0, this.rat);
        GlStateManager.popMatrix();
    }

    public boolean isPauseScreen() {
        return false;
    }

}

