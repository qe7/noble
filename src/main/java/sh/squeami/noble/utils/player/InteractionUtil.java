package sh.squeami.noble.utils.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public class InteractionUtil {

    public static void sendClick(int button, boolean state) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft == null || minecraft.gameSettings == null) {
            return;
        }

        int keyBind = button == 0 ? minecraft.gameSettings.keyBindAttack.getKeyCode() : minecraft.gameSettings.keyBindUseItem.getKeyCode();

        try {
            KeyBinding.setKeyBindState(keyBind, state);

            if (state) {
                KeyBinding.onTick(keyBind);
            }
        } catch (Exception e) {
            System.err.println("Failed to send click: " + e.getMessage());
        }
    }
}
