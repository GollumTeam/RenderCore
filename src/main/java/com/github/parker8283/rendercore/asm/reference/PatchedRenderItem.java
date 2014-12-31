package com.github.parker8283.rendercore.asm.reference;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.item.ItemStack;

import com.github.parker8283.rendercore.asm.Hooks;

public abstract class PatchedRenderItem extends RenderItem {

    //Here to prevent erroring
    public PatchedRenderItem(TextureManager textureManager, ModelManager modelManager) {
        super(textureManager, modelManager);
    }

    public void renderItem(ItemStack stack, IBakedModel model)
    {
        //INJECTION START
        if (Hooks.renderDynamicItem(stack, model)) return;
        //INJECTION END
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5F, 0.5F, 0.5F);

        // ...
    }
}
