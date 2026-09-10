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
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.block.LatticeBlock;
import net.satisfy.vinery.core.block.entity.LatticeBlockEntity;
import net.satisfy.vinery.core.registry.GrapeTypeRegistry;
import net.satisfy.vinery.core.util.GeneralUtil;
import net.satisfy.vinery.core.util.GrapeType;

import java.util.HashMap;
import java.util.Map;

import static net.satisfy.vinery.core.registry.ObjectRegistry.*;

public class LatticeRenderer implements BlockEntityRenderer<LatticeBlockEntity, LatticeRenderer.RenderState> {
    private static Map<Block, Identifier> textureMap;

    private static Map<Block, Identifier> getTextureMap() {
        if (textureMap == null) {
            textureMap = new HashMap<>();
            textureMap.put(OAK_LATTICE.get(), Vinery.identifier("textures/block/lattice/oak_lattice.png"));
            textureMap.put(SPRUCE_LATTICE.get(), Vinery.identifier("textures/block/lattice/spruce_lattice.png"));
            textureMap.put(CHERRY_LATTICE.get(), Vinery.identifier("textures/block/lattice/cherry_lattice.png"));
            textureMap.put(BIRCH_LATTICE.get(), Vinery.identifier("textures/block/lattice/birch_lattice.png"));
            textureMap.put(DARK_OAK_LATTICE.get(), Vinery.identifier("textures/block/lattice/dark_oak_lattice.png"));
            textureMap.put(ACACIA_LATTICE.get(), Vinery.identifier("textures/block/lattice/acacia_lattice.png"));
            textureMap.put(BAMBOO_LATTICE.get(), Vinery.identifier("textures/block/lattice/bamboo_lattice.png"));
            textureMap.put(JUNGLE_LATTICE.get(), Vinery.identifier("textures/block/lattice/jungle_lattice.png"));
            textureMap.put(MANGROVE_LATTICE.get(), Vinery.identifier("textures/block/lattice/mangrove_lattice.png"));
            textureMap.put(DARK_CHERRY_LATTICE.get(), Vinery.identifier("textures/block/lattice/dark_cherry_lattice.png"));
        }

        return textureMap;
    }

    private final ModelPart growingRed;
    private final ModelPart sprout;
    private final ModelPart growingWhite;
    private final ModelPart mesh;
    private final ModelPart supportRight;
    private final ModelPart cornerBracesRight;
    private final ModelPart supportLeft;
    private final ModelPart cornerBracesLeft;
    private final ModelPart growingRedFloor;
    private final ModelPart sproutFloor;
    private final ModelPart growingWhiteFloor;
    private final ModelPart latticeParts;
    private final ModelPart hanging1;
    private final ModelPart hanging2;

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Vinery.identifier("lattice"),
            "main"
    );

    public LatticeRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart root = context.bakeLayer(LAYER_LOCATION);

        ModelPart latticeWall = root.getChild("lattice_wall");
        ModelPart grapeCluster = latticeWall.getChild("grape_cluster");

        this.growingRed = grapeCluster.getChild("growing_red");
        this.sprout = grapeCluster.getChild("sprout");
        this.growingWhite = grapeCluster.getChild("growing_white");
        this.mesh = latticeWall.getChild("mesh");
        this.supportRight = latticeWall.getChild("support_right");
        this.cornerBracesRight = latticeWall.getChild("corner_braces_right");
        this.supportLeft = latticeWall.getChild("support_left");
        this.cornerBracesLeft = latticeWall.getChild("corner_braces_left");

        ModelPart latticeFloor = root.getChild("lattice_floor");
        ModelPart grapeClusterFloor = latticeFloor.getChild("grape_cluster_floor");

        this.growingRedFloor = grapeClusterFloor.getChild("growing_red_floor");
        this.sproutFloor = grapeClusterFloor.getChild("sprout_floor");
        this.growingWhiteFloor = grapeClusterFloor.getChild("growing_white_floor");
        this.latticeParts = latticeFloor.getChild("lattice_parts");
        this.hanging1 = grapeClusterFloor.getChild("hanging_1_r1");
        this.hanging2 = grapeClusterFloor.getChild("hanging_2_r1");
    }

    @SuppressWarnings("unused")
    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition lattice_wall = partdefinition.addOrReplaceChild(
                "lattice_wall",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );

        PartDefinition grape_cluster = lattice_wall.addOrReplaceChild(
                "grape_cluster",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.0F, -10.0F)
        );

        grape_cluster.addOrReplaceChild(
                "growing_red",
                CubeListBuilder.create()
                        .texOffs(46, 17)
                        .addBox(
                                -15.0F,
                                -16.0F,
                                -10.5F,
                                16.0F,
                                16.0F,
                                1.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offset(7.0F, 0.0F, 17.0F)
        );

        grape_cluster.addOrReplaceChild(
                "sprout",
                CubeListBuilder.create()
                        .texOffs(46, 0)
                        .addBox(
                                -15.0F,
                                -16.0F,
                                -10.5F,
                                16.0F,
                                16.0F,
                                1.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offset(7.0F, 0.0F, 17.0F)
        );

        grape_cluster.addOrReplaceChild(
                "growing_white",
                CubeListBuilder.create()
                        .texOffs(46, 34)
                        .addBox(
                                -15.0F,
                                -16.0F,
                                -10.5F,
                                16.0F,
                                16.0F,
                                1.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offset(7.0F, 0.0F, 17.0F)
        );

        lattice_wall.addOrReplaceChild(
                "mesh",
                CubeListBuilder.create()
                        .texOffs(48, 64)
                        .addBox(
                                -30.0F,
                                -12.0F,
                                2.0F,
                                16.0F,
                                16.0F,
                                0.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offset(22.0F, -4.0F, 5.0F)
        );

        lattice_wall.addOrReplaceChild(
                "support_right",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                -8.0F,
                                -9.0F,
                                7.0F,
                                2.0F,
                                16.0F,
                                2.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offset(14.0F, -7.0F, -1.0F)
        );

        PartDefinition corner_braces_right = lattice_wall.addOrReplaceChild(
                "corner_braces_right",
                CubeListBuilder.create()
                        .texOffs(8, 0)
                        .addBox(
                                6.0F,
                                -16.0F,
                                -2.0F,
                                2.0F,
                                2.0F,
                                8.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.ZERO
        );

        corner_braces_right.addOrReplaceChild(
                "corner_braces_right_bottom_r1",
                CubeListBuilder.create()
                        .texOffs(8, 10)
                        .addBox(
                                -7.99F,
                                -4.0F,
                                6.0F,
                                1.98F,
                                9.0F,
                                1.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offsetAndRotation(
                        14.0F,
                        -7.0F,
                        -1.0F,
                        0.7854F,
                        0.0F,
                        0.0F
                )
        );

        corner_braces_right.addOrReplaceChild(
                "corner_braces_right_top_r1",
                CubeListBuilder.create()
                        .texOffs(3, 10)
                        .addBox(
                                -7.99F,
                                -6.0F,
                                -5.5F,
                                1.98F,
                                1.0F,
                                11.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offsetAndRotation(
                        14.0F,
                        -7.0F,
                        -1.0F,
                        -0.7854F,
                        0.0F,
                        0.0F
                )
        );

        lattice_wall.addOrReplaceChild(
                "support_left",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                -16.0F,
                                -16.0F,
                                14.0F,
                                2.0F,
                                16.0F,
                                2.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offset(8.0F, 0.0F, -8.0F)
        );

        PartDefinition corner_braces_left = lattice_wall.addOrReplaceChild(
                "corner_braces_left",
                CubeListBuilder.create()
                        .texOffs(8, 0)
                        .addBox(
                                -8.0F,
                                -16.0F,
                                -2.0F,
                                2.0F,
                                2.0F,
                                8.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.ZERO
        );

        corner_braces_left.addOrReplaceChild(
                "corner_braces_left_top_r1",
                CubeListBuilder.create()
                        .texOffs(8, 10)
                        .addBox(
                                -7.99F,
                                -4.0F,
                                6.0F,
                                1.98F,
                                9.0F,
                                1.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offsetAndRotation(
                        0.0F,
                        -7.0F,
                        -1.0F,
                        0.7854F,
                        0.0F,
                        0.0F
                )
        );

        corner_braces_left.addOrReplaceChild(
                "corner_braces_left_bottom_r1",
                CubeListBuilder.create()
                        .texOffs(3, 10)
                        .addBox(
                                -7.99F,
                                -6.0F,
                                -5.5F,
                                1.98F,
                                1.0F,
                                11.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offsetAndRotation(
                        0.0F,
                        -7.0F,
                        -1.0F,
                        -0.7854F,
                        0.0F,
                        0.0F
                )
        );

        PartDefinition lattice_floor = partdefinition.addOrReplaceChild(
                "lattice_floor",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );

        PartDefinition grape_cluster_floor = lattice_floor.addOrReplaceChild(
                "grape_cluster_floor",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -4.5F, 4.0F)
        );

        grape_cluster_floor.addOrReplaceChild(
                "hanging_2_r1",
                CubeListBuilder.create()
                        .texOffs(32, 14)
                        .addBox(
                                -7.0F,
                                -11.5F,
                                -1.0F,
                                8.0F,
                                12.0F,
                                0.0F
                        ),
                PartPose.offsetAndRotation(
                        2.8284F,
                        14.5F,
                        -3.4142F,
                        0.0F,
                        0.7854F,
                        0.0F
                )
        );

        grape_cluster_floor.addOrReplaceChild(
                "hanging_1_r1",
                CubeListBuilder.create()
                        .texOffs(32, 0)
                        .addBox(
                                -7.0F,
                                -9.5F,
                                -1.0F,
                                8.0F,
                                10.0F,
                                0.0F
                        ),
                PartPose.offsetAndRotation(
                        1.4142F,
                        12.5F,
                        0.8284F,
                        0.0F,
                        -0.7854F,
                        0.0F
                )
        );

        PartDefinition growing_red_floor = grape_cluster_floor.addOrReplaceChild(
                "growing_red_floor",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -3.5F, 1.0F)
        );

        growing_red_floor.addOrReplaceChild(
                "growing_red_floor_r1",
                CubeListBuilder.create()
                        .texOffs(2, 52)
                        .addBox(
                                -18.0F,
                                -30.5F,
                                -3.0F,
                                16.0F,
                                1.0F,
                                12.0F
                        ),
                PartPose.offsetAndRotation(
                        3.0F,
                        32.5F,
                        8.5F,
                        0.0F,
                        -1.5708F,
                        0.0F
                )
        );

        PartDefinition sprout_floor = grape_cluster_floor.addOrReplaceChild(
                "sprout_floor",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -3.5F, 1.0F)
        );

        sprout_floor.addOrReplaceChild(
                "sprouting_grapes_floor_r1",
                CubeListBuilder.create()
                        .texOffs(2, 39)
                        .addBox(
                                -18.0F,
                                -30.5F,
                                -3.0F,
                                16.0F,
                                1.0F,
                                12.0F
                        ),
                PartPose.offsetAndRotation(
                        3.0F,
                        32.5F,
                        8.5F,
                        0.0F,
                        -1.5708F,
                        0.0F
                )
        );

        PartDefinition growing_white_floor = grape_cluster_floor.addOrReplaceChild(
                "growing_white_floor",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -3.5F, 1.0F)
        );

        growing_white_floor.addOrReplaceChild(
                "growing_white_floor_r1",
                CubeListBuilder.create()
                        .texOffs(2, 65)
                        .addBox(
                                -18.0F,
                                -30.5F,
                                -3.0F,
                                16.0F,
                                1.0F,
                                12.0F
                        ),
                PartPose.offsetAndRotation(
                        3.0F,
                        32.5F,
                        8.5F,
                        0.0F,
                        -1.5708F,
                        0.0F
                )
        );

        PartDefinition lattice_parts = lattice_floor.addOrReplaceChild(
                "lattice_parts",
                CubeListBuilder.create(),
                PartPose.ZERO
        );

        lattice_parts.addOrReplaceChild(
                "cross_brace_r1",
                CubeListBuilder.create()
                        .texOffs(-12, 24)
                        .addBox(
                                -18.0F,
                                -29.0F,
                                -3.0F,
                                16.0F,
                                0.0F,
                                12.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offsetAndRotation(
                        3.0F,
                        28.0F,
                        10.0F,
                        0.0F,
                        -1.5708F,
                        0.0F
                )
        );

        lattice_parts.addOrReplaceChild(
                "support_floor_left_r1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                -16.0F,
                                -16.0F,
                                14.0F,
                                2.0F,
                                16.0F,
                                2.0F,
                                new CubeDeformation(0.0F)
                        )
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(
                                -30.0F,
                                -16.0F,
                                14.0F,
                                2.0F,
                                16.0F,
                                2.0F,
                                new CubeDeformation(0.0F)
                        )
                        .mirror(false),
                PartPose.offsetAndRotation(
                        22.0F,
                        -16.0F,
                        -8.0F,
                        -1.5708F,
                        0.0F,
                        0.0F
                )
        );

        return LayerDefinition.create(meshdefinition, 80, 80);
    }

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(
            LatticeBlockEntity blockEntity,
            RenderState renderState,
            float partialTick,
            Vec3 cameraPos,
            ModelFeatureRenderer.CrumblingOverlay crumblingOverlay
    ) {
        BlockEntityRenderer.super.extractRenderState(
                blockEntity,
                renderState,
                partialTick,
                cameraPos,
                crumblingOverlay
        );

        BlockState state = blockEntity.getBlockState();

        renderState.direction = state.getValue(LatticeBlock.FACING);
        renderState.support = state.getValue(LatticeBlock.SUPPORT);
        renderState.bottom = state.getValue(LatticeBlock.BOTTOM);
        renderState.type = state.getValue(LatticeBlock.TYPE);
        renderState.age = state.getValue(LatticeBlock.AGE);
        renderState.grapeType = state.getValue(LatticeBlock.GRAPE);

        renderState.texture = getTextureMap().getOrDefault(
                state.getBlock(),
                Vinery.identifier("textures/entity/lattice/default_lattice.png")
        );

        renderState.showHanging = blockEntity.shouldShowHanging();
        renderState.randomSeed = blockEntity.getBlockPos().asLong();
    }

    @Override
    public void submit(
            RenderState renderState,
            PoseStack poseStack,
            SubmitNodeCollector collector,
            CameraRenderState cameraState
    ) {
        if (renderState.texture == null
                || renderState.direction == null
                || renderState.type == null
                || renderState.grapeType == null) {
            return;
        }

        RenderType renderType = RenderTypes.entityCutout(renderState.texture);

        poseStack.pushPose();

        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(
                Axis.YP.rotationDegrees(-renderState.direction.toYRot())
        );
        poseStack.scale(1.0F, -1.0F, -1.0F);

        if (renderState.bottom) {
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
            poseStack.scale(1.0F, -1.0F, -1.0F);

            submitPart(
                    latticeParts,
                    poseStack,
                    collector,
                    renderType,
                    renderState
            );
        } else {
            submitPart(
                    mesh,
                    poseStack,
                    collector,
                    renderType,
                    renderState
            );

            if (renderState.type != GeneralUtil.LineConnectingType.MIDDLE
                    && renderState.type != GeneralUtil.LineConnectingType.LEFT) {
                submitPart(
                        supportRight,
                        poseStack,
                        collector,
                        renderType,
                        renderState
                );

                if (renderState.support) {
                    submitPart(
                            cornerBracesRight,
                            poseStack,
                            collector,
                            renderType,
                            renderState
                    );
                }
            }

            if (renderState.type != GeneralUtil.LineConnectingType.MIDDLE
                    && renderState.type != GeneralUtil.LineConnectingType.RIGHT) {
                submitPart(
                        supportLeft,
                        poseStack,
                        collector,
                        renderType,
                        renderState
                );

                if (renderState.support) {
                    submitPart(
                            cornerBracesLeft,
                            poseStack,
                            collector,
                            renderType,
                            renderState
                    );
                }
            }
        }

        if (!renderState.grapeType.equals(GrapeTypeRegistry.NONE)) {
            if (renderState.bottom) {
                if (renderState.age < 4) {
                    submitPart(
                            sproutFloor,
                            poseStack,
                            collector,
                            renderType,
                            renderState
                    );
                } else if (renderState.grapeType.isRed()) {
                    submitPart(
                            growingRedFloor,
                            poseStack,
                            collector,
                            renderType,
                            renderState
                    );
                } else {
                    submitPart(
                            growingWhiteFloor,
                            poseStack,
                            collector,
                            renderType,
                            renderState
                    );
                }
            } else {
                if (renderState.age < 4) {
                    submitPart(
                            sprout,
                            poseStack,
                            collector,
                            renderType,
                            renderState
                    );
                } else if (renderState.grapeType.isRed()) {
                    submitPart(
                            growingRed,
                            poseStack,
                            collector,
                            renderType,
                            renderState
                    );
                } else {
                    submitPart(
                            growingWhite,
                            poseStack,
                            collector,
                            renderType,
                            renderState
                    );
                }
            }
        }

        if (renderState.bottom && renderState.showHanging) {
            poseStack.pushPose();

            /*
             * Preserve the original hanging-cluster transformation.
             *
             * Note that the original renderer applies the facing rotation
             * a second time here, so we retain that behaviour.
             */
            poseStack.mulPose(
                    Axis.YP.rotationDegrees(-renderState.direction.toYRot())
            );
            poseStack.scale(1.0F, -1.0F, -1.0F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));

            RandomSource random = RandomSource.create(renderState.randomSeed);

            float offsetX = Mth.lerp(
                    random.nextFloat(),
                    -0.02F,
                    0.0F
            );

            float offsetZ = Mth.lerp(
                    random.nextFloat(),
                    -0.02F,
                    0.0F
            );

            poseStack.translate(
                    offsetX,
                    -0.2F,
                    offsetZ
            );

            submitPart(
                    hanging1,
                    poseStack,
                    collector,
                    renderType,
                    renderState
            );

            submitPart(
                    hanging2,
                    poseStack,
                    collector,
                    renderType,
                    renderState
            );

            poseStack.popPose();
        }

        poseStack.popPose();
    }

    private static void submitPart(
            ModelPart part,
            PoseStack poseStack,
            SubmitNodeCollector collector,
            RenderType renderType,
            RenderState renderState
    ) {
        collector.submitModelPart(
                part,
                poseStack,
                renderType,
                renderState.lightCoords,
                OverlayTexture.NO_OVERLAY,
                null
        );
    }

    public static class RenderState extends BlockEntityRenderState {
        private Direction direction;
        private boolean support;
        private boolean bottom;
        private GeneralUtil.LineConnectingType type;
        private int age;
        private GrapeType grapeType;
        private Identifier texture;
        private boolean showHanging;
        private long randomSeed;
    }
}