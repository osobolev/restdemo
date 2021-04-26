package ru.mirea.my_di;

import ru.mirea.books2.BookRest;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class DIContainer {

    private static final class BeanCell {

        Object bean;
        boolean trying;
    }

    private final Map<Class<?>, BeanCell> beans = new HashMap<>();

    private BeanCell getCell(Class<?> cls) {
        return beans.computeIfAbsent(cls, c -> new BeanCell());
    }

    public <T> void register(Class<T> cls, T obj) {
        BeanCell cell = getCell(cls);
        cell.bean = obj;
    }

    public <T> T getBean(Class<T> cls) {
        BeanCell cell = getCell(cls);
        if (cell.bean == null) {
            if (cell.trying)
                throw new RuntimeException("Circular dependency involving " + cls);
            if (cls.isInterface()) {
                // todo: find implementation in classpath
            }
            Constructor<?>[] constructors = cls.getConstructors();
            if (constructors.length != 1)
                throw new RuntimeException("Must have single constructor: " + cls);
            Constructor<?> constructor = constructors[0];
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] parameters = new Object[parameterTypes.length];
            cell.trying = true;
            try {
                for (int i = 0; i < parameterTypes.length; i++) {
                    parameters[i] = getBean(parameterTypes[i]);
                }
            } finally {
                cell.trying = false;
            }
            try {
                cell.bean = constructor.newInstance(parameters);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
        return cls.cast(cell.bean);
    }

    public static void main(String[] args) {
        DIContainer container = new DIContainer();
        BookRest rest = container.getBean(BookRest.class);
        System.out.println(rest);
    }
}
