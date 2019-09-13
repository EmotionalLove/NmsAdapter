package me.someonelove.nmsadapter.field;

import java.lang.reflect.Field;

public class TypedFieldSetter<T> {

    private T setTo;
    private final Field field;
    private final Object instance;

    public TypedFieldSetter(Field field, /* @Nullable */ Object instance, T setTo) {
        this.field = field;
        this.instance = instance;
        this.setTo = setTo;
    }

    public void setSetTo(T setTo) {
        this.setTo = setTo;
    }

    public T getSetTo() {
        return this.setTo;
    }

    /**
     * Returns whether the two types are equal and compatible.
     * @return Whether they're compatible
     *
     * this will not account for super-types and interfaces. This should only be used if you know that
     * what you're planning on putting in is the same exact type as what the field expects.
     */
    public boolean validate() {
        return field.getType() == setTo.getClass();
    }

    /**
     * Set the field
     * @return Whether the operation was successful.
     */
    public boolean execute() {
        try {
            field.set(this.instance, setTo);
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

}
