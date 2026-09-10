package net.satisfy.vinery.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.satisfy.vinery.client.gui.handler.FermentationBarrelGuiHandler;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.platform.PlatformHelper;

@Environment(EnvType.CLIENT)
public class FermentationBarrelGui extends AbstractContainerScreen<FermentationBarrelGuiHandler> {
    public static final Identifier BACKGROUND =
            Vinery.identifier("textures/gui/fermentation_barrel_gui.png");

    private static final int TEXTURE_WIDTH = 256;
    private static final int TEXTURE_HEIGHT = 256;

    private static final int FLUID_WIDTH = 20;
    private static final int FLUID_X = 82;
    private static final int FLUID_Y = 44;

    private static final int CRAFT_PROGRESS_TEXTURE_X = 176;
    private static final int CRAFT_PROGRESS_TEXTURE_Y = 0;
    private static final int CRAFT_PROGRESS_WIDTH = 11;
    private static final int CRAFT_PROGRESS_HEIGHT = 29;
    private static final int CRAFT_PROGRESS_GUI_X = 122;
    private static final int CRAFT_PROGRESS_GUI_Y = 20;
    private static final int CRAFT_PROGRESS_GUI_HEIGHT = 29;

    public FermentationBarrelGui(
            FermentationBarrelGuiHandler handler,
            Inventory inventory,
            Component title
    ) {
        super(handler, inventory, title);

        this.titleLabelX = 8;
        this.titleLabelY = 6;

        this.inventoryLabelX = 8;
        this.inventoryLabelY = this.imageHeight - 94;
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
                BACKGROUND,
                this.leftPos,
                this.topPos,
                0,
                0,
                this.imageWidth,
                this.imageHeight,
                TEXTURE_WIDTH,
                TEXTURE_HEIGHT
        );

        drawJuiceBar(
                graphics,
                this.menu.getJuiceType(),
                this.menu.getFluidLevel(),
                this.leftPos + FLUID_X,
                this.topPos + FLUID_Y
        );

        extractCraftingProgress(graphics);
    }

    @Override
    public void extractContents(
            GuiGraphicsExtractor graphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        super.extractContents(graphics, mouseX, mouseY, partialTick);

        if (isMouseOverFluidArea(mouseX, mouseY)) {
            graphics.setTooltipForNextFrame(
                    this.font,
                    getFluidTooltip(),
                    mouseX,
                    mouseY
            );
        }

        if (isMouseOverCraftingTimeArea(mouseX, mouseY)) {
            graphics.setTooltipForNextFrame(
                    this.font,
                    getCraftingTimeTooltip(),
                    mouseX,
                    mouseY
            );
        }
    }

    @Override
    protected void extractLabels(
            GuiGraphicsExtractor graphics,
            int mouseX,
            int mouseY
    ) {
        graphics.text(
                this.font,
                this.title,
                this.titleLabelX,
                this.titleLabelY,
                0xFF404040,
                false
        );

        graphics.text(
                this.font,
                this.playerInventoryTitle,
                this.inventoryLabelX,
                this.inventoryLabelY,
                0xFF404040,
                false
        );
    }

    private Component getFluidTooltip() {
        String juiceType = this.menu.getJuiceType();
        int fluidLevel = this.menu.getFluidLevel();
        int maxFluidLevel = PlatformHelper.getMaxFluidLevel();

        double percentage = maxFluidLevel > 0
                ? (double) fluidLevel / maxFluidLevel * 100.0
                : 0.0;

        String percentageString = String.format("%.2f", percentage);

        if (juiceType.startsWith("red")) {
            String region = juiceType.substring(4);

            return Component.translatable(
                    "tooltip.vinery.fermentation_barrel.red_"
                            + region
                            + "_juice_with_percentage",
                    percentageString
            );
        }

        if (juiceType.startsWith("white")) {
            String region = juiceType.substring(6);

            return Component.translatable(
                    "tooltip.vinery.fermentation_barrel.white_"
                            + region
                            + "_juice_with_percentage",
                    percentageString
            );
        }

        if (juiceType.equals("apple")) {
            return Component.translatable(
                    "tooltip.vinery.fermentation_barrel.apple_juice_with_percentage",
                    percentageString
            );
        }

        return Component.translatable(
                "tooltip.vinery.fermentation_barrel.empty"
        );
    }

    private Component getCraftingTimeTooltip() {
        int totalTicks = this.menu.data.get(1);
        int currentTicks = this.menu.data.get(0);
        int remainingTicks = totalTicks - currentTicks;

        if (remainingTicks <= 0) {
            return Component.translatable(
                    "tooltip.vinery.fermentation_barrel.crafting_time",
                    "0:00 Seconds"
            );
        }

        int seconds = remainingTicks / 20;
        int minutes = seconds / 60;
        seconds %= 60;

        String formattedTime = String.format(
                "%d:%02d Seconds",
                minutes,
                seconds
        );

        return Component.translatable(
                "tooltip.vinery.fermentation_barrel.crafting_time",
                formattedTime
        );
    }

    private boolean isMouseOverFluidArea(int mouseX, int mouseY) {
        int left = this.leftPos + FLUID_X - 1;
        int top = this.topPos + FLUID_Y - 5;
        int right = this.leftPos + FLUID_X + FLUID_WIDTH + 1;
        int bottom = this.topPos + FLUID_Y + 10;

        return mouseX >= left
                && mouseX <= right
                && mouseY >= top
                && mouseY <= bottom;
    }

    private boolean isMouseOverCraftingTimeArea(
            int mouseX,
            int mouseY
    ) {
        int totalTicks = this.menu.data.get(1);
        int currentTicks = this.menu.data.get(0);

        if (totalTicks <= 0 || currentTicks >= totalTicks) {
            return false;
        }

        int left = this.leftPos + CRAFT_PROGRESS_GUI_X;
        int top = this.topPos + CRAFT_PROGRESS_GUI_Y;
        int right = left + CRAFT_PROGRESS_WIDTH;
        int bottom = top + CRAFT_PROGRESS_GUI_HEIGHT;

        return mouseX >= left
                && mouseX <= right
                && mouseY >= top
                && mouseY <= bottom;
    }

    public static void drawJuiceBar(
            GuiGraphicsExtractor graphics,
            String juiceType,
            int juiceAmount,
            int originX,
            int originY
    ) {
        int maxFluid = PlatformHelper.getMaxFluidLevel();

        int scaledWidth = maxFluid > 0
                ? (int) ((double) juiceAmount / maxFluid * FLUID_WIDTH)
                : 0;

        scaledWidth = Math.max(
                0,
                Math.min(FLUID_WIDTH, scaledWidth)
        );

        if (scaledWidth <= 0) {
            return;
        }

        int textureV;

        if (juiceType.startsWith("red")) {
            textureV = 29;
        } else if (juiceType.startsWith("white")) {
            textureV = 33;
        } else if (juiceType.equals("apple")) {
            textureV = 37;
        } else {
            return;
        }

        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                BACKGROUND,
                originX,
                originY,
                176,
                textureV,
                scaledWidth,
                4,
                TEXTURE_WIDTH,
                TEXTURE_HEIGHT
        );
    }

    private void extractCraftingProgress(
            GuiGraphicsExtractor graphics
    ) {
        int filledHeight =
                this.menu.getScaledProgress(CRAFT_PROGRESS_HEIGHT);

        if (filledHeight <= 0) {
            return;
        }

        int drawY =
                this.topPos
                        + CRAFT_PROGRESS_GUI_Y
                        + CRAFT_PROGRESS_GUI_HEIGHT
                        - filledHeight;

        int textureY =
                CRAFT_PROGRESS_TEXTURE_Y
                        + CRAFT_PROGRESS_HEIGHT
                        - filledHeight;

        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                BACKGROUND,
                this.leftPos + CRAFT_PROGRESS_GUI_X,
                drawY,
                CRAFT_PROGRESS_TEXTURE_X,
                textureY,
                CRAFT_PROGRESS_WIDTH,
                filledHeight,
                TEXTURE_WIDTH,
                TEXTURE_HEIGHT
        );
    }
}
