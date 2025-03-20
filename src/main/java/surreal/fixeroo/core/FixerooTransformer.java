package surreal.fixeroo.core;

import com.google.common.collect.ImmutableSet;
import net.minecraft.launchwrapper.IClassTransformer;
import surreal.fixeroo.FixerooConfig;
import surreal.fixeroo.core.transformers.*;
import surreal.fixeroo.core.transformers.mods.ModularPowersuitsTransformer;
import surreal.fixeroo.core.transformers.mods.TinkersComplementTransformer;

import java.util.Set;

@SuppressWarnings("unused")
public class FixerooTransformer implements IClassTransformer {

    private final Set<String> addTEs = this.buildAddTEs();

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (!FixerooPlugin.configAnytime) return basicClass;
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
            case "net.darkhax.darkutils.features.shulkerpearl.FeatureShulkerPearlItem": return ShulkerColoringTransformer.transformFeatureShulkerPearlItem(transformedName, basicClass);

            // TESR Render Distance
            case "net.minecraft.tileentity.TileEntity": return TESRRenderDistanceTransformer.transformTileEntity(transformedName, basicClass);
            case "net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher": return TESRRenderDistanceTransformer.transformTileEntityRendererDispatcher(transformedName, basicClass);

            case "net.machinemuse.powersuits.common.base.ModuleManager": return ModularPowersuitsTransformer.transformModuleManager(transformedName, basicClass);
            case "net.machinemuse.powersuits.common.config.MPSSettings": return ModularPowersuitsTransformer.transformMPSSettings(transformedName, basicClass);

            case "knightminer.tcomplement.plugin.chisel.items.ItemChisel": return TinkersComplementTransformer.transformItemChisel(transformedName, basicClass);
        }
        if (FixerooConfig.TERotationFix.enableRotationFix && this.addTEs.contains(transformedName)) {
            return MisrotatedModdedTETransformer.transformModdedTE(transformedName, basicClass);
        }

        return basicClass;
    }

    private Set<String> buildAddTEs() {
        ImmutableSet.Builder<String> builder = new ImmutableSet.Builder<>();
        builder.add(
                "funwayguy.bdsandm.blocks.tiles.TileEntityBarrel",
                "funwayguy.bdsandm.blocks.tiles.TileEntityCrate",

                "com.tiviacz.travelersbackpack.tileentity.TileEntityTravelersBackpack"
        );
        builder.add(FixerooConfig.TERotationFix.tileEntities);
        return builder.build();
    }
}
