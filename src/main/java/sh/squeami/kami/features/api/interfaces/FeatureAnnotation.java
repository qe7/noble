package sh.squeami.kami.features.api.interfaces;

import sh.squeami.kami.features.api.enums.FeatureCategory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FeatureAnnotation {

    String name();

    String description() default "No description provided.";

    FeatureCategory category() default FeatureCategory.MISC;

    int keyCode() default 0;

    boolean enabled() default false;

    boolean drawn() default true;
    /**
     * If the feature is hidden, it will not be displayed in the GUI while streaming. (anti HL measures)
     */
    boolean hidden() default false;
}
