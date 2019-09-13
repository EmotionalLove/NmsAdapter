package me.someonelove.nmsadapter.function;

import java.lang.reflect.Method;

public class FunctionInvoker extends TypedFunctionInvoker<Object> {

    public FunctionInvoker(Method function, /* @Nullable */ Object instance, Object... parameters) {
        super(function, instance, parameters);
    }
}
