package com.github.parker8283.rendercore.asm.helper;

import java.util.HashMap;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class ObfHelper {

    // initialized in CorePlugin.injectData
    public static boolean obfuscated = false;
    private static HashMap<String, String> classNameToObfClassNameCache = new HashMap<String, String>();
    private static HashMap<String, String> obfClassNameToClassNameCache = new HashMap<String, String>();

    private static void cacheObfClassMapping(String obfClassName, String className) {
        obfClassNameToClassNameCache.put(obfClassName, className);
        classNameToObfClassNameCache.put(className, obfClassName);
    }

    public static String toDeobfClassName(String obfClassName) {
        if(obfuscated) {
            if(!obfClassNameToClassNameCache.containsKey(obfClassName)) {
                cacheObfClassMapping(obfClassName, FMLDeobfuscatingRemapper.INSTANCE.map(obfClassName.replace('.', '/')).replace('/', '.'));
            }
            return obfClassNameToClassNameCache.get(obfClassName);
        } else
            return obfClassName;
    }

    public static String toObfClassName(String deobfClassName) {
        if(obfuscated) {
            if(!classNameToObfClassNameCache.containsKey(deobfClassName)) {
                cacheObfClassMapping(FMLDeobfuscatingRemapper.INSTANCE.unmap(deobfClassName.replace('.', '/')).replace('/', '.'), deobfClassName);
            }
            return classNameToObfClassNameCache.get(deobfClassName);
        } else
            return deobfClassName;
    }



    public static String getInternalClassName(String className) {
        return toObfClassName(className).replace('.', '/');
    }

    public static String getDescriptor(String className) {
        return "L" + getInternalClassName(className) + ";";
    }

    public static String getCorrectFieldOrMethodName(boolean isObfuscated, String notchName, String srgName, String mcpName) {
        return isObfuscated ? notchName : obfuscated ? srgName : mcpName;
    }
}
