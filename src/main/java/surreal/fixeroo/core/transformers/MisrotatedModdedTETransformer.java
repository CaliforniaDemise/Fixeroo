package surreal.fixeroo.core.transformers;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.*;

public class MisrotatedModdedTETransformer extends TypicalTransformer {

    public static byte[] transformModdedTE(String transformedName, byte[] basicClass) {
        ClassNode cls = read(transformedName, basicClass);
        for (MethodNode method : cls.methods) {
            if (method.name.equals("shouldRefresh") && method.desc.equals("(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/block/state/IBlockState;)Z")) {
                return basicClass;
            }
        }
        {
            MethodVisitor shouldRefresh = cls.visitMethod(ACC_PUBLIC, "shouldRefresh", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/block/state/IBlockState;)Z", null, null);
            shouldRefresh.visitVarInsn(ALOAD, 3);
            shouldRefresh.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", getName("getBlock", "func_177230_c"), "()Lnet/minecraft/block/Block;", true);
            shouldRefresh.visitVarInsn(ALOAD, 4);
            shouldRefresh.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", getName("getBlock", "func_177230_c"), "()Lnet/minecraft/block/Block;", true);
            Label l0 = new Label();
            shouldRefresh.visitJumpInsn(IF_ACMPEQ, l0);
            shouldRefresh.visitInsn(ICONST_1);
            Label l1 = new Label();
            shouldRefresh.visitJumpInsn(GOTO, l1);
            shouldRefresh.visitLabel(l0);
            shouldRefresh.visitFrame(F_SAME, 0, null, 0, null);
            shouldRefresh.visitInsn(ICONST_0);
            shouldRefresh.visitLabel(l1);
            shouldRefresh.visitFrame(F_SAME1, 0, null, 1, new Object[] { INTEGER });
            shouldRefresh.visitInsn(IRETURN);
        }
        return write(cls);
    }
}
