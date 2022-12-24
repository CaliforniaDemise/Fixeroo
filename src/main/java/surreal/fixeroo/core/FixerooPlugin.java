package surreal.fixeroo.core;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import surreal.fixeroo.FixerooConfig;
import surreal.fixeroo.core.transformers.EntityXPOrbTransformer;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.Name(FixerooPlugin.NAME)
@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
@IFMLLoadingPlugin.SortingIndex(1249)
public class FixerooPlugin implements IFMLLoadingPlugin {
    public static final String NAME = "XPOrbClump";
    public static final Logger LOGGER = LogManager.getLogger(NAME);
    public static boolean deobf = FMLLaunchHandler.isDeobfuscatedEnvironment();

    @Override
    public String[] getASMTransformerClass() {
        String str = null;

        if (FixerooConfig.xpOrbClump.enable) str = EntityXPOrbTransformer.class.getName() + ";";

        if (str != null) return str.split(";");
        return null;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {}

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
