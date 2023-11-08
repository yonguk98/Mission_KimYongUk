package com.ll.Annontation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class DependencyInjector {
    public static void injectDependencies(Object target) throws Exception {
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

    private static Object createDependencyInstance(Class<?> dependencyClass) throws Exception {
        Constructor<?> constructor = dependencyClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }
}
