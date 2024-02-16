package sh.squeami.kami.uis.click.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import sh.squeami.kami.features.api.enums.FeatureCategory;
import sh.squeami.kami.uis.click.impl.ClickPanel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiClick extends GuiScreen {

    private final ArrayList<ClickPanel> panelArrayList = new ArrayList<>();

    // amazing lerp!!
    public static float menuOpacity;
    public static float menuAlphaPercentage;

    // ColorUtil.color(20, 20, 20, (int) (255 * menuAlphaPercentage)).getRGB()
    // ColorUtil.color(5, 5, 5, (int) (255 * menuAlphaPercentage)).getRGB()
    // ColorUtil.color(60, 60, 60, (int) (255 * menuAlphaPercentage)).getRGB()

    public GuiClick() {
        AtomicInteger x = new AtomicInteger(15);
        Arrays.stream(FeatureCategory.values()).toList().forEach(category -> {
            panelArrayList.add(new ClickPanel(category, x.get(), 15));
            x.addAndGet((int) (ClickPanel.PANEL_WIDTH + 3));
        });
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        float frequency = 1.f / 0.5F;
        float step = frequency * 2.f;
        if (!(menuOpacity >= 255)) menuOpacity += step;
        menuAlphaPercentage = Math.clamp(menuOpacity, 0, 255) / 255;
        if (menuAlphaPercentage == 0) return;

        for (ClickPanel panel : panelArrayList) {
            panel.drawScreen(mouseX, mouseY);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        if (keyCode == Keyboard.KEY_ESCAPE) {
            menuOpacity = 0;
            Minecraft.getMinecraft().displayGuiScreen(null);
        }

        for (ClickPanel panel : panelArrayList) {
            panel.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        for (ClickPanel panel : panelArrayList) {
            panel.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);

        for (ClickPanel panel : panelArrayList) {
            panel.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public static boolean isHovered(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
