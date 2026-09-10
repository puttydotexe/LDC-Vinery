package net.satisfy.vinery.client.render.entity;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.entity.DarkCherryBoatEntity;

public class DarkCherryBoatRenderer extends BoatRenderer {
    public DarkCherryBoatRenderer(
            EntityRendererProvider.Context context,
            boolean hasChest
    ) {
        super(
                context,
                hasChest
                        ? createChestBoatModelName(
                                DarkCherryBoatEntity.Type.DARK_CHERRY
                        )
                        : createBoatModelName(
                                DarkCherryBoatEntity.Type.DARK_CHERRY
                        )
        );

        this.shadowRadius = 0.8F;
    }

    public static ModelLayerLocation createBoatModelName(
            DarkCherryBoatEntity.Type type
    ) {
        return createLocation(type.getModelLocation());
    }

    public static ModelLayerLocation createChestBoatModelName(
            DarkCherryBoatEntity.Type type
    ) {
        return createLocation(type.getChestModelLocation());
    }

    private static ModelLayerLocation createLocation(String path) {
        return new ModelLayerLocation(
                Identifier.fromNamespaceAndPath(Vinery.MOD_ID, path),
                "main"
        );
    }
}