package me.someonelove.nmsadapter;

import me.someonelove.nmsadapter.field.FieldGetter;
import me.someonelove.nmsadapter.field.FieldSetter;
import me.someonelove.nmsadapter.function.ConstructorInvoker;
import me.someonelove.nmsadapter.function.FunctionInvoker;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MinecraftClass {

    public Class<?> minecraftClass;
    private Object instance = null;

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
            Constructor func = minecraftClass.getConstructor(types);
            func.setAccessible(true);
            ConstructorInvoker invoker = new ConstructorInvoker(func, parameters);
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
        try {
            Field field = minecraftClass.getField(fieldName);
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
}
