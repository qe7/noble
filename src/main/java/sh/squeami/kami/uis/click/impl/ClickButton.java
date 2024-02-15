package sh.squeami.kami.uis.click.impl;

import sh.squeami.kami.Kami;
import sh.squeami.kami.features.api.types.Feature;
import sh.squeami.kami.fonts.api.CFontRenderer;
import sh.squeami.kami.settings.impl.BooleanSetting;
import sh.squeami.kami.settings.impl.EnumSetting;
import sh.squeami.kami.settings.impl.NumberSetting;
import sh.squeami.kami.uis.click.api.GuiClick;
import sh.squeami.kami.uis.click.api.types.Component;
import sh.squeami.kami.uis.click.impl.component.BooleanComponent;
import sh.squeami.kami.uis.click.impl.component.EnumComponent;
import sh.squeami.kami.uis.click.impl.component.numberComponents.DoubleComponent;
import sh.squeami.kami.utils.render.ColorUtil;
import sh.squeami.kami.utils.render.RenderUtil;

import java.io.IOException;
import java.util.ArrayList;

public class ClickButton {

    private final ArrayList<Component> componentArrayList = new ArrayList<>();

    private final Feature feature;

    private float posX, posY;
    private float totalHeight;

    private boolean open = false;

    public ClickButton(Feature feature) {
        this.feature = feature;

        // populate componentArrayList with components
        Kami.INSTANCE.getSettingManager().getSettingsByFeature(feature).forEach(setting -> {
            // yes this is an if chain, no I don't like it.
            // can I be bothered to change it? no.
            if (setting instanceof NumberSetting<?> numberSetting) {
                if (numberSetting.getValue() instanceof Double) {
                    componentArrayList.add(new DoubleComponent(this, (NumberSetting<Double>) numberSetting));
                }
            }
            if (setting instanceof EnumSetting<?> enumSetting) {
                componentArrayList.add(new EnumComponent(this, enumSetting));
            }
            if (setting instanceof BooleanSetting booleanSetting) {
                componentArrayList.add(new BooleanComponent(this, booleanSetting));
            }
        });
    }

    public void drawScreen(int mouseX, int mouseY, float posX, float posY) {

        CFontRenderer fontRenderer = Kami.INSTANCE.getFontManager().getFontRenderer("TAHOMA_14");

        this.posX = posX;
        this.posY = posY;
        this.totalHeight = ClickPanel.PANEL_HEIGHT;

        // draw background of the button
        RenderUtil.drawFilledRect(posX,
                posY,
                ClickPanel.PANEL_WIDTH,
                ClickPanel.PANEL_HEIGHT,
                GuiClick.isHovered(posX, posY, ClickPanel.PANEL_WIDTH, ClickPanel.PANEL_HEIGHT, mouseX, mouseY) ? ColorUtil.color(20, 20, 20, (int) (200 * GuiClick.menuAlphaPercentage)).darker().getRGB() : ColorUtil.color(20, 20, 20, (int) (200 * GuiClick.menuAlphaPercentage)).getRGB());

        // draw text
        fontRenderer.drawStringWithShadow(feature.getFeatureAnnotation().name(),
                posX + 4,
                posY + 5,
                feature.isEnabled() ? -1 : ColorUtil.color(175, 175, 175, 255).getRGB());

        // return if feature has no settings
        // no point in doing anything else
        if (Kami.INSTANCE.getSettingManager().getSettingsByFeature(feature).isEmpty()) return;

        // display if feature has settings... and if feature is open
        fontRenderer.drawStringWithShadow("...",
                posX + ClickPanel.PANEL_WIDTH - fontRenderer.getStringWidth("...") - 4,
                posY + 5,
                open ? -1 : ColorUtil.color(175, 175, 175, 255).getRGB());

        // handle components
        if (!open) return;
        for (Component component : componentArrayList) {
            if (component.getSetting().shouldHide()) continue;
            component.drawScreen(mouseX, mouseY, posX, posY + totalHeight);
            totalHeight += component.returnHeight();
        }
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {

        if (!open) return;
        for (Component component : componentArrayList) {
            component.keyTyped(typedChar, keyCode);
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        final boolean b = GuiClick.isHovered(posX, posY, ClickPanel.PANEL_WIDTH, ClickPanel.PANEL_HEIGHT, mouseX, mouseY);
        if (b) {
            switch (mouseButton) {
                case 0 -> feature.setEnabled(!feature.isEnabled());
                case 1 -> open = !open;
            }
        }

        if (!open) return;
        for (Component component : componentArrayList) {
            component.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {

        if (!open) return;
        for (Component component : componentArrayList) {
            component.mouseReleased(mouseX, mouseY, state);
        }
    }

    public float returnHeight() {
        return totalHeight;
    }

    public Feature getFeature() {
        return feature;
    }
}
