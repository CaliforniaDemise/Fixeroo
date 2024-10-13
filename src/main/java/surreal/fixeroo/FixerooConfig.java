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
    public static final ShulkerColoring shulkerColoring = new ShulkerColoring();
    public static final TileEntityRenderDistance TESRDistance = new TileEntityRenderDistance();

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

    public static class ShulkerColoring {
        @Config.Comment({"Allow coloring shulkers with dyes.", "default: false"})
        public boolean enable = false;
    }

    public static class TileEntityRenderDistance {
        @Config.Comment({"Square of max distance of tile entity rendering. It's square because hypotenuse.", "Right now this only apples for banner because TESR's do get expensive and I'm too lazy to add Map support for Forge configs." })
        public double maxDistance = 4096.0D;

        @Config.Comment({"Apply this to all TESR's (chests, signs, skulls etc.) Beware that it might get laggy because there's no culling algorithm for TESR's", "This might not work for all TESR's because I'm lazy."})
        public boolean applyToEverything = false;

        @Config.Comment({"Remove the distance limit altogether.", "This option makes both of the other options redundant."})
        public boolean I_AM_HIM = false;
    }

    static {
        if (FixerooPlugin.configAnytime) {
            ConfigAnytime.register(FixerooConfig.class);
        }
    }
}
