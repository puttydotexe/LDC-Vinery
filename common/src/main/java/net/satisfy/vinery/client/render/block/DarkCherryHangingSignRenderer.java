package net.satisfy.vinery.client.render.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class DarkCherryHangingSignRenderer extends HangingSignRenderer {
    public DarkCherryHangingSignRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
}