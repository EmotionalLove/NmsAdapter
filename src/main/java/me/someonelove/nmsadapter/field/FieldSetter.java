package me.someonelove.nmsadapter.field;

import java.lang.reflect.Field;

public class FieldSetter extends TypedFieldSetter<Object> {

    public FieldSetter(Field field, /* @Nullable */ Object instance, Object setTo) {
        super(field, instance, setTo);
    }

    @Override
    public boolean validate() {
        return true;
    }
}
