package surreal.fixeroo;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import surreal.fixeroo.events.EventXPOrb;

@Mod.EventBusSubscriber
@Mod(modid = Fixeroo.MODID, name = "Fixeroo", version = "@VERSION@", dependencies = "required-after:configanytime")
@SuppressWarnings("unused")
public class Fixeroo {

    public static final String MODID = "xporbclump";

    @Mod.EventHandler
    public void construction(FMLConstructionEvent event) {
        if (FixerooConfig.xpOrbClump.enable) MinecraftForge.EVENT_BUS.register(EventXPOrb.class);
    }
}
