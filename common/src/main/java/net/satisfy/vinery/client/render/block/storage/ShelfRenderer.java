package net.satisfy.vinery.client.render.block.storage;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.satisfy.vinery.client.util.ClientUtil;

public class ShelfRenderer implements StorageTypeRenderer {
    @Override
    public void submit(
            PoseStack poseStack,
            SubmitNodeCollector collector,
            StorageRenderData storage,
            int packedLight
    ) {
        poseStack.translate(-0.4, 0.5, 0.25);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        poseStack.scale(0.5F, 0.5F, 0.5F);

        for (int i = 0; i < storage.size(); i++) {
            ItemStackRenderState renderState = storage.getItemModel(i);

            if (renderState.isEmpty()) {
                continue;
            }

            poseStack.pushPose();

            poseStack.translate(0.0F, 0.0F, 0.2F * i);
            poseStack.mulPose(Axis.YN.rotationDegrees(22.5F));

            ClientUtil.renderItem(
                    renderState,
                    poseStack,
                    collector,
                    packedLight
            );

            poseStack.popPose();
        }
    }
}