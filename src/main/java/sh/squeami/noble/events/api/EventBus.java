package sh.squeami.noble.events.api;

import sh.squeami.noble.events.api.interfaces.EventSubscribe;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages events and their subscriptions.
 */
public final class EventBus {

    private final List<Object> eventObjects = new CopyOnWriteArrayList<>();
    private final List<Method> eventMethods = new CopyOnWriteArrayList<>();
    private final Map<Method, Object> methodObjectMap = new ConcurrentHashMap<>();

    /**
     * Registers an object for event handling.
     *
     * @param object The object to register.
     */
    public void register(Object object) {
        eventObjects.add(object);
        updateMethods();
    }

    /**
     * Unregisters an object from event handling.
     *
     * @param object The object to unregister.
     */
    public void unregister(Object object) {
        if (!eventObjects.contains(object)) {
            return;
        }

        eventObjects.remove(object);
        updateMethods();
    }

    /**
     * Posts an event to all subscribed methods.
     *
     * @param event The event to post.
     */
    public void post(Object event) {
        eventMethods.stream()
                .filter(method -> method.getParameterTypes()[0] == event.getClass())
                .forEach(method -> invokeMethodSafely(method, event));
    }

    /**
     * Updates the list of subscribed methods based on registered objects.
     */
    private void updateMethods() {
        eventMethods.clear();
        eventObjects.stream()
                .map(Object::getClass)
                .map(Class::getDeclaredMethods)
                .map(Arrays::asList)
                .flatMap(List::stream)
                .filter(this::isValidEventMethod)
                .forEach(this::methodSetup);

        eventMethods.sort(Comparator.comparingInt(value -> value.getDeclaredAnnotation(EventSubscribe.class).priority().ordinal()));
    }

    /**
     * Checks if a method is a valid event method.
     *
     * @param method The method to check.
     * @return True if the method is a valid event method, otherwise false.
     */
    private boolean isValidEventMethod(Method method) {
        return method.isAnnotationPresent(EventSubscribe.class) && method.getParameterTypes().length == 1;
    }

    /**
     * Sets up a method for event handling, adding it to the list and mapping it to the corresponding object.
     *
     * @param method The method to set up.
     */
    private void methodSetup(Method method) {
        eventMethods.add(method);
        methodObjectMap.put(method, eventObjects.stream()
                .filter(o -> method.getDeclaringClass().equals(o.getClass()))
                .findFirst()
                .orElseThrow(RuntimeException::new));
    }

    /**
     * Invokes a method safely, handling exceptions.
     *
     * @param method The method to invoke.
     * @param event  The event to pass to the method.
     */
    private void invokeMethodSafely(Method method, Object event) {
        try {
            method.invoke(methodObjectMap.get(method), event);
        } catch (Exception e) {
            System.out.println("Failed to invoke method " + method.getName() + ".");
        }
    }
}
