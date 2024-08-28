package surreal.fixeroo.core.transformers;

import org.objectweb.asm.tree.*;

import java.util.Iterator;

import static org.objectweb.asm.Opcodes.*;

public class ElytraTransformer extends TypicalTransformer {

    public static byte[] transformModelPlayer(String transformedName, byte[] basicClass) {
        ClassNode cls = read(transformedName, basicClass);
        for (MethodNode method : cls.methods) {
            if (method.name.equals(getName("render", "func_78088_a"))) {
                String entityLivingBase = "net/minecraft/entity/EntityLivingBase";
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                int i = 0;
                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();
                    if (node.getOpcode() == IFEQ) {
                        i++;
                        if (i == 2) {
                            LabelNode label = ((JumpInsnNode) node).label;
                            InsnList list = new InsnList();
                            list.add(new VarInsnNode(ALOAD, 1));
                            list.add(new TypeInsnNode(INSTANCEOF, entityLivingBase));
                            LabelNode l_ef = new LabelNode();
                            list.add(new JumpInsnNode(IFEQ, l_ef));
                            list.add(new VarInsnNode(ALOAD, 1));
                            list.add(new TypeInsnNode(CHECKCAST, entityLivingBase));
                            list.add(new MethodInsnNode(INVOKEVIRTUAL, entityLivingBase, getName("isElytraFlying", "func_184613_cA"), "()Z", false));
                            list.add(new JumpInsnNode(IFNE, label));
                            list.add(l_ef);
                            list.add(new FrameNode(F_SAME, 0, null, 0, null));
                            method.instructions.insert(node, list);
                            break;
                        }
                    }
                }
            }
        }
        return write(cls);
    }

    public static byte[] transformRenderPlayer(String transformedName, byte[] basicClass) {
        ClassNode cls = read(transformedName, basicClass);
        for (MethodNode method : cls.methods) {
            if (method.name.equals(getName("doRender", "func_76986_a"))) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();
                    if (node.getOpcode() == INVOKEVIRTUAL && ((MethodInsnNode) node).name.equals(getName("isSneaking", "func_70093_af"))) {
                        InsnList list = new InsnList();
                        list.add(new VarInsnNode(ALOAD, 1));
                        list.add(hook("RenderPlayer$isSneak", "(ZLnet/minecraft/entity/EntityLivingBase;)Z"));
                        method.instructions.insert(node, list);
                        break;
                    }
                }
            }
            else if (method.name.equals("setModelVisibilities")) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();
                    if (node.getOpcode() == INVOKEVIRTUAL && ((MethodInsnNode) node).name.equals(getName("isSneaking", "func_70093_af"))) {
                        InsnList list = new InsnList();
                        list.add(new VarInsnNode(ALOAD, 1));
                        list.add(hook("RenderPlayer$isSneak", "(ZLnet/minecraft/entity/EntityLivingBase;)Z"));
                        method.instructions.insert(node, list);
                        break;
                    }
                }
                break;
            }
        }
        return write(cls);
    }

    public static byte[] transformEntityPlayer(String transformedName, byte[] basicClass) {
        ClassNode cls = read(transformedName, basicClass);
        for (MethodNode method : cls.methods) {
            if (method.name.equals(getName("getEyeHeight", "func_70047_e"))) {
                AbstractInsnNode node = method.instructions.getLast();
                while (node.getOpcode() != FRETURN) node = node.getPrevious();
                InsnList list = new InsnList();
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(hook("EntityPlayer$getEyeHeight", "(FLnet/minecraft/entity/player/EntityPlayer;)F"));
                method.instructions.insertBefore(node, list);
                break;
            }
        }
        return write(cls);
    }
}
