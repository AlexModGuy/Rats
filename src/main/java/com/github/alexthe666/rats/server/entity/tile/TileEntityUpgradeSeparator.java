package com.github.alexthe666.rats.server.entity.tile;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.ItemRatCombinedUpgrade;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityUpgradeSeparator extends TileEntity implements ITickable {
    public float ratRotation;
    public float ratRotationPrev;

    @Override
    public void update() {
        this.ratRotationPrev = this.ratRotation;
        ratRotation++;
        float i = this.getPos().getX() + 0.5F;
        float j = this.getPos().getY() + 0.75F;
        float k = this.getPos().getZ() + 0.5F;
        double d0 = 0.5D;
        for (ItemEntity ItemEntity : world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0))) {
            ItemStack item = ItemEntity.getItem();
            if (item.getItem() instanceof ItemRatCombinedUpgrade) {
                NBTTagCompound nbttagcompound1 = item.getTagCompound();
                int spawnedItem = 0;
                if (nbttagcompound1 != null && nbttagcompound1.hasKey("Items", 9)) {
                    NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
                    ItemStackHelper.loadAllItems(nbttagcompound1, nonnulllist);
                    for (ItemStack itemstack : nonnulllist) {
                        if (!itemstack.isEmpty()) {
                            ItemEntity splitEntity = new ItemEntity(this.getWorld(), ItemEntity.posX, ItemEntity.posY, ItemEntity.posZ, itemstack.copy());
                            if (!world.isRemote) {
                                world.addEntity(splitEntity);
                            }
                            spawnedItem++;
                        }
                    }
                }
                if(spawnedItem > 0){
                    ItemEntity.playSound(SoundEvents.ENTITY_ITEM_BREAK, 1, 1);
                    ItemEntity.setDead();
                    ItemEntity splitEntity = new ItemEntity(this.getWorld(), ItemEntity.posX, ItemEntity.posY, ItemEntity.posZ, new ItemStack(RatsItemRegistry.GEM_OF_RATLANTIS, spawnedItem));
                    if (!world.isRemote) {
                        world.addEntity(splitEntity);
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public net.minecraft.util.math.AxisAlignedBB getRenderBoundingBox() {
        return new net.minecraft.util.math.AxisAlignedBB(pos, pos.add(1, 2, 1));
    }
}
