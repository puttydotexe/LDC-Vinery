package net.satisfy.vinery.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.satisfy.vinery.core.Vinery;

public class WinemakerBootsModel extends EntityModel<HumanoidRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Vinery.identifier("winemaker_boots"), "main");

    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public WinemakerBootsModel(ModelPart root) {
        super(root);

        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");

        root.xScale = 1.08F;
        root.yScale = 1.08F;
        root.zScale = 1.08F;

        // Old rendering performed scale(1.08) followed by translateY(-0.125).
        root.y = -2.16F;
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        root.addOrReplaceChild(
                "right_leg",
                CubeListBuilder.create()
                        .texOffs(36, 19)
                        .addBox(
                                -2.0F,
                                8.0F,
                                -2.0F,
                                4.0F,
                                4.0F,
                                4.0F,
                                new CubeDeformation(0.26F)
                        ),
                PartPose.offset(-1.9F, 12.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "left_leg",
                CubeListBuilder.create()
                        .texOffs(36, 19)
                        .addBox(
                                -2.0F,
                                8.0F,
                                -2.0F,
                                4.0F,
                                4.0F,
                                4.0F,
                                new CubeDeformation(0.26F)
                        ),
                PartPose.offset(1.9F, 12.0F, 0.0F)
        );

        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    public void copyLegs(ModelPart rightLegModel, ModelPart leftLegModel) {
        this.rightLeg.loadPose(rightLegModel.storePose());
        this.leftLeg.loadPose(leftLegModel.storePose());
    }
}