package net.satisfy.vinery.client.render.block.storage;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.world.item.ItemStack;
import net.satisfy.vinery.client.util.ClientUtil;
import net.satisfy.vinery.core.registry.ObjectRegistry;

public class WineBottleRenderer implements StorageTypeRenderer {
    @Override
    public void submit(
            PoseStack poseStack,
            SubmitNodeCollector collector,
            StorageRenderData storage,
            int packedLight
    ) {
        poseStack.translate(-0.5, 0.0, -0.5);

        switch (getCount(storage)) {
            case 1 -> renderOne(poseStack, collector, storage, packedLight);
            case 2 -> renderTwo(poseStack, collector, storage, packedLight);
            case 3 -> renderThree(poseStack, collector, storage, packedLight);
        }
    }

    private static int getCount(StorageRenderData storage) {
        int count = 0;

        for (int i = 0; i < storage.size(); i++) {
            if (!storage.getStack(i).isEmpty()) {
                count++;
            }
        }

        return count;
    }

    private static void renderOne(
            PoseStack poseStack,
            SubmitNodeCollector collector,
            StorageRenderData storage,
            int packedLight
    ) {
        if (storage.size() < 1) {
            return;
        }

        renderBlock(
                storage.getBlockModel(0),
                poseStack,
                collector,
                packedLight
        );
    }

    private static void renderTwo(
            PoseStack poseStack,
            SubmitNodeCollector collector,
            StorageRenderData storage,
            int packedLight
    ) {
        if (storage.size() < 2) {
            return;
        }

        poseStack.translate(-0.15F, 0.0F, -0.25F);

        renderBlock(
                storage.getBlockModel(0),
                poseStack,
                collector,
                packedLight
        );

        poseStack.translate(0.1F, 0.0F, 0.8F);
        poseStack.mulPose(Axis.YP.rotationDegrees(30.0F));

        renderBlock(
                storage.getBlockModel(1),
                poseStack,
                collector,
                packedLight
        );
    }

    private static void renderThree(
            PoseStack poseStack,
            SubmitNodeCollector collector,
            StorageRenderData storage,
            int packedLight
    ) {
        if (storage.size() < 3) {
            return;
        }

        poseStack.translate(-0.25F, 0.0F, -0.25F);

        renderBlock(
                storage.getBlockModel(0),
                poseStack,
                collector,
                packedLight
        );

        poseStack.translate(0.15F, 0.0F, 0.5F);

        renderBlock(
                storage.getBlockModel(1),
                poseStack,
                collector,
                packedLight
        );

        ItemStack thirdStack = storage.getStack(2);
        BlockModelRenderState thirdRenderState = storage.getBlockModel(2);

        if (thirdRenderState.isEmpty()) {
            return;
        }

        if (thirdStack.is(ObjectRegistry.KELP_CIDER.get().asItem())) {
            poseStack.translate(0.35F, 0.7F, -0.13F);
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

            renderBlock(
                    thirdRenderState,
                    poseStack,
                    collector,
                    packedLight
            );

            return;
        }

        poseStack.translate(0.1F, 0.0F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(30.0F));

        renderBlock(
                thirdRenderState,
                poseStack,
                collector,
                packedLight
        );
    }

    private static void renderBlock(
            BlockModelRenderState renderState,
            PoseStack poseStack,
            SubmitNodeCollector collector,
            int packedLight
    ) {
        if (renderState.isEmpty()) {
            return;
        }

        ClientUtil.renderBlock(
                renderState,
                poseStack,
                collector,
                packedLight
        );
    }
}