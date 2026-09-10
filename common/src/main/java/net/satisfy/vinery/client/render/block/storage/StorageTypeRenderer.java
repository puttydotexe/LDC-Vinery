package net.satisfy.vinery.client.render.block.storage;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;

public interface StorageTypeRenderer {
    void submit(
            PoseStack poseStack,
            SubmitNodeCollector collector,
            StorageRenderData storage,
            int packedLight
    );
}