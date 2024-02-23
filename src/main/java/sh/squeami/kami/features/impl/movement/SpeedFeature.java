package sh.squeami.kami.features.impl.movement;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import sh.squeami.kami.events.api.interfaces.EventSubscribe;
import sh.squeami.kami.events.impl.player.MotionEvent;
import sh.squeami.kami.features.api.enums.FeatureCategory;
import sh.squeami.kami.features.api.interfaces.FeatureAnnotation;
import sh.squeami.kami.features.api.types.Feature;
import sh.squeami.kami.utils.player.MovementUtil;

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
