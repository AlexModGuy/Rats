package com.github.alexthe666.rats.client;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.gui.GuiCheeseStaff;
import com.github.alexthe666.rats.client.gui.GuiRat;
import com.github.alexthe666.rats.client.model.ModelChefToque;
import com.github.alexthe666.rats.client.model.ModelPiperHat;
import com.github.alexthe666.rats.client.model.StateMapperGeneric;
import com.github.alexthe666.rats.client.render.entity.RenderIllagerPiper;
import com.github.alexthe666.rats.client.render.entity.RenderRat;
import com.github.alexthe666.rats.client.render.tile.*;
import com.github.alexthe666.rats.server.CommonProxy;
import com.github.alexthe666.rats.server.blocks.*;
import com.github.alexthe666.rats.server.compat.TinkersCompatBridge;
import com.github.alexthe666.rats.server.entity.EntityIllagerPiper;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.tile.*;
import com.github.alexthe666.rats.server.events.ClientEvents;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
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
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

@Mod.EventBusSubscriber
public class ClientProxy extends CommonProxy {
    @SideOnly(Side.CLIENT)
    private static final RatsTEISR TEISR = new RatsTEISR();
    @SideOnly(Side.CLIENT)
    private static final ModelChefToque MODEL_CHEF_TOQUE = new ModelChefToque(1.0F);
    protected static EntityRat refrencedRat;
    public static BlockPos refrencedPos;
    public static EnumFacing refrencedFacing;

    public void preInit() {
        TinkersCompatBridge.loadTinkersClientCompat();
    }

    @SideOnly(Side.CLIENT)
    public void init(){
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        RenderingRegistry.registerEntityRenderingHandler(EntityRat.class, new RenderRat());
        RenderingRegistry.registerEntityRenderingHandler(EntityIllagerPiper.class, new RenderIllagerPiper());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRatHole.class, new RenderRatHole());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRatTrap.class, new RenderRatTrap());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRatlantisPortal.class, new RenderRatlantisPortal());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRatCageDecorated.class, new RenderRatCageDecorated());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRatCageBreedingLantern.class, new RenderRatCageDecorated());
        Item.getItemFromBlock(RatsBlockRegistry.RAT_HOLE).setTileEntityItemStackRenderer(TEISR);
        Item.getItemFromBlock(RatsBlockRegistry.RAT_TRAP).setTileEntityItemStackRenderer(TEISR);
        Item.getItemFromBlock(RatsBlockRegistry.RATLANTIS_PORTAL).setTileEntityItemStackRenderer(TEISR);
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
                for(int i = 0; i < RatsBlockRegistry.RAT_TUBE_COLOR.length; i++){
                    if(block == RatsBlockRegistry.RAT_TUBE_COLOR[i]){
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
                for(int i = 0; i < RatsBlockRegistry.RAT_TUBE_COLOR.length; i++){
                    if(block == RatsBlockRegistry.RAT_TUBE_COLOR[i]){
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
                for(int i = 0; i < RatsItemRegistry.RAT_IGLOOS.length; i++){
                    if(stack.getItem() == RatsItemRegistry.RAT_IGLOOS[i]){
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
                if(tintIndex == 0){
                    int meta = 0;
                    for(int i = 0; i < RatsItemRegistry.RAT_HAMMOCKS.length; i++){
                        if(stack.getItem() == RatsItemRegistry.RAT_HAMMOCKS[i]){
                            meta = i;
                        }
                    }
                    EnumDyeColor color = EnumDyeColor.byMetadata(meta);
                    return color.getColorValue();
                }else{
                    return -1;
                }

            }
        }, RatsItemRegistry.RAT_HAMMOCKS);
    }

    public void postInit() {

    }


    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomStateMapper(RatsBlockRegistry.RAT_TRAP, (new StateMap.Builder()).ignore(BlockRatTrap.FACING).build());
        ModelLoader.setCustomStateMapper(RatsBlockRegistry.RAT_HOLE, (new StateMap.Builder()).ignore(BlockRatHole.NORTH, BlockRatHole.EAST, BlockRatHole.SOUTH, BlockRatHole.WEST).build());
        ModelLoader.setCustomStateMapper(RatsBlockRegistry.RAT_CAGE_DECORATED, (new StateMap.Builder()).ignore(BlockRatCageDecorated.FACING).build());
        ModelLoader.setCustomStateMapper(RatsBlockRegistry.RAT_CAGE_BREEDING_LANTERN, (new StateMap.Builder()).ignore(BlockRatCageBreedingLantern.FACING).build());

        for(int i = 0; i < 16; i++){
            ModelLoader.setCustomStateMapper(RatsBlockRegistry.RAT_TUBE_COLOR[i], (new StateMapperGeneric("rat_tube")));
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(RatsBlockRegistry.RAT_TUBE_COLOR[i]), 0, new ModelResourceLocation("rats:rat_tube", "inventory"));
            ModelLoader.setCustomModelResourceLocation(RatsItemRegistry.RAT_IGLOOS[i], 0, new ModelResourceLocation("rats:rat_igloo", "inventory"));
            ModelLoader.setCustomModelResourceLocation(RatsItemRegistry.RAT_HAMMOCKS[i], 0, new ModelResourceLocation("rats:rat_hammock", "inventory"));
        }
        try {
            for (Field f : RatsBlockRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Block && !(obj instanceof ICustomRendered)) {
                    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock((Block)obj), 0, new ModelResourceLocation("rats:" + ((Block)obj).getRegistryName().getPath(), "inventory"));
                } else if (obj instanceof Block[]) {
                    for (Block block : (Block[]) obj) {
                        if(!(block instanceof ICustomRendered)){
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
                    ModelLoader.setCustomModelResourceLocation((Item)obj, 0, new ModelResourceLocation("rats:" + ((Item)obj).getRegistryName().getPath(), "inventory"));
                } else if (obj instanceof Item[]) {
                    for (Item item : (Item[]) obj) {
                        if(!(item instanceof ICustomRendered)){
                            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation("rats:" + item.getRegistryName().getPath(), "inventory"));
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static final ResourceLocation SYNESTHESIA = new ResourceLocation("rats:shaders/post/synesthesia.json");
    private float synesthesiaProgress = 0;
    private float prevSynesthesiaProgress = 0;
    private float maxSynesthesiaProgress = 40;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntityLiving() == Minecraft.getMinecraft().player) {
            EntityRenderer renderer = Minecraft.getMinecraft().entityRenderer;
            PotionEffect active = event.getEntityLiving().getActivePotionEffect(RatsMod.CONFIT_BYALDI_POTION);
            boolean synesthesia = active != null;
            if (synesthesia && !renderer.isShaderActive()) {
                renderer.loadShader(SYNESTHESIA);
            }
            if (!synesthesia && renderer.isShaderActive()) {
                renderer.stopUseShader();
            }
            if(prevSynesthesiaProgress == 2 && synesthesia){
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(RatsSoundRegistry.POTION_EFFECT_BEGIN, 1.0F));
            }
            if(prevSynesthesiaProgress == 38 && !synesthesia){
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
        if(event.getEntity() == Minecraft.getMinecraft().player && prevSynesthesiaProgress > 0){
            float prog = (prevSynesthesiaProgress + (synesthesiaProgress - prevSynesthesiaProgress) * LLibrary.PROXY.getPartialTicks());
            float renderProg;
            if(prevSynesthesiaProgress > synesthesiaProgress){
                renderProg = (float)Math.sin(prog / maxSynesthesiaProgress * Math.PI) * 40F;
            }else{
                renderProg = -(float)Math.sin(prog / maxSynesthesiaProgress * Math.PI) * 40F;
            }
            event.setFOV(event.getFOV() + renderProg);
        }
    }

    public boolean shouldRenderNameplates() {
        return Minecraft.getMinecraft().currentScreen == null || !(Minecraft.getMinecraft().currentScreen instanceof GuiRat) && !(Minecraft.getMinecraft().currentScreen instanceof GuiCheeseStaff);
    }

    @SideOnly(Side.CLIENT)
    public Object getArmorModel(int index) {
        if(index == 0){
            return new ModelChefToque(1.0F);

        }else{
            return new ModelPiperHat(1.0F);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void openCheeseStaffGui() {
        if(refrencedRat != null){
            Minecraft.getMinecraft().displayGuiScreen(new GuiCheeseStaff(refrencedRat));
        }
    }

    @Override
    public void setRefrencedRat(EntityRat rat){
        refrencedRat = rat;
    }

    @Override
    public EntityRat getRefrencedRat(){
        return refrencedRat;
    }

    @Override
    public void setCheeseStaffContext(BlockPos pos, EnumFacing facing) {
        refrencedPos = pos;
        refrencedFacing = facing;
    }

}
