package net.satisfy.vinery.client.render.block.storage;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.satisfy.vinery.client.util.ClientUtil;

public class BigBottleRenderer implements StorageTypeRenderer {
    @Override
    public void submit(
            PoseStack poseStack,
            SubmitNodeCollector collector,
            StorageRenderData storage,
            int packedLight
    ) {
        if (storage.size() < 1) {
            return;
        }

        BlockModelRenderState renderState = storage.getBlockModel(0);

        if (renderState.isEmpty()) {
            return;
        }

        poseStack.pushPose();

        poseStack.translate(-0.4, 0.07, -0.5);
        poseStack.scale(0.8F, 0.8F, 0.9F);

        ClientUtil.renderBlock(
                renderState,
                poseStack,
                collector,
                packedLight
        );

        poseStack.popPose();
    }
}