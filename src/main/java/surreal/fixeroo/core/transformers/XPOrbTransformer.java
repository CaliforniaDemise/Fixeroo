package surreal.fixeroo.core.transformers;

import org.objectweb.asm.tree.*;
import surreal.fixeroo.FixerooConfig;

import java.util.Iterator;

import static org.objectweb.asm.Opcodes.*;

public class XPOrbTransformer extends TypicalTransformer {

    public static byte[] transformEntityXPOrb(String transformedName, byte[] basicClass) {
        if (!FixerooConfig.xpOrbClump.enable) return basicClass;
        ClassNode cls = read(transformedName, basicClass);
        for (MethodNode method : cls.methods) {
            if (method.name.equals(getName("onUpdate", "func_70071_h_"))) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();
                    if (node.getOpcode() == INVOKESPECIAL) {
                        node = node.getNext();
                        method.instructions.insertBefore(node, new VarInsnNode(ALOAD, 0));
                        method.instructions.insertBefore(node, hook("EntityXPOrb$onUpdate", "(Lnet/minecraft/entity/item/EntityXPOrb;)V"));
                        break;
                    }
                }
            }
            else if (FixerooConfig.xpOrbClump.removeCooldown && method.name.equals(getName("onCollideWithPlayer", "func_70100_b_"))) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();
                    if (node.getOpcode() == ICONST_2) {
                        method.instructions.insertBefore(node, new InsnNode(ICONST_0));
                        iterator.remove();
                    }
                }
                break;
            }
        }
        return write(cls);
    }

    public static byte[] transformRenderXPOrb(String transformedName, byte[] basicClass) {
        if (!FixerooConfig.xpOrbClump.changeOrbSize) return basicClass;
        ClassNode cls = read(transformedName, basicClass);
        for (MethodNode method : cls.methods) {
            if (method.name.equals(getName("doRender", "func_76986_a"))) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                boolean first = true;
                int i = 0;

                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();

                    if (node instanceof LdcInsnNode) {
                        Object cst = ((LdcInsnNode) node).cst;

                        if (cst instanceof Float && cst.equals(0.3F)) {
                            if (first) {
                                first = false;
                                method.instructions.insertBefore(node, new VarInsnNode(ALOAD, 1));
                                method.instructions.insertBefore(node, hook("RenderXPOrb$getSize", "(Lnet/minecraft/entity/item/EntityXPOrb;)F"));
                            } else {
                                method.instructions.insertBefore(node, new VarInsnNode(FLOAD, 25));
                            }

                            iterator.remove();
                            i++;
                            if (i == 4) break;
                        }
                    }
                }
                break;
            }
        }
        return write(cls);
    }
}
