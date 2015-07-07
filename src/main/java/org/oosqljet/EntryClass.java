package org.oosqljet;

import org.oosqljet.annotation.Entry;

import java.lang.reflect.Field;

public class EntryClass {

    private Field field;
    private String name;

    public EntryClass(Field field) {
        this.field = field;
        if (getAnnotation().name()[0].isEmpty()) name = field.getName();
        else name = null;
    }

    public Entry getAnnotation() {
        return field.getAnnotation(Entry.class);
    }

    public String[] getName() {
        return name == null ? getAnnotation().name() : new String[]{name};
    }

    public Class getType() {
        return field.getType();
    }

    public Object get(Object o) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
                Object out = field.get(o);
                field.setAccessible(false);
                return out;
            } else {
                return field.get(o);
            }
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public void set(Object o,Object in) throws IllegalAccessException {
        if (!field.isAccessible()) {
            field.setAccessible(true);
            field.set(o, in);
            field.setAccessible(false);
        } else {
            field.set(o, in);
        }
    }

}
