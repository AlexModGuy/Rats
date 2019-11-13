package com.github.alexthe666.rats.client;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.gui.GuiCheeseStaff;
import com.github.alexthe666.rats.client.gui.GuiRat;
import com.github.alexthe666.rats.client.model.*;
import com.github.alexthe666.rats.client.particle.*;
import com.github.alexthe666.rats.client.render.NuggetColorRegister;
import com.github.alexthe666.rats.client.render.RenderNothing;
import com.github.alexthe666.rats.client.render.entity.*;
import com.github.alexthe666.rats.client.render.tile.*;
import com.github.alexthe666.rats.server.CommonProxy;
import com.github.alexthe666.rats.server.blocks.*;
import com.github.alexthe666.rats.server.compat.TinkersCompatBridge;
import com.github.alexthe666.rats.server.entity.*;
import com.github.alexthe666.rats.server.entity.tile.*;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelDynBucket;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Map;

@Mod.EventBusSubscriber
public class ClientProxy extends CommonProxy {
    @SideOnly(Side.CLIENT)
    private static final RatsTEISR TEISR = new RatsTEISR();
    @SideOnly(Side.CLIENT)
    private static final ModelChefToque MODEL_CHEF_TOQUE = new ModelChefToque(1.0F);
    public static BlockPos refrencedPos;
    public static EnumFacing refrencedFacing;
    protected static EntityRat refrencedRat;
    public static final ModelResourceLocation RAT_NUGGET_MODEL = new ModelResourceLocation(new ResourceLocation(RatsMod.MODID, "rat_nugget_ore"), "inventory");

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomStateMapper(RatsBlockRegistry.RAT_TRAP, (new StateMap.Builder()).ignore(BlockRatTrap.FACING).build());
        ModelLoader.setCustomStateMapper(RatsBlockRegistry.AUTO_CURDLER, (new StateMap.Builder()).ignore(BlockAutoCurdler.FACING).build());
        ModelLoader.setCustomStateMapper(RatsBlockRegistry.RAT_HOLE, (new StateMap.Builder()).ignore(BlockRatHole.NORTH, BlockRatHole.EAST, BlockRatHole.SOUTH, BlockRatHole.WEST).build());
        ModelLoader.setCustomStateMapper(RatsBlockRegistry.RAT_CAGE_DECORATED, (new StateMap.Builder()).ignore(BlockRatCageDecorated.FACING).build());
        ModelLoader.setCustomStateMapper(RatsBlockRegistry.RAT_CAGE_BREEDING_LANTERN, (new StateMap.Builder()).ignore(BlockRatCageBreedingLantern.FACING).build());

        for (int i = 0; i < 16; i++) {
            ModelLoader.setCustomStateMapper(RatsBlockRegistry.RAT_TUBE_COLOR[i], (new StateMapperGeneric("rat_tube")));
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(RatsBlockRegistry.RAT_TUBE_COLOR[i]), 0, new ModelResourceLocation("rats:rat_tube", "inventory"));
            ModelLoader.setCustomModelResourceLocation(RatsItemRegistry.RAT_IGLOOS[i], 0, new ModelResourceLocation("rats:rat_igloo", "inventory"));
            ModelLoader.setCustomModelResourceLocation(RatsItemRegistry.RAT_HAMMOCKS[i], 0, new ModelResourceLocation("rats:rat_hammock", "inventory"));
        }
        ModelLoader.setCustomMeshDefinition(RatsItemRegistry.RAT_NUGGET_ORE, stack -> RAT_NUGGET_MODEL);
        ModelBakery.registerItemVariants(RatsItemRegistry.RAT_NUGGET_ORE, RAT_NUGGET_MODEL);
        try {
            for (Field f : RatsBlockRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Block && !(obj instanceof ICustomRendered)) {
                    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock((Block) obj), 0, new ModelResourceLocation("rats:" + ((Block) obj).getRegistryName().getPath(), "inventory"));
                } else if (obj instanceof Block[]) {
                    for (Block block : (Block[]) obj) {
                        if (!(block instanceof ICustomRendered)) {
                            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation("rats:" + block.getRegistryName().getPath(), "inventory"));
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            for (Field f : RatsItemRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Item && !(obj instanceof ICustomRendered)) {
                    ModelLoader.setCustomModelResourceLocation((Item) obj, 0, new ModelResourceLocation("rats:" + ((Item) obj).getRegistryName().getPath(), "inventory"));
                } else if (obj instanceof Item[]) {
                    for (Item item : (Item[]) obj) {
                        if (!(item instanceof ICustomRendered)) {
                            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation("rats:" + item.getRegistryName().getPath(), "inventory"));
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void preInit() {
        TinkersCompatBridge.loadTinkersClientCompat();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(new com.github.alexthe666.rats.client.event.ClientEvents());
        RenderingRegistry.registerEntityRenderingHandler(EntityRat.class, new RenderRat());
        RenderingRegistry.registerEntityRenderingHandler(EntityIllagerPiper.class, new RenderIllagerPiper());
        RenderingRegistry.registerEntityRenderingHandler(EntityRatlanteanSpirit.class, new RenderRatlateanSpirit());
        RenderingRegistry.registerEntityRenderingHandler(EntityRatlanteanFlame.class, new RenderRatlanteanFlame());
        RenderingRegistry.registerEntityRenderingHandler(EntityMarbleCheeseGolem.class, new RenderMarbledCheeseGolem());
        RenderingRegistry.registerEntityRenderingHandler(EntityGolemBeam.class, new RenderGolemBeam());
        RenderingRegistry.registerEntityRenderingHandler(EntityFeralRatlantean.class, new RenderFeralRatlantean());
        RenderingRegistry.registerEntityRenderingHandler(EntityNeoRatlantean.class, new RenderNeoRatlantean());
        RenderingRegistry.registerEntityRenderingHandler(EntityLaserBeam.class, new RenderLaserBeam());
        RenderingRegistry.registerEntityRenderingHandler(EntityLaserPortal.class, new RenderLaserPortal());
        RenderingRegistry.registerEntityRenderingHandler(EntityThrownBlock.class, new RenderThrownBlock());
        RenderingRegistry.registerEntityRenderingHandler(EntityVialOfSentience.class, new RenderPotion(Minecraft.getMinecraft().getRenderManager(), Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityPiratBoat.class, new RenderPiratBoat());
        RenderingRegistry.registerEntityRenderingHandler(EntityCheeseCannonball.class, new RenderSnowball<EntityCheeseCannonball>(Minecraft.getMinecraft().getRenderManager(), RatsItemRegistry.CHEESE_CANNONBALL, Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityPirat.class, new RenderPirat());
        RenderingRegistry.registerEntityRenderingHandler(EntityPlagueDoctor.class, new RenderPlagueDoctor());
        RenderingRegistry.registerEntityRenderingHandler(EntityPurifyingLiquid.class, new RenderPotion(Minecraft.getMinecraft().getRenderManager(), Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityBlackDeath.class, new RenderBlackDeath());
        RenderingRegistry.registerEntityRenderingHandler(EntityPlagueCloud.class, new RenderRatlateanSpirit());
        RenderingRegistry.registerEntityRenderingHandler(EntityPlagueBeast.class, new RenderPlagueBeast());
        RenderingRegistry.registerEntityRenderingHandler(EntityPlagueShot.class, new RenderPlagueShot());
        RenderingRegistry.registerEntityRenderingHandler(EntityRatCaptureNet.class, new RenderPotion(Minecraft.getMinecraft().getRenderManager(), Minecraft.getMinecraft().getRenderItem()));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRatHole.class, new RenderRatHole());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRatTrap.class, new RenderRatTrap());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAutoCurdler.class, new RenderAutoCurdler());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRatlantisPortal.class, new RenderRatlantisPortal());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRatCageDecorated.class, new RenderRatCageDecorated());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRatCageBreedingLantern.class, new RenderRatCageDecorated());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityUpgradeCombiner.class, new RenderUpgradeCombiner());
        Item.getItemFromBlock(RatsBlockRegistry.RAT_HOLE).setTileEntityItemStackRenderer(TEISR);
        Item.getItemFromBlock(RatsBlockRegistry.RAT_TRAP).setTileEntityItemStackRenderer(TEISR);
        Item.getItemFromBlock(RatsBlockRegistry.AUTO_CURDLER).setTileEntityItemStackRenderer(TEISR);
        Item.getItemFromBlock(RatsBlockRegistry.RATLANTIS_PORTAL).setTileEntityItemStackRenderer(TEISR);
        Item.getItemFromBlock(RatsBlockRegistry.UPGRADE_COMBINER).setTileEntityItemStackRenderer(TEISR);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor() {
            @Override
            public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
                return worldIn != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos) : ColorizerFoliage.getFoliageColorBasic();
            }
        }, RatsBlockRegistry.MARBLED_CHEESE_GRASS);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
            @Override
            public int colorMultiplier(ItemStack stack, int tintIndex) {
                IBlockState state = ((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
                return Minecraft.getMinecraft().getBlockColors().colorMultiplier(state, null, null, tintIndex);
            }
        }, RatsBlockRegistry.MARBLED_CHEESE_GRASS);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor() {
            @Override
            public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
                Block block = state.getBlock();
                int meta = 0;
                for (int i = 0; i < RatsBlockRegistry.RAT_TUBE_COLOR.length; i++) {
                    if (block == RatsBlockRegistry.RAT_TUBE_COLOR[i]) {
                        meta = i;
                    }
                }
                EnumDyeColor color = EnumDyeColor.byMetadata(meta);
                return color.getColorValue();
            }
        }, RatsBlockRegistry.RAT_TUBE_COLOR);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
            @Override
            public int colorMultiplier(ItemStack stack, int tintIndex) {
                Block block = Block.getBlockFromItem(stack.getItem());
                int meta = 0;
                for (int i = 0; i < RatsBlockRegistry.RAT_TUBE_COLOR.length; i++) {
                    if (block == RatsBlockRegistry.RAT_TUBE_COLOR[i]) {
                        meta = i;
                    }
                }
                EnumDyeColor color = EnumDyeColor.byMetadata(meta);
                return color.getColorValue();
            }
        }, RatsBlockRegistry.RAT_TUBE_COLOR);

        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
            @Override
            public int colorMultiplier(ItemStack stack, int tintIndex) {
                int meta = 0;
                for (int i = 0; i < RatsItemRegistry.RAT_IGLOOS.length; i++) {
                    if (stack.getItem() == RatsItemRegistry.RAT_IGLOOS[i]) {
                        meta = i;
                    }
                }
                EnumDyeColor color = EnumDyeColor.byMetadata(meta);
                return color.getColorValue();
            }
        }, RatsItemRegistry.RAT_IGLOOS);

        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
            @Override
            public int colorMultiplier(ItemStack stack, int tintIndex) {
                if (tintIndex == 0) {
                    int meta = 0;
                    for (int i = 0; i < RatsItemRegistry.RAT_HAMMOCKS.length; i++) {
                        if (stack.getItem() == RatsItemRegistry.RAT_HAMMOCKS[i]) {
                            meta = i;
                        }
                    }
                    EnumDyeColor color = EnumDyeColor.byMetadata(meta);
                    return color.getColorValue();
                } else {
                    return -1;
                }

            }
        }, RatsItemRegistry.RAT_HAMMOCKS);

        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
            @Override
            public int colorMultiplier(ItemStack stack, int tintIndex) {
                if (tintIndex == 1) {
                    return NuggetColorRegister.getNuggetColor(stack);
                } else {
                    return -1;
                }
            }
        }, RatsItemRegistry.RAT_NUGGET_ORE);
        ModelBakery.registerItemVariants(RatsItemRegistry.RAT_SACK, new ResourceLocation("iceandfire:rat_sack"), new ResourceLocation("iceandfire:rat_sack_1"), new ResourceLocation("iceandfire:rat_sack_2"), new ResourceLocation("iceandfire:rat_sack_3"));
    }

    @SideOnly(Side.CLIENT)
    public void postInit() {
        for (Map.Entry<Class<? extends Entity>, Render<? extends Entity>> entry : Minecraft.getMinecraft().getRenderManager().entityRenderMap.entrySet()) {
            Render render = entry.getValue();
            if (render instanceof RenderLivingBase && EntityLivingBase.class.isAssignableFrom(entry.getKey())) {
                ((RenderLivingBase) render).addLayer(new LayerPlague((RenderLivingBase) render));
            }
        }
        for(Map.Entry<String, RenderPlayer> entry :  Minecraft.getMinecraft().getRenderManager().getSkinMap().entrySet()){
            RenderPlayer render = entry.getValue();
            render.addLayer(new LayerPlague(render));
        }
        Field renderingRegistryField = ReflectionHelper.findField(RenderingRegistry.class, ObfuscationReflectionHelper.remapFieldNames(RenderingRegistry.class.getName(), "INSTANCE", "INSTANCE"));
        Field entityRendersField = ReflectionHelper.findField(RenderingRegistry.class, ObfuscationReflectionHelper.remapFieldNames(RenderingRegistry.class.getName(), "entityRenderers", "entityRenderers"));
        Field entityRendersOldField = ReflectionHelper.findField(RenderingRegistry.class, ObfuscationReflectionHelper.remapFieldNames(RenderingRegistry.class.getName(), "entityRenderersOld", "entityRenderersOld"));
        RenderingRegistry registry = null;
        try {
            Field modifier = Field.class.getDeclaredField("modifiers");
            modifier.setAccessible(true);
            registry = (RenderingRegistry) renderingRegistryField.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (registry != null) {
            Map<Class<? extends Entity>, IRenderFactory<? extends Entity>> entityRenders = null;
            Map<Class<? extends Entity>, Render<? extends Entity>> entityRendersOld = null;
            try {
                Field modifier1 = Field.class.getDeclaredField("modifiers");
                modifier1.setAccessible(true);
                entityRenders = (Map<Class<? extends Entity>, IRenderFactory<? extends Entity>>) entityRendersField.get(registry);
                entityRendersOld = (Map<Class<? extends Entity>, Render<? extends Entity>>) entityRendersOldField.get(registry);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (entityRenders != null) {
                for (Map.Entry<Class<? extends Entity>, IRenderFactory<? extends Entity>> entry : entityRenders.entrySet()) {
                    if (entry.getValue() != null) {
                        try {
                            Render render = entry.getValue().createRenderFor(Minecraft.getMinecraft().getRenderManager());
                            if (render != null && render instanceof RenderLivingBase && EntityLivingBase.class.isAssignableFrom(entry.getKey())) {
                                ((RenderLivingBase) render).addLayer(new LayerPlague((RenderLivingBase)render));
                            }
                        } catch (NullPointerException exp) {
                            RatsMod.logger.warn("Rats: Could not apply plague render layer to " + entry.getKey().getSimpleName() + ", someone isn't registering their renderer properly... <.<");
                        }
                    }

                }
            }
            if (entityRendersOld != null) {
                for (Map.Entry<Class<? extends Entity>, Render<? extends Entity>> entry : entityRendersOld.entrySet()) {
                    Render render = entry.getValue();
                    if (render instanceof RenderLivingBase && EntityLivingBase.class.isAssignableFrom(entry.getKey())) {
                        ((RenderLivingBase) render).addLayer(new LayerPlague((RenderLivingBase)render));
                    }
                }
            }
        }
    }

    public boolean shouldRenderNameplates() {
        return Minecraft.getMinecraft().currentScreen == null || !(Minecraft.getMinecraft().currentScreen instanceof GuiRat) && !(Minecraft.getMinecraft().currentScreen instanceof GuiCheeseStaff);
    }

    @SideOnly(Side.CLIENT)
    public Object getArmorModel(int index) {
        if (index == 0) {
            return new ModelChefToque(1.0F);
        } else if (index == 1) {
            return new ModelPiperHat(1.0F);
        } else if (index == 2) {
            return new ModelPiratHat(1.0F);
        } else if (index == 3) {
            return new ModelArcheologistHat(1.0F);
        } else if (index == 4){
            return new ModelFarmerHat(1.0F);
       } else if (index == 5){
            return new ModelPlagueDoctorMask(1.0F);
        } else if (index == 6) {
            return new ModelRatFez(1.0F);
        } else if (index == 7) {
            return new ModelTopHat(1.0F);
        }else {
            return new ModelSantaHat(1.0F);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void openCheeseStaffGui() {
        if (refrencedRat != null) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiCheeseStaff(refrencedRat));
        }
    }

    @Override
    public EntityRat getRefrencedRat() {
        return refrencedRat;
    }

    @Override
    public void setRefrencedRat(EntityRat rat) {
        refrencedRat = rat;
    }

    @Override
    public void setCheeseStaffContext(BlockPos pos, EnumFacing facing) {
        refrencedPos = pos;
        refrencedFacing = facing;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void spawnParticle(String name, double x, double y, double z, double motX, double motY, double motZ) {
        World world = Minecraft.getMinecraft().world;
        if (world == null) {
            return;
        }
        if (name.equals("rat_ghost")) {
            Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleRatGhost(world, x, y, z, (float) motX, (float) motY, (float) motZ));
        }
        if (name.equals("rat_lightning")) {
            Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleLightning(world, x, y, z, (float) motX, (float) motY, (float) motZ));
        }
        if (name.equals("flea")) {
            Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleFlea(world, x, y, z, (float) motX, (float) motY, (float) motZ));
        }
        if (name.equals("upgrade_combiner")) {
            Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleUpgradeCombiner(world, x, y, z, (float) motX, (float) motY, (float) motZ));
        }
        if (name.equals("saliva")) {
            Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleSaliva(world, x, y, z));
        }
        if (name.equals("black_death")) {
            Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleBlackDeath(world, x, y, z, (float) motX, (float) motY, (float) motZ));
        }
    }
}
