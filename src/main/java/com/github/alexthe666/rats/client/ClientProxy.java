package com.github.alexthe666.rats.client;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.gui.GuiCheeseStaff;
import com.github.alexthe666.rats.client.gui.GuiRadiusStaff;
import com.github.alexthe666.rats.client.gui.GuiRat;
import com.github.alexthe666.rats.client.gui.RatsGuiRegistry;
import com.github.alexthe666.rats.client.model.*;
import com.github.alexthe666.rats.client.particle.*;
import com.github.alexthe666.rats.client.render.NuggetColorRegister;
import com.github.alexthe666.rats.client.render.RenderNothing;
import com.github.alexthe666.rats.client.render.entity.*;
import com.github.alexthe666.rats.client.render.tile.*;
import com.github.alexthe666.rats.server.CommonProxy;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.*;
import com.github.alexthe666.rats.server.entity.tile.*;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GrassColors;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.Callable;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = RatsMod.MODID, value = Dist.CLIENT)
public class ClientProxy extends CommonProxy {
    @OnlyIn(Dist.CLIENT)
    private static final RatsTEISR TEISR = new RatsTEISR();
    @OnlyIn(Dist.CLIENT)
    private static final ModelChefToque MODEL_CHEF_TOQUE = new ModelChefToque(1.0F);
    public static BlockPos refrencedPos;
    public static Direction refrencedFacing;
    public static EntityRat refrencedRat;
    public static TileEntity refrencedTileEntity;
    public static ItemStack refrencedItem;

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
      /*  for (int i = 0; i < 16; i++) {
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
        }*/
    }

    @OnlyIn(Dist.CLIENT)
    public void preInit() {
        RenderTypeLookup.setRenderLayer(RatsBlockRegistry.RAT_CAGE, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(RatsBlockRegistry.RAT_CAGE_BREEDING_LANTERN, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(RatsBlockRegistry.RAT_CAGE_DECORATED, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(RatsBlockRegistry.RATGLOVE_FLOWER, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(RatsBlockRegistry.RAT_TUBE_COLOR, RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(RatsBlockRegistry.AUTO_CURDLER, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(RatsBlockRegistry.RAT_HOLE, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(RatsBlockRegistry.MANHOLE, RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(RatsBlockRegistry.TRASH_CAN, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(RatsBlockRegistry.RAT_ATTRACTOR, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(RatsBlockRegistry.MARBLED_CHEESE_DIRT, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(RatsBlockRegistry.MARBLED_CHEESE_GRASS, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(RatsBlockRegistry.AIR_RAID_SIREN, RenderType.getCutout());

    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void init() {
        RatsGuiRegistry.register();
        MinecraftForge.EVENT_BUS.register(new com.github.alexthe666.rats.client.event.ClientEvents());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientProxy::onBlockColors);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientProxy::onItemColors);
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.RAT, manager -> new RenderRat());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.RAT_SPAWNER, manager -> new RenderNothing());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.PIED_PIPER, manager -> new RenderIllagerPiper());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.RATLANTEAN_SPIRIT, manager -> new RenderRatlateanSpirit(false));
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.RATLANTEAN_FLAME, manager -> new SpriteRenderer(manager, Minecraft.getInstance().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.RATLANTEAN_AUTOMATON, manager -> new RenderMarbledCheeseGolem());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.RATLANTEAN_AUTOMATON_BEAM, manager -> new RenderGolemBeam());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.FERAL_RATLANTEAN, manager -> new RenderFeralRatlantean());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.NEO_RATLANTEAN, manager -> new RenderNeoRatlantean());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.LASER_BEAM, manager -> new RenderLaserBeam());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.LASER_PORTAL, manager -> new RenderLaserPortal());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.THROWN_BLOCK, manager -> new RenderThrownBlock());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.VIAL_OF_SENTIENCE, manager -> new SpriteRenderer(Minecraft.getInstance().getRenderManager(), Minecraft.getInstance().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.PIRAT_BOAT, manager -> new RenderPiratBoat());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.CHEESE_CANNONBALL, manager -> new SpriteRenderer(Minecraft.getInstance().getRenderManager(), Minecraft.getInstance().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.PLAGUE_DOCTOR, manager -> new RenderPlagueDoctor());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.PURIFYING_LIQUID, manager -> new SpriteRenderer(Minecraft.getInstance().getRenderManager(), Minecraft.getInstance().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.BLACK_DEATH, manager -> new RenderBlackDeath());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.PLAGUE_CLOUD, manager -> new RenderRatlateanSpirit(true));
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.PLAGUE_BEAST, manager -> new RenderPlagueBeast());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.PLAGUE_SHOT, manager -> new RenderPlagueShot());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.RAT_CAPTURE_NET, manager -> new SpriteRenderer(Minecraft.getInstance().getRenderManager(), Minecraft.getInstance().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.RAT_DRAGON_FIRE, manager -> new RenderNothing());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.RAT_ARROW, manager -> new RenderRatArrow());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.DUTCHRAT, manager -> new RenderDutchrat());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.DUTCHRAT_SWORD, manager -> new RenderDutchratSword());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.RATFISH, manager -> new RenderRatfish());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.RATTLING_GUN, manager -> new RenderRattlingGun());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.RATTLING_GUN_BULLET, manager -> new RenderRattlingGunBullet());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.RATLANTEAN_RATBOT, manager -> new RenderRatlanteanRatbot());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.RAT_MOUNT_GOLEM, manager -> new RenderRatGolemMount());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.RAT_MOUNT_CHICKEN, manager -> new RenderRatChickenMount());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.RAT_MOUNT_BEAST, manager -> new RenderRatBeastMount());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.PIRAT, manager -> new RenderPirat());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.RAT_MOUNT_AUTOMATON, manager -> new RenderRatAutomatonMount());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.GHOST_PIRAT, manager -> new RenderGhostPirat());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.RAT_KING, manager -> new RenderRatKing());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.RAT_SHOT, manager -> new RenderRatShot());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.RAT_BARON, manager -> new RenderRat());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.RAT_BARON_PLANE, manager -> new RenderRatBaronPlane());
        RenderingRegistry.registerEntityRenderingHandler(RatsEntityRegistry.RAT_MOUNT_BIPLANE, manager -> new RenderRatBiplaneMount());
        ClientRegistry.bindTileEntityRenderer(RatsTileEntityRegistry.RAT_HOLE, manager -> new RenderRatHole(manager));
        ClientRegistry.bindTileEntityRenderer(RatsTileEntityRegistry.RAT_TRAP, manager -> new RenderRatTrap(manager));
        ClientRegistry.bindTileEntityRenderer(RatsTileEntityRegistry.AUTO_CURDLER, manager -> new RenderAutoCurdler(manager));
        ClientRegistry.bindTileEntityRenderer(RatsTileEntityRegistry.RATLANTIS_PORTAL, manager -> new RenderRatlantisPortal(manager));
        ClientRegistry.bindTileEntityRenderer(RatsTileEntityRegistry.RAT_CAGE_DECORATED, manager -> new RenderRatCageDecorated(manager));
        ClientRegistry.bindTileEntityRenderer(RatsTileEntityRegistry.RAT_CAGE_BREEDING_LANTERN, manager -> new RenderRatCageDecorated(manager));
        ClientRegistry.bindTileEntityRenderer(RatsTileEntityRegistry.UPGRADE_COMBINER, manager -> new RenderUpgradeCombiner(manager));
        ClientRegistry.bindTileEntityRenderer(RatsTileEntityRegistry.UPGRADE_SEPERATOR, manager -> new RenderUpgradeSeparator(manager));
        ClientRegistry.bindTileEntityRenderer(RatsTileEntityRegistry.DUTCHRAT_BELL, manager -> new RenderDutchratBell(manager));
        ClientRegistry.bindTileEntityRenderer(RatsTileEntityRegistry.AUTOMATON_HEAD, manager -> new RenderRatlanteanAutomatonHead(manager));
        ClientRegistry.bindTileEntityRenderer(RatsTileEntityRegistry.TRASH_CAN, manager -> new RenderTrashCan(manager));
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onBlockColors(ColorHandlerEvent.Block event) {
        RatsMod.LOGGER.info("loaded in block colorizer");
        event.getBlockColors().register((state, worldIn, pos, colorIn) -> {
            int meta = 0;
            if(worldIn.getTileEntity(pos) instanceof TileEntityRatTube){
                TileEntityRatTube tube = (TileEntityRatTube)worldIn.getTileEntity(pos);
                meta = tube.getColor();
            }
            DyeColor color = DyeColor.byId(meta);
            return color.getFireworkColor();
        }, RatsBlockRegistry.RAT_TUBE_COLOR);
        event.getBlockColors().register((state, worldIn, pos, colorIn) -> worldIn != null && pos != null ? BiomeColors.getFoliageColor(worldIn, pos) : GrassColors.get(0.5D, 1.0D), RatsBlockRegistry.MARBLED_CHEESE_GRASS);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onItemColors(ColorHandlerEvent.Item event) {
        RatsMod.LOGGER.info("loaded in item colorizer");
        event.getItemColors().register((p_getColor_1_, p_getColor_2_) -> GrassColors.get(0.5D, 1.0D), Item.getItemFromBlock(RatsBlockRegistry.MARBLED_CHEESE_GRASS));

        event.getItemColors().register((p_getColor_1_, p_getColor_2_) -> {
            int meta = 0;
            for (int i = 0; i < RatsItemRegistry.RAT_TUBES.length; i++) {
                if (p_getColor_1_.getItem() == RatsItemRegistry.RAT_TUBES[i]) {
                    meta = i;
                    break;
                }
            }
            DyeColor color = DyeColor.byId(meta);
            return color.getFireworkColor();
        }, RatsItemRegistry.RAT_TUBES);
        event.getItemColors().register((p_getColor_1_, p_getColor_2_) -> {
            int meta = 0;
            for (int i = 0; i < RatsItemRegistry.RAT_IGLOOS.length; i++) {
                if (p_getColor_1_.getItem() == RatsItemRegistry.RAT_IGLOOS[i]) {
                    meta = i;
                }
            }
            DyeColor color = DyeColor.byId(meta);
            return color.getFireworkColor();
        }, RatsItemRegistry.RAT_IGLOOS);
        event.getItemColors().register((p_getColor_1_, p_getColor_2_) -> {
            if (p_getColor_2_ == 0) {
                int meta = 0;
                for (int i = 0; i < RatsItemRegistry.RAT_HAMMOCKS.length; i++) {
                    if (p_getColor_1_.getItem() == RatsItemRegistry.RAT_HAMMOCKS[i]) {
                        meta = i;
                    }
                }
                DyeColor color = DyeColor.byId(meta);
                return color.getFireworkColor();
            } else {
                return -1;
            }
        }, RatsItemRegistry.RAT_HAMMOCKS);
        event.getItemColors().register((p_getColor_1_, p_getColor_2_) -> {
            if (p_getColor_2_ == 1) {
                return NuggetColorRegister.getNuggetColor(p_getColor_1_);
            } else {
                return -1;
            }
        }, RatsItemRegistry.RAT_NUGGET_ORE);
    }

    @OnlyIn(Dist.CLIENT)
    public void postInit() {
        for (Map.Entry<EntityType<?>, EntityRenderer<?>> entry : Minecraft.getInstance().getRenderManager().renderers.entrySet()) {
            EntityRenderer render = entry.getValue();
            if (render instanceof LivingRenderer) {
                ((LivingRenderer) render).addLayer(new LayerPlague((LivingRenderer) render));
            }
        }
        for (Map.Entry<String, PlayerRenderer> entry : Minecraft.getInstance().getRenderManager().getSkinMap().entrySet()) {
            PlayerRenderer render = entry.getValue();
            render.addLayer(new LayerPlague(render));
        }
        //        Map<String, FallbackResourceManager> resManagers = ObfuscationReflectionHelper.getPrivateValue(SimpleReloadableResourceManager.class, (SimpleReloadableResourceManager)Minecraft.getInstance().getResourceManager(), "field_199014"+"_c");
        Field renderingRegistryField = ObfuscationReflectionHelper.findField(RenderingRegistry.class, "INSTANCE");
        Field entityRendersField = ObfuscationReflectionHelper.findField(RenderingRegistry.class, "entityRenderers");
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
            try {
                Field modifier1 = Field.class.getDeclaredField("modifiers");
                modifier1.setAccessible(true);
                entityRenders = (Map<Class<? extends Entity>, IRenderFactory<? extends Entity>>) entityRendersField.get(registry);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (entityRenders != null) {
                for (Map.Entry<Class<? extends Entity>, IRenderFactory<? extends Entity>> entry : entityRenders.entrySet()) {
                    if (entry.getValue() != null) {
                        try {
                            EntityRenderer render = entry.getValue().createRenderFor(Minecraft.getInstance().getRenderManager());
                            if (render != null && render instanceof LivingRenderer && LivingEntity.class.isAssignableFrom(entry.getKey())) {
                                ((LivingRenderer) render).addLayer(new LayerPlague((LivingRenderer) render));
                            }
                        } catch (NullPointerException exp) {
                            RatsMod.LOGGER.warn("Rats: Could not apply plague render layer to " + entry.getKey().getSimpleName() + ", someone isn't registering their renderer properly... <.<");
                        }
                    }

                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderNameplates() {
        return Minecraft.getInstance().currentScreen == null || !(Minecraft.getInstance().currentScreen instanceof GuiRat) && !(Minecraft.getInstance().currentScreen instanceof GuiCheeseStaff);
    }

    @OnlyIn(Dist.CLIENT)
    public Object getArmorModel(int index) {
        if (index == 0) {
            return new ModelChefToque(1.0F);
        } else if (index == 1) {
            return new ModelPiperHat(1.0F);
        } else if (index == 2) {
            return new ModelPiratHat(1.0F);
        } else if (index == 3) {
            return new ModelArcheologistHat(1.0F);
        } else if (index == 4) {
            return new ModelFarmerHat(1.0F);
        } else if (index == 5) {
            return new ModelPlagueDoctorMask(1.0F);
        } else if (index == 6) {
            return new ModelRatFez(1.0F);
        } else if (index == 7) {
            return new ModelTopHat(1.0F);
        } else if(index == 8){
            return new ModelSantaHat(1.0F);
        }else if(index == 9){
            return new ModelGhostPiratHat(1.0F);
        }else  if(index == 10){
            return new ModelHaloHat(0.5F);
        }else  if(index == 11){
            return new ModelPartyHat(0.5F);
        }else if(index == 12){
            return new ModelMilitaryHat(1.2F);
        }else {
            return new ModelAviatorHat(1.0F);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRat getRefrencedRat() {
        return refrencedRat;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void setRefrencedRat(EntityRat rat) {
        refrencedRat = rat;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void setCheeseStaffContext(BlockPos pos, Direction facing) {
        refrencedPos = pos;
        refrencedFacing = facing;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addParticle(String name, double x, double y, double z, double motX, double motY, double motZ) {
        World world = Minecraft.getInstance().world;
        if (world == null) {
            return;
        }
        if (name.equals("rat_ghost")) {
           Minecraft.getInstance().particles.addEffect(new ParticleRatGhost(world, x, y, z, (float) motX, (float) motY, (float) motZ));
        }
        if (name.equals("pirat_ghost")) {
            Minecraft.getInstance().particles.addEffect(new ParticlePiratGhost(world, x, y, z, (float) motX, (float) motY, (float) motZ));
        }
        if (name.equals("rat_lightning")) {
            Minecraft.getInstance().particles.addEffect(new ParticleLightning(world, x, y, z, (float) motX, (float) motY, (float) motZ));
        }
        if (name.equals("flea")) {
            Minecraft.getInstance().particles.addEffect(new ParticleFlea(world, x, y, z, (float) motX, (float) motY, (float) motZ));
        }
        if (name.equals("upgrade_combiner")) {
          Minecraft.getInstance().particles.addEffect(new ParticleUpgradeCombiner(world, x, y, z, (float) motX, (float) motY, (float) motZ));
        }
        if (name.equals("saliva")) {
            Minecraft.getInstance().particles.addEffect(new ParticleSaliva(world, x, y, z, Fluids.WATER));
        }
        if (name.equals("black_death")) {
            Minecraft.getInstance().particles.addEffect(new ParticleBlackDeath(world, x, y, z, (float) motX, (float) motY, (float) motZ));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public TileEntity getRefrencedTE() {
        return refrencedTileEntity;
    }

    @OnlyIn(Dist.CLIENT)
    public void setRefrencedTE(TileEntity te) {
        refrencedTileEntity = te;
    }

    @OnlyIn(Dist.CLIENT)
    public ItemStack getRefrencedItem() {
        return refrencedItem;
    }

    @OnlyIn(Dist.CLIENT)
    public void setRefrencedItem(ItemStack stack) {
        refrencedItem = stack;
    }

    @OnlyIn(Dist.CLIENT)
    public World getWorld(){ return Minecraft.getInstance().world; }

    @OnlyIn(Dist.CLIENT)
    public void handlePacketAutoCurdlerFluid(long blockPos, FluidStack fluid){
        BlockPos pos = BlockPos.fromLong(blockPos);
        World world = Minecraft.getInstance().world;
        if ( world != null && world.getTileEntity(pos) instanceof TileEntityAutoCurdler) {
            TileEntityAutoCurdler table = (TileEntityAutoCurdler) world.getTileEntity(pos);
            table.tank.setFluid(fluid);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void handlePacketCheeseStaffRat(int entityId, boolean clear){
        Entity e = Minecraft.getInstance().player.world.getEntityByID(entityId);
        if (e instanceof EntityRat) {
            setRefrencedRat((EntityRat) e);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void handlePacketUpdateTileSlots(long blockPos, CompoundNBT tag){
        BlockPos pos = BlockPos.fromLong(blockPos);
        World world = Minecraft.getInstance().world;
        if (world.getTileEntity(pos) != null) {
            TileEntity te = world.getTileEntity(pos);
            te.read(tag);
        }
    }

    public void setupTEISR(Item.Properties props) {
        props.setISTER(ClientProxy::getTEISR);
    }

    @OnlyIn(Dist.CLIENT)
    private static Callable<ItemStackTileEntityRenderer> getTEISR() {
        return com.github.alexthe666.rats.client.render.tile.RatsTEISR::new;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void openCheeseStaffGui() {
        if (refrencedRat != null) {
            Minecraft.getInstance().displayGuiScreen(new GuiCheeseStaff(refrencedRat));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void openRadiusStaffGui() {
        if (refrencedRat != null) {
            Minecraft.getInstance().displayGuiScreen(new GuiRadiusStaff(refrencedRat));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getPartialTicks() {
        return Minecraft.getInstance().getRenderPartialTicks();
    }
}
