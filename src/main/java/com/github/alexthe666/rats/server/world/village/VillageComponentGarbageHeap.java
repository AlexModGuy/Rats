package com.github.alexthe666.rats.server.world.village;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

import java.util.List;
import java.util.Random;

public class VillageComponentGarbageHeap extends StructureVillagePieces.Village {
    int villagerCount = 0;
    private int averageGroundLevel = -1;

    public VillageComponentGarbageHeap() {
        super();
    }

    public VillageComponentGarbageHeap(StructureVillagePieces.Start startPiece, int p2, Random random, StructureBoundingBox structureBox, EnumFacing facing) {
        super();
        this.villagerCount = 0;
        this.setCoordBaseMode(facing);
        this.boundingBox = structureBox;
    }

    public static VillageComponentGarbageHeap buildComponent(StructureVillagePieces.Start startPiece, List<StructureComponent> pieces, Random random, int x, int y, int z, EnumFacing facing, int p5) {
        if (!RatsMod.CONFIG_OPTIONS.villageGarbageHeaps) {
            return null;
        }
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 8, 4, 8, facing);
        return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new VillageComponentGarbageHeap(startPiece, p5, random, structureboundingbox, facing) : null;
    }

    @Override
    public boolean addComponentParts(World world, Random random, StructureBoundingBox sbb) {
        if (this.averageGroundLevel < 0) {
            this.averageGroundLevel = this.getAverageGroundLevel(world, sbb);
            if (this.averageGroundLevel < 0) {
                return false;
            }
            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 4, 0);
        }

        BlockPos blockpos = new BlockPos(this.boundingBox.minX + 1, this.boundingBox.minY + 1, this.boundingBox.minZ + 1);
        EnumFacing facing = EnumFacing.SOUTH;
        BlockPos genPos = blockpos;
        /*for(int i = this.boundingBox.minX; i < this.boundingBox.maxX; i++){
            for(int k = this.boundingBox.minZ; k < this.boundingBox.maxZ; k++){
                world.setBlockState(new BlockPos(i, boundingBox.maxY, k), Blocks.GOLD_BLOCK.getDefaultState());
            }
        }*/
        //world.setBlockState(new BlockPos(this.boundingBox.minX, this.boundingBox.maxY, this.boundingBox.minZ), Blocks.PUMPKIN.getDefaultState().withProperty(BlockHorizontal.FACING, facing));
        return new WorldGenGarbageHeap(this, facing).generate(world, random, genPos);
    }

    public IBlockState getBiomeBlock(IBlockState state) {
        return getBiomeSpecificBlockState(state);
    }
}
