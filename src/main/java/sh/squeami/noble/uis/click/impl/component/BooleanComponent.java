package sh.squeami.noble.uis.click.impl.component;

import sh.squeami.noble.Noble;
import sh.squeami.noble.fonts.api.CFontRenderer;
import sh.squeami.noble.settings.impl.BooleanSetting;
import sh.squeami.noble.uis.click.api.GuiClick;
import sh.squeami.noble.uis.click.api.types.Component;
import sh.squeami.noble.uis.click.impl.ClickButton;
import sh.squeami.noble.uis.click.impl.ClickPanel;
import sh.squeami.noble.utils.render.ColorUtil;
import sh.squeami.noble.utils.render.RenderUtil;

import java.io.IOException;

public class BooleanComponent extends Component {

    private final BooleanSetting setting;

    private float posX, posY;

    public BooleanComponent(ClickButton button, BooleanSetting setting) {
        super(button, setting);

        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float posX, float posY) {

        CFontRenderer fontRenderer = Noble.INSTANCE.getFontManager().getFontRenderer("TAHOMA_14");

        this.posX = posX;
        this.posY = posY;

        // draw background of the button
        RenderUtil.drawFilledRect(posX,
                posY,
                ClickPanel.PANEL_WIDTH,
                ClickPanel.PANEL_HEIGHT,
                GuiClick.isHovered(posX, posY, ClickPanel.PANEL_WIDTH, ClickPanel.PANEL_HEIGHT, mouseX, mouseY) ? ColorUtil.color(20, 20, 20, (int) (200 * GuiClick.menuAlphaPercentage)).darker().getRGB() : ColorUtil.color(20, 20, 20, (int) (200 * GuiClick.menuAlphaPercentage)).getRGB());

        // draw text
        fontRenderer.drawStringWithShadow(setting.getName(), posX + 4, posY + 5, -1);

        // draw the checkbox
        RenderUtil.drawFilledGradientRect(this.posX + ClickPanel.PANEL_WIDTH - 10,
                this.posY + 4,
                6,
                ClickPanel.PANEL_HEIGHT - 8,
                this.setting.getValue() ? ColorUtil.color(105, 20, 25, (int) (200 * GuiClick.menuAlphaPercentage)).getRGB() : ColorUtil.color(21, 21, 21, (int) (200 * GuiClick.menuAlphaPercentage)).getRGB(),
                this.setting.getValue() ? ColorUtil.color(105, 20, 25, (int) (200 * GuiClick.menuAlphaPercentage)).darker().getRGB() : ColorUtil.color(21, 21, 21, (int) (200 * GuiClick.menuAlphaPercentage)).darker().getRGB());

        RenderUtil.drawRect(this.posX + ClickPanel.PANEL_WIDTH - 10,
                this.posY + 4,
                6,
                ClickPanel.PANEL_HEIGHT - 8,
                0.5F,
                ColorUtil.color(0, 0, 0, (int) (200 * GuiClick.menuAlphaPercentage)).getRGB());
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException { }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        final boolean b = GuiClick.isHovered(posX, posY, ClickPanel.PANEL_WIDTH, ClickPanel.PANEL_HEIGHT, mouseX, mouseY);
        if (b) {
            if (mouseButton == 0) {
                this.setting.setValue(!this.setting.getValue());
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) { }
}
