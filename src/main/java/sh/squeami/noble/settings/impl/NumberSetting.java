package sh.squeami.noble.settings.impl;

import sh.squeami.noble.settings.api.types.Setting;

public class NumberSetting<T extends Number & Comparable<T>> extends Setting<T> {

    private final T minimum;
    private final T maximum;
    private final T increment;

    public NumberSetting(String name, T defaultValue, T minimum, T maximum, T increment) {
        super(name, defaultValue);
        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = increment;
    }

    public T getMinimum() {
        return minimum;
    }

    public T getMaximum() {
        return maximum;
    }

    public T getIncrement() {
        return increment;
    }
}
