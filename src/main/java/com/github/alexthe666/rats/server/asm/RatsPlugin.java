package com.github.alexthe666.rats.server.asm;

import com.github.alexthe666.rats.server.misc.RatsRuntimePatcher;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.Name("ratscore")
@IFMLLoadingPlugin.TransformerExclusions({"com.github.alexthe666.rats.server.asm"})
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.SortingIndex(1003)
public class RatsPlugin implements IFMLLoadingPlugin {

    public static boolean runtimeDeobfEnabled = false;

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{RatsRuntimePatcher.class.getName()};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        runtimeDeobfEnabled = (Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}
