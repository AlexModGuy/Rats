package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ForgeBlockTagsProvider;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRatlantisTool {

    public static class Pickaxe extends PickaxeItem{

        public Pickaxe() {
            super(RatsItemRegistry.RATLANTIS_TOOL_MATERIAL, 1, -2.8F, new Item.Properties().group(RatsMod.TAB));
            this.setRegistryName(RatsMod.MODID, "ratlantis_pickaxe");
        }

        @Override
        public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            tooltip.add(new TranslationTextComponent(this.getTranslationKey() + "0.desc").func_240699_a_(TextFormatting.YELLOW));
            tooltip.add(new TranslationTextComponent(this.getTranslationKey() + "1.desc").func_240699_a_(TextFormatting.GRAY));
        }

        public static final ResourceLocation STONE_FORGE_TAG = new ResourceLocation("rats", "pirat_blocks");

        public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
            if(Tags.Blocks.STONE.contains(state.getBlock())){
                if (!worldIn.isRemote && state.getBlockHardness(worldIn, pos) != 0.0F) {
                    stack.damageItem(0, entityLiving, (p_220038_0_) -> {
                        p_220038_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
                    });
                }
                return true;
            }else{
                return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
            }
        }


    }

    public static class Axe extends AxeItem {
        public Axe() {
            super(RatsItemRegistry.RATLANTIS_TOOL_MATERIAL, 5.0F, -3.0F, new Item.Properties().group(RatsMod.TAB));
            this.setRegistryName(RatsMod.MODID, "ratlantis_axe");
        }

        public float getDestroySpeed(ItemStack stack, BlockState state) {
            if(BlockTags.LEAVES.contains(state.getBlock())){
                return efficiency * 1.5F;
            }
            return super.getDestroySpeed(stack, state);
        }

        @Override
        public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            tooltip.add(new TranslationTextComponent(this.getTranslationKey() + "0.desc").func_240699_a_(TextFormatting.YELLOW));
            tooltip.add(new TranslationTextComponent(this.getTranslationKey() + "1.desc").func_240699_a_(TextFormatting.GRAY));
        }

        public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
            if(BlockTags.LEAVES.contains(state.getBlock())){
                if (!worldIn.isRemote && state.getBlockHardness(worldIn, pos) != 0.0F) {
                    stack.damageItem(0, entityLiving, (p_220038_0_) -> {
                        p_220038_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
                    });
                }
                return true;
            }else{
                return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
            }
        }
    }

    public static class Shovel extends ShovelItem {
        public Shovel() {
            super(RatsItemRegistry.RATLANTIS_TOOL_MATERIAL, 1.5F, -3.0F, new Item.Properties().group(RatsMod.TAB));
            this.setRegistryName(RatsMod.MODID, "ratlantis_shovel");
        }

        public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
            if(BlockTags.SAND.contains(state.getBlock())){
                if (!worldIn.isRemote && state.getBlockHardness(worldIn, pos) != 0.0F) {
                    stack.damageItem(0, entityLiving, (p_220038_0_) -> {
                        p_220038_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
                    });
                }
                return true;
            }else{
                return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
            }
        }

        @Override
        public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            tooltip.add(new TranslationTextComponent(this.getTranslationKey() + "0.desc").func_240699_a_(TextFormatting.YELLOW));
            tooltip.add(new TranslationTextComponent(this.getTranslationKey() + "1.desc").func_240699_a_(TextFormatting.GRAY));
        }
    }

    public static class Hoe extends HoeItem {
        public Hoe() {
            super(RatsItemRegistry.RATLANTIS_TOOL_MATERIAL, -3.0F, new Item.Properties().group(RatsMod.TAB));
            this.setRegistryName(RatsMod.MODID, "ratlantis_hoe");
        }

        public ActionResultType onItemUse(ItemUseContext context) {
            World world = context.getWorld();
            BlockPos blockpos = context.getPos();
            int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(context);
            if (hook != 0) return hook > 0 ? ActionResultType.SUCCESS : ActionResultType.FAIL;
            if (context.getFace() != Direction.DOWN && world.isAirBlock(blockpos.up())) {
                boolean b = false;
                PlayerEntity playerentity = context.getPlayer();
                for(int x = -1; x <= 1; x++){
                    for(int z = -1; z <= 1; z++){
                        BlockPos offset = blockpos.add(x, 0, z);
                        BlockState blockstate = HOE_LOOKUP.get(world.getBlockState(offset).getBlock());
                        if (blockstate != null) {
                            world.playSound(playerentity, offset, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            if (!world.isRemote) {
                                world.setBlockState(offset, blockstate, 11);
                                b = true;
                            }
                        }
                    }
                }

                if(b){
                    if (playerentity != null) {
                        context.getItem().damageItem(1, playerentity, (p_220043_1_) -> {
                            p_220043_1_.sendBreakAnimation(context.getHand());
                        });
                    }
                }
                return b ? ActionResultType.SUCCESS : ActionResultType.PASS;

            }

            return ActionResultType.PASS;
        }


        @Override
        public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            super.addInformation(stack, worldIn, tooltip, flagIn);
            tooltip.add(new TranslationTextComponent(this.getTranslationKey() + "0.desc").func_240699_a_(TextFormatting.YELLOW));
            tooltip.add(new TranslationTextComponent(this.getTranslationKey() + "1.desc").func_240699_a_(TextFormatting.GRAY));
        }
    }
}
