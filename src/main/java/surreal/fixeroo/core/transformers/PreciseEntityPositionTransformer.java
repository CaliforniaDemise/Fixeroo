package surreal.fixeroo.core.transformers;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import surreal.fixeroo.FixerooConfig;

import java.util.Iterator;

public class PreciseEntityPositionTransformer extends TypicalTransformer {

    public static byte[] transformEntityPlayerSP(String transformedName, byte[] basicClass) {
        if (!FixerooConfig.preciseEntityPosition.enable) return basicClass;
        ClassNode cls = read(transformedName, basicClass);
        Iterator<MethodNode> iterator = cls.methods.iterator();
        while (iterator.hasNext()) {
            MethodNode method = iterator.next();
            if (method.name.equals(getName("getPosition", "func_174824_e"))) {
                iterator.remove();
                break;
            }
        }
        return write(cls);
    }
}
