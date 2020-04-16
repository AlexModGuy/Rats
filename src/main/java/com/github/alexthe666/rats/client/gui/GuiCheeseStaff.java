package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.ClientProxy;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.message.MessageCheeseStaffSync;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiCheeseStaff extends Screen {

    protected static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/gui/cheese_staff.png");
    private EntityRat rat;

    public GuiCheeseStaff(EntityRat rat) {
        super(new TranslationTextComponent("cheese_staff"));
        this.rat = rat;
        init();
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
        int i = (this.width) / 2;
        int j = (this.height - 166) / 2;
        String topText = I18n.format("entity.rats.rat.staff.mark_block_deposit", getPosName()) + " " + I18n.format("rats.direction." + ClientProxy.refrencedFacing.getName());
        int maxLength = Math.max(150, Minecraft.getInstance().fontRenderer.getStringWidth(topText) + 20);
        this.addButton(new Button(i - maxLength / 2, j + 60, maxLength, 20, topText, (p_214132_1_) -> {
            BlockPos pos = ClientProxy.refrencedPos;
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageCheeseStaffSync(rat.getEntityId(), pos, ClientProxy.refrencedFacing, 0));
            Minecraft.getInstance().displayGuiScreen(null);
            init();
        }));
        this.addButton(new Button(i - maxLength / 2, j + 85, maxLength, 20, I18n.format("entity.rats.rat.staff.mark_block_pickup", getPosName()), (p_214132_1_) -> {
            BlockPos pos = ClientProxy.refrencedPos;
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageCheeseStaffSync(rat.getEntityId(), pos, Direction.UP, 1));
            Minecraft.getInstance().displayGuiScreen(null);
            init();
        }));
        this.addButton(new Button(i - maxLength / 2, j + 110, maxLength, 20, I18n.format("entity.rats.rat.staff.set_home_point", getPosName()), (p_214132_1_) -> {
            BlockPos pos = ClientProxy.refrencedPos;
            rat.setHomePosAndDistance(pos, 32);
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageCheeseStaffSync(rat.getEntityId(), pos, Direction.UP, 2));
            init();
        }));
        this.addButton(new Button(i - maxLength / 2, j + 135, maxLength, 20, I18n.format("entity.rats.rat.staff.un_set_home_point"), (p_214132_1_) -> {
            BlockPos pos = ClientProxy.refrencedPos;
            rat.detachHome();
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageCheeseStaffSync(rat.getEntityId(), pos, Direction.UP, 3));
            init();
        }));
        this.buttons.get(0).active = !isNoInventoryAtPos();
        this.buttons.get(1).active = !isNoInventoryAtPos();
        this.buttons.get(2).active = !ClientProxy.refrencedPos.equals(rat.getHomePosition()) || !rat.detachHome();
        this.buttons.get(3).active = rat.detachHome();
    }

    private String getPosName() {
        if (ClientProxy.refrencedPos != null) {
            BlockState state = rat.world.getBlockState(ClientProxy.refrencedPos);
            List<ITextComponent> namelist = null;
            ItemStack pick = state.getBlock().getItem(Minecraft.getInstance().world, ClientProxy.refrencedPos, state);
            try {
                namelist = pick.getTooltip(Minecraft.getInstance().player, ITooltipFlag.TooltipFlags.NORMAL);
            } catch (Throwable ignored) {
            }
            if (namelist != null && !namelist.isEmpty()) {
                return namelist.get(0).getFormattedText();
            }
        }
        return "";
    }

    private boolean isNoInventoryAtPos() {
        BlockPos pos = ClientProxy.refrencedPos;
        if (pos != null) {
            World worldIn = rat.world;
            return worldIn.getTileEntity(pos) == null;
        }
        return true;
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
