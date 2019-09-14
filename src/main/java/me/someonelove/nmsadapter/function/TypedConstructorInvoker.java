package me.someonelove.nmsadapter.function;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class TypedConstructorInvoker<T> {

    private final Object[] parameters;
    private final Constructor function;

    public TypedConstructorInvoker(Constructor constructor, Object... parameters) {
        this.function = constructor;
        this.parameters = parameters;
    }

    public T construct() {
        try {
            return (T) function.newInstance(parameters);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }

}
