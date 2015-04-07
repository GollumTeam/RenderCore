package com.github.parker8283.rendercore.asm.reference;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RegionRenderCache;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

import com.github.parker8283.rendercore.asm.Hooks;

public abstract class PatchedRenderChunk extends RenderChunk {

    //Here to prevent erroring
    private World world;
    private BlockPos position;

    //Here to prevent erroring
    public PatchedRenderChunk(World worldIn, RenderGlobal renderGlobalIn, BlockPos blockPosIn, int indexIn) {
        super(worldIn, renderGlobalIn, blockPosIn, indexIn);
    }

    public void resortTransparency(float p_178570_1_, float p_178570_2_, float p_178570_3_, ChunkCompileTaskGenerator p_178570_4_)
    {
        CompiledChunk compiledchunk = p_178570_4_.getCompiledChunk();

        if (compiledchunk.getState() != null && !compiledchunk.isLayerEmpty(EnumWorldBlockLayer.TRANSLUCENT))
        {
            //INJECTION START
            Hooks.onPreRebuildChunk(this, EnumWorldBlockLayer.TRANSLUCENT, this.position);
            //INJECTION END
            this.preRenderBlocks(p_178570_4_.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT), this.position);
            p_178570_4_.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT).setVertexState(compiledchunk.getState());
            this.postRenderBlocks(EnumWorldBlockLayer.TRANSLUCENT, p_178570_1_, p_178570_2_, p_178570_3_, p_178570_4_.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT), compiledchunk);
            //INJECTION START
            Hooks.onPostRebuildChunk(this, EnumWorldBlockLayer.TRANSLUCENT, this.position);
            //INJECTION END
        }
    }

    // Copied the entire method because I was failing at ASM.
    public void rebuildChunk(float p_178581_1_, float p_178581_2_, float p_178581_3_, ChunkCompileTaskGenerator p_178581_4_)
    {
        CompiledChunk compiledchunk = new CompiledChunk();
        boolean flag = true;
        BlockPos blockpos = this.position;
        BlockPos blockpos1 = blockpos.add(15, 15, 15);
        p_178581_4_.getLock().lock();
        RegionRenderCache regionrendercache;

        try
        {
            if (p_178581_4_.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING)
            {
                return;
            }

            regionrendercache = new RegionRenderCache(this.world, blockpos.add(-1, -1, -1), blockpos1.add(1, 1, 1), 1);
            p_178581_4_.setCompiledChunk(compiledchunk);
        }
        finally
        {
            p_178581_4_.getLock().unlock();
        }

        VisGraph visgraph = new VisGraph();

        if (!regionrendercache.extendedLevelsInChunkCache())
        {
            ++renderChunksUpdated;
            Iterator iterator = BlockPos.getAllInBoxMutable(blockpos, blockpos1).iterator();

            while (iterator.hasNext())
            {
                BlockPos.MutableBlockPos mutableblockpos = (BlockPos.MutableBlockPos)iterator.next();
                IBlockState iblockstate = regionrendercache.getBlockState(mutableblockpos);
                Block block = iblockstate.getBlock();

                if (block.isOpaqueCube())
                {
                    visgraph.func_178606_a(mutableblockpos);
                }

                if (block.hasTileEntity(iblockstate))
                {
                    TileEntity tileentity = regionrendercache.getTileEntity(new BlockPos(mutableblockpos));

                    if (tileentity != null && TileEntityRendererDispatcher.instance.hasSpecialRenderer(tileentity))
                    {
                        compiledchunk.addTileEntity(tileentity);
                    }
                }

                for(EnumWorldBlockLayer enumworldblocklayer1 : EnumWorldBlockLayer.values()) {
                    if(!block.canRenderInLayer(enumworldblocklayer1)) continue;
                    net.minecraftforge.client.ForgeHooksClient.setRenderLayer(enumworldblocklayer1);
                int i = enumworldblocklayer1.ordinal();

                if (block.getRenderType() != -1)
                {
                    WorldRenderer worldrenderer = p_178581_4_.getRegionRenderCacheBuilder().getWorldRendererByLayerId(i);

                    if (!compiledchunk.isLayerStarted(enumworldblocklayer1))
                    {
                        compiledchunk.setLayerStarted(enumworldblocklayer1);
                        //INJECTION START
                        Hooks.onPreRebuildChunk(this, enumworldblocklayer1, position);
                        //INJECTION END
                        this.preRenderBlocks(worldrenderer, blockpos);
                    }

                    //INJECTION START
                    Hooks.onBlockRender(this, enumworldblocklayer1, iblockstate, regionrendercache, mutableblockpos, compiledchunk);
                    //INJECTION END

                    if (Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlock(iblockstate, mutableblockpos, regionrendercache, worldrenderer))
                    {
                        compiledchunk.setLayerUsed(enumworldblocklayer1);
                    }
                }
                }
            }

            EnumWorldBlockLayer[] aenumworldblocklayer = EnumWorldBlockLayer.values();
            int j = aenumworldblocklayer.length;

            for (int k = 0; k < j; ++k)
            {
                EnumWorldBlockLayer enumworldblocklayer = aenumworldblocklayer[k];

                if (compiledchunk.isLayerStarted(enumworldblocklayer))
                {
                    this.postRenderBlocks(enumworldblocklayer, p_178581_1_, p_178581_2_, p_178581_3_, p_178581_4_.getRegionRenderCacheBuilder().getWorldRendererByLayer(enumworldblocklayer), compiledchunk);
                    //INJECTION START
                    Hooks.onPostRebuildChunk(this, enumworldblocklayer, position);
                    //INJECTION END
                }
            }
        }

        compiledchunk.setVisibility(visgraph.computeVisibility());
    }

    private void preRenderBlocks(WorldRenderer p_178573_1_, BlockPos pos)
    {
        //Here to prevent erroring
    }

    private void postRenderBlocks(EnumWorldBlockLayer p_178584_1_, float p_178584_2_, float p_178584_3_, float p_178584_4_, WorldRenderer p_178584_5_, CompiledChunk p_178584_6_)
    {
        //Here to prevent erroring
    }
}
