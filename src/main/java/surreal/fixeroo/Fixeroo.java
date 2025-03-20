package surreal.fixeroo;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import org.jetbrains.annotations.Nullable;
import surreal.fixeroo.events.EventXPOrb;

import java.util.Arrays;
import java.util.Locale;

@Mod.EventBusSubscriber
@Mod(modid = Fixeroo.MODID, name = "Fixeroo", version = Tags.MOD_VERSION, dependencies = "required-after:configanytime")
@SuppressWarnings("unused")
public class Fixeroo {

    public static final String MODID = "xporbclump";
    public static final Object2DoubleMap<ResourceLocation> TE_DISTANCE = getDistanceMap();

    @Mod.EventHandler
    public void construction(FMLConstructionEvent event) {
        if (FixerooConfig.xpOrbClump.enable) MinecraftForge.EVENT_BUS.register(EventXPOrb.class);
    }

    @Nullable
    public static Object2DoubleMap<ResourceLocation> getDistanceMap() {
        if (FixerooConfig.TESRDistance.distanceList.length == 0) return null;
        Object2DoubleMap<ResourceLocation> map = new Object2DoubleOpenHashMap<>();
        for (String str : FixerooConfig.TESRDistance.distanceList) {
            String[] split = str.split("#");
            if (split.length != 2) throw new RuntimeException("Config expression is wrong " + str);
            double d;
            {
                if (split[1].toLowerCase(Locale.US).equals("max")) d = Double.MAX_VALUE;
                else d = Double.parseDouble(split[1]);
            }
            if (d <= 0D) throw new RuntimeException("Config the given distance can't be 0 or negative");
            map.put(new ResourceLocation(split[0]), d);
        }
        return map;
    }
}
