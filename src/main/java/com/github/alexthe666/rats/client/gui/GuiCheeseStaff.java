package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.ClientProxy;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.message.MessageCheeseStaffSync;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiCheeseStaff extends GuiScreen {

    protected static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/gui/cheese_staff.png");
    private EntityRat rat;

    public GuiCheeseStaff(EntityRat rat) {
        super();
        this.rat = rat;
        initGui();
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
        int i = (this.width) / 2;
        int j = (this.height - 166) / 2;
        String topText = I18n.format("entity.rat.staff.mark_block_deposit", getPosName()) + " " + I18n.format("rats.direction." + ClientProxy.refrencedFacing.getName());
        int maxLength = Math.max(150, Minecraft.getMinecraft().fontRenderer.getStringWidth(topText) + 20);
        this.buttonList.add(new GuiButton(0, i - maxLength / 2, j + 60, maxLength, 20, topText));
        this.buttonList.add(new GuiButton(1, i - maxLength / 2, j + 85, maxLength, 20, I18n.format("entity.rat.staff.mark_block_pickup", getPosName())));
        this.buttonList.add(new GuiButton(2, i - maxLength / 2, j + 110, maxLength, 20, I18n.format("entity.rat.staff.set_home_point", getPosName())));
        this.buttonList.add(new GuiButton(3, i - maxLength / 2, j + 135, maxLength, 20, I18n.format("entity.rat.staff.un_set_home_point")));
        this.buttonList.get(0).enabled = !isNoInventoryAtPos();
        this.buttonList.get(1).enabled = !isNoInventoryAtPos();
        this.buttonList.get(2).enabled = !ClientProxy.refrencedPos.equals(rat.getHomePosition()) || !rat.hasHome();
        this.buttonList.get(3).enabled = rat.hasHome();
    }

    private String getPosName() {
        if (ClientProxy.refrencedPos != null) {
            IBlockState state = rat.world.getBlockState(ClientProxy.refrencedPos);
            List<String> namelist = null;
            ItemStack pick = state.getBlock().getItem(Minecraft.getMinecraft().world, ClientProxy.refrencedPos, state);
            try {
                namelist = pick.getTooltip(Minecraft.getMinecraft().player, ITooltipFlag.TooltipFlags.NORMAL);
            } catch (Throwable ignored) {
            }
            if(namelist != null && !namelist.isEmpty()){
                return namelist.get(0);
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
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
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
        if (button.enabled && button.id == 0) {//deposit
            BlockPos pos = ClientProxy.refrencedPos;
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageCheeseStaffSync(rat.getEntityId(), pos, ClientProxy.refrencedFacing, 0));
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
        if (button.enabled && button.id == 1) {//pickup
            BlockPos pos = ClientProxy.refrencedPos;
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageCheeseStaffSync(rat.getEntityId(), pos, EnumFacing.UP, 1));
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
        if (button.enabled && button.id == 2) {//homepoint
            BlockPos pos = ClientProxy.refrencedPos;
            rat.setHomePosAndDistance(pos, 32);
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageCheeseStaffSync(rat.getEntityId(), pos, EnumFacing.UP, 2));
        }
        if (button.enabled && button.id == 3) {//unset homepoint
            BlockPos pos = ClientProxy.refrencedPos;
            rat.detachHome();
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageCheeseStaffSync(rat.getEntityId(), pos, EnumFacing.UP, 3));
        }
        initGui();
    }


}
