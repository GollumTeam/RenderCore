package com.github.parker8283.rendercore.asm;

import static org.objectweb.asm.Opcodes.*;
import static com.github.parker8283.rendercore.asm.CorePlugin.log;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.github.parker8283.rendercore.RenderCore;
import com.github.parker8283.rendercore.asm.helper.ASMHelper;
import com.github.parker8283.rendercore.asm.helper.ObfHelper;

public class TEISRTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if(transformedName.equals("net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer")) {
            log.info("Found TileEntityItemStackRenderer Class");
            boolean isObfuscated = !name.equals(transformedName);
            ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
            MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, ObfHelper.getCorrectFieldOrMethodName(isObfuscated, "a", "func_179022_a", "renderByItem"), "(" + ObfHelper.getDescriptor("net.minecraft.item.ItemStack") + ")V");
            if(methodNode != null) {
                log.info("Found renderByItem method");
                AbstractInsnNode targetNode = ASMHelper.findLastInstructionWithOpcode(methodNode, GETSTATIC);
                LabelNode L4 = (LabelNode)targetNode.getNext().getNext().getNext().getNext().getNext().getNext().getNext().getNext();
                LabelNode NEW = new LabelNode();
                InsnList toInject = new InsnList();
                toInject.add(new VarInsnNode(ALOAD, 2));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(RenderCore.class), "hasBlockForTESRCall", "(" + ObfHelper.getDescriptor("net.minecraft.block.Block") + ")Z", false));
                toInject.add(new JumpInsnNode(IFEQ, NEW));
                toInject.add(new FieldInsnNode(GETSTATIC, ObfHelper.getInternalClassName("net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher"), ObfHelper.getCorrectFieldOrMethodName(isObfuscated, "a", "field_147556_a", "instance"), ObfHelper.getDescriptor("net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher")));
                toInject.add(new VarInsnNode(ALOAD, 2));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(RenderCore.class), "getTEForTESRCall", "(" + ObfHelper.getDescriptor("net.minecraft.block.Block") + ")" + ObfHelper.getDescriptor("net.minecraft.tileentity.TileEntity"), false));
                toInject.add(new InsnNode(DCONST_0));
                toInject.add(new InsnNode(DCONST_0));
                toInject.add(new InsnNode(DCONST_0));
                toInject.add(new InsnNode(FCONST_0));
                toInject.add(new MethodInsnNode(INVOKEVIRTUAL, ObfHelper.getInternalClassName("net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher"), ObfHelper.getCorrectFieldOrMethodName(isObfuscated, "a", "func_147549_a", "renderTileEntityAt"), "(" + ObfHelper.getDescriptor("net.minecraft.tileentity.TileEntity") + "DDDF)V", false));
                toInject.add(new JumpInsnNode(GOTO, L4));
                toInject.add(NEW);
                methodNode.instructions.insertBefore(targetNode, toInject);
                log.info("TEISR hook injected");
            } else {
                log.fatal("Could not find renderByItem method. Because this is a critical part of many mods, gameplay will end. Please report to Parker8283.", new RuntimeException("Could not find renderByItem method in TileEntityItemStackRenderer class"));
                FMLCommonHandler.instance().exitJava(1, false);
            }
            return ASMHelper.writeClassToBytes(classNode);
        }
        return basicClass;
    }
}
