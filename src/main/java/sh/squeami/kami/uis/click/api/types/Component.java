package sh.squeami.kami.uis.click.api.types;

import sh.squeami.kami.settings.api.types.Setting;
import sh.squeami.kami.uis.click.impl.ClickButton;

import java.io.IOException;

/*
 * Abstract class for click ui components
 */
public abstract class Component {

    protected ClickButton button; // the button this component is attached to

    private final Setting<?> setting; // the type of setting this component is for

    public Component(ClickButton button, Setting<?> setting) {
        this.button = button;
        this.setting = setting;
    }

    public abstract void drawScreen(int mouseX, int mouseY, float posX, float posY);

    public abstract void keyTyped(char typedChar, int keyCode) throws IOException;

    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;

    public abstract void mouseReleased(int mouseX, int mouseY, int state);

    public float returnHeight() {
        return 14.0F;
    }

    public Setting<?> getSetting() {
        return setting;
    }
}
