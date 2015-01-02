package com.github.parker8283.rendercore.asm;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.MetadataCollection;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import com.github.parker8283.rendercore.RenderCore;
import com.github.parker8283.rendercore.asm.helper.ObfHelper;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

@IFMLLoadingPlugin.MCVersion("1.8") //Technically doesn't matter as this will crash and burn in versions prior to 1.8, but meh.
@IFMLLoadingPlugin.TransformerExclusions({"com.github.parker8283.rendercore.asm."})
public class CorePlugin extends DummyModContainer implements IFMLLoadingPlugin {
    public static Logger log = LogManager.getLogger(RenderCore.MOD_ID);
    private static boolean worldRenderingEnabled = !Boolean.parseBoolean(System.getProperty("renderCore.disableWorldRendering", "false"));
    private static boolean dynamicItemRenderingEnabled = !Boolean.parseBoolean(System.getProperty("renderCore.disableItemRendering", "false"));
    private static boolean customTEISRCallsEnabled = !Boolean.parseBoolean(System.getProperty("renderCore.disableTEISRRendering", "false"));
    private static boolean blockModelRegisterEventEnabled = !Boolean.parseBoolean(System.getProperty("renderCore.disableBlockModelRegisterEvent", "false"));

    public CorePlugin() {
        super(MetadataCollection.from(MetadataCollection.class.getResourceAsStream("/rendercore.info"), RenderCore.MOD_ID).getMetadataForId(RenderCore.MOD_ID, null));
        log.info("Initialized");
    }

    @Override
    public String[] getASMTransformerClass() {
        return buildTransformerList();
    }

    @Override
    public String getModContainerClass() {
        return CorePlugin.class.getName();
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        ObfHelper.obfuscated = (Boolean)data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return worldRenderingEnabled ? CoreAT.class.getName() : null;
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        return true;
    }

    public static boolean isWorldRenderingEnabled() {
        return worldRenderingEnabled;
    }

    public static boolean isDynamicItemRenderingEnabled() {
        return dynamicItemRenderingEnabled;
    }

    public static boolean areCustomTEISRCallsEnabled() {
        return customTEISRCallsEnabled;
    }

    public static boolean isBlockModelRegisterEventEnabled() {
        return blockModelRegisterEventEnabled;
    }

    private String[] buildTransformerList() {
        List<String> ret = Lists.newArrayList();

        if(worldRenderingEnabled) {
            ret.add(WorldTransformer.class.getName());
        } else {
            log.warn("World rendering hooks turned off. This may cause mods to act funny.");
        }

        if(dynamicItemRenderingEnabled) {
            ret.add(ItemTransformer.class.getName());
        } else {
            log.warn("Item rendering hooks turned off. This may cause mods to act funny.");
        }

        if(customTEISRCallsEnabled) {
            ret.add(TEISRTransformer.class.getName());
        } else {
            log.warn("TEISR rendering hooks turned off. This may cause mods to act funny.");
        }

        if(blockModelRegisterEventEnabled) {
            ret.add(BMRETransformer.class.getName());
        } else {
            log.warn("BlockModelRegisterEvent turned off. This may cause mods to act funny.");
        }

        return ret.toArray(new String[ret.size()]);
    }
}
