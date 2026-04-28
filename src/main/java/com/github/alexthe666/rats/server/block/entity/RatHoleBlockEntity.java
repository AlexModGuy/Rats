package com.github.alexthe666.rats.server.block.entity;

import com.github.alexthe666.rats.registry.RatsBlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.HolderLookup;

public class RatHoleBlockEntity extends BlockEntity {
	private NonNullList<ItemStack> imitationStack = NonNullList.withSize(1, ItemStack.EMPTY);

	public RatHoleBlockEntity(BlockPos pos, BlockState state) {
		super(RatsBlockEntityRegistry.RAT_HOLE.get(), pos, state);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this, BlockEntity::getUpdateTag);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet, HolderLookup.Provider registries) {
		this.handleUpdateTag(packet.getTag(), registries);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return this.saveWithId(registries);
	}

	@Override
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		ContainerHelper.saveAllItems(tag, this.imitationStack, registries);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		this.imitationStack = NonNullList.withSize(1, ItemStack.EMPTY);
		ContainerHelper.loadAllItems(tag, this.imitationStack, registries);
	}

	public BlockState getImitatedBlockState() {
		BlockState defState = Blocks.OAK_PLANKS.defaultBlockState();
		if (!this.imitationStack.get(0).isEmpty() && this.imitationStack.get(0).getItem() instanceof BlockItem item) {
			Block block = item.getBlock();
			return block.defaultBlockState();
		}
		return defState;
	}

	public void setImitatedBlockState(BlockState state) {
		ItemStack stack = new ItemStack(state.getBlock(), 1);
		this.imitationStack.set(0, stack);
	}
}
