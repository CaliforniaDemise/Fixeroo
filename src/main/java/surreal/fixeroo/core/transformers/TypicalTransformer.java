package surreal.fixeroo.core.transformers;

import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.MethodInsnNode;
import surreal.fixeroo.core.FixerooPlugin;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;

public class TypicalTransformer {

    static Logger logger = FixerooPlugin.getLogger();
    static boolean deobf = FMLLaunchHandler.isDeobfuscatedEnvironment();

    static MethodInsnNode hook(String name, String desc) {
        return new MethodInsnNode(INVOKESTATIC, "surreal/fixeroo/core/FixerooHooks", name, desc, false);
    }
}
