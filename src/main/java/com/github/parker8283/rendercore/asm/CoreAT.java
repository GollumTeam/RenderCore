package com.github.parker8283.rendercore.asm;

import java.io.IOException;

import net.minecraftforge.fml.common.asm.transformers.AccessTransformer;

public class CoreAT extends AccessTransformer {

    public CoreAT() throws IOException {
        super("rendercore_at.cfg");
    }
}
