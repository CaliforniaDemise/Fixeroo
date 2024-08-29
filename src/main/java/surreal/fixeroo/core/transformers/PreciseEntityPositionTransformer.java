package surreal.fixeroo.core.transformers;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import surreal.fixeroo.FixerooConfig;

import java.util.Iterator;

import static org.objectweb.asm.Opcodes.*;

public class PreciseEntityPositionTransformer extends TypicalTransformer {

    public static byte[] transformVec3i(String transformedName, byte[] basicClass) {
        if (!FixerooConfig.preciseEntityPosition.enable) return basicClass;
        ClassNode cls = read(transformedName, basicClass);
        {
            MethodVisitor visitor = cls.visitMethod(ACC_PRIVATE | ACC_STATIC, "Fixeroo$handlePos", "(D)I", null, null);
            visitor.visitVarInsn(DLOAD, 0);
            visitor.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "abs", "(D)D", false);
            visitor.visitMethodInsn(INVOKESTATIC, "net/minecraft/util/math/MathHelper", getName("floor", "func_76128_c"), "(D)I", false);
            visitor.visitInsn(I2D);
            visitor.visitLdcInsn(0.5D);
            visitor.visitInsn(DADD);
            visitor.visitVarInsn(DSTORE, 2);

            Label l_con = new Label();
            visitor.visitVarInsn(DLOAD, 0);
            visitor.visitInsn(DCONST_0);
            visitor.visitInsn(DCMPG);
            visitor.visitJumpInsn(IFGE, l_con);
            visitor.visitVarInsn(DLOAD, 2);
            visitor.visitLdcInsn(-1.0D);
            visitor.visitInsn(DMUL);
            visitor.visitVarInsn(DSTORE, 2);

            visitor.visitLabel(l_con);
            visitor.visitFrame(F_APPEND, 1, new Object[] { DOUBLE }, 0, null);
            visitor.visitVarInsn(DLOAD, 2);
            visitor.visitInsn(D2I);
            visitor.visitInsn(IRETURN);
        }
        for (MethodNode method : cls.methods) {
            if (method.name.equals("<init>") && method.desc.equals("(DDD)V")) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();
                    if (node.getOpcode() == INVOKESTATIC && ((MethodInsnNode) node).owner.endsWith("MathHelper")) {
                        method.instructions.insertBefore(node, new MethodInsnNode(INVOKESTATIC, cls.name, "Fixeroo$handlePos", "(D)I", false));
                        iterator.remove();
                    }
                }
                break;
            }
        }
        return write(cls);
    }
}
