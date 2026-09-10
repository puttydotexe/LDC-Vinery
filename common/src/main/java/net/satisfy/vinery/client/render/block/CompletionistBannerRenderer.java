package net.satisfy.vinery.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.block.CompletionistBannerBlock;
import net.satisfy.vinery.core.block.CompletionistWallBannerBlock;
import net.satisfy.vinery.core.block.entity.CompletionistBannerEntity;

public class CompletionistBannerRenderer implements BlockEntityRenderer<CompletionistBannerEntity, CompletionistBannerRenderer.RenderState> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(Vinery.MOD_ID, "banner"),
            "main"
    );

    public static final String FLAG = "flag";
    private static final String POLE = "pole";
    private static final String BAR = "bar";

    private final ModelPart flag;
    private final ModelPart pole;
    private final ModelPart bar;
    private final SpriteGetter sprites;

    public CompletionistBannerRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart root = context.bakeLayer(LAYER_LOCATION);

        this.flag = root.getChild(FLAG);
        this.pole = root.getChild(POLE);
        this.bar = root.getChild(BAR);
        this.sprites = context.sprites();
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        root.addOrReplaceChild(
                FLAG,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                -10.0F,
                                0.0F,
                                -1.0F,
                                20.0F,
                                40.0F,
                                1.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offsetAndRotation(
                        0.0F,
                        -44.0F,
                        -1.0F,
                        -0.0349F,
                        0.0F,
                        0.0F
                )
        );

        root.addOrReplaceChild(
                POLE,
                CubeListBuilder.create()
                        .texOffs(44, 0)
                        .addBox(
                                -1.0F,
                                -30.0F,
                                -1.0F,
                                2.0F,
                                42.0F,
                                2.0F
                        ),
                PartPose.ZERO
        );

        root.addOrReplaceChild(
                BAR,
                CubeListBuilder.create()
                        .texOffs(0, 42)
                        .addBox(
                                -10.0F,
                                -32.0F,
                                -1.0F,
                                20.0F,
                                2.0F,
                                2.0F
                        ),
                PartPose.ZERO
        );

        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(
            CompletionistBannerEntity banner,
            RenderState renderState,
            float partialTick,
            Vec3 cameraPos,
            ModelFeatureRenderer.CrumblingOverlay crumblingOverlay
    ) {
        BlockEntityRenderer.super.extractRenderState(
                banner,
                renderState,
                partialTick,
                cameraPos,
                crumblingOverlay
        );

        BlockState blockState = banner.getBlockState();

        renderState.blockState = blockState;
        renderState.blockPos = banner.getBlockPos();
        renderState.partialTick = partialTick;
        renderState.gameTime = banner.getLevel() != null
                ? banner.getLevel().getGameTime()
                : 0L;
        renderState.inInventory = banner.getLevel() == null;

        if (blockState.getBlock() instanceof CompletionistBannerBlock bannerBlock) {
            renderState.texture = bannerBlock.getRenderTexture();
        } else {
            renderState.texture = null;
        }
    }

    @Override
    public void submit(
            RenderState renderState,
            PoseStack poseStack,
            SubmitNodeCollector collector,
            CameraRenderState cameraState
    ) {
        if (renderState.blockState == null || renderState.texture == null) {
            return;
        }

        poseStack.pushPose();

        if (renderState.inInventory) {
            poseStack.translate(0.5, 0.5, 0.5);
            pole.visible = true;
        } else if (!(renderState.blockState.getBlock() instanceof CompletionistWallBannerBlock)) {
            poseStack.translate(0.5, 0.5, 0.5);

            float rotation =
                    -renderState.blockState.getValue(CompletionistBannerBlock.ROTATION)
                            * 360.0F
                            / 16.0F;

            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
            pole.visible = true;
        } else {
            poseStack.translate(
                    0.5,
                    -0.1666666716337204,
                    0.5
            );

            float rotation =
                    -renderState.blockState
                            .getValue(CompletionistWallBannerBlock.FACING)
                            .toYRot()
                            + 180.0F;

            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
            poseStack.translate(0.0, -0.3125, -0.4375);
            pole.visible = false;
        }

        poseStack.pushPose();

        float scale = 0.66F;
        poseStack.scale(scale, -scale, -scale);

        collector.submitModelPart(
                pole,
                poseStack,
                Sheets.BANNER_BASE.renderType(RenderTypes::entitySolid),
                renderState.lightCoords,
                OverlayTexture.NO_OVERLAY,
                sprites.get(Sheets.BANNER_BASE)
        );

        collector.submitModelPart(
                bar,
                poseStack,
                Sheets.BANNER_BASE.renderType(RenderTypes::entitySolid),
                renderState.lightCoords,
                OverlayTexture.NO_OVERLAY,
                sprites.get(Sheets.BANNER_BASE)
        );

        BlockPos blockPos = renderState.blockPos;

        float animation = (
                (float) Math.floorMod(
                        blockPos.getX() * 7L
                                + blockPos.getY() * 9L
                                + blockPos.getZ() * 13L
                                + renderState.gameTime,
                        100L
                )
                        + renderState.partialTick
        ) / 100.0F;

        flag.xRot = (
                -0.0125F
                        + 0.01F
                        * Mth.cos((float) Math.PI * 2.0F * animation)
        ) * (float) Math.PI;

        flag.y = -32.0F;

        collector.submitModelPart(
                flag,
                poseStack,
                RenderTypes.entitySolid(renderState.texture),
                renderState.lightCoords,
                OverlayTexture.NO_OVERLAY,
                null
        );

        poseStack.popPose();
        poseStack.popPose();
    }

    public static class RenderState extends BlockEntityRenderState {
        private BlockState blockState;
        private Identifier texture;
        private BlockPos blockPos = BlockPos.ZERO;
        private long gameTime;
        private float partialTick;
        private boolean inInventory;
    }
}