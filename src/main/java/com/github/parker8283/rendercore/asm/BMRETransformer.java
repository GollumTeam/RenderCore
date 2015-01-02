package com.github.parker8283.rendercore.asm;

import static com.github.parker8283.rendercore.asm.CorePlugin.log;
import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.github.parker8283.rendercore.asm.helper.ASMHelper;
import com.github.parker8283.rendercore.asm.helper.ObfHelper;

public class BMRETransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if(transformedName.equals("net.minecraft.client.renderer.BlockModelShapes")) {
            log.info("Found BlockModelShapes class");
            boolean isObfuscated = !name.equals(transformedName);
            ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
            MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "(" + ObfHelper.getDescriptor("net.minecraft.client.resources.model.ModelManager") + ")V");
            if(methodNode != null) {
                log.info("Found constructor");
                AbstractInsnNode targetNode = ASMHelper.findLastInstructionWithOpcode(methodNode, RETURN);
                InsnList toInject = new InsnList();
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "onBlockModelRegister", "(" + ObfHelper.getDescriptor("net.minecraft.client.renderer.BlockModelShapes") + ")V", false));
                methodNode.instructions.insertBefore(targetNode, toInject);
                log.info("BlockModelRegisterEvent posting method injected");
            } else {
                log.fatal("Could not find constructor. Because this is a critical part of many mods, gameplay will end. Please report to Parker8283.", new RuntimeException("Could not find constructor for BlockModelShapes class"));
                FMLCommonHandler.instance().exitJava(1, false);
            }
            return ASMHelper.writeClassToBytes(classNode);
        }
        return basicClass;
    }
}
