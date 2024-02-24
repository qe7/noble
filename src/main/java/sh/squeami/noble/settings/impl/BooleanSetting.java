package sh.squeami.noble.settings.impl;

import sh.squeami.noble.settings.api.types.Setting;

public class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(String name, Boolean defaultValue) {
        super(name, defaultValue);
    }
}
