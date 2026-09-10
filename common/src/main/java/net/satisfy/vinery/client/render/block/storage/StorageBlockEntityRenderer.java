package net.satisfy.vinery.client.render.block.storage;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.satisfy.vinery.core.block.StorageBlock;
import net.satisfy.vinery.core.block.WineBottleBlock;
import net.satisfy.vinery.core.block.entity.StorageBlockEntity;

import java.util.HashMap;
import java.util.Map;

public class StorageBlockEntityRenderer implements BlockEntityRenderer<StorageBlockEntity, StorageBlockEntityRenderer.RenderState> {
    private static final Map<Identifier, StorageTypeRenderer> STORAGE_TYPES = new HashMap<>();
    private static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();

    private final BlockModelResolver blockModelResolver;
    private final ItemModelResolver itemModelResolver;

    public StorageBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.blockModelResolver = context.blockModelResolver();
        this.itemModelResolver = context.itemModelResolver();
    }

    public static void registerStorageType(Identifier name, StorageTypeRenderer renderer) {
        STORAGE_TYPES.put(name, renderer);
    }

    public static StorageTypeRenderer getRendererForId(Identifier name) {
        return STORAGE_TYPES.get(name);
    }

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(
            StorageBlockEntity entity,
            RenderState renderState,
            float partialTick,
            Vec3 cameraPos,
            ModelFeatureRenderer.CrumblingOverlay crumblingOverlay
    ) {
        BlockEntityRenderer.super.extractRenderState(
                entity,
                renderState,
                partialTick,
                cameraPos,
                crumblingOverlay
        );

        BlockState blockState = entity.getBlockState();
        renderState.blockState = blockState;

        if (!(blockState.getBlock() instanceof StorageBlock storageBlock)) {
            renderState.storageType = null;
            renderState.storage.clear();
            return;
        }

        renderState.storageType = storageBlock.type();

        NonNullList<ItemStack> inventory = entity.getInventory();
        renderState.storage.ensureSize(inventory.size());

        for (int slot = 0; slot < inventory.size(); slot++) {
            ItemStack stack = inventory.get(slot);

            renderState.storage.setStack(slot, stack);

            var blockRenderState = renderState.storage.getBlockModel(slot);
            blockRenderState.clear();

            if (!stack.isEmpty() && stack.getItem() instanceof BlockItem blockItem) {
                BlockState itemBlockState = blockItem.getBlock().defaultBlockState();

                if (itemBlockState.hasProperty(WineBottleBlock.FAKE_MODEL)) {
                    itemBlockState = itemBlockState.setValue(WineBottleBlock.FAKE_MODEL, false);
                }

                blockModelResolver.update(
                        blockRenderState,
                        itemBlockState,
                        BLOCK_DISPLAY_CONTEXT
                );
            }

            var itemRenderState = renderState.storage.getItemModel(slot);
            itemRenderState.clear();

            if (!stack.isEmpty() && entity.getLevel() != null) {
                itemModelResolver.updateForTopItem(
                        itemRenderState,
                        stack,
                        ItemDisplayContext.GUI,
                        entity.getLevel(),
                        null,
                        slot
                );
            }
        }
    }

    @Override
    public void submit(
            RenderState renderState,
            PoseStack poseStack,
            SubmitNodeCollector collector,
            CameraRenderState cameraState
    ) {
        if (renderState.blockState == null || renderState.storageType == null) {
            return;
        }

        StorageTypeRenderer renderer = getRendererForId(renderState.storageType);

        if (renderer == null) {
            return;
        }

        poseStack.pushPose();

        applyBlockAngle(poseStack, renderState.blockState, 180.0F);

        renderer.submit(
                poseStack,
                collector,
                renderState.storage,
                renderState.lightCoords
        );

        poseStack.popPose();
    }

    public static void applyBlockAngle(PoseStack poseStack, BlockState state, float angleOffset) {
        float angle = state.getValue(StorageBlock.FACING).toYRot();

        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(angleOffset - angle));
    }

    public static class RenderState extends BlockEntityRenderState {
        private BlockState blockState;
        private Identifier storageType;
        private final StorageRenderData storage = new StorageRenderData();
    }
}