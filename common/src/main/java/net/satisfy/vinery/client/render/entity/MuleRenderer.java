package net.satisfy.vinery.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.resources.Identifier;
import net.satisfy.vinery.client.model.MuleModel;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.entity.TraderMuleEntity;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class MuleRenderer extends AbstractHorseRenderer<
        TraderMuleEntity,
        EquineRenderState,
        MuleModel> {

    private static final Identifier TEXTURE =
            Vinery.identifier("textures/entity/wandering_mule.png");

    public MuleRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new MuleModel(context.bakeLayer(MuleModel.LAYER_LOCATION)),
                new MuleModel(context.bakeLayer(MuleModel.LAYER_LOCATION))
        );

        this.shadowRadius = 0.5F;
    }

    @Override
    public @NotNull Identifier getTextureLocation(EquineRenderState state) {
        return TEXTURE;
    }

    @Override
    public @NotNull EquineRenderState createRenderState() {
        return new EquineRenderState();
    }
}