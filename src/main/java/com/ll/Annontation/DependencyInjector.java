package com.ll.Annontation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class DependencyInjector {
    public static void injectDependencies(Object target) throws IllegalAccessException {
        Class<?> targetClass = target.getClass();
        Field[] fields = targetClass.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Component.class)) {
                Class<?> fieldType = field.getType();
                Object dependency = createDependencyInstance(fieldType);
                field.setAccessible(true);
                field.set(target, dependency);
            }
        }
    }

    private static Object createDependencyInstance(Class<?> dependencyClass) throws IllegalAccessException {
        try {
            return dependencyClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Failed to instantiate dependency: " + dependencyClass.getName(), e);
        } catch (InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
