package surreal.fixeroo.core.transformers.mods;

import org.objectweb.asm.tree.*;
import surreal.fixeroo.core.transformers.TypicalTransformer;

public class TinkersComplementTransformer extends TypicalTransformer {

    /**
     * Fix dupe bug
     **/
    public static byte[] transformItemChisel(String transformedName, byte[] basicClass) {
        ClassNode cls = read(transformedName, basicClass);
        for (MethodNode method : cls.methods) {
            if (method.name.equals("onChisel")) {
                AbstractInsnNode node = method.instructions.getFirst();
                while (node.getOpcode() != ICONST_1) node = node.getNext();
                InsnList list = new InsnList();
                list.add(new VarInsnNode(ALOAD, 3));
                list.add(new VarInsnNode(ALOAD, 3));
                list.add(new MethodInsnNode(INVOKESTATIC, "slimeknights/tconstruct/library/utils/ToolHelper", "isBroken", "(Lnet/minecraft/item/ItemStack;)Z", false));
                list.add(hook("TiCom$fixChiselDupe", "(Lnet/minecraft/item/ItemStack;Z)V"));
                method.instructions.insertBefore(node, list);
                break;
            }
        }
        return write(cls);
    }
}
