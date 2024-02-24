package sh.squeami.noble.managers.impl;

import sh.squeami.noble.Noble;
import sh.squeami.noble.events.api.interfaces.EventSubscribe;
import sh.squeami.noble.events.impl.game.KeyInputEvent;
import sh.squeami.noble.features.api.enums.FeatureCategory;
import sh.squeami.noble.features.api.types.Feature;
import sh.squeami.noble.features.impl.combat.*;
import sh.squeami.noble.features.impl.movement.*;
import sh.squeami.noble.features.impl.render.*;
import sh.squeami.noble.managers.api.Manager;
import sh.squeami.noble.settings.api.types.Setting;

import java.lang.reflect.Field;
import java.util.List;

public class FeatureManager extends Manager<Feature> {

    private static final Class<? extends Feature>[] FEATURE_CLASSES = new Class[]{
            /* Combat */
            KillAuraFeature.class,
            VelocityFeature.class,

            /* Movement */
            SpeedFeature.class,
            SprintFeature.class,

            /* Render */
            BrightnessFeature.class,
            InterfaceFeature.class,
            ClickGUIFeature.class
    };

    public void initialize() {
        for (Class<? extends Feature> clazz : FEATURE_CLASSES) {
            try {
                register(clazz);

                if (get(clazz).getFeatureAnnotation().enabled()) {
                    get(clazz).setEnabled(true);
                    Noble.INSTANCE.getEventBus().register(get(clazz));
                }

                for (Field declaredField : clazz.getDeclaredFields()) {
                    if (declaredField.getType().getSuperclass() != null && declaredField.getType().getSuperclass().equals(Setting.class)) {
                        if(!declaredField.trySetAccessible()) {
                            System.out.println("Couldn't get field " + declaredField.getName() + " from feature " + clazz.getSimpleName() + ".");
                        }

                        Noble.INSTANCE.getSettingManager().addSetting(get(clazz), (Setting<?>) declaredField.get(get(clazz)));
                    }
                }

                System.out.println("Registered class " + clazz.getSimpleName() + ".");
                if (!Noble.INSTANCE.getSettingManager().getSettingsByFeature(get(clazz)).isEmpty())
                    System.out.println("Registered " + Noble.INSTANCE.getSettingManager().getSettingsByFeature(get(clazz)).size() + " settings for class " + clazz.getSimpleName() + ".");
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
