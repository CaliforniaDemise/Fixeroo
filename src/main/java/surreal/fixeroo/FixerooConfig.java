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
    public static final TileEntityRotationFix TERotationFix = new TileEntityRotationFix();

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
        @Config.Comment({"Square of max distance to render tile entities by default. 4096.0 is the vanilla value, use 0.0 to denote to not override the default value, tile entities can have different values." })
        @Config.RangeDouble(min = 0.0D)
        public double maxDistance = 0.0D;

        @Config.Comment({"The max distance you can go away before tile entity gets discarded for rendering.", "You can't use 0 or negative values. You can use MAX to denote no limits. 4096 is the default distance limit.", "The distance given will be square rooted to get actual distance.", "Example: minecraft:banner#4096.0"})
        public String[] distanceList = new String[0];

        @Config.Comment({"Remove the distance limit altogether.", "This option makes both of the other options redundant."})
        public boolean I_AM_HIM = false;
    }

    public static class TileEntityRotationFix {

        @Config.RequiresMcRestart
        @Config.Name("Fix Tile Entity Rotation")
        @Config.Comment({
                "Fix some modded tile entities losing their data when rotated by wrenches",
                "Fixes tile entities from: Barrels, Drums, Storage & More, Traveler's Backpack"
        })
        public boolean enableRotationFix = true;

        @Config.RequiresMcRestart
        @Config.Name("Rotation Glitch Fix Target Classes")
        @Config.Comment({
                "Additional classes of tile entities that are not on the list for people that know how this thing works.",
                "Be sure to start a new issue on this mods GitHub page if you find a tile entity that suffers from this issue.",
                "Example: com.tiviacz.travelersbackpack.tileentity.TileEntityTravelersBackpack"})
        public String[] tileEntities = new String[0];
    }

    static {
        if (FixerooPlugin.configAnytime) {
            ConfigAnytime.register(FixerooConfig.class);
        }
    }
}
