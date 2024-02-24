package sh.squeami.noble.events.api.interfaces;

import sh.squeami.noble.events.api.enums.EventPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a method as a subscriber for events.
 * The annotated method will be called when an event is dispatched.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventSubscribe {

    /**
     * Specifies the priority of the annotated method when multiple subscribers are present.
     *
     * @return The priority of the event subscriber method.
     */
    EventPriority priority() default EventPriority.NORMAL;
}
