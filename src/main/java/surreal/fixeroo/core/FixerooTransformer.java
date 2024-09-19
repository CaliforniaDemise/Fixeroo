package surreal.fixeroo.core;

import net.minecraft.launchwrapper.IClassTransformer;
import surreal.fixeroo.core.transformers.*;


@SuppressWarnings("unused")
public class FixerooTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        switch (transformedName) {
            // Make XP Orbs get together to reduce FPS.
            // Potentially fixes a bug where game crashes when you get output from a furnace that holds too many experience.
            case "net.minecraft.entity.item.EntityXPOrb": return XPOrbTransformer.transformEntityXPOrb(transformedName, basicClass);

            // Change XP Orbs' size based on their level.
            case "net.minecraft.client.renderer.entity.RenderXPOrb": return XPOrbTransformer.transformRenderXPOrb(transformedName, basicClass);

            // Optimize golem building.
            case "net.minecraft.block.BlockPumpkin": return GolemTransformer.transformBlockPumpkin(transformedName, basicClass);

            // Fix being able to crouch while using Elytra. Fixes MC-90598 and MC-162401.
            case "net.minecraft.client.model.ModelPlayer":
            case "net.minecraft.client.model.ModelBiped":
                return ElytraTransformer.transformModelPlayer(transformedName, basicClass);
            case "net.minecraft.client.renderer.entity.RenderPlayer": return ElytraTransformer.transformRenderPlayer(transformedName, basicClass);
            case "net.minecraft.entity.player.EntityPlayer": return ElytraTransformer.transformEntityPlayer(transformedName, basicClass);

            // Fix Entity position
            case "net.minecraft.client.entity.EntityPlayerSP": return PreciseEntityPositionTransformer.transformEntityPlayerSP(transformedName, basicClass);

            // Shulker Coloring
            case "net.minecraft.entity.monster.EntityShulker": return ShulkerColoringTransformer.transformEntityShulker(transformedName, basicClass);
        }
        return basicClass;
    }
}
