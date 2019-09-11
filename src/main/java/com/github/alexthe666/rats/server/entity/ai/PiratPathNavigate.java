package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.blocks.BlockRatTube;
import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PiratPathNavigate extends PathNavigateSwimmer {

    public PiratPathNavigate(EntityLiving entitylivingIn, World worldIn) {
        super(entitylivingIn, worldIn);
    }

    protected boolean isInLiquid() {
        return this.entity.isInWater() || this.entity.isInLava() || this.entity.isRiding();
    }
}