package com.github.parker8283.rendercore;

import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;

/**
 * Interface to allow definition of a custom item rendering method.<br/>
 * Use Forge's ModelBakeEvent to register this model with the mdoelRegistry.
 */
public interface IDynamicItemModel extends IBakedModel {

    /**
     * Called from RenderItem to render the ItemStack.
     * @param stack The ItemStack to be rendered.
     */
    void render(ItemStack stack);
}
