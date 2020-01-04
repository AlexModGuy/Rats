package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.ClientProxy;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.message.MessageCheeseStaffSync;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
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

    public void init() {
        super.init();
        this.buttons.clear();
        int i = (this.width) / 2;
        int j = (this.height - 166) / 2;
        String topText = I18n.format("entity.rats.rat.staff.mark_block_deposit", getPosName()) + " " + I18n.format("rats.direction." + ClientProxy.refrencedFacing.getName());
        int maxLength = Math.max(150, font.getStringWidth(topText) + 20);
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

    public boolean doesGuiPauseGame() {
        return false;
    }
}
