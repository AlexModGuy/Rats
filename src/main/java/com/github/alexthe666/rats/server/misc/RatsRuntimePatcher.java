package com.github.alexthe666.rats.server.misc;

import net.ilexiconn.llibrary.server.asm.InsnPredicate;
import net.ilexiconn.llibrary.server.asm.RuntimePatcher;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RatsRuntimePatcher extends RuntimePatcher {
    @Override
    public void onInit() {
        this.patchClass(RenderGlobal.class)
                .patchMethod("setPartying", World.class, BlockPos.class, boolean.class, void.class)
                .apply(Patch.REPLACE_NODE, new InsnPredicate.Ldc().cst(3.0D), method -> {
                    method.method(INVOKESTATIC, RatsCoreUtils.class, "getPartyDistance", double.class);
                }).pop();
    }
}