package sh.squeami.noble.managers.api;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Manager<T> {

    private final Map<Class<? extends T>, T> classMap = new HashMap<>();

    public void register(Class<? extends T> clazz) {
        try {
            classMap.putIfAbsent(clazz, clazz.getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Failed to register class " + clazz.getSimpleName() + ".");
        }
    }

    public T get(Class<? extends T> clazz) {
        return classMap.get(clazz);
    }

    public Map<Class<? extends T>, T> getMap() {
        return classMap;
    }

    public int getSize() {
        return classMap.size();
    }
}
