package surreal.fixeroo.core.transformers;

import org.objectweb.asm.tree.*;
import surreal.fixeroo.FixerooConfig;

// TODO Add whitelisting
public class TESRRenderDistanceTransformer extends TypicalTransformer {
    
    public static byte[] transformTileEntity(String transformedName, byte[] basicClass) {
        if (!FixerooConfig.TESRDistance.applyToEverything || FixerooConfig.TESRDistance.I_AM_HIM) return basicClass;
        ClassNode cls = read(transformedName, basicClass);
        for (MethodNode method : cls.methods) {
            if (method.name.equals(getName("getMaxRenderDistanceSquared", "func_145833_n"))) {
                AbstractInsnNode node = method.instructions.getLast();
                while (node.getOpcode() != DRETURN) node = node.getPrevious();
                method.instructions.remove(node.getPrevious());
                method.instructions.insertBefore(node, new LdcInsnNode(FixerooConfig.TESRDistance.maxDistance));
                break;
            }
        }
        return write(cls);
    }

    public static byte[] transformTileEntityRendererDispatcher(String transformedName, byte[] basicClass) {
        if (!FixerooConfig.TESRDistance.I_AM_HIM) return basicClass;
        ClassNode cls = read(transformedName, basicClass);
        for (MethodNode method : cls.methods) {
            if (method.name.equals(getName("render", "func_147549_a"))) {
                AbstractInsnNode node = method.instructions.getFirst();
                while (node.getOpcode() != IFGE) {
                    node = node.getNext();
                    method.instructions.remove(node.getPrevious());
                }
                ((JumpInsnNode) node).setOpcode(IFEQ);
                method.instructions.insertBefore(node, new InsnNode(ICONST_1));
                break;
            }
        }
        return write(cls);
    }
}
