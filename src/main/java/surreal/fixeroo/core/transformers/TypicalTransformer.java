package surreal.fixeroo.core.transformers;

import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import surreal.fixeroo.core.FixerooPlugin;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.RETURN;

public class TypicalTransformer {

    static Logger logger = FixerooPlugin.getLogger();
    static boolean deobf = FMLLaunchHandler.isDeobfuscatedEnvironment();

    static ClassNode read(String transformedName, byte[] basicClass) {
        logger.info("Transforming {}", transformedName);
        ClassReader reader = new ClassReader(basicClass);
        ClassNode cls = new ClassNode();
        reader.accept(cls, 0);
        return cls;
    }

    static byte[] write(ClassNode cls) {
        return write(cls, ClassWriter.COMPUTE_MAXS);
    }

    static byte[] write(ClassNode cls, int writerOptions) {
        ClassWriter writer = new ClassWriter(writerOptions);
        cls.accept(writer);
        return writer.toByteArray();
    }

    static void writeClass(ClassNode cls) {
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

    static MethodInsnNode hook(String name, String desc) {
        return new MethodInsnNode(INVOKESTATIC, "surreal/fixeroo/core/FixerooHooks", name, desc, false);
    }

    static String getName(String mcp, String srg) {
        return FMLLaunchHandler.isDeobfuscatedEnvironment() ? mcp : srg;
    }

    static AbstractInsnNode getReturn(InsnList instructions) {
        AbstractInsnNode node = instructions.getLast();
        while (node.getOpcode() != RETURN) node = node.getPrevious();
        return node;
    }
}
