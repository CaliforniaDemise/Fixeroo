package surreal.fixeroo.core.transformers;

import org.objectweb.asm.tree.*;
import surreal.fixeroo.FixerooConfig;

import java.util.Iterator;

public class XPOrbTransformer extends TypicalTransformer {

    // TODO Fix xp orb bounding box with creating a prevXpValue field and checking the difference.
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
            else if (method.name.equals(getName("writeEntityToNBT", "func_70014_b"))) {
                AbstractInsnNode node = method.instructions.getLast();
                while (node.getOpcode() != INVOKEVIRTUAL) node = node.getPrevious();
                method.instructions.remove(node.getPrevious());
                method.instructions.insert(node, new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", getName("setInteger", "func_74768_a"), "(Ljava/lang/String;I)V", false));
                method.instructions.remove(node);
            }
            else if (method.name.equals(getName("readEntityFromNBT", "func_70037_a"))) {
                AbstractInsnNode node = method.instructions.getLast();
                while (node.getOpcode() != INVOKEVIRTUAL) node = node.getPrevious();
                while (node.getOpcode() != ALOAD) {
                    node = node.getPrevious();
                    method.instructions.remove(node.getNext());
                }
                method.instructions.insert(node,hook("EntityXPOrb$getXPValue", "(Lnet/minecraft/nbt/NBTTagCompound;)I") );
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
                            } else {
                                method.instructions.insertBefore(node, new VarInsnNode(ALOAD, 1));
                                method.instructions.insertBefore(node, hook("RenderXPOrb$getSize", "(Lnet/minecraft/entity/item/EntityXPOrb;)F"));
                                iterator.remove();
                            }

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
