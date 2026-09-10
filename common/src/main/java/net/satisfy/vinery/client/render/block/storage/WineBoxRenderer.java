package net.satisfy.vinery.client.render.block.storage;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.satisfy.vinery.client.util.ClientUtil;

public class WineBoxRenderer implements StorageTypeRenderer {
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

        poseStack.translate(0.35, 0.6, -0.35);
        poseStack.scale(0.7F, 0.7F, 0.7F);

        poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
        poseStack.mulPose(Axis.YN.rotationDegrees(90.0F));

        ClientUtil.renderBlock(
                renderState,
                poseStack,
                collector,
                packedLight
        );
    }
}