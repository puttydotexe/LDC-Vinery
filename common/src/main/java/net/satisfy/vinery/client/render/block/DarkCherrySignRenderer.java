package net.satisfy.vinery.client.render.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.StandingSignRenderer;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class DarkCherrySignRenderer extends StandingSignRenderer {
    public DarkCherrySignRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
}