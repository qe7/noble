package sh.squeami.kami.components.impl;

import org.lwjgl.util.vector.Vector2f;
import sh.squeami.kami.events.api.interfaces.EventSubscribe;
import sh.squeami.kami.events.impl.player.MotionEvent;

public class RotationComponent {

    private Vector2f rotation;

    private boolean rotating;

    @EventSubscribe
    public void onRender2DListener(final MotionEvent motionEvent) {
        if (rotating) {
            motionEvent.setRotationYaw(rotation.x);
            motionEvent.setRotationPitch(rotation.y);
            this.rotation = null;
            this.rotating = false;
        }
    }

    public void setRotation(Vector2f rotation) {
        this.rotation = rotation;
        this.rotating = true;
    }
}
