package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.ClientProxy;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.message.MessageSyncRatTag;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class GuiPatrolStaff extends Screen {
    private EntityRat rat;
    private List<BlockPos> nodes = new ArrayList<>();

    public GuiPatrolStaff(EntityRat rat) {
        super(new TranslationTextComponent("patrol_staff"));
        this.rat = rat;
        nodes.addAll(rat.patrolNodes);
        init();
    }

    protected void init() {
        super.init();
        this.buttons.clear();
        int i = (this.width) / 2;
        int j = (this.height - 166) / 2;
        BlockPos pos = ClientProxy.refrencedPos;
        IFormattableTextComponent addText = new TranslationTextComponent("entity.rats.rat.staff.add_patrol_node", blockPosString(pos));
        IFormattableTextComponent removeText = new TranslationTextComponent("entity.rats.rat.staff.remove_patrol_node", blockPosString(pos));
        IFormattableTextComponent removeAllText = new TranslationTextComponent("entity.rats.rat.staff.remove_all_patrol_nodes", blockPosString(pos));
        int maxLength = Math.max(150, Minecraft.getInstance().fontRenderer.getStringWidth(addText.getString()) + 20);
        int removeIndex = -1;
        for (int nodeIndex = 0; nodeIndex < nodes.size(); nodeIndex++) {
            if (pos.equals(nodes.get(nodeIndex))) {
                removeIndex = nodeIndex;
            }
        }
        if (removeIndex == -1) {
            this.addButton(new Button(i - maxLength / 2, j + 60, maxLength, 20, addText, (p_214132_1_) -> {
                nodes.add(pos);
                CompoundNBT tag = buildNewTag();
                rat.read(tag);
                RatsMod.NETWORK_WRAPPER.sendToServer(new MessageSyncRatTag(rat.getEntityId(), tag));
                Minecraft.getInstance().displayGuiScreen(null);
                init();
            }));
        } else {
            this.addButton(new Button(i - maxLength / 2, j + 60, maxLength, 20, removeText, (p_214132_1_) -> {
                nodes.remove(pos);
                CompoundNBT tag = buildNewTag();
                rat.read(tag);
                RatsMod.NETWORK_WRAPPER.sendToServer(new MessageSyncRatTag(rat.getEntityId(), tag));
                Minecraft.getInstance().displayGuiScreen(null);
                init();
            }));
        }
        if (nodes.size() > 0) {
            this.addButton(new Button(i - maxLength / 2, j + 110, maxLength, 20, removeAllText, (p_214132_1_) -> {
                nodes.clear();
                CompoundNBT tag = buildNewTag();
                rat.read(tag);
                RatsMod.NETWORK_WRAPPER.sendToServer(new MessageSyncRatTag(rat.getEntityId(), tag));
                Minecraft.getInstance().displayGuiScreen(null);
                init();
            }));
        }
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
        int j = (this.height - 166) / 2 + 8;
        if (this.rat != null) {
            GuiRadiusStaff.drawEntityOnScreen(i + 114, j + 40, 70, 0, 0, this.rat);
        }
    }

    private CompoundNBT buildNewTag() {
        CompoundNBT tag = new CompoundNBT();
        rat.writeAdditional(tag);
        ListNBT listnbt = new ListNBT();
        for (BlockPos lng : nodes) {
            listnbt.add(new IntArrayNBT(new int[]{lng.getX(), lng.getY(), lng.getZ()}));
        }
        tag.put("PatrolNodesTag", listnbt);
        return tag;
    }

    private String blockPosString(BlockPos pos) {
        return "" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
    }
}
