package surreal.fixeroo.core.transformers;

import org.objectweb.asm.tree.*;

import java.util.Iterator;

public class MPSTransformer extends TypicalTransformer {

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

    public static byte[] transformMPSSettings(String transformedName, byte[] basicClass) {
        ClassNode cls = read(transformedName, basicClass);
        for (MethodNode method : cls.methods) {
            if (method.name.equals("loadCustomInstallCosts")) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                int i = 0;
                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();
                    if (node.getOpcode() == NEW) {
                        i++;
                        if (i == 3) {
                            for (int g = 0;  g < 3; g++) {
                                iterator.remove();
                                node = iterator.next();
                            }
                            InsnList list = new InsnList();
                            list.add(new TypeInsnNode(NEW, "com/google/gson/GsonBuilder"));
                            list.add(new InsnNode(DUP));
                            list.add(new MethodInsnNode(INVOKESPECIAL, "com/google/gson/GsonBuilder", "<init>", "()V", false));
                            list.add(new MethodInsnNode(INVOKEVIRTUAL, "com/google/gson/GsonBuilder", "setPrettyPrinting", "()Lcom/google/gson/GsonBuilder;", false));
                            list.add(new MethodInsnNode(INVOKEVIRTUAL, "com/google/gson/GsonBuilder", "create", "()Lcom/google/gson/Gson;", false));
                            method.instructions.insertBefore(node, list);
                        }
                    }
                    else if (node instanceof LdcInsnNode) {
                        LdcInsnNode ldc = (LdcInsnNode) node;
                        if (ldc.cst.equals("Shock Absorber")) ldc.cst = "shockAbsorber";
                        else if (ldc.cst.equals("powerArmorComponent")) ldc.cst = "powerarmorcomponent";
                    }
                }
                break;
            }
        }
        writeClass(cls);
        return write(cls);
    }
}
