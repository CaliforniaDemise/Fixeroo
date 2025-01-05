package surreal.fixeroo.core.transformers;

import org.objectweb.asm.tree.*;

public class MPTransformer extends TypicalTransformer {

    public static byte[] transformModuleManager(String transformedName, byte[] basicClass) {
        ClassNode cls = read(transformedName, basicClass);
        for (MethodNode method : cls.methods) {
            if (method.name.equals("getInstallCost")) {
                AbstractInsnNode node = method.instructions.getFirst();
                while (node.getOpcode() != GETSTATIC) node = node.getNext();
                InsnList list = new InsnList();
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, cls.name, "hasCustomInstallCost", "(Ljava/lang/String;)Z", false));
                LabelNode l_con = new LabelNode();
                list.add(new JumpInsnNode(IFEQ, l_con));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, "net/machinemuse/powersuits/common/base/ModuleManager", "getCustomInstallCost", "(Ljava/lang/String;)Lnet/minecraft/util/NonNullList;", false));
                list.add(new InsnNode(ARETURN));
                list.add(l_con);
                list.add(new FrameNode(F_SAME, 0, null, 0, null));
                method.instructions.insertBefore(node, list);
                break;
            }
        }
        return write(cls);
    }
}
