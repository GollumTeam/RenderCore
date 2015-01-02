package com.github.parker8283.rendercore.asm;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;

import com.github.parker8283.rendercore.IDynamicItemModel;
import com.github.parker8283.rendercore.IWorldRenderer;
import com.github.parker8283.rendercore.RenderCore;

public class Hooks {

    public static boolean renderDynamicItem(ItemStack stack, IBakedModel model) {
        if(model instanceof IDynamicItemModel) {
            ((IDynamicItemModel)model).render(stack);
            return true;
        }
        return false;
    }

    public static void onPreRenderWorldLayer(RenderGlobal renderer, List<RenderGlobal.ContainerLocalRenderInformation> renderInfos, ChunkRenderContainer chunkContainer, EnumWorldBlockLayer layer, double partialTicks, int pass, Entity entity) {
        for(IWorldRenderer worldRenderer : RenderCore.getAllWorldRenderers()) {
            worldRenderer.onPreRenderLayer(renderer, renderInfos, chunkContainer, partialTicks, pass, entity);
        }
    }

    public static void onPostRenderWorldLayer(RenderGlobal renderer, List<RenderGlobal.ContainerLocalRenderInformation> renderInfos, ChunkRenderContainer chunkContainer, EnumWorldBlockLayer layer, double partialTicks, int pass, Entity entity) {
        for(IWorldRenderer worldRenderer : RenderCore.getAllWorldRenderers()) {
            worldRenderer.onPostRenderLayer(renderer, renderInfos, chunkContainer, partialTicks, pass, entity);
        }
    }

    public static void onPreRebuildChunk(RenderChunk renderChunk, EnumWorldBlockLayer layer, BlockPos pos) {
        for(IWorldRenderer worldRenderer : RenderCore.getAllWorldRenderers()) {
            worldRenderer.onPreRebuildChunk(renderChunk, layer, pos);
        }
    }

    public static void onPostRebuildChunk(RenderChunk renderChunk, EnumWorldBlockLayer layer, BlockPos pos) {
        for(IWorldRenderer worldRenderer : RenderCore.getAllWorldRenderers()) {
            worldRenderer.onPostRebuildChunk(renderChunk, layer, pos);
        }
    }

    public static void onBlockRender(RenderChunk renderChunk, EnumWorldBlockLayer layer, IBlockState state, IBlockAccess world, BlockPos pos, CompiledChunk compiledChunk) {
        if(state.getBlock().getRenderType() == RenderCore.IWR_RENDER_ID) {
            IWorldRenderer worldRenderer = RenderCore.getWorldRendererForBlock(state.getBlock());
            if(worldRenderer != null && worldRenderer.onBlockRender(renderChunk, state, world, pos)) {
                compiledChunk.setLayerUsed(layer);
            }
        }
    }

    public static void onBlockRenderBrightness(int renderType, IBlockState state, float brightness) {
        if(renderType == RenderCore.IWR_RENDER_ID) {
            IWorldRenderer worldRenderer = RenderCore.getWorldRendererForBlock(state.getBlock());
            if(worldRenderer != null) {
                worldRenderer.onBlockRenderBrightness(state, brightness);
            }
        }
    }

}
