package sh.squeami.noble.features.impl.render;

import net.minecraft.client.Minecraft;
import sh.squeami.noble.features.api.enums.FeatureCategory;
import sh.squeami.noble.features.api.interfaces.FeatureAnnotation;
import sh.squeami.noble.features.api.types.Feature;
import org.lwjgl.input.Keyboard;
import sh.squeami.noble.uis.click.api.GuiClick;

@FeatureAnnotation(name = "ClickGUI", description = "Opens the ClickGUI", category = FeatureCategory.RENDER, keyCode = Keyboard.KEY_RSHIFT)
public class ClickGUIFeature extends Feature {

    private GuiClick guiClick;

    @Override
    public void onEnable() {
        if (this.guiClick == null)
            this.guiClick = new GuiClick();

        GuiClick.menuOpacity = 0;
        Minecraft.getMinecraft().displayGuiScreen(guiClick);
        this.setEnabled(false);
    }
}
