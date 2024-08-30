package surreal.fixeroo;

import com.cleanroommc.configanytime.ConfigAnytime;
import net.minecraftforge.common.config.Config;
import surreal.fixeroo.core.FixerooPlugin;

@Config(modid = Fixeroo.MODID)
public class FixerooConfig {

    public static final XPOrbClump xpOrbClump = new XPOrbClump();
    public static final GolemTweaks golemTweaks = new GolemTweaks();
    public static final ElytraTweaks elytraTweaks = new ElytraTweaks();
    public static final PreciseEntityPosition preciseEntityPosition = new PreciseEntityPosition();

    public static class XPOrbClump {
        @Config.Comment("Enable xp orb clumping")
        public boolean enable = true;

        @Config.Comment("Remove xp collecting cooldown")
        public boolean removeCooldown = true;

        @Config.Comment("Size of checking area")
        public double areaSize = 4D;

        @Config.Comment("How many xp orbs can be in that area")
        public int maxOrbCount = 1;

        @Config.Comment("Changes orbs size based on the amount of experience it holds")
        public boolean changeOrbSize = false;
    }

    public static class GolemTweaks {
        @Config.Comment("Enable fixes to golem")
        public boolean enable = true;
    }

    public static class ElytraTweaks {
        @Config.Comment({"Enable fixes to elytra", "Fixes MC-90598 and MC-162401"})
        public boolean enable = true;
    }

    public static class PreciseEntityPosition {
        @Config.Comment({"Fix EntityPlayerSP.getPosition", "For some reason they override getPosition and return wrong position if X and/or Z is negative."})
        public boolean enable = true;
    }

    static {
        if (FixerooPlugin.configAnytime) {
            ConfigAnytime.register(FixerooConfig.class);
        }
    }
}
