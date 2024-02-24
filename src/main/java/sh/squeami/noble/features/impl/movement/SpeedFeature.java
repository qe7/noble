package sh.squeami.noble.features.impl.movement;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import sh.squeami.noble.events.api.interfaces.EventSubscribe;
import sh.squeami.noble.events.impl.player.MotionEvent;
import sh.squeami.noble.features.api.enums.FeatureCategory;
import sh.squeami.noble.features.api.interfaces.FeatureAnnotation;
import sh.squeami.noble.features.api.types.Feature;
import sh.squeami.noble.utils.player.MovementUtil;

@FeatureAnnotation(name = "Speed", description = "Allows you to move faster", category = FeatureCategory.MOVEMENT)
public class SpeedFeature extends Feature {

    @Override
    public void onDisable() {
        super.onDisable();

        final Minecraft minecraft = Minecraft.getMinecraft();

        minecraft.gameSettings.keyBindJump.setPressed(Keyboard.isKeyDown(minecraft.gameSettings.keyBindJump.getKeyCode()));
    }

    @EventSubscribe
    public void onMotionListener(final MotionEvent ignored) {
        final Minecraft minecraft = Minecraft.getMinecraft();

        if (minecraft.thePlayer.moveForward != 0.0f || minecraft.thePlayer.moveStrafing != 0.0f) {
            MovementUtil.setSpeed(0.3D);
            minecraft.gameSettings.keyBindJump.setPressed(minecraft.thePlayer.onGround);
        }
    }
}
