package com.github.parker8283.rendercore.asm.reference;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.settings.GameSettings;

import com.github.parker8283.rendercore.asm.Hooks;

public abstract class PatchedBlockRendererDispatcher extends BlockRendererDispatcher {

    // Here to prevent erroring
    public PatchedBlockRendererDispatcher(BlockModelShapes p_i46237_1_, GameSettings p_i46237_2_) {
        super(p_i46237_1_, p_i46237_2_);
    }

    public void renderBlockBrightness(IBlockState p_175016_1_, float p_175016_2_)
    {
        int i = p_175016_1_.getBlock().getRenderType();

        //INJECTION START
        Hooks.onBlockRenderBrightness(i, p_175016_1_, p_175016_2_);
        //INJECTION END

        // ..
    }
}
