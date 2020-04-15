package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.ClientProxy;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.message.MessageCheeseStaffSync;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class GuiRadiusStaff extends GuiScreen {

    protected static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/gui/cheese_staff.png");
    private final GuiPageButtonList.GuiResponder sliderResponder;
    private final GuiSlider.FormatHelper formatHelper;
    private final EntityRat rat;
    private int sliderValue;
    private int prevSliderValue = sliderValue;

    public GuiRadiusStaff(EntityRat rat) {
        super();
        this.rat = rat;
        sliderValue = rat.getSearchRadius();
        prevSliderValue = sliderValue;
        initGui();
        sliderResponder = new GuiPageButtonList.GuiResponder() {
            @Override
            public void setEntryValue(int id, boolean value) {

            }

            @Override
            public void setEntryValue(int id, float value) {
                GuiRadiusStaff.this.setSliderValue(id, value);
            }

            @Override
            public void setEntryValue(int id, String value) {

            }
        };
        formatHelper = new GuiSlider.FormatHelper() {
            @Override
            public String getText(int id, String name, float value) {
                return name + ": " + (int)Math.round(value);
            }
        };
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
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        GlStateManager.rotate(Minecraft.getMinecraft().player.ticksExisted % 360, 0, 1, 0);
        rendermanager.setRenderShadow(false);
        entity.renderYawOffset = 0;
        entity.rotationYaw = 0;
        entity.rotationPitch = 0;
        entity.prevRotationYawHead = 0;
        entity.rotationYawHead = 0;
        try{
            rendermanager.renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        }catch (Exception e){
            RatsMod.logger.warn("Could not render rat do to interference with vanilla code by another mod");
        }
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

    private void setSliderValue(int id, float value) {
        this.sliderValue = (int)Math.round(value);
        if(prevSliderValue != sliderValue){
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageCheeseStaffSync(rat.getEntityId(), BlockPos.ORIGIN, EnumFacing.UP, 5, sliderValue));
            rat.setSearchRadius((int)Math.round(sliderValue));
        }
        prevSliderValue = sliderValue;
    }

    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        int i = (this.width) / 2;
        int j = (this.height - 166) / 2;
        String topText = I18n.format("entity.rat.staff.set_radius_loc", getPosName());
        String secondText = I18n.format("entity.rat.staff.reset_radius");
        int maxLength = Math.max(150, Minecraft.getMinecraft().fontRenderer.getStringWidth(topText) + 20);
        this.buttonList.add(new GuiButton(0, i - maxLength / 2, j + 60, maxLength, 20, topText));
        this.buttonList.add(new GuiSlider(sliderResponder, 1, i - 150 / 2, j + 85, I18n.format("entity.rat.staff.radius"), 1, RatsMod.CONFIG_OPTIONS.maxRatRadius, sliderValue, formatHelper){

        });
        this.buttonList.add(new GuiButton(2, i - maxLength / 2, j + 110, maxLength, 20, secondText));
    }

    private String getPosName() {
        return "(" + ClientProxy.refrencedPos.getX() + ", " + ClientProxy.refrencedPos.getY() + ", " + ClientProxy.refrencedPos.getZ() + ")";
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (mc != null) {
            try {
                this.drawDefaultBackground();
            } catch (Exception e) {

            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
        int i = (this.width - 248) / 2 + 10;
        int j = (this.height - 166) / 2 + 8;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 10F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawEntityOnScreen(i + 114, j + 40, 70, 0, 0, this.rat);
        GlStateManager.popMatrix();
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled && button.id == 0) {//set radius point
            BlockPos pos = ClientProxy.refrencedPos;
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageCheeseStaffSync(rat.getEntityId(), pos, EnumFacing.UP, 4, 0));
            rat.setSearchRadiusCenter(pos);
        }
        if (button.enabled && button.id == 2) {//reset
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageCheeseStaffSync(rat.getEntityId(), BlockPos.ORIGIN, EnumFacing.UP, 6, 0));
            rat.setSearchRadiusCenter(null);
            rat.setSearchRadius(RatsMod.CONFIG_OPTIONS.defaultRatRadius);
        }
        //initGui();
    }


}

