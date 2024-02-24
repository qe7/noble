package sh.squeami.noble.uis.click.impl.component.numberComponents;

import sh.squeami.noble.Noble;
import sh.squeami.noble.fonts.api.CFontRenderer;
import sh.squeami.noble.settings.impl.NumberSetting;
import sh.squeami.noble.uis.click.api.GuiClick;
import sh.squeami.noble.uis.click.api.types.Component;
import sh.squeami.noble.uis.click.impl.ClickButton;
import sh.squeami.noble.uis.click.impl.ClickPanel;
import sh.squeami.noble.utils.render.ColorUtil;
import sh.squeami.noble.utils.render.RenderUtil;

import java.io.IOException;

public class FloatComponent extends Component {

    private final NumberSetting<Float> setting;

    private float posX, posY;

    private boolean dragging = false;

    public FloatComponent(ClickButton button, NumberSetting<Float> setting) {
        super(button, setting);

        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float posX, float posY) {

        CFontRenderer fontRenderer = Noble.INSTANCE.getFontManager().getFontRenderer("TAHOMA_14");

        this.posX = posX;
        this.posY = posY;

        float value = setting.getValue();
        float min = setting.getMinimum();
        float max = setting.getMaximum();
        float percent = (value - min) / (max - min);

        if (dragging) {
            float diff = Math.min(ClickPanel.PANEL_WIDTH, Math.max(0, mouseX - posX));
            if (diff == 0) setting.setValue(min);
            float percentNew = diff / (ClickPanel.PANEL_WIDTH - 4.0f);
            if (percentNew < 0.0f) percentNew = 0.0f;
            if (percentNew > 1.0f) percentNew = 1.0f;
            float newValue = min + (max - min) * percentNew;
            setting.setValue(roundToIncrement(newValue, setting.getIncrement()));
        }

        // draw background of the button
        RenderUtil.drawFilledRect(posX,
                posY,
                ClickPanel.PANEL_WIDTH,
                ClickPanel.PANEL_HEIGHT,
                GuiClick.isHovered(posX, posY, ClickPanel.PANEL_WIDTH, ClickPanel.PANEL_HEIGHT, mouseX, mouseY) ? ColorUtil.color(20, 20, 20, (int) (200 * GuiClick.menuAlphaPercentage)).darker().getRGB() : ColorUtil.color(20, 20, 20, (int) (200 * GuiClick.menuAlphaPercentage)).getRGB());

        RenderUtil.drawFilledGradientRect(posX,
                posY,
                ClickPanel.PANEL_WIDTH * percent,
                ClickPanel.PANEL_HEIGHT,
                GuiClick.isHovered(posX, posY, ClickPanel.PANEL_WIDTH, ClickPanel.PANEL_HEIGHT, mouseX, mouseY) ? ColorUtil.color(105, 20, 25, 200).darker().getRGB() : ColorUtil.color(105, 20, 25, 200).getRGB(),
                GuiClick.isHovered(posX, posY, ClickPanel.PANEL_WIDTH, ClickPanel.PANEL_HEIGHT, mouseX, mouseY) ? ColorUtil.color(105, 20, 25, 200).darker().darker().getRGB() : ColorUtil.color(105, 20, 25, 200).darker().getRGB());

        RenderUtil.drawRect(posX,
                posY,
                (float) (ClickPanel.PANEL_WIDTH * percent),
                ClickPanel.PANEL_HEIGHT,
                0.5F,
                ColorUtil.color(0, 0, 0, (int) (200 * GuiClick.menuAlphaPercentage)).getRGB());

        // draw text
        fontRenderer.drawStringWithShadow(setting.getName(), posX + 4, posY + 5, -1);
        fontRenderer.drawStringWithShadow(String.format("%.2f", this.setting.getValue()), posX + ClickPanel.PANEL_WIDTH - fontRenderer.getStringWidth(String.format("%.2f", this.setting.getValue())) - 4, posY + 5, -1);
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

    private float roundToIncrement(float value, float increment) {
        if (increment == 0) return value;
        if (value < 0) return -roundToIncrement(-value, increment);
        double remainder = value % increment;
        if (remainder == 0) return value;
        return (float) (value + increment - remainder);
    }
}
