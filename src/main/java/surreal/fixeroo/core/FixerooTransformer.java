package surreal.fixeroo.core;

import net.minecraft.launchwrapper.IClassTransformer;
import surreal.fixeroo.core.transformers.ElytraTransformer;
import surreal.fixeroo.core.transformers.GolemTransformer;
import surreal.fixeroo.core.transformers.XPOrbTransformer;

public class FixerooTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        switch (transformedName) {
            case "net.minecraft.entity.item.EntityXPOrb": return XPOrbTransformer.transformEntityXPOrb(transformedName, basicClass);
            case "net.minecraft.client.renderer.entity.RenderXPOrb": return XPOrbTransformer.transformRenderXPOrb(transformedName, basicClass);

            case "net.minecraft.block.BlockPumpkin": return GolemTransformer.transformBlockPumpkin(transformedName, basicClass);

            case "net.minecraft.client.model.ModelPlayer":
            case "net.minecraft.client.model.ModelBiped":
                return ElytraTransformer.transformModelPlayer(transformedName, basicClass);
            case "net.minecraft.client.renderer.entity.RenderPlayer": return ElytraTransformer.transformRenderPlayer(transformedName, basicClass);
            case "net.minecraft.entity.player.EntityPlayer": return ElytraTransformer.transformEntityPlayer(transformedName, basicClass);
        }
        return basicClass;
    }

    public static boolean a(boolean a, boolean b, boolean c) {
        boolean ass = a && b;
        return ass;
    }
}
