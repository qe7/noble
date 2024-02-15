package sh.squeami.kami.managers.impl;

import sh.squeami.kami.Kami;
import sh.squeami.kami.events.api.interfaces.EventSubscribe;
import sh.squeami.kami.events.impl.game.KeyInputEvent;
import sh.squeami.kami.features.api.enums.FeatureCategory;
import sh.squeami.kami.features.api.types.Feature;
import sh.squeami.kami.features.impl.movement.SprintFeature;
import sh.squeami.kami.features.impl.render.ClickGUIFeature;
import sh.squeami.kami.features.impl.render.InterfaceFeature;
import sh.squeami.kami.managers.api.Manager;
import sh.squeami.kami.settings.api.types.Setting;

import java.lang.reflect.Field;
import java.util.List;

public class FeatureManager extends Manager<Feature> {

    private static final Class<? extends Feature>[] FEATURE_CLASSES = new Class[]{
            SprintFeature.class, InterfaceFeature.class, ClickGUIFeature.class
    };

    public void initialize() {
        for (Class<? extends Feature> clazz : FEATURE_CLASSES) {
            try {
                register(clazz);

                if (get(clazz).getFeatureAnnotation().enabled()) {
                    get(clazz).setEnabled(true);
                    Kami.INSTANCE.getEventBus().register(get(clazz));
                }

                for (Field declaredField : clazz.getDeclaredFields()) {
                    if (declaredField.getType().getSuperclass() != null && declaredField.getType().getSuperclass().equals(Setting.class)) {
                        if(!declaredField.trySetAccessible()) {
                            System.out.println("Couldn't get field " + declaredField.getName() + " from feature " + clazz.getSimpleName() + ".");
                        }

                        Kami.INSTANCE.getSettingManager().addSetting(get(clazz), (Setting<?>) declaredField.get(get(clazz)));
                    }
                }

                System.out.println("Registered class " + clazz.getSimpleName() + ".");
                if (!Kami.INSTANCE.getSettingManager().getSettingsByFeature(get(clazz)).isEmpty())
                    System.out.println("Registered " + Kami.INSTANCE.getSettingManager().getSettingsByFeature(get(clazz)).size() + " settings for class " + clazz.getSimpleName() + ".");
            } catch (Exception e) {
                System.out.println("Failed to register class " + clazz.getSimpleName() + ".");
            }
        }
    }

    @EventSubscribe
    public void onKeyInputListener(KeyInputEvent keyInputEvent) {
        for (Feature feature : getMap().values()) {
            if (feature.getKeyCode() == keyInputEvent.keyCode()) {
                feature.setEnabled(!feature.isEnabled());
            }
        }
    }

    public List<Feature> featuresFromCategory(FeatureCategory category) {
        return getMap().values().stream().filter(feature -> feature.getFeatureAnnotation().category() == category).toList();
    }
}
