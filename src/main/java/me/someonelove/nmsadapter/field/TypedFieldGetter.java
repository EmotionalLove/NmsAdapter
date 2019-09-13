package me.someonelove.nmsadapter.field;

import java.lang.reflect.Field;

/**
 * Everything in this package is kinda dumb and redundant but y'know ocd exists.
 */
public class TypedFieldGetter<T> {

    private final Field field;
    private final Object instance;

    public TypedFieldGetter(Field field, /* @Nullable */ Object instance) {
        this.field = field;
        this.instance = instance;
    }
    /**
     * Set the field
     * @return Whether the operation was successful.
     */
    public T execute() {
        try {
            return (T) field.get(this.instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

}
