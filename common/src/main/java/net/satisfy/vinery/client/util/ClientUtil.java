package net.satisfy.vinery.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

public final class ClientUtil {
    private ClientUtil() {
    }

    public static void renderBlock(
            BlockModelRenderState renderState,
            PoseStack poseStack,
            SubmitNodeCollector collector,
            int packedLight
    ) {
        if (renderState == null || renderState.isEmpty()) {
            return;
        }

        renderState.submit(
                poseStack,
                collector,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                0
        );
    }

    public static void renderItem(
            ItemStackRenderState renderState,
            PoseStack poseStack,
            SubmitNodeCollector collector,
            int packedLight
    ) {
        if (renderState == null || renderState.isEmpty()) {
            return;
        }

        renderState.submit(
                poseStack,
                collector,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                0
        );
    }
}