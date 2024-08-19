package surreal.fixeroo.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;
import surreal.fixeroo.FixerooConfig;
import surreal.fixeroo.core.transformers.GolemTransformer;
import surreal.fixeroo.core.transformers.XPOrbTransformer;

import static org.objectweb.asm.Opcodes.*;

public class FixerooTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
       boolean check = false;

        switch (transformedName) {
            case "net.minecraft.entity.item.EntityXPOrb": basicClass = transformEntityXPOrb(basicClass); check = true; break;
            case "net.minecraft.client.renderer.entity.RenderXPOrb": basicClass = transformRenderXPOrb(basicClass); check = true; break;
            case "net.minecraft.block.BlockPumpkin": basicClass = transformBlockPumpkin(basicClass); check = true; break;
        }

        if (check) FixerooPlugin.LOGGER.info("Transforming {}", transformedName);
        return basicClass;
    }

    private byte[] transformEntityXPOrb(byte[] bytes) {
        ClassNode cls = read(bytes);
        if (FixerooConfig.xpOrbClump.enable) XPOrbTransformer.transformEntityXPOrb(cls);
        return write(cls);
    }

    private byte[] transformRenderXPOrb(byte[] bytes) {
        ClassNode cls = read(bytes);
        if (FixerooConfig.xpOrbClump.changeOrbSize) XPOrbTransformer.transformRenderXPOrb(cls);
        return write(cls);
    }

    private byte[] transformBlockPumpkin(byte[] bytes) {
        ClassNode cls = read(bytes);
        if (FixerooConfig.golemTweaks.enable) GolemTransformer.transformBlockPumpkin(cls);
        return write(cls);
    }

    private ClassNode read(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode cls = new ClassNode();
        reader.accept(cls, 0);
        return cls;
    }

    private byte[] write(ClassNode cls, int options) {
        ClassWriter writer = new ClassWriter(options);
        cls.accept(writer);
        return writer.toByteArray();
    }

    private byte[] write(ClassNode cls) {
        return write(cls, ClassWriter.COMPUTE_MAXS);
    }

    public static MethodInsnNode hook(String name, String desc) {
        return new MethodInsnNode(INVOKESTATIC, "surreal/fixeroo/core/FixerooHooks", name, desc, false);
    }
}
