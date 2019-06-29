package com.github.alexthe666.rats.server.potion;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

public class PotionPlague extends Potion {

    private static final Potion[] NEGATIVE_EFFECTS = new Potion[]{MobEffects.WITHER, MobEffects.POISON, MobEffects.HUNGER, MobEffects.BLINDNESS, MobEffects.MINING_FATIGUE, MobEffects.NAUSEA, MobEffects.SLOWNESS, MobEffects.UNLUCK, MobEffects.WEAKNESS};
    public PotionPlague() {
        super(false, 0X445637);
        this.setRegistryName(RatsMod.MODID, "plague");
    }

    public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {
        if(entityLivingBaseIn.ticksExisted % 300 / (amplifier + 1) == 0){
            entityLivingBaseIn.addPotionEffect(new PotionEffect(NEGATIVE_EFFECTS[(int)(Math.random() * NEGATIVE_EFFECTS.length)], 300 * (amplifier + 1), 0));
        }
    }

    public boolean isReady(int duration, int amplifier) {
        return duration > 0;
    }

    public String getName() {
        return "rats.plague";
    }


    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc) {
        mc.getTextureManager().bindTexture(PotionConfitByaldi.TEXTURE);
        this.drawTexturedModalRect(x + 6, y + 7, 18, 0, 18, 18);
    }


    @SideOnly(Side.CLIENT)
    public void renderHUDEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc, float alpha) {
        mc.getTextureManager().bindTexture(PotionConfitByaldi.TEXTURE);
        this.drawTexturedModalRect(x + 3, y + 3, 18, 0, 18, 18);

    }

    @SideOnly(Side.CLIENT)
    private void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double) (x + 0), (double) (y + height), (double) 200).tex((double) ((float) (textureX + 0) * 0.00390625F), (double) ((float) (textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double) (x + width), (double) (y + height), (double) 200).tex((double) ((float) (textureX + width) * 0.00390625F), (double) ((float) (textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double) (x + width), (double) (y + 0), (double) 200).tex((double) ((float) (textureX + width) * 0.00390625F), (double) ((float) (textureY + 0) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double) (x + 0), (double) (y + 0), (double) 200).tex((double) ((float) (textureX + 0) * 0.00390625F), (double) ((float) (textureY + 0) * 0.00390625F)).endVertex();
        tessellator.draw();
    }

    public java.util.List<ItemStack> getCurativeItems(){
        return new ArrayList<>();
    }
}
