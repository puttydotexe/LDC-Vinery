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

public class StrawHatModel extends EntityModel<HumanoidRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Vinery.identifier("straw_hat"), "main");

    private final ModelPart head;

    public StrawHatModel(ModelPart root) {
        super(root);

        this.head = root.getChild("head");

        root.xScale = 1.05F;
        root.yScale = 1.05F;
        root.zScale = 1.05F;
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(-17, 13)
                        .addBox(
                                -8.5F,
                                -6.0F,
                                -8.5F,
                                17.0F,
                                0.0F,
                                17.0F,
                                new CubeDeformation(0.0F)
                        )
                        .texOffs(0, 0)
                        .addBox(
                                -4.5F,
                                -10.0F,
                                -4.5F,
                                9.0F,
                                4.0F,
                                9.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );

        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    public void copyHead(ModelPart model) {
        this.head.loadPose(model.storePose());
    }
}
