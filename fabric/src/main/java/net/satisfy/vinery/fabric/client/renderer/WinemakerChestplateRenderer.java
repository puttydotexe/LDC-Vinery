package net.satisfy.vinery.fabric.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.satisfy.vinery.client.model.WinemakerChestplateModel;
import net.satisfy.vinery.core.item.WinemakerChestItem;

public class WinemakerChestplateRenderer implements ArmorRenderer {
    private final WinemakerChestplateModel model;

    public WinemakerChestplateRenderer(EntityRendererProvider.Context context) {
        this.model = new WinemakerChestplateModel(context.bakeLayer(WinemakerChestplateModel.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ItemStack stack, HumanoidRenderState humanoidRenderState, EquipmentSlot slot, int light, HumanoidModel<HumanoidRenderState> contextModel) {
        if (stack.getItem() instanceof WinemakerChestItem item) {
            ArmorRenderer.submitTransformCopyingModel(
                    contextModel,
                    humanoidRenderState,
                    this.model,
                    humanoidRenderState,
                    false,
                    submitNodeCollector.order(0),
                    poseStack,
                    contextModel.renderType(item.getChestplateTexture()),
                    light,
                    OverlayTexture.NO_OVERLAY,
                    -1,
                    null
            );
        }
    }
}
