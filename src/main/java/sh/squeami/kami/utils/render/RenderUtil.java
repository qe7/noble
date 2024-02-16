package sh.squeami.kami.utils.render;

import net.minecraft.client.gui.Gui;

public class RenderUtil {

    public static void drawRect(float x, float y, float width, float height, float lineWidth, int color) {
        Gui.drawRect(x + lineWidth, y, x + width - lineWidth, y + lineWidth, color);
        Gui.drawRect(x + lineWidth, y + height - lineWidth, x + width - lineWidth, y + height, color);
        Gui.drawRect(x, y, x + lineWidth, y + height, color);
        Gui.drawRect(x + width - lineWidth, y, x + width, y + height, color);
    }

    public static void drawFilledRect(float x, float y, float width, float height, int color) {
        Gui.drawRect(x, y, x + width, y + height, color);
    }

    public static void drawFilledGradientRect(float x, float y, float width, float height, int startColor, int endColor) {
        Gui.drawGradientRect(x, y, x + width, y + height, startColor, endColor);
    }
}
