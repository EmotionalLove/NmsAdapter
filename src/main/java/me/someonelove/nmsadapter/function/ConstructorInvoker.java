package me.someonelove.nmsadapter.function;

import java.lang.reflect.Constructor;

public class ConstructorInvoker extends TypedConstructorInvoker<Object> {

    public ConstructorInvoker(Constructor constructor, Object... parameters) {
        super(constructor, parameters);
    }
}
