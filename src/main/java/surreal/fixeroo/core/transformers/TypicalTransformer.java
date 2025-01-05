package surreal.fixeroo.core.transformers;

import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import surreal.fixeroo.core.FixerooPlugin;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class TypicalTransformer implements Opcodes {

    static Logger logger = FixerooPlugin.getLogger();

    protected static ClassNode read(String transformedName, byte[] basicClass) {
        logger.info("Transforming {}", transformedName);
        ClassReader reader = new ClassReader(basicClass);
        ClassNode cls = new ClassNode();
        reader.accept(cls, 0);
        return cls;
    }

    protected static byte[] write(ClassNode cls) {
        return write(cls, ClassWriter.COMPUTE_MAXS);
    }

    protected static byte[] write(ClassNode cls, int writerOptions) {
        ClassWriter writer = new ClassWriter(writerOptions);
        cls.accept(writer);
        return writer.toByteArray();
    }

    protected static void writeClass(ClassNode cls) {
        if (!FMLLaunchHandler.isDeobfuscatedEnvironment()) return;
        File file = new File("classOut/" + cls.name + ".class");
        file.getParentFile().mkdirs();
        try {
            OutputStream os = Files.newOutputStream(file.toPath());
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            cls.accept(writer);
            os.write(writer.toByteArray());
            os.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static MethodInsnNode hook(String name, String desc) {
        return new MethodInsnNode(INVOKESTATIC, "surreal/fixeroo/core/FixerooHooks", name, desc, false);
    }

    protected static String getName(String mcp, String srg) {
        return FMLLaunchHandler.isDeobfuscatedEnvironment() ? mcp : srg;
    }

    protected static AbstractInsnNode getReturn(InsnList instructions) {
        AbstractInsnNode node = instructions.getLast();
        while (node.getOpcode() != RETURN) node = node.getPrevious();
        return node;
    }
}
