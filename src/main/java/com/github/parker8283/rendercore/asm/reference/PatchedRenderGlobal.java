package com.github.parker8283.rendercore.asm.reference;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumWorldBlockLayer;

import com.github.parker8283.rendercore.asm.Hooks;
import com.google.common.collect.Lists;

public abstract class PatchedRenderGlobal extends RenderGlobal {

    //Here to prevent erroring
    private final Minecraft mc;
    private List<ContainerLocalRenderInformation> renderInfos = Lists.newArrayListWithCapacity(69696);
    private ChunkRenderContainer renderContainer;

    //Here to prevent erroring
    public PatchedRenderGlobal(Minecraft mcIn) {
        super(mcIn);
        this.mc = mcIn;
    }

    public int renderBlockLayer(EnumWorldBlockLayer blockLayerIn, double partialTicks, int pass, Entity entityIn)
    {
        // ...

        //Here to prevent erroring
        int i1 = 0;

        this.mc.mcProfiler.endStartSection("render_" + blockLayerIn);
        //INJECTION START
        Hooks.onPreRenderWorldLayer(this, renderInfos, renderContainer, blockLayerIn, partialTicks, pass, entityIn);
        //INJECTION END
        this.renderBlockLayer(blockLayerIn);
        //INJECTION START
        Hooks.onPostRenderWorldLayer(this, renderInfos, renderContainer, blockLayerIn, partialTicks, pass, entityIn);
        //INJECTION END
        this.mc.mcProfiler.endSection();
        return i1;
    }

    private void renderBlockLayer(EnumWorldBlockLayer blockLayerIn)
    {
        //Here to prevent erroring
    }
}
