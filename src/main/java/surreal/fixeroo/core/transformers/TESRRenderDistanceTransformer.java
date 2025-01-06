package surreal.fixeroo.core.transformers;

import org.objectweb.asm.tree.*;
import surreal.fixeroo.FixerooConfig;

import java.util.Iterator;

public class TESRRenderDistanceTransformer extends TypicalTransformer {

    public static byte[] transformTileEntity(String transformedName, byte[] basicClass) {
        if (FixerooConfig.TESRDistance.I_AM_HIM) return basicClass;
        ClassNode cls = read(transformedName, basicClass);
        for (MethodNode method : cls.methods) {
            if (method.name.equals(getName("getMaxRenderDistanceSquared", "func_145833_n"))) {
                AbstractInsnNode node = method.instructions.getLast();
                while (node.getOpcode() != DRETURN) node = node.getPrevious();
                InsnList list = new InsnList();
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(hook("TileEntity$getDistance", "(DLnet/minecraft/tileentity/TileEntity;)D"));
                method.instructions.insertBefore(node, list);
                break;
            }
        }
        return write(cls);
    }

    public static byte[] transformTileEntityRendererDispatcher(String transformedName, byte[] basicClass) {
        if (!FixerooConfig.TESRDistance.I_AM_HIM) return basicClass;
        ClassNode cls = read(transformedName, basicClass);
        for (MethodNode method : cls.methods) {
            if (method.name.equals(getName("render", "func_180546_a"))) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();
                    if (node.getOpcode() == INVOKEVIRTUAL && ((MethodInsnNode) node).name.equals(getName("getMaxRenderDistanceSquared", "func_145833_n"))) {
                        method.instructions.remove(node.getPrevious());
                        method.instructions.insert(node, new LdcInsnNode(0x1.fffffffffffffP+1023));
                        method.instructions.remove(node);
                        break;
                    }
                }
                break;
            }
        }
        writeClass(cls);
        return write(cls);
    }
}
