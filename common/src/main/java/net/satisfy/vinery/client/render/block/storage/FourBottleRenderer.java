package net.satisfy.vinery.client.render.block.storage;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.satisfy.vinery.client.util.ClientUtil;

public class FourBottleRenderer implements StorageTypeRenderer {
    @Override
    public void submit(
            PoseStack poseStack,
            SubmitNodeCollector collector,
            StorageRenderData storage,
            int packedLight
    ) {
        poseStack.translate(-0.13, 0.335, 0.125);
        poseStack.scale(0.9F, 0.9F, 0.9F);

        int count = Math.min(storage.size(), 4);

        for (int i = 0; i < count; i++) {
            BlockModelRenderState renderState = storage.getBlockModel(i);

            if (renderState.isEmpty()) {
                continue;
            }

            poseStack.pushPose();

            switch (i) {
                case 0 -> poseStack.translate(-0.35F, 0.0F, 0.0F);
                case 1 -> poseStack.translate(0.0F, -0.33F, 0.0F);
                case 2 -> poseStack.translate(-0.7F, -0.33F, 0.0F);
                case 3 -> poseStack.translate(-0.35F, -0.66F, 0.0F);
            }

            poseStack.mulPose(Axis.XN.rotationDegrees(90.0F));

            ClientUtil.renderBlock(
                    renderState,
                    poseStack,
                    collector,
                    packedLight
            );

            poseStack.popPose();
        }
    }
}