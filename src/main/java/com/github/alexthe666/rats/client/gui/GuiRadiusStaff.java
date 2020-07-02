package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.ClientProxy;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.message.MessageCheeseStaffSync;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.widget.Slider;

@OnlyIn(Dist.CLIENT)
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
        func_231160_c_();
        sliderResponder = new Slider.ISlider() {
            @Override
            public void onChangeSliderValue(Slider slider){
                GuiRadiusStaff.this.setSliderValue(0, (float)slider.sliderValue);
            }
        };
    }

    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, LivingEntity p_228187_5_) {
        float rotate = (Minecraft.getInstance().getRenderPartialTicks() + Minecraft.getInstance().player.ticksExisted) * 2F;
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
        Quaternion quaternion2 = Vector3f.YP.rotationDegrees(rotate);
        quaternion.multiply(quaternion1);
        matrixstack.rotate(quaternion);
        matrixstack.rotate(quaternion2);
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

    protected void func_231160_c_() {
        super.func_231160_c_();
        this.field_230710_m_.clear();
        int i = (this.field_230708_k_) / 2;
        int j = (this.field_230709_l_ - 166) / 2;
        IFormattableTextComponent topText = new TranslationTextComponent("entity.rat.staff.set_radius_loc", getPosName());
        IFormattableTextComponent secondText = new TranslationTextComponent("entity.rat.staff.reset_radius");
        int maxLength = Math.max(150, Minecraft.getInstance().fontRenderer.getStringWidth(topText.getString()) + 20);
        this.func_230480_a_(new Button(i - maxLength / 2, j + 60, maxLength, 20, topText, (p_214132_1_) -> {
            BlockPos pos = ClientProxy.refrencedPos;
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageCheeseStaffSync(rat.getEntityId(), pos, Direction.UP, 4, 0));
            rat.setSearchRadiusCenter(pos);
        }));
        this.func_230480_a_(new Slider(i - 150 / 2, j + 85, 150, 20, new TranslationTextComponent("entity.rat.staff.radius").func_230529_a_(new StringTextComponent( ": ")), new StringTextComponent( ""), 1, RatConfig.maxRatRadius, sliderValue, false, true, (p_214132_1_) -> {

        }, sliderResponder){

        });
        this.func_230480_a_(new Button(i - maxLength / 2, j + 110, maxLength, 20, secondText, (p_214132_1_) -> {
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageCheeseStaffSync(rat.getEntityId(), BlockPos.ZERO, Direction.UP, 6, 0));
            rat.setSearchRadiusCenter(null);
            rat.setSearchRadius(RatConfig.defaultRatRadius);
        }));
    }

    private String getPosName() {
        return "(" + ClientProxy.refrencedPos.getX() + ", " + ClientProxy.refrencedPos.getY() + ", " + ClientProxy.refrencedPos.getZ() + ")";
    }

    @Override
    public void func_230430_a_(MatrixStack stack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        if (getMinecraft() != null) {
            try {
                this.func_230446_a_(stack);
            } catch (Exception e) {

            }
        }
        super.func_230430_a_(stack, p_230430_2_, p_230430_3_, p_230430_4_);
        int i = (this.field_230708_k_ - 248) / 2 + 10;
        int j = (this.field_230709_l_ - 166) / 2 + 8;
        if(this.rat != null){
            drawEntityOnScreen(i + 114, j + 40, 70, 0, 0, this.rat);
        }
    }

    public boolean func_231177_au__() {
        return false;
    }

}

