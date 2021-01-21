package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.ClientProxy;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.message.MessageCheeseStaffSync;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
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

    protected void init() {
        super.init();
        this.buttons.clear();
        int i = (this.width) / 2;
        int j = (this.height - 166) / 2;
        IFormattableTextComponent topText = new TranslationTextComponent("entity.rats.rat.staff.mark_block_deposit", getPosName()).append(new StringTextComponent(" ").append(new TranslationTextComponent("rats.direction." + ClientProxy.refrencedFacing.getName2())));
        int maxLength = Math.max(150, Minecraft.getInstance().fontRenderer.getStringWidth(topText.getString()) + 20);
        this.addButton(new Button(i - maxLength / 2, j + 60, maxLength, 20, topText, (p_214132_1_) -> {
            BlockPos pos = ClientProxy.refrencedPos;
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageCheeseStaffSync(rat.getEntityId(), pos, ClientProxy.refrencedFacing, 0));
            Minecraft.getInstance().displayGuiScreen(null);
            init();
        }));
        this.addButton(new Button(i - maxLength / 2, j + 85, maxLength, 20, new TranslationTextComponent("entity.rats.rat.staff.mark_block_pickup", getPosName()), (p_214132_1_) -> {
            BlockPos pos = ClientProxy.refrencedPos;
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageCheeseStaffSync(rat.getEntityId(), pos, Direction.UP, 1));
            Minecraft.getInstance().displayGuiScreen(null);
            init();
        }));
        this.addButton(new Button(i - maxLength / 2, j + 110, maxLength, 20, new TranslationTextComponent("entity.rats.rat.staff.set_home_point", getPosName()), (p_214132_1_) -> {
            BlockPos pos = ClientProxy.refrencedPos;
            rat.setHomePosAndDistance(pos, 32);
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageCheeseStaffSync(rat.getEntityId(), pos, Direction.UP, 2));
            init();
        }));
        this.addButton(new Button(i - maxLength / 2, j + 135, maxLength, 20, new TranslationTextComponent("entity.rats.rat.staff.un_set_home_point"), (p_214132_1_) -> {
            BlockPos pos = ClientProxy.refrencedPos;
            rat.setHomePosAndDistance(BlockPos.ZERO, -1);
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageCheeseStaffSync(rat.getEntityId(), pos, Direction.UP, 3));
            init();
        }));
        this.addButton(new Button(i - maxLength / 2, j + 160, maxLength, 20, new TranslationTextComponent("entity.rats.rat.staff.un_set_transport_pos"), (p_214132_1_) -> {
            BlockPos pos = ClientProxy.refrencedPos;
            rat.setPickupPos(null);
            rat.setDepositPos(null);
            RatsMod.NETWORK_WRAPPER.sendToServer(new MessageCheeseStaffSync(rat.getEntityId(), pos, Direction.UP, 7));
            init();
        }));
        this.buttons.get(0).visible = !isNoInventoryAtPos();
        this.buttons.get(1).visible = !isNoInventoryAtPos();
        this.buttons.get(2).visible = !ClientProxy.refrencedPos.equals(rat.getHomePosition()) || !rat.detachHome();
        this.buttons.get(3).visible = rat.detachHome();
        this.buttons.get(4).visible = rat.getDepositPos() != null || rat.getPickupPos() != null;
    }

    public boolean isPauseScreen() {
        return false;
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
                return namelist.get(0).getString();
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
    public void render(MatrixStack stack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        if (getMinecraft() != null) {
            try {
                this.renderBackground(stack);
            } catch (Exception e) {

            }
        }
        super.render(stack, p_230430_2_, p_230430_3_, p_230430_4_);
        int i = (this.width - 248) / 2 + 10;
        int j = (this.width - 166) / 2 + 8;
        if(this.rat != null){
            drawEntityOnScreen(i + 114, j + 40, 70, 0, 0, this.rat);
        }

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
    }

    public boolean func_231177_au__() {
        return false;
    }
}
