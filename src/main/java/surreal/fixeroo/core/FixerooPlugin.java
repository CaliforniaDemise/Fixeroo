package surreal.fixeroo.core;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@IFMLLoadingPlugin.Name("Fixeroo")
@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
@IFMLLoadingPlugin.SortingIndex(1249)
public class FixerooPlugin implements IFMLLoadingPlugin {

    protected static final Logger LOGGER = LogManager.getLogger("Fixeroo");

    public static boolean configAnytime = false;

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
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
    public void injectData(Map<String, Object> data) {
        List coremods = (List) data.get("coremodList");
        for (Object coremod : coremods) {
            if (coremod.toString().startsWith("ConfigAnytimePlugin")) configAnytime = true;
        }
    }

    @Override
    public String getAccessTransformerClass() {
        return "surreal.fixeroo.core.FixerooTransformer";
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}