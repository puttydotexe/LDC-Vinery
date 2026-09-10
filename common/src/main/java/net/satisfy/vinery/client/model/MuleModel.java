package net.satisfy.vinery.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.animal.equine.AbstractEquineModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.satisfy.vinery.core.Vinery;

@Environment(EnvType.CLIENT)
@SuppressWarnings("unused")
public class MuleModel extends AbstractEquineModel<EquineRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Vinery.identifier("trader_mule"), "main");

    private final ModelPart rightHindBabyLeg;
    private final ModelPart leftHindBabyLeg;
    private final ModelPart rightFrontBabyLeg;
    private final ModelPart leftFrontBabyLeg;

    private final ModelPart[] saddleParts;
    private final ModelPart[] ridingParts;

    public MuleModel(ModelPart root) {
        super(root);

        this.rightHindBabyLeg = root.getChild("right_hind_baby_leg");
        this.leftHindBabyLeg = root.getChild("left_hind_baby_leg");
        this.rightFrontBabyLeg = root.getChild("right_front_baby_leg");
        this.leftFrontBabyLeg = root.getChild("left_front_baby_leg");

        ModelPart saddle = this.body.getChild("saddle");

        ModelPart leftSaddleMouth =
                this.headParts.getChild("left_saddle_mouth");

        ModelPart rightSaddleMouth =
                this.headParts.getChild("right_saddle_mouth");

        ModelPart leftSaddleLine =
                this.headParts.getChild("left_saddle_line");

        ModelPart rightSaddleLine =
                this.headParts.getChild("right_saddle_line");

        ModelPart headSaddle =
                this.headParts.getChild("head_saddle");

        ModelPart mouthSaddleWrap =
                this.headParts.getChild("mouth_saddle_wrap");

        this.saddleParts = new ModelPart[]{
                saddle,
                leftSaddleMouth,
                rightSaddleMouth,
                headSaddle,
                mouthSaddleWrap
        };

        this.ridingParts = new ModelPart[]{
                leftSaddleLine,
                rightSaddleLine
        };
    }

    public static LayerDefinition getTexturedModelData() {
        CubeDeformation deformation = CubeDeformation.NONE;

        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        PartDefinition body = root.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(0, 32)
                        .addBox(
                                -5.0F,
                                -8.0F,
                                -20.0F,
                                10.0F,
                                10.0F,
                                22.0F,
                                deformation
                        )
                        .texOffs(0, 84)
                        .addBox(
                                -8.0F,
                                -14.0F,
                                -3.0F,
                                16.0F,
                                6.0F,
                                6.0F,
                                deformation
                        )
                        .texOffs(0, 64)
                        .addBox(
                                -11.0F,
                                -9.0F,
                                -13.0F,
                                6.0F,
                                10.0F,
                                10.0F,
                                deformation
                        )
                        .texOffs(32, 64)
                        .addBox(
                                5.0F,
                                -9.0F,
                                -13.0F,
                                6.0F,
                                10.0F,
                                10.0F,
                                deformation
                        ),
                PartPose.offset(0.0F, 11.0F, 9.0F)
        );

        PartDefinition headParts = root.addOrReplaceChild(
                "head_parts",
                CubeListBuilder.create()
                        .texOffs(0, 35)
                        .addBox(
                                -2.05F,
                                -6.0F,
                                -2.0F,
                                4.0F,
                                12.0F,
                                7.0F
                        ),
                PartPose.offsetAndRotation(
                        0.0F,
                        4.0F,
                        -12.0F,
                        0.5235988F,
                        0.0F,
                        0.0F
                )
        );

        PartDefinition head = headParts.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 13)
                        .addBox(
                                -3.0F,
                                -11.0F,
                                -2.0F,
                                6.0F,
                                5.0F,
                                7.0F,
                                deformation
                        ),
                PartPose.ZERO
        );

        headParts.addOrReplaceChild(
                "mane",
                CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(
                                -1.0F,
                                -11.0F,
                                5.01F,
                                2.0F,
                                16.0F,
                                2.0F,
                                deformation
                        ),
                PartPose.ZERO
        );

        headParts.addOrReplaceChild(
                "upper_mouth",
                CubeListBuilder.create()
                        .texOffs(0, 25)
                        .addBox(
                                -2.0F,
                                -11.0F,
                                -7.0F,
                                4.0F,
                                5.0F,
                                5.0F,
                                deformation
                        ),
                PartPose.ZERO
        );

        root.addOrReplaceChild(
                "left_hind_leg",
                CubeListBuilder.create()
                        .texOffs(48, 21)
                        .mirror()
                        .addBox(
                                -3.0F,
                                -1.01F,
                                -1.0F,
                                4.0F,
                                11.0F,
                                4.0F,
                                deformation
                        ),
                PartPose.offset(4.0F, 14.0F, 7.0F)
        );

        root.addOrReplaceChild(
                "right_hind_leg",
                CubeListBuilder.create()
                        .texOffs(48, 21)
                        .addBox(
                                -1.0F,
                                -1.01F,
                                -1.0F,
                                4.0F,
                                11.0F,
                                4.0F,
                                deformation
                        ),
                PartPose.offset(-4.0F, 14.0F, 7.0F)
        );

        root.addOrReplaceChild(
                "left_front_leg",
                CubeListBuilder.create()
                        .texOffs(48, 21)
                        .mirror()
                        .addBox(
                                -3.0F,
                                -1.01F,
                                -1.9F,
                                4.0F,
                                11.0F,
                                4.0F,
                                deformation
                        ),
                PartPose.offset(4.0F, 14.0F, -12.0F)
        );

        root.addOrReplaceChild(
                "right_front_leg",
                CubeListBuilder.create()
                        .texOffs(48, 21)
                        .addBox(
                                -1.0F,
                                -1.01F,
                                -1.9F,
                                4.0F,
                                11.0F,
                                4.0F,
                                deformation
                        ),
                PartPose.offset(-4.0F, 14.0F, -12.0F)
        );

        CubeDeformation babyLegDeformation =
                deformation.extend(0.0F, 5.5F, 0.0F);

        root.addOrReplaceChild(
                "left_hind_baby_leg",
                CubeListBuilder.create()
                        .texOffs(48, 21)
                        .mirror()
                        .addBox(
                                -3.0F,
                                -1.01F,
                                -1.0F,
                                4.0F,
                                11.0F,
                                4.0F,
                                babyLegDeformation
                        ),
                PartPose.offset(4.0F, 14.0F, 7.0F)
        );

        root.addOrReplaceChild(
                "right_hind_baby_leg",
                CubeListBuilder.create()
                        .texOffs(48, 21)
                        .addBox(
                                -1.0F,
                                -1.01F,
                                -1.0F,
                                4.0F,
                                11.0F,
                                4.0F,
                                babyLegDeformation
                        ),
                PartPose.offset(-4.0F, 14.0F, 7.0F)
        );

        root.addOrReplaceChild(
                "left_front_baby_leg",
                CubeListBuilder.create()
                        .texOffs(48, 21)
                        .mirror()
                        .addBox(
                                -3.0F,
                                -1.01F,
                                -1.9F,
                                4.0F,
                                11.0F,
                                4.0F,
                                babyLegDeformation
                        ),
                PartPose.offset(4.0F, 14.0F, -12.0F)
        );

        root.addOrReplaceChild(
                "right_front_baby_leg",
                CubeListBuilder.create()
                        .texOffs(48, 21)
                        .addBox(
                                -1.0F,
                                -1.01F,
                                -1.9F,
                                4.0F,
                                11.0F,
                                4.0F,
                                babyLegDeformation
                        ),
                PartPose.offset(-4.0F, 14.0F, -12.0F)
        );

        body.addOrReplaceChild(
                "tail",
                CubeListBuilder.create()
                        .texOffs(42, 36)
                        .addBox(
                                -1.5F,
                                0.0F,
                                0.0F,
                                3.0F,
                                14.0F,
                                4.0F,
                                deformation
                        ),
                PartPose.offsetAndRotation(
                        0.0F,
                        -5.0F,
                        2.0F,
                        0.5235988F,
                        0.0F,
                        0.0F
                )
        );

        body.addOrReplaceChild(
                "saddle",
                CubeListBuilder.create()
                        .texOffs(26, 0)
                        .addBox(
                                -5.0F,
                                -8.0F,
                                -9.0F,
                                10.0F,
                                9.0F,
                                9.0F,
                                new CubeDeformation(0.5F)
                        ),
                PartPose.ZERO
        );

        headParts.addOrReplaceChild(
                "left_saddle_mouth",
                CubeListBuilder.create()
                        .texOffs(29, 5)
                        .addBox(
                                2.0F,
                                -9.0F,
                                -6.0F,
                                1.0F,
                                2.0F,
                                2.0F,
                                deformation
                        ),
                PartPose.ZERO
        );

        headParts.addOrReplaceChild(
                "right_saddle_mouth",
                CubeListBuilder.create()
                        .texOffs(29, 5)
                        .addBox(
                                -3.0F,
                                -9.0F,
                                -6.0F,
                                1.0F,
                                2.0F,
                                2.0F,
                                deformation
                        ),
                PartPose.ZERO
        );

        headParts.addOrReplaceChild(
                "left_saddle_line",
                CubeListBuilder.create()
                        .texOffs(32, 2)
                        .addBox(
                                3.1F,
                                -6.0F,
                                -8.0F,
                                0.0F,
                                3.0F,
                                16.0F
                        ),
                PartPose.rotation(-0.5235988F, 0.0F, 0.0F)
        );

        headParts.addOrReplaceChild(
                "right_saddle_line",
                CubeListBuilder.create()
                        .texOffs(32, 2)
                        .addBox(
                                -3.1F,
                                -6.0F,
                                -8.0F,
                                0.0F,
                                3.0F,
                                16.0F
                        ),
                PartPose.rotation(-0.5235988F, 0.0F, 0.0F)
        );

        headParts.addOrReplaceChild(
                "head_saddle",
                CubeListBuilder.create()
                        .texOffs(1, 1)
                        .addBox(
                                -3.0F,
                                -11.0F,
                                -1.9F,
                                6.0F,
                                5.0F,
                                6.0F,
                                new CubeDeformation(0.22F)
                        ),
                PartPose.ZERO
        );

        headParts.addOrReplaceChild(
                "mouth_saddle_wrap",
                CubeListBuilder.create()
                        .texOffs(19, 0)
                        .addBox(
                                -2.0F,
                                -11.0F,
                                -4.0F,
                                4.0F,
                                5.0F,
                                2.0F,
                                new CubeDeformation(0.2F)
                        ),
                PartPose.ZERO
        );

        head.addOrReplaceChild(
                "left_ear",
                CubeListBuilder.create()
                        .texOffs(19, 16)
                        .addBox(
                                0.55F,
                                -13.0F,
                                4.0F,
                                2.0F,
                                3.0F,
                                1.0F,
                                new CubeDeformation(-0.001F)
                        ),
                PartPose.ZERO
        );

        head.addOrReplaceChild(
                "right_ear",
                CubeListBuilder.create()
                        .texOffs(19, 16)
                        .addBox(
                                -2.55F,
                                -13.0F,
                                4.0F,
                                2.0F,
                                3.0F,
                                1.0F,
                                new CubeDeformation(-0.001F)
                        ),
                PartPose.ZERO
        );

        return LayerDefinition.create(meshDefinition, 128, 128);
    }

    @Override
    public void setupAnim(EquineRenderState state) {
        super.setupAnim(state);

        boolean saddled = !state.saddle.isEmpty();

        for (ModelPart part : this.saddleParts) {
            part.visible = saddled;
        }

        for (ModelPart part : this.ridingParts) {
            part.visible = saddled && state.isRidden;
        }

        this.rightHindBabyLeg.loadPose(this.rightHindLeg.storePose());
        this.leftHindBabyLeg.loadPose(this.leftHindLeg.storePose());
        this.rightFrontBabyLeg.loadPose(this.rightFrontLeg.storePose());
        this.leftFrontBabyLeg.loadPose(this.leftFrontLeg.storePose());

        boolean baby = state.isBaby;

        this.rightHindLeg.visible = !baby;
        this.leftHindLeg.visible = !baby;
        this.rightFrontLeg.visible = !baby;
        this.leftFrontLeg.visible = !baby;

        this.rightHindBabyLeg.visible = baby;
        this.leftHindBabyLeg.visible = baby;
        this.rightFrontBabyLeg.visible = baby;
        this.leftFrontBabyLeg.visible = baby;
    }
}