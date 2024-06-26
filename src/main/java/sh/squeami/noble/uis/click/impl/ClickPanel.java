package sh.squeami.noble.uis.click.impl;

import sh.squeami.noble.Noble;
import sh.squeami.noble.features.api.enums.FeatureCategory;
import sh.squeami.noble.fonts.api.CFontRenderer;
import sh.squeami.noble.uis.click.api.GuiClick;
import sh.squeami.noble.utils.render.ColorUtil;
import sh.squeami.noble.utils.render.RenderUtil;

import java.io.IOException;
import java.util.ArrayList;

public class ClickPanel {

    private final ArrayList<ClickButton> buttonArrayList = new ArrayList<>();

    private final FeatureCategory category;

    private float posX, posY;
    private float dragX, dragY;
    public static final float PANEL_WIDTH = 120, PANEL_HEIGHT = 14;

    private boolean dragging, open = true;

    public ClickPanel(FeatureCategory category, float startX, float startY) {
        this.category = category;

        this.posX = startX;
        this.posY = startY;

        // populate buttonArrayList with buttons
        Noble.INSTANCE.getFeatureManager().featuresFromCategory(category).forEach(feature -> {
            buttonArrayList.add(new ClickButton(feature));
        });
    }

    public void drawScreen(int mouseX, int mouseY) {

        CFontRenderer fontRenderer = Noble.INSTANCE.getFontManager().getFontRenderer("TAHOMA_14");

        // handle dragging
        if (dragging) {
            this.posX = dragX + mouseX;
            this.posY = dragY + mouseY;
        }

        // draw panel
        RenderUtil.drawFilledGradientRect(posX,
                posY,
                PANEL_WIDTH,
                PANEL_HEIGHT,
                GuiClick.isHovered(posX, posY, PANEL_WIDTH, PANEL_HEIGHT, mouseX, mouseY) ? ColorUtil.color(105, 20, 25, (int) (200 * GuiClick.menuAlphaPercentage)).darker().getRGB() : ColorUtil.color(105, 20, 25, (int) (200 * GuiClick.menuAlphaPercentage)).getRGB(),
                GuiClick.isHovered(posX, posY, PANEL_WIDTH, PANEL_HEIGHT, mouseX, mouseY) ? ColorUtil.color(105, 20, 25, (int) (200 * GuiClick.menuAlphaPercentage)).darker().darker().getRGB() : ColorUtil.color(105, 20, 25, (int) (200 * GuiClick.menuAlphaPercentage)).darker().getRGB());

        // draw border
        RenderUtil.drawRect(posX,
                posY,
                PANEL_WIDTH,
                PANEL_HEIGHT,
                0.5F,
                ColorUtil.color(0, 0, 0, (int) (200 * GuiClick.menuAlphaPercentage)).getRGB());

        // draw text
        fontRenderer.drawStringWithShadow(category.getName(),
                posX + ((PANEL_WIDTH / 2) - (float) (fontRenderer.getStringWidth(category.getName())) / 2),
                posY + ((PANEL_HEIGHT / 2) - ((float) fontRenderer.getHeight() / 2)),
                -1);

        // handle buttons
        if (!open) return;
        float y = this.posY + PANEL_HEIGHT;
        for (ClickButton button : buttonArrayList) {
            button.drawScreen(mouseX, mouseY, posX, y);
            y += button.returnHeight();
        }
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        if (!open) return;
        for (ClickButton button : buttonArrayList) {
            button.keyTyped(typedChar, keyCode);
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        final boolean b = GuiClick.isHovered(posX, posY, PANEL_WIDTH, PANEL_HEIGHT, mouseX, mouseY);
        if (b) {
            switch (mouseButton) {
                case 0 -> {
                    dragging = true;
                    // calculate the drag, so the panel doesn't jump to the mouse position
                    dragX = posX - mouseX;
                    dragY = posY - mouseY;
                }
                case 1 -> open = !open;
            }
        }

        if (!open) return;
        for (ClickButton button : buttonArrayList) {
            button.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (dragging) dragging = false;

        if (!open) return;
        for (ClickButton button : buttonArrayList) {
            button.mouseReleased(mouseX, mouseY, state);
        }
    }

    public FeatureCategory getCategory() {
        return category;
    }
}
