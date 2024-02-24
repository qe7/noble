package sh.squeami.noble.settings.impl;

import sh.squeami.noble.settings.api.types.Setting;
import sh.squeami.noble.settings.impl.interfaces.IEnumSetting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnumSetting<T extends IEnumSetting> extends Setting<T> {

    private final List<IEnumSetting> values;

    public EnumSetting(String name, T defaultValue) {
        super(name, defaultValue);
        this.values = new ArrayList<>(Arrays.asList(defaultValue.getClass().getEnumConstants()));
    }

    public List<IEnumSetting> getValues() {
        return values;
    }

    public int getIndex() {
        return values.indexOf(getValue());
    }

    public void setIndex(int index) {
        if (index < values.size()) {
            setValue((T) values.get(index));
        }
    }
}
