package sh.squeami.noble.uis.click.impl.component;

import sh.squeami.noble.Noble;
import sh.squeami.noble.fonts.api.CFontRenderer;
import sh.squeami.noble.settings.impl.EnumSetting;
import sh.squeami.noble.uis.click.api.GuiClick;
import sh.squeami.noble.uis.click.api.types.Component;
import sh.squeami.noble.uis.click.impl.ClickButton;
import sh.squeami.noble.uis.click.impl.ClickPanel;
import sh.squeami.noble.utils.render.ColorUtil;
import sh.squeami.noble.utils.render.RenderUtil;

import java.io.IOException;

public class EnumComponent extends Component {

    private final EnumSetting<?> setting;

    private float posX, posY;

    public EnumComponent(ClickButton button, EnumSetting<?> setting) {
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
        fontRenderer.drawStringWithShadow(setting.getValue().getName(), posX + ClickPanel.PANEL_WIDTH - fontRenderer.getStringWidth(setting.getValue().getName()) - 4, posY + 5, -1);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException { }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        final boolean b = GuiClick.isHovered(posX, posY, ClickPanel.PANEL_WIDTH, ClickPanel.PANEL_HEIGHT, mouseX, mouseY);
        if (b) {
            switch (mouseButton) {
                case 0 -> {
                    handleIncrement();
                }
                case 1 -> {
                    handleDecrement();
                }
            }
        }
    }

    private void handleIncrement() {
        final int index = setting.getIndex();
        if (index < setting.getValues().size() - 1) {
            setting.setIndex(index + 1);
        } else {
            setting.setIndex(0);
        }
    }

    private void handleDecrement() {
        final int index = setting.getIndex();
        if (index > 0) {
            setting.setIndex(index - 1);
        } else {
            setting.setIndex(setting.getValues().size() - 1);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) { }
}
