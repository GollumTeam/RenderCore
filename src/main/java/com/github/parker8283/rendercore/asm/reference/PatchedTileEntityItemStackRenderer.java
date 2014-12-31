package com.github.parker8283.rendercore.asm.reference;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;

import com.github.parker8283.rendercore.RenderCore;

public abstract class PatchedTileEntityItemStackRenderer extends TileEntityItemStackRenderer {
    private TileEntityChest field_147717_b = new TileEntityChest(0);
    private TileEntityChest field_147718_c = new TileEntityChest(1);
    private TileEntityEnderChest field_147716_d = new TileEntityEnderChest();

    public void renderByItem(ItemStack itemStackIn)
    {
        // ...

        Block block = Block.getBlockFromItem(itemStackIn.getItem());

        if (block == Blocks.ender_chest)
        {
            TileEntityRendererDispatcher.instance.renderTileEntityAt(this.field_147716_d, 0.0D, 0.0D, 0.0D, 0.0F);
        }
        else if (block == Blocks.trapped_chest)
        {
            TileEntityRendererDispatcher.instance.renderTileEntityAt(this.field_147718_c, 0.0D, 0.0D, 0.0D, 0.0F);
        }
        //INJECTION BEGINS
        else if (RenderCore.hasBlockForTESRCall(block))
        {
            TileEntityRendererDispatcher.instance.renderTileEntityAt(RenderCore.getTEForTESRCall(block), 0.0D, 0.0D, 0.0D, 0.0F);
        }
        //INJECTION ENDS
        else
        {
            TileEntityRendererDispatcher.instance.renderTileEntityAt(this.field_147717_b, 0.0D, 0.0D, 0.0D, 0.0F);
        }
    }
}
