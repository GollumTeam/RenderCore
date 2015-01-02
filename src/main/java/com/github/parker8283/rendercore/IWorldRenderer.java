package com.github.parker8283.rendercore;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;

/**
 * Interface to allow manipulation of world rendering,<br/>
 * Register this instance along with the blocks it should handle on the {@code onBlockRender} method in the RenderCore class.<br/>
 * This interface and all the code associated with it is authored by RainWarrior/fry, and is part of Forge PR #1596.
 */
public interface IWorldRenderer {

    /**
     * Called from the main thread right before the corresponding block layer is rendered.
     */
    void onPreRenderLayer(RenderGlobal renderer, List<RenderGlobal.ContainerLocalRenderInformation> renderInfos, ChunkRenderContainer chunkContainer, double partialTicks, int pass, Entity entity);

    /**
     * Called from the main thread right after the corresponding block layer is rendered.
     */
    void onPostRenderLayer(RenderGlobal renderer, List<RenderGlobal.ContainerLocalRenderInformation> renderInfos, ChunkRenderContainer chunkContainer, double partialTicks, int pass, Entity entity);

    /**
     * Called before the blocks in the specified chunk (and layer) are re-rendered.<br/>
     * Might be called from render update thread.
     */
    void onPreRebuildChunk(RenderChunk renderChunk, EnumWorldBlockLayer layer, BlockPos pos);

    /**
     * Called when the specified block needs render update.<br/>
     * Might be called from render update thread.
     * @return {@code true} if rendering was successful.
     */
    boolean onBlockRender(RenderChunk renderChunk, IBlockState state, IBlockAccess world, BlockPos pos);

    /**
     * Called when the specified block needs a brightness render update.<br/>
     * Might be called from render update thread.
     */
    void onBlockRenderBrightness(IBlockState state, float brightness);

    /**
     * Called after the blocks in the specified chunk (and layer) are re-rendered.<br/>
     * Might be called from render update thread.
     */
    void onPostRebuildChunk(RenderChunk renderChunk, EnumWorldBlockLayer layer, BlockPos pos);
}
