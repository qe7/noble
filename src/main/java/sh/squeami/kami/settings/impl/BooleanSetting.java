package sh.squeami.kami.settings.impl;

import sh.squeami.kami.settings.api.types.Setting;

public class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(String name, Boolean defaultValue) {
        super(name, defaultValue);
    }
}
