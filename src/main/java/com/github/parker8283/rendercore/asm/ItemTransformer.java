package com.github.parker8283.rendercore.asm;

import static org.objectweb.asm.Opcodes.*;
import static com.github.parker8283.rendercore.asm.CorePlugin.log;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.github.parker8283.rendercore.asm.helper.ASMHelper;
import com.github.parker8283.rendercore.asm.helper.ObfHelper;

public class ItemTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if(transformedName.equals("net.minecraft.client.renderer.entity.RenderItem")) {
            log.info("Found RenderItem Class");
            boolean isObfuscated = !name.equals(transformedName);
            ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
            MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, ObfHelper.getCorrectFieldOrMethodName(isObfuscated, "a", "func_180454_a", "renderItem"), "(" + ObfHelper.getDescriptor("net.minecraft.item.ItemStack") + ObfHelper.getDescriptor("net.minecraft.client.resources.model.IBakedModel") + ")V");
            if(methodNode != null) {
                log.info("Found renderItem method");
                AbstractInsnNode targetNode = ASMHelper.findFirstInstructionWithOpcode(methodNode, INVOKESTATIC);
                LabelNode NEW = new LabelNode();
                InsnList toInject = new InsnList();
                toInject.add(new VarInsnNode(ALOAD, 1));
                toInject.add(new VarInsnNode(ALOAD, 2));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "renderDynamicItem", "(" + ObfHelper.getDescriptor("net.minecraft.item.ItemStack") + ObfHelper.getDescriptor("net.minecraft.client.resources.model.IBakedModel") + ")Z", false));
                toInject.add(new JumpInsnNode(IFEQ, NEW));
                toInject.add(new InsnNode(RETURN));
                toInject.add(NEW);
                methodNode.instructions.insertBefore(targetNode, toInject);
                log.info("ItemStack hook injected");
            } else {
                log.fatal("Could not find renderItem method. Because this is a critical part of many mods, gameplay will end. Please report to Parker8283.", new RuntimeException("Could not find renderItem method in RenderItem class"));
                FMLCommonHandler.instance().exitJava(1, false);
            }
            return ASMHelper.writeClassToBytes(classNode);
        }
        return basicClass;
    }
}
