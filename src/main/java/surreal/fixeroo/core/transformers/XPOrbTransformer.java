package surreal.fixeroo.core.transformers;

import org.objectweb.asm.tree.*;
import surreal.fixeroo.FixerooConfig;

import java.util.Iterator;

import static org.objectweb.asm.Opcodes.*;

public class XPOrbTransformer extends TypicalTransformer {

    public static void transformEntityXPOrb(ClassNode cls) {
        for (MethodNode method : cls.methods) {
            if (method.name.equals(deobf ? "onUpdate" : "func_70071_h_")) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();

                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();

                    if (node.getOpcode() == POP) {
                        node = node.getNext();

                        method.instructions.insertBefore(node, new VarInsnNode(ALOAD, 0));
                        method.instructions.insertBefore(node, hook("EntityXPOrb$onUpdate", "(Lnet/minecraft/entity/item/EntityXPOrb;)V"));

                        break;
                    }
                }
            }
            else if (FixerooConfig.xpOrbClump.removeCooldown && method.name.equals(deobf ? "onCollideWithPlayer" : "func_70100_b_")) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                boolean remove = false;

                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();

                    if (remove) iterator.remove();

                    if (node instanceof LineNumberNode && ((LineNumberNode) node).line == 243) {
                        remove = true;
                    } else if (node instanceof LabelNode && remove) break;
                }

                break;
            }
        }
    }

    public static void transformRenderXPOrb(ClassNode cls) {
        for (MethodNode method : cls.methods) {
            if (method.name.equals(deobf ? "doRender" : "func_76986_a")) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                boolean first = true;

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
                        }
                    }
                }

                break;
            }
        }
    }
}
