package surreal.fixeroo.core.transformers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class MisrotatedModdedTETransformer extends TypicalTransformer {

    /**
     * {@link TileEntity#shouldRefresh(World, BlockPos, IBlockState, IBlockState)}
     * This is a forge method so no need to worry about obfuscation.
     */
    private static final String TARGET_NAME_REFRESH = "shouldRefresh";
    private static final String TARGET_SIGNATURE_REFRESH = "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/block/state/IBlockState;)Z";

    /**
     * {@link IBlockState#getBlock()}
     */
    private static final String GET_BLOCK_OWNER = "net/minecraft/block/state/IBlockState";
    private static final String TARGET_NAME_BLOCK = getName("getBlock", "func_177230_c");
    private static final String TARGET_SIGNATURE_BLOCK = "()Lnet/minecraft/block/Block;";

    public static byte[] transformModdedTE(String transformedName, byte[] basicClass) {
        ClassNode cn = read(transformedName, basicClass);
        boolean isPresent = false;
        for (MethodNode mn : cn.methods) {
            if (TARGET_NAME_REFRESH.equals(mn.name) && TARGET_SIGNATURE_REFRESH.equals(mn.desc)) {
                isPresent = true;
                logger.error("Method {} is already in the class {}!", TARGET_NAME_REFRESH, transformedName);
                break;
            }
        }
        if (!isPresent) {
            MethodNode mn = new MethodNode(Opcodes.ACC_PUBLIC, TARGET_NAME_REFRESH, TARGET_SIGNATURE_REFRESH, null, null);
            cn.methods.add(mn);
            {
                InsnList il = mn.instructions;
                il.add(new VarInsnNode(ALOAD, 3));
                il.add(new MethodInsnNode(INVOKEINTERFACE, GET_BLOCK_OWNER, TARGET_NAME_BLOCK, TARGET_SIGNATURE_BLOCK, true));
                il.add(new VarInsnNode(ALOAD, 4));
                il.add(new MethodInsnNode(INVOKEINTERFACE, GET_BLOCK_OWNER, TARGET_NAME_BLOCK, TARGET_SIGNATURE_BLOCK, true));
                LabelNode l0 = new LabelNode();
                il.add(new JumpInsnNode(IF_ACMPEQ, l0));
                il.add(new InsnNode(ICONST_1));
                LabelNode l1 = new LabelNode();
                il.add(new JumpInsnNode(GOTO, l1));
                il.add(l0);
                il.add(new FrameNode(F_SAME, 0, null, 0, null));
                il.add(new InsnNode(ICONST_0));
                il.add(l1);
                il.add(new FrameNode(F_SAME1, 0, null, 1, new Object[]{INTEGER}));
                il.add(new InsnNode(IRETURN));
            }
            logger.info("Patched method {} in class {} successfully", TARGET_NAME_REFRESH, transformedName);
        }
        return write(cn);
    }
}
