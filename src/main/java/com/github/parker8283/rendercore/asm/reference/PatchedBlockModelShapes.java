package com.github.parker8283.rendercore.asm.reference;

import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.resources.model.ModelManager;

import com.github.parker8283.rendercore.asm.Hooks;

public abstract class PatchedBlockModelShapes extends BlockModelShapes {

    // Here to prevent erroring
    private final ModelManager modelManager;

    public PatchedBlockModelShapes(ModelManager manager) {
        super(manager); // Here to prevent erroring

        //REAL CODE START
        this.modelManager = manager;
        this.registerAllBlocks();
        //INJECTION START
        Hooks.onBlockModelRegister(this);
        //INJECTION END
    }

    // Here to prevent erroring
    private void registerAllBlocks()
    {

    }
}
