package surreal.fixeroo.core;

import net.minecraft.launchwrapper.IClassTransformer;
import surreal.fixeroo.core.transformers.GolemTransformer;
import surreal.fixeroo.core.transformers.XPOrbTransformer;

public class FixerooTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        switch (transformedName) {
            case "net.minecraft.entity.item.EntityXPOrb": return XPOrbTransformer.transformEntityXPOrb(transformedName, basicClass);
            case "net.minecraft.client.renderer.entity.RenderXPOrb": return XPOrbTransformer.transformRenderXPOrb(transformedName, basicClass);
            case "net.minecraft.block.BlockPumpkin": return GolemTransformer.transformBlockPumpkin(transformedName, basicClass);
        }

        return basicClass;
    }
}
