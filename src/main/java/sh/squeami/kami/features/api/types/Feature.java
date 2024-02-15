package sh.squeami.kami.features.api.types;

import sh.squeami.kami.Kami;
import sh.squeami.kami.features.api.interfaces.FeatureAnnotation;

public abstract class Feature {

    protected FeatureAnnotation featureAnnotation;

    private String suffix;

    private int keyCode;

    private boolean enabled, drawn;
    private final boolean hidden;

    public Feature() {
        if (getClass().isAnnotationPresent(FeatureAnnotation.class)) {
            featureAnnotation = getClass().getAnnotation(FeatureAnnotation.class);
            keyCode = featureAnnotation.keyCode();
            enabled = featureAnnotation.enabled();
            drawn = featureAnnotation.drawn();
            hidden = featureAnnotation.hidden();
        } else {
            throw new RuntimeException("Feature class " + getClass().getSimpleName() + " is missing the FeatureAnnotation annotation.");
        }
    }

    /**
     * @return The feature annotation.
     */
    public FeatureAnnotation getFeatureAnnotation() {
        return featureAnnotation;
    }

    /**
     * @return The suffix of the feature.
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * @param suffix The suffix to set.
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * @return The name of the feature.
     */
    public int getKeyCode() {
        return keyCode;
    }

    /**
     * @param keyCode The key code to set.
     */
    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    /**
     * Called when the feature is enabled.
     */
    public void onEnable() { /* Override this if you want to do something when the feature is enabled. */ }

    /**
     * Called when the feature is disabled.
     */
    public void onDisable() { /* Override this if you want to do something when the feature is disabled. */ }

    /**
     * @return Whether the feature is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled Whether the feature is enabled.
     */
    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;

        this.enabled = enabled;

        if (enabled) {
            onEnable();
            Kami.INSTANCE.getEventBus().register(this);
        } else {
            onDisable();
            Kami.INSTANCE.getEventBus().unregister(this);
        }
    }

    /**
     * @return Whether the feature is drawn.
     */
    public boolean isDrawn() {
        return drawn;
    }

    /**
     * @param drawn Whether the feature is drawn.
     */
    public void setDrawn(boolean drawn) {
        this.drawn = drawn;
    }

    /**
     * @return Whether the feature is hidden.
     */
    public boolean isHidden() {
        return hidden;
    }
}
