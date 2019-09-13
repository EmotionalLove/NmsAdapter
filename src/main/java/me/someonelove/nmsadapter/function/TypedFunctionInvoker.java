package me.someonelove.nmsadapter.function;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TypedFunctionInvoker<T> {

    private final Object[] parameters;
    private final Method function;
    private final Object instance;

    public TypedFunctionInvoker(Method function, /* @Nullable */ Object instance, Object... parameters) {
        this.instance = instance;
        this.function = function;
        this.parameters = parameters;
    }

    public T invoke() {
        try {
            return (T) function.invoke(instance, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

}
