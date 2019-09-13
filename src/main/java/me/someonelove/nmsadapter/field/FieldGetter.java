package me.someonelove.nmsadapter.field;

import java.lang.reflect.Field;

public class FieldGetter extends TypedFieldGetter<Object> {

    public FieldGetter(Field field, Object instance) {
        super(field, instance);
    }
}
