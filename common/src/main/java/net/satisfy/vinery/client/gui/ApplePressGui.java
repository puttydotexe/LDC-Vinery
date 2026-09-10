package net.satisfy.vinery.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.satisfy.vinery.client.gui.handler.ApplePressGuiHandler;
import net.satisfy.vinery.core.Vinery;

@Environment(EnvType.CLIENT)
public class ApplePressGui extends AbstractContainerScreen<ApplePressGuiHandler> {
    public static final Identifier TEXTURE = Vinery.identifier("textures/gui/apple_press_gui.png");

    public static final int MASHING_BAR_X = 40;
    public static final int MASHING_BAR_Y = 17;
    public static final int MASHING_BAR_WIDTH = 24;
    public static final int MASHING_BAR_HEIGHT = 38;
    public static final int MASHING_BAR_U = 176;
    public static final int MASHING_BAR_V = 0;

    public static final int FERMENTING_BAR_X = 101;
    public static final int FERMENTING_BAR_Y = 18;
    public static final int FERMENTING_BAR_WIDTH = 10;
    public static final int FERMENTING_BAR_HEIGHT = 28;
    public static final int FERMENTING_BAR_U = 176;
    public static final int FERMENTING_BAR_V = 47;

    private static final int TEXTURE_WIDTH = 256;
    private static final int TEXTURE_HEIGHT = 256;

    public ApplePressGui(
            ApplePressGuiHandler handler,
            Inventory inventory,
            Component title
    ) {
        super(handler, inventory, title);
    }

    @Override
    public void extractBackground(
            GuiGraphicsExtractor graphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);

        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                TEXTURE,
                this.leftPos,
                this.topPos,
                0,
                0,
                this.imageWidth,
                this.imageHeight,
                TEXTURE_WIDTH,
                TEXTURE_HEIGHT
        );

        extractProgressArrows(graphics);
    }

    private void extractProgressArrows(GuiGraphicsExtractor graphics) {
        if (this.menu.isCrafting(0)) {
            int progress = this.menu.getScaledProgress(0);

            int x = this.leftPos + MASHING_BAR_X;
            int y = this.topPos + MASHING_BAR_Y + progress;
            int textureV = MASHING_BAR_V + progress;
            int height = MASHING_BAR_HEIGHT - progress;

            if (height > 0) {
                graphics.blit(
                        RenderPipelines.GUI_TEXTURED,
                        TEXTURE,
                        x,
                        y,
                        MASHING_BAR_U,
                        textureV,
                        MASHING_BAR_WIDTH,
                        height,
                        TEXTURE_WIDTH,
                        TEXTURE_HEIGHT
                );
            }
        }

        if (this.menu.isCrafting(1)) {
            int progress = this.menu.getScaledProgress(1);

            if (progress > 0) {
                int x = this.leftPos + FERMENTING_BAR_X;
                int y = this.topPos
                        + FERMENTING_BAR_Y
                        + FERMENTING_BAR_HEIGHT
                        - progress;

                int textureV = FERMENTING_BAR_V
                        + FERMENTING_BAR_HEIGHT
                        - progress;

                graphics.blit(
                        RenderPipelines.GUI_TEXTURED,
                        TEXTURE,
                        x,
                        y,
                        FERMENTING_BAR_U,
                        textureV,
                        FERMENTING_BAR_WIDTH,
                        progress,
                        TEXTURE_WIDTH,
                        TEXTURE_HEIGHT
                );
            }
        }
    }
}
