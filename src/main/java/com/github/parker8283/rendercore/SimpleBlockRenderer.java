package com.github.parker8283.rendercore;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;

/**
 * A wrapper for the IWorldRenderer to simplify block rendering. Based off of the original ISimpleBlockRenderingHandler<br/>
 * Make sure to register an instance of this class with both {@link RenderCore#registerWorldRenderingHandler} and the {@link ModelBakeEvent}.<br/>
 *
 * @author Parker8283
 */
public abstract class SimpleBlockRenderer implements IWorldRenderer, IDynamicItemModel {

    @Override
    public void onPreRenderLayer(RenderGlobal renderer, List<RenderGlobal.ContainerLocalRenderInformation> renderInfos, ChunkRenderContainer chunkContainer, double partialTicks, int pass, Entity entity) {}

    @Override
    public void onPostRenderLayer(RenderGlobal renderer, List<RenderGlobal.ContainerLocalRenderInformation> renderInfos, ChunkRenderContainer chunkContainer, double partialTicks, int pass, Entity entity) {}

    @Override
    public void onPreRebuildChunk(RenderChunk renderChunk, EnumWorldBlockLayer layer, BlockPos pos) {}

    @Override
    public void onPostRebuildChunk(RenderChunk renderChunk, EnumWorldBlockLayer layer, BlockPos pos) {}

    @Override
    public List getFaceQuads(EnumFacing p_177551_1_) {
        return null;
    }

    @Override
    public List getGeneralQuads() {
        return null;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return true;
    }

    @Override
    public TextureAtlasSprite getTexture() {
        return null;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public void render(ItemStack stack) {
        renderInventoryBlock(stack);
    }

    @Override
    public boolean onBlockRender(RenderChunk renderChunk, IBlockState state, IBlockAccess world, BlockPos pos) {
        return renderWorldBlock(renderChunk, state, world, pos);
    }

    @Override
    public void onBlockRenderBrightness(IBlockState state, float brightness) {
        renderBlockBrightness(state, brightness);
    }

    @Override
    public boolean isGui3d() {
        return shouldRender3DInInventory();
    }

    // Merely created for a clearer method name when dealing with this SBR.
    public abstract void renderInventoryBlock(ItemStack stack);
    public abstract boolean renderWorldBlock(RenderChunk renderChunk, IBlockState state, IBlockAccess access, BlockPos pos);
    public abstract void renderBlockBrightness(IBlockState state, float brightness);
    public abstract boolean shouldRender3DInInventory();
}
