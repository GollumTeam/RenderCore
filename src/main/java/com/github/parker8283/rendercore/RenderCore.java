package com.github.parker8283.rendercore;

import static com.github.parker8283.rendercore.asm.CorePlugin.log;

import java.util.Collection;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.github.parker8283.rendercore.asm.CorePlugin;
import com.google.common.collect.Maps;

/**
 * Central class for using RenderCore. You should only ever use this class and the interfaces in this package.<br/>
 * You should never touch anything in the asm package.
 *
 * @author Parker8283
 */
public class RenderCore {
    /** The modid for the mod. Here for convenience. */
    public static final String MOD_ID = "RenderCore";
    /**
     * The renderId for blocks that use the IWorldRenderer. Return this value in your block.getRenderType(); method.<br/>
     * It is provided here for convenience, and so in case it changes between versions, it should be automatically compatible with all mods that use it.
     */
    public static final int IWR_RENDER_ID = 4;

    //Private stuff. No touchy touchy.
    private static final Map<Block, TileEntity> customTESRCalls = Maps.newHashMap();
    private static final Map<Block, IWorldRenderer> renderRegistry = Maps.newIdentityHashMap();

    /**
     * A method that returns if the user has disabled features of the mod.<br/>
     * <b>Yes, this is a thing.</b> Why? Because I like providing the options to the users. Also allows turing off things that aren't compatiable with other mods.<br/>
     * Since a config wouldn't be available until I need to know if features are turned off, I use system properties (VM Args) to determine what to turn off.
     * <ul>
     *     <li>{@code -DrenderCore.disableWorldRendering} disables the IWorldRenderer</li>
     *     <li>{@code -DrenderCore.disableItemRendering} disables the IDynamicItemModel</li>
     *     <li>{@code -DrenderCore.disableTEISRRendering} disables the TEISR hooks</li>
     *     <li>{@code -DrenderCore.disableBlockModelRegisterEvent} disables the BlockModelRegisterEvent</li>
     * </ul>
     *
     * Modders, it's up to you to decide if you crash or continue with this knowledge. There are individual methods for each feature to check if you'd like.
     * @return {@code true} if there has been a feature from the mod disabled.
     */
    public static boolean areFeaturesDisabled() {
        return !(CorePlugin.isWorldRenderingEnabled() && CorePlugin.isDynamicItemRenderingEnabled() && CorePlugin.areCustomTEISRCallsEnabled() && CorePlugin.isBlockModelRegisterEventEnabled());
    }

    /**
     * @return {@code false} if the IWorldRenderer has been disabled by the user.
     */
    public static boolean isWorldRenderingEnabled() {
        return CorePlugin.isWorldRenderingEnabled();
    }

    /**
     * @return {@code false} if the IDynamicItemModel has been disabled by the user.
     */
    public static boolean isItemRenderingEnable() {
        return CorePlugin.isDynamicItemRenderingEnabled();
    }

    /**
     * @return {@code false} if the TEISR hooks have been disabled by the user.
     */
    public static boolean isTEISRRenderingEnabled() {
        return CorePlugin.areCustomTEISRCallsEnabled();
    }

    /**
     * @return {@code false} if the BlockModelRegisterEvent has been disabled by the user.
     */
    public static boolean isBMREventEnabled() {
        return CorePlugin.isBlockModelRegisterEventEnabled();
    }

    // Now the good stuff :P

    /**
     * Registers a block to be called by the TEISR when needed.<br/>
     * This is for builtin blocks that are only rendered with TESRs, to make sure they render properly as an ItemStack.<br/>
     * If you'd like control over the rendering, use an {@link IDynamicItemModel}.
     * @param block The block to be rendered.
     * @param tileEntity The TileEntity to be passed into the TESR render method.
     */
    public static void registerTESRCallForBlock(Block block, TileEntity tileEntity) {
        customTESRCalls.put(block, tileEntity);
    }

    /**
     * Registers an IWorldRenderer for the passed in blocks. The blocks only come into effect for the {@code onBlockRender} method.<br/>
     * All other method are called for every IWorldRenderer method.
     * @param renderer The IWorldRenderer instance to register.
     * @param blocks The blocks to be handled by this IWorldRenderer.
     */
    public static void registerWorldRenderingHandler(IWorldRenderer renderer, Block... blocks) {
        for(Block block : blocks) {
            if(renderRegistry.containsKey(block)) {
                log.error(String.format("Duplicate renderer for block %s: have %s, registering %s; ignoring", GameRegistry.findUniqueIdentifierFor(block).toString(), renderRegistry.get(block), renderer));
            } else {
                renderRegistry.put(block, renderer);
            }
        }
    }

    // Really just internal stuff that had to be in this class. I guess you could call them if you'd like, but they aren't really meant for you.

    public static boolean hasBlockForTESRCall(Block block) {
        return customTESRCalls.containsKey(block);
    }

    public static TileEntity getTEForTESRCall(Block block) {
        return customTESRCalls.get(block);
    }

    public static IWorldRenderer getWorldRendererForBlock(Block block) {
        return renderRegistry.get(block);
    }

    public static Collection<IWorldRenderer> getAllWorldRenderers() {
        return renderRegistry.values();
    }
}
