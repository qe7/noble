package sh.squeami.noble.managers.impl;

import sh.squeami.noble.features.api.types.Feature;
import sh.squeami.noble.settings.api.types.Setting;

import java.util.*;

public class SettingManager {

    private final Map<Feature, List<Setting<?>>> setting = new HashMap<>();

    public Map<Feature, List<Setting<?>>> getSetting() {
        return setting;
    }

    public List<Setting<?>> getSettingsByFeature(Feature feature) {
        return setting.getOrDefault(feature, Collections.emptyList());
    }

    public void addSetting(Feature feature, Setting<?> property) {
        setting.putIfAbsent(feature, new ArrayList<>());
        setting.get(feature).add(property);
    }
}
