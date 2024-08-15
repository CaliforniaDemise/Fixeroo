package surreal.fixeroo.core.transformers;

import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

public class GolemTransformer extends TypicalTransformer {

    public static void transformBlockPumpkin(ClassNode cls) {
        for (MethodNode method : cls.methods) {
            // Remove unnecessary random tick
            if (method.name.equals("<init>")) {
                for (AbstractInsnNode n : method.instructions.toArray()) {
                    if (n.getOpcode() == ICONST_1) {
                        for (int i = 0; i < 4; i++) {
                            method.instructions.remove(n.getPrevious());
                            n = n.getNext();
                        }

                        break;
                    }
                }
            }
            // There isn't much people use dispensers let alone know about this feature
            else if (method.name.equals(deobf ? "canDispenserPlace" : "func_176390_d")) {
                AbstractInsnNode node = null;
                boolean remove = false;
                for (AbstractInsnNode n : method.instructions.toArray()) {
                    if (n.getOpcode() == ALOAD) remove = true;
                    if (remove) {
                        method.instructions.remove(n);
                        if (n.getOpcode() == IFNULL) {
                            node = n.getNext();
                            break;
                        }
                    }
                }

                if (node != null) {
                    InsnList list = new InsnList();

                    list.add(new VarInsnNode(ALOAD, 1));
                    // Get Pos Down
                    list.add(new VarInsnNode(ALOAD, 2));
                    list.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", deobf ? "down" : "func_177977_b", "()Lnet/minecraft/util/math/BlockPos;", false));
                    // Get BlockState Down Block
                    list.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/World", deobf ? "getBlockState" : "func_180495_p", "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", false));
                    list.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/block/state/IBlockState", deobf ? "getBlock" : "func_177230_c", "()Lnet/minecraft/block/Block;", false));
                    list.add(new VarInsnNode(ASTORE, 3));

                    // Snow Block Check
                    LabelNode l1 = new LabelNode();
                    list.add(new VarInsnNode(ALOAD, 3));
                    list.add(new FieldInsnNode(GETSTATIC, "net/minecraft/init/Blocks", deobf ? "SNOW" : "field_150433_aE", "Lnet/minecraft/block/Block;"));
                    list.add(new JumpInsnNode(IF_ACMPNE, l1));

                    // Iron Block Check
                    LabelNode l2 = new LabelNode();
                    list.add(new VarInsnNode(ALOAD, 3));
                    list.add(new FieldInsnNode(GETSTATIC, "net/minecraft/init/Blocks", deobf ? "SNOW" : "field_150433_aE", "Lnet/minecraft/block/Block;"));
                    list.add(new JumpInsnNode(IF_ACMPNE, l2));

                    method.instructions.insertBefore(node, list);
                }
            }
            else if (method.name.equals(deobf ? "trySpawnGolem" : "func_180673_e")) {
                AbstractInsnNode node = null;
                boolean remove = false;
                for (AbstractInsnNode n : method.instructions.toArray()) {
                    if (n.getOpcode() == ALOAD) remove = true;
                    if (n.getOpcode() == RETURN) {
                        node = n;
                        break;
                    }
                    else if (remove) method.instructions.remove(n);
                }

                if (node != null) {
                    InsnList list = new InsnList();
                    // Snowman Pattern
                    list.add(new VarInsnNode(ALOAD, 0));
                    list.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/block/BlockPumpkin", deobf ? "getSnowmanPattern" : "func_176391_l", "()Lnet/minecraft/block/state/pattern/BlockPattern;", false));
                    // Iron Golem Pattern
                    list.add(new VarInsnNode(ALOAD, 0));
                    list.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/block/BlockPumpkin", deobf ? "getGolemPattern" : "func_176388_T", "()Lnet/minecraft/block/state/pattern/BlockPattern;", false));
                    // -----------
                    list.add(new VarInsnNode(ALOAD, 1));
                    list.add(new VarInsnNode(ALOAD, 2));
                    list.add(hook("BlockPumpkin$trySpawnGolem", "(Lnet/minecraft/block/state/pattern/BlockPattern;Lnet/minecraft/block/state/pattern/BlockPattern;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"));

                    method.instructions.insertBefore(node, list);
                }
            }
            else if (method.name.equals(deobf ? "getGolemPattern" : "func_176388_T")) {
                AbstractInsnNode node = null;
                for (AbstractInsnNode n : method.instructions.toArray()) {
                    if (n.getOpcode() == BIPUSH && ((IntInsnNode) n).operand == 126) {
                        node = n.getNext();
                        break;
                    }
                }

                if (node != null) {
                    for (int i = 0; i < 3; i++) {
                        node = node.getNext();
                        method.instructions.remove(node.getPrevious());
                    }

                    method.instructions.insertBefore(node, hook("BlockPumpkin$predicateAny", "()Lcom/google/common/base/Predicate;"));
                }
            }
        }
    }
}
