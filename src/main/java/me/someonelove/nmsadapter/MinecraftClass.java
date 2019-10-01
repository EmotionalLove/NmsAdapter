package me.someonelove.nmsadapter;

import me.someonelove.nmsadapter.field.FieldGetter;
import me.someonelove.nmsadapter.field.FieldSetter;
import me.someonelove.nmsadapter.function.ConstructorInvoker;
import me.someonelove.nmsadapter.function.FunctionInvoker;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MinecraftClass {

    public Class<?> minecraftClass;
    private Object instance = null;

    //                         types, method
    private Map<String, Pair<Class<?>[], Method>> functionCache = new HashMap<>();
    private Map<String, Field> fieldCache = new HashMap<>();

    protected MinecraftClass(Class<?> minecraftClass, Object instance) {
        this.minecraftClass = minecraftClass;
        this.instance = instance;
    }

    protected MinecraftClass(Class<?> minecraftClass) {
        this.minecraftClass = minecraftClass;
    }

    public Object newInstance(Object... parameters) {
        Class<?>[] types = new Class<?>[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            types[i] = parameters[i].getClass();
        }
        try {
            Constructor constructor = minecraftClass.getConstructor(types);
            if (!constructor.isAccessible()) {
                System.out.println("NmsAdapter warning - Accessing inaccessible constructor in " + minecraftClass.getSimpleName() + ". It was probably inaccessible for a reason. Bad evil may happen.");
            }
            constructor.setAccessible(true);
            ConstructorInvoker invoker = new ConstructorInvoker(constructor, parameters);
            return invoker.construct();
        } catch (NoSuchMethodException x) {
            x.printStackTrace();
            return null;
        }
    }

    /**
     * Invoke the given function
     *
     * @param functionName The name of the function
     * @param parameters   The parameters to pass to the function
     * @return null if the function is void, or if an exception occurred, or an object if the function returned something.
     */
    public Object invokeFunction(String functionName, Object... parameters) {
        Class<?>[] types = new Class<?>[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            types[i] = parameters[i].getClass();
        }

        try {
            Method func;
            if (functionCache.containsKey(functionName)) {
                if (functionCache.get(functionName).a == types) {
                    func = functionCache.get(functionName).b;
                } else {
                    func = minecraftClass.getMethod(functionName, types);
                    functionCache.put(functionName, new Pair<>(types, func));
                }
            } else {
                func = minecraftClass.getMethod(functionName, types);
                functionCache.put(functionName, new Pair<>(types, func));
            }
            func.setAccessible(true);
            FunctionInvoker invoker = new FunctionInvoker(func, instance, parameters);
            return invoker.invoke();
        } catch (NoSuchMethodException x) {
            x.printStackTrace();
            return null;
        }
    }

    /**
     * Invoke the given function with explicitly stated types because Java just sucks sometimes. Use this if you get wonky errors.
     *
     * @param functionName The name of the function
     * @param types        The explicit types of the params
     * @param parameters   The parameters to pass to the function
     * @return null if the function is void, or if an exception occurred, or an object if the function returned something.
     */
    public Object invokeFunctionExplicit(String functionName, Class<?>[] types, Object... parameters) {
        try {
            Method func = minecraftClass.getMethod(functionName, types);
            func.setAccessible(true);
            FunctionInvoker invoker = new FunctionInvoker(func, instance, parameters);
            return invoker.invoke();
        } catch (NoSuchMethodException x) {
            x.printStackTrace();
            return null;
        }
    }

    /**
     * Invoke the given function
     *
     * @param matcher    A VersionMatcher which determines the name of the function
     * @param version    The current major version of Minecraft running at RUNTIME. (14 if 1.14.4, 13 if 1.13.2, 12 if 1.12.2, etc)
     * @param parameters the paramters to pass to the function.
     * @return null if the function is void, or if an exception occurred, or an object if the function returned something.
     */
    public Object invokeFunction(VersionMatcher matcher, int version, Object... parameters) {
        return this.invokeFunction(matcher.match(version), parameters);
    }

    /**
     * Invoke the given function
     *
     * @param matcher    A VersionMatcher which determines the name of the function
     * @param adapter    A valid and non-null instance of an `NmsAdapter`
     * @param parameters the paramters to pass to the function.
     * @return null if the function is void, or if an exception occurred, or an object if the function returned something.
     */
    public Object invokeFunction(VersionMatcher matcher, NmsAdapter adapter, Object... parameters) {
        return this.invokeFunction(matcher, adapter.getMajorVersion(), parameters);
    }

    /**
     * Set the given field
     *
     * @param fieldName The name of the field
     * @param newValue  The value to set the given field to
     * @return true if the operation was completed successfully.
     */
    public boolean setField(String fieldName, Object newValue) {
        Field field;
        try {
            if (fieldCache.containsKey(fieldName)) {
                field = fieldCache.get(fieldName);
            } else {
                field = minecraftClass.getField(fieldName);
                fieldCache.put(fieldName, field);
            }
            field.setAccessible(true);
            FieldSetter setter = new FieldSetter(field, instance, newValue);
            return setter.execute();
        } catch (NoSuchFieldException x) {
            x.printStackTrace();
            return false;
        }
    }

    /**
     * Set the given field
     *
     * @param matcher  A VersionMatcher that decides the name of the field
     * @param version  The current major version of Minecraft running at RUNTIME. (14 if 1.14.4, 13 if 1.13.2, 12 if 1.12.2, etc)
     * @param newValue The value to set the given field to
     * @return true if the operation was completed successfully.
     */
    public boolean setField(VersionMatcher matcher, int version, Object newValue) {
        return this.setField(matcher.match(version), newValue);
    }

    /**
     * Set the given field
     *
     * @param matcher  A VersionMatcher that decides the name of the field
     * @param adapter  A valid and non-null instance of an `NmsAdapter`
     * @param newValue The value to set the given field to
     * @return true if the operation was completed successfully.
     */
    public boolean setField(VersionMatcher matcher, NmsAdapter adapter, Object newValue) {
        return this.setField(matcher, adapter.getMajorVersion(), newValue);
    }

    /**
     * omg i think you can guess what these last three do based on what i've written above >.< ugh
     */
    public Object getField(String fieldName) {
        try {
            Field field = minecraftClass.getField(fieldName);
            field.setAccessible(true);
            FieldGetter getter = new FieldGetter(field, instance);
            return getter.execute();
        } catch (NoSuchFieldException x) {
            x.printStackTrace();
            return false;
        }
    }

    public Object getField(VersionMatcher matcher, int version) {
        return this.getField(matcher.match(version));
    }

    public Object getField(VersionMatcher matcher, NmsAdapter adapter) {
        return this.getField(matcher, adapter.getMajorVersion());
    }

    /**
     * Set the instance of the class to make calls to
     *
     * @param object The instance :3
     */
    public void setInstance(/* @Nullable */ Object object) {
        this.instance = object;
    }
}
