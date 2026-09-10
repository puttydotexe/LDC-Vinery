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
import net.satisfy.vinery.client.model.WinemakerBootsModel;
import net.satisfy.vinery.core.item.WinemakerBootsItem;

public class WinemakerBootsRenderer implements ArmorRenderer {
    private final WinemakerBootsModel model;

    public WinemakerBootsRenderer(EntityRendererProvider.Context context) {
        this.model = new WinemakerBootsModel(context.bakeLayer(WinemakerBootsModel.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ItemStack stack, HumanoidRenderState humanoidRenderState, EquipmentSlot slot, int light, HumanoidModel<HumanoidRenderState> contextModel) {
        if (stack.getItem() instanceof WinemakerBootsItem item) {
            ArmorRenderer.submitTransformCopyingModel(
                    contextModel,
                    humanoidRenderState,
                    this.model,
                    humanoidRenderState,
                    false,
                    submitNodeCollector.order(0),
                    poseStack,
                    contextModel.renderType(item.getBootsTexture()),
                    light,
                    OverlayTexture.NO_OVERLAY,
                    -1,
                    null
            );
        }
    }
}
