package com.github.parker8283.rendercore.asm;

import static org.objectweb.asm.Opcodes.*;
import static com.github.parker8283.rendercore.asm.CorePlugin.log;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.github.parker8283.rendercore.asm.helper.ASMHelper;
import com.github.parker8283.rendercore.asm.helper.ObfHelper;

public class WorldTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if(transformedName.equals("net.minecraft.client.renderer.RenderGlobal")) {
            log.info("Found RenderGlobal class");
            boolean isObfuscated = !name.equals(transformedName);
            ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
            MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, ObfHelper.getCorrectFieldOrMethodName(isObfuscated, "a", "func_174977_a", "renderBlockLayer"), "(" + ObfHelper.getDescriptor("net.minecraft.util.EnumWorldBlockLayer") + "DI" + ObfHelper.getDescriptor("net.minecraft.entity.Entity") + ")I");
            if(methodNode != null) {
                log.info("Found renderBlockLayer method");
                AbstractInsnNode targetNode = ASMHelper.findLastInstructionWithOpcode(methodNode, INVOKESPECIAL).getPrevious().getPrevious();
                InsnList toInject = new InsnList();
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new FieldInsnNode(GETFIELD, ObfHelper.getInternalClassName("net.minecraft.client.renderer.RenderGlobal"), ObfHelper.getCorrectFieldOrMethodName(isObfuscated, "m", "field_72755_R", "renderInfos"), "Ljava/util/List;"));
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new FieldInsnNode(GETFIELD, ObfHelper.getInternalClassName("net.minecraft.client.renderer.RenderGlobal"), ObfHelper.getCorrectFieldOrMethodName(isObfuscated, "N", "field_174996_N", "renderContainer"), ObfHelper.getDescriptor("net.minecraft.client.renderer.ChunkRenderContainer")));
                toInject.add(new VarInsnNode(ALOAD, 1));
                toInject.add(new VarInsnNode(DLOAD, 2));
                toInject.add(new VarInsnNode(ILOAD, 4));
                toInject.add(new VarInsnNode(ALOAD, 5));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "onPreRenderWorldLayer", "(" + ObfHelper.getDescriptor("net.minecraft.client.renderer.RenderGlobal") + "Ljava/util/List;" + ObfHelper.getDescriptor("net.minecraft.client.renderer.ChunkRenderContainer") + ObfHelper.getDescriptor("net.minecraft.util.EnumWorldBlockLayer") + "DI" + ObfHelper.getDescriptor("net.minecraft.entity.Entity") + ")V", false));
                methodNode.instructions.insertBefore(targetNode, toInject);
                targetNode = ASMHelper.findLastInstructionWithOpcode(methodNode, ALOAD);
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new FieldInsnNode(GETFIELD, ObfHelper.getInternalClassName("net.minecraft.client.renderer.RenderGlobal"), ObfHelper.getCorrectFieldOrMethodName(isObfuscated, "m", "field_72755_R", "renderInfos"), "Ljava/util/List;"));
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new FieldInsnNode(GETFIELD, ObfHelper.getInternalClassName("net.minecraft.client.renderer.RenderGlobal"), ObfHelper.getCorrectFieldOrMethodName(isObfuscated, "N", "field_174996_N", "renderContainer"), ObfHelper.getDescriptor("net.minecraft.client.renderer.ChunkRenderContainer")));
                toInject.add(new VarInsnNode(ALOAD, 1));
                toInject.add(new VarInsnNode(DLOAD, 2));
                toInject.add(new VarInsnNode(ILOAD, 4));
                toInject.add(new VarInsnNode(ALOAD, 5));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "onPostRenderWorldLayer", "(" + ObfHelper.getDescriptor("net.minecraft.client.renderer.RenderGlobal") + "Ljava/util/List;" + ObfHelper.getDescriptor("net.minecraft.client.renderer.ChunkRenderContainer") + ObfHelper.getDescriptor("net.minecraft.util.EnumWorldBlockLayer") + "DI" + ObfHelper.getDescriptor("net.minecraft.entity.Entity") + ")V", false));
                methodNode.instructions.insertBefore(targetNode, toInject);
                log.info("Half of World rendering hook injected");
            } else {
                log.fatal("Could not find renderWorldLayer method. Because this is a critical part of many mods, gameplay will end. Please report to Parker8283.", new RuntimeException("Could not find renderWorldLayer method in RenderGlobal class"));
                FMLCommonHandler.instance().exitJava(1, false);
            }
            return ASMHelper.writeClassToBytes(classNode);
        }
        if(transformedName.equals("net.minecraft.client.renderer.chunk.RenderChunk")) {
            log.info("Found RenderChunk class");
            boolean isObfuscated = !name.equals(transformedName);
            ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
            MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, ObfHelper.getCorrectFieldOrMethodName(isObfuscated, "a", "func_178570_a", "resortTransparency"), "(FFF" + ObfHelper.getDescriptor("net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator") + ")V");
            if(methodNode != null) {
                log.info("Found resortTransparency method");
                AbstractInsnNode targetNode = ASMHelper.findFirstInstructionWithOpcode(methodNode, IFNE).getNext().getNext().getNext();
                InsnList toInject = new InsnList();
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new FieldInsnNode(GETSTATIC, ObfHelper.getInternalClassName("net.minecraft.util.EnumWorldBlockLayer"), isObfuscated ? "d" : "TRANSLUCENT", ObfHelper.getDescriptor("net.minecraft.util.EnumWorldBlockLayer")));
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new FieldInsnNode(GETFIELD, ObfHelper.getInternalClassName("net.minecraft.client.renderer.chunk.RenderChunk"), ObfHelper.getCorrectFieldOrMethodName(isObfuscated, "f", "field_178586_f", "position"), ObfHelper.getDescriptor("net.minecraft.util.BlockPos")));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "onPreRebuildChunk", "(" + ObfHelper.getDescriptor("net.minecraft.client.renderer.chunk.RenderChunk") + ObfHelper.getDescriptor("net.minecraft.util.EnumWorldBlockLayer") + ObfHelper.getDescriptor("net.minecraft.util.BlockPos") + ")V", false));
                methodNode.instructions.insertBefore(targetNode, toInject);
                targetNode = ASMHelper.findLastInstructionWithOpcode(methodNode, INVOKESPECIAL).getNext();
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new FieldInsnNode(GETSTATIC, ObfHelper.getInternalClassName("net.minecraft.util.EnumWorldBlockLayer"), isObfuscated ? "d" : "TRANSLUCENT", ObfHelper.getDescriptor("net.minecraft.util.EnumWorldBlockLayer")));
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new FieldInsnNode(GETFIELD, ObfHelper.getInternalClassName("net.minecraft.client.renderer.chunk.RenderChunk"), ObfHelper.getCorrectFieldOrMethodName(isObfuscated, "f", "field_178586_f", "position"), ObfHelper.getDescriptor("net.minecraft.util.BlockPos")));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "onPreRebuildChunk", "(" + ObfHelper.getDescriptor("net.minecraft.client.renderer.chunk.RenderChunk") + ObfHelper.getDescriptor("net.minecraft.util.EnumWorldBlockLayer") + ObfHelper.getDescriptor("net.minecraft.util.BlockPos") + ")V", false));
                methodNode.instructions.insertBefore(targetNode, toInject);
                log.info("Three-quarters of World rendering hook injected");
            } else {
                log.fatal("Could not find resortTransparency method. Because this is a critical part of many mods, gameplay will end. Please report to Parker8283.", new RuntimeException("Could not find resortTransparency method in RenderChunk class"));
                FMLCommonHandler.instance().exitJava(1, false);
            }
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, ObfHelper.getCorrectFieldOrMethodName(isObfuscated, "b", "func_178581_b", "rebuildChunk"), "(FFF" + ObfHelper.getDescriptor("net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator") + ")V");
            if(methodNode != null) {
                log.info("Found rebuildChunk method");
                AbstractInsnNode targetNode = ASMHelper.findPreviousInstructionWithOpcode(ASMHelper.findLastInstructionWithOpcode(methodNode, INVOKESPECIAL), INVOKESPECIAL).getPrevious().getPrevious().getPrevious();
                InsnList toInject = new InsnList();
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new VarInsnNode(ALOAD, 15));
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new FieldInsnNode(GETFIELD, ObfHelper.getInternalClassName("net.minecraft.client.renderer.chunk.RenderChunk"), ObfHelper.getCorrectFieldOrMethodName(isObfuscated, "f", "field_178586_f", "position"), ObfHelper.getDescriptor("net.minecraft.util.BlockPos")));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "onPreRebuildChunk", "(" + ObfHelper.getDescriptor("net.minecraft.client.renderer.chunk.RenderChunk") + ObfHelper.getDescriptor("net.minecraft.util.EnumWorldBlockLayer") + ObfHelper.getDescriptor("net.minecraft.util.BlockPos") + ")V", false));
                methodNode.instructions.insertBefore(targetNode, toInject);
                targetNode = ASMHelper.findPreviousInstructionWithOpcode(ASMHelper.findLastInstructionWithOpcode(methodNode, INVOKESTATIC), INVOKESTATIC);
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new VarInsnNode(ALOAD, 15));
                toInject.add(new VarInsnNode(ALOAD, 13));
                toInject.add(new VarInsnNode(ALOAD, 9));
                toInject.add(new VarInsnNode(ALOAD, 12));
                toInject.add(new VarInsnNode(ALOAD, 5));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "onBlockRender", "(" + ObfHelper.getDescriptor("net.minecraft.client.renderer.chunk.RenderChunk") + ObfHelper.getDescriptor("net.minecraft.util.EnumWorldBlockLayer") + ObfHelper.getDescriptor("net.minecraft.block.state.IBlockState") + ObfHelper.getDescriptor("net.minecraft.world.IBlockAccess") + ObfHelper.getDescriptor("net.minecraft.util.BlockPos") + ObfHelper.getDescriptor("net.minecraft.client.renderer.chunk.CompiledChunk") + ")V", false));
                methodNode.instructions.insertBefore(targetNode, toInject);
                targetNode = ASMHelper.findLastInstructionWithOpcode(methodNode, IINC);
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new VarInsnNode(ALOAD, 15));
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new FieldInsnNode(GETFIELD, ObfHelper.getInternalClassName("net.minecraft.client.renderer.chunk.RenderChunk"), ObfHelper.getCorrectFieldOrMethodName(isObfuscated, "f", "field_178586_f", "position"), ObfHelper.getDescriptor("net.minecraft.util.BlockPos")));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "onPostRebuildChunk", "(" + ObfHelper.getDescriptor("net.minecraft.client.renderer.chunk.RenderChunk") + ObfHelper.getDescriptor("net.minecraft.util.EnumWorldBlockLayer") + ObfHelper.getDescriptor("net.minecraft.util.BlockPos") + ")V", false));
                methodNode.instructions.insertBefore(targetNode, toInject);
                log.info("World rendering hook injected");
            } else {
                log.fatal("Could not find rebuildChunk method. Because this is a critical part of many mods, gameplay will end. Please report to Parker8283.", new RuntimeException("Could not find rebuildChunk method in RenderChunk class"));
                FMLCommonHandler.instance().exitJava(1, false);
            }
            return ASMHelper.writeClassToBytes(classNode);
        }
        return basicClass;
    }
}
