package com.github.alexthe666.rats.client.event;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.ClientProxy;
import com.github.alexthe666.rats.client.model.ModelRat;
import com.github.alexthe666.rats.client.render.entity.LayerPlague;
import com.github.alexthe666.rats.client.render.entity.RenderRat;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ClientEvents {
    public static final ResourceLocation PLAGUE_HEART_TEXTURE = new ResourceLocation("rats:textures/gui/plague_hearts.png");
    private static final ResourceLocation SYNESTHESIA = new ResourceLocation("rats:shaders/post/synesthesia.json");
    private static final ResourceLocation RADIUS_TEXTURE = new ResourceLocation("rats:textures/blocks/rat_radius.png");
    private static final ModelRat MAIN_MENU_RAT = new ModelRat(0.0F);
    private static final ResourceLocation MAIN_MENU_RAT_TEXTURE = new ResourceLocation("rats:textures/entity/rat/rat_blue.png");
    public static int left_height = 39;
    public static int right_height = 39;
    private int updateCounter = 0;
    private int playerHealth = 0;
    private int lastPlayerHealth = 0;
    private long healthUpdateCounter = 0;
    private long lastSystemTime = 0;
    private Random rand = new Random();
    private float synesthesiaProgress = 0;
    private float prevSynesthesiaProgress = 0;
    private float maxSynesthesiaProgress = 40;
    private int ticksMenuExisted = 0;
    private float prevRatXPosition = 0;
    private float ratXPosition = 0;

    public static void renderMovingAABB(AxisAlignedBB boundingBox, float partialTicks) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        float f3 = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F;

        double maxX = boundingBox.maxX * 0.125F;
        double minX = boundingBox.minX * 0.125F;
        double maxY = boundingBox.maxY * 0.125F;
        double minY = boundingBox.minY * 0.125F;
        double maxZ = boundingBox.maxZ * 0.125F;
        double minZ = boundingBox.minZ * 0.125F;
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(f3 + minX - maxX, f3 + maxY - minY).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(f3 + maxX - minX, f3 + maxY - minY).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(f3 + maxX - minX, f3 + minY - maxY).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(f3 + minX - maxX, f3 + minY - maxY).normal(0.0F, 0.0F, -1.0F).endVertex();

        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(f3 + minX - maxX, f3 + minY - maxY).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(f3 + maxX - minX, f3 + minY - maxY).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(f3 + maxX - minX, f3 + maxY - minY).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(f3 + minX - maxX, f3 + maxY - minY).normal(0.0F, 0.0F, 1.0F).endVertex();

        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(f3 + minX - maxX, f3 + minY - maxY).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(f3 + maxX - minX, f3 + minY - maxY).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(f3 + maxX - minX, f3 + maxZ - minZ).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(f3 + minX - maxX, f3 + maxZ - minZ).normal(0.0F, -1.0F, 0.0F).endVertex();

        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(f3 + minX - maxX, f3 + minY - maxY).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(f3 + maxX - minX, f3 + minY - maxY).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(f3 + maxX - minX, f3 + maxZ - minZ).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(f3 + minX - maxX, f3 + maxZ - minZ).normal(0.0F, 1.0F, 0.0F).endVertex();

        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(f3 + minX - maxX, f3 + minY - maxY).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(f3 + minX - maxX, f3 + maxY - minY).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(f3 + maxX - minX, f3 + maxY - minY).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(f3 + maxX - minX, f3 + minY - maxY).normal(-1.0F, 0.0F, 0.0F).endVertex();

        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(f3 + minX - maxX, f3 + minY - maxY).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(f3 + minX - maxX, f3 + maxY - minY).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(f3 + maxX - minX, f3 + maxY - minY).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(f3 + maxX - minX, f3 + minY - maxY).normal(1.0F, 0.0F, 0.0F).endVertex();
        tessellator.draw();
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPlayerInteract(RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HEALTH || event.isCanceled() || !Minecraft.getMinecraft().player.isPotionActive(RatsMod.PLAGUE_POTION) || !RatsMod.CONFIG_OPTIONS.plagueHearts) {
            return;
        }
        left_height = 39;
        ScaledResolution resolution = event.getResolution();
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();
        GlStateManager.enableBlend();
        EntityPlayer player = (EntityPlayer) Minecraft.getMinecraft().getRenderViewEntity();
        int health = MathHelper.ceil(player.getHealth());
        boolean highlight = healthUpdateCounter > (long) updateCounter && (healthUpdateCounter - (long) updateCounter) / 3L % 2L == 1L;

        if (health < this.playerHealth && player.hurtResistantTime > 0) {
            this.lastSystemTime = Minecraft.getSystemTime();
            this.healthUpdateCounter = this.updateCounter + 20;
        } else if (health > this.playerHealth && player.hurtResistantTime > 0) {
            this.lastSystemTime = Minecraft.getSystemTime();
            this.healthUpdateCounter = this.updateCounter + 10;
        }

        if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L) {
            this.playerHealth = health;
            this.lastPlayerHealth = health;
            this.lastSystemTime = Minecraft.getSystemTime();
        }

        this.playerHealth = health;
        int healthLast = this.lastPlayerHealth;

        IAttributeInstance attrMaxHealth = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
        float healthMax = (float) attrMaxHealth.getAttributeValue();
        float absorb = MathHelper.ceil(player.getAbsorptionAmount());

        int healthRows = MathHelper.ceil((healthMax + absorb) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (healthRows - 2), 3);

        this.rand.setSeed(updateCounter * 312871);

        int left = width / 2 - 91;
        int top = height - left_height;
        left_height += (healthRows * rowHeight);
        if (rowHeight != 10) left_height += 10 - rowHeight;

        int regen = -1;
        if (player.isPotionActive(MobEffects.REGENERATION)) {
            regen = updateCounter % 25;
        }

        final int TOP = 0;
        int MARGIN = 0;
        int BACKGROUND = 9;
        float absorbRemaining = absorb;

        for (int i = MathHelper.ceil((healthMax + absorb) / 2.0F) - 1; i >= 0; --i) {
            //int b0 = (highlight ? 1 : 0);
            int row = MathHelper.ceil((float) (i + 1) / 10.0F) - 1;
            int x = left + i % 10 * 8;
            int y = top - row * rowHeight;

            if (health <= 4) y += rand.nextInt(2);
            if (i == regen) y -= 2;
            drawTexturedModalRect(x, y, MARGIN, BACKGROUND, 9, 9);
            if (highlight) {
                if (i * 2 + 1 < healthLast)
                    drawTexturedModalRect(x, y, MARGIN, TOP, 9, 9); //6
                else if (i * 2 + 1 == healthLast)
                    drawTexturedModalRect(x, y, MARGIN + 9, TOP, 9, 9); //7
            }

            if (absorbRemaining > 0.0F) {
                if (absorbRemaining == absorb && absorb % 2.0F == 1.0F) {
                    drawTexturedModalRect(x, y, MARGIN, TOP, 9, 9); //17
                    absorbRemaining -= 1.0F;
                } else {
                    drawTexturedModalRect(x, y, MARGIN + 9, TOP, 9, 9); //16
                    absorbRemaining -= 2.0F;
                }
            } else {
                if (i * 2 + 1 < health)
                    drawTexturedModalRect(x, y, MARGIN, TOP, 9, 9); //4
                else if (i * 2 + 1 == health)
                    drawTexturedModalRect(x, y, MARGIN + 9, TOP, 9, 9); //5
            }
        }

        GlStateManager.disableBlend();
        Minecraft.getMinecraft().profiler.endSection();
    }

    private void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(PLAGUE_HEART_TEXTURE);
        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x, y, textureX, textureY, width, height);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() == Minecraft.getMinecraft().player) {
            EntityRenderer renderer = Minecraft.getMinecraft().entityRenderer;
            PotionEffect active = event.getEntityLiving().getActivePotionEffect(RatsMod.CONFIT_BYALDI_POTION);
            boolean synesthesia = active != null;
            if (synesthesia && !renderer.isShaderActive()) {
                renderer.loadShader(SYNESTHESIA);
            }
            if (!synesthesia && renderer.isShaderActive() && renderer != null && renderer.getShaderGroup() != null && renderer.getShaderGroup().getShaderGroupName() != null && SYNESTHESIA.toString().equals(renderer.getShaderGroup().getShaderGroupName())) {
                renderer.stopUseShader();
            }
            if (prevSynesthesiaProgress == 2 && synesthesia) {
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(RatsSoundRegistry.POTION_EFFECT_BEGIN, 1.0F));
            }
            if (prevSynesthesiaProgress == 38 && !synesthesia) {
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(RatsSoundRegistry.POTION_EFFECT_END, 1.0F));
            }
            prevSynesthesiaProgress = synesthesiaProgress;
            if (synesthesia && synesthesiaProgress < maxSynesthesiaProgress) {
                synesthesiaProgress += 2F;
            } else if (!synesthesia && synesthesiaProgress > 0.0F) {
                synesthesiaProgress -= 2F;
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onGetFOVModifier(EntityViewRenderEvent.FOVModifier event) {
        if (event.getEntity() == Minecraft.getMinecraft().player && prevSynesthesiaProgress > 0) {
            float prog = (prevSynesthesiaProgress + (synesthesiaProgress - prevSynesthesiaProgress) * LLibrary.PROXY.getPartialTicks());
            float renderProg;
            if (prevSynesthesiaProgress > synesthesiaProgress) {
                renderProg = (float) Math.sin(prog / maxSynesthesiaProgress * Math.PI) * 40F;
            } else {
                renderProg = -(float) Math.sin(prog / maxSynesthesiaProgress * Math.PI) * 40F;
            }
            event.setFOV(event.getFOV() + renderProg);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (RatsMod.CONFIG_OPTIONS.customMainMenu && Minecraft.getMinecraft().player.getHeldItem(EnumHand.MAIN_HAND).getItem() == RatsItemRegistry.RADIUS_STICK) {
            if (RatsMod.PROXY.getRefrencedRat() != null) {
                Vec3d renderCenter = new Vec3d(RatsMod.PROXY.getRefrencedRat().getSearchCenter()).add(0.5, 0.5, 0.5);
                double renderRadius = RatsMod.PROXY.getRefrencedRat().getSearchRadius();
                AxisAlignedBB aabb = new AxisAlignedBB(-renderRadius, -renderRadius, -renderRadius, renderRadius, renderRadius, renderRadius);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableNormalize();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                Minecraft.getMinecraft().getTextureManager().bindTexture(RADIUS_TEXTURE);
                GlStateManager.disableCull();
                GlStateManager.depthMask(false);
                GlStateManager.pushMatrix();
                Entity viewEntity = Minecraft.getMinecraft().getRenderViewEntity();
                double px = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * event.getPartialTicks();
                double py = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * event.getPartialTicks();
                double pz = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * event.getPartialTicks();
                GlStateManager.translate(-px, -py, -pz);
                GlStateManager.translate(renderCenter.x, renderCenter.y, renderCenter.z);
                renderMovingAABB(aabb, event.getPartialTicks());
                GlStateManager.popMatrix();
                GlStateManager.depthMask(true);
                GlStateManager.enableCull();
                GlStateManager.disableBlend();
                GlStateManager.disableNormalize();
            }
        }
    }

    private static final ResourceLocation CHEST_TEXTURE = new ResourceLocation("textures/entity/chest/normal.png");
    private static final ModelChest LEFT_CHEST_MODEL = new ModelChest();
    private static final ModelChest RIGHT_CHEST_MODEL = new ModelChest();
    private float leftChestAngle = 0;
    private float rightChestAngle = 0;

    @SubscribeEvent
    public void onGuiOpened(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (event.getGui() instanceof GuiMainMenu) {
            ticksMenuExisted++;
            float partialAndFullTicks = ticksMenuExisted - 1 + event.getRenderPartialTicks();
            float scale = 30;
            float chestScale = 45;
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            prevRatXPosition = ratXPosition;
            ratXPosition = (float) Math.sin(ticksMenuExisted * 0.025F) * 200;
            int renderRatPos = sr.getScaledWidth() / 2 + (int) (ratXPosition * 0.85F);
            int renderLeftChestPos = sr.getScaledWidth() / 2 - 200;
            int renderRightChestPos = sr.getScaledWidth() / 2 + 170;
            int j = event.getGui().height / 4 + 85;
            float z = 0;
            float rotation = prevRatXPosition > ratXPosition ? -90 : 90;
            if(ratXPosition > 150){
                rightChestAngle = (ratXPosition - 150) / 50F;
            }else{
                rightChestAngle = 0;
            }
            if(ratXPosition < -150){
                leftChestAngle = (Math.abs(ratXPosition) - 150) / 50F;
            }else{
                leftChestAngle = 0;
            }
            GlStateManager.enableDepth();
            GL11.glPushMatrix();
            GL11.glTranslatef(renderLeftChestPos, j + 5, 60);
            GL11.glPushMatrix();
            Minecraft.getMinecraft().getTextureManager().bindTexture(CHEST_TEXTURE);
            GL11.glScalef(-chestScale, chestScale, chestScale);
            GL11.glRotatef(135, 0, 1, 0);
            GL11.glRotatef(-15, 0, 0, 1);
            GL11.glRotatef(15, 1, 0, 0);
            LEFT_CHEST_MODEL.renderAll();
            LEFT_CHEST_MODEL.chestLid.rotateAngleX =  -(leftChestAngle * ((float)Math.PI / 2F));
            GL11.glPopMatrix();
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslatef(renderRightChestPos, j, 60);
            GL11.glPushMatrix();
            Minecraft.getMinecraft().getTextureManager().bindTexture(CHEST_TEXTURE);
            GL11.glScalef(-chestScale, chestScale, chestScale);
            GL11.glRotatef(-135, 0, 1, 0);
            GL11.glRotatef(15, 0, 0, 1);
            GL11.glRotatef(15, 1, 0, 0);
            RIGHT_CHEST_MODEL.renderAll();
            RIGHT_CHEST_MODEL.chestLid.rotateAngleX =  -(rightChestAngle * ((float)Math.PI / 2F));
            GL11.glPopMatrix();
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslatef(renderRatPos, j, 300.0F);
            GL11.glPushMatrix();
            Minecraft.getMinecraft().getTextureManager().bindTexture(MAIN_MENU_RAT_TEXTURE);
            GL11.glScalef(-scale, scale, scale);
            GL11.glRotatef(rotation, 0, 1, 0);
            MAIN_MENU_RAT.animateForMenu(partialAndFullTicks, 1);
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }
    }
}
