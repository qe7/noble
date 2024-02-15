package sh.squeami.kami.uis.click.impl.component.numberComponents;

import sh.squeami.kami.Kami;
import sh.squeami.kami.fonts.api.CFontRenderer;
import sh.squeami.kami.settings.api.types.Setting;
import sh.squeami.kami.settings.impl.NumberSetting;
import sh.squeami.kami.uis.click.api.GuiClick;
import sh.squeami.kami.uis.click.api.types.Component;
import sh.squeami.kami.uis.click.impl.ClickButton;
import sh.squeami.kami.uis.click.impl.ClickPanel;
import sh.squeami.kami.utils.render.ColorUtil;
import sh.squeami.kami.utils.render.RenderUtil;

import java.io.IOException;

public class DoubleComponent extends Component {

    private final NumberSetting<Double> setting;

    private float posX, posY;

    private boolean dragging = false;

    public DoubleComponent(ClickButton button, NumberSetting<Double> setting) {
        super(button, setting);

        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float posX, float posY) {

        CFontRenderer fontRenderer = Kami.INSTANCE.getFontManager().getFontRenderer("TAHOMA_14");

        this.posX = posX;
        this.posY = posY;

        double value = setting.getValue();
        double min = setting.getMinimum();
        double max = setting.getMaximum();
        double percent = (value - min) / (max - min);

        if (dragging) {
            double diff = Math.min(ClickPanel.PANEL_WIDTH, Math.max(0, mouseX - posX));
            if (diff == 0) setting.setValue(min);
            double percentNew = diff / (ClickPanel.PANEL_WIDTH - 4.0f);
            if (percentNew < 0.0f) percentNew = 0.0f;
            if (percentNew > 1.0f) percentNew = 1.0f;
            double newValue = min + (max - min) * percentNew;
            setting.setValue(roundToIncrement(newValue, setting.getIncrement()));
        }

        // draw background of the button
        RenderUtil.drawFilledRect(posX,
                posY,
                ClickPanel.PANEL_WIDTH,
                ClickPanel.PANEL_HEIGHT,
                GuiClick.isHovered(posX, posY, ClickPanel.PANEL_WIDTH, ClickPanel.PANEL_HEIGHT, mouseX, mouseY) ? ColorUtil.color(20, 20, 20, (int) (200 * GuiClick.menuAlphaPercentage)).darker().getRGB() : ColorUtil.color(20, 20, 20, (int) (200 * GuiClick.menuAlphaPercentage)).getRGB());

        RenderUtil.drawFilledRect(posX,
                posY,
                (float) (ClickPanel.PANEL_WIDTH * percent),
                ClickPanel.PANEL_HEIGHT,
                GuiClick.isHovered(posX, posY, ClickPanel.PANEL_WIDTH, ClickPanel.PANEL_HEIGHT, mouseX, mouseY) ? ColorUtil.color(105, 20, 25, 200).darker().getRGB() : ColorUtil.color(105, 20, 25, 200).getRGB());

        RenderUtil.drawRect(posX,
                posY,
                (float) (ClickPanel.PANEL_WIDTH * percent),
                ClickPanel.PANEL_HEIGHT,
                0.5F,
                ColorUtil.color(0, 0, 0, (int) (200 * GuiClick.menuAlphaPercentage)).getRGB());

        // draw text
        fontRenderer.drawStringWithShadow(setting.getName(), posX + 4, posY + 5, -1);
        fontRenderer.drawStringWithShadow(String.format("%.2f", this.setting.getValue()), posX + ClickPanel.PANEL_WIDTH - fontRenderer.getStringWidth(String.valueOf(String.format("%.2f", this.setting.getValue()))) - 4, posY + 5, -1);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (GuiClick.isHovered(posX, posY, ClickPanel.PANEL_WIDTH, ClickPanel.PANEL_HEIGHT, mouseX, mouseY)) {
            dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (dragging) dragging = false;
    }

    private double roundToIncrement(double value, double increment) {
        if (increment == 0) return value;
        if (value < 0) return -roundToIncrement(-value, increment);
        double remainder = value % increment;
        if (remainder == 0) return value;
        return value + increment - remainder;
    }
}
