package sh.squeami.noble.components.impl;

import sh.squeami.noble.events.api.interfaces.EventSubscribe;
import sh.squeami.noble.events.impl.player.MotionEvent;

public class RotationComponent {

    private float[] rotation;

    private boolean rotating;

    @EventSubscribe
    public void onRender2DListener(final MotionEvent motionEvent) {
        if (rotating) {
            motionEvent.setRotationYaw(rotation[0]);
            motionEvent.setRotationPitch(rotation[1]);
            this.rotation = null;
            this.rotating = false;
        }
    }

    public void setRotation(float[] rotation) {
        this.rotation = rotation;
        this.rotating = true;
    }
}
